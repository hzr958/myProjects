package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspDataLogRecord;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 任务记录
 * 
 * @author zzx
 *
 */
@Repository
public class BdspDataLogRecordDao extends SnsbakHibernateDao<BdspDataLogRecord, Long> {

  public Long findMaxIdByType(Integer typeId) {
    String hql = "select max(t.id) from BdspDataLogRecord t where t.typeId=:typeId";
    Object result = this.createQuery(hql).setParameter("typeId", typeId).uniqueResult();
    if (result == null) {
      return 0L;
    }
    return (Long) result;
  }

  public List<Long> findListByType(Integer typeId, Integer count) {
    String hql = "select t.dataId from BdspDataLogRecord t where t.status =0 and t.typeId=:typeId";
    return this.createQuery(hql).setParameter("typeId", typeId).setMaxResults(count).list();
  }

  public void updateInfo(int status, String errorMsg, Long dataId, int typeId) {
    String hql = "update BdspDataLogRecord t set t.status=:status,t.errorMsg=:errorMsg,t.updateDate=sysdate "
        + "where t.status =0 and t.typeId=:typeId and t.dataId=:dataId";
    this.createQuery(hql).setParameter("status", status).setParameter("errorMsg", errorMsg)
        .setParameter("typeId", typeId).setParameter("dataId", dataId).executeUpdate();
  }

}
