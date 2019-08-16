<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<link href="${resmod}/smate-pc/new-mypaper/public2016.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
var role="${role}";
var ispending = "${ispending}";
var curentYear = "${curentYear}";
var lastYear = ${lastYear} ;
var recentYear5 = "${recentYear5}";
var recentYear10 = "${recentYear10}";

var shareTips = "<s:text name='groups.pub.dynamic.share' />";
$(document).ready(function(){
	GrpPub.showGrpPubList();
	//需要判断权限 没有权限 就不请求了
	if(role==1 || role==2){
		GrpPub.showGrpPubRcmd();
	}
	if(role==1 || role==2|| role==3){
		GrpPub.showGrpMemberPubSum();
	}
		var targetelem = document.getElementsByClassName("filter-section__toggle");
		for(var i = 0 ; i< targetelem.length; i++){
			targetelem[i].onclick = function(){
				if(this.innerHTML==="expand_less"){
					 this.innerHTML = "expand_more";
					 this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="none";
				}else{
					 this.innerHTML = "expand_less";
					 this.closest(".filter-list__section").querySelector(".filter-value__list").style.display="block";
				}
			}
		};	
		var targetlist = document.getElementsByClassName("searchbox__main");
		for(var i = 0; i< targetlist.length; i++){
			targetlist[i].querySelector("input").onfocus = function(){
				this.closest(".searchbox__main").style.cssText = "border:1px solid #3faffa;";
			}
			targetlist[i].querySelector("input").onblur = function(){
				this.closest(".searchbox__main").style.cssText = "border:1px solid #ccc;";		
			}
		}
		document.onkeydown = function(event){
		   if(event.keyCode == 27){
		      event.stopPropagation();
		      event.preventDefault();
		      GrpPub.hideGrpPubSelectImportType();
		   }
		}
});
//==============================
function sharePsnCallback (dynId,shareContent,resId,pubId,isB2T,receiverGrpId){
	$.ajax({
        url : '/pub/opt/ajaxshare',
        type : 'post',
        dataType : 'json',
        data : {'des3PubId':resId,
             'comment':shareContent,
             'sharePsnGroupIds':receiverGrpId,
             'platform':"2"
               },
        success : function(data) {
            if (data.result == "success") {
                $('.dev_pub_share_'+pubId).text(shareTips+"("+data.shareTimes+")");
            }
        }
    });
}
function shareGrpCallback (dynId,shareContent,resId,pubId,isB2T,receiverGrpId){
	var count = Number($('.dev_pub_share_'+pubId).text().replace(/[\D]/ig,""))+1;
	$('.dev_pub_share_'+pubId).text(shareTips+"("+count+")");
}
//==============================
 //分享回调
function shareCallback (dynId,shareContent,resId,pubId,isB2T,receiverGrpId){
	var count = Number($('.dev_pub_share_'+pubId).text().replace(/[\D]/ig,""))+1;
    $('.dev_pub_share_'+pubId).text(shareTips+"("+count+")");
}; 

</script>
<div class="container__horiz_left width-8">
  <div class="container__card">
    <div class="main-list">
      <div class="main-list__header">
        <div class="main-list-header__title">
          <s:if test="grpCategory  == 11">
            <div class="filter-list checkbox-style" list-filter="grppub" showModule=${module }>
              <s:if test=" module=='projectPub'  ">
                <div class="filter-list__section" filter-section="showPrjPub" filter-method="master"
                  style="display: none;">
                  <ul class="filter-value__list">
                    <li class="filter-value__item" filter-value="1">
                      <div class="input-custom-style">
                        <input type="checkbox" checked> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="filter-value__option">
                        <s:text name='groups.pub.proOutputs' />
                      </div>
                    </li>
                  </ul>
                </div>
              </s:if>
              <s:else>
                <div class="filter-list__section" filter-section="showRefPub" filter-method="master"
                  style="display: none;">
                  <ul class="filter-value__list">
                    <li class="filter-value__item" filter-value="1">
                      <div class="input-custom-style">
                        <input type="checkbox" checked> <i class="material-icons custom-style"></i>
                      </div>
                      <div class="filter-value__option">
                        <s:text name='groups.pub.references' />
                      </div>
                    </li>
                  </ul>
                </div>
              </s:else>
            </div>
          </s:if>
        </div>
        <div class="main-list-header__searchbox" style="margin-right: 64px;">
          <div class="searchbox__container main-list__searchbox" list-search="grppub">
            <div class="searchbox__main">
              <input placeholder="<s:text name='groups.pub.search' />">
              <div class="searchbox__icon material-icons"></div>
            </div>
          </div>
        </div>
        <button class="button_main button_icon" onclick="GrpPub.showGrpPubFilterBox();">
          <i class="material-icons">filter_list</i>
        </button>
        <c:if test="${role==1 || role==2 || role==3 }">
          <button class="button_main button_primary-reverse" onclick="GrpPub.showGrpPubSelectImportType();">
            <s:if test=" module=='projectPub'  ">
              <s:text name='groups.pub.addPub' />
            </s:if>
            <s:else>
              <s:text name='groups.pub.addRef' />
            </s:else>
          </button>
        </c:if>
      </div>
      <div class="main-list__top-flexbox global__padding_8" style="display: none; flex-direction: column;"
        id="grp_pub_filter_box">
        <div class="filter-list horiz-style" list-filter="grppub">
          <div class="filter-list__section js_filtersection" filter-section="publishYear" filter-method="single">
            <div class="filter-section__header" style="width: 100%;">
              <div class="filter-section__title">
                <s:text name='groups.pub.pubYear' />
              </div>
              <i class="material-icons filter-section__toggle">expand_less</i>
            </div>
            <ul class="filter-value__list">
              <li class="filter-value__item js_filtervalue" filter-value="${curentYear}">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption">
                  ${curentYear}
                  <s:text name='groups.pub.pubYear.suffix' />
                </div>
                <div class="filter-value__stats js_filterstats">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
              <li class="filter-value__item js_filtervalue" filter-value="${lastYear}">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption">
                  ${lastYear}
                  <s:text name='groups.pub.pubYear.suffix' />
                </div>
                <div class="filter-value__stats js_filterstats">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
              <li class="filter-value__item js_filtervalue" filter-value="${recentYear5}">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption">
                  <s:text name='groups.pub.pubYear.lasted5' />
                </div>
                <div class="filter-value__stats js_filterstats">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
              <li class="filter-value__item js_filtervalue"
                filter-value="${recentYear10}">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption">
                  <s:text name='groups.pub.pubYear.lasted10' />
                </div>
                <div class="filter-value__stats js_filterstats">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
            </ul>
          </div>
          <div class="filter-list__section js_filtersection" filter-section="pubType" filter-method="multiple">
            <div class="filter-section__header" style="width: 100%;">
              <div class="filter-section__title">
                <s:text name='groups.pub.pubType' />
              </div>
              <i class="material-icons filter-section__toggle">expand_less</i>
            </div>
            <ul class="filter-value__list">
              <li class="filter-value__item js_filtervalue" filter-value="4">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption">
                  <s:text name='groups.pub.pubType.journal' />
                </div>
                <div class="filter-value__stats js_filterstats">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
              <li class="filter-value__item js_filtervalue" filter-value="3">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption">
                  <s:text name='groups.pub.pubType.conferPaper' />
                </div>
                <div class="filter-value__stats js_filterstats">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
              <li class="filter-value__item js_filtervalue" filter-value="5">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption">
                  <s:text name='groups.pub.pubType.patent' />
                </div>
                <div class="filter-value__stats js_filterstats">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
              <li class="filter-value__item js_filtervalue" filter-value="1,2,7,8,10,12,13">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption">
                  <s:text name='groups.pub.pubType.other' />
                </div>
                <div class="filter-value__stats js_filterstats">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
            </ul>
          </div>
          <div class="filter-list__section js_filtersection" filter-section="includeType" filter-method="multiple">
            <div class="filter-section__header" style="width: 100%;">
              <div class="filter-section__title">
                <s:text name='groups.pub.indexType' />
              </div>
              <i class="material-icons filter-section__toggle">expand_less</i>
            </div>
            <ul class="filter-value__list">
              <li class="filter-value__item js_filtervalue" filter-value="scie">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption">SCIE</div>
                <div class="filter-value__stats js_filterstats">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
              <li class="filter-value__item js_filtervalue" filter-value="ssci">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption">SSCI</div>
                <div class="filter-value__stats js_filterstats">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
              <li class="filter-value__item js_filtervalue" filter-value="ei">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption">EI</div>
                <div class="filter-value__stats js_filterstats">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
              <li class="filter-value__item js_filtervalue" filter-value="istp">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption">ISTP</div>
                <div class="filter-value__stats js_filterstats">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
            </ul>
          </div>
          <div class="filter-list__section js_filtersection" filter-section="orderBy" filter-method="compulsory">
            <div class="filter-section__header" style="width: 100%;">
              <div class="filter-section__title">
                <s:text name='groups.pub.order' />
              </div>
              <i class="material-icons filter-section__toggle">expand_less</i>
            </div>
            <ul class="filter-value__list">
              <li class="filter-value__item js_filtervalue" filter-value="createDate">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption">
                  <s:text name='groups.pub.order.newAdd' />
                </div>
                <div class="filter-value__stats js_filterstats">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
              <li class="filter-value__item js_filtervalue" filter-value="publishYear">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption">
                  <s:text name='groups.pub.order.newPub' />
                </div>
                <div class="filter-value__stats js_filterstats">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
              <li class="filter-value__item js_filtervalue" filter-value="citedTimes">
                <div class="input-custom-style">
                  <input type="checkbox"> <i class="material-icons custom-style"></i>
                </div>
                <div class="filter-value__option js_filteroption">
                  <s:text name='groups.pub.order.cite' />
                </div>
                <div class="filter-value__stats js_filterstats">(0)</div> <i
                class="material-icons filter-value__cancel js_filtercancel">close</i>
              </li>
            </ul>
          </div>
        </div>
        <div class="main-list__top-flexbox_upicon">
          <i class="material-icons" onclick="GrpPub.showGrpPubFilterBox();">expand_less</i>
        </div>
      </div>
      <div class="main-list__list" id="grp_pubs_List" list-main="grppub"></div>
      <div class="main-list__footer">
        <div class="pagination__box" list-pagination="grppub">
          <!-- 翻页 -->
        </div>
      </div>
    </div>
  </div>
</div>
<div class="container__horiz_right width-4">
  <div class="container__card" id="grp_pub_rcmd"></div>
  <div class="container__card" id="grp_member_pub" style="margin-top: 43px;"></div>
</div>
<div class="dialogs__box"
  style="max-width: 100%; min-height: calc(100% - 8px); box-shadow: none; background-color: transparent; justify-content: flex-start;"
  dialog-id="grp_member_pub_box" id="grp_member_pub_box" flyin-direction="left">
  <div class="dialogs__childbox_adapted" style="height: 2560px; max-height: 100%; overflow-y: hidden;">
    <div class="container__horiz" style="height: 100%; width: 100%; margin-top: 72px !important;">
      <div class="container__horiz_left width-8" style="height: 100%">
        <div class="main-list"
          style="box-shadow: 0 2px 4px -1px hsla(0, 0%, 0%, 0.20), 0 4px 5px 0 hsla(0, 0%, 0%, 0.14), 0 1px 10px 0 hsla(0, 0%, 0%, 0.12); /* height: 100%; */ height: 810px;">
          <div class="main-list__header">
            <div class="main-list-header__title"></div>
            <div class="main-list-header__searchbox">
              <div class="searchbox__container main-list__searchbox" list-search="memberpub">
                <div class="searchbox__main">
                  <input placeholder=" <s:text name='groups.pub.search' />">
                  <div class="searchbox__icon material-icons"></div>
                </div>
              </div>
            </div>
            <button class="button_main button_icon" onclick="GrpMemberPub.showGrpMemberPubFilterBox();">
              <i class="material-icons">filter_list</i>
            </button>
            <button class="button_main button_error-reverse" onclick="GrpMemberPub.hideMemberPub();">
              <s:text name='groups.pub.member.backPub' />
            </button>
            <button class="button_main button_primary-reverse" onclick="GrpMemberPub.importMemberPubToGrp(this);">
              <s:text name='groups.pub.member.importPub' />
            </button>
          </div>
          <div class="main-list__top-flexbox global__padding_8" style="display: none; flex-direction: column;"
            id="grp_member_pub_filter_box">
            <div class="filter-list horiz-style" list-filter="memberpub">
              <div class="filter-list__section js_filtersection" filter-section="publishYear" filter-method="single">
                <div class="filter-section__header" style="width: 100%;">
                  <div class="filter-section__title">
                    <s:text name='groups.pub.pubYear' />
                  </div>
                  <i class="material-icons filter-section__toggle">expand_less</i>
                </div>
                <ul class="filter-value__list">
                  <li class="filter-value__item js_filtervalue" filter-value="2019">
                    <div class="input-custom-style">
                      <input type="checkbox"> <i class="material-icons custom-style"></i>
                    </div>
                    <div class="filter-value__option js_filteroption">
                      2019
                      <s:text name='groups.pub.pubYear.suffix' />
                    </div>
                    <div class="filter-value__stats js_filterstats">(0)</div> <i
                    class="material-icons filter-value__cancel js_filtercancel">close</i>
                  </li>
                  <li class="filter-value__item js_filtervalue" filter-value="2016">
                    <div class="input-custom-style">
                      <input type="checkbox"> <i class="material-icons custom-style"></i>
                    </div>
                    <div class="filter-value__option js_filteroption">
                      2016
                      <s:text name='groups.pub.pubYear.suffix' />
                    </div>
                    <div class="filter-value__stats js_filterstats">(0)</div> <i
                    class="material-icons filter-value__cancel js_filtercancel">close</i>
                  </li>
                  <li class="filter-value__item js_filtervalue" filter-value="2017,2016,2015,2014,2013,2012">
                    <div class="input-custom-style">
                      <input type="checkbox"> <i class="material-icons custom-style"></i>
                    </div>
                    <div class="filter-value__option js_filteroption">
                      <s:text name='groups.pub.pubYear.lasted5' />
                    </div>
                    <div class="filter-value__stats js_filterstats">(0)</div> <i
                    class="material-icons filter-value__cancel js_filtercancel">close</i>
                  </li>
                  <li class="filter-value__item js_filtervalue"
                    filter-value="2017,2016,2015,2014,2013,2012,2011,2010,2009,2008,2007">
                    <div class="input-custom-style">
                      <input type="checkbox"> <i class="material-icons custom-style"></i>
                    </div>
                    <div class="filter-value__option js_filteroption">
                      <s:text name='groups.pub.pubYear.lasted10' />
                    </div>
                    <div class="filter-value__stats js_filterstats">(0)</div> <i
                    class="material-icons filter-value__cancel js_filtercancel">close</i>
                  </li>
                </ul>
              </div>
              <div class="filter-list__section js_filtersection" filter-section="pubType" filter-method="multiple">
                <div class="filter-section__header" style="width: 100%;">
                  <div class="filter-section__title">
                    <s:text name='groups.pub.pubType' />
                  </div>
                  <i class="material-icons filter-section__toggle">expand_less</i>
                </div>
                <ul class="filter-value__list">
                  <li class="filter-value__item js_filtervalue" filter-value="4">
                    <div class="input-custom-style">
                      <input type="checkbox"> <i class="material-icons custom-style"></i>
                    </div>
                    <div class="filter-value__option js_filteroption">
                      <s:text name='groups.pub.pubType.journal' />
                    </div>
                    <div class="filter-value__stats js_filterstats">(0)</div> <i
                    class="material-icons filter-value__cancel js_filtercancel">close</i>
                  </li>
                  <li class="filter-value__item js_filtervalue" filter-value="3">
                    <div class="input-custom-style">
                      <input type="checkbox"> <i class="material-icons custom-style"></i>
                    </div>
                    <div class="filter-value__option js_filteroption">
                      <s:text name='groups.pub.pubType.conferPaper' />
                    </div>
                    <div class="filter-value__stats js_filterstats">(0)</div> <i
                    class="material-icons filter-value__cancel js_filtercancel">close</i>
                  </li>
                  <li class="filter-value__item js_filtervalue" filter-value="5">
                    <div class="input-custom-style">
                      <input type="checkbox"> <i class="material-icons custom-style"></i>
                    </div>
                    <div class="filter-value__option js_filteroption">
                      <s:text name='groups.pub.pubType.patent' />
                    </div>
                    <div class="filter-value__stats js_filterstats">(0)</div> <i
                    class="material-icons filter-value__cancel js_filtercancel">close</i>
                  </li>
                  <li class="filter-value__item js_filtervalue" filter-value="1,2,7,8,10,12,13">
                    <div class="input-custom-style">
                      <input type="checkbox"> <i class="material-icons custom-style"></i>
                    </div>
                    <div class="filter-value__option js_filteroption">
                      <s:text name='groups.pub.pubType.other' />
                    </div>
                    <div class="filter-value__stats js_filterstats">(0)</div> <i
                    class="material-icons filter-value__cancel js_filtercancel">close</i>
                  </li>
                </ul>
              </div>
              <div class="filter-list__section js_filtersection" filter-section="includeType" filter-method="multiple">
                <div class="filter-section__header" style="width: 100%;">
                  <div class="filter-section__title">
                    <s:text name='groups.pub.indexType' />
                  </div>
                  <i class="material-icons filter-section__toggle">expand_less</i>
                </div>
                <ul class="filter-value__list">
                  <li class="filter-value__item js_filtervalue" filter-value="scie">
                    <div class="input-custom-style">
                      <input type="checkbox"> <i class="material-icons custom-style"></i>
                    </div>
                    <div class="filter-value__option js_filteroption">SCIE</div>
                    <div class="filter-value__stats js_filterstats">(0)</div> <i
                    class="material-icons filter-value__cancel js_filtercancel">close</i>
                  </li>
                  <li class="filter-value__item js_filtervalue" filter-value="ssci">
                    <div class="input-custom-style">
                      <input type="checkbox"> <i class="material-icons custom-style"></i>
                    </div>
                    <div class="filter-value__option js_filteroption">SSCI</div>
                    <div class="filter-value__stats js_filterstats">(0)</div> <i
                    class="material-icons filter-value__cancel js_filtercancel">close</i>
                  </li>
                  <li class="filter-value__item js_filtervalue" filter-value="ei">
                    <div class="input-custom-style">
                      <input type="checkbox"> <i class="material-icons custom-style"></i>
                    </div>
                    <div class="filter-value__option js_filteroption">EI</div>
                    <div class="filter-value__stats js_filterstats">(0)</div> <i
                    class="material-icons filter-value__cancel js_filtercancel">close</i>
                  </li>
                  <li class="filter-value__item js_filtervalue" filter-value="istp">
                    <div class="input-custom-style">
                      <input type="checkbox"> <i class="material-icons custom-style"></i>
                    </div>
                    <div class="filter-value__option js_filteroption">ISTP</div>
                    <div class="filter-value__stats js_filterstats">(0)</div> <i
                    class="material-icons filter-value__cancel js_filtercancel">close</i>
                  </li>
                </ul>
              </div>
              <div class="filter-list__section js_filtersection" filter-section="orderBy" filter-method="compulsory">
                <div class="filter-section__header" style="width: 100%;">
                  <div class="filter-section__title">
                    <s:text name='groups.pub.order' />
                  </div>
                  <i class="material-icons filter-section__toggle">expand_less</i>
                </div>
                <ul class="filter-value__list" id="id_order_type">
                  <li class="filter-value__item js_filtervalue option_selected" filter-value="createDate">
                    <div class="input-custom-style">
                      <input type="checkbox"> <i class="material-icons custom-style"></i>
                    </div>
                    <div class="filter-value__option js_filteroption">
                      <s:text name='groups.pub.order.newAdd' />
                    </div>
                    <div class="filter-value__stats js_filterstats">(0)</div> <i
                    class="material-icons filter-value__cancel js_filtercancel">close</i>
                  </li>
                  <li class="filter-value__item js_filtervalue" filter-value="publishYear">
                    <div class="input-custom-style">
                      <input type="checkbox"> <i class="material-icons custom-style"></i>
                    </div>
                    <div class="filter-value__option js_filteroption">
                      <s:text name='groups.pub.order.newPub' />
                    </div>
                    <div class="filter-value__stats js_filterstats">(0)</div> <i
                    class="material-icons filter-value__cancel js_filtercancel">close</i>
                  </li>
                  <li class="filter-value__item js_filtervalue" filter-value="citedTimes">
                    <div class="input-custom-style">
                      <input type="checkbox"> <i class="material-icons custom-style"></i>
                    </div>
                    <div class="filter-value__option js_filteroption">
                      <s:text name='groups.pub.order.cite' />
                    </div>
                    <div class="filter-value__stats js_filterstats">(0)</div> <i
                    class="material-icons filter-value__cancel js_filtercancel">close</i>
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <div class="main-list__list" id="grp_member_pub_list" style="overflow-y: auto;" list-main="memberpub"></div>
          <div class="main-list__footer">
            <div class="pagination__box" list-pagination="memberpub">
              <!-- 翻页 -->
            </div>
          </div>
        </div>
      </div>
      <div class="container__horiz_right width-4" style="height: 100%"></div>
    </div>
  </div>
</div>
<div class="dialogs__box dialogs__childbox_limited-big" style="width: auto; min-height: 285px; height: 285px; padding: 0px 20px; border-radius: 5px;"
  dialog-id="grp_pub_select_import_type" flyin-direction="bottom" id="grp_pub_select_import_type">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header" style="min-height: 52px;">
      <div class="dialogs__header_title" style="font-size: 18px; font-weight: normal; padding: 20px 0px 12px 0px;">
        <s:text name='groups.pub.importTypes' />
      </div>
      <i class="list-results_close" onclick="GrpPub.hideGrpPubSelectImportType();"
        style="padding: 0px; margin: 0px; width: 24px; height: 24px;"></i>
    </div>
  </div>
  <div class="dialogs__childbox_adapted">
    <div class="import-methods__box">
      <div class="import-methods__sxn" onclick="GrpPub.searchPubImport()">
        <div class="import-methods__sxn_logo online-search"></div>
        <div class="import-methods__sxn_name">
          <s:text name='groups.pub.importTypes.database' />
        </div>
        <div class="import-methods__sxn_explain">
          <s:text name='groups.pub.importTypes.dbDesc' />
        </div>
      </div>
      <div class="import-methods__sxn" onclick="GrpPub.manualImportPub()">
        <div class="import-methods__sxn_logo manual-entry"></div>
        <div class="import-methods__sxn_name">
          <s:text name='groups.pub.importTypes.entry' />
        </div>
        <div class="import-methods__sxn_explain">
          <s:text name='groups.pub.importTypes.entryDesc' />
        </div>
      </div>
      <!--SCM-16536 添加导入群组个人成果  -->
      <div class="import-methods__sxn" style="" onclick="GrpPub.manualImpMemberPub(event)">
        <div class="import-methods__sxn_logo member-results"></div>
        <div class="import-methods__sxn_name">
          <s:text name='groups.pub.importTypes.ImpMemberPub' />
        </div>
        <div class="import-methods__sxn_explain">
          <s:text name='groups.pub.importTypes.ImpMemberPubDesc' />
        </div>
      </div>
    </div>
  </div>
</div>
<div class="dialogs__box" style="width: 720px;" dialog-id="show_all_pub_rcmd" flyin-direction="bottom"
  id="show_all_pub_rcmd">
  <div class="dialogs__childbox_fixed">
    <div class="dialogs__header">
      <div class="dialogs__header_title">
        <s:text name='groups.pub.member.confirmPub' />
      </div>
      <button class="button_main button_primary-reverse" onclick="GrpPub.confirmAllRcmdGrpPub()">
        <s:text name='groups.pub.member.confirmAll' />
      </button>
      <button class="button_main button_icon" onclick="GrpPub.hideGrpPubRcmdBox();">
        <i class="material-icons">close</i>
      </button>
    </div>
  </div>
  <div class="dialogs__childbox_adapted" style="height: 560px;">
    <div class="main-list__list" id="grp_pub_rcmd_list"></div>
  </div>
</div>
