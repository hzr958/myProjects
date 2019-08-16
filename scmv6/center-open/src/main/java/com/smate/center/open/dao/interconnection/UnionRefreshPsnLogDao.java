package com.smate.center.open.dao.interconnection;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.interconnection.UnionRefreshPsnLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 互联互通 群组 成果刷新表
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class UnionRefreshPsnLogDao extends SnsHibernateDao<UnionRefreshPsnLog, Long> {


  /**
   * 查找刷新记录
   * 
   * @param openId
   * @param token
   * @return
   */
  public UnionRefreshPsnLog findUnionRefreshPsnLog(Long psnId) {

    String hql = "from  UnionRefreshPsnLog t where t.psnId =:psnId ";
    List<UnionRefreshPsnLog> list = this.createQuery(hql).setParameter("psnId", psnId).list();
    if (list != null && list.size() > 0)
      return list.get(0);
    return null;
  }



}
