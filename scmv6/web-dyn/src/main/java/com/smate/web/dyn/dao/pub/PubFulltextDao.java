package com.smate.web.dyn.dao.pub;

import java.util.Objects;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.pub.PubFulltext;

/**
 * 成果全文Dao.
 * 
 * @author ajb
 * 
 */
@Repository
public class PubFulltextDao extends SnsHibernateDao<PubFulltext, Long> {

  /**
   * 只获取，全文图片
   * 
   * @param pubId
   * @return
   */
  public String getPubFulltextImageByPubId(Long pubId) {
    String hql = "select  p.fulltextImagePath from  PubFulltext p where p.pubId =:pubId ";
    Object obj = super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
    return Objects.toString(obj, null);
  }
}
