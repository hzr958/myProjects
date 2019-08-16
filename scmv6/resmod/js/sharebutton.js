(function(){
	var a={id:"wx",label:"微信",class_name:"rec-wx",log_vendor:"bshare_wx",url:"javascript:DoubanShareQRCode('{{url}}');"};
	window.ShareQRCode=function(o){
		var p=[175,175];
		var n="width:"+p[0]+"px;height:"+p[1]+"px;display:inline-block;*display:inline;zoom:1;overflow:hidden;background:#f4f4f4;vertical-align:top;text-align:center;line-height:"+p[1]+"px;";
		o=encodeURIComponent(o);
		var con = '<div id="share-qr-img" style="'+n+'"></div>';
		var ti = "";
		if(locale == "zh_CN"){
			ti = "扫描下方二维码分享到微信";
			con = con + '<img src="' + ressns + '/images/new_qr_help.jpg" style="width:200px; height: auto;" width="200">';
		}else{
			ti = "Scan QR Code to Share to Wechat";
			con += '<img src="' + ressns + '/images/new_qr_help_en.jpg" style="width:200px; height: auto;" width="200">';
		}
		var q=dui.Dialog({width:420,title:ti,content:con}).open();
		q.update()
	};
	function c(){
		var n=Array.prototype.slice.call(arguments);
		var m=n.shift();
		for(var o=0,r,q;r=n[o++];){
			for(q in r){
				if(r.hasOwnProperty(q)){
					m[q]=r[q]
				}
			}
		}
		return m
	}
	function d(o,n,m){
		if(!n){
			return
		}
		o[n]={};
		return c(o[n],m)
	}
	function h(m){
		return Object.prototype.toString.call(m)==="[object Array]"
	}
	function f(o){
		if(document.getElementById("css-share-button")){
			return
		}
		var m=document.createElement("style");
		m.type="text/css";
		m.id="css-share-button";
		if(m.styleSheet){
			m.styleSheet.cssText=o
		}else{
			m.appendChild(document.createTextNode(o))
		}try{
			document.getElementsByTagName("head")[0].appendChild(m)
		}catch(n){}
	}
	var b="background-repeat: no-repeat !important; background-position: 100% -19px !important;*display:inline-block;} a.bn-sharing:hover {text-decoration:underline;} .bn-sharing-on {background-position:100% 4px !important;position:relative !important;z-index:1 !important;}      #db-div-sharing {position:absolute;width:100px;*margin-top:-2px;}      #db-div-sharing .bd {border:1px solid #aaa;background:#fff;padding:10px 0 0 10px;}      #db-div-sharing .bd li {line-height:17px;margin-bottom:10px;}      #db-div-sharing .hd {position:absolute;height:24px;*line-height:21px;overflow:hidden;right:0;top:-24px;padding:0 5px;border:1px solid #aaa;border-bottom:none;background:#fff;}      .rec-wx{padding-left:20px;background:url({icons}) no-repeat 0 0;}.rec-wx {background-position:0 -128px;}   ";
	b=b.replace("{icons}",window.DoubanShareIcons||"");
	var k={
		defaultShareItems:["wx"],
		services:{},
		dom:{button:null,list:null},
		renderUrl:function(m,r){
			var o=(m.id=="sina"&&r.type=="Topic")?this.services.sina_topic.url:m.url;
			var q=encodeURIComponent;
			o=o.replace("{{url}}",q(r.url))
					.replace("{{title}}",q(r.title))
					.replace("{{image_url}}",q(r.image_url))
					.replace(/'/g,"%27");
					if(m.popup){
						var n=m.popup.width,p=m.popup.height;
						o='javascript:void((function(s,d){var%20f="'+q(o)+'";function%20a(){if(!window.open(f,"mb",["toolbar=0,status=0,resizable=1,width='+n+",height="+p+',left=",(s.width-'+n+')/2,",top=",(s.height-'+p+')/2].join("")))location.href=f};if(/Firefox/.test(navigator.userAgent))setTimeout(a,0);else%20a()})(screen, document))'}return o},genMenuListHTML:function(q){var p;var o=[];var n=this.menuItems;for(var m=0;m<n.length;m++){p=this.services[n[m]];if(!p){continue}o.push('              <li class="'+(p.class_name||"")+'">              <a '+(p.popup||/^javascript\:/i.test(p.url)?"":'target="_blank"')+" href='"+this.renderUrl(p,q)+"'>"+p.label+"</a>              </li>")}o='<div class="hd">&nbsp;</div>                  <div class="bd">                  <ul>'+o.join("")+"</ul>                  </div>";return o},wrapShareParams:function(q){var s={};var o=q.srcElement;var r;var n;var m;if(o.hasClass("in-list")){var p=o.closest(".ctsh").find("h3 a:eq(0)");r=q.title||p.text()}else{r=q.title||document.title}s.title=r;s.type=q.type||"douban";s.url=q.url||location.href;m=q.pic;if(!m){m=(q.type=="Topic")?$(".topic-figure img").attr("src"):$("#mainpic img").attr("src")}s.image_url=m||"";return s},hideMenu:function(){k.dom.button&&k.dom.button.removeClass("bn-sharing-on");k.dom.list&&k.dom.list.hide()},bindEvents:function(){var m=this;$(document.body).click(function(p){var o=$(p.target);if(!o.hasClass("bn-sharing")||o.hasClass("bn-sharing-on")){m.hideMenu();return}p.preventDefault();p.stopPropagation();o.addClass("bn-sharing-on");var n=k.dom.list;if(!n){n=$('<div id="db-div-sharing"></div>').appendTo("body");k.dom.list=n;k.dom.button=$(".bn-sharing")}var q=m.wrapShareParams(c({srcElement:o},o.data()));n.html(m.genMenuListHTML(q));var r=o.offset();n.find(".hd").css("width",o[0].offsetWidth+"px");n.css("top",(r.top+24-4)+"px").css("left",(r.left-n.width()+o.width()+16)+"px");n.find(".hd a").addClass("bn-sharing-on");n.show()});window.onresize=this.hideMenu},init:function(m){var p=[];var o=0;if(m&&h(m)){var n;while(n=m[o++]){if(typeof n==="string"){p.push(n)}else{p.push(n.id);d(this.services,n.id,n)}}}this.menuItems=p.length?p:this.defaultShareItems;f(b);this.bindEvents()}};d(k.services,a.id,a);window.ShareButton=k})();window.ShareButton.init();