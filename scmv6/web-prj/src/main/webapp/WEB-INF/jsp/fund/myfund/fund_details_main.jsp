<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="fund_details_citation.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><s:text name='homepage.fundmain.recommend' /></title>
<c:if test="${!empty constFundCategoryInfo.description }">
  <meta name="description" content="<c:out value="${constFundCategoryInfo.description}"/>" />
</c:if>
<c:if test="${empty constFundCategoryInfo.description }">
  <meta name="description" content="<s:text name='homepage.fundmain.description'/>" />
</c:if>
<link href="${resmod}/smate-pc/new-fundRecommend/header2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/new-fundRecommend/public2016.css" rel="stylesheet" type="text/css" />
<link href="${resmod}/smate-pc/new-fundRecommend/footer2016.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="${resmod}/css/plugin/dialog.css" />
<script type="text/javascript" src="${resmod}/js/smate.share.js"></script>
<script type="text/javascript" src="${resmod}/js/smate.share_${locale}.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/share/jquery.dyn.share.plugin.js"></script>
<script type="text/javascript" src="${resmod}/js/sharebutton.js"></script>
<script type="text/javascript" src="${resmod}/js/json2.js"></script>
<script type="text/javascript" src="${resmod}/smate-pc/js/scmpc_form.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="${resscmsns}/js_v5/plugin/dialog.js"></script>
<script src="${resmod}/smate-pc/js/scmpc_dialogs.js" type="text/javascript"></script>
<script type="text/javascript" src="${ressns }/js/fund/fund_recommend.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	addFormElementsEvents();
	var desFundId = $("#encodeFundId").val();
	var fundId = $("#fundId").val();
	FundRecommend.initFundOperation(desFundId, fundId);
	document.getElementsByClassName("fund-detial")[0].style.minHeight = window.innerHeight - 145 - 149 + "px";
	document.getElementsByClassName("dy-fund_showRight-absoBox")[0].style.minHeight = window.innerHeight - 145 - 149 + "px";
});

//转义方法
function decodeHtml(s){
  var REGX_HTML_DECODE = /&\w+;|&#(\d+);/g;
  var HTML_DECODE  = {
      "&lt;" : "<", 
      "&gt;" : ">", 
      "&amp;" : "&", 
      "&nbsp;": " ", 
      "&quot;": "\"", 
      "&copy;": ""
  };
  s = (s != undefined) ? s : this.toString();
  return (typeof s != "string") ? s :
      s.replace(REGX_HTML_DECODE,
                function($0, $1){
                    var c = HTML_DECODE[$0];
                    if(c == undefined){
                        if(!isNaN($1)){
                            c = String.fromCharCode(($1 == 160) ? 32:$1);
                        }else{
                            c = $0;
                        }
                    }
                    return c;
                });
  };
//初始化 分享 插件
function initSharePlugin(obj){
	if (locale == "en_US") {
		$(obj).dynSharePullMode({
			'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)',
			'language' : 'en_US'
		});
	} else {
		$(obj).dynSharePullMode({
			'shareToSmateMethod' : 'SmateShare.showShareToScmBox(event)'
		});
	}
};
//==============================
function sharePsnCallback (dynId,shareContent,resId,fundId,isB2T ,receiverGrpId){
	$.ajax({
		url : '/prjweb/fund/ajaxsharecount',
		type : 'post',
		dataType : 'json',
		data : {
			'des3FundId':resId
		},
		success : function(data) {
			if (data.result == "success") {
				$('.shareCount_'+fundId).text("("+data.shareCount+")");
			}
		}
	});
}
function shareGrpCallback (dynId,shareContent,resId,fundId,isB2T ,receiverGrpId){
	$.ajax({
		url : '/prjweb/fund/ajaxsharecount',
		type : 'post',
		dataType : 'json',
		data : {
			'des3FundId':resId
		},
		success : function(data) {
			if (data.result == "success") {
				$('.shareCount_'+fundId).text("("+data.shareCount+")");
			}
		}
	});
}
//==============================
//分享回调
function shareCallback (dynId,shareContent,resId,fundId,isB2T ,receiverGrpId){
    if(shareContent){
        $.ajax({
            url : '/prjweb/fund/ajaxsharecount',
            type : 'post',
            dataType : 'json',
            data : {
                'des3FundId':resId
            },
            success : function(data) {
                if (data.result == "success") {
                    $('.shareCount_'+fundId).html("("+data.shareCount+")");
                }
            }
        });
    }else{
		var count = Number($('.shareCount_'+fundId).text().replace(/[\D]/ig,""))+1;
		$('.shareCount_'+fundId).html("("+count+")");
    }
};

</script>
</head>
<body>
  <input type="hidden" name="encryptedFundId" value="${encryptedFundId }" id="encodeFundId" />
  <input type="hidden" name="fundId" value="${fundId }" id="fundId" />
  <input type="hidden" id="zhTitle_${fundId }" value="${constFundCategoryInfo.zhTitle }" />
  <input type="hidden" id="enTitle_${fundId }" value="${constFundCategoryInfo.enTitle }" />
  <input type="hidden" id="zhShowDesc_${fundId }" value="${constFundCategoryInfo.zhShowDesc }" />
  <input type="hidden" id="enShowDesc_${fundId }" value="${constFundCategoryInfo.enShowDesc }" />
  <input type="hidden" id="zhShowDescBr_${fundId }" value="${constFundCategoryInfo.zhShowDescBr }" />
  <input type="hidden" id="enShowDescBr_${fundId }" value="${constFundCategoryInfo.enShowDescBr }" />
  <div class="clear_h20"></div>

  <div class="dy-fund" >
    <div class="fund-detial" style="padding-bottom: 30px;">
      <div class="dy-fund_item-funcNeck">
        <div class="dy-fund_item-funcReader_container">
           <div>
             <span class="readCount_${fundId }  dy-fund_item-funcReader_num">0</span>
             <span class="dy-fund_item-funcReader_title"><s:text name='homepage.fundmain.recommend.read' /></span>
           </div>
        </div>
        <div class="fund-detial__btnbox" style="margin: 0px;">
            <div style="display: flex; height: 72px; align-items: center;">
              <c:if test="${not empty constFundCategoryInfo.guideUrl && !isStaleDated}">
                <div class="apply-button applybtn">
                  <a href="${constFundCategoryInfo.guideUrl}" target="_blank"><s:text
                      name='homepage.fundmain.apply.guide' /></a>
                </div>
              </c:if>
              <c:if test="${not empty constFundCategoryInfo.declareUrl && !isStaleDated}">
                <div class="apply-button applybtn-now">
                  <a href="${constFundCategoryInfo.declareUrl}" target="_blank"><s:text
                      name='homepage.fundmain.apply.applyNow' /></a>
                </div>
              </c:if>
             </div>
         </div>
      </div>
     <div style=" width: 100%; display: flex; align-items: flex-start;">
      <div style="width: 75%;">
      <div class="dy-fund_containner-title" id="fund_title_${fundId }">
        ${constFundCategoryInfo.fundTitle}
      </div>
      <div class="dy-fund_containner-content">
        <c:if test="${not empty constFundCategoryInfo.showYear}">
          <div class="dy-fund_item">
            <div class="dy-fund_title">
              <s:text name='homepage.fundmain.apply.year' />
            </div>
            <div class="dy-fund_inforcontent">
              ${constFundCategoryInfo.showYear}
            </div>
          </div>
        </c:if>
        <c:if test="${not empty constFundCategoryInfo.showDate}">
          <div class="dy-fund_item">
            <div class="dy-fund_title">
              <s:text name='homepage.fundmain.apply.date' />
            </div>
            <div class="dy-fund_inforcontent">
              ${constFundCategoryInfo.showDate}
            </div>
          </div>
        </c:if>
        <c:if test="${not empty constFundCategoryInfo.fundAgencyName}">
          <div class="dy-fund_item">
            <div class="dy-fund_title">
              <s:text name='homepage.fundmain.apply.agency' />
            </div>
            <div class="dy-fund_inforcontent">
              ${constFundCategoryInfo.fundAgencyName}
            </div>
          </div>
        </c:if>
        <c:if test="${not empty constFundCategoryInfo.strength}">
          <div class="dy-fund_item">
            <div class="dy-fund_title">
              <s:text name='homepage.fundmain.apply.funding' />
            </div>
            <div class="dy-fund_inforcontent">
              ${constFundCategoryInfo.strength}
            </div>
          </div>
        </c:if>
        <c:if test="${not empty constFundCategoryInfo.description}">
          <div class="dy-fund_item">
            <div class="dy-fund_title">
              <s:text name='homepage.fundmain.apply.type' />
            </div>
            <div class="dy-fund_inforcontent">
              ${constFundCategoryInfo.description}
            </div>
          </div>
        </c:if>
        <c:if test="${not empty constFundCategoryInfo.showIsMatch}">
          <div class="dy-fund_item">
            <div class="dy-fund_title">
              <s:text name='homepage.fundmain.apply.match' />
            </div>
            <div class="dy-fund_inforcontent">
              ${constFundCategoryInfo.showIsMatch}
            </div>
          </div>
        </c:if>
        <c:if test="${not empty constFundCategoryInfo.percentage}">
          <div class="dy-fund_item">
            <div class="dy-fund_title">
              <s:text name='homepage.fundmain.apply.percentage' />
            </div>
            <div class="dy-fund_inforcontent">
             ${constFundCategoryInfo.percentage}
            </div>
          </div>
        </c:if>
        <c:if test="${not empty constFundCategoryInfo.regionName}">
          <div class="dy-fund_item">
            <div class="dy-fund_title">
              <s:text name='homepage.fundmain.apply.area' />
            </div>
            <div class="dy-fund_inforcontent">
             ${constFundCategoryInfo.regionName}
            </div>
          </div>
        </c:if>
        <s:if test="constFundCategoryInfo.fundFileList.size() > 0">
          <div class="dy-fund_item">
            <div class="dy-fund_title">
              <s:text name='homepage.fundmain.apply.attachment' />
            </div>
            <div class="dy-fund_inforcontent dy-fund_item-content">
              <s:iterator value="constFundCategoryInfo.fundFileList" var="file">
                <span class="dy-fund_inforcontent-detaile" title="${fileName}" onclick="location.href='${filePath}'">
                    ${fileName}</span>
                <br>
              </s:iterator>
            </div>
          </div>
        </s:if>
      </div>
      <div class="dy-fund_item-func" style="display: flex; justify-content: center; align-items: center;">
        <div class="dy-fund_item-funcContainer">
          <div class="new-Standard_Function-bar">
            <a href="javascript:FundRecommend.awardOperation($(this), '${encryptedFundId}', 0, '${fundId }', 'detail');"
              class="margin-right:20px;font-size:14px" id="detailAward_${fundId }">
              <div class="new-Standard_Function-bar_item" style="margin-left: 0px;">
                <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"> <s:text name='homepage.fundmain.recommend.like' />
                  <span class="awardOperateCount">( 0 )</span>
                </span>
              </div>
            </a> <a style="display: none;"
              href="javascript:FundRecommend.awardOperation($(this), '${encryptedFundId}', 1, '${fundId }', 'detail');"
              class="margin-right:20px;font-size:14px" id="detailAwardCancel_${fundId }">
              <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected" style="margin-left: 0px;">
                <i class="new-Standard_function-icon new-Standard_Praise-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"> <s:text name='homepage.fundmain.recommend.unlike' />
                  <span class="awardOperateCount">( 0 )</span>
                </span>
              </div>
            </a> <a class="margin-right:20px;font-size:14px"
              onclick="SmateShare.shareRecommendFund($(this));initSharePlugin();" resid="${encryptedFundId}"
              fundId="${fundId }" logoUrl="" fundDesc="">
              <div class="new-Standard_Function-bar_item">
                <i class="new-Standard_function-icon new-Standard_Share-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"> <s:text name='homepage.fundmain.recommend.share' />
                  <span class="shareCount_${fundId }">( 0 )</span>
                </span>
              </div>
            </a> <a href="javascript:FundRecommend.collectCoperation($(this), '${encryptedFundId}', 0, '${fundId }');"
              class="margin-right:20px;font-size:14px" id="collect_${fundId }">
              <div class="new-Standard_Function-bar_item">
                <i class="new-Standard_function-icon new-Standard_Save-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"> <s:text name='homepage.fundmain.recommend.save' />
                </span>
              </div>
            </a> <a style="display: none;"
              href="javascript:FundRecommend.collectCoperation($(this), '${encryptedFundId}', 1, '${fundId }');"
              style="margin-right:20px;font-size:14px" id="collectCancel_${fundId }">
              <div class="new-Standard_Function-bar_item new-Standard_Function-bar_selected">
                <i class="new-Standard_function-icon new-Standard_Save-icon"></i> <span
                  class="new-Standard_Function-bar_item-title"> <s:text name='homepage.fundmain.recommend.unsave' />
                </span>
              </div>
            </a>
          </div>
          
        </div>
      </div>
     </div>
       <div class="dy-fund_showRight-absoBox" >
        <div style="width: 100%; height: 100%; position: relative;">
             <div class="dy-fund_showRight-Box">
                <div>
                  <div class="dy-fund_showRight-opHeader">
                     <span class="dy-fund_showRight-opHeader_detail"><s:text name='homepage.fundmain.recommend.fundChance'/></span>
                     <a href="/prjweb/fund/main?module=recommend" target="_blank"><span class="dy-fund_showRight-opHeader_icon"><s:text name='homepage.fundmain.recommend.more'/></span></a>
                  </div>
               <s:if test="fundInfoList != null && fundInfoList.size() > 0">
                   <s:iterator value="fundInfoList" id="fund" status="sta">
                  <div class="dy-fund_showRight-opBody">
                     <div class="dy-fund_showRight-opItem">
                         <div class="dy-fund_showRight-opItem_avator">
                          <a  href="/prjweb/outside/showfund?encryptedFundId=${fund.encryptedFundId }" target="_blank">
                             <img src="${fund.logoUrl}" onerror="this.src='${ressns }/images/default/default_fund_logo.jpg'"></a>
                         </div>
                         <div class="dy-fund_showRight-opItem_content">
                            <div class="dy-fund_showRight-opItem_title">
                            	<a href="/prjweb/funddetails/show?encryptedFundId=${fund.encryptedFundId }"title="${fund.fundTitle }" target="_blank">${fund.fundTitle }</a>
                            </div>
                            <div class="dy-fund_showRight-opItem_source">
                            	<a title="${fund.fundAgencyName }"href="/prjweb/fundAgency/detailMain?Des3FundAgencyId=<iris:des3 code='${fund.fundAgencyId }'/>"target="_black">${fund.fundAgencyName }</a>
                            </div>
                            <div class="dy-fund_showRight-opItem_sice" >
                    			<span style="cursor: default" title='${fund.scienceAreas }'>${fund.scienceAreas }</span>
                  			</div>
                         </div>
                     </div>
                  </div>
                  </s:iterator>
                  </s:if> 
                  <div class="dy-fund_showRight-opFooter">
                    <div class="dy-fund_showRight-opFooter_title">
                         <div class="dy-fund_showRight-opFooter_titleDetail"><s:text name='homepage.fundmain.recommend.download.app'/></div>
                         <div class="dy-fund_showRight-opFooter_titleDetail"><s:text name='homepage.fundmain.recommend.check'/></div>
                     </div>
                    <div class="dy-fund_showRight-opFooter_avator">
                        <img src="/resmod/smate-pc/img/app_download/run_app_download.png" >
                    </div>
                  </div>
             </div>   
        </div>
    </div>
    </div>
    </div>
    <div class="clear"></div>
    <div class="clear_h20"></div>
    <jsp:include page="/common/smate.share.jsp" />
</body>
</html>
