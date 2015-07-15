/**
 * Created by gaylor on 02.07.15.
 * Javascript of the Home page
 */
$(document).ready(function() {
    $(".app-slider").slick({
        mobileFirst: true,
        slidesToShow: 1,
        infinite: false,
        swipeToSlide: true,
        respondTo: 'slider',
        responsive: [
            {
                breakpoint: 1200,
                settings: {
                    slidesToShow: 8
                }
            },
            {
                breakpoint: 1000,
                settings: {
                    slidesToShow: 6
                }
            },
            {
                breakpoint: 800,
                settings: {
                    slidesToShow: 5
                }
            },
            {
                breakpoint: 600,
                settings: {
                    slidesToShow: 4
                }
            },
            {
                breakpoint: 400,
                settings: {
                    slidesToShow: 3
                }
            },
            {
                breakpoint: 250,
                settings: {
                    slidesToShow: 1
                }
            }
        ]
    });

    var opts = {
        lines: 12,
        angle: 0,
        lineWidth: 0.15,
        pointer: {
            length: 0.4,
            strokeWidth: 0.05,
            color: '#000000'
        },
        limitMax: 'false',
        percentColors: [[0.0, "#cf1414" ], [0.5, "#aeaeae"], [1.0, "#1c8e17"]],
        strokeColor: '#e0e0e0',
        generateGradient: true
    };

    $(".app-gauge").each(function() {
        var gauge = new Gauge($(this).get(0)).setOptions(opts);
        gauge.maxValue = 100;
        gauge.animationSpeed = 1;
        gauge.set(parseInt($(this).data("value")));
    });

    /** CHECKBOXES **/
    var $checkboxAll = $("#checkbox-all");
    var $checkboxCategory = $(".checkbox-category");

    $checkboxAll.on('change', function() {
        $checkboxCategory.add(OBVIZ.$checkboxes).prop("checked", $(this).prop("checked"));
    });

    $checkboxCategory.on('change', function() {
        $(this).closest(".container-categories")
            .find("input[type='checkbox'][name='category']")
            .prop("checked", $(this).prop("checked"));
    });

    $("#select-categories").on('change', "input[type='checkbox'][name='category']", function() {
        var parent = $(this).closest(".container-categories");
        var mustBeChecked = parent.find("input[type='checkbox'][name='category']").not(":checked").size() == 0;

        parent.find(".checkbox-category").prop("checked", Boolean(mustBeChecked));

        var allMustBeChecked = OBVIZ.$checkboxes.not(":checked").size() == 0;
        $checkboxAll.prop("checked", Boolean(allMustBeChecked));
    });

    $checkboxAll.prop("checked", true).change();

    /** Searching **/
    OBVIZ.search.init();
});

OBVIZ.$homeSearchbar = $("#home-searchbar");
OBVIZ.$searchbar = $(".search-bar").find("input[type='text']");
OBVIZ.$searchResults = $("#search-results");
OBVIZ.$checkboxes = $("input[type='checkbox'][name='category']");
OBVIZ.$selectCategories = $("#select-categories");
OBVIZ.$listApp = $("#list-applications");

OBVIZ.search = {

    init: function() {
        $("#search-button").click(OBVIZ.search.get);

        OBVIZ.$searchResults.find(".back-button button").click(function() {
            OBVIZ.$searchResults.fadeOut(500, function() {
                OBVIZ.$selectCategories.fadeIn(300);
                OBVIZ.$listApp.fadeIn(300);
            });
        });

        $(document).keydown(function(event) {

            if ((event.which || event.keyCode) == 13) {
                $("#search-button").click();
            }
        });
    },

    get: function() {

        // Hide the error message
        OBVIZ.$searchResults.find(".error-message").hide();
        OBVIZ.$searchResults.find(".icon-loader").show();
        // Hide elements
        OBVIZ.$selectCategories.fadeOut(300, function() {
            // Show a loading icon
            OBVIZ.$searchResults.show();
        });
        OBVIZ.$listApp.fadeOut(300);

        var query = OBVIZ.$searchbar.val();
        var url = OBVIZ.$homeSearchbar.data("url");

        // Get the categories
        var categories = [];
        if (OBVIZ.$checkboxes.not(":checked").size() > 0) {
            OBVIZ.$checkboxes.filter(":checked").each(function() {
                categories.push($(this).val());
            });
        }

        // Cancel the previous request
        if (typeof OBVIZ.request !== 'undefined') {
            OBVIZ.request.abort();
        }

        OBVIZ.request = $.get(url, { name: query, categories: categories.join(",") })
            .done(function(data) {
                var $container = OBVIZ.$searchResults.find(".results-container");
                $container.hide();

                // Clean the container
                $container.html("");
                // Add the items
                $.each(data, function(i, item) {
                    $container.append(item);
                });

                // Hide the elements before display the block
                var $items = $container.find(".body-app")
                    .css("opacity", 0)
                    .css("top", 100);
                $container.show();
                OBVIZ.$searchResults.find(".icon-loader").stop().fadeOut();

                // Animate to show the items
                $items.each(function(i) {
                    $(this).delay(i*100).animate({
                        opacity: 1,
                        top: 0
                    });
                });
            })
            .fail(function(xhr, status) {

                if (status != 'abort') {
                    OBVIZ.$searchResults.find(".icon-loader").stop().fadeOut();
                    OBVIZ.$searchResults.find(".error-message").stop().fadeIn();
                }
            });
    }
};
