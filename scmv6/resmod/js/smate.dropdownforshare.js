/**
 * @author xys
 * 下拉模式分享插件.
 */
(function($){
	
	var sharePullMode = sharePullMode ? sharePullMode : {};
	
	$.fn.sharePullMode = function(options){
		var defaults = {
				'shareTxtUrl' : '/inspg/share/ajaxGetInspgShareTxt',
				'snsctx' : '',
				'isShowSmate' : 1, // 是否显示SMATE 1,显示 0,不显示
				'showShareSites' : null, // 展开分享站点列表
				'showSharePage' : null, // 打开分享弹出框
				'url' : 'http://' + document.domain + snsctx,
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
				'category' : 0, // 分享类型，99其它，0普通分享，1成果分享，2文献，4项目   ，11基金
				'isTimeOut' : 0, // 是否超时
				'isExist':0,//资源是否存在
				'isGetFromPage' : 0,// 是否直接从页面获取分享内容
        };
		options.language = typeof locale != undefined ? locale : defaults.language;
		var opts = $.extend(defaults, options);
		
		this.each(function(){
			var _this = $(this);
			sharePullMode.shareFunctions.initSiteList(_this,opts);
		});
		
		// 初始化分享站点列表的展开
		opts.showShareSites == null ? sharePullMode.shareFunctions.showShareSites() : opts.showShareSites();
		
		return this.each(function(){
			var _pthis = $(this);
			return _pthis.parent().find(".share_site").each(function(){
				var _this = $(this);
				sharePullMode.shareFunctions.initSharePullMode(_pthis,_this,opts);
			});
		});
	};
	
	sharePullMode.shareFunctions = {
			"initSiteList" : function(_this,options){
				_this.after(this.buildSiteList(options));
			},
			"initSharePullMode" : function(_pthis,_this,options){
//				SCM-6550  在分享功能中添加“微信” 在单击“微信”事件上绑定两个函数，ShareQRCode()弹出提示框，getQrImg获得二维码
//				if( _this.attr("class").indexOf("weChat")>-1){
//					options = this.initShareTxt(_pthis,_this,options);
//					var url = "'" + encodeURIComponent(options.url) + "'";
//					_this.attr("href","javascript:ShareQRCode();getQrImg(" + url + ")");
//					return false;
//				}			
				if(_this.attr("class").indexOf("outside")>-1){
					_this.delegate({
						'click' : function(e){
							var shareUrl = "";
							shareUrl = sharePullMode.shareFunctions['buildShareUrl'](_pthis,this,options);
							if(shareUrl=="" || shareUrl == null || shareUrl == "null"){
								$.smate.scmtips.show('warn',"该资源已被删除");
								return false
							}
							if(options.isTimeOut==1){
								return ;
							}
							if($(this).attr("class").indexOf("weChat")>-1){
								ShareQRCode();
								if (options.category == "104") {
									getQrImg(shareUrl);
								} else {
									getQrImg(shareUrl+"&locale="+locale);
								}
								return false;
							}
							
							if(options.isTimeOut==0){
								window.open(shareUrl);
							}
							return false;
						}
					});
				}else if(_this.attr("class").indexOf("inside")>-1){
					_this.unbind();
					_this.delegate({
						'click' : function(e){
							options.showSharePage !=null ? options["showSharePage"](_pthis) : _pthis.click();
							return false;
						}
					});
				}
			},
			"buildSiteList" : function(options){
				var _html = "";
				//这几个js和css是微信分享用到的SCM-6550
//				var _html = '<script type="text/javascript" src="/resscmwebsns/js_v5/plugin/jquery.qrcode.min.js"></script><script type="text/javascript" src="/resscmwebsns/js_v5/plugin/dialog.js"></script><script type="text/javascript" src="/resscmwebsns/js_v5/plugin/sharebutton.js"></script><link rel="stylesheet" type="text/css" href="/resscmwebsns/css_v5/plugin/dialog.css" />';
//				_html += '<script type="text/javascript">function getQrImg(url){$("#share-qr-img").qrcode({render: "table",width: 175,height:175,text:url });}</script>';
				if(options.language=='en_US'){
					_html += '<div class="public_pulldown_zh_CN share_site_list" style="display: none;">';
					_html += '<dl>';
					_html += '<dd><a class="share_site outside" type="Facebook"><i class="img_facebook"></i>Facebook</a></dd>';
					_html += '<dd><a class="share_site outside" type="LinkedIn"><i class="img_linkedin"></i>Linkedin</a></dd>';
					if(options.isShowSmate==1){
						_html += '<dd><a class="share_site inside"><i class="img_smate"></i>SMate</a></dd>';
					}
					_html += '<dd><a class="share_site outside" type="sina"><i class="img_weibo"></i>Sina &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Weibo</a></dd>';
					_html += '<dd><a class="share_site outside" type="tencent"><i class="img_qq_web"></i>QQ Weibo</a></dd>';
					_html += '<dd><a class="share_site outside weChat" type="WeChat"><i class="img_weChat"></i>WeChat</a></dd>';
					_html += '</dl>';
					_html += '</div>';
				}else{
					_html += '<div class="public_pulldown_zh_CN share_site_list" style="display: none;">';
					_html += '<dl>';
					_html += '<dd><a class="share_site outside" type="sina"><i class="img_weibo"></i>新浪微博</a></dd>';
					_html += '<dd><a class="share_site outside" type="tencent"><i class="img_qq_web"></i>腾讯微博</a></dd>';
					_html += '<dd><a class="share_site outside weChat" type="WeChat"><i class="img_weChat"></i>微信</a></dd>';
					if(options.isShowSmate==1){
						_html += '<dd><a class="share_site inside"><i class="img_smate"></i>科研之友</a></dd>';
					}
					_html += '<dd><a class="share_site outside" type="LinkedIn"><i class="img_linkedin"></i>Linkedin</a></dd>';
					_html += '<dd><a class="share_site outside" type="Facebook"><i class="img_facebook"></i>Facebook</a></dd>';
					_html += '</dl>';
					_html += '</div>';
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
					url : options['snsctx'] + options['shareTxtUrl'],
					type : 'post',
					dataType : "json",
					async: false,
					data : post_data,
					success : function(data){
						var timeOut = data['ajaxSessionTimeOut'];
            			if(timeOut != null && typeof timeOut != 'undefined' && timeOut == 'yes'){
            				jConfirm(sharePullMode.tips["sessionTimeout_" + locale], sharePullMode.tips["prompt_" + locale], function(r){
				   				if (r) {
				   					//区别机构主页(站外)和其他项目_临时办法
			   						if(window.location.href.indexOf("/inspg")>=0){
			   							document.location.href="/inspg/login?service="+window.location.href;
			   						}else{
			   							document.location.reload();
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
								if(options.category == 104) {
									options.irisText = '#成员分享#';
								} else if(options.category ==105) {
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
				if(options.isGetFromPage==0)
				options = this.initShareTxt(_pobj,_obj,options);
				if(options.isExist==1){
					return "";
				}
				var type = $(_obj).attr('type') || 'sina';
				shareUrl = sharePullMode.requestUrl[type] + this[type](options);
				return shareUrl;
			},
			"showShareSites" : function(){
				$(".share_sites_show").each(function(){
					$(this).click(function(e){
						$(".share_site_list").each(function(){
							$(this).hide();
						});
						var _obj = $(this).parent().find(".share_site_list");
						// 显示隐藏
						if (_obj.is(":hidden")) {
							_obj.show();
						} else {
							_obj.hide();
							return;
						}
						//点击其他地方关闭
					    if (e && e.stopPropagation) {//非IE  
					        e.stopPropagation();  
					    }  
					    else {//IE  
					        window.event.cancelBubble = true;  
					    } 
					    $(document).click(function(){_obj.hide();});
					});
				});
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
						'searchPic' : options.picSearch ? 'true':'false',
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
	 sharePullMode.requestUrl = {
			'sina' : 'http://v.t.sina.com.cn/share/share.php?',
			'tencent' : 'http://share.v.t.qq.com/index.php?c=share&a=index&',
			'LinkedIn' : 'http://www.linkedin.com/shareArticle?',
			'Facebook' : 'https://www.facebook.com/sharer/sharer.php?',
			'WeChat' : ''
				
	};
	sharePullMode.tips = {
            "prompt_zh_CN" : "提示",
            "prompt_en_US" : "Reminder",
		    "sessionTimeout_zh_CN" : "系统超时或未登录，你要登录吗？",
		    "sessionTimeout_en_US" : "You are not logged in or session time out, please log in again."
    };
})(jQuery);
