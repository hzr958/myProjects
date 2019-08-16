<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ taglib prefix='fmt' uri="http://java.sun.com/jsp/jstl/fmt"%>
<c:set var="resmod" value="/resmod" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "https://www.w3.org/TR/html4/loose.dtd">
<title>专利矩阵</title>
<script src="${resmod}/js/heatmap.js"></script>
<script src="${resmod}/js/echarts.min.js"></script>
<script type="text/javascript">
var data =${mdata};
var measures =${measures};
var planning =${planning};
var cmax =${cmax};
$(document).ready(function() {
	heatmap('main4',data,measures,planning,cmax);
});

</script>
