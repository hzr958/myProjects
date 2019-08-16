/**
 * @author xys
 * 平铺模式分享插件.
 */
(function($){
	$.fn.shareTileMode = function(options){
		var defaults = {
				'shareTxtUrl' : '/inspg/share/ajaxGetInspgShareTxt',
				'ctxpath' : '',
				'isShowSmate' : 1, // 是否显示SMATE   1,显示 0,不显示
				'showSharePage' : null, // 打开分享弹出框
				'url' : 'http://' + document.domain,
				'ralateUid' : '2411599032', // 关联用户uid，分享微薄时会@该用户.
				'appKey' : '', // 申请的应用appKey，显示分享来源.
				'language' : 'zh', // 设置语言.
		    	'irisTitle' : '科研之友站外分享',  // 分享的标题.
		    	'irisText' : '#分享#', // 分享的文字内容.
				'irisDes' : '分享自@科研之友', // 分享摘要.
				'irisComment' : '',	// 分享评论.
				'authorNames' : '',
				'title' : '',
				'source' : '',
				'pic' : '', // 图片路径.
				'picSearch' : true, // 是否自动抓取网页图片.
				'rnd' : new Date().valueOf(),
				'category' : 0, // 分享类型，0普通分享，1成果分享，2文献，4项目 ，11基金
				'isExist':0,//资源是否存在
				'isTimeOut' : 0 // 是否超时
        };
		options.language = typeof locale != undefined ? locale : defaults.language;
		var opts = $.extend(defaults, options);
		
		this.each(function(){
			var _this = $(this);
			shareFunctions.initSiteList(_this,opts);
		});
		
		return this.each(function(){
			var _pthis = $(this);
			return _pthis.parent().find(".share_site").each(function(){
				var _this = $(this);
				shareFunctions.initShareTileMode(_pthis,_this,opts);
			});
		});
	};
	
	var shareFunctions = {
			"initSiteList" : function(_this,options){
				_this.after(this.buildSiteList(options));
			},
			"initShareTileMode" : function(_pthis,_this,options){
				//SCM-6550  在分享功能中添加“微信” 在单击“微信”事件上绑定两个函数，ShareQRCode()弹出提示框，getQrImg获得二维码
//				if( _this.attr("class").indexOf("weChat")>-1){
//					options = this.initShareTxt(_pthis,_this,options);
//					var url = "'" + encodeURIComponent(options.url) + "'";
//					_this.attr("href","javascript:javascript:ShareQRCode();getQrImg(" + url + ")");
//					return false;
//				}
				if(_this.attr("class").indexOf("outside")>-1){
					_this.live({
						'click' : function(e){
							var shareUrl = "";
							shareUrl = shareFunctions['buildShareUrl'](_pthis,this,options);
							if(shareUrl==""){
								$.smate.scmtips.show('warn',"该资源已被删除");
								return false
							}
							if(options.isTimeOut==1){
								return ;
							}
							if($(this).attr("class").indexOf("weChat")>-1){
								ShareQRCode();
								getQrImg(shareUrl+"&locale="+locale);
								return false;
							}
							if(options.isTimeOut==0){
								window.open(shareUrl);
							}
							return false;
						}
					});
				}
				//分享新闻到科研之友功能原来是没有的开发的，SCM-8801
				/*else if(_this.attr("class").indexOf("inside")>-1){
					_this.live({
						'click' : function(e){
							options.showSharePage !=null ? options["showSharePage"](_pthis) : _pthis.click();
							return false;
						}
					});
				}*/
			},
			"buildSiteList" : function(options){
				var _html = "";
				//这几个js和css是微信分享用到的SCM-6550
//				var _html = '<script type="text/javascript" src="/resscmwebsns/js_v5/plugin/jquery.qrcode.min.js"></script><script type="text/javascript" src="/resscmwebsns/js_v5/plugin/dialog.js"></script><script type="text/javascript" src="/resscmwebsns/js_v5/plugin/sharebutton.js"></script><link rel="stylesheet" type="text/css" href="/resscmwebsns/css_v5/plugin/dialog.css" />';
//				_html += '<script type="text/javascript">function getQrImg(url){$("#share-qr-img").qrcode({render: "table",width: 175,height:175,text:url });}</script>';
				if(options.language=='en_US'){
					_html += '<a class="img_facebook2 share_site outside" type="Facebook" title="Facebook"></a>';
					_html += '<a class="img_linkedin2 share_site outside" type="LinkedIn" title="Linkedin"></a>';
					if(options.isShowSmate==1){
						_html += '<a class="img_smate2 share_site inside" title="SMate"></a>';
					}
					_html += '<a class="img_weibo2 share_site outside" type="sina" title="Sina Weibo"></a>';
					_html += '<a class="img_qq_web2 share_site outside" type="tencent" title="QQ Weibo"></a>';
					_html += '<a class="img_wechat2 share_site outside weChat" type="WeChat" title="WeChat"></a>';
				}else{
					_html += '<a class="img_weibo2 share_site outside" type="sina" title="新浪微博"></a>';
					_html += '<a class="img_qq_web2 share_site outside" type="tencent" title="腾讯微博"></a>';
					_html += '<a class="img_wechat2 share_site outside weChat" type="WeChat" title="微信"></a>';
					//分享新闻到科研之友功能原来是没有的开发的，SCM-8801
					/*if(options.isShowSmate==1){
						_html += '<a class="img_smate2 share_site inside" title="科研之友"></a>';
					}*/
					_html += '<a class="img_linkedin2 share_site outside" type="LinkedIn" title="Linkedin"></a>';
					_html += '<a class="img_facebook2 share_site outside" type="Facebook" title="Facebook"></a>';
				}
				
				return _html;
			},
			"initShareTxt" : function(_pobj,_obj,options){
				var resId = _pobj.attr("resId");
				var resType = _pobj.attr("resType");
				var dbid = _pobj.attr("dbid");
				
				var post_data = {'resIds' : resId, 'resType' : resType};
				if (dbid!=undefined && dbid!='') {
					post_data['dbid'] = dbid;
				}
				$.ajax({
					url : options['ctxpath'] + options['shareTxtUrl'],
					type : 'post',
					dataType : "json",
					async: false,
					data : post_data,
					success : function(data){
						var timeOut = data['ajaxSessionTimeOut'];
            			if(timeOut != null && typeof timeOut != 'undefined' && timeOut == 'yes'){
            				jConfirm(tips["sessionTimeout_" + locale], tips["prompt_" + locale], function(r){
				   				if (r) {
				   					if(typeof loginCommend != "undefined"){
				   						loginCommend();
				   					}else{
				   					//区别机构主页(站外)和其他项目_临时办法
				   						if(window.location.href.indexOf("/inspg")>=0){
				   							document.location.href="/inspg/login?service="+window.location.href;
				   						}else{
				   							document.location.reload();
				   						}
				   					}
				   				}
				   			});
            				options.isTimeOut=1;
            				options.isExist = 0;
            	        }else if (data.result == "success" && data.shareTxt != undefined && data.shareTxt != '') {
							var pubCon = eval("[" + data.shareTxt + "]");
								options.url = pubCon[0].resLink;
								options.pic = pubCon[0].pic;
								options.category = resType;
								if(options.category == 104 ) {
									options.irisText = '#成员分享#';
								} else if(options.category == 105) {
									options.irisText = '#新闻分享#';
								}
								options.language = pubCon[0].language;
								options.authorNames = pubCon[0].authorNames;
								options.title = pubCon[0].title;
								options.source = pubCon[0].source;
								options.isExist = 0;
						}else{
							options.isExist = 1;
						}
					},
					error : function(data) {
						
					}
				});
				return options;
			},
			"buildShareUrl" : function(_pobj,_obj,options) {
				var shareUrl = "";
				options = this.initShareTxt(_pobj,_obj,options);
				if(options.isExist==1){
					return "";
				}
				var type = $(_obj).attr('type') || 'sina';
				shareUrl = requestUrl[type] + this[type](options);
				return shareUrl;
			},
			'textLength' : function(text) {
				if($.trim(text).length==0){
					return 0;
				}else{
					var inputLength = 0;
					text = $.trim(text);
					for(var i=0;i<text.length;i++){
						var cchar = text.charAt(i);
						if(this.isChinaStr(cchar)){
							inputLength += 1;
						}else{
							inputLength += 0.5;
						}
					}
					inputLength = Math.round(inputLength);
					return inputLength;
				}
			},
			'isChinaStr' : function(str) {
				var regu = "[\w\W]*[\u4e00-\u9fa5][\w\W]*";
				var re = new RegExp(regu);
				if (str == "")
					return false;
				if (re.test(str)) {
					return true;
				} else {
					return false;
				}
			},
			'getShareContent' : function(options, maxTxtLen) {
				var split =  (options.language != undefined && "zh" == options.language) ? "，" : ", ";
				var shareContent = options.irisText;
				if (options.authorNames != undefined&&options.authorNames != '') {
					options.authorNames = options.authorNames.replace(/<strong>|<\/strong>/ig, '');
					shareContent = shareContent + options.authorNames + split;
				}
				shareContent += options.title;
				if (options.source != undefined&&options.source != '') {
					shareContent = shareContent + split + options.source;
				}
				if (this.textLength(shareContent) > maxTxtLen) {
					shareContent = options.irisText;
					if (options.authorNames != undefined&&options.authorNames != '') {
						var split2 =  (options.language != undefined && "zh" == options.language) ? "；" : "; ";
						var authorNames = options.authorNames.split(/; |；/);
						for (var i = 0; i < authorNames.length && i < 3; i++) {
							shareContent = shareContent + authorNames[i] + split2;
						}
						shareContent = shareContent.substring(0, shareContent.length - (split2 == '；' ? 1 : 2)) + split;
					}
					if (options.title != undefined&&options.title != '') {
					shareContent = shareContent + options.title;
					}
					if (options.source != undefined&&options.source != '') {
						shareContent = shareContent + split + options.source;
					}
				}
				return shareContent;
			},
			'buildUrl' : function(params) {
				var urlParam = [];
				for (var item in params) {
					urlParam.push(item + '=' + encodeURIComponent(params[item] || ''))
				}
				return urlParam;
			},
			'sina' : function(options) {
				var params = {
						'url' : options.url,
						'type' : 'icon',
						'ralateUid' : '2411599032',
						'language' : 'zh_cn',
						'appKey' : '1813949776',
						'title' : options.irisText,
						'searchPic' : false,
						'style' : 'simple'
				};
				if (options.pic != '') {
					params.pic = options.pic;
				}
				if (options.category == 104 || options.category ==105) {
					params.title = this.getShareContent(options, 140);
				}
				return this.buildUrl(params).join('&');
			},
			'tencent' : function(options) {
				var params = {
						'url' : options.url,
						'appKey' : '801477883',
						'title' : options.irisText
				};
				if (options.pic != '') {
					params.pic = options.pic;
				}
				if (options.category == 104 || options.category ==105) {
					params.title = this.getShareContent(options, 140);
				}
				return this.buildUrl(params).join('&');
			},
			'LinkedIn' : function(options) {
				var params = {
						'url' : options.url,
						'title' : options.title,
						'source' : 'ScholarMate'
				};
				return this.buildUrl(params).join('&');
			},
			'Facebook' : function(options) {
				var params = {
						'u' : options.url
				};
				
				return this.buildUrl(params).join('&');
			},
			'WeChat' : function(options){
				return options.url;
			}
	};
	var requestUrl = {
			'sina' : 'http://v.t.sina.com.cn/share/share.php?',
			'tencent' : 'http://share.v.t.qq.com/index.php?c=share&a=index&',
			'LinkedIn' : 'http://www.linkedin.com/shareArticle?',
			'Facebook' : 'https://www.facebook.com/sharer/sharer.php?',
			'WeChat' : ''
	};
	var tips = {
            "prompt_zh_CN" : "提示",
            "prompt_en_US" : "Reminder",
		    "sessionTimeout_zh_CN" : "系统超时或未登录，你要登录吗？",
		    "sessionTimeout_en_US" : "You are not logged in or session time out, please log in again."
    };
})(jQuery);
