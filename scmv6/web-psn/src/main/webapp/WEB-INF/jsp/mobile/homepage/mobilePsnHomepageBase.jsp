<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/WEB-INF/jsp/resume/psn_cnf_const.jsp"%>
<!doctype html>
<html>
<head>
<title>${showName }的个人主页</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<link href="${resmod}/mobile/css/style.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/weixin/jquery.qrcode.min.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.2.0.js"></script>
<script type="text/javascript" src="/resmod/mobile/js/wechat.custom.js"></script>
<script type="text/javascript" src="/resmod/mobile/css/mobile.css"></script>
<script type="text/javascript" src="${resmod }/js/common/smate.common.js"></script>
<!-- SCM-6550 微信-->
<script type="text/javascript">
	function getQrImg(url) {
		var length = $(".zy_top").height() * 0.77;
		var log_length = length * 0.25;
		var log_left = (length - log_length) * 0.5;
		$("#codeico").css("left", log_left);
		$("#codeico").css("top", log_left);
		$("#codeico").height(log_length);
		$("#codeico").width(log_length);
		if (navigator.userAgent.indexOf("MSIE") > 0) {
			$("#big_qr_code").qrcode({
				render : "table",
				width : 200,
				height : 200,
				text : url
			});
		} else {
			$("#big_qr_code").qrcode({
				render : "canvas",
				width : 200,
				height : 200,
				text : url
			});
		}
	}
	
	function moreScienAreas(){
		var post_data = {
		        'des3PsnId': $("#des3PsnId").val()
		    };
		$.ajax({
			url:"/psnweb/mobile/morePersonScienAreas",
			type:"post",
			dataType:"html",
			data:post_data,
			success:function(data){
				if(typeof data!='undefined'&&data){
					$(".s_field").empty();
					$(".s_field").append(data);
					
				}
				   
			}
			
		});
	}
	function toPsnList( scienceAreaId){
		window.location.href = "/psnweb/mobile/psnlistview?scienceAreaId="+scienceAreaId+"&serviceType=saIdentific&des3PsnId="+$("#des3PsnId").val()+"&fromPage="+"0";
	}
	
	var module ="${module}" ;
	var showDialogPub ="${showDialogPub}" ;
	var filterPubType ="${filterPubType}" ;
	$(document).ready(function(){
			if("${wechatBrowser}"){
				var shareImgUrl = "${person.avatars}";
				if(shareImgUrl == null || shareImgUrl == ""){
					shareImgUrl = "${domainMobile}/resmod/smate-pc/img/logo_psndefault.png";
				}
				smatewechat.customWeiXinShare(
						"${appId}",
						"${timestamp}", 
						"${nonceStr}",
						"${signature}",
						"${showName}的主页",
						"${domainMobile}/psnweb/mobile/outhome?des3ViewPsnId=${person.personDes3Id }",
						shareImgUrl,
						"这是${showName}的科研之友移动端主页");
			}
			getQrImg("${domainMobile}/psnweb/mobile/outhome?des3ViewPsnId=${person.personDes3Id }");
			if ($.trim($("#outHomePage").val()) != "true") {
				mobile_bottom_setTag("psn");
			};
			
			//二维码的点击
			$("#qr_code").click(function(){
				$("#bg").show();
				$("#bg_whilt").show();
				$("#big_qr_code").show();
			});
			$("#bg,#big_qr_code,#bg_whilt").click(function(){
				$("#big_qr_code").hide();
				$("#bg_whilt").hide();
				$("#bg").hide();
			});
			
			if($("#outHomePage").val()){
				//添加访问记录
			    SmateCommon.addVisitRecord($("#des3PsnId").val(),$("#des3PsnId").val(),6);
			}

			//跳转到对应的模块,成果列表
			if(module === "pub"){
				//默认调转，成果列表
				var href = $(".zy_wrap    .effort  .dev_pubweb ").find("a").attr("href");
				if(showDialogPub ==="showPubConfirmMore"){//成果认领
					var pubConfirmUrl="/pub/confirmpublist?fromPage=mobileConfirmPub&toBack=psnpub"
						window.location.href=pubConfirmUrl;
				}else if(filterPubType === "5"){//专利列表
					if( href.indexOf("?")> 0 ) {
						href=href+"&pubType="+filterPubType
					}else{
						href=href+"?pubType="+filterPubType
					}
					window.location.href=href;
				}else{
			    	window.location.href=href;
				}		
			}
		});
</script>
</head>
<body style="background: #e7ecf7;">
  <input id="outHomePage" type="hidden" value="${outHomePage}" />
  <div id="bg"
    style="display: none; position: fixed; z-index: 99999997; width: 100%; height: 100%; background: #2e2e2e; opacity: 0.6;"></div>
  <div id="bg_whilt"
    style="display: none; position: fixed; top: 10%; left: 0; right: 0; margin: auto; z-index: 99999998; width: 80%; height: 75%; background: #fff;">
    <div style="position: absolute; top: 5%; text-align: center; width: 100%;">
      <span id="persName"
        style="display: -moz-inline-box; display: inline-block; max-width: 120px; white-space: nowrap; word-break: keep-all; overflow: hidden; text-overflow: ellipsis; font-size: 14px; font-weight: bold;">
        ${showName } </span> <span
        style="display: -moz-inline-box; display: inline-block; white-space: nowrap; word-break: keep-all; overflow: hidden; text-overflow: ellipsis; font-size: 14px; font-weight: bold;">
        的二维码 </span>
    </div>
    <div style="position: absolute; top: 80%; text-align: center; width: 100%;">
      <span>扫一扫</span>
    </div>
  </div>
  <s:if test="psnId==0">
   	人员信息已变更
   </s:if>
  <s:else>
    <input type="hidden" id="des3PsnId" value="${person.personDes3Id }">
    <div class="top">
      <div class="top_mn" style="display: flex; justify-content: flex-start; align-items: center;">
        <a href="javascript:void();" onclick="window.history.back();" style="width: 10vw;"> <i
          class="material-icons paper__func-header__tip" style="margin-left: -15px">keyboard_arrow_left</i></a>
        <!--   <i class="material-icons Sig__out-tip" style="color: #fff; position: relative;">
	            more_vert
		            <div class="sig__out-box">
			            <em class="sig__out-header"></em>
			            <a class="sig__out-body" href="/oauth/mobile/unbind">退出</a>
		            </div>
	            </i> -->
        <div class="psn-mobile_page-title">个人主页</div>
      </div>
    </div>
    <div class="b_set"></div>
    <div class="zy_top">
      <div class="tx">
        <img alt="" src='${person.avatars}' onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'" />
        <!-- <a href="#"></a> -->
      </div>
      <s:if test="outHomePage!='true'">
        <div></div>
        <div id="big_qr_code"
          style="display: none; position: fixed; top: 20%; left: 0; right: 0; margin: auto; z-index: 99999999; width: 200px; height: 200px;">
          <div
            style=" position:absolute; left:45%;top:45%; z-index:9999998;width:10%; height:10%;background:url(${resmod}/images/wechat/iconsmate.jpg)  no-repeat;background-size:cover; ">
          </div>
        </div>
        <div class="qr_code" id="qr_code">
          <img src="${resmod}/mobile/images/code.png">
        </div>
      </s:if>
      <h1>${showName }</h1>
      <p>${person.insName}</p>
      <p>${person.position}</p>
    </div>
    <div class="zy_wrap">
      <c:if test="${psnCnfBuild!=null}">
        <s:set var="CNF_ANYMOD" value="psnCnfBuild.cnfMoudle.anyMod" />
        <jsp:include page="mobilePsnHomepageInfoStatistics.jsp"></jsp:include>
        <s:if test="(#CNF_ANYMOD&#CNF_EXPERTISE)==#CNF_EXPERTISE">
          <c:if test="${ ! empty psnScienceAreaFormList}">
            <h2>科技领域</h2>
            <jsp:include page="mobilePsnHomepageScienceArea.jsp"></jsp:include>
          </c:if>
        </s:if>
        <s:else>
          <c:if test="${isMyself}">
            <h2>科技领域</h2>
            <jsp:include page="mobilePsnHomepageScienceArea.jsp"></jsp:include>
          </c:if>
        </s:else>
        <s:if
          test="(#CNF_ANYMOD&#CNF_EDU)==#CNF_EDU || (#CNF_ANYMOD&#CNF_BRIEF)==#CNF_BRIEF || (#CNF_ANYMOD&#CNF_WORK)==#CNF_WORK || isMyself">
          <c:if test="${fn:length(person.brief) !=0   ||  fn:length(psnWorkList) != 0 || fn:length(psnEduList)>0}">
            <h2>基本资料</h2>
            <ul class="base_infro">
              <s:if test="(#CNF_ANYMOD&#CNF_BRIEF)==#CNF_BRIEF">
                <jsp:include page="mobilePsnHomepageBrief.jsp"></jsp:include>
              </s:if>
              <s:else>
                <c:if test="${isMyself}">
                  <jsp:include page="mobilePsnHomepageBrief.jsp"></jsp:include>
                </c:if>
              </s:else>
              <s:if test="(#CNF_ANYMOD&#CNF_WORK)==#CNF_WORK">
                <jsp:include page="mobilePsnHomepageWorkHis.jsp"></jsp:include>
              </s:if>
              <s:else>
                <c:if test="${isMyself}">
                  <jsp:include page="mobilePsnHomepageWorkHis.jsp"></jsp:include>
                </c:if>
              </s:else>
              <s:if test="(#CNF_ANYMOD&#CNF_EDU)==#CNF_EDU">
                <jsp:include page="mobilePsnHomepageEduHis.jsp"></jsp:include>
              </s:if>
              <s:else>
                <c:if test="${isMyself}">
                  <jsp:include page="mobilePsnHomepageEduHis.jsp"></jsp:include>
                </c:if>
              </s:else>
            </ul>
          </c:if>
        </s:if>
        <%-- <s:if test="(#CNF_ANYMOD&#CNF_CONTACT)==#CNF_CONTACT">
			<c:if test="${not empty  person.tel || not empty  person.email}">
			<h2>联系信息</h2>
			<ul class="ctact">
				<c:if test="${not empty  person.email }">
				<li><h3>邮件：</h3>
					<p>${person.email}</p></li>
				</c:if>
				<c:if test="${not empty  person.tel }">
					<li><h3>电话：</h3>
						<p>${person.tel}</p></li>
				</c:if>
			</ul>
			</c:if>
			</s:if>
			<s:else>
			     <c:if test="${isMyself}">
			        	<c:if test="${not empty  person.tel || not empty  person.email}">
			 <h2>联系信息</h2>
			<ul class="ctact">
				<c:if test="${not empty  person.email }">
				<li><h3>邮件：</h3>
					<p>${person.email}</p></li>
				</c:if>
				<c:if test="${not empty  person.tel }">
					<li><h3>电话：</h3>
						<p>${person.tel}</p></li>
				</c:if>
			</ul>
			</c:if>
			    </c:if>
			</s:else> --%>
      </c:if>
    </div>
    <s:if test="outHomePage!='true'">
      <jsp:include page="../bottom/mobile_bottom.jsp"></jsp:include>
    </s:if>
    <s:elseif test="hasLogin == 0">
      <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_outside_footer.jsp"></jsp:include>
    </s:elseif>
  </s:else>
</body>
</html>