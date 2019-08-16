package com.smate.core.base.utils.dao.security;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.CasHibernateDao;
import com.smate.core.base.utils.model.cas.security.AutoLoginOauthInfo;

/**
 * 自动登录 信息表dao
 * 
 * @author tsz
 *
 */
@Repository
public class AutoLoginOauthInfoDao extends CasHibernateDao<AutoLoginOauthInfo, String> {

  /**
   * 传入 id 及时间
   * 
   * @param AID
   * @param time
   * @return
   */
  public AutoLoginOauthInfo getAutoLoginOauthInfoByTime(String AID, Date time) {

    String hql = "From AutoLoginOauthInfo t where t.securityId=:AID and t.overdueTime>=:time";

    Object object = super.createQuery(hql).setParameter("AID", AID).setParameter("time", time).uniqueResult();
    if (object != null) {
      return (AutoLoginOauthInfo) object;
    }
    return null;
  }
}
