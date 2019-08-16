/**
 * @author zzx
 * 短地址编辑
 * 
 * 各定义方法说明
 * @method	loadSaveOrCopyBtnEvent		短地址复制保存按钮事件处理
 * @method	loadShortUrlEvent			短地址span事件处理
 * @method 	editShow					进入编辑状态
 * @method 	selectText					进入选中状态,使元素的文本选中
 * @method 	removeCheckAll				取消选中状态
 * @method 	getSelectionText			获取选中的文本
 * @method 	doBack						还原操作
 * @method 	doSave						保存操作
 * @method 	doCopy						复制操作
 */
(function (window, document) {
	//Main 方法
	var LoadShortUrlEdit = function(options){
		var self_obj = this;
		var defaults = {
				"scm_id":$("*[scm-id='span_id']"),//短地址节点
				"scm_c_s":$("*[scm-id='span_c_s']"),//复制或保存按钮
				"scm_parent":$("*[scm-paren='span_paren']"),
				"scm_role":1,//scm_role=1为有权限进入编辑状态
				"split_text":"/G/",//短地址分隔节点
				"input_maxlength":20,
				"scm_myfunction":"",//保存执行的函数
				"old_title":"",//按钮的原始title内容
				"save_title":""//'保存'按钮的title内容
		};
		if (typeof options != "undefined") {
			defaults=$.extend({},defaults, options);
	    }
		defaults.old_title=defaults.scm_c_s.attr("title");
		//短地址复制保存按钮事件处理（短地址旁边的小按钮）
		self_obj.loadSaveOrCopyBtnEvent(defaults);
		//短地址span事件处理
		self_obj.loadShortUrlEvent(defaults);
	};
	
	
	//短地址span事件处理
	LoadShortUrlEdit.prototype.loadShortUrlEvent = function(defaults){
		var self_obj = this;
		var obj_input = defaults.scm_parent.find("*[scm_id='input_shorturl']");
		var Mouse = {
			    x: 0,
			    y: 0
		};
		defaults.scm_id.unbind()
		.bind("mousedown",function(e){//鼠标按下事件
			 if(3 == e.which){//右键事件
				//暂无
			}else if(1 == e.which){//左键事件
				Mouse.y = e.clientY;
			    Mouse.x = e.clientX;
			}
		})
		.bind("mouseup",function(e){//鼠标弹起事件
			e.preventDefault();//为了不触发默认弹起就清空选中的默认事件
			if(3 == e.which){//右键事件
				 //暂无
			}else if(1 == e.which){//左键事件
				if(Math.abs(Mouse.y-e.clientY)>1||Math.abs(Mouse.x-e.clientX)>1 ){//滑动过
					//TODO 鼠标弹起滑动事件-为了支持部分选中功能
				}else{//没滑动过
					//编辑框已存在还点击，则要还原内容并返回
					var obj_input = defaults.scm_parent.find("*[scm_id='input_shorturl']");
					if(obj_input&&obj_input.length>0){
						self_obj.doBack(defaults);
						return;
					}
					//主逻辑
					if(self_obj.getSelectionText()==defaults.scm_id.text()){//是否是已选中状态 
						//取消选中状态
						self_obj.removeCheckAll();
						//是=进入编辑状态
						if(defaults.scm_role==1){//scm_role=1为有权限进入编辑状态
							self_obj.editShow(defaults);
							defaults.scm_c_s.text("save");
							defaults.scm_c_s.attr("title",defaults.save_title);
							document.getElementById("shareshorturl").innerHTML= "content_copy";
							document.getElementById("shareshorturl").style.display = "block";
						}
					}else{
						// 否=进入选中状态
						self_obj.selectText(defaults.scm_id);
						defaults.scm_c_s.text("content_copy");
					}
				}
			}
		});
	}
	
	
	//短地址复制保存按钮事件处理（短地址旁边的小按钮）
	LoadShortUrlEdit.prototype.loadSaveOrCopyBtnEvent = function(defaults){
		var self_obj = this;
		defaults.scm_c_s.unbind()
		.bind("mousedown",function(e){
			e.preventDefault();//为了不触发input的blur事件
			var input = defaults.scm_parent.find("*[scm_id='input_shorturl']");
			if(input&&input.length>0){
				self_obj.doSave(defaults);
			}else{
				self_obj.doCopy(defaults);
			}
		});
	}
	
	
	//取消全选状态
	LoadShortUrlEdit.prototype.removeCheckAll = function(){
		if(window.getSelection()){
			window.getSelection().removeAllRanges();
		}else{
			document.selection.empty();
		}
	}
	
	//点击后的短地址可编辑状态显示
	LoadShortUrlEdit.prototype.editShow = function(defaults){
		var self_obj = this;
		var strArr = defaults.scm_id.text().split(defaults.split_text);
		strArr[0]+=defaults.split_text;
		defaults.scm_id.text(strArr[0]);
		defaults.scm_id.after("<input scm_id='input_shorturl' style='min-width:10px;' maxlength='"+defaults.input_maxlength+"'  type='text' value='"+strArr[1]+"'/>");
		var obj_input = defaults.scm_parent.find("*[scm_id='input_shorturl']");
		obj_input.unbind()
		.bind("keydown",function(e){
			 if(e.keyCode==13) {//回车事件
				 self_obj.doSave(defaults);
		     }else if(e.keyCode==27){//Esc事件
		    	//移除编辑框并要还原内容
		    	 self_obj.doBack(defaults);
		     }
		})
		.bind("blur",function(){
			//移除编辑框 并 要填充短地址内容
			setTimeout(function(){
				self_obj.doBack(defaults);
			},500);
		})
		.bind("keyup",function(e){
			//暂无
		})
		.focus().select();
		//光标置后
		//var num = obj_input.val().length;
		//obj_input[0].setSelectionRange(num,num);
	};
	
	
	//使元素的文本选中
	LoadShortUrlEdit.prototype.selectText=function(obj,element) {
		 var text = obj[0];
	    if (document.body.createTextRange) {
	        var range = document.body.createTextRange();
	        range.moveToElementText(text);
	        range.select();
	    } else if (window.getSelection) {
	        var selection = window.getSelection();
	        var range = document.createRange();
	        range.selectNodeContents(text);
	        selection.removeAllRanges();
	        selection.addRange(range);
	    }
	};
	
	
	//获取选中的文本- 用于判断是否已经点击一次--
	LoadShortUrlEdit.prototype.getSelectionText = function () {
		if(window.getSelection) {
		return window.getSelection().toString();
		} else if(document.selection && document.selection.createRange) {
		return document.selection.createRange().text;
		}
		return '';
	};
		
	
	//保存操作
	LoadShortUrlEdit.prototype.doSave =function(defaults){
		 var self_obj = this;
		 var obj_input = defaults.scm_parent.find("*[scm_id='input_shorturl']");
		 var newUrl = $.trim(obj_input.val());
		 defaults.scm_c_s.attr("title",defaults.old_title);
		 if(newUrl==""){
			 scmpublictoast(shortUrlEdit.locale["notEmpty"],2000);
			 self_obj.doBack(defaults);
			 return;
		 }
		
		 if(typeof defaults.scm_myfunction=="function"){
			 var strArr = defaults.scm_id.attr("scm-oldurl").split(defaults.split_text);
			 //正则过滤用户输入的短地址
			 //var patrn=/^(?!_)(?!.*?_$)[a-zA-Z0-9_]{1,20}$/; //不能已下划线开头或结尾
			 var patrn=/^[a-zA-Z0-9_]{1,20}$/; 
			 if(patrn.exec(newUrl)==null){
				 scmpublictoast(shortUrlEdit.locale["urlContains"],3000);
				 return;
			 }
			 var oldUrl = $.trim(strArr[1]);
			 if(oldUrl==newUrl){
				 //scmpublictoast(shortUrlEdit.locale["notChange"],1000);
				 self_obj.doBack(defaults);
				 return;
			 }else{
				 defaults.scm_myfunction(strArr[1],newUrl,function(){
						//保存成功-移除编辑框前要记录编辑内容
						 defaults.scm_id.attr("scm-oldurl", defaults.scm_id.text()+obj_input.val());
						 defaults.scm_id.text( defaults.scm_id.text()+obj_input.val());
						 obj_input.remove();
						 defaults.scm_c_s.text("content_copy");
					 },function(){
						//保存失败-移除编辑框前要还原内容
						 self_obj.doBack(defaults);
					 });
			 }
		 }
	};
	
	//复制操作
	LoadShortUrlEdit.prototype.doCopy = function(defaults){
		var self_obj = this;
		if(self_obj.getSelectionText()==""){
			 const $range = document.createRange();
			 $range.selectNodeContents(defaults.scm_id[0]);
			 window.getSelection().removeAllRanges();
			 window.getSelection().addRange($range);
		}
		 document.execCommand('Copy');
		 if (!!window.ActiveXObject || "ActiveXObject" in window) {//判断是否为IE浏览器
			 if(window.clipboardData){
				 var clipStr=window.clipboardData.getData("text");
				 if(clipStr!=self_obj.getSelectionText()){
					 scmpublictoast(shortUrlEdit.locale["copyFail"],2000);
				 }
			 }else{
				 scmpublictoast(shortUrlEdit.locale["copyFail"],2000);
			 }
		 }
		 scmpublictoast(shortUrlEdit.locale["copySuccess"],2000);
		 
	}
	
	//还原操作
	LoadShortUrlEdit.prototype.doBack = function(defaults){
	    document.getElementById("shareshorturl").style.display = "none";
		defaults.scm_id.text(defaults.scm_id.attr("scm-oldurl"));
		defaults.scm_parent.find("*[scm_id='input_shorturl']").remove();	
		defaults.scm_c_s.text("content_copy");
		defaults.scm_c_s.attr("title",defaults.old_title)
	}
	
	//点击其他地方事件
	LoadShortUrlEdit.prototype.clickOtherHide = function(e,myFunction){
	
	if (e && e.stopPropagation) {//非IE  
	    e.stopPropagation();  
	}  
	else {//IE  
	    window.event.cancelBubble = true;  
	} 
	$(document).click(function(){
	    document.getElementById("shareshorturl").style.display = "none";
		if(typeof myFunction == "function"){
			myFunction();
		}
		});
	};
	//主入口
	window.LoadShortUrlEdit = function (options) {
		return new LoadShortUrlEdit(options);
	};
})(window, document);