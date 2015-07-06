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

                // Show the reviews of the opinion
                var id = $(this).data("topic");

                OBVIZ.carousel.containers.hide();
                var container = $("#topic-"+id);
                container.fadeIn(500, function() {
                    // Scroll the opinions after the elements are displayed
                    container.find('.review').each(function() {
                        var $body = $(this).find('.content');
                        var position = $body.find(".clause-negative, .clause-positive").position();
                        $body.scrollTop(position.top - 20);

                        // hide the gradient if the opinion is at the top or the text is too small
                        if ($body.find(".inner").outerHeight() <= 100) {
                            $body.siblings(".top-gradient, .bottom-gradient").hide();
                        } else if ($body.scrollTop() <= 10) {
                            $body.siblings(".top-gradient").hide();
                        }
                    });
                });
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
    }
};

OBVIZ.gauges = {
    elements: $(".gauge"),

    init: function() {
        var opts = {
            lines: 12,
            angle: 0.0,
            lineWidth: 0.15,
            pointer: {
                length: 0.4,
                strokeWidth: 0.05,
                color: '#000000'
            },
            limitMax: 'false',
            percentColors: [[0.0, "#cf1414" ], [0.5, "#aeaeae"], [1.0, "#1c8e17"]],
            strokeColor: '#e0e0e0',
            generateGradient: true
        };

        $(".gauge").each(function() {
            var gauge = new Gauge($(this).get(0)).setOptions(opts);
            gauge.maxValue = 100;
            gauge.animationSpeed = 300;
            gauge.set(parseInt($(this).data("value")));
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

    // Open the comparison box when the user click on a related application
    var $relatedApps = $(".related-app");
    $relatedApps.click(function() {
        $relatedApps.removeClass("active");
        $(this).addClass("active");

        $("#details-reviews").fadeOut(200, function() {
            $("#details-comparison").fadeIn(200);
        });
    });

    // Button to close the comparison box
    $("#close-comparison").click(function() {
        $relatedApps.removeClass("active");
        $("#details-comparison").fadeOut(200, function() {
            $("#details-reviews").fadeIn(200);
        });
    });
});