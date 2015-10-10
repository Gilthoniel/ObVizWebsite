/**
 * Created by gaylor on 03.07.15.
 * Javascript of the page where you visualize the information about one app
 */

OBVIZ.NUMBER_OF_REVIEWS = 10;

$(document).ready(function() {

    /** Data **/
    OBVIZ.data = (function() {
        var $data = $("#data");

        return {
            getMain: function() {
                return $data.find("div[data-main='true']");
            },

            getAlternative: function(id) {
                return $data.find("div[data-id='"+id+"']");
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
        var firstScroll = true;

        $gauges.each(function() {
            var text = {
                value: $(this).data("title"),
                position: 0.9,
                font: "14px Dosis"
            };

            var options = {
                bands: OBVIZ.bands,
                radius: 0.9,
                text: text
            };

            if (typeof $(this).data("value") !== 'undefined') {
                var arrow = {
                    value: Number($(this).data("value")),
                    color: "#505050",
                    baseLength: 5,
                    radius: 0.9,
                    innerRadius: 0.4
                };
            }

            if ($(this).is("#global-gauge")) {
                text.position = 0.8;
                text.font = "40px Dosis";

                arrow.baseLength = 20;
            } else if ($(this).is(".gauge-alternative")) {
                text.font = "10px Dosis";
                text.position = 0.6;
                options.angle = 140;
            } else {
                text.position = 0.5;
                options.angle = 140;
            }

            $(this).data("gauge", GaugeCharts.make($(this), options));

            if (typeof arrow !== 'undefined') {
                $(this).data("gauge").addArrow(arrow);
            }
        });

        // Click action
        $("#details-aspects").find(".list-aspects").on('click', '.opinion-box', function() {
            topicID = Number($(this).data("topic"));

            $boxes.removeClass("active");
            $(this).addClass("active");

            OBVIZ.alternatives.setGaugeTopic(topicID, $(this).data("title"));
            OBVIZ.reviews.get(true);

            // Smooth scroll
            if (!firstScroll) {
                $('body,html').animate({
                    scrollTop: $("#anchor-comparison").offset().top
                });
            } else {
                firstScroll = false;
            }
        });

        // Tooltips
        $("[data-toggle='tooltip']").tooltip({
            container: 'body'
        });

        return {

            getTopicID: function() {
                return topicID;
            },

            selectRandom: function() {
                $boxes.get(Math.floor(Math.random() * $boxes.size())).click();
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
        var $actions = $container.siblings(".button-more");
        var $button = $actions.find("button");
        var $loader = $("#details-reviews").find(".box-loading");
        var page = 0;
        var maxPage = 1;
        var xhr = null;

        function load() {
            startLoading();

            var ids = [];
            ids.push(OBVIZ.data.getMain().data("id"));
            if (OBVIZ.comparison.getCurrent().length > 0) {
                ids.push(OBVIZ.comparison.getCurrent());
            }

            if (xhr != null) {
                xhr.abort();
            }
            xhr = $.get($container.data("url"), {
                id: ids.join(":"),
                t: OBVIZ.gauges.getTopicID(),
                p: page,
                s: OBVIZ.NUMBER_OF_REVIEWS,
                order: OBVIZ.filters.getOrder()
            }).done(function(data) {

                if (page == 0) {
                    $container.empty();
                }

                maxPage = data.nbPage;

                $.each(data.reviews, function(i, review) {
                    $container.append(review);
                });

                endLoading();
            }).fail(function() {
                $loader.addClass("with-error");
                $loader.each(function() {
                    $(this).find(".text-error").detach().insertAfter($(this));
                });
            });
        }

        function startLoading() {
            $actions.stop().fadeOut(300, function() {
                $button.hide();
                $loader.show();

                $actions.fadeIn(300);
            });
        }

        function endLoading() {
            $actions.stop().fadeIn(300, function() {
                if (page < maxPage) {
                    $button.show();
                }
                $loader.hide();

                $actions.fadeIn(300);
            });
        }

        $button.click(function() {
            page++;
            load();
        });

        return {
            get: function(reset) {
                if (typeof reset === 'boolean' && reset) {
                    page = 0;
                }

                load();
            }
        }
    })();

    /** Filters for the reviews **/
    OBVIZ.filters = (function() {
        var $selects = $(".selection-filter");
        $selects.on('change', function() {
            $selects.val($(this).val());

            // Get the reviews of the main application of the page
            OBVIZ.reviews.get(true);
        });

        return {
            getOrder: function() {
                return $selects.val();
            }
        }
    })();

    /** Alternatives **/
    OBVIZ.alternatives = (function() {
        var $parent = $("#details-reviews");
        var $container = $("#details-alternatives");

        $container.affix({
            offset: {
                top: function() {
                    return $parent.offset().top
                }
            }
        });
        $container.find(".scrollbars-container").mCustomScrollbar({
            theme: 'inset-dark',
            scrollbarPosition: 'inside'
        });

        $(document).resize(function() {
            $parent.css("min-height", $(window).height()+"px");
            $container.width($container.parent().width());

            $container.find(".scrollbars-container")
                .height($(window).height()  - 210)
                .mCustomScrollbar('update');
        });
        $(document).resize();

        return {
            setGaugeTopic: function(topicID, title) {
                // Sort the alternatives
                var $bodies = $container.find(".alternative");
                var $parent = $bodies.parent();
                $bodies.sort(function(a,b) {
                    var valueA = Number(OBVIZ.data.getAlternative($(a).find(".alternative-body").data("id"))
                        .find("div[data-topic='"+topicID+"']")
                        .data("value"));
                    var valueB = Number(OBVIZ.data.getAlternative($(b).find(".alternative-body").data("id"))
                        .find("div[data-topic='"+topicID+"']")
                        .data("value"));

                    if (valueA > valueB) {
                        return -1;
                    } else if (valueB > valueA) {
                        return 1;
                    } else {
                        return 0;
                    }
                });
                $bodies.detach().appendTo($parent);
                $container.find(".scrollbars-container").mCustomScrollbar('update');

                // Set the gauge values
                $container.find(".alternative-body").each(function() {
                    var data = OBVIZ.data.getAlternative($(this).data("id")).find("div[data-topic='"+topicID+"']");
                    var $element = $(this).find(".chart-gauge");
                    var chart = $element.data("gauge");

                    chart.cleanArrow();

                    if (data.size() > 0) {
                        chart.setText(title);
                        chart.addArrow({
                            value: Number(data.data("value")),
                            color: "#505050",
                            baseLength: 5,
                            radius: 1.0,
                            innerRadius: 0.4
                        });
                    } else {
                        chart.setText("Global");
                        chart.addArrow({
                            value: Number($element.data("value")),
                            color: "#505050",
                            baseLength: 5,
                            radius: 1.0,
                            innerRadius: 0.4
                        });
                    }
                });
            }
        }
    })();

    OBVIZ.comparison = (function() {
        var $alternatives = $("#details-alternatives");
        var $elements = $alternatives.find(".alternative-body");
        var current = '';

        $alternatives.on('click', '.alternative-body', function() {
            // Keep the element in blue
            $elements.removeClass("active");
            $(this).addClass("active");

            // Save the id of the comparison application
            current = $(this).data("id");

            // Set the arrow of the gauge on the specified topic
            $("#details-aspects").find(".chart-gauge").each(function() {
                var value = OBVIZ.data.getAlternative(current).find("div[data-topic='"+$(this).data("topic")+"']").data("value");

                $(this).data("gauge").cleanArrow(1);
                if (typeof value !== 'undefined') {
                    var options = {
                        value: Number(value),
                        color: "#9ECFFF",
                        baseLength: 8,
                        radius: 1.0,
                        innerRadius: 0.4
                    };
                    if ($(this).data("topic") == '0') {
                        options.baseLength = 16
                    }

                    $(this).data("gauge").addArrow(options);
                }
            });

            // Show the gauges
            $('body,html').animate({
                scrollTop: $("#anchor-comparison").offset().top
            });

            // Load the good reviews
            OBVIZ.reviews.get(true);

            // Set the application's names
            var $names = $("#applications-name");
            $names.find("a").attr("href", $(this).data("url")).html($(this).data("name") + " &raquo;");
            $names.slideDown();
        });

        $("#stop-comparison").click(function() {
            // Clean the the blueish and saved id
            $elements.removeClass("active");
            current = '';

            // Remove the second arrows
            $("#details-aspects").find(".chart-gauge").each(function() {
                $(this).data("gauge").cleanArrow(1);
            });

            // Reload the reviews
            OBVIZ.reviews.get(true);

            // Hide the button
            $(this).closest("#applications-name").slideUp();
        }).hover(function() {
            $(this).stop().html(".");
            $(this).animate({
                width: 160
            }, function() {
                $(this).html("Turn off the comparison");
            });
        }, function() {
            $(this).stop().html(".");
            $(this).animate({
                width: 40
            }, function() {
                $(this).html('<span class="glyphicon glyphicon-off"></span>');
            });
        });

        return {
            getCurrent: function() {
                return current;
            }
        }
    })();

    OBVIZ.gauges.selectRandom();
});