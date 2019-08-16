package com.smate.web.management.dao.institution.bpo;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.BpoHibernateDao;
import com.smate.core.base.utils.model.bpo.BpoInsPortal;
import com.smate.web.management.model.analysis.DaoException;

/**
 * 单位注册Dao.
 * 
 * zjh
 */
@Repository
public class InsDomainDao extends BpoHibernateDao<BpoInsPortal, Long> {
  public BpoInsPortal findInsPortalByInsId(Long insId) throws DaoException {
    String hql = "from BpoInsPortal t where insId=?";
    return this.findUnique(hql, insId);
  }

  public BpoInsPortal findInsPortalByCheck(String insDomain, Long insId) {
    if (insId == null) {
      String hql = "from BpoInsPortal t where lower(t.domain)=?";
      return this.findUnique(hql, insDomain.toLowerCase());
    } else {
      String hql = "from BpoInsPortal t where lower(t.domain)=? and insId<>?";
      return this.findUnique(hql, insDomain.toLowerCase(), insId);
    }
  }
}
