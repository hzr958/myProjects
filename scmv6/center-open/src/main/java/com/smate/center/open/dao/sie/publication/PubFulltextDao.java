package com.smate.center.open.dao.sie.publication;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.publication.PubFulltext;
import com.smate.core.base.utils.data.SnsHibernateDao;

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
  public String queryFulltextUrl(Long pubId) {
    Object obj =
        super.createQuery("select  t.fulltextImagePath  from PubFulltext t where t.pubId=?", pubId).uniqueResult();
    if (obj != null) {
      return obj.toString();
    }
    return "";
  }


}
