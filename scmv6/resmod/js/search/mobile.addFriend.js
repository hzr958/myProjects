/**
 * 移动端-添加联系人js事件
 */
var AddFriend = AddFriend ? AddFriend : {};

//移动端-添加联系人
AddFriend.request = function(des3PsnId,psnId) {
	$.ajax({
		url: '/psnweb/friend/ajaxaddfriend',
		type:'post',
		data:{'des3Id':des3PsnId},
		dataType:'json',
		timeout:10000,
		success:function(data){
			if (data.result == "true") {
				$("#add_"+psnId).remove();
				scmpublictoast("操作成功",1000);
			} else {
				scmpublictoast("对方隐私设置为不接受联系人请求",1000);
			}
		},
		error:function(){
			scmpublictoast("操作失败",1000);
		}
	});
};