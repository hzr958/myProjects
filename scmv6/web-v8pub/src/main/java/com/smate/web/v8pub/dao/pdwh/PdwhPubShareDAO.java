package com.smate.web.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PdwhPubSharePO;

/**
 * 成果 分享dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PdwhPubShareDAO extends PdwhHibernateDao<PdwhPubSharePO, Long> {
  @SuppressWarnings("unchecked")
  public List<PdwhPubSharePO> findByPdwhPubId(Long pdwhPubId) {
    String hql = "from PdwhPubSharePO p where p.pdwhPubId=:pdwhPubId"
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) "
        + " order by p.gmtCreate asc";
    List<PdwhPubSharePO> list = this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
    if (list != null && list.size() > 0) {
      return list;
    }
    return null;
  }

  public Long getShareCount(Long pubId) {
    String hql = "select count (*) from PdwhPubSharePO t where t.pdwhPubId=:pubId and t.status=0 "
        + " and exists (SELECT 1 FROM PubPdwhPO p where p.status = 0 and p.pubId = t.pdwhPubId) ";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }
}
