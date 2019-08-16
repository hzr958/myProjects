package com.smate.web.dyn.dao.fund;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.RcmdHibernateDao;
import com.smate.web.dyn.model.fund.ConstFundAgency;

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
        "select new ConstFundAgency(id, nameZh, nameEn, address, enAddress, logoUrl, regionId) from ConstFundAgency t where t.id = :id";
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

  /**
   * 查找资助机构名称
   * 
   * @param id
   * @return
   */
  public ConstFundAgency findFundAgencyName(Long id) {
    String hql = "select new ConstFundAgency(regionId, nameZh, nameEn) from ConstFundAgency t where t.id = :id";
    return (ConstFundAgency) super.createQuery(hql).setParameter("id", id).uniqueResult();
  }

}
