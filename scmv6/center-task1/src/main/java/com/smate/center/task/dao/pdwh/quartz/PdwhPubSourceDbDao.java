package com.smate.center.task.dao.pdwh.quartz;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.pub.PdwhPubSourceDb;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果dbid dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhPubSourceDbDao extends PdwhHibernateDao<PdwhPubSourceDb, Long> {

  /**
   * 根据pubId获取PdwhPubSourceDb
   * 
   * @param pubId
   * @return
   */
  public PdwhPubSourceDb getPubSourceDb(Long pubId) {
    String hql = "from PdwhPubSourceDb where pubId = ? ";
    return findUnique(hql, pubId);

  }

}
