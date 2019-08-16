package com.smate.center.task.dao.innocity;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.innocity.JyPatent;
import com.smate.core.base.utils.data.InnoCityHibernateDao;



/**
 * jxonline由申请书推荐专利列表
 * 
 * @author liqinghua
 * 
 */
@Repository
public class JyPatentDao extends InnoCityHibernateDao<JyPatent, Long> {

  @SuppressWarnings("unchecked")
  public List<JyPatent> findPatByBatchSize(Long lastId, Integer batchSize) {
    String hql = "from JyPatent t where t.id>:lastId order by t.id asc";
    return super.createQuery(hql).setParameter("lastId", lastId).setMaxResults(batchSize).list();
  }
}
