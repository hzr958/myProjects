package com.smate.center.batch.service.pdwh.isipub;

import com.smate.center.batch.dao.pdwh.psn.PsnPmCoEmailDao;
import com.smate.center.batch.dao.pdwh.psn.PsnPmCoEnNameDao;
import com.smate.center.batch.dao.pdwh.psn.PsnPmJournalDao;
import com.smate.center.batch.dao.pdwh.psn.PsnPmKeywordsDao;
import com.smate.center.batch.dao.pdwh.pub.isi.*;
import com.smate.center.batch.model.pdwh.psn.PsnPmCoEmail;
import com.smate.center.batch.model.pdwh.psn.PsnPmCoEnName;
import com.smate.center.batch.model.pdwh.psn.PsnPmJournal;
import com.smate.center.batch.model.pdwh.psn.PsnPmKeywords;
import com.smate.center.batch.model.pdwh.pub.isi.*;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ISI成果拆分匹配表相关操作的业务逻辑实现类.
 * 
 * @author mjg
 * 
 */
@Service("isiPubMatchService")
@Transactional(rollbackFor = Exception.class)
public class IsiPubMatchServiceImpl implements IsiPubMatchService {

  private static final long serialVersionUID = 4406752640022851048L;
  @Autowired
  private IsiPubMatchAssignDao isiPubMatchAssignDao;
  @Autowired
  private IsiPubMatchEmailDao isiPubMatchEmailDao;
  @Autowired
  private IsiPubMatchJournalDao isiPubMatchJournalDao;
  @Autowired
  private IsiPubMatchKeywordDao isiPubMatchKeywordDao;
  @Autowired
  private IsiPubMatchAuthorDao isiPubMatchAuthorDao;
  @Autowired
  private PsnPmCoEmailDao psnPmCoEmailDao;
  @Autowired
  private PsnPmCoEnNameDao psnPmCoEnNameDao;
  @Autowired
  private PsnPmJournalDao psnPmJournalDao;
  @Autowired
  private PsnPmKeywordsDao psnPmKeywordsDao;
  @Autowired
  private IsiPubKeywordsSplitDao isiPubKeywordsSplitDao;
  @Autowired
  private IsiPubAssignDao isiPubAssignDao;
  @Autowired
  private IsiPubPsnDao isiPubPsnDao;

  /**
   * 初始化保存ISI匹配结果.
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  @Override
  public IsiPubMatchAssign initIsiPubMatchAssign(Long pubId, Long psnId) {
    // 判断数据库中是否已存在匹配的该成果.
    IsiPubMatchAssign assign = isiPubMatchAssignDao.getIsiPubMatchAssign(pubId, psnId);
    if (assign == null) {
      assign = new IsiPubMatchAssign();
      assign.setPubId(pubId);
      assign.setPsnId(psnId);
      assign.setStatus(0);// 匹配状态0-待确认.
      isiPubMatchAssignDao.save(assign);
    }
    return assign;
  }

  /**
   * 获取ISI匹配结果记录.
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  @Override
  public IsiPubMatchAssign getIsiPubMatchAssign(Long pubId, Long psnId) {
    return isiPubMatchAssignDao.getIsiPubMatchAssign(pubId, psnId);
  }

  /**
   * 排除用户已确认或已拒绝的成果.
   * 
   * @param psnId
   * @param pubIdList
   */
  @Override
  public void excludeMatchedPub(String psnId, List<Long> pubIdList) {
    // 获取用户已确认或已拒绝的成果.
    List<IsiPubPsn> matchedPubList = isiPubPsnDao.getPubIdByPsn(Long.valueOf(psnId));
    if (CollectionUtils.isNotEmpty(matchedPubList) && CollectionUtils.isNotEmpty(pubIdList)) {
      for (Long pubId : pubIdList) {
        for (IsiPubPsn pubPsn : matchedPubList) {
          // 如果当前匹配到的成果已被确认或拒绝，则修改匹配记录的状态.
          if (pubPsn.getPubId().longValue() == pubId.longValue()) {
            isiPubMatchAssignDao.updatePubStatus(Long.valueOf(psnId), pubId, pubPsn.getResult());
          }
        }
      }
    }
  }

  /**
   * 补充人员信息.
   * 
   * @param pubId
   * @param author 成果作者.
   * @param authorList 成果所有作者列表.
   */
  @Override
  public void supplePsnPmInfo(Long pubId, Long psnId, IsiPubMatchAuthor author, List<IsiPubMatchAuthor> authorList) {
    if (CollectionUtils.isNotEmpty(authorList)) {
      // 获取成果的作者邮件列表.
      List<IsiPubMatchEmail> emailList = this.getEmailListByPubId(pubId);
      boolean notEmptyEmailFlag = (CollectionUtils.isNotEmpty(emailList));
      for (IsiPubMatchAuthor iAuthor : authorList) {
        // 保存成果的合作者信息.
        if (iAuthor.getId().longValue() != author.getId().longValue()) {
          // 保存合作者的邮件.
          if (notEmptyEmailFlag) {
            String email = this.getEmailWithAuthor(iAuthor, emailList);
            if (StringUtils.isNotBlank(email)) {
              PsnPmCoEmail coEmail = new PsnPmCoEmail(psnId, email);
              psnPmCoEmailDao.save(coEmail);
            }
          }
          // 保存合作者的英文名.
          this.savePsnPmCoEnName(psnId, iAuthor);
        } else {
          // 保存用户使用过的期刊列表.
          this.savePsnPmJournal(pubId, psnId);
          // 保存用户使用过的关键词列表.
          this.savePsnPmKeywords(pubId, psnId);
        }
      }
    }
  }

  /**
   * 保存用户的成果合作者英文姓名列表.
   * 
   * @param iAuthor
   */
  private void savePsnPmCoEnName(Long psnId, IsiPubMatchAuthor iAuthor) {
    if (StringUtils.isNotBlank(iAuthor.getFullName())) {
      PsnPmCoEnName coEnName = new PsnPmCoEnName();
      coEnName.setPsnId(psnId);
      coEnName.setCoName(StringUtils.substring(iAuthor.getFullName(), 0, 100));
      coEnName.setPrjCo(0);
      coEnName.setPrpCo(0);
      coEnName.setPubCo(1);// 是否成果合作者.
      coEnName.setType(2);// 英文名类别2-全称.
      psnPmCoEnNameDao.save(coEnName);
    }
  }

  /**
   * 保存用户使用过的期刊列表.
   * 
   * @param pubId
   * @param psnId
   */
  private void savePsnPmJournal(Long pubId, Long psnId) {
    // 获取成果的期刊列表.
    List<IsiPubMatchJournal> pubJournalList = isiPubMatchJournalDao.getIsiPubMatchJournalByPubId(pubId);
    if (CollectionUtils.isNotEmpty(pubJournalList)) {
      for (IsiPubMatchJournal pubJournal : pubJournalList) {
        if (StringUtils.isNotBlank(pubJournal.getIssn())) {
          PsnPmJournal journal = new PsnPmJournal();
          journal.setPsnId(psnId);
          journal.setIssn(StringUtils.substring(pubJournal.getIssn(), 0, 100));
          journal.setIssnHash(pubJournal.getIssnHash());
          psnPmJournalDao.save(journal);
        }
      }
    }
  }

  /**
   * 保存用户使用过的关键词列表.
   * 
   * @param pubId
   * @param psnId
   */
  private void savePsnPmKeywords(Long pubId, Long psnId) {
    List<IsiPubKeywordsSplit> pubKeywordList = isiPubKeywordsSplitDao.getIsiPubKwList(pubId);
    if (CollectionUtils.isNotEmpty(pubKeywordList)) {
      for (IsiPubKeywordsSplit pubKeyword : pubKeywordList) {
        PsnPmKeywords keyword = new PsnPmKeywords();
        keyword.setPsnId(psnId);
        String keywordStr = StringUtils.substring(pubKeyword.getKeyword(), 0, 100);
        keyword.setKeyword(keywordStr);
        Long keywordHash = pubKeyword.getKeywordHash();
        if (keywordHash == null) {
          keywordHash = PubHashUtils.getKeywordHash(keywordStr);
        }
        keyword.setKwHash(keywordHash);
        keyword.setPrjKw(0);
        keyword.setPubKw(1);
        keyword.setZtKw(0);
        psnPmKeywordsDao.save(keyword);
      }
    }
  }

  /**
   * 获取作者的邮件地址.
   * 
   * @param iAuthor
   * @param emailList
   * @return
   */
  private String getEmailWithAuthor(IsiPubMatchAuthor iAuthor, List<IsiPubMatchEmail> emailList) {
    String email = null;
    for (IsiPubMatchEmail isiEmail : emailList) {
      if (isiEmail.getSeqNo() != null && iAuthor.getSeqNo() != null
          && isiEmail.getSeqNo().intValue() == iAuthor.getSeqNo().intValue()) {
        email = StringUtils.substring(isiEmail.getEmail(), 0, 100);
      }
    }
    return email;
  }

  /**
   * 获取IsiPubAssign.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  @Override
  public IsiPubAssign getIsiPubAssign(Long pubId, Long insId) {
    return isiPubAssignDao.getIsiPubAssign(pubId, insId);
  }

  /**
   * 查找单位所有已匹配上的成果.
   * 
   * @param insId
   * @return
   */
  @Override
  public List<IsiPubAssign> getIsiPubAssignList(Long insId) {
    return isiPubAssignDao.getIsiPubAssignList(insId);
  }

  /**
   * 根据成果ID获取成果作者的邮件和用户序号列表.
   * 
   * @param pubId
   * @return
   */
  @Override
  public List<IsiPubMatchEmail> getEmailListByPubId(Long pubId) {
    return isiPubMatchEmailDao.getEmailListByPubId(pubId);
  }

  /**
   * 根据人员ID获取匹配的成果ID列表.
   * 
   * @param psnId
   * @return
   */
  @Override
  public List<Long> getPubIdListByPsnId(Long psnId) {
    return isiPubMatchAssignDao.getPubIdListByPsnId(psnId);
  }

  /**
   * 查找单位所有已匹配上的成果ID.
   * 
   * @param insId
   * @return
   */
  @Override
  public List<Long> getIsiPubAssignIdList(Long insId) {
    return isiPubAssignDao.getIsiPubAssignIdList(insId);
  }

  /**
   * 根据邮件获取成果ID列表.
   * 
   * @param email
   * @return
   */
  @Override
  public List<Long> getPubIdListByEmail(String email) {
    return isiPubMatchEmailDao.getPubIdListByEmail(email);
  }

  /**
   * 检查是否已匹配到成果.
   * 
   * @param psnId
   * @return true-已匹配到；false-未匹配到.
   */
  @Override
  public boolean isExistMatchedPub(Long psnId) {
    return isiPubMatchAssignDao.isExistMatchedPub(psnId);
  }

  /**
   * 删除匹配到的未确认的成果.
   * 
   * @param psnId
   */
  @Override
  public void deleteUnFirmPub(Long psnId) {
    isiPubMatchAssignDao.deleteUnFirmPub(psnId);
  }

  /**
   * 保存成果匹配结果记录.
   * 
   * @param assign
   */
  @Override
  public void saveIsiPubMatchAssign(IsiPubMatchAssign assign) {
    isiPubMatchAssignDao.savePubMatchAssign(assign);
  }

  /**
   * 获取成果的作者列表.
   * 
   * @param pubId
   * @return
   */
  @Override
  public List<IsiPubMatchAuthor> getIsiPubMatchAuthorList(Long pubId) {
    return isiPubMatchAuthorDao.getIsiPubMatchAuthorList(pubId);
  }

  /**
   * 根据成果ID获取期刊的ISSN和ISSN的hash值列表.
   * 
   * @param pubId
   * @return
   */
  @Override
  public List<IsiPubMatchJournal> getMatchedJournalList(Long pubId) {
    return isiPubMatchJournalDao.getIsiPubMatchJournalByPubId(pubId);
  }

  /**
   * 根据成果ID获取期刊的关键词hash值列表.
   * 
   * @param pubId
   */
  @Override
  public List<IsiPubMatchKeyword> getPubKWList(Long pubId) {
    return isiPubMatchKeywordDao.getIsiPubMatchKeywordByPubId(pubId);
  }

  @Override
  public void saveIsiPubMatchEmail(IsiPubMatchEmail email) {
    isiPubMatchEmailDao.savePubMatchEmail(email);
  }

  @Override
  public IsiPubMatchEmail getIsiPubMatchEmail(Long pubId, String email) {
    return isiPubMatchEmailDao.getIsiPubMatchEmail(pubId, email);
  }

  @Override
  public void saveIsiPubMatchAuthor(IsiPubMatchAuthor author) {
    isiPubMatchAuthorDao.savePubMatchAuthor(author);
  }

  @Override
  public IsiPubMatchAuthor getIsiPubMatchAuthor(Long pubId, String initName) {
    return isiPubMatchAuthorDao.getIsiPubMatchAuthor(pubId, initName);
  }

  @Override
  public void saveIsiPubMatchJournal(IsiPubMatchJournal journal) {
    isiPubMatchJournalDao.saveIsiPubMatchJournal(journal);
  }

  @Override
  public IsiPubMatchJournal getIsiPubMatchJournal(Long pubId, String issn) {
    return isiPubMatchJournalDao.getIsiPubMatchJournal(pubId, issn);
  }

  @Override
  public void saveIsiPubMatchKeyword(IsiPubMatchKeyword keyword) {
    isiPubMatchKeywordDao.savePubMatchKey(keyword);
  }

  @Override
  public IsiPubMatchKeyword getIsiPubMatchKeyword(Long pubId, Long kwHash) {
    return isiPubMatchKeywordDao.getIsiPubMatchKeyword(pubId, kwHash);
  }

  @Override
  /**
   * 获取和人员名称匹配的单位成果.
   * 
   * @param insId
   * @param psnEnNameList
   * @param size
   * @return
   */
  public List<Long> getMatchAuPubList(Long insId, List<String> psnEnNameList, int size) {
    List<Long> pubIdList = new ArrayList<Long>();
    // 获取作者名称匹配的成果ID.
    List<Long> authorPubIdList = isiPubMatchAuthorDao.getMatchAuthorPubList(psnEnNameList);
    if (CollectionUtils.isNotEmpty(authorPubIdList)) {
      pubIdList = isiPubAssignDao.getIsiPubAssList(insId, authorPubIdList, size);
    }
    return pubIdList;
  }

  /**
   * 保存匹配成果的合作者邮件.
   * 
   * @param psnId
   * @param pubId
   * @param emailMatchedNum
   * @param score
   */
  @Override
  public void updateAssignCoEmail(Long psnId, Long pubId, Integer emailMatchedNum, Integer score) {
    isiPubMatchAssignDao.updateAssignCoEmail(psnId, pubId, emailMatchedNum, score);
  }

  /**
   * 保存匹配成果的合作者名称.
   * 
   * @param psnId
   * @param pubId
   * @param coFNameNum
   * @param coINameNum
   * @param score
   */
  @Override
  public void updateAssignCoName(Long psnId, Long pubId, Integer coFNameNum, Integer coINameNum, Integer score) {
    isiPubMatchAssignDao.updateAssignCoName(psnId, pubId, coFNameNum, coINameNum, score);
  }

  /**
   * 保存匹配成果的期刊.
   * 
   * @param psnId
   * @param pubId
   * @param i
   * @param score
   */
  @Override
  public void updateAssignJnl(Long psnId, Long pubId, Integer jnlNum, Integer score) {
    isiPubMatchAssignDao.updateAssignJnl(psnId, pubId, jnlNum, score);
  }

  /**
   * 保存匹配成果的关键词.
   * 
   * @param psnId
   * @param pubId
   * @param matchedKwNum
   * @param score
   */
  @Override
  public void updateAssignKw(Long psnId, Long pubId, Integer matchedKwNum, Integer score) {
    isiPubMatchAssignDao.updateAssignKw(psnId, pubId, matchedKwNum, score);
  }
}
