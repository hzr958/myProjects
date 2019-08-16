package com.smate.center.task.dyn.dao.base;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.dyn.model.base.DynContentUpdateStatus;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class DynContentUpdateStatusDao extends SnsHibernateDao<DynContentUpdateStatus, Long> {

  /**
   * 批量获取动态信息ids
   * 
   * @param index
   * @param batchSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<DynContentUpdateStatus> BatchGetDynList(Integer batchSize) {

    String hql = "from DynContentUpdateStatus where updateStatus=0  order by dynId desc";
    return super.createQuery(hql).setMaxResults(batchSize).list();

  }

  /**
   * 更新动态内容更新状态
   * 
   * @param dynId
   * @param updateStatus
   */
  public void updateStatus(Long dynId, int updateStatus, String updateMsg) {
    String sql =
        "update DYN_CONTENT_UPDATE_STATUS set update_Status=? ,update_msg=?,update_Date=sysdate where dyn_Id=? ";
    super.update(sql, new Object[] {updateStatus, updateMsg, dynId});


  }

  public Long getNeedHandleCount() {
    String hql = "select count(1) from DynContentUpdateStatus where updateStatus=0  ";
    return (Long) super.createQuery(hql).uniqueResult();
  }
}
