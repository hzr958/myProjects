<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
  <title></title>
  <meta charset="utf-8">
  <link href="${resmod}/smate-pc/css/scmpcframe.css" rel="stylesheet" type="text/css">
  <script type="text/javascript" src="${resprj}/sns/institution/institution.base.js"></script>
  <style>
    .dev_error_tip{
      border-color: #bf3127!important;
      border: 1px solid #bf3127!important;
    }


  </style>
  <script type="text/javascript">
      window.onload = function(){
          var changelist = document.getElementsByClassName("create-Homepage_Option-item");
          for(var i = 0; i < changelist.length; i++){
              changelist[i].addEventListener("click",function(){
                  if(!this.classList.contains("create-Homepage_Option-item_checked")){
                      if(document.getElementsByClassName("create-Homepage_Option-item_checked").length > 0){
                          document.getElementsByClassName("create-Homepage_Option-item_checked")[0].classList.remove("create-Homepage_Option-item_checked");
                      }
                      this.classList.add("create-Homepage_Option-item_checked");
                  }
              })
          }


      }

      $(function(){
          addFormElementsEvents();
          Institution.bindInsPropertyEvent();
      });

      function getInsArea(){
          $("#area_erroe_tip").removeClass("dev_error_tip");
          var $valueBox=document.querySelector('.sel-dropdown__box[selector-data="1st_area"]');
          $("#psnRegionId").val($valueBox.querySelector('*[selected="selected"]').getAttribute("sel-itemvalue"));
          var $selectBox = document.querySelector('.sel__box[selector-id="2nd_area"]');
          if ($selectBox.style.visibility === "visible"&&$(".sel__box[selector-id='1st_area']").attr("sel-nextlevel")!="true") {
              $selectBox.style.visibility = "hidden";
              var $selectBox3 = document.querySelector('.sel__box[selector-id="3nd_area"]').style.visibility = "hidden";
          }
          if ($selectBox.style.visibility === "hidden"&&$(".sel__box[selector-id='1st_area']").attr("sel-nextlevel")=="true") {
              $selectBox.style.visibility = "visible";
          } else {
              $selectValue = $selectBox.querySelector("span");
              $selectValue.textContent = "选择二级区域";
              $selectValue.classList.add("sel__value_placeholder");
              $selectBox.setAttribute("sel-value","");
              document.querySelector('.sel__box[selector-id="3nd_area"]').style.visibility = "hidden";
          }
      }

      function getInsSubArea(){
          var $valueBox=document.querySelector('.sel-dropdown__box[selector-data="2nd_area"]');
          $("#psnRegionId").val($valueBox.querySelector('*[selected="selected"]').getAttribute("sel-itemvalue"));
          var $selectBox = document.querySelector('.sel__box[selector-id="3nd_area"]');
          if ($selectBox.style.visibility === "visible"&&$(".sel__box[selector-id='2nd_area']").attr("sel-nextlevel")!="true") {
              $selectBox.style.visibility = "hidden";
          }
          if ($selectBox.style.visibility === "hidden"&&$(".sel__box[selector-id='2nd_area']").attr("sel-nextlevel")=="true") {
              $selectBox.style.visibility = "visible";
          } else {
              $selectValue = $selectBox.querySelector("span");
              $selectValue.textContent = "选择三级区域";
              $selectValue.classList.add("sel__value_placeholder");
              $selectBox.setAttribute("sel-value","");
          }
      }

      function getInsRegionId (){
          var $valueBox=document.querySelector('.sel-dropdown__box[selector-data="3nd_area"]');
          $("#psnRegionId").val($valueBox.querySelector('*[selected="selected"]').getAttribute("sel-itemvalue"));
      }
      var getInsAreaJSON =function () {
          var $valueBox=document.querySelector('.sel__box[selector-id="1st_area"]');
          return {"superRegionId":$valueBox.getAttribute("sel-value")};
      };
      function getInsSubAreaJSON (){
          var $valueBox=document.querySelector('.sel__box[selector-id="2nd_area"]');
          return {"superRegionId":$valueBox.getAttribute("sel-value")};
      }
  </script>
</head>
<body>

<div class="create-Homepage_Container">
  <div  class="create-Homepage_Header">
    创建主页
  </div>
  <div  class="create-Homepage_Neck">
    与科研人员、创新人员及科研之友社区建立联系，请选择主页类型
  </div>
  <div  class="create-Homepage_Option">
    <input  type="hidden" id="nature" name="nature" value="1">
    <div class="create-Homepage_Option-item create-Homepage_Option-item_checked" item="1">
      <span class="create-Homepage_Option-item_title">大学</span>
      <span class="create-Homepage_Option-item_detail">高等院校、成人教育等</span>
    </div>
    <div class="create-Homepage_Option-item" item="2">
      <span class="create-Homepage_Option-item_title">企业</span>
      <span class="create-Homepage_Option-item_detail">公司、企业等</span>
    </div>
    <div class="create-Homepage_Option-item" item="0">
      <span class="create-Homepage_Option-item_title">其他组织</span>
    </div>
  </div>
  <div  class="create-Homepage_body">

    <div class="create-Homepage_body-item">
      <div class="create-Homepage_body-item_title">
        <span class="create-Homepage_body-item_tip">*</span>
        <span>机构名称：</span>
      </div>
      <div class="create-Homepage_body-item_aditlarge  targetloca-input_container-tipborder">
        <input type="text" id="insName" name="insName" maxlength="20">
        <div class="error_message-prompt error_message-rightprompt" id="insNameError" style="display: none;top: -4px;">
          <div class="error_message-prompt_side error_message-prompt_rightside">
            <div class="error_message-prompt_detail">机构主页已经存在，<a target="_blank" href="#" id="guide_complain">查看并申诉？</a></div>
            <div class="error_message-prompt_icon-right"></div>
          </div>
        </div>
      </div>
    </div>
    <div class="create-Homepage_body-item">
      <div class="create-Homepage_body-item_title">
        <span class="create-Homepage_body-item_tip">*</span>
        <span>科研之友公开网址：</span>
      </div>
      <div class="create-Homepage_body-item_aditnormal">
        <input type="text" id="insDomain" maxlength="10"><span style="position: absolute;right: -145px;display: flex;    top: 2px;">.scholarmate.com
        <i class="find-nothing_tip" title="" onmouseleave="Institution.hideScmDomainTip();" onmouseenter="Institution.showScmDomainTip();"></i></span>
        <div class="error_message-prompt error_message-rightprompt" id="insDomainTip" style="display: none;right: -423px;top: -4px;">
          <div class="error_message-prompt_side error_message-prompt_rightside">
            <div class="error_message-prompt_detail">科研之友的用户和搜索引擎将会通过该唯一网址找到你的主页。</div>
            <div class="error_message-prompt_icon-right"></div>
          </div>
        </div>
        <div class="error_message-prompt error_message-rightprompt" id="insDomainError" style="display: none;right: -423px;top: -4px;">
          <div class="error_message-prompt_side error_message-prompt_rightside">
            <div class="error_message-prompt_detail">机构域名已经存在</div>
            <div class="error_message-prompt_icon-right"></div>
          </div>
        </div>
        <div class="error_message-prompt error_message-rightprompt" id="insDomainRule" style="display: none;right: -423px;top: -4px;">
          <div class="error_message-prompt_side error_message-prompt_rightside">
            <div class="error_message-prompt_detail">请输入数字或者字母</div>
            <div class="error_message-prompt_icon-right"></div>
          </div>
        </div>
      </div>

    </div>

    <div class="create-Homepage_body-item">
      <div class="create-Homepage_body-item_title">
        <span>机构网站：</span>
      </div>
      <div class="create-Homepage_body-item_aditnormal">
        <input type="text"  placeholder="以http:// 或 https:// 或 www. 开头" id="insUrl"  name="insUrl" maxlength="100">
      </div>
    </div>

    <div class="create-Homepage_body-item">
      <div class="create-Homepage_body-item_title">
        <span class="create-Homepage_body-item_tip">*</span>
        <span>所属行业：</span>
      </div>
      <div class="create-Homepage_body-item_aditnormal targetloca-input_container-tipborder"   style="border: none; flex-direction: column; align-items: flex-start; height: auto;" >
        <div class="handin_import-content_container-right_area-content handin_import-content_rightbox-border" id="eco_code_error" style="height: 30px"  onclick="Institution.showEconomicBox('1')">
          <div class="handin_import-content_container-right_area-content json_scienceArea" name="economicName" style="display: flex; align-items: center;"> 
            <input type="hidden" id="eco_code_id1"/>
            <input type="hidden" id="eco_code_id2"/>
            <input type="text" placeholder="请选择行业" id="eco_code_id" name="eco_code_id" onfocus="this.blur()" readonly="" unselectable="on" disabled="disabled" value="" style="margin-left: 2px; background: #fff;">
            <input type="hidden" class="json_scienceArea_scienceAreaId" value="">
            <input type="hidden" class="json_scienceArea_scienceAreaId" value="">
          </div>
          <i class="selected-func_field"></i>
        </div>
        <div class="json_scienceArea_scienceAreaId_msg" style="display: none"></div>
        <div class="create-Homepage_body-item_aditnormal-toplayer"  onclick="Institution.showEconomicBox('1')"></div>
      </div>
    </div>


    <div class="create-Homepage_body-item">
      <div class="create-Homepage_body-item_title">
        <span>新兴产业：</span>
      </div>
      <div class="create-Homepage_body-item_aditnormal"  style="border: none; flex-direction: column; align-items: flex-start; height: auto;" >
        <div class="handin_import-content_container-right_area-content handin_import-content_rightbox-border" style="height: 30px"  onclick="Institution.showCseiBox('1')">
          <div class="handin_import-content_container-right_area-content json_scienceArea" name="scienceAreaName" style="display: flex; align-items: center;">

            <input type="hidden" id="csei_code_id1"/>
            <input type="hidden" id="csei_code_id2"/>
            <input type="text" placeholder="请选择产业" id="csei_code_id" name="csei_code_id" onfocus="this.blur()" readonly="" unselectable="on" disabled="disabled" value="" style="margin-left: 2px; background: #fff;">
            <input type="hidden" class="json_scienceArea_scienceAreaId" value="">
            <input type="hidden" class="json_scienceArea_scienceAreaId" value="">
          </div>
          <i class="selected-func_field"></i>
        </div>
        <div class="json_scienceArea_scienceAreaId_msg" style="display: none"></div>
        <div class="create-Homepage_body-item_aditnormal-toplayer" onclick="Institution.showCseiBox('1')"></div>
      </div>
    </div>



    <div class="create-Homepage_body-item" id="other_nature" style="display: none">
      <div class="create-Homepage_body-item_title">
        <span class="create-Homepage_body-item_tip">*</span>
        <span>性质：</span>
      </div>
      <div class="create-Homepage_body-item_aditnormal"  id="property_error" onclick="Institution.openInsProperty(this);">
        <div class="handin_import-content_container-right_select"  style="    width: 350px">
          <div class="handin_import-content_container-right_select-box">
            <input type="text" class="dev-detailinput_container dev_select_input" id="showPropertyName" readonly="" onfocus="this.blur()" unselectable="on" value="">
          </div>
          <i class="material-icons handin_import-content_container-right_select-tip dev_ins_property_tip" style="padding-right: 0px !important;">arrow_drop_down</i>
          <div class="handin_import-content_container-right_select-detail dev_select-detail" style="display: none;">
            <div class="handin_import-content_container-right_select-item dev_ins_property">
              <span class="handin_import-content_container-right_select-item_detail" value="3">医疗机构</span>
            </div>
            <div class="handin_import-content_container-right_select-item dev_ins_property">
              <span class="handin_import-content_container-right_select-item_detail" value="4">研究机构</span>
            </div>
            <div class="handin_import-content_container-right_select-item dev_ins_property">
              <span class="handin_import-content_container-right_select-item_detail" value="5">政府机构</span>
            </div>
            <div class="handin_import-content_container-right_select-item dev_ins_property">
              <span class="handin_import-content_container-right_select-item_detail" value="6">出版机构</span>
            </div>
            <div class="handin_import-content_container-right_select-item dev_ins_property">
              <span class="handin_import-content_container-right_select-item_detail" value="7">虚拟机构</span>
            </div>
            <div class="handin_import-content_container-right_select-item dev_ins_property">
              <span class="handin_import-content_container-right_select-item_detail" value="99">其他</span>
            </div>

          </div>
        </div>
      </div>
    </div>

    <div class="create-Homepage_body-item">
      <div class="create-Homepage_body-item_title">
        <span  class="create-Homepage_body-item_tip">*</span>
        <span>所在地区：</span>
      </div>
      <div class="create-Homepage_body-item_aditnormal" id="area_erroe_tip" style="border: none;">
        <div class="form__sxn_row" style=" width: 100%; display: flex;  justify-content: space-around;">
          <div class="input__box no-flex input_not-null">
            <div class="sel__box" selector-id="1st_area" sel-value="" sel-nextlevel="undefined">
              <div class="sel__value" style="display: flex; align-items: center;">
                <span class="sel__value_selected">请选择国家</span> <i class="material-icons sel__dropdown-icon">arrow_drop_down</i>
              </div>
            </div>
            <div class="input__helper" invalid-message=""></div>
          </div>
          <div class="input__box no-flex input_not-null">
            <label class="input__title"></label>
            <div class="sel__box" style="visibility: hidden;" selector-id="2nd_area" sel-value="" sel-nextlevel="true">
              <div class="sel__value" style="display: flex; align-items: center;">
                <span class="sel__value_selected sel__value_placeholder">选择二级地区</span> <i class="material-icons sel__dropdown-icon">arrow_drop_down</i>
              </div>
            </div>
            <div class="input__helper" invalid-message=""></div>
          </div>
          <div class="input__box no-flex input_not-null">
            <label class="input__title"></label>
            <div class="sel__box" style="visibility: hidden;" selector-id="3nd_area">
              <div class="sel__value" style="display: flex; align-items: center;">
                <span class="sel__value_selected sel__value_placeholder">选择三级地区</span> <i class="material-icons sel__dropdown-icon">arrow_drop_down</i>
              </div>
            </div>
            <div class="input__helper" invalid-message=""></div>
          </div>
        </div>
      </div>
    </div>

    <div class="create-Homepage_body-item" style="align-items: flex-start;">
      <div class="create-Homepage_body-item_title">
        <span>关于我们：</span>
      </div>
      <div class="create-Homepage_body-item_edit">
        <textarea  placeholder="机构介绍，限400字符" id="description" style="min-height: 250px;" name="description" class="create-Homepage_body-item_editbox" maxlength="400"></textarea>

        <div class="create-Homepage_body-item_edittip">
          <i class="material-icons create-Homepage_body-item_edittip-icon"  id="service_terms" onclick="Institution.changeServiceTerms()">check_box</i>
          <div class="create-Homepage_body-item_edittext">我证明我是该组织的官方代表，且有权代表创建和管理该主页，该组织和我均同意遵守
            <span style="color: #2882d8;">
            <a href="https://sie.scholarmate.com/ressie/help/help_center.html?type=3" target="_blank" rel="nofollow" class="checkbox-tk">《服务条款》</a>
          </span></div>
        </div>

        <div class="create-Homepage_body-item_edit-now" onclick="Institution.saveInsPage(this);">立即创建</div>
      </div>
    </div>
  </div>
</div>

<!-- 经济产业弹出框 -->
<div class="dialogs__box dialog-normal_Width-size" dialog-id="economicBox" style="width: 720px;" cover-event="" id="economicBox"
     process-time="0"></div>
<!-- 经济产业弹出框 -->

<!-- 地区事件 -->
<div class="sel-dropdown__box" selector-data="1st_area" data-type="json" item-event="getInsArea()"
     data-src="request" request-url="/psnweb/common/ajaxRegion"></div>
<div class="sel-dropdown__box" selector-data="2nd_area" data-type="json" item-event="getInsSubArea()" data-src="request"
     request-url="/psnweb/common/ajaxRegion" request-data="getInsAreaJSON()"></div>
<div class="sel-dropdown__box" selector-data="3nd_area" data-type="json" item-event="getInsRegionId()" data-src="request"
     request-url="/psnweb/common/ajaxRegion" request-data="getInsSubAreaJSON()"></div>
<!-- 地区事件 -->
</body>
</html>
