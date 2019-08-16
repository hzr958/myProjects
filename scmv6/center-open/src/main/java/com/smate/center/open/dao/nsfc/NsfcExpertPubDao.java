package com.smate.center.open.dao.nsfc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.NsfcExpertPub;
import com.smate.core.base.utils.constant.DBSessionEnum;
import com.smate.core.base.utils.data.HibernateDao;

@Repository
public class NsfcExpertPubDao extends HibernateDao<NsfcExpertPub, Long> {

  @Override
  public DBSessionEnum getDbSession() {
    return DBSessionEnum.SNS;
  }

  @SuppressWarnings("unchecked")
  public List<NsfcExpertPub> getMyExpertPubs(Long psnId) throws Exception {

    String hql = "From  NsfcExpertPub t where t.pubOwnerPsnId=? order by t.seqNo";
    return super.createQuery(hql, new Object[] {psnId}).list();

  }

}
