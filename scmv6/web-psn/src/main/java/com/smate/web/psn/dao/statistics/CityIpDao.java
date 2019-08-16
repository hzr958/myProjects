package com.smate.web.psn.dao.statistics;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.statistics.CityIp;

/**
 * 
 * @author Scy
 * 
 */
@Repository
public class CityIpDao extends SnsHibernateDao<CityIp, Long> {

  /**
   * 根据Ip段查找IP记录
   * 
   * @param ip
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public String findProvinceByIp(Long ip) {
    String hql = "select t.province from CityIp t where t.startIp <=:ip and t.endIp >=:ip and t.province is not null";
    List<String> provinceList = super.createQuery(hql).setParameter("ip", ip).list();
    System.out.println("------------------------------------------------"
        + CollectionUtils.class.getProtectionDomain().getCodeSource().getLocation());
    if (CollectionUtils.isNotEmpty(provinceList)) {
      return provinceList.get(0);
    }
    return null;
  }
}
