package com.smate.core.base.app.dao;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.smate.core.base.app.model.AppAuthToken;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class AppAuthTokenDao extends SnsHibernateDao<AppAuthToken, Long> {

  public void updateTokenAndTime(Long psnId, String token, Date date) {
    String hql = "update AppAuthToken  set effectivedate =:effectivedate ,token=:token where psnId=:psnId";
    this.createQuery(hql).setParameter("effectivedate", date).setParameter("token", token).setParameter("psnId", psnId)
        .executeUpdate();

  }

  public AppAuthToken findOne(Long psnId, String token) {
    String hql = "from AppAuthToken t where t.token=:token and t.psnId=:psnId";
    return (AppAuthToken) this.createQuery(hql).setParameter("psnId", psnId).setParameter("token", token)
        .uniqueResult();
  }

}
