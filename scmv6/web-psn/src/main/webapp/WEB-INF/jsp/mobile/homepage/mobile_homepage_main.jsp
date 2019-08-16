<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/resume/psn_cnf_const.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>科研之友</title>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/common.css" />
<link href="${resmod}/css/scmjscollection.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/plugin/scm.pop.mobile.css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/jquery-qrcode.min.js"></script>
<script type="text/javascript" src="${resmod }/js/weixin/wechat.pub.js?version=2"></script>
<script type="text/javascript" src="${resmod}/mobile/js/wechat.custom.js"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript" src="${resmod }/smate-pc/new-confirmbox/confirm.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
<script type="text/javascript" src="${resmod}/js/homepage/mobile/mobile.homepage.js?version=20181013"></script>
<script type="text/javascript" src="${resmod}/mobile/js/plugin/mobile.share.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/msgbox/mobile.msg.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script type="text/javascript" src="${resmod }/mobile/js/pub/mobile.pub.js?version=2"></script>
<script type="text/javascript" src="${resmod }/js/psnhome/prj/mobile_prj.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_mainlist.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/browsercompatible.js"></script>
<script type="text/javascript" src="${resmod}/js/loadStateIco.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/new-mobile_movedelete.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/android/app_fun.js"></script>
<script type="text/javascript">
    $(function(){
        scmShare.buildImgTipsDiv();
        if ($.trim($("#outHomePage").val()) != "true") {
            mobile_bottom_setTag("psn");
        };
        showRepresentPub();
        showRepresentPrj();
        showPsnKeywords();
        $("#qrcode_div").click(function(e){
            $("#qrcode_div").hide();
        });
        $('#qrcode_show_div').click(function(e){
            e.stopPropagation();
        });
        $("#homepage_share_box").click(function(e){
            $("#homepage_share_box").hide();
        });
        $(".footer_share-function_item").click(function(e){
            e.stopPropagation();
        });
        
        $("#more_operation_back_div").click(function(){
            $("#more_operation_div").hide();
            $("#more_operation_back_div").hide();
        });
        $("#more_operation_div").click(function(e){
            e.stopPropagation();
        });
        
        //生成二维码的方法放到img标签的onload属性里面了，保证生成的二维码中间的logo不会空白
//        getQrImg("${domainMobile}/psnweb/mobile/outhome?des3ViewPsnId=${person.personDes3Id }");
        //添加访问记录
        if($("#outHomePage").val()){
            SmateCommon.addVisitRecord($("#des3PsnId").val(),$("#des3PsnId").val(),6);
        }
        initWechatMenu();
        initShareMenu();
        //跳转成果， 专利，成果认领表 start
        var module ="${module}";
        var showDialogPub ="${showDialogPub}";
        var filterPubType ="${filterPubType}";
        if(module!=""&&module==="pub"){
            if(filterPubType!==""){
                window.location.href="/pub/querylist/psn?pubType="+filterPubType
            }else if(showDialogPub === "showPubConfirmMore"){
                window.location.href="/pub/confirmpublist?fromPage=mobileConfirmPub&toBack=psnpub" ;
            }else{
                window.location.href="/pub/querylist/psn"
            }
            
        }
      //跳转成果， 专利，成果认领表  end
      if($("#isMyself").val() == "true"){
          $(".edit_icon_i").show();
      }

    //判断是否为iso系统
      var ua = navigator.userAgent.toLowerCase();
      if(ua.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/i)){
      //调整app打开按钮的居中
        var oWidth = window.innerWidth;
           document.getElementsByClassName("openpage-inapp_btn")[0].style.left = (oWidth - 96 )/2 + "px";
           window.onresize = function(){
               var oWidth = window.innerWidth;
               document.getElementsByClassName("openpage-inapp_btn")[0].style.left = (oWidth - 96 )/2 + "px";
           }
        $("#openAppBtn").show();
      }
      var itemList = $(".app_psn-main_page-body_item");
      if(itemList.length == 0){
        $(".new-mobile_Privacy-tip_container").css("display","");
        $(".app_psn-main_page").css("margin-bottom","");
      }
      if("${isMyself}" == "true"){
        movedeletetarget("mobile-remove_item-target");
      }
      //判断个人简介内容是否溢出
      if($("#psn_brief_div").height() < $("#psn_brief_text").height()){
        $("#more_psn_brief").show();
      }
    });

    function openApp(){
      var service = window.location.href;
      if(service.indexOf("/PM") == -1){
        service += "?des3ViewPsnId=${person.personDes3Id }";
      }
      window.location.href = "/oauth/openApp?service=" + encodeURIComponent(service);
    }

    function doEditScienceArea(){
        document.location.href = "/psn/mobile/improvearea?isHomepageEdit=1";
    }
    
    function commonResAward(obj){
      var resType = $(obj).attr("resType");
      if(resType=='prj'){
        Project.award(obj);        
      }
      if(resType=='sns'){
        mobile.snspub.awardoptnew(obj);       
      }

    }
    function commonShowReplyBox(obj){
      var resType = $(obj).attr("resType");
      if(resType=='prj'){
        var des3PrjId = $(obj).attr("resId");
        location.href="/prjweb/wechat/findprjxml?des3PrjId="+des3PrjId;        
      }
      if(resType=='sns'){
        var des3PubId = $(obj).attr("resId");
        mobile.snspub.details(des3PubId, 'single');
      }

    }

    function callBackAward(obj,awardCount){
      if(awardCount>999){
        awardCount = "1k+";
      }
      $(obj).find(".new-Standard_Function-bar_item").toggleClass("new-Standard_Function-bar_selected");
      var isAward = $(obj).attr("isAward");
      if (isAward == 1) {// 取消赞
        $(obj).attr("isAward", 0);           
        if (awardCount == 0) {
          $(obj).find('span').text("赞");
        } else {
          $(obj).find('span').text("赞" + "(" + awardCount + ")");
        }
      } else {// 赞
        $(obj).attr("isAward", 1);
        $(obj).find('span').text("取消赞" + "(" + awardCount + ")");
      }
    }  
    function callBackCollect(obj){
      $(obj).toggleClass("new-Standard_Function-bar_selected");
      var isCollect = $(obj).attr("collect");
      if(isCollect==0){
        $(obj).find("span").text("取消收藏");
        $(obj).attr("collect",1);
      }else{
        $(obj).find("span").text("收藏");
        $(obj).attr("collect",0);    
      }
    }  
    
  //初始化移动端分享
    var psnInsAndDep = "${psnInfo.insAndDep }";
    var psnPosAndTitolo = "${psnInfo.posAndTitolo }";
    var summary = "";
    if(psnInsAndDep != "" && psnPosAndTitolo != "" && psnInsAndDep != null && psnPosAndTitolo != null){
        summary = "${psnInfo.insAndDep }, ${psnInfo.posAndTitolo }";
    }else{
        summary = "${psnInfo.insAndDep }${psnInfo.posAndTitolo }";
    }
    var config = {
        shareUrl: "${domainMobile}/psnweb/mobile/outhome?des3PsnId=${person.personDes3Id }", // 分享的链接
        shareTitle: "${showName}", // 分享的标题
        shareImg: "${person.avatars}", // 分享的图片url
        defaultShareImg: "${domainMobile}/resmod/smate-pc/img/logo_psndefault.png", // 若分享的图片url为空,则用这个默认的图片url
        desc: "", // 描述信息
        summary: summary, // 概要信息
        source: "${domainMobile}", // 来源，域名
        appKey: "1813949776", // 微博那边有个appKey参数
        type: "" //分享类型，分享到微信时用来判断是分享到朋友圈还是微信联系人
    }


    //安卓app分享的参数
    var appShareParams = {
        "shareUrl":  "${domainMobile}/psnweb/mobile/outhome?des3PsnId=" + encodeURIComponent("${person.personDes3Id }"),
        "shareImgUrl": "${person.avatars}",
        "title" : "${showName}",
        "description" : summary
    };
    
    //显示分享菜单
    function showMobileShareMenu(){
      showShareBox(appShareParams);
    }
    
    //移动端浏览器分享到具体平台
    function scmShareFun(type){
        scmShare.share(config, type);
    }
    //个人主页代表成果和代表项目分享
    function commonShare(obj){
      SmateCommon.mobileShareEntrance($(obj).attr("resId"),$(obj).attr("resType").toLowerCase());
    }
    
  //初始化微信菜单
    function initWechatMenu(){
        if("${wechatBrowser}" == "true"){
            var showName = "${showName}";
            var shareUrl = "${domainMobile}/psnweb/mobile/outhome?des3ViewPsnId=${person.personDes3Id }";
            var shareImgUrl = "${person.avatars}";
            if(shareImgUrl == null || shareImgUrl == ""){
                shareImgUrl = "${domainMobile}/resmod/smate-pc/img/logo_psndefault.png";
            }
            $.post(
                "/psnweb/outside/ajaxsignature",
                {"currentUrl": encodeURIComponent(window.location.href)},
                function(data){
                    if(data.result == "success"){
                        smatewechat.customWeiXinShare(
                                data.appId,
                                data.timeStamp, 
                                data.nonceStr,
                                data.signature,
                                showName,
                                shareUrl,
                                shareImgUrl,
                                summary);
                    }
                },
                "json");
        }
    }
    
    //编辑个人头衔
    function editPsnInfo(){
      document.location.href="/psn/mobile/edit/psnInfo";
    }

    //编辑个人简介
    function editPsnIntro(){
      document.location.href="/psn/mobile/edit/psnIntro";
    }

    //工作/教育经历操作
    function operateHistory(type,module){
      var parameter = "type=add";
      if(type != "add"){
        parameter = "type=edit&"+ (module=="workhistory"?"workId=":"eduId=") + type;
      }
      document.location.href="/psn/mobile/edit/"+module +"?"+ parameter;
    }
    
  //全文请求提示
    function fullTextUpTips(){
      if (document.getElementsByClassName("bckground-cover").length == 0) {
        var innerele = '<div class="background-cover__content">请使用电脑端上传全文</div>';
        var newele = document.createElement("div");
        newele.className = "bckground-cover";
        newele.innerHTML = innerele;
        document.getElementsByTagName("body")[0].appendChild(newele);
        var windheight = newele.offsetHeight;
        var windwidth = newele.offsetWidth;
        var windbottom = (windheight - 48) / 2 + "px";
        var windleft = (windwidth - 240) / 2 + "px";
        document.getElementsByClassName("background-cover__content")[0].style.left = windleft;
        document.getElementsByClassName("background-cover__content")[0].style.bottom = windbottom;
        setTimeout(function() {
          document.getElementsByClassName("background-cover__content")[0].style.bottom = "-64px";
          setTimeout(function() {
            document.getElementsByTagName("body")[0].removeChild(newele);
          }, 500);
        }, 1500);
      }
    }
  
  
    </script>
</head>
<body>
  <%@ include file="/WEB-INF/jsp/psnprofile/psn_cnf_const.jsp"%>
  <input type="hidden" id="des3PsnId" value="${person.personDes3Id }" />
  <input type="hidden" id="outHomePage" value="${outHomePage }" />
  <input type="hidden" id="pubSum" value="${psnStatistics.pubSum }" />
  <input type="hidden" id="des3CurrentPsnId" value="${des3CurrentPsnId }" />
  <input type="hidden" id="hasLogin" value="${hasLogin }" />
  <input type="hidden" value="${payAttention }">
  <input type="hidden" id="isWechatBrowser" value="${wechatBrowser }" />
  <div class="app_psn-main_page" style="background: #eee; margin-bottom:50px;">
    <div class="app_psn-main_page-header">
      <div class="app_psn-main_page-header_top">
        <i class="material-icons" onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');">keyboard_arrow_left</i>
        <div class="app_psn-main_page-header_top-right">
          <i class="app_psn-main_page-header_top-share" onclick="showQrcode();"></i> <i class="material-icons"
            onclick="showMoreOperation();">more_horiz</i>
          <div class="scan_addfriend" style="display: none" id="more_operation_back_div"></div>
          <c:if test="${hasLogin ==1}">
            <div class="app_psn-main_page-header_top-func" id="more_operation_div" style="display: none">
              <div class="chevron-icon__div2"></div>
              <div class="app_psn-main_page-header_top-func_item" onclick="showMobileShareMenu();">
                <div class="app_psn-main_page-header_top-func_tip-container">
                  <i class="app_psn-main_page-header_top-func_share"></i>
                </div>
                <span>分享</span>
              </div>
              <s:if test="payAttention == 0 && !isMyself">
                <div class="app_psn-main_page-header_top-func_item attention_btn" id="addAttention"
                  onclick="addAttention('${person.personDes3Id }')">
                  <div class="app_psn-main_page-header_top-func_tip-container">
                    <i class="app_psn-main_page-header_top-func_follow"></i>
                  </div>
                  <span>关注</span>
                </div>
              </s:if>
              <s:if test="payAttention == 1 && !isMyself">
                <div class="app_psn-main_page-header_top-func_item attention_btn" id="cancelAttention"
                  onclick="cancelAttention('${attentionId }', '${person.personDes3Id }')">
                  <div class="app_psn-main_page-header_top-func_tip-container">
                    <i class="app_psn-main_page-header_top-func_deletefollow"></i>
                  </div>
                  <span>取消关注</span>
                </div>
              </s:if>
              <s:if test="isFriend == 1 && !isMyself">
                <div class="app_psn-main_page-header_top-func_item friend_req_btn" id="cancelFrdBtn"
                  onclick="cancelFriend('${person.personDes3Id }')">
                  <div class="app_psn-main_page-header_top-func_tip-container">
                    <i class="app_psn-main_page-header_top-func_delete"></i>
                  </div>
                  <span>删除联系人</span>
                </div>
              </s:if>
            </div>
          </c:if>
          <c:if test="${hasLogin ==0}">
            <div class="app_psn-main_page-header_top-func" id="more_operation_div" style="display: none">
              <div class="app_psn-main_page-header_top-func_item" onclick="wechat.pub.PsnHometimeOut();">
                <div class="app_psn-main_page-header_top-func_tip-container">
                  <i class="app_psn-main_page-header_top-func_share"></i>
                </div>
                <span>分享</span>
              </div>
              <s:if test="payAttention == 0 && !isMyself">
                <div class="app_psn-main_page-header_top-func_item attention_btn" onclick="wechat.pub.PsnHometimeOut()">
                  <div class="app_psn-main_page-header_top-func_tip-container">
                    <i class="app_psn-main_page-header_top-func_follow"></i>
                  </div>
                  <span>关注</span>
                </div>
              </s:if>
              <s:if test="payAttention == 1 && !isMyself">
                <div class="app_psn-main_page-header_top-func_item attention_btn" onclick="wechat.pub.PsnHometimeOut()">
                  <div class="app_psn-main_page-header_top-func_tip-container">
                    <i class="app_psn-main_page-header_top-func_deletefollow"></i>
                  </div>
                  <span>取消关注</span>
                </div>
              </s:if>
            </div>
          </c:if>
        </div>
      </div>
      <c:if test="${hasLogin ==1}">
        <div class="app_psn-main_page-header_middle">
          <div class="app_psn-main_page-header_middle-container">
            <div class="app_psn-main_page-header_middle-container_avator">
              <img alt="" src='${person.avatars}' onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'" />
            </div>
            <s:if test="isFriend == 0 && !isMyself">
              <div class="app_psn-main_page-header_middle-container_btn " id="addFrdBtn"
                onclick="SmateCommon.addIdentificFriend('${person.personDes3Id }', this, addFriendSuccessFun, addFriendErrorFun)">加为联系人</div>
            </s:if>
            <div class="app_psn-main_page-header_middle-container_btn " style="display: none" id="addFrdBtn"
              onclick="SmateCommon.addIdentificFriend('${person.personDes3Id }', this, addFriendSuccessFun, addFriendErrorFun)">加为联系人</div>
          </div>
        </div>
      </c:if>
      <c:if test="${hasLogin ==0}">
        <div class="app_psn-main_page-header_middle">
          <div class="app_psn-main_page-header_middle-container">
            <div class="app_psn-main_page-header_middle-container_avator">
              <img alt="" src='${person.avatars}' onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'" />
            </div>
            <s:if test="isFriend == 0 && !isMyself">
              <div class="app_psn-main_page-header_middle-container_btn " id="addFrdBtn"
                onclick="wechat.pub.PsnHometimeOut()">加为联系人</div>
            </s:if>
            <div class="app_psn-main_page-header_middle-container_btn " style="display: none" id="addFrdBtn"
              onclick="wechat.pub.PsnHometimeOut()">加为联系人</div>
          </div>
        </div>
      </c:if>
      <div class="app_psn-main_page-header_footer">
        <div class="app_psn-main_page-header_footer-name">${showName}</div>
        <c:if test="${!empty psnInfo.insAndDep }"><div class="app_psn-main_page-header_footer-school">${psnInfo.insAndDep }</div></c:if>
        <div class="app_psn-main_page-header_footer-position">${psnInfo.posAndTitolo }</div>
        <s:if test="isMyself">
        <i class="app_psn-main_page-header_edit" onclick="editPsnInfo();"></i>
        </s:if>
      </div>
    </div>
    <div class="app_psn-main_page-neck">
      <div class="app_psn-main_page-neck_item app_psn-main_page-neck_item-left" onclick="showPrjs(this);">
        <div>${psnStatistics.prjSum > 0 ? psnStatistics.prjSum : 0 }</div>
        <div>项目</div>
      </div>
      <div class="app_psn-main_page-neck_item" onclick="showPubs('',this);">
        <div>${psnStatistics.pubSum > 0 ? psnStatistics.pubSum : 0 }</div>
        <div>成果</div>
      </div>
    </div>
    <div class="app_psn-main_page-body">
      <s:if test="psnCnfBuild != null">
        <s:set var="CNF_ANYMOD" value="psnCnfBuild.cnfMoudle.anyMod" />
        <!-- 个人简介  begin -->
        <s:if test="(#CNF_ANYMOD&#CNF_BRIEF)==#CNF_BRIEF || isMyself">
            <input type="hidden" id="psn_brif_content" value="${ person.brief}">
            <div class="app_psn-main_page-body_psninfor app_psn-main_page-body_item" style="background: #fff; align-items: center;">
              <div class="app_psn-main_page-body_item-title">
                <span>个人简介</span>
                <s:if test="isMyself">
                <i class="app_psn-main_page-body_item-edit" onclick="editPsnIntro();"></i>
                </s:if>
              </div>
              <c:if test="${!empty person.brief  }">
                  <div class="app_psn-main_page-body_psninfor-detail" id="psn_brief_div" style="white-space: pre-wrap;"><span id="psn_brief_text">${person.brief }</span></div>
                     <div class="app_psn-main_page-body_psninfor-checkall" id="more_psn_brief" onclick="showAllBrief();" style="display:none;">查看全部</div>
              </c:if>
            </div>
        </s:if>
        <!-- 个人简介  end -->
        <!-- 科技领域  begin -->
        <s:if test="(#CNF_ANYMOD&#CNF_EXPERTISE)==#CNF_EXPERTISE || isMyself">
          <input type="hidden" id="load_psn_keywords" value="1" />
          <c:if test="${ !empty psnScienceAreaFormList || isMyself}">
            <div class="app_psn-main_page-body_search app_psn-main_page-body_item" style="margin-top: 12px; background:#fff;">
              <div class="app_psn-main_page-body_item-title">
                <span>科技领域</span> <i class="app_psn-main_page-body_item-icon edit_icon_i" onclick="doEditScienceArea();"
                  style="display: none;"></i>
              </div>
              <c:forEach items="${psnScienceAreaFormList}" var="key" varStatus="stat">
                <div class="app_psn-main_page-body_search-detail">
                  <div class="app_psn-main_page-body_search-detail_item">${ key.psnScienceArea.showScienceArea}</div>
                </div>
              </c:forEach>
            </div>
          </c:if>
          <!-- 科技领域  end -->
          <!-- 关键词  begin -->
          <div class="app_psn-main_page-body_keyword app_psn-main_page-body_item" id="psn_keywords_div" style="margin-top: 12px; background:#fff;"></div>
          <!-- 关键词  end -->
        </s:if>
        <s:else>
          <input type="hidden" id="load_psn_keywords" value="0" />
        </s:else>
        <!-- 工作经历  begin -->
        <s:if test="(#CNF_ANYMOD&#CNF_WORK)==#CNF_WORK || isMyself">
            <div class="app_psn-main_page-body_psnwork app_psn-main_page-body_item" style=" background: #fff; margin-top: 12px;">
              <div class="app_psn-main_page-body_item-title">
                <span>工作经历</span>
                <s:if test="isMyself">
                <i class="material-icons app_psn-main_page-body_item-add" onclick="operateHistory('add','workhistory');" style="color: #999;">add</i>
                </s:if>
              </div>
              <c:forEach items="${psnWorkList}" var="psnwork">

                <div class="app_psn-main_page-body_work-detail mobile-remove_item-target">
                  <div class="app_psn-main_page-body_work-detail_item">

                    <img src="${psnwork.insImgPath }" class="app_psn-main_page-body_work-detail_item-avator"
                      onerror="src='/resmod/smate-pc/img/logo_instdefault.png'" />
                    <div class="app_psn-main_page-body_work-detail_item-content">
                      <div class="work-detail_item-name">${psnwork.insName }</div>
                      <div class="work-detail_item-time">${psnwork.workDesc }</div>
                      <div class="work-detail_item-discrible">${psnwork.description }</div>
                    </div>
                  </div>
                  <s:if test="isMyself">
                    <i class="app_psn-main_page-body_item-edit" onclick="operateHistory(${psnwork.workId },'workhistory');" workId="${psnwork.workId }" style="margin-right: 12px;"></i>
                    </s:if>
                </div>
              </c:forEach>
            </div>
        </s:if>
        <!-- 工作经历  end -->
        <!-- 教育经历  begin -->
        <s:if test="(#CNF_ANYMOD&#CNF_EDU)==#CNF_EDU || isMyself">
            <div class="app_psn-main_page-body_educate app_psn-main_page-body_item" style="margin-top: 12px; background: #fff;">
              <div class="app_psn-main_page-body_item-title">
                <span>教育经历</span>
                <s:if test="isMyself">
                <i class="material-icons app_psn-main_page-body_item-add" onclick="operateHistory('add','educatehistory');" style="color: #999;">add</i>
                </s:if>
              </div>
              <c:forEach items="${psnEduList}" var="psnEdu">
                <div class="app_psn-main_page-body_educate-detail mobile-remove_item-target">
                  <div class="app_psn-main_page-body_educate-detail_item ">

                    <img class="app_psn-main_page-body_educate-detail_item-avator" src="${psnEdu.insImgPath }"
                      onerror="this.src='${resmod}/smate-pc/img/logo_instdefault.png'">
                    <div class="app_psn-main_page-body_educate-detail_item-content">
                      <div class="educate-detail_item-name">${psnEdu.insName }</div>
                      <div class="educate-detail_item-time">${psnEdu.eduDesc }</div>
                      <div class="educate-detail_item-discrible">${psnEdu.description }</div>
                    </div>
                  </div>
                   <s:if test="isMyself">
                    <i class="app_psn-main_page-body_item-edit" onclick="operateHistory(${psnEdu.eduId },'educatehistory');" eduId="${psnEdu.eduId }" style="margin-right: 12px;"></i>
                    </s:if>
                </div>
              </c:forEach>
            </div>
        </s:if>
        <!-- 教育经历  end -->
        <!-- 代表性成果  begin -->
        <s:if test="(#CNF_ANYMOD&#CNF_PUB)==#CNF_PUB || isMyself">
          <input type="hidden" id="load_represent_pub" value="1" />
          <div class="app_psn-main_page-body_program app_psn-main_page-body_item" id="mobile_represent_pub_div" style=" margin-top: 12px; background: #fff; padding-bottom: 0px;"></div>
        </s:if>
        <s:else>
          <input type="hidden" id="load_represent_pub" value="0" />
        </s:else>
        <!-- 代表性成果  end -->
        <!-- 代表性项目  begin -->
        <s:if test="(#CNF_ANYMOD&#CNF_PRJ)==#CNF_PRJ || isMyself">
          <input type="hidden" id="load_represent_prj" value="1" />
          <div class="app_psn-main_page-body_program app_psn-main_page-body_item" id="mobile_represent_prj_div" style=" margin-top: 12px; background: #fff; padding-bottom: 0px;"></div>
        </s:if>
        <s:else>
          <input type="hidden" id="load_represent_prj" value="0" />
        </s:else>
        <!-- 代表性项目  end -->
    </div>
    </s:if>
    <!-- 电话 邮件 -->
    <input type="hidden" id="isMyself" value="${isMyself}" /> <input type="hidden" id="isFriend" value="${isFriend}" />
    <s:if test="(#CNF_ANYMOD&#CNF_CONTACT)==#CNF_CONTACT || isMyself">
      <c:if test="${isMyself ==true}">
        <c:if test="${not empty  person.tel || not empty  person.email}">
          <div class="app_psn-main_page-body_psnwork app_psn-main_page-body_item" id="mobile_represent_psn_div"
            style="display: flex; margin-top: 12px; background: #fff;">
            <div class="app_psn-main_page-body_item-title">
              <span>联系信息</span> <i class="material-icons" style="visibility: hidden;">mode_edit</i>
            </div>
            <div class="app_psn-main_page-body_program-detail">
              <c:if test="${not empty  person.tel }">
                <div class="page-body_program-detail_item represent_prj_item" style="justify-content: flex-start; width: 100%; padding-bottom: 12px;">
                  <i class="page-body_program-detail_item-phone"></i>
                  <div class="page-body_program-detail_item-content" style="align-items: flex-start; ">电话：${person.tel}</div>
                </div>
              </c:if>
              <c:if test="${not empty  person.email }">
                <div class="page-body_program-detail_item represent_prj_item" style="justify-content: flex-start; width: 100%; padding-bottom: 12px;">
                  <i class="page-body_program-detail_item-email"></i>
                  <div class="page-body_program-detail_item-content" style="align-items: flex-start;">邮件：${person.email}</div>
                </div>
              </c:if>
            </div>
          </div>
        </c:if>
      </c:if>
      <c:if test="${isMyself ==false && isFriend == 0}">
        <c:if test="${not empty tel || not empty email}">
          <div class="app_psn-main_page-body_psnwork app_psn-main_page-body_item" id="mobile_represent_psn_div"
            style="display: flex; margin-top: 12px; background: #fff;">
            <div class="app_psn-main_page-body_item-title">
              <span>联系信息</span> <i class="material-icons" style="display: none;">mode_edit</i>
            </div>
            <div class="app_psn-main_page-body_program-detail">
              <c:if test="${not empty tel }">
                <div class="page-body_program-detail_item represent_prj_item" style="justify-content: flex-start; width: 100%; padding-bottom: 12px;">
                  <i class="page-body_program-detail_item-phone"></i>
                  <div class="page-body_program-detail_item-content" style="display: block; text-align: left;">电话：${tel}</div>
                </div>
              </c:if>
              <c:if test="${not empty email }">
                <div class="page-body_program-detail_item represent_prj_item" style="justify-content: flex-start; width: 100%; padding-bottom: 12px;">
                  <i class="page-body_program-detail_item-email"></i>
                  <div class="page-body_program-detail_item-content" style="display: block; text-align: left;">邮件：${email}</div>
                </div>
              </c:if>
            </div>
          </div>
        </c:if>
      </c:if>
      <c:if test="${isMyself ==false && isFriend == 1}">
        <c:if test="${not empty person.tel || not empty person.email}">
          <div class="app_psn-main_page-body_psnwork app_psn-main_page-body_item" id="mobile_represent_psn_div"
            style="display: flex; margin-top: 12px; background: #fff;">
            <div class="app_psn-main_page-body_item-title">
              <span>联系信息</span> <i class="material-icons" style="display: none;">mode_edit</i>
            </div>
            <div class="app_psn-main_page-body_program-detail">
              <c:if test="${not empty person.tel }">
                <div class="page-body_program-detail_item represent_prj_item" style="justify-content: flex-start; width: 100%; padding-bottom: 12px;">
                  <i class="page-body_program-detail_item-phone"></i>
                  <div class="page-body_program-detail_item-content" style="display: block; text-align: left;">电话：${person.tel}</div>
                </div>
              </c:if>
              <c:if test="${not empty person.email }">
                <div class="page-body_program-detail_item represent_prj_item" style="justify-content: flex-start; width: 100%; padding-bottom: 12px;">
                  <i class="page-body_program-detail_item-email"></i>
                  <div class="page-body_program-detail_item-content" style="display: block; text-align: left;">邮件：${person.email}</div>
                </div>
              </c:if>
            </div>
          </div>
        </c:if>
      </c:if>
      
    </s:if>
    <div id="share_div" class=""></div>
  </div>
  </div>
  <!-- 二维码  begin -->
  <div class="scan_addfriend" style="display: none;" id="qrcode_div">
    <div class="scan_addfriend-container" id="qrcode_show_div">
      <div class="scan_addfriend-container_title" style="line-height: 15px;">
        <img id="psn_avatar_img" src='${person.avatars}' onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
        <div class="scan_addfriend-container_infor">
          <div class="scan_addfriend-container_name">${showName }</div>
          <div class="scan_addfriend-container_work">${psnInfo.insAndDep }</div>
          <div class="scan_addfriend-container_work">${psnInfo.posAndTitolo }</div>
        </div>
      </div>
      <div class="scan_addfriend-container_content">
        <c:if test="${picIsBase64==true}">
          <img id="qrCodeIco" src='data:image/gif;base64,${psnAvatarsUrl}'
            onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'" style="position: absolute; display: none;"
            onload="getQrImg('${domainMobile}/psnweb/mobile/outhome?des3ViewPsnId=${person.personDes3Id }');" />
        </c:if>
        <c:if test="${picIsBase64==false}">
          <img id="qrCodeIco" src='${psnAvatarsUrl}' onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'"
            style="position: absolute; display: none;"
            onload="getQrImg('${domainMobile}/psnweb/mobile/outhome?des3ViewPsnId=${person.personDes3Id }');" />
        </c:if>
        <img alt="" src="" id="create_qrcode_img">
        <div class="scan_addfriend-container_content" id="qrcode_img"></div>
      </div>
      <div class="scan_addfriend-container_footer">扫描二维码，加我为联系人</div>
    </div>
  </div>
  <!-- 二维码  end -->
  <!-- 分享操作  begin -->
  <div class="scan_addfriend" style="display: none" id="homepage_share_box">
    <!-- <div style="height: 16vh; width: 100vw; background: rgba(0,0,0,0.64); overflow-x: hidden;display:none;" id="homepage_share_box"> -->
    <div class="footer_share-function">
      <div class="footer_share-function_item">
        <div class="footer_share-function_item-list share_in_wechat" onclick="scmShareFun('pengyouquan')">
          <i class="footer_share-function_item-tip footer_share-function_item-wechat"></i> <span
            class="footer_share-function_item-list_detaile">朋友圈</span>
        </div>
        <div class="footer_share-function_item-list share_in_wechat" onclick="scmShareFun('wechatfriend')">
          <i class="footer_share-function_item-tip footer_share-function_item-wechatfriend"></i> <span
            class="footer_share-function_item-list_detaile">微信联系人</span>
        </div>
        <div class="footer_share-function_item-list" onclick="scmShareFun('linkedin')">
          <i class="footer_share-function_item-tip footer_share-function_item-link"></i> <span
            class="footer_share-function_item-list_detaile">领英</span>
        </div>
        <div class="footer_share-function_item-list" onclick="scmShareFun('weibo')">
          <i class="footer_share-function_item-tip footer_share-function_item-sina"></i> <span
            class="footer_share-function_item-list_detaile">新浪微博</span>
        </div>
        
        <!-- <div class="footer_share-function_item-list show_in_not_wechat" onclick="scmShareFun('douban')" style="display:none;">
          <i class="footer_share-function_item-tip footer_share-function_item-douban"></i> <span
            class="footer_share-function_item-list_detaile">豆瓣</span>
        </div> -->
        <div class="footer_share-function_item-list show_in_not_wechat" onclick="scmShareFun('qzone')" style="display:none;">
          <i class="footer_share-function_item-tip footer_share-function_item-qzone"></i> <span
            class="footer_share-function_item-list_detaile">QQ空间</span>
        </div>
      </div>
      <div class="footer_share-function_item share_in_wechat">
        <!-- <div class="footer_share-function_item-list" onclick="scmShareFun('douban')">
          <i class="footer_share-function_item-tip footer_share-function_item-douban"></i> <span
            class="footer_share-function_item-list_detaile">豆瓣</span>
        </div> -->
        <div class="footer_share-function_item-list" onclick="scmShareFun('qzone')">
          <i class="footer_share-function_item-tip footer_share-function_item-qzone"></i> <span
            class="footer_share-function_item-list_detaile">QQ空间</span>
        </div>
        <!-- <div class="footer_share-function_item-list"></div>
        <div class="footer_share-function_item-list"></div> -->

      </div>
    </div>
    <!-- </div> -->
  </div>
   <div class="openpage-inapp_btn" onclick="openApp()" id="openAppBtn" style="display:none">在App中打开</div>
  <!-- 分享操作  end -->
  <s:if test="outHomePage!='true'">
    <jsp:include page="../bottom/mobile_bottom.jsp"></jsp:include>
  </s:if>
  <s:else>
    <s:if test="hasLogin == 0">
      <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_outside_footer.jsp"></jsp:include>
    </s:if>
  </s:else>
          <div class="new-mobile_Privacy-tip_container" style="background: #eee;height: 60%;display:none;">
            <div class="new-mobile_Privacy-tip_container">
                <div class="new-mobile_Privacy-tip_avator"></div>
            <div class="new-mobile_Privacy-tip_content">
                由于用户主页设置了隐私，</br>内容未完全显示
            </div>
            </div>
        </div>
</body>
</html>
