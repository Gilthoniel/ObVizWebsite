/**
 * Created by gaylor on 03.07.15.
 * Javascript of the page where you visualize the information about one app
 */

var OBVIZ = OBVIZ || {};

OBVIZ.carousel = {
    slider: $(".slider-apps"),
    $opinions: $(".opinion-box"),
    $carousel: $("#header-carousel"),

    init: function() {
        var $slick = OBVIZ.carousel.slider;
        $slick.slick({
            mobileFirst: true,
            infinite: false,
            swipeToSlide: true,
            dots: true,
            arrows: false,
            variableWidth: true
        });
        $slick.on('click', '.slick-slide', OBVIZ.comparison.start);
        $slick.fadeIn();
    }
};

OBVIZ.comparison = {
    $resumes: $("#related-resumes"),

    init: function() {
        // Button to stop
        OBVIZ.comparison.$resumes.find(".btn-remove").click(function() {
            OBVIZ.comparison.stop();
        });
    },

    start: function() {
        var $slide = $(this);

        // Display the new gauges
        OBVIZ.carousel.$opinions.find(".mini-gauge").remove();
        OBVIZ.carousel.$opinions.each(function() {
            var value = $slide.find("span[data-topic='"+$(this).data("topic")+"']").data("value");

            $(this).append('<canvas width="70" height="40" data-value="'+value+'" class="mini-gauge"></canvas>');
        });
        OBVIZ.gauges.initMini();

        // Display the information of the app
        OBVIZ.comparison.$resumes.find(".box").html($slide.find(".app-information").clone());
        OBVIZ.carousel.$carousel.fadeOut(300, function() {
            OBVIZ.comparison.$resumes.fadeIn(300);
        });
    },

    stop: function() {

        OBVIZ.carousel.$opinions.find(".mini-gauge").fadeOut().remove();

        OBVIZ.comparison.$resumes.fadeOut(500, function() {
            OBVIZ.carousel.$carousel.fadeIn();
        });
    }
};

OBVIZ.gauges = {
    elements: $(".gauge"),
    options: {
        lines: 8,
        angle: 0.1,
        lineWidth: 0.13,
        pointer: {
            length: 0.4,
            strokeWidth: 0.08,
            color: '#000000'
        },
        limitMax: 'false',
        percentColors: [[0.0, "#cf1414" ], [0.5, "#aeaeae"], [1.0, "#1c8e17"]],
        strokeColor: '#e0e0e0',
        generateGradient: true
    },

    init: function() {
        OBVIZ.gauges.elements.each(function() {
            var gauge = new Gauge($(this).get(0)).setOptions(OBVIZ.gauges.options);
            gauge.maxValue = 100;
            gauge.animationSpeed = 1;
            gauge.set(parseInt($(this).data("value")));
        });
    },

    initMini: function() {
        var opts = OBVIZ.gauges.options;
        opts.pointer.color = '#506A98';
        opts.lineWidth = 0.3;

        $(".mini-gauge").each(function() {
            var value = $(this).data("value");
            if ($.isNumeric(value)) {
                value = Number(value);
            } else {
                value = 1;
            }

            var gauge = new Gauge($(this).get(0)).setOptions(OBVIZ.gauges.options);
            gauge.maxValue = 100;
            gauge.animationSpeed = 100;
            gauge.set(value);
        });
    }
};

OBVIZ.switch = {
    $block1: $("#details-block1"),
    $block2: $("#details-block2"),
    $reviews: $("#details-reviews"),

    init: function() {

        OBVIZ.switch.$opinions = OBVIZ.switch.$block2.find(".opinion-box");
        OBVIZ.switch.$containerReviews = OBVIZ.switch.$reviews.find(".topic-container");

        this.$block1.find(".opinion-box").click(function() {

            // Set the good opinion-box in the block 2
            OBVIZ.switch.$opinions
                .hide()
                .filter("[data-topic='"+($(this).data("topic"))+"']").show();

            // Show the reviews
            var $reviews = OBVIZ.switch.$containerReviews
                .hide()
                .filter("[data-topic='"+($(this).data("topic"))+"']");
            $reviews.show();

            // Show / hide blocks
            OBVIZ.switch.$block1.slideUp();
            OBVIZ.switch.$block2.slideDown(500, function() {
                OBVIZ.refreshScroll($reviews);
            });
        });

        this.$block2.find(".btn-back").click(function() {
            OBVIZ.switch.$block2.slideUp(500);
            OBVIZ.switch.$block1.slideDown(500);
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

OBVIZ.goTo = function(id) {

    // Change the opinion box
    OBVIZ.switch.$opinions.filter(":visible").fadeOut(400, function() {

        OBVIZ.switch.$opinions.filter("[data-topic='"+id+"']").fadeIn(400);
    });

    // Change the reviews
    OBVIZ.switch.$containerReviews.filter(":visible").fadeOut(400, function() {
        var container = OBVIZ.switch.$containerReviews.filter("[data-topic='"+id+"']").fadeIn(400, function() {
            OBVIZ.refreshScroll(container);
        })
    });
};

$(document).ready(function() {

    OBVIZ.gauges.init();
    OBVIZ.carousel.init();
    OBVIZ.comparison.init();
    OBVIZ.switch.init();
});