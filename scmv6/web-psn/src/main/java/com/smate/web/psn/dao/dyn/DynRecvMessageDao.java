package com.smate.web.psn.dao.dyn;



import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.model.dyn.DynRecvMessage;

/**
 * 动态消息添加Dao
 * 
 * @author oyh
 * 
 */

@Repository
public class DynRecvMessageDao extends SnsHibernateDao<DynRecvMessage, Long> {

  public void updatePubDynByAttType(Long senderId, String attType, Integer permission) throws Exception {

    StringBuilder sb = new StringBuilder();
    sb.append(
        "update  DynRecvMessage t set t.permission=? where t.senderId=? and  t.dynType=?  and t.content not like ?");
    super.createQuery(sb.toString(), new Object[] {permission, senderId, attType, "%\"dynContent\"%"}).executeUpdate();

  }

  public void updateDynByAttType(Long senderId, String attType, Integer permission) throws Exception {

    StringBuilder sb = new StringBuilder();
    sb.append("update  DynRecvMessage t set t.permission=? where t.senderId=? and  t.dynType=?");
    super.createQuery(sb.toString(), new Object[] {permission, senderId, attType}).executeUpdate();

  }
}
