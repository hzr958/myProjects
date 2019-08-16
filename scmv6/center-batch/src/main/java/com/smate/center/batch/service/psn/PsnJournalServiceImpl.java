package com.smate.center.batch.service.psn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.rcmd.journal.RcmdPsnJournalDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.BaseJournal;
import com.smate.center.batch.model.rcmd.journal.RcmdPsnJournal;
import com.smate.center.batch.model.sns.psn.FriendExpertJournal;
import com.smate.center.batch.service.pdwh.pub.BaseJournalService;

@Service("psnJournalService")
@Transactional(rollbackFor = Exception.class)
public class PsnJournalServiceImpl implements PsnJournalService {

  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private RcmdPsnJournalDao psnJournalDao;
  @Autowired
  private BaseJournalService baseJournalService;

  @Override
  public int getPsnJnlMaxGrade(Long psnId) throws ServiceException {
    return psnJournalDao.getPsnJnlMaxGrade(psnId);
  }

  @Override
  public List<RcmdPsnJournal> getPsnJournal(Long psnId) throws ServiceException {
    return psnJournalDao.getPsnAllJournal(psnId);
  }

  @Override
  public int getPsnJnlCountByIssn(Long psnId, String issn) throws ServiceException {
    return psnJournalDao.getPsnJnlCountByIssn(psnId, issn);
  }

  @Override
  public List<String> getPsnPubCopartnerByJnl(Long psnId) throws ServiceException {
    return psnJournalDao.getPsnPubCopartnerByJnl(psnId);
  }

  @Override
  public int getPsnFriendJnlCountByIssn(Long psnId, String issn) throws ServiceException {
    return psnJournalDao.getPsnFriendJnlCountByIssn(psnId, issn);
  }

  @Override
  public List<Map<String, Object>> getPsnListByIssn(String issn) throws ServiceException {
    return psnJournalDao.getPsnListByIssn(issn);
  }

  // 获取个人期刊数据
  @Override
  public List<FriendExpertJournal> getFriendExpertJournal(Long psnId) throws ServiceException {

    List<FriendExpertJournal> expertJournal = new ArrayList<FriendExpertJournal>();
    try {
      List<String> journalIssnList = psnJournalDao.getPsnIssnTxtList(psnId);
      if (CollectionUtils.isNotEmpty(journalIssnList)) {
        List<BaseJournal> baseJournalList = baseJournalService.findBaseJournalsByIssns(journalIssnList);
        if (CollectionUtils.isNotEmpty(baseJournalList)) {
          this.wrapFriendExpertJournal(psnId, expertJournal, baseJournalList, journalIssnList,
              LocaleContextHolder.getLocale().toString());
        }
      }
    } catch (Exception e) {
      logger.error("查询个人期刊信息出错", e);
    }
    return expertJournal;
  }

  /**
   * 封闭期刊数据
   * 
   * @author zk
   * 
   * @param expertJournal
   * @param baseJournalList
   * @param journalIssnList
   */
  private void wrapFriendExpertJournal(Long psnId, List<FriendExpertJournal> expertJournal,
      List<BaseJournal> baseJournalList, List<String> journalIssnList, String locale) {
    if (expertJournal == null) {
      expertJournal = new ArrayList<FriendExpertJournal>();
    }
    Map<String, BaseJournal> baseJournalMap = wrapBaseJournalMap(baseJournalList);
    for (String issn : journalIssnList) {

      if (baseJournalMap.get(issn) != null) {
        BaseJournal bj = baseJournalMap.get(issn);
        FriendExpertJournal fej = new FriendExpertJournal();
        fej.setIssn(issn);
        fej.setPsnId(psnId);
        fej.setTitleZh(bj.getTitleXx());
        fej.setTitleEn(bj.getTitleEn());
        if ("zh_CN".equals(locale)) {
          fej.setTitle(bj.getTitleXx());
        } else {
          fej.setTitle(bj.getTitleEn());
        }
        expertJournal.add(fej);
      }

    }
  }

  /**
   * 封装BaseJournal，便于查询
   * 
   * @author zk
   * 
   * @param baseJournalList
   * @return
   */
  private Map<String, BaseJournal> wrapBaseJournalMap(List<BaseJournal> baseJournalList) {
    Map<String, BaseJournal> baseJournalMap = new HashMap<String, BaseJournal>();
    for (BaseJournal baseJournal : baseJournalList) {
      // if (baseJournalMap.get(baseJournal.getPissn()) == null) {
      if (StringUtils.isNotBlank(baseJournal.getTitleEn()) || StringUtils.isNotBlank(baseJournal.getTitleXx())) {
        if (StringUtils.isBlank(baseJournal.getTitleEn())) {
          baseJournal.setTitleEn(baseJournal.getTitleXx());
        }
        if (StringUtils.isBlank(baseJournal.getTitleXx())) {
          baseJournal.setTitleXx(baseJournal.getTitleEn());
        }
        baseJournalMap.put(baseJournal.getPissn(), baseJournal);
        // }
      }
    }
    return baseJournalMap;
  }

  /**
   * 得到有相同期刊的人员
   */
  @Override
  public List<Long> getSameIssnPsnId(String issnStr, List<Long> psnIds) throws ServiceException {

    return psnJournalDao.getSameIssnPsnId(issnStr, psnIds);
  }
}
