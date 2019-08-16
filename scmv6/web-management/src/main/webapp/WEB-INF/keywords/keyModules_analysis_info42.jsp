<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="resmod" value="/resmod" />
<script src="${resmod}/js/management/chemistry-psn.js"></script>
<script src="${resmod}/js/echarts.min.js"></script>
<script type="text/javascript">
var mid=${mId};//左侧菜单Id，0开始
var moId=${modId};//模块Id
$(document).ready(function() {
	chemistryPsn('main4',1);
	
});

</script>
