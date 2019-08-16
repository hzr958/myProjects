package com.smate.center.open.dao.interconnection;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.interconnection.UnionRefreshGroupLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 互联互通 群组刷新表
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class UnionRefreshGroupLogDao extends SnsHibernateDao<UnionRefreshGroupLog, Long> {


  /**
   * 查找刷新记录
   * 
   * @param openId
   * @param token
   * @return
   */
  public UnionRefreshGroupLog findUnionRefreshGroupLog(String groupCode) {

    String hql = "from  UnionRefreshGroupLog t where t.groupCode =:groupCode   ";
    List<UnionRefreshGroupLog> list = this.createQuery(hql).setParameter("groupCode", groupCode).list();
    if (list != null && list.size() > 0)
      return list.get(0);
    return null;
  }



}
