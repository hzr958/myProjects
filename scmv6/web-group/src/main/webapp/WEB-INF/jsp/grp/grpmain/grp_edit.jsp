
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	addFormElementsEvents($("#grp_base_ui")[0]);
	$createGrpChipBox = window.ChipBox({name:"chipcodeedit",maxItem:10,isSeparate:true,callbacks:{
		compose:function(){
			var str="";
			$("#manage_keywords").find(".chip__text").each(function(i,o){
				str+=$(o).text()+";";
			});
			if(str.length>640){
				if('${locale}'=="en_US"){
					scmpublictoast("Keywords is limited to 650 characters.",2000);
				}else{
					scmpublictoast("关键词总长度不能超过650个字符",2000);
				}
			}
		}
	}});
	GrpBase.bindKeyWordsRcmd();
	GrpBase.sndDisciplieLoad();

    var secondCategoryId = $(".dev_sel__box[selector-id='2nd_discipline']").attr("sel-value");
    if($.trim(secondCategoryId) != ""){
        $(".dev_sel__box[selector-id='2nd_discipline']").css("visibility","");
    }

	//根据openType进行权限的显示
	var hitOpneType = $("[name = 'hitOpneType']").val();
	if ("O" == hitOpneType) {
	  $("#grpEditPublicDescribe").css("display", "block");
      $("#grpEditSemiPublicDescribe").css("display", "none");
      $("#grpEditPrivacyDescribe").css("display", "none");
	}
	if ("H" == hitOpneType) {
	  $("#grpEditPublicDescribe").css("display", "none");
      $("#grpEditSemiPublicDescribe").css("display", "block");
      $("#grpEditPrivacyDescribe").css("display", "none");
	}
	if ("P" == hitOpneType) {
	  $("#grpEditPublicDescribe").css("display", "none");
      $("#grpEditSemiPublicDescribe").css("display", "none");
      $("#grpEditPrivacyDescribe").css("display", "block");
	}
	
});
</script>
<s:if test="grpShowInfo.role==1 || grpShowInfo.role==2">
  <div class="container__horiz_left width-3">
    <nav class="nav_vert depth2">
      <ul class="nav__list" id="grp_manage_li">
        <li class="nav__item item_selected" onclick='GrpBase.showGrpBaseUI();GrpBase.clickGrpManageLi(this);'><s:text
            name='groups.base.manage.basicInfo' /></li>
        <s:if test="grpShowInfo.role==1">
          <li class="nav__item  " onclick='GrpBase.jconfirmdelMyGrp(this)'><s:text name='groups.base.manage.del' /></li>
        </s:if>
        <s:else>
          <li class="nav__item item_disabled"><s:text name='groups.base.manage.del' /></li>
        </s:else>
      </ul>
    </nav>
  </div>
  <div class="container__horiz_right width-9">
    <div class="pro-manage depth2" id='grp_base_ui'>
      <div class="pro-manage__content">
        <form onsubmit="return false;">
          <div class="form__sxn_row">
            <div class="input__box">
              <c:if test="${grpShowInfo.grpBaseInfo.grpCategory==11}">
                <label class="input__title"><s:text name='groups.base.manage.proTitle' /></label>
              </c:if>
              <c:if test="${grpShowInfo.grpBaseInfo.grpCategory==10}">
                <label class="input__title"><s:text name='groups.base.manage.curTitle' /></label>
              </c:if>
              <div class="input__area">
                <input type="text" class='input_grpname_val' value="${grpShowInfo.grpBaseInfo.grpName}" maxlength="199">
              </div>
              <div class="input__helper" helper-text="" invalid-message=""></div>
            </div>
          </div>
          <c:if test="${grpShowInfo.grpBaseInfo.grpCategory==11}">
            <div class="form__sxn_row">
              <div class="input__box">
                <label class="input__title"><s:text name='groups.base.manage.proNo' /></label>
                <div class="input__area">
                  <input type="text" class='input_projectno_val' value="${grpShowInfo.grpBaseInfo.projectNo}"
                    maxlength="99">
                </div>
                <div class="input__helper" helper-text="" invalid-message=""></div>
              </div>
            </div>
          </c:if>
          <div class="form__sxn_row">
            <div class="input__box">
              <c:if test="${grpShowInfo.grpBaseInfo.grpCategory==11}">
                <label class="input__title"><s:text name='groups.base.manage.proabs' /></label>
              </c:if>
              <c:if test="${grpShowInfo.grpBaseInfo.grpCategory==10}">
                <label class="input__title"><s:text name='groups.base.manage.curInfo' /></label>
              </c:if>
              <div class="input__area">
                <textarea class='input_grpdescription_val' maxlength="1999" style="padding: 0px;">${grpShowInfo.grpBaseInfo.grpDescription}</textarea>
                <div class="textarea-autoresize-div"></div>
              </div>
              <div class="input__helper" invalid-message=""></div>
            </div>
          </div>
          <!--  研究领域  start -->
          <div class="form__sxn_row">
            <div class="input__box no-flex input_not-null">
              <label class="input__title"><s:text name='groups.base.manage.area' /></label>
              <div class="dev_sel__box" selector-id="1st_discipline"  sel-value='${grpShowInfo.grpKwDisc.firstCategoryId}' onclick="GrpBase.showScienceAreaBox(1)">
                <div class="sel__value" style="display: flex; align-items: center;">
                  <span class="sel__value_selected sel__value_placeholder sel__value_selected-content">
                    <c:if test="${firstDisciplinetName!=''}">
                      ${firstDisciplinetName}
                    </c:if> <c:if test="${ empty grpShowInfo.grpKwDisc || empty  firstDisciplinetName}">
                    <s:text name='groups.base.priCtg' />
                  </c:if>
                  </span> <i class="material-icons sel__dropdown-icon">arrow_drop_down</i>
                </div>
              </div>
              <div class="input__helper" invalid-message=""></div>
            </div>
            <div class="input__box no-flex input_not-null">
              <label class="input__title"></label>
              <div class="dev_sel__box" style="visibility: hidden" selector-id="2nd_discipline" sel-value='${grpShowInfo.grpKwDisc.secondCategoryId}'
                   onclick="GrpBase.showScienceAreaBox(1)">
                <div class="sel__value" style="display: flex; align-items: center;">
                  <span class="sel__value_selected sel__value_placeholder sel__value_selected-content">
                    <c:if test="${secondDisciplinetName!=''}">
                      ${secondDisciplinetName}
                    </c:if>
                    <c:if test="${ empty grpShowInfo.grpKwDisc || empty   secondDisciplinetName }">
                      <s:text name='groups.base.secCtg' />
                    </c:if>
                  </span> <i class="material-icons sel__dropdown-icon">arrow_drop_down</i>
                </div>
              </div>
              <div class="input__helper" invalid-message=""></div>
            </div>
          </div>
          <!--  研究领域  end -->
          <div class="form__sxn_row">
            <div class="input__box no-input-area">
              <label class="input__title"><s:text name='groups.base.keywords' /></label>
              <div class="chip-panel__box" id='manage_keywords' chipbox-id="chipcodeedit"
                smate_keywords='${grpShowInfo.grpKwDisc.keywords}'>
                <!-- 关键词 -->
                <s:iterator value="grpShowInfo.grpKeywordList" id="keyword">
                  <div class="chip__box">
                    <div class="chip__avatar"></div>
                    <div class="chip__text">
                      <s:property value="keyword" />
                    </div>
                    <div class="chip__icon icon_delete">
                      <i class="material-icons">close</i>
                    </div>
                  </div>
                </s:iterator>
                <div class="chip-panel__manual-input js_autocompletebox" id='autokeywords'
                  request-url="/groupweb/mygrp/ajaxautoconstkeydiscs" contenteditable="true"></div>
              </div>
              <div class="global__para_caption" style="white-space: normal;">
                <s:text name='groups.keywords.describe' />
              </div>
            </div>
          </div>
          <div class="form__sxn_row">
            <!-- 推荐关键词 -->
            <div class="input__box no-input-area grp_rcmd_keywords"></div>
          </div>
          <div class="form__sxn_row" style="flex-direction: column;">
            <div class="input__box no-input-area">
              <label class="input__title"><s:text name='groups.base.permissions' /></label>
              <div class="input-radio__box_vert" id="choose-authority-list"
                style="display: flex; flex-direction: inherit; padding: 0px;">
                <div class="input-radio__sxn" onclick="grpChangePublic('grpEdit')"
                  title="<s:text name='groups.public.describe'/>">
                  <div class="input-custom-style">
                    <input type="hidden" value="${grpShowInfo.grpBaseInfo.openType}" name="hitOpneType" /> <input
                      type="radio" name="choose-authority" value='O'
                      <c:if test="${grpShowInfo.grpBaseInfo.openType=='O'}">
                            checked
                            </c:if>>
                    <i class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label">
                    <s:text name='groups.public' />
                  </div>
                  <%--  <div class="input-radio_grp-auth"> <s:text name='groups.public.describe' /> </div> --%>
                </div>
                <div class="input-radio__sxn" style="margin: 0px 48px;" onclick="grpChangeSemiPublic('grpEdit')"
                  title="<s:text name='groups.semi.public.describe' />">
                  <div class="input-custom-style">
                    <input type="radio" name="choose-authority" value='H'
                      <c:if test="${grpShowInfo.grpBaseInfo.openType=='H'}">
                            checked
                            </c:if>>
                    <i class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label">
                    <s:text name='groups.semi.public' />
                  </div>
                  <%-- <div class="input-radio_grp-auth"> <s:text name='groups.semi.public.describe' /> </div> --%>
                </div>
                <div class="input-radio__sxn" onclick="grpChangeSemiPrivacy('grpEdit')"
                  title="<s:text name='groups.privacy.describe' />">
                  <div class="input-custom-style">
                    <input type="radio" name="choose-authority" value='P'
                      <c:if test="${grpShowInfo.grpBaseInfo.openType=='P'}">
                            checked
                            </c:if>>
                    <i class="material-icons custom-style"></i>
                  </div>
                  <div class="input-radio__label">
                    <s:text name='groups.privacy' />
                  </div>
                  <%-- <div class="input-radio_grp-auth"> <s:text name='groups.privacy.describe' /> </div> --%>
                </div>
              </div>
            </div>
            <div>
              <div id="grpEditPublicDescribe" style="display: none;">
                <div class="input-radio_grp-auth">
                  <div class="input-radio__box_horiz" id="openType">
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" disabled="disabled" name="isIndexDiscussOpen1">
                        <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        <s:text name='groups.base.home' />
                      </div>
                    </div>
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox"
                          <c:if test="${grpShowInfo.grpControl.isIndexMemberOpen=='1'}">
                                        checked
                                    </c:if>
                          name="isIndexMemberOpen1"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        <s:text name='groups.base.person' />
                      </div>
                    </div>
                    <!--  课程群组 成员，文献，课件，作业  -->
                    <s:if test="grpShowInfo.grpBaseInfo.grpCategory ==10">
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isCurwareFileShow=='1'}">
                                        checked
                                    </c:if>
                            name="isCurwareFileShow1"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.file.curware' />
                        </div>
                      </div>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isWorkFileShow=='1'}">
                                        checked
                                    </c:if>
                            name="isWorkFileShow1"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.file.work' />
                        </div>
                      </div>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isIndexPubOpen=='1'}">
                                        checked
                                    </c:if>
                            name="isIndexPubOpen1"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.base.pub' />
                        </div>
                      </div>
                    </s:if>
                    <!--  项目群组 成员，成果，文献，文件   -->
                    <s:elseif test="grpShowInfo.grpBaseInfo.grpCategory ==11">
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isPrjPubShow=='1'}">
                                        checked
                                    </c:if>
                            name="isPrjPubShow1"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.project.pub' />
                        </div>
                      </div>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isPrjRefShow=='1'}">
                                        checked
                                    </c:if>
                            name="isPrjRefShow1"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.project.ref' />
                        </div>
                      </div>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isIndexFileOpen=='1'}">
                                        checked
                                    </c:if>
                            name="isIndexFileOpen1"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.base.file' />
                        </div>
                      </div>
                    </s:elseif>
                    <s:else>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isIndexPubOpen=='1'}">
                                        checked
                                    </c:if>
                            name="isIndexPubOpen1"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.base.pub' />
                        </div>
                      </div>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isIndexFileOpen=='1'}">
                                        checked
                                    </c:if>
                            name="isIndexFileOpen1"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.base.file' />
                        </div>
                      </div>
                    </s:else>
                  </div>
                </div>
              </div>
              <div id="grpEditSemiPublicDescribe" style="display: none;">
                <div class="input-radio_grp-auth">
                  <div class="input-radio__box_horiz" id="halfType">
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox" checked="checked" disabled="disabled" name="isIndexDiscussOpen1">
                        <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        <s:text name='groups.base.home' />
                      </div>
                    </div>
                    <div class="input-radio__sxn">
                      <div class="input-custom-style">
                        <input type="checkbox"
                          <c:if test="${grpShowInfo.grpControl.isIndexMemberOpen=='1'}">
                                        checked
                                    </c:if>
                          name="isIndexMemberOpen2"> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="input-radio__label">
                        <s:text name='groups.base.person' />
                      </div>
                    </div>
                    <!--  课程群组 成员，文献，课件，作业  -->
                    <s:if test="grpShowInfo.grpBaseInfo.grpCategory ==10">
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isCurwareFileShow=='1'}">
                                        checked
                                    </c:if>
                            name="isCurwareFileShow2"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.file.curware' />
                        </div>
                      </div>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isWorkFileShow=='1'}">
                                        checked
                                    </c:if>
                            name="isWorkFileShow2"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.file.work' />
                        </div>
                      </div>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isIndexPubOpen=='1'}">
                                        checked
                                    </c:if>
                            name="isIndexPubOpen2"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.base.pub' />
                        </div>
                      </div>
                    </s:if>
                    <!--  项目群组 成员，成果，文献，文件   -->
                    <s:elseif test="grpShowInfo.grpBaseInfo.grpCategory ==11">
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isPrjPubShow=='1'}">
                                        checked
                                    </c:if>
                            name="isPrjPubShow2"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.project.pub' />
                        </div>
                      </div>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isPrjRefShow=='1'}">
                                        checked
                                    </c:if>
                            name="isPrjRefShow2"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.project.ref' />
                        </div>
                      </div>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isIndexFileOpen=='1'}">
                                        checked
                                    </c:if>
                            name="isIndexFileOpen2"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.base.file' />
                        </div>
                      </div>
                    </s:elseif>
                    <s:else>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isIndexPubOpen=='1'}">
                                        checked
                                    </c:if>
                            name="isIndexPubOpen2"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.base.pub' />
                        </div>
                      </div>
                      <div class="input-radio__sxn">
                        <div class="input-custom-style">
                          <input type="checkbox"
                            <c:if test="${grpShowInfo.grpControl.isIndexFileOpen=='1'}">
                                        checked
                                    </c:if>
                            name="isIndexFileOpen2"> <i class="material-icons custom-style"></i>
                        </div>
                        <div class="input-radio__label">
                          <s:text name='groups.base.file' />
                        </div>
                      </div>
                    </s:else>
                  </div>
                </div>
              </div>
            </div>
          </div>
      </div>
      </form>
      <div class="pro-manage__footer">
        <button class="button_main button_primary-cancle" onclick='GrpBase.hideGrpManageUI()'>
          <s:text name='groups.base.btn.cancel' />
        </button>
        <button class="button_main button_primary-reverse" onclick='GrpBase.doGrpBaseManage(this)'>
          <s:text name='groups.base.btn.confirm' />
        </button>
      </div>
    </div>
  </div>
  <%-- <div class="pro-manage depth2" id='grp_opentype_ui' style="display: none;">
      <div class="pro-manage__content">
        <form>
          <div class="input__box no-input-area">
            <label class="input__title"><s:text name='groups.base.label.chooseOpenModel' /></label>
            
          </div>
        </form>
      </div>
      <div class="pro-manage__footer">
        <button class="button_main button_primary-cancle" onclick='GrpBase.hideGrpManageUI()'><s:text name='groups.base.btn.cancel' /></button>
        <button class="button_main button_primary-reverse" onclick='GrpBase.modifyGrpPermissions(this)'><s:text name='groups.base.btn.confirm' /></button>
      </div>
    </div> --%>
  </div>
  <div class="sel-dropdown__box" selector-data="1st_discipline">
    <div class="sel-dropdown__list">
      <div class="sel-dropdown__item" sel-itemvalue="1" onClick="GrpBase.resetSecondDiscipline()">
        <s:text name='groups.base.manage.area.agri' />
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="2" onClick="GrpBase.resetSecondDiscipline()">
        <s:text name='groups.base.manage.area.sci' />
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="3" onClick="GrpBase.resetSecondDiscipline()">
        <s:text name='groups.base.manage.area.humanSci' />
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="4" onClick="GrpBase.resetSecondDiscipline()">
        <s:text name='groups.base.manage.area.econManage' />
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="5" onClick="GrpBase.resetSecondDiscipline()">
        <s:text name='groups.base.manage.area.engi' />
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="6" onClick="GrpBase.resetSecondDiscipline()">
        <s:text name='groups.base.manage.area.infoSci' />
      </div>
      <div class="sel-dropdown__item" sel-itemvalue="7" onClick="GrpBase.resetSecondDiscipline()">
        <s:text name='groups.base.manage.area.pharSci' />
      </div>
    </div>
  </div>
  <div class="sel-dropdown__box" selector-data="2nd_discipline" data-src="request"
    request-url="/groupweb/mygrp/ajaxgetseconddiscipline" request-data="GrpBase.getJSON()"></div>
</s:if>

<!-- 科技领域弹出框 -->
<div class="dialogs__box" dialog-id="scienceAreaBox" style="width: 720px;" cover-event="" id="scienceAreaBox"
     process-time="0"></div>
<!-- 科技领域弹出框 -->
