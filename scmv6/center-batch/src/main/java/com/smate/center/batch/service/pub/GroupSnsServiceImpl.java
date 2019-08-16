package com.smate.center.batch.service.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.GroupInvitePsnFriendDao;
import com.smate.center.batch.dao.sns.pub.GroupInvitePsnNodeDao;
import com.smate.center.batch.dao.sns.pub.GroupPsnNodeDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.GroupInvitePsnNode;
import com.smate.center.batch.model.sns.pub.GroupPsn;
import com.smate.center.batch.model.sns.pub.GroupPsnNode;

/**
 * 群组同步接口实现类.
 * 
 * @author zhuagnyanming
 * 
 */
@Service("groupSnsService")
@Transactional(rollbackFor = Exception.class)
public class GroupSnsServiceImpl implements GroupSnsService {

  /**
   * 
   */
  private static final long serialVersionUID = -4556456444742879030L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GroupInvitePsnNodeDao groupInvitePsnNodeDao;
  @Autowired
  private GroupInvitePsnFriendDao groupInvitePsnFriendDao;
  @Autowired
  private GroupPsnNodeDao groupPsnNodeDao;



  // 同步群组信息到所有节点
  @Override
  public void syncGroupInvitePsn(GroupPsn groupPsn, List<Long> psnList) throws ServiceException {
    try {
      List<GroupInvitePsnNode> gipnList = groupInvitePsnNodeDao.findGroupInvitePsnNodeList(groupPsn.getGroupId());
      for (GroupInvitePsnNode groupInvitePsnNode : gipnList) {// 同步冗余字段
        groupInvitePsnNode.setSumFiles(groupPsn.getSumFiles());
        groupInvitePsnNode.setSumPubs(groupPsn.getSumPubs());
        groupInvitePsnNode.setSumPrjs(groupPsn.getSumPrjs());
        groupInvitePsnNode.setSumMembers(groupPsn.getSumMembers());
        groupInvitePsnNode.setSumToMembers(groupPsn.getSumToMembers());
        groupInvitePsnNode.setSumRefs(groupPsn.getSumRefs());
        groupInvitePsnNode.setSumSubjects(groupPsn.getSumSubjects());
        groupInvitePsnNode.setSumWorks(groupPsn.getSumWorks());
        groupInvitePsnNode.setSumMaterials(groupPsn.getSumMaterials());
        groupInvitePsnNode.setCreateDate(groupPsn.getCreateDate());
        groupInvitePsnNodeDao.save(groupInvitePsnNode);
      }

      for (Long psnId : psnList) {
        groupInvitePsnFriendDao.deleteGroupInvitePsnFriendByPsnId(psnId);
      }

      this.syncGroupPsnMember(groupPsn);// 群组同步群组人员统计信息到GroupPsnNode
    } catch (Exception e) {
      logger.error("同步群组成员出错", e);
      throw new ServiceException(e);
    }

  }


  // 群组同步群组人员统计信息到GroupPsnNode
  private void syncGroupPsnMember(GroupPsn groupPsn) throws ServiceException {

    try {
      GroupPsnNode groupPsnNode = groupPsnNodeDao.findGroupPnsNode(groupPsn.getGroupId());
      if (groupPsnNode != null) {
        groupPsnNode.setSumMembers(groupPsn.getSumMembers());
        groupPsnNodeDao.save(groupPsnNode);
      } else {
        logger.error(String.format("GroupPsnNode群组数据不完整，检查群组groupId=%s", groupPsn.getGroupId()));
      }

    } catch (Exception e) {
      logger.error("群组同步群组人员统计信息到GroupPsnNode出错", e);
      throw new ServiceException(e);
    }

  }
}
