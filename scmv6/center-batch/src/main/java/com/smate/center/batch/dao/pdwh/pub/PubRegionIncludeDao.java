package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PubRegionInclude;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果匹配，单位国家包含的.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubRegionIncludeDao extends PdwhHibernateDao<PubRegionInclude, Long> {

  /**
   * 单位国家包含的.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubRegionInclude> getInsRegionInclude(Long insId) {

    String hql = "from PubRegionInclude t where t.insId = ? ";
    return super.createQuery(hql, insId).list();
  }
}
