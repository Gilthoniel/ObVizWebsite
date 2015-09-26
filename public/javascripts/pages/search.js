/**
 * Created by Gaylor on 26.09.2015.
 *
 */

$(document).ready(function() {
    /* Searching */
    OBVIZ.search = new Search();
    OBVIZ.search.get();
});

/**
 * Functions to execute a search
 * @type {{init: Function, get: Function}}
 */
function Search() {

    var $container = $("#search-results");
    var $searchRequest = undefined;
    var $discoverRequest = undefined;

    // Scrollbar for the results of the search
    $("#search-result-applications").mCustomScrollbar({
        axis: "x",
        theme: 'dark',
        mouseWheel: {
            enable: false
        },
        scrollButtons: {
            enable: true,
            scrollAmount: 370,
            scrollType: 'stepped'
        },
        advanced: {
            updateOnImageLoad: false
        }
    });

    this.get = function(categories) {

        var query = $container.data("query");

        if (typeof categories === 'undefined') {
            categories = "";
        }

        performSearch(query, categories);
        performDiscover(query, categories);
    };

    function performSearch(query, categories) {

        $container.find(".icon-loader").stop().show();
        var $results = $("#search-result-applications");

        var url = $container.data("search");

        if (typeof $searchRequest !== 'undefined') {
            $searchRequest.abort();
        }
        $searchRequest = $.get(url, { query: query, categories: categories })
            .done(function(data) {
                $container.find(".icon-loader").stop().fadeOut();

                var $ul = $results.find(".list-container");
                $ul.empty();
                $ul.width(data.length * 370);
                $ul.hide();

                $.each(data, function(i, item) {
                    $ul.append("<li>"+item+"</li>");
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
                        color: "rgb(75, 129, 174)",
                        baseLength: 8
                    });
                });

                $ul.fadeIn(1000);
            });
    }

    function performDiscover(query, categories) {
        var url = $container.data("discover");
        var $discover = $("#search-result-discover");

        $discover.siblings(".icon-loader").stop().slideDown();

        if (typeof $discoverRequest !== 'undefined') {
            $discoverRequest.abort();
        }
        $discoverRequest = $.get(url, { query: query, categories: categories })
            .done(function(data) {

                $discover
                    .hide()
                    .empty()
                    .append(data)
                    .fadeIn(1000)
                    .find(".scroll-container").mCustomScrollbar({
                        axis: "x",
                        theme: 'dark',
                        scrollButtons: {
                            enable: true
                        },
                        mouseWheel: {
                            enable: false
                        },
                        advanced: {
                            updateOnImageLoad: false
                        }
                    });

                $discover.siblings(".icon-loader").stop().slideUp();

                $discover.find(".chart-gauge").each(function() {
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
                        color: "rgb(75, 129, 174)",
                        baseLength: 8
                    });
                })
            });
    }
}