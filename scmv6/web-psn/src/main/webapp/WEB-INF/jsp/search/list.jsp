<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "https://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="https://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=10;IE=9; IE=8; IE=EDGE" />
<title>人员列表</title>
<script type="text/javascript">
	//解决检索人员 鼠标放在高亮人名时 提示出代码样式问题
	$(function(){
		$(".person").each(function(){
		  $(this).find("p").find("#psnName").attr("title",$(this).find("span").text());
		});
	});
</script>
</head>
<body>
  <div class="cont_r" style="float: none; border-left: none; margin-left: 100px;">
    <div class="sorting_box" id="sortingbox" style="border-bottom: 2px solid #ccc;">
      <span class="f999"><s:text name='pub.menu.search.resultPre' />${page.totalCount} <c:if
          test="${page.totalCount>1 }">
          <s:text name='pub.menu.search.resultEnds' />
        </c:if> <c:if test="${page.totalCount<=1 }">
          <s:text name='pub.menu.search.resultEnd' />
        </c:if> </span>
    </div>
    <table border="0" cellpadding="0" cellspacing="0" class="tab_style1">
      <c:forEach items="${page.result}" var="psnContent" varStatus="status">
        <input name="psnId" type="hidden" value="${psnContent.des3PsnId}" />
        <tr>
          <td class="func_container-container">
            <div class="person" style="width: 515px;">
              <span style="display: none;">${psnContent.name}</span> 
              <a href="/psnweb/outside/homepage?des3PsnId=${psnContent.des3PsnId }" class="head_img" target="_blank">
                <img src="http://bj.scholarmate.com/resmod/smate-pc/img/logo_psndefault.png" onerror="this.src='${resmod}/smate-pc/img/logo_psndefault.png'" id="avata_${psnContent.psnId}" />
              </a>
              <p class="beyond_hidden" style="color: black">
                <div class="beyond_hidden-name">
                   <a id="psnName" class="beyond_hidden-name_detail" href="/psnweb/outside/homepage?des3PsnId=${psnContent.des3PsnId }" target="_blank">${psnContent.name}</a>
                </div>
                <c:if test="${not empty psnContent.openId && psnContent.openId!=0}">
                <div style="display: flex;">
               <i class="SID-container_avator"></i><p class="SID-container_text">${psnContent.openId}</p>
                </div>
                </c:if>
              </p>
              <c:choose>
                <c:when test="${not empty psnContent.insName&&not empty psnContent.position}">
                  <p class="beyond_hidden">${psnContent.insName}</p>
                  <p class="beyond_hidden">${psnContent.position}</p>
                </c:when>
                <c:when test="${not empty psnContent.insName&&empty psnContent.position}">
                  <p class="beyond_hidden">${psnContent.insName}</p>
                </c:when>
                <c:otherwise>
                  <p class="beyond_hidden">${psnContent.position}</p>
                </c:otherwise>
              </c:choose>
              <div class="kw__box kw_box_search">
                <c:forEach items="${psnContent.discList}" var="keyword">
                  <div class="kw-chip_small">${keyword.keyWords}</div>
                </c:forEach>
              </div>
            </div>
            <div class="func_container-psnlist" style="margin-left: 150px;" psnId="${psnContent.psnId }">
              <div class="func_container-item" style="width: 100px !important;">
                <c:if test="${psnContent.prjSum > 0}">
                  <div class="func_container-item_cnt dev_prjsum">${psnContent.prjSum }</div>
                  <div class="">
                    <s:text name="psn.search.friend.item.prj.sum" />
                  </div>
                </c:if>
              </div>
              <div class="func_container-item dev_down-count" style="width: 100px !important;">
                <c:if test="${psnContent.pubSum > 0}">
                  <div class="func_container-item_cnt dev_pubsum">${psnContent.pubSum }</div>
                  <div class="">
                    <s:text name="psn.search.friend.item.pub.sum" />
                  </div>
                </c:if>
              </div>
            </div>
            <div class="psnlist_search_actions">
              <c:if test="${psnContent.needButton }">
                <button class="button_main button_dense button_primary"
                  onclick="MainBase.addOneFriend('${psnContent.des3PsnId }',this)">
                  <s:text name="psn.find.friend.item.addfriend" />
                </button>
              </c:if>
            </div>
          </td>
        </tr>
      </c:forEach>
    </table>
    <jsp:include page="/common/psn-page-tages.jsp"></jsp:include>
    <div class="clear"></div>
  </div>
</body>
</html>
