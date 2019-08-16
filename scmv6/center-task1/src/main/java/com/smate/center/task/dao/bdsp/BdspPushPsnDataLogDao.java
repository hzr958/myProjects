package com.smate.center.task.dao.bdsp;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPushPsnDataLog;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class BdspPushPsnDataLogDao extends SnsbakHibernateDao<BdspPushPsnDataLog, Long> {

  public void updateData() {
    String hql = "update BdspPushPsnDataLog t set t.pushStatus=0 where exists ("
        + "select 1 from BdspResearchPsnBase t3 where t3.createDate>t.updateDate and t3.psnId=t.psnId" + ")";
    this.createQuery(hql).executeUpdate();
  }

  public void addData() {
    String sql = "insert into BDSP_PUSH_PSN_DATA_LOG(PSN_ID,PUSH_STATUS,UPDATE_DATE,CREATE_DATE) "
        + "select t.psn_id,0,sysdate,sysdate from BDSP_RESEARCH_PSN_BASE t where not exists("
        + "select 1 from BDSP_PUSH_PSN_DATA_LOG m1 where t.psn_id=m1.psn_id" + ")";
    this.getSession().createSQLQuery(sql).executeUpdate();
  }

}
