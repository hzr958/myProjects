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
    <script type="text/javascript" src="${resmod}/js/homepage/mobile/mobile.homepage.psnhistory.js"></script>
    <script type="text/javascript">
    var isNullFlag = true;
    var fromDataFlag = false;
    var toDataFlag = false;
    var isActive = ${psnOperateVO.isActive};
        window.onload = function(){
            var containerele = document.getElementsByClassName("new-edit_psntitle-body")[0];
            containerele.style.height =  document.body.clientHeight -  96 + "px";
            window.onresize = function(){
                containerele.style.height =  document.body.clientHeight -  96 + "px";
            }
            var inputlist = document.getElementsByClassName("new-edit_psntitle-body_item-input");
            for(var i = 0; i < inputlist.length; i++){
                inputlist[i].onfocus = function(){
                    this.closest(".new-edit_psntitle-body_item-box").querySelector(".new-edit_psntitle-body_item-detail").classList.add("new-edit_psntitle-body_item-detail_selected");
                    this.closest(".new-edit_psntitle-body_item-box").classList.add("new-edit_psntitle-body_item-box_selected");
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
          $("#newInsName").bind('blur',checkInsNameNull);
          $("#work_from_year").bind('blur',checkFromDate);
          $("#work_from_month").bind('blur',checkFromDate);
          $("#work_to_year").bind('blur',checkToDate);
          $("#work_to_month").bind('blur',checkToDate);
          if( $("#type").val() != "add"){
            $( ".new-edit_psntitle-body_item-input").each(function( index ) {
              if($(this).val() != null && $(this).val() != ""){
                $(this).addClass("new-edit_psntitle-body_item-input-show");
                $(this).prev("span").addClass("new-edit_psntitle-body_item-detail_inputed");
              }
            });
            isNullFlag = false;
          }
          setYear();
          setMonth();
        })
        
    </script>
</head>
<body>
<input type="hidden" id="nowTime" value="${psnOperateVO.nowTime}"/>
<input type="hidden" id="workId" value="${psnOperateVO.workId}"/>
<input type="hidden" id="type" value="${psnOperateVO.type}"/>
<input type="hidden" id="fromYear" value="${psnOperateVO.fromYear}"/>
<input type="hidden" id="fromMonth" value="${psnOperateVO.fromMonth}"/>
<input type="hidden" id="toYear" value="${psnOperateVO.toYear}"/>
<input type="hidden" id="toMonth" value="${psnOperateVO.toMonth}"/>
        <div class="new-edit_keword-header">
            <i class="material-icons" onclick="javascript:SmateCommon.goBack('/psnweb/mobile/homepage');">keyboard_arrow_left</i>
            <span class="new-edit_keword-header_title">${psnOperateVO.type=="add"?"新增":"编辑"}工作经历</span> 
            <i class="material-icons"></i>
            
        </div>
        <div class="new-edit_psntitle-body" style=" margin-top: 60px;">
            
            <div class="new-edit_psntitle-body_item">
                <div class="new-edit_psntitle-body_item-box">
                    <span class="new-edit_psntitle-body_item-detail">机构名称*</span>
                    <input type="text" class="new-edit_psntitle-body_item-input" autocomplete="new-insName" name="insName" id="newInsName" oninput="showAutoInstitution();" value="${psnOperateVO.insName }" maxlength="200" >
                 <div class="new-edit_psntitle-body_item-input_autoshow" style="display:none;height: auto;" id="autoinstitution"></div>
                </div>
                <div style="font-size: 10px;color: #f00;visibility:hidden;width:90%;text-align:left;" id="insNameErrorMsg">机构名称不能为空!</div>
            </div>

            <div class="new-edit_psntitle-body_item">
                <div class="new-edit_psntitle-body_item-box">
                    <span class="new-edit_psntitle-body_item-detail">部门</span>
                    <input type="text" class="new-edit_psntitle-body_item-input change-input_backgroundcolor" autocomplete="new-department" name="department" oninput="showAutoDepartment();" id="newDepartment" value="${psnOperateVO.department }" maxlength="601">
                    <div class="new-edit_psntitle-body_item-input_autoshow" style="display:none;height: auto;" id="autoinsunit"></div>
                </div>
            </div>
           
            <div class="new-edit_psntitle-body_item">
                <div class="new-edit_psntitle-body_item-box">
                    <span class="new-edit_psntitle-body_item-detail">职称</span>
                    <input type="text" class="new-edit_psntitle-body_item-input change-input_backgroundcolor" autocomplete="new-position" name="position" oninput="showAutoPostion();" id="newPosition" value="${psnOperateVO.position }" maxlength="100">
                    <div class="new-edit_psntitle-body_item-input_autoshow" style="display:none;height: auto;" id="autoposition"></div>
                </div>
            </div>
            
            <div style="width: 100%; display: flex; align-items: center; justify-content: space-around;">
                <div class="new-edit_psntitle-body_item" style="width: 45%;">
                    <div class="new-edit_psntitle-body_item-box">
                        <span class="new-edit_psntitle-body_item-detail new-edit_psntitle-body_item-detail_inputed">开始年份</span>
                        <select class="new-edit_psntitle-body_item-input new-edit_psntitle-body_item-input-show"  name="fromYear" id="work_from_year" value></select> <select class="new-edit_psntitle-body_item-input new-edit_psntitle-body_item-input-show"  name="fromMonth"
          id="work_from_month" value></select>
                    </div>
                    <div style="font-size: 10px;color: #f00;width: 90%; text-align: left;visibility:hidden;" id="fromDateErrorMsg">请填写正确的开始年份!</div>
                </div>
                <div class="new-edit_psntitle-body_item" style="width: 45%;">
                    <div class="new-edit_psntitle-body_item-box" style="border-bottom: 0px;">
                        <span class="new-edit_psntitle-body_item-detail new-edit_psntitle-body_item-detail_inputed">终止年份</span>
                        <select class="new-edit_psntitle-body_item-input new-edit_psntitle-body_item-input-show" style="border-bottom: 1px solid #ddd;" onchange="selectYear();"  name="toYear" id="work_to_year" value>
                        </select> <select class="new-edit_psntitle-body_item-input new-edit_psntitle-body_item-input-show" style="border-bottom: 1px solid #ddd;"  name="toMonth"
          id="work_to_month" value></select>
                    </div>
                    <div style="font-size: 10px;color: #f00;width: 90%; text-align: left;visibility:hidden;" id="toDateErrorMsg">请填写正确的终止年份!</div>
                </div>
            </div>


            <div class="new-edit_psntitle-body_item">
                <div class="new-edit_psntitle-body_item-box">
                    <label class="new-edit_psntitle-body_item-detail">描述</label>
                    <input autofocus type="text" class="new-edit_psntitle-body_item-input" maxlength="200" name="description" id="newDescription" value="${psnOperateVO.description }">
                    <!-- <textarea  class="new-edit_psntitle-body_item-input" style="height: auto; min-height: 20px; "></textarea> -->
                </div>
                <div style="font-size: 14px; color: #666; line-height: 24px; width: 90%; text-align: right;">
                    描述所教课程或所主持的项目
                </div>
            </div>

        </div>
        
        <div class="new-edit_page-normal_footer" onclick="saveWorkHistory();">保存</div>
</body>
</html>