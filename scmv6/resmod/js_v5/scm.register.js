/**
 * 科研之友未登录首页页面的JS事件.
 * @author MJG
 * @since 2012-08-10
 */
var SCMRegister=SCMRegister?SCMRegister:{};
SCMRegister.transfer=SCMRegister.transfer?SCMRegister.transfer:{};
SCMRegister.autocomplete=SCMRegister.autocomplete?SCMRegister.autocomplete:{};//注册页面的自动加载控件的JS.
SCMRegister.step=SCMRegister.step?SCMRegister.step:{};//注册步骤页面的JS.
/**
 * 初始化目前身份.
 */
SCMRegister.initRadio = function() {
	$("input[type=radio][name=positionType]").each(function() {
		var value = $(this).attr("value");
		if ($(this).attr("checked")) {
			if ($(this).attr("value") == "2") {
				$("#workBegin").hide();
				$("#studyBegin").hide();
			} else if ($(this).attr("value") == "1") {
				$("#workBegin").hide();
				$("#studyBegin").show();
			} else {
				$("#studyBegin").hide();
				$("#workBegin").show();
			}
			$("tr[name=positionType" + value + "]").show();
		} else {
			$("tr[name=positionType" + value + "]").hide();
		};
	});
};
/**
 * 标签 checkedall 的点击事件.
 */
SCMRegister.transfer.checkedall = function(obj) {
	if ($(obj).attr('checked')) {
		$("#registerButton").removeAttr("disabled");
	} else
		//$("#submit1").attr("disabled", true);
		$("#registerButton").attr("disabled", true);
};

/**
 * 
 */


/**
 * 拆分中文姓名自动填充注册用户的姓、名信息.
 */
SCMRegister.ajaxpinyin= function(name) {
	var value=$(name).val();
	//姓名内容为中文且first_name和last_name输入框显示内容为空，则进行拆分.
	if(/^[\u4e00-\u9fa5]+$/i.test(value) && $("#last_name").attr("value")=="" && $("#first_name").attr("value")==""){
		$.ajax( {
			url : ctx+'/register/pinyin',
			type : 'post',
			dataType:'json',
			data : {'name':value},
			success : function(data) {
				if($("#last_name").val()==""){
					$("#last_name").attr("value",data.lastName);
				}
				if($("#first_name").val()==""){
					$("#first_name").attr("value",data.firstName);
				}
				$("label[for='last_name']").hide();
				$("label[for='first_name']").hide();
			}
		});
	};
};
/**
 * 注册数据提交初始化验证.
 */
SCMRegister.initCommitData = function() {
	var name=$("#name");
	//如果注册页面的中文姓名内容不为空，则自动填充用户的姓、名输入框.
	if(name.val()!=""){
		SCMRegister.ajaxpinyin(name);
	}
	$("#from_month").bind("change", function() {
		var fromTime = $("#fromTime");
		if ($.trim(fromTime.val()) != "") {
			$(".error[for='fromTime']").hide();
		};
	});
};
/**
 * 标签insName的自动完成加载事件.
 */
SCMRegister.autocomplete.insName=function(){
	$("#insName").complete({"ctx":"/scmwebsns","key":"ins_name","bind":{"code":"insId","enName":"enName","callback":function(data){
		$("#old_insName").val(data.name);
	}}});
};

/**
 *  标签unit单位院系自动完成加载事件.
 */
SCMRegister.autocomplete.insUnit=function(){
	 $("#unit").complete({
                "ctx": ctxpath,
                "key": "insUnit",
				    "formatItem": $.ins_unit_formatItem,
					 "extraParams":{insName:function(){return $.trim($("#insName").val());}}
     });
};
/**
 * 标签colleageName的自动完成加载事件.
 */
SCMRegister.autocomplete.colleageName =function(){
	$("#colleageName").complete({"ctx":"/scmwebsns","key":"ins_name","bind":{"code":"colleageId","callback":function(data){}}});
};
/**
 * 标签position的自动完成加载事件.
 */
SCMRegister.autocomplete.position = function() {
	$("#position").complete({"ctx":"/scmwebsns","key":"position","bind":{"callback":function(data){
		$("#posId").val(data.id);
		$("#posGrades").val(data.grades);
	}}});
};
/**
 * 验证单个标签内容是否为空.
 */
SCMRegister.validateParams=function(name,labelName,content){
	var params=$("#"+name).val();//获取要查询的标签的值.
	if(params==''){
		var errorLabel=$("#"+labelName);//获取提示标签的标签名.
		errorLabel.text(content);//加载提示内容.
		return false;
	}else{
		return true;
	}
};
/**
 * @keyword_auto 学科代码.
 * @param i 序号
 * @param val 关键词值.
 * @param text 关键词显示内容.
 * @param locale 语言类型(zh-中文；en-英文)
 */
SCMRegister.step.initKeyword=function(keyword_auto,i,val,text,locale){
	keyword_auto[locale+"_auto_disckey_outer_div_"+i].put(val,text);//添加关键词.
};
/**
 * 响应注册步骤页面的删除学科代码请求，清空学科代码与关键字并隐藏学科代码输入框与对应的关键词.
 */
SCMRegister.step.deleteDisc=function(thiss){
	//获取删除链接标签的ID,取其中的序号.
	var id_arr=$(thiss).attr("id").split("_");
	var i_count=id_arr[1];
	var i_div=$("#disc_count_"+i_count);
	
	//原删除学科代码事件逻辑(隐藏div标签块)
	var i_disc=i_div.find(".discipline");
	i_disc.find("option:selected").val('');
	i_disc.find("option:selected").text(select_label);  //获取Select选择的Text 
	i_disc.attr("disc_code",'');
	//$.autoword["zh_auto_disckey_outer_div_"+i_count].clear();
	//$.autoword["en_auto_disckey_outer_div_"+i_count].clear();
	//刷新注册步骤页面的学科代码的显示序号.
	SCMRegister.step.refreshDiscNum(i_count);
	SCMRegister.step.updateCode(i_count,'5');//将当前的div移动到最后一个位置学科代码，并隐藏.
	i_div.insertAfter($("#disc_count_4"));
	i_div.css("display","none");
	//如果页面显示的学科代码框少于5个，则显示添加按钮.
	var boxCount = SCMRegister.step.findDiscBoxCount('1');
	if(boxCount<5){
		$("#add_select").show();
	}
};
/**
 * 刷新注册步骤页面的学科代码的显示序号.<执行删除学科代码事件deleteDisc时同步操作>
 */
SCMRegister.step.refreshDiscNum=function(i_count){
	
	//刷新所有序号大于当前删除的学科代码的标签序号.
	for(var i=i_count;i<5;i++){
		var num=parseInt(i)+1;
		SCMRegister.step.updateCode(num,i);
	}
};
/**
 * 获取div标签并刷新其子标签的序号.
 * @param num 改变前的序号.
 * @param i 改变后的序号.
 */
SCMRegister.step.updateCode=function(num,i){
	var temp_disc = $("#disc_count_"+num);
	
	//if(temp_disc.is(":visible")){//是否可见
		var tr_item=temp_disc.find("#item_select_"+num);
		//更新学科代码序号.
		tr_item.find(".cuti").html(disc_label+i+' '+colon_label);
		//更新学科代码标签.
		tr_item.find(".discipline").attr("id","discipline_"+i);
		tr_item.find(".discipline").attr("name","discipline_"+i);
		//更新删除链接标签和错误提示标签.
		tr_item.find("#a_"+num).attr("id","a_"+i);
		tr_item.find("#p_"+num).find(".error").attr("id","discipline_"+i+"_error");
		tr_item.find("#p_"+num).attr("id","p_"+i);
		tr_item.attr("id","item_select_"+i);
		/*
		//更新关键词的行序号.
		var tr_keyword=temp_disc.find("#item_zh_"+num);
		tr_keyword.find(".inp_textarea").attr("id","zh_auto_disckey_outer_div_"+i);
		tr_keyword.attr("id","item_zh_"+i);
		var tr_keyword=temp_disc.find("#item_zh_"+num);
		tr_keyword.find(".inp_textarea").attr("id","en_auto_disckey_outer_div_"+i);
		temp_disc.find("#item_en_"+num).attr("id","item_en_"+i);
		*/
		//更新div标签的序号.
		temp_disc.attr("id","disc_count_"+i);
	//}
};
/**
 * 注册步骤页面提交保存研究领域设置的响应事件.
 */

//解决因自动填词插件延时引起的问题 by zk SCM-3293
SCMRegister.step.submit_discEx = function(){
	setTimeout(SCMRegister.step.submit_disc,200);
}
SCMRegister.step.submit_disc=function(){
	//获取关键词的内容.
	var keysArr = [];
   var keyStr = "";
	var keys = $.autoword['auto_disckey_outer_div'].words();
	 $.each(keys, function(){
	        keysArr.push({
	            'keys': this["text"]
	        });
	        keyStr = keyStr.concat(this["text"]).concat("; ");
	    });

	  var str_key = JSON.stringify(keysArr);
	  if (str_key == "[]") {//关键词为空
	        str_key = '[{"keys":""}]';//区分学科领域与关键词
	    }
	  var post_data = {
	        'strDisc': str_key,
	        'permission':'0'
	    };
	$.proceeding.show();
	$("#save_personal_btn").attr("disabled","true");
	setTimeout(function(){SCMRegister.step.doSubmit_disc(post_data);},1000);
	
};
/**
 * 将控件方法得到的关键词转换为所需格式的json.
 */
SCMRegister.step.format_keyword=function(div_con){
	var disc_kw=[];
	for(var i=0;i<div_con.length;i++){
		var item = div_con[i];
		item.text = item.text=="undefined"?"":item.text;//名称.
		item.val = item.val=="undefined"?"":item.val;//ID.
		disc_kw.push({
            'key_id': item.val,
            'key_words': item.text
        });
	}
	return disc_kw;
};
/**
 * 请求后台保存学科代码和关键词.
 */
SCMRegister.step.doSubmit_disc=function(post_data){
	
	$.ajax( {
		url: ctxpath + '/profile/ajaxSavePersonal',
	   type: 'post',
	   dataType: 'json',
	   data: post_data,
		success : function(data) {
			if(data.result == 'server_error'){
				$.scmtips.show('error',failed_msg);
			}else{
				location.href=ctx+"/wizard/wizard2";
				$.proceeding.hide();
			}
			$("#save_personal_btn").removeAttr("disabled");
		},
		error:function(){
			$.scmtips.show('error',failed_msg);
			$("#save_personal_btn").removeAttr("disabled");
		}
	});
};
/**
 * 学科代码的点击事件.<点击打开学科代码选择框时隐藏错误提示内容>
 */
SCMRegister.step.click_disc=function(thiss){
	var disc_id=$(thiss).attr("id").split("_");
	var i_count=disc_id[1];
	$("#discipline_"+i_count+"_error").parent().css("display","none");
};
/**
 * 扩充学科代码和关键词编辑区域.
 */
SCMRegister.step.add_disc = function(){
	var i_count=SCMRegister.step.findDiscBoxCount('0');
	if(i_count<5){
		var tmpDiv = $("#disc_model").clone();//获取研究领域和关键词的模版.
		tmpDiv.css("display", "");//div设置为可显示.
		var j_count=i_count+1;
		//修改标签的ID.
		tmpDiv.attr("id","disc_count_"+j_count);
		var tmp_content=tmpDiv.html();
		//替换标签内容中的序号.
		tmp_content=tmp_content.replace(new RegExp("\\{0\\}", "g"), j_count);
		tmpDiv.html(tmp_content);
		if(i_count>0){
			//获取ID最大的学科领域标签.
			var max_disc=$("#disc_count_"+i_count);
			//添加学科代码框.
			max_disc.parent().append(tmpDiv);
		}else{
			$("#disc_td").append(tmpDiv);
		}
	}
};
/**
 * 统计当前有多少个学科领域输入框.
 * @param type 统计类型(0-全部；1-显示的；2-隐藏的)
 */
SCMRegister.step.findDiscBoxCount=function(type){
	var count = 0;
	for(var i=1;i<=5;i++){
		var tempId = "disc_count_"+i;
		if($("#"+tempId).attr("id")==tempId){//是否可见.
			if(type=='1'){
				if($("#"+tempId).is(":visible")){
					count++;
				}
			}else if(type=='2'){
				if($("#"+tempId).css("display")=='none'){
					count++;
				}
			}else{
				count++;
			}
		}
	}
	return count;
};
/**
 * 修改需显示的学科领域输入框数.
 * @param type 类型(init-初始化显示个数；show-增加显示一个学科领域的输入框)
 */
SCMRegister.step.changeDiscBoxCount=function(type){
	if(type=='init'){
		//统计所有的div标签块的个数.
		var i_count=SCMRegister.step.findDiscBoxCount('0');
		if(i_count<5){
			//不够5个时补够5个.
			for(var i=i_count;i<5;i++){
				SCMRegister.step.add_disc();
			}
		}
		//隐藏三个以上的学科领域输入框.
		for(var j=3;j<5;j++){
			var tempId = "disc_count_"+(j+1);
			$("#"+tempId).hide();
		}
	}
	if(type=='show'){
		//统计显示的与隐藏的div标签块的个数.
		var v_count=SCMRegister.step.findDiscBoxCount('1');
		var s_count=SCMRegister.step.findDiscBoxCount('2');
		if(v_count<5){
			//如果有未显示的div标签，则增加显示一个.
			if(s_count>0){
				$("#disc_count_"+(v_count+1)).show();
				var val=$("#disc_count_"+(v_count+1)).find("option:selected").val();
				if(val==''){
					//$.autoword["zh_auto_disckey_outer_div_"+(parseInt(v_count)+1)].clear();
					//$.autoword["en_auto_disckey_outer_div_"+(parseInt(v_count)+1)].clear();
				}
				
			}else{//否则增加一个.
				SCMRegister.step.add_disc();
			}
			//如果增加后的标签块达到5个，则隐藏添加按钮.
			if((parseInt(v_count)+1)==5){
				$("#add_select").hide();
			}
		}
	}
};
/**
 * 绑定学科代码插件.
 */
SCMRegister.step.bindDiscipline=function(){
	
	var keyDiv = $.autoword["auto_disckey_outer_div"];	
	$(".disc_input_class").disccomplete({
        "ctx":"/scmwebsns",
		"respath":"/resscmwebsns",
		"supportExt":true,//是否支持推荐插件
        "allowUserInput":false,//是否允许用户输入，不采用nsfc学科
		"discLevel" : 3,
		"keyDiv":keyDiv,
		"act":"creatGroup",
		"input_width":378,
		"extBind":function(result, type, _close){
			if(type=="save"){
				var _input = $(result["obj"]).prev(".disc_input");
	            if (_input != null ) {
	                var _val = result["val_ext"]();//ext结果集
	                _input.attr("code", _val == null ? "" : _val["disc_code"]);
	                _input.val(_val == null ? "" : _val["name"]);
					_input.attr("disc_id", _val == null ? "" : _val["id"]);
				}
        	}
		},
		"discBind":function(result, type, _close){
			if(type=="save"){
				var _input = $(result["obj"]).prev(".disc_input");
	            if (_input != null) {
	                var _val = result["val"]();//disc结果集
	                _input.attr("code", _val == null ? "" : _val["disc_code"]);
	                _input.val(_val == null ? "" : _val["name"]);
					_input.attr("disc_id", _val == null ? "" : _val["id"]);
				}
        	}
		}
    });
};
/**
 * 控制显示注册步骤页面的信息完整度计算方式.
 */
var workRemindTimeout;
SCMRegister.step.fadeInfo=function(){
	if (workRemindTimeout != null
			&& typeof (workRemindTimeout) != "undefined") {
		clearTimeout(workRemindTimeout);
	}
	//触发本事件的a标签.
	var top = $("#complete_compute_link").offset().top;    //获取a的居上位置
	var left = $("#complete_compute_link").offset().left;    //获取a的居左位置
	var a_height = $("#complete_compute_link").height();   //获取a的高度
	//var a_width1 = $("#complete_compute_link").width();       //获取a的宽度
	//var div_height = $("#complete_compute_div").height();   //获取div的高度
	var div_width = $("#complete_compute_div").width();       //获取div的宽度
	$("#complete_compute_div").css({'top':top+a_height+10,'left':(left-10)-div_width/2});//设置DIV的css属性

	$("#complete_compute_div").fadeIn();
};
SCMRegister.step.fadeOut=function(){
	workRemindTimeout = setTimeout(function() {
		$("#complete_compute_div").fadeOut();
	}, 100);
};
/**
 * 注册步骤-通过Web邮件通讯录添加联系人 页面 全选按钮的响应事件(是否选中复选框).
 */

SCMRegister.step.selectAll=function(){
	var jsonCopyFormArr = "";
	var jsonDelFormArr = "";
	//如果全选按钮选中，则选中所有的checkbox标签.
	if($("#box_all").attr("checked")){
		//选中页面的checkbox标签的选中状态.
		$("input[name='imp_email']").each(function() { 
			if ($(this).attr("disabled") != "disabled") {
				$(this).attr("checked", true);
				jsonCopyFormArr += SCMRegister.step.copyimpmailall(this) + ";";
			}
		});
	}else{
		//取消页面的checkbox标签的选中状态.
		$("input[name='imp_email']").each(function() { 
			$(this).attr("checked",false);
			jsonDelFormArr += SCMRegister.step.copyimpmailall(this) + ";";
		});
	}
	
	if(jsonCopyFormArr.length > 0){
		$.ajax({
			url:ctxpath+'/wizard/saveCopy',
			type:'post',
			data:{'jsonCopyForm':jsonCopyFormArr},
			dataType : "json",
			timeout: 10000,
			success: function(data){
			}
		});
	}
	
	if(jsonDelFormArr.length > 0){
		 $.ajax({
				url:ctxpath+'/wizard/delCopy',
				type:'post',
				data:{'jsonDelForm':jsonDelFormArr},
				dataType : "json",
				timeout: 10000,
				success: function(data){
				}
			});
	}
};


/**
 * 注册步骤-通过Web邮件通讯录添加联系人 页面-要添加人员框中复选框的响应事件(是否取消全选).
 */
SCMRegister.step.removeSelectAll=function(){
	var flag=true;
	//检查页面的checkbox标签是否处于选中状态.
	$("input[name='imp_email']").each(function() { 
		if(!$(this).attr('checked')){
			flag=false;
		}
	});
	if(!flag)
		$("#box_all").attr("checked",false);
	else
		$("#box_all").attr("checked",true);
};


SCMRegister.step.copyimpmailall=function(thiss){
	var sendPsnId = $(thiss).val();
	var sendPsnMail = $(thiss).attr("alt");
	var sendPsnName = $(thiss).attr("title");
	var id ="tomail_psn_"+(sendPsnId==''?sendPsnMail:sendPsnId);
	//判断复选框是否选中，如果选中则添加.
	if($(thiss).attr("checked")){
		var flag = false;
		$("#copy_checked_mail").find("li").each(function(){
			var delPnsId = $(this).find("input[name='send_psn_id']").val();
			var delPsnMail =  $(this).find("input[name='send_psn_email']").val();
			var delPsnName =  $(this).find("input[name='send_psn_name']").val();
			var id2 ="tomail_psn_"+(delPnsId==''?delPsnMail:delPnsId);
			if(id==this.id && sendPsnName == delPsnName){
					flag=true;
					return false;
			}
		});
		if(!flag){
			//往已选择人员框中添加人员记录
			SCMRegister.step.appendCopyInfo(id,sendPsnName,sendPsnId,sendPsnMail);
			//请求后台保存已复制数据.
			var jsonCopyForm = JSON.stringify({"id":id,"psnId":sendPsnId,"psnEmail":sendPsnMail,"psnName":sendPsnName});
			return jsonCopyForm;
		}
	}else{
		var delPnsId="";
		var delMail="";
		var jsonDelForm = "";
		//在已选择人员框中查找与ID匹配的记录并确定是否请求后台对记录进行删除.
		$("#copy_checked_mail").find("li").each(function(){ 
			var delName =  $(this).find("input[name='send_psn_name']").val();
			if(id==this.id && sendPsnName == delName){
				delPnsId = $(this).find("input[name='send_psn_id']").val();
				delMail =  $(this).find("input[name='send_psn_email']").val();
				$(this).remove(); 	
				SCMRegister.step.delChecked(delPnsId,delMail,delName);
				jsonDelForm = JSON.stringify({"id":id,"psnId":delPnsId,"psnEmail":delMail,"psnName":delName});				
			}
		});
		return jsonDelForm;
	}
};

/**
 * 注册步骤-通过Web邮件通讯录添加联系人 页面 复选框按钮的响应事件(是否复制标签信息到“已选择人员”的框中).
 */
SCMRegister.step.copyimpmail=function(thiss){
	var sendPsnId = $(thiss).val();
	var sendPsnMail = $(thiss).attr("alt");
	var sendPsnName = $(thiss).attr("title");
	var id ="tomail_psn_"+(sendPsnId==''?sendPsnMail:sendPsnId);
	//判断复选框是否选中，如果选中则添加.
	if($(thiss).attr("checked")){
		var flag = false;
		$("#copy_checked_mail").find("li").each(function(){
			var delPnsId = $(this).find("input[name='send_psn_id']").val();
			var delPsnMail =  $(this).find("input[name='send_psn_email']").val();
			var delPsnName =  $(this).find("input[name='send_psn_name']").val();
			var id2 ="tomail_psn_"+(delPnsId==''?delPsnMail:delPnsId);
			if(id==this.id && sendPsnName == delPsnName){
					flag=true;
					return false;
			}
		});
		if(!flag){
			//往已选择人员框中添加人员记录
			SCMRegister.step.appendCopyInfo(id,sendPsnName,sendPsnId,sendPsnMail);
			//请求后台保存已复制数据.
			var jsonCopyForm = JSON.stringify({"id":id,"psnId":sendPsnId,"psnEmail":sendPsnMail,"psnName":sendPsnName});
			$.ajax({
				url:ctxpath+'/wizard/saveCopy',
				type:'post',
				data:{'jsonCopyForm':jsonCopyForm},
				dataType : "json",
				timeout: 10000,
				success: function(data){
				}
			});
		}
	}else{
		SCMRegister.step.delSendMailPsn(id, sendPsnName);
	}
};


/**
 * 注册步骤-通过Web邮件通讯录添加联系人 页面 复选框按钮的响应事件(是否删除未选中记录在“已选择人员”的框中的信息).
 */
SCMRegister.step.delSendMailPsn=function(id, name){
	var delPnsId="";
	var delMail="";
	//在已选择人员框中查找与ID匹配的记录并确定是否请求后台对记录进行删除.
	$("#copy_checked_mail").find("li").each(function(){ 
		var delName =  $(this).find("input[name='send_psn_name']").val();
		if(id==this.id && name == delName){
			delPnsId = $(this).find("input[name='send_psn_id']").val();
			delMail =  $(this).find("input[name='send_psn_email']").val();
			$(this).remove(); 	
			SCMRegister.step.delChecked(delPnsId,delMail,delName);
			var jsonCopyForm = JSON.stringify({"id":id,"psnId":delPnsId,"psnEmail":delMail,"psnName":delName});
			 $.ajax({
				url:ctxpath+'/wizard/delCopy',
				type:'post',
				data:{'jsonDelForm':jsonCopyForm},
				dataType : "json",
				timeout: 10000,
				success: function(data){
				}
			});
		}
	});
};
/**
 * 注册步骤-通过Web邮件通讯录添加联系人 页面 复选框按钮的响应事件(同步去掉导入框中选中状态).
 */
SCMRegister.step.delChecked=function(delPnsId,delMail,delName){
	//同步去掉导入框中选中状态.	
	$("#search_resutl").find("li").each(function() { 
		var sendPsnId = $(this).find("input[name='imp_email']").val();
		var sendMail = $(this).find("input[name='imp_email']").attr('alt');
		var sendName = $(this).find("input[name='imp_email']").attr('title');
		if((delPnsId!="" && delPnsId==sendPsnId) || (delMail!="" && delMail==sendMail) && (delName!="" && delName==sendName)){
			$(this).find("input[name='imp_email']").attr('checked',false);	
		}
	});
	SCMRegister.step.removeSelectAll();
};
/**
 * 注册步骤-通过Web邮件通讯录添加联系人 页面 往已选择人员框中添加人员记录.
 */
SCMRegister.step.appendCopyInfo=function(id,sendPsnName,sendPsnId,sendPsnMail){
	var flag=false;
	//如果已经添加了选择的人员，则将待添加的与已添加的人员进行比对.
	if($('#copy_checked_mail').has('li').length>0){
		$('#copy_checked_mail').find("li").each(function(){
			var name_content=$(this).find(".add_name").text();
			//如果已添加该人员，则不予再次添加.
			if(sendPsnName==name_content){
				flag=true;
			}
		});
	}
	if(!flag){
		//$('#copy_checked_mail').find("li").each(function(){});
		var tmpDiv = $("#copy_mail_model").clone();//获取已选择人员单条记录的模版.
		tmpDiv.css("display", "");//标签设置为可显示.
		//标签的ID.
		tmpDiv.attr("id",id);
		var tmp_content=tmpDiv.html();
		//链接标签的参数.
		tmp_content=tmp_content.replace(new RegExp("\\{0\\}", "g"), id);
		//人员ID.(ID为空时设置值为 -1)
		if(sendPsnId!=''){
			tmp_content=tmp_content.replace(new RegExp("\\{1\\}", "g"), sendPsnId);
		}else{
			tmp_content=tmp_content.replace(new RegExp("\\{1\\}", "g"), '-1');
		}
		//人员名称.
		if(sendPsnName!=''){
			tmp_content=tmp_content.replace(new RegExp("\\{2\\}", "g"), sendPsnName);
		}
		//Email地址.
		if(sendPsnMail!=''){
			tmp_content=tmp_content.replace(new RegExp("\\{3\\}", "g"), sendPsnMail);
		}
		tmpDiv.html(tmp_content);
		//添加已选择人员.
		$('#copy_checked_mail').append(tmpDiv);
	}
	
};

/**
 * 注册步骤-通过Web邮件通讯录添加联系人 页面 发送邀请邮件处理事件，由发送邀请邮件按钮的响应事件调用.
 */
SCMRegister.step.sendMail = function(){
	var mailList = $("#mailList").val();
	if(mailList==undefined){
		return;
	}
	if(mailList==''||mailList.length<2){
		jAlert(msg_choose_send_psn,msg_alert);
		return;
	}
	
	$.ajax({
		url:ctxpath+'/friend/sendmail',
		type:'post',
		data:{'sendMails':mailList},
		dataType:'json',  
		timeout: 10000,
		success:function(result){
			if(result.ajaxSessionTimeOut=='yes'){
				alert('系统超时');
				top.location.reload(true);
				return;
		    }
			if(result){
				if(typeof(result_callBack)!= 'undefined'){
					result_callBack(mailList);
				}
				if($("#writeEmail").length>0){
					$("#writeEmail").val('');
				}
				$.scmtips.show('success',msg_invite_yes);
			}else{
				$.scmtips.show('error',msg_invite_error);
			}
		},
		error:function(){
			$.scmtips.show('error',msg_invite_error);
		}
	});
};

/**
 * 注册步骤-通过Web邮件通讯录添加联系人 页面 发送邀请邮件按钮的响应事件.
 */
SCMRegister.step.sendWebmailMsg=function(){
	var mailList = "[";
	$("#copy_checked_mail").find("li").each(function(){
		var sendPsnId = $(this).find("input[name=send_psn_id]").val();
		var sendPsnMail = $(this).find("input[name='send_psn_email']").val();
		var sendPsnName = $(this).find("input[name='send_psn_name']").val();
		var mail = "{'email':'";
		if(sendPsnId==null || sendPsnId=="" || sendPsnId=="-1")	
			mail+=sendPsnMail+"', 'name':'" + sendPsnName + "'}";
		else
			mail+=sendPsnMail+"','psnId':'"+sendPsnId+"', 'name':'" + sendPsnName + "'}";
		mailList+=mail+",";
	});
	mailList = mailList.substr(0,mailList.length-1);
	mailList +="]";
	$("#mailList").attr("value",mailList);
	if(mailList.length<2){
		jAlert(msg_choose_send_psn, msg_alert);
	}else{
		SCMRegister.step.sendMail();
	}
};
/**
 * 将已发送的记录的复选框置为不可操作.
 */
SCMRegister.step.setPsnStatus=function(psnX){
	$("#search_resutl").find("li").each(function() { 
		var psnId = $(this).find("input[name='imp_email']").val();
		var email = $(this).find("input[name='imp_email']").attr('alt');
		if(psnId==psnX || email==psnX){
			$(this).find("label[name='"+psnX+"_status']").html(msg_send_yes);	
			$(this).find("input[name='imp_email']").attr('disabled','disabled');
			$.ajax({
				url:ctxpath+'/wizard/sendReqStatus',
				type:'post',
				data:{'sendPsnX':psnX}, 
				timeout: 10000,
				success: function(data){	
				}
			});
		}	
	});
};
/**
 * 注册步骤-通过Web邮件通讯录添加联系人 页面 继续按钮的响应事件.
 */
SCMRegister.step.fun_continue=function(){
	var isSend=false;
	//要添加的人员.
	$("#search_resutl").find("li").each(function() { 
		if($(this).find("input[name='imp_email']").attr('disabled')){
			isSend=true;
		}
	});
	//响应继续发送按钮，跳转到检索成果页面.
	if(isSend){
		location.href=ctx+"/publication/collect";
	}else{
		jConfirm(msg_warn,msg_alert,function(value){
			if(value){
				location.href=ctx+"/publication/collect";
			}
		});	
	}
};
/**
 * 注册第二步-检索联系人页面的从web邮件通讯录导入联系人的验证事件.
 */
SCMRegister.step.impWebMails=function(thiss){
	var name=$('#userName');
	var pwd=$('#passWord');
	if($.trim(name.val())=="" || $.trim(pwd.val())==""){
		$.scmtips.show('warn',msg_check_web_email);
		if($.trim(name.val())==""){
			name.focus();
			return false;
		}
		if($.trim(pwd.val())==""){
			pwd.focus();
			return false;
		}
	}
	$("#load_email").show();
	return true;
	
};
/**
 * 注册第二步-检索联系人页面的修改邮箱账号类型事件.
 */
SCMRegister.step.setInitMail=function(value){
	$('#suffixType').attr('value',value); 
};

/**
 * 注册第二步-检索联系人页面的从MSN导入联系人的验证事件.
 */
SCMRegister.step.impMsnMails=function(thiss){
	var userName = $('#msn_userName').val();
	var pwd = $('#msn_passWord').val();
	if($.trim(userName)=="" || $.trim(pwd)==""){
		 $.scmtips.show('warn',msg_check_web_email);
		if($.trim(userName)==""){
			$('#msn_userName').focus();
			return false;
		}
		if($.trim(pwd)==""){
			$('#msn_passWord').focus();
			return false;
		}
	}
	if(!SCMRegister.step.ismail(userName)){
		 $.scmtips.show('warn',msg_check_mail);
		 $('#msn_userName').focus();
		 return false;
	}
	var suffixType = userName.substr(userName.indexOf('@'),userName.length);
	$("#load_email_msn").show();
	$.ajax({
		url:ctxpath+'/wizard/ajaxMsn',
		type:'post',
		data:{'userName':userName,'passWord':pwd,'suffixType':suffixType}, 
		timeout: 10000,
		success: function(data){	
			if(data!="false"){
				setTimeout(function(){
					location.href=appCtx+"/wizard/subMsn";
				},8000);
			}
		},
		error:function(){	
			$("#load_email_msn").hide();	
			$(thiss).show();
			 $.scmtips.show('warn',msg_find_data_error_webMail); 
		}
	});
	return false;
};


/**
 * 注册第二步-检索联系人页面的从MSN导入联系人的验证事件.
 */
SCMRegister.step.impMsnMailsGuide=function(thiss){
	var userName = $('#msn_userName').val();
	var pwd = $('#msn_passWord').val();
	if($.trim(userName)=="" || $.trim(pwd)==""){
		 $.scmtips.show('warn',msg_check_web_email);
		if($.trim(userName)==""){
			$('#msn_userName').focus();
			return false;
		}
		if($.trim(pwd)==""){
			$('#msn_passWord').focus();
			return false;
		}
	}
	if(!SCMRegister.step.ismail(userName)){
		 $.scmtips.show('warn',msg_check_mail);
		 $('#msn_userName').focus();
		 return false;
	}
	var suffixType = userName.substr(userName.indexOf('@'),userName.length);
	$("#load_email_msn").show();
	$.ajax({
		url:ctxpath+'/wizard/ajaxMsn',
		type:'post',
		data:{'userName':userName,'passWord':pwd,'suffixType':suffixType}, 
		timeout: 10000,
		success: function(data){	
			if(data!="false"){
				setTimeout(function(){
					location.href=appCtx+"/userguide/subMsn";
				},6000);
			}
		},
		error:function(){	
			$("#load_email_msn").hide();	
			$(thiss).show();
			 $.scmtips.show('warn',msg_find_data_error_webMail); 
		}
	});
	return false;
};


/**
 * 判断是否MSN格式.
 */
SCMRegister.step.ismail=function(mail) {
	 var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	 if (filter.test(mail)) 
		 return true;
	 else	
		 return false;
};
/**
 * 注册页面的点击“此处”找回密码的响应事件.
 */
function forgetPassword() {
	var email = $('#email').val();
	//修正忘记密码的连接调转URL_TSZ_SCM-4073.
	var url = ctx+"/forgetpwd/forgetPwd?email="+email+"&from=sns";  
	if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)) {
		var referLink = document.createElement('a');
		referLink.href = url;
		referLink.style.display = "none";
		document.body.appendChild(referLink);
		referLink.click();
	} else {
		location.href = url;
	}
}

