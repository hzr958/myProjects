package com.smate.center.open.dao.nsfc;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.nsfc.NsfcPubOtherInfo;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 成果其他信息DAO.
 * 
 * @author zhanglingling
 *
 */
@Repository
public class NsfcPubOtherInfoDao extends SnsHibernateDao<NsfcPubOtherInfo, Long> {

  public String getNsfcPubSource(Long pubId) {
    String hql = "select t.source from NsfcPubOtherInfo t where t.pubId=?";
    return findUnique(hql, pubId);
  }

}
