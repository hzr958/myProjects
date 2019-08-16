package com.smate.web.group.dao.group.pub;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.exception.GroupPubException;
import com.smate.web.group.model.group.pub.PubFulltext;

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
  public String queryFulltext(Long pubId) throws GroupPubException {
    Object img =
        super.createQuery("select t.fulltextImagePath from PubFulltext t where t.pubId=?", pubId).uniqueResult();
    if (img != null) {
      return img.toString();
    } else {
      return null;
    }
  }

}
