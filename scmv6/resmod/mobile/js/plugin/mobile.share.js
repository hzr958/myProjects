    //测试页面请看testshare.html页面
	var scmShare = scmShare ? scmShare : {};
	'use strict';
	var UA = navigator.appVersion;
    var uc = UA.split('UCBrowser/').length > 1 ? 1 : 0; //是否是 UC 浏览器
    var qq = UA.split('MQQBrowser/').length > 1 ? 2 : 0; //判断 qq 浏览器,然而qq浏览器分高低版本, 2:代表高版本, 1:代表低版本
    var inQQ = UA.indexOf('QQ/') > -1 ? 1 : 0; //判断是否是QQ内置浏览器
    var wx = /micromessenger/i.test(UA); //是否是微信
    var qqVs = qq ? parseFloat(UA.split('MQQBrowser/')[1]) : 0; //浏览器版本
    var ucVs = uc ? parseFloat(UA.split('UCBrowser/')[1]) : 0;  //浏览器版本
    //获取操作系统信息  iPhone(1)  Android(2)
    var os = (function () {                        
        var ua = navigator.userAgent;
        if (/iphone|ipod/i.test(ua)) {
            return 1;
        } else if (/android/i.test(ua)) {
            return 2;
        } else {
            return 0;
        }
    }());
    //qq浏览器下面 是否加载好了相应的api文件
    var qqBridgeLoaded = false;
    // 进一步细化版本和平台判断
    if ((qq && qqVs < 5.4 && os == 1) || (qq && qqVs < 5.3 && os == 1)) {
        qq = 0;
    } else {
        if (qq && qqVs < 5.4 && os == 2) {
            qq = 1;
        } else {
            if (uc && ((ucVs < 10.2 && os == 1) || (ucVs < 9.7 && os == 2))) {
                uc = 0;
            }
        }
    }
    /**
     * qq浏览器下面 根据不同版本 加载对应的bridge
     * @method loadqqApi
     * @param  {Function} cb 回调函数
     */
    function loadqqApi(cb) {
        if (!qq) {
            return cb && cb();
        }
        var script = document.createElement('script');
        script.src = (+qq === 1) ? '//3gimg.qq.com/html5/js/qb.js' : '//jsapi.qq.com/get?api=app.share';
        /**
         * 需要等加载过 qq 的 bridge 脚本之后
         * 再去初始化分享组件
         */
        script.onload = function () {
        	cb && cb();
        };
        
        document.body.appendChild(script);
    }
    /**
     * UC浏览器分享
     * @method ucShare
     */
    function ucShare(config) {
        // ['title', 'content', 'url', 'platform', 'disablePlatform', 'source', 'htmlID']
        // 关于platform
        // ios: kWeixin || kWeixinFriend;
        // android: WechatFriends || WechatTimeline
        // uc 分享会直接使用截图
        var platform = '';
        var shareInfo = null;
        // 指定了分享类型
        if (config.type) {
            if (os == 2) {
                platform = config.type == 1 ? 'WechatTimeline' : 'WechatFriends';
            } else if (os == 1) {
                platform = config.type == 1 ? 'kWeixinFriend' : 'kWeixin';
            }
        }
        shareInfo = [config.shareTitle, config.desc, config.shareUrl, platform, '', '', ''];
        // android 
        if (window.ucweb) {
            ucweb.startRequest && ucweb.startRequest('shell.page_share', shareInfo);
            return;
        }
        if (window.ucbrowser) {
            ucbrowser.web_share && ucbrowser.web_share.apply(null, shareInfo);
            return;
        }
    }
    /**
     * qq 浏览器分享函数
     * @method qqShare
     */
    function qqShare(config) {
        var type = config.type;
        //微信联系人 1, 微信朋友圈 8
        type = type ? ((type == 1) ? 8 : 1) : '';

        var share = function () {
            var shareInfo = {
                'url': config.shareUrl,
                'title': config.shareTitle,
                'description': config.desc,
                'img_url': config.shareImg,
                'img_title': config.shareTitle,
                'to_app': type,
                'cus_txt': ''
            };

            if (window.browser) {
                browser.app && browser.app.share(shareInfo);
            } else if (window.qb) {
                qb.share && qb.share(shareInfo);
            }
        };

        if (qqBridgeLoaded) {
            share();
        } else {
            loadqqApi(share);
        }
    }
    
    
  //uc或qq浏览器里面分享到微信朋友圈或微信联系人
    function mShare(config) {
        this.config = config;
        this.init = function (type) {
            if (typeof type != 'undefined') this.config.type = type;
            try {
                if (uc) {
                    ucShare(this.config);
                } else if (qq && !inQQ && !wx) {
                    qqShare(this.config);
                } else{
                	scmShare.showShareDialog(this.config);
                }
            } catch (e) {
            }
        }
    }
    
	scmShare.share = function(config, shareType){
		var defaultConfig = {
				shareUrl: "", // 分享的链接
				shareTitle: "", // 分享的标题
				shareImg: "", // 分享的图片url
				defaultShareImg: "", // 若分享的图片url为空,则用这个默认的图片url
				desc: "", // 描述信息
				summary: "", // 概要信息
				source: "", // 来源，域名
				appKey: "", // 微博那边有个appKey参数
				type: "" //分享类型，分享到微信时用来判断是分享到朋友圈还是微信联系人
		};
		if (config) {
			defaultConfig=$.extend({},defaultConfig, config);
		}
		switch(shareType){
			case "linkedin": 
				scmShare.shareToLinkedIn(defaultConfig);
				break;
			case "weibo":
				scmShare.shareToWeibo(defaultConfig);
				break;
			case "qzone":
				scmShare.shareToQzone(defaultConfig);
				break;
			case "douban":
				scmShare.shareToDouban(defaultConfig);
				break;
			case "pengyouquan":
				var mshareFun = new mShare(defaultConfig);
				mshareFun.init(1);
				break;
			case "wechatfriend":
				var mshareFun = new mShare(defaultConfig);
				mshareFun.init(2);
				break;
			default:
				break;
		}
	}
	//分享到微博
	scmShare.shareToWeibo = function(config){
        var url = encodeURIComponent(config.shareUrl);
        var title = encodeURIComponent(config.shareTitle);
        var shareImgUrl = config.shareImg;
        if(!shareImgUrl){
            shareImgUrl = config.defaultShareImg;
        }
        var imgUrl = encodeURIComponent(shareImgUrl);
        var shareUrl = "https://service.weibo.com/share/share.php?url="+url+"&title="+title+"&pic="+imgUrl+"&appkey="+config.appKey;
        window.location.href = shareUrl;
    }
    
    //分享到linkedIn
	scmShare.shareToLinkedIn = function(config){
        var url = encodeURIComponent(config.shareUrl);
        var title = encodeURIComponent(config.shareTitle);
        var summary = encodeURIComponent(config.summary);
        var source = encodeURIComponent(config.source);
        var shareUrl = "http://www.linkedin.com/shareArticle?mini=true&title="+title+"&url="+url+"&summary="+summary+"&source="+source+"&armin=armin";
        window.location.href = shareUrl;
    }
    
    //分享到QQ空间
	scmShare.shareToQzone = function(config){
        var url = encodeURIComponent(config.shareUrl);
        var title = encodeURIComponent(config.shareTitle);
        var summary = encodeURIComponent(config.summary);
        var desc = config.desc;
        var site = encodeURIComponent(config.source);
        var shareImgUrl = config.shareImg;
        if(!shareImgUrl){
            shareImgUrl = config.defaultShareImg;
        }
        var imgUrl = encodeURIComponent(shareImgUrl);
        var shareUrl = "http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url="+url+"&title="+title+"&desc="+desc+"&summary="+summary+"&site="+site+"&pics="+imgUrl;
        window.location.href = shareUrl;
    }
    
    //分享到豆瓣
	scmShare.shareToDouban = function(config){
        var url = encodeURIComponent(config.shareUrl);
        var title = encodeURIComponent(config.shareTitle);
        var summary = encodeURIComponent(config.summary);
        var shareImgUrl = config.shareImg;
        if(!shareImgUrl){
            shareImgUrl = config.defaultShareImg;
        }
        var imgUrl = encodeURIComponent(shareImgUrl);
        var shareUrl = "http://shuo.douban.com/!service/share?href="+url+"&name="+title+"&text="+summary+"&image="+imgUrl+"&starid=0&aid=0&style=11";
        window.location.href = shareUrl;
    }
    
    
    
    
	scmShare.showShareDialog = function(config){
    	//是微信或QQ里面
    	if(wx || inQQ){
    		$("#share_dialog_img").addClass("share_wx_weixin");
    	}
    	//微信朋友圈
    	if(config.type == 1){
    		//是其他浏览器
    		if(!uc && !inQQ && !wx){
    			$("#share_dialog_img").removeClass("share_wx_weixin share_wxf_other_browser");
    			$("#share_dialog_img").addClass("share_wxt_other_browser");
    		}
    	}
    	//微信联系人
    	if(config.type == 2){
    		//是其他浏览器
    		if(!uc && !inQQ && !wx){
    			$("#share_dialog_img").removeClass("share_wx_weixin share_wxt_other_browser");
    			$("#share_dialog_img").addClass("share_wxf_other_browser");
    		}
    	}
    	$("#share_dialog_img").show();
    }
    
    //隐藏提示
    scmShare.hideShareDialog = function(){
    	$("#share_dialog_img").hide();
    }
    
    //添加div用来显示提示文案
    scmShare.buildImgTipsDiv = function(){
    	$("body").append('<div class="scan_addfriend" id="share_dialog_img" style="display:none;" onclick="scmShare.hideShareDialog();"></div>');
    }
    
    // 预加载 qq bridge
    loadqqApi(function () {
        qqBridgeLoaded = true;
    });
    if (typeof module === 'object' && module.exports) {
        module.exports = mShare;
    } else {
        window.mShare = mShare;
    }
