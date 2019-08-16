package com.smate.web.v8pub.service.pdwh;

import com.smate.web.v8pub.dao.pdwh.PdwhPubCitationsDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubCitationsPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class PdwhPubCitationsServiceImpl implements PdwhPubCitationsService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubCitationsDAO pdwhPubCitationsDAO;

  @Override
  public PdwhPubCitationsPO get(Long id) throws ServiceException {
    try {
      return pdwhPubCitationsDAO.get(id);
    } catch (Exception e) {
      logger.error("根据逻辑id获取基准库成果引用次数记录表对象出错！id={}", id, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PdwhPubCitationsPO pdwhPubCitationsPO) throws ServiceException {
    try {
      pdwhPubCitationsDAO.save(pdwhPubCitationsPO);
    } catch (Exception e) {
      logger.error("保存基准库成果引用次数记录表对象出错！对象属性={}", pdwhPubCitationsPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PdwhPubCitationsPO pdwhPubCitationsPO) throws ServiceException {
    try {
      pdwhPubCitationsDAO.update(pdwhPubCitationsPO);
    } catch (Exception e) {
      logger.error("更新基准库成果引用次数记录表对象出错！对象属性={}", pdwhPubCitationsPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PdwhPubCitationsPO pdwhPubCitationsPO) throws ServiceException {
    try {
      pdwhPubCitationsDAO.saveOrUpdate(pdwhPubCitationsPO);
    } catch (Exception e) {
      logger.error("更新或保存基准库成果引用次数记录表对象出错！对象属性={}", pdwhPubCitationsPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    try {
      pdwhPubCitationsDAO.delete(id);
    } catch (Exception e) {
      logger.error("删除基准库成果引用次数记录表对象出错！id={}", id, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PdwhPubCitationsPO pdwhPubCitationsPO) throws ServiceException {
    try {
      pdwhPubCitationsDAO.delete(pdwhPubCitationsPO);
    } catch (Exception e) {
      logger.error("删除基准库成果引用次数记录表对象出错！对象属性={}", pdwhPubCitationsPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PdwhPubCitationsPO getByPubIdAndDbId(Long pubId, Integer dbId) throws ServiceException {
    try {
      return pdwhPubCitationsDAO.getByPubIdAndDbId(pubId, dbId);
    } catch (Exception e) {
      logger.error("获取基准库成果引用次数记录对象出错！pubId={}，dbId={}", new Object[] {pubId, dbId}, e);
      throw new ServiceException(e);
    }
  }


}
