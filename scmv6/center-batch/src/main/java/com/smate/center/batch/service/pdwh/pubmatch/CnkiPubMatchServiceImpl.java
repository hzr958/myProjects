package com.smate.center.batch.service.pdwh.pubmatch;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.constant.PubMatchedScoreConstants;
import com.smate.center.batch.dao.pdwh.pub.cnki.CnkiPubAssignDao;
import com.smate.center.batch.dao.pdwh.pub.cnki.CnkiPubMatchAssignDao;
import com.smate.center.batch.dao.pdwh.pub.cnki.CnkiPubMatchAuthorDao;
import com.smate.center.batch.dao.pdwh.pub.cnki.CnkiPubMatchJournalDao;
import com.smate.center.batch.dao.pdwh.pub.cnki.CnkiPubMatchKeywordDao;
import com.smate.center.batch.dao.pdwh.pub.cnki.CnkiPubPsnDao;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubAssign;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubMatchAssign;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubMatchAuthor;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubMatchJournal;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubMatchKeyword;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubPsn;


/**
 * CNKI成果拆分匹配表相关操作的业务逻辑实现类.
 * 
 * @author mjg
 * 
 */
@Service("cnkiPubMatchService")
@Transactional(rollbackFor = Exception.class)
public class CnkiPubMatchServiceImpl implements CnkiPubMatchService {

  private static final long serialVersionUID = 5544429021369891424L;
  @Autowired
  private CnkiPubMatchAssignDao cnkiPubMatchAssignDao;
  @Autowired
  private CnkiPubMatchAuthorDao cnkiPubMatchAuthorDao;
  @Autowired
  private CnkiPubMatchJournalDao cnkiPubMatchJournalDao;
  @Autowired
  private CnkiPubMatchKeywordDao cnkiPubMatchKeywordDao;
  @Autowired
  private CnkiPubPsnDao cnkiPubPsnDao;
  @Autowired
  private CnkiPubAssignDao cnkiPubAssignDao;

  /**
   * 初始化保存成果匹配结果记录.
   * 
   * @param author
   * @param psnId
   */
  @Override
  public CnkiPubMatchAssign initCnkiPubMatchAssign(CnkiPubMatchAuthor author, Long psnId) {
    // 保存成果匹配结果记录.
    CnkiPubMatchAssign assign = cnkiPubMatchAssignDao.getCnkiPubMatchAssign(author.getPubId(), psnId);
    if (assign == null) {
      assign = new CnkiPubMatchAssign();
      assign.setPubId(author.getPubId());
      assign.setPsnId(psnId);
      assign.setName(1);
      assign.setAthId(author.getId());
      assign.setAthSeq(author.getSeqNo());
      assign.setAthPos(author.getAuthorPos());
      assign.setStatus(0);
      assign.setScore(PubMatchedScoreConstants.FNAME_SCORE);
      cnkiPubMatchAssignDao.save(assign);
    }
    return assign;
  }

  /**
   * 排除用户已确认或已拒绝的成果.
   * 
   * @param psnId
   * @param pubIdList
   */
  @Override
  public void excludeMatchedPub(Long psnId, List<Long> pubIdList) {
    // 获取用户已确认或已拒绝的成果.
    List<CnkiPubPsn> matchedPubList = cnkiPubPsnDao.getCnkiPubPsnByPsn(psnId);
    if (CollectionUtils.isNotEmpty(matchedPubList) && CollectionUtils.isNotEmpty(pubIdList)) {
      for (Long pubId : pubIdList) {
        for (CnkiPubPsn matchedPub : matchedPubList) {
          // 如果当前匹配到的成果已被确认或拒绝，则修改匹配记录的状态.
          if (pubId.longValue() == matchedPub.getPubId().longValue()) {
            cnkiPubMatchAssignDao.updatePubStatus(Long.valueOf(psnId), pubId, matchedPub.getResult());
          }
        }
      }
    }
  }

  /**
   * 获取CnkiPubAssign.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  @Override
  public CnkiPubAssign getCnkiPubAssign(Long pubId, Long insId) {
    return cnkiPubAssignDao.getCnkiPubAssign(pubId, insId);
  }

  /**
   * 查找单位的所有已匹配上机构的成果.
   * 
   * @param insId
   * @return
   */
  @Override
  public List<CnkiPubAssign> getCnkiPubAssign(Long insId) {
    return cnkiPubAssignDao.getCnkiPubAssign(insId);
  }

  /**
   * 查找单位的所有已匹配上机构的成果ID.
   * 
   * @param insId
   * @return
   */
  @Override
  public List<Long> getCnkiPubAssignId(Long insId, List<String> psnZhNameList, int size) {
    List<Long> pubIdList = new ArrayList<Long>();
    // 获取作者名称匹配的成果ID.
    List<Long> authorPubIdList = cnkiPubMatchAuthorDao.getMatchAuthorPubList(psnZhNameList);
    if (CollectionUtils.isNotEmpty(authorPubIdList)) {
      pubIdList = cnkiPubAssignDao.getCnkiPubAssList(insId, authorPubIdList, size);
    }
    return pubIdList;
  }

  /**
   * 根据成果ID获取期刊的关键词和关键词的hash值列表.
   * 
   * @param pubId
   * @return
   */
  @Override
  public List<CnkiPubMatchKeyword> getMatchedKeywordByPubId(Long pubId) {
    return cnkiPubMatchKeywordDao.getMatchedKeywordByPubId(pubId);
  }

  /**
   * 保存成果匹配结果记录.
   * 
   * @param assign
   */
  @Override
  public void saveCnkiPubMatchAssign(CnkiPubMatchAssign assign) {
    cnkiPubMatchAssignDao.save(assign);
  }

  /**
   * 根据人员ID获取匹配的成果ID列表.
   * 
   * @param psnId
   * @return
   */
  @Override
  public List<Long> getPubIdListByPsnId(Long psnId) {
    return cnkiPubMatchAssignDao.getPubIdListByPsnId(psnId);
  }

  /**
   * 获取成果的期刊列表.
   * 
   * @param pubId
   * @return
   */
  @Override
  public List<CnkiPubMatchJournal> getMatchedJournalList(Long pubId) {
    return cnkiPubMatchJournalDao.getMatchedJournalList(pubId);
  }

  /**
   * 获取用户匹配到的成果.
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  @Override
  public CnkiPubMatchAssign getCnkiPubMatchAssign(Long pubId, Long psnId) {
    return cnkiPubMatchAssignDao.getCnkiPubMatchAssign(pubId, psnId);
  }

  /**
   * 获取CNKI已拆分成果表中的最大成果ID.
   * 
   * @return
   */
  @Override
  public Long getMaxPubMatchAssignId() {
    return cnkiPubMatchAssignDao.getMaxPubId();
  }

  /**
   * 获取用户名称获取匹配的成果ID记录.
   * 
   * @param pubId
   * @return
   */
  @Override
  public List<CnkiPubMatchAuthor> getCnkiPubMatchAuthorList(Long pubId, String name) {
    return cnkiPubMatchAuthorDao.getCnkiPubMatchAuthorList(pubId, name);
  }

  /**
   * 获取成果的作者列表.
   * 
   * @param pubId
   * @return
   */
  @Override
  public List<CnkiPubMatchAuthor> getMatchedAuthorList(Long pubId) {
    return cnkiPubMatchAuthorDao.getMatchedAuthorList(pubId);
  }

  /**
   * 检查是否已匹配到成果.
   * 
   * @param psnId
   * @return true-已匹配到；false-未匹配到.
   */
  @Override
  public boolean isExistMatchedPub(Long psnId) {
    return cnkiPubMatchAssignDao.isExistMatchedPub(psnId);
  }

  /**
   * 删除已匹配到的待确认的成果.
   * 
   * @param psnId
   */
  @Override
  public void deleteUnFirmPub(Long psnId) {
    cnkiPubMatchAssignDao.deleteUnFirmPub(psnId);
  }

  @Override
  public void saveCnkiPubMatchAuthor(CnkiPubMatchAuthor author) {
    cnkiPubMatchAuthorDao.saveCnkiPubMatchAuthor(author);
  }

  @Override
  public CnkiPubMatchAuthor getCnkiPubMatchAuthor(Long pubId, String name) {
    return cnkiPubMatchAuthorDao.getCnkiPubMatchAuthor(pubId, name);
  }

  @Override
  public void saveCnkiPubMatchJournal(CnkiPubMatchJournal journal) {
    cnkiPubMatchJournalDao.saveCNKIPubMatchJournal(journal);
  }

  @Override
  public CnkiPubMatchJournal getCnkiPubMatchJournal(Long pubId, String issn) {
    return cnkiPubMatchJournalDao.getCnkiPubMatchJournal(pubId, issn);
  }

  @Override
  public void saveCnkiPubMatchKeyword(CnkiPubMatchKeyword keyword) {
    cnkiPubMatchKeywordDao.save(keyword);
  }

  @Override
  public CnkiPubMatchKeyword getCnkiPubMatchKeyword(Long pubId, Long kwHash) {
    return cnkiPubMatchKeywordDao.getCnkiPubMatchKeyword(pubId, kwHash);
  }

  /**
   * 保存匹配成果的合作者名称.
   * 
   * @param pubId
   * @param psnId
   * @param coFNameNum
   * @param score
   */
  @Override
  public void updateAssignCoName(Long pubId, Long psnId, Integer coFNameNum, Integer score) {
    cnkiPubMatchAssignDao.updateAssignCoName(pubId, psnId, coFNameNum, score);
  }

  /**
   * 保存匹配成果的期刊.
   * 
   * @param pubId
   * @param psnId
   * @param journal
   * @param score
   */
  @Override
  public void updateAssignJnl(Long pubId, Long psnId, Integer journal, Integer score) {
    cnkiPubMatchAssignDao.updateAssignJnl(pubId, psnId, journal, score);
  }

  /**
   * 保存匹配成果的关键词.
   * 
   * @param pubId
   * @param psnId
   * @param matchedKwNum
   * @param score
   */
  @Override
  public void updateAssignKw(Long pubId, Long psnId, Integer matchedKwNum, Integer score) {
    cnkiPubMatchAssignDao.updateAssignKw(pubId, psnId, matchedKwNum, score);
  }
}
