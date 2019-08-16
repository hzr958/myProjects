package com.smate.web.psn.dao.third.user;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.CasHibernateDao;
import com.smate.web.psn.model.third.user.SysThirdUser;

/**
 * 第三方登录
 * 
 * @author Scy
 * 
 */
@Repository
public class SysThirdUserDao extends CasHibernateDao<SysThirdUser, Long> {

  /**
   * 根据第三方ID获取psnId
   * 
   * @param type
   * @param thirdId
   * @return
   */
  public SysThirdUser find(Integer type, Long psnId) {
    String hql = "from SysThirdUser t where t.type = ? and t.psnId = ?";
    return (SysThirdUser) this.createQuery(hql, type, psnId).uniqueResult();
  }

  /**
   * 根据第三方ID获取psnId
   * 
   * @param type
   * @param thirdId
   * @return
   */
  public Integer delete(Integer type, Long psnId) {
    String hql = "delete  from SysThirdUser t where t.type = ? and t.psnId = ?";
    return this.createQuery(hql, type, psnId).executeUpdate();
  }

}
