<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    <meta name="viewport" content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
    <meta charset="utf-8">
    <link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${resmod }/js/jquery.js"></script>
    <script type="text/javascript" src="${resmod}/js/group/mobile/mobile_grp_file.js"></script>
    <script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
    <script type="text/javascript">
        $(function(){
           //页面初始化
           MobileGrpFile.initSharePage($("#m_grp_file_sharePlatform").val(),$("#m_grp_file_des3RecieverIds").val(),"${grpInfo.grpName}");
           window.onresize = function(){
               document.getElementsByClassName("new-mobile_totarget-friend_input").style.height = window.innerHeight - 270 + "px";
               document.getElementsByClassName("new-mobile_totarget-group_input").style.height = window.innerHeight - 220 + "px";
           } 
        });
        function checkmaxlength(){
          var content = $("#msg_textarea").val();
          var textNum = content.length;
          if(textNum > 500){
            $("#msg_textarea").val(content.substring(0,500));
            newMobileTip("最大限制输入500个字符", 1000);
          }
        }
    </script>
</head>
<body>
<form action="/psn/mobile/choosefriend" method="post" id = "m_grp_file_choose_page">
    <input type="hidden" value = "${groupFile.des3ResId }" id = "m_grp_file_des3ResId" name = "des3ResId">
    <input type="hidden" value = "${groupFile.resType }" id = "m_grp_file_resType" name = "resType">
    <input type="hidden" value = "${groupFile.sharePlatform }" id = "m_grp_file_sharePlatform" name = "sharePlatform">
    <input type="hidden" value = "${groupFile.des3RecieverIds }" id = "m_grp_file_des3RecieverIds" name = "des3RecieverIds">
    <input type="hidden" value = "${groupFile.des3GrpIds }" id = "m_grp_file_des3GrpIds" name = "des3GrpIds">
    <input type="hidden" value = "${groupFile.des3GrpId }" id = "m_grp_file_des3GrpId" name = "des3GrpId">
    <input type="hidden" value = "${groupFile.leaveMsg }" id = "m_grp_file_leaveMsg" name = "leaveMsg">
</form>
<c:if test="${not empty grpInfo }">
    <input type="hidden" value = "<iris:des3 code="${grpInfo.grpId }"/>" id = "m_grp_file_choose_grpId">
</c:if>
<div class="paper__func-header">
     <span class="paper__func-header_function-left" onclick="MobileGrpFile.cancelShare()">取消</span>
     <span>文件分享</span>
     <span class="paper__func-header_function-right" id="sharebt" shareto='friend' onclick="MobileGrpFile.shareFile(this)">分享</span>
</div>
<div class="new-mobilegroup_neck" style="top: 48px; background:#fff!important;">
     <div class="new-mobilegroup_neck-list new-mobilegroup_neck-list_selected" onclick="MobileGrpFile.shareTypeChange(this,'friend')"><span>分享给联系人</span></div>
     <div class="new-mobilegroup_neck-list" onclick="MobileGrpFile.shareTypeChange(this,'grp')"><span>分享给群组</span></div>
</div>
<div class="new-mobile_totarget-friend" id = "m_grp_file_share_friend">
    <div class="new-mobile_totarget-friend_header">
         <div class="new-mobile_totarget-friend_header-title">你可能想分享给:</div>
         <div class="new-mobile_totarget-friend_header-box">
            <div class="new-mobile_totarget-friend_item" id = "friend_cancel_choose">
                <c:forEach items="${psnInfos }" var="psnInfo">
                    <div class="new-mobile_totarget-friend_list" select_option = "friend_${psnInfo.person.personDes3Id }" onclick="MobileGrpFile.addChoose(this,'${psnInfo.person.personDes3Id }','${psnInfo.person.name }','friend')">
                       <i class="material-icons new-mobile_totarget-friend_list-icon">add</i>
                       <span class="new-mobile_totarget-friend_list-detail friend_${psnInfo.person.personDes3Id }">${psnInfo.person.name }</span>
                    </div>
                </c:forEach>
            </div>
         </div>
    </div>
    <div class="new-mobile_totarget-friend_choice">
         <div class="new-mobile_totarget-friend_choice-title">分享给:</div>
         <div class="new-mobile_totarget-friend_choice-box" id = "friend_choose">
         </div>
         <div class="new-mobile_totarget-friend_btn" onclick="MobileGrpFile.loadChoosePage('friend')">选择好友</div>
    </div>
    <div class="new-mobile_totarget-friend_input" style="height: 50%; width: 90%;left: 9px; overflow-x: hidden; overflow-y: auto;">
         <textarea id="msg_textarea" oninput="checkmaxlength()" placeholder="分享留言" ></textarea>
    </div>

</div>
<div class="new-mobile_totarget-group" id = "m_grp_file_share_grp"  style="display: none;">
    <div class="new-mobile_totarget-header">
         <div class="new-mobile_totarget-header_title">
                    分享给:
         </div>
         <div class="new-mobile_totarget-group_box" id = "grp_choose" style="height: 28px;">
         </div>
         <div class="new-mobile_totarget-group_btn" onclick="MobileGrpFile.loadChoosePage('grp')">选择群组</div>
    </div>
    <div class="new-mobile_totarget-group_input">
         <textarea placeholder="分享留言"></textarea>
    </div>
</div>

<div class="new-mobilefile_share-box">
     <div class="new-mobilefile_share-item">
        <div class="new-mobilefile_share-item_avator">
            <c:choose>
                <c:when test="${third eq 'txt' }">
                    <img src="${resmod}/smate-pc/img/fileicon_txt.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_txt.png'">
                </c:when>
                <c:when test="${third eq 'ppt' or third eq 'pptx'}">
                    <img src="${resmod}/smate-pc/img/fileicon_ppt.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_ppt.png'">
                </c:when>
                <c:when test="${third eq 'doc' or third eq 'docx'}">
                    <img src="${resmod}/smate-pc/img/fileicon_doc.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_doc.png'">
                </c:when>
                <c:when test="${third eq 'rar' or third eq 'zip'}">
                    <img src="${resmod}/smate-pc/img/fileicon_zip.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_zip.png'">
                </c:when>
                <c:when test="${third eq 'xls' or third eq 'xlsx'}">
                    <img src="${resmod}/smate-pc/img/fileicon_xls.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_xls.png'">
                </c:when>
                <c:when test="${third eq 'pdf'}">
                    <img src="${resmod}/smate-pc/img/fileicon_xls.png"
                  onerror="this.src='${resmod}/smate-pc/img/fileicon_xls.png'">
                </c:when>
                <c:otherwise>
                    <img src="${fourth}" onerror="this.src='${resmod}/smate-pc/img/fileicon_default.png'">
                </c:otherwise>
              </c:choose>
        </div>
        <div class="new-mobilefile_share-item_content">
           <div class="new-mobilefile_share-item_title">
                <a>${first }</a>
           </div>
           <div class="new-mobilefile_share-item_source">
                ${second }
           </div>
        </div>
     </div>
</div>

</body>
</html>