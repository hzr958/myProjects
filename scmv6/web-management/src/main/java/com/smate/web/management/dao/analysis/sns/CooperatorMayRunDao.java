package com.smate.web.management.dao.analysis.sns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.management.model.analysis.DaoException;
import com.smate.web.management.model.analysis.sns.CooperatorMayRun;

/**
 * 基金、论文推荐合作者：计算可能合作者控制Dao.
 * 
 * @author zhuangyanming
 * 
 */

@Repository
public class CooperatorMayRunDao extends SnsHibernateDao<CooperatorMayRun, Long> {
  // 两两组合查询对象
  private final String[] HQL_OBJS =
      {"PsnInsDetail t1", "PsnPositionGrade t2", "PsnJournalGrade t3", "PsnInsDetail t4", "PsnNfcPrjGrade t5"};
  // 两两组合筛选条件
  private final String[] FILTER_COND = {"t1.grade=?", "t2.grade=?", "t3.grade=?", "t4.insId=?", "t5.prjGrade=?"};
  // 两两组合连接条件
  private final String[] JOIN_COND = {"t1.psnId", "t2.psnId", "t3.psnId", "t4.psnId", "t5.psnId"};

  private final String HQL_PATTERN = "select %s from PsnAreaClassify t where "
      + " exists(select 1 from %s where %s=t.psnId and %s) and exists(select 1 from %s where %s=t.psnId and %s)"
      + " and t.psnId<>? %s %s %s";

  // 相关度大于0（不包括关键词）
  private final String EXT_COND = " and (exists"
      + "(select 1 from PsnInsDetail a1 where a1.psnId=t.psnId and (a1.deptEnHash=? or a1.deptZhHash=?))"
      + " or exists(select 1 from PsnJournal a1 where a1.psnId=t.psnId and a1.issnTxt in(:extIssnTxt))"
      + " or exists(select 1 from PersonTaughtHash a1 where a1.psnId=t.psnId and a1.tauhtHash in(:extTauhtHash))" + ")";

  // 当前用户大类条件
  private final String PAC_COND = " and t.classify in(:classify)";
  // 相关度大于0（仅有关键词）
  private final String KW_COND = " and t.psnId in(:psnId)";

  // 最大查询数量
  private final Integer MAX_RESULT = 500;
  // 取数据的起始位置标识
  private final String START_FETCH = "fetch";

  /**
   * 统计可能合作者的数量(两两关联查询).
   * 
   * @param classifyList
   * @param insGrade
   * @param posGrade
   * @param jnlGrade
   * @param insId
   * @param prjGrade
   * @return
   */
  public Map<String, Long> cooperatorMayCount(List<String> classifyList, Integer insGrade, Integer posGrade,
      Integer jnlGrade, Long insId, Integer prjGrade, Long psnId, Long deptEnHash, Long deptZhHash,
      List<String> issnTxtList, List<Long> tauhtHashList) throws DaoException {
    Object[] params = {insGrade, posGrade, jnlGrade, insId, prjGrade};
    // 符合必要条件的人员数量
    Map<String, Long> sumMap = new HashMap<String, Long>();
    List<String> doList = new ArrayList<String>();// 记录执行过的语句，防止重复执行
    boolean isClassifyEmpty = CollectionUtils.isEmpty(classifyList);// 大类是否为空
    // 构造数量统计语句
    for (int i = 0; i < HQL_OBJS.length; i++) {
      String fristObj = HQL_OBJS[i];
      String fristCond = FILTER_COND[i];
      String fristJoin = JOIN_COND[i];
      Object fristParam = params[i];
      for (int j = 0; j < HQL_OBJS.length; j++) {
        Object secondParam = params[j];
        if (i == j || fristParam == null || secondParam == null || doList.contains(i + "," + j)
            || doList.contains(j + "," + i)) {// 同个查询对象，不需要组合查询；参数不能为空
          continue;
        }
        String secondObj = HQL_OBJS[j];
        String secondCond = FILTER_COND[j];
        String secondJoin = JOIN_COND[j];
        String hql = String.format(HQL_PATTERN, "count(t.psnId)", fristObj, fristJoin, fristCond, secondObj, secondJoin,
            secondCond, EXT_COND, isClassifyEmpty ? "" : PAC_COND, "");
        Long sub = 0L;
        if (isClassifyEmpty) {// 无大类
          sub = (Long) super.createQuery(hql, fristParam, secondParam, psnId, deptEnHash, deptZhHash)
              .setParameterList("extIssnTxt", issnTxtList).setParameterList("extTauhtHash", tauhtHashList)
              .uniqueResult();
        } else {
          sub = (Long) super.createQuery(hql, fristParam, secondParam, psnId, deptEnHash, deptZhHash)
              .setParameterList("extIssnTxt", issnTxtList).setParameterList("extTauhtHash", tauhtHashList)
              .setParameterList("classify", classifyList).uniqueResult();
        }

        sumMap.put(i + "," + j, sub);
        sumMap.put(i + "," + j + START_FETCH, 0L);
        doList.add(i + "," + j);
        doList.add(j + "," + i);
      }
    }

    return sumMap;
  }

  /**
   * 可能合作者人员id列表(两两关联查询).
   * 
   * @param classifyList
   * @param insGrade
   * @param posGrade
   * @param jnlGrade
   * @param insId
   * @param prjGrade
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<List<Long>> cooperatorMayList(List<String> classifyList, Integer insGrade, Integer posGrade,
      Integer jnlGrade, Long insId, Integer prjGrade, Map<String, Long> sumMap, Long psnId, Long deptEnHash,
      Long deptZhHash, List<String> issnTxtList, List<Long> tauhtHashList) throws DaoException {
    Object[] params = {insGrade, posGrade, jnlGrade, insId, prjGrade};
    // 结果集
    List<List<Long>> mayList = new ArrayList<List<Long>>();
    List<String> doList = new ArrayList<String>();// 记录执行过的语句，防止重复执行
    boolean isClassifyEmpty = CollectionUtils.isEmpty(classifyList);// 大类是否为空
    // 构造人员列表查询语句
    for (int i = 0; i < HQL_OBJS.length; i++) {
      String fristObj = HQL_OBJS[i];
      String fristCond = FILTER_COND[i];
      String fristJoin = JOIN_COND[i];
      Object fristParam = params[i];
      for (int j = 0; j < HQL_OBJS.length; j++) {
        Object secondParam = params[j];
        if (i == j || fristParam == null || secondParam == null || doList.contains(i + "," + j)
            || doList.contains(j + "," + i)) {// 同个查询对象，不需要组合查询；参数不能为空
          continue;
        }
        Long sum = sumMap.get(i + "," + j);// 数据数量
        Long startFetch = sumMap.get(i + "," + j + START_FETCH);// 数据抓取起始位置
        if (sum == null || startFetch == null || startFetch >= sum) {// 无数据跳过
          continue;
        }
        String secondObj = HQL_OBJS[j];
        String secondCond = FILTER_COND[j];
        String secondJoin = JOIN_COND[j];
        String hql = String.format(HQL_PATTERN, "t.psnId", fristObj, fristJoin, fristCond, secondObj, secondJoin,
            secondCond, EXT_COND, isClassifyEmpty ? "" : PAC_COND, "order by t.psnId");
        Query queryResult = null;
        if (isClassifyEmpty) {// 无大类
          queryResult = super.createQuery(hql, fristParam, secondParam, psnId, deptEnHash, deptZhHash)
              .setParameterList("extIssnTxt", issnTxtList).setParameterList("extTauhtHash", tauhtHashList);
        } else {
          queryResult = super.createQuery(hql, fristParam, secondParam, psnId, deptEnHash, deptZhHash)
              .setParameterList("extIssnTxt", issnTxtList).setParameterList("extTauhtHash", tauhtHashList)
              .setParameterList("classify", classifyList);
        }
        queryResult.setFirstResult(startFetch.intValue());
        queryResult.setMaxResults(MAX_RESULT);
        List<Long> result = queryResult.list();
        mayList.add(result);
        // 更新数据抓取起始位置
        sumMap.put(i + "," + j + START_FETCH, startFetch + MAX_RESULT);
        doList.add(i + "," + j);
        doList.add(j + "," + i);
      }
    }
    return mayList;
  }

  /**
   * 统计可能合作者的数量(两两关联查询)，关键词专用.
   * 
   * @param classifyList
   * @param insGrade
   * @param posGrade
   * @param jnlGrade
   * @param insId
   * @param prjGrade
   * @return
   */
  public Map<String, Long> cooperatorMayCount(List<String> classifyList, Set<Long> kwPsnIdList, Integer insGrade,
      Integer posGrade, Integer jnlGrade, Long insId, Integer prjGrade, Long psnId) throws DaoException {
    Object[] params = {insGrade, posGrade, jnlGrade, insId, prjGrade};
    // 符合必要条件的人员数量
    Map<String, Long> sumMap = new HashMap<String, Long>();
    List<String> doList = new ArrayList<String>();// 记录执行过的语句，防止重复执行
    boolean isClassifyEmpty = CollectionUtils.isEmpty(classifyList);// 大类是否为空
    // 构造数量统计语句
    for (int i = 0; i < HQL_OBJS.length; i++) {
      String fristObj = HQL_OBJS[i];
      String fristCond = FILTER_COND[i];
      String fristJoin = JOIN_COND[i];
      Object fristParam = params[i];
      for (int j = 0; j < HQL_OBJS.length; j++) {
        Object secondParam = params[j];
        if (i == j || fristParam == null || secondParam == null || doList.contains(i + "," + j)
            || doList.contains(j + "," + i)) {// 同个查询对象，不需要组合查询；参数不能为空
          continue;
        }
        String secondObj = HQL_OBJS[j];
        String secondCond = FILTER_COND[j];
        String secondJoin = JOIN_COND[j];
        String hql = String.format(HQL_PATTERN, "count(t.psnId)", fristObj, fristJoin, fristCond, secondObj, secondJoin,
            secondCond, KW_COND, isClassifyEmpty ? "" : PAC_COND, "");
        Long sub = 0L;
        if (isClassifyEmpty) {// 无大类
          sub = (Long) super.createQuery(hql, fristParam, secondParam, psnId).setParameterList("psnId", kwPsnIdList)
              .uniqueResult();
        } else {
          sub = (Long) super.createQuery(hql, fristParam, secondParam, psnId).setParameterList("psnId", kwPsnIdList)
              .setParameterList("classify", classifyList).uniqueResult();
        }
        sumMap.put(i + "," + j, sub);
        sumMap.put(i + "," + j + START_FETCH, 0L);
        doList.add(i + "," + j);
        doList.add(j + "," + i);
      }
    }

    return sumMap;
  }

  /**
   * 可能合作者人员id列表(两两关联查询)，关键词专用.
   * 
   * @param classifyList
   * @param insGrade
   * @param posGrade
   * @param jnlGrade
   * @param insId
   * @param prjGrade
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<List<Long>> cooperatorMayList(List<String> classifyList, Set<Long> kwPsnIdList, Integer insGrade,
      Integer posGrade, Integer jnlGrade, Long insId, Integer prjGrade, Map<String, Long> sumMap, Long psnId)
      throws DaoException {
    Object[] params = {insGrade, posGrade, jnlGrade, insId, prjGrade};
    // 结果集
    List<List<Long>> mayList = new ArrayList<List<Long>>();
    List<String> doList = new ArrayList<String>();// 记录执行过的语句，防止重复执行
    boolean isClassifyEmpty = CollectionUtils.isEmpty(classifyList);// 大类是否为空
    // 构造人员列表查询语句
    for (int i = 0; i < HQL_OBJS.length; i++) {
      String fristObj = HQL_OBJS[i];
      String fristCond = FILTER_COND[i];
      String fristJoin = JOIN_COND[i];
      Object fristParam = params[i];
      for (int j = 0; j < HQL_OBJS.length; j++) {
        Object secondParam = params[j];
        if (i == j || fristParam == null || secondParam == null || doList.contains(i + "," + j)
            || doList.contains(j + "," + i)) {// 同个查询对象，不需要组合查询；参数不能为空
          continue;
        }
        Long sum = sumMap.get(i + "," + j);// 数据数量
        Long startFetch = sumMap.get(i + "," + j + START_FETCH);// 数据抓取起始位置
        if (sum == null || startFetch == null || startFetch >= sum) {// 无数据跳过
          continue;
        }
        String secondObj = HQL_OBJS[j];
        String secondCond = FILTER_COND[j];
        String secondJoin = JOIN_COND[j];
        String hql = String.format(HQL_PATTERN, "t.psnId", fristObj, fristJoin, fristCond, secondObj, secondJoin,
            secondCond, KW_COND, isClassifyEmpty ? "" : PAC_COND, "order by t.psnId");
        Query queryResult = null;
        if (isClassifyEmpty) {// 无大类
          queryResult = super.createQuery(hql, fristParam, secondParam, psnId).setParameterList("psnId", kwPsnIdList);
        } else {
          queryResult = super.createQuery(hql, fristParam, secondParam, psnId).setParameterList("psnId", kwPsnIdList)
              .setParameterList("classify", classifyList);
        }
        queryResult.setFirstResult(startFetch.intValue());
        queryResult.setMaxResults(MAX_RESULT);
        List<Long> result = queryResult.list();
        mayList.add(result);
        // 更新数据抓取起始位置
        sumMap.put(i + "," + j + START_FETCH, startFetch + MAX_RESULT);
        doList.add(i + "," + j);
        doList.add(j + "," + i);
      }
    }
    return mayList;
  }

  // 合作者推荐任务队列(人员列表).
  @SuppressWarnings("unchecked")
  public List<CooperatorMayRun> taskRunList() throws DaoException {
    String hql = "from CooperatorMayRun t where t.status in(0,1) order by t.status";
    return super.createQuery(hql).setMaxResults(MAX_RESULT).list();
  }

  // 合作者推荐任务已经挂起的队列(人员列表).
  @SuppressWarnings("unchecked")
  public List<CooperatorMayRun> taskSuspendList() throws DaoException {
    String hql = "from CooperatorMayRun t where t.status=-1";
    return super.createQuery(hql).setMaxResults(MAX_RESULT).list();
  }

  // 合作者推荐，将用户添加到任务表中
  @SuppressWarnings("unchecked")
  public boolean addNewUserToTask() throws DaoException {
    String lastHql = "select min(t.personId) from Person t where t.regData>add_months(sysdate, -1)";
    Long lastPsnId = super.findUnique(lastHql);
    if (lastPsnId != null && lastPsnId > 0) {

      String hql =
          "select t.personId from Person t where not exists(select 1 from PsnPrivate pp where pp.psnId=t.personId) and t.regData>add_months(sysdate, -1) and t.personId>?"
              + " and not exists(select 1 from CooperatorMayRun a where a.psnId=t.personId)"
              + " and exists(select 1 from PsnAreaClassify b where b.psnId=t.personId)";

      List<Long> psnList = super.createQuery(hql, lastPsnId).setMaxResults(10).list();
      if (psnList != null) {
        for (Long psnId : psnList) {
          CooperatorMayRun cmr = new CooperatorMayRun(psnId);
          super.save(cmr);
        }
      }
      return psnList != null && psnList.size() > 0;
    } else {
      return false;
    }
  }

  // 合作者推荐，重置用户任务
  @SuppressWarnings("unchecked")
  public boolean resetUsersToTask() throws DaoException {
    String hql = "from CooperatorMayRun t where t.status=2";

    List<CooperatorMayRun> taskList = super.createQuery(hql).setMaxResults(100).list();
    if (taskList != null) {
      for (CooperatorMayRun cmr : taskList) {
        cmr.setStatus(0);
        super.save(cmr);
      }
    }
    return taskList != null && taskList.size() > 0;
  }
}
