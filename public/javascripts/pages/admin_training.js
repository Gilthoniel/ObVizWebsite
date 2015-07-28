
$(document).ready(function() {
    /** Search apps **/
    var $list = $("#search-results");
    $("#search-button").click(function() {

        var url = $(this).data("url");
        var query = $(this).closest(".input-group").find("input[type='text']").val();
        $list.html("<li>Loading...</li>");

        $.get(url, { name: query, admin: 'true' })
            .done(function(data) {

                $list.html("");

                $.each(data, function(i, item) {
                    $list.append(item);
                })
            }).fail(function() {

                $list.html("<li>Error, please retry</li>");
            });
    });

    /** Load reviews **/
    var $reviewsContainer = $("#reviews-container");
    $list.on('click', 'li', function() {

        // Active state management
        $list.children().removeClass("active");
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