package com.smate.web.psn.dao.friend;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.model.security.Person;
import com.smate.web.psn.model.friend.FriendTempSys;

/**
 * 系统推荐好友Dao.
 * 
 * zx
 */
@Repository
public class FriendTempSysDao extends SnsHibernateDao<FriendTempSys, Long> {
  public List<Long> findLocalPersonIds(Long psnId, boolean firstPage, Page<Person> page) {
    StringBuffer sql = new StringBuffer();
    sql.append("select psn.personId from Person psn,PrivacySettings ps ");
    sql.append(
        "where psn.personId=ps.pk.psnId and ps.pk.privacyAction='reqAddFrd' and ps.permission=0 and not exists(select 1 from PsnPrivate pp where pp.psnId=psn.personId) and psn.isLogin=1 ");
    sql.append("and psn.personId not in(select pf.friendPsnId from Friend pf where pf.psnId =:psnId) ");
    sql.append("and psn.personId not in(select ps.tempPsnId from FriendTempSys ps where ps.psnId =:psnId) ");
    // 已发送过好友请求的
    sql.append(
        "and psn.personId not in(select pft.tempPsnId from FriendTemp pft where pft.psnId =:psnId and pft.tempPsnId is not null) ");
    sql.append("order by psn.personId asc ");
    @SuppressWarnings("unchecked")
    StringBuffer countHql = new StringBuffer();
    countHql.append("select count(*) from Person psn,PrivacySettings ps ");
    countHql.append(
        "where psn.personId=ps.pk.psnId and ps.pk.privacyAction='reqAddFrd' and ps.permission=0 and not exists(select 1 from PsnPrivate pp where pp.psnId=psn.personId) and psn.isLogin=1 ");
    countHql.append("and psn.personId not in(select pf.friendPsnId from Friend pf where pf.psnId =:psnId) ");
    countHql.append("and psn.personId not in(select ps.tempPsnId from FriendTempSys ps where ps.psnId =:psnId) ");
    // 已发送过好友请求的
    countHql.append(
        "and psn.personId not in(select pft.tempPsnId from FriendTemp pft where pft.psnId =:psnId and pft.tempPsnId is not null) ");
    page.setTotalCount((Long) super.createQuery(countHql.toString()).setParameter("psnId", psnId).uniqueResult());
    List<Long> psnIds = new ArrayList<Long>();
    if (firstPage != false) {
      psnIds = super.createQuery(sql.toString()).setParameter("psnId", psnId).list();

    } else {

      psnIds = super.createQuery(sql.toString()).setParameter("psnId", psnId).list();
    }
    return psnIds;
  }

  /**
   * 从person表里面获取人员,排除好友和添加好友,排除自己 那部分
   * 
   * @param psnId
   * @return
   */
  public List<Long> findPersonIds(Long psnId) {

    /*
     * StringBuffer sql = new StringBuffer(); List<Long> psnIds = new ArrayList<Long>();
     * sql.append("select psn.personId from Person psn,PrivacySettings ps " ); sql.append(
     * "where psn.personId=ps.pk.psnId and ps.pk.privacyAction='reqAddFrd' and ps.permission=0 " +
     * "and not exists(select 1 from PsnPrivate pp where pp.psnId=psn.personId) and psn.isLogin=1 " );
     * sql.append(
     * "and psn.personId not in(select pf.friendPsnId from Friend pf where pf.psnId =:psnId) " );
     * sql.append(
     * "and psn.personId not in(select ps.tempPsnId from FriendTempSys ps where ps.psnId =:psnId) " );
     * // 已发送过好友请求的 sql.append(
     * "and psn.personId not in(select pft.tempPsnId from FriendTemp pft where pft.psnId =:psnId and pft.tempPsnId is not null) "
     * ); // 排除掉自己 sql.append("and psn.personId !=:psnId"); sql.append(" order by psn.personId asc ");
     */
    List<Long> psnIds = new ArrayList<Long>();
    String sql =
        "select psn.personId  from Person psn,PrivacySettings ps where psn.personId=ps.pk.psnId and ps.pk.privacyAction='reqAddFrd' and ps.permission=0 and psn.isLogin=1 "
            + " and   not exists(select 1 from FriendTemp pft where pft.tempPsnId=:psnId and pft.psnId=psn.personId ) and  complete>80 and rownum<20000";
    psnIds = super.createQuery(sql).setParameter("psnId", psnId).list();
    return psnIds;
  }

  /**
   * 根据个人id和推荐好友id获取推荐信息
   * 
   * @param psnId
   * @param tempPsnId
   * @return
   */
  public FriendTempSys getFriendTempSys(Long psnId, Long tempPsnId) {
    String hql = "from FriendTempSys t where t.psnId =:psnId and t.tempPsnId=:tempPsnId";
    return (FriendTempSys) super.createQuery(hql).setParameter("psnId", psnId).setParameter("tempPsnId", tempPsnId)
        .uniqueResult();
  }

  /**
   * 排除排除好友和添加好友,排除自己 那部分
   */
  public List<Long> getNotRecommendPsnIds(Long psnId) {
    String hql =
        "select psn.personId from Person psn where exists(select 1 from PsnPrivate pp where pp.psnId=psn.personId) "
            + "or exists(select 1 from Friend pf where pf.psnId =:psnId and pf.friendPsnId=psn.personId) "
            + "or exists(select 1 from FriendTempSys ps where ps.psnId =:psnId and ps.tempPsnId=psn.personId)"
            + "or exists(select 1 from FriendTemp pft where pft.psnId =:psnId and pft.tempPsnId=psn.personId )";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

}
