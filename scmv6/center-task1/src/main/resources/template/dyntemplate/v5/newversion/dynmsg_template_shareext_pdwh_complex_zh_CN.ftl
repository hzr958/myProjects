<li onmouseover="dynMsgUtil.showDynOperation(this)"  onmouseout="dynMsgUtil.hideDynOperation(this)">
    <div class="ui_avatar">
        <a href="/scmwebsns/resume/psnView?des3PsnId=${des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank"><img src="${psnAvatar}" width="50" height="50" border="0" /></a>
    </div>
    <div class="publish-nr">
     <div class="moving-contat">
       <#list sharerDetails as sharer><a href="/scmwebsns/resume/psnView?des3PsnId=${sharer.sharer_des3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" target="_blank" class="Blue">${sharer.sharer_psnName}</a><#if sharer_has_next>、</#if></#list>分享成果：
     </div>
     
	 <#if (resDetails?exists) && (resDetails?size>0)>   
     <#list resDetails as dynRes>
      <div class="txt_box">
        <#if (dynRes.fileExt?exists)>
        <#if (dynRes.fileExt)!='imgIc'>
        <span class="icon_${dynRes.fileExt} shared_pic">&nbsp;</span><a href="${dynRes.resLink}" class="Blue">${dynRes.resTitle}</a>
        <#else>
        <p><a href="${dynRes.imgIcPath}" class="gallery_link thickbox" rel="gallery-plants${dynId?c}" title="${dynRes.resTitle}"><img src="${dynRes.imgIc}"/></a></p>
        <p><a href="${dynRes.resLink}" class="Blue" target="_blank">${dynRes.resTitle}</a></p>
        </#if>
        
        <#else>
            <p id="request_content_${dynId?c}_${dynRes.resId?c}"><#if dynRes.resAuthor!=''>${dynRes.resAuthor}，</#if><a href="${dynRes.resLink}<#if (dynRes.groupId?exists)>&des3GroupId=${dynRes.groupId}</#if>" id="shareTheme_${resType}_${dynRes.resId?c}" class="Blue" target="_blank">${dynRes.resTitle}</a><#if dynRes.resOther!=''>，${dynRes.resOther}</#if></p>
        </#if>
       
      </div>
      
      <div class="appraisa-choose">
        <div class="t_detail">
          <#assign dynDateVar>
          <#if dynDate=='dateTime'>${normalDynDate}<#else>${dynDate?replace("s","秒")?replace("m","分钟")?replace("h","小时")?replace("d","天")}以前</#if>
          </#assign>
        <span class="f888 date01">${dynDateVar}&nbsp;&nbsp;&nbsp;&nbsp;</span>
        
        <div style=" float:left; width:450px;">
        
        <#if (isObjectOwner!=1)>
        <div class="collect_box"><a style="cursor:pointer;" onmouseover="dynMsgUtil.showCollectDiv(this)" class="Blue">收藏</a>&nbsp;
            <div id="collect_zh_CN" class="collect_list" onmouseover="$(this).show()" onmouseout="$(this).hide()" style="display:none">
                   <ul>
                       <li><a onclick="dynMsgUtil.impMyPubFromPdwh('${dynRes.resNode}','${dynRes.resId?c}', '${dynRes.dbid}', this)" style="cursor:pointer;" id="myPub_iris_${dynId?c}_${dynRes.resId?c}">&nbsp;&nbsp;&nbsp;我的成果库</a></li>
                       <li><a onclick="dynMsgUtil.impMyRefFromPdwh('${dynRes.resNode}','${dynRes.resId?c}', '${dynRes.dbid}', this)" style="cursor:pointer;" id="myRef_iris_${dynId?c}_${dynRes.resId?c}">&nbsp;&nbsp;&nbsp;我的文献库</a></li>
                   </ul>
            </div>
        </div>
        </#if>
        
        </div>
       
        <div class="clear" style="height:0px; overflow:hidden;"></div>
       
        </div>
        <#if (dynRes.awardPsnContent!='')>
        <p class="total-praise total-praise${resType}_${dynRes.resNode}_${dynRes.resId?c}">${dynRes.awardPsnContent}</p>
        </#if>
      </div>

      <!--第二个成果显示-->
      <#if dynRes_has_next>
      <div class="clear_h15 two_line"></div>
      </#if>
      </#list>
      
      <#if ((resDetails?size)<resTotal)>
      <!--更多成果铵钮-->
      <div class="clear" id="clear_more_${dynId?c}"></div>
      <div class="more_btn" id="more_div_${dynId?c}" style="cursor:pointer" onclick="dynMsgUtil.ajaxLoadExtend(this,'${dynId?c}','${resType}','${permission}')" currentDes3PsnId="${currentDes3PsnId}&menuId=<#if (isGroupFlag==1)>3<#else>1100</#if>" currentPsnAvatar="${currentPsnAvatar}" currentPsnName="${currentPsnName}" dynDate="${dynDateVar}" isMine="${isMine}" isObjectOwner="${isObjectOwner}"><a class="Blue">更多……</a></div>
      <!--更多铵钮结束-->
      </#if>
      
      </#if>
      
    </div>
        <#if (isGroupFlag==1)>
           	<#if (isMine==1) || (isGroupManager=='1')>
           	<div class="drop_down"><a style="cursor:pointer;display:none;" onmouseover="dynMsgUtil.extendDynOperation(this)" onmouseout="dynMsgUtil.shrinkDynOperation(this)" class="link_delete"></a>
		        <div class="div_item_delete drop_down_zh_CN" style="display:none" onmouseout="$(this).hide()" onmouseover="$(this).show();$(this).parent().find('.link_delete').show();">
		        	<div class="drop_jt_zh_CN"></div>
			        <ul>
			        	<li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>删除此动态</a></li>
			        </ul>
	         	</div>
    		</div>
           	</#if>
        <#else>
        <div class="drop_down"><a style="cursor:pointer;display:none;" onmouseover="dynMsgUtil.extendDynOperation(this)" onmouseout="dynMsgUtil.shrinkDynOperation(this)" class="link_delete"></a>
        <div class="div_item_delete drop_down_zh_CN" style="display:none" onmouseout="$(this).hide()" onmouseover="$(this).show();$(this).parent().find('.link_delete').show();">
        <div class="drop_jt_zh_CN"></div>
           <ul>
             <#if (isMine==1)>
               <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>删除此动态</a></li>
             <#else>
               <li><a style="cursor:pointer; border-width:0;" onclick="dynMsgUtil.ajaxDeleteDyn('${dynId?c}')"><i class="img_nochoose"></i>删除此动态</a></li>
             </#if>
           </ul>
         </div>
    	</div>
    	</#if>
</li>