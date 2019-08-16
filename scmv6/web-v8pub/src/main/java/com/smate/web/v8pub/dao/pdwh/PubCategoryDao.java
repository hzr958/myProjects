package com.smate.web.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PubCategory;

@Repository
public class PubCategoryDao extends PdwhHibernateDao<PubCategory, Long> {

  @SuppressWarnings("unchecked")
  public List<Long> getScmCategoryByPubId(Long pubId) {
    String hql = "select t.scmCategoryId from PubCategory t where t.pubId =:pubId "
        + "and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pubId) ";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }
}
