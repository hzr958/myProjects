package com.smate.web.psn.dao.message;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.message.MsgRelation;

/**
 * 消息关系实体dao
 * 
 * @author zzx
 *
 */

@Repository
public class MsgRelationDao extends SnsHibernateDao<MsgRelation, Long> {

  /**
   * 删除消息
   * 
   * @param id
   */
  public void delMsg(Long id) {
    String hql = "update MsgRelation t set t.status = 2 where t.id=:id";
    this.createQuery(hql).setParameter("id", id).executeUpdate();
  }

  /**
   * 全部标记为已读消息
   * 
   * @param id
   */
  public void setReadAllMsg(Long psnId) {
    String hql =
        "update MsgRelation t set t.status = 1 where t.type <> 7 and t.type <> 11 and t.status=0 and t.receiverId=:psnId";
    this.createQuery(hql).setParameter("psnId", psnId).executeUpdate();
  }

  /**
   * 全部标记为已读消息
   * 
   * @param id
   */
  public void setReadchatMsg(Long senderId, Long psnId) {
    String hql =
        "update MsgRelation t set t.status = 1 where t.status=0 and t.receiverId=:psnId and t.senderId=:senderId";
    this.createQuery(hql).setParameter("psnId", psnId).setParameter("senderId", senderId).executeUpdate();
  }

  /**
   * 全部标记为已读消息
   * 
   * @param id
   */
  public void setReadMsg(Long id) {
    String hql = "update MsgRelation t set t.status = 1 where t.id=:id";
    this.createQuery(hql).setParameter("id", id).executeUpdate();
  }

  /**
   * 获取与某人的未读消息数量
   * 
   * @param senderId
   * @param psnId
   * @return
   */
  public Long getChatMsgCount(Long senderId, Long psnId) {
    String hql =
        "select count(1) from MsgRelation t where t.status=0 and t.senderId=:senderId and t.receiverId=:psnId ";
    return (Long) this.createQuery(hql).setParameter("senderId", senderId).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 查询添加好友的未读消息
   */
  @SuppressWarnings("unchecked")
  public List<Long> getAddFriendMsg(Long senderId, Long psnId) {
    String hql =
        "select t.id from MsgRelation t where t.status<>2 and t.senderId=:senderId and t.receiverId=:psnId and type=1 and t.dealStatus = 0";
    return super.createQuery(hql).setParameter("senderId", senderId).setParameter("psnId", psnId).list();
  }

  /**
   * 更新重复添加好友消息 ，status置删除状态--消息中心列表不会显示。
   */
  public void updateAddFriendMsg(List<Long> id, Integer dealStatus) {
    String hql = "update MsgRelation t  set t.dealStatus=:dealStatus , t.status = 2  where t.id in(:id)";
    super.createQuery(hql).setParameter("dealStatus", dealStatus).setParameterList("id", id).executeUpdate();
  }

  /**
   * 删除添加好友发送的请求
   */
  public void delAddFriendMsg(Long senderId, Long receiverId) {
    String hql =
        "delete from MsgRelation t where t.status=0 and t.senderId=:senderId and t.receiverId=:receiverId and type=1";
    super.createQuery(hql).setParameter("senderId", senderId).setParameter("receiverId", receiverId).executeUpdate();
  }

  /**
   * 查询本人发送添加好友未被处理的消息
   * 
   * @param psnId
   * @return
   */
  public List<Long> findReceiverPsnIds(Long psnId) {
    String hql =
        "select t.receiverId from MsgRelation t where t.senderId=:psnId and type=1 and dealStatus=0 order by createDate desc";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 查询发送的添加好友请求记录
   * 
   * @param senderId
   * @param receiverId
   * @param type
   * @param dealStatus
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<MsgRelation> findMsgRelations(Long senderId, Long receiverId, Integer type, Integer dealStatus) {
    String hql =
        "from MsgRelation t where t.senderId=:senderId and t.receiverId=:receiverId and type=:type and dealStatus=:dealStatus";
    return super.createQuery(hql).setParameter("senderId", senderId).setParameter("receiverId", receiverId)
        .setParameter("type", type).setParameter("dealStatus", dealStatus).list();
  }

  /**
   * 获取双方所有未删除的聊天信息内容，最新的排在前面
   * 
   * @param senderId
   * @param receiverId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getAllContentId(Long senderId, Long receiverId) {
    String hql = "select t.contentId from MsgRelation t where t.type=7 and t.status <> 2 and "
        + "(( t.senderId=:senderId and t.receiverId=:receiverId) or "
        + "( t.senderId=:receiverId and t.receiverId=:senderId)) order by t.createDate desc, id desc ";
    return this.createQuery(hql).setParameter("senderId", senderId).setParameter("receiverId", receiverId).list();
  }
}
