<#if resReplyDetails?exists>
<#list resReplyDetails as resReply>
  <dd class="reply_item_${resReply.recordId?c}">
    <div class="t_namecard"><a href="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReplyerId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${resReply.replyerAvatar}" width="32" height="32" border="0" /></a></div>
    <div class="t_message2">
      <p><a href="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReplyerId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${resReply.replyerName}</a><#if (resReply.des3ReceiverId?exists) && (resReply.des3ReceiverId!='')>&nbsp;回复&nbsp;<a href="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReceiverId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${resReply.receiverName}</a></#if>：${resReply.replyContent}</p>
      <p class="en10">${resReply.replyDate}&nbsp;&nbsp;<span class="reply_operation" style="display:none;"><a style="cursor:pointer" receiverPsnName="${resReply.replyerName}" receiverPsnLink="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReplyerId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" onclick="dynMsgUtil.replyToPsn('${resId?c}','${resType}','${resNode}','${dynId?c}','${resReply.des3ReplyerId}',event, this)" class="Blue" title="回复"><i class="evaluate-icon"></i></a>&nbsp;<#if (isResOwner==1 || resReply.replyerId==currentPsnId || isShowDelReplyBtn == 1)><a onclick="dynMsgUtil.deleteReply('${dynId?c}','${resReply.recordId?c}','${resId?c}','${resType}','${resNode}',event, this)" style="cursor:pointer" class="Blue" title="删除"><i class="delete-icon"></i></a></#if></span></p>
    </div>
  </dd>
</#list>
</#if>