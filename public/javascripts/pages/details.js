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
OBVIZ.$data = $("#app-data").find(".data");
OBVIZ.$detailsGauge = $("#details-gauge");

OBVIZ.carousel = {

    init: function() {
        var $slick = OBVIZ.$slider;
        $slick.slick({
            mobileFirst: true,
            infinite: false,
            swipeToSlide: true,
            dots: true,
            arrows: false,
            slidesToShow: 8
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

        // Move the information of the chosen app
        OBVIZ.$resumes.find(".box").html($slide.find(".app-information").clone());

        // Add the second arrow to each gauge
        OBVIZ.$gauges.each(function() {

            var id = -1;
            if ($(this).parent().is(".opinion-box")) {

                // This is a gauge of the current app
                id = $(this).parent().data("topic");
            } else {

                // This is a gauge of the compared app so we need the current topic
                id = OBVIZ.$detailsGauge.data("current");
                if (typeof id === 'undefined') {
                    id = "1";
                }
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
        });
        OBVIZ.$header.find("#img-comparison").addClass("versus");
    },

    stop: function() {

        // Clean the arrows
        OBVIZ.$gauges.each(function() {
            OBVIZ.gauges.cleanArrow($(this));
        });

        // Display the carousel of related apps
        OBVIZ.$resumes.fadeOut(500, function() {
            OBVIZ.$headerCarousel.fadeIn();
        });

        // Hide the versus image
        OBVIZ.$header.find("#img-comparison").removeClass("versus").css("opacity", 0);
    }
};

OBVIZ.gauges = {

    init: function() {

        // Init the gauges element and add store the object in the field gauge of the data
        OBVIZ.$gauges.each(function() {
            OBVIZ.gauges.makeGauge($(this));
        })
    },

    makeGauge: function(element) {
        var chartID = element.attr("id");
        element.data("gauge", AmCharts.makeChart(chartID, {
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
                "startWidth": 8,
                "radius": "70%",
                "innerRadius": "5%"
            } ],
            "export": {
                "enabled": true
            }
        }));
    },

    addArrow: function(gauge, value, color) {
        color = typeof color !== 'undefined' ? color : "#337AB7";
        var chart = gauge.data("gauge");

        if (typeof chart !== 'undefined') {

            OBVIZ.gauges.cleanArrow(gauge);

            chart.addArrow({
                "value": Number(value),
                "color": color,
                "startWidth": 6,
                "radius": "60%",
                "innerRadius": "15%"
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
    }
};

OBVIZ.switch = {

    init: function() {

        OBVIZ.$containerReviews = OBVIZ.$reviews.find(".topic-container");

        OBVIZ.$block1.find(".opinion-box").click(function() {

            // Set the good value to the gauge
            var elemData = OBVIZ.$data.find("span[data-topic='"+$(this).data("topic")+"']");
            OBVIZ.switch.changeGaugeValue(elemData);

            // Set the current topic for the block2 gauge
            OBVIZ.$detailsGauge.data("current", $(this).data("topic"));

            // Show the reviews
            var $reviews = OBVIZ.$containerReviews
                .hide()
                .filter("[data-topic='"+($(this).data("topic"))+"']");
            $reviews.show();

            // Show / hide blocks
            OBVIZ.$block1.slideUp();
            OBVIZ.$block2.slideDown(500, function() {
                OBVIZ.refreshScroll($reviews);
            });
        });

        OBVIZ.$block2.find(".btn-back").click(function() {
            OBVIZ.$block2.slideUp(500);
            OBVIZ.$block1.slideDown(500);
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

    get: function() {
        var url = OBVIZ.$reviews.data("url");

        $.get(url).done(function(data) {

            $.each(data, function(key, value) {
                var elem = OBVIZ.$reviews.find(".topic-container[data-topic='"+key+"']").first();

                if (elem.size() > 0) {
                    $.each(value, function(i, review) {
                        elem.append('<div class="col-xs-12 col-lg-4">'+review+'</div>');
                    });
                }
            })

        }).fail(function() {

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
    OBVIZ.$containerReviews.filter(":visible").fadeOut(400, function() {
        var container = OBVIZ.$containerReviews.filter("[data-topic='"+id+"']").fadeIn(400, function() {
            OBVIZ.refreshScroll(container);
        })
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

    OBVIZ.reviews.get();
});