/**
 * 人员检索.
 * 
 * @author xys
 * 
 */
var PsnSearch = PsnSearch ? PsnSearch : {};

PsnSearch.init = function(){
	
};

/**
 * 加载人员列表.
 */
PsnSearch.ajaxlist = function(data){
  $("#psnList").doLoadStateIco({
    style : "height:28px; width:28px; margin:auto;margin-top:24px;",
    status : 1
  });
	$.ajax( {
		url : '/psnweb/search/ajaxlist?locale='+locale,
		type : 'post',
		dataType:'html',
		data:data,
		success : function(data) {
		  if(data.indexOf('id="head_div"') == -1 ){
		    $("#psnList").html(data);
	      PsnSearch.ajaxOherInfo();
		  }else{
		    jQuery('body').html(data);
		  }
		},
		error: function (){
			alert(searchPsn.listError);
		}
	});
};
//加载头像以及人员统计信息
PsnSearch.ajaxOherInfo = function(){
	var psnList='';
	$("input[name='psnId']").each(function(){
		psnList+=','+$(this).val();
	});
	if(psnList.length>0){
	   	psnList=psnList.substring(1);
	}
	$.ajax( {
		url : '/psnweb/search/ajaxPsnOtherInfos',
		type : 'post',
		dataType:'json',
		data:{"des3PsnIds":psnList},
		success : function(data) {
			if(data.result=="success"){
		      var jsonData = data.rtnJson;
		      for(var i=0; i<jsonData.length; i++){
		    	 var url = jsonData[i].avatarUrl;
		    	 var hidex=jsonData[i].psnStatistics.hindex;
		    	 if(hidex!=0&&hidex!=null){
		    		hidex=searchPsn.hindex+hidex;
		    		$("#hidex_"+jsonData[i].psnId).html(hidex);
		    	 }
		    	 var obj = $(".func_container-psnlist[psnId='"+jsonData[i].psnStatistics.psnId+"']");
		    	 var pubSum=jsonData[i].psnStatistics.pubSum;
		    	 if(pubSum!=0&&pubSum!=null){
		    		 if(obj&&obj.find(".dev_pubsum")){
		    			 obj.find(".dev_pubsum").html(pubSum);
		    		 }
		    		 pubSum=searchPsn.pubSum+pubSum;
		    		 $("#pub_"+jsonData[i].psnId).html(pubSum);
		    	 }
		    	 var prjSum=jsonData[i].psnStatistics.prjSum;
		    	 if(prjSum!=0&&prjSum!=null){
		    		 if(obj&&obj.find(".dev_prjsum")){
		    			 obj.find(".dev_prjsum").html(prjSum);
		    		 }
		    	 }
		    	 var citedSum=jsonData[i].psnStatistics.citedSum;
		    	 if(citedSum!=0&&citedSum!=null){
		    		 citedSum=searchPsn.citedSum+citedSum;
		    		 $("#cite_"+jsonData[i].psnId).html(citedSum);
		    	 }
		    	 if(url!=null){
		    		 $("#avata_"+jsonData[i].psnId).attr("src",url);
		    	 }
		      }
			}
		},
		error: function (){
			alert(searchPsn.loadlistError);
		}
	});
}