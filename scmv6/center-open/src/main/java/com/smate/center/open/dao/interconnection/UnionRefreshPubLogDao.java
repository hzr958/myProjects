package com.smate.center.open.dao.interconnection;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.interconnection.UnionRefreshPubLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 互联互通 成果刷新表
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class UnionRefreshPubLogDao extends SnsHibernateDao<UnionRefreshPubLog, Long> {

  /**
   * 查找刷新记录
   * 
   * @param openId
   * @param token
   * @return
   */
  public List<UnionRefreshPubLog> findUnionRefreshPubLogList(Long openId, String token) {
    String hql = "from  UnionRefreshPubLog t where t.openId =:openId and t.token=:token";
    List<UnionRefreshPubLog> list =
        this.createQuery(hql).setParameter("openId", openId).setParameter("token", token).list();
    return list;
  }

  /**
   * 查找当天的刷新记录
   * 
   * @param openId
   * @param token
   * @return
   */
  public List<UnionRefreshPubLog> findUnionRefreshPubLogListCurrentDay(Long openId, String token, String date) {

    String hql =
        "from  UnionRefreshPubLog t where t.openId =:openId and t.token=:token  and  to_char(t.logDate ,'YYYY-MM-DD')=:date ";
    List<UnionRefreshPubLog> list = this.createQuery(hql).setParameter("openId", openId).setParameter("token", token)
        .setParameter("date", date).list();
    return list;
  }

  /**
   * 删除日志记录
   * 
   * @param openId
   * @param token
   * @return
   */
  public int deleteLog(Long openId, String token) {

    String hql = "delete from  UnionRefreshPubLog t where t.openId =:openId and t.token=:token";
    return this.createQuery(hql).setParameter("openId", openId).setParameter("token", token).executeUpdate();
  }

}
