package com.smate.web.dyn.dao.pdwhpub;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.dyn.model.pub.PdwhPubFullTextPO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 基准库成果全文Dao
 * 
 * @author YJ
 *
 *         2018年5月31日
 */
@Repository
public class PdwhPubFullTextDAO extends PdwhHibernateDao<PdwhPubFullTextPO, Long> {

  public PdwhPubFullTextPO getFullTextByPubId(Long pubId) {
    String hql = "from PdwhPubFullTextPO p where p.pdwhPubId =:pubId order by p.gmtModified desc,p.fileId desc";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    if (list != null && list.size() > 0) {
      return (PdwhPubFullTextPO) list.get(0);
    }
    return null;
  }

}
