<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="characterList != null && characterList.size > 0">
	<s:iterator value="characterList" status="sta" var="character">
	<s:if test="#character.count > 0">
		<div name="insCharacter" style="padding-left: 32px;">
	</s:if>
	<s:else>
		<div name="insCharacter">
	</s:else>
     <c:if test="${character.count == 0}">
		    <a  class="menuList_list-container_none"  href="javascript:;" style="display: flex; align-items: center; justify-content: space-between;">
                <span class="menuList-papper__style">${character.showName }</span> <em class="fr">(${character.count })</em><i
                name="insCharacter" class="material-icons close" style="display: none;visibility: unset;">close</i>
            </a>
      </c:if>
      <c:if test="${character.count > 0}">
            <a href="javascript:;"  onclick="page.searchByCharacter('${character.characterId }',this)" style="display: flex; align-items: center; justify-content: space-between;">
                <span class="menuList-papper__style">${character.showName }</span> <em class="fr">(${character.count })</em><i
                name="insCharacter" class="material-icons close" style="display: none;visibility: unset;">close</i>
            </a>
       </c:if>
		</div>
	</s:iterator>
</s:if>