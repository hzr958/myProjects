package com.smate.web.v8pub.service.pdwh;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.smate.core.base.utils.pubxml.ImportPubXmlUtils;
import com.smate.web.v8pub.dao.pdwh.PdwhPubKeywordsDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.pdwh.PdwhPubKeywordsPO;

/**
 * 基准库成果关键词服务实现类
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PdwhPubKeywordsServiceImpl implements PdwhPubKeywordsService {
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubKeywordsDAO pdwhPubKeywordsDAO;

  @Override
  public List<PdwhPubKeywordsPO> getByPubId(Long pubId) throws ServiceException {
    try {
      List<PdwhPubKeywordsPO> list = pdwhPubKeywordsDAO.getByPubId(pubId);
      return list;
    } catch (Exception e) {
      logger.error("基准库成果关键词服务:获取成果关键词列表异常，pubId=" + pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PdwhPubKeywordsPO get(Long id) throws ServiceException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void save(PdwhPubKeywordsPO pdwhPubKeywordsPO) throws ServiceException {
    try {
      pdwhPubKeywordsDAO.save(pdwhPubKeywordsPO);
    } catch (Exception e) {
      logger.error("基准库成果关键词服务:保存成果关键词列表异常，PdwhPubKeywordsPO={}", pdwhPubKeywordsPO, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void update(PdwhPubKeywordsPO entity) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void saveOrUpdate(PdwhPubKeywordsPO entity) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(PdwhPubKeywordsPO entity) throws ServiceException {
    // TODO Auto-generated method stub

  }

  @Override
  public void savePubKeywords(Long pdwhPubId, String keywords) throws ServiceException {
    try {
      List<String> kwList = ImportPubXmlUtils.parsePubKeywords2(keywords);
      pdwhPubKeywordsDAO.savePubKeywords(pdwhPubId, kwList);
    } catch (Exception e) {
      logger.error("保存基准库成果关键词出错！pdwhPubId:={}", pdwhPubId, e);
      throw new ServiceException("保存基准库成果关键词出错");
    }
  }

}
