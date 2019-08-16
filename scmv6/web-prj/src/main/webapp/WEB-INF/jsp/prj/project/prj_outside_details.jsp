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
<meta charset="utf-8">
<title></title>
<%@ include file="prj_head_res.jsp"%>
<script type="text/javascript">
  $(function() {
    // 首次加载，默认加载项目基本信息
    NewProject.loadProjectInfo($(".New-proManage_containerBody-headerItem").eq(0), '${des3PrjId}');
    // 记录阅读记录
    NewProject.ajaxProjectView();
  });

  // 分享回调
  function shareCallback() {
    var $div = $('.dev_prj_share');
    var count = Number($div.text().replace(/[\D]/ig, "")) + 1;
    $div.text($div.attr("sharetitle") + "(" + count + ")");
  }
  function sharePsnCallback() {
    shareCallback();
  }
  function shareGrpCallback() {
    shareCallback();
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
      </div>
      <!-- 项目基本信息，项目经费，项目报告，项目成果 显示的容器 -->
      <div class="New-proManage_BodyItem" id="project_content_container"></div>
    </div>
  </div>
  <!-- 分享插件 -->
  <jsp:include page="/common/smate.share.jsp" />
</body>
</html>