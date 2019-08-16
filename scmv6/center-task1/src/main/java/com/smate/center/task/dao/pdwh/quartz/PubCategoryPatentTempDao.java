package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.PubCategoryPatentTemp;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 
 * @author zzx
 *
 */
@Repository
public class PubCategoryPatentTempDao extends PdwhHibernateDao<PubCategoryPatentTemp, Long> {

  public List<PubCategoryPatentTemp> findList(int batchSize) {
    String hql = " from PubCategoryPatentTemp t where t.dealStatus=4";
    return this.createQuery(hql).setMaxResults(batchSize).list();
  }

}
