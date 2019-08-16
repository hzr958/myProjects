<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- <%@ include file="/common/taglibs.jsp"%> --%>
<script type="text/javascript">
var pub = new Publication();
window.onload = function(){
	PubEdit.bindASyncUpload();//初始化上传文件
	PubEdit.initArea();//初始化地区
    addFormElementsEvents();//绑定下拉选择
    PubEdit.selectAndRadioOnclickInit();//下拉框选择
    window.addFormElementsEvents();
    $("#login_box_refresh_currentPage").val("false");//登录不跳转    
    $("#login_box_refresh_currentPage_always").val("true");//超时弹框不跳转    
   
    //添加关键词
    $("#addKeyInput").keyup(function(event){
        event.stopPropagation();
        if(event.keyCode == 13){  
        	PubEdit.addkey(event);
        }
    });
    $("#addKeyInput").blur(function(event){
        event.stopPropagation();
    	PubEdit.addkey(event);
    });
    $("#addKeyInput").on("input",function(event){
        event.stopPropagation();
    	if(/[;；]/.test($(this).val())){
    		PubEdit.addkey(event);
    	}
    });
    //摘要
    if(document.getElementsByClassName("header__nav")){
        document.getElementById("num2").style.display="flex";
        document.getElementsByClassName("header-main__box")[0].removeChild(document.getElementById("num1"));
    }
    if(document.getElementById("search_some_one")){
        document.getElementById("search_some_one").onfocus = function(){
            this.closest(".searchbox__main").style.borderColor = "#2882d8";
        }
        document.getElementById("search_some_one").onblur = function(){
            this.closest(".searchbox__main").style.borderColor = "#ccc";
        }
    }
    if(document.getElementsByTagName("textarea").length>0){
    	document.getElementsByTagName("textarea")[0].onfocus = function(){
            this.closest(".handin_import-outborder").style.border = "1px solid #2882d8";
        };
        document.getElementsByTagName("textarea")[0].onblur = function(){
            this.closest(".handin_import-outborder").style.border = "1px solid #ddd";
        };
    }
    if(document.getElementsByClassName("handin_import-content_container-right_select")){
        var total = document.getElementsByClassName("handin_import-content_container-right_select");
        for(var i = 0; i < total.length; i++){
            total[i].onclick = function(event){
                event.stopPropagation();
                if(this.querySelector(".material-icons").innerHTML == "arrow_drop_down"){
                    this.querySelector(".material-icons").innerHTML = "arrow_drop_up";
                    if (this.querySelector(".handin_import-content_container-right_select-detail")) {
                      this.querySelector(".handin_import-content_container-right_select-detail").style.display = "block";
                    }
                }else{
                    this.querySelector(".material-icons").innerHTML = "arrow_drop_down";
                    if (this.querySelector(".handin_import-content_container-right_select-detail")) {
                      this.querySelector(".handin_import-content_container-right_select-detail").style.display = "none";         
                    }
                }
            }
        }
    }
    if(document.getElementsByClassName("selected-func_down")&&document.getElementsByClassName("selected-func_up")){
        var length = document.getElementsByClassName("selected-func_down").length;
        if(document.getElementsByClassName("selected-func_up")[0]){
            document.getElementsByClassName("selected-func_up")[0].classList.add("selected-func_up-none");          
        }
        if(document.getElementsByClassName("selected-func_down")[length-1]){
            document.getElementsByClassName("selected-func_down")[length-1].classList.add("selected-func_down-none");           
        }
    }
    var show = document.getElementsByClassName("handin_import-content_container-right_select-detail");
    var tiplist = document.getElementsByClassName("handin_import-content_container-right_select-tip");
    document.onclick = function(){
       for(var i = 0; i < show.length; i++){
            show[i].style.display="none";
            show[i].closest(".handin_import-content_container-right_select").querySelector(".handin_import-content_container-right_select-tip").innerHTML = "arrow_drop_up";
       }
       for(var j = 0; j < tiplist.length; j++){
           tiplist[j].innerHTML = "arrow_drop_up";
       }
    }
    var openkey = document.getElementsByClassName("handin_import-content_container-right_select-tip");
    for(key in openkey){
        openkey[key].onclick = function(e){
            e.stopPropagation();
            e.preventDefault();
            if(this.innerHTML === "arrow_drop_down"){
                this.innerHTML = "arrow_drop_up";
                this.closest(".handin_import-content_container-right_select").querySelector(".handin_import-content_container-right_select-detail").style.display ="block";
            }else{
                this.innerHTML = "arrow_drop_down";
                this.closest(".handin_import-content_container-right_select").querySelector(".handin_import-content_container-right_select-detail").style.display ="none";
            } 
        }
    };
    var inner = $(".handin_import-content_container-right_select-item");
    if(inner.length>0){
	    for(key in inner){
	        inner[key].onclick = function(e){
	            e.stopPropagation();
	            e.preventDefault();
	            var node=this.querySelector(".handin_import-content_container-right_select-item_detail");
	            if(!$(this).hasClass("dev_pub_type")){
	            	  $(this).closest(".handin_import-content_container-right_select").find(".handin_import-content_container-right_select-box>.dev_select_input").val(node.innerHTML);
	            }
	            $(this).closest(".handin_import-content_container-right_select").find(".handin_import-content_container-right_select-tip").click();
	        }
	    };
    }
    var headeopen = document.getElementsByClassName("handin_import-content_container-right_select-detail");
    var subopen = document.getElementsByClassName("handin_import-content_container-right_select-tip");
    document.onclick = function(){
        for(var i = 0; i < headeopen.length; i++){
            headeopen[i].style.display = "none";
        };
        for(var j = 0; j < subopen.length; j++ ){
            subopen[j].innerHTML = "arrow_drop_down";
        }
    }
    var addele = document.getElementsByClassName("handin_import-content_container-right_author-addbtn")[0];
    if(addele){
        addele.onclick = function(event){
            event.stopPropagation();
        	addmenber();  
        };  
    }
    if(document.getElementsByClassName("new-importantkey_container-input").length>0){
        var inputlist = document.getElementsByClassName("new-importantkey_container-input");
        var targetcontainer = document.getElementsByClassName("new-importantkey_container")[0];
        var inputtarget = document.getElementsByClassName("new-importantkey_container-input")[0];
        for(var i = 0; i < inputlist.length; i++){
            inputlist[i].onfocus = function(){
                if(this.closest(".handin_import-content_container-right_input")){
                    this.closest(".handin_import-content_container-right_input").style.border="1px solid #2882d8";
                }
                if(this.closest(".handin_import-content_rightbox-border")){
                    this.closest(".handin_import-content_rightbox-border").style.border="1px solid #2882d8";
                }
                if(this.closest(".handin_import-content_container-right_Citation-item")){
                    this.closest(".handin_import-content_container-right_Citation-item").style.border="1px solid #2882d8";
                }
                this.onkeydown= function(event){
                    var e = event || window.event|| arguments.callee.caller.arguments[0];
                    var settext = this.value;
                    var reg = "《》{}（）[]'”“'";
                    if(e.keyCode == "13"){
                        if ((document.getElementsByClassName("new-importantkey_container-item_checked").length < 1)&&(settext != "")){
                            var inner = "";
                            var flag = false;
                            var inner = settext.trim() + "";
                            if(inner.toUpperCase()=="<IFRAME>"){
                              return;
                            }
                            var content = document.createElement("div");
                            content.className = "new-importantkey_container-item";
                            var setbox = '<div class="new-importantkey_container-item_detaile json_keyword">'
                                    + inner
                                    + '</div>'
                                    + '<div class="new-importantkey_container-item_colse">'
                                    + '<i class="material-icons" onclick="deleteElement(this)">close</i>'
                                    + '</div>';
                            content.innerHTML = setbox;
                            inputtarget.value = "";
                            content.querySelector(".new-importantkey_container-item_detaile").setAttribute("title",inner);
                            if(document.getElementsByClassName("new-importantkey_container-item").length<5){
                                if(document.getElementsByClassName("new-importantkey_container-item").length>0){
                                    var list = document.getElementsByClassName("new-importantkey_container-item");
                                    for(var i = 0; i < list.length; i++){
                                        if(list[i].querySelector(".new-importantkey_container-item_detaile").innerHTML.toUpperCase() == inner.toUpperCase()){
                                            flag = true;
                                        }
                                    }
                                }
                                if(flag){
                                    scmpublictoast(Enter.keyWordRepeat,2000);
                                }else{
                                    targetcontainer.insertBefore(content,inputtarget);
                                }
                            }else{
                                scmpublictoast(Enter.keywordsLimit,2000);
                            }
                        }
                    }else if(e.keyCode == "37"){
                        e.stopPropagation();
                        if(settext!='' && settext.indexOf(reg)){
                          return;
                        }
                        if (document.getElementsByClassName("new-importantkey_container-item_checked").length > 0) {
                            if (document.getElementsByClassName("new-importantkey_container-item_checked")[0].previousElementSibling != undefined) {
                                var targetele = document.getElementsByClassName("new-importantkey_container-item_checked")[0].previousElementSibling;
                                document.getElementsByClassName("new-importantkey_container-item_checked")[0].classList.remove("new-importantkey_container-item_checked");
                                targetele.classList.add("new-importantkey_container-item_checked");
                            } else {
                                document.getElementsByClassName("new-importantkey_container-item_checked")[0].classList.remove("new-importantkey_container-item_checked");
                                document.getElementsByClassName("new-importantkey_container-item")[document.getElementsByClassName("new-importantkey_container-item").length - 1].classList.add("new-importantkey_container-item_checked");
                            }
                        } else {
                          if(inputtarget.previousElementSibling != null){
                            inputtarget.previousElementSibling.classList.add("new-importantkey_container-item_checked");
                          }
                        }
                    }else if(e.keyCode == "39"){
                        e.stopPropagation();
                        if(settext!='' && settext.indexOf(reg)){
                          return;
                        }
                        if (document.getElementsByClassName("new-importantkey_container-item_checked").length > 0) {
                            if (document.getElementsByClassName("new-importantkey_container-item_checked")[0].nextElementSibling != undefined) {
                                var afterele = document.getElementsByClassName("new-importantkey_container-item_checked")[0].nextElementSibling;
                                if (afterele.tagName == "INPUT") {
                                    document.getElementsByClassName("new-importantkey_container-item_checked")[0].classList.remove("new-importantkey_container-item_checked");
                                    document.getElementsByClassName("new-importantkey_container-item")[0].classList.add("new-importantkey_container-item_checked");
                                } else {
                                    document.getElementsByClassName("new-importantkey_container-item_checked")[0].classList.remove("new-importantkey_container-item_checked");
                                    afterele.classList.add("new-importantkey_container-item_checked");
                                }
                            }
                        } else {
                          if(document.getElementsByClassName("new-importantkey_container-item")[0] != undefined){
                            document.getElementsByClassName("new-importantkey_container-item")[0].classList.add("new-importantkey_container-item_checked");
                          }
                        }
                    }else if (e.keyCode == "8") {
                        e.stopPropagation();
                        if (document.getElementsByClassName("new-importantkey_container-item_checked").length > 0) {
                            targetcontainer.removeChild(document.getElementsByClassName("new-importantkey_container-item_checked")[0]);
                        }else if(settext == ""){
                            PubEdit.deleteKey(e);
                        }
                    }
                }
            }
            
            inputlist[i].onblur = function(){
                if(this.closest(".handin_import-content_container-right_input")){
                    this.closest(".handin_import-content_container-right_input").style.border="1px solid #ddd";
                }
                if(this.closest(".handin_import-content_rightbox-border")){
                    this.closest(".handin_import-content_rightbox-border").style.border="1px solid #ddd";
                }
                if(this.closest(".handin_import-content_container-right_Citation-item")){
                    this.closest(".handin_import-content_container-right_Citation-item").style.border="1px solid #ddd";
                }
                var settext = this.value;
                var inner = "";
                var flag = false;
                var inner = settext.trim() + "";
                if(inner!=""){
                    var content = document.createElement("div");
                    content.className = "new-importantkey_container-item";
                    var setbox = '<div class="new-importantkey_container-item_detaile json_keyword" >'
                            + inner
                            + '</div>'
                            + '<div class="new-importantkey_container-item_colse">'
                            + '<i class="material-icons" onclick="deleteElement(this)">close</i>'
                            + '</div>';
                    content.innerHTML = setbox;
                    inputtarget.value = "";
                    content.querySelector(".new-importantkey_container-item_detaile").setAttribute("title",inner);
                    if(document.getElementsByClassName("new-importantkey_container-item").length<5){
                        if(document.getElementsByClassName("new-importantkey_container-item").length>0){
                            var list = document.getElementsByClassName("new-importantkey_container-item");
                            for(var i = 0; i < list.length; i++){
                                if(list[i].querySelector(".new-importantkey_container-item_detaile").innerHTML.toUpperCase() == inner.toUpperCase()){
                                    flag = true;
                                }
                            }
                        }
                        if(flag){
                            scmpublictoast(Enter.keyWordRepeat,2000);
                        }else{
                            targetcontainer.insertBefore(content,inputtarget);
                        }
                    }else{
                        scmpublictoast(Enter.keywordsLimit,2000);
                    }
                   
                }
            } 
        }
    }
}
var deleteElement = function(obj) {
    obj.closest(".new-importantkey_container").removeChild(
            obj.closest(".new-importantkey_container-item"));
}
function addmenber(){
	 var box = document.createElement("div");
     box.className="handin_import-content_container-right_author-body json_member";
     box.innerHTML ='<input type="hidden" class="json_member_des3PsnId" value="">'
    	 +'<div class="handin_import-content_container-right_author-body_oneself">'
         +'<i class="selected-oneself dev_menber_i"></i>'
         +'<input type="hidden" class="json_member_owner" value="false">'
         +'</div>'
         +'<div class="handin_import-content_container-right_author-body_name">'
         +'<span class="handin_import-content_container-right_author-body_item error_import-tip_border">'
         +'<input type="text" maxlength="61"  style="width: 85%!important;" class="dev-detailinput_container full_width json_member_name js_autocompletebox"   other-event="callbackName" data-src="request" request-url="/psnweb/ac/ajaxpsncooperator" request-data="getExitPubId();" value=""/>'
         +'</span>'
         +'</div>'
         +'<div class="handin_import-content_container-right_author-body_work">'
         +'<span class="handin_import-content_container-right_author-body_item">'
         +'<input type="text" maxlength="100" class="dev-detailinput_container full_width json_member_insNames js_autocompletebox" itemevent="callbackInsName" request-url="/psnweb/ac/ajaxautoinstitution" value=""/>'
         +'</span>'
         +'</div>'
         +'<div class="handin_import-content_container-right_author-body_email error_import-tip_border">'
         +'<span class="handin_import-content_container-right_author-body_item  error_import-tip_border">'
         +'<input type="text" maxlength="50" class="dev-detailinput_container full_width json_member_email" value="" />'
         +'</span>'
         +'</div>'
         +'<div class="handin_import-content_container-right_author-body_Communication">'
         +'<i class="selected-author dev_communicale"></i>'
         +'<input type="hidden" class="json_member_communicable" name="communicable" value="false">'
         +'</div>'
         +'<div  class="handin_import-content_container-right_author-body_edit">'
         +'<i class="selected-func_delete selected-func_nodelete" onclick="PubEdit.deletetarget(this)"></i>'
         +'<i class="selected-func_down" onclick="PubEdit.downchange(this)"></i>'
         +'<i class="selected-func_up" onclick="PubEdit.upchange(this);"></i>'
         +'</div>';
     document.getElementsByClassName("handin_import-content_container-right_infor")[0].appendChild(box);
     if(document.getElementsByClassName("selected-func_up-none").length>0){
         document.getElementsByClassName("selected-func_up-none")[0].classList.remove("selected-func_up-none");              
     }
     if(document.getElementsByClassName("selected-func_down-none").length>0){
         document.getElementsByClassName("selected-func_down-none")[0].classList.remove("selected-func_down-none");              
     }
     if(document.getElementsByClassName("selected-func_down")&&document.getElementsByClassName("selected-func_up")){
         var length = document.getElementsByClassName("selected-func_down").length;
         if(document.getElementsByClassName("selected-func_up")[0]){
             document.getElementsByClassName("selected-func_up")[0].classList.add("selected-func_up-none");              
         }
         if(document.getElementsByClassName("selected-func_down")[length-1]){
             document.getElementsByClassName("selected-func_down")[length-1].classList.add("selected-func_down-none");               
         }
     }
     window.addFormElementsEvents(box);
     PubEdit.addMenberInputOn();
     $(".selected-func_delete:eq(-2)").removeClass("selected-func_nodelete");
};

function callbackCategory(obj){
    PubEdit.validObj(obj);
};
function callbackGrade(obj){
    PubEdit.validObj(obj);
};
function callbackDate(obj){
    PubEdit.validObj(obj);
};
</script>
<div class="handin_import-container_title">
  <div class="handin_import-container_title-left">
    <span class="handin_import-container_title-left_content"><spring:message code="pub.enter.sglr" /></span> (<span
      class="handin_import-container_title-left_tip"><spring:message code="pub.enter.dai" /><span
      class="handin_import-content_container-tip">*</span> <spring:message code="pub.enter.hdwbt" /></span>) <i id="pubPermissionDiv"
      class='selected-func_close <c:if test="${pubVo.permission!=4 }">selected-func_close-open</c:if>'
      onclick="PubEdit.changPubPermission(this)"
      title='<c:if test="${pubVo.permission!=4 }"><spring:message code="pub.permission.public" /></c:if><c:if test="${pubVo.permission==4 }"><spring:message code="pub.permission.onlyMe" /></c:if>'></i>
    <input type="hidden" class="json_permission" name="pubPermission" value="${pubVo.permission}" />
  </div>
  <div class="handin_import-container_title-right">
    <div></div>
    <input type="hidden" id="returnOldType" value="${des3pdwhPubId }" /> <input type="hidden" id="titleDes3PdwhId"
      value="" /> <input type="hidden" name="pubId" value="${pubVo.des3PubId }" class="json_des3PubId" /> <input
      type="hidden" name="psnId" value="${pubVo.des3PsnId }" class="json_des3PsnId" /> <input type="hidden"
      name="recordFrom" value="${pubVo.recordFrom.value }" class="json_recordFrom" /> <input type="hidden" name="pubGenre"
      value="${pubVo.pubGenre }" class="json_pubGenre" /> <input type="hidden" name="isProjectPub"
      value="${pubVo.isProjectPub }" class="json_isProjectPub" /> <input type="hidden" name="des3GrpId"
      value="${pubVo.des3GrpId }" class="json_des3GrpId" /> <input type="hidden" name="des3GrpId"
      value="${pubVo.oldPubType }" class="json_oldPubType" />
    <!-- 这个是改变成果类型的时候避免丢掉的字段 begin -->
    <input type="hidden" class="change_type_publishDate" value="${pubVo.publishDate }" /> <input type="hidden"
      class="change_type_countryId" value="${pubVo.countryId }" />
    <!-- 这个是改变成果类型的时候避免丢掉的字段 end -->
    <c:if test="${not empty pubVo.modifyDate }">
      <div style="font-size: 14px; color: #999;">
        <span><spring:message code="pub.enter.zjbc" /></span> <span>${pubVo.modifyDate }</span>
      </div>
    </c:if>
  </div>
</div>
<input type="hidden" name="updateMark" value="${pubVo.updateMark }" class="json_updateMark" />