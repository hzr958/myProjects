package com.smate.center.batch.dao.sns.pub;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.NsfcLabPub;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 
 * 实验室成果Dao.
 * 
 * @author oyh
 * 
 */
@Repository
public class NsfcLabPubDao extends SnsHibernateDao<NsfcLabPub, Long> {

  public Integer getNsfcLabPubMaxSeqNo(Long pId) throws DaoException {

    String hql = "select count(*) from NsfcLabPub t where t.labPsnId=? and t.refId=? ";
    Long count = (Long) super.createQuery(hql, new Object[] {SecurityUtils.getCurrentUserId(), pId}).uniqueResult();

    return count.intValue();

  }

  @SuppressWarnings("unchecked")
  public List<NsfcLabPub> getMyLabPubs(Long psnId, Long pId) throws DaoException {

    String hql =
        "From  NsfcLabPub t where t.labPsnId=? and t.refId=? order by t.labPubType,t.pubYear desc,t.pubMonth desc,t.pubDay desc";
    return super.createQuery(hql, new Object[] {psnId, pId}).list();

  }

  public void deleteLabPubs(Long psnId, Long pubId, Long pId) throws DaoException {

    String hql = "delete NsfcLabPub t where t.labPsnId=? and t.pubId=? and t.refId=?";

    super.createQuery(hql, new Object[] {psnId, pubId, pId}).executeUpdate();

  }

  @SuppressWarnings("unchecked")
  public List<Long> getLabPubOrder(Long psnId, Long pId) throws DaoException {
    String hql = "select t.pubId from NsfcLabPub t where t.labPsnId=? and t.refId=? order by t.seqNo";
    return this.createQuery(hql, new Object[] {psnId, pId}).list();
  }

  public void updateLabPubOrder(Integer seqNo, Long pubId, Long pId) throws DaoException {

    String hql = "update NsfcLabPub t set t.seqNo=?  where t.pubId=? and t.refId=?";
    super.createQuery(hql, new Object[] {seqNo, pubId, pId}).executeUpdate();

  }

  public void saveLabPubs(List<NsfcLabPub> labPubs) throws DaoException {
    for (NsfcLabPub labPub : labPubs) {
      this.save(labPub);
    }

  }

  public Set<Long> getLabPubIds(Long psnId, Long pId) throws DaoException {
    String hql = "select t.pubId from  NsfcLabPub t where t.labPsnId=? and t.refId=?";
    List<Long> queryList = super.createQuery(hql, new Object[] {psnId, pId}).list();
    Set<Long> retSet = new HashSet<Long>();
    if (CollectionUtils.isNotEmpty(queryList)) {
      for (int i = 0; i < queryList.size(); i++) {
        Object obj = (Object) queryList.get(i);
        if (obj != null) {
          Long pubId = Long.valueOf(ObjectUtils.toString(obj));
          retSet.add(pubId);
        }
      }
    }

    return retSet;

  }

  public NsfcLabPub loadLabPub(Long psnId, Long pubId, Long pId) throws DaoException {

    String hql = "From NsfcLabPub t where t.labPsnId=? and t.pubId=? and t.refId=?";

    return (NsfcLabPub) super.createQuery(hql, new Object[] {psnId, pubId, pId}).uniqueResult();

  }

  public Long getRepPubTotal(Long labPsnId, Long pId) throws DaoException {

    String hql = "select count(t.id) From NsfcLabPub t where t.labPsnId=?  and t.refId=? and t.labPubType  not in(?)";

    return (Long) super.createQuery(hql, new Object[] {labPsnId, pId, 1}).uniqueResult();

  }

  @SuppressWarnings("unchecked")
  public List<NsfcLabPub> queryNsfcLabPubByPubId(Long pubId) throws DaoException {
    String hql = "from  NsfcLabPub t where t.pubId=?";
    return super.createQuery(hql, pubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<NsfcLabPub> queryNsfcLabPubByPsnId(Long psnId) throws DaoException {
    String hql = "from  NsfcLabPub t where t.labPsnId=?";
    return super.createQuery(hql, psnId).list();
  }

  public void deleteNsfcLabPubById(Long id) throws DaoException {
    super.createQuery("delete from NsfcLabPub t where t.id=?", id).executeUpdate();
  }

  public void updateNsfcLabPubById(Long id, Long psnId) throws DaoException {
    super.createQuery("update NsfcLabPub t set t.pubOwnerPsnId=? where t.id=?", psnId, id).executeUpdate();
  }

}
