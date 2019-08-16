<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link href="/resmod/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="/resscmwebsns/js_v5/jquery.js"></script>
</head>
<script language="javascript" type="text/javascript">
  function c(objO) {
    var objTop = getOffsetTop(document.getElementById("d"));//对象x位置
    var objLeft = getOffsetLeft(document.getElementById("d"));//对象y位置

    var mouseX = event.clientX + document.body.scrollLeft;//鼠标x位置
    var mouseY = event.clientY + document.body.scrollTop;//鼠标y位置
    //计算点击的相对位置
    var objX = mouseX - objLeft;
    var objY = mouseY - objTop;
    clickObjPosition = objX + "," + objY;

    //alert(clickObjPosition);
    $.ajax({
      url : "/oauth/clickcheck/ajaxcode",
      type : "post",
      dataType : "json",
      data : {
        "clickx" : objX,
        "clicky" : objY
      },
      success : function(data) {
        if (data != null) {
          //alert("验证结果：" + data.result);
          checkDiv($(objO), data.result, mouseX, mouseY);
          setTimeout(function() {
            $("#show").hide();
            refresh();
          }, 1000);

        }
      },
      error : function() {
        alert("出错了");
      }
    });
  }

  function refresh() {
    $("#show").hide();
    $("#d").attr("src", "/oauth/clickcheck/img?A=" + Math.random());
  }

  function getOffsetTop(obj) {
    var tmp = obj.offsetTop;
    var val = obj.offsetParent;
    while (val != null) {
      tmp += val.offsetTop;
      val = val.offsetParent;
    }
    return tmp;
  }
  function getOffsetLeft(obj) {
    var tmp = obj.offsetLeft;
    var val = obj.offsetParent;
    while (val != null) {
      tmp += val.offsetLeft;
      val = val.offsetParent;
    }
    return tmp;
  }

  function checkDiv(obj, str, x, y) {
    //获取当前点击的位置
    var offset = obj.offset();

    //给要显示的DIV设置左边距
    var left = offset.left;
    var intleft = parseInt(left) + 15;

    //给要显示的DIV设置上边距
    var top = offset.top;
    var inttop = parseInt(top) + 15;
    //$("#show").text(str);
    if (str) {
      $("#show_img").attr("src", "/resmod/smate-pc/img/clickcheck/对号@30x30(2).png");
    } else {
      $("#show_img").attr("src", "/resmod/smate-pc/img/clickcheck/错号@30x30.png");
    }
    $("#show").show();
    $("#show").css({
      "position" : 'absolute',
      "z-index" : 99,
      "top" : y,
      "left" : x
    });

  }
</script>
<body>
  <div class="dialogs__box js_dialognoscroll"
    style="width: 350px; height: 320px; top: 300px; left: 700px; visibility: visible; opacity: 1;"
    dialog-id="homepage_security_setting" cover-event="hide" id="homepage_security_setting" visibility="hidden">
    <div class="dialogs__childbox_fixed">
      <div class="dialogs__header">
        <div class="dialogs__header_title">验证</div>
      </div>
    </div>
    <div class="dialogs__childbox_adapted">
      <div class="dialogs__content global__padding_24" style="width: 300px; height: 180px;">
        <a> <img id="d" alt="" src="/oauth/clickcheck/img" onclick="c(this)" style="cursor: hand">
        </a>
      </div>
    </div>
    <div class="dialogs__childbox_fixed">
      点击验证码验证(请选择小于5的数字) <a style="color: #186fbe" href="javascript:void()" onclick="refresh()">刷新</a>
    </div>
  </div>
  <div id="show" style="display: none;">
    <img id="show_img" style="width: 40px; height: 40px;">
  </div>
</body>
</html>