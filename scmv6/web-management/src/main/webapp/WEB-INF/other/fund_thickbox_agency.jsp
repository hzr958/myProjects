<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctx" value="/scmmanagement" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>基金管理</title>
<script type="text/javascript">var resctx="${resmod}";var respath="${resmod}"; var ctxpath="${ctx}";</script>
<link href="${resmod}/css_v5/rol_header.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/rol_common.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/rol_public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/rol_footer.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/home/home.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css"	href="${resmod}/css_v5/plugin/jquery.scmtips.css" />
<link rel="stylesheet" type="text/css" media="screen"	href="${resmod}/css_v5/plugin/jquery.alerts.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/pop.css" />
<script type="text/javascript" src="${resmod}/js_v5/common.ui.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.proceedingwin.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.scmtips.js"></script>
<script type='text/javascript' src='${resmod}/js_v5/plugin/jquery.alerts_${locale}.js'></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.alerts.js"></script>
<script type="text/javascript" src="${resmod}/js_v5//bpo_fund/agency.thickbox.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/link.status.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/loadding_div.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.fileupload.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.filestyle.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.watermark.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.thickbox.min.js"></script>

<script type="text/javascript">
var jsessionId = "<%=session.getId() %>";
var typeId = "${fundAgency.type}";
var addrCoun = '${fundAgency.addrCoun}';
var addrPrv = '${fundAgency.addrPrv}';
var addrCity = '${fundAgency.addrCity}';
var agencyLogoUrl = '${fundAgency.logoUrl}';
$(document).ready(function() {
	//bpo-37  基金信息维护》新增和编辑资助机构，没有logo的情况下直接展开“编辑“页面，有logo才显示”编辑“链接
	if(agencyLogoUrl == '' || agencyLogoUrl.indexOf("/images_v5/fund_logo/default_logo.jpg") != -1 ){
		$(".uploadLogo1").css("display","none");
		$(".uploadLogo2").css("display","");
	}else{
		$(".uploadLogo2").css("display","none");
		$(".uploadLogo1").css("display","");
	}
	
	$("input[type='file']").filestyle({ 
		 image: "${resmod}/images_v5/browse_icon_${locale}.gif",
	    imageheight : 22,
	    imagewidth : 60,
	    width : 310
	   //position:""
	}).change(function(){                      
		if($(this).val().length>0){
			$('.filedata').css("color","#000");
			$('.filedata').val($(this).val().substring($(this).val() .replace(/\\/g, '/').lastIndexOf('/') + 1));	
		} 
	}); 	
	$('.filedata').watermark({
		tipCon : ""
	}); 
	if(Sys.ie==11.0){
		$('#filedata').css("width","");
	}
	if($("#url").val() == ""){//网址字段为空时，默认为http://
	 	   $("#url").val("http://");
	    }
	
});
</script>
</head>
<body style="line-height:100%">
<input type="hidden" class="thickbox" id="thickbox_agency"/>
<input type="hidden" id="agencyId" value="${fundAgency.insId>0?'':fundAgency.id}" />
<input type="hidden" id="auditAgencyId" value="${fundAgency.id}" />
<input type="hidden" id="auditType" value="${auditType }" /><!-- 新增或者更新 -->
<input type="hidden" id="parentAgencyId" value="${fundAgency.parentAgencyId}" /><!-- BPO对应的id -->
<div id="pro_fund_main" class="pro_fund_main">
        <div class="change_password" style="margin-bottom:10px">
        <c:if test="${not empty fundAgency.parentAgencyId}">
       		 <span ><img src="${resmod}/images/ico.gif"/> <a href="${ctx }/fund/agency/compare?agencyId=${fundAgency.id }&parentAgencyId=${fundAgency.parentAgencyId}" target="_blank" class="Blue b">与上个历史审核版本比较</a></span>
        </c:if>
           <span class="Fright">带<span class="red">*</span>号的为必填项</span> 
        </div>
       <table width="100%" border="0" cellspacing="0" cellpadding="0" class="clear c_account">
  <tr class="uploadLogo1">
    <td align="right"><span class="red">*</span>资助机构Logo：</td> 
    <td align="left">
    	<c:choose>
       		<c:when test="${empty fundAgency.logoUrl }"><img id="agency_logo" src="${resmod}/images_v5/fund_logo/default_logo.jpg" width="50" height="50" /></c:when>
       		<c:when test="${fn:contains(fundAgency.logoUrl,'http')}"><img id="agency_logo" src="${fundAgency.logoUrl}" width="50" height="50" /></c:when>
       		<c:when test="${fn:contains(fundAgency.logoUrl,'ressns')}"><img id="agency_logo" src="${fundAgency.logoUrl}" width="50" height="50" /></c:when>
       		<c:otherwise><img id="agency_logo"src="${resmod}/${fundAgency.logoUrl}" width="50" height="50" /></c:otherwise>
       	</c:choose>
       	<lable><a class="Blue mleft5" onclick="agencyThickbox.editLogo();">编辑</a></lable>
    </td>
  </tr>
  <tr class="uploadLogo2" style="display:none;">
    <td align="right"><span class="red">*</span>资助机构Logo：</td> 
    <td align="left"> <input id="filedata" onchange="agencyThickbox.change();" name="filedata" style="width:450px;" type="file"/><br/></td>
  </tr>
  <tr class="uploadLogo2" style="display:none;">
    <td align="right">&nbsp;</td> 
    <td align="left"><a id="upload_btn" onclick="agencyThickbox.ajaxUploadFile();" class="uiButton anniu f_normal">上传</a></td>
  </tr>
  <tr>
    <td width="120" align="right"><span class="red">*</span>资助机构：</td>
    <td align="left"><input type="text" maxlength="200" name="nameZh" id="nameZh" value="${fundAgency.nameZh}"  class="inp_text" style="width:400px;"/></td>
  </tr>
  <tr>
    <td align="right"><span class="red">*</span>资助机构(英文)：</td>
    <td align="left"><input type="text" maxlength="200" name="nameEn" id="nameEn" value="${fundAgency.nameEn}" class="inp_text" style="width:400px;"/></td>
  </tr>
  <tr>
    <td align="right">机构缩写：</td>
    <td align="left">
         <input type="text" name="abbr" maxlength="200" id="abbr" value="${fundAgency.abbr}" class="inp_text" style="width:400px;"/></td>
  </tr>
  <tr>
  	 <td align="right"><span class="red">*</span>机构类型：</td>
    <td align="left" valign="top" colspan="2">
    	 <select name="type" id="type" class="inp_text" style="width:103px;">
         <option value="">请选择</option>
			<s:iterator value="agencyTypeList">
				<option value="${id}">${name}</option>
			</s:iterator>
      </select>
       <span id="region_div" style="display:none;">
      &nbsp;&nbsp;省市：
      <select name="regionId" id="regionId" class="inp_text" style="width:103px;">
         <option value="">请选择</option>
			<s:iterator value="agencyRegionList">
				<option value="${id}" ${id eq fundAgency.regionId?'selected=selected':''} ${id eq fundAgency.superRegionId?'selected=selected':''} >${name}</option>
			</s:iterator>
      </select>
        </span>
        <span id="city_div" style="display:none;">
      &nbsp;&nbsp;地区：
      <select name="cityId" id="cityId" class="inp_text" style="width:103px;">
         <option value="">请选择</option>
         	<c:if test="${!empty agencyCityList }">
         		 <s:iterator value="agencyCityList">
						<option value="${id}" ${id eq fundAgency.superRegionId?'selected=selected':''} ${id eq fundAgency.regionId?'selected=selected':''}>${name}</option>
					</s:iterator>
         	</c:if>
      </select>
        </span>
    </td>
  </tr>
  <tr>
    <td align="right">机构地址：</td>
    <td align="left">
    	<select name="addrCoun" id="addrCoun" class="inp_text" style="width:130px;">
              <option value="">请选择</option>
			  <s:iterator value="countryList">
			  <option value="${id}">${name}</option>
			  </s:iterator>
        </select>
    	<span id="addr_prv_div" style="display:none;">
		&nbsp;&nbsp;
    	<select name="addrPrv" id="addrPrv" class="inp_text" style="width:130px;">
              <option value="" >请选择</option>
              <s:iterator value="provinceList">
			  	<option value="${id}">${name}</option>
			  </s:iterator>
        </select>
        </span>
        <span id="addr_city_div" style="display:none;">
		&nbsp;&nbsp;
		<select name="addrCity" id="addrCity" class="inp_text" style="width:130px;">
			<option value="" >请选择</option>
			<s:iterator value="cityList">
			  <option value="${id}">${name}</option>
			</s:iterator>
		</select>
		</span>
    </td>
  </tr>
  <tr>
    <td align="right">&nbsp;</td>
    <td align="left"><input type="text" maxlength="200" name="address" id="address" value="${fn:escapeXml(fundAgency.address)}" class="inp_text" style="width:400px;"/></td>
  </tr>
  <tr>
    <td align="right">URL网址：</td>
    <td align="left"><input type="text" maxlength="200" name="url" id="url"  value="${fn:escapeXml(fundAgency.url)}" class="inp_text" style="width:400px;"/></td>
  </tr>
  <tr>
  
</table>
</div>
<div class="pop_buttom">
	<c:choose>
  			<c:when test="${empty fundAgency.insId || fundAgency.insId eq 0}">
  				<a id="btn_submit" onclick="agencyThickbox.saveAgency();" class="uiButton uiButtonConfirm text14">保存</a> 
    			<a onclick="parent.$.Thickbox.closeWin();" class="uiButton text14 mright20">取消</a>
  			</c:when>
  			<c:otherwise>
  				<a onclick="$('#auditType').val('save');agencyThickbox.approve();" class="uiButton uiButtonConfirm text14">批准(新增)</a>
  				<c:if test="${!empty fundAgency.parentAgencyId}">
  				<a onclick="$('#auditType').val('update');agencyThickbox.approve();" class="uiButton uiButtonConfirm text14">更新</a>
  				</c:if>  
    			<a onclick="agencyThickbox.refuse();" class="uiButton text14 mright20">拒绝</a>
  			</c:otherwise>
  		</c:choose>
</div>
</body>
</html>