<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<title>专利分析</title>
<script type="text/javascript">
var mid="${mId}";
$(document).ready(function() {
	var data;
    data=${cdata};
	piechart("main4",data);
});
</script>
<div id="main4" style="width: 800px; height: 600px;"></div>
