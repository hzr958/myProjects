package com.smate.center.batch.service.pub;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.psn.PsnKwPubDao;
import com.smate.center.batch.dao.sns.pub.PubKeyWordsDao;
import com.smate.center.batch.dao.sns.pub.PubOwnerMatchDao;
import com.smate.center.batch.dao.sns.pub.PublicationDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.KeywordsEnTranZh;
import com.smate.center.batch.model.sns.pub.KeywordsZhTranEn;
import com.smate.center.batch.model.sns.pub.PsnKwPub;
import com.smate.center.batch.model.sns.pub.PubKeyWords;
import com.smate.center.batch.model.sns.pub.PubOwnerMatch;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.util.pub.ImportPubXmlUtils;
import com.smate.center.batch.util.pub.KeywordsDicUtils;

/**
 * 成果关键词service.
 * 
 * @author liqinghua
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Service("pubKeyWordsService")
public class PubKeyWordsServiceImpl implements PubKeyWordsService {

  /**
   * 
   */
  private static final long serialVersionUID = -2506671928765316144L;
  /**
   * 
   */
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubKeyWordsDao pubKeyWordsDao;
  @Autowired
  private PsnKwPubDao psnKwPubDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PubOwnerMatchDao pubOwnerMatchDao;
  @Autowired
  private PublicationJournalService publicationJournalService;
  @Autowired
  private KeywordsTranService keywordsTranService;

  @Override
  public void savePubKeywords(Long pubId, Long psnId, String zhKeywords, String enKeywords) throws ServiceException {
    try {
      String keywords = "";
      if (StringUtils.isNotBlank(zhKeywords)) {
        keywords += ";" + zhKeywords;
      }
      if (StringUtils.isNotBlank(enKeywords)) {
        keywords += ";" + enKeywords;
      }
      List<String> kwList = ImportPubXmlUtils.parsePubKeywords(keywords);
      this.pubKeyWordsDao.savePubKeywords(pubId, psnId, kwList);
    } catch (Exception e) {
      logger.error("保存成果关键词pubId:" + pubId, e);
      throw new ServiceException("保存成果关键词pubId:" + pubId, e);
    }

  }

  @Override
  public void delPubKeywords(Long pubId) throws ServiceException {
    try {
      pubKeyWordsDao.delPubKeywords(pubId);
    } catch (Exception e) {
      logger.error("删除成果关键词pubId:" + pubId, e);
      throw new ServiceException("删除成果关键词pubId:" + pubId, e);
    }

  }

  @Override
  public void delPsnPubKw(Long pubId) throws ServiceException {
    try {
      psnKwPubDao.delPsnKwPub(pubId);
    } catch (Exception e) {
      logger.error("删除人员成果关键词pubId:" + pubId, e);
      throw new ServiceException("删除人员成果关键词pubId:" + pubId, e);
    }
  }

  @Override
  public void savePubKwToPsnPubKw(Long pubId, Long psnId) throws ServiceException {

    try {
      psnKwPubDao.delPsnKwPub(pubId);
      List<PubKeyWords> pubkwList = this.pubKeyWordsDao.getPubKeyWords(pubId);
      if (CollectionUtils.isEmpty(pubkwList)) {
        return;
      }
      // 先判断成果关键词类型
      boolean enFlag = false;
      boolean zhFlag = false;
      for (PubKeyWords pubkw : pubkwList) {
        if (StringUtils.isAsciiPrintable(pubkw.getKeyword())) {
          enFlag = true;
        } else {
          zhFlag = true;
        }
        if (enFlag && zhFlag) {
          break;
        }
      }
      Publication pub = this.publicationDao.getPubInfoForPsnKw(pubId);
      // 作者序号（为0表示未匹配上）
      Integer auSeq = 0;
      // 是否通讯作者
      Integer auPos = 0;
      PubOwnerMatch pubOm = this.pubOwnerMatchDao.getPubOwnerMatch(pubId);
      if (pubOm != null) {
        auSeq = pubOm.getAuSeq();
        auPos = pubOm.getAuPos();
      }
      boolean isHxj = publicationJournalService.isPubHxj(pubId);
      for (PubKeyWords pubkw : pubkwList) {
        PsnKwPub psnKwPub = new PsnKwPub(pubId, psnId);
        psnKwPub.setId(pubkw.getId());
        psnKwPub.setKeyword(pubkw.getKeyword());
        psnKwPub.setKeywordTxt(pubkw.getKeywordTxt());
        psnKwPub.setPubType(pub.getTypeId());
        psnKwPub.setPublishYear(pub.getPublishYear());
        psnKwPub.setAuSeq(auSeq);
        psnKwPub.setAuPos(auPos);
        psnKwPub.setHxj(isHxj ? 1 : 0);
        // 中英关键词同时存在，不需要翻译
        if (enFlag && zhFlag) {
          if (StringUtils.isAsciiPrintable(pubkw.getKeyword())) {
            psnKwPub.setEnKw(pubkw.getKeyword());
            psnKwPub.setEnKwTxt(pubkw.getKeywordTxt());
            psnKwPub.setEnKwLen(KeywordsDicUtils.getKwWordLength(pubkw.getKeywordTxt()));
          } else {
            psnKwPub.setZhKw(pubkw.getKeyword());
            psnKwPub.setZhKwTxt(pubkw.getKeywordTxt());
            psnKwPub.setZhKwLen(KeywordsDicUtils.getKwWordLength(pubkw.getKeywordTxt()));
          }
          // 只有英文关键词
        } else if (enFlag) {
          psnKwPub.setEnKw(pubkw.getKeyword());
          psnKwPub.setEnKwTxt(pubkw.getKeywordTxt());
          psnKwPub.setEnKwLen(KeywordsDicUtils.getKwWordLength(pubkw.getKeywordTxt()));
          // 把英文翻译成中文
          KeywordsEnTranZh tranZhKw = this.keywordsTranService.findEnTranZhKw(pubkw.getKeywordTxt());
          if (tranZhKw != null) {
            psnKwPub.setZhKw(tranZhKw.getZhKw());
            psnKwPub.setZhKwTxt(tranZhKw.getZhKwTxt());
            psnKwPub.setZhKwLen(KeywordsDicUtils.getKwWordLength(tranZhKw.getZhKw()));
          }
          // 只有中文关键词
        } else {
          psnKwPub.setZhKw(pubkw.getKeyword());
          psnKwPub.setZhKwTxt(pubkw.getKeywordTxt());
          psnKwPub.setZhKwLen(KeywordsDicUtils.getKwWordLength(pubkw.getKeywordTxt()));
          // 把中文关键词翻译成英文关键词
          KeywordsZhTranEn tranEnKw = this.keywordsTranService.findZhTranEnKw(pubkw.getKeywordTxt());
          if (tranEnKw != null) {
            psnKwPub.setEnKw(tranEnKw.getEnKw());
            psnKwPub.setEnKwTxt(tranEnKw.getEnKwTxt());
            psnKwPub.setEnKwLen(KeywordsDicUtils.getKwWordLength(tranEnKw.getEnKw()));
          }
        }
        Double weight = 0D;
        // ISI/CNKI核心期刊+第123/通信作者=1，单非=0.618，双非=0.618×0.618
        if ((auSeq > 0 && (auSeq <= 3 || auPos == 1)) && isHxj) {
          weight = 1D;
        } else if (auSeq > 0 && ((auSeq <= 3 || auPos == 1) || isHxj)) {
          weight = 0.618;
        } else if (auSeq > 0) {
          weight = 0.618 * 0.618;
        }
        psnKwPub.setWeight(weight);
        this.psnKwPubDao.save(psnKwPub);
      }
    } catch (Exception e) {
      logger.error("保存成果关键词到人员成果关键词pubId:" + pubId, e);
      throw new ServiceException("保存成果关键词到人员成果关键词pubId:" + pubId, e);
    }
  }

  @Override
  public List<PubKeyWords> getPubKws(Long pubId) throws ServiceException {

    try {
      return this.pubKeyWordsDao.getPubKeyWords(pubId);
    } catch (Exception e) {
      logger.error("获取成果关键词列表pubId:" + pubId, e);
      throw new ServiceException("获取成果关键词列表pubId:" + pubId, e);
    }
  }

}
