package com.smate.core.base.utils.dao.security;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.security.PersonKnowSyn;
import org.springframework.stereotype.Repository;


@Repository
public class PersonKnowSynDao extends SnsHibernateDao<PersonKnowSyn, Long> {

  public void syncPersonKnowByIsLogin(Long psnId) {
    super.createQuery("update PersonKnowSyn set isLogin=1,enabled=1 where personId =:psnId").setParameter("psnId", psnId).executeUpdate();
  }
}
