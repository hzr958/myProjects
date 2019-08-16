<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<style type="text/css">
.area__list {
  display: flex;
  justify-content: flex-start;
}

.input__area-itemlist {
  display: flex;
  align-items: center;
  justify-content: center;
}

.input__area-itemlist .select-icon {
  margin-right: 4px;
  cursor: pointer;
}

.input__area-itemlist-title {
  font-size: 14px;
}

.formtip-word {
  position: absolute;
  top: 22px;
  width: 125px;
  min-height: 24px;
  height: auto;
  border: 1px solid #ccc;
  display: none;
  opacity: 1;
}

.forminput-word {
  border-bottom: 1px solid #ccc;
  width: 125px;
  position: relative;
}

.tip-container {
  position: absolute;
  top: 28px;
  left: 4px;
  opacity: 1;
  background-color: #fff;
  z-index: 99999;
}

.tip-container_item {
  position: fixed;
  opacity: 1;
  background-color: #FFF;
  border: 1px solid #ccc;
  width: 136px;
  height: auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  z-index: 99999;
}

.tip-container_item-list {
  width: 136px;
  text-align: center;
  margin: 2px 0px;
  cursor: pointer;
}

.tip-container_item-list:hover {
  background-color: #f4f4f4;
}
</style>
<script type="text/javascript">
		var seniority = "${seniority}";

		$(document).ready(function(){
			initRegion();
			var clicklist = document.getElementsByClassName("select-icon");
			for(var i = 0; i < clicklist.length;i++){
                clicklist[i].onclick = function(){
                	var val = $(this).attr("val");
                    if(this.classList.contains("iconselected")){
                    	FundRecommend.dealSeniority(val, -1);
                  	    this.classList.remove("iconselected");
                  	    this.querySelector("i").innerHTML="check_box_outline_blank";
                    }else{
                    	FundRecommend.dealSeniority(val, 1);
                        this.classList.add("iconselected");
                        this.querySelector("i").innerHTML="check_box";
                    }
                }
			}
			
			initCheckBox();
			var inputlist = document.getElementsByClassName("forminput-word");
			var dnv = "";
			if (inputlist.length > 0) {
				if ('${locale}' == 'zh_CN') {
					dnv = '<div class="tip-container_item">'
							+ '<div class="tip-container_item-list" data-code="110000">北京市</div>'
							+ '<div class="tip-container_item-list" data-code="120000">天津市</div>'
							+ '<div class="tip-container_item-list" data-code="340000">安徽省</div>'
							+ '<div class="tip-container_item-list" data-code="140000">山西省</div>'
							+ '<div class="tip-container_item-list" data-code="150000">内蒙古自治区</div>'
							+ '<div class="tip-container_item-list" data-code="210000">辽宁省</div>'
							+ '<div class="tip-container_item-list" data-code="220200">吉林省</div>'
							+ '<div class="tip-container_item-list" data-code="230000">黑龙江省</div>'
							+ '<div class="tip-container_item-list" data-code="310000">上海市</div>'
							+ '<div class="tip-container_item-list" data-code="330000">浙江省 </div>'
							+ '</div>';
				} else {
					dnv = '<div class="tip-container_item">'
							+ '<div class="tip-container_item-list" data-code="110000">Beijing</div>'
							+ '<div class="tip-container_item-list" data-code="120000">Tianjin</div>'
							+ '<div class="tip-container_item-list" data-code="340000">Anhui</div>'
							+ '<div class="tip-container_item-list" data-code="140000">Shanxi</div>'
							+ '<div class="tip-container_item-list" data-code="150000">Inner Mongolia Autonomous Region</div>'
							+ '<div class="tip-container_item-list" data-code="210000">Liaoning</div>'
							+ '<div class="tip-container_item-list" data-code="220200">Jilin</div>'
							+ '<div class="tip-container_item-list" data-code="230000">Heilongjiang</div>'
							+ '<div class="tip-container_item-list" data-code="310000">Shanghai</div>'
							+ '<div class="tip-container_item-list" data-code="330000">Zhejiang</div>'
							+ '</div>';
				}
			}
			      for(var i = 0; i<inputlist.length; i++){
			        inputlist[i].onfocus = function(){
			          var inputext = this.value.trim() + "";
			          var textlength = inputext.length;
			          if(textlength===0){
			          var dlv = document.createElement("div");
			          dlv.innerHTML= dnv;
			          dlv.className ="tip-container";
			          this.closest(".forminput-box").appendChild(dlv);
			          var selectitem = document.getElementsByClassName("tip-container_item-list");
			          for(var j = 0; j<selectitem.length; j++){
			            selectitem[j].onmousedown = function(){
			            	const $this = this;
			            	$this.closest(".forminput-box").querySelector(".forminput-word").focus();
			                var selectext = $this.innerText;
			                $this.closest(".forminput-box").querySelector(".forminput-word").value = selectext;
			                $this.closest(".forminput-box").querySelector(".forminput-word").setAttribute("data-code",$this.getAttribute("data-code")); 
			            }
			          }
			         }
			        }
			      inputlist[i].onblur = function(){
			          var dlv = this.closest(".forminput-box").querySelector(".tip-container");
			          if(dlv != null){		        	  
			             this.closest(".forminput-box").removeChild(dlv);
			          }
			        }
			      inputlist[i].onkeyup = function(){
			            var dlv = this.closest(".forminput-box").querySelector(".tip-container");
			            if(dlv != null){	
			                this.closest(".forminput-box").removeChild(dlv);
			            }
			      }
			      }
		});
		
		function initCheckBox(){
			var clicklist = document.getElementsByClassName("select-icon");
			for(var i = 0; i < clicklist.length;i++){
				var $this = $(clicklist[i]);
				var val = $this.attr("val");
				if(seniority.indexOf(val) > -1){
					$this.addClass("iconselected");
					$this.find("i").html("check_box");
				}
			}
		}
		function getJSON(obj){
			return {"superRegionId":obj.attr("code")};
		}
		
		function initRegion(){
			for(var i=1; i<4; i++){
				$("#area_"+i).attr("value", $("#region_"+i).val());
				$("#area_"+i).attr("code", $("#region_"+i).attr("code"));
			}
		}
	</script>
<input type="hidden" name="seniorityIds" value="${seniority }" id="seniorityIds" />
<div class="dialogs__box oldDiv setnormalZindex" style="width: 640px; height: auto;" dialog-id="fundConditionsBox" cover-event=""
  id="fundConditionsBox">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='homepage.fundmain.recommend.criteriaSet' />
      </div>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="dialogs__content" style="padding: 24px 24px 4px 24px !important;">
      <form>
        <div class="form__sxn_row" style="display: flex; flex-direction: column; margin-bottom: 64px;">
          <div>
            <s:text name='homepage.fundmain.recommend.followArea' />
          </div>
          <s:iterator value="regionInfo" id="reg" status="sta">
            <s:if
              test="#reg.showRegionName != null && #reg.showRegionName != '' && #reg.showRegionName != '不限' && #reg.showRegionName != 'all'">
              <input type="hidden" id="region_${sta.count}" value="${reg.showRegionName }" code="${reg.regionId }" />
            </s:if>
          </s:iterator>
          <div style="display: flex; justify-content: space-between; margin-top: 24px;">
            <div class="forminput-box" style="position: relative;">
              <input id="area_1" class="forminput-word chip-panel__manual-input js_autocompletebox "
                request-url="/psnweb/homepage/ajaxautoregion" data-src="request" manual-input="no" value="" code=""
                style="padding: 4px; margin: 4px;"
                placeholder="<s:text name='homepage.fundmain.recommend.followArea1' />">
            </div>
            <div class="forminput-box" style="position: relative;">
              <input id="area_2" class="forminput-word chip-panel__manual-input js_autocompletebox "
                request-url="/psnweb/homepage/ajaxautoregion" data-src="request" manual-input="no" value="" code=""
                style="padding: 4px; margin: 4px;"
                placeholder="<s:text name='homepage.fundmain.recommend.followArea2' />">
            </div>
            <div class="forminput-box" style="position: relative;">
              <input id="area_3" class="forminput-word chip-panel__manual-input js_autocompletebox "
                request-url="/psnweb/homepage/ajaxautoregion" data-src="request" manual-input="no" value="" code=""
                contenteditable="true" style="padding: 4px; margin: 4px;"
                placeholder="<s:text name='homepage.fundmain.recommend.followArea3' />">
            </div>
          </div>
        </div>
        <input type="hidden" name="psnRegionId" id="psnRegionId" value="440300">
        <div class="form__sxn_row">
          <div class="input__box input_not-null" style="margin: 0px !important;">
            <div style="font-size: 14px !important; color: black;">
              <s:text name='homepage.fundmain.recommend.eligibility' />
            </div>
            <div class="input__area area__list" style="margin-top: 24px;">
              <div class="input__area-itemlist">
                <div class="select-icon" val="1">
                  <i class="material-icons">check_box_outline_blank</i>
                </div>
                <div class="input__area-itemlist-title">
                  <s:text name='homepage.fundmain.recommend.enterprise' />
                </div>
              </div>
              <div class="input__area-itemlist" style="margin-left: 176px;">
                <div class="select-icon" val="2">
                  <i class="material-icons">check_box_outline_blank</i>
                </div>
                <div class="input__area-itemlist-title">
                  <s:text name='homepage.fundmain.recommend.institution' />
                </div>
              </div>
            </div>
            <div class="input__helper"></div>
          </div>
        </div>
      </form>
    </div>
  </div>
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__footer">
      <button class="button_main button_primary-reverse" onclick="javascript:FundRecommend.showScienceAreaBox($(this));">
        <s:text name='homepage.fundmain.btn.next' />
      </button>
      <button class="button_main" onclick="javascript:hideDialog('fundConditionsBox');">
        <s:text name='homepage.fundmain.btn.cancel' />
      </button>
    </div>
  </div>
</div>
