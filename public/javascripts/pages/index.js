/**
 * Created by gaylor on 02.07.15.
 * Javascript of the Home page
 */
$(document).ready(function() {

    OBVIZ.$trending = $("#trending");

    /* Trending applications */
    OBVIZ.trending = new Trending();
    var categories = OBVIZ.$trending.find(".categories li");
    categories.eq(Math.floor(categories.length * Math.random())).click();

    OBVIZ.tip = (function() {

        /* Private */
        var $container = $("#home-tip");

        // Constructor
        {
            $container.find(".chart-gauge").each(function() {
                var $element = $(this);

                $element.data("gauge", GaugeCharts.make($element, {
                    bands: OBVIZ.bands,
                    radius: 0.9,
                    text: {
                        value: $element.data("title"),
                        position: 0.8,
                        font: "14px Dosis"
                    }
                }));
                $element.data("gauge").addArrow({
                    value: Number($element.data("value")),
                    color: "rgb(0,0,0)",
                    baseLength: 8
                });
            });
        }

        /* Public */
        return {

        }
    })();
});

/**
 * Functions to get the list of trending applications related to categories
 * @type {{init: Function, get: Function}}
 */
function Trending() {

    this.get = function(categories) {

        var url = OBVIZ.$trending.data("url");

        if (typeof categories === 'undefined') {
            categories = "";
        }

        OBVIZ.$trending.find(".icon-loader").stop().fadeIn();
        $.get(url, { categories: categories })
            .done(function(data) {
                var $container = OBVIZ.$trending.find(".results-container");
                $container.hide();

                // Clean the container
                $container.empty();
                // Add the items
                $.each(data, function(i, item) {
                    if (i % 3 == 0) {
                        $container.append('<div class="hidden-xs hidden-sm clearfix"></div>');
                    }

                    if (i % 2 == 0) {
                        $container.append('<div class="visible-sm-block clearfix"></div>');
                    }

                    $container.append('<div class="col-xs-12 col-sm-6 col-md-4">'+item+'</div>');
                });

                // Hide the elements before display the block
                var $items = $container.find(".body-app")
                    .css("opacity", 0)
                    .css("top", 100);
                $container.show();
                OBVIZ.$trending.find(".icon-loader").stop().fadeOut();

                // Animate to show the items
                $items.each(function(i) {
                    $(this).delay(i * 100).animate({
                        opacity: 1,
                        top: 0
                    }, function() {
                        $(this).find(".chart-gauge").each(function() {
                            var $element = $(this);

                            $element.data("gauge", GaugeCharts.make($element, {
                                bands: OBVIZ.bands,
                                radius: 0.8,
                                text: {
                                    value: $element.data("title"),
                                    position: 1.0,
                                    font: "12px Dosis"
                                }
                            }));
                            $element.data("gauge").addArrow({
                                value: Number($element.data("value")),
                                color: "rgb(75, 129, 174)",
                                baseLength: 8
                            });
                        });
                    });
                });

            }).fail(function(xhr) {

                if (xhr.status == '400') {
                    OBVIZ.$trending.find(".icon-loader").find(".box-loading").addClass("with-error");
                }
            });
    };
}
