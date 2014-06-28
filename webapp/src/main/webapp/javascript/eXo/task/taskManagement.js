
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
		eXo.task.TaskManagement.initQuickEdit();
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

		function stopDragRow(){
			// TODO: Update state of task after drag drop
		}

		function stopDragCol(){
			$(".uiListColBoard").sortable("disable");
		}
	}
	
	TaskManagement.prototype.initAddTask = function() {
	   $("li.add > a:first").click(function(){	   
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
	
	TaskManagement.prototype.initQuickEdit = function(){
		var taskForm = $(".UITaskForm");
		taskForm.delegate(".toggleQuickEdit","click",function(){
			$( this ).addClass("edit");			
		});
		taskForm.delegate("div.quickEdit > button","click",function(){			
			var name = $("input#name").val();
			var description = $("input#description").val();
			var stage = $("b#stage").html();
			var duedate = $("input#duedate").val();
			var uri = eXo.task.TaskManagement.restContext + "/taskManager/task?" +
			  "projectId=" + eXo.task.TaskManagement.projectId +
			  "&name=" + name +
			  "&description=" + description +
			  "&stage=" + stage;
			$.ajax({url: uri,
					   success: function(result, status, xhr) {
						 $( this ).parents(".toggleQuickEdit").removeClass("edit");
				       },
				       error: function(e) {
			    	     		alert("Error: "+e);
				 	}
			       }
		 	);
		});	
		taskForm.delegate("div.quickEdit > .uiIconRemove","click",function(){
			$( this ).parents(".toggleQuickEdit").removeClass("edit");
		});	
	}
	
	//---------------All setter methods---------------------//
	if (!eXo.task) eXo.task = {};
	eXo.task.TaskManagement = new TaskManagement();
	
	return eXo.task.TaskManagement;
	//-------------------------------------------------------------------------//
})(gj);
