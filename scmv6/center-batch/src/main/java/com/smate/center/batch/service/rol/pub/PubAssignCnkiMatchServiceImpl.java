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
import org.springframework.util.CollectionUtils;

import com.smate.center.batch.chain.pubassign.process.IPubAssignMatchProcess;
import com.smate.center.batch.constant.PsnMatchTypeConstants;
import com.smate.center.batch.constant.PubAssignModeEnum;
import com.smate.center.batch.dao.rol.psn.RolPsnInsDao;
import com.smate.center.batch.dao.rol.pub.InsUnitDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiAuthorDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiConferenceDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiDeptDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiJournalDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiKeywordsDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiScoreDao;
import com.smate.center.batch.dao.rol.pub.SettingPubAssignCnkiScoreDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.psn.PsnPmJournalRol;
import com.smate.center.batch.model.rol.pub.PsnPmConference;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignCnkiScore;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.rol.pub.SettingPubAssignCnkiScore;
import com.smate.center.batch.model.rol.pub.SettingPubAssignScoreWrap;
import com.smate.center.batch.util.pub.PubXmlDbUtils;

/**
 * 成果匹配服务.
 * 
 * @author liqinghua
 * 
 */
@Service("pubAssignCnkiMatchService")
public class PubAssignCnkiMatchServiceImpl extends BasePubAssignMatchService implements PubAssignCnkiMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = -7865159213230494310L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private Map<Long, SettingPubAssignScoreWrap> settingPubAssignScoreMap =
      new HashMap<Long, SettingPubAssignScoreWrap>();
  @Autowired
  private PubAssignCnkiAuthorDao pubAssignCnkiAuthorDao;
  @Autowired
  private PubAssignCnkiKeywordsDao pubAssignCnkiKeywordsDao;
  @Autowired
  private PubAssignCnkiJournalDao pubAssignCnkiJournalDao;
  @Autowired
  private PubAssignCnkiConferenceDao pubAssignCnkiConferenceDao;
  @Autowired
  private PsnPubStatSyncService psnPubStatSyncService;
  @Autowired
  private PubAssignCnkiScoreDao pubAssignCnkiScoreDao;
  private IPubAssignMatchProcess cnkiPubAssignMatchProcess;
  private IPubAssignMatchProcess cnkiPsnAssignMatchStep1Process;
  @Autowired
  private SettingPubAssignCnkiScoreDao settingPubAssignCnkiScoreDao;
  @Autowired
  private PubAssignCnkiDeptDao pubAssignCnkiDeptDao;
  @Autowired
  private RolPsnInsDao rolPsnInsDao;
  @Autowired
  private InsUnitDao insUnitDao;
  @Autowired
  private PublicationRolService publicationRolService;

  @Override
  public void assignByPub(PublicationRol pub, Long insId) throws ServiceException {

    try {
      // CNKI匹配
      if (pub.getSourceDbId() != null && PubXmlDbUtils.isCnkiDb(Long.valueOf(pub.getSourceDbId()))) {
        // 构造PubAssginMatchContext.
        PubAssginMatchContext context = super.buildPubAssginMatchContext(pub, insId,
            this.getSettingPubAssignScore(pub.getSourceDbId().longValue()));
        // 开始匹配成果
        cnkiPubAssignMatchProcess.start(context);
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
      PubAssginMatchContext context = super.buildPsnAssginMatchContext(psnId, insId, PsnMatchTypeConstants.CNKI);
      // 开始匹配成果
      cnkiPsnAssignMatchStep1Process.start(context);
    } catch (Exception e) {
      logger.error("按CNKI成果匹配人员", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getCnkiNameMatchPubAuthor(Long pubId, Long insId) throws ServiceException {

    try {
      return pubAssignCnkiAuthorDao.getCnkiNameMatchPubAuthor(pubId, insId);
    } catch (Exception e) {
      logger.error("获取匹配上CNKI成果作者名称的单位人员", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getCnkiPubAuthorMatchPsn(Long psnId, Long insId) throws ServiceException {

    try {
      List<Object[]> assignList = pubAssignCnkiAuthorDao.getCnkiPubAuthorMatchPsn(psnId, insId);
      return this.mergeCnkiNameMatchSource(assignList);
    } catch (Exception e) {
      logger.error("获取匹配上单位人员的cnki成果列表", e);
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
  public List<Object[]> mergeCnkiNameMatchSource(List<Object[]> assignList) throws ServiceException {
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
        // 是CNKI成果、并且成果状态不是删除状态
        if (PubXmlDbUtils.isCnkiDb(sourceId) && pid.equals(pubId)) {
          result.add(new Object[] {pubId, sourceId, seqNo, insId});
        }
      }
    }
    return result;
  }

  @Override
  public Map<Long, Long> getKwMatchPubKw(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      return pubAssignCnkiKeywordsDao.getKwMatchPubKw(pubId, psnIds);

    } catch (Exception e) {
      logger.error("获取匹配上CNKI成果关键词的指定人员ID与关键词列表", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, PsnPmJournalRol> getPJMatchPubPJ(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      List<PsnPmJournalRol> list = pubAssignCnkiJournalDao.getPJMatchPubPJ(pubId, psnIds);

      return super.buildPJMatchPubPJMap(list);
    } catch (Exception e) {
      logger.error("获取匹配上CNKI成果期刊的指定人员ID与期刊列表", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, PsnPmConference> getPCMatchPubPc(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      List<PsnPmConference> list = pubAssignCnkiConferenceDao.getPcMatchPubPc(pubId, psnIds);

      return super.buildPCMatchPubPcMap(list);
    } catch (Exception e) {
      logger.error("获取匹配上CNKI成果会议论文的指定人员ID与期刊列表", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, Long> getCnkiConMatchPubAuth(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      List<Object[]> list = this.pubAssignCnkiAuthorDao.getCnkiConMatchPubAuthor(pubId, psnIds);
      return super.buildIsiCoMatchPubAuthMap(list);
    } catch (Exception e) {
      logger.error("获取匹配上CNKI成果作者的指定用户的合作者个数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveAssignScore(PubAssignCnkiScore pubAssignScore) throws ServiceException {

    try {
      // 保存匹配结果
      PubAssignCnkiScore preScore =
          this.pubAssignCnkiScoreDao.getPubAssignScore(pubAssignScore.getPubId(), pubAssignScore.getPsnId());
      if (preScore != null) {
        preScore.copy(pubAssignScore);
      } else {
        preScore = pubAssignScore;
      }
      this.pubAssignCnkiScoreDao.save(preScore);
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
        SettingPubAssignCnkiScore settingCnki = settingPubAssignCnkiScoreDao.getSettingPubAssignScore(dbId);
        setting = new SettingPubAssignScoreWrap(settingCnki);
        settingPubAssignScoreMap.put(dbId, setting);
      }
      return setting;
    } catch (Exception e) {
      logger.error("获取SettingPubAssignScore", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Integer, String> getInsPubDept(List<Integer> seqNos, Long pubId, Long insId) throws ServiceException {

    try {
      List<Object[]> list = pubAssignCnkiDeptDao.getInsPubDept(seqNos, pubId, insId);
      if (list == null || list.size() == 0) {
        return null;
      }
      Map<Integer, String> map = new HashMap<Integer, String>();
      for (Object[] depts : list) {
        Integer seqNo = (Integer) depts[0];
        String deptName = (String) depts[1];
        map.put(seqNo, deptName);
      }
      return map;
    } catch (Exception e) {
      logger.error("获取指定序号、机构ID的CNKI成果部门", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public boolean isPubDeptMatchPsnUnit(Long psnId, Long insId, String deptName) throws ServiceException {

    try {
      // 获取用户在机构的部门ID
      Long unitId = rolPsnInsDao.getPsnUnitId(psnId, insId);
      if (unitId == null) {
        return false;
      }
      // 判断指定机构是否包含用户所在部门或者子部门
      return this.insUnitDao.isDeptContainUnitName(deptName, unitId);
    } catch (Exception e) {
      logger.error("CNKI成果作者部门是否匹配指定用户所在部门", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePubAssignScore(Long psnId, Long insId) throws ServiceException {

    try {
      this.pubAssignCnkiScoreDao.remove(psnId, insId);
    } catch (Exception e) {
      logger.error("删除人员所在单位匹配关系", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePubAssignScore(Long pubId) throws ServiceException {
    try {
      this.pubAssignCnkiScoreDao.removeByPubId(pubId);
    } catch (Exception e) {
      logger.error("删除成果匹配关系", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePubPsnAssignScore(Long pubId, Long psnId) throws ServiceException {
    try {
      this.pubAssignCnkiScoreDao.removePubPsnAssignScore(pubId, psnId);
    } catch (Exception e) {
      logger.error("删除成果匹配关系", e);
      throw new ServiceException(e);
    }
  }

  public void setCnkiPubAssignMatchProcess(IPubAssignMatchProcess cnkiPubAssignMatchProcess) {
    this.cnkiPubAssignMatchProcess = cnkiPubAssignMatchProcess;
  }

  public void setCnkiPsnAssignMatchStep1Process(IPubAssignMatchProcess cnkiPsnAssignMatchStep1Process) {
    this.cnkiPsnAssignMatchStep1Process = cnkiPsnAssignMatchStep1Process;
  }

}
