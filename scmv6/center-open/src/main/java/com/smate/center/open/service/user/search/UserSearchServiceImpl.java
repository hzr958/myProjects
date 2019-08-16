package com.smate.center.open.service.user.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.center.open.dao.usersearch.UserSearchDao;
import com.smate.core.base.consts.model.Institution;

/**
 * 检索用户服务实现
 * 
 * @author wsn
 *
 */
@Service("userSearchService")
@Transactional(rollbackFor = Exception.class)
public class UserSearchServiceImpl implements UserSearchService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserSearchDao userSearchDao;

  /**
   * 更新保存用户检索表的单位记录.
   * 
   * @param psnId
   * @param ins
   */
  public void updateUserIns(Long psnId, Institution ins) {
    this.userSearchDao.updateUserIns(psnId, ins);
  }
}
