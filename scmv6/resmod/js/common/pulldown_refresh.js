/**
 * 参数：obj，要滑动的对象；
 * offset，提示部分的transform的值（ 代码中是 transform:translate(0px,-61px) ，那么这里就是61 ）；
 * callback，回调函数，在下拉完成后调用的函数（ 页面刷新或数据加载 ）。
 * 使用例子：
 * Pulldown.slide(".main-list", 60, function (e) {
 *   setTimeout(function () {
 *      Group.myGroupList();
 *   }, 500);
 * });
 */

var Pulldown = Pulldown || {};
var refreshMsg;
Pulldown.slide = function (obj, offset, callback) {
 var start,
  end,
  isLock = false,//是否锁定整个操作
  isCanDo = false,//是否移动滑块
  isTouchPad = (/hp-tablet/gi).test(navigator.appVersion),
  hasTouch = 'ontouchstart' in window && !isTouchPad;
 //将对象转换为jquery的对象
 obj = $(obj);
 var objparent = obj.parent();
 /*操作方法*/
 var fn =
  {
   //移动容器
   translate: function (diff) {
    obj.css({
     "-webkit-transform": "translate(0," + diff + "px)",
     "transform": "translate(0," + diff + "px)"
    });
   },
   //设置效果时间
   setTranslition: function (time) {
    obj.css({
     "-webkit-transition": "all " + time + "s",
     "transition": "all " + time + "s"
    });
   },
   //返回到初始位置
   back: function () {
    fn.translate(1);
    //标识操作完成
    isLock = false;
   }
  };
 
 //滑动开始
 obj.bind("touchstart", function (e) {
  if (objparent.scrollTop() <= 0 && !isLock) {
   var even = typeof event == "undefined" ? e : event;
   //标识操作进行中
   isLock = true;
   isCanDo = true;
   //保存当前鼠标Y坐标
   start = hasTouch ? even.touches[0].pageY : even.pageY;
   //消除滑块动画时间
   fn.setTranslition(0);
  }
 });
 //滑动中
 obj.bind("touchmove", function (e) {
  if (objparent.scrollTop() <= 0 && isCanDo) {
   var even = typeof event == "undefined" ? e : event;
   //保存当前鼠标Y坐标
   end = hasTouch ? even.touches[0].pageY : even.pageY;
   if($("body").scrollTop()<=0){
     if (start < end) {
       //even.preventDefault();
       //消除滑块动画时间
       fn.setTranslition(0);
       //移动滑块
       fn.translate(end - start);
       if (end - start < offset) {//滑动距离小于指定值不刷新
         if (refreshMsg) {
           refreshMsg.text("下拉可以刷新");
           refreshMsg.show();
         }else{
           refreshMsg = $("<div id='refresh_msg'>下拉可以刷新</div>");
           obj.prepend(refreshMsg);
         }
       }else{
         if (refreshMsg) {
           refreshMsg.text("释放刷新");
           refreshMsg.show();
         }else{
           refreshMsg = $("<div id='refresh_msg'>释放刷新</div>");
           obj.prepend(refreshMsg);
         } 
       }
       $("#refresh_msg").show();
      }
   }
  }
 });
 //滑动结束
 obj.bind("touchend", function (e) {
  if (isCanDo) {
   isCanDo = false;
   //判断滑动距离是否大于等于指定值
   if (end - start >= offset) {
    //设置滑块回弹时间
    fn.setTranslition(0.3);
    setTimeout(function () {
      refreshMsg.hide();
    }, 100);   
    //保留提示部分
    fn.translate(0);
    //执行回调函数
    if (typeof callback == "function") {
     callback.call(fn, e);
    }
   } else {
    if (refreshMsg) {
       refreshMsg.hide();
    }
    //返回初始状态
    fn.back();
   }
  }
 });
}