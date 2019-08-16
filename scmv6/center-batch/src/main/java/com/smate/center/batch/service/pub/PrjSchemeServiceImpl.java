package com.smate.center.batch.service.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.PrjSchemeDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.AcPrjScheme;
import com.smate.center.batch.model.sns.pub.PrjScheme;


/**
 * 项目资助类别.
 * 
 * @author liqinghua
 * 
 */
@Service("prjSchemeService")
@Transactional(rollbackFor = Exception.class)
public class PrjSchemeServiceImpl implements PrjSchemeService {

  /**
   * 
   */
  private static final long serialVersionUID = -2485041014431827715L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PrjSchemeDao prjSchemeDao;

  @Override
  public PrjScheme findByName(String name, Long agencyId) throws ServiceException {
    try {
      return prjSchemeDao.findByName(name, agencyId);
    } catch (Exception e) {
      logger.error("通过名字查找项目资助类别错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<AcPrjScheme> getAcPrjScheme(String startStr, Long agencyId, int size) throws ServiceException {
    try {
      if (agencyId == null)
        return null;
      return prjSchemeDao.getAcPrjSchemeAgency(startStr, agencyId, size);
    } catch (Exception e) {
      logger.error("查询指定智能匹配前 N 条数据错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PrjScheme findByName(String name) throws ServiceException {
    try {
      return prjSchemeDao.findByName(name);
    } catch (Exception e) {
      logger.error("通过名字查找项目资助类别错误", e);
      throw new ServiceException(e);
    }
  }

}
