package com.smate.center.task.dao.sns.msg;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.msg.MsgRelation;
import com.smate.core.base.utils.constant.MsgConstants;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 消息关系实体dao
 * 
 * @author zzx
 *
 */

@Repository
public class MsgRelationDao extends SnsHibernateDao<MsgRelation, Long> {
  public void insertMsgRelation(MsgRelation m) {
    if (this.get(m.getId()) == null) {
      String sql = "insert into v_msg_relation "
          + "values(:ID,:SENDER_ID,:RECEIVER_ID,:CONTENT_ID,:TYPE,:CREATE_DATE,:STATUS,:DEAL_STATUS,:DEAL_DATE)";
      this.getSession().createSQLQuery(sql).setParameter("ID", m.getId()).setParameter("SENDER_ID", m.getSenderId())
          .setParameter("RECEIVER_ID", m.getReceiverId()).setParameter("CONTENT_ID", m.getContentId())
          .setParameter("TYPE", m.getType())
          .setParameter("CREATE_DATE", m.getCreateDate() != null ? m.getCreateDate() : new Date())
          .setParameter("STATUS", m.getStatus()).setParameter("DEAL_STATUS", m.getDealStatus())
          .setParameter("DEAL_DATE", m.getDealDate() != null ? m.getDealDate() : new Date()).executeUpdate();
    }
  }

  public Long getMsgRelationId() {
    String sql = "select SEQ_V_MSG_RELATION.nextval from dual";
    SQLQuery query = this.getSession().createSQLQuery(sql);
    Object obj = query.uniqueResult();
    if (obj != null) {
      return NumberUtils.toLong(obj.toString());
    }
    return 0L;
  }

  /**
   * 获取未读消息
   * 
   * @param totalCount
   * 
   * @param form
   * @return
   */
  public Long countUnreadMsg(Long personId, Map<String, Object> countMap) {
    // 未读 统计三种类型
    // 站内信
    String hql = "select count(t.id) from MsgRelation t where t.receiverId=:psnId and t.type="
        + MsgConstants.MSG_TYPE_SMATE_INSIDE_LETTER + " and t.status=0";
    // 全文请求
    String hql1 = "select count(t.id) from MsgRelation t join t.sender where t.receiverId=:psnId and t.type="
        + MsgConstants.MSG_TYPE_PUBFULLTEXT_REQUEST + " and t.dealStatus=0";

    // 其他所有消息
    String hql2 = "select count(t.id) from MsgRelation t where t.receiverId=:psnId and "
        + " (t.type=0 or t.type=4 or t.type=5 or t.type=6 or t.type=10 ) " + " and t.status=0";

    Long count = (Long) super.createQuery(hql).setParameter("psnId", personId).uniqueResult();
    Long count1 = (Long) super.createQuery(hql1).setParameter("psnId", personId).uniqueResult();
    Long count2 = (Long) super.createQuery(hql2).setParameter("psnId", personId).uniqueResult();
    countMap.put("insideLetterCount", count);
    countMap.put("pubFulltextReqCount", count1);
    countMap.put("otherCount", count2);
    return count + count1 + count2;
  }

}
