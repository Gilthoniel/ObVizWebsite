/**
 * Created by gaylor on 02.07.15.
 * Javascript of the Home page
 */

function onYouTubeIframeAPIReady() {
    console.log("Loading video");
    OBVIZ.video.setPlayer(new YT.Player('player', {
        videoId: 'PCahcqUqv3A',
        events: {
            'onReady': function(event) {
                if (OBVIZ.video.state()) {
                    OBVIZ.video.getContainer().slideDown();
                } else {
                    console.log(OBVIZ.video.getButton());
                    OBVIZ.video.getButton().slideDown();
                }
            },
            'onStateChange': function(event) {
                if (event.data == YT.PlayerState.ENDED) {
                    OBVIZ.video.getContainer().slideUp();
                    OBVIZ.video.getButton().slideDown();
                }
            }
        }
    }));
}

$(document).ready(function() {

    OBVIZ.$trending = $("#trending");

    /* Trending applications */
    OBVIZ.trending = new Trending();
    var categories = OBVIZ.$trending.find(".categories li");
    categories.eq(Math.floor(categories.length * Math.random())).click();

    OBVIZ.video = (function() {
        var $container = $("#home-intro");
        var $button = $("#replay-button");
        var player;

        var tag = document.createElement('script');

        tag.src = "https://www.youtube.com/iframe_api";
        var firstScriptTag = document.getElementsByTagName('script')[0];
        firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

        $button.click(function() {
            $container.slideDown(500, function() {
                OBVIZ.video.getPlayer().playVideo();
            });
            $(this).slideUp();
        });

        return {
            getContainer: function() {
                return $container;
            },
            getButton: function() {
                return $button;
            },
            getPlayer: function() {
                return player;
            },
            setPlayer: function(value) {
                player = value;
            },
            state: function() {
                return $container.data("cookie");
            }
        }
    })();

    OBVIZ.tip = (function() {

        /* Private */
        var $container = $("#home-tip");

        // Constructor
        {
            $container.find(".chart-gauge").each(function() {
                var $element = $(this);

                $element.data("gauge", GaugeCharts.make($element, {
                    bands: OBVIZ.bands,
                    radius: 1.0,
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

            var $ul = $container.find(".headline-alternatives ul");
            $.get($ul.data("url"), { id: $ul.data("id"), topic: $ul.data("topic") })
                .done(function(data) {
                    $.each(data, function(i, item) {

                        if (i < 2) {
                            $ul.append("<li>" + item + "</li>");
                        }
                    });

                    $ul.find(".chart-gauge").each(function() {
                        var $element = $(this);

                        $element.data("gauge", GaugeCharts.make($element, {
                            bands: OBVIZ.bands,
                            radius: 1.0
                        }));
                        $element.data("gauge").addArrow({
                            value: Number($element.data("value")),
                            color: "rgb(79, 187, 222)",
                            baseLength: 5,
                            radius: 0.9
                        });
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
                                    position: 0.8,
                                    font: "12px Dosis"
                                }
                            }));
                            $element.data("gauge").addArrow({
                                value: Number($element.data("value")),
                                delay: 1000,
                                color: "rgb(0,0,0)",
                                baseLength: 5,
                                radius: 0.9
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
