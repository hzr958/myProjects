<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<script type="text/javascript">
  $(function() {
    //添加至少3个作者
    var start = 3;
    if (!$(".json_member")) {
      start = 0;
    } else if ($(".json_member").length < 3) {
      start = $(".json_member").length;
    } else {
      addmenber();
    }
    for (start; start < 3; start++) {
      addmenber();
    }

    PubEdit.addMenberInputOn(); //自动加行,输入控制
    $(".json_member").each(function() {
      PubEdit.addMenberInputBlur(this);
      PubEdit.addMenberInputOnChange(this);
    });
    window.addFormElementsEvents($(".dev_member")[0]);
    var inputlist = document.getElementsByClassName("dev-detailinput_container");
    for (var i = 0; i < inputlist.length; i++) {
      inputlist[i].onfocus = function() {
        if (this.closest(".handin_import-content_container-right_input")) {
          this.closest(".handin_import-content_container-right_input").style.border = "1px solid #2882d8";
        }
        if (this.closest(".handin_import-content_rightbox-border")) {
          this.closest(".handin_import-content_rightbox-border").style.border = "1px solid #2882d8";
        }
        if (this.closest(".handin_import-content_container-right_Citation-item")) {
          this.closest(".handin_import-content_container-right_Citation-item").style.border = "1px solid #2882d8";
        }
      }
      inputlist[i].onblur = function() {
        if (this.closest(".handin_import-content_container-right_input")) {
          this.closest(".handin_import-content_container-right_input").style.border = "1px solid #ddd";
        }
        if (this.closest(".handin_import-content_rightbox-border")) {
          this.closest(".handin_import-content_rightbox-border").style.border = "1px solid #ddd";
        }
        if (this.closest(".handin_import-content_container-right_Citation-item")) {
          this.closest(".handin_import-content_container-right_Citation-item").style.border = "1px solid #ddd";
        }
      }
    }
    PubEdit.titleChange();
  });
  function callbackName(obj, otherStr) {
    if (otherStr && otherStr != "") {
      var other = JSON.parse(otherStr);
      var $parent = $(obj).closest(".json_member");
      $parent.find(".json_member_insNames").val(other.insName);
      $parent.find(".json_member_insNames").attr("code", other.insId);
      $parent.find(".json_member_email").val(other.email);
      $parent.find(".json_member_owner").val(other.owner);
      // 遍历是否存在本人，存在则不进行标记为本人，不存在则标记
      var existsOwner = false;
      $(".dev_menber_i").each(function(index, element) {
        if ($(element).hasClass("selected-oneself_confirm")) {
          existsOwner = true;
        }
      });
      if (other.owner && !existsOwner) {
        $parent.find(".dev_menber_i").removeClass("selected-oneself");
        $parent.find(".dev_menber_i").addClass("selected-oneself_confirm");
      } else {

        $parent.find(".json_member_owner").val("false");
      }
      $parent.find(".json_member_des3PsnId").val(other.des3PsnId);
    }
  };

  function insNameChange(obj) {
    $(obj).attr("code", "");
  };
  function callbackInsName() {
  };

  function getExitPubId() {
    var ids = "";
    $(".dev_member").find(".json_member").each(function() {
      var des3PsnId = $(this).find(".json_member_des3PsnId").val();
      if (des3PsnId != "") {
        ids += "," + des3PsnId;
      }
    });
    if (ids != "") {
      ids = ids.substring(1);
    };
    return {
      "des3PsnId" : ids
    };
  };
  function authorNameChange(obj) {
    $(obj).parent().find(".json_member_des3PsnId").val("");
  };
</script>
<%-- <%@ include file="/common/taglibs.jsp"%> --%>
<!-- 摘要 begin -->
<div class="handin_import-content_container-start">
  <div class="handin_import-content_container-left">
    <span style="color: red;"></span><span><spring:message code="pub.enter.summary" /></span>
  </div>
  <div class="handin_import-content_container-right"
    style="display: flex; flex-direction: column; align-items: flex-start;">
    <div class="handin_import-outborder">
      <textarea maxlength="20000" class="handin_import-content_container-right_input json_summary"
        style="min-height: 81px; border: none; width: 100%; margin: 4px;" name="summary">${pubVo.summary }</textarea>
    </div>
    <div class="json_summary_msg" style="display: none"></div>
  </div>
</div>
<!-- 摘要 end -->
<div class="handin_import-content_container-center">
  <!-- 科技领域  begin -->
  <div class="handin_import-content_container-left">
    <span style="color: red;"></span>
      <span><spring:message code="pub.enter.scienceArea" /></span>
  </div>
  <div class="handin_import-content_container-right_area"
    style="border: none; flex-direction: column; align-items: flex-start; height: auto; margin-left: 12px;">
    <div class="handin_import-content_container-right_area-content handin_import-content_rightbox-border"
      onclick="PubEdit.showScienceAreaBox('1')">
      <div class="handin_import-content_container-right_area-content json_scienceArea" name="scienceAreaName"
        style="display: flex; align-items: center;">
        <input type="text" id="scienceAreaId" name="scienceAreaId" onfocus="this.blur()" readonly unselectable="on"
          disabled="disabled"
          value="${pubVo.scienceAreas[0].scienceAreaName}${not empty pubVo.scienceAreas[1].scienceAreaName ? ', ':''}${pubVo.scienceAreas[1].scienceAreaName}"
          style="margin-left: 10px; background: #fff;"> <input type="hidden"
          class="json_scienceArea_scienceAreaId" value="${pubVo.scienceAreas[0].scienceAreaId }"> <input
          type="hidden" class="json_scienceArea_scienceAreaId" value="${pubVo.scienceAreas[1].scienceAreaId }">
      </div>
      <i class="selected-func_field"></i>
    </div>
    <div class="json_scienceArea_scienceAreaId_msg" style="display: none"></div>
  </div>
  <!-- 科技领域  end -->
  <!-- 行业  begin -->
  <c:if test="${pubVo.pubType == 12 || pubVo.pubType == 13}">
    <div class="handin_import-content_container-left" style="width: 105px;">
      <span style="color: red;"></span><span><spring:message code="pub.enter.industry" /></span>
    </div>
    <div class="handin_import-content_container-right_area"
      style="border: none; flex-direction: column; align-items: flex-start; height: auto; margin-left: 12px;">
      <div class="handin_import-content_container-right_area-content handin_import-content_rightbox-border"
        id="industryBox"  data-url="/pub/industry/ajaxselect" 
        codes = "${pubVo.industrys[0].industryCode},${pubVo.industrys[1].industryCode}" >
        <div class="handin_import-content_container-right_area-content json_industry"
          style="display: flex; align-items: center;">
          <input type="text" id="industryName" class="json_industry-selectbox" name="industryName" onfocus="this.blur()" readonly unselectable="on"
            disabled="disabled" value="${pubVo.industrys[0].industryName}${not empty pubVo.industrys[1].industryName ? ', ':''}${pubVo.industrys[1].industryName}"
            title ="${pubVo.industrys[0].industryName}${not empty pubVo.industrys[1].industryName ? ', ':''}${pubVo.industrys[1].industryName}" 
            >
          <input type="hidden" class="json_industry_industryCode" value="${pubVo.industrys[0].industryCode}">
          <input type="hidden" class="json_industry_industryCode" value="${pubVo.industrys[1].industryCode}">
        </div>
        <i class="selected-func_field"></i>
      </div>
      <div class="json_industry_industryCode_msg" style="display: none"></div>
    </div>
  </c:if>
  <!-- 行业  end -->
</div>
<!-- 关键词  begin -->
<div class="handin_import-content_container-center" style="align-items: flex-start;">
  <div class="handin_import-content_container-left" style="margin-top: 6px;">
    <span><spring:message code="pub.enter.keyWords" /></span>
  </div>
  <div class="handin_import-content_container-right">
    <div class="handin_import-content_container-right_input"
      style="justify-content: center; min-height: 28px; height: auto;">
      <div class="new-importantkey_container" style="width: 98%; margin-left: -15px; min-height: 28px; height: auto;">
        <c:forEach var="key" items="${pubVo.keyWordsList}">
          <div class="new-importantkey_container-item">
            <div class="new-importantkey_container-item_detaile json_keyword" title="${key}">${key}</div>
            <div class="new-importantkey_container-item_colse">
              <i class="material-icons" onclick="PubEdit.deleteKeyElement(this)">close</i>
            </div>
          </div>
        </c:forEach>
        <c:if test="${fn:length(pubVo.keyWordsList) < 5}">
          <input type="text" id="addKeyInput" maxlength="80"
            class="new-importantkey_container-input  dev-detailinput_container"
            style="width: 10% !important; height: 28px; margin-left: 0px;" autocomplete="off"
            placeholder='<spring:message code="pub.enter.srkword"/>' onfouce="PubEdit.keyCodefun(obj)">
        </c:if>
        <c:if test="${fn:length(pubVo.keyWordsList) >= 5}">
          <input type="text" id="addKeyInput" maxlength="80" autocomplete="off"
            class="new-importantkey_container-input  dev-detailinput_container"
            style="width: 10% !important; display: none; height: 28px;"
            placeholder='<spring:message code="pub.enter.srkword"/>' onfouce="PubEdit.keyCodefun(obj)">
        </c:if>
      </div>
    </div>
    <div class="json_keyword_msg" style="display: none"></div>
  </div>
</div>
<!-- 关键词  end -->
<!-- 附件  begin -->
<div class="handin_import-content_container-start">
  <div class="handin_import-content_container-left">
    <span><spring:message code="pub.enter.accessorys" /></span>
  </div>
  <!-- 上传全文 start -->
  <div class="handin_import-content_container-right"
    style="display: flex; justify-content: flex-start; align-items: flex-start;">
    <div class="upfile" fileType="file">
      <div class="handin_import-content_container-right_upload-box fileupload__box" maxlength="10"
        maxclass="json_accessory" onclick="PubEdit.fileuploadBoxOpenInputClick(event);"
        title="<spring:message code="pub.enter.upfileMsg"/>">
        <div class="fileupload__core initial_shown  fileupload__position">
          <div class="fileupload__initial">
            <div class="pubv8-enter_add_file1_avator"></div>
            <div class="pubv8-enter_add_file2_avator"></div>
            <input type="file" class="fileupload__input" style='display: none;'>
          </div>
          <div class="fileupload__hint-text">
            <spring:message code="pub.enter.accessorysMsg" />
          </div>
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
    <div class="handin_import-content_container-right_upload-container dev_upfiled_list" style="height: 145px;">
      <c:forEach items="${pubVo.accessorys}" var="file">
        <div class="handin_import-content_container-right_upload-item json_accessory">
          <span class="handin_import-content_container-right_upload-item_detaile"> <a title="${file.fileName }"
            onclick="PubEdit.link('${file.fileUrl}',event);">${file.fileName }</a></span> <i
            class="material-icons handin_import-content_upload-item_icon" onclick="PubEdit.deleteFileItem(this)">close</i>
          <input type="hidden" class="json_accessory_fileName" value="${file.fileName }"> <input type="hidden"
            class="json_accessory_des3fileId" value="${file.des3fileId }"> <i
            class='selected-func_close <c:if test="${file.permission=='0' }">selected-func_close-open</c:if>'
            onclick="PubEdit.changAccessorysPermission(this)"
            title='<c:if test="${file.permission=='0' }"><spring:message code="pub.permission.public" /></c:if><c:if test="${file.permission!='0' }"><spring:message code="pub.permission.onlyMe" /></c:if>'></i>
          <input type="hidden" class="json_accessory_permission" value="${file.permission }">
        </div>
      </c:forEach>
    </div>
  </div>
  <!-- 上传全文 start -->
</div>
<!-- 附件  end -->
<!-- 全文链接  begin -->
<div class="handin_import-content_container-center" style="align-items: flex-start;">
  <div class="handin_import-content_container-left" style="margin-top: 4px;">
    <span><spring:message code="pub.enter.fulltextUrl" /></span>
  </div>
  <div class="handin_import-content_container-right">
    <div class="handin_import-content_container-right_input">
      <c:if test="${not empty pubVo.srcFulltextUrl}">
        <input type="text" class="dev-detailinput_container full_width json_srcFulltextUrl" maxlength="1000"
          name="srcFulltextUrl" value="${pubVo.srcFulltextUrl }" />
      </c:if>
      <c:if test="${empty pubVo.srcFulltextUrl}">
        <input type="text" class="dev-detailinput_container full_width json_srcFulltextUrl" maxlength="1000"
          name="srcFulltextUrl" value="http://" />
      </c:if>
    </div>
    <div class="json_srcFulltextUrl_msg" style="display: none"></div>
  </div>
</div>
<!-- 全文链接  end -->
<!-- 作者  begin -->
<div class="handin_import-content_container-start dev_member" style="flex-direction: column;">
  <div class="handin_import-content_container-start_neck">
    <div class="handin_import-content_container-left" style="margin-top: 4px; max-width: 20%; min-width: 14%; width: auto;">
      <span class="handin_import-content_container-tip">*</span>
      <c:if test="${pubVo.pubType == 5}">
        <span style="font-size: 16px;"><spring:message code="patent.member.msg" /></span>
      </c:if>
      <c:if test="${pubVo.pubType == 12}">
        <span style="font-size: 16px;"><spring:message code="standard.member.msg" /></span>
      </c:if>
      <c:if test="${pubVo.pubType == 13}">
        <span style="font-size: 16px;"><spring:message code="softwarecopyright.member.msg" /></span>
      </c:if>
      <c:if test="${pubVo.pubType != 5 && pubVo.pubType != 12 && pubVo.pubType != 13}">
        <span style="font-size: 16px;"><spring:message code="pub.enter.author" /></span>
      </c:if>
    </div>
    <div class="handin_import-content_container-start_neck-detail" style="justify-content: space-between;">
      <div class="json_member_name_msg json_member_email_msg" style="float: left; visibility: hidden;"></div>
      <input type="hidden" name="checkAuthor" value="true">
      <!--           <div class="handin_import-content_container-right_author-addbtn"><i class="handin_import-content_container-right_author-add"></i><spring:message code="pub.enter.authorAdd"/></div>
 -->
    </div>
  </div>
  <div style="width: 100%; display: flex;">
    <div class="handin_import-content_container-left" style="margin-top: 4px; width: 7%;">
      <span style="color: red;"></span><span></span>
    </div>
    <div class="handin_import-content_container-right handin_import-content_container-right_infor">
      <div class="handin_import-content_container-right_author-title">
        <div class="handin_import-content_container-right_author-oneself">
          <spring:message code="pub.enter.ismyselft" />
        </div>
        <div class="handin_import-content_container-right_author-name">
          <spring:message code="pub.enter.menberName" />
        </div>
        <div class="handin_import-content_container-right_author-work">
          <spring:message code="pub.enter.menberIns" />
        </div>
        <div class="handin_import-content_container-right_author-email">
          <spring:message code="pub.enter.menberEnail" />
        </div>
        <div class="handin_import-content_container-right_author-Communication">
          <spring:message code="pub.enter.menbertell" />
        </div>
        <div class="handin_import-content_container-right_author-edit">
          <spring:message code="pub.enter.deal" />
        </div>
      </div>
      <c:forEach items="${pubVo.members }" var="member">
        <div class="handin_import-content_container-right_author-body json_member">
          <input type="hidden" class="json_member_des3PsnId" value="${member.des3PsnId }">
          <div class="handin_import-content_container-right_author-body_oneself">
            <c:if test="${member.owner }">
              <i class="selected-oneself_confirm dev_menber_i"></i>
            </c:if>
            <c:if test="${!member.owner}">
              <i class="selected-oneself dev_menber_i"></i>
            </c:if>
            <input type="hidden" class="json_member_owner" value="${member.owner }">
          </div>
          <div class="handin_import-content_container-right_author-body_name">
            <span class="handin_import-content_container-right_author-body_item error_import-tip_border"> <input
              type="text" style="width: 85% !important;" maxlength="61" autocomplete="off"
              class="dev-detailinput_container full_width json_member_name js_autocompletebox"
              onchange="authorNameChange(this)" other-event="callbackName" data-src="request"
              request-url="/psnweb/ac/ajaxpsncooperator" request-data="getExitPubId();" value="${member.name }" />
            </span>
          </div>
          <div class="handin_import-content_container-right_author-body_work">
            <span class="handin_import-content_container-right_author-body_item"> <input type="text"
              autocomplete="off" maxlength="100"
              class="dev-detailinput_container full_width json_member_insNames js_autocompletebox"
              onchange="insNameChange(this);" itemevent="callbackInsName" code="${member.insNames[0].insId}"
              request-url="/psnweb/ac/ajaxautoinstitution" value="${member.insNames[0].insName }" />
            </span>
          </div>
          <div class="handin_import-content_container-right_author-body_email ">
            <span class="handin_import-content_container-right_author-body_item error_import-tip_border"><input
              type="text" maxlength="50" class="dev-detailinput_container full_width json_member_email"
              autocomplete="off" value="${member.email }" /></span>
          </div>
          <div class="handin_import-content_container-right_author-body_Communication">
            <c:if test="${member.communicable }">
              <i class="selected-author_confirm dev_communicale"></i>
            </c:if>
            <c:if test="${!member.communicable}">
              <i class="selected-author dev_communicale"></i>
            </c:if>
            <input type="hidden" class="json_member_communicable" name="communicable" value="${member.communicable }">
          </div>
          <div class="handin_import-content_container-right_author-body_edit">
            <i class="selected-func_delete" onclick="PubEdit.deletetarget(this)"></i> <i class="selected-func_down"
              onclick="PubEdit.downchange(this)"></i> <i class="selected-func_up" onclick="PubEdit.upchange(this);"></i>
          </div>
        </div>
      </c:forEach>
    </div>
  </div>
</div>
<!-- 作者  end -->
<!-- 保存和取消按钮 begin -->
<div class="handin_import-content_container-center" style="margin: 32px;">
  <div class="handin_import-content_container-left">
    <span></span>
  </div>
  <div class="handin_import-content_container-right" style="display: flex; align-items: center; margin-right: 24px;">
    <div class="handin_import-content_container-save" onclick="PubEdit.formValid();PubEdit.checkRepeat(this);">
      <spring:message code="pub.enter.savePub" />
    </div>
    <div class="handin_import-content_container-cancle" onclick="PubEdit.viewPubs();">
      <spring:message code="pub.enter.pubBack" />
    </div>
  </div>
</div>
<!-- 保存和取消按钮 end -->
<!-- <div class="dialogs__box" dialog-id="scienceAreaBox" style="width: 720px;" cover-event="" id="scienceAreaBox" process-time="0">
</div> -->
<div class="background-cover" id="resultMsg" style="display: none;">
  <div class="new-success_save" id="new-success_save">
    <div class="new-success_save-body">
      <div class="new-success_save-body_avator">
        <img id="show_img" src="${resmod}/smate-pc/img/pass.png"> <input type="hidden" id="successImg"
          value="${resmod}/smate-pc/img/pass.png" /> <input type="hidden" id="errorImg"
          value="${resmod}/smate-pc/img/fail.png" />
      </div>
      <div class="new-success_save-body_tip">
        <span id="show_msg">保存成功</span> <input type="hidden" id="save_error_msg" value="保存成果失败！" /> <input
          type="hidden" id="save_success_msg" value="保存成功" /> <input type="hidden" id="change_msg"
          value='<spring:message code="pub.label.prompt.switchType"/>' />
      </div>
      <div class="new-success_save-body_footer" id="saveBotten">
        <div id="returnBottom" class="new-success_save-body_footer-complete" isEdit="true"
          onclick="PubEdit.viewPubs(this);">返回列表</div>
        <div id="continueBottom" class="new-success_save-body_footer-continue" onclick="PubEdit.continueEdit();">继续编辑</div>
      </div>
      <input type="hidden" id="changTypeName" value="" />
    </div>
  </div>
</div>
<input type="hidden" id="dup_box_flag" value="0" />
<!-- 成果查重结果弹出框 -->
<div class="bckground-cover dev_duppub_box_bg" id="dev_duppub_box" style="display: none;"></div>
<!-- 成果查重结果弹出框 -->
<!-- 确认导入成果其他数据弹出框 -->
<div class="background-cover cover_colored" id="autoFillPub" style="display: none; z-index: 200;">
  <div class="auto_Fill_Pub" id="auto_Fill_Pub">
    <div class="auto_Fill_Pub-header">
      <span class="auto_Fill_Pub-header_content"><spring:message code="pub.label.prompt.notice" /></span> <i
        class="auto_Fill_Pub-header_close new-normal_close-tip" onclick="PubEdit.cancleAutoFillPub();"></i>
    </div>
    <div class="auto_Fill_Pub-body">
      <div class="auto_Fill_Pub-body_name">
        <i class="new-searchplugin_container-body_tipicon" style="padding-right: 6px; margin-bottom: 0px;"></i> <span
          class="auto_Fill_Pub-body_name-tip_content"><spring:message code="pub.label.prompt.autoFill" /></span>
      </div>
    </div>
    <div class="auto_Fill_Pub-footer">
      <div class="auto_Fill_Pub-footer_confirm" style="cursor: pointer" onclick="PubEdit.cancleAutoFillPub();">
        <spring:message code="pub.addJournal.button.cancel" />
      </div>
      <div class="auto_Fill_Pub-footer_cancle" style="cursor: pointer" onclick="PubEdit.confirmAutoFillPub();">
        <spring:message code="pub.addJournal.button.save" />
      </div>
    </div>
  </div>
</div>
