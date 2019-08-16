package com.smate.center.task.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhPubFullTextPO;
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

  public Long findFulltextId(Long pdwhPubId) {
    String hql = "select max(t.id) from PdwhPubFullTextPO t where t.pdwhPubId =:pdwhPubId "
        + "and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId) ";
    return (Long) this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }

  /**
   * 通过成果id获取与之相关的全文文件id
   * 
   * @param pdwhPubId
   * @return
   */
  public List<Long> findFileIdList(Long pdwhPubId) {
    String hql = "select t.fileId from PdwhPubFullTextPO t where t.pdwhPubId =:pdwhPubId "
        + "and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId) ";
    return super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

  public PdwhPubFullTextPO findByPubIdAndFileId(Long pdwhPubId, Long fileId) {
    String hql = "from PdwhPubFullTextPO t where t.pdwhPubId =:pdwhPubId and t.fileId=:fileId "
        + "and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId) ";
    return (PdwhPubFullTextPO) this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("fileId", fileId)
        .uniqueResult();
  }

  public PdwhPubFullTextPO getPdwhPubFulltext(Long pdwhPubId) {
    String hql =
        "from PdwhPubFullTextPO t where t.id in (select max(f.id) from PdwhPubFullTextPO f where f.pdwhPubId = :pdwhPubId ) "
            + "and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId) ";
    return (PdwhPubFullTextPO) createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();

  }

  public Long findMaxFileIdByPubId(Long pdwhPubId) {
    String hql = "select max(t.fileId) from PdwhPubFullTextPO t where t.pdwhPubId =:pdwhPubId "
        + "and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = t.pdwhPubId) ";
    return (Long) this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).uniqueResult();
  }

  public PdwhPubFullTextPO getByPubId(Long pubId) {
    String hql = "from PdwhPubFullTextPO p where p.pdwhPubId =:pubId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) "
        + " order by p.fileId desc";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return (PdwhPubFullTextPO) list.get(0);
    }
    return null;
  }
}
