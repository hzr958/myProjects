var page = page ? page : {};
page.submit = function(p) {
	if (!p || !/\d+/g.test(p))
		p = 1;
	msgboxLoadPage(p,$("#pageSize").val());
};

page.topage = function() {
	var toPage = $.trim($("#toPage").val());
	if (!/^\d+$/g.test(toPage))
		toPage = 1;
	msgboxLoadPage(toPage,$("#pageSize").val());
};

/**
 * 分页加载.
 * @return
 */
function msgboxLoadPage(p,pageSize) {
	var msgboxStatus = $("#page_status").val();
	var msgboxUrl = $("#page_url").val();
	var searchKey = $.trim($("#msgbox_search").val());
	if(searchKey==msgboxTip.searchKey){
		searchKey="";
	}
	var msgboxReplace = $("#page_element").val();
	$.ajax( {
		type : "post",
		url : msgboxUrl,
		data : {
			"opStatus":msgboxStatus,
			"status" : msgboxStatus,
			"searchKey" : searchKey,
	  		"page.pageNo" : p,
	  		"page.pageSize" : pageSize
		},
		dataType : "html",
		cache : false,
		success : function(data) {	
		   if (data.lastIndexOf("ajaxSessionTimeOut") != -1) {
		   	     jConfirm(msgboxTip.sessionTimeout, msgboxTip.dialoagTitle, function(r) {
						   if (r) {
								document.location.reload();
						    }
					   });
		   }else{
			   $("#" + msgboxReplace).html(data);
		    }
			
		},
		error : function(e) {

		}
	});
}