package com.smate.web.dyn.service.group;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.smate.web.dyn.dao.group.GroupInvitePsnDao;
import com.smate.web.dyn.dao.grp.GrpMemberDao;
import com.smate.web.dyn.model.group.GroupInvitePsn;

/**
 * 群组操作服务类
 * 
 * @author zk
 *
 */
@Service("groupOptService")
@Transactional(rollbackOn = Exception.class)
public class GroupOptServiceImpl implements GroupOptService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Resource
  private GroupInvitePsnDao groupInvitePsnDao;
  @Resource
  private GrpMemberDao grpMemberDao;

  /**
   * 获取人与群组的关系
   * 
   * 
   * 群组角色状态 0 陌生人 没有申请 ， 1 ，陌生人 已经申请 ， 2 是群组普通成员 3 群组管理成员 4.群主
   */

  @Override
  public Integer getRelationWithGroup(Long psnId, Long groupId) throws Exception {
    Integer oaInteger = new Integer(0);
    GroupInvitePsn groupInvitePsn = groupInvitePsnDao.getGroupInvitePsn(groupId, psnId);
    if (groupInvitePsn == null) {
      oaInteger = 0;
    } else if ("1".equals(groupInvitePsn.getIsAccept())) {
      if ("1".equals(groupInvitePsn.getGroupRole())) {
        oaInteger = 4;
      } else if ("2".equals(groupInvitePsn.getGroupRole())) {
        oaInteger = 3;
      } else {
        oaInteger = 2;
      }
    } else if (StringUtils.isBlank(groupInvitePsn.getIsAccept())) {
      oaInteger = 1;
    } else {
      oaInteger = 0;
    }
    return oaInteger;
  }

  @Override
  public Integer getRelationWithGrp(Long psnId, Long groupId) throws Exception {
    return grpMemberDao.getRoleById(psnId, groupId);
  }


}
