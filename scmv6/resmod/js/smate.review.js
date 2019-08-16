/**
 * 新成果系统的赞操作
 * 
 * @author lxz
 */

var Review = Review ? Review: {};

// 初始化赞操作
Review.initReview = function() {
	var pubsStr = "";
	$(".comment_init").each(function(){
		pubsStr+=$(this).attr("pubId")+",";
	});
	$.ajax( {
		type : "post",
		url : "/pubweb/ajaxInitReview",
		data : {
			"pubsStr" : pubsStr
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "success") {
				$(".comment_init").each(function(){
					var num = data.result[$(this).attr("pubId")];
					if(num!=null){
						$(this).append("<span id='comment_count_${pubId}' class='center'>("+data.result[$(this).attr("pubId")]+")</span>")
					}
				});
			}
		},
		error : function(e) {
		}
	});
	
};


