package com.smate.core.base.utils.dao.msg;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.msg.MobileWhitelist;
import org.springframework.stereotype.Repository;

@Repository
public class MobileWhitelistDao extends SnsHibernateDao<MobileWhitelist, Long> {

  public MobileWhitelist  getByMobile(String mobile){
    String hql = "from MobileWhitelist t where t.mobile =:mobile and t.status = 0";
    Object obj = this.createQuery(hql).setParameter("mobile", mobile).uniqueResult();
    if(obj !=null){
      return (MobileWhitelist)obj;
    }
    return  null ;
  }
}
