/**
 * 通用的排序JS控件.
 */
ScholarCommonOrder = function(){};
//排序内容的标头属性
ScholarCommonOrder.orderbyAttr = "orderBy";
//排序类别的标头属性
ScholarCommonOrder.valueAttr = "order";
//保存排序类别的hiddenID
ScholarCommonOrder.orderbyId = "orderBy";
//保存排序值的hiddenID
ScholarCommonOrder.valueId = "order";
//标识为排序标头的class
ScholarCommonOrder.headerClass = "headOrder";
//默认的formID
ScholarCommonOrder.formId = "mainForm";
//默认的formMethod
ScholarCommonOrder.formMethod = "post";
//action url
ScholarCommonOrder.formUrl = "post";
//action url
ScholarCommonOrder.res = "";
/**
 * 初始化，页面加载时调用，需传入actionurl.
 */
ScholarCommonOrder.init = function(formUrl,res, queryFn){
	ScholarCommonOrder.formUrl = formUrl;
	ScholarCommonOrder.res = res;
	//绑定排序值
	ScholarCommonOrder.initOrderValue();
	//表头排序
	$("."+ScholarCommonOrder.headerClass).click(function(){
		
		ScholarCommonOrder.doOrder(this, queryFn);
	});
};
/**
 * 执行排序操作.
 */
ScholarCommonOrder.doOrder = function(obj, queryFn){
	$("#pageNo").val("1");
	var orderByValue=$(obj).attr(ScholarCommonOrder.orderbyAttr);
	$("#"+ScholarCommonOrder.orderbyId).val(orderByValue);
	$('#orderBy').val(orderByValue);
	var order=$(obj).attr(ScholarCommonOrder.valueAttr);
	if($.trim(order)==""){
		order="asc";
	}else{
		if(order=="desc"){
			order="asc";
		}else if(order=="asc"){
			order="desc";
		}else{
			order="asc";
		}

	}
	$('#order').val(order);
	$("#"+ScholarCommonOrder.valueId).val(order);
	 
	ScholarCommonOrder.initOrderValue(obj);

	if (typeof queryFn == 'function') {
		queryFn();
	} else {
		$("#"+ScholarCommonOrder.formId).attr("method",ScholarCommonOrder.formMethod);
		$("#"+ScholarCommonOrder.formId).attr("action",ScholarCommonOrder.formUrl);
		$("#"+ScholarCommonOrder.formId).submit();
	}
};
/**
 * 绑定排序值.
 */
ScholarCommonOrder.initOrderValue = function(obj){
	var order =$("#"+ScholarCommonOrder.valueId).val();
	var orderBy =$("#"+ScholarCommonOrder.orderbyId).val();
	$("."+ScholarCommonOrder.headerClass).each(function(){
		var orderName=$(this).attr(ScholarCommonOrder.orderbyAttr);
		if(orderBy==orderName){
			$(this).parent().find('i').remove("i");
			if(order == 'desc')
			   $("<i class='shear_head-down' />").insertAfter($(this));
			else
				$("<i class='shear_head-up' />").insertAfter($(this));
			$(this).attr(ScholarCommonOrder.valueAttr,order);
		}
	});
};