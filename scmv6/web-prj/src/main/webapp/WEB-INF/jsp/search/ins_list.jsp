<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="mechanism-right__container main-list__list" list-main="insSearch">
    <s:if test="insInfoList != null && insInfoList.size > 0">
    <div class="sorting_box" id="sortingbox" style="margin-left:16px;height:20px; display: flex; align-items: center; justify-content: space-between;">
        <span class="f999">
            <s:text name="ins.search.result.count.tip">
                <s:param>${page.totalCount }</s:param>
            </s:text>
        </span>
        <div class="create-InstitutionalGroups_header-btn" onclick="SearchIns.createInsMain();">
             <div class="create-InstitutionalGroups_header-icon">+</div>
             <span class="create-InstitutionalGroups_header-detail"><s:text name="ins.search.list.create.sie"/></span>
        </div>
    </div>
    </s:if>
    <div class="js_listinfo" smate_count="${page.totalCount }"></div>
    <s:if test="insInfoList != null && insInfoList.size > 0">
        <s:iterator value="insInfoList" var="ins" status="st">
            <input type="hidden" class="initInsIds" value="${ins.desInsId}" />
            <div class="mechanism-right__container-item">
                <div class="mechanism-right__container-item__box">
                    <div class="mechanism-right__container-item_r">
                        <div class="mechanism-right__container-item_infor">
                            <a id="item_href_${ins.insId }" target="_blank" href="${ins.domainUrl }">
                              <img  class="mechanism-right__container-item_avator"
                                  src="${ins.logoUrl}"
                                  onerror="this.src='/resmod/smate-pc/img/logo_instdefault.png'"/>
                             </a>
                            <div class="mechanism-right__container-item_body" style="min-height:64px;" >
                                <div class="mechanism-right__container-item_name  noUrl pub-idx__main_title-multipleline" id="item_click_${ins.insId }" onclick="window.open('${ins.domainUrl }');" style="width: 600px!important;height: 40px;overflow: hidden;line-height: 20px!important;">
                                     <a class="file-rigth__main_title-multipleline_box"  title="${ins.showName }">${ins.showName }</a>
                                </div>
                                <div class="mechanism-right__container-item_addr" style="line-height: 24px; height: 24px;  flex-shrink: 0;">${ins.characterName }</div>
                                <div class="new-Standard_Function-bar">
                                       <a href="javascript:;" class="manage-one mr20" isAward="${ins.isAward == 1}" onclick="SearchIns.awardNewInsOpt('${ins.insId}', this)">
                                          <c:if test="${ins.isAward == 0}">
                                            <div class="new-Standard_Function-bar_item" style="margin-left: 0px;">
                                                    <i class="new-Standard_function-icon new-Standard_Praise-icon"></i>
                                                    <span class="new-Standard_Function-bar_item-title"><s:text name="ins.search.list.like"/></span>
                                                    <span class="likeSum_${ins.insId }" style="<s:if test='#ins.likeSum == 0'>display:none;</s:if>">(${ins.likeSum })</span>
                                            </div>
                                           </c:if>
                                           <c:if test="${ins.isAward == 1}">
                                                <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected" style="margin-left: 0px;">
                                                    <i class="new-Standard_function-icon new-Standard_Praise-icon"></i>
                                                    <span class="new-Standard_Function-bar_item-title"><s:text name="ins.search.list.unlike"/></span>
                                                    <span class="likeSum_${ins.insId }" style="<s:if test='#ins.likeSum == 0'>display:none;</s:if>">(${ins.likeSum })</span>
                                               </div>
                                           </c:if>
                                    </a>
                                    <input type="hidden" class="share_title_input" value='${ins.domainUrl}' /> 
                                    <a class="manage-one mr20 dev_ins_share" onclick="SmateShare.shareInsParams($(this));initInsShare(this);"
                                        resId="${ins.desInsId}" insId="${ins.insId }" insPic="${ins.logoUrl}" insName="${ins.showName }" insUrl="${ins.domainUrl }">
                                        <div class="new-Standard_Function-bar_item">
                                          <i class="new-Standard_function-icon new-Standard_Share-icon"></i> <span
                                            class="new-Standard_Function-bar_item-title dev_ins_share_${ins.insId }"><s:text name="ins.search.list.share" /><iris:showCount count="${ins.shareSum }" preFix="(" sufFix=")"/></span>
                                        </div>
                                    </a>
                                    <a href="javascript:;" class="manage-one mr20" followed="${ins.isFollow}" onclick="SearchIns.followNewInsOpt('${ins.insId}', this)"> 
                                    <c:if test="${ins.isFollow == 0}">
                                          <div class="new-Standard_Function-bar_item">
                                              <i class="new-Standard_function-icon new-Standard_Save-icon"></i> 
                                              <span class="new-Standard_Function-bar_item-title interest_word_span"><s:text name="ins.search.list.follow"/></span>
                                            </div>
                                      </c:if>
                                      <c:if test="${ins.isFollow == 1}">
                                          <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected">
                                              <i class="new-Standard_function-icon new-Standard_Save-icon"></i> 
                                              <span class="new-Standard_Function-bar_item-title interest_word_span"><s:text name="ins.search.list.unfollow"/></span>
                                            </div>
                                        </c:if> 
                                    </a>
                                </div>
                            </div>
                        </div>
                        <div class="mechanism-right__container-item_subinfor">
                        <div class="func_container-list">
                            <s:if test="#ins.characterId == 1">
                              <div class="func_container-item">
                                  <div>${ins.pubSum}</div>
                                  <div><s:text name="ins.search.list.pub.count"/></div>
                              </div>
                              <div class="func_container-item">
                                  <div>${ins.prjSum}</div>
                                  <div><s:text name="ins.search.list.pat.count"/></div>
                              </div>
                              <div class="func_container-item">
                                  <div>${ins.psnSum}</div>
                                  <div><s:text name="ins.search.list.psn.count"/></div>
                              </div>
                            </s:if>
                        </div>
                        
                        </div>
                    </div>
                </div>
            </div>
        </s:iterator>
    </s:if>
    <s:else>
        <div class="no_effort">
            <h2>
                <s:text name="ins.search.no.result.tip1"/>
            </h2>
            <div class="no_effort_tip">
                <span><s:text name="ins.search.no.result.tip2"/></span>
                <p>
                    <s:text name="ins.search.no.result.tip3"/>
                </p>
                <p>
                    <s:text name="ins.search.no.result.tip4"/>
                </p>
                <p>
                    <s:text name="ins.search.no.result.tip5"/>
                </p>
            </div>
        </div>

    </s:else>
</div>
<div class="main-list__footer" style="padding-left: 32px;width:100%;">
    <%@ include file="../common/page-tages.jsp"%>
</div>