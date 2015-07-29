
$(document).ready(function() {

    /** Load parsed applications **/
    var activePage = 1;
    var $pagination = $("#app-pagination");
    var $searchResults = $("#search-results");
    var $appLoading = $("#app-loading");
    $pagination.on('click', 'li', function() {

        if ($(this).hasClass("page-number")) {

            loadApplications($(this).data("value"));
        } else if ($(this).hasClass("empty-page")) {

            loadApplications(activePage + 1);
        } else if ($(this).hasClass("btn-navigation")) {

            var index = activePage + Number($(this).data("nav"));
            if (index >= 1) {
                loadApplications(index);
            }
        }
    });

    function loadApplications(pageNumber) {

        $appLoading.fadeIn();

        var url = $pagination.data("url");
        $.get(url, { p: Number(pageNumber) - 1})
            .done(function(data) {

                $appLoading.fadeOut();

                if (data.length == 0) {

                    $pagination.find(".empty-page").remove();

                } else {
                    $searchResults.html("");
                    activePage = pageNumber;

                    $pagination.find(".page-number.active").removeClass("active");

                    if ($pagination.find(".page-number[data-value='" + pageNumber + "']").size() == 0) {

                        $pagination.find(".empty-page")
                            .before('<li class="page-number" data-value="' + pageNumber + '"><a href="#">' + pageNumber + '</a></li>');
                    }

                    $pagination.find(".page-number[data-value='" + pageNumber + "']").addClass("active");

                    $.each(data, function(i, item) {
                        $searchResults.append(item);
                    });
                }
            })
            .fail(function() {
                $appLoading.fadeOut();

                $searchResults.html("Error during loading");
            })
    }

    // Load the first page
    loadApplications(1);

    /** Load reviews **/
    var $reviewsContainer = $("#reviews-container");
    $searchResults.on('click', 'li', function() {

        // Active state management
        $searchResults.children().removeClass("active");
        $(this).addClass("active");

        var url = $(this).data("url");
        $reviewsContainer.html("Loading...");

        $.get(url, { admin: true })
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
        $(this).closest(".review").find(".clause").removeClass("clause-" + opposite);

        if ($(this).hasClass("clause-" + clauseSide)) {
            $(this).removeClass("clause-" + clauseSide);
        } else {
            $(this).addClass("clause-" + clauseSide);
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
        var $clauses = $body.find(".clause-positive, .clause-negative");
        if ($clauses.size() <= 0) {
            console.log("Proposition without clauses !");
            return;
        }

        var side = $clauses.is(".clause-positive") ? 'positive' : 'negative';
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

        // Show the loading, disable the button
        $(this).siblings("img").show();
        $(this).prop("disabled", true);

        var url = $sideIndicator.data("url");
        var $button = $(this);
        var $successIndicator = $("#result-indicator");
        $.post(url, { json: JSON.stringify({
            appID: $list.find("li.active").data("id"),
            permalink: $(this).data("permalink"),
            side: side,
            type: type,
            text: text,
            components: components,
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
            $clauses.removeClass("clause-positive").removeClass("clause-negative");
        });
    });
});