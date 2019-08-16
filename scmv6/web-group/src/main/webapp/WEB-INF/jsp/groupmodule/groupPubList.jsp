<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	$(document).ready(function(){
		//分页主入口
		TagesMain.main(doLoad);
		//加载排序选择的结果
		loadPubSort();
		$("#_showPubImport").thickbox({});
		var sumPubCount=$("#totalCount").val();
		if(sumPubCount ==null||typeof sumPubCount == "undefined"||sumPubCount==""){
			sumPubCount=0;
		}
		if (typeof(sumPubCount) != 'undefined'&&$.trim($("#input_default01").val())==""&&$("#screen_box").find(".hover").size()==0) { 
			//$("#group_sumPub").html($("#group_sumPub").html().replace(/\d+/g,sumPubCount));
		}
		 //全文下载
		$(".notPrintLinkSpan").fullTextRequest();
		$("#screen_box").click(function(event){
		    event.stopPropagation();

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
	});
	//分页执行的函数
	function doLoad(){
		Group.loadGroupPubs(Group.getGroupParam());
	}
	//加载排序选择的结果
	function loadPubSort(){
		var order = $("#_orderBy").val();
		$("#yearSort,#citedSort,#defSort").parent().addClass("existed");
		if(order=="publishYear"){
			$("#yearSort").parent().addClass("active");
			$("#citedSort,#defSort").parent().removeClass("active");
		}else if(order=="citedTimes"){
			$("#citedSort").parent().addClass("active");
			$("#yearSort,#defSort").parent().removeClass("active");
		}else if(order==""){
			$("#defSort").parent().addClass("active");
			$("#yearSort,#citedSort").parent().removeClass("active");
		}
	}
</script>
<div class="both-center" style="width: 100%; height: 240px; display: none;">
  <div class="preloader active" style="height: 28px; width: 28px;"></div>
</div>
<s:if test="page.totalCount > 0">
  <table border="0" cellpadding="0" cellspacing="0" class="tab_lt" id="groupPub_table">
    <s:include value="groupPubImport.jsp" />
    <!-------------------------------- 隐藏数据start------------------------------------------------------- -->
    <input type="hidden" id="_searchKey" name="searchKey" value="<c:out value="${searchKey}"/>" />
    <input type="hidden" id="_orderBy" name="orderBy" value="<c:out value="${page.orderBy}"/>" />
    <!-------------------------------- 隐藏数据end------------------------------------------------------- -->
    <s:iterator value="page.result" id="result" status="itStat">
      <tr id="tr${pubId}">
        <!-------------------------------tr- 隐藏数据start------------------------------------------------------- -->
        <input type="hidden" class="pub_nodeId_class" value="${nodeId}" />
        <input type="hidden" class="pub_groupId_class" value="${des3GroupId}" />
        <input type="hidden" class="group_des3PubsId_class" value="<iris:des3 code='${groupPubsId}'/>" />
        <input type="hidden" class="pub_pubId_class" value="${pubId}" />
        <input type="hidden" class="pub_des3PsnId_class" value="<iris:des3 code='${psnId}'/>" />
        <input type="hidden" class="pub_ownerId_class" value="<iris:des3 code='${ownerPsnId}'/>" />
        <input type="hidden" class="inputpubId" value="<iris:des3 code='${pubId}'/>" />
        <input type="hidden" class="inputpubId_nodes" value="${pubId}" />
        <input type="hidden" class="inputnodeId" value="${nodeId}" />
        <input type="hidden" class="pubType" value="${typeId }" />
        <!-------------------------------- 隐藏数据end------------------------------------------------------- -->
        <td valign="top" width="100"><c:if test="${fulltextExt !=null}">
            <s:if test="fileTypeIcoUrl !=null">
              <a href="javascript:;" class="file_bg ml10 notPrintLinkSpan" groupid="${groupId}" resid="${pubId}"
                resnode="${fullTextNodeId}" restype="1"><img src="${fileTypeIcoUrl }" width="72px" height="88px"></img></a>
            </s:if>
            <s:else>
              <a href="javascript:;" class="file_bg ml10 notPrintLinkSpan" groupid="${groupId}" resid="${pubId}"
                resnode="${fullTextNodeId}" restype="1"><img src="${resscmsns }/images_v5/images2016/file_img1.jpg"></img></a>
            </s:else>
          </c:if> <c:if test="${fulltextExt ==null}">
            <a href="javascript:;" style="cursor: default" class="file_bg ml10 "><img
              src="${resscmsns }/images_v5/images2016/file_img.jpg"></a>
          </c:if></td>
        <td align="left">
          <div class="">
            <p class="blue" onclick="">
              <a class="blue"
                href="javascript:Group.groupPubDetails('<iris:des3 code="${pubId}"/>','${des3GroupId}','<iris:des3 code="${ownerPsnId}"/>');"
                style="text-decoration: none;"> <s:if test="#locale=='zh_CN'">
                  <s:if test="zhTitle==null || zhTitle==''">${enTitle }</s:if>
                  <s:else>${zhTitle }</s:else>
                </s:if> <s:else>
                  <s:if test="enTitle==null || enTitle==''">${zhTitle }</s:if>
                  <s:else>${enTitle }</s:else>
                </s:else>
              </a>
            </p>
            <p class="f333">${authorNamesForShow }</p>
            <p class="f999">${briefDesc}</p>
            <div class="screen_mn_l">
              <c:if
                test="${currentPsnGroupRoleStatus == 2||currentPsnGroupRoleStatus == 3||currentPsnGroupRoleStatus == 4}">
                <a href="javascript:Group.editGroupPub('tr${pubId}');" class="paper_mn_btn"><i
                  class="material-icons edit">&#xe3c9;</i></a>
                <a href="javascript:Group.delGroupPub('tr${pubId}');" class="paper_mn_btn"><i
                  class="material-icons delete">&#xe872;</i></a>
              </c:if>
            </div>
          </div>
        </td>
        <td width="60" align="center"><c:if test="${relevance == 1}">
            <i class="relevance03"></i>
          </c:if> <c:if test="${relevance == 2}">
            <i class="relevance02"></i>
          </c:if> <c:if test="${relevance >= 3}">
            <i class="relevance01"></i>
          </c:if> <c:if test="${relevance == null || relevance == 0}">
            <i class="relevance04"></i>
          </c:if> <c:if test="${labeled !=null&&labeled ==1}">
            <i class="mark_icon01"></i>
          </c:if></td>
      </tr>
    </s:iterator>
  </table>
  <!------------------------------------------------------- 分页------------------------------------------------>
  <s:include value="/common/group-page-tages.jsp"></s:include>
</s:if>
<s:else>
  <div class="both-center" style="width: 100%; height: 240px;">未找到相关记录</div>
  <s:include value="groupPubImport.jsp" />
</s:else>
