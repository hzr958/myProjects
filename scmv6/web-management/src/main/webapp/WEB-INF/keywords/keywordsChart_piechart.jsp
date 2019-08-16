<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
    $(document).ready(function(){
    	var text = '${keywordsDistributionJson}';
    	if (text!=''){
    	var listData = eval('(' +text+ ')');
    	piechart("my_piechart_kw", listData);
    	}
    	});
</script>
<div id="my_piechart_kw" style="width: 800px; height: 600px;"></div>