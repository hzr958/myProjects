package com.smate.web.v8pub.service.sns;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.pubxml.ImportPubXmlUtils;
import com.smate.web.v8pub.dao.sns.PubKeywordsDAO;
import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.po.sns.PubKeywordsPO;

/**
 * 成果关键词服务实现类
 * 
 * @author aijiangbin
 * @date 2018年6月1日
 */
@Service("pubKeywordsService")
@Transactional(rollbackFor = Exception.class)
public class PubKeywordsServiceImpl implements PubKeywordsService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubKeywordsDAO pubKeywordsDAO;

  @Override
  public List<PubKeywordsPO> getByPubId(Long pubId) throws ServiceException {
    try {
      List<PubKeywordsPO> list = pubKeywordsDAO.getByPubId(pubId);
      return list;
    } catch (Exception e) {
      logger.error("成果关键词服务：通过成果id查询关键词异常,pubId=" + pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveOrUpdate(PubKeywordsPO entity) throws ServiceException {
    try {
      pubKeywordsDAO.save(entity);
    } catch (Exception e) {
      logger.error("成果关键词服务：通过保存or更新关键词异常,pubId=" + entity.getPubId(), e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void deleteById(Long id) throws ServiceException {
    try {
      pubKeywordsDAO.delete(id);
    } catch (Exception e) {
      logger.error("成果关键词服务：通过主键id删除关键词异常，id=" + id, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void savePubKeywords(Long pubId, String keywords) throws ServiceException {
    try {
      List<String> kwList = ImportPubXmlUtils.parsePubKeywords2(keywords);
      pubKeywordsDAO.savePubKeywords(pubId, kwList);
    } catch (Exception e) {
      logger.error("保存成果关键词pubId:" + pubId, e);
      throw new ServiceException("保存成果关键词pubId:" + pubId, e);
    }

  }

}
