package com.smate.core.base.utils.dao.security.role;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.core.base.utils.model.rol.SieMergeInsPortalBak;

/**
 * 单位门户信息备份DAO.
 * 
 */
@Repository
public class SieMergeInsPortalBakDao extends SieHibernateDao<SieMergeInsPortalBak, Long> {

  public void deleteByInsId(Long mergeid) {
    String hql = "delete from SieInsPortal t where t.insId = ? ";
    super.createQuery(hql, mergeid).executeUpdate();
  }
}
