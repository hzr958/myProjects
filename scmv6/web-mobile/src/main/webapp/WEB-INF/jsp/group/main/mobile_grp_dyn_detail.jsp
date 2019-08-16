<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友-动态详情</title>
<link href="${resmod }/mobile/css/common.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/common_HideShowDiv.css" rel="stylesheet" type="text/css">
<link href="${resmod}/css/smate.alerts.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/plugin/scm.pop.mobile.css">

<script type="text/javascript" src="${resmod }/js_v8/common/jquery/jquery-3.4.1.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js" charset="UTF-8"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js" charset="UTF-8"></script>
<script type="text/javascript" src="/resmod/js/plugin/jquery.thickbox.min.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/autosize.min.js"></script>
<script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="/resmod/mobile/js/wechat.custom.js"></script>
<script type="text/javascript" src="/resmod/js/plugin/smate.custom.valuechange.js"></script>
<script type="text/javascript" src="${resmod}/js/ajaxparamsutil_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/group/mobile.grp.main.js"></script>

<script type="text/javascript">
	$(function(){
	  $(".dyn_content_div").removeAttr("onclick");
	  autosize(document.querySelectorAll('textarea'));
      $("#comment_content").focus();
      Group.ajaxLoadDynComments("${dto.des3DynId}", "${dto.des3GrpId}");
      $(".comments").css("display","none");
      $(".look_all").css("display","none");
      $("#comment_content").on('valuechange', function (e, previous) {
      	 if($.trim($(this).val()).length>0){
               $("#scm_send_comment_btn").attr("onclick","Group.doCommentGrpDyn('${dto.des3DynId}', '${dto.des3GrpId}');");
               $("#scm_send_comment_btn").removeClass("not_point");
               
      	 }else{
      		 if($.trim($(this).val()).length==0){
      			 $("#scm_send_comment_btn").removeAttr("onclick");
      			 $("#scm_send_comment_btn").addClass("not_point");
      		 }
      	 }
      });
      transDetailUrl();
	});
	
	/**
	 * 发表的动态中含有网址的话展示时显示网页链接
	 */
	function transDetailUrl(){
		var $div = $(".dyn_content");
		if(!$div.attr('class') || $div.hasClass('dyn_content')){
			if(!$div.attr('transurl')){
				var c = $div.html().trim();
				var matchArray = Group.matchUrl(c);
				var newstr = "";
				for(var i=0; i <= matchArray.length; i++){
					var beginIndex = i==0 ? 0 : matchArray[i-1].lastIndex;
					var endIndex = i == matchArray.length ? c.length : matchArray[i].index;
					var stri = c.substring(beginIndex, endIndex);
					var urli = "";
					if(i < matchArray.length){
						urli = " <a href=\"" + matchArray[i].str + "\" style=\"color: #005eac !important;\" target=\"_blank\">" + matchArray[i].str + "</a> ";
					}
					newstr += stri + urli;
				}
				$div.html(newstr);
				$div.attr('transurl', 'true');
			}
		}
	}
	
	function focusCommentInput(){
	  var commentVal = $("#comment_content").val();
	  $("#comment_content").val("").focus().val(commentVal);
	}
</script>
</head>
<body>
    <input type="hidden" id="des3GrpId" value="${dto.des3GrpId}"/>
    <input type="hidden" id="des3CurrentPsnId" value="${dto.des3PsnId}"/>
  <div class="m-top m-top_top-background_color">
    <a onclick="window.history.back();" class="fl" style="width: 46px;"><i class="material-icons navigate_before">&#xe408;</i></a>
    <span class="m-top_top-background_color-title"> 动态详情 </span>
  </div>
  <div class="top_clear"></div>
  <div class="content dynamic__box discuss_box" id="div_dynamicContent" style="margin-top: 0px; background: #fff;">
    
    
    <c:set var="jsonDynInfo" value="${dynInfo.jsonDynInfo}"></c:set>
    <c:set var="groupDynShowInfo" value="${dynInfo}"></c:set>
    <%@ include file="grp_dyn_content.jsp" %>
    
    <div class="new-Standard_Function-bar" style="margin-top: 12px; padding: 0px 16px; width: 90vw; margin-bottom: 12px;">
            <a class="manage-one mr20" onclick="GroupDyn.award('${dynInfo.des3DynId }', '${dynInfo.resType }',
             '${dynInfo.des3ResId}', '${dynInfo.dynType}', this)"> 
             <div class="new-Standard_Function-bar_item" style="margin-left: 0px;width:100%">
                  <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> 
                  <span class="new-Standard_Function-bar_item-title span_award">
                    <c:if test="${dynInfo.awardstatus == 1 }">取消赞</c:if> 
                    <c:if test="${dynInfo.awardstatus == 0 }">赞</c:if>
                    <iris:showCount count="${dynInfo.awardCount }" preFix="(" sufFix=")"/>
                  </span>
               </div>
           </a>
          
          <c:if test="${ dynInfo.resType != 'fund' && dynInfo.resType != 'agency'}">
            <a class="manage-one mr20 dev_pub_comment" onclick="focusCommentInput();">
              <div class="new-Standard_Function-bar_item" style="width:100%;">
                <i class="new-Standard_function-icon new-Standard_comment-icon"></i> 
                <span class="new-Standard_Function-bar_item-title span_comment" des3DynId="${dto.des3DynId}">
                                                        评论<iris:showCount count="${dynInfo.commentCount }" preFix="(" sufFix=")"/>
                </span>
              </div>
            </a>
            
          </c:if>
          <c:if test="${dynInfo.resId !=0 && dynInfo.resId !=null && not empty dynInfo.resId}">
            <a class="manage-one mr20 dev_pub_share" from="grp_dyn" des3DynId="${dynInfo.des3DynId }" onclick="Group.shareRes('${dynInfo.des3ResId}', '${dynInfo.resType}', '${jsonDynInfo.RES_NOT_EXISTS }', this);"
              resid="${dynInfo.des3ResId}" des3resid="" nodeid="1" restype="${dynInfo.resType}"
              databaseType="" resInfoId="${dynInfo.dynId }" notEncodeId="${dynInfo.resId }">
                <div class="new-Standard_Function-bar_item" style="width:100%;">
                  <i class="new-Standard_function-icon new-Standard_Share-icon"></i> 
                  <span class="new-Standard_Function-bar_item-title span_share">
                                                            分享<iris:showCount count="${dynInfo.shareCount }" preFix="(" sufFix=")"/>
                  </span>
                </div>
            </a>
          </c:if>
          
          <c:if
            test="${dynInfo.resId !=0 && dynInfo.resId !=null && not empty dynInfo.resId}">
            <!-- 收藏、取消收藏成果 -->
            <c:if test="${dynInfo.resType =='pub' || dynInfo.resType =='pdwhpub' }">
              <c:if test="${ dynInfo.hasCollenciton}">
                <a class="manage-one mr20" collected="1" onclick="GroupDyn.dealCollectedPub('<iris:des3 code="${dynInfo.resId}"/>','${dynInfo.resType}',this, '${jsonDynInfo.RES_NOT_EXISTS }')"> 
                   <div class="new-Standard_Function-bar_item" style="width:100%;" >
                         <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                         <span class="new-Standard_Function-bar_item-title span_collect">取消收藏</span>
                   </div>
                </a>
              </c:if>
              <c:if test="${ !dynInfo.hasCollenciton}">
                <a class="manage-one mr20" collected="0" onclick="GroupDyn.dealCollectedPub('<iris:des3 code="${dynInfo.resId}"/>','${dynInfo.resType}',this, '${jsonDynInfo.RES_NOT_EXISTS }')"> 
                   <div class="new-Standard_Function-bar_item" style="width:100%;" >
                         <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                         <span class="new-Standard_Function-bar_item-title span_collect">收藏</span>
                   </div>
                </a>
              </c:if>
            </c:if>
            <!-- 收藏文件 -->
            <c:if test="${ dynInfo.resType =='grpfile'}">
              <a class="manage-one mr20" onclick="GroupDyn.grpDyncollectionGrpFile(this, '${jsonDynInfo.RES_NOT_EXISTS }');"> 
                   <div class="new-Standard_Function-bar_item" style="width:100%;" >
                         <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                         <span class="new-Standard_Function-bar_item-title span_collect">收藏</span>
                   </div>
                </a>
            </c:if>
            
            <!-- 收藏、取消收藏基金 -->
            <c:if test="${dynInfo.resType =='fund'}">
              <a class="manage-one mr20 collectCancel_${dynInfo.resId }" style="display: none;"
                onclick="GroupDyn.dynCollectCoperation($(this), '${dynInfo.des3ResId}', 1, '${dynInfo.resId }', '${jsonDynInfo.RES_NOT_EXISTS }');"> 
                   <div class="new-Standard_Function-bar_item" style="width:100%;" >
                         <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                         <span class="new-Standard_Function-bar_item-title span_collect">取消收藏</span>
                   </div>
                </a>
              
              <a class="manage-one mr20 collect_${dynInfo.resId }" onclick="GroupDyn.dynCollectCoperation($(this), '${dynInfo.des3ResId}', 0, '${dynInfo.resId }', '${jsonDynInfo.RES_NOT_EXISTS }');"> 
                   <div class="new-Standard_Function-bar_item" style="width:100%;" >
                         <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                         <span class="new-Standard_Function-bar_item-title span_collect">收藏</span>
                   </div>
                </a>
            </c:if>
            
            <!-- 关注、取消关注资助机构 -->
            <c:if test="${ dynInfo.resType =='agency'}">
              <c:if test="${ dynInfo.hasCollenciton}">
              <a class="manage-one mr20 agency_cancel_interest_opt"
                agencyDes3Id="${dynInfo.des3ResId}"
                onclick="GroupDyn.ajaxDynamicInterest($(this), '${dynInfo.des3ResId}', 0, '${jsonDynInfo.RES_NOT_EXISTS }');"> 
                 <div class="new-Standard_Function-bar_item" style="width:100%;" >
                       <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                       <span class="new-Standard_Function-bar_item-title span_collect">取消关注</span>
                 </div>
              </a>
              </c:if>
              
              <c:if test="${ !dynInfo.hasCollenciton}">
              <a class="manage-one mr20 agency_interest_opt" agencyDes3Id="${dynInfo.des3ResId}"
                onclick="GroupDyn.ajaxDynamicInterest($(this), '${dynInfo.des3ResId}', 1, '${jsonDynInfo.RES_NOT_EXISTS }');"> 
                   <div class="new-Standard_Function-bar_item" style="width:100%;" >
                         <i class="new-Standard_function-icon new-Standard_Save-icon"></i>
                         <span class="new-Standard_Function-bar_item-title span_collect">关注</span>
                   </div>
                </a>
                </c:if>
                
            </c:if>
          </c:if>
        </div>
    
    
    <div style="padding: 0px 16px">
      <h2 style="" class="wdful_comments">
        <a id="moreComment" style="display: none;" href="javascript:;" onclick="">查看更多评论</a>精彩评论
      </h2>
      <div id="pubview_discuss_list"></div>
    </div>
  </div>
  <div class="bottom_clear"></div>
  <div class="m-bottom">
    <div class="m-bottom_wrap" style=" display: flex; align-items: center; ">
     
      <!--后当没有输入不可点状态-->
      <div class="input_box" style="width: 85%; margin: 0px;">
        <textarea id="comment_content" rows="1" style="white-space: pre-wrap; " placeholder="添加评论" maxlength="300"></textarea>
      </div>
       <input type="button" id="scm_send_comment_btn" value="发布" class="not_point" style="width: 15%;">
    </div>
  </div>
  
  
  
  
  <!-- 评论模板 -->
  <div class="dynamic_one" id="grp_dyn_comment_template" style="display:none;">
      <div onclick="Group.openPsnDetail('${dto.des3PsnId }', event);" class="psn_info_div">
        <a class="dynamic_head"><img class="psn_avatars" src="${avatars }" onerror="this.onerror=null;this.src='/resmod/smate-pc/img/logo_psndefault.png'"></a>
        <p>
          <span class="fr do_comment_time">1秒前</span>
          <em class="do_comment_psn_name">${psnName }</em>
        </p>
      </div>
      <p class="p2 do_comment_content"></p>
    </div>
  
</body>
</html>
