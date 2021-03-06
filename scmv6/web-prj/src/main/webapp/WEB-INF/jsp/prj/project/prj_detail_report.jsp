<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!-- 项目报告 -->
<script type="text/javascript">
$(function(){
    //项目报告列表
    NewProject.reportList('${des3PrjId}');
    var changflag = document.getElementsByClassName("filter-section__header");
    for(var i = 0;i<changflag.length;i++){
        changflag[i].onclick = function(){
            if(this.querySelector(".filter-section__toggle").innerHTML == "expand_less"){
                this.querySelector(".filter-section__toggle").innerHTML="expand_more";
                this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="block";
            }else{
                this.querySelector(".filter-section__toggle").innerHTML = "expand_less";
                 this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="none";
            }
        } 
    }
    var targetlist = document.getElementsByClassName("searchbox__main");
    for(var i = 0; i< targetlist.length; i++){
        targetlist[i].querySelector("input").onfocus = function(){
            this.closest(".searchbox__main").style.cssText = "border:1px solid #3faffa;";
        }
        targetlist[i].querySelector("input").onblur = function(){
                this.closest(".searchbox__main").style.cssText = "border:1px solid #ccc;";
        }
    }
});

window.onload = function(){
    var hiddenlist = document.getElementsByClassName("filter-section__toggle");
    for( var i = 0;i < hiddenlist.length; i++){
            hiddenlist[i].onclick = function(){
                if(this.innerHTML == "expand_less"){
                    this.innerHTML = "expand_more";
                    this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="none";
                }else{
                    this.innerHTML = "expand_less";
                    this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="block";
                }
            }
    }
}
</script>
<div class="container__horiz">
  <div style="display: flex; width: 100%;">
    <input id="dev_pubconfirm_isall" type="hidden" value="one" /> <input id="dev_pubftconfirm_isall" type="hidden"
      value="one" /> <input id="dev_pubcooperator_isall" type="hidden" value="one" /> <input id="whiteListKey"
      type="hidden" value="${psnId}" />
    <div style="width: 280px; height: auto; margin-right: 16px;">
      <!-- 检索条件  begin -->
      <jsp:include page="prj_report_conditions.jsp" />
      <!-- 检索条件  end -->
    </div>
    <div style="width: 100%; max-width: 880px;">
      <div class="container__card">
        <div class="main-list">
          <div class="main-list__header">
          <div class="main-list-header__title" style="display: flex; font-size: 15px;">
            <div class="main-list-header__title-item">
                                       报告数:&nbsp;<span class="main-list-header__title-item_num" id="prjReportNumbers">${reportCount}</span>
            </div>
            </div>
          </div>
          <div class="main-list__list dev_pub_list" list-main="prjreport"></div>
          <div class="main-list__footer">
            <div class="pagination__box" list-pagination="prjreport">
              <!-- 翻页 -->
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>