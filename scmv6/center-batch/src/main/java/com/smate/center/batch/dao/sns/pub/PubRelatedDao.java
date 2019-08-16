package com.smate.center.batch.dao.sns.pub;

import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.PubRelated;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;


@Repository
public class PubRelatedDao extends SnsHibernateDao<PubRelated, Long> {

  public void deletePubRelated(Long pubId) {
    String hql = "delete from PubRelated where pubId=?";
    super.createQuery(hql, pubId).executeUpdate();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Long> findPubAllIdsByPubId(Page page, Long pubId) {
    String countHql = "select count(t.id) ";
    String listHql = "select t.pubAllId ";
    String hql = " from PubRelated t where t.pubId=?";
    String orderHql = " order by t.score desc";
    Long totalCount = super.findUnique(countHql + hql, pubId);
    page.setTotalCount(totalCount);
    Query queryResult = super.createQuery(listHql + hql + orderHql, pubId);
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    return queryResult.list();
  }
}
