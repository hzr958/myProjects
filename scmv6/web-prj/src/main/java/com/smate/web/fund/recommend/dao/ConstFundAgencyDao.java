package com.smate.web.fund.recommend.dao;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.web.fund.model.common.ConstFundAgency;

/**
 * 基金资助机构
 * 
 * @author WSN
 *
 *         2017年8月18日 下午2:47:08
 *
 */
@Repository
public class ConstFundAgencyDao extends RcmdHibernateDao<ConstFundAgency, Long> {

  /**
   * 查询资助机构
   * 
   * @param id
   * @return
   */
  public ConstFundAgency findFundAgencyInfo(Long id) {
    String hql =
        "select new ConstFundAgency(nameZh, nameEn, logoUrl, regionId) from ConstFundAgency t where t.id = :id";
    return (ConstFundAgency) super.createQuery(hql).setParameter("id", id).uniqueResult();
  }

  /**
   * 查询资助机构logo
   * 
   * @param id
   * @return
   */
  public String findFundAgencyLogoUrl(Long id) {
    String hql = "select t.logoUrl from ConstFundAgency t where t.id = :id";
    return (String) super.createQuery(hql).setParameter("id", id).uniqueResult();
  }

}
