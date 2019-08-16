<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
PubRecommend.defultKeyList=$("#defultKeyJson").val(); 
PubRecommend.searchPsnKey="";
PubRecommend.addPsnKey="";
var locale='${locale}';
$(document).ready(function(){
	var targetlist = document.getElementsByClassName("click-target_btn");
	/* var hoverlist = $("#area_ul li"); */
	var hoverkeylist = $("#key_ul li");
	/* var textarea = "<i class='material-icons filter-value__cancel delete_area' onclick='deleteScienArea(this)'>close</i>"; */
	/* var textkey = '<i class="material-icons filter-value__cancel delete_key" onclick="deleteKeyWord(this)">close</i>' */
	/* PubRecommend.addHover(hoverlist,textarea); */
	/* PubRecommend.addHover(hoverkeylist,textkey); */
 	addFormElementsEvents();  
    for(var i = 0; i < targetlist.length;i++){
    	
        targetlist[i].onclick = function(){
          if(this.innerHTML=="expand_less"){
            this.innerHTML="expand_more";
            this.closest(".setting-parent").querySelector(".setting-list").style.display="none";
          }else{
            this.innerHTML="expand_less";
            this.closest(".setting-parent").querySelector(".setting-list").style.display="block";
          }
        }
    }
});

 //输入框添加科技领域
function callbackArea(){
	var areaCode = $("#choooseScienceAreaIds").val();
	if(areaCode != null && areaCode != ""){
		$.ajax({
			url: '/pub/pubconditions/ajaxAddScienArea',
			type: 'post',
			dataType: 'json',
			data:{'addPsnAreaCode' : areaCode},
			success : function(data){
				if(data.result == 'success'){
					$("#area_ul").empty();
					var scienceAreaIds = new Array;
					$.each(data.scienceAreaList,function(n,areaStr) { 
						var title;
						if(locale == 'zh_CN'){
							title = areaStr.scienceArea;
						}else{
							title = areaStr.enScienceArea;
						}
						var html = '<li class="item_list-align"><div class="item_list-align_item type_area" style="width:195px;" data-id="add-areacode" onclick="PubRecommend.addCondition(this,\'type_area\')" value="'+areaStr.scienceAreaId+'" title="'+ title +'">'+title+'</div></i>';  
                        $("#area_ul").prepend(html); 
                        PubRecommend.addCondition($(".type_area")[0],'type_area');
                        scienceAreaIds.push(areaStr.scienceAreaId);
				})
				$("#defultArea").val(scienceAreaIds.join(","));
				hideScienceAreaBox();
				PubRecommend.ajaxLondPubList();				
			}
			}
		});
	}else{
		scmpublictoast(pubRecommend.addEmptyArea,1500);
	}
}
 
$("#area_input").keyup(function(event){ 
	if(event.keyCode == 13){
		callbackArea();
	}
});
function callbackKey(){
	if($(".item_list-align_item.type_key").length < 10){
		addKeyWord();
	}else{
		//最多只能选择5个关键词 pubRecommend.addKeySizeFail
		scmpublictoast("最多只能选择10个关键词",2000);
	}
	
}
$("#serchKeyInput").on('input',function(e){  
	var code = $("#serchKeyInput").attr("code");
	if(!code || code ==""){
		PubRecommend.addPsnKey=$("#serchKeyInput").val();	
	}
}); 

</script>
<input type="hidden" id="defultArea" value='<c:out value="${pubQueryDTO.defultArea}"/>' />
<input type="hidden" id="defultKeyJson" value='<c:out value="${pubQueryDTO.defultKeyJson}"/>' />
<input type="hidden" id="searchPsnKey" name="searchPsnKey" value='' />
<input type="hidden" id="searchArea" value="" />
<input type="hidden" id="searchPubYear" value="" />
<input type="hidden" id="searchPubType" value="" />
<input type="hidden" id="scienceAreaIds" value="${pubQueryDTO.scienceAreaIds}" />
<div class="setting" style="overflow-y: auto; overflow-x: hidden; width: 230px;">
  <div class="l_setting_tit">
    <spring:message code="pub.recommend.setting" />
  </div>
  <div class="setting-parent">
    <div class="setting-title" style="padding-left: 0px; width: 230px; display: block;">
      <strong><spring:message code="pub.recommend.area" /></strong> <i class="material-icons click-target_btn">expand_less</i>
    </div>
    <div class="setting-list condition-list">
      <input itemEvent="callbackArea" class="search-box chip-panel__manual-input js_autocompletebox" id="area_input"
        type="hidden" request-url="/psnweb/pubrecommend/ajaxgetScienceArea" manual-input="no" />
      <div class="setting-list_searchbtn" style="width: 195px" id="area_input" onclick="showScienceAreaBox()">
        <spring:message code='pub.recommend.search.area' />
      </div>
      <ul id="area_ul">
        <c:forEach items="${pubQueryDTO.areaList}" var="area">
          <li class="item_list-align"><c:if test="${locale== 'en_US'}">
              <div class="item_list-align_item type_area" style="width: 195px;"
                onclick="PubRecommend.addCondition(this,'type_area')" value="${area.scienceAreaId}"
                title='<c:out value="${area.enScienceArea}"/>'>${area.enScienceArea}</div>
            </c:if> <c:if test="${locale== 'zh_CN'}">
              <div class="item_list-align_item type_area" style="width: 195px;"
                onclick="PubRecommend.addCondition(this,'type_area')" value="${area.scienceAreaId}"
                title='<c:out value="${area.scienceArea}"/>'>${area.scienceArea}</div>
            </c:if></li>
        </c:forEach>
      </ul>
    </div>
  </div>
  <div class="setting-parent">
    <div class="setting-title" style="padding-left: 0px; width: 230px; display: block;">
      <strong><spring:message code="pub.recommend.keyword" /></strong> <i class="material-icons click-target_btn">expand_less</i>
    </div>
    <div class="setting-list">
      <div class="setting-list_searchbtn" style="width: 195px" id="serchKeyInput" onclick="showKeyWordsBox();">
        <spring:message code='pub.recommend.search.keyword' />
      </div>
      <!-- /psnweb/recommend/ajaxautoconstkeydiscscodeid -->
      <!--  callbackKey() -->
      <ul id="key_ul">
        <c:forEach items="${pubQueryDTO.keyList}" var="disciplineKey">
          <li class="item_list-align">
            <div class="item_list-align_item type_key" style="width: 195px;"
              onclick="PubRecommend.addCondition(this,'type_key')" code="${keyId}"
              value="<c:out value='${disciplineKey.keyWords}'/>" title="<c:out value='${disciplineKey.keyWords}'/>">
              <c:out value="${disciplineKey.keyWords}" />
            </div>
          </li>
        </c:forEach>
      </ul>
    </div>
  </div>
  <div class="setting-parent">
    <div class="setting-title" style="padding-left: 0px; width: 230px; display: block;">
      <strong><spring:message code="pub.recommend.publish" /></strong> <i class="material-icons click-target_btn">expand_less</i>
    </div>
    <div class="setting-list">
      <ul id="year_ul">
        <c:forEach items="${pubQueryDTO.pubYearMap}" var="pubYear">
          <li class="item_list-align">
            <div class="item_list-align_item type_time" style="width: 195px;"
              onclick="PubRecommend.addCondition(this,'type_time')" value="${pubYear.key}">${pubYear.value}</div>
          </li>
        </c:forEach>
      </ul>
    </div>
  </div>
  <div class="setting-parent">
    <div class="setting-title" style="padding-left: 0px; width: 230px; display: block;">
      <strong><spring:message code="pub.recommend.type" /></strong> <i class="material-icons click-target_btn">expand_less</i>
    </div>
    <div class="setting-list">
      <ul id="type_ul">
      <li class="item_list-align">
        <div class="item_list-align_item type_pub" style="width: 195px;"
                onclick="PubRecommend.addCondition(this,'type_pub')" value="4"><spring:message code="pub.filter.journalArticle" /></div>
      </li>
      <li class="item_list-align">
        <div class="item_list-align_item type_pub" style="width: 195px;"
                onclick="PubRecommend.addCondition(this,'type_pub')" value="3"><spring:message code="pub.filter.conferencePaper" /></div>
      </li>
      <li class="item_list-align">
        <div class="item_list-align_item type_pub" style="width: 195px;"
                onclick="PubRecommend.addCondition(this,'type_pub')" value="5"><spring:message code="pub.filter.patent" /></div>
      </li>
      <li class="item_list-align">
        <div class="item_list-align_item type_pub" style="width: 195px;"
                onclick="PubRecommend.addCondition(this,'type_pub')" value="7"><spring:message code="pub.filter.indexes.others" /></div>
      </li>
        <%-- <c:forEach items="${pubQueryDTO.pubTypeList}" var="pubType">
          <li class="item_list-align"><c:if test="${locale== 'en_US'}">
              <div class="item_list-align_item type_pub" style="width: 195px;"
                onclick="PubRecommend.addCondition(this,'type_pub')" value="${pubType.id}">${pubType.enName}</div>
            </c:if> <c:if test="${locale== 'zh_CN'}">
              <div class="item_list-align_item type_pub" style="width: 195px;"
                onclick="PubRecommend.addCondition(this,'type_pub')" value="${pubType.id}">${pubType.zhName}</div>
            </c:if></li>
        </c:forEach> --%>
      </ul>
    </div>
  </div>
</div>