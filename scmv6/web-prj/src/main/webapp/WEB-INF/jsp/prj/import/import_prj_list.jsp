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
       function closeImportPrjBox(){
           $("#showList").hide();
           $("#selected_to_import_num").html("0");
           //删除缓存的待导入成果
           $.post("/prjweb/cache/ajaxremove", {"cacheKey": $("#cache_key").val()},
                function(data){}, "json");
       }
       //切换操作
       function changeImportOperation(obj, index){
           var optVal = $(obj).find("option:selected").val();
           var addTips = "<s:text name='referencelist.label.notduplicated' />";
           var skipTips = "<s:text name='referencesearch.prjList.tip.skip' />";
           var tips = "<s:text name='referencesearch.prjList.tips' />";
           if(optVal == "skip"){
               tips = skipTips;
           }else if(optVal == "add"){
               tips = addTips;
           }
           $("#import_tips_"+index).html(tips);
       }
       function positioncenter(options) {
         var defaults = {
             targetele : "",
             closeele : ""
         };
         var opts = Object.assign(defaults, options);
         if ((opts.targetele == "") || (opts.closeele == "")) {
             alert("未传入必须的操作元素");
         } else {
             var postarget = document.getElementsByClassName(opts.targetele)[0];
             var opstclose = document.getElementsByClassName(opts.closeele);
             postarget.closest(".background-cover").style.display = "block";
             setTimeout(function() {
                 postarget.style.left = (window.innerWidth - postarget.offsetWidth)
                         / 2 + "px";
                 postarget.style.bottom = (window.innerHeight - postarget.offsetHeight)
                         / 2 + "px";
             }, 300);
             window.onresize = function() {
                 postarget.style.left = (window.innerWidth - postarget.offsetWidth)
                         / 2 + "px";
                 postarget.style.bottom = (window.innerHeight - postarget.offsetHeight)
                         / 2 + "px";
             }
             for (var i = 0; i < opstclose.length; i++) {
                 opstclose[i].onclick = function() {
                     this.closest("." + opts.targetele).style.bottom = -600 + "px";
                     setTimeout(function() {
                         postarget.closest(".background-cover").style.display = "none";
                     }, 300);
                 }
             }
         }
       };
       //保存待导入成果
       function importSelectedPrj(obj){
           var click = $(obj).attr("onclick");
           BaseUtils.unDisable(obj);
           var prjJsonList = new Array();
           $("#pengding_import_prj_list_div .pending_import_prj_item").each(function(){
               var prjJsonParams = {};
               var $this = $(this);
               //是否有重复成果和对重复成果选择的操作
               var des3DupPrjId = $this.find("input.des3DupPrjId").val();
               if(!BaseUtils.checkIsNull(des3DupPrjId)){
                   prjJsonParams["dup_des3_prj_id"] = des3DupPrjId;
                   prjJsonParams["dup_opt"] = $this.find("select.dup_prj_opt").val();
               }
               //是否选中了要保存起来
               prjJsonParams["pub_save"] = 1;
               prjJsonList.push(prjJsonParams);
           });
           //发送导入保存请求
           $.ajax({
              url: "/prjweb/import/ajaxsave",
              type: "post",
              data: {
                  "prjJsonParams": JSON.stringify(prjJsonList),
                  "cacheKey": $("#cache_key").val()
              },
              dataType: "html",
              success: function(data){
                  $("#import_prj_list_container").hide();
                  $("#import_result_tip_div").html(data);
                  $("#import_result_tip_div").show();
                  positioncenter({targetele:'search_import_result_tips',closeele:'search_import_result_tips_close'});
                  BaseUtils.disable(obj, click);
              },
              error: function(data){
                  scmpublictoast("保存失败",5000);
                  BaseUtils.disable(obj, click);
              }
           });
           $("#selected_to_import_num").html("0");
       }
       
       //完成检索导入
       function finishImport(){
           $("#import_result_tip_div").remove();
           closeImportPrjBox();
       }
       
       //继续导入操作
       function continueImport(){
           $("#import_result_tip_div").remove();
           closeImportPrjBox();
       }
</script>
<input type="hidden" id="cache_key" value="${cacheKey }" />
<input type="hidden" id="import_error_msg" value="${errorMsg }">
<c:if test="${empty errorMsg }">
<div class="new-importresult_container" id="import_prj_list_container">
  <div class="new-importresult_container-header">
    <span class="new-importresult_container-header_title"><s:text name="referencesearch.prjList.title"/></span>
    <i class="list-results_close new-importresult_container-header_tip" onclick="closeImportPrjBox();"></i>
  </div>
  <%-- <div class="new-importresult_container-neck">
    <s:text name="referencesearch.pubList.text.pre" />
    <span class="new-importresult_container-neck_tip"></span>
    <s:text name="referencesearch.pubList.text.pos" />
  </div> --%>
  <div class="new-importresult_container-body" id="pengding_import_prj_list_div">
    <div class="new-importresult_container-body_header">
      <div class="new-importresult_container-body_header-chioce">
        <s:text name="referencesearch.prjList.seqNO"/>
      </div>
      <div class="new-importresult_container-body_header-infor">
        <s:text name="referencesearch.pubList.table.field.infor" />
      </div>
      <div class="new-importresult_container-body_header-style">
        <s:text name="referencesearch.pubList.table.field.style" />
      </div>
      <c:if test="${empty importVo.des3GroupId }">
         <div class="new-importresult_container-body_header-record">
            <s:text name="referencesearch.pubList.table.field.record" />
         </div>
      </c:if>
    </div>
    <div class="new-importresult_container-body_item-box">
      <c:forEach items="${prjInfoList}" varStatus="status" var="prjInfo">
        <div
          class="new-importresult_container-body_item pending_import_prj_item">
          <div class="new-importresult_container-body_item-chioce">
            ${status.count }
          </div>
          <div class="new-importresult_container-body_item-infor">
            <div class="new-importresult_container-body_item-infor_title" style="max-height: 40px; overflow: hidden;">
              <c:choose>
                <c:when test="${!empty prjInfo.tempSourceUrl }">
                  <c:choose>
                    <c:when test="${(prjInfo.sourceDbCode eq 'CnkiFund')}">
                      <a class="Blue" title="${prjInfo.showTitle }"
                        onclick="ScholarView.viewTmpSrcUrl('${prjInfo.tempSourceUrl}')">
                        <div class="multipleline-ellipsis__content-box">${prjInfo.showTitle }</div>
                      </a>
                    </c:when>
                    <c:otherwise>
                      <a class="Blue" title="${prjInfo.showTitle }"
                        href="<c:out value='${prjInfo.tempSourceUrl}' escapeXml='true' />"
                        target="_blank">
                        <div class="multipleline-ellipsis__content-box">${prjInfo.showTitle }</div>
                      </a>
                    </c:otherwise>
                  </c:choose>
                </c:when> 
                <c:otherwise>
                  <div class="multipleline-ellipsis__content-box">${prjInfo.showTitle }</div>
                </c:otherwise>
              </c:choose>
            </div>
            <span class="new-importresult_container-body_item-infor_author" title="${prjInfo.showAuthorNames }">${prjInfo.showAuthorNames }<br></span>
            <span class="new-importresult_container-body_item-infor_time" title="${prjInfo.showBriefDesc }">${prjInfo.showBriefDesc }<br></span>
          </div>
          
          <div class="new-importresult_container-body_item-Collection">
            <s:text name="referencesearch.prjList.type"/>
          </div>
            <div class="new-importresult_container-body_item-record" style="width: 20%;">
         <c:if test="${!empty prjInfo.des3DupPrjId && prjInfo.des3DupPrjId > 0}">
              <input type="hidden" name="des3DupPrjId" value="<iris:des3 code='${prjInfo.des3DupPrjId }'/>"
                class="des3DupPrjId" />
              <div class="new-importresult_container-body_item-record_tip">
                <s:text name="referencesearch.prjList.tip" />
              </div>
              <div class="new-importresult_container-body_item-record_chioce">
                <select onchange="changeImportOperation(this, '${status.index }');" class="dup_prj_opt">
                  <option value="refresh"><s:text name="pub.opt.update" /></option>
                  <option value="skip"><s:text name="pub.opt.skip" /></option>
                </select>
              </div>
              <div class="new-importresult_container-body_item-record_detail import_operation_tips"
                id="import_tips_${status.index }">
                <s:text name="referencesearch.prjList.tips" />
              </div>
          </c:if>
            </div>
        </div>
      </c:forEach>
    </div>
  </div>
  <div class="new-importresult_container-footer">
    <div class="new-importresult_container-footer_close" onclick="closeImportPrjBox();">
      <s:text name="dialog.manageTag.btn.close" />
    </div>
    <div class="new-importresult_container-footer_save" onclick="importSelectedPrj(this);">
      <s:text name="pub.opt.save" />
    </div>
  </div>
</div>
</c:if>
<div class="background-cover" id="import_result_tip_div" style="display: none;"></div>