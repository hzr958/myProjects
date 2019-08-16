<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="ctx" value="/scmmanagement" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE8" />
<title>基金管理</title>
<script type="text/javascript">var resctx="${resmod}";var respath="${resmod}";var ctxpath="${ctx}";var loacle='${locale }'</script>
<link href="${resmod}/css_v5/rol_header.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/rol_common.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/rol_public.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/rol_footer.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/home/home.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/sie_pop.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/plugin/jquery.scmtips.css" />
<link rel="stylesheet" type="text/css" media="screen" href="${resmod}/css_v5/plugin/jquery.alerts.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/pop.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css_v5/plugin/jquery.thickbox.css" />
<link type="text/css" rel="stylesheet" href="${resmod}/css_v5/plugin/jquery.discipline.css" />
<link type="text/css" rel="stylesheet" href="${resmod}/css_v5/plugin/jquery.autoword.css" />
<link type="text/css" rel="stylesheet" href="${resmod}/css_v5/plugin/jquery.complete.css" />
<link type="text/css" rel="stylesheet" href="${resmod}/smate-pc/css/new-jquerythick.css" />
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/common.ui.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.thickbox.resources.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.proceedingwin.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.scmtips.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/rol_common.js"></script>
<script type='text/javascript' src='${resmod}/js_v5/plugin/jquery.alerts_${locale}.js'></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.alerts.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.watermark.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.discipline.bpo.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.autoword.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.complete.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.fileupload.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/jquery.filestyle.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/link.status.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/loadding_div.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/bpo_fund/agency.maint.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/bpo_fund/category.thickbox.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/plugin/DatePicker/WdatePicker.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/json2.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/newstructuresubject.js"></script>
<script type="text/javascript" src="${resmod}/js_v5/bpo_fund/fundthird.maint.js"></script>

<script type="text/javascript">
var need=false;
var application_file_upload_maxSize = "文件大小不能超过30M";
var pageContext_request_serverName="${pageContext.request.serverName}";
var jsessionId = "<%=session.getId()%>";
var fileList = ${empty fundCategory.fileList?"[]":fundCategory.fileList};
var fileCount=fileList.length;
var autoWord;
var prvcityWord;
var regionJsonList=${empty fundCategory.regionList?"[]":fundCategory.regionList};
var keywordList = "${fundCategory.keywordList}";
var disJsonList = ${empty fundCategory.disJson?"[]":fundCategory.disJson};
var search_tipcon = '请输入：基金机会名称';

function setTabUrl(url){
	location.href=url;
}
$(document).ready(function() {
  /*   $('#agencyNameId')[0].addEventListener('input',matchDropAgencyName($('#agencyNameId')[0]));
    $('#agencyNameId')[0].addEventListener('change',matchDropAgencyName($('#agencyNameId')[0])); */
	$("a.thickbox,input.thickbox").thickbox({ctxpath:ctxpath,respath:respath});
	//common.setTextareaMaxLength(1000); 
	$('#discipline_1, #discipline_2,#discipline_3,#discipline_4').discipline({
		'ctx' : ctxpath,
		'respath' : respath,
		'locale' : loacle,
		'simple':true,
		'bind':function(result,type,_close, selectId){
			if(type=="save"){
				var vals = result["vals"]();//学科代码各级别的值
				var firstVal = "";//选择的第一级学科代码
				if(vals.length > 0){
					firstVal = vals[0]["name"];
				}
				
				//rol-2287 
				$("#auto_div").find(".auto_word_div").each(function(){
					if($(this).attr("val") == "auto_" + selectId){
						$(this).attr("val","");
					}
				});
				
				$("#container").find(".hot_01").each(function(){//先删除
					if($(this).find("a").attr("flag") == selectId){
						$(this).remove();
					}
				});
				
				if(firstVal == ""){
					return;
				}else{
					var isRepeat = false;
					$("#container").find(".hot_01").each(function(){
						if($(this).attr("valText") == firstVal){
							isRepeat = true;
							return;
						}
					});
					if(!isRepeat){//重复则不添加
						$("#container").append(categoryThickbox.createWordHtml(firstVal, selectId, true));
					}
				}
			}
		}
	}); 
	 var addChossen=document.getElementById("addChossen");
	if(disJsonList && disJsonList.length>0){
    	//新增的下拉框需要回显
    	for(var i=0;i<(disJsonList.length+1)/2<<0;i++){//取整
        categoryThickbox.addChossenDisc(addChossen,"学科：");
    	}
		for ( var i = 0; i < disJsonList.length; i++) {
  		 var datashowid=""+disJsonList[i].code;
	    	 $("#discipline_"+(i+1)).attr("data-showid",datashowid.substring(0,1)).attr("data-subshowid",datashowid);
			 $("#discipline_"+(i+1)).empty().append("<option value="+disJsonList[i].id+">"+disJsonList[i].name+"</option>").attr("disc_code",disJsonList[i].code);
		}
	}else{	//如果为空默认增加两个空白下拉框
	  categoryThickbox.addChossenDisc(addChossen,"学科：");
	}	
	prvcityWord = $("#prvCity_div").autoword({
		"split_char" :[",", "，", ";", "；"],
		"words_max" : 100,
		"repeat" : false,
		"onlyText":true,
		"filter": ["<", ">", "&","'",'"'],
		"watermark_flag":true
	});
	//显示地区的div层限制不能输入:
	prvcityWord._input_hide();
	if(regionJsonList.length>0){
		prvcityWord.putAll(regionJsonList);
	}
  autoWord = $("#auto_div").autoword({
		"split_char" :[",", "，", ";", "；"],
		"words_max" : 100,
		"repeat" : false,
		"onlyText":true,
		"filter": ["<", ">", "&","'",'"'],
		"delClick": function (val,text){ categoryThickbox.delSelectKeyword(val,text);},
		"select": $.auto["bpo_fund_keywords"],
		"watermark_flag":true,
		"watermark_tip":"请输入关键词，多个关键词以，或;分割"
	});
	if(keywordList.length>0){
		var keywords=[];
		var keywordArr = keywordList.split(/[,;]/);
		for ( var i = 0; i < keywordArr.length; i++) {
			keywords.push({"val":keywordArr[i],"text":keywordArr[i]});
		}
		if(keywords.length>0)
		autoWord.putAll(keywords);
	}
	$("input[type='file']").filestyle({ 
		  image: respath+"/images_v5/browse_icon_${locale}.gif",
  	     imageheight : 22,
  	     imagewidth : 60,
  	     width : 346,
  	   	 position:"",
  	 }).change(function(){                      
		if($(this).val().length>0){
			$('.filedata').css("color","#000");
			$('.filedata').val($(this).val().substring($(this).val() .replace(/\\/g, '/').lastIndexOf('/') + 1));	
		} 
    }); 
  
   $('.filedata').watermark({
		tipCon : application_file_upload_maxSize
	}); 
   if(Sys.firefox){
  		$("#filedata").css("margin-left","-286px");
   }
   if(Sys.ie==11.0){
		$('#filedata').css("width","");
	}
   
   var fileJson = ${empty fundCategory.fileList?"[]":fundCategory.fileList};
    if(fileJson && fileJson.length>0){
      if(${empty fundCategory.fundId}){
        for ( var i = 0; i < fileJson.length; i++) {
          if (fileJson[i].filePath.indexOf("fileDownload") == -1) {
             $("#viwe_files_table").append("<tr><td align=\"center\">"+(i+1)+"</td><td align=\"left\"><a href=\""+fileJson[i].filePath+"\" class=\"Blue file\" target=\"_blank\">"+fileJson[i].fileName+"</a></td><td align=\"center\"><a onclick=\"categoryThickbox.removeFile(this);\" class=\"Blue\">删除</a></td></tr>");
          }else{
            $("#viwe_files_table").append("<tr><td align=\"center\">"+(i+1)+"</td><td align=\"left\"><a href=\""+fileJson[i].filePath+"\" class=\"Blue file\">"+fileJson[i].fileName+"</a></td><td align=\"center\"><a onclick=\"categoryThickbox.removeFile(this);\" class=\"Blue\">删除</a></td></tr>");
          }
          $("#viwe_files_table").css("display","");
        }
      }else{
        for ( var i = 0; i < fileJson.length; i++) {
          if (fileJson[i].url.indexOf("fileDownload") == -1) {
            $("#viwe_files_table").append("<tr><td align=\"center\">"+(i+1)+"</td><td align=\"left\"><a href=\""+fileJson[i].url+"\" class=\"Blue file\" target=\"_blank\">"+fileJson[i].name+"</a></td><td align=\"center\"><a onclick=\"categoryThickbox.removeFile(this);\" class=\"Blue\">删除</a></td></tr>");
         }else{
           $("#viwe_files_table").append("<tr><td align=\"center\">"+(i+1)+"</td><td align=\"left\"><a href=\""+fileJson[i].url+"\" class=\"Blue file\">"+fileJson[i].name+"</a></td><td align=\"center\"><a onclick=\"categoryThickbox.removeFile(this);\" class=\"Blue\">删除</a></td></tr>");
         }
          $("#viwe_files_table").css("display","");
        }
      }
    };
    
    if($("#guideUrl").val() == ""){//网址字段为空时，默认为http://
  	   $("#guideUrl").val("http://");
     }
    if($("#declareUrl").val() == ""){//网址字段为空时，默认为http://
   	   $("#declareUrl").val("http://");
      }
    
    $("#counId").change(function(){
    	if($(this).val()!=""){
			$.ajax({
				url:ctxpath+"/fund/ajaxGetCity",
				type:"post",
				data:{"id":$(this).val()},
				timeout:10000,
				success:function(data){
					  if(data){
						if(data.length==0){
							$("#prvId").empty();
							$("#prv_div").css("display","none");
							$("#cityId").empty();
							$("#city_div").css("display","none");
							$("#quId").empty();
							$("#quId").css("display","none");
							return;
						}
						$("#prvId").empty();
						$("#prvId").append("<option value=\"\"></option>");
						for ( var i = 0; i < data.length; i++) {
							$("#prvId").append("<option value=\""+data[i].id+"\">"+data[i].name+"</option>");
						}
						$("#prv_div").css("display","");
						$("#cityId").empty();
						$("#city_div").css("display","none");
						$("#quId").empty();
						$("#quId").css("display","none");
					  }
				  },error:function(){
					  $("#prvId").empty();
					  $("#prv_div").css("display","none");
					  $("#cityId").empty();
					  $("#city_div").css("display","none");
					  $("#quId").empty();
					  $("#quId").css("display","none");
				  }
			});
		}else{
			$("#prvId").empty();
			$("#prv_div").css("display","none");
			$("#cityId").empty();
			$("#city_div").css("display","none");
			$("#quId").empty();
			$("#quId").css("display","none");
		}
    });
    
    $("#prvId").change(function(){
		if($(this).val()!=""){
			$.ajax({
				url:ctxpath+"/fund/ajaxGetCity",
				type:"post",
				data:{"id":$(this).val()},
				timeout:10000,
				success:function(data){
					  if(data){
						if(data.length==0){
							$("#cityId").empty();
							$("#quId").empty();
							$("#city_div").css("display","none");
							return;
						}
						$("#quId").empty();
						$("#quId").css("display","none");
						$("#cityId").empty();
						$("#cityId").append("<option value=\"\"></option>");
						for ( var i = 0; i < data.length; i++) {
							$("#cityId").append("<option value=\""+data[i].id+"\">"+data[i].name+"</option>");
						}
						$("#city_div").css("display","");
					  }
				  },error:function(){
					  $("#cityId").empty();
					  $("#quId").empty();
					  $("#city_div").css("display","none");
				  }
			});
		}else{
			$("#cityId").empty();
			$("#city_div").css("display","none");
		}
	});
    document.onclick=function(){
      $(".pro_fund_main-selector_box")[0].style.display="none";
    }
    $("#cityId").change(function(){
		if($(this).val()=="440300"){
			$.ajax({
				url:ctxpath+"/fund/ajaxGetCity",
				type:"post",
				data:{"id":440300},
				timeout:10000,
				success:function(data){
					  if(data){
						if(data.length==0){
							$("#quId").empty();
							$("#quId").css("display","none");
							return;
						}
						$("#quId").empty();
						$("#quId").append("<option value=\"\"></option>");
						for ( var i = 0; i < data.length; i++) {
							$("#quId").append("<option value=\""+data[i].id+"\">"+data[i].name+"</option>");
						}
						$("#quId").css("display","");
					  }
				  },error:function(){
					  $("#quId").empty();
					  $("#quId").css("display","none");
				  }
			});
		}else{
			$("#quId").empty();
			$("#quId").css("display","none");
		}
	});
   
    $("#agencyId option").each(function(){
    	var name = $(this).text();
    	if(parent.agency_name==name){
    		$(this).attr("selected","selected");
    	};
    });
    
    $("#description").blur(function(){//类别描述提取关键词
    	var description = $.trim($("#description").val());
    	/* if(description == ""){
    		return;
    	} */
    	var params = {"description":description};
    	$.ajax({
    		url:ctxpath+"/fund/ajaxGetDesKws",
    		type:"post",
    		data:params,
    		timeout:10000,
    		success:function(data){
    			$("#auto_div").find(".auto_word_div").each(function(){
					if($(this).attr("val").indexOf("description") != -1){
						$(this).attr("val","");
					}
				});
    			
    			$("#container").find(".hot_01").each(function(){//先删除
					if($(this).find("a").attr("flag").indexOf("description") != -1){
						$(this).remove();
					}
				});
    			var hotWordCount = 0;
    			$("#container").find(".hot_01").each(function(){
    				if($(this).is(":visible")){
    					hotWordCount++;
    				}
    			});
    			if(hotWordCount<1){//没有就隐藏
    				$("#hotKeyWord").css("display","none");
    			}
    			if(data){
    				if(data.length > 0){
    					for ( var i = 0; i < data.length; i++) {
    						var isRepeat = false;
    						$("#container").find(".hot_01").each(function(){
    							if($(this).attr("valText") == data[i].kwtxt){
    								isRepeat = true;
    								return;
    							}
    						});
    						if(!isRepeat){//重复则不添加
    							$("#container").append(categoryThickbox.createWordHtml(data[i].kwtxt, "description_" + i, true));
    						}
						}
    				}
    			}
    			
    		},error:function(){
    			console.log("出错了");
    		}
    	});
    });
});
</script>
</head>
<body style="overflow: hidden;">
  <div id="content" style="height: 560px; overflow-y: hidden; overflow-y: auto;">
    <input type="hidden" class="thickbox" id="thickbox_agency" /> <input type="hidden" id="constCategoryId"
      value="${fundCategory.insId>0?'':fundCategory.id}" /> <input type="hidden" id="insCategoryId"
      value="${fundCategory.id}" /> <input type="hidden" id="regionList" value="${fundCategory.regionList}" /> <input
      type="hidden" id="defTtile" value="340" /> <input type="hidden" id="defDegree" value="240" /> <input
      type="hidden" id="auditType" value="${auditType }" /><input
      type="hidden" id="fundId" value="${fundCategory.fundId }" />
    <!-- 新增或者更新 -->
    <input type="hidden" id="parentCategoryId" value="${fundCategory.parentCategoryId }" />
    <div id="con_one_1" class="pro_fund_main">
      <div class="Contentbox">
        <div id="con_two_1" class="hover">
          <c:if test="${not empty fundCategory.parentCategoryId}">
            <span><img src="${resmod}/images/ico.gif" /> <a
              href="${ctx }/fund/category/compare?categoryId=${fundCategory.id}&parentCategoryId=${fundCategory.parentCategoryId }"
              target="_blank" class="Blue b">与上个历史审核版本比较</a></span>
          </c:if>
          <p class=""
            style="margin-bottom: 10px; border-bottom: 1px #CCC dashed; line-height: 220%; padding-left: 10px;">
            <span style="font-size: 14px; font-weight: bold;">基本信息</span> <span class="Fright">带<span class="red">*</span>号的为必填项
            </span>
          </p>
          <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <tr>
              <td width="170" align="right"><span class="red">*</span>&nbsp;资助机构：</td>
              <td align="left" colspan="3">
              <div style="width: 100%; height:100%; position: relative;"> 
              <input type="text" maxlength="200"  autocomplete="off"
                  id="agencyNameId" value="${fundCategory.agencyViewName}" oninput="matchDropAgencyName(this)" class="inp_text" style="width: 610px;"  />
              <input  type="hidden" id="agencyId" name="agencyId" value="${fundCategory.agencyId}"  />
              &nbsp;&nbsp; <a onclick="agencyMaint.add();" class="Blue">新增资助机构</a>
              <input type="hidden" id="isExist" value="false"/>
              <div class="pro_fund_main-selector_box" style="display:none">
              </div>
              </div>
              
              </td>
            </tr>
            <tr>
              <td align="right"><span class="red">*</span>&nbsp;类别名称：</td>
              <td align="left" colspan="3"><input type="text" maxlength="200" name="nameZh" id="nameZh"
                class="inp_text" value="${fundCategory.nameZh}" style="width: 610px;" />&nbsp;&nbsp;<span
                class="f666">（中文）</span></td>
            </tr>
            <tr>
              <td align="right">&nbsp;</td>
              <td align="left" colspan="3"><input type="text" maxlength="200" name="nameEn" id="nameEn"
                class="inp_text" value="${fundCategory.nameEn}" style="width: 610px;" />&nbsp;&nbsp;<span
                class="f666">（英文）</span></td>
            </tr>
            <tr>
              <td align="right">资助编号：</td>
              <td align="left" colspan="3">
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="35%" style="padding-left: 0;"><input type="text" name="code" maxlength="50"
                      id="code" class="inp_text" value="${fundCategory.code}" style="width: 268px;" /></td>
                    <td align="right" style="width: 15%">资助类别简称：</td>
                    <td align="left"><input type="text" maxlength="100" name="abbr" id="abbr" class="inp_text"
                      value="${fundCategory.abbr}" style="width: 220px;" /></td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td align="right">描述：</td>
              <td align="left" colspan="3"><label for="textarea"></label> <textarea name="description"
                  id="description" cols="45" rows="5" class="inp_textarea"
                  style="width: 610px; height: 50px; color: #000000;">${fundCategory.descriptionHtml}</textarea></td>
            </tr>
            <tr>
              <td align="right"></td>
              <td align="left" valign="top" colspan="3"><a class="Blue Fleft mleft5" id="addChossen"
                onclick="categoryThickbox.addChossenDisc(this);">新增</a></td>
            </tr>
            <tr>
              <td align="right" valign="middle">关键词：</td>
              <td align="left" colspan="3">
                <div id="auto_div" name="keywordList" class="enter-name auto_word_outer_div" style="width: 600px;"></div>
              </td>
            </tr>
            <tr id="hotKeyWord" style="display: none;">
              <td></td>
              <td colspan="3" align="left">
                <div class="hot_keywords" style="width: 610px;">
                  <h4>
                    <span>建议您采用以下关键词，单击即可选中</span>
                    <div class="clear"></div>
                  </h4>
                  <div id="container" class="hot_key_list"></div>
                </div>
              </td>
            </tr>
            <tr>
              <td align="right"><span class="red">*</span>&nbsp;申请日期：</td>
              <td align="left" valign="top" colspan="3"><input type="text" name="startDate" id="startDate"
                onclick="WdatePicker()" class="inp_text"
                value="<s:date name="fundCategory.startDate" format="yyyy/MM/dd"/>" style="width: 130px;" />
                &nbsp;到&nbsp; <input type="text" name="endDate" id="endDate" onclick="WdatePicker()" class="inp_text"
                value="<s:date name="fundCategory.endDate" format="yyyy/MM/dd"/>" style="width: 130px;" /></td>
            </tr>
            <tr>
              <td align="right">预计资助金额：</td>
              <td colspan="3" align="left"><input type="text" name="strength" id="strength" class="inp_text"
                value="${fundCategory.strength}" style="width: 130px;"
                oninput="categoryThickbox.checkDeadline(this,event);"
                onpropertychange="categoryThickbox.checkDeadline(this,event);" />&nbsp;万元</td>
            </tr>
            <tr>
              <td align="right">是否配套：</td>
              <td colspan='3'>
                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                  <tr>
                    <td width="2%" style="padding-left: 0;"><select name="isMatch" id="isMatch"
                      class="inp_text3 inp_bg1" style="width: 135px;">
                        <option <c:if test="${'1' eq fundCategory.isMatch}">selected=selected</c:if> value="1">是</option>
                        <option <c:if test="${'0' eq fundCategory.isMatch}">selected=selected</c:if> value="0">否</option>
                    </select></td>
                    <td align="right" width="6%">比例：</td>
                    <td align="left" width=""><input type="text" name="percentage" id="percentage" maxlength=50
                      value="${fn:escapeXml(fundCategory.percentage)}" class="inp_text inp_bg1"
                      <c:if test="${locale=='zh_CN' }">style="width:105px;"</c:if>
                      <c:if test="${locale=='en_US' }">style="width:65px;"</c:if> /></td>
                  </tr>
                </table>
              </td>
            </tr>
            <tr>
              <td align="right">申报指南网址：</td>
              <td align="left"><input type="text" maxlength="200" name="guideUrl" id="guideUrl"
                value="${fundCategory.guideUrl}" class="inp_text" style="width: 610px;" /></td>
            </tr>
            <tr>
              <td align="right">申报网址：</td>
              <td align="left"><input type="text" maxlength="200" name="declareUrl" id="declareUrl"
                value="${fundCategory.declareUrl}" class="inp_text" style="width: 610px;" /></td>
            </tr>
            <tr>
              <td align="right">附件：</td>
              <td align="left" colspan='3'>
                <table id="viwe_files_table" width="615px;" border="0" cellspacing="0" cellpadding="0" class="tr_box"
                  style="background-color: #FFF; display: none;">
                  <thead>
                    <tr>
                      <td width="8%">序号</td>
                      <td>文件名</td>
                      <td width="20%">操作</td>
                    </tr>
                  </thead>
                </table>
                <p id="dialog_content" class="pdown10 mtop10" style="position: relative;">
                  <input id="filedata" name="filedata" style="width: 500px;" type="file" onchange="categoryThickbox.change();" /> &nbsp; <a id="upload_btn"
                    onclick="categoryThickbox.ajaxUploadFile();" style="margin-left: 60px;"
                    class="uiButton anniu f_normal">上 传</a>
                </p>
              </td>
            </tr>
            <tr>
              <td align="right" valign="middle">适合地区：</td>
              <td align="left" colspan="3">
                <table>
                  <tr>
                    <td
                      style="border-top: #FFBB00 1px dashed; border-bottom: #FFBB00 1px dashed; border-left: #FFBB00 1px dashed; border-right: #FFBB00 1px dashed;">
                      <select name="counId" id="counId" class="inp_text" style="width: 160px;">
                        <option value=""></option>
                        <s:iterator value="countryList">
                          <option value="${id}">${name}</option>
                        </s:iterator>
                    </select> <span id="prv_div" style="display: none;"> &nbsp;&nbsp; <select name="prvId" id="prvId"
                        class="inp_text" style="width: 160px;">
                          <option value=""></option>
                      </select>
                    </span> <span id="city_div" style="display: none;"> &nbsp;&nbsp; <select name="cityId" id="cityId"
                        class="inp_text" style="width: 160px;">
                          <option value=""></option>
                          <s:iterator value="agencyCityList">
                            <option value="${id}">${name}</option>
                          </s:iterator>
                      </select> <select name="quId" id="quId" class="inp_text" style="width: 103px;" style="display:none;">
                      </select>
                    </span> &nbsp;&nbsp;<a class="uiButton" onclick="categoryThickbox.putPrvCity();">添加地区</a><span style="white-space: nowrap">
                    <a style="display:block; padding-right: 16px; color: red; text-decoration: none;margin-top: 5px;">
                        *添加国家级基金时，地区要求留空</a></span>
                      <div id="prvCity_div" name="prvCityList" class="enter-name auto_word_outer_div"
                        style="width: 600px;"></div>
                    </td>
                    <td width="20%"></td>
                  </tr>
                </table>
              </td>
            </tr>
          </table>
          <p class="bargain-icon"
            style="margin-bottom: 10px; border-bottom: 1px #CCC dashed; line-height: 220%; padding-left: 10px;">
            申请资格</p>
          <table class="changeGaryTable" width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
              <td width="21%" align="right">职称：</td>
              <td align="left"><s:iterator value="titleList" var="tmp">
                  <c:if test="${tmp.id ne 340}">
                    <input type="checkbox" name="titleRequire1"
                      <c:if test="${!empty fundCategory.titleRequire1}">${fn:containsIgnoreCase(fundCategory.titleRequire1,tmp.id)?'checked=checked':'' }</c:if>
                      value="${tmp.id}" />&nbsp;${tmp.name}&nbsp;
					 							</c:if>
                </s:iterator></td>
              <td width="8%" align="left"><span class="red">*</span>&nbsp;单位要求：</td>
              <td align="left"><input type="checkbox" name="insType" value="1"
                <c:if test="${'1,2' eq fundCategory.insType || '1' eq fundCategory.insType}">checked=checked</c:if>>&nbsp;企业&nbsp;
                <input type="checkbox" name="insType" value="2"
                <c:if test="${'1,2' eq fundCategory.insType || '2' eq fundCategory.insType}">checked=checked</c:if>>&nbsp;科研机构&nbsp;
              </td>
            </tr>
            <tr>
              <td align="right">学位：</td>
              <td align="left"><select name="education" id="education" class="inp_text inp_bg1"
                style="width: 242px;">
                  <option
                    <c:if test="${'210,220,230,240' eq fundCategory.degreeRequire1 || '210' eq fundCategory.degreeRequire1}">selected=selected</c:if>
                    value="200">所有</option>
                  <option <c:if test="${'230' eq fundCategory.degreeRequire1}">selected=selected</c:if> value="230">博士或以上</option>
                  <option <c:if test="${'210,220,230' eq fundCategory.degreeRequire1}">selected=selected</c:if> value="210">本科或以上</option>
              </select></td>
            </tr>
            <tr>
              <td align="right" valign="top">出生年月：</td>
              <td align="left">从&nbsp; <input type="text" name="birthLeast" id="birthLeast" onclick="WdatePicker()"
                value="<s:date name="fundCategory.birthLeast" format="yyyy/MM/dd"/>" class="inp_text"
                style="width: 95px;" /> &nbsp;到&nbsp; <input type="text" name="birthMax" id="birthMax"
                onclick="WdatePicker()" class="inp_text" style="width: 95px;"
                value="<s:date name="fundCategory.birthMax" format="yyyy/MM/dd"/>" />
              </td>
            </tr>
          </table>
        </div>
      </div>
    </div>
  </div>
  <div class="pop_buttom">
    <c:if test="${not empty fundCategory.fundId}">
      <a id="btn_submit" onclick="categoryThickbox.saveCategory();" class="uiButton uiButtonConfirm text14">通过</a>
      <a onclick="fundThirdMaint.rejectOpt(${fundCategory.fundId})" class="uiButton uiButtonConfirm text14">拒绝</a>
      <a onclick="parent.$.Thickbox.closeWin();" class="uiButton text14 mright20">返回</a>
    </c:if>
    <c:if test="${empty fundCategory.fundId}">
    <c:choose>
      <c:when test="${empty fundCategory.insId || fundCategory.insId eq 0}">
        <a id="btn_submit" onclick="categoryThickbox.saveCategory();" class="uiButton uiButtonConfirm text14">确定</a>
        <a onclick="parent.$.Thickbox.closeWin();" class="uiButton text14 mright20">取消</a>
      </c:when>
      <c:otherwise>
        <a onclick="$('#auditType').val('save');categoryThickbox.approve();" class="uiButton uiButtonConfirm text14">批准(新增)</a>
        <c:if test="${!empty fundCategory.parentCategoryId}">
          <a onclick="$('#auditType').val('update');categoryThickbox.approve();" class="uiButton uiButtonConfirm text14">更新</a>
        </c:if>
        <a onclick="categoryThickbox.refuse();" class="uiButton text14 mright20">拒绝</a>
      </c:otherwise>
    </c:choose>
    </c:if>
  </div>
</body>
</html>