/**
 * @author gzl 提示框方法
 */

scmpublictoast = function(text, time, type) {
  var sUserAgent = navigator.userAgent.toLowerCase();
  // TODO 有新的移动端系统可以接着往这添加
  var reg = /ios|symbianos|windows mobile|windows ce|ucweb|rv:1.2.3.4|midp|android|webos|iphone|ipad|ipod|blackberry|iemobile|opera mini/i;
  if (reg.test(sUserAgent)) {
    newMobileTip(text, type);
    return;
  }
  // /////下面是p端的提示
  var toast_container = document.getElementsByClassName("toast_container")[0];
  if (toast_container == null) {
    var div = document.createElement("div");
    div.className = "toast_container";
    document.getElementsByTagName("body")[0].appendChild(div);
    div.style.left = (window.innerWidth - div.offsetWidth) / 2 + "px";
  }

  if (document.getElementsByClassName("toast_section").length == 0) {
    var dav = document.createElement("div");
    dav.className = "toast_section";
    document.getElementsByClassName("toast_container")[0].appendChild(dav);
    document.getElementsByClassName("toast_container")[0].style.left = (window.innerWidth - 320) / 2 + "px";
    document.getElementsByClassName("toast_section")[0].innerHTML = text;
    var toast_message = $('.toast_section').last();
    var toast_height = toast_message.outerHeight();
    var toast_prev = toast_message.prevAll();
    toast_message.css("bottom", "-" + toast_height + "px");
    toast_prev.css("bottom", "-" + toast_height + "px");
    toast_message.animate({
      bottom : "0"
    }, 300);
    toast_prev.animate({
      bottom : "0"
    }, 300);

    setTimeout(function() {
      toast_message.animate({
        bottom : "-" + toast_height + "px"
      }, 300, function() {
        toast_message.remove();
      });
      toast_prev.animate({
        bottom : "-" + toast_height + "px"
      }, 300, function() {
        toast_prev.css("bottom", "0");
      });
    }, time);
  }
}

/**
 * 不同的datacode的值 会弹出不同的内容框 datacode = "1" 或者 datacode ="" 时会弹出操作成功的弹框 datacode = "2" 会弹出操作失败的弹框
 * datacode = "3" 或者其他的值 会弹出稍等的弹框 当你需要弹出不同内容的提示框的时候 你只需要把你想弹出内容传进来即可
 */
var newMobileTip = function(datacode, type) {
  var msg = datacode;
  var classname = 'new-mobile_tip-success';
  if (type) {// 设置了显示的图标
    if (type == "1") {
      classname = 'new-mobile_tip-success';
      if (!datacode) {
        msg = "操作成功";
      }
    }
    if (type == "2") {
      classname = 'new-mobile_tip-fail';
      if (!datacode) {
        msg = "操作失败";
      }
    }
    if (type == "3") {
      classname = 'new-mobile_tip-wait';
      if (!datacode) {
        msg = "请稍后";
      }
    }
  } else {// 没有设置图标的类型
    if (datacode.match(/失败|错误|不正确|error/ig)) {
      classname = 'new-mobile_tip-fail';
      if (!datacode) {
        msg = "操作失败";
      }
    } else if (datacode.match(/成功|success/ig)) {
      classname = 'new-mobile_tip-success';
      if (!datacode) {
        msg = "操作成功";
      }
    } else {
      classname = 'new-mobile_tip-wait';
      if (!datacode) {
        msg = "请稍后";
      }
    }
  }

  var toastcontent = '<i class="new-mobile_tip-container_img ' + classname
      + '"></i><span class="new-mobile_tip-container_title">' + msg + '</span>'

  var toastcontainer = document.createElement("div");
  toastcontainer.className = "new-mobile_tip-container_avator";
  toastcontainer.innerHTML = toastcontent;
  if (document.getElementsByClassName("new-mobile_tip-container").length > 0) {
    var parentbox = document.getElementsByClassName("new-mobile_tip-container")[0];
    parentbox.appendChild(toastcontainer);
    var tipelem = document.getElementsByClassName("new-mobile_tip-container_avator")[0];
    tipelem.style.right = (window.innerWidth - tipelem.offsetWidth) / 2 + "px";
    tipelem.style.bottom = (window.innerHeight - tipelem.offsetHeight) / 2 + "px";
    setTimeout(function() {
      tipelem.style.bottom = -600 + "px";
    }, 1500);
    setTimeout(function() {
      parentbox.removeChild(toastcontainer);
      if ($(".new-mobile_tip-container").length > 0) {
        document.body.removeChild(parentbox);
      }
    }, 2000);
  } else {
    var parentbox = document.createElement("div");
    parentbox.className = "new-mobile_tip-container";
    document.body.appendChild(parentbox);
    parentbox.appendChild(toastcontainer);
    var tipelem = document.getElementsByClassName("new-mobile_tip-container_avator")[0];
    tipelem.style.right = (window.innerWidth - tipelem.offsetWidth) / 2 + "px";
    tipelem.style.bottom = (window.innerHeight - tipelem.offsetHeight) / 2 + "px";
    setTimeout(function() {
      tipelem.style.bottom = -600 + "px";
    }, 2000);
    setTimeout(function() {
      parentbox.removeChild(toastcontainer);
      if ($(".new-mobile_tip-container").length > 0) {
        document.body.removeChild(parentbox);
      }
    }, 2000);
  }
}
