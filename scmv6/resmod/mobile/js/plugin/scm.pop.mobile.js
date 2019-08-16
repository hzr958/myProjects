;(function(window, document) {
	window.Smate = window.Smate || {};
	/**
	 * 移动端确认弹窗
	 * @param {Object} t 标题
	 * @param {Object} c 内容
	 * @param {Object} f 回调函数
	 * @param {Object} d 按钮文本，例如：['确定', '取消']，['是', '否']，['confirm', 'cancel']
	 */
	Smate.confirm = function(t, c, f, d){
		var o = {
				"type": "confirm",
				"title": t,
				"content": c,
				"callback": f,
		};
		if(d && d.length == 2){
			$.extend(o, {"ok": d[0], "cancel": d[1]});
		}
		var a = new x(o);
		a.show();
		return a;
	};
	var $d = $('<div class="pop mask-transparent hide">\
					<div class="pop-wrap confirm">\
					    <div class="wrap-content">\
						   	<div class="title"></div>\
						   	<div class="content"></div>\
						   	<div class="btns">\
						   		<button class="cancel"></button>\
						   		<button class="ok"></button>\
						   	</div>\
					    </div>\
				    </div>\
				</div>'),
		$h = $d.find('.pop-wrap'),
		$c = $h.find(".btns .cancel"),
		$o = $h.find(".btns .ok"),
    	$t = $h.find(".title"),
    	$n = $h.find(".content"),
    	to;
    	
	var x = function(o){
		this.options = $.extend({
			"content": "",
			"title": "提示",
			"type": "toast",
			"ok": "确定",
			"cancel": "取消",
			"callback": function(){},
			"tap": false
		}, o || {});
		this.init();
	};
	x.prototype = {
		init: function(){
			var t = this,
				o = t.options,
				p = o.type,
				f = o.callback,
				c = $o.attr("class"),
				e = !!o.tap ? "tap" : "click";
			c.replace(/(msg|alert|confirm)/i, p);
			$o.attr("class", c);
			$n.html(o.content);
			if(p == "alert"){
				$o.css("width", "100%");
			}else if(p == "confirm"){
				$t.html(o.title);
				$n.html(o.content);
				$o.html(o.ok).removeClass("hide");
				$c.html(o.cancel);
				$o.css("width", "");
				$o.off(e).on(e, function(e){
					$o.attr("disabled", "disabled");
					t.hide();
					f.call(t, e);
					setTimeout(function(){
						$o.removeAttr("disabled");
					}, 1000);
				});
				$c.off(e).on(e, function(e){
					t.hide();
				});
			}
			$h.off("resize").on("resize", function(){
				setTimeout(function(){
					t.pos();
				}, 500);
			});
			$("body").append($d);
		},
		pos: function(){
			var t = this,
				d = document,
				e = d.documentElement,
				b = d.body,
				w, h, cw, ch, l, r, sl, st;
			t.isHide() || (st = b.scrollTop, sl = b.scrollLeft, 
				cw = b.clientWidth, ch = b.clientHeight,
				w = $h.width(), h = $h.height(), $h.css({
					top: (ch - h) / 2,
	                left: (cw - w) / 2
				}));
		},
		isHide: function(){
			return $d.hasClass("hide");
		},
		isShow: function(){
			return !this.isHide();
		},
		_show: function(){
			var f = this.options.onShow;
			$d.css("opacity", "1");
			f && f.call(this);
		},
		show: function(){
			var t = this;
			to && (clearTimeout(to), to = void 0);
			t.isShow() ? t._show() : ($d.css("opacity", "0").removeClass("hide"), t.pos(), setTimeout(function() {
                t._show();
            }, 300), setTimeout(function() {
                $d.animate({
                    opacity: "1"
                }, 300, "linear")
            }, 1));
		},
		_hide: function(){
			var f = this.options.onHide;
			$d.css("opacity", "0");
			f && f.call(this);
		},
		hide: function(){
			var t = this;
			t.isHide() ? t._hide() : ($d.css("opacity", "1").addClass("hide"), setTimeout(function() {
                t._hide()
            }, 300), setTimeout(function() {
                $d.animate({
                    opacity: "0"
                }, 300, "linear")
            }, 1));
		},
		flash: function(s){
			var t = this,
				o = t.options;
			o.onShow = function(){
				to = setTimeout(function() {
                    to && t.hide()
                }, s);
			};
			t.show();
		}
	};
    	
}(window, document));