var FindPub = FindPub ? FindPub : {}
FindPub.showLeftConditions = function(){
  $.ajax({
    url:"/pub/ajaxfindpubconditions",
    data:{},
    type:"post",
    dataType:"html",
    success:function(data){
      $("#left_condition").html(data);
      initLeftCondition();
      FindPub.ajaxLoadePubList();
    },
    error:function(){
      
    }
  });
};

FindPub.ajaxLoadePubList = function(){
  var selectCondition = FindPub.getSelectCondition();
  window.Mainlist({
    name : "pub_list",
    listurl : "/pub/ajaxfindpublist",
    listdata : selectCondition,
    method : "pagination",
    listcallback : function(xhr) {

    }
  });
};


FindPub.getSelectCondition = function(){
  var selectCondition = {};
  selectCondition["searchKey"] = FindPub.getSearchKey();
  
  selectCondition["searchArea"] = FindPub.getSelectValue("searchArea");
  selectCondition["publishYear"] = FindPub.getSelectValue("publishYear");
  selectCondition["includeType"] = FindPub.getSelectValue("includeType");
  selectCondition["searchPubType"] = FindPub.getSelectValue("searchPubType");
/*  selectCondition["searchLanguage"] = FindPub.getSelectValue("searchLanguage");*/
  selectCondition["orderBy"] = $("#findPubOrderBy").attr("value");
  return selectCondition;
};
FindPub.getSearchKey = function(){
  var value = $.trim($("input[name='findPubSearchKey']").val());
  value = value.replace(/<[^>]*>|<\/[^>]*>/gm,"");
  $("input[name='findPubSearchKey']").val(value);
  return value;
};
FindPub.getSelectValue = function(name){
  var value = [];
  var parent=[];
  $("div[name='"+name+"']").find(".new-mainpage_left-sublist_body-item_selected").each(function(){
    if ($(this).attr("value")!="0") {//不限的不传
       value.push($(this).attr("value"));
      /* if(name=="searchArea"){
         var parentId = $(this).closest(".dev_area_parent").attr("value");
         var isRepeat = false;
         $.each(parent,function(index,value){
           if(parentId==value){
             isRepeat=true;
           }
         });
         if(!isRepeat){
           value.push(parentId);
           parent.push(parentId);
         }
       }*/
    }
  });
  return value.join();
};

//成果详情,点击分享，把成果信息加载到分享界面
FindPub.getPubDetailsSareParam = function(obj) {
  // 初始化推荐联系人
  var des3ResId = $(obj).attr("des3ResId");
  if (des3ResId == null) {
    des3ResId = $(obj).attr("resId");
  }
  var pubId = $(obj).attr("pubId");
  var databaseType = $(obj).attr("databaseType");
  var dbId = $(obj).attr("dbId");
  var resType = $(obj).attr("resType");
  $("#share_to_scm_box").attr("dyntype", "GRP_SHAREPUB").attr("des3ResId", des3ResId).attr("pubId", pubId).attr(
      "resType", resType).attr("dbId", dbId);
  var content = $(obj).prev().val();
  var $parent = $("#share_to_scm_box").find(".dev_dialogs_share_file_module  .share-attachmemts__list");
  $parent.html("");
  SmateShare.createFileDiv($parent, content, des3ResId);
};