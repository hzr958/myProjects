var scmmanagement =scmmanagement||{};

scmmanagement.pubInfoList=function(url,data){
	 $.ajax({
			url: url,
			type: "post",
			dataType: "html",
			data: data,
			success: function(data){
				$(".main-column").html(data);
			},
		}); 
 }
//检索
function simpleSearch(){
	var data = {
			'page.pageNo' : $('#pageNo').val(),
			'page.pageSize' : $('#pageSize').val(),
			'pubId' : $('#pubId').val(),
			'title' : $('#title').val()
	};
	scmmanagement.pubInfoList("/scmmanagement/pubInfo/ajaxPubList",data);
}
//清空
function clearSearch(){
  $("#pubId").val("");
  $("#title").val("");
}
//删除成果
function deletePub(){
  //遍历
  var ids = "";
  $("#psn_list").find("input:checked").each(function(i, o) {
      var pubIdStr = $(this).val();
        if ($.trim(pubIdStr) != "") {
          ids += "," + pubIdStr;
       }
   });
  if (ids != "") {
    ids = ids.substring(1);
  };
  if ($.trim(ids) == "") {
    alert("请选择成果");
    return;
  }
  doDel(ids);
}

function doDel(des3PubId) {
  var post_data = {
    "des3PubIds" : des3PubId
  };
  $.ajax({
    url : '/scmmanagement/setting/ajaxdelete',
    type : 'post',
    dataType : 'json',
    data : post_data,
    success : function(data) {
        if (data.result == "success") {
          alert("成功删除" + data.count +"条成果");
          simpleSearch();
        } else if (data.result == "isDel") {
          alert(data.msg);
        } else {
          alert(data.msg);
        }
    },
  });
}
//翻页跳页
var page = {};
page.submit = function(p){
	var pageSize =$("#pageSize").val();
	if(!p || !/\d+/g.test(p))
  	  	p = 1;
	var title = $("#title").val();
	var data = {
		    "page.pageSize":pageSize  ,
		    "page.pageNo":p,
		    'title' : title
		};
	scmmanagement.pubInfoList("/scmmanagement/pubInfo/ajaxPubList",data);
};

page.topage = function(){
	var  toPage = $.trim($("#toPage").val()) ;
	var pageSize =$("#pageSize").val();
	if(!/^\d+$/g.test(toPage))
		toPage = 1;
	
	toPage   =Number(toPage) ;
	var totalPages = Number( $("#totalPages").val()  );
	
	if(toPage > totalPages){
		toPage = totalPages ;
	}else if(toPage<1){
		toPage = 1 ;
	}
	var data = {
			    
		        "page.pageSize":pageSize  ,
		        "page.pageNo":toPage,
		        'title' : $("#title").val()
		     };
	 var url = $("#currentUrl").val();
	 scmmanagement.pubInfoList("/scmmanagement/pubInfo/ajaxPubList",data);
	
};