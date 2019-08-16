<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<%@ taglib uri="/WEB-INF/iristaglib.tld" prefix="iris"%>
<!DOCTYPE html>
<html>
<head>
  <link rel="stylesheet" href="${resmod }/css_v5/reset.css"/>
  <link rel="stylesheet" href="${resmod }/css_v5/project/unit.css"/>
  <link rel="stylesheet" href="${resmod }/css_v5/project/achievement_lt.css"/>
  <link rel="stylesheet" href="${resmod }/css_v5/project/administrator.css"/>
  <link rel="stylesheet" href="${resmod }/css_v5/project/main_list.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Insert title here</title>
<script type="text/javascript">
  function saveProject(obj){
      BaseUtils.doHitMore(obj,4000);
      BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", saveAction);
  }

  function  saveAction() {
      var prjJsonList = new Array();
      $("#con_five_1 .pending_import_prj_item").each(function(){
          var prjJsonParams = {};
          var $this = $(this);
          //是否有重复成果和对重复成果选择的操作
          var flag = $this.find("input:checked").val();  //dup_opt = refresh ; skip
          if(!BaseUtils.checkIsNull(flag)){
              prjJsonParams["dup_des3_prj_id"] = $this.attr("des3DupPrjId");
              prjJsonParams["dup_opt"] = flag;
          }else{
              //是否选中了要保存起来
              prjJsonParams["pub_save"] = 1;
          }
          prjJsonList.push(prjJsonParams);
      });
       $("#prjJsonParams").val(JSON.stringify(prjJsonList));
       $("#listForm").submit();
  }
  $(document).ready(function(){
	    var spanArray = document.getElementsByClassName("multipleline-ellipsis__content-box");
	    var pArray = document.getElementsByClassName("sie_lsit_p3");
	    for(var i=0; i<pArray.length; i++){
	        if(spanArray[i].clientHeight == 48){
	            continue;
	        }
	        if(spanArray[i].clientHeight < 48){
	            document.getElementsByClassName("sie_lsit_p3")[i].style.height = "24px";
	        }
	    }
	});
</script> 
</head>
<body>
<form id="listForm" action="/prjweb/fileimport/save" method="post">
<input type="hidden" id="xmlId" name="xmlId" value="${xmlId}"/>
  <input type="hidden" id="prjJsonParams" name="prjJsonParams" />
<div class="matter mt80" style="" >
    <div id="con_five_1" style="display:block">
        <div class="ds_jc">
            <div class="step ds_f">
                <div class="step_one step_one-ed">
                    <span>1</span>
                    <p><s:text name="prj.fileimport.stupselectfile"/></p>
                </div>
                <div class="step_one step_one-ing">
                    <span>2</span>
                    <p><s:text name="prj.fileimport.dataview"/></p>
                </div>
                <div class="step_one step_one-un">
                    <span>3</span>
                    <p><s:text name="prj.fileimport.importsuccess"/></p>
                </div>
                <div class="step_up ds_c">
                    <span class="step_up-ed wd210"></span>
                    <span class="step_up-ing wd210">
                        <span class="step_up-ing01"></span>
                    </span>
                </div>
            </div>
        </div>
        <div class="achievement_conter_right channel_achievement_conter">
            <div class="channel_achievement" style="line-height: 56px; height: 56px;">
                <p>
               <c:if test="${locale=='zh_CN'}">
                 数据预览：此文件共有 <span style="color:#1265cf;">${count}</span> 条记录。
               </c:if>
              <c:if test="${locale=='en_US'}">
                Preview: ${count} record(s) was/were found in the file.
              </c:if>
                    <%--有 <span class="redness">${hanldCount}</span> 条记录待处理。--%>
                </p>
                <div>
                    <a href="###" class="martter-demo-step" onclick="saveProject(this);"><s:text name="prj.fileimport.save"/></a>
                    <a href="/prjweb/fileimport"  class="martter-demo-browse"><s:text name="prj.fileimport.return"/></a>
                </div>
            </div>
            <div class="headline ftbold">
                <span class="sie_message_ask_left f999"><s:text name="prj.filelist.title"/></span>
                <span class="f999 fr mr70"><s:text name="prj.filelist.dupresult"/></span>
            </div>
            <!--大数据列表-->
              <c:forEach items="${prjInfoList}" varStatus="status" var="prjInfo">
                 <c:set value="${status.index+1}" var="index" scope="page"/>
                  <div class="message_ask">
                      <div class="message_big message_channer_big">
                          <div class="w550 sie_lsit_p3" style="line-height: 24px; height: 52px; max-width: 800px; width: 800px;  pointer-events: none ">
                                <input type="hidden" id="dup_id_${index}" name="dup_id_${index}" class="dup_value" value="${prjInfo.des3DupPrjId}"/>
                                <span class="multipleline-ellipsis__content-box data3" style="max-width:800px; width: 800px;" title="${prjInfo.showTitle}" >${prjInfo.showTitle}<c:if test="${!empty prjInfo.externalNo}">&nbsp;<span class="f999">(${prjInfo.externalNo})</span></c:if></span>
                          </div>
                          <p class="sie_monicker">
                            <b class="redness_jx mr46">
                              <c:if test="${prjInfo.dupPrjId >0}">
                              <s:text name="prj.filelist.sameresult"/>
                              </c:if>
                            </b>
                             <span>
                                 <span class="sie_message_ask_left ofw" style="color: #666; width: 800px;" title="${prjInfo.showAuthorNames }">${prjInfo.showAuthorNames}</span>
                             </span>
                          </p>
                           <label id="isInsert_${index}" style="display:none;color:red">
                             <input type="radio"  id="nodup_${index}" value="2" class="radiobutton" checked="checked" name="dup_flag_${index}"/>
                           </label>
                            <span class="sie_message_ask_left ofw f999">
                              <!--  项目来源 在 日期 + 金额-->
                            <c:if test="${!empty prjInfo.showBriefDesc}">${prjInfo.showBriefDesc}</c:if>
                            </span>


                          <div class="js-demo-1 fr pending_import_prj_item" style="margin-right: 40px;" des3DupPrjId="${ prjInfo.dupPrjId}">
                          <c:if test="${prjInfo.dupPrjId > 0}">
                            <label class="ml8 mr8"><input type="radio" name="dup_flag_${index}" value="refresh" checked/>&nbsp;<s:text name="prj.filelist.update"/></label>
                            <label class="ml8 mr8"><input type="radio" name="dup_flag_${index}" value="skip" />&nbsp;<s:text name="prj.filelist.skip"/></label>
                          </c:if>
                          </div>
                      </div>
                  </div>
          </c:forEach>
        </div>
    </div>
    <div id="con_five_2" style="display:none"></div>
    <div id="con_five_3" style="display:none"></div>
    <div id="con_five_4" style="display:none"></div>
</div>
</form>
</body>
</html>
