package com.smate.center.task.dao.innocity;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.innocity.InnoCityRequirement;
import com.smate.core.base.utils.data.InnoCityHibernateDao;



/**
 * innocity需求书dao
 * 
 * @author liqinghua
 * 
 */
@Repository
public class InnoCityRequirementDao extends InnoCityHibernateDao<InnoCityRequirement, Long> {

  @SuppressWarnings("unchecked")
  public List<InnoCityRequirement> findRequirementByBatchSize(Long lastId, Integer batchSize) {
    String hql = "from InnoCityRequirement t where t.id>:lastId order by t.id asc";
    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(batchSize).list();
  }
}
