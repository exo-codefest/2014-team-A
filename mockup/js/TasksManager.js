$(function(){

	$(".dragdropContainer").sortable({
		items: ".uiListBoard li",
		stop: stopDragRow
	});

	$(".uiHeadColBoard").mousedown(function(){

		$(".uiListColBoard").sortable({
			items: ".uiColBoard",
			cursor: "move",
			stop: stopDragCol
		});
	    $(".uiListColBoard").sortable( "option", "disabled", false );
	    $( ".uiListColBoard" ).disableSelection();
	});

	function stopDragRow(){
		// TODO: Update state of task after drag drop
	}

	function stopDragCol(){
		$(".uiListColBoard").sortable("disable");
	}
});