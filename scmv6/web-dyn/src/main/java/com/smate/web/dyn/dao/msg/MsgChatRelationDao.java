package com.smate.web.dyn.dao.msg;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.dyn.model.msg.MsgChatRelation;
import com.smate.web.dyn.model.msg.MsgShowInfo;

/**
 * 站内信聊天关系实体dao
 * 
 * @author zzx
 *
 */
@Repository
public class MsgChatRelationDao extends SnsHibernateDao<MsgChatRelation, Long> {
  /**
   * 获取聊天人员列表
   * 
   * @param psnId
   * @return
   */
  public List<MsgChatRelation> getChatPsnList(Long psnId, Page<MsgShowInfo> page, String searchKey) {
    String count = "select count(1) ";
    String hql = "from MsgChatRelation t where ";
    hql += " exists( select 1 from Person t1 where ";
    if (StringUtils.isNotBlank(searchKey)) {
      hql += " instr(upper(nvl(t1.name,t1.firstName||t1.lastName)),upper('" + searchKey + "'))>0 and ";
    }
    hql += " t1.personId=t.senderId) ";
    hql += " and t.status=0 and t.receiverId=:psnId and t.senderId!=:psnId order by t.updateDate desc,t.id";
    page.setTotalCount((Long) this.createQuery(count + hql).setParameter("psnId", psnId).uniqueResult());
    return this.createQuery(hql).setParameter("psnId", psnId).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
  }

  /**
   * 获取所有聊天人员列表
   * 
   * @param psnId
   * @return
   */
  public List<MsgChatRelation> getAllChatPsnList(Long psnId) {
    String hql =
        "from MsgChatRelation t where t.receiverId=:psnId and exists (select 1 from Person  p where p.personId = t.senderId)";
    return this.createQuery(hql).setParameter("psnId", psnId).list();
  }

  public Long getChatPsnCount(Long psnId, String searchKey) {
    String count = "select count(1) ";
    String hql = "from MsgChatRelation t where  ";
    if (StringUtils.isNotBlank(searchKey)) {
      hql += "exists( select 1 from Person t1 where  instr(upper(nvl(t1.name,t1.firstName||t1.lastName)),upper('"
          + searchKey + "'))>0 and t1.personId=t.senderId  ) and ";
    }
    hql += " t.status=0 and t.receiverId=:psnId ";
    return (Long) this.createQuery(count + hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取站内信聊天某个人的第一条聊天记录
   * 
   * @param psnId
   * @return
   */
  public List<MsgChatRelation> getChatPsnLastRecode(Long psnId, Long receiverId) {
    String hql = "from MsgChatRelation t where t.status=0 and t.receiverId=:receiverId  and t.senderId=:psnId    ";
    return this.createQuery(hql).setParameter("psnId", psnId).setParameter("receiverId", receiverId).list();
  }

  /**
   * 获取站内信聊天某个人的第一条聊天记录
   * 
   * @param psnId
   * @return
   */
  public MsgChatRelation getPsnLastRecode(Long psnId, Long receiverId) {
    String hql = "from MsgChatRelation t where t.receiverId=:receiverId  and t.senderId=:psnId    ";
    List<MsgChatRelation> list =
        this.createQuery(hql).setParameter("psnId", psnId).setParameter("receiverId", receiverId).list();
    if (list != null && list.size() > 0) {
      return list.get(0);
    } else {
      return null;
    }
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
