
$(document).ready(function() {

    /** Load parsed applications **/
    var $pagination = $("#app-pagination");
    var offset = 1;
    var $searchResults = $("#search-results");
    var $appLoading = $("#app-loading");
    $pagination.data("page", 1);
    $pagination.on('click', 'a', function() {

        var page = 1;
        if ($(this).parent().is(".previous")) {

            page = Number($pagination.data("page")) - 1;

        } else if ($(this).parent().is(".next")) {

            page = Number($pagination.data("page")) + 1;
        } else {

            page = $(this).data("page");
        }

        $pagination.data("page", page);
        loadApplications(page);
    });

    function loadApplications(pageNumber) {

        $appLoading.fadeIn();

        var url = $pagination.data("url");
        $.get(url, { p: Number(pageNumber) - 1})
            .done(function(data) {

                $appLoading.fadeOut();

                $searchResults.empty();
                $.each(data.applications, function(i, item) {
                    $searchResults.append(item);
                });

                reloadPager(pageNumber, data.nbPage);
            })
            .fail(function() {
                $appLoading.fadeOut();

                $searchResults.html("Error during loading");
            })
    }

    // Load the first page
    loadApplications($pagination.data("page"));

    /** Load reviews **/
    var $reviewsContainer = $("#reviews-container");
    $searchResults.on('click', 'li', function() {

        // Active state management
        $searchResults.children().removeClass("active");
        $(this).addClass("active");

        var url = $("#reviews-container").data("url");
        $reviewsContainer.html("Loading...");

        $.get(url, { id: $(this).data("id"), p: 0 })
            .done(function(data) {
                $reviewsContainer.html("");

                $.each(data, function(i, item) {
                    if (i % 2 == 0) {
                        $reviewsContainer.append('<div class="clearfix"></div>');
                    }

                    $reviewsContainer.append(item);
                });
            }).fail(function() {
                $reviewsContainer.html("Error, please retry");
            });
    });

    /** Choose clauses **/
    var clauseSide = "positive";
    $reviewsContainer.on('click', '.clause', function() {
        var opposite = clauseSide == 'positive' ? 'negative' : 'positive';
        $(this).closest(".review").find(".clause").removeClass("clause-propose-" + opposite);

        if ($(this).hasClass("clause-propose-" + clauseSide)) {
            $(this).removeClass("clause-propose-" + clauseSide);
        } else {
            $(this).addClass("clause-propose-" + clauseSide);
        }
    });

    /** Change side selection **/
    var $sideIndicator = $("#side-indicator");
    $(document).keydown(function(event) {
        if (event.which == 87) { // If press 'W'
            changeSideIndicator();
        } else if (event.which == 13) {
            $("#search-button").click();
        }
    });
    $sideIndicator.click(function() {
        changeSideIndicator();
    });

    function changeSideIndicator() {
        if (clauseSide == 'positive') {
            clauseSide = 'negative';
            $sideIndicator.css("background", "#cc6b6b");
        } else {
            clauseSide = 'positive';
            $sideIndicator.css("background", "#57b05e");
        }
    }

    /** Propose argument **/
    $reviewsContainer.on('click', '.btn-send', function() {

        // Get information
        var $body = $(this).closest(".review").find(".body");
        $body = $body.add($(this).closest(".review").find(".title"));
        var $clauses = $body.find(".clause-propose-positive, .clause-propose-negative");
        if ($clauses.size() <= 0) {
            console.log("Proposition without clauses !");
            return;
        }

        var side = $clauses.is(".clause-propose-positive") ? 'positive' : 'negative';
        var type = Number($(this).siblings("form").find("input[type='radio']").filter(":checked").val());

        var text = '';
        var components = [];
        $clauses.each(function() {
            text += $(this).html();
            components.push({
                sentenceID: $(this).data("sentence"),
                clauseID: $(this).data("clause"),
                inTitle: $(this).data("in-title")
            });
            console.log($(this).data("in-title"));
        });

        var opinions = [];
        if (type == 3) {
            // Put the side of the opinions if we choose "Not an argument"
            side = $clauses.is(".clause-positive") ? 'positive' : 'negative';

            var regexp = /<strong>(.+?)<\/strong>/g;
            var match = regexp.exec(text);
            while (match != null) {
                opinions.push(match[1]);

                match = regexp.exec(text);
            }
        }
        text = text.replace(/<strong>(.+?)<\/strong>/g, "$1");

        // Show the loading, disable the button
        $(this).siblings("img").show();
        $(this).prop("disabled", true);

        var url = $sideIndicator.data("url");
        var $button = $(this);
        var $successIndicator = $("#result-indicator");
        $.post(url, { json: JSON.stringify({
            appID: $searchResults.find("li.active").data("id"),
            permalink: $(this).data("permalink"),
            side: side,
            type: type,
            text: text,
            components: components,
            opinions: opinions,
            createdAt: {
                $date: $.now()
            },
            reviewID: {
                $oid: $(this).data("id")
            }
        })}).done(function() {

            $successIndicator.html("Success!");
            $successIndicator.fadeIn();
            setTimeout(function() {
                $successIndicator.fadeOut();
            }, 2000);
        }).fail(function() {

            $successIndicator.html("An error occurred.");
            $successIndicator.fadeIn();
            setTimeout(function() {
                $successIndicator.fadeOut();
            }, 2000);
        }).always(function() {

            $button.siblings("img").hide();
            $button.prop("disabled", false);
            $clauses.removeClass("clause-propose-positive").removeClass("clause-propose-negative");
        });
    });

    function reloadPager(page, maxPage) {

        $pagination.empty();

        if (page > 1) {
            $pagination.append('<li class="previous"><a href="#">Previous</a></li>');
        }

        if (page - offset > 1) {
            appendPage(false, 1);
            $pagination.append('<li class="separator">&bull;&bull;&bull;</li>');
        }

        var min = Math.max(page - offset, 1);
        var max = Math.min(page + offset, maxPage);
        for (var i = min; i <= max; i++) {
            appendPage(i == page, i);
        }

        if (page + offset < maxPage) {
            $pagination.append('<li class="separator">&bull;&bull;&bull;</li>');
            appendPage(false, maxPage);
        }

        if (page < maxPage) {
            $pagination.append('<li class="next"><a href="#">Next</a></li>');
        }
    }

    function appendPage(isActive, page) {
        $pagination.append('<li class="' + (isActive ? "active" : "") + '"><a data-page="' + page + '" href="#">' + page + '</a></li>');
    }
});