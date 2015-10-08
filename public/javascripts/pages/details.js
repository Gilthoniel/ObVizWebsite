/**
 * Created by gaylor on 03.07.15.
 * Javascript of the page where you visualize the information about one app
 */

OBVIZ.NUMBER_OF_REVIEWS = 24;

$(document).ready(function() {

    /** Data **/
    OBVIZ.data = (function() {
        var $data = $("#data");

        return {
            getMain: function() {
                return $data.find("div[data-main='true']");
            }
        }
    })();

    /** Screenshots carousel **/
    OBVIZ.carousel = (function() {
        var $slickScreenshots = $(".slick-screenshots");
        $slickScreenshots.slick({
            infinite: false,
            dots: true,
            arrows: true,
            slidesToShow: 1,
            centerMode: true,
            variableWidth: true,
            swipeToSlide: true,
            autoplay: true,
            autoplaySpeed: 5000,
            cssEase: 'ease-out',
            speed: 1000,
            lazyLoad: 'ondemand'
        });
        $slickScreenshots.fadeIn();
    })();

    /** Gauges **/
    OBVIZ.gauges = (function() {
        var $gauges = $(".chart-gauge");
        var $boxes = $(".opinion-box");
        var topicID = 0;

        $gauges.each(function() {
            var text = {
                value: $(this).data("title"),
                position: 0.9,
                font: "14px Dosis"
            };

            var arrow = {
                value: Number($(this).data("value")),
                color: "#505050",
                baseLength: 10,
                radius: 1.0,
                innerRadius: 0.4
            };

            if ($(this).is("#global-gauge")) {
                text.position = 0.8;
                text.font = "40px Dosis";

                arrow.baseLength = 20;
            }

            $(this).data("gauge", GaugeCharts.make($(this), {
                bands: OBVIZ.bands,
                radius: 0.9,
                text: text
            }));
            $(this).data("gauge").addArrow(arrow);

            // Click action
            $boxes.click(function() {
                topicID = Number($(this).data("topic"));

                $boxes.removeClass("active");
                $(this).addClass("active");

                OBVIZ.reviews.get();
            });
        });

        // Put the height as the same than the width for the canvas
        $(window).resize(function() {
            $gauges.each(function() {
                var width = $(this).width();
                $(this).height(width);
            });
        });
        $(window).resize();

        return {

            getTopicID: function() {
                return topicID;
            },

            addArrow: function($element, arrow) {
                var chart = $element.data("gauge");

                if (typeof chart !== 'undefined') {

                    chart.cleanArrow();
                    chart.addArrow(arrow);
                }
            }
        }
    })();

    /** Reviews **/
    OBVIZ.reviews = (function() {

        var $container = $("#reviews-container");
        var page = 0;

        return {
            get: function() {

                $.get($container.data("url"), {
                    id: OBVIZ.data.getMain().data("id"),
                    t: OBVIZ.gauges.getTopicID(),
                    p: page,
                    s: OBVIZ.NUMBER_OF_REVIEWS,
                    order: OBVIZ.filters.getOrder()
                }).done(function(data) {

                    $container.empty();

                    $.each(data.reviews, function(i, review) {
                        $container.append(review);
                    });
                });
            }
        }
    })();

    /** Filters for the reviews **/
    OBVIZ.filters = (function() {
        var $selects = $(".selection-filter");
        $selects.on('change', function() {
            $selects.val($(this).val());

            // Get the reviews of the main application of the page
            OBVIZ.reviews.get();
        });

        return {
            getOrder: function() {
                return $selects.val();
            }
        }
    })();
});