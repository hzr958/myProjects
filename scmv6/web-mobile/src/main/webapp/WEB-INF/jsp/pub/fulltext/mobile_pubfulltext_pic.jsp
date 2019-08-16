<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
<title>科研之友</title>
<link rel="stylesheet" type="text/css" href="/resmod/mobile/css/mobile.css" />
<script type="text/javascript" src="${resmod }/js/jquery.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/msgbox/mobile.msg.js"></script>
<script src="https://hammerjs.github.io/dist/hammer.min.js"></script>
             
</head>
<body>
  <div class="new-full_text-container">
    <div class="new-full_text-container_header" style="height: 80px!important;">
      <i onclick="Msg.openPubfulltextDetail('${model.detailPageNo}','${model.toBack}');" style="font-size: 60px!important;"
        class="material-icons new-full_text-container_header-left">keyboard_arrow_left</i> <span
        class="new-full_text-container_header-middle"></span> <i
        class="material-icons new-full_text-container_header-right">keyboard_arrow_right</i>
    </div>
    
    
    <div class="new-full_text-container_picture">
    <div id="myElement" style="width: 90vw!important;">       
      <img id="img" src="${model.srcFullTextImagePath}" onerror="javascript:this.src='${model.fullTextImagePath}'">
    </div>
    </div>
  </div>
  <script>
  var hammertime = Hammer(document.getElementById('myElement'),{
    transform_always_block:true,
    transform_min_scale:0.5,
    drag_block_horizontal:true,
    drag_block_vertical:true,
    drag_min_distance:0
  });


   var img = document.getElementById('img');
  //初始值
  var posX = 0,
    posY = 0,
    last_posX = 0,
    last_posY = 0,
    scale = 1,
    last_scale = 1,
    rotation = 0,
    last_rotation = 0;
 // hammertime.get("panmove").set({ enable: true });
  hammertime.get("pinch").set({ enable: true });
  hammertime .get("rotate").set({ enable: true });
  
  hammertime.on('tap pinch panmove rotate ',function(e){
    switch(e.type){
        //当tap开始时，记录下当前的缩放量，旋转量和位移量
        case 'tap':
            last_scale = e.scale;
            last_rotation = e.rotation;
            last_posX = posX;
            last_posY = posY;
            break;
        case 'pinch':
            scale = Math.min(last_scale* e.scale,10);
            break;
        //拖拽时改变位移量
        case 'panmove':
            posX = last_posX + e.deltaX;
            posY = last_posY + e.deltaY;
            break;
        case 'rotate':
            rotation = last_rotation + e.rotation;           
            break;
    }
    if(e.type != "tap"){
      //使用CSS3 transform进行图片的变换
      var transform = "translate3d("+posX+"px,"+posY+"px,0)"+"scale("+scale+","+scale+")";//+"rotate("+rotation+"deg)";
      img.style.webkitTransform = transform;
    }
  }) 

  </script>
</body>
</html>