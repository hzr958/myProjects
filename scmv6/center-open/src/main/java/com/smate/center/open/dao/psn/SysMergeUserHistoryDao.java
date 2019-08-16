package com.smate.center.open.dao.psn;



import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.psn.SysMergeUserHis;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 人员合并记录表数据库操作类.
 * 
 * @author mjg
 * 
 */
@Repository
public class SysMergeUserHistoryDao extends SnsHibernateDao<SysMergeUserHis, Long> {

  /**
   * 获取被删除的帐号记录
   * 
   * @param delPsnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public SysMergeUserHis getMergeByDelPsnId(Long delPsnId) throws Exception {

    String hql = "from SysMergeUserHis t where t.delPsnId=? and t.mergeStatus=3";
    List list = super.createQuery(hql, delPsnId).list();
    if (list != null || list.size() > 0) {
      return (SysMergeUserHis) list.get(0);
    }
    return null;
  }

}
