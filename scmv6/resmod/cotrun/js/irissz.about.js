var about = about ? about : {};

var cliectIP="";
var domain=location.href;
var inpTips =  "";

about.loadResfile = function(filename,filetype){
    if(filetype == "js"){
        var fileref = document.createElement('script');
        fileref.setAttribute("type","text/javascript");
        fileref.setAttribute("src","../js/"+filename);
    }else if(filetype == "css"){

        var fileref = document.createElement('link');
        fileref.setAttribute("rel","stylesheet");
        fileref.setAttribute("type","text/css");
        fileref.setAttribute("href","../css/"+filename);
    }
   if(typeof fileref != "undefined"){
        document.getElementsByTagName("head")[0].appendChild(fileref);
    }
};

about.loadResfile("jquery.thickbox.js","js");
about.loadResfile("jquery.watermark.js","js");
about.loadResfile("jquery.thickbox.css","css");

$(document).ready(function() {
  setTimeout(function(){about.loadResfile("irissz.init.js","js");},200);;

	if(domain.indexOf("/cn/")>0){
		inpTips = inpTips_cn;
		$(".contact_sales_btn").find("a").addClass("thickbox").attr("href","/cn/pop-main.html?TB_iframe=true&height=540&width=640");
	}
else{
		inpTips = inpTips_en;
		$(".contact_sales_btn").find("a").addClass("thickbox").attr("href","/en/pop-main.html?TB_iframe=true&height=540&width=640");
	}

	var url = 'http://chaxun.1616.net/s.php?type=ip&output=json&callback=?&_='+Math.random();
	$.getJSON(url, function(data){
		cliectIP=data.Ip;
	});

	about.setTextareaMaxLength(2000);

	$("#sumbit_btn").click(function(){
			var postData  = about.getPostData();
			if(postData){
					$(this).attr("disabled","disabled");
			  	about.sendMail(postData);
			}
	 });

});

about.sendMail = function(postData){
 $.ajax({
        async:false,
        url: 'http://www.scholarmate.com//scmmanagement/irismail',
        type : 'post',
        dataType : "jsonp",//数据类型为jsonp
        jsonp: "jsonpCallback",//服务端用于接收callback调用的function名的参数
        data: postData,
        timeout: 5000,
        success: function (data) {
         		$.scmtips.show("success",inpTips.sendReslutMsg,420);
       	    setTimeout(function(){parent.$.Thickbox.closeWin();},3000);
        },
        error: function(xhr) {
       	    $.scmtips.show("success",inpTips.sendReslutMsg,420);
       	    setTimeout(function(){parent.$.Thickbox.closeWin();},3000);
        }
   });
};

about.getPostData = function(){
	var firstName=$.trim($("#firstName").val());
	    firstName = firstName==inpTips.firstName?"":firstName;
	var lastName=$.trim($("#lastName").val());
			lastName = lastName==inpTips.lastName?"":lastName;
	var email=$.trim($("#email").val());
	    email = email==inpTips.email?"":email;
	var tel=$.trim($("#tel").val());
	var insName=$.trim($("#insName").val());
	var insScale=$.trim($("#insScale").val());//单位范围
	var position=$.trim($("#position").val());
	var address=$.trim($("#address").val());
	var city=$.trim($("#city").val());
	var country=$.trim($("#country").val());
	var remark=$.trim($("#remark").val());
	var flag="";
	if(firstName==""){
		$("#firstName").val("");
		$("#firstName").css("border-color","#FF0000");
		flag = true;
	}else{
		$("#firstName").css("border-color","");
	}
	if(lastName==""){
		$("#lastName").val("");
		$("#lastName").css("border-color","#FF0000");
		flag = true;
	}else{
		$("#lastName").css("border-color","");
	}
	if(email==""){
		$("#email").val("");
		$("#email").css("border-color","#FF0000");
		flag = true;
	}else{
	  $("#email").css("border-color","");
	}
	if(tel==""){
		$("#tel").val("");
		$("#tel").css("border-color","#FF0000");
		flag = true;
	}else{
		$("#tel").css("border-color","");
	}
	if(insName==""){
		$("#insName").val("");
		$("#insName").css("border-color","#FF0000");
		flag = true;
	}else{
	  $("#insName").css("border-color","");
	}
	if(insScale==""){
		$("#insScale").css("border-color","#FF0000");
		flag = true;
	}else{
	  $("#insScale").css("border-color","");
	}
	if(address==""){
		$("#address").val("");
		$("#address").css("border-color","#FF0000");
		flag = true;
	}else{
	  $("#address").css("border-color","");
	}
	if(city==""){
		$("#city").css("border-color","#FF0000");
		flag = true;
	}else{
	  $("#city").css("border-color","");
	}
	if(country==""){
		$("#country").css("border-color","#FF0000");
		flag = true;
	}else{
	  $("#country").css("border-color","");
	}
	if(remark==""){
		$("#remark").css("border-color","#FF0000");
		flag = true;
	}else{
		$("#remark").css("border-color","");
	}
	if(flag){
		if(domain.indexOf("/cn/")>0){
		  if(firstName==""){
				$("#firstName").focus();
			}else if(lastName==""){
				$("#lastName").focus();
			}else if(email==""){
			  $("#email").focus();
			}else if(tel==""){
			  $("#tel").focus();
			}else if(insName==""){
			  $("#insName").focus();
			}else if(address==""){
			  $("#address").focus();
			}else if(city==""){
			  $("#city").focus();
			}else if(remark==""){
			  $("#remark").focus();
			}
		}else{
			if(lastName==""){
				$("#lastName").focus();
			}else if(firstName==""){
				$("#firstName").focus();
			}else if(email==""){
			  $("#email").focus();
			}else if(tel==""){
			  $("#tel").focus();
			}else if(insName==""){
			  $("#insName").focus();
			}else if(address==""){
			  $("#address").focus();
			}else if(city==""){
			  $("#city").focus();
			}else if(remark==""){
			  $("#remark").focus();
			}
		}
		return false;
	}
  return {"cliectIP":cliectIP,"domain":domain,"firstName":firstName,"lastName":lastName,"email":email,"tel":tel,"insName":insName,"insScale":insScale,"position":position,"address":address,"city":city,"country":country,"remark":remark};
};

// 限制textarea最多输入
about.setTextareaMaxLength = function(maxLength) {
	$("textarea").keyup(function() {
		var area = $(this).val();
		if (area.length > maxLength) {
			$(this).val(area.substring(0, maxLength));
		}
	});
	$("textarea").blur(function() {
		var area = $(this).val();
		if (area.length > maxLength) {
			$(this).val(area.substring(0, maxLength));
		}
	});
};
// 验证邮件格式是否合法
about.isEmail = function(email) {
	return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i
			.test(email);
};

var inpTips_cn={
  firstName : "（如：Chen）",
  lastName : "（如：Xiao Ming）",
	email : "examples@irissz.com",
  sendReslutMsg : "非常感谢！您的信息及建议已提交成功，我们的销售代表将在第一时间和您联系。"
};
var inpTips_en={
  firstName:"(e.g. Chen)",
  lastName : "(e.g. Xiao Ming)",
   email : "examples@irissz.com",
   sendReslutMsg : "Thanks for your submission! We'll be in touch."
};

