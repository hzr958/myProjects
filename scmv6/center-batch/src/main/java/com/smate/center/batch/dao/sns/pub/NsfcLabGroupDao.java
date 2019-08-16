package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.NsfcLabGroup;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * 实验室群组关系Dao.
 * 
 * @author oyh
 * 
 */
@Repository
public class NsfcLabGroupDao extends SnsHibernateDao<NsfcLabGroup, Long> {

  @SuppressWarnings("unchecked")
  public List<NsfcLabGroup> findByLabId(Long labId) throws DaoException {

    String hql = "From  NsfcLabGroup t where  t.labId=?";
    return super.createQuery(hql, new Object[] {labId}).list();

  }

  @SuppressWarnings("unchecked")
  public List<NsfcLabGroup> findByLabPsnId(Long labPsnId) throws DaoException {

    String hql = "From  NsfcLabGroup t where  t.labPsnId=?  and t.status<>2 order by t.year desc";
    return super.createQuery(hql, new Object[] {labPsnId}).list();

  }

  public NsfcLabGroup find(Long id, Long psnId) throws DaoException {
    String hql = "From NsfcLabGroup t where t.id=? and t.labPsnId=?";

    return (NsfcLabGroup) super.createQuery(hql, new Object[] {id, psnId}).uniqueResult();
  }

  public NsfcLabGroup find(Long labId, Integer year) throws DaoException {
    String hql = "From NsfcLabGroup t where  t.labId=? and t.year=?";

    return (NsfcLabGroup) super.createQuery(hql, new Object[] {labId, year}).uniqueResult();
  }

  /**
   * 获取实验室群组关系记录列表.
   * 
   * @param psnId
   * @return
   */
  public List<NsfcLabGroup> getNsfcLabGroupList(Long psnId) {
    String ql = "from NsfcLabGroup where labPsnId = ?";
    return super.createQuery(ql, psnId).list();
  }
}
