package com.smate.web.v8pub.dao.sns.psnconfigpub;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.core.base.utils.data.SnsHibernateDao;


@Repository
public class PsnConfigDAO extends SnsHibernateDao<PsnConfig, Long> {

  public PsnConfig getByPsnId(Long psnId) {
    String hql = "from PsnConfig t where t.psnId =:psnId";
    List list = this.createQuery(hql).setParameter("psnId", psnId).list();
    if (list != null && list.size() > 0) {
      return (PsnConfig) list.get(0);
    }
    return null;
  }

}
