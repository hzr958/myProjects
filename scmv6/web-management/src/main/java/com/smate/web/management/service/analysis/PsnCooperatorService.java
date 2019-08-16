package com.smate.web.management.service.analysis;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.smate.web.management.model.analysis.sns.CooperatorMayRun;
import com.smate.web.management.model.analysis.sns.PsnInsDetail;
import com.smate.web.management.model.analysis.sns.RecommendScore;

/**
 * 
 * 基金、论文合作者推荐：遍历所有符合必要条件的人员接口.
 * 
 * @author zhuangyanming
 * 
 */
public interface PsnCooperatorService {
  // 任务状态:0进入任务队列，1挂起任务进入队列，2执行成功，-1挂起任务，-2执行失败
  Integer STATUS_RUN = 0;
  Integer STATUS_RERUN = 1;
  Integer STATUS_SUCC = 2;
  Integer STATUS_SUSPEND = -1;
  Integer STATUS_FAIL = -2;
  // 最大允许记录数量
  Long MAX_RECORD_FOUND = 1000L;

  // 关键词匹配最多人员数量
  Integer MAX_MATCH_KW_PSN = 100;
  // 关键词结果集最多人员数量
  Integer MAX_KW_PSN = 30;

  String MSG_CLASSIFY_EMPTY = "人员没有大类，先挂起，最后执行";
  String MSG_LEVEL_EMPTY = "人员级别信息不足，忽略推荐合作者";
  String MSG_RECORD_EMPTY = "人员合作者数据不足，忽略推荐合作者";
  String MSG_MORE_RECORD = "人员匹配数据超量，允许数量%d，现数量%d，先挂起，最后执行";
  String MSG_CALC_ERROR = "人员推荐数据级别失败";
  String MSG_FATAL_ERROR = "人员推荐抛出不可预料失败";
  String MSG_SOME_ERROR = "个别推荐人员计算失败，coPsnId=%s";
  String ISSN_TXT_NO_MATCH = "no match in issn_txt";

  /**
   * 可能合作者推荐任务队列(人员列表).
   * 
   * @return
   */
  List<CooperatorMayRun> taskRunList() throws Exception;

  /**
   * 可能合作者推荐任务队列执行失败.
   * 
   * @param cmrList
   */
  void taskRunListError(List<CooperatorMayRun> cmrList);

  /**
   * 可能合作者推荐任务重新执行（挂起推荐记录重新进入执行队列）.
   * 
   * @throws Exception
   */
  void taskSuspendToRun() throws Exception;

  /**
   * 合作者推荐任务执行成功.
   * 
   * @param psnId
   * @throws Exception
   */
  void taskRunSucc(Long psnId) throws Exception;

  /**
   * 合作者推荐任务执行失败.
   * 
   * @param psnId
   * @param errMsg
   * @throws Exception
   */
  void taskRunFail(Long psnId, String errMsg) throws Exception;

  /**
   * 人员单位信息.
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  PsnInsDetail getPsnInsDetail(Long psnId) throws Exception;

  /**
   * 人员大类.
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  List<String> findClassifyList(Long psnId) throws Exception;

  /**
   * 更新可能合作者推荐记录.
   * 
   * @param psnId
   * @param coPsnId
   * @param rs
   * @throws Exception
   */
  void saveCMR(Long psnId, Long coPsnId, RecommendScore rs) throws Exception;

  /**
   * 统计可能合作者数量.
   * 
   * @param classifyList
   * @param insGrade
   * @param posGrade
   * @param jnlGrade
   * @param insId
   * @param prjGrade
   * @param psnId
   * @return
   * @throws Exception
   */
  Map<String, Long> cooperatorMayCount(List<String> classifyList, Integer insGrade, Integer posGrade, Integer jnlGrade,
      Long insId, Integer prjGrade, Long psnId, Long deptEnHash, Long deptZhHash, List<String> issnTxtList,
      List<Long> tauhtHashList) throws Exception;

  /**
   * 可能合作者人员id列表.
   * 
   * @param classifyList
   * @param insGrade
   * @param posGrade
   * @param jnlGrade
   * @param insId
   * @param prjGrade
   * @param sumMap
   * @return
   * @throws Exception
   */
  List<List<Long>> cooperatorMayList(List<String> classifyList, Integer insGrade, Integer posGrade, Integer jnlGrade,
      Long insId, Integer prjGrade, Map<String, Long> sumMap, Long psnId, Long deptEnHash, Long deptZhHash,
      List<String> issnTxtList, List<Long> tauhtHashList) throws Exception;

  /**
   * 统计可能合作者数量.
   * 
   * @param classifyList
   * @param kwPsnIdList
   * @param insGrade
   * @param posGrade
   * @param jnlGrade
   * @param insId
   * @param prjGrade
   * @param psnId
   * @return
   * @throws Exception
   */
  Map<String, Long> cooperatorMayCount(List<String> classifyList, Set<Long> kwPsnIdList, Integer insGrade,
      Integer posGrade, Integer jnlGrade, Long insId, Integer prjGrade, Long psnId) throws Exception;

  /**
   * 可能合作者人员id列表.
   * 
   * @param classifyList
   * @param kwPsnIdList
   * @param insGrade
   * @param posGrade
   * @param jnlGrade
   * @param insId
   * @param prjGrade
   * @param sumMap
   * @return
   * @throws Exception
   */
  List<List<Long>> cooperatorMayList(List<String> classifyList, Set<Long> kwPsnIdList, Integer insGrade,
      Integer posGrade, Integer jnlGrade, Long insId, Integer prjGrade, Map<String, Long> sumMap, Long psnId)
      throws Exception;

  // 单位级别
  Integer insGrade(PsnInsDetail pid) throws Exception;

  // 职称级别
  Integer posGrade(Long psnId) throws Exception;

  // 期刊级别
  Integer jnlGrade(Long psnId) throws Exception;

  // 单位insId
  Long insId(PsnInsDetail pid) throws Exception;

  // 申请级别
  Integer prjGrade(Long psnId) throws Exception;

  // 检查级别信息是否足够，超过3项内容，匹配无意义
  boolean isGradePass(Object[] checks, Long psnId) throws Exception;

  boolean isGradePass(Object[] checks, Long psnId, boolean isTask) throws Exception;

  // 无可能合作者，匹配无意义
  boolean isNoRecord(Map<String, Long> sumMap, Long psnId) throws Exception;

  // 检查是否有大类
  boolean isClassifyPass(List<String> classifyList, Long psnId) throws Exception;

  // 计算耗时超过允许范围，先挂起
  boolean isSuspend(Map<String, Long> sumMap, Long psnId) throws Exception;

  // 删除过时的合作者版本
  void delPassCoVersion(Long psnId) throws Exception;

  // 删除合作者数据(关键词临时记录)
  void delTmpCoKwRecord(Long psnId) throws Exception;

  // 删除合作者数据
  void delCoRecord(Long psnId) throws Exception;

  // 删除合作者分数值低的数据
  void delLowScoreRecord(Long psnId) throws Exception;

  // 合作者推荐，将用户添加到任务表中
  boolean addNewUserToTask() throws Exception;

  // 合作者推荐，重置用户任务
  boolean resetUsersToTask() throws Exception;

  /**
   * 获取人员的issn列表．
   * 
   * @param psnId
   * @return
   */
  List<String> getPsnIssnTxtList(Long psnId) throws Exception;

  /**
   * 获取人员的所教课程hash列表.
   * 
   * @param psnId
   * @return
   */
  List<Long> getPersonTaughHashList(Long psnId) throws Exception;

  /**
   * 判断是否为隐私用户.
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  boolean isPsnPrivate(Long psnId) throws Exception;

}
