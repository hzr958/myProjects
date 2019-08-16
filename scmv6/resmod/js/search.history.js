/**
 * History
 * 用法
 * var his = new History('key');  // 参数标示cookie的键值
 * his.add("标题", "连接 比如 http://www.baidu.com", "其他内容")； 
 * 得到历史数据 返回的是json数据
 * var data = his.getList();  // [{title:"标题", "link": "http://www.baidu.com"}]
 * 
 */
function History(key) {
    this.key = key || 'y_his';  // 键值
    this.jsonData = null;  // 数据缓存
    this.cacheTime = 720;  // 30天
    this.path = '/';  // cookie path
}
History.prototype = {
    constructor : History
    ,setCookie: function(name, value, expiresHours, options) {
        options = options || {};
        var cookieString = name + '=' + encodeURIComponent(value);
        //判断是否设置过期时间
        if(undefined != expiresHours){
            var date = new Date();
            date.setTime(date.getTime() + expiresHours * 3600 * 1000);
            cookieString = cookieString + '; expires=' + date.toUTCString();
        }

        var other = [
            options.path ? '; path=' + options.path : '',
            options.domain ? '; domain=' + options.domain : '',
            options.secure ? '; secure' : ''
        ].join('');

        document.cookie = cookieString + other; 
    }
    ,getCookie: function(name) {
        // cookie 的格式是个个用分号空格分隔
        var arrCookie = document.cookie ? document.cookie.split('; ') : [],
            val = '',
            tmpArr = '';

        for(var i=0; i<arrCookie.length; i++) {
            tmpArr = arrCookie[i].split('=');
            tmpArr[0] = tmpArr[0].replace(' ', '');  // 去掉空格
            if(tmpArr[0] == name) {
                val = decodeURIComponent(tmpArr[1]);
                break;
            }
        }
        return val.toString();
    }
    ,deleteCookie: function(name) {
        this.setCookie(name, '', -1, {"path" : this.path});
        //console.log(document.cookie);
    }
    ,initRow : function(title, link, other) {
        return '{"title":"'+title+'", "link":"'+link+'", "other":"'+other+'"}';
    }
    ,parse2Json : function(jsonStr) {
        var json = [];
        try {
            json = JSON.parse(jsonStr);
        } catch(e) {
            //alert('parse error');return;
            json = eval(jsonStr);
        }

        return json;
    }

    // 添加记录
    ,add : function(title, link, other) {
        var jsonStr = this.getCookie(this.key);
        //alert(jsonStr); return;

        if("" != jsonStr) {
            this.jsonData = this.parse2Json(jsonStr);

            var firstItem = '';
            var otherItem = '';
            // 排重(重复放到第一个
            for(var x=0; x<this.jsonData.length; x++) {
                if(title == this.jsonData[x]['title']) {
                	firstItem = this.initRow(this.jsonData[x]['title'], this.jsonData[x]['link'], this.jsonData[x]['other']);
                }else{
                	otherItem += this.initRow(this.jsonData[x]['title'], this.jsonData[x]['link'], this.jsonData[x]['other']) + ','; 
                }
            }
            if(firstItem != ''){//重复了
                jsonStr = '[' + firstItem + ',' + otherItem;
            }else{//没重复的就添加
            	// 重新赋值 组装 json 字符串
            	jsonStr = '[' + this.initRow(title, link, other) + ',';
            	for(var i=0; i<this.jsonData.length; i++) {
            		if(undefined != this.jsonData[i]) {
            			jsonStr += this.initRow(this.jsonData[i]['title'], this.jsonData[i]['link'], this.jsonData[i]['other']) + ',';
            		} else {
            			break;
            		}
            	}            	
            }
            
            jsonStr = jsonStr.substring(0, jsonStr.lastIndexOf(','));
            jsonStr += ']';

        } else {
            jsonStr = '['+ this.initRow(title, link, other) +']';
        }

        //alert(jsonStr);
        this.jsonData = this.parse2Json(jsonStr);
        this.setCookie(this.key, jsonStr, this.cacheTime, {"path" : this.path});
    }
    ,deleteItem : function(title){
      var jsonStr = this.getCookie(this.key);

      if("" != jsonStr) {
          this.jsonData = this.parse2Json(jsonStr);
          this.jsonData = $.grep(this.jsonData,function(item,i){
            return $.trim(title) != $.trim(item['title']);
          });
      }
      this.setCookie(this.key, JSON.stringify(this.jsonData), this.cacheTime, {"path" : this.path});
    }
    // 得到记录
    ,getList : function() {
        // 有缓存直接返回
        if(null != this.jsonData) {
            return this.jsonData;  // Array
        } 
        // 没有缓存从 cookie 取
        var jsonStr = this.getCookie(this.key);
        if("" != jsonStr) {
            this.jsonData = this.parse2Json(jsonStr);
        }

        return this.jsonData;
    }
    // 清空历史
    ,clearHistory : function() {
        this.deleteCookie(this.key);
        this.jsonData = null;
    }
};