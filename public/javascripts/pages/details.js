/**
 * Created by gaylor on 03.07.15.
 * Javascript of the page where you visualize the information about one app
 */

/**
 *
 * Elements
 */
OBVIZ.$headerCarousel = $("#header-carousel");
OBVIZ.$gauges = $(".chart-gauge");
OBVIZ.$reviews = $("#details-reviews");
OBVIZ.$baseContainer = $("#base-container");
OBVIZ.$comparedContainer = $("#comparison-container");
OBVIZ.$data = $("#data");
OBVIZ.$topics = $("#details-aspects");
OBVIZ.$pagers = $(".pager");

OBVIZ.NUMBER_OF_REVIEWS = 24;

OBVIZ.carousel = {

    init: function() {
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

        var $slickAlternatives = $(".slick-alternatives");
        $slickAlternatives.slick({
            infinite: false,
            arrows: true,
            dots: true,
            variableWidth: true,
            swipeToSlide: true
        });
        $slickAlternatives.on('click', '.alternative-body', OBVIZ.comparison.start);
    }
};

OBVIZ.comparison = {

    init: function() {

        OBVIZ.$comparedContainer.find(".title-app").mouseenter(function() {
            OBVIZ.gauges.hoverIn(0);
        });

        OBVIZ.$comparedContainer.find(".title-comparison").mouseenter(function() {
            OBVIZ.gauges.hoverIn(1);
        });
    },

    start: function() {
        // This function must be called with a click event
        var $slide = $(this);
        if (typeof $slide === 'undefined') {
            return;
        }

        if (typeof $(this).data("url") !== 'undefined') {

            window.location = $(this).data("url");
        }

        // Clean the containers
        OBVIZ.$comparedContainer.find(".compared").find(".topic-container").empty();
        // Swap the container if necessary
        OBVIZ.$baseContainer.fadeOut(200, function() {

            OBVIZ.$comparedContainer.fadeIn(200);
        });

        // Add the second arrow to each gauge
        OBVIZ.$gauges.each(function() {

            var id = -1;
            var arrow = {
                color: "rgb(79, 187, 222)",
                baseLength: 10,
                radius: 1.0,
                innerRadius: 0.4
            };

            if ($(this).parent().is(".opinion-box")) {

                // Topic gauge
                id = $(this).parent().data("topic");
            } else {
                // Global gauge
                id = "0";
                arrow.baseLength = 20;
            }

            arrow.value = OBVIZ.$data.find("div[data-id='"+$slide.data("id")+"']").find("div[data-topic='"+id+"']").data("value");
            if (typeof arrow.value === 'undefined') {
                // TODO : if the compared app has no data for this topic, what to do ?
                console.log("Fail for id "+id);
            } else {
                OBVIZ.gauges.addArrow($(this), arrow);
            }
        });

        // Store the id of the compared app
        OBVIZ.$reviews.data("compared", $(this).data("id"));

        // Swap the good panes
        $("#tab-description")
            .find(".app-description").hide()
            .filter("[data-id='" + $(this).data("id") + "']").show();

        $("#tab-screenshots")
            .find(".app-screenshot").hide()
            .filter("[data-id='" + $(this).data("id") + "']").show();

        $("#comparison-container")
            .find(".app-title").hide()
            .filter("[data-id='" + $(this).data("id") + "']").show();

        // Click again on the active topic
        OBVIZ.$topics.find(".opinion-box.active").click();

        $("html, body").animate({
            scrollTop: $("#anchor-comparison").offset().top - 60
        });
    },

    stop: function() {

        // Clean the arrows
        OBVIZ.$gauges.each(function() {
            $(this).data("gauge").cleanArrow();
        });

        OBVIZ.$reviews.data("compared", undefined);

        // Swap the containers
        OBVIZ.$comparedContainer.fadeOut(200, function() {

            OBVIZ.$baseContainer.fadeIn(200);
        });
    }
};

OBVIZ.gauges = {

    init: function() {

        // Init the gauges element and add store the object in the field gauge of the data
        OBVIZ.$gauges.each(function() {
            OBVIZ.gauges.makeGauge($(this));
        });

        // Put the height as the same than the width for the canvas
        $(window).resize(function() {
            OBVIZ.$gauges.each(function() {
                var width = $(this).width();
                $(this).height(width);
            });
        });
        $(window).resize();
    },

    makeGauge: function(element) {

        var text = {
            value: element.data("title"),
            position: 0.9,
            font: "14px Dosis"
        };

        var arrow = {
            value: Number(element.data("value")),
            color: "#505050",
            baseLength: 10,
            radius: 1.0,
            innerRadius: 0.4
        };

        if (element.is("#global-gauge")) {
            text.position = 0.8;
            text.font = "40px Dosis";

            arrow.baseLength = 20;
        }

        element.data("gauge", GaugeCharts.make(element, {
            bands: OBVIZ.bands,
            radius: 0.9,
            text: text
        }));
        element.data("gauge").addArrow(arrow);
    },

    addArrow: function(gauge, arrow) {
        var chart = gauge.data("gauge");

        if (typeof chart !== 'undefined') {

            chart.cleanArrow();
            chart.addArrow(arrow);
        }
    },

    hoverIn: function(index) {

        if (typeof index === 'undefined') {
            index = 0;
        }

        OBVIZ.$gauges.each(function() {

            $(this).data("gauge").shakeArrow(index);
        });
    }
};

OBVIZ.reviews = {

    init: function() {

        OBVIZ.$topics.find(".list-aspects").on('click', '.opinion-box', function() {

            OBVIZ.$topics.find(".opinion-box").removeClass("active");
            $(this).addClass("active");

            var topicID = $(this).data("topic");
            OBVIZ.$reviews.data("topic", topicID);
            OBVIZ.$baseContainer.find(".topic-container:visible").fadeOut(200);
            OBVIZ.$baseContainer.find(".topic-container[data-topic='" + topicID + "']").delay(200).fadeIn(200);
            OBVIZ.$baseContainer.data("page", 0);

            OBVIZ.$comparedContainer.find(".topic-container:visible").fadeOut(200);
            OBVIZ.$comparedContainer.find(".topic-container[data-topic='" + topicID + "']").delay(200).fadeIn(200);
            OBVIZ.$comparedContainer.data("page", 0);

            // Load the reviews for the compared app only if we have an ID
            if (typeof OBVIZ.$reviews.data("compared") !== 'undefined') {

                var comparedID = OBVIZ.$reviews.data("compared");
                OBVIZ.reviews.get(topicID, comparedID, 0, false);
            }

            var appID = OBVIZ.$data.find("div[data-main='true']").data("id");
            OBVIZ.reviews.get(topicID, appID, 0, true);

            // Display the reviews tab
            $("#tab-link-reviews").click();
        });

        OBVIZ.$pagers.on('click', 'a', function(event) {
            event.preventDefault();

            if ($(this).parent().is(".disabled") || $(this).parent().is(".active")) {
                return;
            }

            var page = 0;
            var topicID = OBVIZ.$reviews.data("topic");

            // Set the page number
            if ($(this).is(".previous") || $(this).is(".next")) {
                page = $(this).is(".previous") ? -1 : 1;

                if (typeof OBVIZ.$reviews.data("compared") !== 'undefined') {
                    page += Number(OBVIZ.$comparedContainer.data("page"));
                } else {
                    page += Number(OBVIZ.$baseContainer.data("page"));
                }
            } else if (typeof $(this).data("page") !== 'undefined') {

                page = $(this).data("page");
            }

            // Check if we are in compare mode
            if (typeof OBVIZ.$reviews.data("compared") !== 'undefined') {
                var comparedID = OBVIZ.$reviews.data("compared");
                OBVIZ.reviews.get(topicID, comparedID, page, false);
            }

            // Get the reviews of the main application of the page
            var appID = OBVIZ.$data.find("div[data-main='true']").data("id");
            OBVIZ.reviews.get(topicID, appID, page, true);

            OBVIZ.$comparedContainer.data("page", page);
            OBVIZ.$baseContainer.data("page", page);
        });
    },

    get: function(topicID, appID, page, mainApp) {
        var $containers;

        if (mainApp) {

            $containers = OBVIZ.$baseContainer.add(OBVIZ.$comparedContainer.find(".current")).find("div[data-topic='"+topicID+"']");
        } else {

            $containers = OBVIZ.$comparedContainer.find(".compared").find("div[data-topic='"+topicID+"']");
        }

        var order = OBVIZ.filters.getOrder();

        // Show the loading image
        $containers.empty();
        $containers.parent().find(".loading-message").find(".box-loading").removeClass("with-error");
        $containers.parent().find(".loading-message").fadeIn();

        var url = OBVIZ.$reviews.data("url");

        $.get(url, { id: appID, t: topicID, p: page, s: OBVIZ.NUMBER_OF_REVIEWS, order: order })
            .done(function(data) {

                $containers.parent().find(".loading-message").hide(); // Hide the loading icon
                OBVIZ.$pagers.show();

                $.each(data.reviews, function(i, review) {
                    $containers.each(function() {
                        var columns = "col-xs-12";
                        if ($(this).parent().is("#base-container")) {
                            if (i % 2 == 0) {
                                $(this).append('<div class="clearfix"></div>');
                            }

                            columns += " col-lg-6";
                        }

                        $(this).append('<div class="'+columns+'">'+review+'</div>');
                    });
                });

                if (mainApp) {
                    rebuildPager(page, data.nbPage);
                }

            }).fail(function(xhr) {

                if (xhr.status == '400') {
                    // Hide the loading icon and display an error message
                    $containers.parent().find(".loading-message").find(".box-loading").addClass("with-error");
                }
            });
    }
};

OBVIZ.rotate = function($elems, offset, cx, cy) {
    $({ deg: 0 }).animate({ deg: offset }, {
        duration: 200,
        step: function(now) {
            $elems.attr('transform', 'rotate(' + now + ' '+cx+' '+cy+')');
        },
        done: function() {
            $({ deg: offset }).animate({ deg: -offset }, {
                duration: 200,
                step: function(now) {
                    $elems.attr('transform', 'rotate(' + now + ' '+cx+' '+cy+')');
                },
                done: function() {
                    $({ deg: -offset }).animate({ deg: 0 }, {
                        duration: 200,
                        step: function(now) {
                            $elems.attr('transform', 'rotate(' + now + ' '+cx+' '+cy+')');
                        }
                    });
                }
            });
        }
    });
};

$(document).ready(function() {

    OBVIZ.gauges.init();
    OBVIZ.carousel.init();
    OBVIZ.reviews.init();
    OBVIZ.comparison.init();

    var $aspects = OBVIZ.$topics.find(".list-aspects");
    $aspects.find("ul").each(function() {
        $(this).css("width", ($(this).find("li").width()+20) * $(this).find("li").size() + "px");
    });
    $aspects.mCustomScrollbar({
        axis: "x",
        theme: 'dark',
        mouseWheel: {
            enable: false
        },
        scrollButtons: {
            enable: true
        }
    });

    /** Filters for the reviews **/
    OBVIZ.filters = (function() {
        var $selects = $(".selection-filter");
        $selects.on('change', function() {
            $selects.val($(this).val());

            var page = 0;
            var topicID = OBVIZ.$reviews.data("topic");

            // Check if we are in compare mode
            if (typeof OBVIZ.$reviews.data("compared") !== 'undefined') {
                var comparedID = OBVIZ.$reviews.data("compared");
                OBVIZ.reviews.get(topicID, comparedID, page, false);
            }

            // Get the reviews of the main application of the page
            var appID = OBVIZ.$data.find("div[data-main='true']").data("id");
            OBVIZ.reviews.get(topicID, appID, page, true);

            OBVIZ.$comparedContainer.data("page", page);
            OBVIZ.$baseContainer.data("page", page);
        });

        return {
            getOrder: function() {
                return $selects.val();
            }
        }
    })();

    OBVIZ.$topics.find(".opinion-box").get(1).click();
});

/** Private functions **/

function rebuildPager(page, totalPage) {

    var $element = OBVIZ.$pagers.first();

    $element.empty();

    if (totalPage > 1) {
        if (page == 0) {
            $element.append('<li class="disabled"><a class="previous" href="#">Previous</a></li>');
        } else {
            $element.append('<li><a class="previous" href="#">Previous</a></li>');
        }
    }

    if (totalPage > 10) {

        var offset = 3;
        if (page - offset > 0) {

            appendPage(false, 0, $element);
            $element.append('<li class="separator">&bull;&bull;&bull;</li>');

        } else {

            for(var j = 0; j <= offset + 1; j++) {

                appendPage(j == page, j, $element);
            }

        }

        if (page - offset > 0 && page + offset < totalPage) {

            for (var l = page - offset; l <= page + offset; l++) {
                appendPage(l == page, l, $element);
            }
        }

        if (page + offset < totalPage) {
            $element.append('<li class="separator">&bull;&bull;&bull;</li>');
            appendPage(false, totalPage - 1, $element);
        } else {
            for(var k = totalPage - offset - 1; k < totalPage; k++) {

                appendPage(k == page, k, $element);
            }
        }
    } else {

        for(var i = 0; i < totalPage; i++) {

            appendPage(i == page, i, $element);
        }
    }

    if (totalPage > 1) {
        if (page >= totalPage - 1) {
            $element.append('<li class="disabled"><a class="next" href="#">Next</a></li>');
        } else {
            $element.append('<li><a class="next" href="#">Next</a></li>');
        }
    }

    OBVIZ.$pagers.each(function() {
        $(this).html($element.html());
    });
}

function appendPage(isActive, page, element) {

    var active = isActive ? "active" : "";
    element.append('<li class="' + active + '"><a data-page="' + page + '" href="#">' + (page + 1) + '</a></li>');
}