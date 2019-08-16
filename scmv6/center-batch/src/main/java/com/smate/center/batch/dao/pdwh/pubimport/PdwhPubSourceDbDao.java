package com.smate.center.batch.dao.pdwh.pubimport;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubSourceDb;
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
