package com.smate.web.v8pub.dao.sns;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.v8pub.po.sns.MsgRelation;

/**
 * 消息关系实体dao
 * 
 * @author zzx
 *
 */

@Repository
public class MsgRelationDao extends SnsHibernateDao<MsgRelation, Long> {
  /**
   * 获取未处理的消息
   *
   * @author houchuanjie
   * @date 2017年12月13日 下午8:36:26
   * @param id
   * @return
   */
  public MsgRelation getUnprocessed(Long id) {
    String hql = "from MsgRelation where dealStatus=0 and id= :id";
    return (MsgRelation) super.createQuery(hql).setParameter("id", id).uniqueResult();
  }
}
