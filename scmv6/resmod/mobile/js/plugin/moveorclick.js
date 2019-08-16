/**
 * 左滑、右滑、点击dom元素执行对应的方法
 * 支持pc和移动端
 * domClassName:需要滑动的dom元素的class属性名称
 * leastMoveX: 触发左滑或右滑需要执行方法的最小距离，不用加单位，直接给个数值就行 
 * needMove: 是否需要随着手指或鼠标的滑动，dom也跟着滑动
 * leftMoveCallBack：向左滑动一定距离执行方法
 * rightMoveCallBack：向右滑动一定距离执行方法
 * clickCallBack：单击或移动距离小于leastMoveX时执行的方法
 */
function slideDom(domClassName, leastMovePercent, needMove, leftMoveCallBack, rightMoveCallBack, clickCallBack){
	var selectClass = "." + domClassName;
    // 获取所有行，对每一行设置监听
    var lines = $(selectClass);
    var len = lines.length;
    var lastX, lastXForMobile, lastY;
    var pressedObj;  // 当前左滑的对象
    var start;       // 用于记录按下的点
    var moved;       //判断时候移动
    var leastMoveX = $(window).width() * leastMovePercent / 100;	//百分比转换成px
    var initialPositionX, finalPositionX
    // 网页在移动端运行时的监听
    for (var i = 0; i < len; ++i) {
    	lines[i].addEventListener('touchstart', function(e){
    		lastXForMobile = e.changedTouches[0].pageX;
    		pressedObj = this; // 记录被按下的对象
            // 记录开始按下时的点
    		initialPositionX = $(pressedObj).offset().left;
    		var touches = event.touches[0];
    		start = {
    			x: touches.clientX, // 横坐标
    			y: touches.clientY  // 纵坐标
    		};
    	});
    	lines[i].addEventListener('touchmove',function(e){
    		// 计算划动过程中x和y的变化量
    		var touchesN = event.touches[0];
    		delta = {
    			x: touchesN.clientX - start.x,
    			y: touchesN.clientY - start.y
    		};
            // 横向位移大于纵向位移，阻止纵向滚动
    		if (Math.abs(delta.x) > Math.abs(delta.y)) {	   
    			e.preventDefault();
    			if(needMove && Math.abs(delta.x) > 16){
    				//实时改变对象的位移实现拖动效果
    				this.style.webkitTransform = "translateX("+delta.x+"px)";
    			}
    		}
    	});
    	lines[i].addEventListener('touchend', function(e){
    		finalPositionX = $(pressedObj).offset().left;
    		var diffPositionX = initialPositionX - finalPositionX
    		if (Math.abs(diffPositionX) > 0){
    			var diffX = e.changedTouches[0].clientX - lastXForMobile;               
    			if (diffX < 0 - leastMoveX) {
    				//左滑到一定距离后执行的方法
    				leftMoveCallBack($(pressedObj), event);
    			} else if (diffX > leastMoveX) {
    				//右滑到一定距离后执行的方法
    				rightMoveCallBack($(pressedObj), event);
    			} else{
    				//单击或移动距离太小时执行的方法
    				clickCallBack($(pressedObj), event);
    			}
    		} else {
    			return false;
    		}
    	});
    }
}