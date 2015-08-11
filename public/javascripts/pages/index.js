/**
 * Created by gaylor on 02.07.15.
 * Javascript of the Home page
 */
$(document).ready(function() {

    /* Searching */
    OBVIZ.search.init();

    /* Trending applications */
    OBVIZ.trending.init();
});

OBVIZ.$homeSearchbar = $("#home-searchbar");
OBVIZ.$searchbar = $(".search-bar").find("input[type='text']");
OBVIZ.$searchResults = $("#search-results");
OBVIZ.$trending = $("#trending");

/**
 * Functions to execute a search
 * @type {{init: Function, get: Function}}
 */
OBVIZ.search = {

    init: function() {
        $("#search-button").click(function() {

            var $categories = OBVIZ.$searchResults.find(".list-categories li");
            $categories.removeClass("active");
            $categories.first().addClass("active");

            OBVIZ.search.get();
        });

        OBVIZ.$searchResults.find(".list-categories").on('click', 'li', function() {

            var $categories = OBVIZ.$searchResults.find(".list-categories li");
            $categories.removeClass("active");
            $(this).addClass("active");

            OBVIZ.search.get($(this).data("categories"));
        });

        $(document).keydown(function(event) {

            if ((event.which || event.keyCode) == 13) {
                $("#search-button").click();
            }
        });
    },

    get: function(categories) {

        var query = OBVIZ.$searchbar.val();
        var url = OBVIZ.$homeSearchbar.data("url");

        if (typeof categories === 'undefined') {
            categories = "";
        }

        OBVIZ.get(OBVIZ.$searchResults, url, { query: query, categories: categories });
    },

    close: function() {

        OBVIZ.toggleContainer(OBVIZ.$trending);
    }
};

/**
 * Functions to get the list of trending applications related to categories
 * @type {{init: Function, get: Function}}
 */
OBVIZ.trending = {

    init: function() {

        var $categories = OBVIZ.$trending.find(".list-categories li");
        OBVIZ.$trending.find(".list-categories").on('click', 'li', function() {

            $categories.removeClass("active");
            $(this).addClass("active");

            OBVIZ.trending.get($(this).data("categories"));
        });

        $categories.first().addClass("active");
        this.get();
    },

    get: function(categories) {

        var url = OBVIZ.$trending.data("url");

        if (typeof categories === 'undefined') {
            categories = "";
        }

        OBVIZ.get(OBVIZ.$trending, url, { categories: categories });
    }
};

OBVIZ.get = function($baseContainer, url, params) {
    // Hide the error message
    $baseContainer.find(".error-message").hide();
    // Show a loading icon
    $baseContainer.find(".icon-loader").slideDown();

    // Display the good container
    OBVIZ.toggleContainer($baseContainer);

    // Cancel the previous request
    if (typeof OBVIZ.request !== 'undefined') {
        OBVIZ.request.abort();
    }

    OBVIZ.request = $.get(url, params)
        .done(function(data) {
            var $container = $baseContainer.find(".results-container");
            $container.hide();

            // Clean the container
            $container.empty();
            // Add the items
            $.each(data, function(i, item) {
                $container.append(item);
            });

            // Hide the elements before display the block
            var $items = $container.find(".body-app")
                .css("opacity", 0)
                .css("top", 100);
            $container.show();
            $baseContainer.find(".icon-loader").stop().slideUp();

            // Init the gauges
            $items.find(".chart-gauge").each(function() {
                var $element = $(this);
                var chartID = $element.attr("id");
                $element.data("gauge", AmCharts.makeChart(chartID, {
                    "addClassNames": true,
                    "type": "gauge",
                    "theme": "light",
                    "axes": [ {
                        "axisThickness": 0,
                        "axisAlpha": 0.0,
                        "tickAlpha": 0.0,
                        "valueInterval": 100,
                        "showFirstLabel": false,
                        "showLastLabel": false,
                        "bands": OBVIZ.bands,
                        "bottomText": $element.data("title"),
                        "bottomTextYOffset": 20,
                        "endValue": 100
                    } ],
                    "arrows": [ {
                        "value": Number($element.data("value")),
                        "color": "#505050",
                        "startWidth": 5,
                        "radius": "100%",
                        "innerRadius": "30%",
                        "borderAlpha": 1,
                        "id": "0"
                    } ],
                    "export": {
                        "enabled": true
                    },
                    "panEventsEnabled": false
                }));
            });

            // Animate to show the items
            $items.each(function(i) {
                $(this).delay(i * 50).animate({
                    opacity: 1,
                    top: 0
                });
            });
        })
        .fail(function(xhr, status) {

            if (status != 'abort') {
                $baseContainer.find(".icon-loader").stop().fadeOut();
                $baseContainer.find(".error-message").stop().fadeIn();
            }
        });
};

OBVIZ.toggleContainer = function ($container) {

    if ($container.is("#trending")) {

        OBVIZ.$searchResults.fadeOut(300, function() {
            OBVIZ.$trending.fadeIn(300);
        });

    } else {

        OBVIZ.$trending.fadeOut(300, function() {
            OBVIZ.$searchResults.show();
        });

    }
};
