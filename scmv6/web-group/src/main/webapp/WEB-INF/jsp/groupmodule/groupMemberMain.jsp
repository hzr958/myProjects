<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript" src="${ressns}/js/group/groupMembers_${locale}.js"></script>
<script type="text/javascript">

var currentPsnGroupRoleStatus='${currentPsnGroupRoleStatus}';
var Group = Group  ?Group :{} ;
Group.applyMember = function(){
	$("#submodule").val("apply");
	Group.changeUrl(null ,"apply");
	Group.groupPendingListening();
	Group.groupPendingList();
	setTab('two',2,2)
}

	$(document).ready(function(){
		if ((currentPsnGroupRoleStatus==3 ||currentPsnGroupRoleStatus==4)&&($("#targetToPending").val() == "targetToPending"  ||  $("#submodule").val()=="apply")) {
			$("#targetToPending").val("");
			Group.applyMember()
			//$("#two2").click();
		} else {
		Group.groupMemberList();
		}
		//群组成员右侧-邀请成员加入   判断权限
		if(currentPsnGroupRoleStatus==2 || currentPsnGroupRoleStatus==3 || currentPsnGroupRoleStatus==4){
			Group.inviteMemberJion();
		}
		Group.groupMemberListening();//群组成员列表搜索框
		//群组成员右侧-邀请其他成员(弹出框) 
		//邀请联系人改造，注释 
		/*  $("#invite_member_thickbox").thickbox({
			parameters : {
				"groupPsn.des3GroupId" : $("#des3GroupId").val(),
				"groupPsn.groupNodeId" : 1,
				"newGroupMemberInvite" : "true" //该参数用来识别是从当前页面调弹出框
			},
			type : "groupMemInvite"
		});  */
	
	});
	$("#two1").click(function(){
		$("#submodule").val("member")
		Group.changeUrl(null ,"member");
		Group.groupMemberList();
	})
	$("#two2").click(function(){
		Group.applyMember();
	})
</script>
<input id="currentPsnGroupRole" type="hidden" value="${currentPsnGroupRole}" />
<div class="group_box">
  <input id="applyMemberCount" type="hidden" value="${page.totalCount}" />
  <div class="group_l fl">
    <div class="lt_box pa10">
      <div id="member_tab">
        <div class="menubox">
          <ul>
            <s:if test="page.totalCount>0 ">
              <li id="two1" onClick="setTab('two',1,2)" class="hover"><s:text name="group.tab.member"></s:text></li>
              <li id="two2" onClick="setTab('two',2,2)" style="display: none"><s:text name="group.member.apply"></s:text><span
                class="num_tip"></span></li>
            </s:if>
            <s:else>
              <li id="two1" onClick="setTab('two',1,1)" class="hover"><s:text name="group.tab.member"></s:text></li>
            </s:else>
          </ul>
          <%-- <a href="javascript:;" class="jion_time"><s:text name="group.member.jionTime"/><i class="time_down"></i><!--<i class="time_up"></i>--></a> --%>
        </div>
        <div class="mber_contbox">
          <div id="con_two_1" class="hover">
            <div class="m_s_box mt20">
              <input type="text" placeholder="<s:text name='group.member.findMember'/>" class="input_default"
                id="memberSearch"> <input id="member_search_btn" type="button" class="s_btn_small">
            </div>
          </div>
          <div id="con_two_2" style="display: none">
            <div class="m_s_box mt20">
              <input type="text" placeholder="<s:text name='group.member.findMember'/>" class="input_default"
                id="pendingSearch"> <input id="pending_search_btn" type="button" class="s_btn_small">
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <c:if test="${currentPsnGroupRoleStatus==2 || currentPsnGroupRoleStatus==3 || currentPsnGroupRoleStatus==4}">
    <div class="group_r fr" id="group_r">
      <div class="rt_box">
        <div class="r_title1">
          <h2>
            <s:text name="group.member.inviteJion" />
          </h2>
        </div>
        <div class="invite_mber">
          <ul id="invite_member">
          </ul>
          <a id="invite_member_thickbox" onclick="Group.selectFriendsForGroup()"
            class="waves-effect waves-light button03 w358 invite_other_btn"><s:text
              name="group.member.inviteOtherJion" /></a>
        </div>
      </div>
    </div>
  </c:if>
  <div class="clear_h40"></div>
</div>