package com.smate.web.psn.dao.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.psn.model.pdwh.pub.PdwhAddrPsnUpdateRecord;

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
   * 删除人员更新记录
   * 
   * @param psnId
   * @author LIJUN
   * @date 2018年4月4日
   */
  public void deleteRecordByPsnId(Long psnId) {

    String hql = "delete PdwhAddrPsnUpdateRecord where type=2 and taskId=:psnId";
    super.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  /**
   * 查询已经存在的没有处理的记录
   * 
   * @param taskId
   * @param type
   * @return
   * @author LIJUN
   * @date 2018年4月3日
   */
  @SuppressWarnings("unchecked")
  public List<PdwhAddrPsnUpdateRecord> getRecByTaskIdAndType(Long taskId, Integer type) {

    String hql = "from PdwhAddrPsnUpdateRecord where taskId =:taskId and type=:type and status=0";
    return super.createQuery(hql).setParameter("taskId", taskId).setParameter("type", type).list();
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

}
