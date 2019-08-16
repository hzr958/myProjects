package com.smate.center.batch.service.pub;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.PrjSchemeAgencyDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.AcPrjSchemeAgency;
import com.smate.center.batch.model.sns.pub.PrjSchemeAgency;


/**
 * 项目资助机构.
 * 
 * @author liqinghua
 * 
 */
@Service("prjSchemeAgencyService")
@Transactional(rollbackFor = Exception.class)
public class prjSchemeAgencyServiceImpl implements PrjSchemeAgencyService {


  /**
   * 
   */
  private static final long serialVersionUID = 7195171329244250994L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PrjSchemeAgencyDao prjSchemeAgencyDao;

  @Override
  public PrjSchemeAgency findByName(String name) throws ServiceException {
    try {
      return prjSchemeAgencyDao.findByName(name);
    } catch (Exception e) {
      logger.error("通过名字查找项目资助机构错误", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<AcPrjSchemeAgency> getAcPrjSchemeAgency(String startStr, int size) throws ServiceException {
    try {
      return prjSchemeAgencyDao.getAcPrjSchemeAgency(startStr, size);
    } catch (Exception e) {
      logger.error("查询指定智能匹配前 N条数据错误", e);
      throw new ServiceException(e);
    }
  }

}
