package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * 群组-管理员邀请人员加入群组的业务逻辑实现类.
 * 
 * @author MaoJianGuo.
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Service("groupAdminInviteService")
public class GroupAdminInviteServiceImpl implements GroupAdminInviteService {

  @Override
  public boolean isPsnEmailVerified(Long psnId, String email)
      throws com.smate.center.batch.exception.pub.ServiceException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public List<com.smate.center.batch.model.sns.pub.PersonEmail> findListByEmail(String email)
      throws com.smate.center.batch.exception.pub.ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Map<String, Object>> findGroupAdminsInfo(Long groupId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void toSendGroupInvite(List<Map<String, String>> tmpList,
      com.smate.center.batch.model.sns.pub.GroupPsn groupPsn, Map<String, Object> map)
      throws com.smate.center.batch.exception.pub.ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public Integer dealGroupInvite(com.smate.center.batch.model.mail.InviteMailBox mailBox,
      com.smate.center.batch.model.mail.InviteInbox inbox, Long currentPsnId, Long inviteId, Integer nodeId)
      throws com.smate.center.batch.exception.pub.ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void toSendGroupJoinMail(List<Map<String, String>> tmpList,
      com.smate.center.batch.model.sns.pub.GroupPsn groupPsn, String casUrl)
      throws com.smate.center.batch.exception.pub.ServiceException {
    // TODO Auto-generated method stub

  }
}
