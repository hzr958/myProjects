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
import com.smate.center.batch.dao.rol.pub.PubAssignSpsAuthorDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsConferenceDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsEmailDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsJournalDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsKeywordsDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsScoreDao;
import com.smate.center.batch.dao.rol.pub.SettingPubAssignSpsScoreDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.psn.PsnPmJournalRol;
import com.smate.center.batch.model.rol.pub.PsnPmConference;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignSpsScore;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.rol.pub.SettingPubAssignScoreWrap;
import com.smate.center.batch.model.rol.pub.SettingPubAssignSpsScore;
import com.smate.center.batch.util.pub.PubXmlDbUtils;

/**
 * scopus成果匹配服务.
 * 
 * @author liqinghua
 * 
 */
@Service("pubAssignSpsMatchService")
@Transactional(rollbackFor = Exception.class)
public class PubAssignSpsMatchServiceImpl extends BasePubAssignMatchService implements PubAssignSpsMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = 5332707400605613327L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private Map<Long, SettingPubAssignScoreWrap> settingPubAssignScoreMap =
      new HashMap<Long, SettingPubAssignScoreWrap>();
  @Autowired
  private PubAssignSpsAuthorDao pubAssignSpsAuthorDao;
  @Autowired
  private PubAssignSpsEmailDao pubAssignSpsEmailDao;
  @Autowired
  private PubAssignSpsKeywordsDao pubAssignSpsKeywordsDao;
  @Autowired
  private PubAssignSpsJournalDao pubAssignSpsJournalDao;
  @Autowired
  private PubAssignSpsConferenceDao pubAssignSpsConferenceDao;
  @Autowired
  private WorkHistoryRolDao workHistoryDao;
  @Autowired
  private PsnPubStatSyncService psnPubStatSyncService;
  @Autowired
  private PubAssignSpsScoreDao pubAssignSpsScoreDao;
  private IPubAssignMatchProcess spsPubAssignMatchProcess;
  private IPubAssignMatchProcess spsPsnAssignMatchStep1Process;
  @Autowired
  private SettingPubAssignSpsScoreDao settingPubAssignSpsScoreDao;
  @Autowired
  private PublicationRolService publicationRolService;

  @Override
  public void assignByPub(PublicationRol pub, Long insId) throws ServiceException {

    try {
      // SCOPUS匹配
      if (pub.getSourceDbId() != null && PubXmlDbUtils.isScopusDb(Long.valueOf(pub.getSourceDbId()))) {
        // 构造PubAssginMatchContext.
        PubAssginMatchContext context = super.buildPubAssginMatchContext(pub, insId,
            this.getSettingPubAssignScore(pub.getSourceDbId().longValue()));
        // 开始匹配成果
        spsPubAssignMatchProcess.start(context);
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
      PubAssginMatchContext context = super.buildPsnAssginMatchContext(psnId, insId, PsnMatchTypeConstants.SCOPUS);
      // 开始匹配成果
      spsPsnAssignMatchStep1Process.start(context);
    } catch (Exception e) {
      logger.error("按成果匹配人员", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getSpsPrefixNameMatchPubAuthor(Long pubId, Long insId) throws ServiceException {

    try {
      List<Object[]> list = pubAssignSpsAuthorDao.getSpsPrefixNameMatchPubAuthor(pubId, insId);
      return list;
    } catch (Exception e) {
      logger.error("获取匹配上成果作者名称的单位人员", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getSpsInitNameMatchPubAuthor(Long pubId, Long insId, Set<Long> psnIds) throws ServiceException {
    try {
      List<Object[]> list = pubAssignSpsAuthorDao.getSpsInitNameMatchPubAuthor(pubId, insId, psnIds);
      return list;
    } catch (Exception e) {
      logger.error("获取匹配上sps成果作者名称简写的单位人员", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getSpsPubAuthorMatchPsnPrefixName(Long psnId, Long insId) throws ServiceException {

    try {
      List<Object[]> list = pubAssignSpsAuthorDao.getSpsPubAuthorMatchPsnPrefixName(psnId, insId);
      return this.mergeSpsNameMatchSource(list);
    } catch (Exception e) {
      logger.error("获取匹配上单位人员的sps成果列表", e);
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
  private List<Object[]> mergeSpsNameMatchSource(List<Object[]> assignList) throws ServiceException {
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
      String name = (String) pub[3];
      Integer type = (Integer) pub[4];
      for (Object[] pubSource : pubSourceList) {
        Long pid = (Long) pubSource[0];
        Integer sourceId = (Integer) pubSource[1];
        // 是scopus成果、并且成果状态不是删除状态
        if (PubXmlDbUtils.isScopusDb(sourceId) && pid.equals(pubId)) {
          result.add(new Object[] {pubId, sourceId, seqNo, insId, name, type});
        }
      }
    }
    return result;
  }

  @Override
  public List<Object[]> getSpsFullNameMatchPubAuthor(Long pubId, Set<Long> psnIds, Long insId) throws ServiceException {

    try {
      List<Object[]> list = pubAssignSpsAuthorDao.getSpsFullNameMatchPubAuthor(pubId, psnIds, insId);
      return list;
    } catch (Exception e) {
      logger.error("获取匹配上成果作者名称全名的指定人员ID", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getEmailMatchPubEmail(Long pubId, Long insId) throws ServiceException {

    try {
      List<Object[]> list = this.pubAssignSpsEmailDao.getEmailMatchPubEmail(pubId, insId);
      return list;
    } catch (Exception e) {
      logger.error("获取匹配上成果作者email的指定人员ID", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getPsnEmailMatchSpsPubEmail(Long psnId, Long insId) throws ServiceException {
    try {
      List<Object[]> list = this.pubAssignSpsEmailDao.getPsnEmailMatchSpsPubEmail(psnId, insId);
      return this.mergeSpsEmailMatchSource(list);
    } catch (Exception e) {
      logger.error("获取人员匹配上SCOPUS成果作者email的成果列表", e);
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
  private List<Object[]> mergeSpsEmailMatchSource(List<Object[]> assignList) throws ServiceException {
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
    // pr.id,pr.sourceDbId,pe.seqNo,pe.insId
    for (Object[] pub : assignList) {
      Long pubId = (Long) pub[0];
      Integer seqNo = (Integer) pub[1];
      Long insId = (Long) pub[2];
      for (Object[] pubSource : pubSourceList) {
        Long pid = (Long) pubSource[0];
        Integer sourceId = (Integer) pubSource[1];
        // 是scopus成果、并且成果状态不是删除状态
        if (PubXmlDbUtils.isScopusDb(sourceId) && pid.equals(pubId)) {
          result.add(new Object[] {pubId, sourceId, seqNo, insId});
        }
      }
    }
    return result;
  }

  @Override
  public Map<Long, Long> getKwMatchPubKw(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      return pubAssignSpsKeywordsDao.getKwMatchPubKw(pubId, psnIds);
    } catch (Exception e) {
      logger.error("获取匹配上成果关键词的指定人员ID与关键词列表", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, PsnPmJournalRol> getPJMatchPubPJ(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      List<PsnPmJournalRol> list = pubAssignSpsJournalDao.getPJMatchPubPJ(pubId, psnIds);
      return super.buildPJMatchPubPJMap(list);
    } catch (Exception e) {
      logger.error("获取匹配上成果期刊的指定人员ID与期刊列表", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, PsnPmConference> getPCMatchPubPc(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      List<PsnPmConference> list = pubAssignSpsConferenceDao.getPcMatchPubPc(pubId, psnIds);
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
  public Map<Long, Long> getSpsConMatchPubAuth(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      List<Object[]> list = this.pubAssignSpsAuthorDao.getSpsConMatchPubAuthor(pubId, psnIds);

      return super.buildIsiCoMatchPubAuthMap(list);
    } catch (Exception e) {
      logger.error("获取匹配上成果作者的指定用户的合作者个数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, Long> getSpsConMatchPubAuthPreName(Long pubId, Set<Long> psnIds) throws ServiceException {
    try {
      List<Object[]> list = this.pubAssignSpsAuthorDao.getSpsConMatchPubAuthorPreName(pubId, psnIds);

      return super.buildIsiCoMatchPubAuthMap(list);
    } catch (Exception e) {
      logger.error("获取匹配上成果作者的指定用户的合作者个数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, Long> getSpsCoeMatchPubAuth(Long pubId, Set<Long> psnIds) throws ServiceException {
    try {
      List<Object[]> list = this.pubAssignSpsEmailDao.getSpsCoeMatchPubAuthor(pubId, psnIds);

      return super.buildIsiCoMatchPubAuthMap(list);
    } catch (Exception e) {
      logger.error("获取匹配上成果作者的指定用户的合作者EMAIL个数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveAssignScore(PubAssignSpsScore pubAssignSpsScore) throws ServiceException {

    try {
      // 保存匹配结果
      PubAssignSpsScore preScore =
          this.pubAssignSpsScoreDao.getPubAssignScore(pubAssignSpsScore.getPubId(), pubAssignSpsScore.getPsnId());
      if (preScore != null) {
        preScore.copy(pubAssignSpsScore);
      } else {
        preScore = pubAssignSpsScore;
      }
      this.pubAssignSpsScoreDao.save(preScore);
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
        SettingPubAssignSpsScore settingSps = settingPubAssignSpsScoreDao.getSettingPubAssignScore(dbId);
        setting = new SettingPubAssignScoreWrap(settingSps);
        settingPubAssignScoreMap.put(dbId, setting);
      }
      return setting;
    } catch (Exception e) {
      logger.error("获取SettingPubAssignSpsScore", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePubAssignScore(Long psnId, Long insId) throws ServiceException {
    try {
      this.pubAssignSpsScoreDao.remove(psnId, insId);
    } catch (Exception e) {
      logger.error("删除人员所在单位匹配关系", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePubAssignScore(Long pubId) throws ServiceException {
    try {
      this.pubAssignSpsScoreDao.removeByPubId(pubId);
    } catch (Exception e) {
      logger.error("删除成果匹配关系", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePubPsnAssignScore(Long pubId, Long psnId) throws ServiceException {
    try {
      this.pubAssignSpsScoreDao.removePubPsnAssignScore(pubId, psnId);
    } catch (Exception e) {
      logger.error("删除成果匹配关系", e);
      throw new ServiceException(e);
    }
  }

  public void setSpsPubAssignMatchProcess(IPubAssignMatchProcess spsPubAssignMatchProcess) {
    this.spsPubAssignMatchProcess = spsPubAssignMatchProcess;
  }

  public void setSpsPsnAssignMatchStep1Process(IPubAssignMatchProcess spsPsnAssignMatchStep1Process) {
    this.spsPsnAssignMatchStep1Process = spsPsnAssignMatchStep1Process;
  }

}
