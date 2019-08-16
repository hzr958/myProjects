package com.smate.center.batch.dao.dynamic;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.dynamic.InspgDynamicRefresh;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.exception.DynTaskException;

/**
 * 动态刷新表Dao
 * 
 * @author hzr
 * 
 * @since 6.0.1
 * @version 6.0.1
 * 
 */

@Repository
public class InspgDynamicRefreshDao extends SnsHibernateDao<InspgDynamicRefresh, Long> {

  /**
   * 获取待处理动态List
   * 
   * @param
   * 
   */
  @SuppressWarnings("unchecked")
  public List<InspgDynamicRefresh> getDynList(Integer size) throws DynTaskException {
    String hql = " from InspgDynamicRefresh t where t.status = 0";
    return super.createQuery(hql).setFirstResult(0).setMaxResults(size).list();
  }

  /**
   * 动态任务执行成功后在表中删除
   * 
   * @param
   * 
   */
  public void successAndDelete(InspgDynamicRefresh inspgDynamicRefresh) throws DynTaskException {
    String hql = "delete from InspgDynamicRefresh t where t.id =?";
    super.createQuery(hql, inspgDynamicRefresh.getId()).executeUpdate();
  }

  /**
   * 动态任务执行失败后在表中记录，设置状态为99
   * 
   * @param inspgDynamicRefresh, errorMsg
   * 
   */
  public void failAndUpdate(InspgDynamicRefresh inspgDynamicRefresh, String errorMsg) throws DynTaskException {
    inspgDynamicRefresh.setErrorMsg(errorMsg);
    inspgDynamicRefresh.setStatus(99);
    super.save(inspgDynamicRefresh);
  }


}
