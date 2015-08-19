$(".emailButton").popover();
$('.emailButton').on('show.bs.popover', function () {
	$(this).attr("data-content", $(this).attr("data-content").replace(/_-/g, ""));
})