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
            width: "20%"
        }]
    });

    table.on('select', function(event, table, type, indexes) {

        var $editor = $("#table-editor");
        var data = table.row(indexes[0]).data();
        console.log(data);

        $editor.find("input[name='id']").val(data[0]);
        $editor.find("input[name='type']").val(data[1]);
        $editor.find("input[name='title']").val(data[2].replace(/&amp;/g, "&"));
        $editor.find("input[name='keys']").val(data[3]);

        $editor.stop().slideDown(700);
    });

    table.on('deselect', function() {

        $("#table-editor").stop().slideUp(700);
    });
});
