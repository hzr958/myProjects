package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PdwhAddrPsnUpdateRecord;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PdwhAddrPsnUpdateRecordDao extends PdwhHibernateDao<PdwhAddrPsnUpdateRecord, Long> {
  /**
   * 获取需要更新的地址常量constIds
   * 
   * @param size
   * @return
   * @author LIJUN
   * @date 2018年3月31日
   */
  @SuppressWarnings("unchecked")
  public List<Long> getNeedUpdateConstIds(Integer size) {

    String hql = "select taskId from PdwhAddrPsnUpdateRecord where status =0 and type=1";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  /**
   * 获取需要更新的人员ids
   * 
   * @param size
   * @return
   * @author LIJUN
   * @date 2018年3月31日
   */
  @SuppressWarnings("unchecked")
  public List<Long> getNeedUpdatePsnIds(Integer size) {
    String hql = "select taskId from PdwhAddrPsnUpdateRecord where status =0 and type=2";
    return super.createQuery(hql).setMaxResults(size).list();
  }

  /**
   * 更新处理状态
   * 
   * @param taskId
   * @param type 地址 1 ，人员2
   * @param status 0 默认，1成功，2失败
   * @author LIJUN
   * @date 2018年3月31日
   */
  public void updateStatus(Long taskId, int type, int status) {
    String hql =
        "update  PdwhAddrPsnUpdateRecord set status=:status ,updateTime=sysdate where taskId =:taskId and type=:type";
    super.createQuery(hql).setParameter("taskId", taskId).setParameter("status", status).setParameter("type", type)
        .executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getTmpNeedUpdatePsnIds(Integer size) {
    String hql = "select taskId from PdwhAddrPsnUpdateRecord where status =0 and type=9";
    return super.createQuery(hql).setMaxResults(size).list();
  }

}
