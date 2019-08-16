<li onmouseover="dynMsgUtil.showDynOperation(this)"  onmouseout="dynMsgUtil.hideDynOperation(this)">
    <div class="ui_avatar">
         <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${psnAvatar}" width="50" height="50" border="0" /></a>
    </div>
    <div class="publish-nr">
     <div class="moving-contat">
        <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${psnEnName}</a>&nbsp;shared ${resTotal}&nbsp;<#switch resType><#case 1><#if (resTotal>1)>publications<#else>publication</#if><#break><#case 2><#if (resTotal>1)>references<#else>reference</#if><#break><#case 3><#if (resTotal>1)>files<#else>file</#if><#break><#case 4><#if (resTotal>1)>projects<#else>project</#if><#break><#case 5><#if (resTotal>1)>coursewares<#else>courseware</#if><#break><#default>others</#switch>: ${sayContent}
     </div>
	<#if (resDetails?exists) && (resDetails?size>0)>   
     <#list resDetails as dynRes>
      <div class="txt_box">
        <#if (dynRes.fileExt?exists)>
        <#if (dynRes.fileExt)!='imgIc'>
        <span class="icon_${dynRes.fileExt} shared_pic">&nbsp;</span><a onclick="dynMsgUtil.ajaxDownloadRes(this, '${resType}','${dynRes.resNode}','${dynRes.resId?c}')" class="Blue">${dynRes.resEnTitle}</a>
        <#else>
        <p><a href="${dynRes.imgIcPath}" onclick="dynMsgUtil.ajaxIsShowPicInTB(this,'${resType}','${dynRes.resNode}','${dynRes.resId?c}')" class="gallery_link thickbox" rel="gallery-plants${dynId?c}" title="${dynRes.resEnTitle}"><img src="${dynRes.imgIc}"/></a></p>
        <p><a onclick="dynMsgUtil.ajaxDownloadRes(this, '${resType}','${dynRes.resNode}','${dynRes.resId?c}')" class="Blue">${dynRes.resEnTitle}</a></p>
        </#if>
        
        <#else>
        <p id="request_content_${dynId?c}_${dynRes.resId?c}"><#if dynRes.resEnauthor!=''>${dynRes.resEnauthor}, </#if><a href="${dynRes.resLink}<#if (dynRes.groupId?exists)>&des3GroupId=${dynRes.groupId}</#if>" id="shareTheme_${resType}_${dynRes.resId?c}" class="Blue" target="_blank">${dynRes.resEnTitle}</a><#if dynRes.resEnother!=''>, ${dynRes.resEnother}</#if></p>
        </#if>
       
      </div>
      
      <div class="appraisa-choose">
        <div class="t_detail">
             <#assign dynDateVar>
             <#if dynDate=='dateTime'>${normalEnDynDate}<#else>${dynDate} ago</#if>
             </#assign>
        <span class="f888 date01"> 
           ${dynDateVar}&nbsp;&nbsp;&nbsp;&nbsp;
        </span>
        
	        <div class="praise_box" style="margin-left:20px;">
	        
		        <#if (dynRes.hasAward==0)>
		        <a onclick="dynMsgUtil.ajaxAward(this, '${resType}','${dynRes.resNode}','${dynRes.resId?c}','${permission}')" style="cursor:pointer" class="mright10 f888 award_link${resType}_${dynRes.resNode}_${dynRes.resId?c}"><i class="dt_icon zan_icon"></i>Like</a>  
		        <a onclick="dynMsgUtil.ajaxCancelAward(this, '${resType}','${dynRes.resNode}','${dynRes.resId?c}','${permission}')" style="cursor:pointer;display:none" class="mright10 f888 cancel_award_link${resType}_${dynRes.resNode}_${dynRes.resId?c}"><i class="dt_icon zan_icon"></i>Unlike</a>  
		        <#else>
		        <a onclick="dynMsgUtil.ajaxAward(this, '${resType}','${dynRes.resNode}','${dynRes.resId?c}','${permission}')" style="cursor:pointer;display:none" class="mright10 f888 award_link${resType}_${dynRes.resNode}_${dynRes.resId?c}"><i class="dt_icon zan_icon"></i>Like</a>  
		        <a onclick="dynMsgUtil.ajaxCancelAward(this, '${resType}','${dynRes.resNode}','${dynRes.resId?c}','${permission}')" style="cursor:pointer;" class="mright10 f888 cancel_award_link${resType}_${dynRes.resNode}_${dynRes.resId?c}"><i class="dt_icon zan_icon"></i>Unlike</a>  
		        </#if>
		        
		        <#if (dynRes.isShowReply==1)>
		        <span class="fe6e6 mright10">|</span>
		        <a style="cursor:pointer" onclick="dynMsgUtil.showReplyBox('${dynRes.resId?c}','${resType}','${dynRes.resNode}','${dynId?c}',event, this)" class="mright10 f888"><i class="dt_icon review_icon"></i>Comment<span<#if (dynRes.replyNum==0)> style="display:none"</#if>>(<label id="reply_num_label_${dynId?c}_${dynRes.resId?c}" class="reply_num_label_${resType}_${dynRes.resId?c}">${dynRes.replyNum}</label>)</span></a>
		        <#else>
		        <#if (dynRes.replyNum>0)><span class="fe6e6 mright10">|</span></#if>
		        <span<#if (dynRes.replyNum==0)> style="display:none"</#if> class="f666" >Comment(<label id="reply_num_label_${dynId?c}_${dynRes.resId?c}" class="reply_num_label_${resType}_${dynRes.resId?c}">${dynRes.replyNum}</label>)</span>
		        </#if>
		        
		        <span class="fe6e6 mright10">|</span>
		        <b class="public_pulldown">
	        	<#if (resType==1||resType==2||resType==4)>
		        	<a style="cursor:pointer" class="f888 mright10 share_sites_show"><i class="dt_icon share_icon"></i>Share<span class="shareCountSpan_${resType}_${dynRes.resId?c}"<#if (dynRes.shareNum==0)> style="display:none"</#if>>(<label class="shareCountLabel_${resType}_${dynRes.resId?c}">${dynRes.shareNum}</label>)</span><i class="publication-up"></i><!--<i class="publication-down"></i>--></a>
		        	<a class="share_pull" style="display:none" resId="${dynRes.resId?c}" resType="${resType}" resNode="${dynRes.resNode}" dynId="${dynId?c}" ></a>
	        	<#else>
		        	<a style="cursor:pointer" class="f888 mright10" onclick="dynMsgUtil.showSharePage(this,'${resType}','${dynRes.resNode}','${dynRes.resId?c}', '${dynId?c}')"><i class="dt_icon share_icon"></i>Share<span class="shareCountSpan_${resType}_${dynRes.resId?c}"<#if (dynRes.shareNum==0)> style="display:none"</#if>>(<label class="shareCountLabel_${resType}_${dynRes.resId?c}">${dynRes.shareNum}</label>)</span></a>
	        	</#if>
		        </b>
		        
		        <#if dynRes.resIsMine?exists>
			        <#if (dynRes.resIsMine!=1)&&(resType==1||resType==2)>
			      	<span class="fe6e6 mright10">|</span>
			      	<b class="public_pulldown"><a onmouseover="dynMsgUtil.showCollectDiv(this)" class="f888 mright10"><i class="dt_icon collection_icon"></i>Save</a>
			          <div class="public_pulldown_zh_CN collect_list" onmouseover="$(this).show()" onmouseout="$(this).hide()" style="display:none">
			            <dl>
			              <dd><a onclick="dynMsgUtil.impMyPub('${dynRes.resNode}','${dynRes.resId?c}', this)" id="myPub_iris_${dynId?c}_${dynRes.resId?c}">My Publication</a></dd>
			              <dd><a onclick="dynMsgUtil.impMyRef(this,'${dynRes.resNode}','${dynRes.resId?c}', this)" id="myRef_iris_${dynId?c}_${dynRes.resId?c}">My Reference</a></dd>
			            </dl>
			          </div>
			      	</b>
			      	</#if>
		        <#else>
			        <#if (isObjectOwner!=1)&&(resType==1||resType==2)>
			      	<span class="fe6e6 mright10">|</span>
			      	<b class="public_pulldown"><a onmouseover="dynMsgUtil.showCollectDiv(this)" class="f888 mright10"><i class="dt_icon collection_icon"></i>Save</a>
			          <div class="public_pulldown_zh_CN collect_list" onmouseover="$(this).show()" onmouseout="$(this).hide()" style="display:none">
			            <dl>
			              <dd><a onclick="dynMsgUtil.impMyPub('${dynRes.resNode}','${dynRes.resId?c}', this)" id="myPub_iris_${dynId?c}_${dynRes.resId?c}">My Publication</a></dd>
			              <dd><a onclick="dynMsgUtil.impMyRef(this,'${dynRes.resNode}','${dynRes.resId?c}', this)" id="myRef_iris_${dynId?c}_${dynRes.resId?c}">My Reference</a></dd>
			            </dl>
			          </div>
			      	</b>
			      	</#if>
		        </#if>
		        
		        <#if dynRes.resIsMine?exists>
			        <#if (dynRes.resIsMine!=1)&&(resType==1||resType==2)>
			      	<span class="fe6e6 mright10">|</span>
			      	<a class="fulltext_iris f888 mright10" style="cursor:pointer" resId="${dynRes.resId?c}" resNode="${dynRes.resNode}" resType="${resType}"><i class="dt_icon fulltext_icon"></i>Full text</a>
			      	</#if>
		        <#else>
			        <#if (isObjectOwner!=1)&&(resType==1||resType==2)>
			      	<span class="fe6e6 mright10">|</span>
			      	<a class="fulltext_iris f888 mright10" style="cursor:pointer" resId="${dynRes.resId?c}" resNode="${dynRes.resNode}" resType="${resType}"><i class="dt_icon fulltext_icon"></i>Full text</a>
			      	</#if>
		        </#if>
	        
	        </div>
        </div>
        <div class="zan_list award_detail_${resType}_${dynRes.resNode}_${dynRes.resId?c}" <#if (dynRes.awardNum<=0)>style="display:none"</#if> >
        <#if (dynRes.hasAward==0)>
        <a title="Like" onclick="dynMsgUtil.ajaxAward(this, '${resType}','${dynRes.resNode}','${dynRes.resId?c}','${permission}')" style="cursor:pointer" class="zan_a award_link${resType}_${dynRes.resNode}_${dynRes.resId?c}"><i class="dt_icon zan_icon"></i></a>  
        <a title="Unlike" onclick="dynMsgUtil.ajaxCancelAward(this, '${resType}','${dynRes.resNode}','${dynRes.resId?c}','${permission}')" style="cursor:pointer;display:none" class="zan_a cancel_award_link${resType}_${dynRes.resNode}_${dynRes.resId?c}"><i class="dt_icon zan_icon"></i></a>  
        <#else>
        <a title="Like" onclick="dynMsgUtil.ajaxAward(this, '${resType}','${dynRes.resNode}','${dynRes.resId?c}','${permission}')" style="cursor:pointer;display:none" class="zan_a award_link${resType}_${dynRes.resNode}_${dynRes.resId?c}"><i class="dt_icon zan_icon"></i></a>  
        <a title="Unlike" onclick="dynMsgUtil.ajaxCancelAward(this, '${resType}','${dynRes.resNode}','${dynRes.resId?c}','${permission}')" style="cursor:pointer;" class="zan_a cancel_award_link${resType}_${dynRes.resNode}_${dynRes.resId?c}"><i class="dt_icon zan_icon"></i></a>  
        </#if>
        
        <span class="user-list Fleft award_psn_list_${resType}_${dynRes.resNode}_${dynRes.resId?c}"><#if (dynRes.enAwardPsnContent!='')>${dynRes.enAwardPsnContent}</#if></span>
        <span class="f666 Fleft mtop10 award_num_${resType}_${dynRes.resNode}_${dynRes.resId?c}" <#if (dynRes.awardNum<=0)>style="display:none"</#if> >A total of <a onclick="Award.showAwardDetail(this)" style="cursor:pointer" awardId="${dynRes.awardId?c}" class="award_detail_thickbox_${resType}_${dynRes.resNode}_${dynRes.resId?c} Blue b"><span class="award_num">${dynRes.awardNum}</span></a> people like this.</span>
        </div>
      </div>
      
      <div class="f_comment2">
        <dl id="reply_dl_${dynId?c}_${dynRes.resId?c}">
        	<#if dynRes.resReplyDetails?exists>
	         <#list dynRes.resReplyDetails as resReply>
	          <dd class="reply_item_${resReply.recordId?c}">
	            <div class="t_namecard"><a href="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReplyerId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${resReply.replyerAvatar}" width="32" height="32" border="0" /></a></div>
	            <div class="t_message2">
	              <p><a href="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReplyerId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${resReply.replyerEnName}</a><#if (resReply.des3ReceiverId?exists) && (resReply.des3ReceiverId!='')>&nbsp;Replied to&nbsp;<a href="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReceiverId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${resReply.receiverEnName}</a></#if>: ${resReply.replyContent}</p>
	              <p class="en10">${resReply.replyDate}&nbsp;&nbsp;<span class="reply_operation" style="display:none;"><a style="cursor:pointer" receiverPsnName="${resReply.replyerEnName}" receiverPsnLink="/scmwebsns/resume/psnView?des3PsnId=${resReply.des3ReplyerId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" onclick="dynMsgUtil.replyToPsn('${dynRes.resId?c}','${resType}','${dynRes.resNode}','${dynId?c}','${resReply.des3ReplyerId}',event, this)" class="Blue" title="Reply"><i class="evaluate-icon"></i></a>&nbsp;<#if (dynRes.isResOwner==1 || resReply.replyerId==currentPsnId)><a onclick="dynMsgUtil.deleteReply('${dynId?c}','${resReply.recordId?c}','${dynRes.resId?c}','${resType}','${dynRes.resNode}',event, this)" style="cursor:pointer" class="Blue" title="Delete"><i class="delete-icon"></i></a></#if></span></p>
	            </div>
	          </dd>
	          <#if (dynRes.replyNum>2) && resReply_has_next>
	          <dd style="cursor:pointer" id="reply_more_dd_${dynId?c}_${dynRes.resId?c}" onclick="dynMsgUtil.ajaxExtendReply(this,'${dynId?c}','${resReply.replyId?c}','${queryType}')" beginRecord="1"><a class="Blue">Others&nbsp;<label id="reply_num_${dynId?c}_${resReply.replyId?c}">${(dynRes.replyNum?number)-2}</label>&nbsp;comment(s)</a></dd>
	          </#if>
	          </#list>
          </#if>
        </dl>
      </div>

	<#if (dynRes.isShowReply==1)>
      <div class="comments_box">
        <div class="reply_enter_div" id="input_${dynId?c}_${dynRes.resId?c}">
          <input type="text" class="inp_text f666" value="Comment……"  style="width:590px;cursor:pointer" onclick="dynMsgUtil.showReplyBox('${dynRes.resId?c}','${resType}','${dynRes.resNode}','${dynId?c}',event, this)"/>
        </div>
        <div style="display:none;" class="reply_content_div" id="textarea_${dynId?c}_${dynRes.resId?c}">
          <div class="t_namecard">
             <a href="/scmwebsns/resume/psnView?des3PsnId=${currentDes3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" id="link_current_${dynId?c}_${dynRes.resId?c}">
             <img src="${currentPsnAvatar}" id="img_currentAvatar_${dynId?c}_${dynRes.resId?c}" width="32" height="32" border="0"/></a>
             <input type="hidden" value="${currentDes3PsnId}" id="input_currentPsn_des3PsnId_${dynId?c}_${dynRes.resId?c}"/>
             <input type="hidden" value="${currentPsnName}" id="input_currentPsn_${dynId?c}_${dynRes.resId?c}"/>
          </div>
          <div class="t_message3">
              <div class="main_input" style="width:540px;margin-bottom:5px;">
              <div class="input-line-dyn" id="textarea_box_${dynId?c}_${dynRes.resId?c}" contentEditable="true" onKeyDown="dynMsgUtil.replyWordCount(this,'${dynRes.resId?c}','${dynId?c}',250)" onKeyUp="dynMsgUtil.replyWordCount(this,'${dynRes.resId?c}','${dynId?c}',250)" onPaste="dynMsgUtil.replyWordCount(this,'${dynRes.resId?c}','${dynId?c}',250)" onFocus="dynMsgUtil.replyWordCount(this,'${dynRes.resId?c}','${dynId?c}',250)" onClick="dynMsgUtil.textCommentBoxClick(event)"></div>
              <p style="color: #999; text-align: right; padding-right: 5px;"><label id="count_label_${dynId?c}_${dynRes.resId?c}">0</label>/250</p>
              </div>
  
              <p>
                <a style="cursor:pointer" onclick="dynMsgUtil.ajaxReply('${resType}','${dynRes.resNode}','${dynRes.resId?c}','${dynId?c}','${permission}', 0, this)" class="replyBtn uiButton uiButtonConfirm">Comment</a>&nbsp;&nbsp;
              </p> 
           </div>
        </div>
      </div>
    </#if>

      <!--第二个成果显示-->
      <#if dynRes_has_next>
      <div class="clear_h15 two_line"></div>
      </#if>
      </#list>
      
      <#if ((resDetails?size)<resTotal)>
      <!--更多成果铵钮-->
      <div class="clear" id="clear_more_${dynId?c}"></div>
      <div class="more_btn" id="more_div_${dynId?c}" style="cursor:pointer" onclick="dynMsgUtil.ajaxLoadExtend(this,'${dynId?c}','${resType}','${permission}')" currentDes3PsnId="${currentDes3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" currentPsnAvatar="${currentPsnAvatar}" currentPsnName="${currentPsnName}" dynDate="${dynDateVar}" isMine="${isMine}" isObjectOwner="${isObjectOwner}"><a class="Blue">More……</a></div>
      <!--更多铵钮结束-->
      </#if>
      
      </#if>
      
    </div>
        <#if (isGroupFlag==1)>
           	<#if (isMine==1) || (isGroupManager=='1')>
	    	<div class="drop_down"><a style="cursor:pointer;display:none;" onmouseover="dynMsgUtil.extendDynOperation(this)" onmouseout="dynMsgUtil.shrinkDynOperation(this)" class="link_delete"></a>
		        <div class="div_item_delete drop_down_en_US" style="display:none" onmouseout="$(this).hide()" onmouseover="$(this).show();$(this).parent().find('.link_delete').show();">
			        <div class="drop_jt_en_US"></div>
			        <ul>
			        	<li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>Delete</a></li>
			        </ul>
		        </div>
	    	</div>
           	</#if>
        <#else>
        <div class="drop_down"><a style="cursor:pointer;display:none;" onmouseover="dynMsgUtil.extendDynOperation(this)" onmouseout="dynMsgUtil.shrinkDynOperation(this)" class="link_delete"></a>
        <div class="div_item_delete drop_down_en_US" style="display:none" onmouseout="$(this).hide()" onmouseover="$(this).show();$(this).parent().find('.link_delete').show();">
        <div class="drop_jt_en_US"></div>
           <ul>
             <#if (isMine==1)>
               <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>Delete</a></li>
               <!--<li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxShieldDynByType('09','${dynId?c}')"><i class="img_nochoose"></i>Shield such update</a></li>-->
             <#else>
               <li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>Delete</a></li>
               <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxShieldDynByPsn('${dynId?c}')"><i class="img_nochoose"></i>Shield this person</a></li>
               <!--<li><a style="cursor:pointer;" onclick="dynMsgUtil.ajaxShieldDynByType('09','${dynId?c}')"><i class="img_nochoose"></i>Shield such update</a></li>-->
             </#if>
           </ul>
         </div>
    	</div>
    	</#if>
</li>