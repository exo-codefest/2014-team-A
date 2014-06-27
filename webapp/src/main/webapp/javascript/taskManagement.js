$(document).ready(function(){	
   $("div.addTask").click(function(){	   
	   this.append($("#taskEditName_fragment").html());
   })

   function NotificationPortlet() {
	   this.init = function(userPrefs){
		   jq("#portletId-btnSave").click(function(){
		   savePrefs(userPrefs);
		   });
		
		   jq("#portletId-btnCancel").click(function(){
		   renderPrefs(userPrefs);
		   });
		
		   jq("#portletId-interval,#portletId-isSummaryMail,.portletId-NotifyOption").live("change", function(){
		   onChangeSummary(jq("#portletId-isSummaryMail").is(':checked'));
		   });
		
		   renderPrefs(userPrefs);
	   }
   }

   notificationPortlet = new NotificationPortlet();
   
});