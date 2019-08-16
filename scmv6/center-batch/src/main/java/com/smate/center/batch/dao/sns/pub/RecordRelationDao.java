package com.smate.center.batch.dao.sns.pub;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.pub.RecordRelation;
import com.smate.core.base.utils.data.SnsHibernateDao;


@Repository
public class RecordRelationDao extends SnsHibernateDao<RecordRelation, Long> {

  public RecordRelationDao() {
    super();
    // TODO Auto-generated constructor stub
  }

  /**
   * 保存更新关系表
   * 
   * @param rel
   * 
   */
  public void saveOrUpdate(RecordRelation rel) {
    this.save(rel);
  }

  /**
   * 跟怒参数寻找实例
   * 
   * @param relId
   * @param type
   * @return
   */

  @SuppressWarnings("unchecked")
  public RecordRelation findRecord(Long relId, Integer type, Long recvPsnId) {
    String queryString =
        "from RecordRelation t where t.relId = ? and t.type = ?  and   exists ( select 1 from  InviteInbox iv where iv.id=t.inviteId and iv.psnId=?)";
    Query q = this.createQuery(queryString, relId, type, recvPsnId);
    return (RecordRelation) q.uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public RecordRelation findRecord(Long relId, Integer type) {
    String queryString = "from RecordRelation t where t.relId = ? and t.type = ?";
    Query q = this.createQuery(queryString, relId, type);
    return (RecordRelation) q.uniqueResult();
  }

  public void deleteRecord(Long inviteId) {
    String queryString = "delete from RecordRelation t where t.inviteId = ?";
    Query q = this.createQuery(queryString, inviteId);
    q.executeUpdate();
  }
}
