package com.smate.center.task.dao.snsbak;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.snsbak.NewYearGreetingEmail;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class NewYearGreetingEmailDao extends SnsbakHibernateDao<NewYearGreetingEmail, Long> {
  public List<NewYearGreetingEmail> getToHandleList(Integer size) {
    String hql = "from NewYearGreetingEmail t where t.status=0";
    return super.createQuery(hql).setMaxResults(size).list();
  }

}
