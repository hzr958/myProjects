package com.smate.web.psn.dao.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

@Repository
public class PubMemberDao extends HibernateDao<PubMemberPO, Long> {
  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  public List<PubMemberPO> getPubMemberList(Long pubId) {
    String hql = "from PubMemberPO p where p.pubId =:pubId  order by p.seqNo asc  nulls last ";
    List list = this.createQuery(hql).setParameter("pubId", pubId).list();
    return list;
  }

}
