package com.smate.center.oauth.service.profile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.oauth.dao.profile.PsnDisciplineDao;
import com.smate.center.oauth.dao.profile.PsnDisciplineKeyDao;
import com.smate.center.oauth.exception.OauthException;
import com.smate.core.base.psn.dao.PersonProfileDao;

/**
 * 个人专长、研究领域服务接口实现
 * 
 * @author Administrator
 *
 */
@Service("personalManager")
@Transactional(rollbackFor = Exception.class)
public class PersonalManagerImpl implements PersonalManager {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnDisciplineDao psnDisciplineDao;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;

  @Override
  public boolean isPsnDiscExit(Long psnId) throws OauthException {

    try {
      return psnDisciplineKeyDao.countPsnDisciplineKey(psnId) > 0l;
    } catch (OauthException e) {
      logger.error("获取刷新用户信息完整度的数据", e);
      throw new OauthException(e);
    }
  }

}
