<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" href="${ressie}/css/resect.css" />
<link rel="stylesheet" href="${ressie}/css/reset.css" />
<link rel="stylesheet" href="${ressie}/css/newplugstyle.css" />
<script type="text/javascript" src="${ressie}/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="${ressie}/js/jquery.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/title-tip.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/scm-pc_filedragbox.js"></script>
<script type="text/javascript" src="${resapp }/validate/validate.form.js"></script>
<script type="text/javascript" src="${ressie}/js/plugin/restipboxlist.js"></script>
<title>科研验证</title>
<script type="text/javascript">
$(document).ready(function(){
	 //closeUP();
});
//归拢每一项
function closeUP() {
	  var totalDetailNum = '${totalDetailNum   }';
	    if(totalDetailNum > 7){
	      var setleft =  (window.offsetWidth - document.getElementsByClassName("new-checkinfor_box")[0].offsetWidth)/2 + "px"   
	      var targetscroll1 =  document.getElementsByClassName("new-checkinfor_box1")[0];
	      var targetscroll2 =  document.getElementsByClassName("new-checkinfor_box2")[0];
	      var targetscroll3 =  document.getElementsByClassName("new-checkinfor_box3")[0];
	      var scrollbox2 =  document.getElementsByClassName("new-checklist_container2")[0];
	      var scrollbox3 =  document.getElementsByClassName("new-checklist_container3")[0];
	      window.onscroll = function(){
	          var scrollheight = document.body.scrollTop + document.documentElement.scrollTop; /*页面卷起的高度 leg*/
	          var ter1 = '';
	          if(targetscroll1){
	            ter1 = targetscroll1.offsetTop;                                
	          }
	          var settop = '';
	          if(scrollbox2){
	            settop = scrollbox2.offsetTop;                              
	          }
	          var settop2 = '';
	          if(scrollbox3){
	            settop2 = scrollbox3.offsetTop;                                
	          }
	          if(Math.abs(scrollheight) > ter1){
	            if(targetscroll1){
	            targetscroll1.classList.add("position");
	            targetscroll1.style.left=  setleft;
	            }
	            if(targetscroll2){
	            var ter2 = targetscroll2.offsetTop;
	            }
	          }else if(scrollheight <= ter1){
	            if(targetscroll1){
	              if(targetscroll1.classList.contains("position")){
	                  targetscroll1.classList.remove("position");
	              } 
	            }
	          }
	         if(Math.abs(scrollheight) + 92 >ter2){
	            if(targetscroll2){
	              targetscroll2.classList.add("position2");
	              targetscroll2.style.left=  setleft;
	            }
	            if(targetscroll3){
	              var ter3 = targetscroll3.offsetTop; 
	            }
	            if(scrollbox2){
	              var top = scrollbox2.offsetTop;;
	            }
	         }
	          
	         if(scrollheight + 167 < settop){
	           if(targetscroll2){
	             if(targetscroll2.classList.contains("position2")){
	                  targetscroll2.classList.remove("position2");
	             }
	           }
	         }
	          
	         if(Math.abs(scrollheight) + 167 > ter3){
	           if(targetscroll3){
	            targetscroll3.classList.add("position3");
	            targetscroll3.style.left=  setleft;
	           }
	         }
	         if(scrollheight + 242 < settop2){
	           if(targetscroll3){
	            if(targetscroll3.classList.contains("position3")){
	              targetscroll3.classList.remove("position3");
	            }
	           }
	          }
	      }
	    }
}

function viewValidateResult(misContent,rightContent){
  if('内容无误，验证通过' == misContent){
    $('.mis_content_title').addClass("new-Verifi_mesg-item-title-dis");
    $('.cor_content_title').addClass("new-Verifi_mesg-item-title-dis");
  }else{
    $('.mis_content_title').removeClass("new-Verifi_mesg-item-title-dis");
    $('.cor_content_title').removeClass("new-Verifi_mesg-item-title-dis");
  }
  document.getElementsByClassName('mis_content')[0].innerHTML = misContent;
  document.getElementsByClassName('cor_content')[0].innerHTML = rightContent;
  var a=document.getElementById('popup').innerHTML;
  scmpcnewtip({
      targettitle: '验证信息', 
      targetcllback: '', 
      targettxt: a,
      targetfooter:0
      });
  
}

/*window.onload = function(){
  newSimilarcontainer();
}*/
</script>
<style type="text/css">
        .new-checkinfor_box{
           background: #fff;
           z-index: 120;
        }
        .new-checklist_container{
           z-index: 110;
        }
        .position{
            position: fixed; 
            top: 92px; 
            width: 1200px;
            z-index: 120;
        }
        .position2{
            position: fixed;
            top: 167px; /*32*/ 
            width: 1200px;
            z-index: 120;
        }
        .position3{
            position: fixed;
            top: 242px; /*64*/
            width: 1200px;
            z-index: 120;
        }
        .new-Verifi_mesg-item{
            width: 100%;
            margin: 10px -20px;
            word-break: break-all;
        }
        .new-Verifi_mesg-item-title{
            line-height: 20px; 
            color: #333; 
            font-size: 14px;
        }
        .new-Verifi_mesg-item-content{
            line-height: 20px; 
            color:#666; 
            font-size: 14px;
        }
        .new-Verifi_mesg-item-title-dis{
            display: none;
        }
</style>
</head>
<body>
  <s:if test = "ifOutside == 0">  <!-- 根据是否站外，来确定要不要加载这不部分 -->
    <header>
      <div class="header__2nd-nav" style="z-index: 135;">
        <div class="header__2nd-nav_box" style="justify-content: flex-end;">
          <nav class="nav_horiz nav_horiz-container" style="margin-left: 944px; top: 0px;">
            <ul class="nav__list" scm_file_id="menu__list">
              <li class="nav__item item_selected" onclick="Validate.backList();">科研验证</li>
            </ul>
            <div class="nav__underline" style="width: 75px; left: 9px;"></div>
          </nav>
          <div class="header__2nd-nav_action-list">
            <a href="###" style="margin-right: 0px;"></a>
          </div>
        </div>
      </div>
    </header>
  </s:if>
  <s:if test = "ifOutside == 1">
    <header>
      <div class="header__2nd-nav" style="z-index: 135;">
        
      </div>
    </header>
  </s:if>
  <div class="w1200" style="margin-top: 65px; position: relative;">
    <div class="write_headline">
      <p class="ft20 ftbold" style="line-height: 19px;">
          <span style="margin-right: 8px;">科研验证结果</span>
          <div class="view_span Similar-title_target Similar-title_target-detail" style="max-width: 975px; line-height: 19px;text-align: left; margin-top: 6px;" 
           >${title }</div>
      </p>
      <s:if test = "ifOutside == 0"><!-- =0  时， 加载 -->
          <a href="###" onclick="Validate.backList();" class="derive" style="margin-right: 0px">返回</a>
      </s:if>
    </div>
    <c:if test="${flag eq true}">
      <div class="new-checkinfor_box new-checkinfor_box1">
        <div class="achievement_achievement">
          <p>
            一、人员验证 <span>（总数：<span>${psnCount }</span>；验证通过人数：<span>${psnPass }</span>）
            </span>
          </p>
        </div>
        <div class="schedule ds_c">
          <div class="wb90 pl10">人员信息</div>
          <div class="wb5">手机</div>
          <div class="wb5">邮箱</div>
          <!--             <div class="wb5">证件</div> -->
        </div>
      </div>
      <div class="new-checklist_container new-checklist_container1"> 
      <c:if test="${empty psnList}">
        <div class="ds_c pd014 btd">
          <div class="confirm_words_bt" style="margin-left: 530px;">未检测到待验证人员。</div>
        </div>
      </c:if>
      <c:forEach var="list" items="${psnList}" varStatus="psnIndex">
        <div class="ds_c pd014 btd">
          <div class="wb90">
            <div class="list_head">${psnIndex.count}&nbsp;&nbsp;${list['psn_name'] }</div>
          </div>
          <div style="display: flex; justify-content: space-around; width: 10%;">
            <div class="wb5" style="width: 20px; height: 20px;">
              <c:if test="${list['validate_mobile'] eq '1'}">
                <div class="icon_via" data-title="内容无误，验证通过"  onclick="viewValidateResult('内容无误，验证通过','');">
<!--                   <img src="/ressie/images/icon_via.png" alt=""> -->
                </div>
              </c:if>
              <c:if test="${list['validate_mobile'] eq '2'}">
                <div class="icon_mistake" data-title="${list['mistake_mobile_tip'] }"  onclick="viewValidateResult('${list['mistake_mobile_tip'] }','${list['right_mobile_tip'] }');">
<!--                     <img src="/ressie/images/icon_mistake.png" alt="" > -->
                </div>
              </c:if>
              <c:if test="${list['validate_mobile'] eq '4'}">
              </c:if>
            </div>
            <div class="wb5" style="width: 20px; height: 20px;">
              <c:if test="${list['validate_email'] eq '1'}">
                <div class="icon_via" data-title="内容无误，验证通过"  onclick="viewValidateResult('内容无误，验证通过','');">
<!--                     <img src="/ressie/images/icon_via.png" alt=""> -->
                </div>
              </c:if>
              <c:if test="${list['validate_email'] eq '2'}">
                <div class="icon_mistake" data-title="${list['mistake_email_tip'] }"  onclick="viewValidateResult('${list['mistake_email_tip'] }','${list['right_email_tip'] }');">
<!--                   <img src="/ressie/images/icon_mistake.png" alt=""> -->
                </div>
              </c:if>
              <c:if test="${list['validate_email'] eq '4'}">
              </c:if>
            </div>
          </div>
        </div>
      </c:forEach>
      </div>
    
       <div  class="new-checkinfor_box new-checkinfor_box2">
        <div class="achievement_achievement">
          <p>
            二、单位验证 <span>（总数：<span>${insCount }</span>；验证通过单位：<span>${insPass }</span>）
            </span>
          </p>
        </div>
        <div class="schedule ds_c">
          <div class="wb90 pl10">单位信息</div>
          <div class="wb10">社会信用代码</div>
        </div>
      </div>
      <div class="new-checklist_container new-checklist_container2">
      <c:if test="${empty insList}">
        <div class="ds_c pd014 btd">
          <div class="confirm_words_bt" style="margin-left: 530px;">未检测到待验证单位。</div>
        </div>
      </c:if>
       
      <c:forEach var="list" items="${insList}" varStatus="insIndex">
        <div class="ds_c pd014 btd">
          <div class="wb90">
            <div class="list_head">${insIndex.count}&nbsp;&nbsp;${list['org_name'] }</div>
          </div>
          <c:if test="${list['validate_uniform'] eq '1'}">
            <div class="wb10" style="width: 20px; height: 20px; margin-left: 41px;">
              <div class="icon_via" data-title="内容无误，验证通过" onclick="viewValidateResult('内容无误，验证通过','');">
<!--                 <img src="/ressie/images/icon_via.png" alt=""> -->
              </div>
            </div>
          </c:if>
          <c:if test="${list['validate_uniform'] eq '2'}">
            <div class="wb10" style="width: 20px; height: 20px; margin-left: 41px;">
              <div class="icon_mistake" data-title="${list['mistake_uniform_tip'] }" onclick="viewValidateResult('${list['mistake_uniform_tip'] }','${list['right_uniform_tip'] }');">
<!--                 <img src="/ressie/images/icon_mistake.png" alt=""> -->
              </div>
            </div>
          </c:if>
          <c:if test="${list['validate_uniform'] eq '4'}">
<%--             <div class="wb10" style="width: 20px; height: 20px; margin-left: 41px;" title="内容为空，无法验证"  onclick="viewValidateResult('${list['mistake_uniform_tip'] }');"> --%>
<!--             </div> -->
          </c:if>
        </div>
      </c:forEach>
      </div>

    <div  class="new-checkinfor_box new-checkinfor_box3">
      <div class="achievement_achievement">
        <p>
          三、成果验证 <span>（总数：<span>${prjPubCount }</span>；验证通过成果：<span>${prjPubPass }</span>）
          </span>
        </p>
      </div>
      <div class="schedule ds_c">
        <div class="wb75 pl10">成果信息</div>
        <div class="wb5">标题</div>
        <div class="wb5">DOI</div>
        <div class="wb5">期刊</div>
        <div class="wb5">作者</div>
        <div class="wb5">标注</div>
        <div class="wb5">年份</div>
      </div>
      </div>
      <div class="new-checklist_container new-checklist_container3">
      <c:if test="${empty prjPubList}">
        <div class="ds_c pd014 btd">
          <div class="confirm_words_bt" style="margin-left: 530px;">未检测到待验证成果。</div>
        </div>
      </c:if>
       
        <c:forEach var="list" items="${prjPubList}" varStatus="prjPubIndex">
            <div class="ds_c pd014 btd">
                <div class="wb70"><div class="list_head">${prjPubIndex.count}&nbsp;&nbsp;${list['zh_title'] }</div></div>
                <div style="display: flex; justify-content: space-around; width: 30%; margin-left: 60px;">
                    <div class="wb5" style="width: 20px; height: 20px;">
                        <c:if test="${list['validate_title'] eq '1'}">
                        <div class="icon_via" data-title="内容无误，验证通过"  onclick="viewValidateResult('内容无误，验证通过','');">
<!--                             <img src="/ressie/images/icon_via.png" alt=""> -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_title'] eq '2'}">
                        <div class="icon_mistake" data-title="${list['mistake_title_tip'] }"  onclick="viewValidateResult('${list['mistake_title_tip'] }','${list['right_title_tip'] }');">
<!--                             <img src="/ressie/images/icon_mistake.png" alt=""> -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_title'] eq '4'}">
                        </c:if>
                    </div>
                    <div class="wb5" style="width: 20px; height: 20px;">
                        <c:if test="${list['validate_doi'] eq '1'}">
                        <div class="icon_via" data-title="内容无误，验证通过" onclick="viewValidateResult('内容无误，验证通过','');">
<!--                             <img src="/ressie/images/icon_via.png" alt=""> -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_doi'] eq '2'}">
                        <div class="icon_mistake" data-title="${list['mistake_doi_tip'] }" onclick="viewValidateResult('${list['mistake_doi_tip'] }','${list['right_doi_tip'] }');">
<!--                             <img src="/ressie/images/icon_mistake.png" alt=""> -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_doi'] eq '4'}">
                        </c:if>
                    </div>
                    <div class="wb5" style="width: 20px; height: 20px;">
                        <c:if test="${list['validate_jname'] eq '1'}">
                        <div class="icon_via" data-title="内容无误，验证通过" onclick="viewValidateResult('内容无误，验证通过','');">
<!--                         <img src="/ressie/images/icon_via.png" alt=""> -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_jname'] eq '2'}">
                        <div class="icon_mistake" data-title="${list['mistake_jname_tip']}" onclick="viewValidateResult('${list['mistake_jname_tip'] }','${list['right_jname_tip'] }');">
<!--                         <img src="/ressie/images/icon_mistake.png" alt="" > -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_jname'] eq '4'}">
                        </c:if>
                    </div>
                    <div class="wb5" style="width: 20px; height: 20px;">
                        <c:if test="${list['validate_author'] eq '1'}">
                        <div class="icon_via" data-title="内容无误，验证通过" onclick="viewValidateResult('内容无误，验证通过','');">
<!--                         <img src="/ressie/images/icon_via.png" alt="" > -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_author'] eq '2'}">
                        <div class="icon_mistake" data-title="${list['mistake_author_tip']}" onclick="viewValidateResult('${list['mistake_author_tip'] }','${list['right_author_tip'] }');">
<!--                         <img src="/ressie/images/icon_mistake.png" alt="" > -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_author'] eq '4'}">
                        </c:if>
                    </div>
                    <div class="wb5" style="width: 20px; height: 20px;">
                        <c:if test="${list['validate_fundinfo'] eq '1'}">
                        <div class="icon_via" data-title="内容无误，验证通过" onclick="viewValidateResult('内容无误，验证通过','');">
                            <img src="/ressie/images/icon_via.png" alt="">
                            </div>
                        </c:if>
                        <c:if test="${list['validate_fundinfo'] eq '2'}">
                        <div class="icon_mistake" data-title="${list['mistake_fund_tip']}" onclick="viewValidateResult('${list['mistake_fund_tip'] }','${list['right_fund_tip'] }');">
<!--                             <img src="/ressie/images/icon_mistake.png" alt="" > -->
                            </div>
                        </c:if>
                        <c:if test="${list['validate_fundinfo'] eq '4'}">
                        </c:if>
                    </div>
                    <div class="wb5" style="width: 20px; height: 20px;">
                        <c:if test="${list['validate_pubyear'] eq '1'}">
                        <div class="icon_via" data-title="内容无误，验证通过" onclick="viewValidateResult('内容无误，验证通过','');">
<!--                             <img src="/ressie/images/icon_via.png" alt="" > -->
                            </div>
                        </c:if>
                        <c:if test="${list['validate_pubyear'] eq '2'}">
                        <div class="icon_mistake" data-title="${list['mistake_year_tip']}" onclick="viewValidateResult('${list['mistake_year_tip'] }','${list['right_year_tip'] }');">
<!--                             <img src="/ressie/images/icon_mistake.png" alt="" > -->
                            </div>
                        </c:if>
                        <c:if test="${list['validate_pubyear'] eq '4'}">
                        </c:if>
                    </div>
                </div>
            </div>
        </c:forEach>
        </div>
    </c:if>
    <c:if test="${flag eq false}">
      <div class="new-checkinfor_box new-checkinfor_box3">
      <div class="achievement_achievement">
        <p>
          一、成果验证 <span>（总数：<span>${prjPubCount }</span>；验证通过成果：<span>${prjPubPass }</span>）
          </span>
        </p>
      </div>
      <div class="schedule ds_c">
        <div class="wb75 pl10">成果信息</div>
        <div class="wb5">标题</div>
        <div class="wb5">DOI</div>
        <div class="wb5">期刊</div>
        <div class="wb5">作者</div>
        <div class="wb5">标注</div>
        <div class="wb5">年份</div>
      </div>
      </div>
      <div class="new-checklist_container new-checklist_container3">
      <c:if test="${empty prjPubList}">
        <div class="ds_c pd014 btd">
          <div class="confirm_words_bt" style="margin-left: 530px;">未检测到待验证成果。</div>
        </div>
      </c:if>
       
        <c:forEach var="list" items="${prjPubList}" varStatus="prjPubIndex">
            <div class="ds_c pd014 btd">
                <div class="wb70"><div class="list_head">${prjPubIndex.count}&nbsp;&nbsp;${list['zh_title'] }</div></div>
                <div style="display: flex; justify-content: space-around; width: 30%; margin-left: 60px;">
                    <div class="wb5" style="width: 20px; height: 20px;">
                        <c:if test="${list['validate_title'] eq '1'}">
                        <div class="icon_via" data-title="内容无误，验证通过" onclick="viewValidateResult('内容无误，验证通过','');">
<!--                         <img src="/ressie/images/icon_via.png" alt=""> -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_title'] eq '2'}">
                        <div class="icon_mistake" data-title="${list['mistake_title_tip']}" onclick="viewValidateResult('${list['mistake_title_tip'] }','${list['right_title_tip'] }');">
<!--                         <img src="/ressie/images/icon_mistake.png" alt="" > -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_title'] eq '4'}">
                        </c:if>
                    </div>
                    <div class="wb5" style="width: 20px; height: 20px;">
                        <c:if test="${list['validate_doi'] eq '1'}">
                        <div class="icon_via" data-title="内容无误，验证通过" onclick="viewValidateResult('内容无误，验证通过','');">
<!--                         <img src="/ressie/images/icon_via.png" alt=""> -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_doi'] eq '2'}">
                        <div class="icon_mistake" data-title="${list['mistake_doi_tip']}" onclick="viewValidateResult('${list['mistake_doi_tip'] }','${list['right_doi_tip'] }');">
<!--                         <img src="/ressie/images/icon_mistake.png" alt="" > -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_doi'] eq '4'}">
                        </c:if>
                    </div>
                    <div class="wb5" style="width: 20px; height: 20px;">
                        <c:if test="${list['validate_jname'] eq '1'}">
                        <div class="icon_via" data-title="内容无误，验证通过" onclick="viewValidateResult('内容无误，验证通过','');">
<!--                         <img src="/ressie/images/icon_via.png" alt="" > -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_jname'] eq '2'}">
                        <div class="icon_mistake" data-title="${list['mistake_jname_tip']}" onclick="viewValidateResult('${list['mistake_jname_tip'] }','${list['right_jname_tip'] }');">
<!--                         <img src="/ressie/images/icon_mistake.png" alt="" > -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_jname'] eq '4'}">
                        </c:if>
                    </div>
                    <div class="wb5" style="width: 20px; height: 20px;">
                        <c:if test="${list['validate_author'] eq '1'}">
                        <div class="icon_via" data-title="内容无误，验证通过" onclick="viewValidateResult('内容无误，验证通过','');">
<!--                         <img src="/ressie/images/icon_via.png" alt=""> -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_author'] eq '2'}">
                        <div class="icon_mistake" data-title="${list['mistake_author_tip']}" onclick="viewValidateResult('${list['mistake_author_tip'] }','${list['right_author_tip'] }');">
<!--                         <img src="/ressie/images/icon_mistake.png" alt="" > -->
                        </div>
                        </c:if>
                        <c:if test="${list['validate_author'] eq '4'}">
                        </c:if>
                    </div>
                    <div class="wb5" style="width: 20px; height: 20px;">
                        <c:if test="${list['validate_fundinfo'] eq '1'}">
                        <div class="icon_via" data-title="内容无误，验证通过" onclick="viewValidateResult('内容无误，验证通过','');">
<!--                             <img src="/ressie/images/icon_via.png" alt=""> -->
                            </div>
                        </c:if>
                        <c:if test="${list['validate_fundinfo'] eq '2'}">
                        <div class="icon_mistake" data-title="${list['mistake_fund_tip']}" onclick="viewValidateResult('${list['mistake_fund_tip'] }','${list['right_fund_tip'] }');">
<!--                             <img src="/ressie/images/icon_mistake.png" alt="" > -->
                            </div>
                        </c:if>
                        <c:if test="${list['validate_fundinfo'] eq '4'}">
                        </c:if>
                    </div>
                    <div class="wb5" style="width: 20px; height: 20px;">
                        <c:if test="${list['validate_pubyear'] eq '1'}">
                            <div class="icon_via" data-title="内容无误，验证通过" onclick="viewValidateResult('内容无误，验证通过','');">
<!--                             <img src="/ressie/images/icon_via.png" alt="" > -->
                            </div>
                        </c:if>
                        <c:if test="${list['validate_pubyear'] eq '2'}">
                        <div class="icon_mistake" data-title="${list['mistake_year_tip']}" onclick="viewValidateResult('${list['mistake_year_tip'] }','${list['right_year_tip'] }',);">
<!--                             <img src="/ressie/images/icon_mistake.png" alt=""> -->
                            </div>
                        </c:if>
                        <c:if test="${list['validate_pubyear'] eq '4'}">
                        </c:if>
                    </div>
                </div>
            </div>
        </c:forEach>
        </div>
    </c:if>
    <div id="blank_module" style="height:123px;"></div>
  </div>
  <div class="ball_content_top" id="popup" style="visibility:hidden; display: none;">
        <div style="width: 350px; min-height: 280px; margin: 0px 28px; padding: 15px;">
        <!-- 内容开始 -->
        <div class="new-Verifi_mesg-item">
            <span class="new-Verifi_mesg-item-title mis_content_title">填写值:</span>
            <span class="new-Verifi_mesg-item-content mis_content">
            </span> 
        </div>
        <div class="new-Verifi_mesg-item">
            <span class="new-Verifi_mesg-item-title cor_content_title">正确值:</span>
            <span class="new-Verifi_mesg-item-content cor_content">
            </span> 
        </div>
        <!-- 内容结束 -->
    </div>
  </div>
</body>
</html>
