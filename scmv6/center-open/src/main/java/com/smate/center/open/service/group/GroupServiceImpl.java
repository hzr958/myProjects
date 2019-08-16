package com.smate.center.open.service.group;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.dao.group.GroupBaseinfoDao;
import com.smate.center.open.dao.group.GroupMemberDao;

/**
 * 群组服务实现类
 * 
 * @author tsz
 *
 */
@Service("groupService")
@Transactional(rollbackFor = Exception.class)
public class GroupServiceImpl implements GroupService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GroupBaseinfoDao groupBaseInfoDao;
  @Autowired
  private GroupMemberDao groupMemberDao;

  /**
   * 创建groupId
   * 
   * @author lhd
   */
  @Override
  public Long findNewGroupId() throws Exception {
    Long groupId = null;
    try {
      groupId = groupBaseInfoDao.findNewGroupId();
    } catch (Exception e) {
      logger.error("获取群组Id出错", e);
    }
    return groupId;
  }


}
