<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<s:if test='keywords != null && keywords != ""'>
  <meta name="keywords" content="${keywords }" charset="utf-8">
</s:if>
<s:if test='prjAbstract != null && prjAbstract != ""'>
  <meta name="description" content="${prjAbstract }" charset="utf-8">
</s:if>
<s:elseif test='#prjAbstract == null || #prjAbstract == " "'>
  <s:if test='#locale == "zh_CN"'>
    <meta name="description" content="科研之友为用户提供基于科研社交网络平台的科技管理，成果推广和技术转移服务，使命是连接人与人，人与知识，人与服务，分享与发现知识，让科研更成功，让创新更高效"
      charset="utf-8">
  </s:if>
  <s:if test='#locale == "en_US"'>
    <meta name="description"
      content="Smate.com is a research social network service for research management, research marketing and technology transfer.  Our mission is to connect people to share and discover knowledge, and to research and innovate smart."
      charset="utf-8">
  </s:if>
</s:elseif>
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="apple-mobile-web-app-capable" content="yes">
<meta name="apple-mobile-web-app-status-bar-style" content="black">
<link href="/resmod/smate-pc/css/scm-newpagestyle.css" rel="stylesheet" type="text/css">
<link href="/resmod/smate-pc/css/newprograme.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/prj/prj.common.js"></script>
<script type="text/javascript" src="${resmod}/js/psnhome/prj/prj.common_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/friend/myfriend/friend.js"></script>
<script type="text/javascript" src="${resmod}/js/friend/friend.main_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/friend/findpsn/friend.findpsn.js"></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type='text/javascript' src='${resmod}/js/dialog.js'></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
  <style type="text/css">
   a{
     font-size: inherit;
     font-weight: inherit;
     line-height: inherit;
     color: inherit;
   }
  </style>
<script type="text/javascript">
        $(function(){
            // 请求评论数据
            project.ajaxPrjComments('${des3PrjId}');
            //添加访问记录
            SmateCommon.addVisitRecord("${des3PrjPsnId}", "${des3PrjId}", 4);
        });
		//==============================
		function sharePsnCallback (){
			shareCallback();
		}
		function shareGrpCallback (){
			shareCallback();
		}
		//==============================
        // 分享回调
        function shareCallback(){
            var $div = $('.dev_prj_share');
            var count = Number($div.text().replace(/[\D]/ig,""))+1;
            $div.text($div.attr("sharetitle")+"("+count+")");
            var receiverNum = 0;
            //如果是好友分享，则分享数加N start
            $("#grp_friends").find(".chip__text").each(function(i, n) {
                var code = $(n).closest(".chip__box").attr("code");
                if (code != null && $.trim(code) != "") {
                    receiverNum = receiverNum + 1;
                }
            });
            if(receiverNum>1){
                count  = count + receiverNum -1 ;
                $div.text($div.attr("sharetitle")+"("+count+")");
            }
            // end
        }
        //监控textarea内容
        function keypress(){
            var replyContent=$.trim($("#prjComment").find("textarea[name$='comments']").val());
            if(replyContent!=''){
                $("#prjCommnetBtn").removeAttr("disabled");
            }else{
                $("#prjCommnetBtn").attr("disabled", "disabled");
            }
        }
        //监控是否聚焦
        function onfocusShow(){
            $("#prjCommnetBtn").show();
            $("#prjCommnetCancle").show();
            var replyContent=$.trim($("#prjComment").find("textarea[name$='comments']").val());
            if(replyContent ==''){
                $("#prjCommnetBtn").attr("disabled", "disabled");
            }
            document.getElementById("input").focus();
        }
        //离开聚焦
        function onblurHidden(){
            var replyContent=$.trim($("#prjComment").find("textarea[name$='comments']").val());
            if(replyContent ==''){
            /*  $("#pubCommnetBtn").hide();
                $("#pubCommnetCancle").hide(); */
                $("#prjComment").find("textarea[name$='comments']").val("");
            }
        }
        
    </script>
</head>
<body>
  <div class="new-program_neck" style="margin: 0 auto; z-index: 35;">
    <div class="new-program_neck-container" style="margin: 0 auto;">
      <div class="new-program_neck-container_left">
        <a href="${ownerPsnIndexUrl }" target="_blank"> <img src="${ownerAvatars }"
          class="new-program_neck-container__avator">
        </a>
        <div class="new-program_neck-container_left-infor">
          <div class="new-program_neck-container_left-infor_box">
            <div class="new-program_neck-container_left-name">
              <a href="${ownerPsnIndexUrl }" title="${ownerName }" target="_blank">${ownerName }</a>
            </div>
          </div>
          <div class="new-program_neck-container_left-work" title="${ownerMessage}">${ownerMessage}</div>
          <div class="new-program_neck-container_left-prj">
            <div class="new-program_neck-container_left-prj_h">
              <s:text name="project.details.H_index"></s:text>
            </div>
            <div class="new-program_neck-container_left-num">${hIndex}</div>
          </div>
        </div>
      </div>
      <!-- 判断项目拥有者和浏览者关系  -->
      <s:if test="isOwn == true"></s:if>
      <!-- 说明关系是联系人 -->
      <%--  <s:if test="isFriend == true && isOwn == false">
                <div class="new-program_header-add"
                    onclick="Friend.ajaxDelFriend('${psnId}','${des3PrjPsnId}');">
                  <!--   <i class="material-icons">add</i> --> <span
                        class="new-program_header-add_detaile" style="line-height:30px;margin-right:8px;" ><s:text name="project.details.cancel.friend"></s:text></span>
                </div>
            </s:if> --%>
      <!-- 说明关系是其他 -->
      <s:if test="isFriend == false && isOwn == false">
        <div class="new-program_header-add" onclick="findPsn.addFriendOther('${des3PrjPsnId}')">
          <i class="material-icons">add</i> <span class="new-program_header-add_detaile"
            style="line-height: 30px; margin-right: 8px;"><s:text name="project.details.add.friend"></s:text></span>
        </div>
      </s:if>
    </div>
  </div>
  <div class="new-program_body-container">
    <div class="new-program_body-container_title-box">
      <div class="new-program_body-container_title" id="id_prj_title">${title}</div>
      <s:if test="isOwn == true">
        <div class="new-program_body-container_edit" onclick="project.editor('${des3PrjId}');">
          <s:text name="project.details.edit"></s:text>
        </div>
      </s:if>
    </div>
    <div class="new-program_body-container_author">${authorNames }</div>
    <s:if test="insName != null && insName != ''">
      <div class="new-program_body-container_mechanism">
        <span><s:text name="project.details.organization"></s:text></span>
        <div>${insName }</div>
      </div>
    </s:if>
    <s:if test="agencyName != null && agencyName != ''">
      <div class="new-program_body-container_category">
        <span><s:text name="project.details.organization.type"></s:text></span>
        <div>${agencyName}</div>
      </div>
    </s:if>
    <s:if test="prjInternalNo != null && prjInternalNo != ''">
      <div class="new-program_body-container_category">
        <span><s:text name="project.details.label.prj_internal_no"></s:text></span>
        <div>${prjInternalNo}</div>
      </div>
    </s:if>
    <s:if test="prjExternalNo != null && prjExternalNo != ''">
      <div class="new-program_body-container_category">
        <span><s:text name="project.details.label.prj_external_no"></s:text></span>
        <div>${prjExternalNo}</div>
      </div>
    </s:if>
    <s:if test="prjAmt != null && prjAmt != ''">
      <div class="new-program_body-container_category">
        <span><s:text name="project.details.prjAmt"></s:text></span>
        <div>${prjAmt}</div>
      </div>
    </s:if>
    <s:if test="prjDate != null && prjDate != ''">
      <div class="new-program_body-container_category">
        <span><s:text name="project.details.prjDate"></s:text></span>
        <div>${prjDate}</div>
      </div>
    </s:if>
    <div class="detail-pub__abstract" style="margin-top: 40px;">
      <span class="detail-pub__abstract_title" style="font-size: 18px;"><s:text name="project.details.abstract"></s:text></span>
      <div class="detail-pub__abstract_content">${!empty prjAbstract ? prjAbstract : "--"}</div>
    </div>
    <div class="detail-pub__keyword" style="margin-top: 40px;">
      <span class="detail-pub__keyword_title" style="font-size: 18px;"><s:text name="project.details.keywords"></s:text></span>
      <div class="detail-pub__keyword_content">${!empty keywords ? keywords : "--"}</div>
    </div>
    <div class="new-program_body-container_func">
      <s:if test="isAward == false">
        <div class="new-program_body-container_func-item" onclick="project.award('4','1','${des3PrjId}',this);">
          <i class="new-icon_aggregate new-thumbsup-icon"></i>
          <s:if test="awardCount > 0">
            <span class=""><s:text name="project.details.like"></s:text>(${awardCount})</span>
          </s:if>
          <s:else>
            <span class=""><s:text name="project.details.like"></s:text></span>
          </s:else>
        </div>
      </s:if>
      <s:elseif test="isAward == true">
        <div class="new-program_body-container_func-item new-program_body-container_func-checked"
          onclick="project.award('4','1','${des3PrjId}',this);">
          <i class="new-icon_aggregate new-thumbsup-icon"></i>
          <s:if test="awardCount > 0">
            <span class=""><s:text name="project.details.unlike"></s:text>(${awardCount})</span>
          </s:if>
        </div>
      </s:elseif>
      <s:if test="shareCount > 0">
        <div class="new-program_body-container_func-item"
          style="margin-left: 16px;" onclick="project.share('${des3PrjId}',event)">
          <i class="new-icon_aggregate new-share-icon"></i> <span class="dev_prj_share"
            sharetitle="<s:text name='project.details.share'/>"><s:text name="project.details.share"></s:text>(${shareCount})</span>
        </div>
      </s:if>
      <s:else>
        <div class="new-program_body-container_func-item " style="margin-left: 16px;"
          onclick="project.share('${des3PrjId}',event)">
          <i class="new-icon_aggregate new-share-icon"></i> <span class="dev_prj_share"
            sharetitle="<s:text name='project.details.share'/>"><s:text name="project.details.share"></s:text></span>
        </div>
      </s:else>
      <s:if test="commentCount > 0">
        <div class="new-program_body-container_func-item"
          style="margin-left: 16px;" id="dev_prj_comment" onclick="project.comments();">
          <i class="new-icon_aggregate new-focus-icon"></i> <span class=""><s:text name="project.details.comment"></s:text>(${commentCount})</span>
        </div>
      </s:if>
      <s:else>
        <div class="new-program_body-container_func-item" style="margin-left: 16px;" id="dev_prj_comment"
          onclick="project.comments();">
          <i class="new-icon_aggregate new-focus-icon"></i> <span class=""><s:text name="project.details.comment"></s:text></span>
        </div>
      </s:else>
    </div>
  </div>
  <div class="new-program_body-comment" id="dev_correlation_comment" style="margin-top: 60px;">
    <div class="new-program_body-comment_title">
      <s:if test="commentCount > 0">
        <span><s:text name="project.details.fantastic.comment"></s:text>(${commentCount})</span>
      </s:if>
      <s:else>
        <span><s:text name="project.details.fantastic.comment"></s:text></span>
      </s:else>
    </div>
    <div class="detail-comment__box" style="width: 1200px;">
      <div class="detail-post__box">
        <div class="detail-post__avatar">
          <a href="/psnweb/homepage/show?des3PsnId='${des3PsnId}'" target="_blank"> <img src="${visitAvatars}"
            class="new-program_body-comment_avator" onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
          </a>
        </div>
        <div class="detail-post__main">
          <div class="detail-post__input">
            <div class="form__sxn_row">
              <div class="input__box">
                <div class="input__area" id="prjComment">
                  <textarea maxlength="250" name="comments" id="input"
                    placeholder="<s:text name="project.details.add.comment"></s:text>" onKeyUp="keypress()"
                    onblur="onblurHidden()" onfocus="onfocusShow()"></textarea>
                  <div class="textarea-autoresize-div"></div>
                </div>
              </div>
            </div>
          </div>
          <div class="detail-post__actions">
            <div class="multiple-button-container">
              <div class="button__box button__model_rect" id="prjCommnetCancle" style="display: none;"
                onclick="project.cancelComment()">
                <a>
                  <div
                    class="button__main button__style_flat button__size_dense button__color-plain color-display_grey ripple-effect">
                    <s:text name="project.details.cancel"></s:text>
                  </div>
                </a>
              </div>
              <div class="button__box button__model_rect" id="prjCommnetBtn" disabled style="display: none;"
                onclick="project.comment('${des3PrjId}',this);">
                <a>
                  <div
                    class="button__main button__style_flat button__size_dense button__color-reverse color-display_blue ripple-effect">
                    <s:text name="project.details.post"></s:text>
                    </span>
                  </div>
                </a>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="detail-comment__list">
        <div class="main-list">
          <div class="main-list__list item_no-border item_zero-space" list-main="prj_comment_list"></div>
        </div>
      </div>
    </div>
  </div>
  <!-- 分享插件 -->
  <jsp:include page="/common/smate.share.jsp" />
</body>
</html>
