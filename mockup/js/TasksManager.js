$(function(){

	$( ".uiListColBoard" ).sortable({
		items: ".uiListBoard li",
		stop: handleSort
	});

	function handleSort(){
		// TODO: Update state of task after drag drop
	}
})