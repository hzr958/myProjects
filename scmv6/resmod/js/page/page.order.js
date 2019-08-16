var smate = smate ? smate : {};
smate.pageorder = smate.pageorder ? smate.pageorder : {};

/**
 * 重置页码
 */
smate.pageorder.resetno = function(){
	$("#pageNo").attr("value",1);
}