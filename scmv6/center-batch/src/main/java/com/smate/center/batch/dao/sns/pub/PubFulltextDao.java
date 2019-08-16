package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.exception.pub.PubDataException;
import com.smate.center.batch.model.sns.pub.PubFulltext;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 成果全文Dao.
 * 
 * @author tsz
 * 
 */
@Repository
public class PubFulltextDao extends SnsHibernateDao<PubFulltext, Long> {
  /**
   * 获取成果全文图片.
   * 
   * @param pubId
   * @return
   * @throws DaoException
   */
  public PubFulltext queryFulltext(Long pubId) throws PubDataException {
    return (PubFulltext) super.createQuery("from PubFulltext t where t.pubId=?", pubId).uniqueResult();
  }

  /**
   * 批量获取全文的pubIds.
   * 
   * @param pubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getFulltextPubIds(List<Long> pubIds) {
    List<Long> ftPubIds = new ArrayList<Long>();
    Collection<Collection<Long>> container = ServiceUtil.splitList(pubIds, 500);
    for (Collection<Long> item : container) {
      ftPubIds.addAll(super.createQuery("select t.pubId from PubFulltext t where t.pubId in(:pubIds)")
          .setParameterList("pubIds", item).list());
    }
    return ftPubIds;
  }

  @SuppressWarnings("unchecked")
  public List<PubFulltext> queryFulltextList(Long pubId, Integer size) {
    List<PubFulltext> rsList = null;
    String hql = "from PubFulltext t where t.pubId > :pubId order by t.pubId asc";
    rsList = super.createQuery(hql).setParameter("pubId", pubId).setMaxResults(size).list();
    return rsList;
  }
}
