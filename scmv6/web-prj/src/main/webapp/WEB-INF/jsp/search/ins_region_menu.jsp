<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<s:if test="regionList != null && regionList.size > 0">
	<s:iterator value="regionList" status="sta" var="region">
		<div style="padding-left: 32px;">
			<a href="javascript:;" onclick="page.searchByRegion('${region.regionId}' ,this)" class="region_menu_item" style="width:100%;display:flex; align-items: center; justify-content: space-between;">
				<span class="filter_item_name"> ${region.showName } </span> <em class="fr">(${region.count })</em>
				<i name="insRegion" class="material-icons close" style="display: none;visibility: unset;">close</i>
			</a>
		</div>
	</s:iterator>
</s:if>
