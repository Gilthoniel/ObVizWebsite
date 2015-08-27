/**
 * Created by gaylor on 08/27/2015.
 */

$(document).ready(function() {

    var table = $("#topics-table").data("table");

    var $filters = $("#form-filters");
    $filters.find(".form-control").on('input change', function() {

        table.column(Number($(this).data("column"))).search($(this).val()).draw();
    });
    $filters.find("#search-type").change();
    $filters.find("button").click(function() {

        $filters.find(".form-control").val("").change();
    });

    table.on('select', function(event, table, type, indexes) {

        var $editor = $("#table-editor");
        var data = table.row(indexes[0]).data();

        if (typeof updateFieldsEditor == 'function') {
            updateFieldsEditor(data, $editor);
        }

        $editor.stop().slideDown(700);
    });

    table.on('deselect', function() {

        $("#table-editor").stop().slideUp(700);
    });

    $("#table-editor").find("button[type='button']").click(function() {
        table.rows().deselect();
        $("#table-editor").stop().slideUp(700);
    });
});
