package com.smate.web.file.dao.fulltext;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.file.model.fulltext.pdwh.PdwhPubFullTextPO;

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
    String hql = "from PdwhPubFullTextPO t where t.pdwhPubId =:pubId "
        + " and exists (SELECT 1 FROM PubPdwhPO p where p.status = 0 and p.pubId = t.pdwhPubId) "
        + " order by t.fileId desc";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return (PdwhPubFullTextPO) list.get(0);
    }
    return null;
  }

}
