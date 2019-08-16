package com.smate.center.task.dao.sns.msg;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.msg.MsgChatRelation;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 站内信聊天关系实体dao
 * 
 * @author zzx
 *
 */

@Repository
public class MsgChatRelationDao extends SnsHibernateDao<MsgChatRelation, Long> {
  public MsgChatRelation findMsgChatRelation(Long senderId, Long receiverId) {
    String hql = " from  MsgChatRelation  t where  t.senderId=:senderId  and t.receiverId=:receiverId  ";
    Object obj =
        this.createQuery(hql).setParameter("senderId", senderId).setParameter("receiverId", receiverId).uniqueResult();
    if (obj != null) {
      return (MsgChatRelation) obj;
    }
    return null;
  }
}
