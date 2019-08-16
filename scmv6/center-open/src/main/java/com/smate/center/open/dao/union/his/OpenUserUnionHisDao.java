package com.smate.center.open.dao.union.his;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.union.his.OpenUserUnionHis;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 第三方系统与SNS关联表Dao 历史表操作
 * 
 * tsz
 */
@Repository
public class OpenUserUnionHisDao extends SnsHibernateDao<OpenUserUnionHis, Long> {

  /**
   * 获取对应系统 未处理的记录
   * 
   * @param openId
   * @param token
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<OpenUserUnionHis> getAllNeedDealList(String token) {
    String hql = "from OpenUserUnionHis t where  t.token=:token and t.status=0";

    return super.createQuery(hql).setParameter("token", token).setMaxResults(50).list();
  }

  /**
   * 获取删除的关联历史记录
   * 
   * @param openId
   * @param token
   * @return
   */
  public OpenUserUnionHis getByOpenidAndToken(Long openId, String token) {
    String hql = "from OpenUserUnionHis t where   t.openId =:openId and   t.token=:token ";

    return (OpenUserUnionHis) super.createQuery(hql).setParameter("openId", openId).setParameter("token", token)
        .uniqueResult();
  }

}
