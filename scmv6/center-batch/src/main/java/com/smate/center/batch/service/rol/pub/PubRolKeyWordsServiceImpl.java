package com.smate.center.batch.service.rol.pub;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rol.pub.PubRolKeyWordsDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubRolKeyWords;
import com.smate.center.batch.util.pub.ImportPubXmlUtils;

/**
 * 成果关键词service.
 * 
 * @author liqinghua
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Service("pubRolKeyWordsService")
public class PubRolKeyWordsServiceImpl implements PubRolKeyWordsService {

  /**
   * 
   */
  private static final long serialVersionUID = 2088024525545734632L;

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubRolKeyWordsDao pubRolKeyWordsDao;

  @Override
  public void savePubKeywords(Long pubId, Long insId, String zhKeywords, String enKeywords) throws ServiceException {
    try {
      String keywords = "";
      if (StringUtils.isNotBlank(zhKeywords)) {
        keywords += ";" + zhKeywords;
      }
      if (StringUtils.isNotBlank(enKeywords)) {
        keywords += ";" + enKeywords;
      }
      List<String> kwList = ImportPubXmlUtils.parsePubKeywords(keywords);
      this.pubRolKeyWordsDao.savePubKeywords(pubId, insId, kwList);
    } catch (Exception e) {
      logger.error("保存成果关键词pubId:" + pubId, e);
      throw new ServiceException("保存成果关键词pubId:" + pubId, e);
    }

  }

  @Override
  public void delPubKeywords(Long pubId) throws ServiceException {
    try {
      pubRolKeyWordsDao.delPubKeywords(pubId);
    } catch (Exception e) {
      logger.error("删除成果关键词pubId:" + pubId, e);
      throw new ServiceException("删除成果关键词pubId:" + pubId, e);
    }

  }

  @Override
  public String getPubkeywords(Long pubId) throws ServiceException {
    String keywordStr = "";
    try {
      List<PubRolKeyWords> pubKeyList = this.pubRolKeyWordsDao.getPubKeyWords(pubId);
      if (CollectionUtils.isNotEmpty(pubKeyList)) {
        for (PubRolKeyWords pubKey : pubKeyList) {
          keywordStr = pubKey.getKeyword() + "；" + keywordStr;
        }
        keywordStr = StringUtils.substring(keywordStr, 0, keywordStr.length() - 1);
      }
    } catch (Exception e) {
      logger.error("获取成果关键词出现异常pubId=" + pubId, e);
      throw new ServiceException(e);
    }
    return keywordStr;
  }

}
