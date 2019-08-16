package com.smate.center.batch.service.psn;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.dao.security.PsnPrivateDao;

@Service("psnPrivateService")
@Transactional(rollbackFor = Exception.class)
public class PsnPrivateServiceImpl implements PsnPrivateService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnPrivateDao psnPrivateDao;

  @Override
  public boolean isPsnPrivate(Long psnId) throws ServiceException {
    try {
      Long count = psnPrivateDao.isPsnPrivate(psnId);
      if (count != null && count.longValue() > 0) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      logger.error("判断是否隐私人员出错,pnsId=" + psnId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 在一组人员中查找出隐私出人员
   * 
   * @author zk
   */
  @Override
  public List<Long> isPsnPrivate(List<Long> psnIds) throws ServiceException {
    return psnPrivateDao.isPsnPrivate(psnIds);
  }

}
