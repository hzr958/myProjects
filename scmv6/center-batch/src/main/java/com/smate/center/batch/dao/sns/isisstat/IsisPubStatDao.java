package com.smate.center.batch.dao.sns.isisstat;

import org.hibernate.Session;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.isisstat.IsisRptPubStat;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class IsisPubStatDao extends SnsHibernateDao<IsisRptPubStat, Long> {

  /**** do sns proc isis_pub_stat_proc by everyday ****/
  public void doIsisPubStatProc() {
    Session session = this.getSessionFactory().openSession();// 打开Session
    session.createStoredProcedureCall("{CALL isis_pub_stat_proc()}");
    session.close();
  }
}
