package com.smate.web.psn.dao.message;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.message.MsgChatRelation;

/**
 * 站内信聊天关系实体dao
 * 
 * @author zzx
 *
 */

@Repository
public class MsgChatRelationDao extends SnsHibernateDao<MsgChatRelation, Long> {

  public MsgChatRelation getMsgChatRelation(Long senderId, Long receiverId) {
    String hql = "from MsgChatRelation t where t.status=0 and t.receiverId=:receiverId  and t.senderId=:senderId    ";
    List<MsgChatRelation> list =
        this.createQuery(hql).setParameter("senderId", senderId).setParameter("receiverId", receiverId).list();
    if (list != null) {
      return list.get(0);
    }
    return null;
  }

  /**
   * 更新站内信最新会话内容
   */
  public void updateContent(Long senderId, Long receiverId, String content) {
    String contentNewest = content;
    if (StringUtils.isNotBlank(content) && content.length() > 1000) {
      contentNewest = content.substring(0, 300);// CONTENT_NEWEST VARCHAR2(2000)
    }
    String hql =
        "update MsgChatRelation t set t.contentNewest=:content where (t.senderId=:senderId and receiverId=:receiverId) or t.senderId=:receiverId and receiverId=:senderId";
    this.createQuery(hql).setParameter("senderId", senderId).setParameter("receiverId", receiverId)
        .setParameter("content", contentNewest).executeUpdate();
  }
}
