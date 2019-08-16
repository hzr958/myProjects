package com.smate.center.open.dao.interconnection;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.interconnection.UnionRefreshGroupPubLog;
import com.smate.center.open.model.interconnection.UnionRefreshPubLog;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 互联互通 群组 成果刷新表
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class UnionRefreshGroupPubLogDao extends SnsHibernateDao<UnionRefreshGroupPubLog, Long> {


  /**
   * 查找当天的刷新记录
   * 
   * @param openId
   * @param token
   * @return
   */
  public List<UnionRefreshGroupPubLog> findUnionRefreshPubLogListCurrentDay(String groupCode, String date) {

    String hql =
        "from  UnionRefreshGroupPubLog t where t.groupCode =:groupCode   and  to_char(t.logDate ,'YYYY-MM-DD')=:date ";
    List<UnionRefreshGroupPubLog> list =
        this.createQuery(hql).setParameter("groupCode", groupCode).setParameter("date", date).list();
    return list;
  }

  /**
   * 删除日志
   * 
   * @param openId
   * @param token
   * @return
   */
  public void deleteLogByGroupCode(String groupCode) {
    String hql = " delete  from  UnionRefreshGroupPubLog t where t.groupCode =:groupCode   ";
    this.createQuery(hql).setParameter("groupCode", groupCode).executeUpdate();

  }


}
