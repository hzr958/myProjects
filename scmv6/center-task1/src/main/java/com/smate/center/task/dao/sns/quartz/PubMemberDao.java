package com.smate.center.task.dao.sns.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.PubMember;
import com.smate.core.base.utils.data.SnsHibernateDao;

@Repository
public class PubMemberDao extends SnsHibernateDao<PubMember, Long> {

  /**
   * 获取成果作者列表.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubMember> getPubMemberList(Long pubId) {
    String hql = "from PubMember t where t.pubId =:pubId order by t.seqNo asc";
    return super.createQuery(hql).setParameter("pubId", pubId).list();
  }

}
