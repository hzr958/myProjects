package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PdwhPubFullTextPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果全文Dao
 * 
 * @author YJ
 *
 *         2018年5月31日
 */
@Repository
public class PdwhPubFullTextDAO extends PdwhHibernateDao<PdwhPubFullTextPO, Long> {

  public PdwhPubFullTextPO getByPubId(Long pubId) {
    String hql = "from PdwhPubFullTextPO p where p.pdwhPubId =:pubId"
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId)"
        + " order by p.fileId desc";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return (PdwhPubFullTextPO) list.get(0);
    }
    return null;
  }

  public PdwhPubFullTextPO getByPubId(Long pubId, Long fileId) {
    String hql = "from PdwhPubFullTextPO p where p.pdwhPubId =:pubId and p.fileId =:fileId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId)";
    List list = this.createQuery(hql).setParameter("pubId", pubId).setParameter("fileId", fileId).list();
    if (list != null && list.size() > 0) {
      return (PdwhPubFullTextPO) list.get(0);
    }
    return null;
  }

  /**
   * 获取基准库成果全文的数量
   * 
   * @param pdwhPubId
   * @return
   */
  public Long getCountByPdwhPubId(Long pdwhPubId) {
    String hql = "select count(1) from PdwhPubFullTextPO t where t.pdwhPubId =:pdwhPubId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId)";
    return (Long) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }

  public PdwhPubFullTextPO getPdwhPubFulltext(Long pdwhPubId) {
    String hql = "from PdwhPubFullTextPO t where t.id in (select max(f.id) from PdwhPubFullTextPO f "
        + "where f.pdwhPubId = :pdwhPubId ) and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId)";
    return (PdwhPubFullTextPO) createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();

  }

}
