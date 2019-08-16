<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>

<c:if test="${jsonDynInfo.DYN_TYPE == 'GRP_ADDFILE' || jsonDynInfo.DYN_TYPE == 'GRP_ADDPUB' || jsonDynInfo.DYN_TYPE == 'GRP_ADDCOURSE' || jsonDynInfo.DYN_TYPE == 'GRP_ADDWORK'}">
    上传了
</c:if>
<c:if test="${jsonDynInfo.DYN_TYPE == 'GRP_SHAREPUB' || jsonDynInfo.DYN_TYPE == 'GRP_SHAREPDWHPUB' || jsonDynInfo.DYN_TYPE == 'GRP_SHAREFILE' || jsonDynInfo.DYN_TYPE == 'GRP_SHAREAGENCY'
 || jsonDynInfo.DYN_TYPE == 'GRP_SHAREPRJ' || jsonDynInfo.DYN_TYPE == 'GRP_SHAREFUND'}">
    分享了
</c:if>