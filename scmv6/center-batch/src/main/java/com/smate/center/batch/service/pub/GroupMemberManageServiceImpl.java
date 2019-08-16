package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.constant.GroupMemberContants;
import com.smate.center.batch.dao.sns.friend.GroupInviteDao;
import com.smate.center.batch.dao.sns.prj.GroupInvitePsnDao;
import com.smate.center.batch.dao.sns.pub.GroupInvitePsnNodeDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.prj.GroupInvitePsn;
import com.smate.center.batch.model.sns.pub.GroupInvite;
import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.service.pub.mq.MessageService;
import com.smate.center.batch.service.pub.mq.SnsSyncGroupProducer;
import com.smate.center.batch.service.pub.mq.SnsSyncGroupStatisticsProducer;
import com.smate.center.batch.util.pub.BasicRmtSrvModuleConstants;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 群组人员管理(增删改)的业务逻辑实现类.
 * 
 * @author maojianguo
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Service("groupMemberManageService")
public class GroupMemberManageServiceImpl implements GroupMemberManageService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupPsnSearchService groupPsnSearchService;
  @Autowired
  private GroupInvitePsnDao groupInvitePsnDao;
  @Resource(name = "snsSyncGroupProducer")
  private SnsSyncGroupProducer snsSyncGroupProducer;
  @Resource(name = "syncGroupStatisticsProducer")
  private SnsSyncGroupStatisticsProducer syncGroupStatisticsProducer;
  @Autowired
  private GroupInviteDao groupInviteDao;
  @Autowired
  private GroupInvitePsnNodeDao groupInvitePsnNodeDao;
  @Autowired
  private MessageService messageService;

  /**
   * 同步群组信息到群组成员.
   * 
   * @param groupId
   * @param groupInvitePsnList
   * @throws ServiceException
   */
  public void toSendSyncGroupInvitePsn(Long groupId, List<GroupInvitePsn> groupInvitePsnList) throws ServiceException {

    try {
      GroupPsn groupPsn = groupPsnSearchService.getGroupPsn(groupId);// groupPsnDao.findMyGroup(groupId);
      groupPsn.setGroupNodeId(BasicRmtSrvModuleConstants.SNS_MODULE_ID.intValue());

      List<Long> psnList = new ArrayList<Long>();// 群组成员psnId
      if (groupInvitePsnList == null) {
        psnList = groupInvitePsnDao.findGroupMemberPsnIdByGroupId(groupId);
      } else {
        for (GroupInvitePsn groupInvitePsn : groupInvitePsnList) {
          psnList.add(groupInvitePsn.getPsnId());
        }
      }

      snsSyncGroupProducer.syncGroupInvitePsnToSns(groupPsn, psnList);// 把群组信息更新到所有节点

      // 群组信息和成员有变动，同步到ROL(合作分析)
      List<Long> memberPsnIdList = groupInvitePsnDao.findGroupMemberPsnIdByGroupId(groupPsn.getGroupId());
      syncGroupStatisticsProducer.syncForAllGroupUpdateToRol(groupPsn, memberPsnIdList);

    } catch (Exception e) {
      logger.error("同步群组信息到群组成员出错", e);
      throw new ServiceException(e);
    }

  }


  @Override
  public String toApprove(Map<String, Object> ctxMap) throws ServiceException {
    try {
      Long groupInviteId = Long.valueOf(ctxMap.get("inviteId").toString());
      GroupInvite groupInvite = groupInviteDao.get(groupInviteId);
      if (groupInvite == null) {// 群组邀请已经处理
        return "resolved";
      }

      GroupInvitePsn checkGroupInvitePsn =
          groupInvitePsnDao.findGroupMember(groupInvite.getGroupId(), groupInvite.getPsnId());
      // status = '01' and isAccept='1'
      if (checkGroupInvitePsn != null) {
        if (GroupMemberContants.GROUP_MEMBER_STATUS_NORMAL.equals(checkGroupInvitePsn.getStatus())
            && GroupMemberContants.ACCEPT.equals(checkGroupInvitePsn.getIsAccept())) {// 已经是群组成员
          return "exist";
        } else if (GroupMemberContants.GROUP_MEMBER_STATUS_DELLET.equals(checkGroupInvitePsn.getStatus())) {// 邀请已经被删除
          return "delete";
        }
      }
      // 当好友接受，群主的邀请时，改变isAccept状态为1
      groupInvitePsnNodeDao.updateGroupInvitePsnNode(groupInvite.getGroupId(), groupInvite.getPsnId());
      GroupPsn groupPsn = groupPsnSearchService.getGroupBaseInfo(groupInvite.getGroupId());
      if (groupPsn == null) {// 如果群组记录为空则返回empty.
        groupInviteDao.delete(groupInvite);
        return "empty";
      }
      GroupInvitePsn groupInvitePsn =
          groupInvitePsnDao.findGroupMember(groupInvite.getGroupId(), groupInvite.getPsnId());

      if (groupInvitePsn == null) {
        groupInvitePsn =
            this.toJoinInvite(groupInvite.getGroupId(), groupInvite.getPsnId(), GroupMemberContants.ACCEPT);
      }
      // 表明是以 被邀请 的方式加入群组
      groupPsn.setJoinInGroupStyle("invited");
      groupPsn.setExeIsJoinInGroupPsnId(groupInvite.getSendPsnId());
      Map<String, Object> inviteMap = new HashMap<String, Object>();
      inviteMap.put("inviteId", groupInvitePsn.getInvitePsnId());
      inviteMap.put("casUrl", ctxMap.get("casUrl"));
      this.toJoinApprove(inviteMap, groupPsn);

      // 发送站内短消息通知
      HashMap<String, String> map = new HashMap<String, String>();
      map.put("inviteId", groupInviteId.toString());
      messageService.sendConfirmMsgForAttendGroup(map);

      // tsz
      // 同意群组邀请，不清除 邀请信息，TSZ_2014-06-30_SCM-5380
      // groupInviteDao.delete(groupInvite);

      return "success";

    } catch (Exception e) {
      logger.error("接受加入群组操作失败!", e);
      throw new ServiceException(e);
      // return "failure";

    }

    //
  }

  @Override
  public GroupInvitePsn toJoinInvite(Long groupId, Long userId, String isAccept) throws ServiceException {
    // TODO
    return null;
  }

  /**
   * 以邀请的方式将人员加为群组成员.
   * 
   * @param invitePsnId 被操作人员.
   * @param groupPsn 群组记录.
   * @throws ServiceException
   */
  @Override
  public void toJoinApprove(Long invitePsnId, GroupPsn groupPsn) throws ServiceException {
    // TODO
  }

  /**
   * 批准加为群组成员.
   * 
   * @param params
   * @param groupPsn
   * @throws ServiceException
   */
  public void toJoinApprove(Map<String, Object> params, GroupPsn groupPsn) throws ServiceException {
    // TODO
  }

  @Override
  public String toRefuse(Long groupInviteId, Integer groupNodeId) throws ServiceException {

    try {
      String result = "success";
      GroupInvite groupInvite = groupInviteDao.get(groupInviteId);
      GroupPsn groupPsnInfo = groupPsnSearchService.getGroupBaseInfo(groupInvite.getGroupId());
      if (groupPsnInfo == null) {// 如果群组记录为空则返回empty.
        groupInviteDao.delete(groupInvite);
        return "empty";
      }
      GroupInvitePsn groupInvitePsn =
          groupInvitePsnDao.findGroupMember(groupInvite.getGroupId(), groupInvite.getPsnId());
      if (groupInvitePsn == null) {
        groupInvitePsn =
            this.toJoinInvite(groupInvite.getGroupId(), groupInvite.getPsnId(), GroupMemberContants.NOT_ACCEPT);
      }
      if (GroupMemberContants.ACCEPT.equals(groupInvitePsn.getIsAccept())
          && GroupMemberContants.GROUP_MEMBER_STATUS_NORMAL.equals(groupInvitePsn.getStatus())) {// 已经加入群组，点邮箱拒绝按钮，不退出群组
        result = "exist";
      } else if (GroupMemberContants.GROUP_MEMBER_STATUS_DELLET.equals(groupInvitePsn.getStatus())) {// 邀请已经被删除
        return "delete";
      } else {
        GroupPsn groupPsn = new GroupPsn();
        groupPsn.setGroupId(groupInvitePsn.getGroupId());
        groupPsn.setGroupNodeId(SecurityUtils.getCurrentUserNodeId());
        groupPsn.setJoinInGroupStyle("invited");
        groupPsn.setExeIsJoinInGroupPsnId(groupInvite.getSendPsnId());
        // pwl-20130924-需求SCM-3689-操作后不需要发提醒消息及邮件给被忽略的对象
        this.toJoinRefuse(groupInvitePsn.getInvitePsnId(), groupNodeId, false, groupPsn);
      }
      // tsz
      // 忽略群组邀请，不清除 邀请信息，TSZ_2014-06-30_SCM-5380
      // groupInviteDao.delete(groupInvite);
      return result;
    } catch (Exception e) {

      logger.error("拒绝加入群组操作失败!", e);
      return "failure";
    }
  }

  /**
   * 拒绝待批准成员加入群组.
   * 
   * @param invitePsnId
   * @param groupNodeId
   * @param isSysMsg
   * @param groupPsn
   * @throws ServiceException
   */
  public void toJoinRefuse(Long invitePsnId, Integer groupNodeId, boolean isSysMsg, GroupPsn groupPsn)
      throws ServiceException {
    // TODO
  }


  @Override
  public String dealToJoinApprove(Map map) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public String toJoinRefuseApply(Long invitePsnId, Integer groupNodeId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

}
