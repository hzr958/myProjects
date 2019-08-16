package com.smate.web.v8pub.service.sns;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.web.v8pub.dao.sns.PubCitationsDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubCitationsPO;


@Service("pubCitationsService")
@Transactional(rollbackFor = Exception.class)
public class PubCitationsServiceImpl implements PubCitationsService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubCitationsDAO pubCitationsDAO;

  @Override
  public PubCitationsPO get(Long pubId) throws ServiceException {
    PubCitationsPO pubCitationsPO = null;
    try {
      pubCitationsPO = pubCitationsDAO.findByPubId(pubId);
      return pubCitationsPO;
    } catch (Exception e) {
      logger.error("获取成果引用数据失败，pubCitationsPO={}", pubCitationsPO, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void save(PubCitationsPO pubCitationsPO) throws ServiceException {
    try {
      pubCitationsDAO.save(pubCitationsPO);
    } catch (Exception e) {
      logger.error("保存成果引用数据失败，pubCitationsPO={}", pubCitationsPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PubCitationsPO pubCitationsPO) throws ServiceException {
    try {
      if (pubCitationsDAO.allowUpdate(pubCitationsPO)) {
        pubCitationsDAO.save(pubCitationsPO);
      }
    } catch (Exception e) {
      logger.error("更新成果引用数据失败，pubCitationsPO={}", pubCitationsPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PubCitationsPO pubCitationsPO) throws ServiceException {
    try {
      pubCitationsDAO.saveOrUpdate(pubCitationsPO);
    } catch (Exception e) {
      logger.error("保存或更新成果引用数据失败，pubCitationsPO={}", pubCitationsPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    try {
      pubCitationsDAO.delete(id);
    } catch (Exception e) {
      logger.error("根据逻辑逐渐id删除成果引用数据失败，id={}", id, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PubCitationsPO pubCitationsPO) throws ServiceException {
    try {
      pubCitationsDAO.delete(pubCitationsPO);
    } catch (Exception e) {
      logger.error("删除成果引用数据失败，pubCitationsPO={}", pubCitationsPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean isExistsPubCitations(Long pubId) throws ServiceException {
    try {
      return pubCitationsDAO.isExistsPubCitations(pubId);
    } catch (Exception e) {
      logger.error("判断是否存在成果引用数据失败，pubId={}", pubId, e);
      throw new ServiceException(e);
    }
  }

}
