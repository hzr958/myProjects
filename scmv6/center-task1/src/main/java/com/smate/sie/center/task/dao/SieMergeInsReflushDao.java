package com.smate.sie.center.task.dao;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieMergeInsReflush;

/**
 * 需要合并的单位Dao
 * 
 * @author 叶星源
 * 
 */
@Repository
public class SieMergeInsReflushDao extends SieHibernateDao<SieMergeInsReflush, Long> {

  @SuppressWarnings("unchecked")
  public List<SieMergeInsReflush> queryNeedRefresh(int maxSize) {
    return super.createQuery("from SieMergeInsReflush t where t.status=0").setMaxResults(maxSize).list();
  }

}
