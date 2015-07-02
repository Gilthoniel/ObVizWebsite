/**
 * Created by gaylor on 02.07.15.
 * Javascript of the Home page
 */
$(document).ready(function() {
    $(".app-slider").slick({
        mobileFirst: true,
        slidesToShow: 1,
        infinite: false,
        swipeToSlide: true,
        respondTo: 'slider',
        responsive: [
            {
                breakpoint: 1200,
                settings: {
                    slidesToShow: 8
                }
            },
            {
                breakpoint: 1000,
                settings: {
                    slidesToShow: 6
                }
            },
            {
                breakpoint: 800,
                settings: {
                    slidesToShow: 5
                }
            },
            {
                breakpoint: 600,
                settings: {
                    slidesToShow: 4
                }
            },
            {
                breakpoint: 400,
                settings: {
                    slidesToShow: 3
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

    var opts = {
        lines: 12,
        angle: 0,
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

    $(".app-gauge").each(function() {
        var gauge = new Gauge($(this).get(0)).setOptions(opts);
        gauge.maxValue = 100;
        gauge.animationSpeed = 1;
        gauge.set(parseInt($(this).data("value")));
    });
});
