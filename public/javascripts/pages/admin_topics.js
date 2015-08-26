/**
 * Created by gaylor on 08/25/2015.
 * Management for the topics page
 */

$(document).ready(function() {

    var table = $("#topics-table").removeAttr('width').DataTable({
        ordering: true,
        select: 'single',
        columnDefs: [{
            targets: 2,
            width: "10%"
        }]
    });

    var $filters = $("#form-filters");
    $filters.find(".form-control").on('input change', function() {

        table.column(Number($(this).data("column"))).search($(this).val()).draw();
    });
    $filters.find("button").click(function() {

        $filters.find(".form-control").val("").change();
    });

    table.on('select', function(event, table, type, indexes) {

        var $editor = $("#table-editor");
        var data = table.row(indexes[0]).data();
        console.log(data);

        $editor.find("input[name='id']").val(data[0]);
        $editor.find("select[name='type']").val(data[1]);
        $editor.find("input[name='title']").val(data[2].replace(/&amp;/g, "&"));
        $editor.find("input[name='category']").val(data[3]).prop("disabled", !(data[1] == 'CATEGORY'));
        $editor.find("input[name='app']").val(data[4]).prop("disabled", !(data[1] == 'SPECIFIC'));
        $editor.find("textarea[name='keys']").val(data[5]);

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
