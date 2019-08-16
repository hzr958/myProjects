(function($) {
	$.fn.extend({
		imgcut: function(options) {
			var defaults = {
					resmod:"/resmod",
					imgDefaultWidth:700,
					imgDefaultHeight:500,
					imgPath:""
			};
			options = $.extend({},defaults, {
				target : this
			}, options);
			return this.each(function() {
				new $.ImgCut(this, options);
			});
		}
	}),
	$.ImgCut = function(obj, options) {
		
		var TP_IMG_WIDHT = 0;
		var TP_IMG_HEIGHT = 0;
		var TP_Img_SRCWIDTH = 0;
		var TP_Img_SRCHEIGHT = 0;
		init();
		
		function init(){
			
			setImagewh();
			$("#pre_edit_avatars").attr('src',options.imgPath);
			$("#pre_view_avatars").attr('src',options.imgPath);
			$("#save_cut_btn").bind("click",saveCut);
		}
		
		function setImagewh(){
			var imgPreloader = new Image();
			imgPreloader.onload = function(){		
				imgPreloader.onload = null;
				// Resizing large images - orginal by Christian Montoya edited by me.
				var x = options.imgDefaultWidth ;
				var y = options.imgDefaultHeight;
				var imageWidth = imgPreloader.width;
				var imageHeight = imgPreloader.height;
				TP_Img_SRCWIDTH = imgPreloader.width;
				TP_Img_SRCHEIGHT = imgPreloader.height;
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
				TP_IMG_WIDHT = imageWidth;
				TP_IMG_HEIGHT = imageHeight;
				
				if(imageWidth < 650){
					parent.$.Thickbox.resetWidth(950-(650 - imageWidth),950);
					$("#img_cut_wrap_div").width(920-(650 - imageWidth));
				}
				if(imageHeight < 350){
					var tp_height = imageHeight;
					if(tp_height < 150){
						tp_height = 150;
					}
					parent.$.Thickbox.resetHeight(530-(350 - tp_height));
					$("#img_cut_wrap_div").height(500-(350 - tp_height));
				}
				$("#pre_edit_avatars").width(TP_IMG_WIDHT);
				$("#pre_edit_avatars").height(TP_IMG_HEIGHT);
				$("#pre_edit_avatars").show();
				$("#pre_view_avatars").show();
				bindEditAvatars();
			};
			imgPreloader.src = options.imgPath;
		}
		
		function bindEditAvatars(){

			var pox = getViewPosition();
			var default_x1 = pox[0];
			var default_y1 = pox[1];
			var default_x2 = pox[2];
			var default_y2 = pox[3];
			
			setTimeout(function(){
				var customAspectRatio = typeof(options.aspectRatio) != "undefined" ? options.aspectRatio : '1:1';
				
				imgAreaSelect = $('#pre_edit_avatars').imgAreaSelect({aspectRatio: customAspectRatio, handles: true,fadeSpeed: 200,instance: true, onSelectChange: preview,minWidth: 50, minHeight: 50});
				imgAreaSelect.setOptions({ show: true,  x1: default_x1, y1: default_y1, x2: default_x2, y2: default_y2 });
				doPreView(default_x1,default_y1,(default_x2),(default_y2));
			},500);
		}
		
		function getViewPosition(){
			var width = TP_IMG_WIDHT;
			var height = TP_IMG_HEIGHT;
			
			var customDragWidth =  (typeof(options.dragWidth) == "undefined" || isNaN(options.dragWidth) || options.dragWidth.length==0) ? 0 :parseInt(options.dragWidth);
			var customDragHeight =  (typeof(options.dragHeight) == "undefined" || isNaN(options.dragHeight) || options.dragHeight.length==0) ? 0 :parseInt(options.dragHeight);
			
			// 修正 有些图片无法初始化 截取大小问题。 TSZ_2014.11.04_NSCXZX-810
			var minwh = width;
			var mincwh = customDragWidth ;
			if(width>height){
				minwh = width > height ? height : width;
				mincwh = customDragWidth > customDragHeight ? customDragHeight : customDragWidth;
			}
			
			
			var pcx = width / 2;
			var pcy = height / 2;
			//自定义范围太小
			if(mincwh > minwh | pcx<options.cutwidth){
				var ratio = customDragWidth / customDragHeight;
				if(ratio > 0){
					return [0,0,(minwh - 1),(minwh - 1) / ratio];
				}else{
					return [0,0,(minwh - 1) * ratio,(minwh - 1)];
				}
			}
		
			
			var cutwidth = options.cutwidth;
			var cutheight = options.cutheight;
			if(typeof cutwidth == "undefined" || typeof cutheight == "undefined" || cutwidth.length==0 || cutheight==0 || isNaN(cutwidth) || isNaN(cutheight)){
				cutwidth = 60;
				cutheight = 60;
			}else{
				cutwidth = Math.round(parseInt(cutwidth)/2);
				cutheight = Math.round(parseInt(cutheight)/2);
			}
			
			var minctwh = cutwidth > cutheight ? cutheight : cutwidth;
			//范围太小
			if(mincwh > minwh){
				var ratio = cutwidth / cutheight;
				if(ratio > 0){
					return [0,0,(minwh - 1),(minwh - 1) / ratio];
				}else{
					return [0,0,(minwh - 1) * ratio,(minwh - 1)];
				}
			}
			
			if(mincwh > 0){
				//坐标是否超出访问
				var pox1 = pcx -customDragWidth;
				var poy1 = pcy - customDragHeight;
				var pox2 = pcx + customDragWidth;
				var poy3 = pcy + customDragHeight;
				return [pcx -cutwidth,pcy - cutheight,pcx + cutwidth,pcy + cutheight];
			}
			return [pcx -cutwidth,pcy - cutheight,pcx + cutwidth,pcy + cutheight];
		}
		
		function preview(img, selection) {
		    if (!selection.width || !selection.height)
		        return;
			var x1 = selection.x1;
			var x2 = selection.x2;
			var y1 = selection.y1;
			var y2 = selection.y2;
			doPreView(x1,y1,x2,y2);
		}
		
		function doPreView(x1,y1,x2,y2){
			var width = x2 - x1;
			var height = y2 - y1;
			
			var cutwidth = options.cutwidth;
			var cutheight = options.cutheight;
			if(typeof cutwidth == "undefined" || typeof cutheight == "undefined" || cutwidth.length==0 || cutheight==0 || isNaN(cutwidth) || isNaN(cutheight)){
				cutwidth = 120;
				cutheight = 120;
			}
			
			var scaleX = cutwidth/ width;
		    var scaleY = cutheight/ height;

		    //此处修改 显示图片的大小，注意 页面上修改 div的大小 是修改图片显示框的大小。 tsz
		    //需要按比例来控制。图片显示定义 WIDTH：200为基数，比例在此基数的基础上得出 tsz
		    $('#preview img').css({
		    	//图片大小
		        width: Math.round(scaleX * TP_IMG_WIDHT/(options.cutwidth/options.cutImgWidthBase)),
		        height: Math.round(scaleY * TP_IMG_HEIGHT/(options.cutheight/(options.cutImgWidthBase/(options.cutwidth/options.cutheight)))),
		        //位移量 
		        marginLeft: -Math.round(scaleX * x1/(options.cutwidth/options.cutImgWidthBase)),
		        marginTop: -Math.round(scaleY * y1/(options.cutheight/(options.cutImgWidthBase/(options.cutwidth/options.cutheight))))
		    });
		    x1 = Math.round(x1 * TP_Img_SRCWIDTH / TP_IMG_WIDHT);
		    x2 = Math.round(x2 * TP_Img_SRCHEIGHT / TP_IMG_HEIGHT);
		    y1 = Math.round(y1 * TP_Img_SRCWIDTH / TP_IMG_WIDHT);
		    y2 = Math.round(y2 * TP_Img_SRCWIDTH / TP_IMG_WIDHT);
		    $('#imgx').val(x1);
		    $('#imgy').val(y1);
		    $('#imgw').val(x2 - x1);
		    $('#imgh').val(y2 - y1); 
		}
		
		function saveCut(){
			$("#save_cut_btn").attr("disabled","disabled");
			var imgx = $('#imgx').val();
		    var imgy = $('#imgy').val();
		    var imgw = $('#imgw').val();
		    var imgh = $('#imgh').val();
		    var markName=$("#markName").val();
		    var dataType=$("#dataType").val();
		    var post_data = {'dataType':dataType,'markName':markName,'imgx':imgx,'imgy':imgy,'imgw':imgw,'imgh':imgh};
			parent.$.smate.imgCutUpload.saveCutResult(post_data);
		}
	};
})(jQuery);