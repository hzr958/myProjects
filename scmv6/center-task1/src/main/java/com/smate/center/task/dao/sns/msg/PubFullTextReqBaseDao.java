package com.smate.center.task.dao.sns.msg;

import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import com.smate.core.base.pub.model.fulltext.req.PubFullTextReqBase;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 全文请求记录dao
 * 
 * @author zzx
 *
 */

@Repository
public class PubFullTextReqBaseDao extends SnsHibernateDao<PubFullTextReqBase, Long> {
  /**
   * 获取序列
   * 
   * @return
   */
  public Long getReqId() {
    String sql = "select SEQ_V_PUB_FULLTEXT_REQ_BASE.nextval from dual";
    SQLQuery query = this.getSession().createSQLQuery(sql);
    Object obj = query.uniqueResult();
    if (obj != null) {
      return NumberUtils.toLong(obj.toString());
    }
    return 0L;
  }

  public void insertPubFullTextReqBase(PubFullTextReqBase pb) {
    String sql = "insert into V_PUB_FULLTEXT_REQ_BASE "
        + "values(:REQ_ID,:PUB_ID,:PUB_DB,:REQ_PSN_ID,:REQ_DATE,:UPDATE_DATE,:UPDATE_PSN_ID,:STATUS)";
    this.getSession().createSQLQuery(sql).setParameter("REQ_ID", pb.getReqId()).setParameter("PUB_ID", pb.getPubId())
        .setParameter("PUB_DB", 1).setParameter("REQ_PSN_ID", pb.getReqPsnId())
        .setParameter("REQ_DATE", pb.getReqDate()).setParameter("UPDATE_DATE", pb.getUpdateDate())
        .setParameter("UPDATE_PSN_ID", pb.getUpdatePsnId()).setParameter("STATUS", 0).executeUpdate();
  }
}
