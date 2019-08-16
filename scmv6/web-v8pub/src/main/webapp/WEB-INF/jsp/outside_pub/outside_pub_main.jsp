<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script src="${resmod}/smate-pc/js/scmpc_form.js" type="text/javascript"></script>
<script src="${resmod}/js_v5/scm.maint.js" type="text/javascript"></script>
<script type="text/javascript" src="${resmod}/js/baseutils/baseutils.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/smate-pc/new-confirmbox/confirm.css">
<link href="${resmod}/css_v5/css2016/newfirstpage.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${resmod}/smate-pc/new-confirmbox/confirm.js"></script>
<script src="${resmod}/smate-pc/new-importprj/newimportprj.js" type="text/javascript" charset="utf-8"></script>
<script src="${resmod}/smate-pc/new-importAchieve/new-import_Achieve.js" type="text/javascript"></script>
<script type="text/javascript">


var ctxpath = "${snsctx}";
var locale = "${locale}"; 
var ressns = "${ressns}";
var snsctx = "${snsctx}";
var domainrol = "${domainrol}";
var pageContext_request_serverName = '${pubListVO.snsDomain}';
var pubIdArray = new Array();
if(pageContext_request_serverName.indexOf("https://") != -1){
	pageContext_request_serverName = "http://"+pageContext_request_serverName.substring(8);
}
var shareI18 = '<spring:message code="publication.recommend.btn.share"/>';
$(function(){
	//成果列表
    Pub.pubList();
    //成果合作者
    Pub.cooperator();
    addFormElementsEvents(); //横线的样式添加
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
    };
});
//初始化 分享 插件
function initSharePlugin(obj){
	if(SmateShare.timeOut && SmateShare.timeOut == true)
		return;
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
</script>
<div class="container__horiz">
  <div style="display: flex; width: 100%;">
    <input id="dev_pubconfirm_isall" type="hidden" value="one" /> <input id="dev_pubftconfirm_isall" type="hidden"
      value="one" /> <input id="dev_pubcooperator_isall" type="hidden" value="one" /> <input id="whiteListKey"
      type="hidden" value="${psnId}" /> <input id="pub_type" type="hidden" value="5" />
    <div style="width: 280px; height: auto; margin-right: 16px;">
      <!-- 检索条件  begin -->
      <jsp:include page="pub_search_conditions.jsp" />
      <!-- 检索条件  end -->
      <!-- 成果合作者  begin -->
      <jsp:include page="recommend_partners.jsp" />
      <!-- 成果合作者  end -->
    </div>
    <div style="width: 100%; max-width: 960px;">
      <div class="container__card">
        <div class="main-list">
          <!-- 成果统计模块  begin -->
          <jsp:include page="pub_statistics.jsp" />
          <!-- 成果统计模块  end -->
          <!-- 成果列表  begin -->
          <div class="main-list__list dev_pub_list" list-main="psnpub"></div>
          <div class="main-list__footer">
            <div class="pagination__box" list-pagination="psnpub">
              <!-- 翻页 -->
            </div>
          </div>
          <!-- 成果列表 end -->
        </div>
      </div>
    </div>
  </div>
</div>
