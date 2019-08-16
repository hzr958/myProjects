package com.smate.core.base.psn.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 人员工作经历Dao
 * 
 * @author zk
 *
 */
@Repository
public class WorkHistoryDao extends SnsHibernateDao<WorkHistory, Long> {
  /**
   * @Author LIJUN
   * @Description 查询该人员所有的工作单位id，去除重复
   * @Date 15:38 2018/7/10
   * @Param [psnId]
   * @return java.util.List<java.lang.Long>
   **/
  public List<Long> getPsnWorkInsId(Long psnId) {
    String hql = "select distinct(insId) from WorkHistory t where t.psnId=:psnId and t.insId is not null";
    return super.createQuery(hql).setParameter("psnId", psnId).list();

  }

  /**
   * 判断用户是否存在工作经历.
   * 
   * @param psnId
   * @return @throws
   */
  public boolean isWorkHistoryExit(Long psnId) {
    String hql = "select count(psnId) from WorkHistory where psnId=? ";
    Long count = findUnique(hql, psnId);
    return count > 0 ? true : false;
  }

  /**
   * 判断用户的工作经历是否完善
   * 
   * @param psnId
   * @return
   */
  public boolean isWorkHistoryComplete(Long psnId) {
    String hql =
        "select count(psnId) from WorkHistory where psnId=? " + "and insName is not null and fromYear is not null";
    Long count = findUnique(hql, psnId);
    return count > 0 ? true : false;
  }

  /**
   * psnid是否有首要工作经历_zk
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("rawtypes")
  public Boolean hasPrimaryWorkHistory(Long psnId) {
    String hql = "select t.id from WorkHistory t where t.psnId = :psnId and t.isPrimary = 1";
    List list = super.createQuery(hql).setParameter("psnId", psnId).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 获取某用户的工作经历ID列表.
   * 
   * @param psnId
   * @return
   */
  public List<Long> findWorkId(Long psnId) {
    // 2018-03-26 没有person对象， person
    String hql = "select workId from WorkHistory t where t.psnId=?";
    return super.find(hql, psnId);
  }

  /**
   * 根据人员Id重置历史工作经历首要工作单位.
   * 
   * @param personId
   * @return @throws
   */
  public int updateIsPrimary(Long personId) {
    String hql = "update WorkHistory set isPrimary = 0 where psnId=:personId";
    return createQuery(hql).setParameter("personId", personId).executeUpdate();
  }

  /**
   * 查询人员的所有工作经历
   * 
   * @param psnId
   * @return
   */
  public List<WorkHistory> findWorkInsByPsnId(Long psnId) {
    String hql =
        "from WorkHistory where psnId=? order by nvl(isActive,0) desc,nvl(toYear,0) desc,nvl(toMonth,0) desc,nvl(fromYear,0) desc,nvl(fromMonth,0) desc";
    return find(hql, psnId);
  }

  /**
   * 根据psnId获取该人的首要工作单位名称 * @author yph.
   * 
   * @param psnId
   * @return
   */
  public String getPrimaryWorkNameByPsnId(Long psnId) {
    String hql = "from WorkHistory where isPrimary = 1 and psnId=?";
    List<WorkHistory> lst = this.find(hql, psnId);
    if (CollectionUtils.isNotEmpty(lst)) {
      return lst.get(0).getInsName();
    } else {
      hql = "from EducationHistory where isPrimary = 1 and psnId=?";
      List<EducationHistory> l = this.find(hql, psnId);
      if (CollectionUtils.isNotEmpty(l)) {
        return l.get(0).getInsName();
      } else {
        return null;
      }
    }
  }

  /**
   * 获取首要工作单位
   */
  public WorkHistory getFirstWork(Long psnId) {
    String hql = "from WorkHistory t where t.psnId=? and t.isPrimary=1 order by t.isPrimary desc ";
    List<WorkHistory> workList = super.createQuery(hql, new Object[] {psnId}).list();
    if (workList != null && workList.size() > 0) {
      return workList.get(0);
    }
    return null;
  }

  /**
   * 获取首要单位信息[单位名\部门\职称]
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<String, String> getPrimaryInsInfoByPsnId(Long psnId) {
    Map<String, String> resultMap = new HashMap<String, String>();
    String hql =
        "select new WorkHistory(w.insId,w.insName,w.department,w.position) from WorkHistory w,PsnConfigWork pw where pw.id.workId=w.workId and pw.anyUser=7 and w.isPrimary = 1 and w.psnId=:psnId";
    List<WorkHistory> workList = super.createQuery(hql).setParameter("psnId", psnId).list();
    if (CollectionUtils.isNotEmpty(workList)) {
      resultMap.put("INSID", workList.get(0).getInsId() == null ? "" : workList.get(0).getInsId().toString());
      resultMap.put("INSNAME", workList.get(0).getInsName());
      resultMap.put("INSDPT", workList.get(0).getDepartment());
      resultMap.put("INSPOS", workList.get(0).getPosition());
    } else {
      hql =
          "select new EducationHistory(e.insId,e.insName,e.study,e.degreeName) from EducationHistory e,PsnConfigEdu pe where pe.id.eduId=e.eduId and pe.anyUser=7 and e.isPrimary = 1 and e.psnId=:psnId";
      List<EducationHistory> eduList = super.createQuery(hql).setParameter("psnId", psnId).list();
      if (CollectionUtils.isNotEmpty(eduList)) {
        resultMap.put("INSID", eduList.get(0).getInsId() == null ? "" : eduList.get(0).getInsId().toString());
        resultMap.put("INSNAME", eduList.get(0).getInsName());
        resultMap.put("INSDPT", eduList.get(0).getStudy());
        resultMap.put("INSPOS", eduList.get(0).getDegreeName());
      }
    }
    return resultMap;
  }

  public List<Long> findWorkByPrimary(Long psnId) {
    String hql = "select insId from WorkHistory where psnId=? and isPrimary=1 and insId is not null";
    return find(hql, psnId);
  }

  @SuppressWarnings("unchecked")
  public List<Long> findEduByPrimary(Long psnId) {
    String hql = "select insId from EducationHistory where psnId=? and isPrimary=1 and insId is not null";
    return super.createQuery(hql, psnId).list();
  }

  public List<Long> findWorkByPsnId(Long psnId) {
    String hql = "select insId from WorkHistory where psnId=? and isActive=1 and insId is not null";
    return find(hql, psnId);
  }

  @SuppressWarnings("unchecked")
  public Long getWorkInsIdByLastDate(Long psnId) {
    String hql =
        "select insId from WorkHistory where psnId=? and toYear is not null and insId is not null order by (toYear+toMonth) desc";
    Query query = createQuery(hql, psnId);
    query.setFirstResult(0);
    query.setMaxResults(1);
    List<Long> list = query.list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public Long getEduInsIdByLastDate(Long psnId) {
    String hql =
        "select insId from EducationHistory where psnId=? and toYear is not null and insId is not null order by (toYear+toMonth) desc";
    Query query = createQuery(hql, psnId);
    query.setFirstResult(0);
    query.setMaxResults(1);
    List<Long> list = query.list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 
   * 根据用户编码获得用户的工作经历列表.
   * 
   * @param personId
   * @return List<WorkHistory> @
   * 
   */
  public List<WorkHistory> findListByPersonId(Long personId) {
    return find(
        "from WorkHistory where psnId = ? order by nvl(isActive,0) desc,nvl(toYear,0) desc,nvl(toMonth,0) desc,nvl(fromYear,0) desc,nvl(fromMonth,0) desc",
        new Object[] {personId});
  }

  /**
   * 删除人员工作经历
   * 
   * @param id
   * @return
   */
  public int deleteWorkHistory(Long id) {
    return createQuery("delete from WorkHistory where workId=:id").setParameter("id", id).executeUpdate();
  }

  /**
   * 是否是某个工作经历的拥有者
   */
  public boolean isOwnerOfWorkHistory(Long psnId, Long workId) {
    String hql = "select t.workId from WorkHistory t where t.workId = :workId and t.psnId = :psnId";
    Long id = (Long) super.createQuery(hql).setParameter("workId", workId).setParameter("psnId", psnId).uniqueResult();
    if (id == null) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * 是否是首要工作经历
   * 
   * @param psnId
   * @param workId
   * @return
   */
  public Long isPrimaryWorkHistory(Long psnId, Long workId) {
    String hql = "select t.isPrimary from WorkHistory t where t.workId = :workId and t.psnId = :psnId";
    return (Long) super.createQuery(hql).setParameter("workId", workId).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 查找人员工作经历
   * 
   * @param psnId
   * @param workId
   * @return
   */
  public WorkHistory findPsnWorkHistory(Long psnId, Long workId) {
    String hql = " from WorkHistory t where t.workId = :workId and t.psnId = :psnId";
    return (WorkHistory) super.createQuery(hql).setParameter("workId", workId).setParameter("psnId", psnId)
        .uniqueResult();
  }

  /**
   * 查找人员最新工作经历前5个的insId
   */
  public List<WorkHistory> getPsnWorkHistory(Long psnId) {
    String hql =
        " select new WorkHistory(t.insId,t.insName) from WorkHistory t where t.psnId = :psnId and t.insName is not null and  exists(select 1 from Institution f where f.id=t.insId) order by t.toYear+t.toMonth desc nulls last";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 查看用户工作经历配置是否有丢失
   * 
   * @return
   */
  public boolean checkPsnConfigWorkLost(Long psnId, Long cnfId) {
    String hql =
        "select count(1) from WorkHistory t where t.psnId = :psnId and not exists(select 1 from PsnConfigWork pc where pc.id.cnfId = :cnfId and t.workId = pc.id.workId)";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("cnfId", cnfId).uniqueResult();
    if (count != null && count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 获取首要工作经历
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  public WorkHistory getWorkHistory(Long psnId) throws Exception {
    String hql =
        "select new WorkHistory(t.insName , t.department , t.position) from WorkHistory t where t.psnId = :psnId and t.isPrimary = 1";
    List<WorkHistory> list = super.createQuery(hql).setParameter("psnId", psnId).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 互联互通 获取首要 部门
   * 
   * @param psnId
   * @return
   */
  public String findDepartmentByPsnId(Long psnId, Long insId) {
    String hql =
        "select department from WorkHistory t where  t.person.personId =:psnId and   t.insId=:insId  and  t.isPrimary = 1";
    Object department = this.createQuery(hql).setParameter("psnId", psnId).setParameter("insId", insId).uniqueResult();
    if (department != null) {
      return department.toString();
    }
    return "";
  }

  /**
   * 获取最新的工作单位 部门 职称
   * 
   * @param psnId
   * @return
   */
  public WorkHistory findNewestWorkHistoryInsname(Long psnId) {
    String hql =
        "select new WorkHistory(t.insName , t.position , t.department) from WorkHistory t where  t.psnId =:psnId   and t.isActive =1  order by  t.workId desc nulls last  , t.toYear desc,t.toMonth desc nulls last";
    List<WorkHistory> insNameList = this.createQuery(hql).setParameter("psnId", psnId).list();
    if (insNameList != null && insNameList.size() > 0) {
      return insNameList.get(0);
    } else {
      hql =
          "select new WorkHistory(t.insName , t.position , t.department) from WorkHistory t where  t.psnId =:psnId   and t.isActive =0  order by  t.toYear desc nulls last,t.toMonth desc nulls last";
      insNameList = this.createQuery(hql).setParameter("psnId", psnId).list();
      if (insNameList != null && insNameList.size() > 0) {
        return insNameList.get(0);
      }
    }
    return null;
  }

  /**
   * 
   * 智能提示获取工作经历中的单位.
   * 
   * @param personId
   * @return List<WorkHistory> @
   * 
   */
  public List<WorkHistory> findAcInsByPsnId(Long personId, String excludes) {

    if (StringUtils.isNotBlank(excludes) && excludes.matches(ServiceConstants.IDPATTERN)) {
      return find("select new WorkHistory(insId,insName) from WorkHistory where psnId = ? and (insId not in(" + excludes
          + ") or insId = null)", personId);
    } else {
      return find("select new WorkHistory(insId,insName) from WorkHistory where psnId = ? ", personId);
    }

  }

  public List<WorkHistory> findWorkListByCnf(Long cnfId, Integer anyUser) {

    return super.find(
        "select h from WorkHistory h,PsnConfigWork r where h.workId = r.id.workId and r.id.cnfId = ? and r.anyUser>=? order by  nvl(h.isActive,0) desc,nvl(h.toYear,0) desc,nvl(h.toMonth,0) desc,nvl(h.fromYear,0) desc,nvl(h.fromMonth,0) desc ",
        cnfId, anyUser);
  }

  /**
   * 获取给我的评价，工作经历下拉列表.
   * 
   * @param psnId
   * @return
   */
  public List<WorkHistory> getPsnWorkHistoryIns(Long psnId) {

    return find(
        "select new WorkHistory(workId,insId,insName) from WorkHistory where psnId = ? order by nvl(isActive,0) desc,nvl(toYear,0) desc,nvl(toMonth,0) desc,nvl(fromYear,0) desc,nvl(fromMonth,0) desc",
        new Object[] {psnId});
  }

  /**
   * @param workHistory @
   */
  public void saveOrUpdate(WorkHistory workHistory) {
    this.save(workHistory);
  }

  /**
   * @param id
   * @return WorkHistory @
   */
  public WorkHistory findById(Long id) {

    return super.findUniqueBy("id", id);
  }

  @SuppressWarnings("unchecked")
  public Page<WorkHistory> findWork(Long insId, Long psnId, Page<WorkHistory> page) {
    String hql =
        "select t from WorkHistory t,PersonKnow t2 where t.psnId=t2.personId and t2.isAddFrd=0 and t2.isPrivate=0 and t2.isLogin=1 and t2.enabled=1 and t.psnId not in(select t3.friendPsnId from Friend t3 where t3.psnId=?) and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.insId=?";
    Object[] objects = new Object[] {psnId, psnId, insId};
    Query q = createQuery(hql, objects);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql, objects);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<WorkHistory> result = q.list();
    page.setResult(result);
    return page;
  }

  @SuppressWarnings("unchecked")
  public List<WorkHistory> findWorkByAutoRecommend(List<Long> psnIds) {
    String hql = "from WorkHistory where psnId in(:psnIds) order by psnId asc";
    return createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  /**
   * 获取人员工作经历首要工作单位.
   * 
   * @param psnId
   * @return
   */
  public WorkHistory findPsnPrimaryWork(Long psnId) {
    String hql = "from WorkHistory where psnId=? and isPrimary=1";
    List<WorkHistory> list = super.createQuery(hql, psnId).list();
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 获取人员工作经历首要工作单位.
   * 
   * @param psnId
   * @return
   */
  public EducationHistory findPsnPrimaryEdu(Long psnId) {
    String hql = "from EducationHistory where psnId=? and isPrimary=1";
    List<EducationHistory> list = super.createQuery(hql, psnId).list();
    if (list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  public List<Long> findWorkInsId(Long psnId) {
    String hql = "select distinct insId from WorkHistory where psnId=? and insId is not null";
    return super.find(hql, psnId);
  }

  @SuppressWarnings("unchecked")
  public List<Long> findWorkInsIdByOrder(Long psnId) {
    String hql = "select insId from WorkHistory where psnId=? and insId is not null order by nvl(isActive,0) desc";
    return super.createQuery(hql, psnId).setMaxResults(1).list();

  }

  public Long getPsnWorkId(Long psnId, String insName) {
    String hql = "select workId from WorkHistory where psnId=? and insName=?";
    return super.findUnique(hql, psnId, insName);
  }

  /**
   * 是否存在某单位工作经历.
   * 
   * @param psnId
   * @param insId
   * @return
   */
  public boolean isExistInsWork(Long psnId, Long insId) {

    String hql = "select count(id) from WorkHistory where psnId=? and insId = ? ";
    Long count = super.findUnique(hql, psnId, insId);
    if (count == 0) {
      return false;
    }
    return true;
  }

  /**
   * 是否存在首要工作单位.
   * 
   * @param psnId
   * @return
   */
  public boolean isExitPrmIns(Long psnId) {
    String hql = "select count(id) from WorkHistory where psnId=? and (isPrimary is not null and isPrimary = 1) ";
    Long count = super.findUnique(hql, psnId);
    if (count == 0) {
      return false;
    }
    return true;
  }

  /**
   * 获取教育经历ID.
   * 
   * @param psnId
   * @param excIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getAllWorkIds(Long psnId, List<Long> excIds) {

    if (CollectionUtils.isNotEmpty(excIds)) {
      String hql = "select workId from WorkHistory where psnId=:psnId and workId not in(:workIds)";
      return super.createQuery(hql).setParameter("psnId", psnId).setParameterList("workIds", excIds).list();
    } else {
      String hql = "select workId from WorkHistory where psnId= ? ";
      return super.find(hql, psnId);
    }
  }

  /**
   * 获取当前的工作单位.
   * 
   * @param psnId
   * @return @
   */
  public WorkHistory getWorkHistoryByNow(Long psnId) {
    String hql = "from WorkHistory t where t.psnId=? and (t.isActive=? or t.toYear is null)";
    List<WorkHistory> workList = super.createQuery(hql, new Object[] {psnId, 1l}).list();
    if (workList != null && workList.size() > 0) {
      return workList.get(0);
    }

    return null;
  }

  /**
   * 获取当前的工作单位.
   * 
   * @param psnId
   * @return @
   */
  public List<WorkHistory> getWorkHistoryByNow2(Long psnId) {
    String hql = "from WorkHistory t where t.psnId=? and (t.isActive=? or t.toYear is null)";
    return super.createQuery(hql, new Object[] {psnId, 1l}).list();
  }

  /**
   * 获取当前的工作单位.（当前工作单位指的是首要单位，如果没有首要单位则取一个结束时间为当前的单位）
   * 
   * @author liangguokeng
   * @param psnId
   * @return @
   */
  public WorkHistory getCurrentWork(Long psnId) {
    String hql =
        "from WorkHistory t where t.psnId=? order by t.isPrimary desc ,t.isActive desc, t.toYear desc,t.toMonth desc";
    List<WorkHistory> workList = super.createQuery(hql, new Object[] {psnId}).list();
    if (workList != null && workList.size() > 0) {
      return workList.get(0);
    }
    return null;
  }

  private String generateParamStr(String param, List<Long> insIds) {
    StringBuffer sb = new StringBuffer();
    int length = insIds.size();
    for (int i = 0; i < length - 1; i++) {
      sb.append(param + " = " + insIds.get(i) + " or ");
    }
    sb.append(param + " = " + insIds.get(length - 1));
    return sb.toString();
  }

  /**
   * 查找教育经历和工作经历是否跟指定人有相同机构，并返回相同机构insid zk 已废弃
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<Long> haveEqualsIns(Long psnId, List<Long> insIds) {

    List<Long> returnInsIds = new ArrayList<Long>();
    List<Map> insIdList =
        super.queryForList("select distinct ins_id from ( select pw.ins_id from psn_work_history pw where pw.psn_id = "
            + psnId + " and (" + generateParamStr("pw.psn_id", insIds)
            + ") union select pe.ins_id from psn_edu_history pe where pe.psn_id = " + psnId + " and ( "
            + generateParamStr("pe.psn_id", insIds) + " ))");
    for (Map map : insIdList) {
      returnInsIds.add(Long.valueOf(map.get("INS_ID").toString()));
    }
    return returnInsIds;
  }

  /**
   * @author zk 获取指定work_id得到ins_id
   * 
   */
  @SuppressWarnings("unchecked")
  public List<WorkHistory> findInsIdByWordIds(Set<Long> eduIds, Long psnId) {
    String hql = "from WorkHistory where workId in (:eduIds) and psnId = :personId";
    return super.createQuery(hql).setParameterList("eduIds", eduIds).setParameter("personId", psnId).list();
  }

  public boolean saveSyncRolInsUnit(WorkHistory history, Boolean needToSaveToPerson) {
    StringBuilder hql =
        new StringBuilder("select count(t.workId) from WorkHistory t where t.psnId = ? and t.insId = ?");
    StringBuilder hql_ =
        new StringBuilder("select count(t.workId) from WorkHistory t where t.psnId = ? and t.isPrimary = 1");

    List<Object> params = new ArrayList<Object>();
    params.add(history.getPsnId());
    params.add(history.getInsId());
    if (history.getDepartment() != null) {
      hql.append(" and t.department = ?");
      params.add(history.getDepartment());
    }
    // 查询用户是否有该条工作经历
    Long count = (Long) super.createQuery(hql.toString(), params.toArray()).uniqueResult();
    if (count == 0) {
      // 查询用户是否设置了首要工作经历
      Long count_ = (Long) super.createQuery(hql_.toString(), history.getPsnId()).uniqueResult();
      if (count_ == 0) {
        // 设置为首要工作经历
        history.setIsPrimary(1l);
        // 需要同时在person表中更新首要单位
        needToSaveToPerson = true;
      }
      // 工作经历的年份设置为至今
      history.setIsActive(1l);
      super.save(history);
    }
    return needToSaveToPerson;
  }

  public List<WorkHistory> findDepartment(Long psnId, Long insId, String zhName, String enName) {
    StringBuilder hql = new StringBuilder("from WorkHistory t where t.psnId = ? and (t.insId = ? ");
    List<Object> params = new ArrayList<Object>();
    params.add(psnId);
    params.add(insId);
    if (StringUtils.isNotBlank(zhName)) {
      hql.append(" or lower(t.insName) = ?");
      params.add(zhName.toLowerCase());
    }
    if (StringUtils.isNotBlank(enName)) {
      hql.append(" or lower(t.insName) = ?");
      params.add(enName.toLowerCase());
    }
    hql.append(")");
    return super.createQuery(hql.toString(), params.toArray()).list();
  }

  /**
   * 查找相同工作经历的人员
   * 
   * @param insStr
   * @param psnIdList
   * @return
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<Long> findSameWorkHisPsnId(String insStr, List<Long> psnIdList) {

    if (StringUtils.isBlank(insStr)) {
      return null;
    }
    Map<String, List> insMap = rebuildInsQueryStr(insStr);
    if (MapUtils.isEmpty(insMap)) {
      return null;
    }
    String hql = "select t.psnId from WorkHistory t where ";
    if (CollectionUtils.isNotEmpty(insMap.get("idList"))) {
      hql += " t.insId in (:insId)";
    }
    if (CollectionUtils.isNotEmpty(insMap.get("idList")) && CollectionUtils.isNotEmpty(insMap.get("nameList"))) {
      hql += " or t.insName in (:nameList) ";
    } else if (CollectionUtils.isEmpty(insMap.get("idList")) && CollectionUtils.isNotEmpty(insMap.get("nameList"))) {
      hql += "  t.insName in (:nameList) ";
    }
    if ((CollectionUtils.isNotEmpty(insMap.get("idList")) || CollectionUtils.isNotEmpty(insMap.get("nameList")))
        && CollectionUtils.isNotEmpty(psnIdList)) {
      hql += " and  t.psnId in(:psnIdList)";
    }
    Query query = super.createQuery(hql);
    if (CollectionUtils.isNotEmpty(insMap.get("idList"))) {
      query.setParameterList("insId", insMap.get("idList"));
    }
    if (CollectionUtils.isNotEmpty(insMap.get("nameList"))) {
      query.setParameterList("nameList", insMap.get("nameList"));
    }
    if ((CollectionUtils.isNotEmpty(insMap.get("idList")) || CollectionUtils.isNotEmpty(insMap.get("nameList")))
        && CollectionUtils.isNotEmpty(psnIdList)) {
      query.setParameterList("psnIdList", psnIdList);
    }
    return query.setMaxResults(100).list();
  }

  @SuppressWarnings("rawtypes")
  private Map<String, List> rebuildInsQueryStr(String insStr) {

    Map<String, List> returnMap = new HashMap<String, List>();
    List<Long> idList = new ArrayList<Long>();
    List<String> nameList = new ArrayList<String>();
    String[] ins = insStr.split(",");
    for (String s : ins) {
      if (StringUtils.isNumeric(s)) {
        idList.add(Long.parseLong(s));
      } else {
        nameList.add(s);
      }
    }
    returnMap.put("idList", idList);
    returnMap.put("nameList", nameList);
    return returnMap;
  }

  /**
   * 未登陆过,rol向sns同步工作经历信息
   * 
   * @param psnId
   * @param insId
   * @return
   */
  @SuppressWarnings({"unused", "unchecked"})
  public WorkHistory getWorkHistoryByInsId(Long psnId, Long insId) {
    String hql = "from WorkHistory t where t.psnId=? and t.insId=? ";
    List<WorkHistory> workList = super.createQuery(hql, new Object[] {psnId, insId}).list();
    if (workList != null && workList.size() > 0) {
      return workList.get(0);
    }
    return null;
  }

  /**
   * 根据psnId获取用户的工作经历
   * 
   * @param psnId
   * @return
   */
  public WorkHistory getWorkHistoryByPsnId(Long psnId) {
    String hql = "from WorkHistory t where t.psnId=?";
    List<WorkHistory> workList = super.createQuery(hql, new Object[] {psnId}).list();
    if (workList != null && workList.size() > 0) {
      return workList.get(0);
    }
    return null;
  }

  /**
   * 通过insId获取insName查询人员的工作经历
   * 
   * @param psnId
   * @param insId
   * @param insName
   * @return
   */
  @SuppressWarnings("unchecked")
  public WorkHistory getWorkHistoryByInsIdOrInsInfo(Long psnId, Long insId, String insName, String unitName) {
    // 先只用单位名称去查，若查到多个，则再加上部门去查，若还是有多个，则取第一个
    StringBuffer hql = new StringBuffer("from WorkHistory t where t.psnId=:psnId  and lower(t.insName) = :insName");
    List<WorkHistory> workList = super.createQuery(hql.toString()).setParameter("psnId", psnId)
        .setParameter("insName", insName.toLowerCase()).list();
    if (workList != null && workList.size() > 1) {
      hql.append(" and lower(t.department) = :unitName ");
      List<WorkHistory> workList2 = super.createQuery(hql.toString()).setParameter("psnId", psnId)
          .setParameter("insName", insName.toLowerCase()).setParameter("unitName", unitName.toLowerCase()).list();
      if (CollectionUtils.isNotEmpty(workList2)) {
        workList = workList2;
      }
    }
    return CollectionUtils.isNotEmpty(workList) ? workList.get(0) : null;
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPsnWorkByIdAndYear(Long psnId, Long pubYear) {
    String hql =
        "select distinct(insId) from WorkHistory t where t.psnId=:psnId and t.insId is not null  and ((t.toYear Is Null And nvl(t.fromYear,0) <= :pubYear) Or( t.toYear>=:pubYear And nvl(t.fromYear,0) <= :pubYear))";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubYear", pubYear).list();
  }
}
