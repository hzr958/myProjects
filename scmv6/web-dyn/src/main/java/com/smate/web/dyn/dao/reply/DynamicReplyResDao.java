package com.smate.web.dyn.dao.reply;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.model.reply.DynamicReplyRes;

/**
 * 资源回复Dao.
 * 
 * @author zk
 * 
 */
@Repository
public class DynamicReplyResDao extends SnsHibernateDao<DynamicReplyRes, Long> {
  /**
   * 获得回复id
   * 
   * @param resId 资源id
   * @param resType 资源类型
   * @return
   */
  public Long getReplyId(Long resId, Integer resType) {
    String hql = "select replyId from DynamicReplyRes where resId=:resId and resType=:resType";
    return (Long) super.createQuery(hql).setParameter("resId", resId).setParameter("resType", resType).uniqueResult();
  }

  /**
   * 获取资源DynamicReplyRes
   * 
   * @param resId 资源id
   * @param resType 资源类型
   * @return
   */
  public DynamicReplyRes getDynResByResId(Long resId, Integer resType) {
    String hql = "from DynamicReplyRes where resId=:resId and resType=:resType";
    return (DynamicReplyRes) super.createQuery(hql).setParameter("resId", resId).setParameter("resType", resType)
        .uniqueResult();
  }

}
