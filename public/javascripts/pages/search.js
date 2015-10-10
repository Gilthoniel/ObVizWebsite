/**
 * Created by Gaylor on 26.09.2015.
 *
 */

$(document).ready(function() {

    OBVIZ.search = (function() {
        var $root = $("#search-results");
        var $buttonMore = $root.find(".get-more");
        var $container = $root.find(".results-container");
        var $wrapper = $root.find(".results-wrapper");
        var $searchRequest = undefined;

        $buttonMore.click(function() {
            $(this).slideUp();

            $container.animate({
                height: $wrapper.height()
            });
        });

        $wrapper.on('mouseenter', '.search-app', function() {
            var $description = $(this).find(".description");
            $(this).find(".opinions").stop().fadeOut(100, function() {
                $description.stop().fadeIn(100);
            });
        });
        $wrapper.on('mouseleave', '.search-app', function() {
            var $opinions = $(this).find(".opinions");
            $(this).find(".description").stop().fadeOut(100, function() {
                $opinions.stop().fadeIn(100);
            });
        });

        function performSearch(query, categories) {

            $root.find(".icon-loader").stop().show();

            if (typeof $searchRequest !== 'undefined') {
                $searchRequest.abort();
            }
            $searchRequest = $.get($root.data("url"), { query: query, categories: categories })
                .done(function(data) {
                    $root.find(".icon-loader").stop().fadeOut();

                    var $ul = $wrapper.find(".list-container");
                    $ul.fadeOut();
                    $ul.empty();

                    $.each(data, function(i, item) {
                        $ul.append(item);
                    });

                    $ul.find(".chart-gauge").each(function() {
                        GaugeCharts.make($(this), {
                            bands: OBVIZ.bands,
                            radius: 0.9,
                            text: {
                                value: $(this).data("title"),
                                position: 1.0,
                                font: "12px Dosis"
                            }
                        }).addArrow({
                            value: Number($(this).data("value")),
                            delay: 1000,
                            color: "rgb(75, 129, 174)",
                            baseLength: 8
                        });
                    });

                    $container.animate({
                        height: $ul.find("li:first-child").outerHeight() * 2 + 20 * 2
                    }, function() {
                        if ($container.height() < $wrapper.height()) {
                            $root.find(".get-more").fadeIn();
                        }
                    });

                    $ul.fadeIn(500);
                });
        }

        return {
            get: function(categories) {

                var query = $root.data("query");

                if (typeof categories === 'undefined') {
                    categories = "";
                }

                performSearch(query, categories);
                OBVIZ.discover.get(query, categories);
            }
        }
    })();

    OBVIZ.discover = (function() {
        var $root = $("#discover-results");
        var $container = $root.find(".results-container");
        var $discoverRequest = undefined;

        $container.on('mouseenter', '.search-app', function() {
            var $description = $(this).find(".description");
            $(this).find(".opinions").stop().fadeOut(100, function() {
                $description.stop().fadeIn(100);
            });
        });
        $container.on('mouseleave', '.search-app', function() {
            var $opinions = $(this).find(".opinions");
            $(this).find(".description").stop().fadeOut(100, function() {
                $opinions.stop().fadeIn(100);
            });
        });
        $container.on('click', '.get-more', function() {
            $(this).slideUp();

            var $wrapper = $(this).siblings(".results-wrapper");
            $wrapper.animate({
                height: $wrapper.find(".discover-apps").height()
            });
        });

        function performDiscover(query, categories) {
            $root.find(".icon-loader").stop().slideDown();

            if (typeof $discoverRequest !== 'undefined') {
                $discoverRequest.abort();
            }
            $discoverRequest = $.get($root.data("url"), { query: query, categories: categories })
                .done(function(data) {

                    $container
                        .hide()
                        .empty()
                        .append(data)
                        .fadeIn(1000);

                    $container.find(".discover-item").each(function() {

                        // Set the good height to display only one element
                        var $wrapper = $(this).find(".results-wrapper");
                        var $ul = $(this).find(".discover-apps");
                        $wrapper.animate({
                            height: $ul.find("li:first-child").height() + 20
                        }, function() {
                            // Display or not the give me more button
                            if ($wrapper.height() < $ul.height()) {
                                $wrapper.siblings(".get-more").show();
                            }
                        });
                    });

                    $root.find(".icon-loader").stop().slideUp();

                    $container.find(".chart-gauge").each(function() {
                        GaugeCharts.make($(this), {
                            bands: OBVIZ.bands,
                            radius: 0.9,
                            text: {
                                value: $(this).data("title"),
                                position: 1.0,
                                font: "12px Dosis"
                            }
                        }).addArrow({
                            value: Number($(this).data("value")),
                            delay: 1000,
                            color: "rgb(75, 129, 174)",
                            baseLength: 8
                        });
                    })
                });
        }

        return {
            get: function(query, categories) {
                performDiscover(query, categories)
            }
        }
    })();

    OBVIZ.search.get();
});