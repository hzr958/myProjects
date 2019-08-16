<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="group_box" id="groupbox">
  <script type="text/javascript">
	$(document).ready(function(){
		
		//添加模块隐藏域
		$("#pubToModule").val( $("#toModule").val()  ) ;
		//筛选年份加载
		Group.pubScreenBoxLoadYear("#screen_year",Group.pubScreenBoxConfirm,'${newestpublishYear}');

		//$(".thickbox").thickbox({});
		 //全文下载
		$(".notPrintLinkSpan").fullTextRequest();
		$("#screen_box").click(function(event){
		    event.stopPropagation();

		});
		//筛选年份加载
		Group.pubScreenBoxLoadYear("#screen_year",Group.pubScreenBoxConfirm,'${newestpublishYear}');
		Group.pubScreenBoxLoadPubType($("#_pubTypesStr").val(),'#screen_pubType','Group.pubScreenBoxHit(this,1,Group.pubScreenBoxConfirm)');
		Group.pubScreenBoxLoadRecords($("#_recordsStr").val(),'#screen_records','Group.pubScreenBoxHit(this,1,Group.pubScreenBoxConfirm)');
		$("#screen_box").find("div[class='delete']").click(function(){
			$(this).prev().click();
		});
		
	  	//检索成果
		$("#search_publication_togroup_op,#search_publication_togroup_op22,#search_publication_togroup_op33").click(function(){
			var artType=$("#navAction").val();
			var articleType;
			if(artType=='groupPubs'){
				articleType=1;
			}else if(artType=='groupRefs'){
				articleType=2;
			}else{
				return;
			}
			var des3GroupId=$("#des3GroupId").val();
			var groupFolderId = $("#groupFolderId").val();
			var groupNodeId=$("#groupNodeId").val();
			Group.publication.clearBackPage();
			if(articleType==1){
				forwardUrl1("/pubweb/publication/collect?groupId="+encodeURIComponent(des3GroupId) +"&nodeId="+groupNodeId+"&artType="+artType);
			}else if(articleType==2){
				forwardUrl1("/reference/group/collect?groupId="+encodeURIComponent(des3GroupId) +"&nodeId="+groupNodeId+"&artType="+artType);
			}
		});
	  	//Placeholder兼容IE11处理
		var $_input = $("#input_default01");
		Group.compatibilityPlaceholder($_input);
		//回显
		var  groupPubEdit = $("#gp_groupPubEdit").val() ;
		if( "true" == groupPubEdit    ){
			////年份 
			var gp_screenYears = $("#gp_screenYears").val();
			if(gp_screenYears !=undefined && gp_screenYears !=""){
				var div_obj = $("#screen_year").find("div[paramvalue='"+gp_screenYears+"']");
					div_obj.addClass("paramSelected");
					div_obj.parent().addClass("active");
				
			}
			//// 类别 
			var gp_screenPubTypes = $("#gp_screenPubTypes").val();
			if(gp_screenPubTypes !=undefined && gp_screenPubTypes !=""){
				var  pubTypeArr = gp_screenPubTypes.split(",") ;
				for(var  i = 0 ; i<pubTypeArr .length  ; i++){
					var div_obj = $("#screen_pubType").find("div[paramvalue='"+pubTypeArr[i]+"']");
					div_obj.addClass("paramSelected");
					div_obj.parent().addClass("active");
				}
			}
			//收录screen_records
			var gp_screenRecords = $("#gp_screenRecords").val();
			if(gp_screenRecords !=undefined && gp_screenRecords !=""){
				var  pubRecordArr = gp_screenRecords.split(",") ;
				for(var  i = 0 ; i<pubRecordArr .length  ; i++){
					var div_obj = $("#screen_records").find("div[paramvalue='"+pubRecordArr[i]+"']");
					div_obj.addClass("paramSelected");
					div_obj.parent().addClass("active");
				}
			} 
			//排序 gp_orderBy
			var gp_orderBy = $("#gp_orderBy").val();
			if(gp_orderBy !=undefined && gp_orderBy !=""){
				 $("#_orderBy").val(gp_orderBy)
				
			} 
			//清空 群组编辑 专用的隐藏域
			 $("input[id^='gp_']").each(function(){
				 $(this).val("");
			 }) ;
		}
		
	});
	function compatibilityPlaceholder(obj){
		checkPlaceholder(obj);
		obj.blur(function(){
	  		checkPlaceholder(obj);
	  	});
		obj.keyup(function(){
	  		checkPlaceholder(obj);
	  	});
	}
	function checkPlaceholder(obj){
		if(obj.val().length>0){
			obj.css("color","#333");
	  	}else{
	  		obj.css("color","#999");
	  	}
	}
	function toPubCollect(){
		var des3GroupId=$("#des3GroupId").val();
		var folderId=$("#groupFolderId").val();
		var groupNodeId=$("#groupNodeId").val();
		Group.publication.clearBackPage();
		forwardUrl1("/pubweb/publication/enter?backType=1&menuId=31&groupId="+encodeURIComponent(des3GroupId) + "&folderId="+folderId+"&nodeId=1");
		
	}
	</script>
  <div class="group_l fl">
    <form action="" method="post" id="mainForm">
      <!----------------------------------------隐藏的数据  ----------------------------------------->
      <input type="hidden" id="pubToModule" value="${toModule}" name="toModule" /> <input type="hidden"
        id="newestpublishYear" value="${newestpublishYear}" name="newestpublishYear" /> <input type="hidden"
        id="_pubTypesStr" name="_pubTypesStr" value="<c:out value="${pubTypesStr}"/>" /> <input type="hidden"
        id="_recordsStr" name="_recordsStr" value="<c:out value="${recordsStr}"/>" />
      <!-- 成果是否导入成果 -->
      <input type="hidden" id="memberPubsImport" value="false" name="memberPubsImport" /> <input type="hidden"
        id="groupPsn.des3GroupId" value="${des3GroupId}" name="groupPsn.des3GroupId" /> <input type="hidden"
        id="groupPsn.groupNodeId" value="${groupNodeId}" name="groupPsn.groupNodeId" /> <input type="hidden"
        id="des3GroupId" value="<iris:des3 code='${groupId }'/>" name="des3GroupId" /> <input type="hidden"
        id="groupNodeId" value="${groupNodeId}" name="groupNodeId" /> <input type="hidden" id="des3GroupNodeId"
        value="<iris:des3 code="${groupNodeId}"/>" name="des3GroupNodeId" /> <input type="hidden" id="groupName"
        value="${groupPsn.groupName}" name="groupName" /> <input type="hidden" id="groupDescriptionSub"
        value="${groupDescriptionSub}" name="groupDescriptionSub" /> <input type="hidden" name="leftMenuId"
        id="leftMenuId" value="${leftMenuId}" /> <input type="hidden" name="searchName" id="searchName"
        value="${searchName}" /> <input type="hidden" name="searchId" id="searchId"
        value="${empty searchId?'-1':searchId}" /> <input type="hidden" name="pubIds" id="pubIds" value="" /> <input
        type="hidden" id="forwardUrl" name="forwardUrl" value="" /> <input type="hidden" name="exportType"
        id="exportType" value="" /> <input type="hidden" name="articleType" id="articleType" value="1" /> <input
        type="hidden" name="navAction" id="navAction" value="groupPubs" /> <input type="hidden" id="groupFolderId"
        name="groupFolderId" value="${searchId}" /> <input type="hidden" id="groupFolderIds" name="groupFolderIds"
        value="${searchIds}" /> <input type="hidden" id="des3PsnId" name="des3PsnId" value="${des3PsnId}" />
      <%-- 用于弹出成果评价 --%>
      <input type="hidden" id="doComment_btn" class="thickbox" alt="" title="<s:text name="group.res.pubs.appraisal"/>" />
      <input type="hidden" id="ooPsnId" value="${psnId}" /><input type="hidden" id="ooDes3PsnId"
        value="<iris:des3 code='${psnId}'/>" /> <input type="hidden" id="groupRole" value="${groupInvitePsn.groupRole}" />
      <input type="hidden" alt="" class="thickbox" id="hidden-shareLink" /> <input type="hidden" alt=""
        class="thickbox" id="hidden-awardLink" />
      <!-------------------------------------------隐藏的数据  ---------------  href="/pubweb/group/ajaxMyPubList?articleType=1&groupId=${groupPsn.groupId}&TB_iframe=true&height=435&width=800"-------------------------->
    </form>
    <div class="lt_box pa10">
      <div class="mn_wrap">
        <input id="add_group_pub_btn" class="thickbox" thickbox_bound="1" type="hidden"> <a
          class="waves-effect waves-light w86 fr ml10 button01" style="display: none"><s:text
            name="group.pubs.manageLists" /></a>
        <!-- 判断权限 currentGroupRoleStatus -->
        <c:if test="${currentPsnGroupRoleStatus==2 || currentPsnGroupRoleStatus==3 || currentPsnGroupRoleStatus==4}">
          <a id="groupPubImport_Btn" class="waves-effect waves-light w86 fr button01 thickbox"
            href="javascript:Group.groupPubImportUI();"> <s:text name="group.tip.noRecord.pub.myimportPub" />
          </a>
        </c:if>
        <div class="m_s_box fl">
          <input style="color: #333;" type="text" value="<c:out value='${searchKey}'/>" class="input_default"
            placeholder="<s:text name='group.pubs.screening.findPub' />" id="input_default01"> <input
            type="button" onclick="javascript:Group.searchGroupPub();" class="s_btn_small">
        </div>
        <div class="sorting">
          <a onclick="javascript:Group.pubScreenBoxLoad('#screen_box',event);" class="sorting_btn popbox-link"><i
            class="material-icons filter_list">&#xe152;</i> <s:text name="group.pubs.screening" /></a>
        </div>
        <div class="clear"></div>
      </div>
      <div style="width: 100%;">
        <div class="filter_container" id="screen_box" style="display: none;">
          <div class="filter_list">
            <div class="filter_list_title">
              <s:text name='group.pubs.screening.years' />
            </div>
            <div id="screen_year" class="filter_list_content paramAttr" paramName="screenYears">
              <!----------------------------------------筛选-年份  ----------------------------------------->
            </div>
          </div>
          <div class="filter_list">
            <div class="filter_list_title">
              <s:text name='group.pubs.screening.type' />
            </div>
            <div id="screen_pubType" class="filter_list_content paramAttr" paramName="screenPubTypes">
              <!----------------------------------------筛选-类别  ----------------------------------------->
              <div class="filter_list_value" onclick="">
                <div class="text">期刊论文</div>
                <div class="delete" onclick="javascript:;" paramValue="4">
                  <i class="material-icons">&#xe5cd;</i>
                </div>
              </div>
              <div class="filter_list_value" onclick="">
                <div class="text">会议论文</div>
                <div class="delete" onclick="javascript:;" paramValue="3">
                  <i class="material-icons">&#xe5cd;</i>
                </div>
              </div>
              <div class="filter_list_value" onclick="">
                <div class="text">学位论文</div>
                <div class="delete" onclick="javascript:;" paramValue="8">
                  <i class="material-icons">&#xe5cd;</i>
                </div>
              </div>
              <div class="filter_list_value" onclick="">
                <div class="text">专利</div>
                <div class="delete" onclick="javascript:;" paramValue="5">
                  <i class="material-icons">&#xe5cd;</i>
                </div>
              </div>
              <div class="filter_list_value" onclick="">
                <div class="text">其他</div>
                <div class="delete" onclick="javascript:;" paramValue="7,1,2,10">
                  <i class="material-icons">&#xe5cd;</i>
                </div>
              </div>
            </div>
          </div>
          <div class="filter_list">
            <div class="filter_list_title">
              <s:text name='group.pubs.screening.include' />
            </div>
            <div id="screen_records" class="filter_list_content paramAttr" paramName="screenRecords">
              <!----------------------------------------筛选-收录  ------------------------------------------->
              <div class="filter_list_value" onclick="">
                <div class="text">EI</div>
                <div class="delete" onclick="javascript:;" paramValue="ei">
                  <i class="material-icons close-st">&#xe5cd;</i>
                </div>
              </div>
              <div class="filter_list_value" onclick="">
                <div class="text">SCI</div>
                <div class="delete" onclick="javascript:;" paramValue="sci">
                  <i class="material-icons close-st">&#xe5cd;</i>
                </div>
              </div>
              <div class="filter_list_value" onclick="">
                <div class="text">ISTP</div>
                <div class="delete" onclick="javascript:;" paramValue="istp">
                  <i class="material-icons close-st">&#xe5cd;</i>
                </div>
              </div>
              <div class="filter_list_value" onclick="">
                <div class="text">SSCI</div>
                <div class="delete" onclick="javascript:;" paramValue="ssci">
                  <i class="material-icons close-st">&#xe5cd;</i>
                </div>
              </div>
            </div>
          </div>
          <div class="filter_list">
            <div class="filter_list_title">
              <s:text name='group.pubs.order' />
            </div>
            <div class="filter_list_content">
              <div class="filter_list_value">
                <div class="text" id="defSort" for="test1" onclick="Group.clickPubSort('',this)">最新发表</div>
                <!-- <input class="with-gap" name="group1"
								value="" checked="checked" type="radio" id="test1" /> -->
              </div>
              <div class="filter_list_value">
                <div class="text" id="yearSort" for="test2" onclick="Group.clickPubSort('publishYear',this)">发表年份</div>
                <!-- <input class="with-gap" name="group1" value="" type="radio"
								id="test2"> -->
              </div>
              <div class="filter_list_value">
                <div class="text" id="citedSort" for="test3" onclick="Group.clickPubSort('citedTimes',this)">引用次数</div>
                <!-- <input class="with-gap" name="group1"
								value="" type="radio" id="test3"> -->
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="publication_list_container" id="group_pub_list">
        <!--------------------------------------------------- 成果列表 ------------------------------------------------------------>
        <s:include value="/WEB-INF/jsp/groupmodule/groupPubList.jsp"></s:include>
      </div>
    </div>
  </div>
  <div class="group_r fr">
    <div class="rs_whole" id="grouppubmembers">
      <!-------------------------------------------------- 成果成员列表---------------------------------------------------------->
    </div>
    <div class="rt_box mt10" style="display: none">
      <!-- 匹配项目成果列表 -->
      <div class="r_title1">
        <h2>项目成果匹配</h2>
      </div>
      <div class="eft_match">
        <ul>
          <li><a href="#" class="eft-l">
              <p class="p-title">Blocking Chenli Ye, Zhenghong ZhangChenli Ye, Zhenghong Zhang.</p>
              <p class="p-author">Therapeutics.2016 Jun;349(3):437-43.</p>
          </a> <a href="#" class="import_btn">导入</a></li>
          <li><a href="#" class="eft-l">
              <p class="p-title">Blocking Chenli Ye, Zhenghong ZhangChenli Ye, Zhenghong Zhang.</p>
              <p class="p-author">Therapeutics.2016 Jun;349(3):437-43.</p>
          </a> <a href="#" class="import_btn">导入</a></li>
          <li><a href="#" class="eft-l">
              <p class="p-title">Blocking Chenli Ye, Zhenghong ZhangChenli Ye, Zhenghong Zhang.</p>
              <p class="p-author">Therapeutics.2016 Jun;349(3):437-43.</p>
          </a> <a href="#" class="import_btn">导入</a></li>
          <li><a href="#" class="eft-l">
              <p class="p-title">Blocking Chenli Ye, Zhenghong ZhangChenli Ye, Zhenghong Zhang.</p>
              <p class="p-author">Therapeutics.2016 Jun;349(3):437-43.</p>
          </a> <a href="#" class="import_btn">导入</a></li>
          <li><a href="#" class="eft-l">
              <p class="p-title">Blocking Chenli Ye, Zhenghong ZhangChenli Ye, Zhenghong Zhang.</p>
              <p class="p-author">Therapeutics.2016 Jun;349(3):437-43.</p>
          </a> <a href="#" class="import_btn">导入</a></li>
        </ul>
        <a class="waves-effect waves-light button03 w358 invite_other_btn">导入所有匹配成果</a>
      </div>
    </div>
  </div>
  <div class="clear_h40"></div>
</div>