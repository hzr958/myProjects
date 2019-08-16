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
import com.smate.sie.center.task.dao.SiePatKeyWordsDao;
import com.smate.sie.center.task.model.PatKeyWords;
import com.smate.sie.core.base.utils.pubxml.ImportPubXmlUtils;

@Transactional(rollbackFor = Exception.class)
@Service("patKeyWordsService")
public class PatKeyWordsServiceImpl implements PatKeyWordsService {

  /**
   * 
   */
  private static final long serialVersionUID = 6685668378435813754L;

  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SiePatKeyWordsDao siePatKeyWordsDao;

  @Override
  public void savePatKeywords(Long patId, Long insId, String zhKeywords, String enKeywords) throws SysServiceException {
    try {
      String keywords = "";
      if (StringUtils.isNotBlank(zhKeywords)) {
        keywords += ";" + zhKeywords;
      }
      if (StringUtils.isNotBlank(enKeywords)) {
        keywords += ";" + enKeywords;
      }
      List<String> kwList = ImportPubXmlUtils.parsePubKeywords(keywords);
      this.siePatKeyWordsDao.savePatKeywords(patId, insId, kwList);
    } catch (Exception e) {
      logger.error("保存专利关键词patId:" + patId, e);
      throw new SysServiceException("保存专利关键词patId:" + patId, e);
    }

  }

  @Override
  public void delPatKeywords(Long patId) throws SysServiceException {
    try {
      siePatKeyWordsDao.delPatKeywords(patId);
    } catch (Exception e) {
      logger.error("删除专利关键词patId:" + patId, e);
      throw new SysServiceException("删除专利关键词patId:" + patId, e);
    }

  }

  @Override
  public String getPatkeywords(Long patId) throws SysServiceException {
    String keywordStr = "";
    try {
      List<PatKeyWords> patKeyList = this.siePatKeyWordsDao.getPatKeyWords(patId);
      if (CollectionUtils.isNotEmpty(patKeyList)) {
        for (PatKeyWords pubKey : patKeyList) {
          keywordStr = pubKey.getKeyword() + "；" + keywordStr;
        }
        keywordStr = StringUtils.substring(keywordStr, 0, keywordStr.length() - 1);
      }
    } catch (Exception e) {
      logger.error("获取专利关键词出现异常patId=" + patId, e);
      throw new SysServiceException(e);
    }
    return keywordStr;
  }

}
