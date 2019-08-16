package com.smate.core.base.psn.dao;


import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 人员教育经历Dao
 *
 * @author zk
 */
@Repository
public class EducationHistoryDao extends SnsHibernateDao<EducationHistory, Long> {
  /**
   * @return java.util.List<java.lang.Long>
   * @Author LIJUN
   * @Description //查询该人员所有教育经历的insid,已经去重复
   *
   * @Date 15:35 2018/7/10
   * @Param [psnId]
   **/
  public List<Long> getPsnEduInsId(Long psnId) {

    String hql = "select distinct(insId) from EducationHistory t where t.psnId=:psnId and t.insId is not null";
    return super.createQuery(hql).setParameter("psnId", psnId).list();

  }

  /**
   * 获取某用户的教育经历ID列表.
   *
   * @param psnId
   * @return
   * @throws DaoException
   */
  public List<Long> findEduIdList(Long psnId) {
    String hql = "select distinct eduId from EducationHistory t where t.psnId=?";
    return super.find(hql, psnId);
  }

  /**
   * 判断用户是否存在教育经历.
   *
   * @param psnId
   * @return
   * @throws DaoException
   */
  public boolean isEduHistoryExit(Long psnId) {

    String hql = "select count(psnId) from EducationHistory where psnId=?";
    Long count = findUnique(hql, psnId);
    return count > 0 ? true : false;
  }

  /**
   * 根据人员Id重置历史教育经历首要工作单位.
   *
   * @return
   */
  public int updateEduIsPrimary(Long personId) {
    String hsql = "update EducationHistory set isPrimary=0 where psnId=:personId";
    return createQuery(hsql).setParameter("personId", personId).executeUpdate();
  }

  /**
   * 获取人员教育经历列表
   *
   * @param psnId
   * @return
   */
  public List<EducationHistory> findEduInsByPsnId(Long psnId) {
    return find("from EducationHistory where psnId=?", psnId);
  }

  /**
   * 获取人员教育的机构名，专业，学位，以及受教育的开始时间和结束时间
   *
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<EducationHistory> findEduInsList(Long psnId) {
    String hql =
        "select new EducationHistory(e.eduId,e.psnId,e.insName,e.study,e.degreeName,e.fromYear,e.fromMonth,e.toYear,e.toMonth)from EducationHistory e where e.psnId=? order by e.toYear desc";
    return super.createQuery(hql, psnId).list();
  }

  /**
   * 获取首要教育经历
   */
  public EducationHistory getFirstEdu(Long psnId) {
    String hql = "from EducationHistory t where t.psnId=? and t.isPrimary=1 order by t.isPrimary desc";
    List<EducationHistory> eduList = super.createQuery(hql, new Object[] {psnId}).list();
    if (eduList != null && eduList.size() > 0) {
      return eduList.get(0);
    }
    return null;
  }

  /**
   * 取的当前用户的教育经历.
   *
   * @param personId
   * @return List<EducationHistory>
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<EducationHistory> findByPsnId(Long personId) {
    String hql =
        "from EducationHistory t where t.psnId =:personId order by t.isActive desc,nvl(t.toYear,0) desc,nvl(t.toMonth,0) desc,nvl(t.fromYear,0) desc,nvl(t.fromMonth,0) desc";
    return super.createQuery(hql).setParameter("personId", personId).list();
  }

  /**
   * 查找人员教育经历
   * 
   * @param psnId
   * @param workId
   * @return
   */
  public EducationHistory findPsnEducationHistory(Long psnId, Long eduId) {
    String hql = " from EducationHistory t where t.eduId = :eduId and t.psnId = :psnId";
    return (EducationHistory) super.createQuery(hql).setParameter("eduId", eduId).setParameter("psnId", psnId)
        .uniqueResult();
  }

  /**
   * 人员是否拥有该教育经历
   *
   * @param psnId
   * @param eduId
   * @return
   */
  public EducationHistory isEduHistoryOwner(Long psnId, Long eduId) {
    String hql = " from EducationHistory where psnId=:psnId and eduId = :eduId ";
    return (EducationHistory) super.createQuery(hql).setParameter("psnId", psnId).setParameter("eduId", eduId)
        .uniqueResult();
  }

  /**
   * 删除教育经历
   *
   * @param eduId
   * @return
   */
  public int deleteEduHistory(Long eduId) {
    return createQuery("delete from EducationHistory where eduId=:eduId").setParameter("eduId", eduId).executeUpdate();
  }

  /**
   * 获取最新的教育经历前5个
   */
  public List<EducationHistory> getPsnEduHistory(Long psnId) {
    String hql =
        "select distinct new EducationHistory(t.insId,t.insName,t.toYear,t.toMonth) from EducationHistory t where t.psnId=:psnId and t.insName is not null and exists(select 1 from Institution f where f.id=t.insId) order by t.toYear+t.toMonth desc nulls last";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 查看用户教育经历配置是否有丢失
   *
   * @return
   */
  public boolean checkPsnConfigEduLost(Long psnId, Long cnfId) {
    String hql =
        "select count(1) from EducationHistory t where t.psnId = :psnId and not exists(select 1 from PsnConfigEdu pc where pc.id.cnfId = :cnfId and t.eduId = pc.id.eduId)";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("cnfId", cnfId).uniqueResult();
    if (count != null && count > 0) {
      return true;
    }
    return false;
  }

  @SuppressWarnings("unchecked")
  public List<EducationHistory> findEduByAutoRecommend(List<Long> psnIds) {
    String hql = "from EducationHistory t1 where t1.psnId in(:psnIds) order by t1.psnId asc";
    return createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  @SuppressWarnings("unchecked")
  public Page<EducationHistory> findEducationHistory(Long insId, Long psnId, Page<EducationHistory> page) {
    String hql =
        "select t from EducationHistory t,PersonKnow t2 where t.psnId=t2.personId and t2.isAddFrd=0 and t2.isPrivate=0 and t2.isLogin=1 and t2.enabled=1 and t.psnId not in(select t3.friendPsnId from Friend t3 where t3.psnId=?) and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t.insId=?";
    Object[] objects = new Object[] {psnId, psnId, insId};
    Query q = createQuery(hql, objects);
    if (page.isAutoCount()) {
      long totalCount = countHqlResult(hql, objects);
      page.setTotalCount(totalCount);
    }
    setPageParameter(q, page);
    List<EducationHistory> result = q.list();
    page.setResult(result);
    return page;
  }

  public EducationHistory findAccurate(Long psnId) {
    String hql =
        "from EducationHistory t where exists(select 1 from Institution f where f.id=t.insId) and t.psnId=:psnId order by t.isPrimary desc,t.eduId desc";
    List<EducationHistory> list = super.createQuery(hql).setParameter("psnId", psnId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 查找insId不为空的首要教育经历
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> findPrimaryEdu(Long psnId) {
    String hql = "select insId from EducationHistory where psnId=:psnId and isPrimary=1 and insId is not null";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 按时间排序，取最近的insId不为空的教育经历
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public Long getEduInsIdByLastDate(Long psnId) {
    String hql =
        "select insId from EducationHistory where psnId=:psnId and toYear is not null and insId is not null order by (toYear+toMonth) desc";
    Query query = createQuery(hql).setParameter("psnId", psnId);
    query.setFirstResult(0);
    query.setMaxResults(1);
    List<Long> list = query.list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;
  }


  @SuppressWarnings("unchecked")
  public List<Long> getPsnEduByIdAndYear(Long psnId, Long pubYear) {
    String hql =
        "select distinct(insId) from EducationHistory t where t.psnId=:psnId  and t.insId is not null and ((t.toYear Is Null And nvl(t.fromYear,0) <= :pubYear) Or( t.toYear>=:pubYear And nvl(t.fromYear,0) <= :pubYear))";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubYear", pubYear).list();
  }

}
