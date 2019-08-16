package com.smate.web.management.dao.institution.bpo;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.BpoHibernateDao;
import com.smate.web.management.model.institution.bpo.InsSource;

/**
 * 单位数据来源表.
 * 
 * @author zjh
 * 
 */
@Repository
public class InsSourceDao extends BpoHibernateDao<InsSource, Long> {
  /**
   * 获取单位数据来源表.
   * 
   * @param insId
   * @return
   */
  public InsSource getInsSource(Long insId) {
    return super.findUnique("from InsSource t where t.insId = ? ", insId);
  }

}
