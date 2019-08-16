package com.smate.center.task.service.sns.quartz;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.exception.ServiceException;
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


}
