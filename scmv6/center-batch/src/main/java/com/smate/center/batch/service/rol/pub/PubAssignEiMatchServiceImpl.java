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
import com.smate.center.batch.dao.rol.pub.PubAssignEiAuthorDao;
import com.smate.center.batch.dao.rol.pub.PubAssignEiConferenceDao;
import com.smate.center.batch.dao.rol.pub.PubAssignEiEmailDao;
import com.smate.center.batch.dao.rol.pub.PubAssignEiJournalDao;
import com.smate.center.batch.dao.rol.pub.PubAssignEiKeywordsDao;
import com.smate.center.batch.dao.rol.pub.PubAssignEiScoreDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsAuthorDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsConferenceDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsEmailDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsJournalDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsKeywordsDao;
import com.smate.center.batch.dao.rol.pub.PubAssignSpsScoreDao;
import com.smate.center.batch.dao.rol.pub.SettingPubAssignEiScoreDao;
import com.smate.center.batch.dao.rol.pub.SettingPubAssignSpsScoreDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.psn.PsnPmJournalRol;
import com.smate.center.batch.model.rol.pub.PsnPmConference;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignEiScore;
import com.smate.center.batch.model.rol.pub.PubAssignSpsScore;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.rol.pub.SettingPubAssignEiScore;
import com.smate.center.batch.model.rol.pub.SettingPubAssignScoreWrap;
import com.smate.center.batch.model.rol.pub.SettingPubAssignSpsScore;
import com.smate.center.batch.util.pub.PubXmlDbUtils;

/**
 * Ei成果匹配服务.
 * 
 * @author liqinghua
 * 
 */
@Service("pubAssignEiMatchService")
@Transactional(rollbackFor = Exception.class)
public class PubAssignEiMatchServiceImpl extends BasePubAssignMatchService implements PubAssignEiMatchService {


  /**
   * 
   */
  private static final long serialVersionUID = 6449535259589227652L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private Map<Long, SettingPubAssignScoreWrap> settingPubAssignScoreMap =
      new HashMap<Long, SettingPubAssignScoreWrap>();
  @Autowired
  private PubAssignEiAuthorDao pubAssignEiAuthorDao;
  @Autowired
  private PubAssignEiEmailDao pubAssignEiEmailDao;
  @Autowired
  private PubAssignEiKeywordsDao pubAssignEiKeywordsDao;
  @Autowired
  private PubAssignEiJournalDao pubAssignEiJournalDao;
  @Autowired
  private PubAssignEiConferenceDao pubAssignEiConferenceDao;
  @Autowired
  private WorkHistoryRolDao workHistoryDao;
  @Autowired
  private PsnPubStatSyncService psnPubStatSyncService;

  private IPubAssignMatchProcess eiPubAssignMatchProcess;
  private IPubAssignMatchProcess eiPsnAssignMatchStep1Process;
  @Autowired
  private SettingPubAssignEiScoreDao settingPubAssignEiScoreDao;
  @Autowired
  private PubAssignEiScoreDao pubAssignEiScoreDao;
  @Autowired
  private PublicationRolService publicationRolService;

  @Override
  public void assignByPub(PublicationRol pub, Long insId) throws ServiceException {

    try {
      // EI匹配
      if (pub.getSourceDbId() != null && PubXmlDbUtils.isEiDb(Long.valueOf(pub.getSourceDbId()))) {
        // 构造PubAssginMatchContext.
        PubAssginMatchContext context = super.buildPubAssginMatchContext(pub, insId,
            this.getSettingPubAssignScore(pub.getSourceDbId().longValue()));
        // 开始匹配成果
        eiPubAssignMatchProcess.start(context);
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
      eiPsnAssignMatchStep1Process.start(context);
    } catch (Exception e) {
      logger.error("按成果匹配人员", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getEmailMatchPubEmail(Long pubId, Long insId) throws ServiceException {

    try {
      List<Object[]> list = this.pubAssignEiEmailDao.getEmailMatchPubEmail(pubId, insId);
      return list;
    } catch (Exception e) {
      logger.error("获取匹配上成果作者email的指定人员ID", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, Long> getKwMatchPubKw(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      return pubAssignEiKeywordsDao.getKwMatchPubKw(pubId, psnIds);
    } catch (Exception e) {
      logger.error("获取匹配上成果关键词的指定人员ID与关键词列表", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, PsnPmJournalRol> getPJMatchPubPJ(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      List<PsnPmJournalRol> list = pubAssignEiJournalDao.getPJMatchPubPJ(pubId, psnIds);
      return super.buildPJMatchPubPJMap(list);
    } catch (Exception e) {
      logger.error("获取匹配上成果期刊的指定人员ID与期刊列表", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, PsnPmConference> getPCMatchPubPc(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      List<PsnPmConference> list = pubAssignEiConferenceDao.getPcMatchPubPc(pubId, psnIds);
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
  public void saveAssignScore(PubAssignEiScore pubAssignEiScore) throws ServiceException {

    try {
      // 保存匹配结果
      PubAssignEiScore preScore =
          this.pubAssignEiScoreDao.getPubAssignEiScore(pubAssignEiScore.getPubId(), pubAssignEiScore.getPsnId());
      if (preScore != null) {
        preScore.copy(pubAssignEiScore);
      } else {
        preScore = pubAssignEiScore;
      }
      this.pubAssignEiScoreDao.save(preScore);
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
        SettingPubAssignEiScore settingEi = settingPubAssignEiScoreDao.getSettingPubAssignEiScore(dbId);
        if (settingEi == null) {
          throw new ServiceException("请在数据表SETTING_PUBASSIGN_EISCORE中配置成果指派SettingPubAssignEiScore相关计分标准！");
        }
        setting = new SettingPubAssignScoreWrap(settingEi);
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
      this.pubAssignEiScoreDao.remove(psnId, insId);
    } catch (Exception e) {
      logger.error("删除人员所在单位匹配关系", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePubAssignScore(Long pubId) throws ServiceException {
    try {
      this.pubAssignEiScoreDao.removeByPubId(pubId);
    } catch (Exception e) {
      logger.error("删除成果匹配关系", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePubPsnAssignScore(Long pubId, Long psnId) throws ServiceException {
    try {
      this.pubAssignEiScoreDao.removePubPsnAssignEiScore(pubId, psnId);
    } catch (Exception e) {
      logger.error("删除成果匹配关系", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getEiPrefixNameMatchPubAuthor(Long pubId, Long insId) throws ServiceException {

    try {
      List<Object[]> listInit = new ArrayList<Object[]>();
      // 名称
      List<Object[]> listName = pubAssignEiAuthorDao.getEiNameMatchPubAuthor(pubId, insId);
      // 全称
      List<Object[]> listFullName = pubAssignEiAuthorDao.getEiNameFullMatchPubAuthor(pubId, insId);
      // 合并
      listInit = this.mergeEiNameMatchPubAuthor(listName, listInit);
      listInit = this.mergeEiNameMatchPubAuthor(listFullName, listInit);
      return listInit;
    } catch (Exception e) {
      logger.error("获取匹配上成果作者名称的单位人员", e);
      throw new ServiceException(e);
    }
  }

  /**
   * 合并匹配上isi成果作者名称的单位人员.
   * 
   * @param fromlist
   * @param tolist
   * @return
   */
  public List<Object[]> mergeEiNameMatchPubAuthor(List<Object[]> fromlist, List<Object[]> tolist) {
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
  public List<Object[]> getEiFullNameMatchPubAuthor(Long pubId, Set<Long> psnIds, Long insId) throws ServiceException {

    try {
      List<Object[]> list = pubAssignEiAuthorDao.getEiFullNameMatchPubAuthor(pubId, psnIds, insId);
      return list;
    } catch (Exception e) {
      logger.error("获取匹配上成果作者名称全名的指定人员ID", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, Long> getEiConMatchPubAuth(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      List<Object[]> list = this.pubAssignEiAuthorDao.getEiConMatchPubAuthor(pubId, psnIds);

      return super.buildIsiCoMatchPubAuthMap(list);
    } catch (Exception e) {
      logger.error("获取匹配上成果作者的指定用户的合作者个数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, Long> getEiCoeMatchPubAuth(Long pubId, Set<Long> psnIds) throws ServiceException {
    try {
      List<Object[]> list = this.pubAssignEiAuthorDao.getEiCoeMatchPubAuthor(pubId, psnIds);

      return super.buildIsiCoMatchPubAuthMap(list);
    } catch (Exception e) {
      logger.error("获取匹配上成果作者的指定用户的合作者EMAIL个数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getEiInitNameMatchPubAuthor(Long pubId, Long insId, Set<Long> psnIds) throws ServiceException {
    try {
      List<Object[]> list = pubAssignEiAuthorDao.getEiInitNameMatchPubAuthor(pubId, insId, psnIds);
      return list;
    } catch (Exception e) {
      logger.error("获取匹配上ei成果作者名称简写的单位人员", e);
      throw new ServiceException(e);
    }
  }

  public void setEiPubAssignMatchProcess(IPubAssignMatchProcess eiPubAssignMatchProcess) {
    this.eiPubAssignMatchProcess = eiPubAssignMatchProcess;
  }

  public void setEiPsnAssignMatchStep1Process(IPubAssignMatchProcess eiPsnAssignMatchStep1Process) {
    this.eiPsnAssignMatchStep1Process = eiPsnAssignMatchStep1Process;
  }

}
