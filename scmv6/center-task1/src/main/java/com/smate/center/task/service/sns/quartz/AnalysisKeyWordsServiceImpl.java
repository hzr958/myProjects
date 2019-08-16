package com.smate.center.task.service.sns.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.sns.quartz.JournalGradeDao;
import com.smate.center.task.dao.sns.quartz.KeywordsCommendDao;
import com.smate.center.task.dao.sns.quartz.KeywordsEnTranZhDao;
import com.smate.center.task.dao.sns.quartz.KeywordsZhTranEnDao;
import com.smate.center.task.dao.sns.quartz.PsnKwEptDao;
import com.smate.center.task.dao.sns.quartz.PsnKwEptGidDao;
import com.smate.center.task.dao.sns.quartz.PsnKwEptGroupDao;
import com.smate.center.task.dao.sns.quartz.PsnKwEptRefreshDao;
import com.smate.center.task.dao.sns.quartz.PsnKwEptTmpDao;
import com.smate.center.task.dao.sns.quartz.PsnKwNsfcprjDao;
import com.smate.center.task.dao.sns.quartz.PubKeyWordsDao;
import com.smate.center.task.dao.sns.quartz.PubMemberDao;
import com.smate.center.task.dao.sns.quartz.PublicationDao;
import com.smate.center.task.dao.sns.quartz.PublicationListDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.model.sns.quartz.KeywordsEnTranZh;
import com.smate.center.task.model.sns.quartz.KeywordsZhTranEn;
import com.smate.center.task.model.sns.quartz.PsnKwEpt;
import com.smate.center.task.model.sns.quartz.PsnKwEptGid;
import com.smate.center.task.model.sns.quartz.PsnKwEptTmp;
import com.smate.center.task.model.sns.quartz.PubKeyWords;
import com.smate.center.task.model.sns.quartz.PubMember;
import com.smate.center.task.model.sns.quartz.PublicationList;
import com.smate.center.task.single.util.pub.PsnPmIsiNameUtils;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.dao.security.PsnPrivateDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.model.security.Person;

/**
 * 解析关键词
 * 
 * @author Administrator
 *
 */
@Service("analysisKeyWordsService")
@Transactional(rollbackFor = Exception.class)
public class AnalysisKeyWordsServiceImpl implements AnalysisKeyWordsService {
  Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnKwEptRefreshDao psnKwEptRefreshDao;
  @Autowired
  private PsnKwEptDao psnKwEptDao;
  @Autowired
  private PsnKwEptGidDao psnKwEptGidDao;
  @Autowired
  private PubKeyWordsDao pubKeyWordsDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PublicationListDao publicationListDao;
  @Autowired
  private PsnKwEptTmpDao psnKwEptTmpDao;
  @Autowired
  private PsnKwNsfcprjDao psnKwNsfcprjDao;
  @Autowired
  private KeywordsCommendDao keywordsCommendDao;
  @Autowired
  private PsnKwEptGroupDao psnKwEptGroupDao;
  @Autowired
  private PsnPrivateDao psnPrivateDao;
  @Autowired
  private JournalGradeDao journalGradeDao;
  @Autowired
  private PubMemberDao pubMemberDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private KeywordsEnTranZhDao keywordsEnTranZhDao;
  @Autowired
  private KeywordsZhTranEnDao keywordsZhTranEnDao;
  @Autowired
  private ParsePsnKeyWordsService parsePsnKeyWordsService;

  @Override
  public void analyzeKeyWords(Long psnId) throws ServiceException {
    boolean isPrivate = psnPrivateDao.existsPsnPrivate(psnId);
    int result = 1;
    try {
      if (isPrivate) {
        psnKwEptRefreshDao.deleteByPsnId(psnId);
        psnKwEptDao.deleteBypsnId(psnId);
        psnKwEptGidDao.deleteBypsnId(psnId);
      } else {
        parsePsnKeyWordsService.parseSourceType(psnId);
        parsePsnKeyWordsService.updatePsnKwTmp(psnId);
        parsePsnKeyWordsService.statPsnKeywords(psnId);
        // 因为现在的数据都是从sns中去取而不是从rcmd中取的，所以不能和存储过程中的逻辑一样了
        // parsePsnKeyWordsService.parsePsnKeywords(psnId, result);
        // 解析人员热词到keywords_psn_hot
        parsePsnKeywordsHot(psnId, result);
        psnKwEptRefreshDao.deleteByPsnId(psnId);

      }
    } catch (Exception e) {
      psnKwEptRefreshDao.deleteByPsnId(psnId);
      logger.error("解析关键词时出错", e);
      throw new ServiceException(e);
    }
  }

  private void parsePsnKeywordsHot(Long psnId, int result) throws ServiceException {

    try {
      result = 1;
      // 删除之前人员熟悉的关键词
      psnKwEptDao.deleteBypsnId(psnId);
      psnKwEptGidDao.deleteBypsnId(psnId);
      // 保留人员前50个关键词
      List<PsnKwEptTmp> psnKwEptList = psnKwEptTmpDao.getPsnKwByPsnId(psnId);
      if (CollectionUtils.isEmpty(psnKwEptList)) {
        return;
      }
      for (PsnKwEptTmp psnKwEptTmp : psnKwEptList) {
        try {
          Long count = psnKwEptGroupDao.getCount(psnKwEptTmp.getKeyWordTxt());
          Long gid;
          if (count == 0) {
            Long seq = psnKwEptGroupDao.querySeq();
            gid = psnKwEptGroupDao.querySeq1();
            psnKwEptGroupDao.addNew(seq, psnKwEptTmp.getKeyWordTxt(), gid);
          } else {
            gid = psnKwEptGroupDao.queryGid(psnKwEptTmp.getKeyWordTxt());
          }
          PsnKwEpt psnKwEpt = buildPsnKwEpt(psnKwEptTmp, gid);
          psnKwEptDao.save(psnKwEpt);
          PsnKwEptGid psnKwEptGid = new PsnKwEptGid();
          psnKwEptGid.setGid(gid);
          psnKwEptGid.setPsnId(psnKwEptTmp.getPsnId());
          psnKwEptGidDao.save(psnKwEptGid);
        } catch (Exception e) {
          logger.error("解析人员热词到到PsnKwEpt失败" + psnKwEptTmp.getKeyWord(), e);
        }

      }

    } catch (Exception e) {
      result = 0;
      logger.error("解析人员热词到到PsnKwEpt", e);
      throw new ServiceException(e);
    }

  }


  private PsnKwEpt buildPsnKwEpt(PsnKwEptTmp psnKwEptTmp, Long gid) {
    PsnKwEpt psnKwEpt = new PsnKwEpt();
    psnKwEpt.setKeyword(psnKwEptTmp.getKeyWord());
    psnKwEpt.setKeywordGid(gid);
    psnKwEpt.setKwTxt(psnKwEptTmp.getKeyWordTxt());
    psnKwEpt.setPsnId(psnKwEptTmp.getPsnId());
    psnKwEpt.setPt1Num(psnKwEptTmp.getPt1Num());
    psnKwEpt.setPt1Num(psnKwEptTmp.getPt2Num());
    psnKwEpt.setPt1Num(psnKwEptTmp.getPt3Num());
    psnKwEpt.setPrjNum(psnKwEptTmp.getPrjNum());
    psnKwEpt.setScore(psnKwEptTmp.getScore());
    psnKwEpt.setScoreSum(0D);
    return psnKwEpt;

  }

  /**
   * 获取人员所有关键词 插入到临时表PSN_KW_EPT_TMP中
   * 
   * @param psnId
   * @param result
   */
  private void parsePsnKeywords(Long psnId, int result) throws Exception {
    result = 1;
    try {
      List<PubKeyWords> rcmdPubKeyWordList = pubKeyWordsDao.getPubKeyWord(psnId);
      for (PubKeyWords rcmdPubKeyword : rcmdPubKeyWordList) {
        // 之前是在
        // and t.hxj=1 and (t.au_seq between 1 and 3) or t.au_pos =1)
        // and t.zh_kw_len>0
        int hxj = 0;
        int grad = journalGradeDao.getHxj(rcmdPubKeyword.getPubId()) == null ? 0
            : NumberUtils.toInt(journalGradeDao.getHxj(rcmdPubKeyword.getPubId()).toString());
        if (grad <= 3) {
          hxj = 1;
        }
        List<PubMember> pubMemberList = pubMemberDao.getPubMemberList(rcmdPubKeyword.getPubId());
        PubMember pubMember = matchOwnerPubMember(psnId, pubMemberList);
        int auSeq = 0;
        int auPos = 0;
        if (pubMember != null) {
          auPos = pubMember.getAuthorPos() == null ? 0 : pubMember.getAuthorPos();
          auSeq = pubMember.getSeqNo() == null ? 0 : pubMember.getSeqNo();
        }

        buildkw(rcmdPubKeyword);
        if (rcmdPubKeyword.getZhKwLen() > 0) {
          if (hxj == 1 && ((1 <= auSeq && auSeq <= 3) || auPos == 1)) {
            psnKwEptTmpDao.insertNewPubZhkw(psnId);
            psnKwEptTmpDao.updatePubZhkwByPsnId(psnId, rcmdPubKeyword.getZhKw(), rcmdPubKeyword.getKeywordTxt(),
                rcmdPubKeyword.getPubId());
          }

          psnKwEptTmpDao.insertNewPrjZhkw(psnId);
          psnKwEptTmpDao.updatePrjZhkwByPsnId(psnId);
          updatePsnKwEptTempNum(psnId);
          int sourceType = parsePubSourceType(rcmdPubKeyword.getPubId());
          psnKwEptTmpDao.updatePsnKwEptTemp(psnId, sourceType);
        }
        if (rcmdPubKeyword.getEnKwLen() > 0) {
          if (hxj == 1 && ((1 <= auSeq && auSeq <= 3) || auPos == 1)) {
            psnKwEptTmpDao.insertNewPubEnkw(psnId);
            psnKwEptTmpDao.updatePubEnkwByPsnId(psnId, rcmdPubKeyword.getEnKw(), rcmdPubKeyword.getKeywordTxt(),
                rcmdPubKeyword.getPubId());
          }
          psnKwEptTmpDao.insertNewPrjEnkw(psnId);
          psnKwEptTmpDao.updatePrjEnkwByPsnId(psnId);
          updatePsnKwEptTempNum(psnId);
          int sourceType = parsePubSourceType(rcmdPubKeyword.getPubId());
          psnKwEptTmpDao.updatePsnKwEptTemp(psnId, sourceType);
        }



      }
    } catch (Exception e) {
      result = 0;
      logger.error("在更新关键词到临时表PSN_KW_EPT_TMP中时出错,人员id是" + psnId, e);
      throw new Exception(e);
    }
  }

  private void updatePsnKwEptTempNum(Long psnId) {
    try {
      List<Object[]> kewordsList = psnKwEptTmpDao.getKeyWordsByPsnId(psnId);
      if (CollectionUtils.isEmpty(kewordsList)) {
        return;
      }
      for (Object[] keyword : kewordsList) {
        Long kwId = (Long) keyword[0];
        String keywordTxt = (String) keyword[1];
        double pubScore = 0;
        double prjSocre = 0;
        double score = 0;
        Long kId = 0L;
        Long pt1Num = psnKwEptTmpDao.getPubKeyWordCount(1, keywordTxt, psnId);
        Long pt2Num = psnKwEptTmpDao.getPubKeyWordCount(2, keywordTxt, psnId);
        Long pt3Num = psnKwEptTmpDao.getPubKeyWordCount(3, keywordTxt, psnId);
        Long prjNum = psnKwNsfcprjDao.getPrjkeywordCount(keywordTxt, psnId);
        if (pt1Num > 0 || pt2Num > 0 || pt3Num > 0) {
          pubScore = Math.min(
              Math.log10(Math.min((float) pt1Num, 5.5) + Math.min(pt2Num * 0.5, 3) + Math.min(pt3Num * 0.2, 1.5)), 1.0);
        }
        if (prjNum > 0) {
          prjSocre = 1 + Math.min(Math.log10(prjNum), 1);
        }
        score = prjSocre * 0.5 + pubScore * 0.5;// 总分
        int commendCount = keywordsCommendDao.getCommendCount(keywordTxt);
        if (commendCount > 0) {
          kId = keywordsCommendDao.getId(keywordTxt);
        }
        psnKwEptTmpDao.updateScore(pt1Num, pt2Num, pt3Num, prjNum, score, kId, kwId);
      }
    } catch (Exception e) {
      logger.error("计算关键词分数出错psnId=" + psnId, e);
      throw new ServiceException(e);
    }

  }

  private int parsePubSourceType(Long pubId) {
    PublicationList pubList = publicationListDao.getPublicationList(pubId);
    int sortType = 0;
    if (pubList == null) {
      sortType = 3;
    } else {
      if (pubList.getListSci() == 1 || pubList.getListSsci() == 1) {
        sortType = 1;
      } else if (pubList.getListEi() == 1 || pubList.getListIstp() == 1) {
        sortType = 2;
      } else {
        if (publicationDao.get(pubId) != null) {
          sortType = 3;
        } else {
          int SourceDbId = publicationDao.getSourceDbId(pubId);
          if (SourceDbId == 8) {
            sortType = 2;
          } else {
            sortType = 3;
          }

        }
      }

    }
    return sortType;
  }

  @Override
  public void buildkw(PubKeyWords rcmdPubKeyword) {
    // 先判断成果关键词类型
    boolean enFlag = false;
    boolean zhFlag = false;
    if (StringUtils.isAsciiPrintable(rcmdPubKeyword.getKeyword())) {
      enFlag = true;
    } else {
      zhFlag = true;
    }
    if (enFlag && zhFlag) {
      return;
    }
    if (enFlag && zhFlag) {
      if (StringUtils.isAsciiPrintable(rcmdPubKeyword.getKeyword())) {
        rcmdPubKeyword.setEnKw(rcmdPubKeyword.getKeyword());
        rcmdPubKeyword.setEnKwTxt(rcmdPubKeyword.getKeywordTxt());
        rcmdPubKeyword.setEnKwLen(getKwWordLength(rcmdPubKeyword.getKeywordTxt()));
      } else {
        rcmdPubKeyword.setZhKw(rcmdPubKeyword.getKeyword());
        rcmdPubKeyword.setZhKwTxt(rcmdPubKeyword.getKeywordTxt());
        rcmdPubKeyword.setZhKwLen(getKwWordLength(rcmdPubKeyword.getKeywordTxt()));
      }
    } else if (enFlag) {
      rcmdPubKeyword.setEnKw(rcmdPubKeyword.getKeyword());
      rcmdPubKeyword.setEnKwTxt(rcmdPubKeyword.getKeywordTxt());
      rcmdPubKeyword.setEnKwLen(getKwWordLength(rcmdPubKeyword.getKeywordTxt()));
      // 把英文翻译成中文
      KeywordsEnTranZh tranZhKw = keywordsEnTranZhDao.findEnTranZhKw(rcmdPubKeyword.getKeywordTxt());
      if (tranZhKw != null) {
        rcmdPubKeyword.setZhKw(tranZhKw.getZhKw());
        rcmdPubKeyword.setZhKwTxt(tranZhKw.getZhKwTxt());
        rcmdPubKeyword.setZhKwLen(getKwWordLength(tranZhKw.getZhKw()));
      }
    } else {
      rcmdPubKeyword.setZhKw(rcmdPubKeyword.getKeyword());
      rcmdPubKeyword.setZhKwTxt(rcmdPubKeyword.getKeywordTxt());
      rcmdPubKeyword.setZhKwLen(getKwWordLength(rcmdPubKeyword.getKeywordTxt()));
      // 把中文关键词翻译成英文关键词
      KeywordsZhTranEn tranEnKw = keywordsZhTranEnDao.findZhTranEnKw(rcmdPubKeyword.getKeywordTxt());
      if (tranEnKw != null) {
        rcmdPubKeyword.setEnKw(tranEnKw.getEnKw());
        rcmdPubKeyword.setEnKwTxt(tranEnKw.getEnKwTxt());
        rcmdPubKeyword.setEnKwLen(getKwWordLength(tranEnKw.getEnKw()));
      }
    }
  }

  /**
   * 获取关键词长度.
   * 
   * @param keywords
   * @return
   * @throws ServiceException
   */
  public int getKwWordLength(String keywords) {

    if (StringUtils.isBlank(keywords)) {
      return 0;
    }
    if (StringUtils.isAsciiPrintable(keywords)) {
      return getEnKwWordLength(keywords);
    }
    return getZhKwWordLength(keywords);
  }

  /**
   * 获取英文关键词长度.
   * 
   * @param enKw
   * @return
   * @throws ServiceException
   */
  private static int getEnKwWordLength(String enKw) {

    // 直接用非a-z 0-9 - .的字符拆分
    String[] strs = enKw.toLowerCase().split("[^a-z0-9\\-\\.\\_]");
    int num = 0;
    for (String str : strs) {
      if (StringUtils.isNotBlank(str)) {
        num++;
      }
    }
    return num;
  }

  /**
   * 获取中文关键词或者中英混合关键词长度.
   * 
   * @param zhKw
   * @return
   */
  private static int getZhKwWordLength(String zhKw) {

    String lzhKw = zhKw.toLowerCase();
    // 中文关键词里面可能会有英文关键词，先剔除出来
    Pattern p = Pattern.compile("[0-9a-z\\-\\.\\_]{2,}");
    Matcher m = p.matcher(lzhKw);
    int num = 0;
    while (m.find()) {
      num++;
    }
    lzhKw = lzhKw.replaceAll("[0-9a-z\\-\\.\\_]{2,}", "");

    // 非中文字符，不算长度
    String[] strs = lzhKw.split("");
    for (String str : strs) {
      if (StringUtils.isNotBlank(str) && !StringUtils.isAsciiPrintable(str)) {
        num++;
      }
    }
    return num;
  }


  /**
   * 匹配成果作者.
   * 
   * @param psnId
   * @param pmList
   * @return
   * @throws ServiceException
   */
  @Override
  public PubMember matchOwnerPubMember(Long psnId, List<PubMember> pubMemberList) throws ServiceException {
    if (CollectionUtils.isEmpty(pubMemberList)) {
      return null;
    }
    try {
      Person person = personDao.get(psnId);
      if (person == null) {
        return null;
      }
      String zhName = person.getName();
      String firstName = person.getFirstName();
      String lastName = person.getLastName();
      String otherName = person.getOtherName();
      List<String> enNameList = builderEnNames(firstName, lastName, otherName);
      // 先确认用户自己选的作者
      for (PubMember pm : pubMemberList) {
        if (pm.getOwner() == null || pm.getOwner() != 1) {
          continue;
        }
        // 精确、模糊匹配上
        if (this.matchPubAthorExact(pm, zhName, enNameList)
            || this.matchPubAthorFuzzy(pm, zhName, firstName, lastName, otherName)) {
          return pm;
        }
      }
      // 如果匹配不上，再试试其他人员是否精确匹配上
      for (PubMember pm : pubMemberList) {
        // 精确、模糊匹配上
        if (this.matchPubAthorExact(pm, zhName, enNameList)) {
          return pm;
        }
      }
      // 如果匹配不上，再试试其他人员是否模糊匹配上
      for (PubMember pm : pubMemberList) {
        // 精确、模糊匹配上
        if (this.matchPubAthorFuzzy(pm, zhName, firstName, lastName, otherName)) {
          return pm;
        }
      }
      return null;
    } catch (Exception e) {
      logger.error("匹配成果作者信息psnId=" + psnId, e);
      throw new ServiceException("匹配成果作者信息psnId=" + psnId, e);
    }
  }

  private boolean matchPubAthorFuzzy(PubMember pm, String zhName, String firstName, String lastName, String otherName) {
    String pmName = pm.getName();
    if (StringUtils.isBlank(pmName)) {
      return false;
    }
    pmName = pmName.toLowerCase();
    // 有很多成果作者是拼接在一起的
    if (zhName != null && pmName.indexOf(zhName.toLowerCase()) > -1) {
      return true;
    }
    firstName = XmlUtil.getCleanAuthorName(firstName);
    lastName = XmlUtil.getCleanAuthorName(lastName);
    otherName = XmlUtil.getCleanAuthorName(otherName);
    if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
      return false;
    }
    String preF = firstName.substring(0, 1).toLowerCase();
    lastName = lastName.toLowerCase();
    // 尝试z lin 是否匹配上alen z lin或者 z alen lin
    int index = pmName.indexOf(preF);
    if (index > -1 && pmName.substring(index).endsWith(lastName)) {
      return true;
    }
    // 尝试lin z是否匹配上lin z alen或者lin alen z
    index = pmName.lastIndexOf(preF);
    if (index > 0 && pmName.substring(0, index).startsWith(lastName)) {
      return true;
    }
    if (StringUtils.isBlank(otherName)) {
      return false;
    }
    String preO = otherName.substring(0, 1).toLowerCase();
    // 尝试a lin 是否匹配上a zhen lin或者 zhen a lin
    index = pmName.indexOf(preO);
    if (index > -1 && pmName.substring(index).endsWith(lastName)) {
      return true;
    }
    // 尝试lin a是否匹配上lin zhen a或者lin a zhen
    index = pmName.lastIndexOf(preO);
    if (index > 0 && pmName.substring(0, index).startsWith(lastName)) {
      return true;
    }
    return false;
  }

  /**
   * 精确匹配成果作者.
   * 
   * @param pm
   * @param zhName
   * @param enNameList
   * @return
   */
  private boolean matchPubAthorExact(PubMember pm, String zhName, List<String> enNameList) {
    String pmName = pm.getName();
    if (StringUtils.isBlank(pmName)) {
      return false;
    }
    // 匹配中文名
    if (pmName.equalsIgnoreCase(zhName)) {
      return true;
    }
    // 匹配英文名
    pmName = XmlUtil.getCleanAuthorName(pmName);
    for (String enName : enNameList) {
      if (pmName.equalsIgnoreCase(enName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 构造用户英文名.
   * 
   * @param firstName
   * @param lastName
   * @param otherName
   * @return
   */
  public List<String> builderEnNames(String firstName, String lastName, String otherName) {

    firstName = XmlUtil.getCleanAuthorName(firstName);
    lastName = XmlUtil.getCleanAuthorName(lastName);
    otherName = XmlUtil.getCleanAuthorName(otherName);
    List<String> enNameList = new ArrayList<String>();
    if (StringUtils.isBlank(firstName) || StringUtils.isBlank(lastName)) {
      return enNameList;
    }
    // 构造全称，简称
    Set<String> ifNameList = PsnPmIsiNameUtils.buildAllName(firstName, lastName, otherName);
    if (ifNameList != null) {
      enNameList.addAll(ifNameList);
    }
    return enNameList;
  }

  @Override
  public void analysisKeyWords(List<Long> psnKwEptRefreshList, String startId) throws ServiceException {
    for (Long psnId : psnKwEptRefreshList) {
      try {
        startId = psnId.toString();
        this.analyzeKeyWords(psnId);
      } catch (Exception e) {
        logger.error("解析关键词失败", e);
        throw new ServiceException(e);
      }
    }
  }

}
