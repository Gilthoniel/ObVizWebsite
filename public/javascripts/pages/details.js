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
        var slick = OBVIZ.carousel.slider;
        slick.slick({
            mobileFirst: true,
            slidesToShow: 1,
            infinite: false,
            swipeToSlide: true,
            dots: false,
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
                        slidesToShow: 3
                    }
                },
                {
                    breakpoint: 400,
                    settings: {
                        slidesToShow: 2
                    }
                },
                {
                    breakpoint: 250,
                    settings: {
                        slidesToShow: 1
                    }
                }
            ]
        });
        slick.on('init', function() {
            // Display the reviews on a user click
            OBVIZ.carousel.opinionBoxes.click(function() {

                // Add active class
                OBVIZ.carousel.opinionBoxes.removeClass("active");
                $(this).addClass("active");

                // Show the reviews of the opinion
                var id = $(this).data("topic");

                OBVIZ.carousel.containers.hide();
                $("#topic-"+id).fadeIn(500);
            });

            // Initialize the indicator of position
            var instance = slick.slick('getSlick');
            console.log(instance);
            var nbSlides = instance.$slides.length;
            var activeBreakpoint = instance.activeBreakpoint;
            var nbSlidesDisplayed = instance.breakpointSettings[activeBreakpoint].slidesToShow;

            if (typeof nbSlidesDisplayed !== 'undefined') {
                console.log(nbSlidesDisplayed);
                var percent = 100.0 / (nbSlides - nbSlidesDisplayed + 1);
                OBVIZ.carousel.indicator.find("div").css("width", Math.round(percent)+"%");
                OBVIZ.carousel.step = percent;
            }
        });
        slick.on('beforeChange', function(event, slick, current, next) {
            OBVIZ.carousel.indicator.find("div").animate({
                left: Math.round(OBVIZ.carousel.step * next)+"%"
            });
        });
        slick.trigger('init'); // The first init is fired before the bind
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
    })
});