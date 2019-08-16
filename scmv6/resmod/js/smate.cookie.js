/**
 * 系统页面cookie处理的JS.
 * @author MJG
 */
var Cookie =
{
		"setCookie":function(name, value) {// 写入cookies.
			document.cookie = name + "=" + escape(value)
					+ ";domain=scholarmate.com;path=/";
		},
		"setCookieTime":function(name, value,day) {// 写入cookies.
			var exp = new Date();    
			exp.setTime(exp.getTime() + day*24*60*60*1000);   
			
			document.cookie = name + "=" + escape(value)
					+ ";domain=scholarmate.com;path=/" + ";expires=" + exp.toGMTString(); ;
		},
		"getCookie":function(name) {// 读取cookies
			var arr, reg = new RegExp("(^|)" + name + "=([^;]*)(;|$)");
			if (arr = document.cookie.match(reg))
				return unescape(arr[2]);
			else
				return null;
		},
		"foreachDel":function() {// js 遍历所有Cookie
			var strCookie = document.cookie;
			var arrCookie = strCookie.split("; "); // 将多cookie切割为多个名/值对
			for ( var i = 0; i < arrCookie.length; i++) { // 遍历cookie数组，处理每个cookie对
				var arr = arrCookie[i].split("=");
				if (arr.length > 0)
					delCookie(arr[0]);
			}
		},
		"delCookie":function(name) {// 删除cookie
			var exp = new Date();
			exp.setTime(exp.getTime() - 10000);
			var cval = this.getCookie(name);
			document.cookie = name + "=" + cval + "; expires=" + exp.toGMTString()+"; path=/; domain=.scholarmate.com";
		}
};







