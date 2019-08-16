package com.smate.center.oauth.dao.profile;

import org.springframework.stereotype.Repository;

import com.smate.center.oauth.model.profile.ScmRegisterVerifCode;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class ScmRegisterVerifCodeDao extends SnsHibernateDao<ScmRegisterVerifCode, Long> {


  public ScmRegisterVerifCode getInfoByAccount(String account) throws Exception {

    String hql = "from ScmRegisterVerifCode where account =:account ";

    return (ScmRegisterVerifCode) super.createQuery(hql).setParameter("account", account).uniqueResult();
  }

  public Integer getStatusByAccount(String account) {
    String hql = "select status from ScmRegisterVerifCode where account =:account ";
    return (Integer) super.createQuery(hql).setParameter("account", account).uniqueResult();
  }


}
