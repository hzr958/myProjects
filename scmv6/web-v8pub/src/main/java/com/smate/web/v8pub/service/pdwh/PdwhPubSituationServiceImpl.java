package com.smate.web.v8pub.service.pdwh;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.dao.pdwh.PdwhPubSituationDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubSituationPO;

@Service("pdwhPubSituationService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubSituationServiceImpl implements PdwhPubSituationService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubSituationDAO pdwhSituationDAO;

  @Override
  public PdwhPubSituationPO get(Long pdwhId) throws ServiceException {
    try {
      PdwhPubSituationPO pdwhSituationPO = pdwhSituationDAO.get(pdwhId);
      return pdwhSituationPO;
    } catch (Exception e) {
      logger.error("查询基准库成果收录情况出错！pdwhId={}", pdwhId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PdwhPubSituationPO pdwhSituationPO) throws ServiceException {
    try {
      pdwhSituationDAO.save(pdwhSituationPO);
    } catch (Exception e) {
      logger.error("保存基准库成果收录情况出错！对象属性为={}", pdwhSituationPO);
      throw new ServiceException(e);
    }

  }

  @Override
  public void update(PdwhPubSituationPO pdwhSituationPO) throws ServiceException {
    try {
      pdwhSituationDAO.update(pdwhSituationPO);
    } catch (Exception e) {
      logger.error("更新基准库成果收录情况出错！对象属性为={}", pdwhSituationPO);
      throw new ServiceException(e);
    }

  }

  @Override
  public void saveOrUpdate(PdwhPubSituationPO pdwhPubSituationPO) throws ServiceException {
    try {
      pdwhSituationDAO.saveOrUpdate(pdwhPubSituationPO);
    } catch (Exception e) {
      logger.error("保存或更新基准库成果收录情况出错！对象属性为={}", pdwhPubSituationPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    try {
      pdwhSituationDAO.delete(id);
    } catch (Exception e) {
      logger.error("根据id删除基准库成果收录情况出错！id={}", id);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PdwhPubSituationPO pdwhSituationPO) throws ServiceException {
    try {
      pdwhSituationDAO.delete(pdwhSituationPO);
    } catch (Exception e) {
      logger.error("删除基准库成果收录情况出错！对象属性为={}", pdwhSituationPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteByPubId(Long pdwhPubId) throws ServiceException {
    pdwhSituationDAO.deleteByPubId(pdwhPubId);
  }

  @Override
  public List<String> listByPdwhPubId(Long pdwhPubId) {
    try {
      return pdwhSituationDAO.listByPdwhPubId(pdwhPubId);
    } catch (Exception e) {
      logger.error("删除基准库成果收录情况出错！pdwhPubId={}", pdwhPubId);
      throw new ServiceException(e);
    }
  }

}
