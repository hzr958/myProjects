package com.smate.center.task.service.sns.quartz;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;

/**
 * 个人主页URL实现类.
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnProfileUrlService")
@Transactional(rollbackFor = Exception.class)
public class PsnProfileUrlServiceImpl implements PsnProfileUrlService {

  /**
   * 
   */
  private static final long serialVersionUID = -5711349376925686886L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PsnScoreService psnScoreService;

  @Override
  public String findUrl(Long psnId) throws ServiceException {
    try {
      PsnProfileUrl obj = this.psnProfileUrlDao.find(psnId);
      if (obj != null)
        return obj.getUrl();
      return null;
    } catch (Exception e) {
      logger.error("查找个人主页的URL", e);
      throw new ServiceException(e);
    }
  }

}
