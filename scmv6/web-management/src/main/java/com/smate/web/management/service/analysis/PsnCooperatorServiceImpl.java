package com.smate.web.management.service.analysis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.security.PsnPrivateDao;
import com.smate.web.management.dao.analysis.sns.CooperatorMayRecommendDao;
import com.smate.web.management.dao.analysis.sns.CooperatorMayRunDao;
import com.smate.web.management.dao.analysis.sns.PersonTaughtDao;
import com.smate.web.management.dao.analysis.sns.PsnAreaClassifyDao;
import com.smate.web.management.dao.analysis.sns.PsnInsDetailDao;
import com.smate.web.management.dao.analysis.sns.PsnJournalDao;
import com.smate.web.management.dao.analysis.sns.PsnJournalGradeDao;
import com.smate.web.management.dao.analysis.sns.PsnNfcPrjGradeDao;
import com.smate.web.management.dao.analysis.sns.PsnPositionDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.analysis.sns.CooperatorMayRecommend;
import com.smate.web.management.model.analysis.sns.CooperatorMayRun;
import com.smate.web.management.model.analysis.sns.PsnInsDetail;
import com.smate.web.management.model.analysis.sns.PsnJournalGrade;
import com.smate.web.management.model.analysis.sns.PsnNfcPrjGrade;
import com.smate.web.management.model.analysis.sns.PsnPositionGrade;
import com.smate.web.management.model.analysis.sns.RecommendScore;

/**
 * 
 * 基金、论文合作者推荐：遍历所有符合必要条件的人员实现.
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnCooperatorService")
@Transactional(rollbackFor = Exception.class)
public class PsnCooperatorServiceImpl implements PsnCooperatorService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  // 大类相同
  // 表：psn_area_classify(psn_id,classify);

  // 级别>=2
  // //单位级别，表：scholar2.psn_ins_detail(psn_id,ins_id,grade)
  // //职称级别，表：schoalr2.psn_postion_grade(psn_id,grade)
  // //发表期刊级别,表：scholar2.psn_journal_grade(psn_id,grade)
  // //同单位，表：scholar2.psn_ins_detail(psn_id,ins_id)
  // //申请级别，表：psn_nsfcprj_grade(psn_id,prj_grade)

  @Autowired
  private CooperatorMayRunDao cooperatorMayRunDao;
  @Autowired
  private CooperatorMayRecommendDao cooperatorMayRecommendDao;
  @Autowired
  private PsnAreaClassifyDao psnAreaClassifyDao;
  @Autowired
  private PsnInsDetailDao psnInsDetailDao;
  @Autowired
  private PsnPositionDao psnPositionDao;
  @Autowired
  private PsnJournalGradeDao psnJournalGradeDao;
  @Autowired
  private PsnNfcPrjGradeDao psnNfcPrjGradeDao;
  @Autowired
  private PsnJournalDao psnJournalDao;
  @Autowired
  private PersonTaughtDao personTaughtDao;
  @Autowired
  private PsnPrivateDao psnPrivateDao;

  // 可能合作者推荐任务队列(人员列表).
  @Override
  public List<CooperatorMayRun> taskRunList() throws Exception {
    try {
      return cooperatorMayRunDao.taskRunList();
    } catch (DaoException e) {
      logger.error("可能合作者推荐任务队列(人员列表)获取失败，cooperator_may_run:status in(0,1)", e);
      throw new Exception(e);
    }
  }

  // 可能合作者推荐任务队列执行失败.
  @Override
  public void taskRunListError(List<CooperatorMayRun> cmrList) {
    try {

      if (cmrList != null && cmrList.size() > 0) {
        for (CooperatorMayRun obj : cmrList) {
          CooperatorMayRun cmr = cooperatorMayRunDao.get(obj.getPsnId());
          cmr.setStatus(STATUS_FAIL);
          cmr.setMsg(MSG_FATAL_ERROR);// 人员推荐抛出不可预料失败
          cooperatorMayRunDao.save(cmr);
        }
      }
    } catch (Exception e) {
      logger.error("可能合作者推荐任务队列失败状态设置失败，cooperator_may_run:status in(0,1)", e);
    }
  }

  // 可能合作者推荐任务重新执行（挂起推荐记录重新进入执行队列）.
  @Override
  public void taskSuspendToRun() throws Exception {
    try {
      List<CooperatorMayRun> cmrList = cooperatorMayRunDao.taskSuspendList();
      if (cmrList != null && cmrList.size() > 0) {
        for (CooperatorMayRun cmr : cmrList) {
          cmr.setStatus(STATUS_RERUN);// 挂起推荐记录重新进入执行队列
          cooperatorMayRunDao.save(cmr);
        }
      }
    } catch (DaoException e) {
      logger.error("可能合作者推荐任务重新执行（挂起推荐记录重新进入执行队列）失败，cooperator_may_run:status=-1", e);
      throw new Exception(e);
    }
  }

  // 合作者推荐任务执行成功.
  @Override
  public void taskRunSucc(Long psnId) throws Exception {
    try {
      // 执行成功
      CooperatorMayRun cmr = cooperatorMayRunDao.get(psnId);
      cmr.setStatus(STATUS_SUCC);
      cmr.setMsg("");
      cmr.setDate(new Date());
      cooperatorMayRunDao.save(cmr);
    } catch (Exception e) {
      logger.error("可能合作者推荐任务写入成功状态执行失败，cooperator_may_run:psn_id={}", psnId, e);
      throw new Exception(e);
    }
  }

  // 合作者推荐任务执行失败.
  @Override
  public void taskRunFail(Long psnId, String errMsg) throws Exception {
    try {
      CooperatorMayRun cmr = cooperatorMayRunDao.get(psnId);
      cmr.setStatus(STATUS_FAIL);
      if (errMsg.length() < 50) {// 个别推荐数据异常
        cmr.setMsg(MSG_CALC_ERROR + "(" + errMsg + ")");
      } else {
        cmr.setMsg(MSG_CALC_ERROR);// 人员推荐数据级别异常
      }
      cmr.setDate(new Date());
      cooperatorMayRunDao.save(cmr);
    } catch (Exception e) {
      logger.error("可能合作者推荐任务写入失败状态执行失败，cooperator_may_run:psn_id={}", psnId, e);
      throw new Exception(e);
    }
  }

  @Override
  public PsnInsDetail getPsnInsDetail(Long psnId) throws Exception {
    PsnInsDetail pid = psnInsDetailDao.get(psnId);
    return pid;
  }

  // 人员大类
  @Override
  public List<String> findClassifyList(Long psnId) throws Exception {

    List<String> classifyList = psnAreaClassifyDao.getPsnAreaClassifyStr(psnId);
    return classifyList;
  }

  // 更新可能合作者推荐记录
  @Override
  public void saveCMR(Long psnId, Long coPsnId, RecommendScore rs) throws Exception {
    try {
      CooperatorMayRecommend recommend = cooperatorMayRecommendDao.findRecommend(psnId, coPsnId);
      if (recommend == null) {// 如不存在，则新创建
        recommend = new CooperatorMayRecommend();
        recommend.setPsnId(psnId);
        recommend.setCoPsnId(coPsnId);

      } else {// 合作者版本号加１
        recommend.setCoVersion(recommend.getCoVersion() + 1);
      }

      Integer coDegree = rs.getDegreeScore().getTotalScore();
      Integer coDept = rs.getRelevanceScore().getDeptScore();
      Integer coJnl = rs.getRelevanceScore().getJnlScore();
      Integer coTaught = rs.getRelevanceScore().getTaughtScore();
      Integer coQuality = rs.getQualityScore();
      // 构造推荐比较对象
      CooperatorMayRecommend refresh =
          new CooperatorMayRecommend(psnId, coPsnId, coQuality, coDept, coJnl, coTaught, coDegree);
      rs.getTotalScore();
      if (!refresh.equals(recommend)) {// 只有计算分数有变化，才进行更新
        recommend.setCreateDate(new Date());
        recommend.setCoDegree(coDegree);
        recommend.setCoDept(coDept);
        recommend.setCoJnl(coJnl);
        recommend.setCoTaught(coTaught);
        recommend.setCoQuality(coQuality);
        recommend.setCoTotal(rs.getTotalScore());
      }

      if (rs.getRelevanceScore().getKwScore() > 0) {// 保存临时关键词分数
        recommend.setTmpCoKw(rs.getRelevanceScore().getKwScore());
        recommend.setCoTotal(rs.getTotalScore());
      }
      cooperatorMayRecommendDao.save(recommend);
    } catch (DaoException e) {
      logger.error("可能合作者记录查找失败，cooperator_may_run:psn_id={},co_psn_id={}", psnId, coPsnId, e);
      throw new Exception(e);
    }
  }

  // 统计可能合作者数量失败
  @Override
  public Map<String, Long> cooperatorMayCount(List<String> classifyList, Integer insGrade, Integer posGrade,
      Integer jnlGrade, Long insId, Integer prjGrade, Long psnId, Long deptEnHash, Long deptZhHash,
      List<String> issnTxtList, List<Long> tauhtHashList) throws Exception {
    try {
      return cooperatorMayRunDao.cooperatorMayCount(classifyList, insGrade, posGrade, jnlGrade, insId, prjGrade, psnId,
          deptEnHash, deptZhHash, issnTxtList, tauhtHashList);
    } catch (DaoException e) {
      logger.error("统计可能合作者数量失败，psnId={}", psnId, e);
      throw new Exception(e);
    }
  }

  // 可能合作者人员id列表
  @Override
  public List<List<Long>> cooperatorMayList(List<String> classifyList, Integer insGrade, Integer posGrade,
      Integer jnlGrade, Long insId, Integer prjGrade, Map<String, Long> sumMap, Long psnId, Long deptEnHash,
      Long deptZhHash, List<String> issnTxtList, List<Long> tauhtHashList) throws Exception {

    try {
      return cooperatorMayRunDao.cooperatorMayList(classifyList, insGrade, posGrade, jnlGrade, insId, prjGrade, sumMap,
          psnId, deptEnHash, deptZhHash, issnTxtList, tauhtHashList);
    } catch (DaoException e) {
      logger.error("可能合作者列表失败，cooperator_may_run", e);
      throw new Exception(e);
    }
  }

  // 统计可能合作者数量失败(关键词专用)
  @Override
  public Map<String, Long> cooperatorMayCount(List<String> classifyList, Set<Long> kwPsnIdList, Integer insGrade,
      Integer posGrade, Integer jnlGrade, Long insId, Integer prjGrade, Long psnId) throws Exception {
    try {
      return cooperatorMayRunDao.cooperatorMayCount(classifyList, kwPsnIdList, insGrade, posGrade, jnlGrade, insId,
          prjGrade, psnId);
    } catch (DaoException e) {
      logger.error("统计可能合作者数量失败，psnId={}", psnId, e);
      throw new Exception(e);
    }
  }

  // 可能合作者人员id列表(关键词专用)
  @Override
  public List<List<Long>> cooperatorMayList(List<String> classifyList, Set<Long> kwPsnIdList, Integer insGrade,
      Integer posGrade, Integer jnlGrade, Long insId, Integer prjGrade, Map<String, Long> sumMap, Long psnId)
      throws Exception {

    try {
      return cooperatorMayRunDao.cooperatorMayList(classifyList, kwPsnIdList, insGrade, posGrade, jnlGrade, insId,
          prjGrade, sumMap, psnId);
    } catch (DaoException e) {
      logger.error("可能合作者列表失败，cooperator_may_run", e);
      throw new Exception(e);
    }
  }

  // 单位级别
  @Override
  public Integer insGrade(PsnInsDetail pid) throws Exception {
    Integer insGrade = null;
    if (pid != null && pid.getGrade() != null) {
      insGrade = pid.getGrade();
    }

    return insGrade;
  }

  // 职称级别
  @Override
  public Integer posGrade(Long psnId) throws Exception {
    Integer posGrade = null;

    PsnPositionGrade ppg = psnPositionDao.getPsnPositionGrade(psnId);
    if (ppg != null && ppg.getGrade() != null) {
      posGrade = ppg.getGrade();
    }

    return posGrade;
  }

  // 期刊级别
  @Override
  public Integer jnlGrade(Long psnId) throws Exception {
    Integer jnlGrade = null;

    PsnJournalGrade pjg = psnJournalGradeDao.get(psnId);
    if (pjg != null && pjg.getGrade() != null) {
      jnlGrade = pjg.getGrade();
    }

    return jnlGrade;
  }

  // 单位insId
  @Override
  public Long insId(PsnInsDetail pid) throws Exception {
    Long insId = null;
    if (pid != null && pid.getInsId() != null) {
      insId = pid.getInsId();
    }

    return insId;
  }

  // 申请级别
  @Override
  public Integer prjGrade(Long psnId) throws Exception {
    Integer prjGrade = null;
    PsnNfcPrjGrade ppg = psnNfcPrjGradeDao.get(psnId);
    if (ppg != null && ppg.getPrjGrade() != null) {
      prjGrade = ppg.getPrjGrade();
    }
    return prjGrade;

  }

  // 检查级别信息是否足够，超过3项内容，匹配无意义
  @Override
  public boolean isGradePass(Object[] checks, Long psnId) throws Exception {

    return this.isGradePass(checks, psnId, true);

  }

  @Override
  public boolean isGradePass(Object[] checks, Long psnId, boolean isTask) throws Exception {
    int counter = 0;
    for (Object c : checks) {
      if (c == null) {
        counter++;
      }
    }
    if (counter > 3) {
      if (isTask) {
        CooperatorMayRun cmr = cooperatorMayRunDao.get(psnId);
        cmr.setStatus(STATUS_SUCC);
        cmr.setMsg(MSG_LEVEL_EMPTY);
        cmr.setDate(new Date());
        cooperatorMayRunDao.save(cmr);
      }
      return false;
    }
    return true;

  }

  // 无可能合作者，匹配无意义
  @Override
  public boolean isNoRecord(Map<String, Long> sumMap, Long psnId) throws Exception {
    int counter = 0;
    for (Long c : sumMap.values()) {
      if (c != null && c > 0) {
        counter += c;
      }
    }
    if (counter == 0) {
      CooperatorMayRun cmr = cooperatorMayRunDao.get(psnId);
      cmr.setStatus(STATUS_SUCC);
      cmr.setMsg(MSG_RECORD_EMPTY);
      cmr.setDate(new Date());
      cooperatorMayRunDao.save(cmr);
      return false;
    }
    return true;

  }

  // 检查是否有大类
  @Override
  public boolean isClassifyPass(List<String> classifyList, Long psnId) throws Exception {
    if (classifyList == null || classifyList.size() == 0) {// 人员没有大类，跳过
      CooperatorMayRun cmr = cooperatorMayRunDao.get(psnId);
      cmr.setStatus(STATUS_SUSPEND);
      cmr.setMsg(MSG_CLASSIFY_EMPTY);
      cmr.setDate(new Date());
      cooperatorMayRunDao.save(cmr);
      return false;
    }
    return true;
  }

  // 计算耗时超过允许范围，先挂起
  @Override
  public boolean isSuspend(Map<String, Long> sumMap, Long psnId) throws Exception {
    Long counter = 0L;
    if (sumMap != null) {
      for (Long val : sumMap.values()) {
        counter += val;
      }
    }

    if (counter > MAX_RECORD_FOUND) {// 耗时任务，先挂起，最后再执行
      CooperatorMayRun cmr = cooperatorMayRunDao.get(psnId);
      cmr.setStatus(STATUS_SUSPEND);
      cmr.setMsg(String.format(MSG_MORE_RECORD, MAX_RECORD_FOUND, counter));
      cmr.setDate(new Date());
      cooperatorMayRunDao.save(cmr);
      return true;
    }
    return false;
  }

  @Override
  public void delPassCoVersion(Long psnId) throws Exception {
    try {
      cooperatorMayRecommendDao.delPassCoVersion(psnId);
    } catch (DaoException e) {
      logger.error("删除过时的合作者数据失败，cooperator_may_recommend:psnId={}", psnId, e);
      throw new Exception(e);
    }

  }

  @Override
  public void delTmpCoKwRecord(Long psnId) throws Exception {
    try {
      cooperatorMayRecommendDao.delTmpCoKwRecord(psnId);
    } catch (DaoException e) {
      logger.error("删除合作者数据(关键词临时记录)失败，cooperator_may_recommend:psnId={}", psnId, e);
      throw new Exception(e);
    }

  }

  @Override
  public void delCoRecord(Long psnId) throws Exception {
    try {
      cooperatorMayRecommendDao.delCoRecord(psnId);
    } catch (DaoException e) {
      logger.error("删除合作者数据失败，cooperator_may_recommend:psnId={}", psnId, e);
      throw new Exception(e);
    }

  }

  @Override
  public boolean addNewUserToTask() throws Exception {
    try {
      return cooperatorMayRunDao.addNewUserToTask();
    } catch (DaoException e) {
      logger.error("合作者推荐，将用户添加到任务表中失败，cooperator_may_run", e);
      throw new Exception(e);
    }

  }

  @Override
  public boolean resetUsersToTask() throws Exception {
    try {
      return cooperatorMayRunDao.resetUsersToTask();
    } catch (DaoException e) {
      logger.error("合作者推荐，重置用户任务失败，cooperator_may_run", e);
      throw new Exception(e);
    }

  }

  // 删除合作者分数值低的数据
  @Override
  public void delLowScoreRecord(Long psnId) throws Exception {
    try {
      cooperatorMayRecommendDao.delLowScoreRecord(psnId);
    } catch (DaoException e) {
      logger.error("删除分数值低的数据失败，cooperator_may_recommend:psnId={}", psnId, e);
      throw new Exception(e);
    }

  }

  /**
   * 获取人员的issn列表．
   * 
   * @param psnId
   * @return
   */
  @Override
  public List<String> getPsnIssnTxtList(Long psnId) throws Exception {
    try {
      List<String> issnTxtList = psnJournalDao.getPsnIssnTxtList(psnId);
      if (issnTxtList == null || issnTxtList.size() == 0) {
        issnTxtList = new ArrayList<String>();
        issnTxtList.add(ISSN_TXT_NO_MATCH);
      }
      if (issnTxtList.size() > 1000) {
        return issnTxtList.subList(0, 1000);
      }
      return issnTxtList;
    } catch (DaoException e) {
      logger.error("获取人员的issn列表失败，psn_journal:psnId={}", psnId, e);
      throw new Exception(e);
    }
  }

  /**
   * 获取人员的所教课程hash列表.
   * 
   * @param psnId
   * @return
   */
  @Override
  public List<Long> getPersonTaughHashList(Long psnId) throws Exception {
    try {
      List<Long> taughHashList = personTaughtDao.getPersonTaughHashList(psnId);
      if (taughHashList == null || taughHashList.size() == 0) {
        taughHashList = new ArrayList<Long>();
        taughHashList.add(-1L);
      }
      if (taughHashList.size() > 1000) {
        return taughHashList.subList(0, 1000);
      }
      return taughHashList;
    } catch (DaoException e) {
      logger.error("获取人员的所教课程hash列表失败，person_taught_hash:psnId={}", psnId, e);
      throw new Exception(e);
    }
  }

  @Override
  public boolean isPsnPrivate(Long psnId) throws Exception {
    try {
      return psnPrivateDao.isPsnPrivate(psnId) > 0;
    } catch (Exception e) {
      logger.error("合作者推荐，判断是否为隐私用户失败，psn_private={}", psnId, e);
      throw new Exception(e);
    }

  }
}
