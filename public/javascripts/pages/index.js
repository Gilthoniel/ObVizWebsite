/**
 * Created by gaylor on 02.07.15.
 * Javascript of the Home page
 */
$(document).ready(function() {

    /* Trending applications */
    OBVIZ.trending.init();
});

OBVIZ.$trending = $("#trending");

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

        OBVIZ.hideAppElements(OBVIZ.$trending);

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
