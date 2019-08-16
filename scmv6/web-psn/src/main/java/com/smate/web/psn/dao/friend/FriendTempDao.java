package com.smate.web.psn.dao.friend;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.model.friend.FriendTemp;

/**
 * 好友分组数据层.
 * 
 * @author lhd
 */
@Repository
public class FriendTempDao extends SnsHibernateDao<FriendTemp, Long> {

  /**
   * 获取邀请人员id
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getTempFriendIds(Long psnId) {
    String hql =
        "select t.tempPsnId from FriendTemp t where t.psnId=:psnId and t.tempPsnId !=:psnId and t.tempPsnId is not null"
            + " and not exists(select 1 from Friend f where f.psnId=:psnId and f.friendPsnId = t.tempPsnId) order by t.createDate desc";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 检查好友请求记录是否存在
   * 
   * @param currentPsnId
   * @param reqPsnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<FriendTemp> checkFriendTempExists(Long currentPsnId, Long reqPsnId) {
    String hql =
        "select new FriendTemp(id, psnId, tempPsnId, sendMail) from FriendTemp t where t.tempPsnId = :currentPsnId and t.psnId = :reqPsnId order by t.id desc";
    return super.createQuery(hql).setParameter("currentPsnId", currentPsnId).setParameter("reqPsnId", reqPsnId).list();
  }

  /**
   * 删除好友请求记录
   * 
   * @param currentPsnId
   * @param reqPsnId
   * @return
   */
  public int deleteFriendTemp(Long currentPsnId, Long reqPsnId) {
    String hql = "delete FriendTemp where psnId=:reqPsnId and tempPsnId=:currentPsnId";
    return createQuery(hql).setParameter("currentPsnId", currentPsnId).setParameter("reqPsnId", reqPsnId)
        .executeUpdate();
  }

  public List<FriendTemp> getTempFriend(Long psnId, Long tempPsnId) {
    String hql = "from FriendTemp t where t.psnId=:psnId and t.tempPsnId=:tempPsnId";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("tempPsnId", tempPsnId).list();
  }

  /**
   * 删除发送的好友请求
   * 
   * @param psnId
   * @param tempPsnId
   */
  /*
   * public void delTempFriendReq(Long psnId, Long tempPsnId) { String hql =
   * "delete from FriendTemp t where t.psnId=:psnId and t.tempPsnId=:tempPsnId" ;
   * super.createQuery(hql).setParameter("psnId", psnId).setParameter("tempPsnId",
   * tempPsnId).executeUpdate(); }
   */

  /**
   * 删除请求记录
   * 
   * @param psnId
   * @param tempPsnId
   */
  public void delTempFriend(Long psnId, Long tempPsnId) {
    String hql = " delete from FriendTemp t where t.psnId=:psnId and t.tempPsnId=:tempPsnId";
    super.createQuery(hql).setParameter("psnId", psnId).setParameter("tempPsnId", tempPsnId).executeUpdate();
  }

  /**
   * 获取请求者id,根据个人id作为tempPsnId字段值查询psnId 20170729_由于旧数据原因导致个别账号的请求列表上显示了好友,故在这里加上排除好友id,待旧数据清完可以去掉
   * 20170731_又由于无处无在的bug,好友请求列表出现了自己的,故再加上排除自己的id
   * 
   * @param psnId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getPsnIdsByTempPsnId(Long psnId) {
    String hql = "select t.psnId from FriendTemp t where t.tempPsnId=:psnId"
        + " and t.psnId !=:psnId and not exists(select 1 from Friend f where f.psnId=:psnId and f.friendPsnId = t.psnId)"
        + " order by t.createDate desc";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  public List<Long> getPsnIdsByTempPsnIdPage(Long psnId, Page page) {
    String hql = "select t.psnId from FriendTemp t where t.tempPsnId=:psnId"
        + " and t.psnId !=:psnId and not exists(select 1 from Friend f where f.psnId=:psnId and f.friendPsnId = t.psnId)"
        + " order by t.createDate desc";
    return super.createQuery(hql).setParameter("psnId", psnId).setFirstResult(page.getFirst() - 1)
        .setMaxResults(page.getPageSize()).list();
  }

  public Long getReqFriendNumber(Long psnId) {
    String hql = "select count(*) from FriendTemp t where t.tempPsnId=:psnId"
        + " and t.psnId !=:psnId and not exists(select 1 from Friend f where f.psnId=:psnId and f.friendPsnId = t.psnId)";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取某个时间段的邀请人员id
   * 
   * @param psnId
   * @param time
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> getTempFriendIdsByTime(Long psnId, Long time) {
    String hql =
        "select t.tempPsnId from FriendTemp t where t.psnId=:psnId and t.tempPsnId !=:psnId and t.tempPsnId is not null and t.createDate >=sysdate-:time";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameter("time", time).list();
  }
}
