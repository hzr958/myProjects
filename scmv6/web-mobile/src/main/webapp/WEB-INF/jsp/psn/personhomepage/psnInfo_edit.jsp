<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%><html>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<head>
    <title></title>
    <meta charset="utf-8">
    <meta name="viewport" content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
    <link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/mobile.css">
    <link href="${resmod}/mobile/css/plugin/scm.pop.mobile.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${resmod}/js/jquery.js"></script>
    <script type="text/javascript" src="${resmod}/js/common/smate.common.js"></script>
    <script type="text/javascript" src="${resmod}/mobile/js/plugin/scm.pop.mobile.js"></script>
    <script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
    <script type="text/javascript" src="${resmod}/js/homepage/mobile/mobile.homepage.psninfo.js"></script>
    <script type="text/javascript">
    window.onload = function(){
      initRegion();
      initWorkHistory();
      $(".new-edit_psntitle-body_item-input_areabox").closest(".new-edit_psntitle-body_item-box").find(".new-edit_psntitle-body_item-detail").addClass("new-edit_psntitle-body_item-detail_inputed");
      $(".new-edit_psntitle-body_item-input_areabox").closest(".new-edit_psntitle-body_item-box").addClass("new-edit_psntitle-body_item-box_selected");
      $(".new-edit_psntitle-body_item-input_areabox").closest(".new-edit_psntitle-body_item-box_selected").addClass("new-edit_psntitle-body_item-box_underline");
      var inputlist = document.getElementsByClassName("new-edit_psntitle-body_item-input");
      var containerele = document.getElementsByClassName("new-edit_psntitle-body")[0];
      containerele.style.height =  document.body.clientHeight -  96 + "px";
      window.onresize = function(){
          containerele.style.height =  document.body.clientHeight -  96 + "px";
      }
      for(var i = 0; i < inputlist.length; i++){
          inputlist[i].onfocus = function(){
            this.closest(".new-edit_psntitle-body_item-box").querySelector(".new-edit_psntitle-body_item-detail").classList.add("new-edit_psntitle-body_item-detail_selected");
            if(this.closest(".new-edit_psntitle-body_item-box").classList.contains("new-edit_psntitle-body_item-box_underline")){
                this.closest(".new-edit_psntitle-body_item-box").classList.remove("new-edit_psntitle-body_item-box_underline");
            }
            if(this.classList.contains("new-edit_psntitle-body_item-input_down")){
                this.closest(".new-edit_psntitle-body_item-box").querySelector(".new-edit_psntitle-body_item-box_selector").style.display = "block";
            }
            this.closest(".new-edit_psntitle-body_item-box").classList.add("new-edit_psntitle-body_item-box_selected");
            if(this.classList.contains("region-select_btn")){
               var  url = this.getAttribute("code-url");
               getregionselect(url);
             }
          }
          inputlist[i].onblur = function(){
            if(this.closest(".new-edit_psntitle-body_item-box").querySelector(".new-edit_psntitle-body_item-detail").classList.contains("new-edit_psntitle-body_item-detail_selected")){
              this.closest(".new-edit_psntitle-body_item-box").querySelector(".new-edit_psntitle-body_item-detail").classList.remove("new-edit_psntitle-body_item-detail_selected");
              this.closest(".new-edit_psntitle-body_item-box").classList.remove("new-edit_psntitle-body_item-box_selected");
              var gettext = this.closest(".new-edit_psntitle-body_item-box").querySelector(".new-edit_psntitle-body_item-input").value.trim();
              if(gettext != ""){
                      this.closest(".new-edit_psntitle-body_item-box").querySelector(".new-edit_psntitle-body_item-detail").classList.add("new-edit_psntitle-body_item-detail_inputed");
                      this.closest(".new-edit_psntitle-body_item-box").querySelector(".new-edit_psntitle-body_item-input").classList.add("new-edit_psntitle-body_item-input-show");
              }else{
                if(this.closest(".new-edit_psntitle-body_item-box").querySelector(".new-edit_psntitle-body_item-detail").classList.contains("new-edit_psntitle-body_item-detail_inputed")){
                         this.closest(".new-edit_psntitle-body_item-box").querySelector(".new-edit_psntitle-body_item-detail").classList.remove("new-edit_psntitle-body_item-detail_inputed");
                         this.closest(".new-edit_psntitle-body_item-box").querySelector(".new-edit_psntitle-body_item-input").classList.remove("new-edit_psntitle-body_item-input-show");
                }
              }
            }
          }
        }
  }
      
    $(function(){
        $( ".new-edit_psntitle-body_item-input").each(function( index ) {
          if($(this).val() != null && $(this).val() != ""){
            $(this).addClass("new-edit_psntitle-body_item-input-show");
            $(this).prev("span").addClass("new-edit_psntitle-body_item-detail_inputed");
          }
        });
    })
    </script>
</head>
<body>
<input type="hidden" id="psnRegionId" value="${psnOperateVO.regionId }" />
<input type="hidden" id="selectRegionId" value />
<input type="hidden" id="psnInsName" value="${psnOperateVO.insName }" />
<input type="hidden" id="psnDepartment" value="${psnOperateVO.department }" />
<input type="hidden" id="psnPosition" value="${psnOperateVO.position }" />
<input type="hidden" id="psnPosition" value="${psnOperateVO.position }" />
<input type="hidden" id="newWorkId" value="${psnOperateVO.workId }" />
       <div class="new-edit_keword-header">
            <i class="material-icons" onclick="javascript:SmateCommon.goBack('/psnweb/mobile/homepage');">keyboard_arrow_left</i>
            <span class="new-edit_keword-header_title">编辑个人头衔</span> 
            <i class="material-icons" ></i>
        </div>
        <div class="new-edit_psntitle-body" style=" margin-top: 60px;z-index: 5;">
            
            <div class="new-edit_psntitle-body_item">
                <div class="new-edit_psntitle-body_item-box">
                    <span class="new-edit_psntitle-body_item-detail">姓</span>
                    <input type="text" class="new-edit_psntitle-body_item-input"  maxlength="40" id="newZhLastName" value="${psnOperateVO.zhLastName }">
                </div>
            </div>

            <div class="new-edit_psntitle-body_item">
                <div class="new-edit_psntitle-body_item-box">
                    <span class="new-edit_psntitle-body_item-detail">名</span>
                    <input type="text" class="new-edit_psntitle-body_item-input" maxlength="20" id="newZhFirstName" value="${psnOperateVO.zhFirstName }">
                </div>
            </div>
            
            <div class="new-edit_psntitle-body_item">
                <div class="new-edit_psntitle-body_item-box">
                    <span class="new-edit_psntitle-body_item-detail">First name*</span>
                    <input type="text" class="new-edit_psntitle-body_item-input" maxlength="40" id="newFirstName" value="${psnOperateVO.firstName }">
                    <span style="position: absolute;top: 50px;left: 0px;font-size: 10px;color: #f00;display:none;" id="firstNameErrorMsg">First name不能为空!</span>
                </div>
            </div>
            
            <div class="new-edit_psntitle-body_item">
                <div class="new-edit_psntitle-body_item-box">
                    <span class="new-edit_psntitle-body_item-detail">Middle name</span>
                    <input type="text" class="new-edit_psntitle-body_item-input" maxlength="61" id="newMiddleName" value="${psnOperateVO.otherName }">
                </div>
            </div>
            
            <div class="new-edit_psntitle-body_item">
                <div class="new-edit_psntitle-body_item-box">
                    <span class="new-edit_psntitle-body_item-detail">Last name*</span>
                    <input type="text" class="new-edit_psntitle-body_item-input" maxlength="20" id="newLastName" value="${psnOperateVO.lastName }">
                    <span style="position: absolute;top: 50px;left: 0px;font-size: 10px;color: #f00;display:none;" id="lastNameErrorMsg">Last name不能为空!</span>
                </div>
            </div>
            
            <div class="new-edit_psntitle-body_item">
                <div class="new-edit_psntitle-body_item-box">
                    <span class="new-edit_psntitle-body_item-detail">所在单位</span>
                    <input type="text" class="new-edit_psntitle-body_item-input new-edit_psntitle-body_item-input_down" style="text-overflow: ellipsis;" id="workHistory" onclick="openWorkHistory();" readonly="readonly">
                    <div class="new-edit_psntitle-body_item-box_selector" id="workHistorySelect">
                    </div>
                </div>
            </div>

            <div class="new-edit_psntitle-body_item">
                <div class="new-edit_psntitle-body_item-box">
                    <span class="new-edit_psntitle-body_item-detail">头衔</span>
                    <input type="text" class="new-edit_psntitle-body_item-input" maxlength="200" id="newTitolo" value="${psnOperateVO.titolo }">
                </div>
            </div>

            <div class="new-edit_psntitle-body_item">
                <div class="new-edit_psntitle-body_item-box">
                    <span class="new-edit_psntitle-body_item-detail">所在地区</span>
                    <input type="text" class="new-edit_psntitle-body_item-input  region-select_btn new-edit_psntitle-body_item-input_areabox" code-url="/psn/mobile/psnInfo/ajaxRegion" id="newRegionId">
                </div>
            </div>
        </div>
        <div class="new-edit_page-normal_footer" onclick="savePsnInfo();">保存</div>
</body>
</html>