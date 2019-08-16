<li onmouseover="dynMsgUtil.showDynOperation(this)"  onmouseout="dynMsgUtil.hideDynOperation(this)">
    <div class="ui_avatar">
      <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${psnAvatar}" width="50" height="50" border="0" /></a>
    </div>
    <div class="publish-nr">
      <div class="moving-contat">
          <#list sharerDetails as sharer><a href="/scmwebsns/resume/psnView?des3PsnId=${sharer.sharer_des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${sharer.sharer_psnName}</a><#if sharer_has_next>、</#if></#list>分享：
      </div>
      <div class="reproduced">
        <div class="dyn_img"><a href="${linkUrl}" target="_blank"><img src="${icon}" width="32" height="32" title="${Title}"></a></div>
        <div class="dyn_introduction">
         <h3><a href="${linkUrl}" class="b" target="_blank">${Title}</a></h3>
         <p>${desc}</p>
        </div>
		<div style="clear:both"></div>
	  </div>
      <div class="appraisa-choose">
        <div class="t_detail">
                  <#assign dynDateVar>
          <#if dynDate=='dateTime'>${normalDynDate}<#else>${dynDate?replace("s","秒")?replace("m","分钟")?replace("h","小时")?replace("d","天")}以前</#if>
          </#assign>
        <span class="f888 date01"> ${dynDateVar}</span>&nbsp;&nbsp;&nbsp;&nbsp;
	        <div class="praise_box" style="margin-left:20px;">
	        
		        <#if (hasAward==0)>
		        <a onclick="dynMsgUtil.ajaxAward(this, '${resType}','${resNode}','${resId?c}','${permission}','${dynId?c}')" style="cursor:pointer" class="mright10 f888 award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i>赞</a>
		        <a onclick="dynMsgUtil.ajaxCancelAward(this, '${resType}','${resNode}','${resId?c}','${permission}')" style="cursor:pointer;display:none" class="mright10 f888 cancel_award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i>取消赞</a>
		        <#else>
		        <a onclick="dynMsgUtil.ajaxAward(this, '${resType}','${resNode}','${resId?c}','${permission}','${dynId?c}')" style="cursor:pointer;display:none" class="mright10 f888 award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i>赞</a>
		        <a onclick="dynMsgUtil.ajaxCancelAward(this, '${resType}','${resNode}','${resId?c}','${permission}')" style="cursor:pointer;" class="mright10 f888 cancel_award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i>取消赞</a>
		        </#if>
		        
		        <#if (isShowReply==1)>
		        <span class="fe6e6 mright10">|</span>
		        <a style="cursor:pointer" onclick="dynMsgUtil.showReplyBox('${resId?c}','${resType}','${resNode}','${dynId?c}',event, this)" class="mright10 f888"><i class="dt_icon review_icon"></i>评论<span<#if (replyNum==0)> style="display:none"</#if>>(<label id="reply_num_label_${dynId?c}_${resId?c}" class="reply_num_label_${resType}_${resId?c}">${replyNum}</label>)</span></a>
		        <#else>
		        <#if (replyNum>0)><span class="fe6e6 mright10">|</span></#if>
		        <span<#if (replyNum==0)> style="display:none"</#if> class="f666" >评论(<label id="reply_num_label_${dynId?c}_${resId?c}" class="reply_num_label_${resType}_${resId?c}">${replyNum}</label>)</span>
		        </#if>
		        
		        <span class="fe6e6 mright10">|</span>
		        <b class="public_pulldown">
		        <#if (resType==1||resType==2||resType==4)>
		        	<a style="cursor:pointer" class="f888 mright10 share_sites_show"><i class="dt_icon share_icon"></i>分享<span class="shareCountSpan_${resType}_${resId?c}"<#if (shareNum==0)> style="display:none"</#if>>(<label class="shareCountLabel_${resType}_${resId?c}">${shareNum}</label>)</span><i class="publication-up"></i><!--<i class="publication-down"></i>--></a>
		        	<a class="share_pull" style="display:none" resId="${resId?c}" resType="${resType}" resNode="${resNode}" dynId="${dynId?c}" ></a>
		        <#else>
		        	<a style="cursor:pointer" class="f888 mright10" onclick="dynMsgUtil.showSharePage(this,'${resType}','${resNode}','${resId?c}', '${dynId?c}')"><i class="dt_icon share_icon"></i>分享<span class="shareCountSpan_${resType}_${resId?c}"<#if (shareNum==0)> style="display:none"</#if>>(<label class="shareCountLabel_${resType}_${resId?c}">${shareNum}</label>)</span></a>
		        </#if>
		        </b>
	        
	        </div>
        </div>
        <div class="zan_list award_detail_${resType}_${resNode}_${resId?c}" <#if (awardNum<=0)>style="display:none"</#if> >
        <#if (hasAward==0)>
        <a title="赞" onclick="dynMsgUtil.ajaxAward(this, '${resType}','${resNode}','${resId?c}','${permission}')" style="cursor:pointer" class="zan_a award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i></a>  
        <a title="取消赞" onclick="dynMsgUtil.ajaxCancelAward(this, '${resType}','${resNode}','${resId?c}','${permission}')" style="cursor:pointer;display:none" class="zan_a cancel_award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i></a>  
        <#else>
        <a title="赞" onclick="dynMsgUtil.ajaxAward(this, '${resType}','${resNode}','${resId?c}','${permission}')" style="cursor:pointer;display:none" class="zan_a award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i></a>  
        <a title="取消赞" onclick="dynMsgUtil.ajaxCancelAward(this, '${resType}','${resNode}','${resId?c}','${permission}')" style="cursor:pointer;" class="zan_a cancel_award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i></a>  
        </#if>
        
        <span class="user-list Fleft award_psn_list_${resType}_${resNode}_${resId?c}"><#if (awardPsnContent!='')>${awardPsnContent}</#if></span>
        <span class="f666 Fleft mtop10 award_num_${resType}_${resNode}_${resId?c}" <#if (awardNum<=0)>style="display:none"</#if> >共 <a onclick="Award.showAwardDetail(this)" style="cursor:pointer" awardId="${awardId?c}" class="award_detail_thickbox_${resType}_${resNode}_${resId?c} Blue b"><span class="award_num">${awardNum}</span></a> 人表示赞！</span>
        </div>
      </div>
      
    
      <div class="f_comment2">
        <dl id="reply_dl_${dynId?c}_${resId?c}">
        	<#if resReplyDetails?exists>
	         <#list resReplyDetails as resReply>
	          <dd class="reply_item_${resReply.recordId?c}">
	            <div class="t_namecard"><a href="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReplyerId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${resReply.replyerAvatar}" width="32" height="32" border="0" /></a></div>
	            <div class="t_message2">
	              <p><a href="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReplyerId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${resReply.replyerName}</a><#if (resReply.des3ReceiverId?exists) && (resReply.des3ReceiverId!='')>&nbsp;回复&nbsp;<a href="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReceiverId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${resReply.receiverName}</a></#if>：${resReply.replyContent}</p>
	              <p class="en10">${resReply.replyDate}&nbsp;&nbsp;<span class="reply_operation" style="display:none;"><a style="cursor:pointer" receiverPsnName="${resReply.replyerName}" receiverPsnLink="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReplyerId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" onclick="dynMsgUtil.replyToPsn('${resId?c}','${resType}','${resNode}','${dynId?c}','${resReply.des3ReplyerId}',event, this)" class="Blue"  title="回复"><i class="evaluate-icon"></i></a>&nbsp;<#if (isResOwner==1 || resReply.replyerId==currentPsnId || isMine==1)><a onclick="dynMsgUtil.deleteReply('${dynId?c}','${resReply.recordId?c}','${resId?c}','${resType}','${resNode}',event, this)" style="cursor:pointer" class="Blue" title="删除"><i class="delete-icon"></i></a></#if></span></p>
	            </div>
	          </dd>
	          <#if (replyNum>2) && resReply_has_next>
	          <dd style="cursor:pointer" id="reply_more_dd_${dynId?c}_${resId?c}" onclick="dynMsgUtil.ajaxExtendReply(this,'${dynId?c}','${resReply.replyId?c}','${queryType}')" beginRecord="1"><a class="Blue">还有<label id="reply_num_${dynId?c}_${resReply.replyId?c}">${(replyNum?number)-2}</label>条评论</a></dd>
	          </#if>
	          </#list>
          </#if>
        </dl>
      </div>
      
      <#if (isShowReply==1)>
      <div class="comments_box">
        <div class="reply_enter_div" id="input_${dynId?c}_${resId?c}">
          <input type="text" class="inp_text f666" value="评论……"  style="width:590px;cursor:pointer" onclick="dynMsgUtil.showReplyBox('${resId?c}','${resType}','${resNode}','${dynId?c}',event, this)"/>
        </div>
        <div style="display:none;" class="reply_content_div" id="textarea_${dynId?c}_${resId?c}">
          <div class="t_namecard">
             <a href="/scmwebsns/resume/psnView?des3PsnId=${currentDes3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" id="link_current_${dynId?c}_${resId?c}">
             <img src="${currentPsnAvatar}" id="img_currentAvatar_${dynId?c}_${resId?c}" width="32" height="32" border="0"/></a>
             <input type="hidden" value="${currentDes3PsnId}" id="input_currentPsn_des3PsnId_${dynId?c}_${resId?c}"/>
             <input type="hidden" value="${currentPsnName}" id="input_currentPsn_${dynId?c}_${resId?c}"/>
          </div>
          <div class="t_message3">
              <div class="main_input" style="width:540px;margin-bottom:5px;">
              <div class="input-line-dyn" id="textarea_box_${dynId?c}_${resId?c}" contentEditable="true" onKeyDown="dynMsgUtil.replyWordCount(this,'${resId?c}','${dynId?c}',250)" onKeyUp="dynMsgUtil.replyWordCount(this,'${resId?c}','${dynId?c}',250)" onPaste="dynMsgUtil.replyWordCount(this,'${resId?c}','${dynId?c}',250)" onFocus="dynMsgUtil.replyWordCount(this,'${resId?c}','${dynId?c}',250)" onClick="dynMsgUtil.textCommentBoxClick(event)"></div>
              <p style="color: #999; text-align: right; padding-right: 5px;"><label id="count_label_${dynId?c}_${resId?c}">0</label>/250</p>
              </div>
  
              <p>
                <a style="cursor:pointer" onclick="dynMsgUtil.ajaxReply('${resType}','${resNode}','${resId?c}','${dynId?c}','${permission}', 0, this)" class="replyBtn uiButton uiButtonConfirm">发表</a>&nbsp;&nbsp;
              </p> 
           </div>
        </div>
      </div>
      </#if>
      
    </div>
    
    <div class="drop_down"><a style="cursor:pointer;display:none;" onmouseover="dynMsgUtil.extendDynOperation(this)" onmouseout="dynMsgUtil.shrinkDynOperation(this)" class="link_delete"></a>
        <div class="div_item_delete drop_down_zh_CN" style="display:none" onmouseout="$(this).hide()" onmouseover="$(this).show();$(this).parent().find('.link_delete').show();">
        <div class="drop_jt_zh_CN"></div>
           <ul>
             <#if (isMine==1)>
               <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>删除此动态</a></li>
               <!--<li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxShieldDynByType('09','${dynId?c}')"><i class="img_nochoose"></i>屏蔽此类动态</a></li>-->
             <#else>
               <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>删除此动态</a></li>
               <!--<li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxShieldDynByPsn('${dynId?c}')"><i class="img_nochoose"></i>屏蔽此人动态</a></li>-->
               <!--<li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxShieldDynByType('09','${dynId?c}')"><i class="img_nochoose"></i>屏蔽此类动态</a></li>-->
             </#if>
           </ul>
         </div>
    </div>
 </li>