package com.smate.center.batch.dao.pdwh.psn;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.psn.PsnPmRefresh;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 用户个人信息标记表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class PsnPmRefreshDao extends PdwhHibernateDao<PsnPmRefresh, Long> {

  /**
   * 获取用户个人信息刷新表的处理任务.
   * 
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PsnPmRefresh> getPsnPmRefreshTaskList(Long startId, int size) {
    String hql = "from PsnPmRefresh t where t.psnId>=?  ";
    return super.createQuery(hql, startId).setMaxResults(size).list();
  }

  /**
   * 删除人员刷新信息.
   * 
   * @param psnId
   */
  public void delPsnPmRefresh(Long psnId) {
    String hql = "delete from PsnPmRefresh t where t.psnId=? ";
    super.createQuery(hql, psnId).executeUpdate();
  }
}
