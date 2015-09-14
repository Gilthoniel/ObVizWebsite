/**
 * Created by gaylor on 29.06.15.
 * Javascripts for all the pages
 */

var OBVIZ = OBVIZ || {};

/* Gauge's colors */
OBVIZ.bands = [{
    "color": "#cc4748",
    "end": 20,
    "radius": 0.8,
    "start": 0
}, {
    "color": "#CF6868",
    "end": 40,
    "radius": 0.8,
    "start": 20
}, {
    "color": "#e6e6e6",
    "end": 60,
    "radius": 0.8,
    "start": 40
}, {
    "color": "#A5D1A5",
    "end": 80,
    "radius": 0.8,
    "start": 60
}, {
    "color": "#84b761",
    "end": 100,
    "radius": 0.8,
    "start": 80
}];

$(document).ready(function() {

    OBVIZ.$searchbar = $("#search-bar");
    OBVIZ.$searchResults = $("#search-results");
    OBVIZ.$bodyContent = $("#body-content");

    /* Searching */
    OBVIZ.search = new Search();

    /* General categories mechanism */
    $(".categories").on('click', 'li', function() {

        var categories = $(this).data("categories");
        if ($(this).is(".active")) {

            $(this).removeClass("active").find(".title").animate({
                width: "0px"
            });
            categories = '';
        } else {

            var $categories = $(this).siblings("li");
            $categories
                .removeClass("active")
                .find(".title").animate({
                    width: "0px"
                });

            $(this)
                .addClass("active")
                .find(".title").animate({
                    width: "150px"
                });
        }

        switch ($(this).data("action")) {
            case 'trends':
                OBVIZ.trending.get(categories);
                break;
            default:
                OBVIZ.search.get(categories);
                break;
        }
    }).on('input change', 'select', function() {

        var categories = $(this).val();

        switch ($(this).data("action")) {
            case 'trends':
                OBVIZ.trending.get(categories);
                break;
            default:
                OBVIZ.search.get(categories);
                break;
        }
    });
});

OBVIZ.toggleContainer = function ($container) {

    if ($container.is("#body-content")) {

        OBVIZ.$searchResults.fadeOut(300, function() {
            OBVIZ.$bodyContent.fadeIn(300);
        });

    } else {

        OBVIZ.$bodyContent.fadeOut(300, function() {
            OBVIZ.$searchResults.show();
        });

    }
};

/**
 * Functions to execute a search
 * @type {{init: Function, get: Function}}
 */
function Search() {

    var $searchbar = $("#search-bar");
    var $container = $("#search-results");
    var $searchRequest = undefined;
    var $discoverRequest = undefined;

    var $this = this;
    $searchbar.submit(function(event) {
        event.preventDefault();

        // Close the menu
        $("#navbar-obviz").find(".navbar-collpase").removeClass("in");

        var $categories = $container.find(".list-categories li");
        $categories.removeClass("active");

        $this.get();
    });
    $searchbar.hover(function() {

        $searchbar.find(".form-group.hidden-xs").animate({
            width: 200
        }, 200);

        $searchbar.find(".hidden-xs input[type='text']").focus();

    }, function() {
        // Nothing happens on hover out
    });
    $searchbar.focusout(function() {

        // Close only if there's no text inside the search bar
        if ($searchbar.find("input[type='text']").val() == "") {
            $searchbar.find(".form-group").animate({
                width: 0
            }, 200);
        }
    });

    // Scrollbar for the results of the search
    $("#search-result-applications").mCustomScrollbar({
        axis: "x",
        theme: 'dark',
        mouseWheel: {
            enable: false
        },
        scrollButtons: {
            enable: true,
            scrollAmount: 270,
            scrollType: 'stepped'
        },
        advanced: {
            updateOnImageLoad: false
        }
    });

    this.get = function(categories) {

        var query = $searchbar.find("input[type='text']").val();

        if (typeof categories === 'undefined') {
            categories = "";
        }

        performSearch(query, categories);
        performDiscover(query, categories);
    };

    this.close = function() {

        OBVIZ.toggleContainer(OBVIZ.$bodyContent);
    };

    function performSearch(query, categories) {

        $container.find(".icon-loader").stop().show();
        var $results = $("#search-result-applications");
        OBVIZ.toggleContainer($container);

        var url = $container.data("search");

        if (typeof $searchRequest !== 'undefined') {
            $searchRequest.abort();
        }
        $searchRequest = $.get(url, { query: query, categories: categories })
            .done(function(data) {
                $container.find(".icon-loader").stop().fadeOut();

                var $ul = $results.find(".list-container");
                $ul.empty();
                $ul.width(data.length * 280);
                $ul.hide();

                $.each(data, function(i, item) {
                    $ul.append("<li>"+item+"</li>");
                });

                $ul.find(".chart-gauge").each(function() {
                    GaugeCharts.make($(this), {
                        bands: OBVIZ.bands,
                        radius: 0.9
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
                        radius: 0.9
                    }).addArrow({
                        value: Number($(this).data("value")),
                        color: "rgb(75, 129, 174)",
                        baseLength: 8
                    });
                })
            });
    }
}
