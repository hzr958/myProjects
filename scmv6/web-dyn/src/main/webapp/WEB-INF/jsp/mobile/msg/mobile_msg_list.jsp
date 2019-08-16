<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
$(function(){
	if ($(".share_msg_div").length == 0) {
		$("#no_msg_style").val(Number($("#no_msg_style").val())+0);
	}
	else{
		$("#no_msg_style").val(Number($("#no_msg_style").val())+1);
	}
	if ($("#no_msg_style").val() == 0) {
		$(".no_msg").show();
	}
// 	Msg.scrollToDelete();//左滑出删除选项
	slideDom("share_msg_div", 35, true, delShareMsgCallBack, toOriginCallBack, clickItemCallBack);
	//防止多次绑定touch事件
	$(".share_msg_div").removeClass("share_msg_div");
	showDate();
});
//分享消息为当天时,显示时分(24小时制),其余显示月日
function showDate() {
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth() + 1;
	var day = date.getDate();
	$(".y_m_d").each(function(){
		if ($.trim($(this).text()) == year+"-"+month+"-"+day) {
			$(this).prev().show();
			$(this).next().hide();
		}
	});
};

//左滑移除消息(置为已读)
function delShareMsgCallBack(obj,event) {
	var msgId = obj.attr("id").substr(8);
	var option = {
		fun: function() {
			$.ajax( {
				url : "/dynweb/mobile/ajaxsetread",
				type : "post",
				dataType : "json",
				data : {
					"msgRelationIds" : msgId,
					"des3PsnId" : $("#currentDes3PsnId").val()
				},
				success : function(data) {
				   	if (data.result == "success") {
				   		scmpublictoast("操作成功",1000,1);
				   	}
				}
			});
		}	
	};
	$("#dev_msg_"+msgId).moveItem(option);
	event.stopPropagation();
};

function toOriginCallBack(obj, event){
	$(obj).css('transform', 'translateX(0px)');
	$(obj).css('transition', '0.3s');
	setTimeout(function(){
		$(obj).css('transition', '0s')
	},10);//回复原位
}

function clickItemCallBack(obj, event){
	$(obj).css('transform', 'translateX(0px)');
	$(obj).css('transition', '0.3s');
	setTimeout(function(){
		$(obj).css('transition', '0s')
	},10); //回复原位
};
</script>
<s:if test="msgShowInfoList.size()>0">
  <s:iterator value="msgShowInfoList" var="msg">
    <div class="list_item_container share_msg_div" id="dev_msg_${msgRelationId}">
      <div class="list_item_section"></div>
      <div class="list_item_section">
        <div class="notification_whole">
          <div class="notification_content">
            <!-- 站内信类型 text-->
            <s:if test="#msg.smateInsideLetterType=='text'">
              <div class="person">
                <div class="person_container">
                  <s:if test="#msg.senderZhName == null || #msg.senderZhName == ''">
                    <c:out value="${senderEnName}" escapeXml="false" />
                  </s:if>
                  <s:else>
                    <c:out value="${senderZhName}" escapeXml="false" />
                  </s:else>
                </div>
                <div>向你发了消息</div>
              </div>
              <div style="font-size: 14px !important;" class="file_name-content">
                <c:out value="${content}" escapeXml="false" />
              </div>
            </s:if>
            <!-- 站内信类型 pub -->
            <s:if test="#msg.smateInsideLetterType=='pub'">
              <div class="person">
                <div class="person_container">
                  <s:if test="#msg.senderZhName == null || #msg.senderZhName == ''">
                    <c:out value="${senderEnName}" escapeXml="false" />
                  </s:if>
                  <s:else>
                    <c:out value="${senderZhName}" escapeXml="false" />
                  </s:else>
                </div>
                <div>向你分享了成果</div>
              </div>
              <div class="file_name-content">
                <span style="font-size: 14px !important;" onclick="Msg.opendetail('${des3PubId}');"> <s:if
                    test="#msg.pubTitleZh == null || #msg.pubTitleZh == ''">
                    <c:out value="${pubTitleEn}" escapeXml="false" />
                  </s:if> <s:else>
                    <c:out value="${pubTitleZh}" escapeXml="false" />
                  </s:else>
                </span>
              </div>
              <div class="message">
                <c:out value="${content}" escapeXml="false" />
              </div>
            </s:if>
            <!-- 站内信类型 file -->
            <s:if test="#msg.smateInsideLetterType=='file'">
              <div class="person">
                <div class="person_container">
                  <s:if test="#msg.senderZhName == null || #msg.senderZhName == ''">
                    <c:out value="${senderEnName}" escapeXml="false" />
                  </s:if>
                  <s:else>
                    <c:out value="${senderZhName}" escapeXml="false" />
                  </s:else>
                </div>
                <div>向你分享了文件</div>
              </div>
              <div class="file_name-content">
                <span style="font-size: 14px !important;" onclick="location.href='${fileDownloadUrl}';"><c:out
                    value="${fileName}" escapeXml="false" /></span>
              </div>
              <div class="message">
                <c:out value="${content}" escapeXml="false" />
              </div>
            </s:if>
            <!-- 基金 -->
            <s:if test="#msg.smateInsideLetterType=='fund'">
              <div class="person">
                <div class="person_container">
                  <s:if test="#msg.senderZhName == null || #msg.senderZhName == ''">
                    <c:out value="${senderEnName}" escapeXml="false" />
                  </s:if>
                  <s:else>
                    <c:out value="${senderZhName}" escapeXml="false" />
                  </s:else>
                </div>
                <div>向你分享了基金</div>
              </div>
              <div class="file_name-content">
                <span style="font-size: 14px !important;"
                  onclick="Msg.openFundDetail('<iris:des3 code="${fundId }"/>');"> <s:if
                    test="#msg.fundZhTitle == null || #msg.fundZhTitle == ''">
                    <c:out value="${fundEnTitle}" escapeXml="false" />
                  </s:if> <s:else>
                    <c:out value="${fundZhTitle}" escapeXml="false" />
                  </s:else>
                </span>
              </div>
              <div class="message">
                <c:out value="${!empty zhFundDesc ? zhFundDesc : enFundDesc}" escapeXml="false" />
              </div>
            </s:if>
          </div>
          <div class="notification_operations">
            <div style="display: none;" class="time h_m">
              <s:date name="#msg.createDate" format="HH:mm" />
            </div>
            <div style="display: none;" class="time y_m_d">
              <s:date name="#msg.createDate" format="yyyy-MM-dd" />
            </div>
            <div class="time m_d">
              <s:date name="#msg.createDate" format="MM-dd" />
            </div>
          </div>
        </div>
      </div>
      <div class="list_item_section">
        <div class="list_item_right_commands ignore">
          <i class="material-icons icon"></i>
          <div>忽 略</div>
        </div>
      </div>
    </div>
  </s:iterator>
</s:if>
<s:else>
  <input id="msg_next_page" type="hidden" value="false" />
</s:else>