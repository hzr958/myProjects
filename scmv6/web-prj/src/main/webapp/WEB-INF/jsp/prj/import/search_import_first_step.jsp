<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<meta charset="utf-8">
<link rel="stylesheet" type="text/css" href="${resmod }/smate-pc/css/scmpcframe.css">
<%@ include file="collect_global_i18n.jsp"%>
<script type="text/javascript">var respath='${resmod}';</script>
<script type="text/javascript" src="${resmod}/js/plugin/judge-browser/browser.match.js"></script>
<script type="text/javascript" src="${resmod}/js/collect/collect.global2.js"></script>
<script type="text/javascript" src="${resmod}/js/collect/collect.new.js"></script>
<script type="text/javascript" src="${resmod}/js/collect/collect.func_ie_new.js"></script>
<script type="text/javascript" src="${resmod}/js/search/searchplugin/iris_Octopus.html"></script>
<script type="text/javascript" src="${resmod}/js/collect/collect.func_chrome.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery.selectseach.min.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/pub/language/search_import_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/loadding_div.js"></script>
<script type="text/javascript" src="${resmod}/js_v8/pub/plugin/smate.plugin.alerts.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.toast.js"></script>
<script type="text/javascript">
		var multDB=true;//检索类型标识，文献:false，成果:true，现在没有文献了
		var artType='';//群组标示，提示导入操作完成后应该跳转到的页面.
		if('${artType}' != null && '${artType}' != ''){
		    artType='${artType}';
		}
		var isProjectCollect = true;
		$(function(){
		    //回车搜索
  		    $("body").bind("keyup",function(event) { 
  		      if (event.keyCode == "13") {
  		         $("#federalBtnSearch").click();
  		      }
            }); 
  		    
  		    if(document.getElementById("search_some_one")){
  		        document.getElementById("search_some_one").onfocus = function(){
  		            this.closest(".searchbox__main").style.borderColor = "#2882d8";
  		        }
  		        document.getElementById("search_some_one").onblur = function(){
  		            this.closest(".searchbox__main").style.borderColor = "#ccc";
  		        }
  		    }
		    collect.radioIsChoose('${isInitRadio}');
		    var selectall = document.getElementsByClassName("selected-author_all")[0];
            selectall.onclick = function(){
                if(this.classList.contains("selected-author_confirm")){
                    this.classList.remove("selected-author_confirm");
                    var songllist = this.closest(".Federal-retrieval_content-right").querySelectorAll(".selected-author_single");
                    for(var i = 0; i < songllist.length; i++){
                        if(songllist[i].classList.contains("selected-author_confirm")){
                            songllist[i].classList.remove("selected-author_confirm");
                        }
                    }
                }else{
                    this.classList.add("selected-author_confirm");
                    var songllist = this.closest(".Federal-retrieval_content-right").querySelectorAll(".selected-author_single");
                    for(var i = 0; i < songllist.length; i++){
                        if(!songllist[i].classList.contains("selected-author_confirm")){
                            songllist[i].classList.add("selected-author_confirm");
                        }
                    }
                }
            } 
            var targetele = document.getElementsByClassName("new-getdata_tip-container")[0];
            targetele.style.left = (window.innerWidth - targetele.offsetWidth)/2 + "px";
            targetele.style.top = (window.innerHeight - targetele.offsetHeight)/2 + "px";
            window.onresize = function(){
                targetele.style.left = (window.innerWidth - targetele.offsetWidth)/2 + "px";
                targetele.style.top = (window.innerHeight - targetele.offsetHeight)/2 + "px";
            }
            
            $(".selected-author_single").click(function(){
                //如果在检索时，不许改变检索条件 2018-11-16-ajb
                if($.trim($("#search_ref_db_enum").html()) !== ""){
                    return false;
                }
                var $this = $(this);
                if($this.hasClass("selected-author_confirm")){
                    $this.removeClass("selected-author_confirm");
                }else{
                    $this.addClass("selected-author_confirm");
                }
                //TODO 控制全选按钮的选中和取消选中状态
            });
            var keyoncode = document.getElementsByClassName("Federal-retrieval_infor-right_key")[0];
           //SCM-19133,方法中未加event参数
            keyoncode.onclick = function(event){
                event.stopPropagation();
                event.preventDefault();
	              if(this.innerHTML == "arrow_drop_down"){
	                  if(this.closest(".Federal-retrieval_infor-right").getElementsByClassName("Federal-retrieval_infor-right_sub-item").length>0){
	                      this.innerHTML = "arrow_drop_up";
                          this.closest(".Federal-retrieval_infor-right").querySelector(".Federal-retrieval_infor-right_sub").style.display = "block";
	                  }
	              }else{
	                this.innerHTML = "arrow_drop_down";
	                this.closest(".Federal-retrieval_infor-right").querySelector(".Federal-retrieval_infor-right_sub").style.display = "none";
	              }
            }
            var sublist = document.getElementsByClassName("Federal-retrieval_infor-right_sub-item");
            for(var i = 0; i < sublist.length; i++){
                sublist[i].onclick = function(event){
                  event.stopPropagation();
                  event.preventDefault();
                  this.closest(".Federal-retrieval_infor-right").querySelector("input").value = this.querySelector(".Federal-retrieval_infor-right_sub-detail").innerHTML;
                  this.closest(".Federal-retrieval_infor-right_sub").style.display = "none";
                  this.closest(".Federal-retrieval_infor-right").querySelector(".Federal-retrieval_infor-right_key").innerHTML = "arrow_drop_down";
                }
            }
            document.onclick = function(){
                if(keyoncode.innerHTML ==  "arrow_drop_up"){
                    keyoncode.innerHTML = "arrow_drop_down";
                    keyoncode.closest(".Federal-retrieval_infor-right").querySelector(".Federal-retrieval_infor-right_sub").style.display = "none";
                }
            }
            
            $(".work_item[isPrimary='1']:first").click();
            
            window.addFormElementsEvents();
            
            document.getElementById("insName").onclick = function(event){
                var changtext = $("#insName").val();
                event.stopPropagation();
                if($("#insName").val()!=""){
                    this.closest(".Federal-retrieval_infor-right").querySelector(".Federal-retrieval_infor-right_sub").style.display = "none";
                    this.closest(".Federal-retrieval_infor-right").querySelector(".Federal-retrieval_infor-right_key").innerHTML = "arrow_drop_down";
                }else{
                    this.closest(".Federal-retrieval_infor-right").querySelector(".Federal-retrieval_infor-right_sub").style.display = "block";
                    this.closest(".Federal-retrieval_infor-right").querySelector(".Federal-retrieval_infor-right_key").innerHTML = "arrow_drop_up";
                }
           }
           document.getElementById("insName").onblur = function(event){
               var $this = this;
               setTimeout(function(){
                   $this.closest(".Federal-retrieval_infor-right").querySelector(".Federal-retrieval_infor-right_sub").style.display = "none";
                   $this.closest(".Federal-retrieval_infor-right").querySelector(".Federal-retrieval_infor-right_key").innerHTML = "arrow_drop_down";
               },200)

          }
		});
		
		//按人员姓名还是论文作者姓名检索
		function changeNameCondition(obj, type){
		    var $this = $(obj);
		    if(!$this.hasClass("selected-oneself_confirm")){
		        $(".selected-oneself_confirm").removeClass("selected-oneself_confirm");
                $this.addClass("selected-oneself_confirm");
            }
		    if(type == "0"){
		        $("#div_author_else").hide();
		        $("#oname_else").val("");
		        $(".search_by_psnName").show();
		    }else{
		        $(".search_by_psnName").hide();
		        $("#cname").val("");
		        $("#lname").val("");
		        $("#fname").val("");
		        $("#div_author_else").show();
		    }
		}
		
		var searImport = searImport ? searImport : {};
	    
	    //完成导入，返回进入检索导入之前的页面
	    searImport.viewHistory = function() {
	      parent.location.href = "/psnweb/homepage/show?module=prj";
	    }
		
		
		function insNameChange(obj){
		    $(obj).attr("code","");
		};
		function callbackInsName(){
		};
		
		function checkIsEmpty(obj){
			var insName = $("#insName").val();
			if(insName == ''){
				document.getElementsByClassName("Federal-retrieval_infor-right_key")[0].closest(".Federal-retrieval_infor-right").querySelector(".Federal-retrieval_infor-right_sub").style.display = "block";
			}else{
				document.getElementsByClassName("Federal-retrieval_infor-right_key")[0].closest(".Federal-retrieval_infor-right").querySelector(".Federal-retrieval_infor-right_sub").style.display = "none";
			}
		}
	</script>
</head>
<body>
  <input type="hidden" id="des3GroupId" value="${des3GroupId }" />
  <input type="hidden" id="savePubType" value="${savePubType }" />
  <div class="Federal-retrieval_container">
    <!-- 第一步、第二步、第三步标题  begin -->
    <div class="Federal-retrieval_header">
      <div class="Federal-retrieval_header-item">
        <div class="Federal-retrieval_header-avator Federal-retrieval_header-item_first" id="step_one_div">
          <span class="Federal-retrieval_header-avator_infor">1</span>
        </div>
        <span class="Federal-retrieval_header-line"></span>
        <div class="Federal-retrieval_header-avator Federal-retrieval_header-item_second" id="step_two_div">
          <span class="Federal-retrieval_header-avator_infor">2</span>
        </div>
        <span class="Federal-retrieval_header-line2"></span>
        <div class="Federal-retrieval_header-avator Federal-retrieval_header-item_third" id="step_three_div">
          <span class="Federal-retrieval_header-avator_infor">3</span>
        </div>
      </div>
      <div class="Federal-retrieval_header-tip_box">
        <span class="Federal-retrieval_header-item_detail" style="color: #2882d8;">
            <s:text name="referencesearch.label.alert1" /></span> <span class="Federal-retrieval_header-item_detail">
            <s:text name="referencesearch.label.alert2" /></span> <span class="Federal-retrieval_header-item_detail">
            <s:text name="referencesearch.label.alert5" /></span>
      </div>
    </div>
    <!-- 第一步、第二步、第三步标题  end -->
    <div id="box_main_pub">
      <!-- 文献库列表  begin -->
      <div class="Federal-retrieval_content">
        <div class="Federal-retrieval_content-title">
          <span><s:text name="referencesearch.label.db" /></span>
        </div>
        <div class="Federal-retrieval_content-right">
          <div class="Federal-retrieval_content-right_container">
            <c:forEach items="${constRefDBList }" varStatus="status" var="dbItem">
              <div class="Federal-retrieval_content-right_item">
                <i class="selected-author selected-author_single" onclick="select_ShDB_Code(this);"
                  id="dbCB_${dbItem.code}" value='${dbItem.code}' name="${dbItem.nameDetails } "></i> <span
                  class="Federal-retrieval_content-right_item-detaile">${dbItem.nameDetails }</span>
              </div>
            </c:forEach>
          </div>
          <div class="Federal-retrieval_content-right_footer">
            <div style="display: flex;">
              <i id="selected-author_all" class="selected-author selected-author_all"></i> <span
                class="Federal-retrieval_content-right_footer-detail">
                <s:text name="referencesearch.all.checkbox" /></span>
            </div>
            <div>
              <a href='<c:if test='${locale!="en_US" }'>/resmod/html/helpcenter/MyProject.html</c:if><c:if test='${locale=="en_US" }'>/resmod/html/helpcenter/MyProject_en_US.html</c:if>' target="_blank"><span
                class="Federal-retrieval_content-right_footer-search">
                <s:text name="commons.search.db.help" /></span></a>
            </div>
          </div>
        </div>
      </div>
      <!-- 文献库列表  end -->
      <!-- 标题  begin -->
      <div class="Federal-retrieval_infor-item">
        <div class="Federal-retrieval_infor-left">
          <span><s:text name="referencesearch.label.title" /></span>
        </div>
        <div class="Federal-retrieval_infor-right">
          <input type="text" name="title" id="title" maxlength="300">
        </div>
      </div>
      <!-- 标题  end -->
      <!-- DOI 不显示但不要去掉 begin -->
      <div class="Federal-retrieval_infor-item" style="display:none;">
        <div class="Federal-retrieval_infor-left">
          <span>DOI:</span>
        </div>
        <div class="Federal-retrieval_infor-right">
          <input type="text" id="doiSearch" maxlength="100">
        </div>
      </div>
      <!-- DOI 不显示但不要去掉  end -->
      <!-- 作者姓名  begin -->
      <div class="Federal-retrieval_content-search">
        <div class="Federal-retrieval_psninfor-left">
          <span><s:text name="referencesearch.label.author" />:</span>
        </div>
        <div class="Federal-retrieval_psninfor-right">
          <div class="Federal-retrieval_psninfor-right_title">
            <div class="Federal-retrieval_psninfor-right_title-item">
              <i class="selected-oneself selected-oneself_confirm" value="0" onclick="changeNameCondition(this,'0');"></i>
              <span class="Federal-retrieval_psninfor-right_title-content"
                title="<s:text name='referencesearch.tip.author1'/>">
                <s:text name="referenceSearch.search.href2" /></span> <i class="find-nothing_tip"
                title="<s:text name='referencesearch.tip.author1'/>"></i>
            </div>
            <div class="Federal-retrieval_psninfor-right_title-item" style="margin-left: 32px;">
              <i class="selected-oneself" value="1" onclick="changeNameCondition(this, '1');"></i> <span
                class="Federal-retrieval_psninfor-right_title-content"
                title="<s:text name='referencesearch.tip.author2'/>">
                <s:text name="referenceSearch.search.href1" /></span> <i class="find-nothing_tip"
                title="<s:text name='referencesearch.tip.author2'/>"></i>
            </div>
          </div>
          <div class="Federal-retrieval_psninfor-right_content">
            <div class="Federal-retrieval_psninfor-right_content-item search_by_psnName">
              <span class="Federal-retrieval_psninfor-right_content-item_title">
                <s:text name="referencesearch.label.cname" /></span> <input
                class="Federal-retrieval_psninfor-right_content-item_input" type="text" name="cname" id="cname"
                maxlength="50" value="${zhName }">
            </div>
            <div class="Federal-retrieval_psninfor-right_content-item search_by_psnName" style="margin: 0px 16px;">
              <span class="Federal-retrieval_psninfor-right_content-item_title">
                <s:text name="referencesearch.label.lname" /></span> <input
                class="Federal-retrieval_psninfor-right_content-item_input" id="lname" maxlength="50" name="lname"
                value="${lastName }">
            </div>
            <div class="Federal-retrieval_psninfor-right_content-item search_by_psnName">
              <span class="Federal-retrieval_psninfor-right_content-item_title">
                <s:text name="referencesearch.label.fname" /></span> <input
                class="Federal-retrieval_psninfor-right_content-item_input" type="text" maxlength="50" id="fname"
                name="fname" value="${firstName }">
            </div>
            <div id="div_author_else" style="display: none; width: 100%">
              <p style="font-size: 14px; lineheight: 20px; margin-bottom: 8px; color: #999;">
                  <s:text name="referencesearch.label.texttip" />
         <!--      作者名的检索格式应符合各文献库的要求，如在SCIE中检索Chen
                Wen的成果时使用Chen W* -->
                
                <%-- <spring:message code="referencesearch.label.texttip" /> --%>
                </p>
              <div
                style="height: 28px; width: 100%; border: 1px solid #ddd; border-radius: 3px; display: flex; align-items: center; justify-content: flex-start;">
                <input id="oname_else" type="text" maxlength="50" class="inp_text"
                  style="width: 98%; height: 97%; padding-left: 8px; border: none;" />
              </div>
            </div>
          </div>
        </div>
      </div>
      <!-- 作者姓名  end -->
      <!-- 作者单位  begin -->
      <div class="Federal-retrieval_infor-item">
        <div class="Federal-retrieval_infor-left">
          <span><s:text name="referencesearch.label.author.ins" />:</span>
        </div>
        <div class="Federal-retrieval_infor-right">
          <input type="text" id="insName" maxlength="300" style="width: 95%;" autocomplete="off"
            onkeyup="checkIsEmpty(this);" class="Federal-retrieval_infor-right—input js_autocompletebox"
            onchange="insNameChange(this);" itemevent="callbackInsName" request-url="/psnweb/ac/ajaxautoinstitution"
            value="<c:if test='${!empty primaryWork}'>${primaryWork.insName }</c:if>" /> <i
            class="material-icons Federal-retrieval_infor-right_key">arrow_drop_down</i>
          <div class="Federal-retrieval_infor-right_sub">
            <c:forEach var="work" varStatus="status" items="${workList }">
              <div class="Federal-retrieval_infor-right_sub-item work_item"
                fromYear="${work.fromYear }"
                toYear="${work.isActive == 1 ? (currentYear) : (!empty work.toYear ? work.toYear : '') }"
                isPrimary="${work.isPrimary}">
                <span class="Federal-retrieval_infor-right_sub-detail">${work.insName }</span>
                <c:if test="${!empty work.fromYear || !empty work.toYear || work.isActive == 1}">
                <span class="Federal-retrieval_infor-right_sub-detail2">(${work.fromYear}-${work.isActive == 1 ? (currentYear) : (!empty work.toYear ? work.toYear : '') })</span>
                </c:if>
              </div>
            </c:forEach>
          </div>
        </div>
      </div>
      <!-- 作者姓名  end -->
      
      <!-- 检索和清除按钮  begin -->
      <div class="Federal-retrieval_infor-footer">
        <div class="Federal-retrieval_infor-left"></div>
        <div class="Federal-retrieval_infor-footer_fuc">
        <!-- onclick="OnUpdate('4', '1', '-2', '');importData_chrome('', 'CnkiFund');" -->
          <div class="Federal-retrieval_infor-footer_search btnSearch" id="federalBtnSearch" onclick="searchData()">
            <s:text name="referencesearch.button.search" />
          </div>
          <div class="Federal-retrieval_infor-footer_clear" onclick="clearInput();">
            <s:text name="referencesearch.button.clear" />
          </div>
          <c:if test="${!empty des3GroupId }">
            <div class="Federal-retrieval_infor-footer_clear" onclick="searImport.viewHistory();">
              <s:text name="referencesearch.button.back" />
            </div>
          </c:if>
        </div>
      </div>
    </div>
    <!-- 检索和清除按钮  end -->
    <div id="divMessage" class="online-search_container"
      style="display: flex; align-items: center; justify-content: center; width: 100%; height: 24px; line-height: 24px; margin: 12px 0px; display: none;">
      <div class="online-search_container-content"
        style="color: #333; font-size: 14px; text-overflow: ellipsis; white-space: nowrap; overflow: hidden;"
        id="search_ref_db_enum"></div>
      <span class="online-search_container-searching" style="color: #333; font-size: 14px;"><s:text name="referencesearch.lable.searching" /></span> <img
        src="${resmod }/smate-pc/img/icon_schedule.gif" class="online-search_container-avator"> <span
        style="color: #1265cf; font-size: 14px; cursor: pointer; margin: 0px 4px" class="online-search_container-cancel"
        onclick="cancelJob();"><s:text name="referencesearch.lable.cancelSearch" /></span>
    </div>
    <%@ include file="collect_search_result.jsp"%>
    <%@ include file="collect_plugin.jsp"%>
  </div>
  <!-- 导入成果时正在加载数据的弹框提示 begin-->
  <div class="background-cover" style="display: none;" id="getDataFromTips">
    <div class="new-getdata_tip-container">
      <img src="${resmod }/smate-pc/img/upload.gif" class="new-getdata_tip-container_avator">
      <div class="new-getdata_tip-container_content" id="get_data_from_content"></div>
      <div class="new-getdata_tip-container_cancel" onclick="cancelJob();"><s:text name="pub.update.cancel" /></div>
    </div>
  </div>
  <!-- 导入成果时正在加载数据的弹框提示 end -->
  <!-- 待导入成果列表弹框 begin -->
  <div class="background-cover" style="display: none;" id="showList"></div>
  <!-- 待导入成果列表弹框 end-->
  <%@ include file="plugin_install_or_update_tips.jsp"%>
</body>
</html>