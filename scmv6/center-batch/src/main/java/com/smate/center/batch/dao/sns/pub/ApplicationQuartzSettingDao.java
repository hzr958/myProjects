package com.smate.center.batch.dao.sns.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.ApplicationQuartzSetting;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class ApplicationQuartzSettingDao extends SnsHibernateDao<ApplicationQuartzSetting, Long> {

  public Integer getAppValue(String taskName) {
    String hql = "select t.value from ApplicationQuartzSetting t where t.taskName =:taskName";
    Integer rs = (Integer) super.createQuery(hql).setParameter("taskName", taskName).uniqueResult();
    if (rs == null) {
      rs = 0;
    }
    return rs;
  }

  public void closeApplication(String taskName) {
    String hql = "update ApplicationQuartzSetting t set t.value = 0 where t.taskName =:taskName";
    super.createQuery(hql).setParameter("taskName", taskName).executeUpdate();
  }
}
