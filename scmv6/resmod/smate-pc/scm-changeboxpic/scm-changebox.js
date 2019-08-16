var publicPicclipupload = function(options){ 
		var elasticAction;
        var dragmoveAction;
        var presmoveAction;
        var img; //原图
		var dataImg;
        var dataURL;
        var sendimageurldata;
        var munsemoveImgAction;
        var imgmoveAction;
    
        
        
        var defaults = {
            parelement : "",
            sonelement : "parenodelement",
            maxwidth:700,
        	maxheight:500,
            filecallback : " ",
            scanwidth : 120,
            scanheight : 120,
            imgSrcWidth:"", //图片原始大小
            imgSrcHeight:"" 
        } 
        
        var opts = Object.assign(defaults,options);
        var filecallback = opts.filecallback || function () {};
	    if((!document.getElementById("newDiv"))&&(!document.getElementById("newscan"))){
            var structureContent = '<div class="cut_pic">'
            +'<div class="cut_pic-header">'+scmChangebox.title+'</div>'
            +'<div class="cut_pic-item">'
            +'<div class="cut_pic-container">'
            +'<div class="cut_pic-select">'
            +'<input id="fileinput" class="cut_pic-select-btn" type="file" unselectable="on" unselectable="true" style="outline: none;"/>'
            +'<div class="cut_pic-select_load" >'+scmChangebox.selectImg+'</div>'
            +'</div>'
            +'<div class="dragmove" style="overflow: hidden;">'
            +'<canvas class="cut_pic-show_box" id="myCanvas" style="position:absolute;display: flex;"></canvas>'
            +'<div class="cutpic-box">'
            +'<div class="cutpic-box_top">'
            +'<div class="cutpic-box_top-left"></div>'
            +'<div id="topcenetr" class="cutpic-box_top-center"></div>'
            +'<div class="cutpic-box_top-right"></div>'
            +'</div>'
            +'<div class="cutpic-box_center">'
            +'<div class="cutpic-box_center-left"></div>'
            +'<div class="cutpic-box_center-center"></div>'
            +'<div class="cutpic-box_center-right"></div>'
            +'</div>'
            +'<div class="cutpic-box-bottom">'
            +'<div class="cutpic-box_bottom-left"></div>'
            +'<div class="cutpic-box_bottom-center"></div>'
            +'<div class="cutpic-box_bottom-right"></div>'
            +'</div>'
            +'</div>'
            +'</div>'
            +'<div class="cutpic-box_footer-func">'
            +'<div class="cutpic-box_footer-func_list cutpic-box_footer-func_list-none" id="enlarge">'+scmChangebox.enlarge+'</div>'
            +'<div class="cutpic-box_footer-func_list cutpic-box_footer-func_list-none" id="narrow">'+scmChangebox.narrow+'</div>'
            +'</div>'
            +'</div>'
            +'<div class="preview_box">'
            +'<div class="preview_box-title">'+scmChangebox.preview+'</div>'
            +'<canvas id="pic" style="width: 100px; height: 100px;" class="preview_box-showpic" data-scanpicid="picwindow">'
            +'</div>'
            +'</div>'
            +'<div class="select-btn">'
            +'<div class="select-btn_upload">'+scmChangebox.cancel+'</div>'
            +'<div class="select-btn_giveup">'+scmChangebox.request+'</div>'
            +'</div>'
            +'</div>';
			
            var dtv = document.createElement("div");
            dtv.innerHTML=structureContent;
            dtv.className="cut_pic-box";
            dtv.setAttribute("id", "newDiv");
			dtv.setAttribute("data-cutpicid","parenodelement");
            opts.parelement.appendChild(dtv);

            const $cover = document.getElementsByClassName("target")[0];
            const $son = document.getElementsByClassName("cut_pic-box")[0];
            var $windowidth = $cover.clientWidth;
            var $windoheight = $cover.clientHeight;
            var $height = $son.clientHeight;
            var $width = $son.clientWidth;   
            const $setheight = ($windoheight - $height)/2 + "px"; 
            const $setwidth = ($windowidth - $width)/2 + "px";
            dtv.style.top = $setheight;
            $son.style.left = $setwidth; 
            dtv.style.top =  $setheight + "px";

         
            document.querySelectorAll('[data-scanpicid="picwindow"]')[0].style.width = opts.scanwidth + "px";
            document.querySelectorAll('[data-scanpicid="picwindow"]')[0].style.height = opts.scanheight + "px"; 
            var movebox = document.getElementsByClassName("cutpic-box")[0];          //移动拖拽剪切框    
            var parebox = document.getElementsByClassName("dragmove")[0];            //外框
            var cancload = document.getElementsByClassName("select-btn_upload")[0];
            var upload = document.getElementsByClassName("select-btn_giveup")[0];    //图片上传按钮
            var inputele = document.getElementsByClassName("cut_pic-select-btn")[0]; //图片选择按钮
            var canvas = document.getElementById("myCanvas");                        //图片预览框
            var canvasbox = document.getElementsByClassName("canvas_div")[0]; 
            var canvas2 = document.getElementsByClassName("preview_box-showpic")[0]; //要显示图片的目标元素
            var tarwidth = document.getElementById("topcenetr");
            var flexwidth = parseInt(window.getComputedStyle(tarwidth,null).getPropertyValue("flex-basis"));
            var enlarge=document.getElementById("enlarge");
            var narrow=document.getElementById("narrow");
            window.onresize = function(){
                var $windowidth = $cover.clientWidth;
                var $windoheight = $cover.clientHeight;
                var $height = $son.clientHeight;
                var $width = $son.clientWidth;
                const $setheight = ($windoheight - $height)/2 + "px"; 
                const $setwidth = ($windowidth - $width)/2 + "px";
                $son.style.top = $setheight;
                $son.style.left = $setwidth; 
               
            }
            var ctx = canvas.getContext("2d");
            inputele.addEventListener("change",function(){                 //图片选择按钮的值发生改变时
              //启用方法和缩小按钮
                $("#enlarge").removeClass("no_page-  for_selected");
                $("#narrow").removeClass("no_page-for_selected");
            	img = new Image();
                var file = this.files[0];                                  //获取文件
                console.log(file.size/1024/1024);
                if((file.size/1024/1024) > 20){   //将上传的图片的大小尺寸限制在20M内
                    scmpublictoast("图片尺寸过大，最大不超过20M",2000);
                    return;
                }
                if((file.type).indexOf("image/")==-1){ //判断文件是不是图片  
                	scmpublictoast(scmChangebox.noSelect,2000);  
                	return;
                }  
                var reader = new FileReader();
                document.getElementsByClassName("dragmove")[0].classList.add("new-uploading_tipshow");
                reader.onload = function(){
                    var picurl = reader.result;                            //获取文件的地址
                    img.src = picurl;
                    setTimeout(function(){  //延时 避免加载图片为空
                    //按一定的比例来显示图片
                    var imageWidth=img.width;
                    var imageHeight=img.height;
                    //如果图片小于截取框 按比例放大图片
                    if(imageWidth<opts.scanwidth){
                    	imageWidth=opts.scanwidth;
                    	imageHeight=Math.min(imageHeight*(imageWidth/img.width),opts.maxheight);
                    	
                    }else if(imageHeight<opts.scanheight){
                    	imageHeight=opts.scanheight;
                    	imageWidth=Math.min(imageWidth*(imageHeight/img.height),opts.maxwidth);
                    }
                    opts.imgSrcWidth=imageWidth;
                    opts.imgSrcHeight=imageHeight;
                    canvas.width = imageWidth;
                    canvas.height = imageHeight;
                    var x=defaults.maxwidth;
                    var y=defaults.maxheight;
                    //如果图片超过最大显示大小 按比例缩放
                    if (imageWidth > x) {
    					imageHeight = imageHeight * (x / imageWidth); 
    					imageWidth = x; 
    					if (imageHeight > y) { 
    						imageWidth = imageWidth * (y / imageHeight); 
    						imageHeight = y; 
    					}
    				} else if (imageHeight > y) { 
    					imageWidth = imageWidth * (y / imageHeight); 
    					imageHeight = y; 
    					if (imageWidth > x) { 
    						imageHeight = imageHeight * (x / imageWidth); 
    						imageWidth = x;
    					}
    				}
                   parebox.style.width = imageWidth + 2 +"px";
                   parebox.style.height = imageHeight + 2 + "px";
                   document.getElementsByClassName("dragmove")[0].classList.remove("new-uploading_tipshow");
                   ctx.drawImage(img,0,0,canvas.width,canvas.height); //将已选择的图片显示到剪辑框内                  
                   moveCutBox(opts.scanwidth,opts.scanheight,0,0); 
                   movebox.style.width = opts.scanwidth + "px";
                   movebox.style.height = opts.scanheight +"px";
                   cutpicShowinBox(0,0,opts.scanwidth,opts.scanheight);
                   },500);
				};
                reader.readAsDataURL(file);                                //将文件读取为DataURL
            });    
          
            var ctx2 = canvas2.getContext("2d");            
            var calculatePosition = function(x1, y1, x2, y2, dx, dy){              //x1,y1边框相对中心初始坐标，x2,y2鼠标相对中心初始坐标，dx,dy鼠标移动距离;
                function checkSameSign(a, b){                                     //判断鼠标落点的象限
                    if (a*b>0){
                        return 1;
                    } else {
                        return 0;}
                }
                function checkPointSign(a,b,flexwidth){                           //判断鼠标落点的具体位置
                    if(Math.abs(a) - Math.abs(b) <= flexwidth){
                        return 1;
                    }else{
                        return 0;}
                }
                const newX = Math.abs(x1)*(Math.abs(x1)/x1) + checkSameSign(x1,x2)*checkPointSign(x1,x2,flexwidth)*dx; 
                const newY = Math.abs(y1)*(Math.abs(y1)/y1) + checkSameSign(y1,y2)*checkPointSign(y1,y2,flexwidth)*dy;
                return [newX, newY];                                               //返回新的一个角相对中心的坐标
            };
	
           var cutpicShowinBox = function (Sx,Sy,Sw,Sh){                            // 将范围内的图片显示到预览框里面
        	   if((Sx>=0)&&(Sy>=0)&&(Sw>0)&&(Sh>0)){
        		    Sx=Sx-canvas.offsetLeft; //减去偏移量
        		    Sy=Sy-canvas.offsetTop; 
                    dataImg = ctx.getImageData(Sx,Sy,Sw,Sh);
                    canvas2.width = Sw;
                    canvas2.height = Sh;
                    ctx2.putImageData(dataImg, 0, 0);
                    dataURL = canvas2.toDataURL("image/png");
                    const $cover = document.getElementsByClassName("target")[0];
                    const $son = document.getElementsByClassName("cut_pic-box")[0];
                    var $windowidth = $cover.clientWidth;
                    var $windoheight = $cover.clientHeight;
                    var $height = $son.clientHeight;
                    var $width = $son.clientWidth;
                    const $setheight = ($windoheight - $height)/2 + "px"; 
                    const $setwidth = ($windowidth - $width)/2 + "px";
                    $son.style.top = $setheight;
                    $son.style.left = $setwidth; 
                }
            };
            
            var moveCutBox=function (w,h,t,l){  //移动剪切框
            	movebox.style.width = w;
                movebox.style.height = h; 
                movebox.style.top = t;
                movebox.style.left = l;
            }
            
            presmoveAction = function(e){
                var firstX = e.clientX,                                       /*获取鼠标点击时点击的位置*/
                    firstY = e.clientY;
                e.preventDefault();
                e.stopPropagation(); 
                const iPos = movebox.getBoundingClientRect();
                const oPos = parebox.getBoundingClientRect();
                const iCenter = [iPos.width/2 + iPos.left, iPos.height/2 + iPos.top];                     //Array, [x, y] 中心相对屏幕坐标
                const boxCorner = [ [iPos.left-iCenter[0], iPos.top-iCenter[1]],
                                    [iPos.left+iPos.width-iCenter[0], iPos.top-iCenter[1]],
                                    [iPos.left+iPos.width-iCenter[0], iPos.top+iPos.height-iCenter[1]],
                                    [iPos.left-iCenter[0], iPos.top+iPos.height-iCenter[1]]];            //[LT,RT,RB,LB]; 相对中心坐标；
                const iRelativeCenter = [iCenter[0]-oPos.left, iCenter[1]-oPos.top];                     //[x,y] 相对父元素的坐标   
                const newcoorder = [];
                boxCorner.forEach(function(x){
                    newcoorder.push([x[0] + iRelativeCenter[0] , x[1] + iRelativeCenter[1]]);
                });            
                if (firstX < iPos.left ||  firstX > iPos.left + iPos.width || firstY < iPos.top ||  firstY > iPos.top + iPos.height
                ) {
                    return;
                } else if (firstX > iPos.left+flexwidth && firstX < iPos.left + iPos.width-flexwidth && firstY > iPos.top+flexwidth &&  firstY < iPos.top + iPos.height-flexwidth) {
                        dragmoveAction = function(e){                                                   //当鼠标落在拖拽框的中央拖拽区域
                            const nowposition = [e.clientX , e.clientY];                                   //现在坐标
                            const moveDistance = [ nowposition[0] - firstX, nowposition[1] - firstY];      //鼠标移动的距离 
                            const newRelativeCorner = [];                                                  //新的四坐标     
                            newcoorder.forEach(function(x){
                                newRelativeCorner.push( [x[0]+ moveDistance[0], x[1]+moveDistance[1]]);
                            });
                            newRelativeCorner[0][0] = Math.min(Math.max(newRelativeCorner[0][0],0),oPos.width - iPos.width);   //规定边框四坐标运动范围
                            newRelativeCorner[0][1] = Math.min(Math.max(0,newRelativeCorner[0][1]),oPos.height - iPos.height);
                            
                            newRelativeCorner[1][0] = Math.min(Math.max(newRelativeCorner[1][0], oPos.width - iPos.width),oPos.width);
                            newRelativeCorner[1][1] = Math.min(Math.max(newRelativeCorner[1][1],oPos.height - iPos.height),0);

                            newRelativeCorner[2][0] = Math.min(Math.max(newRelativeCorner[2][0],oPos.width - iPos.width),oPos.width);
                            newRelativeCorner[2][1] = Math.min(Math.max(newRelativeCorner[2][1],oPos.height - iPos.height),oPos.height);
      
                            newRelativeCorner[3][0] = Math.min(Math.max(newRelativeCorner[3][0],0),oPos.width - iPos.width);
                            newRelativeCorner[3][1] = Math.min(Math.max(newRelativeCorner[3][1],oPos.height - iPos.height),oPos.height);
                            
                            if(iPos.width+newRelativeCorner[0][0]>oPos.width-2){
                            	newRelativeCorner[0][0]=oPos.width-2-iPos.width;
                            } //不能超过框
                            if(iPos.height+newRelativeCorner[0][1]>oPos.height-2){
                            	newRelativeCorner[0][1]=oPos.height-2-iPos.height;
                            }
                            moveCutBox(iPos.width  + "px",iPos.height + "px",newRelativeCorner[0][1] + "px",newRelativeCorner[0][0] + "px")
                            cutpicShowinBox(newRelativeCorner[0][0],newRelativeCorner[0][1],iPos.width,iPos.height);
                        };
                        parebox.addEventListener("mousemove",dragmoveAction,false);
                    } else {                                                                                  //当鼠标落在拖拽框变形区域
                        elasticAction = function(e){
                        
                            var scanwidth = opts.scanwidth;
                            var scanheight = opts.scanheight;
                            /**
                             * const iPos = movebox.getBoundingClientRect();
                             * SCM-14033 以及有时候裁剪时拉不动，以上代码解决这个问题
                             */
                            const iPos = movebox.getBoundingClientRect();   // 实时获得内框的属性
                            const cursorPosition = [e.clientX, e.clientY];                                //鼠标相对屏幕坐标
                            const deltaPostion = [cursorPosition[0]-firstX, cursorPosition[1]-firstY];    //鼠标移动的距离
                            const newBoxCorner = [];                                                      //新相对中心坐标
                            boxCorner.forEach(function(x){
                                newBoxCorner.push(calculatePosition(x[0],x[1],firstX-iCenter[0],firstY-iCenter[1],deltaPostion[0],deltaPostion[1]));
                            });
                            const newRelativeCorner = [];                                                 //新的四坐标
                            newBoxCorner.forEach(function(x){
                                newRelativeCorner.push( [x[0] + iRelativeCenter[0], x[1] + iRelativeCenter[1]]);
                            });                                                                       
                            newRelativeCorner[0][0] = Math.min(Math.max(newRelativeCorner[0][0],0),newcoorder[2][0] - scanwidth);  //规定边框四坐标运动范围
                            newRelativeCorner[0][1] = Math.min(Math.max(newRelativeCorner[0][1],0),newcoorder[2][1] - scanheight);

                            newRelativeCorner[1][0] = Math.min(Math.max(newRelativeCorner[1][0],newcoorder[3][0] + scanwidth),oPos.width);
                            newRelativeCorner[1][1] = Math.min(Math.max(newRelativeCorner[1][1],0),newcoorder[3][1] - scanheight);
 
                            newRelativeCorner[2][0] = Math.min(Math.max(newRelativeCorner[2][0],newcoorder[0][0] + scanwidth),oPos.width);
                            newRelativeCorner[2][1] = Math.min(Math.max(newRelativeCorner[2][1],newcoorder[0][1] + scanheight),oPos.height);

                            newRelativeCorner[3][0] = Math.min(Math.max(newRelativeCorner[3][0],0),newcoorder[1][0] - scanwidth);
                            newRelativeCorner[3][1] = Math.min(Math.max(newRelativeCorner[3][1],newcoorder[1][1] + scanheight),oPos.height);
                            var newwidth=newRelativeCorner[1][0] - newRelativeCorner[0][0];
                            var newheight=newRelativeCorner[3][1] - newRelativeCorner[0][1];                           
                            if(newwidth!=iPos.width  ){ //不能操作框
                            	newheight=newwidth;  //需要按比例来算高度
                            	if(newheight+iPos.top-oPos.top>oPos.height){
                            		return;
                            	}
                            }else if(newheight!=iPos.height){ //不能操作框
                            	newwidth=newheight;//需要按比例来算宽度
                            	if(newwidth+iPos.left-oPos.left>oPos.width){
                            		return;
                            	}
                            }
                            if(newwidth>oPos.width-2 | newheight>oPos.height-2){
                            	return;
                            }
                            moveCutBox(newwidth+ "px",newheight+ "px",newRelativeCorner[0][1]+ "px",newRelativeCorner[0][0] +"px");
                            cutpicShowinBox(newRelativeCorner[0][0],newRelativeCorner[0][1],newwidth,newheight);      //将剪切框内的图片显示到预览框内
                         }; 
                         parebox.addEventListener("mousemove",elasticAction,false);
					}           
			};
			//移动图片时间
			imgmoveAction = function(ev){
				  ev.preventDefault();
				  ev.stopPropagation();
				 const oPos = parebox.getBoundingClientRect();
				  const iPos = movebox.getBoundingClientRect();
				 var disX = ev.clientX - canvas.offsetLeft;  
		         var disY = ev.clientY - canvas.offsetTop; 
		         munsemoveImgAction=function(em){
		        	 
		        	 if(Math.abs(em.clientX -disX)+oPos.width<canvas.width && (em.clientX -disX)<0){
				        		 canvas.style.left = em.clientX -disX+"px";  
				     }
		        	 if(Math.abs(em.clientY -disY)+oPos.height<canvas.height&& (em.clientY -disY)<0){
			        	 canvas.style.top = em.clientY -disY+"px";
		        	 }
		        	 cutpicShowinBox(iPos.left-oPos.left,iPos.top-oPos.top,iPos.width,iPos.height);
		         }		         
		         canvas.addEventListener("mousemove",munsemoveImgAction,false);
		    };
		    canvas.addEventListener("mousedown",imgmoveAction,false);
		    parebox.addEventListener("mousedown",presmoveAction,false);
			document.addEventListener("mouseup",function(){                            //将mousemove事件解除绑定
				parebox.removeEventListener("mousemove", dragmoveAction, false);
				parebox.removeEventListener("mousemove", elasticAction, false);
				canvas.removeEventListener("mousemove",munsemoveImgAction, false);								 						 		 
		    });
			
			//放大图片事件  放大图片 不能超过原图大小 每次放大5%
			imgEnlargeAction = function(ev){
				 const oPos = parebox.getBoundingClientRect();
				 const iPos = movebox.getBoundingClientRect();
				 var imgSrcWidth=opts.imgSrcWidth;
				 var imgSrcHeight=opts.imgSrcHeight;
				 var imgWidth=canvas.width;
				 var imgHeight=canvas.height;								 
				 if(imgWidth>=imgSrcWidth){
				   $("#enlarge").addClass("no_page-for_selected");
					 return;
				 }
				 $("#narrow").removeClass("no_page-for_selected");
				 canvas.width = Math.min(imgWidth*(1+0.05),imgSrcWidth);
                 canvas.height = Math.min(imgHeight*(1+0.05),imgSrcHeight);
                
                 //重新设置图片偏移量
                 if(canvas.offsetLeft<0){
                	 if(canvas.offsetLeft-(imgWidth*0.05)>0){
                		 canvas.style.left=canvas.offsetLeft-(imgWidth*0.05)+"px"; 
                	 }else{
                		 canvas.style.left='0px';
                	 }
                 }
                 if(canvas.offsetTop<0){
                	 if(canvas.offsetTop-(imgHeight*0.05)>0){
                		 canvas.style.top =canvas.offsetTop-(imgHeight*0.05)+"px";
                	 }else{
                		 canvas.style.top='0px';
                	 }
                 }
                  
                  ctx.drawImage(img,0,0,canvas.width,canvas.height)
                 //重新设置图片预览内容
	        	 cutpicShowinBox(iPos.left-oPos.left,iPos.top-oPos.top,iPos.width,iPos.height);

			};
			//缩小图片事件 缩小图片 不能小于画布框显示大小 每次缩小5%
			imgNarrowAction = function(ev){
				 const oPos = parebox.getBoundingClientRect();
				 const iPos = movebox.getBoundingClientRect();
				 var imgSrcWidth=opts.imgSrcWidth;
				 var imgSrcHeight=opts.imgSrcHeight;
				 var imgWidth=canvas.width;
				 var imgHeight=canvas.height;

				 if(oPos.width>=imgWidth){
           $("#narrow").addClass("no_page-for_selected");
					 return;
				 }
				 $("#enlarge").removeClass("no_page-for_selected");
				 canvas.width = Math.max(imgWidth*(1-0.05),oPos.width);
                 canvas.height = Math.max(imgHeight*(1-0.05),oPos.height);
                 
               //重新设置图片偏移量
                 if(canvas.offsetLeft<0){
                	 if(canvas.offsetLeft+(imgWidth*0.05)<0){
                		 canvas.style.left=canvas.offsetLeft+(imgWidth*0.05)+"px";
                	 }else{
                		 canvas.style.left='0px';
                	 }
                 }
                 if(canvas.offsetTop<0){
                	 if(canvas.offsetTop+(imgHeight*0.05)<0){
                		 canvas.style.top =canvas.offsetTop+(imgHeight*0.05)+"px";
                	 }else{
                		 canvas.style.top='0px';
                	 }
                 }
                 try {
                   ctx.drawImage(img,0,0,canvas.width,canvas.height);
                 } catch (err) {
                 }
               //重新设置图片预览内容
	            cutpicShowinBox(iPos.left-oPos.left,iPos.top-oPos.top,iPos.width,iPos.height);

			};
			enlarge.addEventListener("click",imgEnlargeAction,false);
            narrow.addEventListener("click",imgNarrowAction,false);           

        sendimageurldata = upload.addEventListener("click",function(){             //将已剪切到预览框内的图片上传
            BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
                if(dataURL){                                                               //确保预览框内的图片不为空
                    //截图确认后的回调
                    //得到的图片 不是指定大小 而是裁剪框大小
                    var resultImg=new Image();
                    resultImg.src=dataURL;
                    setTimeout(function(){  //延时 避免加载图片为空
                        var resultCanvas = document.createElement("canvas");  
                        resultCanvas.width = opts.scanwidth;  
                        resultCanvas.height = opts.scanheight;  
                        // 坐标(0,0) 表示从此处开始绘制，相当于偏移。  
                        resultCanvas.getContext("2d").drawImage(resultImg, 1, 1,resultImg.width,resultImg.height,1,1,opts.scanwidth,opts.scanheight);  
                        filecallback(resultCanvas.toDataURL("image/png"));
                        cancload.click();
                    },100);
                    
                }else{
                    scmpublictoast(scmChangebox.noSelect,2000);  
                }
            }, 0);
            
        });
        cancload.onclick = function(){
            dtv.style.top = "1250px";
            setTimeout(function(){
               opts.parelement.style.display = "none"; 
               $cover.removeChild($son);
            },300);
        }
        window.oncontextmenu = function () {                                       //取消鼠标右键点击
           return false;};
        }
  
};
