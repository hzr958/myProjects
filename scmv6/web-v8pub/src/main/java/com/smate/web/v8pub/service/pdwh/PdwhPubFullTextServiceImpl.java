package com.smate.web.v8pub.service.pdwh;

import com.smate.web.v8pub.dao.pdwh.PdwhPubFullTextDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubFullTextPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class PdwhPubFullTextServiceImpl implements PdwhPubFullTextService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubFullTextDAO pdwhFullTextDAO;

  @Override
  public PdwhPubFullTextPO get(Long pdwhId) throws ServiceException {
    try {
      PdwhPubFullTextPO pdwhFullTextPO = pdwhFullTextDAO.get(pdwhId);
      return pdwhFullTextPO;
    } catch (Exception e) {
      logger.error("查询基准库成果全文出错! pdwhId={}", pdwhId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void save(PdwhPubFullTextPO pdwhFullTextPO) throws ServiceException {
    try {
      pdwhFullTextDAO.save(pdwhFullTextPO);
    } catch (Exception e) {
      logger.error("保存基准库成果全文表记录出错！", pdwhFullTextPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PdwhPubFullTextPO pdwhFullTextPO) throws ServiceException {
    try {
      pdwhFullTextDAO.update(pdwhFullTextPO);
    } catch (Exception e) {
      logger.error("更新基准库成果全文表记录出错！", pdwhFullTextPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PdwhPubFullTextPO pdwhFullTextPO) throws ServiceException {
    try {
      pdwhFullTextDAO.saveOrUpdate(pdwhFullTextPO);
    } catch (Exception e) {
      logger.error("保存或保存基准库成果全文表记录出错！", pdwhFullTextPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public void deleteById(Long pdwhId) throws ServiceException {
    try {
      pdwhFullTextDAO.delete(pdwhId);
    } catch (Exception e) {
      logger.error("根据pdwhId删除基准库成果全文表记录出错！pdwhId={}", pdwhId);
      throw new ServiceException(e);
    }
  }

  @Override
  public void delete(PdwhPubFullTextPO pdwhFullTextPO) throws ServiceException {
    try {
      pdwhFullTextDAO.delete(pdwhFullTextPO);
    } catch (Exception e) {
      logger.error("删除基准库成果全文表记录出错！ 对象属性为", pdwhFullTextPO);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<PdwhPubFullTextPO> queryPdwhFullTextByIds(PubQueryDTO pubQueryDTO) throws ServiceException {
    try {
      List<PdwhPubFullTextPO> list = pdwhFullTextDAO.findByIds(pubQueryDTO);
      return list;
    } catch (Exception e) {
      logger.error("通过ids查询全文出现异常：ids={}", pubQueryDTO.getPubIds(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PdwhPubFullTextPO getPdwhPubfulltext(Long pubId) {
    try {
      return pdwhFullTextDAO.getByPubId(pubId);
    } catch (Exception e) {
      logger.error("通过pubId获取基准库成果全文信息出错！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public PdwhPubFullTextPO getFullText(Long pubId, Long fileId) throws ServiceException {
    try {
      return pdwhFullTextDAO.getByPubId(pubId, fileId);
    } catch (Exception e) {
      logger.error("通过pubId和file获取基准库成果全文信息出错！", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public Long getCountByPdwhPubId(Long pdwhPubId) throws ServiceException {
    try {
      return pdwhFullTextDAO.getCountByPdwhPubId(pdwhPubId);
    } catch (Exception e) {
      logger.error("获取基准成果全文的数量！pdwhPubId={}", pdwhPubId, e);
      throw new ServiceException(e);
    }
  }
}
