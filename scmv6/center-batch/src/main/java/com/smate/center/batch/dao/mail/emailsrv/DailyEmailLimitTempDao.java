package com.smate.center.batch.dao.mail.emailsrv;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.mail.emailsrv.DailyEmailLimitTemp;
import com.smate.core.base.utils.data.EmailSrvHibernateDao;


/**
 * 每日邮件限制模板Dao
 * 
 * @author zk
 *
 */
@Repository
public class DailyEmailLimitTempDao extends EmailSrvHibernateDao<DailyEmailLimitTemp, String> {

  /**
   * 获取限制模板名
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public ArrayList<String> getDailyEmailLimitTempName() {
    String hql = "select d.tempName from DailyEmailLimitTemp d where d.status=1";
    return (ArrayList<String>) super.createQuery(hql).list();
  }
}
