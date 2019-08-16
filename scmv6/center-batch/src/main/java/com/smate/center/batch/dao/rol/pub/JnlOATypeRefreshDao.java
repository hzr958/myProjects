package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.JnlOATypeRefresh;
import com.smate.core.base.utils.data.RolHibernateDao;

/**
 * 期刊-开放存储类型刷新DAO.
 * 
 * @author xys
 * 
 */
@Repository
public class JnlOATypeRefreshDao extends RolHibernateDao<JnlOATypeRefresh, Long> {

  /**
   * 批量获取待刷新记录.
   * 
   * @param maxSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<JnlOATypeRefresh> getJnlOATypeNeedRefreshBatch(int maxSize) {
    String hql = "from JnlOATypeRefresh t where t.status=0";
    return super.createQuery(hql).setMaxResults(maxSize).list();
  }
}
