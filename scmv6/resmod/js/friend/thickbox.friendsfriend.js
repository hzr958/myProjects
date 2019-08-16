var friendsfriend ={};

function add_friendReq_callBack(result,psnId){
	if(result){
		if(typeof locale!='undefined' && "en_US"==locale){
			$.smate.scmtips.show('success',"Operated successfully");
		}else{
			$.smate.scmtips.show('success',"操作成功");
		}
	}else{
		if(typeof locale!='undefined' && "en_US"==locale){
			$.smate.scmtips.show('success',"Operated failed");
		}else{
			$.smate.scmtips.show('error',"操作失败");
		}
	}
}