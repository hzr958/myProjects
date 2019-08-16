var NewsBase = NewsBase ? NewsBase : {}  ;

NewsBase.newsList = function (from) {
  $newsList = window.Mainlist({
        name : "dynnews",
        listurl : "/dynweb/news/ajaxnewslist",
        listdata : {
            from:from
        },
        listcallback : function(xhr) {
            NewsBase.orderCallBack();
            NewsBase.judgeUpMove();
        },
        //statsurl : "/pub/query/ajaxpsnpubcount",
    });
};


NewsBase.edit = function(des3NewsId,origin){
    var forwardUrl = "/scmmanagement/news/edit?des3NewsId=" +des3NewsId+"&origin="+origin;
    BaseUtils.checkCurrentSysTimeout("/dynweb/ajaxtimeout", function(){
        window.location.href=forwardUrl;
    }, 0);
}
NewsBase.viewDetail = function(des3NewsId) {
    window.location.href=("/dynweb/news/details?des3NewsId=" + encodeURIComponent(des3NewsId));
}
NewsBase.add = function(){
    var forwardUrl = "/scmmanagement/news/edit";
    BaseUtils.checkCurrentSysTimeout("/dynweb/ajaxtimeout", function(){
        window.location.href=forwardUrl;
    }, 0);
}
NewsBase.checkData = function () {
    var title = $("#news_title").val();
    var content = editor.getData();
    //content = BaseUtils.htmlEscape(content);
    $("#news_content").val(content);
    if($.trim(title) == ""){
        scmpublictoast(newsi18n.i18n_title_not_blank, 1500);
        return false;
    }
    if($.trim(content) == ""){
        scmpublictoast(newsi18n.i18n_content_not_blank, 1500);
        return false;
    }
    return true ;
}
NewsBase.save = function(obj,publish) {
    $("#news_save_id").attr("publish",true);
    if(publish == "" || publish == "false"){
        $("#news_save_id").attr("publish",false);
        NewsBase.beforesave(obj);
    }else{
        //再次修改保存时会直接提示  修改的内容将直接发布显示出来，请确认是否保存
        smate.showTips._showNewTips(newsi18n.i18n_save_tip1,newsi18n.i18n_delete_title,
            "NewsBase.beforesave()", "", newsi18n.i18n_choose_confirm_btn, newsi18n.i18n_choose_cancel_btn);
    }
};
NewsBase.beforesave = function(obj){
    var b = NewsBase.checkData();
    if(b){
        if(obj){
            BaseUtils.doHitMore(obj,5000);
        }
        BaseUtils.checkCurrentSysTimeout("/dynweb/ajaxtimeout", NewsBase.saveNews, 0);
    }
}
NewsBase.saveNews = function(publish) {
    var isPublish = $("#news_save_id").attr("publish") ;
    var data = {
        "newsShowInfo.id" :$.trim($("#news_id").val()),
        "newsShowInfo.title" :$.trim($("#news_title").val()),
        "newsShowInfo.content" :$.trim($("#news_content").val()),
        "newsShowInfo.brief" :$.trim($("#news_brief").val()),
        "newsShowInfo.image" :$.trim($("#news_image").val()),
        "newsShowInfo.publish" : isPublish ,
    }
    $.ajax({
        type: "POST",// 方法类型
        dataType: "json",// 预期服务器返回的数据类型
        url: "/scmmanagement/news/save" ,// url
        data: data,
        success: function (data) {
            if (data.result == "success") {
                if(isPublish == "true"){
                    scmpublictoast(newsi18n.i18n_publish_sucess, 2000);
                }else{
                    scmpublictoast(newsi18n.i18n_save_sucess, 2000);
                }

                var pluginclose = document.getElementsByClassName("new-searchplugin_container-close")[0];
                if(pluginclose){
                    if($(".new-searchplugin_container")){
                        $(".background-cover").remove();
                        $(".new-searchplugin_container").remove();
                     }
                }
                setTimeout(function () {
                   window.location.href = "/scmmanagement/news/edit?des3NewsId="+data.des3NewsId;
                },2000);

            }else{
                scmpublictoast("保存失败", 2000);
            }
        },
        error : function() {
            alert(ProjectEnter.error);
        }
    });
};

NewsBase.newsPublish = function(des3NewsIds) {
    smate.showTips._showNewTips(newsi18n.i18n_publish_news_is, newsi18n.i18n_delete_title, "NewsBase.doPublish('" + des3NewsIds + "')", "", newsi18n.i18n_choose_confirm_btn, newsi18n.i18n_choose_cancel_btn);
};

NewsBase.doPublish = function(des3NewsId) {
    // 发布时，保存按钮禁止 点击
    BaseUtils.doHitMore($("#news_save_id")[0],5000);
    if(des3NewsId == ""){
        $("#news_save_id").attr("publish",true) ;
        NewsBase.saveNews(true);
        return ;
    }
    var post_data = {
        "des3NewsId" : des3NewsId
    };
    $.ajax({
        url : '/scmmanagement/news/publish',
        type : 'post',
        dataType : 'json',
        data : post_data,
        success : function(data) {
            if (data.result == "success") {
                $("#news_publish").addClass("Global-prohibit_click");
                var pluginclose = document.getElementsByClassName("new-searchplugin_container-close")[0];
                if(pluginclose){
                    if($(".new-searchplugin_container")){
                        $(".background-cover").remove();
                        $(".new-searchplugin_container").remove();
                     }
                }
                $("#news_save_id").attr("onclick","NewsBase.save(this,'true');");
                scmpublictoast(newsi18n.i18n_publish_sucess, 1000);
            } else if (data.result == "exist") {
                scmpublictoast("新闻不存在", 1000);
            } else {
                scmpublictoast("操作失败", 1000);
            }
        },
    });
}

NewsBase.backNewsList = function (origin) {
    if(origin != undefined && origin=="edit"){
        window.location.href = "/dynweb/news/details?des3NewsId="+$("#des3_news_id").val();
    }else{
        window.location.href = "/scmmanagement/news/main";
    }
};
NewsBase.preview = function () {
    editor.execCommand('preview');
}
/**
 * 初始化编辑器
 */
var  editor ;
NewsBase.initEdit = function () {

    var data = {
        fileDealType: "newsfile"
    };
    editor  =   CKEDITOR.replace( 'news_content',{
        //extraPlugins: '',
        extraAllowedContent: 'h3{clear};h2{line-height};h2 h3{margin-left,margin-top}',

        // Configure your file manager integration. This example uses CKFinder 3 for PHP.
        filebrowserBrowseUrl: '/psnweb/myfile/filemain?model=myFile',
        filebrowserImageBrowseUrl: '/psnweb/myfile/filemain?model=myFile',
        filebrowserUploadUrl: '/fileweb/fileupload',
        filebrowserImageUploadUrl: '/fileweb/fileupload',

        // Upload dropped or pasted images to the CKFinder connector (note that the response type is set to JSON).
        uploadUrl: '/fileweb/fileupload',

        // Reduce the list of block elements listed in the Format drop-down to the most commonly used.
        format_tags: 'p;h1;h2;h3;pre',
        // Simplify the Image and Link dialog windows. The "Advanced" tab is not needed in most cases.
        removeDialogTabs: 'image:advanced;link:advanced',

        height: 450
    });
    editor.config.height = 452;
    editor.on( 'fileUploadRequest', function( evt ) {
        var fileLoader = evt.data.fileLoader,
            formData = new FormData(),
            xhr = fileLoader.xhr;

        xhr.open( 'PUT', fileLoader.uploadUrl, true );
        formData.append( 'filedata', fileLoader.file, fileLoader.fileName );
        for (var key2 in data) {
            formData.append(key2, data[key2]);
        }
        fileLoader.xhr.send( formData );

        // Prevented the default behavior.
        evt.stop();
    }, null, null, 4 ); // Listener with a priority 4 will be executed before priority 5.

    editor.on( 'fileUploadResponse', function( evt ) {
        // Prevent the default response handler.
        evt.stop();

        // Get XHR and response.
        var data = evt.data,
            xhr = data.fileLoader.xhr,
            response = xhr.responseText.split( '|' );

        const result = eval('('+xhr.responseText+')');
        if (result.successFiles >=1 ) {
            var extendFileInfo = result.extendFileInfo[0];
            var downloadUrl = extendFileInfo.fileUrl;
            data.url =downloadUrl;
        }else{
            // An error occurred during upload.
            data.message = "上传文件异常";
            evt.cancel();
        }
    } );
}
/**
 * 排序回调
 */
NewsBase.orderCallBack = function () {
    var $check = $(".filter-list__section").find(".filter-value__list").find("input:checked");
    if($check){
        var html = $check.closest(".filter-value__item").find(".filter-value__option").html();
        $(".filter-section__title").html(html);
    }
}

/**
 * 新闻 赞/取消赞
 */
NewsBase.newsAward = function(des3NewsId, obj) {
  var post_data = {
    "des3NewsId" : des3NewsId
  };
  var isAward = $(obj).attr("isAward");
  if (isAward == '1') {
    $(obj).attr("isAward", 0);
    post_data.operate = '0';// 取消赞
  } else {
    $(obj).attr("isAward", 1);
    post_data.operate = '1';// 点赞
  }
  if (!$(obj).hasClass('new-Standard_Function-bar_item')) {
    obj = $(obj).find('.new-Standard_Function-bar_item:first');
  }
  $(obj).attr("disabled", "true");
  var click = $(obj).attr("onclick");
  $(obj).attr("onclick", "");
  var awardText = $(obj).text();
  var awardCount = 0;
  $.ajax({
    url : '/dynweb/news/ajaxupdatenewsaward',
    type : 'post',
    dataType : 'json',
    data : post_data,
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data.result == "success") {
          awardCount = data.awardTimes;
          if (isAward == 1) {// 由取消赞变成赞
            if (awardCount == 0) {
              $(obj).find(".new-Standard_Function-bar_item-title").text(newsi18n.i18n_like);
            } else {
              if (awardCount >= 1000) {
                $(obj).find(".new-Standard_Function-bar_item-title").text(newsi18n.i18n_like + "(1K+)");
              } else {
                $(obj).find(".new-Standard_Function-bar_item-title").text(newsi18n.i18n_like + "(" + awardCount + ")");
              }
            }
            if ($(obj).hasClass("new-Standard_Function-bar_item")) {
              $(obj).removeClass("new-Standard_Function-bar_selected");
            }
          } else {// 由赞变成取消赞
            if (awardCount >= 1000) {
              $(obj).find(".new-Standard_Function-bar_item-title").text(newsi18n.i18n_unlike + "(1K+)");
            } else {
              $(obj).find(".new-Standard_Function-bar_item-title").text(newsi18n.i18n_unlike + "(" + awardCount + ")");
            }
             if ($(obj).hasClass("new-Standard_Function-bar_item")) {
               $(obj).addClass("new-Standard_Function-bar_selected");
             }
          }
          $(obj).removeAttr("disabled");
          $(obj).attr("onclick", click);
        }
      })
    }
  });
};

NewsBase.newsDel = function(des3NewsIds,from) {
  smate.showTips._showNewTips(newsi18n.i18n_delete_content, newsi18n.i18n_delete_title, "NewsBase.doDel('" + des3NewsIds + "','"
      + from + "')" ,"", newsi18n.i18n_choose_confirm_btn, newsi18n.i18n_choose_cancel_btn);
};

NewsBase.doDel = function(des3NewsIds,from) {
  var post_data = {
    "des3NewsIds" : des3NewsIds
  };
  $.ajax({
    url : '/scmmanagement/news/ajaxdeletenews',
    type : 'post',
    dataType : 'json',
    data : post_data,
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        if (data.result == "success") {
            scmpublictoast("删除操作成功", 1000);
            if("edit"==from){
              NewsBase.backNewsList();
            }else{
              $newsList.reloadCurrentPage();
            }
            var pluginclose = document.getElementsByClassName("new-searchplugin_container-close")[0];
            if($(".new-searchplugin_container")){
                $(".background-cover").remove();
                $(".new-searchplugin_container").remove();
             }
        } else if (data.result == "exist") {
          scmpublictoast("新闻不存在", 1000);
        } else {
          scmpublictoast("操作失败", 1000);
        }
      })
    },
  });
}
/**
 * 下移
 */
NewsBase.downMoveNews = function(obj){
  var $curItem = $(obj).closest(".new-newshow_container-item");
  var $nextItem = $curItem.next(".new-newshow_container-item");
  var $curSeqNo = $curItem.attr("seqNo");
  var $nextSeqNo = $nextItem.attr("seqNo");
  var $curDes3newsId = $curItem.attr("des3newsId");
  var $nextDes3newsId = $nextItem.attr("des3newsId");
  var $_ul = $("div[list-main='dynnews']");
  $_ul.find("[des3newsid='" + $curDes3newsId + "']").attr("seqNo", $nextSeqNo);
  $_ul.find("[des3newsid='" + $nextDes3newsId + "']").attr("seqNo", $curSeqNo);
  var $nowItem1 = $curItem.clone(true);
  var $nextItem1 = $nextItem.clone(true);
  NewsBase.changeNewsSeqNo($curDes3newsId,$nextDes3newsId);
  $nextItem.replaceWith($nowItem1);
  $curItem.replaceWith($nextItem1);
  NewsBase.addNoneClass();
}
/**
 * 上移
 */
NewsBase.upMoveNews = function(obj){
  var $curItem = $(obj).closest(".new-newshow_container-item");
  var $prevItem = $curItem.prev(".new-newshow_container-item");
  var $curSeqNo = $curItem.attr("seqNo");
  var $prevSeqNo = $prevItem.attr("seqNo");
  var $curDes3newsId = $curItem.attr("des3newsId");
  var $prevDes3newsId = $prevItem.attr("des3newsId");
  var $_ul = $("div[list-main='dynnews']");
  $_ul.find("[des3newsid='" + $curDes3newsId + "']").attr("seqNo", $prevSeqNo);
  $_ul.find("[des3newsid='" + $prevDes3newsId + "']").attr("seqNo", $curSeqNo);
  var $nowItem1 = $curItem.clone(true);
  var $prevItem1 = $prevItem.clone(true);
  NewsBase.changeNewsSeqNo($curDes3newsId,$prevDes3newsId);
  $prevItem.replaceWith($nowItem1);
  $curItem.replaceWith($prevItem1);
  NewsBase.addNoneClass();
}

NewsBase.changeNewsSeqNo = function($curDes3newsId,$nextDes3newsId){
  var post_data = {
      "des3NewsId" : $curDes3newsId,
      "nextDes3NewsId":$nextDes3newsId
    };
    $.ajax({
      url : '/scmmanagement/news/ajaxchangeseqno',
      type : 'post',
      dataType : 'json',
      data : post_data,
      success : function(data) {
          if (data.result == "success") {
          } else {
          }
      },
    });
}

//列表第一条记录上移置灰，最后一条记录下移置灰
NewsBase.judgeUpMove = function(){
  if(document.getElementsByClassName("selected-item_up")&&document.getElementsByClassName("selected-item_up")){
    var length = document.getElementsByClassName("selected-item_down").length;
    if(document.getElementsByClassName("selected-item_up")[0]){
        document.getElementsByClassName("selected-item_up")[0].classList.add("Global-prohibit_click-up");          
    }
    if(document.getElementsByClassName("selected-item_down")[length-1]){
        document.getElementsByClassName("selected-item_down")[length-1].classList.add("Global-prohibit_click-down");           
    }
  }
}

NewsBase.addNoneClass = function() {
  if (document.getElementsByClassName("Global-prohibit_click-up").length > 0) {
    document.getElementsByClassName("Global-prohibit_click-up")[0].classList
        .remove("Global-prohibit_click-up");
  }
  if (document.getElementsByClassName("Global-prohibit_click-down").length > 0) {
    document.getElementsByClassName("Global-prohibit_click-down")[0].classList
        .remove("Global-prohibit_click-down");
  }
  if (document.getElementsByClassName("selected-item_down")
      && document.getElementsByClassName("selected-item_up")) {
    var length = document.getElementsByClassName("selected-item_down").length;
    if (document.getElementsByClassName("selected-item_up")[0]) {
      document.getElementsByClassName("selected-item_up")[0].classList
          .add("Global-prohibit_click-up");
    }
    if (document.getElementsByClassName("selected-item_down")[length - 1]) {
      document.getElementsByClassName("selected-item_down")[length - 1].classList
          .add("Global-prohibit_click-down");
    }
  }
};

NewsBase.outsideTimeOut = function(){
  $.ajax({
    url : "/psnweb/timeout/ajaxtest",
    type : "post",
    dataType : "json",
    success : function(data) {
      BaseUtils.ajaxTimeOut(data, function() {
        document.location.href =window.location.href;
      });
    }
  });
}
