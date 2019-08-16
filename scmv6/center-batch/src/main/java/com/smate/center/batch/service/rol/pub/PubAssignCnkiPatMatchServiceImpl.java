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
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiPatAuthorDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiPatDeptDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiPatKeywordsDao;
import com.smate.center.batch.dao.rol.pub.PubAssignCnkiPatScoreDao;
import com.smate.center.batch.dao.rol.pub.SettingPubAssignCnkiPatScoreDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.rol.pub.PubAssginMatchContext;
import com.smate.center.batch.model.rol.pub.PubAssignCnkiPatScore;
import com.smate.center.batch.model.rol.pub.PublicationRol;
import com.smate.center.batch.model.rol.pub.SettingPubAssignCnkiPatScore;
import com.smate.center.batch.model.rol.pub.SettingPubAssignScoreWrap;
import com.smate.center.batch.util.pub.PubXmlDbUtils;

/**
 * 成果匹配服务.
 * 
 * @author liqinghua
 * 
 */
@Service("pubAssignCnkiPatMatchService")
public class PubAssignCnkiPatMatchServiceImpl extends BasePubAssignMatchService
    implements PubAssignCnkiPatMatchService {

  /**
   * 
   */
  private static final long serialVersionUID = -7768282459099865216L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  private Map<Long, SettingPubAssignScoreWrap> settingPubAssignScoreMap =
      new HashMap<Long, SettingPubAssignScoreWrap>();
  @Autowired
  private PubAssignCnkiPatAuthorDao pubAssignCnkiPatAuthorDao;
  @Autowired
  private PubAssignCnkiPatKeywordsDao pubAssignCnkiPatKeywordsDao;
  @Autowired
  private PsnPubStatSyncService psnPubStatSyncService;
  @Autowired
  private PubAssignCnkiPatScoreDao pubAssignCnkiPatScoreDao;
  private IPubAssignMatchProcess cnkiPubAssignMatchProcess;
  private IPubAssignMatchProcess cnkiPsnAssignMatchStep1Process;
  @Autowired
  private SettingPubAssignCnkiPatScoreDao settingPubAssignCnkiPatScoreDao;
  @Autowired
  private PubAssignCnkiPatDeptDao pubAssignCnkiPatDeptDao;
  @Autowired
  private RolPsnInsDao rolPsnInsDao;
  @Autowired
  private InsUnitDao insUnitDao;
  @Autowired
  private PublicationRolService publicationRolService;

  @Override
  public void assignByPub(PublicationRol pub, Long insId) throws ServiceException {

    try {
      // CnkiPat匹配
      if (pub.getSourceDbId() != null && PubXmlDbUtils.isCnkipatDb(Long.valueOf(pub.getSourceDbId()))) {
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
      PubAssginMatchContext context = super.buildPsnAssginMatchContext(psnId, insId, PsnMatchTypeConstants.CNKIPAT);
      // 开始匹配成果
      cnkiPsnAssignMatchStep1Process.start(context);
    } catch (Exception e) {
      logger.error("按CNKIPAT成果匹配人员", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getCnkiPatNameMatchPubAuthor(Long pubId, Long insId) throws ServiceException {

    try {
      return pubAssignCnkiPatAuthorDao.getCnkiPatNameMatchPubAuthor(pubId, insId);
    } catch (Exception e) {
      logger.error("获取匹配上CNKIPAT成果作者名称的单位人员", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public List<Object[]> getCnkiPatPubAuthorMatchPsn(Long psnId, Long insId) throws ServiceException {

    try {
      List<Object[]> assignList = pubAssignCnkiPatAuthorDao.getCnkiPatPubAuthorMatchPsn(psnId, insId);
      return this.mergeCnkiPatNameMatchSource(assignList);
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
  public List<Object[]> mergeCnkiPatNameMatchSource(List<Object[]> assignList) throws ServiceException {
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
        // 是CNKIPAT成果、并且成果状态不是删除状态
        if (PubXmlDbUtils.isCnkipatDb(sourceId) && pid.equals(pubId)) {
          result.add(new Object[] {pubId, sourceId, seqNo, insId});
        }
      }
    }
    return result;
  }

  @Override
  public Map<Long, Long> getKwMatchPubKw(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      return pubAssignCnkiPatKeywordsDao.getKwMatchPubKw(pubId, psnIds);

    } catch (Exception e) {
      logger.error("获取匹配上CNKIPAT成果关键词的指定人员ID与关键词列表", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Map<Long, Long> getCnkiPatConMatchPubAuth(Long pubId, Set<Long> psnIds) throws ServiceException {

    try {
      List<Object[]> list = this.pubAssignCnkiPatAuthorDao.getCnkiPatConMatchPubAuthor(pubId, psnIds);
      return super.buildIsiCoMatchPubAuthMap(list);
    } catch (Exception e) {
      logger.error("获取匹配上CNKIPAT成果作者的指定用户的合作者个数", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void saveAssignScore(PubAssignCnkiPatScore pubAssignScore) throws ServiceException {

    try {
      // 保存匹配结果
      PubAssignCnkiPatScore preScore =
          this.pubAssignCnkiPatScoreDao.getPubAssignScore(pubAssignScore.getPubId(), pubAssignScore.getPsnId());
      if (preScore != null) {
        preScore.copy(pubAssignScore);
      } else {
        preScore = pubAssignScore;
      }
      this.pubAssignCnkiPatScoreDao.save(preScore);
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
        SettingPubAssignCnkiPatScore settingCnkiPat = settingPubAssignCnkiPatScoreDao.getSettingPubAssignScore(dbId);
        setting = new SettingPubAssignScoreWrap(settingCnkiPat);
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
      List<Object[]> list = pubAssignCnkiPatDeptDao.getInsPubDept(seqNos, pubId, insId);
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
      logger.error("获取指定序号、机构ID的CNKIPAT成果部门", e);
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
      logger.error("CNKIPAT成果作者部门是否匹配指定用户所在部门", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePubAssignScore(Long psnId, Long insId) throws ServiceException {

    try {
      this.pubAssignCnkiPatScoreDao.remove(psnId, insId);
    } catch (Exception e) {
      logger.error("删除人员所在单位匹配关系", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePubAssignScore(Long pubId) throws ServiceException {
    try {
      this.pubAssignCnkiPatScoreDao.removeByPubId(pubId);
    } catch (Exception e) {
      logger.error("删除成果匹配关系", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void removePubPsnAssignScore(Long pubId, Long psnId) throws ServiceException {
    try {
      this.pubAssignCnkiPatScoreDao.removePubPsnAssignScore(pubId, psnId);
    } catch (Exception e) {
      logger.error("删除成果匹配关系", e);
      throw new ServiceException(e);
    }
  }

  public void setCnkiPatPubAssignMatchProcess(IPubAssignMatchProcess cnkiPubAssignMatchProcess) {
    this.cnkiPubAssignMatchProcess = cnkiPubAssignMatchProcess;
  }

  public void setCnkiPatPsnAssignMatchStep1Process(IPubAssignMatchProcess cnkiPsnAssignMatchStep1Process) {
    this.cnkiPsnAssignMatchStep1Process = cnkiPsnAssignMatchStep1Process;
  }

}
