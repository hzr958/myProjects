package com.smate.center.task.dao.sns.msg;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.msg.MsgContent;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 消息内容实体dao
 * 
 * @author zzx
 *
 */

@Repository
public class MsgContentDao extends SnsHibernateDao<MsgContent, Long> {

  public void insertMsgContent(MsgContent m) {
    if (this.get(m.getContentId()) == null) {
      String sql = "insert into V_MSG_CONTENT " + "values(:CONTENT_ID,:CONTENT)";
      this.getSession().createSQLQuery(sql).setParameter("CONTENT_ID", m.getContentId())
          .setParameter("CONTENT", m.getContent()).executeUpdate();
    }
  }

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
