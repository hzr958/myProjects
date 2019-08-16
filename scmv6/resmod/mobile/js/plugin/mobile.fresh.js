/**
 * @author wsn
 * 移动端像ios按后退物理键页面不刷新的问题，这个方法放到需要刷新的页面上
 */
var Fresh = Fresh ? Fresh: {};
var EventUtil = {
    addHandler: function (element, type, handler) {
        if (element.addEventListener) {
            element.addEventListener(type, handler, false);
        } else if (element.attachEvent) {
            element.attachEvent("on" + type, handler);
        } else {
            element["on" + type] = handler;
        }
    }
};

Fresh.backFresh = function(){
	var showCount = 0;
    EventUtil.addHandler(window, "pageshow", function (event) {
    	if(showCount != 0){
    		window.location.reload(); 
    	}
    	showCount++;
    });
}