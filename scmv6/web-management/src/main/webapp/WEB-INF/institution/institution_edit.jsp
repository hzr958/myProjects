<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>单位信息修改</title>
<script type="text/javascript">
	var appContextPath = "${res}";
</script>
<link href="${res }/css/menu/navMenu.css" rel="stylesheet" type="text/css" />
<link href="${res}/js/plugins/scmtip/scmtip.css" rel="stylesheet" type="text/css" />
<link href="${res}/css/thickbox.css" rel="stylesheet" type="text/css" />
<link href="${res}/js/plugins/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
<style type="text/css">
.msg_div {
  background: url("${res}/images/warn.gif") no-repeat scroll center left transparent;
  color: #FF0000;
  font-size: 12px;
  padding: 5px 0 5px 35px;
  border: 1px #ccc solid;
  width: 400px;
  margin-left: 70px;
}
</style>
<script type="text/javascript" src="${res }/js/CommonUI.js"></script>
<script type="text/javascript" src="${res}/js/thickbox-compressed.js"></script>
<script type="text/javascript" src="${res}/js/json2.js"></script>
<script type="text/javascript" src="${res}/js/autoname/autoInsName.js"></script>
<script type="text/javascript" src="${res}/js/plugins/scmtip/scmtip.js"></script>
<script type='text/javascript' src='${res}/js/plugins/jquery.autocomplete_custom.js'></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.filestyle.js"></script>
<script type="text/javascript" src="${res}/js_v5/plugin/jquery.fileupload.js"></script>
<script type="text/javascript" src="${res}/js/plugins/fileUpload/fax.fileupload.handler.js"></script>
<script type="text/javascript" src="${res}/js/ins_logo_upload.js"></script>
<script type="text/javascript" src="${res}/js_v5/link.status.js"></script>
<script type="text/javascript">
var v = false;
var ctxpath = "/scmmanagement";
var res = "${res}";
function validateLong(tude) {
	var pattern = new RegExp(/^[1-9]?[0-9]+\.[0-9]*[1-9]+$/);
	return pattern.test(tude);
}

$(document).ready(function() {
	$("#nature").val('${nature}');
	
	$('#fax_filedata').filestyle({ 
  	     image: "${res}/images_v5/browse_icon_zh_CN.gif",
  	     imageheight : 22,
  	     imagewidth : 60,
  	     width : 270
    }).live('change', function(){        
		if($(this).val().length>0){
			$('.fax_filedata').val($(this).val().substring($(this).val() .replace(/\\/g, '/').lastIndexOf('/') + 1));
			$('#upload_fax_btn').attr('disabled', false);
		} 
    });
	<c:if test="${!empty logoAddr}">
	$("#img_inslogo").attr("src","/scmmanagement${logoAddr }?temp="+ (new Date().getTime().toString(36)));
	$("#tr_inslogo").show();
	</c:if>
	//(图片上传)加载到哪个块里面.
	$(document).inslogoupload({
	  	ctxpath:"/scmmanagement",
	  	respath:"${res}",
		locale:"zh_CN",
	    //数据ID
		dataDes3Id:"${insId}"
	});
	$('#insDomain_txt').bind('blur', function() {
		$(this).val($(this).val().toLowerCase());
	});
	$('#insDomainSuffix_txt').bind('blur', function() {
		$(this).val($(this).val().toLowerCase());
	});
	$('#provinceId').bind(
			"change",
			function() {
				$('#prv').hide();
				var provinceId = $('#provinceId')
						.val();
				if (provinceId == ""
						|| provinceId == "110000"
						|| provinceId == "120000"
						|| provinceId == "310000"
						|| provinceId == "500000") {
					$("#cityId").find(
							"option[value!='']")
							.remove();
					$("#cityId").hide();
					return;
				}
				$.ajax({
					url : '/scmmanagement/approve/ajaxSelect',
					type : 'post',
					dataType : 'json',
					data : {
						"provinceId" : provinceId
					},
					success : function(data) {
						if (data.result == "success") {
							var cityMap = eval('(' +data.cityJson+ ')');
							if (cityMap.length > 0) {
								$("#cityId").find("option[value!='']").remove();
								for(var i=0;i<cityMap.length;i++){
									$("#cityId").append($("<option title='"+cityMap[i].zhName+"'></option>")
											.attr("value",
													cityMap[i].cyId)
											.text(cityMap[i].zhName));
								}
								$('#prvTip').hide();
								$("#cityId").show();
							}
						} else {
							show_msg_tips("error", "操作失败");
						}
					},
					error : function() {
						show_msg_tips("error", "ajax error");
					}
				});
			});
		
	});
	
	
function returnPage(){
	window.location.href ="/scmmanagement/institution/manage/maint";
};

//查看历史备注
function viewInsEditRemark(insId) {
	$('#edit_ins_remark_btn').attr('alt', '/scmmanagement/institution/manage/openInsEditRemarkPage?insId='+ insId + '&TB_iframe=true&height=400&width=650').click();
};
					
function confirmEditIns() {
	var insId = $("#insId_hidden").val();
	var insName = $.trim($("#insName_txt").val());
	if (insName == '') {
		$('#insName_tip').html(
				"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
						+ "<font color='red'>请输入单位名称</font>").show();
		$('#insName_txt').focus();
		return;
	}
	$('#insName_tip').hide();
	
	$.ajax({
		url : '/scmmanagement/institution/manage/ajaxCheckInsName',
		type : 'post',
		dataType : 'json',
		data : {
			"insName" : insName,
			"insId" : insId
		},
		success : function(data) {
			if(data.result == "success") {
				if (data.isUsing) {
					$('#insName_tip').html(
							"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
									+ "<font color='red'>单位名称已被其他单位使用</font>").show();
				} else {
					$('#insName_tip').hide();
					var insDomain = $("#insDomain_txt").val().replace(/(http\:\/\/)/gi, "").replace(/\s/g, "");
					$("#insDomain_txt").val(insDomain);
					if (insDomain == '') {
						$('#insDomain_tip').html(
								"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
										+ "<font color='red'>请输入单位域名</font>").show();
						$('#insDomain_txt').focus();
						return;
					}
					var insDomainSuffix = $.trim($("#insDomainSuffix_txt").val());
					$("#insDomainSuffix_txt").val(insDomainSuffix);
					if (insDomainSuffix == "") {
						$('#insDomain_tip').html(
								"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
										+ "<font color='red'>输入的单位域名不正确</font>").show();
						$('#insDomainSuffix_txt').focus();
						return;
					}
					$('#insDomain_tip').hide();
					insDomain = insDomain + "." + insDomainSuffix + ".scholarmate.com";
					$.ajax({
							url : '/scmmanagement/institution/manage/ajaxCheckDomain',
							type : 'post',
							dataType : 'json',
							data : {
								"insDomain" : insDomain,
								"insId" : insId
							},
							success : function(data) {
								if (data.result == "success") {
									if (data.isUsing) {
										$('#insDomain_tip').html(
												"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
												+ "<font color='red'>域名已被其他单位使用</font>").show();
									} else {
										$('#insDomain_tip').hide();
										var contactPsCn = $.trim($('#contactPsCn_txt').val());
										var insAddress = $.trim($('#insAddress_txt').val());
										var contactEmail = $.trim($('#contactEmail_txt').val());
										var contactTel = $.trim($('#contactTel_txt').val());
										if(contactPsCn == '') {
											$('#contactPsCn_tip').html(
													"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
													+ "<font color='red'>请填写单位联系人</font>").show();
											return;
										}
										$('#contactPsCn_tip').hide();
										
										if(insAddress == '') {
											$('#insAddress_tip').html(
													"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
													+ "<font color='red'>请填写单位地址</font>").show();
											return;
										}
										$('#insAddress_tip').hide();
										
										if(contactEmail == '') {
											$('#contactEmail_tip').html(
													"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
													+ "<font color='red'>请填写联系人邮件</font>").show();
											return;
										}
										$('#contactEmail_tip').hide();
										
										if(contactTel == '') {
											$('#contactTel_tip').html(
													"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
													+ "<font color='red'>请填写单位联系电话</font>").show();
											return;
										}
										$('#contactTel_tip').hide();
										
										var loginName = $.trim($('#loginName_txt').val());
										var toSetPsw = $("#toSetPsw_txt").attr("checked") ? "yes" : "no";
										var psnId = $('#psnId_hidden').val();
										if ((psnId == '' || psnId == null) && loginName == '' && toSetPsw == 'yes') {
											$('#loginName_tip').html(
													"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
													+ "<font color='red'>请输入管理账号</font>").show();
											return;
										}
										$('#loginName_tip').hide();
										
										var provinceId = $('#provinceId').val();
										var cityId = $('#cityId').val();
										var location = $.trim($('#locationInput').val());
										var stat = $("input[name='report_function']:checked").val();
										if (stat == '1') {
											if (provinceId == "" || (provinceId != "110000"
													&& provinceId != "120000"
													&& provinceId != "310000"
													&& provinceId != "500000"
													&& provinceId != "344"
													&& cityId == "")) {
												$('#prvTip').html(
														"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
														+ "<font color='red'>请选择单位所在省市</font>").show();
												return;
											}
											$('#prvTip').hide();
											
											if (location == '') {
												$('#locationTip').html(
														"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
														+ "<font color='red'>请输入单位的经纬度</font>").show();
												return;
											}
											$('#locationTip').hide();
										}
										$('#prvTip').hide();
										$('#locationTip').hide();
										
										//单位性质
										var nature = $.trim($("#nature").val());
										if (nature == '') {
											$('#nature_tip').html(
													"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
															+ "<font color='red'>请选择单位性质</font>").show();
											$('#nature_tip').focus();
											return;
										}
										$('#nature_tip').hide();
										
										var remark = $.trim($('#remark_area').val());
										if (remark == '') {
											$('#remark_tip').html(
													"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
													+ "<font color='red'>请填写备注</font>").show();
											return;
										}
										if(remark.length > 250) {
											$('#remark_tip').html(
													"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
													+ "<font color='red'>备注信息不能超过250个字符</font>").show();
											return;
										}
										$('#remark_tip').hide();
										
										var researchGroupEmail = $('#researchGroupEmail_txt').val();
										if ($('#sendNotification_ck').attr('checked') && researchGroupEmail == '') {
											$('#researchGroupEmail_tip').html(
													"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
													+ "<font color='red'>请填写检索组通知邮件</font>").show();
											return;
										}
										$('#researchGroupEmail_tip').hide();
										
										doEditInstitution(insId, insName, insDomain, contactPsCn, insAddress, 
												contactEmail, contactTel, loginName, psnId, toSetPsw, location, remark, researchGroupEmail);
									}
								} else {
									if(data.result == "warn") {
										$('#insDomain_tip').html(
												"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
												+ "<font color='red'>" + data.msg + "</font>").show();
									} else if(data.result == 'error') {
										show_msg_tips('error', '修改单位失败');
									} else {
										if(window.confirm('您还未登陆或登陆已超时，是否重新登陆？')) {
											 parent.window.location.reload();
										}
									}
								}
							}
						});
				}
			} else {
				if(data.result == "warn") {
					$('#insName_tip').html(
							"<img src='"+res+"/images/mainreg_icon_error.gif' style='vertical-align:text-top;'/>&nbsp"
									+ "<font color='red'>" + data.msg + "</font>").show();
					$('#insName_txt').focus();
				} else if(data.result == "error") {
					show_msg_tips('error', '修改单位失败');
				} else {
					if(window.confirm('您还未登陆或登陆已超时，是否重新登陆？')) {
						 parent.window.location.reload();
					}
				}
			}
		},
		error : function() {
			show_msg_tips("error", "网络连接异常");
		} 
	});
};


function doEditInstitution(insId, insName, insDomain, contactPsCn, 
		insAddress, contactEmail, contactTel, loginName, psnId, toSetPsw, location, remark, researchGroupEmail) {
	var stat = $("input[name='report_function']:checked").val();
	var cons = $("input[name='contrast_right']:checked").val();
	var provinceId = $('#provinceId').val() == null ? '' : $('#provinceId').val();
	var cityId = $('#cityId').val() == null ? '' : $('#cityId').val();
	var longitude = "";
	var latitude = "";
	if (location != '') {
		longitude = location.split(",")[0];
		latitude = location.split(",")[1];
	}
	var nature = $.trim($("#nature").val());
	$("#processImg").show();
	var post_data = {
		"insId" : insId,
		"insName" : insName,
		"insDomain" : insDomain,
		"contactPerson" : contactPsCn,
		"zhAddress" : insAddress,
		"serverEmail" : contactEmail,
		"tel" : contactTel,
		"stat" : stat,
		"cons" : cons,
		"proviceId" : provinceId,
		"cityId" : cityId,
		"longitude" : longitude,
		"latitude" : latitude,
		"nature":nature,
		"remark" : remark,
		//"approveStepDetail" : str,
		//"approveStepIndex" : setIndex(),
	};
	$.ajax({
		url : '/scmmanagement/institution/manage/ajaxEditInstitution',
		type : 'post',
		dataType : 'json',
		data : post_data,
		success : function(data) {
			if (data.result == "success") {
				show_msg_tips("success", data.msg);
				window.location.href = "/scmmanagement/institution/manage/maint";
			} else {
				show_msg_tips(data.result, data.msg);
				$('#processImg').hide();
			}
		},
		error : function() {
			show_msg_tips("error", "网络连接异常");
			$('#processImg').hide();
		}
	});
};

//var step_info = ${approve_step};
function setIndex(){//建立索引
	var status1 = step_info.step1.status;
	var status2 = step_info.step2.status;
	var status3 = step_info.step3.status;

	var indexArray = [];

	indexArray.push(status1==null||status1==""?0:(status1=="2"?2:1));
	indexArray.push(status2==null||status2==""?0:1);
	indexArray.push(status3==null||status3==""?0:1);
	return indexArray.join("");
};

function toggleCons() {
	var stat = $("input[name='report_function']:checked").val();
	if (stat == '0') {
		$('#cons_tr').hide();
		$("input[name='contrast_right'][value=0]").attr("checked",
				"checked");
	} else {
		$('#cons_tr').show();
	}
};
</script>
</head>
<body id="main_body">
  <form id="mainForm">
    <input type="hidden" name="pageType" id="pageType_hidden" value="edit" /> <input type="hidden" name="des3Id"
      id="des3Id_hidden" value="${insId }" /> <input type="hidden" name="insId" id="insId_hidden" value="${insId }" />
    <input type="hidden" name="psnId" id="psnId_hidden" value="${psnId }" /> <input type="hidden"
      id="edit_ins_remark_btn" class="thickbox" title="单位修改历史备注" />
    <div class="box_minTitle">
      <span style="float: left; font-size: 14px; font-weight: bold;">编辑单位</span> <span style="float: right">加<font
        color="#ff0000"> * </font>为必填
      </span>
    </div>
    <table border="0" cellpadding="0" cellspacing="0" class="table_noborder">
      <tr>
        <td colspan="2"><strong>1、单位名称</strong></td>
      </tr>
      <tr>
        <td align="right" height="30"><span style="color: #ff0000">*&nbsp;</span>单位名称：</td>
        <td><input type="text" name="insName" id="insName_txt" value="${insName }" class="input_text"
          style="width: 300px;" /> <label id="insName_tip">&nbsp;</label></td>
      </tr>
      <tr>
        <td colspan="2" style="padding-top: 10px;"><strong>2、设置访问独立域名</strong> (如：http://xxx.cn.scholarmate.com)</td>
      </tr>
      <tr>
        <td height="30" colspan="2">&nbsp;&nbsp;&nbsp;<span style="color: #ff0000">*&nbsp;</span>http:// <input
          type="text" name="insDomain" id="insDomain_txt" value="${insDomain }" class="input_text" style="width: 125px" />
          . <input type="text" name="insDomainSuffix" id="insDomainSuffix_txt" value="${insDomainSuffix }"
          class="input_text" style="width: 40px" /> .scholarmate.com <label id="insDomain_tip">&nbsp;</label>
        </td>
      </tr>
      <tr>
        <td colspan="2" style="padding-top: 10px;"><strong>3、单位联系人</strong></td>
      </tr>
      <tr>
        <td align="right" height="30"><span style="color: #ff0000">*&nbsp;</span>单位联系人：</td>
        <td><input type="text" name="contactPsCn" id="contactPsCn_txt" value="${contactPerson }" class="input_text"
          style="width: 150px;" /> <label id="contactPsCn_tip">&nbsp;</label></td>
      </tr>
      <tr>
        <td align="right" height="30"><span style="color: #ff0000">*&nbsp;</span>单位地址：</td>
        <td><input type="text" name="insAddress" id="insAddress_txt" value="${zhAddress }" class="input_text"
          style="width: 150px;" /> <label id="insAddress_tip">&nbsp;</label></td>
      </tr>
      <tr>
        <td align="right" height="30"><span style="color: #ff0000">*&nbsp;</span>联系人邮件：</td>
        <td><input type="text" name="contactEmail" id="contactEmail_txt" value="${serverEmail }" class="input_text"
          style="width: 150px;" /> <label id="contactEmail_tip">&nbsp;</label></td>
      </tr>
      <tr>
        <td align="right" height="30"><span style="color: #ff0000">*&nbsp;</span>联系电话：</td>
        <td><input type="text" name="contactTel" id="contactTel_txt" value="${tel }" class="input_text"
          style="width: 150px;" /> <label id="contactTel_tip">&nbsp;</label></td>
      </tr>
      <tr>
        <td colspan="2" style="padding-top: 10px;"><strong>4、单位统计报表：</strong></td>
      </tr>
      <tr class="close">
        <td height="30" colspan="2">开放报表统计功能： <input type="radio" name="report_function" value="1"
          <c:if test="${stat != 0 }">checked="checked"</c:if> onclick="toggleCons()" />&nbsp;是&nbsp;&nbsp;&nbsp;&nbsp;
          <input type="radio" name="report_function" value="0" <c:if test="${stat == 0 }">checked="checked"</c:if>
          onclick="toggleCons()" />&nbsp;否
        </td>
      </tr>
      <tr class="close" id="cons_tr" style="<c:if test='${stat == 0 }'>display:none;</c:if>">
        <td height="30" colspan="2">开放其他单位对比权限： <input type="radio" name="contrast_right" value="1"
          <c:if test="${cons == 1 }">checked="checked"</c:if> />&nbsp;是&nbsp;&nbsp;&nbsp;&nbsp; <input type="radio"
          name="contrast_right" value="0" <c:if test="${cons != 1 }">checked="checked"</c:if> />&nbsp;否
        </td>
      </tr>
      <tr class="close">
        <td height="30" style="text-align: right">单位所在省市：</td>
        <td height="30"><select name="provinceId" id="provinceId" class="select" style="width: 105px">
            <option value="">请选择省</option>
            <c:forEach items="${provinceList}" var="province">
              <option value="${province.prvId}" <c:if test="${proviceId eq province.prvId }">selected="selected"</c:if>>${province.zhName}</option>
            </c:forEach>
        </select> <select name="cityId" id="cityId" class="select"
          style='width:105px; <c:if test="${empty proviceId || proviceId == 0 || proviceId == 110000 || proviceId == 120000 || proviceId == 310000 || proviceId == 500000 || proviceId == 344  }">display:none;</c:if>'>
            <option value="">请选择地区</option>
            <c:forEach items="${cityList }" var="city">
              <option value="${city.cyId }" <c:if test="${cityId eq city.cyId }">selected="selected"</c:if>>${city.zhName}</option>
            </c:forEach>
        </select> <label id="prvTip" class="error" style="display: none;"></label></td>
      </tr>
      <tr class="close">
        <td align="right" height="30">单位经纬度：</td>
        <td height="30"><c:if test="${!empty longitude && !empty latitude }">
            <c:set var="insCoordinate">${longitude },${latitude }</c:set>
          </c:if> <input name="name22" id="locationInput" type="text" value="${insCoordinate }" class="input_text"
          style="width: 150px" /> (<a href="http://api.map.baidu.com/lbsapi/getpoint/index.html" target="_blank"
          id="search">点击此处获取经纬度!</a>) <label id="locationTip" class="error" style="display: none;"></label></td>
      </tr>
      <tr>
        <td colspan="2" style="padding-top: 10px;"><strong>5、单位认证传真</strong></td>
      </tr>
      <tr>
        <td height="30" colspan="2"><input type="file" id="fax_filedata" name="filedata" /> <input type="button"
          id="upload_fax_btn" disabled="disabled" value="上传" style="padding: 2px 10px; margin-left: 80px;"
          class="button" onclick="fileUploadHandler.ajaxFaxFileUpload();" /> <span id="fax_loading"
          style="display: none;"><img alt="" src="${res}/images/loading.gif" width="25px" height="25px" /></span></td>
      </tr>
      <tr>
        <td colspan="2" style="padding-top: 10px;"><strong>6、设置单位Logo</strong></td>
      </tr>
      <tr id="tr_inslogo" style="display: none; padding-top: 3px;">
        <td align="right">单位Logo：</td>
        <td><div class="use_logo">
            <img src="/scmmanagement${logoAddr }" style="height: 50px;" id="img_inslogo" />
          </div></td>
      </tr>
      <tr>
        <td height="40px;" align="right">上传单位Logo：</td>
        <td>
          <div style="float: left;">
            <input type="file" id="upload_img_cutfile" name="filedata" class="upload_img_cutfile"
              style="width: 200px; height: 19px; line-height: 19px;" />
          </div>
          <div style="float: left;">
            <input type="button" id="img_cut_upload_btn" disabled="disabled" value="上传"
              style="padding: 2px 10px; margin-left: 80px;" class="button" />
          </div>
        </td>
      </tr>
      <tr>
        <td align="right" valign="top">&nbsp;</td>
        <td valign="top">
          <p style="margin: 2px 0;">允许上传的图片类型为jpg、gif、png、bmp，图片大小不能超过100KB；</p>
          <p style="color: #ff0000; margin: 2px 0;">最佳显示效果：图片高度为50像素，透明背景（建议先处理好图片再上传）。</p>
        </td>
      </tr>
      <tr>
        <td colspan="2" style="padding-top: 10px;"><strong>7、单位性质</strong></td>
      </tr>
      <tr>
        <td align="right" valign="top" style="padding-top: 3px;"><span style="color: #ff0000">*&nbsp;</span>单位性质：</td>
        <td style="padding-top: 5px;" valign="top"><select name="nature" id="nature" class="select"
          style='width: 105px;'>
            <option value=""></option>
            <c:forEach items="${natureList }" var="nature">
              <option value="${nature.key.code }">${nature.zhCnName}</option>
            </c:forEach>
        </select> <label id="nature_tip">&nbsp;</label></td>
      </tr>
      <tr>
        <td colspan="2" style="padding-top: 10px;"><strong>8、审核备注</strong></td>
      </tr>
      <tr>
        <td align="right" valign="top" style="padding-top: 3px;"><span style="color: #ff0000">*&nbsp;</span>备注：</td>
        <td style="padding-top: 5px;" valign="top">
          <div style="float: left;">
            <textarea name="remark" id="remark_area" style="width: 450px; height: 80px;"></textarea>
          </div>
          <div style="float: left;">
            <p>
              <label id="remark_tip">&nbsp;</label>
            </p>
            <c:if test="${insEditRemarkCount gt 0 }">
              <p style="margin: 40px 0 0 10px;">
                <a href="javascript:void(0);" onclick="viewInsEditRemark('${insId }');">查看历史备注</a>
              </p>
            </c:if>
          </div>
        </td>
      </tr>
      <tr>
        <td colspan="2" style="padding-top: 10px;"><input type="button" class="button" value="确认"
          style="height: 25px" onclick="confirmEditIns()" /> &nbsp;&nbsp; <input type="button" class="button"
          value="返回" style="height: 25px" onclick="returnPage()" /> <img src="${res}/images/loading.gif"
          id="processImg" style="display: none;" /></td>
      </tr>
    </table>
  </form>
</body>
</html>
