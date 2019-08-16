package com.smate.center.task.dao.snsbak;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.PubCategorySnsbak;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
// public class PubCategoryDao extends PdwhHibernateDao<PubCategory, Long>
public class PubCategorySnsbakDao extends SnsbakHibernateDao<PubCategorySnsbak, Long> {

  public Long findTechfiledIdByPubId(Long pubId) {
    String hql = "select t.scmCategoryId from PubCategorySnsbak t where t.pubId =:pubId";
    List<Long> list = super.createQuery(hql).setParameter("pubId", pubId).setMaxResults(1).list();
    if (list == null || list.size() == 0) {
      return null;
    } else {
      return Long.parseLong(list.get(0).toString().substring(0, 1));
    }
  }

  public void deleteByPubId(Long pubId) {
    String hql = "delete PubCategory t where t.pubId =:pubId";
    super.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

  public Long getCountsByPubId(Long pubId) {
    String hql = "select count(1) from PubCategory t where t.pubId =:pubId";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getScmCategoryByPubId(Long pubId) {
    String hql = "select t.scmCategoryId from PubCategory t where t.pubId =:pubId";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<String> getScmCategory1stLevelByPubId(Long pubId) {
    String hql = "select distinct(substr(t.scmCategoryId,0,1)) from PubCategorySnsbak t where t.pubId =:pubId";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

}
