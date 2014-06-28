
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
		eXo.task.TaskManagement.initAdminBar();
	}
	
	TaskManagement.prototype.initAdminBar = function() {
		$(".toggleShowSidebar").click(function(){
			var container = $(this).parent();
			if (container.hasClass("open")) container.removeClass("open");
			else container.addClass("open");
		});
		
		$("#searchMember").click(function(){
			eXo.task.TaskManagement.searchUser();
		});
		
		$("#memberKeywork").keypress(function(event) {
			var key = window.event ? event.keyCode : event.which;
			if (key == 13) {
				eXo.task.TaskManagement.searchUser();
			} else return true;
		});
		
		$("#addMemberToProject").click(function(){
			var listMembers = $("#listMembers").find(".checkItem");
			var userIds = new Array();
			if (listMembers){
				for (var i=0;i<listMembers.length;i++){
					var item = listMembers[i];
					var value = $(item).prop("checked");
					if (value) {
						var parent = $(item).parent().parent();
						var userId = parent.attr("id");
						userIds.push(userId);
						
						var image = parent.find("img");
						var avatarUrl = image.attr("src");
						var username = image.attr("title");
						html = "<span class=\"memInBoard\">";
						html += "<img src=\"" + avatarUrl + "\" alt=\"" + username + "\" title=\"" + username + "\"></span>";
						$("#addedMember").append(html);
					}
				}
			}
		});
	}
	
	TaskManagement.prototype.searchUser = function() {
		var keyword = $("#memberKeywork").val();
		if (keyword) {
			$.ajax({
				url: "/rest/taskManager/search/" + keyword,
				dataType: "json",
				type: "GET"
			})
			.success(function(data) {
				var html = "";
				if (data.length > 0) {
					for (var i=0;i<data.length;i++){
						var member = data[i];
						html += '<li id="' + member.userId + '"><a href="#">';
						html += '<img src="' + member.avatar + '" alt="' + member.fullname + '" title="' + member.fullname + '">';
						html += '<span class="name">' + member.fullname + '</span></a>';
						html += '<span style="float: right; padding-top: 2px;"><input class="checkItem" type="checkbox"></span></li>';
					}
				} else {
					html += '<li>Result not found</li>';
				}
				$("#listMembers").empty();
				$("#listMembers").append(html);
			})
			.error(function() {
				var err = textStatus + ', ' + error;
				console.log("Transaction Failed: " + err);
			});
		}
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

		function stopDragCol(event, ui){
			$(".uiListColBoard").sortable("disable");
			var stages = "";
			$(".uiColBoard").each(function(index, elem) {
				console.log(index + " " + elem);
				stages += $(elem).attr("stage") + "_";
			});
			stages = stages.substring(0, stages.length -1);
			console.log(stages);
			
		    var uri = eXo.task.TaskManagement.restContext + "/taskManager/project/changeStages?" +
			  "id=" + eXo.task.TaskManagement.projectId +
			  "&stages=" + stages;
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
	
	TaskManagement.prototype.initQuickEdit = function(){
		var taskForm = $(".UITaskForm");
		$(".toggleQuickEdit", taskForm).click(function(){
			$( this ).addClass("edit");			
		});
		$(".uiIconRemove", taskForm).each(function(index, elem){
			$(elem.parentNode).click(function(event){
	 			$( this ).parents(".toggleQuickEdit").removeClass("edit");
  	   		    event.stopPropagation();
			});	
		})
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
		
	}
	
	//---------------All setter methods---------------------//
	if (!eXo.task) eXo.task = {};
	eXo.task.TaskManagement = new TaskManagement();
	
	return eXo.task.TaskManagement;
	//-------------------------------------------------------------------------//
})(gj);
