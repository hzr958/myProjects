package com.smate.center.batch.dao.nsfc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.sns.nsfc.NsfcPubMember;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

@Repository
public class NsfcPubMemberDao extends HibernateDao<NsfcPubMember, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  /**
   * 获取成果作者列表.
   * 
   * @param pubId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<NsfcPubMember> getNsfcPubMemberList(Long pubId) {

    String hql = "from NsfcPubMember t where t.pubId = ? order by t.seqNo asc";
    return super.createQuery(hql, pubId).list();
  }

}
