package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.PubPdwhSieRelation;

/**
 * 
 * @author ztt
 *
 */
@Repository
public class PubPdwhSieRealtionDao extends SieHibernateDao<PubPdwhSieRelation, Long> {

  /**
   * 获取基准库成果和sie业务成果关联信息
   * 
   * @param pubId
   * @return
   */
  public PubPdwhSieRelation isExitPublicationBySiePubId(Long pubId) {
    String hql = "from PubPdwhSieRelation where siePubId =:pubId ";
    Object obj = super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
    if (null != obj) {
      PubPdwhSieRelation pdwhSieRelation = (PubPdwhSieRelation) obj;
      return pdwhSieRelation;
    }
    return null;
  }

  // 临时用做查重
  public PubPdwhSieRelation isExitPublicationByPdwhId(Long pubId) {
    String hql = "from PubPdwhSieRelation where pdwhPubId =:pubId ";
    Object obj = super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
    if (null != obj) {
      PubPdwhSieRelation pdwhSieRelation = (PubPdwhSieRelation) obj;
      return pdwhSieRelation;
    }
    return null;
  }

  public void delete(Long id) {
    String hql = "delete from PubPdwhSieRelation t where t.pdwhPubId = ? ";
    super.createQuery(hql, id).executeUpdate();
  }

  public Long getPubSize() {
    String hql = "select count(distinct t.pdwhPubId) from PubPdwhSieRelation t";
    return (Long) super.createQuery(hql).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getPubList() {
    String hql = "select distinct t.pdwhPubId from PubPdwhSieRelation t ";
    return super.createQuery(hql).list();
  }

  /**
   * 获取SiePubId集合
   */
  @SuppressWarnings("unchecked")
  public List<Long> getIdByPdwhPubId(Long pdwhPubId) {
    String hql = "select distinct t.siePubId from PubPdwhSieRelation t where t.pdwhPubId=:pdwhPubId";
    return super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

}
