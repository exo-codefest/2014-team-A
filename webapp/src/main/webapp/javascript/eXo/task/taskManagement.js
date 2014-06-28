//(function($){
//$(document).ready(function(){
//   $("li.add > a:first").click(function(){	   
//	addLink = $(this);
//	addLink.hide();
//	editForm = $(this).siblings("div.toggleQuickEdit");
//	editForm.show();
//	$("div.quickEdit > div.actionIcon",editForm).click(function(){
//		addLink.show();
//		editForm.hide();
//	});
//   });   
//});
//
//function TaskManager() {
//	
//}
//
//taskManager = new TaskManager();
//
//})(jq);


(function(gj) {
	function TaskManagement() {
		this.ws = "collaboration";
	};
	
	TaskManagement.prototype.PROCESS = "Process";

	TaskManagement.prototype.alert = function(msg) {
		alert("hehe");
		alert(gj.ui.sortable);
	};
	
	//---------------All setter methods---------------------//
	if (!eXo.task) eXo.task = {};
	eXo.task.TaskManagement = new TaskManagement();
	
	return eXo.task.TaskManagement;
	//-------------------------------------------------------------------------//
})(gj);

