<%@ page language="java" pageEncoding="UTF-8"%>
<div id="box_main_result" style="display: none; overflow: hidden; width: 100%">
  <!-- 查询条件提示  begin-->
  <div class="Federal-retrieval_searchtip">
    <span class="Federal-retrieval_searchtip-detail" id="showcriteria"></span>
  </div>
  <!-- 查询条件提示  end-->
  <div class="Federal-retrieval_searchbox">
    <div class="Federal-retrieval_searchtitle">
      <div class="Federal-retrieval_searchtitle-left">
        <span class="Federal-retrieval_searchtitle-haedline"><spring:message
            code="referencesearch.label.criteria_foot2" /></span> <span class="Federal-retrieval_searchtitle-left_tip">(<spring:message
            code="referencesearch.label.notes_import" />)
        </span> <i class="find-nothing_tip"></i> <span class="Federal-retrieval_searchtitle-left_answer"><a
          href="/resscmwebsns/search_faq/search_up_${lang }.html#b10" target="_blank"><spring:message
              code="referencesearch.label.cannotfind" /></a></span>
      </div>
      <div class="Federal-retrieval_searchtitle-right">
        <span style="padding-right: 10px;" title="<spring:message code='pub.search.import.limit.maxtips'/>"
          id="select_maxnum_limit_span"><span id="selected_to_import_num">0</span> / 20</span>
        <div class="Federal-retrieval_searchtitle-right_import" onclick="impGetDataXmlAll(this,'import');">
          <spring:message code="referencesearch.button.import" />
        </div>
        <div class="Federal-retrieval_searchtitle-right_back" onclick="switchDIV(false);">
          <spring:message code="referencesearch.button.back" />
        </div>
        <div class="Federal-retrieval_searchtitle-right_check" onclick="searImport.viewHistory();">
          <spring:message code="referencesearch.button.finish2" />
        </div>
      </div>
    </div>
    <div class="Federal-retrieval_Resultbox">
      <div class="Federal-retrieval_Resultbox-left" id="db_list_ul"></div>
      <div class="Federal-retrieval_Resultbox-right" style="padding-left: 20px;" id="list_lable_info">
        <c:forEach items="${importVo.constRefDBList }" var="db" varStatus="idx">
          <iframe id="if_${db.code}" frameborder="0" width="100%" scrolling="no" style="display: none">${idx.count}
          </iframe>
          <div class="bckground-cover" id="boxDiv_${db.code}_background" style="display: none">
            <div class="new-delete_alias-container" id="boxDiv_${db.code}">
              <div class="new-delete_alias-header">
                <span class="new-delete_alias-header_title"><spring:message code="referencesearch.exclusive.name" /></span> <i
                  class="new-delete_alias-close list-results_close"></i>
              </div>
              <div class="new-delete_alias-body label-list"></div>
              <div class="new-delete_alias-footer">
                <div class="new-delete_alias-footer_close  save_psn_alias_btn">
                  <spring:message code='pub.opt.save' />
                </div>
              </div>
            </div>
          </div>
        </c:forEach>
      </div>
    </div>
  </div>
</div>
