<li onmouseover="dynMsgUtil.showDynOperation(this)"  onmouseout="dynMsgUtil.hideDynOperation(this)">
    <div class="ui_avatar">
      <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${psnAvatar}" width="50" height="50" border="0" /></a>
    </div>
    <div class="publish-nr">
      <div class="moving-contat">
      <#if (forwardPsnDetails?is_sequence)>
       <#list forwardPsnDetails as forwardPsn>
        <a href="/scmwebsns/resume/psnView?des3PsnId=${forwardPsn.forwarder_des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${forwardPsn.forwarder_psnEnName}</a> shared a status: ${forwardPsn.sayContent}<#if forwardPsn_has_next>&nbsp;||&nbsp;</#if>
      </#list>
      <#else>
      <#list forwardPsnDetails?eval as forwardPsn>
        <a href="/scmwebsns/resume/psnView?des3PsnId=${forwardPsn.forwarder_des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${forwardPsn.forwarder_psnEnName}</a> shared a status: ${forwardPsn.sayContent}<#if forwardPsn_has_next>&nbsp;||&nbsp;</#if>
      </#list>
      </#if>
      </div>
      <div class="reproduced" id="theme_iris_${dynId?c}"  style="word-wrap:break-word;"> 
      ${forwardEnTheme}
      </div>
      
     <div class="appraisa-choose">
        <div class="t_detail">
             <#assign dynDateVar>
             <#if dynDate=='dateTime'>${normalEnDynDate}<#else>${dynDate} ago</#if>
             </#assign>
        <span class="f888 date01"> ${dynDateVar}&nbsp;&nbsp;&nbsp;&nbsp;</span>
        
	        <div class="praise_box" style="margin-left:20px;">
		        
		        <#if (hasAward==0)>
		        <a onclick="dynMsgUtil.ajaxAward(this, '${resType}','${resNode}','${resId?c}','${permission}','${dynId?c}')" style="cursor:pointer" class="mright10 f888 award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i>Like</a>
		        <a onclick="dynMsgUtil.ajaxCancelAward(this, '${resType}','${resNode}','${resId?c}','${permission}')" style="cursor:pointer;display:none" class="mright10 f888 cancel_award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i>Unlike</a>
		        <#else>
		        <a onclick="dynMsgUtil.ajaxAward(this, '${resType}','${resNode}','${resId?c}','${permission}','${dynId?c}')" style="cursor:pointer;display:none" class="mright10 f888 award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i>Like</a>
		        <a onclick="dynMsgUtil.ajaxCancelAward(this, '${resType}','${resNode}','${resId?c}','${permission}')" style="cursor:pointer;" class="mright10 f888 cancel_award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i>Unlike</a>
		        </#if>
		        
		        <#if (isShowReply==1)>
		        <span class="fe6e6 mright10">|</span>
		        <a style="cursor:pointer" onclick="dynMsgUtil.showReplyBox('${resId?c}','${resType}','${resNode}','${dynId?c}',event, this)" class="mright10 f888"><i class="dt_icon review_icon"></i>Comment<span<#if (replyNum==0)> style="display:none"</#if>>(<label id="reply_num_label_${dynId?c}_${resId?c}" class="reply_num_label_${resType}_${resId?c}">${replyNum}</label>)</span></a>
		        <#else>
		        <#if (replyNum>0)><span class="fe6e6 mright10">|</span></#if>
		        <span<#if (replyNum==0)> style="display:none"</#if> class="f666" >Comment(<label id="reply_num_label_${dynId?c}_${resId?c}" class="reply_num_label_${resType}_${resId?c}">${replyNum}</label>)</span>
		        </#if>
		        
		        <span class="fe6e6 mright10">|</span>
		        <b class="public_pulldown">
		        <#if (resType==1||resType==2||resType==4)>
		        	<a style="cursor:pointer" class="f888 mright10 share_sites_show"><i class="dt_icon share_icon"></i>Share<span class="shareCountSpan_${resType}_${resId?c}"<#if (shareNum==0)> style="display:none"</#if>>(<label class="shareCountLabel_${resType}_${resId?c}">${shareNum}</label>)</span><i class="publication-up"></i><!--<i class="publication-down"></i>--></a>
		        	<a class="share_pull" style="display:none" resId="${resId?c}" resType="${resType}" resNode="${resNode}" dynId="${dynId?c}" ></a>
		        <#else>
		        	<a style="cursor:pointer" class="f888 mright10" onclick="dynMsgUtil.showSharePage(this,'${resType}','${resNode}','${resId?c}', '${dynId?c}')"><i class="dt_icon share_icon"></i>Share<span class="shareCountSpan_${resType}_${resId?c}"<#if (shareNum==0)> style="display:none"</#if>>(<label class="shareCountLabel_${resType}_${resId?c}">${shareNum}</label>)</span></a>
		        </#if>
		        </b>
		        
		        <#if (isObjectOwner!=1)&&(resType==1||resType==2)>
		      	<span class="fe6e6 mright10">|</span>
		      	<b class="public_pulldown"><a onmouseover="dynMsgUtil.showCollectDiv(this)" class="f888 mright10"><i class="dt_icon collection_icon"></i>Save</a>
		          <div class="public_pulldown_zh_CN collect_list" onmouseover="$(this).show()" onmouseout="$(this).hide()" style="display:none">
		            <dl>
		              <dd><a onclick="dynMsgUtil.impMyPub('${resNode}','${resId?c}', this)" id="myPub_iris_${dynId?c}_${resId?c}">My Publication</a></dd>
		              <dd><a onclick="dynMsgUtil.impMyRef(this,'${resNode}','${resId?c}', this)" id="myRef_iris_${dynId?c}_${resId?c}">My Reference</a></dd>
		            </dl>
		          </div>
		      	</b>
		      	</#if>
		        
		        <#if (isObjectOwner!=1)&&(resType==1||resType==2)>
		      	<span class="fe6e6 mright10">|</span>
		      	<a class="fulltext_iris f888 mright10" style="cursor:pointer" resId="${resId?c}" resNode="${resNode}" resType="${resType}"><i class="dt_icon fulltext_icon"></i>Full text</a>
		      	</#if>
	        
	        </div>
        </div>
        <div class="zan_list award_detail_${resType}_${resNode}_${resId?c}" <#if (awardNum<=0)>style="display:none"</#if> >
        <#if (hasAward==0)>
        <a title="Like" onclick="dynMsgUtil.ajaxAward(this, '${resType}','${resNode}','${resId?c}','${permission}')" style="cursor:pointer" class="zan_a award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i></a>  
        <a title="Unlike" onclick="dynMsgUtil.ajaxCancelAward(this, '${resType}','${resNode}','${resId?c}','${permission}')" style="cursor:pointer;display:none" class="zan_a cancel_award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i></a>  
        <#else>
        <a title="Like" onclick="dynMsgUtil.ajaxAward(this, '${resType}','${resNode}','${resId?c}','${permission}')" style="cursor:pointer;display:none" class="zan_a award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i></a>  
        <a title="Unlike" onclick="dynMsgUtil.ajaxCancelAward(this, '${resType}','${resNode}','${resId?c}','${permission}')" style="cursor:pointer;" class="zan_a cancel_award_link${resType}_${resNode}_${resId?c}"><i class="dt_icon zan_icon"></i></a>  
        </#if>
        
        <span class="user-list Fleft award_psn_list_${resType}_${resNode}_${resId?c}"><#if (enAwardPsnContent!='')>${enAwardPsnContent}</#if></span>
        <span class="f666 Fleft mtop10 award_num_${resType}_${resNode}_${resId?c}" <#if (awardNum<=0)>style="display:none"</#if> >A total of <a onclick="Award.showAwardDetail(this)" style="cursor:pointer" awardId="${awardId?c}" class="award_detail_thickbox_${resType}_${resNode}_${resId?c} Blue b"><span class="award_num">${awardNum}</span></a> people like this.</span>
        </div>
      </div>
      
    
      <div class="f_comment2">
        <dl id="reply_dl_${dynId?c}_${resId?c}">
        	<#if resReplyDetails?exists>
	         <#list resReplyDetails as resReply>
	          <dd class="reply_item_${resReply.recordId?c}">
	            <div class="t_namecard"><a href="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReplyerId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${resReply.replyerAvatar}" width="32" height="32" border="0" /></a></div>
	            <div class="t_message2">
	              <p><a href="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReplyerId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${resReply.replyerEnName}</a><#if (resReply.des3ReceiverId?exists) && (resReply.des3ReceiverId!='')>&nbsp;Replied to&nbsp;<a href="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReceiverId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${resReply.receiverEnName}</a></#if>: ${resReply.replyContent}</p>
	              <p class="en10">${resReply.replyDate}&nbsp;&nbsp;<span class="reply_operation" style="display:none;"><a style="cursor:pointer" receiverPsnName="${resReply.replyerEnName}" receiverPsnLink="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReplyerId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" onclick="dynMsgUtil.replyToPsn('${resId?c}','${resType}','${resNode}','${dynId?c}','${resReply.des3ReplyerId}',event, this)" class="Blue" title="回复"><i class="evaluate-icon"></i></a>&nbsp;<#if (isResOwner==1 || resReply.replyerId==currentPsnId)><a onclick="dynMsgUtil.deleteReply('${dynId?c}','${resReply.recordId?c}','${resId?c}','${resType}','${resNode}',event, this)" style="cursor:pointer" class="Blue" title="删除"><i class="delete-icon"></i></a></#if></span></p>
	            </div>
	          </dd>
	          <#if (replyNum>2) && resReply_has_next>
	          <dd style="cursor:pointer" id="reply_more_dd_${dynId?c}_${resId?c}" onclick="dynMsgUtil.ajaxExtendReply(this,'${dynId?c}','${resReply.replyId?c}','${queryType}')" beginRecord="1"><a class="Blue">Others&nbsp;<label id="reply_num_${dynId?c}_${resReply.replyId?c}">${(replyNum?number)-2}</label>&nbsp;comment(s)</a></dd>
	          </#if>
	          </#list>
          </#if>
        </dl>
      </div>
      
      <#if (isShowReply==1)>
      <div class="comments_box">
        <div class="reply_enter_div" id="input_${dynId?c}_${resId?c}">
          <input type="text" class="inp_text f666" value="Comment……"  style="width:590px;cursor:pointer" onclick="dynMsgUtil.showReplyBox('${resId?c}','${resType}','${resNode}','${dynId?c}',event, this)"/>
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
                <a style="cursor:pointer" onclick="dynMsgUtil.ajaxReply('${resType}','${resNode}','${resId?c}','${dynId?c}','${permission}', 0, this)" class="replyBtn uiButton uiButtonConfirm">Comment</a>&nbsp;&nbsp;
              </p> 
           </div>
        </div>
      </div>
      </#if>
      
    </div>
    
    <div class="drop_down"><a style="cursor:pointer;display:none;" onmouseover="dynMsgUtil.extendDynOperation(this)" onmouseout="dynMsgUtil.shrinkDynOperation(this)" class="link_delete"></a>
        <div class="div_item_delete drop_down_en_US" style="display:none" onmouseout="$(this).hide()" onmouseover="$(this).show();$(this).parent().find('.link_delete').show();">
        <div class="drop_jt_en_US"></div>
           <ul>
             <#if (isMine==1)>
               <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>Delete</a></li>
             <#else>
               <li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>Delete</a></li>
               <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxShieldDynByPsn('${dynId?c}')"><i class="img_nochoose"></i>Shield this person</a></li>
             </#if>
           </ul>
         </div>
    </div>
</li>