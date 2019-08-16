package com.smate.sie.center.task.pdwh.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.sie.center.task.dao.SiePubKeyWordsDao;
import com.smate.sie.center.task.model.PubKeyWords;
import com.smate.sie.core.base.utils.pub.exception.ServiceException;
import com.smate.sie.core.base.utils.pubxml.ImportPubXmlUtils;

/**
 * 成果关键词service.
 * 
 * @author liqinghua
 */
@Transactional(rollbackFor = Exception.class)
@Service("pubKeyWordsService")
public class PubKeyWordsServiceImpl implements PubKeyWordsService {

  /**
   * 
   */
  private static final long serialVersionUID = 2088024525545734632L;

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SiePubKeyWordsDao pubRolKeyWordsDao;

  @Override
  public void savePubKeywords(Long pubId, Long insId, String zhKeywords) throws ServiceException {
    try {
      String keywords = "";
      if (StringUtils.isNotBlank(zhKeywords)) {
        keywords += ";" + zhKeywords;
      }
      List<String> kwList = ImportPubXmlUtils.parsePubKeywords(keywords);
      this.pubRolKeyWordsDao.savePubKeywords(pubId, insId, kwList);
    } catch (Exception e) {
      logger.error("保存成果关键词pubId:" + pubId, e);
      throw new ServiceException("保存成果关键词pubId:" + pubId, e);
    }

  }

  @Override
  public void delPubKeywords(Long pubId) throws SysServiceException {
    try {
      pubRolKeyWordsDao.delPubKeywords(pubId);
    } catch (Exception e) {
      logger.error("删除成果关键词pubId:" + pubId, e);
      throw new SysServiceException("删除成果关键词pubId:" + pubId, e);
    }

  }

  @Override
  public String getPubkeywords(Long pubId) throws SysServiceException {
    String keywordStr = "";
    try {
      List<PubKeyWords> pubKeyList = this.pubRolKeyWordsDao.getPubKeyWords(pubId);
      if (CollectionUtils.isNotEmpty(pubKeyList)) {
        for (PubKeyWords pubKey : pubKeyList) {
          keywordStr = pubKey.getKeyword() + "；" + keywordStr;
        }
        keywordStr = StringUtils.substring(keywordStr, 0, keywordStr.length() - 1);
      }
    } catch (Exception e) {
      logger.error("获取成果关键词出现异常pubId=" + pubId, e);
      throw new SysServiceException(e);
    }
    return keywordStr;
  }

}
