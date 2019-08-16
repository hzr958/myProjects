<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://java.sun.com/jstl/xml_rt" prefix="x"%>
<%@ taglib uri="/WEB-INF/iristaglib.tld" prefix="iris"%>
<!DOCTYPE html>
<html>
<head>
<link rel="stylesheet" href="${ressie }/css/achievement_lt.css" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Insert title here</title>
<script type="text/javascript">
  function saveProject(){
	  $("#listForm").submit();
  }
</script>
</head>
<body>
  <form id="listForm" action="/prjweb/project/savefile" method="post">
    <input type="hidden" id="xmlId" name="xmlId" value="${xmlId}" />
    <div class="matter">
      <div id="con_five_1" style="display: block">
        <div class="channel-box_cont">
          <ul class="matter_conter_flow">
            <li class="hover"><div>
                <span class="channel-bg01">1</span>
                <p>选择文件</p>
              </div></li>
            <li class="hover"><p class="channel-bt02"></p>
              <div>
                <span class="channel-bg01">2</span>
                <p>数据预览</p>
              </div></li>
            <li><p class="channel-bt02"></p>
              <div>
                <span class="channel-bg01">3</span>
                <p>导入成功</p>
              </div></li>
          </ul>
        </div>
        <div class="achievement_conter_right channel_achievement_conter">
          <div class="channel_achievement">
            <p>
              数据预览：此文件共有 <span style="color: #1265cf;">${count}</span> 条记录，有 <span class="redness">${hanldCount}</span>
              条记录待处理。
            </p>
            <div>
              <a href="#" class="martter-demo-step" onclick="saveProject();">保存</a> <a href="/prjweb/project/importfile"
                class="martter-demo-browse">返回</a>
            </div>
          </div>
          <div class="headline ftbold">
            <span class="sie_message_ask_left f999">项目名称 / 批准号 / 负责人 / 项目来源 / 起止日期 / 资助金额(万元)</span> <span
              class="f999 fr mr70">查看结果</span>
          </div>
          <!--大数据列表-->
          <x:parse xml="${xmlData}" var="output"></x:parse>
          <x:forEach select="$output/scholarWorks/data" var="refList">
            <c:set value="${index+1}" var="index" scope="page" />
            <c:set var="ctitle">
              <x:out select="$refList/project/@ZH_TITLE" />
            </c:set>
            <c:set var="etitle">
              <x:out select="$refList/project/@EN_TITLE" />
            </c:set>
            <c:set var="dup_value">
              <x:out select="$refList/project/@dup_value" />
            </c:set>
            <c:set var="PRJ_EXTER_NO">
              <x:out select="$refList/project/@PRJ_EXTER_NO" />
            </c:set>
            <c:set var="start_date">
              <x:out select="$refList/project/@start_date" />
            </c:set>
            <c:set var="end_date">
              <x:out select="$refList/project/@end_date" />
            </c:set>
            <c:set var="PRJ_FROM_NAME">
              <x:out select="$refList/project/@PRJ_FROM_NAME" />
            </c:set>
            <c:set var="amount">
              <x:out select="$refList/project/@amount" />
            </c:set>
            <c:set var="PSN_NAME">
              <x:out select="$refList/project/@PSN_NAME" />
            </c:set>
            <div class="message_ask">
              <div class="message_big message_channer_big">
                <p class="w550 sie_channer_fg">
                  <a href="#" class="data2" style="cursor: default"> <input type="hidden" id="dup_id_${index}"
                    name="dup_id_${index}" class="dup_value" value="${dup_value}" /> <iris:lable zhText="${ctitle}"
                      enText="${etitle}"></iris:lable> <span class="ml10" style="font-size: 14px;">（${PRJ_EXTER_NO}）</span>
                  </a>
                </p>
                <c:if test="${!empty dup_value}">
                  <p class="sie_monicker">
                    <b class="redness_jx mr46">项目库有相同记录</b><span><span class="sie_message_ask_left ofw">${PSN_NAME}</span></span>
                  </p>
                  <%--                                   <p class="monicker"><b class="redness_jx mr46">项目库有相同记录</b>${PSN_NAME}</p> --%>
                  <p class="sie_monicker">
                  <div class="js-demo-1 fr" style="margin-right: 32px;">
                    <label><input type="radio" name="dup_flag_${index}" value="2" />新增</label> <label><input
                      type="radio" name="dup_flag_${index}" value="1" checked />更新</label> <label><input type="radio"
                      name="dup_flag_${index}" value="0" />跳过</label>
                  </div>
                </c:if>
                <c:if test="${empty dup_value}">
                  <%--                                <p class="f666">${PSN_NAME}</p> --%>
                  <p class="sie_monicker">
                    <b></b><span><span class="sie_message_ask_left ofw">${PSN_NAME}</span></span>
                  </p>
                  <label id="isInsert_${index}" style="display: none; color: red"> <input type="radio"
                    id="nodup_${index}" value="2" class="radiobutton" checked="checked" name="dup_flag_${index}" />
                  </label>
                </c:if>
                <span class="sie_message_ask_left ofw f999"><c:if test="${!empty PRJ_FROM_NAME}">${PRJ_FROM_NAME}，</c:if>
                  <c:choose>
                    <c:when test="${empty start_date}">
                                              ${end_date}
                                           </c:when>
                    <c:when test="${empty end_date}">
                                             ${start_date}
                                           </c:when>
                    <c:otherwise>
                                               ${start_date} - ${end_date}
                                            </c:otherwise>
                  </c:choose> <c:if test="${!empty amount}">，${amount}</c:if></span>
                </p>
              </div>
            </div>
            <!--                     <div class="message_ask"> -->
            <!--                         <div class="message_big message_channer_big"> -->
            <!--                             <p class=" fg"><a href="#" class="data"> -->
            <%--                             <input type="hidden" id="dup_id_${index}" name="dup_id_${index}" class="dup_value" value="${dup_value}"/> --%>
            <%--                             <iris:lable zhText="${ctitle}" enText="${etitle}"></iris:lable> --%>
            <%--                             </a><span class="ml10">（${PRJ_EXTER_NO}）</span></p> --%>
            <%--                             <c:if test="${!empty dup_value}"> --%>
            <%--                                   <p class="monicker"><b class="redness_jx mr46">项目库有相同记录</b>${PSN_NAME}</p> --%>
            <!--                                     <p class="monicker"> -->
            <!--                                     <div class="js-demo-1 fr"> -->
            <%-- <%--                                         <label><input type="radio" name="dup_flag_${index}" value="0" />新增</label> --%>
            <%--                                         <label><input type="radio" name="dup_flag_${index}" value="1" checked/>更新</label> --%>
            <%--                                         <label><input type="radio" name="dup_flag_${index}" value="0" />跳过</label> --%>
            <!--                                     </div> -->
            <%--                             </c:if> --%>
            <%--                             <c:if test="${empty dup_value}"> --%>
            <%--                                <p class="f666">${PSN_NAME}</p> --%>
            <%--                                <label id="isInsert_${index}" style="display:none;color:red"> --%>
            <%--                                  <input type="radio"  id="nodup_${index}" value="2" class="radiobutton" checked="checked" name="dup_flag_${index}"/> --%>
            <!--                                </label> -->
            <%--                             </c:if> --%>
            <%--                             <span>${PRJ_FROM_NAME}，${start_date} - ${end_date}，${amount}</span> --%>
            <!--                             </p> -->
            <!--                         </div> -->
            <!--                     </div> -->
          </x:forEach>
        </div>
      </div>
      <div id="con_five_2" style="display: none"></div>
      <div id="con_five_3" style="display: none"></div>
      <div id="con_five_4" style="display: none"></div>
    </div>
  </form>
</body>
</html>