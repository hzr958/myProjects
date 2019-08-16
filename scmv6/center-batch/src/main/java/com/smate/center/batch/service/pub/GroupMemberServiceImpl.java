package com.smate.center.batch.service.pub;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.prj.GroupInvitePsn;
import com.smate.center.batch.model.sns.prj.GroupMember;
import com.smate.center.batch.model.sns.pub.PersonEmail;
import com.smate.core.base.utils.model.Page;


/**
 * 群组成员相关信息检索的业务逻辑实现类.
 * 
 * @author maojianguo
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Service("groupMemberService")
public class GroupMemberServiceImpl implements GroupMemberService {

  @Override
  public Page<GroupInvitePsn> findGroupMember(Long groupId, Page<GroupInvitePsn> page, String searchKey)
      throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<GroupInvitePsn> findGroupMemberByAll(Long groupId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<GroupInvitePsn> findGroupMember(Long groupId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Map<String, String>> getInvitePsnList(List<Map<String, String>> inviteList,
      Map<String, Object> configParam) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public GroupInvitePsn copyGroupMemberDetail(GroupInvitePsn gip) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public GroupMember findGroupMemberDetail(Long psnId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Integer> findAdminNodeListByGroupId(Long groupId) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isPsnEmailVerified(Long psnId, String email) throws ServiceException {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public List<PersonEmail> findListByEmail(String email) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Map<String, String>> getMatchedUser(Map<String, Object> paramInfo) throws DaoException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<Long> findGroupMemberPsnId(List<GroupInvitePsn> groupMemberList) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public List<GroupInvitePsn> groupMemberPage(List<Long> groupMemberPsnIdList,
      List<GroupInvitePsn> tempGroupInvitePsnList) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public GroupInvitePsn getGroupInvitePsn(Long psnId, Long groupId) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isGroupManager(Long psnId, Long groupId) {
    // TODO Auto-generated method stub
    return false;
  }

}
