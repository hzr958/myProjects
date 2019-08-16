/**
 * @author gzl 提示框方法
 */

scmpublictoast = function(text, time) {
  var toast_container = document.getElementsByClassName("toast_container")[0];
  if (toast_container == null) {
    var div = document.createElement("div");
    div.className = "toast_container";
    document.getElementsByTagName("body")[0].appendChild(div);
  }

  if (document.getElementsByClassName("toast_section").length == 0) {
    var dav = document.createElement("div");
    dav.className = "toast_section";
    document.getElementsByClassName("toast_container")[0].appendChild(dav);
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