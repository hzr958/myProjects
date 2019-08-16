package com.smate.center.task.dao.bdsp;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPushPaperDataLog;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

@Repository
public class BdspPushPaperDataLogDao extends SnsbakHibernateDao<BdspPushPaperDataLog, Long> {
  public void updateData() {
    String hql = "update BdspPushPaperDataLog t set t.pushStatus=0 where exists ("
        + "select 1 from BdspPaperBase t3 where t3.createDate>t.updateDate and t3.pubId=t.pubId" + ")";
    this.createQuery(hql).executeUpdate();
  }

  public void addData() {
    String sql = "insert into BDSP_PUSH_PAPER_DATA_LOG(PUB_ID,PUSH_STATUS,UPDATE_DATE,CREATE_DATE) "
        + "select t.pub_id,0,sysdate,sysdate from BDSP_PAPER_BASE t where not exists("
        + "select 1 from BDSP_PUSH_PAPER_DATA_LOG m1 where t.pub_id=m1.pub_id" + ")";
    this.getSession().createSQLQuery(sql).executeUpdate();
  }
}
