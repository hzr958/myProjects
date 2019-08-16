var scmmanagement =scmmanagement||{};

scmmanagement.psnInfoList=function(url,data){
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

function simpleSearch(){
	var data = {
			'page.pageNo' : $('#pageNo').val(),
			'page.pageSize' : $('#pageSize').val(),
			'nameSearchContent' : $('#nameSearchContent').val(),
			'emailSearchContent' : $('#emailSearchContent').val(),
			'searchType' : $("input[name='search_type']:checked").val()
	};
	scmmanagement.psnInfoList("/scmmanagement/psnInfo/ajaxPsnList",data);
}

function ajaxVLoginUrl(psnId){
	    
		$.ajax( {
			url :domainbpo+'/scmwebbpo/login/ajaxVLoginUrl?jsoncallback=jsoncallback',
			type : 'post',
			dataType:'jsonp',
			data : {
				"vPsnId":psnId,
				"jsoncallback":"jsoncallback"
				},
			jsonpCallback:"jsoncallback",
		}).done(function(data) {
			if(data.result=='success'){
				if(data.vUserName=="null"){
					alert(vPsnId+"不存在对应的用户");
					return;
				}
				var vUrl = domainscm+"/dynweb/"+data.vLoginUrl;
				$("#vLoginUrl_"+psnId).addClass("Blue");
				$("#vLoginUrl_"+psnId).attr("href",vUrl);
				
			}else{
				alert("生成代用户登录URL出错");
			}
		});	
}

/*function jsoncallback(data){  
	var html=JSON.stringify(data);  
	alert(html);  
 }  */
//查看邮件
function checkEmail(psnId,psnEmail){
	if(psnEmail == null){
		return;
	}
	var tbHeight = "660";
	var tbWidth = "880";
	$("#checkEmail").attr("alt","/scmmanagement/psnInfo/email?psnEmail="+psnEmail+"&psnId="+psnId+"&TB_iframe=true&height="+tbHeight+"&width="+tbWidth+"");
	$("#checkEmail").click();
	
}


//加载邮件列表
scmmanagement.ajaxEmailInfoList=function(url,data){
	$.ajax({
		url: url,
		type: "post",
		dataType: "html",
		data: data,
		success: function(data){
			$("#main-column").html(data);
		},
	});
}

//邮件列表时间

function switchEmailDate(tag){
	var newTagValue = tag.options[tag.selectedIndex].value;
	$("#typeId").attr("value",newTagValue);
	$.ajax({
		url: "/scmmanagement/emailInfo/ajaxEmailInfoList",
		type: "post",
		dataType: "html",
		data: {
			"psnEmail": $("#mailAddress").val(),
			"psnId": $("#psnId").val(),
			"typeId":$("#typeId").val()
			},
		success: function(data){
			$("#main-column").html(data);
		},
	});
}

//翻页跳页
var page = {};
page.submit = function(p){
	var pageSize =$("#pageSize").val();
	if(!p || !/\d+/g.test(p))
  	  	p = 1;
	var data = {
		    "page.pageSize":pageSize  ,
		    "page.pageNo":p,
		    'searchContent' : $('#simpleSearchContent').val()
		};
	scmmanagement.psnInfoList("/scmmanagement/psnInfo/ajaxPsnList",data);
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
		        'searchContent' : $('#simpleSearchContent').val()
		     };
	 var url = $("#currentUrl").val();
	 scmmanagement.psnInfoList("/scmmanagement/psnInfo/ajaxPsnList",data);
	
};