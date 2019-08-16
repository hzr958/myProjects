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
  <script type="text/javascript" src="${resmod }/js/jquery.js"></script>
  <script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>群组管理</title>
<script type="text/javascript">
  function saveProject(obj){
      BaseUtils.doHitMore(obj,4000);
      //BaseUtils.checkCurrentSysTimeout("/prjweb/ajaxtimeout", saveAction);
      saveAction();
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
       //$("#prjJsonParams").val(JSON.stringify(prjJsonList));
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
<form id="listForm" action="/scmmanagement/grpmanage/saveimportgrp" method="post">
<input type="hidden" id="xmlId" name="xmlId" value="${xmlId}"/>
  <input type="hidden" id="prjJsonParams" name="prjJsonParams" />
<div class="matter mt80" style="" >
    <div id="con_five_1" style="display:block">
        <div class="ds_jc">
            <div class="step ds_f">
                <div class="step_one step_one-ed">
                    <span>1</span>
                    <p>选择文件</p>
                </div>
                <div class="step_one step_one-ing">
                    <span>2</span>
                    <p>数据预览</p>
                </div>
                <div class="step_one step_one-un">
                    <span>3</span>
                    <p>导入成功</p>
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
                 数据预览：此文件共有 <span style="color:#1265cf;">${count}</span> 条记录。
                    <%--有 <span class="redness">${hanldCount}</span> 条记录待处理。--%>
                </p>
                <div>
                    <a href="###" class="martter-demo-step" onclick="saveProject(this);">保存</a>
                    <a href="/scmmanagement/grpmanage/importgrp"  class="martter-demo-browse">返回</a>
                </div>
            </div>
            <div class="headline ftbold">
                <span class="sie_message_ask_left f999">序号 / 群组名称 / 群组简介</span>
            </div>
            <!--大数据列表-->
              <c:forEach items="${showInfos}" varStatus="status" var="grpInfo">
                  <c:set value="${status.index+1}" var="index" scope="page"/>
                  <div class="message_ask">
                     <div> ${index}</div>
                      <div class="message_big message_channer_big">
                          <div class="w550 sie_lsit_p3" style="line-height: 24px; height: 52px; max-width: 800px; width: 800px;  pointer-events: none ">
                                <span class="multipleline-ellipsis__content-box data3" style="max-width:800px; width: 800px; " title="" >${grpInfo.grpName}</span>
                          </div>
                          <p class="sie_monicker">
                            <b class="redness_jx mr46">
                            </b>
                             <span>
                                 <span class="sie_message_ask_left ofw" style="color: #666; width: 800px;" title="">${grpInfo.brief}</span>
                             </span>
                          </p>
                          <%--  <span class="sie_message_ask_left ofw f999">
                           ${prjInfo.showBriefDesc}
                            </span>--%>
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
