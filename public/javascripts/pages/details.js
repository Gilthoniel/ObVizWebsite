/**
 * Created by gaylor on 03.07.15.
 * Javascript of the page where you visualize the information about one app
 */

var OBVIZ = OBVIZ || {};

OBVIZ.carousel = {
    slider: $(".opinion-slider"),
    indicator: $(".indicator-slider"),
    containers: $(".topic-container"),
    opinionBoxes: $(".opinion-box"),
    step: 0,

    init: function() {
        var $slick = OBVIZ.carousel.slider;
        $slick.slick({
            mobileFirst: true,
            slidesToShow: 1,
            infinite: false,
            swipeToSlide: true,
            dots: false,
            centerMode: true,
            centerPadding: '0px',
            respondTo: 'slider',
            responsive: [
                {
                    breakpoint: 1200,
                    settings: {
                        slidesToShow: 6
                    }
                },
                {
                    breakpoint: 1000,
                    settings: {
                        slidesToShow: 5
                    }
                },
                {
                    breakpoint: 800,
                    settings: {
                        slidesToShow: 4
                    }
                },
                {
                    breakpoint: 600,
                    settings: {
                        slidesToShow: 3,
                        centerPadding: '0px'
                    }
                },
                {
                    breakpoint: 400,
                    settings: {
                        slidesToShow: 1,
                        centerPadding: '100px'
                    }
                },
                {
                    breakpoint: 250,
                    settings: {
                        slidesToShow: 1,
                        centerPadding: '0px'
                    }
                }
            ]
        });
        $slick.on('init', function() {
            // Display the reviews on a user click
            OBVIZ.carousel.opinionBoxes.click(function() {

                // Add active class
                OBVIZ.carousel.opinionBoxes.removeClass("active");
                $(this).addClass("active");

                // Update the field in the comparison box
                OBVIZ.comparison.$currentOpinion.html($(this).data("value"));

                // Show the reviews of the opinion
                var id = $(this).data("topic");
                if (OBVIZ.comparison.$reviews.is(":visible")) {
                    OBVIZ.carousel.containers.hide();

                    var container = $("#topic-"+id);
                    container.fadeIn(500, function() {
                        OBVIZ.carousel.refreshScroll(container);
                    });
                } else {

                    // If there is an active element, we trigger click to refresh the data
                    OBVIZ.comparison.$relatedAppsItem.filter(".active").click();
                }
            });

            // Initialize the indicator of position
            var instance = $slick.slick('getSlick');
            var nbSlides = instance.$slides.length;

            var percent = 100.0 / nbSlides;
            OBVIZ.carousel.indicator.find("div").css("width", Math.round(percent)+"%");
            OBVIZ.carousel.step = percent;
        });
        $slick.on('beforeChange', function(event, slick, current, next) {
            OBVIZ.carousel.indicator.find("div").animate({
                left: Math.round(OBVIZ.carousel.step * next)+"%"
            });
        });
        // The first init is fired before the bind
        $slick.trigger('init');
        // Click on the first item by default
        OBVIZ.carousel.opinionBoxes.first().click();
    },

    /**
     * Get the topic ID of the active slide
     * @returns {*} String of the ID
     */
    getActiveID: function() {
        // Get the active slide
        var slide = OBVIZ.carousel.opinionBoxes.filter(".active");

        return slide.data("topic");
    },

    refreshScroll: function(container) {
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
                $body.siblings(".top-gradient, .bottom-gradient").hide();
            } else if ($body.scrollTop() <= 10) {
                $body.siblings(".top-gradient").hide();
            }
        });
    }
};

OBVIZ.gauges = {
    elements: $(".gauge"),
    options: {
        lines: 12,
        angle: 0.15,
        lineWidth: 0.4,
        pointer: {
            length: 0.5,
            strokeWidth: 0.05,
            color: '#000000'
        },
        limitMax: 'false',
        percentColors: [[0.0, "#cf1414" ], [0.5, "#aeaeae"], [1.0, "#1c8e17"]],
        strokeColor: '#e0e0e0',
        generateGradient: true
    },

    init: function() {
        $(".gauge").each(function() {
            var gauge = new Gauge($(this).get(0)).setOptions(OBVIZ.gauges.options);
            gauge.maxValue = 100;
            gauge.animationSpeed = 300;
            gauge.set(parseInt($(this).data("value")));
        });
    }
};

OBVIZ.comparison = {
    $content: $("#details-comparison"),
    $relatedApps: $(".related-app"),
    $reviews: $("#details-reviews"),
    $currentOpinion: $("#current-opinion"),

    init: function() {

        // Init the gauge
        OBVIZ.comparison.gauge = new Gauge(this.$content.find("#gauge-comparison").get(0))
            .setOptions(OBVIZ.gauges.options);

        this.gauge.maxValue = 100;
        this.gauge.animationSpeed = 100;
        this.gauge.set(50);

        // Open the comparison box when the user click on a related application
        this.$relatedAppsItem = this.$relatedApps.find("li");
        this.$relatedApps.on("click", "li", function() {
            OBVIZ.comparison.$relatedAppsItem.removeClass("active");
            $(this).addClass("active");

            // Get the active topic id
            var id = OBVIZ.carousel.getActiveID();
            // Display the opinion value in the gauge object
            var value = $(this).find(".data").find("span[data-id='"+id+"']").data("value");
            if ($.isNumeric(value)) {
                OBVIZ.comparison.gauge.set(Number(value));
            } else {
                OBVIZ.comparison.gauge.set(1);
            }

            var info = $(this).find(".data").find("dl").clone();
            OBVIZ.comparison.$reviews.fadeOut(200, function() {

                // Update the information before show it
                OBVIZ.comparison.$content.find(".information-container").html(info);

                OBVIZ.comparison.$content.fadeIn(200);
            });
        });

        // Button to close the comparison box
        $("#close-comparison").click(function() {

            // Hide the content
            OBVIZ.comparison.$relatedAppsItem.removeClass("active");
            OBVIZ.comparison.$content.fadeOut(200, function() {

                OBVIZ.comparison.$reviews.fadeIn(200);
            });
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

OBVIZ.goTo = function(id) {

    $(".opinion-box").each(function(i) {
        if ($(this).data("topic") == id) {
            OBVIZ.carousel.slider.slick('slickGoTo', i, false);
            $(this).click();
        }
    });
};

$(document).ready(function() {

    OBVIZ.gauges.init();
    OBVIZ.carousel.init();
    OBVIZ.comparison.init();
});