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


(function($) {
	if (!eXo.task) eXo.task = {};	
	function TaskManagement() {
		this.ws = "collaboration";
		this.portalName = eXo.env.portal.portalName;
		this.context = eXo.env.portal.context;
		this.restContext = eXo.env.portal.context + "/" + eXo.env.portal.rest;
	};
	
	TaskManagement.prototype.PROCESS = "Process";

	TaskManagement.prototype.init = function(projectId) {
		eXo.task.TaskManagement.projectId = projectId;
		eXo.task.TaskManagement.initDnD();
		eXo.task.TaskManagement.initAddTask();
	}
	
	TaskManagement.prototype.initDnD = function() {
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

		function stopDragRow(event, ui){
			// TODO: Update state of task after drag drop
			var taskName = $("span", ui.item)[0].innerHTML;
			var taskId = ui.item.attr("taskId");
			var stage = $($(ui.item).parents(".uiColBoard:first")).attr("stage");
		    var uri = eXo.task.TaskManagement.restContext + "/taskManager/task/changeStage?" +
  			  "taskId=" + taskId +
  			  "&stage=" + stage;
		    
			$.ajax({url: uri,
				   success: function(result, status, xhr) {
				     location.reload();
			       },
			       error: function() {
		    	     location.reload();
				   }
			       }
		   	);
		}

		function stopDragCol(){
			$(".uiListColBoard").sortable("disable");
		}
	}
	
	TaskManagement.prototype.initAddTask = function() {
	   $("li.add").each(function() {
		   $(this).removeClass("ui-sortable-handle");
	   });
	   $("li.add > a").click(function(){	   
			var addLink = $(this);
			addLink.hide();
			editForm = $(this).siblings("div.toggleQuickEdit");
			editForm.show();
			editForm.addClass("edit")
			$("div.quickEdit > div.actionIcon, div.quickEdit > button",editForm).click(function(){
				addLink.show();
				editForm.hide();
			});
			$("div.quickEdit > button",editForm).click(function(){
				var taskName = $(this).siblings("textarea").val();
				var stage = $(this).parent().attr("stage");
  			    var uri = eXo.task.TaskManagement.restContext + "/taskManager/task/addTask?" +
			  			  "projectId=" + eXo.task.TaskManagement.projectId +
			  			  "&taskName=" + taskName +
			  			  "&stage=" + stage;
				$.ajax({url: uri,
						   success: function(result, status, xhr) {
						     location.reload();
	 				       },
					       error: function() {
				    	     location.reload();
						   }
 				       }
			   	);
			});
	   });
	}
	
	//---------------All setter methods---------------------//
	if (!eXo.task) eXo.task = {};
	eXo.task.TaskManagement = new TaskManagement();
	
	return eXo.task.TaskManagement;
	//-------------------------------------------------------------------------//
})(gj);

