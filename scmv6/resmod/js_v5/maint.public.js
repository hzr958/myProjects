/**
 * 全选
 * 
 * @param obj
 * @return
 */
function checkAll(obj) {
	$("input:enabled[type='checkbox'][name='ckpub']").each(function() {
		//if(!$(this).attr("disabled"))
		//{
			$(this).attr("checked", obj.checked);
		//}
	});
}
/**
 * 成果单选
 * 
 * @param obj
 * @return
 */
function checkPub(obj) {
	var flag = true;
	if (obj.checked) {
		var cks = $("input:enabled[type='checkbox'][name='ckpub']");
		for ( var i = 0; i < cks.length; i++) {
			// 没有全部选择
			if (!cks[i].checked) {
				flag = false;
				break;
			}
		}
	} else {
		flag = false;
	}
	$(":checkbox[name='ckAll']").attr("checked", flag);
}
/**
 * 选中的成果
 * 
 * @return
 */
function selectedPubIds() {
	var pubIds = "";
	$("input:enabled[type='checkbox'][name='ckpub']").each(function() {

		if ($(this).attr("checked")) {
			if (pubIds == "") {
				pubIds = $(this).attr("value");
			} else {
				pubIds = pubIds + "," + $(this).attr("value");
			}
		}
	});
	$("#pubIds").attr("value", pubIds);
	return pubIds;
}


function ajaxSuccess(isAlert,response,action) {
	if (response) {
		if (response.result == 'success') {
			setActionMsg(response,isAlert);
			if(action)
			{
				query(action);
			}
		} else if (response.result == 'error') {
			setActionMsg(response,isAlert);
			if(action)
			{
				query(action);
			}
		}
	}
}
function query(action) {
	$("#mainForm").attr("action", action);
	$("#mainForm").submit();
}
function ajaxError(errorMsg) {
	alert(errorMsg);
}
function setActionMsg(data,isAlert) {
	if(isAlert)
	{
		$("#actionMsg").attr("value",
				"[{result:'" + data.result + "',msg:'" + data.msg + "'}]");
		alert(data.msg);
	}else{
		$("#actionMsg").attr("value",
				"[{result:'" + data.result + "',msg:'" + data.msg + "'}]");
	}
}
/**
 * 必须引用，thickbox.css,thickbox-compress.js.
 * 背景加上灰色，用tb_remove(),移除.
 * @return
 */
function overGrayBg(){
	if(document.getElementById("TB_overlay") == null){
		$("body").append("<div id='TB_overlay'><div id='TB_window'></div></div>");
		//$("#TB_overlay").click(tb_remove);
	}
	if(tb_detectMacXFF()){
		$("#TB_overlay").addClass("TB_overlayMacFFBGHack");//use png overlay so hide flash
	}else{
		$("#TB_overlay").addClass("TB_overlayBG");//use background and opacity
	}
}