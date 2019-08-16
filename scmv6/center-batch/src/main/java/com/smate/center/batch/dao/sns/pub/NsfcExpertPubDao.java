package com.smate.center.batch.dao.sns.pub;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.NsfcExpertPub;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 
 * 评议专家成果Dao.
 * 
 * @author oyh
 * 
 */
@Repository
public class NsfcExpertPubDao extends SnsHibernateDao<NsfcExpertPub, Long> {

  public Integer getNsfcExpertPubMaxSeqNo() throws DaoException {

    String hql = "select count(*) from NsfcExpertPub t where t.pubOwnerPsnId=?";
    Long count = (Long) super.createQuery(hql, new Object[] {SecurityUtils.getCurrentUserId()}).uniqueResult();

    return count.intValue();

  }

  @SuppressWarnings("unchecked")
  public List<NsfcExpertPub> getMyExpertPubs(Long psnId) throws DaoException {

    String hql = "From  NsfcExpertPub t where t.pubOwnerPsnId=? order by t.seqNo";
    return super.createQuery(hql, new Object[] {psnId}).list();

  }

  public void deleteExpertPubs(Long psnId, Long pubId) throws DaoException {

    String hql = "delete NsfcExpertPub t where t.pubOwnerPsnId=? and t.pubId=?";

    super.createQuery(hql, new Object[] {psnId, pubId}).executeUpdate();

  }

  @SuppressWarnings("unchecked")
  public List<Long> getExpertPubOrder(Long psnId) throws DaoException {
    String hql = "select t.pubId from NsfcExpertPub t where t.pubOwnerPsnId=? order by t.seqNo";
    return this.createQuery(hql, new Object[] {psnId}).list();
  }

  public void updateExpertPubOrder(Integer seqNo, Long pubId, Long psnId) throws DaoException {

    String hql = "update NsfcExpertPub t set t.seqNo=?  where t.pubId=? and t.pubOwnerPsnId=?";
    super.createQuery(hql, new Object[] {seqNo, pubId, psnId}).executeUpdate();

  }

  public void saveExpertPubs(List<NsfcExpertPub> expertPubs) throws DaoException {
    for (NsfcExpertPub pub : expertPubs) {
      this.save(pub);
    }

  }

  public Set<Long> getExpertPubIds(Long psnId) throws DaoException {
    String hql = "select t.pubId from  NsfcExpertPub t where t.pubOwnerPsnId=?";
    List<Long> queryList = super.createQuery(hql, new Object[] {psnId}).list();
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

  public NsfcExpertPub loadExpertPub(Long psnId, Long pubId) throws DaoException {

    String hql = "From NsfcExpertPub t where t.pubOwnerPsnId=? and t.pubId=?";

    return (NsfcExpertPub) super.createQuery(hql, new Object[] {psnId, pubId}).uniqueResult();

  }

  public List<NsfcExpertPub> findNsfcExpertPubsByPubId(Long pubId) throws DaoException {
    String hql = "from NsfcExpertPub t where t.pubId = ?";
    return super.createQuery(hql, pubId).list();
  }

  public Long queryNsfcExpertPubCount(Long psnId, String title) throws DaoException {
    return (Long) super.findUnique(
        "select count(t.id) from NsfcExpertPub t where t.pubOwnerPsnId=? and lower(t.title)=?", psnId, title);
  }

}
