/**
 * Created by ManerFan on 2016/6/16 0016.
 */

require([
    "jquery",
    "plax"
], function ($) {
    // Plaxify all `js-plaxify` element layers
    var layers = $('.js-plaxify')

    $.each(layers, function (index, layer) {
        $(layer).plaxify({
            xRange: $(layer).data('xrange') || 0,
            yRange: $(layer).data('yrange') || 0,
            invert: $(layer).data('invert') || false
        })
    });

    $.plax.enable();

    $("input[name='kw']").focus();
});
