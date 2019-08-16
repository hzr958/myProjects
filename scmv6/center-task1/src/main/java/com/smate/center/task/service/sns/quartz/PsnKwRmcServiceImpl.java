package com.smate.center.task.service.sns.quartz;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.DiscKeywordHotDao;
import com.smate.center.task.dao.sns.quartz.JournalGradeDao;
import com.smate.center.task.dao.sns.quartz.KeywordsCommendTaskDao;
import com.smate.center.task.dao.sns.quartz.KwRmcGroupDao;
import com.smate.center.task.dao.sns.quartz.PsnCoRmcRefreshDao;
import com.smate.center.task.dao.sns.quartz.PsnKwNsfcprjDao;
import com.smate.center.task.dao.sns.quartz.PsnKwRmcDao;
import com.smate.center.task.dao.sns.quartz.PsnKwRmcExtDao;
import com.smate.center.task.dao.sns.quartz.PsnKwRmcGidDao;
import com.smate.center.task.dao.sns.quartz.PsnKwRmcRefreshDao;
import com.smate.center.task.dao.sns.quartz.PsnKwRmcSyncDao;
import com.smate.center.task.dao.sns.quartz.PsnKwRmcTmpDao;
import com.smate.center.task.dao.sns.quartz.PsnKwZtDao;
import com.smate.center.task.dao.sns.quartz.PubKeyWordsDao;
import com.smate.center.task.dao.sns.quartz.PubMemberDao;
import com.smate.center.task.dao.sns.quartz.PublicationDao;
import com.smate.center.task.dao.sns.quartz.RecmdKwDropHistoryDao;
import com.smate.center.task.dao.sns.quartz.RermcPsnKwHotDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.KwRmcGroup;
import com.smate.center.task.model.sns.quartz.PsnCoRmcRefresh;
import com.smate.center.task.model.sns.quartz.PsnKwRmc;
import com.smate.center.task.model.sns.quartz.PsnKwRmcGid;
import com.smate.center.task.model.sns.quartz.PsnKwRmcTmp;
import com.smate.center.task.model.sns.quartz.PubKeyWords;
import com.smate.center.task.model.sns.quartz.PubMember;
import com.smate.center.task.model.sns.quartz.Publication;

@Service("psnKwRmcService")
@Transactional(rollbackFor = Exception.class)
public class PsnKwRmcServiceImpl implements PsnKwRmcService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnKwRmcRefreshDao psnKwRmcRefreshDao;
  @Autowired
  private PsnKwRmcTmpDao psnKwRmcTmpDao;
  @Autowired
  private KeywordsCommendTaskDao keywordsCommendTaskDao;
  @Autowired
  private PubKeyWordsDao pubKeyWordsDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PubMemberDao pubMemberDao;
  @Autowired
  private JournalGradeDao journalGradeDao;
  @Autowired
  private AnalysisKeyWordsService analysisKeyWordsService;
  @Autowired
  private PsnKwNsfcprjDao psnKwNsfcprjDao;
  @Autowired
  private PsnKwZtDao psnKwZtDao;
  @Autowired
  private PsnKwRmcDao psnKwRmcDao;
  @Autowired
  private PsnKwRmcExtDao psnKwRmcExtDao;
  @Autowired
  private PsnKwRmcGidDao psnKwRmcGidDao;
  @Autowired
  private KwRmcGroupDao kwRmcGroupDao;
  @Autowired
  private RermcPsnKwHotDao rermcPsnKwHotDao;
  @Autowired
  private RecmdKwDropHistoryDao recmdKwDropHistoryDao;
  @Autowired
  private DiscKeywordHotDao discKeywordHotDao;
  @Autowired
  private PsnKwRmcSyncDao psnKwRmcSyncDao;
  @Autowired
  private PsnCoRmcRefreshDao psnCoRmcRefreshDao;
  @Autowired
  private PsnKwRmcTmpService psnKwRmcTmpService;;

  @Override
  public List<Long> getRefreshData() {
    return psnKwRmcRefreshDao.getRefreshData();
  }

  @Override
  public void updateRefreshData(Long psnId) {
    psnKwRmcRefreshDao.updateByPsnId(psnId);
  }

  @Override
  public int getRefreshFlag() {
    return keywordsCommendTaskDao.getPsnKwRmcTask();
  }

  @Override
  public void updateRefreshFlag() {
    keywordsCommendTaskDao.updatePsnKwRmcFlag();
  }

  @Override
  public void deleteFromTmp() {
    psnKwRmcTmpDao.deleteAll();
  }

  public void handlePsnKwRmc(Long psnId) throws ServiceException {
    try {
      psnKwRmcRefreshDao.deleteByPsnid(psnId);
      if (psnKwRmcSyncDao.queryCount(psnId) == 0) {
        psnKwRmcSyncDao.addNew(psnId);
      }
      if (psnCoRmcRefreshDao.queryCount(psnId) == 0) {
        PsnCoRmcRefresh psnCoRmcRefresh = new PsnCoRmcRefresh();
        psnCoRmcRefresh.setPsnId(psnId);
        psnCoRmcRefresh.setMkAt(new Date());
        psnCoRmcRefresh.setStatus(0);
        psnCoRmcRefreshDao.save(psnCoRmcRefresh);
      } else {
        psnCoRmcRefreshDao.updatePsnCoRmcRefresh(psnId);
      }
    } catch (Exception e) {
      logger.error("更新psnId为" + psnId + "时出错", e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void HandlesupplementPsnKwRmc(Long psnId) throws ServiceException {
    // --先删除前面反推的数据
    rermcPsnKwHotDao.deleteByPsnId(psnId);
    // --计算psnid现有多少推荐的关键词
    int count = psnKwRmcDao.getCount(psnId);
    int maxSize = 0;
    int vCount = 0;
    if (count <= 50) {
      int tCount = recmdKwDropHistoryDao.getDropCount(psnId);
      int tCount1 = psnKwRmcDao.getZtCount(psnId);
      maxSize = maxSize + tCount + tCount1;
    }
    // --计算需要再取多少个
    maxSize = maxSize + 50 - count;
    List<BigDecimal> psnKwRmcList = psnKwRmcDao.getHotKid(psnId, maxSize);
    for (BigDecimal hotkId : psnKwRmcList) {
      Long hotKwCount = discKeywordHotDao.queryHotKwCount(hotkId.longValue(), psnId);
      if (hotKwCount != 0L) {
        // --将数据保存到反推热词表中
        rermcPsnKwHotDao.addNewRermcPsnKwHot(hotkId.longValue(), psnId);
      }
      vCount++;
      if (vCount > 50 - count) {
        break;
      }
    }

  }

  @Override
  public void handleKwRcmdScore(Long psnId) throws ServiceException {
    psnKwRmcTmpDao.updateScore(psnId);
    psnKwRmcTmpDao.updateScoreType2(psnId);
    psnKwRmcTmpDao.updateScoreType1(psnId);
  }

  @Override
  public void ItoratorEnKw(Long psnId) throws ServiceException {
    // --前40个关键词,20个中文，20个英文
    List<PsnKwRmcTmp> enKwRmcTmpList = psnKwRmcTmpDao.queryEnKw(psnId);
    try {
      for (PsnKwRmcTmp enTmp : enKwRmcTmpList) {
        try {
          Long count = kwRmcGroupDao.queryKwCount(enTmp.getKeyWordTxt());
          Long gid = 0L;
          if (count == 0) {
            KwRmcGroup KwRmcGroup = new KwRmcGroup();
            gid = kwRmcGroupDao.querySeq();
            KwRmcGroup.setGid(gid);
            KwRmcGroup.setKwTxt(enTmp.getKeyWordTxt());
            kwRmcGroupDao.save(KwRmcGroup);
          } else {
            gid = kwRmcGroupDao.queryGid(enTmp.getKeyWordTxt());
          }

          PsnKwRmc psnKwRmc = BuildPsnKwRmc(enTmp, gid);
          if (psnKwRmcDao.getCountByPsnIdKW(psnId, psnKwRmc.getKeywordTxt()) < 1) {
            psnKwRmcDao.save(psnKwRmc);
          } ;

          String userGid = ",";
          String str = "," + gid + ",";
          if (userGid.indexOf(str) <= 0) {
            userGid = "," + userGid + gid + ",";
            Long psnKwRmcGidCount = psnKwRmcGidDao.getCount(psnId, gid);
            if (psnKwRmcGidCount < 1) {
              PsnKwRmcGid psnKwGid = new PsnKwRmcGid();
              psnKwGid.setGid(gid);
              psnKwGid.setPsnId(psnId);
              psnKwRmcGidDao.save(psnKwGid);
            }
          }
        } catch (Exception e) {
          logger.error("更新临时表中的英文数据到关键词表中出错", e);
        }

      }

    } catch (Exception e) {
      logger.error("更新临时表中的英文数据到关键词表中出错", e);
      throw new ServiceException(e);
    }

    try {
      psnKwRmcExtDao.saveNewFromRmc(psnId);
      psnKwRmcExtDao.saveNewFromRmcEn(psnId);
      psnKwRmcExtDao.saveNewFromKwSyn(psnId);
    } catch (Exception e) {
      logger.error("psnKwRmcExtDao保存关键词出错!,psnId:" + psnId, e);
    }

  }

  @Override
  public void ItoratorZhKw(Long psnId) throws ServiceException {
    // --前40个关键词,20个中文，20个英文
    List<PsnKwRmcTmp> zhKwRmcTmpList = psnKwRmcTmpDao.queryZhKw(psnId);
    try {
      for (PsnKwRmcTmp zhTmp : zhKwRmcTmpList) {
        try {
          Long count = kwRmcGroupDao.queryKwCount(zhTmp.getKeyWordTxt());
          Long gid = 0L;
          if (count == 0) {
            KwRmcGroup KwRmcGroup = new KwRmcGroup();
            gid = kwRmcGroupDao.querySeq();
            KwRmcGroup.setGid(gid);
            KwRmcGroup.setKwTxt(zhTmp.getKeyWordTxt());
            kwRmcGroupDao.save(KwRmcGroup);
          } else {
            gid = kwRmcGroupDao.queryGid(zhTmp.getKeyWordTxt());
          }

          PsnKwRmc psnKwRmc = BuildPsnKwRmc(zhTmp, gid);
          if (psnKwRmcDao.getCountByPsnIdKW(psnId, psnKwRmc.getKeywordTxt()) < 1) {
            psnKwRmcDao.save(psnKwRmc);
          }
          String userGid = ",";
          String str = "," + gid + ",";
          if (userGid.indexOf(str) <= 0) {
            Long psnKwRmcGidCount = psnKwRmcGidDao.getCount(psnId, gid);
            if (psnKwRmcGidCount < 1) {
              userGid = "," + userGid + gid + ",";
              PsnKwRmcGid psnKwGid = new PsnKwRmcGid();
              psnKwGid.setGid(gid);
              psnKwGid.setPsnId(psnId);
              psnKwRmcGidDao.save(psnKwGid);
            }
          }
        } catch (Exception e) {
          logger.error("更新临时表中的英文数据到关键词表中出错", e);
        }

      }

    } catch (Exception e) {
      logger.error("更新临时表中的英文数据到关键词表中出错", e);
      throw new ServiceException(e);
    }

  }

  private PsnKwRmc BuildPsnKwRmc(PsnKwRmcTmp zhTmp, Long gid) {
    PsnKwRmc psnKwRmc = new PsnKwRmc();
    psnKwRmc.setId(zhTmp.getId());
    psnKwRmc.setPsnId(zhTmp.getPsnId());
    psnKwRmc.setKeywordTxt(zhTmp.getKeyWordTxt());
    psnKwRmc.setKeyword(zhTmp.getKeyWord());
    psnKwRmc.setWordNum(zhTmp.getWordNum());
    psnKwRmc.setType(zhTmp.getType());
    psnKwRmc.setZtTf(zhTmp.getZtTf());
    psnKwRmc.setPrjTf(zhTmp.getPrjTf());
    psnKwRmc.setPubTf(zhTmp.getPubTf());
    psnKwRmc.setAuPos(zhTmp.getAuPos());
    psnKwRmc.setAuSeq(zhTmp.getAuSeq());
    psnKwRmc.setHxj(zhTmp.getHxj());
    psnKwRmc.setGrade1(zhTmp.getGrade1());
    psnKwRmc.setGrade2(zhTmp.getGrade2());
    psnKwRmc.setGrade3(zhTmp.getGrade3());
    psnKwRmc.setScore(zhTmp.getScore());
    psnKwRmc.setKwGid(gid);

    return psnKwRmc;

  }

  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
  private void HandlePsnKwRcmdTmp(Long psnId) {
    // 插入成果关键词到临时表
    // savePubKw(psnId);
    savePubKeyWord(psnId);
    // 插入项目关键词到临时表
    savePrjKwNsfc(psnId);
    // 插入
    saveZtKw(psnId);
    // 补充项目关键词个数
    psnKwRmcTmpDao.updatePrjKwNum();
    // 补充自填关键词标记
    psnKwRmcTmpDao.updateZtKwNum();
    // --标记等级
    psnKwRmcTmpDao.updateGrade1();
    psnKwRmcTmpDao.updateGrade2();
    psnKwRmcTmpDao.updateGrade3();
  }

  private void savePubKeyWord(Long psnId) {
    psnKwRmcTmpDao.addNewZhToPubKwEptTmp(psnId);
    psnKwRmcTmpDao.updateNewPubZhkwByPsnId(psnId);
    psnKwRmcTmpDao.addNewEnToPubKwEptTmp(psnId);
    psnKwRmcTmpDao.updateNewPubEnkwByPsnId(psnId);
    List<PsnKwRmcTmp> psnKwRmcTmpLists = psnKwRmcTmpDao.getPsnKwEptTemp(psnId);
    for (PsnKwRmcTmp psnKwRmcTmp : psnKwRmcTmpLists) {
      psnKwRmcTmpDao.updatePsnKwEptTemp(psnKwRmcTmp.getId(), this.getKwNum(psnKwRmcTmp.getKeyWordTxt()));
    }
  }

  private void savePubKw(Long psnId) {
    // 插入成果关键词到临时表
    List<PubKeyWords> pubKeyWords = pubKeyWordsDao.getkeyWords(psnId);
    for (PubKeyWords keyWords : pubKeyWords) {
      keyWords = getzhKeywords(keyWords);
      if (keyWords == null) {
        continue;
      }
      if (psnKwRmcTmpDao.getPsnKwRmcTmp(psnId, keyWords.getKeywordTxt())) {
        continue;
      }
      PsnKwRmcTmp tmp = buildPsnKwRmcTmp(keyWords);
      psnKwRmcTmpDao.save(tmp);
    }

  }

  private void saveZtKw(Long psnId) {
    List<Object[]> object = psnKwZtDao.getEnPsnKwZt(psnId);
    for (Object[] a : object) {
      String zhKwTxt = a[1].toString();
      int prjTf = NumberUtils.toInt(a[2].toString());
      if (psnKwRmcTmpDao.getPsnKwRmcTmp(psnId, zhKwTxt)) {
        continue;
      }
      PsnKwRmcTmp tmp = new PsnKwRmcTmp();
      tmp.setPsnId(psnId);
      tmp.setKeyWord(zhKwTxt);
      tmp.setPrjTf(prjTf);
      tmp.setKeyWordTxt(zhKwTxt);
      tmp.setZtTf(1);
      tmp.setWordNum(this.getKwNum(zhKwTxt));
      tmp.setGrade1(0);
      tmp.setGrade2(0);
      tmp.setGrade3(0);
      tmp.setAuPos(0);
      tmp.setAuSeq(0);
      tmp.setHxj(0);
      tmp.setScore(0.0);
      tmp.setType(2);
      psnKwRmcTmpDao.save(tmp);
    }
    List<Object[]> object1 = psnKwZtDao.getZhPsnKwZt(psnId);
    for (Object[] a : object1) {
      String zhKwTxt = a[1].toString();
      int prjTf = NumberUtils.toInt(a[2].toString());
      if (psnKwRmcTmpDao.getPsnKwRmcTmp(psnId, zhKwTxt)) {
        continue;
      }
      PsnKwRmcTmp tmp = new PsnKwRmcTmp();
      tmp.setPsnId(psnId);
      tmp.setKeyWord(zhKwTxt);
      tmp.setPrjTf(prjTf);
      tmp.setKeyWordTxt(zhKwTxt);
      tmp.setZtTf(1);
      tmp.setWordNum(this.getKwNum(zhKwTxt));
      tmp.setGrade1(0);
      tmp.setGrade2(0);
      tmp.setGrade3(0);
      tmp.setAuPos(0);
      tmp.setAuSeq(0);
      tmp.setHxj(0);
      tmp.setScore(0.0);
      tmp.setType(2);
      psnKwRmcTmpDao.save(tmp);
    }
  }

  private void savePrjKwNsfc(Long psnId) {
    // 插入中文关键词
    List<Object[]> object = psnKwNsfcprjDao.getPsnKwNsfcprj(psnId);
    for (Object[] a : object) {
      String zhKwTxt = a[1].toString();
      int prjTf = NumberUtils.toInt(a[2].toString());
      if (psnKwRmcTmpDao.getPsnKwRmcTmp(psnId, zhKwTxt)) {
        continue;
      }
      PsnKwRmcTmp tmp = new PsnKwRmcTmp();
      tmp.setPsnId(psnId);
      tmp.setKeyWord(zhKwTxt);
      tmp.setPrjTf(prjTf);
      tmp.setKeyWordTxt(zhKwTxt);
      tmp.setZtTf(0);
      tmp.setWordNum(this.getKwNum(zhKwTxt));
      tmp.setGrade1(0);
      tmp.setGrade2(0);
      tmp.setGrade3(0);
      tmp.setAuPos(0);
      tmp.setAuSeq(0);
      tmp.setHxj(0);
      tmp.setScore(0.0);
      tmp.setType(2);
      psnKwRmcTmpDao.save(tmp);
    }

    // 插入英文关键词
    List<Object[]> object1 = psnKwNsfcprjDao.getEnPsnKwNsfcprj(psnId);
    for (Object[] a : object1) {
      String zhKwTxt = a[1].toString();
      int prjTf = NumberUtils.toInt(a[2].toString());
      if (psnKwRmcTmpDao.getPsnKwRmcTmp(psnId, zhKwTxt)) {
        continue;
      }
      PsnKwRmcTmp tmp = new PsnKwRmcTmp();
      tmp.setPsnId(psnId);
      tmp.setKeyWord(zhKwTxt);
      tmp.setPrjTf(prjTf);
      tmp.setKeyWordTxt(zhKwTxt);
      tmp.setZtTf(0);
      tmp.setWordNum(this.getKwNum(zhKwTxt));
      tmp.setGrade1(0);
      tmp.setGrade2(0);
      tmp.setGrade3(0);
      tmp.setAuPos(0);
      tmp.setAuSeq(0);
      tmp.setHxj(0);
      tmp.setScore(0.0);
      tmp.setType(2);
      psnKwRmcTmpDao.save(tmp);
    }

  }

  private PsnKwRmcTmp buildPsnKwRmcTmp(PubKeyWords keyWords) {
    PsnKwRmcTmp tmp = new PsnKwRmcTmp();
    tmp.setAuPos(keyWords.getAuPos());
    tmp.setAuSeq(keyWords.getAuSeq());
    tmp.setGrade1(0);
    tmp.setGrade2(0);
    tmp.setGrade3(0);
    tmp.setHxj(keyWords.getHxj());
    tmp.setKeyWord(keyWords.getKeyword());
    tmp.setKeyWordTxt(keyWords.getKeywordTxt());
    tmp.setPrjTf(0);
    tmp.setZtTf(2);
    tmp.setWordNum(getKwNum(keyWords.getKeyword()));
    tmp.setScore(0.0);
    tmp.setType(2);
    tmp.setPsnId(keyWords.getPsnId());
    return tmp;
  }

  private PubKeyWords getzhKeywords(PubKeyWords keyWords) {
    Publication pub = publicationDao.getPubBasicRcmdInfo(keyWords.getPubId(), 2007);
    if (pub == null) {
      return null;
    }
    keyWords.setPublishYear(pub.getPublishYear());
    List<PubMember> pubMemberList = pubMemberDao.getPubMemberList(keyWords.getPubId());
    PubMember pubMember = analysisKeyWordsService.matchOwnerPubMember(keyWords.getPsnId(), pubMemberList);
    int auSeq = 0;
    int auPos = 0;
    int hxj = 0;
    if (pubMember != null) {
      auPos = pubMember.getAuthorPos() == null ? 0 : pubMember.getAuthorPos();
      auSeq = pubMember.getSeqNo() == null ? 0 : pubMember.getSeqNo();
    }
    int grad = journalGradeDao.getHxj(keyWords.getPubId()) == null ? 0
        : NumberUtils.toInt(journalGradeDao.getHxj(keyWords.getPubId()).toString());
    if (grad <= 3) {
      hxj = 1;
    }
    keyWords.setAuPos(auPos);
    keyWords.setAuSeq(auSeq);
    keyWords.setHxj(hxj);
    analysisKeyWordsService.buildkw(keyWords);
    if (keyWords.getZhKwLen() < 2 || keyWords.getZhKwLen() == 2) {
      return null;
    }
    Double weight = 0D;
    // ISI/CNKI核心期刊+第123/通信作者=1，单非=0.618，双非=0.618×0.618
    if ((auSeq > 0 && (auSeq <= 3 || auPos == 1)) && hxj == 1) {
      weight = 1D;
    } else if (auSeq > 0 && ((auSeq <= 3 || auPos == 1) || hxj == 1)) {
      weight = 0.618;
    } else if (auSeq > 0) {
      weight = 0.618 * 0.618;
    }
    keyWords.setWeight(weight);
    return keyWords;
  }

  public int getKwNum(String keywords) {
    int type = 2;
    int blankNum = 0;
    int wordNum = 1;
    if (StringUtils.isBlank(keywords)) {
      return 0;
    }
    if (keywords.getBytes().length != keywords.length()) {// 包含中文字符
      type = 1;
    }
    if (type == 1) {
      Pattern pattern = Pattern.compile("\\s+");
      Matcher matcher = pattern.matcher(keywords);
      while (matcher.find()) {
        blankNum++;
      }
      if (blankNum > 0) {
        wordNum = blankNum + 1;
      }
    } else if (type == 2) {
      Pattern pattern = Pattern.compile("([a-z]|[0-9]|\\-){2,}");
      Matcher matcher = pattern.matcher(keywords);
      while (matcher.find()) {
        wordNum++;
      }
      int length = keywords.replaceAll("([a-z]|[0-9]|\\-){2,}", "").length();
      wordNum = wordNum + length;
    }
    return wordNum;

  }

  @Override
  public List<Long> getPsnIdList(Long startPsnId, Integer maxSize) {
    return psnKwRmcDao.getPsnIdList(startPsnId, maxSize);
  }

}
