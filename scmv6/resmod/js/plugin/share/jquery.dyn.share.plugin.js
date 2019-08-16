/**
 * @author ajb
 * 群组动态分享插件.
 */
(function($){
	
	$.fn.dynSharePullMode = function(options){
		var defaults = {
				'shareSiteDivId' : 'share_site_div_id',   //显示插件的div的 id
				'groupDynAddShareCount' : {},   //分享数 加一 ，如果要调用别的分享数据加一****， 需要 自己 传 一个回调函数** <函数哦>
				'shareToSmateMethod' : '',   //分享到科研之友的点击方法 回调   ****需要自己传过来   **《方法名o》
				'shareTxtUrl' : '/dynweb/outside/ajaxgetsharetxt',
				'ctxpath' : '/scmwebsns',
				'showShareSites' : null, // 展开分享站点列表
				'showSharePage' : null, // 打开分享弹出框
				'url' : 'http://' + document.domain + '/scmwebsns',
				'ralateUid' : '2411599032', // 关联用户uid，分享微薄时会@该用户.
				'appKey' : '', // 申请的应用appKey，显示分享来源.
				'language' : 'zh', // 设置语言.
		    	'irisTitle' : '科研之友站外分享',  // 分享的标题.
		    	'irisText' : '#论文分享#', // 分享的文字内容.
				'irisDes' : '分享自@科研之友', // 分享摘要.
				'irisComment' : '',	// 分享评论.
				'authorNames' : '',
				'title' : '',
				'source' : '',
				'pic' : '', // 图片路径.
				'picSearch' : true, // 是否自动抓取网页图片.
				'rnd' : new Date().valueOf(),
				'category' : 0, // 分享类型，99其它，0普通分享，1成果分享，2文献，4项目 ，11基金, 25资助机构
				'isTimeOut' : 0, // 是否超时
				'isGetFromPage' : 0,// 是否直接从页面获取分享内容
				'styleVersion':0,// 样式版本，可能的版本号：0,1,2......   10:grp文件
				'mobileResUrl': '', //移动端资源详情url
				'platform':''//分享平台 微信、微博等
        };
		//if(typeof(options.language) == undefined){
			if("undefined" != typeof locale){ 
				options.language =locale;
			}else{
				options.language = defaults.language;
			}
		//}
		var opts = $.extend(defaults, options);
		var _pthis = $(this);
		var type;
		// 每一次都将 分享至动态，联系人，群组三个选择框显示出来，外部要改再进行变化
		$("#share_to_scm_box").find("li").show();
		
		// 初始化分享站点列表的展开
		shareFunctions.showShareSites(opts  ,_pthis ) ;
		return shareFunctions.initSharePullMode(_pthis,opts);
	}
	
	var shareFunctions = {
			"initSiteList" : function(options){
				 var $_site_list = $("#"+options.shareSiteDivId) ;
				 if($_site_list.length  != 0 ){
					 $_site_list.remove() ;//移除之前的框
				 }
				 $("body").prepend( shareFunctions['buildSiteList'](options) )
			},
			"initSharePullMode" : function(_pthis,options){
                var length = $("#"+options.shareSiteDivId).find(".dialogs_whole > .dialogs_container  > .dialogs_content  > .share_plugin_destination_list  > .share_plugin_section ").length ;
              
				$("#"+options.shareSiteDivId).find(".dialogs_whole > .dialogs_container  > .dialogs_content  > .share_plugin_destination_list  > .share_plugin_section ").each(
						function(i){
							var _this = $(this) ;
							_this.unbind() ;
							if(_this.attr("class").indexOf("outside")>-1){
								_this.on({
									'click' : function(e){
									    //设置分享平台
                                        shareFunctions['resetSharePlatform'](this);
										//隐藏分享框
									    var  div_id = options.shareSiteDivId;
										var _obj = $("#"+div_id);
										_obj.css("display","none");
										$("body").css("overflow","");
										//end
										var shareUrl = "";
										shareUrl = shareFunctions['buildShareUrl'](_pthis,this,options);
										//统计分享到站外的次数，只要点击了就加1，无论分享是否成功
										if(options.isTimeOut==1){
											return ;
										}
										if(  typeof  options.groupDynAddShareCount   ==  "function"){
											options.groupDynAddShareCount();
										}
										
										//scm-6550
										if($(this).attr("class").indexOf("weChat")>-1){
											ShareQRCode();
											if(shareUrl.indexOf("?")>-1){
												getQrImg(shareUrl+"&locale="+locale);
											}else{
												getQrImg(shareUrl+"?locale="+locale);
											}
											return false;
										}
										// 分享链接-将资源地址复制到剪切板
										if($(this).attr("class").indexOf("links")>-1){
											// 非IE浏览器
											var body = document.getElementsByTagName("body")[0];
											var input = document.createElement("input");
											input.value = shareUrl;
											body.appendChild(input);
											input.select();
											document.execCommand("Copy");
											input.style.display = "none";
											 if (!!window.ActiveXObject || "ActiveXObject" in window) {//判断是否为IE浏览器
												var clipStr=window.clipboardData.getData("text");
												if(clipStr != input.value){
													if(locale=='en_US'){
														scmpublictoast("Copied failed.",2000);
													}else{
														scmpublictoast("链接复制失败 ",2000);
													}
												}
											}
											if(locale=='en_US'){
												scmpublictoast("Copied successfully.",2000);
											}else{
												scmpublictoast("链接已复制成功",2000);
											}
											return false;
										}
										
										if(options.isTimeOut==0){
											var shareStyle = "status=no,height=520,width=550,resizable=yes,left=675,top=230,screenX=675,screenY=230,toolbar=no,menubar=no,scrollbars=no,location=no,directories=no";
											window.open(shareUrl, "sharer", shareStyle);
											//window.open(shareUrl);
										}
										return false;
									}
								});
							}else if(_this.attr("class").indexOf("inside")>-1){
								_this.on({
									'click' : function(e){
										//options.showSharePage !=null ? options["showSharePage"](_pthis) : _pthis.click();
										//隐藏分享框
									    var  div_id = options.shareSiteDivId  ;
										var _obj = $("#"+div_id);
										_obj.css("display","none")
										$("body").css("overflow","")
										//end
										return false;
									}
								});
							}//end  遍历click 事件
						}
				)
				//end
			},
			"buildSiteList" : function(options){

				var _html = "";
				if(options.styleVersion == 10){
					if(options.language=='en_US'){
						_html +=' <div class="background_cover_layer" onclick = "hideShareSite(this ,event)" style="display: flex;" id="'+options.shareSiteDivId+'">'
						_html +=' <div class="dialogs_whole" style="width:240px; height:auto; max-height: calc(100% - 8px)">' 
						_html +=' <div class="dialogs_container">' 
					    _html +=' <div class="dialogs_content" style="padding: 24px;">' 
						_html +=' <div class="fc_black54 fz_18" style="margin-bottom: 8px;">分享至</div>' 
						_html +=' <div class="share_plugin_destination_list">' 
							
							
						_html +=' <div class="share_plugin_section  inside" onclick="'+options.shareToSmateMethod+'">' 
						_html +=' <div ><img src="/resscmwebsns/images_v5/share-plugin/share_plugin_smate.png"  class="avatar"></div>' 
						_html +=' <div class="text_content">科研之友</div>' 
						_html +=' </div>' 
									
					
				    	_html +=' </div>' 
					    _html +=' </div>' 
					    _html +=' </div>' 
				        _html +=' </div>' 
					    _html +=' </div>' 
					}else{  
						_html +=' <div class="background_cover_layer"  onclick = "hideShareSite(this ,event)" style="display: flex;" id="'+options.shareSiteDivId+'"> '
						_html +=' <div class="dialogs_whole" style="width:240px; height:auto; max-height: calc(100% - 8px)">' 
						_html +=' <div class="dialogs_container">' 
					    _html +=' <div class="dialogs_content" style="padding: 24px;">' 
						_html +=' <div class="fc_black54 fz_18" style="margin-bottom: 8px;">分享至</div>' 
						_html +=' <div class="share_plugin_destination_list">  ' 
							
						_html +=' <div class="share_plugin_section  inside" onclick="'+options.shareToSmateMethod+'">' 
						_html +=' <div><img src="/resscmwebsns/images_v5/share-plugin/share_plugin_smate.png"  class="avatar"></div>' 
						_html +=' <div class="text_content">科研之友</div>' 
						_html +=' </div>  ' 
					
					
				    	_html +=' </div>' 
					    _html +=' </div>' 
					    _html +=' </div>' 
				        _html +=' </div>' 
					    _html +=' </div>' 
					}
				}else{
					if(options.language=='en_US'){
						_html +=' <div class="background_cover_layer" onclick ="hideShareSite(this ,event)" style="display: flex;" id="'+options.shareSiteDivId+'">'
						_html +=' <div class="dialogs_whole" style="width:240px; height:auto; max-height: calc(100% - 8px)">' 
						_html +=' <div class="dialogs_container">' 
					    _html +=' <div class="dialogs_content" style="padding: 24px;">' 
						_html +=' <div class="fc_black54 fz_18" style="margin-bottom: 8px;">Share</div>' 
						_html +=' <div class="share_plugin_destination_list">' 
							
						_html +=' <div class="share_plugin_section inside" onclick="'+options.shareToSmateMethod+'">' 
						_html +=' <div><img src="/resscmwebsns/images_v5/share-plugin/share_plugin_smate.png" class="avatar"></div>' 
						_html +=' <div class="text_content">ScholarMate</div>' 
						_html +=' </div>' 
									
						_html +=' <div class="share_plugin_section outside weChat" onclick="getOutSideType(this);" type="WeChat">' 
						_html +=' <div><img src="/resscmwebsns/images_v5/share-plugin/share_plugin_weixin.png" class="avatar"></div>' 
						_html +=' <div class="text_content">Wechat</div>' 
						_html +=' </div>  ' 
									
			            _html +=' <div class="share_plugin_section  outside" onclick="getOutSideType(this);" type="sina">' 
						_html +=' <div><img src="/resscmwebsns/images_v5/share-plugin/share_plugin_weibo.png" class="avatar"></div>' 
						_html +=' <div class="text_content">Sina</div>' 
						_html +=' </div>' 
									
						_html +=' <div class="share_plugin_section  outside" onclick="getOutSideType(this);" type="Facebook">' 
						_html +=' <div><img src="/resscmwebsns/images_v5/share-plugin/share_plugin_facebook.png" class="avatar"></div>' 
						_html +=' <div class="text_content">Facebook</div>' 
						_html +=' </div>' 
									
					    _html +=' <div class="share_plugin_section outside" onclick="getOutSideType(this);" type="LinkedIn">' 
					    _html +=' <div><img src="/resscmwebsns/images_v5/share-plugin/share_plugin_linkin.png" class="avatar"></div>' 
					    _html +=' <div class="text_content">LinkedIn</div>' 
					    _html +=' </div>' 
					           
					    _html +=' <div class="share_plugin_section outside links" onclick="getOutSideType(this);" type="Link">' 
					    _html +=' <div><img src="/resscmwebsns/images_v5/share-plugin/share_plugin_link.png" class="avatar"></div>' 
					    _html +=' <div class="text_content">Share link</div>' 
					    _html +=' </div>'

				    	_html +=' </div>' 
					    _html +=' </div>' 
					    _html +=' </div>' 
				        _html +=' </div>' 
					    _html +=' </div>' 
					}else{  
						_html +=' <div class="background_cover_layer"  onclick = "hideShareSite(this ,event)" style="display: flex;" id="'+options.shareSiteDivId+'"> '
						_html +=' <div class="dialogs_whole" style="width:240px; height:auto; max-height: calc(100% - 8px)">' 
						_html +=' <div class="dialogs_container">' 
					    _html +=' <div class="dialogs_content" style="padding: 24px;">' 
						_html +=' <div class="fc_black54 fz_18" style="margin-bottom: 8px;">分享至</div>' 
						_html +=' <div class="share_plugin_destination_list">' 
							
						_html +=' <div class="share_plugin_section  inside" onclick="'+options.shareToSmateMethod+'">' 
						_html +=' <div><img src="/resscmwebsns/images_v5/share-plugin/share_plugin_smate.png"  class="avatar"></div>' 
						_html +=' <div class="text_content">科研之友</div>' 
						_html +=' </div>' 
									
						_html +=' <div class="share_plugin_section outside weChat" onclick="getOutSideType(this);" type="WeChat">' 
						_html +=' <div><img src="/resscmwebsns/images_v5/share-plugin/share_plugin_weixin.png"  class="avatar"></div>' 
						_html +=' <div class="text_content">微信</div>' 
						_html +=' </div>' 
									
			            _html +=' <div class="share_plugin_section  outside" onclick="getOutSideType(this);"  type="sina">' 
						_html +=' <div><img src="/resscmwebsns/images_v5/share-plugin/share_plugin_weibo.png"  class="avatar"></div>' 
						_html +=' <div class="text_content">新浪微博</div>' 
						_html +=' </div>' 
									
						_html +=' <div class="share_plugin_section  outside" onclick="getOutSideType(this);" type="Facebook">  ' 
						_html +=' <div><img src="/resscmwebsns/images_v5/share-plugin/share_plugin_facebook.png"  class="avatar"></div>' 
						_html +=' <div class="text_content">Facebook</div>  ' 
						_html +=' </div>   ' 
									
					    _html +=' <div class="share_plugin_section outside" onclick="getOutSideType(this);" type="LinkedIn">  ' 
					    _html +=' <div><img src="/resscmwebsns/images_v5/share-plugin/share_plugin_linkin.png"  class="avatar"></div>' 
					    _html +=' <div class="text_content">LinkedIn</div>  ' 
				        _html +=' </div>   '
				    	   
				        _html +=' <div class="share_plugin_section outside links" onclick="getOutSideType(this);" type="Link">' 
				        _html +=' <div><img src="/resscmwebsns/images_v5/share-plugin/share_plugin_link.png"  class="avatar"></div>' 
					    _html +=' <div class="text_content">分享链接</div>' 
					    _html +=' </div>'
				        _html +=' </div>' 
					    _html +=' </div>' 
					    _html +=' </div>' 
				        _html +=' </div>' 
					    _html +=' </div>' 
					}
				}
				
				
					
				return _html;
			},
			"initShareTxt" : function(_pobj,_obj,options){
				var resType = $("#share_to_scm_box").attr("resType");
				var des3ResId  = $("#share_to_scm_box").attr("des3ResId");
				switch (resType) {
				case "prj":
					resType=4;
					break;
				case "pub":
					resType=1;
					break;
				case "pdwhpub":
					resType=22;
					break;
				case "fund":
					resType=11;
					break;
				case "agency":
				    resType = 25;
				    break;
				default:
					break;
				}
				if(resType==null||$.trim(resType)==""){
					resType = _pobj.attr("resType");
				}
				if(des3ResId==null||$.trim(des3ResId)==""){
					des3ResId = _pobj.attr("des3ResId");
				}
				var dbid = _pobj.attr("dbid");
				var des3Id=$("#des3PsnId").val();
				var resumeId=$("#view_resume_resId").val();
				
				var post_data = {'des3ResId' : des3ResId, 'resType' : resType};
				if (dbid!=undefined && dbid!='') {
					post_data['dbid'] = dbid;
				}
				var resInfoId = $("#share_to_scm_box").attr("resInfoId");
				var fundId = $("#share_to_scm_box").attr("fundId");
			    var resInfoJson = "";
			    if(resInfoId != null && resInfoId != "" && typeof(resInfoId) != "undefined" && fundId != undefined){
			    	resInfoJson = JSON.stringify({
			    		"fund_desc_zh": $("#zhShowDesc_"+resInfoId).val(),
			    		"fund_desc_en": $("#enShowDesc_"+resInfoId).val(),
			    		"fund_title_zh": $("#zhTitle_"+resInfoId).val(),
			    		"fund_title_en": $("#enTitle_"+resInfoId).val(),
			    		"fund_logo_url": $("#share_to_scm_box").attr("logoUrl")
			    	});
			    	post_data["resType"] = "11";
			    	post_data["resIds"] = fundId;
			    }
			    post_data["resInfoJson"] = resInfoJson;
			    post_data["platform"]=type;
			  if(resType == "29"){//分享机构
			    var insName = $("#share_to_scm_box").attr("insName");
			    var insPic = $("#share_to_scm_box").attr("insPic");
			    var insUrl = $("#share_to_scm_box").attr("insUrl");
			    post_data["insName"]=insName;
			    post_data["insPic"]=insPic;
			    post_data["insUrl"]=insUrl;
			  }
				$.ajax({
					url :options['shareTxtUrl'],
					type : 'post',
					dataType : "json",
					async: false,
					data : post_data,
					success : function(data){
						var timeOut = data['ajaxSessionTimeOut'];
            			if(timeOut != null && typeof timeOut != 'undefined' && timeOut == 'yes'){
            				jConfirm(tips["sessionTimeout_" + locale], tips["prompt_" + locale], function(r){
				   				if (r) {
				   					var url=window.location.href;
				   					if(url.indexOf("/in")>0 || url.indexOf("/pv")>0){//站外的需要设置站内地址才会跳转到登录页面
				   						window.location.href="/scmwebsns/resume/psnView?des3PsnId="+des3Id;  
				   					}else if(url.indexOf("/personalResume/view")>0){//站外的需要设置站内地址才会跳转到登录页面
				   						window.location.href="/scmwebsns/index?service="+url;  
				   					}else if(url.indexOf("/pubweb/search") > 0){
				   						window.location.href="/pubweb/getoauth?url="+encodeURIComponent(url); 
				   					}else{
				   					document.location.reload();
				   					}
				   				}
				   			});
            				options.isTimeOut=1;
            	        }else if (data.result == "success" && data.shareTxt != undefined && data.shareTxt != '') {
							var pubCon = eval("[" + data.shareTxt + "]");
							if (pubCon[0].resCount == 1) {
								options.url = pubCon[0].resLink;
								options.mobileResUrl = pubCon[0].mobileResLink;
								options.pic = pubCon[0].pic;
								options.category = resType;
								if(options.category == 1 || options.category == 2 || options.category == 22) {
									options.irisText = '#论文分享#';
								} else if(options.category == 4) {
									options.irisText = '#项目分享#';
								} else if(options.category == 11){
									options.irisText = '#基金分享#';
									options.category = 11;
								} else if(options.category == 25){
								    options.irisText = '#资助机构分享#';
								} else if(options.category == 26){
                    options.irisText = '#新闻消息分享#';
                }else if(options.category == 6){
                  options.irisText = '#个人主页分享#';
              }else if(options.category == 29){
                  options.irisText = '#机构分享#';
              }
								options.language = pubCon[0].language;
								options.authorNames = pubCon[0].authorNames;
								options.title = pubCon[0].title;
								options.source = pubCon[0].resOther;
							}
							
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
				var type = $(_obj).attr('type') || 'sina';
				shareUrl = requestUrl[type] + this[type](options);
				return shareUrl;
			},
			"showShareSites" : function(opts  ,_pthis){
					    this.initSiteList(opts)  ;
				        var  div_id = opts.shareSiteDivId  ;
						var _obj = $("#"+div_id);
						_obj.css("display","flex")
						//$("body").css("overflow","hidden") SCM-12991
					
		     }
			,
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
			'rebuildLanguage':function(){//重新构建语言，由于这边获取的中文语言表示为'zh',但在请求时要求传输'zh_CN'
			  if(locale.indexOf('zh')!=-1){
			    return 'zh_CN';
			  }else{
			    return 'en_US';
			  }
			},
			'addUrlParam':function(url,paramKeyValue){
			  if(url.indexOf("?")!=-1){
			    return url+"&"+paramKeyValue;
			  }else{
			    return url+"?"+paramKeyValue;
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
				// 统一改为新规则：英文字符+空格
				// var split =  (options.language != undefined && "zh" == options.language) ? ",&nbsp;" : ",&nbsp;";
				var split = ", "
				var shareContent = options.irisText;
				if (options.authorNames) {
					options.authorNames = options.authorNames.replace(/<strong>|<\/strong>/ig, '');
					shareContent = shareContent + options.authorNames + split;
				}
				shareContent += options.title;
				if (options.source) {
					shareContent = shareContent + split + options.source;
				}
				if (this.textLength(shareContent) > maxTxtLen) {
					shareContent = options.irisText;
					if (options.authorNames) {
						// 统一改为新规则：英文字符+空格
						// var split2 =  (options.language != undefined && "zh" == options.language) ? "；" : "; ";
						var split2 = "; "
						var authorNames = options.authorNames.split(/; |；/);
						for (var i = 0; i < authorNames.length && i < 3; i++) {
							shareContent = shareContent + authorNames[i] + split2;
						}
						shareContent = shareContent.substring(0, shareContent.length - (split2 == '；' ? 1 : 2)) + split;
					}
					shareContent = shareContent + options.title;
					if (options.source) {
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
				if (options.category == 29 ||options.category == 6 ||options.category == 26 || options.category == 99 || options.category == 1 || options.category == 2 || options.category == 4 || options.category == 11 || options.category == 22 || options.category == 25) {
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
				if (options.category == 99 || options.category == 1 || options.category == 2 || options.category == 4 || options.category == 11 || options.category == 25) {
					params.title = this.getShareContent(options, 140);
				}
				return this.buildUrl(params).join('&');
			},
			'LinkedIn' : function(options) {
				var params = {
						'mini' : 'true',
						'url' : this.addUrlParam(options.url,"locale="+this.rebuildLanguage()),//指定使用的语言，防止出现分享时为中文，实际获取到的却是英文的
						'source' : 'ScholarMate',
						'title' : options.title,
						'summary' :options.source,
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
				if(options.mobileResUrl == null || options.mobileResUrl == ""){
				    options.mobileResUrl = options.url
				}
				return options.mobileResUrl;
			},
			'Link' : function(options){
				return  options.url;
			},
			/**
			 * 分享到站外时累加分享次数
			 */
			"ajaxAddShareCount" : function(obj){
				var resType = $(obj).attr("resType");
				var resId = $(obj).attr("resId");
				if(  !$.isNumeric(resId)){//由于resId 加密原因需判断一下
					resId = '\"'+resId +'\"' ;
				}
				var resNode = $(obj).attr("nodeId");
				if(typeof resNode == 'undefined'){
					resNode = $(obj).attr("resNode");
				}
				var jsonParam = "{\"resType\":" + resType + ",\"resId\":" + resId + ",\"resNode\":" + resNode + ",\"shareTitle\":" + "\"分享到站外\"" + ",\"shareEnTitle\":" + "\"Share to outside\"" + "}";
				$.ajax({
					type:"post",
					url: ctxpath + "/dynamic/ajaxAddShareCounts",
					data:{
						"jsonParam" : jsonParam
					},
					dataType: "json",
					success:function(data){
//						alert("分享成功！");
					}
				});
			},
			//设置分享的平台
			"resetSharePlatform": function(obj){
			    //1:动态,2:联系人,3:群组,4:微信,5:新浪微博,6:Facebook,7:Linkedin
			    var shareType = $(obj).attr("type");
			    var sharePlatform = "";
			    if(shareType != null && typeof(shareType) != "undefined"){
			        sharePlatform = platformMap.get(shareType);
			    }
			    var sharePlatformInput = $("#share_platform_val")[0];
			    if(sharePlatformInput == null || typeof(sharePlatformInput) == "undefined"){
			        var body = document.getElementsByTagName("body")[0];
			        var input = document.createElement("input");
                    input.id = "share_platform_val";
                    input.style.display = "none";
                    body.appendChild(input);
			        sharePlatformInput = $("#share_platform_val")[0];
			    }
			    sharePlatformInput.value = sharePlatform;
			}
			

	};
	
    hideShareSite = function(obj ,event){
    	if (event.target !== obj) {
    		return false;
    	} else {
    		 $(obj).css("display" , "none");
    		 $("body").css("overflow","")
    	}
    }	
    
    getOutSideType = function(obj){
    	var $obj = $("#share_to_scm_box");
    	var des3ResId = $obj.attr("des3ResId");
    	var pubId = $obj.attr("pubId");
    	var resType = $obj.attr("resType");
            $.ajax({
            url : '/dynweb/outside/ajaxgetsharetxt',
            type : 'post',
            dataType : 'json',
            data : {
                'resId':des3ResId,
                'resType':resType
            },
            success : function(data) {
                if (data.result == "success") {
                    $('.shareCount_'+pubId).html("("+data.shareCount+")");
                }
            }
        });
		
    	var outsideType = $(obj).attr("type");
    	type = 1;
    	if(outsideType != null && typeof(outsideType) != "undefined"){
    	    type = platformMap.get(outsideType);
        }
        dealShareCount();
    }


	
	var requestUrl = {
			'sina' : 'http://v.t.sina.com.cn/share/share.php?',
			'tencent' : 'http://share.v.t.qq.com/index.php?c=share&a=index&',
			'LinkedIn' : 'http://www.linkedin.com/shareArticle?',
			'Facebook' : 'https://www.facebook.com/sharer/sharer.php?',
			'WeChat' : '',
			'Link' : ''
				
	};
	var tips = {
            "prompt_zh_CN" : "提示",
            "prompt_en_US" : "Reminder",
		    "sessionTimeout_zh_CN" : "系统超时或未登录，你要登录吗？",
		    "sessionTimeout_en_US" : "You are not logged in or session time out, please log in again."
    };
	
	var platformMap = new Map();
    platformMap.set("WeChat", 4);
    platformMap.set("sina", 5);
    platformMap.set("Facebook", 6);
    platformMap.set("LinkedIn", 7);
    platformMap.set("Link", 8);
})(jQuery);


// 处理分享数量的
var dealShareCount = function (sum) {
    var addSum = 1;
    if(sum   != undefined){
        addSum = sum;
    }
    var $obj = $("#share_to_scm_box");
    var resid = $obj.attr("resid");
    var resType = $obj.attr("resType");
    switch (resType){
        case "26" :
            dealNewsShare($obj ,addSum);
            break;
        case "29":
            dealInsShare($obj ,addSum);
            break;
    }

}

function dealNewsShare( $obj ,  addSum) {
    //dev_news_share_58
    var content = $('.dev_news_share_'+$obj.attr("newsid")).html();
    if(content == undefined ){ // 动态的
        content = $(".dynamic-social__item[des3resid='"+$obj.attr("des3resid")+"']").html();
    }
    if( content == undefined || content.indexOf("K")>1 || content.indexOf("k")>1){
        return;
    }
    var count = content.replace(/\D+/g,"");
    if(count == ""){
        $('.dev_news_share_'+$obj.attr("newsid")).html(content+"("+addSum+")");
        // 动态的
        $(".dynamic-social__item[des3resid='"+$obj.attr("des3resid")+"']").html(content+"("+addSum+")");
        return;
    };
    count =parseInt(count)+addSum;
    if(count>999){
        count = "1K+";
    }
    content = content.replace(/\d+/g,count);
    $('.dev_news_share_'+$obj.attr("newsid")).html(content)
    // 动态的
    $(".dynamic-social__item[des3resid='"+$obj.attr("des3resid")+"']").html(content);
}

function dealInsShare( $obj ,  addSum) {
  var content = $('.dev_ins_share_'+$obj.attr("insid")).html();
  if( content == undefined || content.indexOf("K")>1 || content.indexOf("k")>1){
    return;
  }
  var count = content.replace(/\D+/g,"");
  if(count == ""){
      $('.dev_ins_share_'+$obj.attr("insid")).html(content+"("+addSum+")");
      return;
  };
  count =parseInt(count)+addSum;
  if(count>999){
    count = "1K+";
  }
  content = content.replace(/\d+/g,count);
  $('.dev_ins_share_'+$obj.attr("insid")).html(content);
}
