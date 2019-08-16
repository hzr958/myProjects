<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<ul class="effort">
  <li><a <s:if test="outHomePage=='true'">href='/prjweb/outside/mobileotherprjs?des3PsnId=${des3PsnId }'</s:if>
    <s:else>href='/prjweb/wechat/prjmain?des3PsnId=${des3PsnId }'</s:else>><i class="pro_icon"></i><span>${psnStatistics.prjSum }</span>
      <p>项目</p></a></li>
  <li class="dev_pubweb"><a
    <s:if test="outHomePage=='true'">href='/pubweb/outside/mobileotherpubs?des3PsnId=${des3PsnId }'</s:if>
    <s:else>href='/pub/querylist/psn?pubCount=${psnStatistics.pubSum }'</s:else>><i class="eff_icon"></i><span>${psnStatistics.pubSum }</span>
      <p>成果</p></a></li>
  <li><a <s:if test="outHomePage=='true'">href='/psnweb/outside/mobilefriendlist?des3PsnId=${des3PsnId }'</s:if>
    <s:else>href='/psnweb/mobile/friendlist'</s:else>><i class="pat_icon"></i><span>${psnStatistics.frdSum }</span>
      <p>联系人</p></a></li>
</ul>