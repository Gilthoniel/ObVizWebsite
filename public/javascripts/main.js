/**
 * Created by gaylor on 29.06.15.
 * Javascripts for all the pages
 */

var OBVIZ = OBVIZ || {};

/* Gauge's colors */
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

    OBVIZ.$searchbar = $("#search-bar");
    OBVIZ.$searchResults = $("#search-results");
    OBVIZ.$bodyContent = $("#body-content");

    /* Searching */
    OBVIZ.search.init();
    OBVIZ.$searchbar.hover(function() {

        OBVIZ.$searchbar.find(".form-group.hidden-xs").animate({
            width: 200
        }, 200);

        OBVIZ.$searchbar.find(".hidden-xs input[type='text']").focus();

    }, function() {
        // Nothing happens on hover out
    });
    OBVIZ.$searchbar.focusout(function() {

        // Close only if there's no text inside the search bar
        if (OBVIZ.$searchbar.find("input[type='text']").val() == "") {
            OBVIZ.$searchbar.find(".form-group").animate({
                width: 0
            }, 200);
        }
    })
});

/**
 * Functions to execute a search
 * @type {{init: Function, get: Function}}
 */
OBVIZ.search = {

    init: function() {
        OBVIZ.$searchbar.submit(function(event) {
            event.preventDefault();

            // Close the menu
            $("#navbar-obviz").find(".navbar-collpase").removeClass("in");

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

        OBVIZ.hideAppElements(OBVIZ.$searchResults);
    },

    get: function(categories) {

        var query = OBVIZ.$searchbar.find("input[type='text']").val();
        var url = OBVIZ.$searchResults.data("url");

        if (typeof categories === 'undefined') {
            categories = "";
        }

        OBVIZ.get(OBVIZ.$searchResults, url, { query: query, categories: categories });
    },

    close: function() {

        OBVIZ.toggleContainer(OBVIZ.$bodyContent);
    }
};

OBVIZ.get = function($baseContainer, url, params) {
    // Show a loading icon
    $baseContainer.find(".icon-loader").find(".box-loading").removeClass("with-error");
    $baseContainer.find(".icon-loader").slideDown();

    // Display the good container
    if ($baseContainer.is("#search-results")) {
        OBVIZ.toggleContainer(OBVIZ.$searchResults);
    } else {
        OBVIZ.toggleContainer(OBVIZ.$bodyContent);
    }

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
                if (i % 3 == 0) {
                    $container.append('<div class="hidden-xs hidden-sm clearfix"></div>');
                }

                if (i % 2 == 0) {
                    $container.append('<div class="visible-sm-block clearfix"></div>');
                }

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
                        "color": "rgb(75, 129, 174)",
                        "startWidth": 10,
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
        .fail(function(xhr) {

            if (xhr.status == '400') {
                $baseContainer.find(".icon-loader").find(".box-loading").addClass("with-error");
            }
        });
};

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

OBVIZ.hideAppElements = function($container) {

    $container.on('mouseenter', '.header', function() {
        $(this).find(".opinion-indicator").finish().fadeOut(200);
    });

    $container.on('mouseleave', '.header', function() {
        $(this).find(".opinion-indicator").finish().fadeIn(200);
    });
};
