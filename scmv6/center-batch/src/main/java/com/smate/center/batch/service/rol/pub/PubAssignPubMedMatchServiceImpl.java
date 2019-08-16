package com.smate.center.batch.service.rol.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.smate.center.batch.chain.pubassign.process.IPubAssignMatchProcess;
import com.smate.center.batch.constant.PsnMatchTypeConstants;
import com.smate.center.batch.constant.PubAssignModeEnum;
import com.smate.center.batch.dao.rol.psn.WorkHistoryRolDao;
import com.smate.center.batch.dao.rol.pub.PubAssignPubMedAuthorDao;
import com.smate.center.batch.dao.rol.pub.PubAssignPubMedConferenceDao;
import com.smate.center.batch.dao.rol.pub.PubAssignPubMedEmailDao;
import com.smate.center.batch.dao.rol.pub.PubAssignPubMedJournalDao;
import com.smate.center.batch.dao.rol.pub.PubAssignPubMedKeywordsDao;
import com.smate.center.batch.dao.rol.pub.PubAssignPubMedScoreDao;
import com.smate.center.batch.dao.rol.pub.SettingPubAssignPubMedScoreDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.psn.PsnPmJournalRol;
import com.smate.center.batch.model.rol.pub.PsnPmConference;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignPubMedScore;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.rol.pub.SettingPubAssignPubMedScore;
import com.smate.center.batch.model.rol.pub.SettingPubAssignScoreWrap;
import com.smate.center.batch.util.pub.PubXmlDbUtils;

/**
 * 成果匹配服务.
 * 
 * @author liqinghua
 * 
 */
@Service("pubAssignPubMedMatchService")
@Transactional(rollbackFor = Exception.class)
public class PubAssignPubMedMatchServiceImpl extends BasePubAssignMatchService implements PubAssignPubMedMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = -9116754886865618147L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private Map<Long, SettingPubAssignScoreWrap> settingPubAssignScoreMap =
      new HashMap<Long, SettingPubAssignScoreWrap>();
  @Autowired
  private PubAssignPubMedAuthorDao pubAssignPubMedAuthorDao;
  @Autowired
  private PubAssignPubMedEmailDao pubAssignPubMedEmailDao;
  @Autowired
  private PubAssignPubMedKeywordsDao pubAssignPubMedKeywordsDao;
  @Autowired
  private PubAssignPubMedJournalDao pubAssignPubMedJournalDao;
  @Autowired
  private PubAssignPubMedConferenceDao pubAssignPubMedConferenceDao;
  @Autowired
  private WorkHistoryRolDao workHistoryDao;
  @Autowired
  private PsnPubStatSyncService psnPubStatSyncService;
  @Autowired
  private PubAssignPubMedScoreDao pubAssignPubMedScoreDao;
  private IPubAssignMatchProcess pubMedPubAssignMatchProcess;
  private IPubAssignMatchProcess pubMedPsnAssignMatchStep1Process;
  @Autowired
  private SettingPubAssignPubMedScoreDao settingPubAssignPubMedScoreDao;
  @Autowired
  private PublicationRolService publicationRolService;

  @Override
  public void assignByPub(PublicationRol pub, Long insId) throws ServiceException {

    try {
      // PUBMED匹配
      if (pub.getSourceDbId() != null && PubXmlDbUtils.isPubMedDb(Long.valueOf(pub.getSourceDbId()))) {
        // 构造PubAssginMatchContext.
        PubAssginMatchContext context = super.buildPubAssginMatchContext(pub, insId,
            this.getSettingPubAssignScore(pub.getSourceDbId().longValue()));
        // 开始匹配成果
        pubMedPubAssignMatchProcess.start(context);
      }
    } catch (Exception e) {
      logger.error("按成果匹配人员", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void assignByPsn(Long psnId, Long insId) throws ServiceException {
    try {
      // 构造PubAssginMatchContext.
      PubAssginMatchContext context = super.buildPsnAssginMatchContext(psnId, insId, PsnMatchTypeConstants.PUBMED);
      // 开始匹配成果
      pubMedPsnAssignMatchStep1Process.start(context);
    } catch (Exception e) {
      logger.error("按成果匹配人员", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getPubMedPrefixNameMatchPubAuthor(Long pubId, Long insId) throws ServiceException {

    try {
      List<Object[]> listInit = new ArrayList<Object[]>();
      /*
       * // 简称 List<Object[]> listInit = pubAssignPubMedAuthorDao.getPrefixNameMatchPubAuthor(pubId,
       * insId);
       */
      // 名称
      List<Object[]> listName = pubAssignPubMedAuthorDao.getPubMedNameMatchPubAuthor(pubId, insId);
      // 全称
      List<Object[]> listFullName = pubAssignPubMedAuthorDao.getPubMedNameFullMatchPubAuthor(pubId, insId);
      // 合并
      listInit = this.mergePubMedNameMatchPubAuthor(listName, listInit);
      listInit = this.mergePubMedNameMatchPubAuthor(listFullName, listInit);
      return listInit;
    } catch (Exception e) {
      logger.error("获取匹配上成果作者名称的单位人员", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 合并匹配上PubMed成果作者名称的单位人员.
   * 
   * @param fromlist
   * @param tolist
   * @return
   */
  public List<Object[]> mergePubMedNameMatchPubAuthor(List<Object[]> fromlist, List<Object[]> tolist) {
    outerLoop: for (Object[] fromPsn : fromlist) {
      Integer fromSeqNo = (Integer) fromPsn[0] == null ? -9 : (Integer) fromPsn[0];
      Long fromPsnId = (Long) fromPsn[1];
      for (Object[] toPsn : tolist) {
        Integer toSeqNo = (Integer) toPsn[0] == null ? -9 : (Integer) toPsn[0];
        Long toPsnId = (Long) toPsn[1];
        // 碰到完全一样的，排除
        if (toSeqNo.equals(fromSeqNo) && toPsnId.equals(fromPsnId)) {
          continue outerLoop;
        }
      }
      tolist.add(fromPsn);
    }
    return tolist;
  }

  @Override
  public List<Object[]> getPubMedInitNameMatchPubAuthor(Long pubId, Long insId, Set<Long> psnIds)
      throws ServiceException {
    try {
      List<Object[]> list = pubAssignPubMedAuthorDao.getPubMedInitNameMatchPubAuthor(pubId, insId, psnIds);
      return list;
    } catch (Exception e) {
      logger.error("获取匹配上PubMed成果作者名称简写的单位人员", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getPubMedPubAuthorMatchPsnPrefixName(Long psnId, Long insId) throws ServiceException {

    try {
      /*
       * // 前缀 List<Object[]> initlist =
       * pubAssignPubMedAuthorDao.getPubMedPubAuthorMatchPsnPrefixName(psnId, insId);
       */
      // 名称
      List<Object[]> namelist = pubAssignPubMedAuthorDao.getPubMedPubAuthorMatchPsnName(psnId, insId);
      // 全称
      List<Object[]> fullnamelist = pubAssignPubMedAuthorDao.getPubMedPubAuthorMatchPsnFullName(psnId, insId);
      // 合并
      List<Object[]> targetlist = new ArrayList<Object[]>();
      /* targetlist = this.mergePubMedPubAuthorMatchPsnName(initlist, targetlist); */
      targetlist = this.mergePubMedPubAuthorMatchPsnName(namelist, targetlist);
      targetlist = this.mergePubMedPubAuthorMatchPsnName(fullnamelist, targetlist);
      // 合并未删除状态成果来源，不关联publication表，用于提高性能.
      targetlist = this.mergePubMedNameMatchSource(targetlist);
      return targetlist;
    } catch (Exception e) {
      logger.error("获取匹配上单位人员的PubMed成果列表", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 合并匹配上PubMed成果作者名称的单位人员.
   * 
   * @param fromlist
   * @param tolist
   * @return
   */
  public List<Object[]> mergePubMedPubAuthorMatchPsnName(List<Object[]> fromlist, List<Object[]> tolist) {
    outerLoop: for (Object[] fromPub : fromlist) {
      Long fromPubId = (Long) fromPub[0];
      Integer fromSeqNo = (Integer) fromPub[1] == null ? -9 : (Integer) fromPub[1];
      for (Object[] toPub : tolist) {
        Long toPubId = (Long) toPub[0];
        Integer toSeqNo = (Integer) toPub[1] == null ? -9 : (Integer) toPub[1];
        // 碰到完全一样的，排除
        if (toSeqNo.equals(fromSeqNo) && toPubId.equals(fromPubId)) {
          continue outerLoop;
        }
      }
      tolist.add(fromPub);
    }
    return tolist;
  }

  /**
   * 合并未删除状态成果来源，不关联publication表，用于提高性能.
   * 
   * @param assignList
   * @return
   * @throws ServiceException
   */
  public List<Object[]> mergePubMedNameMatchSource(List<Object[]> assignList) throws ServiceException {
    if (CollectionUtils.isEmpty(assignList)) {
      return null;
    }
    List<Long> pubIds = new ArrayList<Long>();
    for (Object[] pub : assignList) {
      pubIds.add((Long) pub[0]);
    }
    // 获取未删除状态成果来源
    List<Object[]> pubSourceList = this.publicationRolService.getFilterNotDelPubSource(pubIds);
    if (CollectionUtils.isEmpty(pubSourceList)) {
      return null;
    }
    // 合并结果
    List<Object[]> result = new ArrayList<Object[]>();
    for (Object[] pub : assignList) {
      Long pubId = (Long) pub[0];
      Integer seqNo = (Integer) pub[1];
      Long insId = (Long) pub[2];
      String initName = (String) pub[3];
      String fullName = (String) pub[4];
      for (Object[] pubSource : pubSourceList) {
        Long pid = (Long) pubSource[0];
        Integer sourceId = (Integer) pubSource[1];
        // 是PubMed成果、并且成果状态不是删除状态
        if (PubXmlDbUtils.isPubMedDb(sourceId) && pid.equals(pubId)) {
          result.add(new Object[] {pubId, sourceId, seqNo, insId, initName, fullName});
        }
      }
    }
    return result;
  }

  @Override
  public List<Object[]> getPubMedFullNameMatchPubAuthor(Long pubId, Set<Long> psnIds, Long insId)
      throws ServiceException {

    try {
      List<Object[]> list = pubAssignPubMedAuthorDao.getPubMedFullNameMatchPubAuthor(pubId, psnIds, insId);
      return list;
    } catch (Exception e) {
      logger.error("获取匹配上成果作者名称全名的指定人员ID", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getEmailMatchPubEmail(Long pubId, Long insId) throws ServiceException {

    try {
      List<Object[]> list = this.pubAssignPubMedEmailDao.getEmailMatchPubEmail(pubId, insId);
      return list;
    } catch (Exception e) {
      logger.error("获取匹配上成果作者email的指定人员ID", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getPsnEmailMatchPubMedPubEmail(Long psnId, Long insId) throws ServiceException {
    try {
      List<Object[]> list = this.pubAssignPubMedEmailDao.getPsnEmailMatchPubMedPubEmail(psnId, insId);
      return this.mergePubMedEmailMatchSource(list);
    } catch (Exception e) {
      logger.error("获取人员匹配上PUBMED成果作者email的成果列表", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 合并未删除状态成果来源，不关联publication表，用于提高性能.
   * 
   * @param assignList
   * @return
   * @throws ServiceException
   */
  public List<Object[]> mergePubMedEmailMatchSource(List<Object[]> assignList) throws ServiceException {
    if (CollectionUtils.isEmpty(assignList)) {
      return null;
    }
    List<Long> pubIds = new ArrayList<Long>();
    for (Object[] pub : assignList) {
      pubIds.add((Long) pub[0]);
    }
    // 获取未删除状态成果来源
    List<Object[]> pubSourceList = this.publicationRolService.getFilterNotDelPubSource(pubIds);
    if (CollectionUtils.isEmpty(pubSourceList)) {
      return null;
    }
    // 合并结果
    List<Object[]> result = new ArrayList<Object[]>();
    for (Object[] pub : assignList) {
      Long pubId = (Long) pub[0];
      Integer seqNo = (Integer) pub[1];
      Long insId = (Long) pub[2];
      for (Object[] pubSource : pubSourceList) {
        Long pid = (Long) pubSource[0];
        Integer sourceId = (Integer) pubSource[1];
        // 是PubMed成果、并且成果状态不是删除状态
        if (PubXmlDbUtils.isPubMedDb(sourceId) && pid.equals(pubId)) {
          result.add(new Object[] {pubId, sourceId, seqNo, insId});
        }
      }
    }
    return result;
  }

  @Override
  public Map<Long, Long> getKwMatchPubKw(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      return pubAssignPubMedKeywordsDao.getKwMatchPubKw(pubId, psnIds);
    } catch (Exception e) {
      logger.error("获取匹配上成果关键词的指定人员ID与关键词列表", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, PsnPmJournalRol> getPJMatchPubPJ(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      List<PsnPmJournalRol> list = pubAssignPubMedJournalDao.getPJMatchPubPJ(pubId, psnIds);
      return super.buildPJMatchPubPJMap(list);
    } catch (Exception e) {
      logger.error("获取匹配上成果期刊的指定人员ID与期刊列表", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, PsnPmConference> getPCMatchPubPc(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      List<PsnPmConference> list = pubAssignPubMedConferenceDao.getPcMatchPubPc(pubId, psnIds);
      return super.buildPCMatchPubPcMap(list);
    } catch (Exception e) {
      logger.error("获取匹配上成果会议论文的指定人员ID与期刊列表", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Long> getWkhMatchPubYear(Long insId, Integer pubYear, Set<Long> psnIds) throws ServiceException {

    try {
      if (pubYear == null) {
        return null;
      }
      return workHistoryDao.getMatchYearPsnId(pubYear, psnIds, insId);
    } catch (Exception e) {
      logger.error("获取匹配上成果发表年份的指定人员的工作经历用户ID", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, Long> getPubMedConMatchPubAuth(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      List<Object[]> list = this.pubAssignPubMedAuthorDao.getPubMedConMatchPubAuthor(pubId, psnIds);

      return super.buildIsiCoMatchPubAuthMap(list);
    } catch (Exception e) {
      logger.error("获取匹配上成果作者的指定用户的合作者个数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, Long> getPubMedCoeMatchPubAuth(Long pubId, Set<Long> psnIds) throws ServiceException {
    try {
      List<Object[]> list = this.pubAssignPubMedAuthorDao.getPubMedCoeMatchPubAuthor(pubId, psnIds);

      return super.buildIsiCoMatchPubAuthMap(list);
    } catch (Exception e) {
      logger.error("获取匹配上成果作者的指定用户的合作者EMAIL个数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveAssignScore(PubAssignPubMedScore pubAssignPubMedScore) throws ServiceException {

    try {
      // 保存匹配结果
      PubAssignPubMedScore preScore = this.pubAssignPubMedScoreDao
          .getPubAssignPubMedScore(pubAssignPubMedScore.getPubId(), pubAssignPubMedScore.getPsnId());
      if (preScore != null) {
        preScore.copy(pubAssignPubMedScore);
      } else {
        preScore = pubAssignPubMedScore;
      }
      this.pubAssignPubMedScoreDao.save(preScore);
      // 建立人与成果关系
      psnPubStatSyncService.addPubPsn(preScore.getPubId(), preScore.getInsId(), preScore.getPsnId(),
          preScore.getTotal(), preScore.getSeqNo(), PubAssignModeEnum.BACKGROUND_JOB, false);

    } catch (Exception e) {
      logger.error("保存成果匹配结果", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public SettingPubAssignScoreWrap getSettingPubAssignScore(Long dbId) throws ServiceException {
    try {
      SettingPubAssignScoreWrap setting = settingPubAssignScoreMap.get(dbId);
      if (setting == null) {

        SettingPubAssignPubMedScore settingPubMed = settingPubAssignPubMedScoreDao.getSettingPubAssignScore(dbId);
        setting = new SettingPubAssignScoreWrap(settingPubMed);
        settingPubAssignScoreMap.put(dbId, setting);
      }
      return setting;
    } catch (Exception e) {
      logger.error("获取SettingPubAssignPubMedScore", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePubAssignScore(Long psnId, Long insId) throws ServiceException {
    try {
      this.pubAssignPubMedScoreDao.remove(psnId, insId);
    } catch (Exception e) {
      logger.error("删除人员所在单位匹配关系", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePubAssignScore(Long pubId) throws ServiceException {
    try {
      this.pubAssignPubMedScoreDao.removeByPubId(pubId);
    } catch (Exception e) {
      logger.error("删除成果匹配关系", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePubPsnAssignScore(Long pubId, Long psnId) throws ServiceException {
    try {
      this.pubAssignPubMedScoreDao.removePubPsnAssignScore(pubId, psnId);
    } catch (Exception e) {
      logger.error("删除成果匹配关系", e);
      throw new ServiceException(e);
    }
  }

  public void setPubMedPubAssignMatchProcess(IPubAssignMatchProcess pubMedPubAssignMatchProcess) {
    this.pubMedPubAssignMatchProcess = pubMedPubAssignMatchProcess;
  }

  public void setPubMedPsnAssignMatchStep1Process(IPubAssignMatchProcess pubMedPsnAssignMatchStep1Process) {
    this.pubMedPsnAssignMatchStep1Process = pubMedPsnAssignMatchStep1Process;
  }

}
