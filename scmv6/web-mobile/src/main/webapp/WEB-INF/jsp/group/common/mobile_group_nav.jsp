<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function openMenber(){
  var outside = '${isLogin}' == 'false' ? "/outside" : "";
  window.history.replaceState({}, "", "https://" + document.domain + "/grp"+outside+"/member/main?des3GrpId="+encodeURIComponent('${des3GrpId}'));
  window.location.reload();
}
function openGrpFile(){
  var outside = '${isLogin}' == 'false' ? "/outside" : "";
  window.history.replaceState({}, "", "https://" + document.domain + "/grp"+outside+"/main/grpfilemain?des3GrpId=" + encodeURIComponent('${des3GrpId}'));
  window.location.reload();
}
function openCourseFile(){
  var outside = '${isLogin}' == 'false' ? "/outside" : "";
  window.history.replaceState({}, "", "https://" + document.domain + "/grp"+outside+"/main/grpfilemain?des3GrpId=" + encodeURIComponent('${des3GrpId}') + "&courseFileType=2");
  window.location.reload();
}
function openWorkFile(){
  var outside = '${isLogin}' == 'false' ? "/outside" : "";
  window.history.replaceState({}, "", "https://" + document.domain + "/grp"+outside+"/main/grpfilemain?des3GrpId=" + encodeURIComponent('${des3GrpId}') + "&workFileType=1");
  window.location.reload();
}
function openPrjPub(){
  var outside = '${isLogin}' == 'false' ? "/outside" : "";
  window.history.replaceState({}, "", "https://" + document.domain + "/grp"+outside+"/mobile/grppubmain?showPrjPub=1&des3GrpId="+encodeURIComponent('${des3GrpId}'));
  window.location.reload();
}
function openRefPub(){
  var outside = '${isLogin}' == 'false' ? "/outside" : "";
  window.history.replaceState({}, "", "https://" + document.domain + "/grp"+outside+"/mobile/grppubmain?showRefPub=1&des3GrpId="+encodeURIComponent('${des3GrpId}'));
  window.location.reload();
}
function openGrpDetail(){
  var outside = '${isLogin}' == 'false' ? "/outside" : "";
  window.history.replaceState({}, "", "https://" + document.domain + "/grp"+outside+"/main?des3GrpId="+encodeURIComponent('${des3GrpId}'));
  window.location.reload();
}
function changeSelectStyle(obj){
  $(".new-mobilefund_footer-item").removeClass("new-mobilepage_footer-item_tip-selected"); 
  $(obj).addClass("new-mobilepage_footer-item_tip-selected");
}
</script>
<div class="new-mobilepage_footer-container mobile_menu" style="z-index: 99;">
  <c:if test="${psnRole==9 }">
      <c:if test="${grpControl.isIndexDiscussOpen==1 }">
          <div class="new-mobilefund_footer-item" id="grpDyn" onclick="openGrpDetail();" style=" width: 20vw;">
            <i class="new-mobilepage_footer-item_tip-firstpage"></i>
            <div class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">首页</div>
          </div>
      </c:if>
      <c:if test="${grpControl.isIndexMemberOpen==1 }">
       <div class="new-mobilepage_footer-item" id="grpMenber" onclick="openMenber();" style="position: relative; width: 20vw;">
        <i class="new-mobilepage_footer-item_tip-user"></i>
        <span class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">成员</span>
       </div>
      </c:if>
       <!--  课程群组 成员，课件，作业，文献  -->       
       <c:if test="${grpCategory==10 }">
          <c:if test="${grpControl.isCurwareFileShow==1 }">
             <div class="new-mobilefund_footer-item" id="grpCourseFile" onclick="openCourseFile();" style="position: relative;  width: 20vw;">
                <i class="new-mobilepage_footer-item_tip-Courseware"></i>
                <div class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">课件</div>
             </div>
          </c:if>
          <c:if test="${grpControl.isWorkFileShow==1 }">
             <div class="new-mobilefund_footer-item" id="grpWorkFile" onclick="openWorkFile();" style="position: relative;  width: 20vw;">
                <i class="new-mobilepage_footer-item_tip-task"></i>
                <div class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">作业</div>
              </div>
          </c:if>
          <c:if test="${grpControl.isIndexPubOpen==1 }"> 
             <div class="new-mobilefund_footer-item" id="grpPub" onclick="openPrjPub();" style="position: relative;  width: 20vw;">
                <i class="new-mobilepage_footer-item_tip-literature"></i>
                <div class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">文献</div>
              </div>
          </c:if>   
       </c:if>
       <!--  项目群组 成员，成果，文献，文件   -->       
       <c:if test="${grpCategory==11 }">
          <c:if test="${grpControl.isPrjPubShow==1 }">
             <div class="new-mobilepage_footer-item" id="grpPubMenu" onclick="openPrjPub();" style="position: relative;  width: 20vw;">
                <i class="new-mobilepage_footer-item_tip-Achievements"></i>
                <span class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">成果</span>
              </div>
          </c:if>
          <c:if test="${grpControl.isPrjRefShow==1 }">
              <div class="new-mobilefund_footer-item" id="grpRefMenu" onclick="openRefPub();" style="position: relative;  width: 20vw;">
                <i class="new-mobilepage_footer-item_tip-literature"></i>
                <div class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">文献</div>
              </div>
          </c:if>
          <c:if test="${grpControl.isIndexFileOpen==1 }">
             <div class="new-mobilefund_footer-item" id="grpFile" onclick="openGrpFile();" style="position: relative;  width: 20vw;">
                <i class="new-mobilepage_footer-item_tip-file"></i>
                <div class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">文件</div>
              </div            
          </c:if>
       </c:if>
       <!--  兴趣群组 成员，成果，文件   -->       
       <c:if test="${grpCategory==12 }">
          <c:if test="${grpControl.isIndexPubOpen==1 }">
            <div class="new-mobilepage_footer-item" id="grpPub" onclick="openPrjPub();" style="position: relative;  width: 20vw;">
              <i class="new-mobilepage_footer-item_tip-Achievements"></i>
              <span class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">成果</span>
            </div>
          </c:if>
          <c:if test="${grpControl.isIndexFileOpen==1 }">
             <div class="new-mobilefund_footer-item" id="grpFile" onclick="openGrpFile();" style="position: relative;  width: 20vw;">
                <i class="new-mobilepage_footer-item_tip-file"></i>
                <div class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">文件</div>
              </div 
          </c:if>
       </c:if>
  </c:if>
  <c:if test="${psnRole!=9 }">
   <div class="new-mobilefund_footer-item" id="grpDyn" onclick="openGrpDetail();" style=" width: 20vw;">
      <i class="new-mobilepage_footer-item_tip-firstpage"></i>
      <div class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">首页</div>
   </div>
   <div class="new-mobilepage_footer-item" id="grpMenber" onclick="openMenber();" style="position: relative; width: 20vw;">
    <i class="new-mobilepage_footer-item_tip-user"></i>
    <span class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">成员</span>
    <c:if test="${proposerCount>0}">
         <div class="new-mobilepage_footer-item_num" id="grp_apply_member_count"  style="color: #fff!important;">${proposerCount}</div>
    </c:if>
   </div>
   <!--  课程群组 成员，课件，作业，文献  -->       
   <c:if test="${grpCategory==10 }">
       <div class="new-mobilefund_footer-item" id="grpCourseFile" onclick="openCourseFile();" style="position: relative;  width: 20vw;">
          <i class="new-mobilepage_footer-item_tip-Courseware"></i>
          <div class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">课件</div>
       </div>

       <div class="new-mobilefund_footer-item" id="grpWorkFile" onclick="openWorkFile();" style="position: relative;  width: 20vw;">
          <i class="new-mobilepage_footer-item_tip-task"></i>
          <div class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">作业</div>
        </div>

       <div class="new-mobilefund_footer-item" id="grpPub" onclick="openPrjPub();" style="position: relative;  width: 20vw;">
          <i class="new-mobilepage_footer-item_tip-literature"></i>
          <div class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">文献</div>
        </div>
   </c:if>
   <!--  项目群组 成员，成果，文献，文件   -->       
   <c:if test="${grpCategory==11 }">
       <div class="new-mobilepage_footer-item" id="grpPubMenu" onclick="openPrjPub();" style="position: relative;  width: 20vw;">
          <i class="new-mobilepage_footer-item_tip-Achievements"></i>
          <span class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">成果</span>
        </div>

        <div class="new-mobilefund_footer-item" id="grpRefMenu" onclick="openRefPub();" style="position: relative;  width: 20vw;">
          <i class="new-mobilepage_footer-item_tip-literature"></i>
          <div class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">文献</div>
        </div>

       <div class="new-mobilefund_footer-item" id="grpFile" onclick="openGrpFile();" style="position: relative;  width: 20vw;">
          <i class="new-mobilepage_footer-item_tip-file"></i>
          <div class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">文件</div>
        </div            
   </c:if>
   <!--  兴趣群组 成员，成果，文件   -->       
   <c:if test="${grpCategory==12 }">
        <div class="new-mobilepage_footer-item" id="grpPub" onclick="openPrjPub();" style="position: relative;  width: 20vw;">
          <i class="new-mobilepage_footer-item_tip-Achievements"></i>
          <span class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">成果</span>
        </div>

         <div class="new-mobilefund_footer-item" id="grpFile" onclick="openGrpFile();" style="position: relative;  width: 20vw;">
            <i class="new-mobilepage_footer-item_tip-file"></i>
            <div class="new-mobilepage_footer-item_title" style="color: rgb(102, 102, 102);">文件</div>
         </div 
   </c:if>
  </c:if>

</div>