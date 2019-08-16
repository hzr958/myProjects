<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
  $("[name = 'importPubListSelectedConfirm']").click(function(){
  var $this = $(this);
  if($this.hasClass("selected-author_confirm")){
      $this.removeClass("selected-author_confirm");
  }else{
     $this.addClass("selected-author_confirm");
  }
  //TODO 控制全选按钮的选中和取消选中状态
  });
  //关闭导入弹框
  function closeImportPubBox() {
    var importStatus = $("#import_status").val();
    if(importStatus && importStatus == 'true'){
      $("#showList").hide();
      $("#selected_to_import_num").html("0");
      //删除缓存的待导入成果
      $.post("/pub/cache/ajaxremove", {
        "cacheKey" : $("#cache_key").val()
      }, function(data) {
      }, "json");
    }
  }
  //切换操作
  function changeImportOperation(obj, index) {
    var optVal = $(obj).find("option:selected").val();
    var addTips = "<spring:message code='referencelist.label.notduplicated' />";
    var skipTips = "<spring:message code='referencesearch.pubList.tip.skip' />";
    var tips = "<spring:message code='referencesearch.pubList.tips' />";
    if (optVal == "skip") {
      tips = skipTips;
    } else if (optVal == "add") {
      tips = addTips;
    }
    $("#import_tips_" + index).html(tips);
  }

  //保存待导入成果
  function importSelectedPub(obj) {
    // 导入之前先将导入的状态位置为false，成功之后才置为true
    $("#import_status").val("false");
    var click = $(obj).attr("onclick");
    BaseUtils.unDisable(obj);
    $("#loadingImage").attr("src", "/resmod/smate-pc/img/upload.gif");//显示导入时的加载中图片
    var pubJsonList = new Array();
    $("#pengding_import_pub_list_div .pending_import_pub_item").each(function() {
      var pubJsonParams = {};
      var $this = $(this);
      //var selected = $this.find(".pedding_import_pub").hasClass("selected-author_confirm");
      //var needSave = 0; //0：未选中不保存， 1：选中了要保存
      //if(selected){
      //needSave = 1;
      //成果类型
      var pubType = $this.find("select.pub_type_options").val();
      pubType = BaseUtils.checkIsNull(pubType) ? 4 : pubType
      pubJsonParams["pub_type"] = pubType;
      //收录情况
      var situationInfo = new Array();
      $this.find(".pub_situation_item").each(function() {
        $situation = $(this);
        if ($situation.hasClass("selected-author_confirm")) {
          situationInfo.push($situation.attr("value"));
        }
      });
      pubJsonParams["situation_info"] = situationInfo.join(",");
      //是否有重复成果和对重复成果选择的操作
      var des3DupPubId = $this.find("input.des3DupPubId").val();
      if (!BaseUtils.checkIsNull(des3DupPubId)) {
        pubJsonParams["dup_des3_pub_id"] = des3DupPubId;
        pubJsonParams["dup_opt"] = $this.find("select.dup_pub_opt").val();
      }
      //}
      //是否选中了要保存起来
      //pubJsonParams["pub_save"] = needSave;
      //pubJsonParams["pub_json"] = $this.find("input.pubJsonData").val();
      pubJsonList.push(pubJsonParams);
    });
    //发送导入保存请求
    $.ajax({
      url : "/pub/import/ajaxsave",
      type : "post",
      data : {
        "pubJsonParams" : JSON.stringify(pubJsonList),
        "cacheKey" : $("#cache_key").val(),
        "des3GroupId" : $("#des3GroupId").val(),
        "savePubType" : $("#savePubType").val()
      },
      dataType : "html",
      success : function(data) {
        $("#import_status").val("true");
        $("#import_pub_list_container").hide();
        $("#import_result_tip_div").html(data);
        $("#import_result_tip_div").show();
        positioncenter({
          targetele : 'search_import_result_tips',
          closeele : 'search_import_result_tips_close'
        });
        BaseUtils.disable(obj, click);
      },
      error : function(data) {
        $("#loadingImage").attr("src", "");
        $("#import_status").val("true");
        scmpublictoast("保存失败", 5000);
        BaseUtils.disable(obj, click);
      }
    });
    $("#selected_to_import_num").html("0");
  }

  //完成检索导入
  function finishImport() {
    $("#import_result_tip_div").remove();
    closeImportPubBox();
  }

  //继续导入操作
  function continueImport() {
    $("#import_result_tip_div").remove();
    closeImportPubBox();
  }

  function checkPubTypeDup(pubType, index) {
    $.ajax({
      url : "/pub/dup/checkpubtype2",
      type : "post",
      data : {
        "cacheKey" : $("#cache_key").val(),
        "pubType" : pubType,
        "index" : index
      },
      dataType : "json",
      timeout : 10000,
      success : function(data) {
        if (data.result == "error") {
          scmpublictoast("查重异常", 1000);
        } else if (data.result == "no_dup") {
          $(".dev_duppub_div").eq(index - 1).css("display", "none");
          $(".dev_duppub_div").eq(index - 1).find("input.des3DupPubId").val("");
        } else {
          $(".dev_duppub_div").eq(index - 1).css("display", "flex");
          $(".dev_duppub_div").eq(index - 1).find("input.des3DupPubId").val(data.result);
        }
      },
      error : function(data) {
      }
    });
  }
</script>
<input type="hidden" id="cache_key" value="${importVo.cacheKey }" />
<input type="hidden" id="valid_pub" value="${importVo.validPub }" />
<input type="hidden" id="import_status" value="true" />
<div class="new-importresult_container" id="import_pub_list_container">
  <div class="new-importresult_container-header">
    <span class="new-importresult_container-header_title"><spring:message code="referencesearch.pubList.title" /></span>
    <i class="list-results_close new-importresult_container-header_tip" onclick="closeImportPubBox();"></i>
  </div>
  <div class="new-importresult_container-neck">
    <spring:message code="referencesearch.pubList.text.pre" />
    <span class="new-importresult_container-neck_tip"></span>
    <spring:message code="referencesearch.pubList.text.pos" />
  </div>
  <div class="new-importresult_container-body" id="pengding_import_pub_list_div">
    <div class="new-importresult_container-body_header">
      <div class="new-importresult_container-body_header-chioce">
        <spring:message code="referencesearch.pubList.table.field.number" />
      </div>
      <div class="new-importresult_container-body_header-infor">
        <spring:message code="referencesearch.pubList.table.field.infor" />
      </div>
      <div class="new-importresult_container-body_header-Collection">
        <spring:message code="referencesearch.pubList.table.field.Collection" />
      </div>
      <div class="new-importresult_container-body_header-style">
        <spring:message code="referencesearch.pubList.table.field.style" />
      </div>
      <c:if test="${empty importVo.des3GroupId }">
        <div class="new-importresult_container-body_header-record 1">
          <spring:message code="referencesearch.pubList.table.field.record" />
        </div>
      </c:if>
    </div>
    <div class="new-importresult_container-body_item-box">
      <c:forEach items="${importVo.pendingImportPubs}" varStatus="status" var="pubInfo">
        <div
          class="new-importresult_container-body_item pending_import_pub_item <c:if test='${pubInfo.authorMatch == 1 }'>new-importresult_container-body_item-background</c:if>">
          <div class="new-importresult_container-body_item-chioce">
            <div>${status.index + 1 }</div>
            <!-- <i class="selected-author pedding_import_pub selected-author_confirm" name="importPubListSelectedConfirm"></i> -->
          </div>
          <div class="new-importresult_container-body_item-infor">
            <div class="new-importresult_container-body_item-infor_title" style="max-height: 40px; overflow: hidden;">
              <c:choose>
                <c:when test="${fn:length(pubInfo.tmpSourceUrl) gt 1 || fn:length(pubInfo.sourceUrl) gt 1}">
                      <a class="Blue" title="${pubInfo.title }"
                        href="<c:out value='${fn:length(pubInfo.tmpSourceUrl) gt 1 ? pubInfo.tmpSourceUrl : pubInfo.sourceUrl}' escapeXml='true' />"
                        target="_blank">
                        <div class="multipleline-ellipsis__content-box">${pubInfo.title }</div>
                      </a>
                </c:when>
                <c:otherwise>
                  <div class="multipleline-ellipsis__content-box">${pubInfo.title }</div>
                </c:otherwise>
              </c:choose>
            </div>
            <span class="new-importresult_container-body_item-infor_author" title="${pubInfo.authorNames }">${pubInfo.authorNames }<br></span>
            <span class="new-importresult_container-body_item-infor_time" title="${pubInfo.briefDesc }">${pubInfo.briefDesc }<br></span>
          </div>
          <div class="new-importresult_container-body_item-style pub_situation_div">
            <div class="new-importresult_container-body_item-style_item">
              <c:if test="${pubInfo.EIIncluded }">
                <i class="selected-author pub_situation_item selected-author_confirm cannot_change" value="EI"></i>
              </c:if>
              <c:if test="${!pubInfo.EIIncluded }">
                <i class="selected-author pub_situation_item" value="EI" name="importPubListSelectedConfirm"></i>
              </c:if>
              <span>EI</span>
            </div>
            <div class="new-importresult_container-body_item-style_item">
              <c:if test="${pubInfo.SCIEIncluded }">
                <i class="selected-author pub_situation_item selected-author_confirm cannot_change" value="SCIE"></i>
              </c:if>
              <c:if test="${!pubInfo.SCIEIncluded }">
                <i class="selected-author pub_situation_item" value="SCIE" name="importPubListSelectedConfirm"></i>
              </c:if>
              <span>SCIE</span>
            </div>
            <div class="new-importresult_container-body_item-style_item">
              <c:if test="${pubInfo.ISTPIncluded }">
                <i class="selected-author pub_situation_item selected-author_confirm cannot_change" value="ISTP"></i>
              </c:if>
              <c:if test="${!pubInfo.ISTPIncluded }">
                <i class="selected-author pub_situation_item" value="ISTP" name="importPubListSelectedConfirm"></i>
              </c:if>
              <span>ISTP</span>
            </div>
            <div class="new-importresult_container-body_item-style_item">
              <c:if test="${pubInfo.SSCIIncluded }">
                <i class="selected-author pub_situation_item selected-author_confirm cannot_change" value="SSCI"></i>
              </c:if>
              <c:if test="${!pubInfo.SSCIIncluded }">
                <i class="selected-author pub_situation_item" value="SSCI" name="importPubListSelectedConfirm"></i>
              </c:if>
              <span>SSCI</span>
            </div>
          </div>
          <div class="new-importresult_container-body_item-Collection">
            <select class="pub_type_options" onchange="checkPubTypeDup(this.value,'${pubInfo.index}');">
              <option value="4" <c:if test="${pubInfo.pubType == 4 }">selected</c:if>><spring:message
                  code="menu.paper.category.journalArticle" /></option>
              <option value="3" <c:if test="${pubInfo.pubType == 3 }">selected</c:if>><spring:message
                  code="pub.filter.conferencePaper" /></option>
              <option value="2" <c:if test="${pubInfo.pubType == 2 }">selected</c:if>><spring:message
                  code="pub.filter.bookAndMonograph" /></option>
              <option value="10" <c:if test="${pubInfo.pubType == 10 }">selected</c:if>><spring:message
                  code="pub.filter.bookChapter" /></option>
              <option value="1" <c:if test="${pubInfo.pubType == 1 }">selected</c:if>><spring:message
                  code="pub.filter.award" /></option>
              <option value="8" <c:if test="${pubInfo.pubType == 8 }">selected</c:if>><spring:message
                  code="pub.filter.thesis" /></option>
              <option value="5" <c:if test="${pubInfo.pubType == 5 }">selected</c:if>><spring:message
                  code="pub.filter.patent" /></option>
              <option value="12" <c:if test="${pubInfo.pubType == 12 }">selected</c:if>><spring:message
                  code="pub.filter.standard" /></option>
              <option value="13" <c:if test="${pubInfo.pubType == 13 }">selected</c:if>><spring:message
                  code="pub.filter.softwarecopyright" /></option>
              <option value="7" <c:if test="${pubInfo.pubType == 7 }">selected</c:if>><spring:message
                  code="pub.filter.other" /></option>
            </select>
          </div>
          <c:if test="${empty importVo.des3GroupId }">
            <div class="new-importresult_container-body_item-record 3" style="width: 20%;">
              <div class="new-importresult_container-body_item-record dev_duppub_div" style='width: 100%;display:
                    <c:choose>
                        <c:when test="${!empty pubInfo.dupPubId && pubInfo.dupPubId > 0}">flex</c:when>
                        <c:otherwise >none</c:otherwise>
                    </c:choose>'>
                <input type="hidden" name="des3DupPubId" value="<iris:des3 code='${pubInfo.dupPubId }'/>"
                  class="des3DupPubId" />
                <div class="new-importresult_container-body_item-record_tip dev_search_tip">
                  <spring:message code="referencesearch.pubList.tip" />
                </div>
                <div class="new-importresult_container-body_item-record_chioce">
                  <select onchange="changeImportOperation(this, '${status.index }');" class="dup_pub_opt">
                    <option value="refresh"><spring:message code="pub.opt.update" /></option>
                    <option value="skip"><spring:message code="pub.opt.skip" /></option>
                    <option value="add"><spring:message code="pub.opt.add" /></option>
                  </select>
                </div>
                <div class="new-importresult_container-body_item-record_detail import_operation_tips"
                  id="import_tips_${status.index }">
                  <spring:message code="referencesearch.pubList.tips" />
                </div>
              </div>
            </div>
          </c:if>
          <c:if test="${empty importVo.des3GroupId }">
            <div class="4"></div>
          </c:if>
        </div>
      </c:forEach>
    </div>
  </div>
  <div class="new-importresult_container-footer">
    <div class="import_Achieve-footer_loading" style="margin-right: 72px;">
      <img id="loadingImage">
    </div>
    <div class="new-importresult_container-footer_close" onclick="closeImportPubBox();">
      <spring:message code="dialog.manageTag.btn.close" />
    </div>
    <div class="new-importresult_container-footer_save" onclick="importSelectedPub(this);">
      <spring:message code="pub.opt.save" />
    </div>
  </div>
</div>
<div class="background-cover" id="import_result_tip_div" style="display: none;"></div>
