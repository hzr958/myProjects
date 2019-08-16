package com.smate.center.task.dao.bdsp;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPushPrjDataLog;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class BdspPushPrjDataLogDao extends SnsbakHibernateDao<BdspPushPrjDataLog, Long> {
  public void updateData() {
    String hql = "update BdspPushPrjDataLog t set t.pushStatus=0 where exists ("
        + "select 1 from BdspPrjBase t3 where t3.createDate>t.updateDate and t3.prjId=t.prjId" + ")";
    this.createQuery(hql).executeUpdate();
  }

  public void addData() {
    String sql = "insert into BDSP_PUSH_PRJ_DATA_LOG(PRJ_ID,PUSH_STATUS,UPDATE_DATE,CREATE_DATE) "
        + "select t.prj_id,0,sysdate,sysdate from BDSP_PRJ_BASE t where not exists("
        + "select 1 from BDSP_PUSH_PRJ_DATA_LOG m1 where t.prj_id=m1.prj_id" + ")";
    this.getSession().createSQLQuery(sql).executeUpdate();
  }
}
