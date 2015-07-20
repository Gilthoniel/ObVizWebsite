/**
 * Created by gaylor on 02.07.15.
 * Javascript of the Home page
 */
$(document).ready(function() {

    /** Categories **/
    OBVIZ.$categoriesElements = OBVIZ.$searchCategories.find("li");
    OBVIZ.$categoriesElements
        .click(function() {

            if ($(this).is(".active")) {
                $(this).removeClass("active");
            } else {
                OBVIZ.$categoriesElements.removeClass("active");
                $(this).addClass("active");
            }
        })
        .hover(function() {

            OBVIZ.$categoriesDesc.find("li[data-title='"+$(this).data("title")+"']").show();
        }, function() {

            OBVIZ.$categoriesDesc.find("li").hide();
        });

    /** Searching **/
    OBVIZ.search.init();
});

OBVIZ.$homeSearchbar = $("#home-searchbar");
OBVIZ.$searchbar = $(".search-bar").find("input[type='text']");
OBVIZ.$searchResults = $("#search-results");
OBVIZ.$listApp = $("#list-applications");
OBVIZ.$searchCategories = $("#search-categories");
OBVIZ.$categoriesDesc = $("#categories-desc");

OBVIZ.search = {

    init: function() {
        $("#search-button").click(OBVIZ.search.get);

        $(document).keydown(function(event) {

            if ((event.which || event.keyCode) == 13) {
                $("#search-button").click();
            }
        });
    },

    get: function() {

        // Hide the error message
        OBVIZ.$searchResults.find(".error-message").hide();
        // Show a loading icon
        OBVIZ.$searchResults.find(".icon-loader").show();
        OBVIZ.$searchResults.show();
        OBVIZ.$listApp.fadeOut(300);

        var query = OBVIZ.$searchbar.val();
        var url = OBVIZ.$homeSearchbar.data("url");

        // Get the categories
        var categories = OBVIZ.$searchCategories.find("li.active").data("categories");
        if (typeof categories === 'undefined') {
            categories = "";
        }

        // Cancel the previous request
        if (typeof OBVIZ.request !== 'undefined') {
            OBVIZ.request.abort();
        }

        OBVIZ.request = $.get(url, { name: query, categories: categories })
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
