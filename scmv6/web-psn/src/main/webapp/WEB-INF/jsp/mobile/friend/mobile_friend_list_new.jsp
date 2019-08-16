<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!doctype html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport"
  content="initial-scale=1.0,maximum-scale=1.0,minimum-scale=1.0,user-scalable=0,width=device-width,maximum-scale=1, user-scalable=no">
<meta name="format-detection" content="telephone=no" />
<meta name="format-detection" content="email=no" />
<title>科研之友</title>
<link href="${resmod}/mobile/css/style.css" rel="stylesheet" type="text/css">
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/wechat.custom.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<script>
	$(window).ready(
			
			function(e) {
				if ("${wxOpenId}") {
					smatewechat.initWeiXinShare("${appId}", "${timestamp}",
							"${nonceStr}", "${signature}");
				}
				if($("#isOtherFrdList").val() != "true"){
					mobile_bottom_setTag("link");
				}
				$('.btn_nav').bind('click', function() {
					var obj = $('#type');
					if (obj.css('display') == 'none') {
						obj.slideDown()
					} else {
						obj.slideUp();
					}
				});
				
				moveToLetter();
			});
	function viewPsnInfo(des3PsnId){
		window.location.href = "/psnweb/mobile/outhome?des3ViewPsnId="+des3PsnId;
	}
	
	function toRelationMain(){
		window.location.href="/psnweb/mobile/relationmain";
	}
	
	function moveToLetter(){
		$(".letter_list div").bind("click", function(e){
			var letter = $(this).find("a").html();
			if($('#'+letter).length>0){
                var LetterTop = $('#'+letter).position().top;
                $("html,body").scrollTop(LetterTop);
            }
		});
	}
	$(document).ready(function(){
		var charNum = $(".list_index_character").length;
        //设置字母高度
		var xheight = $(window).height();
		var h = xheight-112;
		var minH = h/26;
		var lineH = minH;
		if(charNum < 26){
			lineH = minH + minH*(26-charNum)/(charNum*2);
		}
		if(lineH > 34){
			lineH =34;
		}
		$(".list_index_character").css("line-height", lineH+"px");//通过设置CSS属性来设置元素的高
	});
</script>
</head>
<body>
  <input type="hidden" id="isOtherFrdList" value="${other }" />
  <div class="header">
    <div class="header_toolbar" style="z-index: 99; position: relative;">
      <div class="header_toolbar_tools" onclick="window.history.back();"
        style="z-index: 99; position: absolute; height: 100% !important;">
        <div class="header_toolbar_icon" style="height: 100%">
          <i class="material-icons">keyboard_arrow_left</i>
        </div>
      </div>
      <c:if test="${other eq 'true' && !empty psnName}">
        <div class="header_toolbar_title"
          style="font-size: 18px; display: flex; justify-content: center; align-items: center;">
          <span class="text_ellipsis" style="padding-top: 2px;">${psnName }的联系人</span>
        </div>
      </c:if>
      <c:if test="${other ne 'true' || empty psnName}">
        <div class="header_toolbar_title"
          style="font-size: 18px; display: flex; justify-content: center; align-items: center;">
          <span class="text_ellipsis" style="padding-top: 2px;">我的联系人</span>
        </div>
      </c:if>
    </div>
  </div>
  <div style="height: 45px;"></div>
  <div class="body_content">
    <div class="body_content_container">
      <c:if test="${empty psnMap && permission =='0'}">
        <div class="response_no-result">未查询到相关记录</div>
      </c:if>
      <c:if test="${empty psnMap && permission =='1'}">
        <div class="response_no-result">由于权限设置，可能部分数据未显示</div>
      </c:if>
      <c:forEach var="letter" items="${nameLetters}" varStatus="status">
        <c:forEach var="psnInfoList" items="${psnMap}" varStatus="psnMap">
          <c:if test="${psnInfoList.key == letter && fn:length(psnInfoList.value) > 0}">
            <div class="title friend_list_index" name="${letter}" id="${letter}" style="background: #eee!important;">${letter}</div>
            <div class="list_container">
              <c:forEach var="psnInfo" items="${psnInfoList.value}" varStatus="psnInfoList">
                <div class="list_item_container">
                  <div class="list_item_section"></div>
                  <div class="list_item_section" onclick="viewPsnInfo('${psnInfo.des3PsnId}');">
                    <div class="person_namecard_whole hasBg">
                      <div class="avatar">
                        <img class="avatar" src="${psnInfo.avatarUrl }"
                          onerror="this.src='/resmod/smate-pc/img/logo_psndefault.png'">
                      </div>
                      <div class="person_information">
                        <div class="name">${psnInfo.name }</div>
                        <div class="institution">${psnInfo.insInfo }</div>
                      </div>
                    </div>
                  </div>
                  <div class="list_item_section"></div>
                </div>
              </c:forEach>
            </div>
          </c:if>
        </c:forEach>
      </c:forEach>
      <c:forEach var="psnInfoList" items="${psnMap}">
        <c:if test="${psnInfoList.key == 'others' && fn:length(psnInfoList.value) > 0}">
          <div style="position: absolute; top: -56px;"></div>
          <div class="title friend_list_index" name="others" id="others" style="background: #eee!important;">其他</div>
          <div class="list_container">
            <c:forEach var="psnInfo" items="${psnInfoList.value}">
              <div class="list_item_container">
                <div class="list_item_section"></div>
                <div class="list_item_section" onclick="viewPsnInfo('${psnInfo.des3PsnId}');">
                  <div class="person_namecard_whole hasBg">
                    <div class="avatar">
                      <img class="avatar" src="${psnInfo.avatarUrl }">
                    </div>
                    <div class="person_information">
                      <div class="name">${psnInfo.name }</div>
                      <div class="institution">${psnInfo.insInfo }</div>
                    </div>
                  </div>
                </div>
                <div class="list_item_section"></div>
              </div>
            </c:forEach>
          </div>
        </c:if>
      </c:forEach>
    </div>
  </div>
  <div class="list_index letter_list">
    <c:forEach var="psnInfoList" items="${psnMap}">
      <c:if test="${psnInfoList.key != 'others' && fn:length(psnInfoList.value) > 0}">
        <div class="list_index_character">
          <a href="javascript:void(0);">${psnInfoList.key}</a>
        </div>
      </c:if>
    </c:forEach>
  </div>
  <s:if test="other!='true'">
    <div style="height: 56px;"></div>
    <jsp:include page="../bottom/mobile_bottom.jsp"></jsp:include>
  </s:if>
  <s:elseif test="hasLogin == 0">
    <jsp:include page="/WEB-INF/jsp/mobile/bottom/mobile_outside_footer.jsp"></jsp:include>
  </s:elseif>
</body>
</html>
