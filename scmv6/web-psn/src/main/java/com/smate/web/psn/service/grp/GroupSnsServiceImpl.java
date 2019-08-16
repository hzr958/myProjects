package com.smate.web.psn.service.grp;



import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.psn.dao.group.GroupInvitePsnDao;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.friend.GroupInvitePsn;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;

/**
 * 群组资源接口实现类.
 * 
 * @author zhuagnyanming
 * 
 */
@Service("GroupSnsService")
@Transactional(rollbackFor = Exception.class)
public class GroupSnsServiceImpl implements GroupSnsService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private GroupInvitePsnDao groupInvitePsnDao;

  public void syncGroupMember(SnsPersonSyncMessage message) throws ServiceException {
    try {


      List<GroupInvitePsn> groupInvitePsnList = groupInvitePsnDao.findGroupInvitePsnByPsnId(message.getPsnId());
      for (GroupInvitePsn groupInvitePsn : groupInvitePsnList) {// 群组成员人员信息冗余
        groupInvitePsn.setAvatars(message.getAvatars());
        groupInvitePsn.setEmail(message.getEmail());
        // groupInvitePsn.setInsName(message.getInsName());
        groupInvitePsn.setTitolo(message.getTitolo());
        String psnName = message.getName();
        if (psnName == null || "".equals(psnName.trim())) {
          psnName = message.getFirstName() + " " + message.getLastName();
        }
        groupInvitePsn.setPsnName(psnName);
        groupInvitePsn.setPsnFirstName(message.getFirstName());
        groupInvitePsn.setPsnLastName(message.getLastName());
        groupInvitePsn.setMobile(message.getMobile());
        groupInvitePsn.setMsnNo(message.getMsnNo());
        groupInvitePsn.setPosition(message.getPosition());
        groupInvitePsn.setQqNo(message.getQqNo());
        groupInvitePsn.setTel(message.getTel());
        groupInvitePsn.setSkype(message.getSkype());// liangguokeng添加skype
        groupInvitePsnDao.save(groupInvitePsn);
      }

      // 群组讨论区人员信息冗余 这张表好久不用了，注释掉 2017-12-01 ajb
      /*
       * List<GroupDiscussDetail> groupDiscussDetailList = groupDiscussDetailDao
       * .findGroupDiscussDetailByCreatePsnId(message.getPsnId()); for (GroupDiscussDetail
       * groupDiscussDetail : groupDiscussDetailList) {
       * groupDiscussDetail.setInsName(message.getInsName());
       * groupDiscussDetail.setCreatePsnName(message.getName());
       * groupDiscussDetail.setAvatars(message.getAvatars());
       * groupDiscussDetailDao.save(groupDiscussDetail); }
       * 
       * List<GroupDiscussDetail> groupDiscussDetailList2 = groupDiscussDetailDao
       * .findGroupDiscussDetailByCreatePsnId(message.getPsnId()); for (GroupDiscussDetail
       * groupDiscussDetail : groupDiscussDetailList2) {
       * groupDiscussDetail.setUpdatePsnName(message.getName());
       * groupDiscussDetailDao.save(groupDiscussDetail); }
       */

    } catch (Exception e) {
      logger.error("同步人员信息到群组冗余字段出错", e);
      // throw new ServiceException(e);
    }
  }
}
