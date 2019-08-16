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
<script type="text/javascript" src="${resmod}/js/jquery.js"></script>
<script type="text/javascript" src="${resmod}/js/plugin/jquery-migrate-1.2.1.min.js"></script>
<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script type="text/javascript" src="${resmod}/mobile/js/wechat.custom.js"></script>
<link rel="stylesheet" type="text/css" href="${resmod}/mobile/css/scmmobileframe.css" />
<link href="${resmod}/mobile/css/mobile.css" rel="stylesheet" type="text/css">
<script>
	$(window).ready(
			function(e) {
				if ("${wxOpenId}") {
					smatewechat.initWeiXinShare("${appId}", "${timestamp}",
							"${nonceStr}", "${signature}");
				}
				if($("#isOtherFrdList").val() != "true"){
					//消息中心选择联系人
					mobile_bottom_setTag("msg");
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
	//选中菜单栏高亮显示
	function mobile_bottom_setTag(moduleName) {
		$(".mobile_menu").find(".fc_blue500").removeClass("fc_blue500");
		switch(moduleName) {
//		case 'find':$(".menu_find").addClass("active");break;
		case 'link':
			$(".new-mobilepage_footer-item_tip-contect").addClass("new-mobilepage_footer-item_tip-contect_selected");
			$(".new-mobilepage_footer-item_tip-contect").removeClass("new-mobilepage_footer-item_tip-contect");
			$("#mobile_menu_contect").addClass("fc_blue500");
			$("#menu_contect_psn").attr("style", "color:inherit !important;");
			break;
		case 'msg':
			$(".new-mobilepage_footer-item_tip-message").addClass("new-mobilepage_footer-item_tip-message_selected");
	        $(".new-mobilepage_footer-item_tip-message").removeClass("new-mobilepage_footer-item_tip-message");
	        $("#mobile_menu_msg").addClass("fc_blue500");
			break;
		case 'psn':
			$(".new-mobilepage_footer-item_tip-mine").addClass("new-mobilepage_footer-item_tip-mine_selected");
	        $(".new-mobilepage_footer-item_tip-mine").removeClass("new-mobilepage_footer-item_tip-mine");
	        $("#mobile_menu_more").addClass("fc_blue500");
	        $("#menu_more_span").attr("style", "color:inherit !important;");
			break;
		default:
			$(".new-mobilepage_footer-item_tip-first").addClass("new-mobilepage_footer-item_tip-first_selected");
	        $(".new-mobilepage_footer-item_tip-first").removeClass("new-mobilepage_footer-item_tip-first");
	        $("#mobile_menu_dyn").addClass("fc_blue500");
		}
	};
	function viewPsnInfo(des3PsnId){
		window.location.href = "/psnweb/mobile/outhome?des3ViewPsnId="+des3PsnId;
	}
	function selectFriend(des3PsnId){
		window.location.href = "/dynweb/mobile/ajaxshownewcreatemsgui?des3ChatPsnId="+des3PsnId;
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
    <div class="header_toolbar">
      <div class="header_toolbar_title" style="font-size: 18px; width: 100%;">
        <span class="text_ellipsis" style="width: 10%;"></span> <span class="text_ellipsis"
          style="width: 80%; text-align: center;">我的联系人</span> <span class="text_ellipsis"
          style="width: 10%; font-size: 16px;" onclick="window.history.back();">取消</span>
      </div>
    </div>
  </div>
  <div style="height: 56px;"></div>
  <div class="body_content">
    <div class="body_content_container">
      <c:if test="${empty psnMap && permission =='0'}">
        <div class="response_no-result">未查询到相关记录</div>
      </c:if>
      <c:if test="${empty psnMap && permission =='1'}">
        <div class="list_item_container">
          <div class="list_item_section list_item_section-tip" style="margin-top: 15px;">由于权限设置，可能部分数据未显示</div>
          <div class="list_item_section"></div>
          <div class="list_item_section"></div>
        </div>
      </c:if>
      <%--  <c:choose>
           <c:when test="${empty psnMap && permission =='0'}">
           <div class="list_item_container">
                <div class="list_item_section list_item_section-tip">未查询到相关记录</div>
                <div class="list_item_section"></div>
                <div class="list_item_section"></div>
            </div>
           </c:when>
           <c:otherwise>
                 <div class="list_item_section list_item_section-tip">无权限访问</div>
                <div class="list_item_section"></div>
                <div class="list_item_section"></div>
           </c:otherwise>
        </c:choose> --%>
      <c:forEach var="letter" items="${nameLetters}" varStatus="status">
        <c:forEach var="psnInfoList" items="${psnMap}" varStatus="psnMap">
          <c:if test="${psnInfoList.key == letter && fn:length(psnInfoList.value) > 0}">
            <div class="title friend_list_index" name="${letter}" id="${letter}">${letter}</div>
            <div class="list_container">
              <c:forEach var="psnInfo" items="${psnInfoList.value}" varStatus="psnInfoList">
                <div class="list_item_container">
                  <div class="list_item_section"></div>
                  <div class="list_item_section" onclick="selectFriend('${psnInfo.des3PsnId}');">
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
          <div class="title friend_list_index" name="others" id="others">其他</div>
          <div class="list_container">
            <c:forEach var="psnInfo" items="${psnInfoList.value}">
              <div class="list_item_container">
                <div class="list_item_section"></div>
                <div class="list_item_section" onclick="selectFriend('${psnInfo.des3PsnId}');">
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
  <%--  <div style="height: 56px;"></div>
<c:if test="${other!='true' }">
<jsp:include page="../bottom/mobile_bottom.jsp"></jsp:include>
</c:if> --%>
</body>
</html>
