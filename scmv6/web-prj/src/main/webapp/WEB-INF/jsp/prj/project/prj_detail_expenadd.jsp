<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
  window.onload = function() {
     var parentList = document.getElementsByClassName("New-ChangeNormal_Bodyitem-Box");
     for(var i = 0; i < parentList.length; i++){
         parentList[i].addEventListener("click",function(){
             this.querySelector("input").focus();
         })
     }
  }
  function callbackDate() {
    var dateElement = document.getElementsByClassName("dev_expen_expendate")[0];
    if (dateElement.value == "") {
      if (dateElement.closest(".New-ChangeNormal_Bodyitem-Box").querySelector(".New-ChangeNormal_Bodyitem-Title").classList
          .contains("New-ChangeNormal_Bodyitem-TitleInput")) {
        dateElement.closest(".New-ChangeNormal_Bodyitem-Box").querySelector(".New-ChangeNormal_Bodyitem-Title").classList
            .remove("New-ChangeNormal_Bodyitem-TitleInput");
      }
    } else {
      dateElement.closest(".New-ChangeNormal_Bodyitem-Box").classList.remove("New-ChangeNormal_Bodyitem-Bottom");
      dateElement.closest(".New-ChangeNormal_Bodyitem-Box").querySelector(".New-ChangeNormal_Bodyitem-Title").classList
          .remove("New-ChangeNormal_Bodyitem-TitleColor");
      dateElement.closest(".New-ChangeNormal_Bodyitem-Box").querySelector(".New-ChangeNormal_Bodyitem-Title").classList
          .add("New-ChangeNormal_Bodyitem-TitleInput");
    }
  }
</script>
<!-- 项目经费：记一笔弹框界面  -->
<div class="New-ChangeNormal_Container" id="prj_detail_addexpen_box">
  <div class="New-ChangeNormal_Header">
    <span>项目支出</span> <i class="list-results_close" onclick="NewProject.closeAddexpenBox()"></i>
  </div>
  <div class="New-ChangeNormal_Body">
    <div class="New-ChangeNormal_Bodyitem">
      <div class="New-ChangeNormal_Bodyitem-Box">
        <span class="New-ChangeNormal_Bodyitem-Title">项目名称</span> <input class="New-ChangeNormal_Bodyitem-Input"
          type="text" value="${title }" readonly="readonly"  οnfοcus="this.blur()" unselectable="on">
      </div>
    </div>
    <div class="New-ChangeNormal_Bodyitem">
      <div class="New-ChangeNormal_Bodyitem-Box input__box">
        <span class="New-ChangeNormal_Bodyitem-Title">支出日期</span> <input
          class="New-ChangeNormal_Bodyitem-Input dev_expen_expendate" style="border-bottom: none;" type="text"
          confirmevent="callbackDate" unselectable="on" readonly="" datepicker="" focusdata="" mustdate="dd"
          date-format="yyyy-mm-dd" value="<fmt:formatDate value='${expenDate }' type='both' pattern='yyyy-MM-dd'></fmt:formatDate>">
      </div>
    </div>
    <div class="New-ChangeNormal_Bodyitem">
      <div class="New-ChangeNormal_Bodyitem-Box New-ChangeNormal_Bodyitem-on_select">
        <span class="New-ChangeNormal_Bodyitem-Title">经费科目</span> 
        <input class="New-ChangeNormal_Bodyitem-Input dev_expen_expenitem"  value="${prjExpens[0].expenItem }" type="text"
          expenid="${prjExpens[0].id }" οnfοcus="this.blur()" readonly="readonly"  unselectable="on"> 
          <i class="material-icons New-ChangeNormal_Bodyitem-onKey">arrow_drop_down</i>
        <c:if test="${not empty prjExpens}">
          <div class="New-ChangeNormal_Bodyitem-Select">
            <c:forEach items="${prjExpens}" var="expen" varStatus="stat">
              <div class="New-ChangeNormal_Bodyitem-SelectItem" title="${expen.expenItem }">
                <span expenid="${expen.id }">${expen.expenItem }</span>
              </div>
            </c:forEach>
          </div>
        </c:if>
      </div>
    </div>
    <div class="New-ChangeNormal_Bodyitem">
      <div class="New-ChangeNormal_Bodyitem-Box">
        <span class="New-ChangeNormal_Bodyitem-Title">支出金额(单位：元)</span> <input
          class="New-ChangeNormal_Bodyitem-Input dev_expen_expenamount" maxlength="15" type="text" value="${expenAmount }">
      </div>
    </div>
    <div class="New-ChangeNormal_Bodyitem">
      <div class="New-ChangeNormal_Bodyitem-Box">
        <span class="New-ChangeNormal_Bodyitem-Title">备注</span> <input
          class="New-ChangeNormal_Bodyitem-Input dev_expen_remark" type="text" maxlength="100" value="${remark }">
      </div>
    </div>
    <div class="New-ChangeNormal_Bodyitem">
      <div class="New-ChangeNormal_Bodyitem-mainTitle">相关附件</div>
      <div class="upfile" filetype="file">
        <div class="handin_import-content_container-right_upload-box fileupload__box" maxlength="10" maxclass=""
          onclick="NewProject.fileuploadBoxOpenInputClick(event);" title="上传附件" style="width: 100%;">
          <div class="fileupload__core initial_shown  fileupload__position">
            <div class="fileupload__initial">
              <div class="pubv8-enter_add_file1_avator"></div>
              <div class="pubv8-enter_add_file2_avator"></div>
              <input type="file" class="fileupload__input" style="display: none;" multiple="multiple">
            </div>
            <div class="fileupload__hint-text" title="点击或拖动选取文件">点击或拖动选取文件</div>
            <div class="fileupload__progress">
              <canvas width="56" height="56"></canvas>
              <div class="fileupload__progress_text"></div>
            </div>
            <div class="fileupload__saving">
              <div class="preloader"></div>
              <div class="fileupload__saving-text"></div>
            </div>
            <div class="fileupload__finish"></div>
          </div>
        </div>
      </div>
      <div class="New-ChangeNormal_Bodyitem-Upload_Detail">
        <c:if test="${not empty accessorys}">
          <c:forEach items="${accessorys}" var="accessory" varStatus="stat">
            <div class="New-ChangeNormal_Bodyitem-Upload_Detailitem" accessroyid="${accessory.id }">
              <div class="New-ChangeNormal_Bodyitem-Upload_Detailtext" onclick="NewProject.link('${accessory.downloadUrl }')" des3FileId="${accessory.des3FileId }">${accessory.fileName }</div>
              <i class="material-icons New-ChangeNormal_Bodyitem-Upload_Detailclose"
                onclick="NewProject.closeAccessoy(this);">close</i>
            </div>
          </c:forEach>
        </c:if>
      </div>
    </div>
  </div>
  <div class="New-ChangeNormal_FooterItem">
    <div class="New-ChangeNormal_Footer-Save" onclick="NewProject.savePrjExpenditure('${des3PrjId}',this);">保存</div>
  </div>
  <input type="hidden" id="expen_record_id" value="${expenRecordId}">
  <input type="hidden" id="expen_id" value="${expenId}">
</div>