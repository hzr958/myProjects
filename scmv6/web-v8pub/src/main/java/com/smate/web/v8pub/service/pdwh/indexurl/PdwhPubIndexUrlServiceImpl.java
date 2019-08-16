package com.smate.web.v8pub.service.pdwh.indexurl;

import com.smate.web.v8pub.dao.pdwh.PdwhPubIndexUrlDao;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubIndexUrl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "pdwhPubIndexUrlService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubIndexUrlServiceImpl implements PdwhPubIndexUrlService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhPubIndexUrlDao pdwhPubIndexUrlDao;

  @Override
  public PdwhPubIndexUrl get(Long pdwhPubId) throws ServiceException {
    PdwhPubIndexUrl pdwhPubIndexUrl = null;
    try {
      pdwhPubIndexUrl = pdwhPubIndexUrlDao.get(pdwhPubId);
      return pdwhPubIndexUrl;
    } catch (Exception e) {
      logger.error("基准库成果短地址服务：获取对象出错！PdwhPubIndexUrl={}", pdwhPubIndexUrl);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PdwhPubIndexUrl pdwhPubIndexUrl) throws ServiceException {
    try {
      pdwhPubIndexUrlDao.save(pdwhPubIndexUrl);
    } catch (Exception e) {
      logger.error("基准库成果短地址服务：保存对象出错！PdwhPubIndexUrl={}", pdwhPubIndexUrl);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PdwhPubIndexUrl pdwhPubIndexUrl) throws ServiceException {
    try {
      pdwhPubIndexUrlDao.update(pdwhPubIndexUrl);
    } catch (Exception e) {
      logger.error("基准库成果短地址服务：更新对象出错！PdwhPubIndexUrl={}", pdwhPubIndexUrl);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PdwhPubIndexUrl pdwhPubIndexUrl) throws ServiceException {
    try {
      pdwhPubIndexUrlDao.saveOrUpdate(pdwhPubIndexUrl);
    } catch (Exception e) {
      logger.error("基准库成果短地址服务：保存或更新对象出错！PdwhPubIndexUrl={}", pdwhPubIndexUrl);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long pdwhPubId) throws ServiceException {
    try {
      pdwhPubIndexUrlDao.delete(pdwhPubId);
    } catch (Exception e) {
      logger.error("基准库成果短地址服务：根据pdwhPubId删除对象出错！pdwhPubId={}", pdwhPubId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PdwhPubIndexUrl pdwhPubIndexUrl) throws ServiceException {
    try {
      pdwhPubIndexUrlDao.delete(pdwhPubIndexUrl);
    } catch (Exception e) {
      logger.error("基准库成果短地址服务：删除对象出错！PdwhPubIndexUrl={}", pdwhPubIndexUrl);
      throw new ServiceException(e);
    }
  }

  @Override
  public String getIndexUrlByPubId(Long pubId) throws ServiceException {
    try {
      return pdwhPubIndexUrlDao.getIndexUrlByPubId(pubId);
    } catch (Exception e) {
      logger.error("基准库成果短地址服务：获取成果短地址出错！pubId={}", pubId);
      throw new ServiceException(e);
    }
  }

}
