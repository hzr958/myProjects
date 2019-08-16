package com.smate.center.batch.service.psn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.PsnJnlRefreshDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.PsnJnlRefresh;

/**
 * 个人成果、文献期刊刷新记录.
 * 
 * @author WeiLong Peng
 * 
 */
@Service("psnJnlRefreshService")
@Transactional(rollbackFor = Exception.class)
public class PsnJnlRefreshServiceImpl implements PsnJnlRefreshService {

  /**
   * 
   */
  private static final long serialVersionUID = 5708164380947514757L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnJnlRefreshDao psnJnlRefreshDao;

  @Override
  public void markPsnJnlRefresh(Long pubId, Long psnId, Integer articleType, Integer isDel) throws ServiceException {
    try {
      PsnJnlRefresh refresh = psnJnlRefreshDao.queryPsnJnlRefresh(pubId, articleType);
      if (refresh == null) {
        refresh = new PsnJnlRefresh();
        refresh.setPubId(pubId);
        refresh.setArticleType(articleType);
      }
      refresh.setPsnId(psnId);
      refresh.setDel(isDel);
      refresh.setFlag(0);
      psnJnlRefreshDao.save(refresh);
    } catch (Exception e) {
      logger.error("标记个人成果、文献期刊统计刷新记录出现异常：", e);
      throw new ServiceException(e);
    }
  }

}
