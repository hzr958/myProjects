/**
 * @author liangguokeng
 * 学科领域下拉插件
 */
;(function($){
	$.disccomplete = $.disccomplete ? $.disccomplete : {};
    $.fn.disccomplete = function(options){
        var defaults = {
            "url": null,//注：设计，但没用到
            "ctx": ctxpath,//上下文路径
            "respath": null,//资源路径
            "allowUserInput":false,//是否允许用户输入，不采用nsfc学科
            "supportExt":true,//是否支持推荐插件,即jquery.disciplineext.js
            "extBind":null,//推荐插件绑定方法,如果supportExt为false,则此处为null
            "keyDiv":null,//推荐插件，关键词所在的div,jQuery对象。
			"act":null,//推荐插件行为（可不用）
            "discBind":null,//选择学科插件绑定方法
			"discLevel":3,//输入提示下拉默认学科代码3级
			"input_width":320,//输入框长度
			"bind":null//
        };
        if (typeof options != "undefined") {
            $.extend(defaults, options);
        }
		this.each(function(index){
			var _$this = $(this);
			var twidth =defaults["input_width"];
			_$this.width(twidth+"px");
			_$this.append($.disccomplete["initHtml"](index, defaults["input_width"]));
			_$this.find("input").complete({
		            "key": "disc_input",
		            "width": 200,
		    		"extraParams":{"discLevel":defaults["discLevel"]}
		        })["extend"](function(data){
					if(data != null && typeof data != "undefined"){
						var ids = [];
						var texts = [];
						$(".disc_input").each(function(){
							var _$this = $(this);
							if(index != _$this.attr("seq")){
								ids.push(_$this.attr("disc_id"));
								texts.push(_$this.val().replace(/(^\s*)|(\s*$)/g,''));
							}
						})
						var isExist = false;
						if(ids.length>0){
							for (var i in ids) {
								if(data.id == ids[i]){
									isExist = true;
								}
							}
						}
						if(!isExist){//当在下拉框选择与用户自己输入但没有id和code相同的学科，把用户自己输入的学科删除（注：用户通过手动输入的学科是没有id与code的）
							for (var i in texts) {
								if(data.name == texts[i]){
									$("#discipline_input_"+i+"").val("");
									if ($("#disc_list_text").data('mydata')) {
										$("#disc_list_text").data('mydata')[i] = "";
									}
								}
							}
						}
						if(isExist){
							$("#discipline_input_"+index+"").attr("disc_id","");
							$("#discipline_input_"+index+"").attr("code","");
							$("#discipline_input_"+index+"").val("");
							$("#discipline_input_"+index+"").next("span").attr("disc_code","");
							if ($("#disc_list_text").data('mydata')) {
								$("#disc_list_text").data('mydata')[index] = "";
							}
						}else{
	            			$("#discipline_input_"+index+"").attr("disc_id",data.id);
							if ($("#discipline_input_" + index + "").attr("code") == "") {
								$("#discipline_input_" + index + "").attr("code", data.code);
							}
							$("#discipline_input_"+index+"").next("span").attr("disc_code",data.code);
							if($("#disc_list_text").data('mydata')){
        						$("#disc_list_text").data('mydata')[index] = data.name;
							}
						}
						if(defaults["bind"]){
							defaults["bind"](data);
						}
					}
	        	})
		});
		if (defaults["supportExt"]) {
			$(".disc_select_plugin").disciplineext({
				"ctx": defaults['ctx'],
				"respath": respath,
				"bind": defaults["extBind"],
				"discBind": defaults["discBind"],
				"keyDiv":defaults["keyDiv"],//关键词所在的div,jQuery对象。
				"act":defaults["act"]
			});
		}else{
			$(".disc_select_plugin").discipline({
				"ctx": defaults['ctx'],
				"url": defaults['url'],
				"respath": respath,
				"bind": defaults["discBind"]
			});
		}
		if($.browser.msie&&($.browser.version == "8.0" ||$.browser.version == "6.0"  ||$.browser.version == "7.0" ) ){
			$(".disc_input").bind({
				'keyup' : function(event){
					if (event.keyCode != 13) {//回车自动完成
						$(this).attr("disc_id", "");
						$(this).attr("code", "");
						$(this).next("span").attr("disc_code", "");
					}
				},
				'blur' : function(){
					if($(this).attr("disc_id")=="" && !defaults["allowUserInput"]){
						$(this).val("");
					}
				}
			});
		}else{
			$(".disc_input").bind({
				'input': function() {
					$(this).attr("disc_id","");
					$(this).attr("code","");
					$(this).next("span").attr("disc_code","");
				},
				'change' : function(){
					if($(this).attr("disc_id")=="" && !defaults["allowUserInput"]){
						$(this).val("");
					}
				}
			});
		}
    };
	$.disccomplete = {
		"initHtml": function(index, width){
			var arr = [];
			
			arr.push("<input id=\"discipline_input_"+index+"\" disc_id=\"\" seq="+index+" name=\"\" type=\"text\" style=\"width:"+(width-5)+"px;\" class=\"inp_text inp_bg1 disc_input\"/>");
            arr.push("<span id=\"discipline_span_"+index+"\" class=\"disc_select_plugin\"></span>");
			//arr.push("<select style=\"display: none;\" class=\"inp_text\" style=\"width: 320px;\" id=\"discipline_"+index+"\">");
            //arr.push("<option selected=\"selected\" value=\"\">{select_disc}</option>\"");
            //arr.push("</select>");
			return arr.join("");
		}
	};
})(jQuery);
