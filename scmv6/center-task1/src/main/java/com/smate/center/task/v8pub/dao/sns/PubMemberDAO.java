package com.smate.center.task.v8pub.dao.sns;

import java.util.List;
import org.springframework.stereotype.Repository;
import com.smate.center.task.v8pub.sns.po.PubMemberPO;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果 成员dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */
@Repository
public class PubMemberDAO extends SnsHibernateDao<PubMemberPO, Long> {
  public void deleteAll(Long pubId) {
    String hql = "delete from PubMemberPO where pubId=:pubId";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public List<PubMemberPO> getAllMember(Long pubId) {
    String hql = " from PubMemberPO where pubId=:pubId order by id asc";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }

  public void updateCommunicate(Long pubId) {
    String hql = "update PubMemberPO t set t.communicable = 0 where t.communicable = 1 "
        + "and exists (select 1 from PubSnsPO p where p.pubId = t.pubId and p.recordFrom = 1 and p.updateMark = 1)"
        + "and t.pubId =:pubId ";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();

  }

  public void updateFirstAuthor(Long pubId) {
    String hql = "update PubMemberPO t set t.firstAuthor = 1 where t.seqNo = 1 and t.pubId =:pubId ";
    this.createQuery(hql).setParameter("pubId", pubId).executeUpdate();
  }
}
