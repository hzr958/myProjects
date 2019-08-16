package com.smate.center.task.single.service.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.project.dao.ProjectDao;

@Service("myProjectQueryService")
@Transactional(rollbackFor = Exception.class)
public class MyProjectQueryServiceImpl implements MyProjectQueryService {
  @Autowired
  private ProjectDao projectDao;
  // 用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人
  Integer ALLOWS_ANY = 1;
  Integer ALLOWS_FRIEND = 1 << 1;
  Integer ALLOWS_SELF = 1 << 2;

  Integer ALLOWS = ALLOWS_ANY + ALLOWS_FRIEND + ALLOWS_SELF;

  @Override
  public Long getOpenPrjSum(Long psnId) {
    return projectDao.getOpenPrjSum(psnId, ALLOWS);
  }

}
