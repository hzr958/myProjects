<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript" charset="UTF-8"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/new-confirmbox/confirm.css">
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<link href="/resmod/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
<link href="/resmod/css/scmjscollection.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/scm-changeboxpic/scm-changebox.css">
<script type="text/javascript" src="${resmod}/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/news/news.base.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_chipbox.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/pub/plugin/smate.plugin.alerts.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/news/news_${locale}.js"></script>
<script src="${resmod}/js_v5/plugin/editor/ckeditor.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/scm-changeboxpic/scm-changebox_${ locale}.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/scm-changeboxpic/scm-changebox.js"></script>
<script type="text/javascript">
$(function(){

    NewsBase.initEdit();
    if($("#news_id").val() == ""){
        console.log("add");
        setInterval( function () {
            var content = editor.getData();
            $("#news_content").val(content);
            if($.trim($("#news_title").val() ) != "" &&  $.trim(content ) != ""){
                $("#news_publish").removeClass("Global-prohibit_click");
            }else {
                $("#news_publish").addClass("Global-prohibit_click");
            }
        },500);
    }
});


function cutImg(){
    var cutpicinfor = function(imgdata){
        var data={"imgData":imgdata,"fileDealType":"newsfile"}; //后台默认 当前人为 图片唯一标识KEY
        $.ajax({
            url : '/fileweb/imgupload',
            type : 'post',
            dataType:'json',
            data : data,
            success : function(data) {
                if(data.result=='success'){
                    $("#news_image_show").attr("src",data.fileUrl);
                    $("#news_image").val(data.fileUrl);
                }
            },
            error: function(){
            }
        });
    }
    BaseUtils.checkCurrentSysTimeout("/psnweb/ajaxtimeout", function(){
        var parnode = document.getElementById("cut_img");
        parnode.style.display="block";
        publicPicclipupload({
            "parelement":parnode,
            "scanwidth" : 180,
            "scanheight" : 140,
            "filecallback":cutpicinfor
        });
    }, 1);
}




</script>

<div style="margin-top: 48px;">
</div>
<div class="new-newshow_container">
<div class="new-newaedit_header">
  <input type="hidden" id="news_id" value="${newsShowInfo.id}">
  <input type="hidden" id="des3_news_id" value="${newsShowInfo.des3NewsId}">
  <input type="hidden" id="news_image" value="${newsShowInfo.image}">
  <div class="new-newaedit_header-avator pro-header__base-info" >
    <div class="pro-header__avatar pro-header__avatar" id="upload_img" style="position: relative;" onclick="cutImg();">
      <img  src="${newsShowInfo.image}"  id="news_image_show" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_newsdefault.png'">
      <div class="pro-header__avator-tip">
        点击上传图像</div>
    </div>
  </div>
  <div class="new-newaedit_header-input">
    <div class="new-newaedit_header-input_title"><textarea  placeholder="<s:text name="news.edit.title"/>" id="news_title" maxlength="200"><c:out value="${newsShowInfo.title}" /></textarea> </div>
    <div class="new-newaedit_header-input_intruc"><textarea placeholder="<s:text name="news.edit.brief"/>" id="news_brief" maxlength="200"><c:out value="${newsShowInfo.brief}" /> </textarea></div>
  </div>
</div>
<div class="new-newaedit_neck">
  <div class="new-newaedit_neck-save"  id="news_save_id" onclick="NewsBase.save(this,'${newsShowInfo.publish}');"><s:text name="news.edit.save"/></div>
  <div class="new-newaedit_neck-item" onclick="NewsBase.preview();"><s:text name="news.edit.preview"/></div>
  <c:if test="${newsShowInfo.id > 0 && !newsShowInfo.publish}">
    <div class="new-newaedit_neck-item"  id="news_publish" onclick="NewsBase.newsPublish('${newsShowInfo.des3NewsId}')"><s:text name="news.edit.publish"/></div>
  </c:if>
  <c:if test="${newsShowInfo.id == null  ||  newsShowInfo.publish}">
    <div class="new-newaedit_neck-item Global-prohibit_click" id="news_publish" onclick="NewsBase.newsPublish('${newsShowInfo.des3NewsId}')"><s:text name="news.edit.publish"/></div>
  </c:if>

  <c:if test="${newsShowInfo.id > 0}">
    <div class="new-newaedit_neck-item" onclick="NewsBase.newsDel('${newsShowInfo.des3NewsId}','edit')"><s:text name="news.edit.delete"/></div>
  </c:if>
  <c:if test="${newsShowInfo.id == null }">
    <div class="new-newaedit_neck-item Global-prohibit_click" onclick="NewsBase.newsDel('${newsShowInfo.des3NewsId}','edit')"><s:text name="news.edit.delete"/></div>
  </c:if>
  <div class="new-newaedit_neck-item" onclick="NewsBase.backNewsList('${origin}');"><s:text name="news.edit.back"/></div>
</div>
<div class="new-newaedit_body">
  <textarea id="news_content" id="news_content">${newsShowInfo.content}</textarea>

</div>

  <div class="target" style="display: none;" id="cut_img"></div>
</div>

<!-- 超时弹框 -->
<%@ include file="/skins_v6/login_box.jsp"%>
