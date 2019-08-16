package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.PubCategory;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PubCategoryDao extends PdwhHibernateDao<PubCategory, Long> {

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

  public Long findTechfiledIdByPubId(Long pubId) {
    String hql = "select t.scmCategoryId from PubCategory t where t.pubId =:pubId";
    List<Long> list = super.createQuery(hql).setParameter("pubId", pubId).setMaxResults(1).list();
    if (list == null || list.get(0) == null) {
      return null;
    } else {
      return Long.parseLong(list.get(0).toString().substring(0, 1));
    }
  }

  @SuppressWarnings("unchecked")
  public List<String> getScmCategory1stLevelByPubId(Long pubId) {
    String hql = "select distinct(substr(t.scmCategoryId,0,1)) from PubCategory t where t.pubId =:pubId";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }
}
