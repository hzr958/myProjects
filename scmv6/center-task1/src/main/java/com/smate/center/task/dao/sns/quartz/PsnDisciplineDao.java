package com.smate.center.task.dao.sns.quartz;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.quartz.Personal;
import com.smate.core.base.psn.model.profile.PsnDiscipline;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员专长数据层接口 PersonalDao.
 * 
 * @see com.iris.scm.service.orm.Hibernate.HibernateDao
 * @author zt
 */
@Repository
public class PsnDisciplineDao extends SnsHibernateDao<Personal, Long> {

  /**
   * 获取人员的学科专长.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<PsnDiscipline> getPsnDiscipline(Long psnId) throws DaoException {

    String queryString =
        "select new PsnDiscipline(pd.id,pd.psnId, pd.disId,t) from ConstDiscipline t,PsnDiscipline pd where t.id = pd.disId and pd.psnId = ? order by pd.disId";
    return super.createQuery(queryString, psnId).list();
  }

  /**
   * 删除某人的学科专长.
   * 
   * @param psnId
   * @throws DaoException
   */
  public void removePsnDiscipline(Long psnId, Long discId) throws DaoException {
    super.createQuery("delete from PsnDiscipline t where t.psnId = ? and t.disId = ?", psnId, discId).executeUpdate();
  }

  /**
   * 保存人员学科专长.
   * 
   * @param psnId
   * @param disId
   * @throws DaoException
   */
  public PsnDiscipline savePsnDiscipline(Long psnId, Long disId) throws DaoException {
    Long count =
        super.findUnique("select count(t.psnId) from PsnDiscipline t where t.psnId = ? and t.disId = ?", psnId, disId);
    PsnDiscipline pd = new PsnDiscipline(psnId, disId);
    if (count == 0) {
      super.getSession().save(pd);
    }
    return pd;
  }

  public void savePsnDiscipline(PsnDiscipline pd) throws DaoException {
    super.getSession().save(pd);
  }

  /**
   * 判断用户的课题专长是否存在.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public boolean isPsnDiscExit(Long psnId) throws DaoException {

    Long count = super.findUnique("select count(t.id.psnId) from PsnDiscipline t where t.id.psnId = ? ", psnId);
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 判断用户是否已选课题专长.
   * 
   * @param disId
   * @param psnId
   * @return
   * @throws DaoException
   */
  public boolean isChooseDis(Long disId, Long psnId) throws DaoException {
    String hql = "from PsnDiscipline t where t.disId=? and t.psnId=?";
    if (super.find(hql, new Object[] {disId, psnId}).size() > 0)
      return true;
    else
      return false;
  }

  /**
   * 删除个人学科.
   * 
   * @param disId
   * @param psnId
   * @throws DaoException
   */
  public void deleteDiscipline(Long disId, Long psnId) throws DaoException {
    String hql = "delete from PsnDiscipline t where t.disId=? and t.psnId=?";
    super.createQuery(hql, new Object[] {disId, psnId}).executeUpdate();
  }

  /**
   * 获取人员熟悉的学科领域ID列表.
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnDiscId(Long psnId) {

    String hql = "select disId from  PsnDiscipline where psnId = ? ";
    return super.createQuery(hql, psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findPsnDisciplineByAutoRecommend(Long startPsnId, Long endPsnId) {
    String hql =
        "select distinct t1.psnId from PsnDiscipline t1 where t1.psnId>? and t1.psnId<=? order by t1.psnId asc";
    return createQuery(hql, startPsnId, endPsnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findPsnDisciplineByAutoRecommend(List<Long> psnIds) {
    String hql = "select distinct t1.psnId from PsnDiscipline t1 where t1.psnId in(:psnIds) order by t1.psnId asc";
    return createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  /**
   * 获取指定人员所有学科.
   * 
   * @param psnId
   * @return
   */
  public List<PsnDiscipline> findPsnDiscipline(Long psnId) {
    return find("from PsnDiscipline where psnId=?", psnId);
  }

  /**
   * 获取指定人员，指定学科id
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> isFindPsnDisciplineId(Long psnId, List<Long> discIds) {

    List<Long> discIdList = new ArrayList<Long>();
    List<PsnDiscipline> psnDiscList = super.createQuery("from PsnDiscipline where psnId=:psnId and disId in (:discIds)")
        .setParameter("psnId", psnId).setParameterList("discIds", discIds).list();
    if (psnDiscList == null || psnDiscList.size() == 0)
      return discIdList;
    for (PsnDiscipline disc : psnDiscList)
      discIdList.add(disc.getDisId());
    return discIdList;
  }

  /**
   * 获取人员学科专长id
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<Long> findDisciplineByPsnId(Long psnId) {

    List<Long> discIds = new ArrayList<Long>();
    String sql = "select t.dis_id from psn_discipline t where t.psn_id = ? ";
    List<Map> disList = super.queryForList(sql, new Object[] {psnId});
    if (disList == null || disList.size() == 0)
      return discIds;
    for (Map m : disList) {
      discIds.add(Long.valueOf(m.get("DIS_ID").toString()));
    }
    return discIds;
  }

  /**
   * 获取人员在指定学科中的相同学科数.
   * 
   * @param psnId
   * @param discId
   * @return
   * @throws DaoException
   */
  public Long queryPsnCommonDiscCount(Long psnId, List<Long> discIdList) throws DaoException {
    return (Long) super.createQuery(
        "select count(d.id) from PsnDiscipline d where d.disId in(:disIds) and d.psnId=:psnId")
            .setParameterList("disIds", discIdList).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 更新学科领域人员(人员合并用 )
   * 
   * @author zk
   */
  public void updateDiscPsnId(Long fromPsnId, Long toPsnId, Long discId) throws DaoException {
    super.createQuery("update PsnDiscipline set psnId=? where psnId=? and disId = ?", toPsnId, fromPsnId, discId)
        .executeUpdate();
  }
}
