package com.smate.center.open.dao.msg;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.msg.MsgContent;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 消息内容实体dao
 * 
 * @author zzx
 *
 */

@Repository
public class MsgContentDao extends SnsHibernateDao<MsgContent, Long> {
  /**
   * 获取序列
   * 
   * @return
   */
  public Long getContentId() {
    String sql = "select SEQ_V_MSG_CONTENT.nextval from dual";
    SQLQuery query = this.getSession().createSQLQuery(sql);
    Object obj = query.uniqueResult();
    if (obj != null) {
      return NumberUtils.toLong(obj.toString());
    }
    return 0L;
  }

}
