package com.smate.center.batch.dao.sns.institution;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.OpenUserCreateByIns;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class OpenUserCreateByInsDao extends SnsHibernateDao<OpenUserCreateByIns, Long> {

  @SuppressWarnings("unchecked")
  public List<OpenUserCreateByIns> getInsInfoByStatus(Integer status) {
    String hql = "from OpenUserCreateByIns t where t.status =:status ";
    return super.createQuery(hql).setParameter("status", status).list();

  }

}
