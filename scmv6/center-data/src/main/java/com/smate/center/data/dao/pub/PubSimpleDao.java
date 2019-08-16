package com.smate.center.data.dao.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.data.model.pub.PubSimple;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * V_PUB_SIMPLE表实体Dao
 * 
 * @author hzr
 * 
 */
@Repository
public class PubSimpleDao extends SnsHibernateDao<PubSimple, Long> {

  /**
   * 查找成果列表
   * 
   * @param pubIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubSimple> findPubSimpleListByPubIds(List<Long> pubIds) {
    String hql =
        "select new PubSimple(pubId, zhTitle, enTitle, pubType, publishYear) from PubSimple t where t.status!=1 and t.pubId in (:pubIds)";
    return super.createQuery(hql).setParameterList("pubIds", pubIds).list();
  }

}
