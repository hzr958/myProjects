package com.smate.center.merge.dao.thirdparth;

import com.smate.center.merge.model.cas.thirdparty.SysThirdUser;
import com.smate.core.base.utils.data.CasHibernateDao;
import org.springframework.stereotype.Repository;

/**
 * 第三方登录.
 * 
 * @author Scy
 * 
 */
@Repository
public class SysThirdUserDao extends CasHibernateDao<SysThirdUser, Long> {
  /**
   * 根据第三方ID获取psnId.
   * 
   * @return
   */
  public SysThirdUser find(Integer type, Long psnId) {
    String hql = "from SysThirdUser t where t.type = ? and t.psnId = ?";
    return (SysThirdUser) this.createQuery(hql, type, psnId).uniqueResult();
  }

  /**
   * 根据第三方ID获取psnId.
   * 
   * @return
   */
  public Integer delete(Integer type, Long psnId) {
    String hql = "delete  from SysThirdUser t where t.type = ? and t.psnId = ?";
    return this.createQuery(hql, type, psnId).executeUpdate();
  }
}
