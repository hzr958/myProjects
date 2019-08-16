<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>动态分享界面</title>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${resmod }/mobile/css/mobile.css">
    <script type="text/javascript" src="${resmod }/js/jquery.js"></script>
    <script type="text/javascript" src="${resmod }/js/smate.share.js"></script>
    <script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
    <script type="text/javascript" src="${resmod }/js/smate.toast.js"></script>
    <script type="text/javascript" src="${resmod}/js_v8/agency/scm_pc_agency.js"></script>
    <script type="text/javascript" src="${ressns}/js/dyn/dynamic.common.js"></script>
    <script type="text/javascript" src="${ressns}/js/dyn/dynamic.common_${locale }.js"></script>
    <script type="text/javascript" src="${ressns}/js/dyn/dynamic.mobile.detail.js"></script>
    <script type="text/javascript">
      var dynamicMsg = "${dynamicMsg}";
      var dynId = "${form.dynId}";
      var databaseType = "${form.databaseType}";
      var dbId = "${form.dbId}";
      var text_content = "${form.dynText}";
      var resGrpId = "${form.resGrpId}";
      var des3ResId = "${form.des3ResId}";
      var title = "${form.title}";
      var resInfoId = "${form.resInfoId}";
      var fundId = "${form.fundId}";
      var des3PubId = "${form.des3PubId}";
      var parentDynId = "${form.parentDynId}";
      var des3DynId =  "${form.des3DynId}";
      var dynType = "${dynamicMsg.dynType}";
      var resType = "${dynamicMsg.resType}";
      var resid = "${dynamicMsg.resId}";
      window.onload = function(){
          $("#historyPage").val(document.referrer);
          var containerele = document.getElementsByClassName("new-mobile_sharedyn-input")[0];
          //页面异常时不用做操作
          try {
            containerele.style.height = window.innerHeight - 150 + "px";
          } catch (e) {}
          window.onresize = function(){
              containerele.style.height = window.innerHeight - 150 + "px";
          }
          //构建分享的资源
          if (dynamicMsg && dynamicMsg != "undefined") {
              buildRes();
          }
          if (title.length == 0 || title == "undefined") {
            $("#share_to_scm_box").hide();
          }
      }
      //构建分享资源参数
      function buildRes(){
        var $parent = $("#share_to_scm_box");
        $parent.html("");
        if (dynType == "ATEMP" || dynType == "B1TEMP") {
          $("#share_to_scm_box").attr("dyntype", dynType).attr("resType", resType).attr("des3dynid", des3DynId).attr(
              "des3parentdynid", des3DynId).attr("des3resid", des3ResId);
        } else if (dynType == "B2TEMP" || dynType == "B3TEMP") {
          $("#share_to_scm_box").attr("dyntype", dynType).attr("resType", resType).attr("des3ResId", des3ResId).attr("resid",
              resid).attr("text_content", text_content).attr("resgrpid", resGrpId).attr("dynId", dynId).attr("dbId", dbId);
          if ("GRP_ADDPUB" == dynType || "GRP_PUBLISHDYN" == dynType || "GRP_SHAREPUB" == dynType
              || "GRP_SHAREPDWHPUB" == dynType) {
            if (resType == "1") {
              resType = "pub";
            } else if (resType == "22") {
              resType = "pdwhpub";
            }
            $("#share_to_scm_box").attr("resType", resType);
          } else if ("GRP_SHAREFUND" == dynType) {
            if (resType == "11") {
              resType = "fund";
            }
            $("#share_to_scm_box").attr("resInfoId", resInfoId).attr("fundId", fundId).attr("resType", resType);
          } else if ("ATEMP" == dynType || "B1TEMP" == dynType || "B2TEMP" == dynType || "B3TEMP" == dynType) {
            // 个人动态
            if (resType == "pub") {
              resType = "1";
            } else if (resType == "pdwhpub") {
              resType = "22";
            } else if (resType == "fund") {
              resType = "11";
            }
            $("#share_to_scm_box").attr("resType", resType).attr("parentdynid", parentDynId).attr("des3PubId",
                des3PubId);
          }
        }
        createShareFileDiv($parent, title, des3ResId);
      }
      //构建资源框
      function createShareFileDiv($parent, title, des3ResId){
        var html = '<div class="share-attachments__item" style="width: 100%;display: flex;align-items: center;justify-content: space-between;"  des3ResId='
          + des3ResId
          + ' >'
          + '<div class="share-attachments__file-name"  style = "overflow:hidden;text-overflow: ellipsis;white-space: nowrap;width: 95%;">'
          + title
          + '</div> '
          + '<div class="share-attachments__cancel"  onclick="SmateCommon.goBack(\'/dynweb/mobile/dynshow\');"> <i class="material-icons">close</i></div> '
          + '</div> ';
        $parent.append(html);
      }
    </script>
</head>
<body>
    <input type="hidden" id = "historyPage" value = "">
    <c:choose>
        <c:when test="${not empty dynamicMsg }">
            <div class="message-page__header">
                <span class="new-mobile_sharedyn-item" onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');">取消</span>
                <span class="new-mobile_sharedyn-title">分享至动态</span>
                <span class="new-mobile_sharedyn-item" onclick="SmateShare.shareToDyn('mobile')">分享</span>
            </div>
            <div class="new-mobile_sharedyn-container">
                <div class="new-mobile_sharedyn-input">
                    <textarea placeholder="分享留言" class="new-mobile_sharedyn-input_box" id = "id_sharegrp_textarea" oninput="SmateCommon.isSupassMax();"></textarea>
                </div>
                <div class="new-mobile_sharedyn-shareitem" id = "share_to_scm_box">
                
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <!--资源不可用 -->
            <div class="new-mobile_Privacy-tip_container" style="background: rgb(238, 238, 238); height: 60%;">
                <div class="new-mobile_Privacy-tip_container" style="height: 100vh;">
                    <div class="new-mobile_Privacy-tip_avator"></div>
                <div class="new-mobile_Privacy-tip_content">
                                                  访问的资源不存在<a href = "javascript:void(0);" style = "color: #568ec8;font-weight: bold;" onclick="SmateCommon.goBack('/dynweb/mobile/dynshow');">返回</a>
                </div>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</body>  
</html>