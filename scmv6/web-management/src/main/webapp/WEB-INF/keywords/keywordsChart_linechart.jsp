<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
    $(document).ready(function(){
    	var text = '${keywordsCountByYearJson}';
    	var keywords = '${keywordsCountByYearJson}';
    	if(text!=''||keywords!=''){
    	var thisYear = new Date().getFullYear();
    	var years = [thisYear - 5, thisYear - 4, thisYear - 3, thisYear - 2, thisYear - 1];
    	var mapData = eval('(' +text+ ')');
    	var kws = eval('(' +keywords+ ')');
    	linechart("my_linechart_kw", mapData, kws, years);
    	}
    	});
</script>
<div id="my_linechart_kw" style="width: 800px; height: 600px;"></div>