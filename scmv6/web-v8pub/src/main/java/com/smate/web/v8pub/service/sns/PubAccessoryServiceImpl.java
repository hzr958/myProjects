package com.smate.web.v8pub.service.sns;

import com.smate.web.v8pub.dao.sns.PubAccessoryDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubAccessoryPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 成果附件实现类
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
@Service("pubAccessoryService")
@Transactional(rollbackFor = Exception.class)
public class PubAccessoryServiceImpl implements PubAccessoryService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubAccessoryDAO pubAccessoryDAO;

  @Override
  public PubAccessoryPO get(Long id) throws ServiceException {
    try {
      if (id == null || id == 0L) {
        return null;
      }
      return pubAccessoryDAO.get(id);
    } catch (Exception e) {
      logger.error("成果附件服务:查询成果附件异常,pubId=" + id, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PubAccessoryPO pubAccessoryPO) throws ServiceException {
    try {
      pubAccessoryDAO.save(pubAccessoryPO);
    } catch (Exception e) {
      logger.error("成果附件服务:保存成果附件异常,pubId=" + pubAccessoryPO.getPubId(), e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void update(PubAccessoryPO pubAccessoryPO) throws ServiceException {
    try {
      pubAccessoryDAO.save(pubAccessoryPO);
    } catch (Exception e) {
      logger.error("成果附件服务:保存更新成果附件异常,pubId=" + pubAccessoryPO.getPubId(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PubAccessoryPO pubAccessoryPO) throws ServiceException {
    try {
      pubAccessoryDAO.saveOrUpdate(pubAccessoryPO);
    } catch (Exception e) {
      logger.error("成果附件服务:保存or更新成果附件异常,pubId=" + pubAccessoryPO.getPubId(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    try {
      pubAccessoryDAO.delete(id);
    } catch (Exception e) {
      logger.error("成果附件服务:通过id删除成果附件异常,pubId=" + id, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void delete(PubAccessoryPO pubAccessoryPO) throws ServiceException {
    try {
      pubAccessoryDAO.delete(pubAccessoryPO);
    } catch (Exception e) {
      logger.error("成果附件服务:通过对象删除成果附件异常,pubId=" + pubAccessoryPO.getPubId(), e);
      throw new ServiceException(e);
    }

  }

  @Override
  public PubAccessoryPO findByPubIdAndFileId(Long pubId, Long fileId) throws ServiceException {
    try {
      PubAccessoryPO pubAccessoryPO = pubAccessoryDAO.findByPubIdAndFileId(pubId, fileId);
      return pubAccessoryPO;
    } catch (Exception e) {
      logger.error("成果附件服务:查询成果附件异常,pubId=" + pubId + ",fileId=" + fileId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<PubAccessoryPO> findByPubId(Long pubId) throws ServiceException {
    List<PubAccessoryPO> list = pubAccessoryDAO.findByPubId(pubId);
    return list;
  }

  @Override
  public void deleteAll(Long pubId) throws ServiceException {
    try {
      pubAccessoryDAO.deleteAll(pubId);
    } catch (Exception e) {
      logger.error("通过pubId删除所有附件出错！pubId = {}", pubId, e);
      throw new ServiceException(e);
    }
  }

}
