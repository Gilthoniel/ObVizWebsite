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
