/**
 * Created by gaylor on 03.07.15.
 * Javascript of the page where you visualize the information about one app
 */

/**
 *
 * Elements
 */
OBVIZ.$slider = $(".slider-apps");
OBVIZ.$headerCarousel = $("#header-carousel");
OBVIZ.$resumes = $("#related-resumes");
OBVIZ.$header = $("#details-header");
OBVIZ.$gauges = $(".chart-gauge");
OBVIZ.$block1 = $("#details-block1");
OBVIZ.$block2 = $("#details-block2");
OBVIZ.$reviews = $("#details-reviews");
OBVIZ.$baseContainer = $("#base-container");
OBVIZ.$comparedContainer = $("#comparison-container");
OBVIZ.$data = $("#app-data").find(".data");
OBVIZ.$detailsGauge = $("#details-gauge");
OBVIZ.$comparisonBG = $(".comparison-bg");

OBVIZ.carousel = {

    init: function() {
        var $slick = OBVIZ.$slider;
        $slick.slick({
            infinite: false,
            swipeToSlide: true,
            dots: true,
            arrows: false,
            slidesToShow: 8,
            mobileFirst: true,
            respondTo: 'slider',
            responsive: [
                {
                    breakpoint: 500,
                    settings: {
                        slidesToShow: 6
                    }
                },
                {
                    breakpoint: 300,
                    settings: {
                        slidesToShow: 6
                    }
                },
                {
                    breakpoint: 200,
                    settings: {
                        slidesToShow: 4,
                        dots: false
                    }
                }
            ]
        });
        $slick.on('click', '.slick-slide', OBVIZ.comparison.start);
        $slick.fadeIn();
    }
};

OBVIZ.comparison = {

    init: function() {
        // Button to stop
        OBVIZ.$resumes.find(".btn-remove").click(function() {
            OBVIZ.comparison.stop();
        });
    },

    start: function() {
        // This function must be called with a click event
        var $slide = $(this);
        if (typeof $slide === 'undefined') {
            return;
        }

        // Display the comparison reviews block
        OBVIZ.$baseContainer.fadeOut(300, function() {
            OBVIZ.$comparedContainer.fadeIn();
        });

        // Move the information of the chosen app
        OBVIZ.$resumes.find(".box").html($slide.find(".app-information").clone());
        OBVIZ.$resumes.find(".app-information").hover(OBVIZ.gauges.hoverIn, function() {});

        // Add the second arrow to each gauge
        OBVIZ.$gauges.each(function() {

            var id = -1;
            if ($(this).parent().is(".opinion-box")) {

                // This is a gauge of the current app
                id = $(this).parent().data("topic");
            } else if ($(this).is("#details-gauge")) {

                // Global or for the comparison
                id = OBVIZ.$detailsGauge.data("current");
                if (typeof id === 'undefined') {
                    id = "1";
                }
            } else {
                id = "0";
            }

            var value = OBVIZ.$resumes.find("span[data-topic='"+id+"']").data("value");
            if (typeof value === 'undefined') {
                // TODO : if the compared app has no data for this topic, what to do ?
                console.log("Fail for id "+id);
            } else {
                OBVIZ.gauges.addArrow($(this), value);
            }
        });

        // Display the information of the app
        OBVIZ.$headerCarousel.fadeOut(300, function() {
            OBVIZ.$resumes.fadeIn(300);

            OBVIZ.$comparisonBG.find(".current").animate({
                left: 0
            });
            OBVIZ.$comparisonBG.find(".compared").animate({
                right: 0
            });
        });

        // Get the reviews if we are in this mode
        if (typeof OBVIZ.$detailsGauge.data("current") !== 'undefined') {
            OBVIZ.goTo(OBVIZ.$detailsGauge.data("current"));
        }
    },

    stop: function() {

        // Clean the arrows
        OBVIZ.$gauges.each(function() {
            OBVIZ.gauges.cleanArrow($(this));
        });

        // Display the carousel of related apps
        OBVIZ.$comparisonBG.find(".current").animate({
            left: "-100%"
        });
        OBVIZ.$comparisonBG.find(".compared").animate({
            right: "-100%"
        });
        OBVIZ.$resumes.fadeOut(500, function() {
            OBVIZ.$headerCarousel.fadeIn();

            // Clear the header information
            OBVIZ.$resumes.find(".box");
        });

        OBVIZ.$comparedContainer.fadeOut(300, function() {
            OBVIZ.$baseContainer.fadeIn();
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
                $(this).height(width + 20); // Fix for the text
            });
        });
        $(window).resize();

        $("#app-data").find(".app-information").hover(this.hoverIn, function() {});
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

    hoverIn: function() {
        var $element = $(this);

        var index = 1;
        if ($element.parent().is("#app-data")) {
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

OBVIZ.switch = {

    init: function() {

        OBVIZ.$containerReviews = OBVIZ.$reviews.find(".topic-container");

        OBVIZ.$block1.find(".opinion-box").click(function() {
            var topicID = $(this).data("topic");

            // Store the current topicID in the gauge element of the block2
            OBVIZ.$detailsGauge.data("current", topicID);

            // Show the reviews
            var $reviews = OBVIZ.$containerReviews
                .hide()
                .filter("[data-topic='"+(topicID)+"']");
            $reviews.show();
            // Load them
            OBVIZ.goTo(topicID);

            // Show / hide blocks
            OBVIZ.$block1.fadeOut(300, function() {
                OBVIZ.$block2.fadeIn(300, function() {
                    OBVIZ.refreshScroll($reviews);
                })
            });
        });

        OBVIZ.$block2.find(".btn-back").click(function() {
            // Reset the current topicID store in the gauge element
            OBVIZ.$detailsGauge.data("current", undefined);

            OBVIZ.$block2.fadeOut(300, function() {
                OBVIZ.$block1.fadeIn(300);
            });
        });
    },

    changeGaugeValue: function(data) {
        var gauge = OBVIZ.$detailsGauge.data("gauge");

        // Value of the current application
        gauge.arrows[0].setValue(Number(data.data("value")));

        // Value of the compared application
        if (gauge.arrows.length >= 2) {
            var value = OBVIZ.$resumes.find("span[data-topic='" + data.data("topic") + "']").data("value");
            gauge.arrows[1].setValue(Number(value));
        }

        gauge.axes[0].setBottomText(data.data("title"));
    }
};

OBVIZ.reviews = {

    get: function(url, mode, topicID) {
        var $containers;

        if (mode == 'current') {
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

        $containers.data("parsed", true);
        $.get(url).done(function(data) {

            $containers.parent().find(".loading-message").hide(); // Hide the loading icon
            $containers.html("");

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
        $body.animate({
            scrollTop: position.top - 20
        });

        // hide the gradient if the opinion is at the top or the text is too small
        if ($body.find(".inner").innerHeight() <= 100) {
            $body.siblings(".top-gradient, .bottom-gradient").fadeOut();
        } else if ($body.scrollTop() <= 10) {
            $body.siblings(".top-gradient").fadeOut();
        }
    });
};

/**
 * Change the values for the chosen topic (Gauges, title, ...)
 * @param id of the topic
 */
OBVIZ.goTo = function(id) {

    // Change the opinion box
    var data = OBVIZ.$data.find("span[data-topic='"+id+"']");
    OBVIZ.switch.changeGaugeValue(data);

    // Change the reviews
    $.when(OBVIZ.$containerReviews.filter(":visible").fadeOut(400, function() {
            OBVIZ.$containerReviews.filter("[data-topic='"+id+"']").fadeIn(400);
        })
    ).then(function() {
        // Reviews for the current app
        var currentURL = OBVIZ.$data.data("url");
        OBVIZ.reviews.get(currentURL, 'current', id);
        // and for the compared app if activated
        if (OBVIZ.$resumes.find(".data").size() > 0) {
            var comparedURL = OBVIZ.$resumes.find(".data").data("url");
            OBVIZ.reviews.get(comparedURL, 'compared', id);
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

OBVIZ.bands = [ {
    "color": "#cc4748",
    "endValue": 20,
    "innerRadius": "80%",
    "startValue": 0
}, {
    "color": "#CF6868",
    "endValue": 40,
    "innerRadius": "80%",
    "startValue": 20
}, {
    "color": "#e6e6e6",
    "endValue": 60,
    "innerRadius": "80%",
    "startValue": 40
}, {
    "color": "#A5D1A5",
    "endValue": 80,
    "innerRadius": "80%",
    "startValue": 60
}, {
    "color": "#84b761",
    "endValue": 100,
    "innerRadius": "80%",
    "startValue": 80
} ];

$(document).ready(function() {

    OBVIZ.gauges.init();
    OBVIZ.carousel.init();
    OBVIZ.comparison.init();
    OBVIZ.switch.init();
});