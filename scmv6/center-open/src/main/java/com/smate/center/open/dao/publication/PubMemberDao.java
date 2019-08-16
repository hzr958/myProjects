package com.smate.center.open.dao.publication;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.publication.PubMember;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

@Repository
public class PubMemberDao extends HibernateDao<PubMember, Long> {
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
  public List<PubMember> getPubMemberList(Long pubId) {

    String hql = "from PubMember t where t.pubId = ? order by t.seqNo asc";
    return super.createQuery(hql, pubId).list();
  }

}
