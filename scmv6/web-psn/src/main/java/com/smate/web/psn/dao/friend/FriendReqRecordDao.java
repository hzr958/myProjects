package com.smate.web.psn.dao.friend;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.friend.FriendReqRecord;

/**
 * 好友请求记录DAO
 *
 * @author wsn
 * @createTime 2017年7月25日 下午4:52:33
 *
 */
@Repository
public class FriendReqRecordDao extends SnsHibernateDao<FriendReqRecord, Long> {

  /**
   * 查找人员发送的且未处理的好友请求接收人IDs
   * 
   * @param psnId
   * @return
   */
  public List<Long> findNotDealReqPsnIds(Long psnId) {
    String hql = "select t.receivePsnId from FriendReqRecord t where t.sendPsnId = :psnId and t.status = 0";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 是否是已取消的好友请求
   * 
   * @param psnId
   * @return
   */
  public boolean findDealReqPsnIds(Long psnId, Long receivePsnId) {
    String hql =
        "select count(t.id) from FriendReqRecord t where t.sendPsnId = :psnId and t.receivePsnId = :receivePsnId and t.status = 5";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("receivePsnId", receivePsnId)
        .uniqueResult();
    if (count != null && count > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 更新未处理的好友请求状态
   * 
   * @param psnId
   * @param receivePsnId
   * @param status
   * @param date
   */
  public void updateStatus(Long psnId, Long receivePsnId, Integer status, Date date) {
    String hql =
        "update FriendReqRecord t set t.status =:status,t.dealTime =:date  where t.sendPsnId = :psnId and t.receivePsnId =:receivePsnId and t.status=0";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("receivePsnId", receivePsnId)
        .setParameter("status", status).setParameter("date", date).executeUpdate();
  }

  /**
   * 根据psnId 和 receivePsnId找到未处理请求
   * 
   * @param psnId
   * @param receivePsnId
   * @return
   */

  public FriendReqRecord findFriendReqRecord(Long sendPsnId, Long receivePsnId) {
    String hql =
        "from FriendReqRecord t where t.sendPsnId = :sendPsnId and t.receivePsnId =:receivePsnId and t.status = 0";
    return (FriendReqRecord) super.createQuery(hql).setParameter("sendPsnId", sendPsnId)
        .setParameter("receivePsnId", receivePsnId).uniqueResult();
  }

  public boolean findNotDealFriendReqRecord(Long sendPsnId, Long receivePsnId) {
    String hql =
        "select count(1) from FriendReqRecord t where t.sendPsnId = :sendPsnId and t.receivePsnId =:receivePsnId and t.status = 0";
    Long count = (Long) super.createQuery(hql).setParameter("sendPsnId", sendPsnId)
        .setParameter("receivePsnId", receivePsnId).uniqueResult();
    if (count != null && count > 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * 判断当前用户是否添加3次或者3次以上
   * 
   * @param receivePsnId
   * @param sendPsnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<BigDecimal> getReceivePsnId(Long sendPsnId) {
    String sql =
        "select receive_psn_id from(select t.receive_psn_id,count(1) as sendCount from  FRIEND_REQ_RECORD t where send_psn_id = ?  group by t.receive_psn_id) where sendCount >=3";
    return super.getSession().createSQLQuery(sql).setParameter(0, sendPsnId).list();

  }

  @SuppressWarnings("unchecked")
  public List<Long> orderByIds(List<Long> receiveId, Long sendPsnId) {
    String sql =
        "select receive_psn_id from(select t.receive_psn_id,count(t.receive_psn_id) from  FRIEND_REQ_RECORD t where t.send_psn_id = :sendPsnId  and t.receive_psn_id in (:receiveId) and t.status != 1 group by t.receive_psn_id order by count(t.receive_psn_id) desc,max(CREATE_DATE) desc,t.receive_psn_id desc) ";
    List<Object> list = super.getSession().createSQLQuery(sql).setParameter("sendPsnId", sendPsnId)
        .setParameterList("receiveId", receiveId).list();
    List<Long> coPsnIds = new ArrayList<Long>();
    for (Object object : list) {
      coPsnIds.add(Long.valueOf(object.toString()));
    }
    return coPsnIds;
  }

  /**
   * 更新好友请求状态
   * 
   * @param sendPsnId
   * @param receivePsnId
   * @param newStatus
   * @param oldStatus
   * @param date
   */
  public void updateRecordStatus(Long sendPsnId, Long receivePsnId, Integer newStatus, Integer oldStatus, Date date) {
    String hql =
        "update FriendReqRecord t set t.status =:newStatus,t.dealTime =:date  where t.sendPsnId = :sendPsnId and t.receivePsnId =:receivePsnId and t.status=:oldStatus";
    super.createQuery(hql).setParameter("sendPsnId", sendPsnId).setParameter("receivePsnId", receivePsnId)
        .setParameter("newStatus", newStatus).setParameter("date", date).setParameter("oldStatus", oldStatus)
        .executeUpdate();
  }

  /**
   * 获取用户某段时间内发送过的好友请求记录
   * 
   * @param psnId
   * @param time
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getFriendReqRecordByTime(Long psnId, Long time) {
    String hql =
        "select t.receivePsnId from FriendReqRecord t where t.sendPsnId=:psnId and (t.status=2 or t.createTime >=sysdate-:time)";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("time", time).list();
  }

}
