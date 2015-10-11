/**
 * Created by gaylor on 09/08/2015.
 * Gauge chart for ObViz project
 */

GaugeCharts = {

    make: function($element, options) {

        var chart = new GaugeChart($element, options);

        $(window).resize(function() {
            if ($element.width() != chart.$canvas.attr("width")) {
                GaugeCharts.draw(chart);
            }
        });

        return chart;
    },

    animate: function(chart) {

        var delay = 0;
        $.each(chart.arrows, function(i, arrow) {
            delay = Math.max(delay, Number(arrow.delay));
        });

        $({interpolation: 0})
            .delay(delay)
            .animate({interpolation: 1}, {
                duration: 1500,
                step: function(now) {
                    $.each(chart.arrows, function(i, arrow) {

                        if (!arrow.isAnimating) {
                            arrow.animationValue = arrow.value * now;
                        }
                    });

                    GaugeCharts.draw(chart);
                }
            });
    },

    shake: function(chart, index) {

        var arrow = chart.arrows[index];
        if (typeof arrow === 'undefined' || arrow.isAnimating) {
            return;
        } else {
            arrow.isAnimating = true;
        }

        var shakeValue = 5;

        $({interpolation: 0}).animate({interpolation: 2}, {
            duration: 1000,
            step: function(now) {

                if (now < 0.5) {
                    arrow.animationValue = arrow.value + now * 2 * shakeValue;
                } else if (now < 1.5) {
                    arrow.animationValue = arrow.value + shakeValue - (now - 0.5) * 2 * shakeValue;
                } else {
                    arrow.animationValue = arrow.value - shakeValue + (now - 1.5) * 2 * shakeValue;
                }

                GaugeCharts.draw(chart);
            },
            complete: function() {
                arrow.isAnimating = false;
            }
        });
    },

    draw: function(chart) {

        // Set the dimensions
        chart.$canvas.attr("width", chart.$element.width());
        chart.$canvas.attr("height", chart.$element.height());

        var center = {
            x: chart.$element.width() / 2.0,
            y: chart.$element.height() / 2.0
        };

        var ctx = chart.$canvas[0].getContext("2d");
        ctx.clearRect(0, 0, chart.$element.width(), chart.$element.height());
        var zeroAngle = Math.PI * 0.5 + (2 * Math.PI - chart.options.angle) / 2.0;
        var gaugeRadius = chart.options.radius * Math.min(center.x, center.y);

        // Draw the bands
        $.each(chart.options.bands, function(i, band) {

            if (typeof band.color === 'undefined') {
                band.color = "black";
            }

            if (typeof band.radius === 'undefined') {
                band.radius = 0.8;
            }

            if (typeof band.start === 'undefined') {
                band.start = 0;
            }

            if (typeof band.end === 'undefined') {
                band.end = chart.options.max;
            }

            var startAngle = zeroAngle + band.start * chart.options.angle / chart.options.max;
            var endAngle = zeroAngle + band.end * chart.options.angle / chart.options.max;

            ctx.save();

            ctx.beginPath();
            ctx.arc(center.x, center.y, gaugeRadius, startAngle, endAngle);
            ctx.arc(center.x, center.y, gaugeRadius * band.radius, endAngle, startAngle, true);
            ctx.closePath();

            ctx.globalAlpha = 0.8;
            ctx.fillStyle = band.color;
            ctx.fill();
            ctx.strokeStyle = band.color;
            ctx.stroke();

            ctx.restore();
        });

        // Draw the text
        if (typeof chart.options.text !== 'undefined' && typeof chart.options.text.value !== 'undefined') {

            var text = chart.options.text;
            if (typeof text.color === 'undefined') {
                text.color = "black";
            }

            if (typeof text.position === 'undefined') {
                text.position = 0.5;
            }

            if (typeof text.font === 'undefined') {
                text.font = "14px sans-serif";
            }

            ctx.save();

            ctx.textAlign = "center";
            ctx.textBaseline = "bottom";
            ctx.font = text.font;
            ctx.fillStyle = text.color;
            ctx.fillText(text.value, center.x, center.y + center.y * text.position);

            ctx.restore();
        }

        // Draw the arrows
        $.each(chart.arrows, function(i, arrow) {

            if (typeof arrow.value === 'undefined') {
                arrow.value = 0;
            }

            if (typeof arrow.color === 'undefined') {
                arrow.color = "black";
            }

            if (typeof arrow.baseLength === 'undefined') {
                arrow.baseLength = 10;
            }

            if (typeof arrow.radius === 'undefined') {
                arrow.radius = 1.0;
            }

            if (typeof arrow.innerRadius === 'undefined') {
                arrow.innerRadius = 0.2;
            }

            var rotation = (2 * Math.PI - chart.options.angle) / 2 + chart.options.angle * arrow.animationValue / chart.options.max;
            var innerPoint = gaugeRadius * arrow.innerRadius;

            ctx.save();

            ctx.translate(center.x, center.y);
            ctx.rotate(rotation);

            ctx.beginPath();
            ctx.moveTo(-arrow.baseLength / 2.0, innerPoint);
            ctx.lineTo(arrow.baseLength / 2.0, innerPoint);
            ctx.lineTo(0, gaugeRadius * arrow.radius);
            ctx.closePath();

            ctx.globalAlpha = 0.7;
            ctx.fillStyle = arrow.color;
            ctx.fill();
            if (typeof arrow.strokeColor !== 'undefined') {
                ctx.strokeStyle = arrow.strokeColor;
            } else {
                ctx.strokeStyle = arrow.color;
            }
            ctx.globalAlpha = 0.9;
            ctx.stroke();

            ctx.restore();
        });
    }
};

function GaugeChart($element, options) {
    this.$element = $element;

    // Create the canvas
    this.$element.append('<canvas></canvas>');
    this.$canvas = $element.find("canvas");

    // Define options
    if (typeof options === 'undefined') {
        options = {};
    }

    if (typeof options.angle === 'undefined') {
        options.angle = Math.PI * (4.0/3.0);
    } else {
        options.angle = options.angle * Math.PI / 180;
    }

    if (typeof options.radius === 'undefined') {
        options.radius = 1.0;
    }

    if (typeof options.max === 'undefined') {
        options.max = 100;
    }

    if (typeof options.bands === 'undefined') {
        options.bands = [{}];
    }

    this.options = options;

    this.arrows = [];

    this.addArrow = function(arrow) {
        arrow.animationValue = 0;
        arrow.isAnimating = false;

        this.arrows.push(arrow);
        GaugeCharts.animate(this);
    };

    this.cleanArrow = function(index) {

        if (typeof index === 'number') {
            if (this.arrows.length > index) {
                this.arrows.splice(index, 1);
            }
        } else {
            this.arrows = [];
        }
        GaugeCharts.draw(this);
    };

    this.shakeArrow = function(index) {
        GaugeCharts.shake(this, index);
    };

    this.setText = function(text) {
        this.options.text.value = text;
        GaugeCharts.draw(this);
    };

    GaugeCharts.draw(this);
}