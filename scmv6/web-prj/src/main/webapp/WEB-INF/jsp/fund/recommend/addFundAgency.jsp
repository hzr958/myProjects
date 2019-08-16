<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${ressns }/js/fund/fund_recommend_${locale }.js"></script>
<script type="text/javascript">
    $(document).ready(function(){
        //设置选择了的展开和勾选
        addFormElementsEvents(document.getElementById("research-agency-list"));
        var selectAgencyId = $("#selectAgencyCodes").val();
        if(selectAgencyId){
        	var code = selectAgencyId.split(",");
        	for(i in code){
                $("#unchecked_agency_"+code[i]).closest(".nav-cascade__section").find(".nav-cascade__item").removeClass("list_toggle-off");
        		$("#unchecked_agency_"+code[i]).attr("onclick","");
        		$("#unchecked_agency_"+code[i]).find("i").css("color","forestgreen").text("check");
        		$("#unchecked_agency_"+code[i]).attr("id", "checked_agency_" + code[i]);
        	}
        }
/*         //设置选择了的展开和勾选
        debugger;
        var selectAgencyId = $("#selectAgencyCodes").val();
        if(selectAgencyId!=""){
        	var agencyIdList = selectAgencyId.split(",");
        	for(var i=0; i<agencyIdList.length; i++){
        		$("#unchecked_agency_"+agencyIdList[i]).closest(".nav-cascade__item").removeClass("list_toggle-off");
        	}
        } */

    });
    addFundAgence = function(id) {
        var key = $("#" + id + "_category_title").html();
        var sum = $("#choosed_agency_list").find(".main-list__item").length;
        var maxSelectFund = '${maxSelectFund}';
        if (sum < maxSelectFund) {
            var html = '<div class="main-list__item" style="padding: 0px 16px!important;" agencyid = "'
                    + id
                    + '" >'
                    + '<div class="main-list__item_content">'
                    + key
                    + '</div>'
                    + '<div class="main-list__item_actions"  onclick="javascript:delFundAgence(\''
                    + id + '\');"><i class="material-icons">close</i></div></div>';
            $("#choosed_agency_list").append(html);
            $("#unchecked_agency_" + id).attr("onclick", "");
            $("#unchecked_agency_" + id).attr("id", "checked_agency_" + id);
            $("#" + id + "_status").html("check");
            $("#" + id + "_status").css("color", "forestgreen");
        } else {
            // 出提示语
            scmpublictoast(fundRecommend.addFundSizeFail, 1500);
        }
    };
    delFundAgence = function(id) {
        $("#choosed_agency_list").find(".main-list__item[agencyid='" + id + "']")
                .remove();
        $("#checked_agency_" + id).attr("onclick",
                "addFundAgence('" + id + "')");
        $("#checked_agency_" + id).attr("id", "unchecked_agency_" + id);
        $("#" + id + "_status").html("add");
        $("#" + id + "_status").css("color", "");
    };
 </script>
<input type="hidden" id="selectAgencyCodes" value="${selectAgencyCodes}" />
<div class="dialogs__childbox_fixed">
  <div class="dialogs__header">
    <div class="dialogs__header_title">选择资助机构</div>
  </div>
</div>
<div class="dialogs__childbox_adapted">
  <div class="dialogs__content global__padding_24">
    <div class="sugg-picker">
      <div class="sugg-picker__header">按地区选择最多${maxSelectFund}个你感兴趣的资助机构</div>
      <div class="sugg-picker__panel">
        <div class="sugg-panel left-panel">
          <div class="sugg-panel__content">
            <div class="nav-cascade" id="research-agency-list">
              <c:forEach items="${fundAgencyMapList }" var="firstLevel" varStatus="firstStatus">
                <input type="hidden" value="${firstLevel.regionId}" />
                <div class="nav-cascade__section">
                  <div class="nav-cascade__item list_toggle-off">
                    <div class="nav-cascade__title">${firstLevel.regionName }</div>
                    <i class="nav-cascade__icon js_togglelist material-icons">expand_less</i>
                  </div>
                  <div class="nav-cascade__toggle-list">
                    <c:forEach items="${firstLevel.agencyList }" var="subLevel">
                      <div class="nav-cascade__item" onclick="addFundAgence('${subLevel.id}')"
                        id="unchecked_agency_${subLevel.id }">
                        <div class="nav-cascade__title" id="${subLevel.id}_category_title">${subLevel.nameView }</div>
                        <i class="nav-cascade__icon material-icons" id="${subLevel.id}_status">add</i>
                      </div>
                    </c:forEach>
                  </div>
                </div>
              </c:forEach>
            </div>
          </div>
        </div>
        <div class="sugg-panel right-panel">
          <div class="sugg-panel__title">你选择的资助机构</div>
          <div class="sugg-panel__content">
            <div class="main-list__list global_no-border" id="choosed_agency_list">
              <c:forEach items="${selectFundAgencyList }" var="agency" varStatus="status">
                <div class="main-list__item" agencyid="${agency.agencyId }" style="padding: 0px 16px !important;">
                  <div class="main-list__item_content">${agency.showName }</div>
                  <div class="main-list__item_actions">
                    <a onclick="delFundAgence('${agency.agencyId }');"><i class="material-icons">close</i></a>
                  </div>
                </div>
              </c:forEach>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<div class="dialogs__childbox_fixed">
  <div class="dialogs__footer">
    <button class="button_main button_primary-reverse" id="homepage_agency_save_btn" onclick="saveFundAgence();">保存</button>
    <button class="button_main button_primary-cancle" onclick="hideFundAgenceBox(this);">取消</button>
  </div>
</div>
