package com.smate.web.group.service.grp.member;

import java.util.List;

import com.smate.web.group.action.grp.form.GrpDiscussShowMemberInfo;
import com.smate.web.group.action.grp.form.GrpMainForm;
import com.smate.web.group.action.grp.form.GrpMemberForm;
import com.smate.web.group.model.group.psn.PsnInfo;
import com.smate.web.group.model.grp.member.GrpMember;
import com.smate.web.group.model.grp.member.GrpMemberRcmd;
import com.smate.web.group.model.grp.member.GrpProposer;

/**
 * 群组成员显示service
 * 
 * @author zzx
 */
public interface GrpMemberShowService {
  /**
   * 获取群组成员列表
   * 
   * @param form
   * @return
   */
  void getGrpMemberListForShow(GrpMemberForm form);

  void getChatPsnCard(GrpMemberForm form);

  /**
   * 获取群组成员成果统计 列表
   * 
   * @param form
   * @return
   */
  void getGrpMemberPubSum(GrpMemberForm form);

  /**
   * 获取群组申请人员列表
   * 
   * @param form
   * @return
   */
  void getGrpProposerListForShow(GrpMemberForm form);

  /**
   * 获取群组推荐成员列表
   * 
   * @param form
   * @return
   */
  void getGrpRecommendListForShow(GrpMemberForm form);

  /**
   * 构造群组成员显示信息
   * 
   * @param form
   * @return
   */
  void buildGrpMemberShowInfo(GrpMember grpMember, GrpMemberForm form);

  /**
   * 构造群组申请人员显示信息
   * 
   * @param form
   * @return
   */
  void buildGrpProposerShowInfo(GrpProposer grpProposer, GrpMemberForm form);

  /**
   * 构造群组推荐成员显示信息
   * 
   * @param form
   * @return
   */
  void buildGrpRecommendShowInfo(GrpMemberRcmd grpRecommend, GrpMemberForm form);

  /**
   * 排序获取所有的群组id两个 list 合并
   * 
   * @param form
   */
  List<Long> getMyGrpIds(GrpMainForm form);

  /**
   * 群组讨论获取五个成员
   * 
   * @return
   */
  List<GrpDiscussShowMemberInfo> getFiveMemberForGrpDiscuss(Long grpId, Long ownerPsnId) throws Exception;

  /**
   * 获取好友人员列表
   * 
   * @param form
   * @return
   */
  void getFriendListForShow(GrpMemberForm form) throws Exception;

  void findGrpPubPsnList(GrpMemberForm form) throws Exception;

  /**
   * 查询所有群组成员
   * 
   * @param grpId
   * @return
   * @throws Exception
   */
  List<PsnInfo> queryGrpMembers(GrpMemberForm form) throws Exception;

}
