package com.smate.web.psn.service.profile;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.security.PsnPrivateDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.PsnInfoDaoException;

@Service("psnPrivateService")
@Transactional(rollbackFor = Exception.class)
public class PsnPrivateServiceImpl implements PsnPrivateService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnPrivateDao psnPrivateDao;

  @Override
  public boolean isPsnPrivate(Long psnId) throws PsnException {
    try {
      Long count = psnPrivateDao.isPsnPrivate(psnId);
      if (count != null && count.longValue() > 0) {
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      logger.error("判断是否隐私人员出错,pnsId=" + psnId, e);
      throw new PsnException(e);
    }
  }

  /**
   * 在一组人员中查找出隐私出人员
   * 
   * @author zk
   */
  @Override
  public List<Long> isPsnPrivate(List<Long> psnIds) throws PsnInfoDaoException {
    try {
      return psnPrivateDao.isPsnPrivate(psnIds);
    } catch (PsnInfoDaoException e) {
      logger.error("查找隐私人员出错,pnsIds=" + psnIds, e);
      throw new PsnInfoDaoException(e);
    }
  }

}
