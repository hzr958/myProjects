<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title>科研之友</title>
<meta charset="utf-8">
<%@ include file="prj_head_res.jsp"%>
<script type="text/javascript">
  var shareI18 = '<s:text name="project.details.share"/>';
  var module = "${module}";
  var isVIP = "${isVIP}";
  var isOwn = "${isOwn}";
  window.onload = function() {
    if(isVIP == true && isOwn == true){
      if (module == "" || module == "prjinfo") {
        // 首次加载，默认加载项目基本信息
        NewProject.loadProjectInfo($(".New-proManage_containerBody-headerItem").eq(0), '${des3PrjId}');
      } else {
        selectTab(module);
      }
    }else{
      NewProject.loadProjectInfo($(".New-proManage_containerBody-headerItem").eq(0), '${des3PrjId}');
    }
    
    // 记录阅读记录
    NewProject.ajaxProjectView();

    document.getElementsByClassName("New-changeStyle_container")[0].style.top = (window.innerHeight - document
        .getElementsByClassName("New-changeStyle_container")[0].offsetHeight)
        / 2 + "px";
    document.getElementsByClassName("New-changeStyle_container")[0].style.left = (window.innerWidth - document
        .getElementsByClassName("New-changeStyle_container")[0].offsetWidth)
        / 2 + "px";
    window.onresize = function() {
      document.getElementsByClassName("New-changeStyle_container")[0].style.top = (window.innerHeight - document
          .getElementsByClassName("New-changeStyle_container")[0].offsetHeight)
          / 2 + "px";
      document.getElementsByClassName("New-changeStyle_container")[0].style.left = (window.innerWidth - document
          .getElementsByClassName("New-changeStyle_container")[0].offsetWidth)
          / 2 + "px";
    }

  }

  function selectTab(tabName, obj) {
    if (obj != undefined) {
      BaseUtils.doHitMore(obj, 2000);
    }
    switch (tabName) {
      case 'exp' :
        $(".New-proManage_containerBody-headerItem").eq(1).click();
      break;
      case 'report' :
        $(".New-proManage_containerBody-headerItem").eq(2).click();
      break;
      case 'pub' :
        $(".New-proManage_containerBody-headerItem").eq(3).click();
      break;
      default :
        NewProject.loadProjectInfo($(".New-proManage_containerBody-headerItem").eq(0), '${des3PrjId}');
      break;
    }
  }
//分享回调(项目 和 成果 )
  function sharePsnCallback(dynId, shareContent, resId, pubId, isB2T, receiverGrpId, resType) {
    if (resType == "22" || resType == "24") {
      addPdwhPubShare(dynId, shareContent, resId, pubId, isB2T, receiverGrpId, resType);
    } else {
      shareCallback(dynId, shareContent, resId, pubId, isB2T, receiverGrpId, resType);
    }
  }
  function shareGrpCallback(dynId, shareContent, resId, pubId, isB2T, receiverGrpId, resType) {
    shareCallback(dynId, shareContent, resId, pubId, isB2T, receiverGrpId, resType);
  }
  //分享回调(项目 和 成果 )
  function shareCallback(dynId, shareContent, resId, pubId, isB2T, receiverGrpId, resType, dbId) {
    if (resType == "22" || resType == "24" || resType == "GRP_SHAREPUB") {
      var count = Number($('.dev_pub_share_' + pubId).text().replace(/[\D]/ig, "")) + 1;
      if (count > 999) {
        count = "1k+";
      }
      $('.dev_pub_share_' + pubId).html('<i class="icon-share"></i> ' + shareI18 + "(" + count + ")");
    } else {
      var $div = $('.dev_prj_share');
      var count = Number($div.text().replace(/[\D]/ig, "")) + 1;
      $div.text($div.attr("sharetitle") + "(" + count + ")");
      var receiverNum = 0;
      //如果是好友分享，则分享数加N start
      $("#grp_friends").find(".chip__text").each(function(i, n) {
        var code = $(n).closest(".chip__box").attr("code");
        if (code != null && $.trim(code) != "") {
          receiverNum = receiverNum + 1;
        }
      });
      if (receiverNum > 1) {
        count = count + receiverNum - 1;
        $div.text($div.attr("sharetitle") + "(" + count + ")");
      }
      // 及时更新项目统计数中的分享数
      $('.New-proManage_containerHeader-ItemNum').eq(2).html(count);
    }

  };
  function addPdwhPubShare(dynId, shareContent, resId, pubId, isB2T, receiverGrpId, resType) {
    $.ajax({
      url : '/pub/opt/ajaxpdwhshare',
      type : 'post',
      dataType : 'json',
      data : {
        'des3PdwhPubId' : resId,
        'comment' : shareContent,
        'sharePsnGroupIds' : receiverGrpId,
        'platform' : "2"
      },
      success : function(data) {
        var count = data.shareTimes;
        if (count > 999) {
          count = "1k+";
        }
        $('.dev_pub_share_' + pubId).html('<i class="icon-share"></i> ' + shareI18 + "(" + count + ")");
      }
    });
  }

  function fileuploadBoxOpenInputClick(ev) {
    var $this = $(ev.currentTarget);
    $this.find('input.fileupload__input').click();
  }
  function initShare(des3PubId, obj) {
    Pub.pdwhIsExist2(des3PubId, function() {
      Pub.getPdwhPubSareParam(obj);
      initSharePlugin(obj);
    });
  }

  //初始化 分享 插件
  function initSharePlugin(obj) {
    if (SmateShare.timeOut && SmateShare.timeOut == true)
      return;
    if (locale == "en_US") {
      $(obj).dynSharePullMode({
        'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
        'language' : 'en_US'
      });
    } else {
      $(obj).dynSharePullMode({
        'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)'
      });
    }
    var obj_lis = $("#share_to_scm_box").find("li");
    obj_lis.eq(1).click();
  };
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
      <!-- 说明关系是其他 -->
      <s:if test="isFriend == false && isOwn == false">
        <div class="new-program_header-add" onclick="findPsn.addFriendOther('${des3PrjPsnId}')">
          <i class="material-icons">add</i> <span class="new-program_header-add_detaile"
            style="line-height: 30px; margin-right: 8px;"><s:text name="project.details.add.friend"></s:text></span>
        </div>
      </s:if>
    </div>
  </div>
  <div style="width: 1200px; margin: 0 auto; margin-top: 70px;">
    <input type="hidden" id="des3PrjId" value="${des3PrjId}">
    <div class="New-proManage_containerHeader">
      <div class="New-proManage_containerHeader-Count">
        <div class="New-proManage_containerHeader-CountItem">
          <span class="New-proManage_containerHeader-ItemNum">${readCount }</span> <span>阅读数</span>
        </div>
        <div class="New-proManage_containerHeader-CountItem">
          <span class="New-proManage_containerHeader-ItemNum">${awardCount }</span> <span>赞</span>
        </div>
        <div class="New-proManage_containerHeader-CountItem">
          <span class="New-proManage_containerHeader-ItemNum">${shareCount }</span> <span>分享</span>
        </div>
      </div>
      <div class="New-proManage_container-Content">
        <div class="New-proManage_container-ContentTitle">${title }</div>
        <div class="New-proManage_container-ContentAuthor">${authorNames }</div>
        <!--<div class="New-proManage_container-ContentIntroduce">项目介绍</div> 暂时没有-->
        <!-- 项目赞，分享 -->
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
            <div class="new-program_body-container_func-item" style="margin-left: 16px;"
              onclick="project.share('${des3PrjId}',event)">
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
        </div>
      </div>
    </div>
    <div class="New-proManage_containerBody">
      <div class="New-proManage_containerBody-header">
        <div class="New-proManage_containerBody-headerItem" onclick="NewProject.loadProjectInfo(this,'${des3PrjId}');">基本信息</div>
        <!-- 以下功能需要会员才能观看，而且必须还是本人 -->
        <s:if test="isVIP == true && isOwn == true">
          <div class="New-proManage_containerBody-headerItem"
            onclick="NewProject.loadProjectExpenditure(this,'${des3PrjId}');">项目经费</div>
          <div class="New-proManage_containerBody-headerItem"
            onclick="NewProject.loadProjectReport(this,'${des3PrjId}');">项目报告</div>
          <!-- <div class="New-proManage_containerBody-headerItem">项目变更</div> -->
          <div class="New-proManage_containerBody-headerItem" onclick="NewProject.loadProjectPub(this,'${des3PrjId}');">项目成果</div>
        </s:if>
      </div>
      <!-- 项目基本信息，项目经费，项目报告，项目成果 显示的容器 -->
      <div class="New-proManage_BodyItem" id="project_content_container"></div>
    </div>
  </div>
  <div class="New-changeStyle_container" style="display: none;">
    <div class="New-changeStyle_container-header">
      <span>支出明细</span> <i class="list-results_close" onclick="NewProject.closeExpenRecordDetail(this)"></i>
    </div>
    <div class="New-changeStyle_container-body"></div>
  </div>
  <!-- 分享插件 -->
  <jsp:include page="/common/smate.share.jsp" />
</body>
</html>