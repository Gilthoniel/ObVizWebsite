/**
 * Created by gaylor on 03.07.15.
 * Javascript of the page where you visualize the information about one app
 */

/**
 *
 * Elements
 */
OBVIZ.$headerCarousel = $("#header-carousel");
OBVIZ.$gauges = $(".chart-gauge");
OBVIZ.$reviews = $("#details-reviews");
OBVIZ.$baseContainer = $("#base-container");
OBVIZ.$comparedContainer = $("#comparison-container");
OBVIZ.$data = $("#data");
OBVIZ.$topics = $("#details-aspects");

OBVIZ.carousel = {

    init: function() {
        var $slickScreenshots = $(".slick-screenshots");
        $slickScreenshots.slick({
            infinite: true,
            dots: true,
            arrows: false,
            slidesToShow: 1,
            centerMode: true,
            variableWidth: true,
            swipeToSlide: true
        });
        $slickScreenshots.fadeIn();

        var $slickAlternatives = $(".slick-alternatives");
        $slickAlternatives.slick({
            infinite: true,
            arrows: false,
            dots: true,
            variableWidth: true,
            swipeToSlide: true
        });
        $slickAlternatives.on('click', '.alternative-body', OBVIZ.comparison.start);
    }
};

OBVIZ.comparison = {

    start: function() {
        // This function must be called with a click event
        var $slide = $(this);
        if (typeof $slide === 'undefined') {
            return;
        }

        // Clean the containers
        OBVIZ.$comparedContainer.find(".compared").find(".topic-container").empty().data("parsed", false);
        // Swap the container if necessary
        OBVIZ.$baseContainer.fadeOut(200, function() {

            OBVIZ.$comparedContainer.fadeIn(200);
        });

        // Add the second arrow to each gauge
        OBVIZ.$gauges.each(function() {

            var id = -1;
            if ($(this).parent().is(".opinion-box")) {

                // Topic gauge
                id = $(this).parent().data("topic");
            } else {
                // Global gauge
                id = "0";
            }

            var value = OBVIZ.$data.find("div[data-id='"+$slide.data("id")+"']").find("div[data-topic='"+id+"']").data("value");
            if (typeof value === 'undefined') {
                // TODO : if the compared app has no data for this topic, what to do ?
                console.log("Fail for id "+id);
            } else {
                OBVIZ.gauges.addArrow($(this), value);
            }
        });

        // Store the id of the compared app
        OBVIZ.$reviews.data("compared", $(this).data("id"));

        // Click again on the active topic
        OBVIZ.$topics.find(".opinion-box.active").click();
    },

    stop: function() {

        // Clean the arrows
        OBVIZ.$gauges.each(function() {
            OBVIZ.gauges.cleanArrow($(this));
        });

        OBVIZ.$reviews.data("compared", undefined);

        // Swap the containers
        OBVIZ.$comparedContainer.fadeOut(200, function() {

            OBVIZ.$baseContainer.fadeIn(200);
        });
    }
};

OBVIZ.gauges = {

    init: function() {

        // Init the gauges element and add store the object in the field gauge of the data
        OBVIZ.$gauges.each(function() {
            OBVIZ.gauges.makeGauge($(this));
        });

        // Put the height as the same than the width for the canvas
        $(window).resize(function() {
            OBVIZ.$gauges.each(function() {
                var width = $(this).width();
                $(this).height(width);
            });
        });
        $(window).resize();
    },

    makeGauge: function(element) {
        var chartID = element.attr("id");
        element.data("gauge", AmCharts.makeChart(chartID, {
            "addClassNames": true,
            "type": "gauge",
            "theme": "light",
            "color": "#000000",
            "fontFamily": "Dosis",
            "fontSize": 16,
            "axes": [ {
                "axisThickness": 0,
                "axisAlpha": 0.0,
                "tickAlpha": 0.0,
                "valueInterval": 100,
                "showFirstLabel": false,
                "showLastLabel": false,
                "bands": OBVIZ.bands,
                "bottomText": element.data("title"),
                "bottomTextYOffset": 20,
                "endValue": 100
            } ],
            "arrows": [ {
                "value": Number(element.data("value")),
                "color": "#505050",
                "startWidth": 10,
                "radius": "100%",
                "innerRadius": "40%",
                "borderAlpha": 1,
                "id": "0"
            } ],
            "export": {
                "enabled": true
            },
            "panEventsEnabled": false
        }));
    },

    addArrow: function(gauge, value, color) {
        color = typeof color !== 'undefined' ? color : "rgb(79, 187, 222)";
        var chart = gauge.data("gauge");

        if (typeof chart !== 'undefined') {

            OBVIZ.gauges.cleanArrow(gauge);

            chart.addArrow({
                "value": Number(value),
                "color": color,
                "startWidth": 10,
                "radius": "100%",
                "innerRadius": "40%",
                "borderAlpha": 1,
                "borderColor": "#000000",
                "id": "1"
            });

            chart.validateNow();
        }
    },

    cleanArrow: function(gauge) {
        var chart = gauge.data("gauge");

        if (typeof chart !== 'undefined') {

            // Clean the array but keep the first
            while (chart.arrows.length > 1) {
                chart.arrows.pop();
            }

            chart.validateNow();
        }
    },

    hoverIn: function(index) {

        if (typeof index === 'undefined') {
            index = 0;
        }

        OBVIZ.$gauges.each(function() {

            var $svg = $(this).find("svg");
            var cx = $svg.width() / 2;
            var cy = $svg.height() / 2;

            var $elems = $(this).find(".amcharts-gauge-arrow-"+index+" path");
            OBVIZ.rotate($elems, 5, cx, cy);
        });
    }
};

OBVIZ.reviews = {

    init: function() {

        OBVIZ.$topics.find(".list-aspects").on('click', '.opinion-box', function() {

            OBVIZ.$topics.find(".opinion-box").removeClass("active");
            $(this).addClass("active");

            var topicID = $(this).data("topic");
            OBVIZ.$baseContainer.find(".topic-container:visible").fadeOut(200);
            OBVIZ.$baseContainer.find(".topic-container[data-topic='" + topicID + "']").delay(200).fadeIn(200);

            OBVIZ.$comparedContainer.find(".topic-container:visible").fadeOut(200);
            OBVIZ.$comparedContainer.find(".topic-container[data-topic='" + topicID + "']").delay(200).fadeIn(200);

            // Load the reviews for the compared app only if we have an ID
            if (typeof OBVIZ.$reviews.data("compared") !== 'undefined') {

                var comparedID = OBVIZ.$reviews.data("compared");
                OBVIZ.reviews.get(topicID, comparedID, false);
            }

            var appID = OBVIZ.$data.find("div[data-main='true']").data("id");
            OBVIZ.reviews.get(topicID, appID, true);
        });
    },

    get: function(topicID, appID, mainApp) {
        var $containers;

        if (mainApp) {

            $containers = OBVIZ.$baseContainer.add(OBVIZ.$comparedContainer.find(".current")).find("div[data-topic='"+topicID+"']");
        } else {

            $containers = OBVIZ.$comparedContainer.find(".compared").find("div[data-topic='"+topicID+"']");
        }

        // Check if the reviews didn't have already loaded
        if ($containers.data("parsed")) {
            OBVIZ.refreshScroll($containers);
            return;
        } else {
            // Show the loading image
            $containers.parent().find(".loading-message").fadeIn();
        }

        var url = OBVIZ.$reviews.data("url");

        $containers.data("parsed", true);
        $.get(url, { id: appID })
            .done(function(data) {

                $containers.parent().find(".loading-message").hide(); // Hide the loading icon
                $containers.empty();

                if (typeof data[topicID] !== 'undefined') {

                    $.each(data[topicID], function(i, review) {
                        $containers.each(function() {
                            var columns = "col-xs-12";
                            if ($(this).parent().is("#base-container")) {
                                columns += " col-lg-4";
                            }

                            $(this).append('<div class="'+columns+'">'+review+'</div>');
                        });
                    });

                    OBVIZ.refreshScroll($containers);
                }

            }).fail(function() {
                $containers.data("parsed", false);
            });
    }
};

/**
 * Set the height of a review body to its initial height when the user click on it
 * @param element the review body
 */
OBVIZ.expand = function(element) {
    var $gradients = element.find(".top-gradient, .bottom-gradient");
    $gradients.hide();

    var $p = element.find(".inner");
    $p.parent().animate({
        height: ($p.outerHeight() + 10)+"px"
    }, 500, function() {
        $p.parent().height("auto");
    });
};

/**
 * Set the position of the scroll to display the opinion
 * @param container
 */
OBVIZ.refreshScroll = function(container) {
    // Scroll the opinions after the elements are displayed
    container.find('.review').each(function() {
        var $body = $(this).find('.content');
        var position = $body.find(".clause-negative, .clause-positive").position();
        // Move the opinion in the displayed area
        if (typeof position !== 'undefined') {
            $body.animate({
                scrollTop: position.top - 20
            });
        }

        // hide the gradient if the opinion is at the top or the text is too small
        if ($body.find(".inner").innerHeight() <= 100) {
            $body.siblings(".top-gradient, .bottom-gradient").fadeOut();
        } else if ($body.scrollTop() <= 10) {
            $body.siblings(".top-gradient").fadeOut();
        }
    });
};

OBVIZ.rotate = function($elems, offset, cx, cy) {
    $({ deg: 0 }).animate({ deg: offset }, {
        duration: 200,
        step: function(now) {
            $elems.attr('transform', 'rotate(' + now + ' '+cx+' '+cy+')');
        },
        done: function() {
            $({ deg: offset }).animate({ deg: -offset }, {
                duration: 200,
                step: function(now) {
                    $elems.attr('transform', 'rotate(' + now + ' '+cx+' '+cy+')');
                },
                done: function() {
                    $({ deg: -offset }).animate({ deg: 0 }, {
                        duration: 200,
                        step: function(now) {
                            $elems.attr('transform', 'rotate(' + now + ' '+cx+' '+cy+')');
                        }
                    });
                }
            });
        }
    });
};

$(document).ready(function() {

    OBVIZ.gauges.init();
    OBVIZ.carousel.init();
    OBVIZ.reviews.init();
});