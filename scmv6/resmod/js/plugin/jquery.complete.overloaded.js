//jquery Autocomplete 插件方法重载js


//联系人
$.friend_name_parse = function(data){
	var parsed = [];
	if(locale=='en_US'){
		for(var i=0;i<data.length;i++){
			if($.trim(data[i].enName)!=''){
			   data[i].name = data[i].enName;
			}else{
				data[i].name = data[i].zhName;
			}
			data[i].code = data[i].psn_id;
			var item = data[i];
			parsed.push({
				"data":item,
				"value":item.code.toString(),
				"result":item.name
			});
		}
	}else{
		for(var i=0;i<data.length;i++){
			if($.trim(data[i].zhName)!=''){
			  data[i].name =data[i].zhName;
			}else{
				data[i].name = data[i].enName;
			}
			data[i].code = data[i].psn_id;
			var item = data[i];
			parsed.push({
				"data":item,
				"value":item.code.toString(),
				"result":item.name
			});
		}
	}
	return parsed;
};
$.friend_name_formatItem = function(data,i,n,value,term){
	var str = '';
	if(locale=='en_US'){
		if($.trim(data.enName)!=''&&$.trim(data.zhName)!=''){
			str = data.name+"("+data.zhName+")";
		}else{
			str = data.name;
		}
	}else{
		if($.trim(data.zhName)!=''&&$.trim(data.enName)!=''){
			str = data.name + "("+data.enName+")";
		}else{
			str = data.name;
		}
	}
	if($.trim(data.primaryWorkUnit)!=''){
		str=str+"<br/><label style='font-size:75%'>"+data.primaryWorkUnit+"</label>";
	}
	return str;
};

//rol单位人员
$.rolInsPsns_parse = function(data){
	var parsed = [];
	for(var i=0;i<data.length;i++){
		data[i].code = data[i].psn_id;
		var item = data[i];
		parsed.push({
			"data":item,
			"value":item.code.toString(),
			"result":item.name
		});
	}
	return parsed;
};
//群组成员
$.groupMember_name_parse = function (data){
	var parsed = [];
	if(locale=='en_US'){
		for(var i = 0; i<data.length;i++){
			if($.trim(data[i].enName!='')){
				data[i].name = data[i].enName;
			}else{
				data[i].name = data[i].zhName;
			}
			data[i].code = data[i].psnId.toString();	
			var item = data[i];
			parsed.push({
				"data":item,
				"value":item.code,
				"result":item.name
			});

		}
	}else{
		for(var i = 0; i<data.length; i++){
			if($.trim(data[i].zhName!='')){
				data[i].name = data[i].zhName;
			}else{
				data[i].name = data[i].enName;
			}
			data[i].code = data[i].psnId.toString();
			var item = data[i];
			parsed.push({
				"data":item,
				"value":item.code,
				"result":item.name
			});
		}
	}
	return parsed;
}

$.groupMember_name_formateItem = function(data,i,n,value,term){
	var str = '';
	if(locale=='en_US'){
		if($.trim(data.enName)!=''&&$.trim(data.zhName)!=''){
			str = data.name + "("+data.zhName+")";
		}else{
			str = data.name;
		}
	}else{
		if($.trim(data.enName)!=''&&$.trim(data.zhName)!=''){
			str = data.name + "("+data.enName+")";
		}else{
			str = data.name;
		}
	}
	if($.trim(data.titolo!='')){
		str = str+"<br/><label style='font-size:75%'>"+data.titolo+"</label>";
	}
	return str;
}

$.ins_unit_formatItem = function(data, i, n, value, term){
	var code = data.code;
	var department = data.name;//系名称
	var collegeName = data.collegeName;

	if (collegeName && $.trim(collegeName.length) > 0) {//院系名称都存在
		return "<label style='margin-right:5px;'>"+department + "</label><label style='font-size:95%;margin-left:22px;margin-right:10px;'>" + collegeName + "</label>";
	}
	return "<label style='margin-right:5px;'>"+department+"</label>";
};