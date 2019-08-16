function movedeletetarget(ogg) {
  var arrylist = document.getElementsByClassName(ogg);
  var rightval;
  for (var i = 0; i < arrylist.length; i++) {
    var Mouse = {
      x : 0,
      y : 0
    };
    var mouseX = "", mouseY = "";
    arrylist[i].addEventListener('touchstart', function(e) {
      var mouse = e.targetTouches[0];
      Mouse.x = mouse.pageX;
      Mouse.y = mouse.pageY;
    }, false);
    arrylist[i].addEventListener('touchmove', function(e) {
      var mouse = e.targetTouches[0];
      var mouse = e.targetTouches[0];
      var mouseX = mouse.pageX;
      var mouseY = mouse.pageY;
      if (Math.abs(Math.abs(mouseY) - Math.abs(Mouse.y)) < Math.abs(Math.abs(mouseX) - Math.abs(Mouse.x))) {
        if ((mouseX - Mouse.x) > 0) {
          if (Math.abs(120 - mouseX + Mouse.x) < 50) {
            this.style.right = 0 + "px";
            if (this.querySelector(".mobile-remove_item-son")) {
              this.removeChild(this.querySelector(".mobile-remove_item-son"));
            }
          } else {
            this.style.right = 0 + "px";
          }
        } else if ((mouseX - Mouse.x) < 0) {
          if (!(this.querySelector(".mobile-remove_item-son"))) {
            var son = document.createElement("div");
            son.className = "mobile-remove_item-son";
            son.innerText = "删除";
            son.setAttribute("onclick", "newdelete_subitem(this)");
            this.appendChild(son);
          }
          /*if (Math.abs(mouseX - Mouse.x) > 100) {*/
          if (Math.abs(mouseX - Mouse.x) > 50) {
            /*this.style.right = 120 + "px";*/
              this.style.right = 60 + "px";
          } else if (Math.abs(mouseX - Mouse.x) < 30) {
              this.style.right = 0 + "px";
              this.removeChild(this.getElementsByClassName("mobile-remove_item-son")[0]);
          } else {
              this.style.right = Math.abs(mouseX - Mouse.x) + "px";
          }
        }
      }
    }, false);
    arrylist[i].addEventListener('touchend', function(e) {
      var mouseX = event.changedTouches[0].clientX;
      var mouseY = event.changedTouches[0].clientY;
      if (Math.abs(Math.abs(mouseX) - Math.abs(Mouse.x)) > Math.abs(Math.abs(mouseY) - Math.abs(Mouse.y))) {
        if ((mouseX - Mouse.x) > 0) {
          if (Math.abs(120 - mouseX + Mouse.x) < 50) {
            this.style.right = 0 + "px";
          } else {
            this.style.right = Math.abs(120 - mouseX + Mouse.x) + "px";
          }
        } else if ((mouseX - Mouse.x) < 0) {
          if (Math.abs(mouseX - Mouse.x) > 100) {
          } else {
            this.style.right = 0 + "px";
          }
        }
      }
    }, false);
  }
}

var newdelete_subitem = function(obj) {
  Smate.confirm("提示", "要删除该" + ($(obj).prev().attr("workId") != undefined ? "工作" : "教育") + "经历吗？", function() {
    var reqUrl;
    var reqData;
    if ($(obj).prev().attr("workId") != undefined) {
      reqUrl = "/psn/mobile/del/workhistory";
      reqData = {
        "workId" : $(obj).prev().attr("workId")
      };
    } else {
      reqUrl = " /psn/mobile/del/educatehistory";
      reqData = {
        "eduId" : $(obj).prev().attr("eduId")
      };
    }
    $.ajax({
      url : reqUrl,
      type : "post",
      dataType : "json",
      data : reqData,
      success : function(data) {
        if (data.result == "success") {
          $(obj).closest(".mobile-remove_item-target").remove();
        } else {
          scmpublictoast("操作失败, 请稍后再试", 1000);
        }
      }
    });
  }, ["确定", "取消"]);
}