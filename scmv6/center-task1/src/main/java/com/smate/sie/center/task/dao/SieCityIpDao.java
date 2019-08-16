package com.smate.sie.center.task.dao;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.core.base.utils.data.SieHibernateDao;
import com.smate.sie.center.task.model.SieCityIp;

/**
 * 
 * @author hd
 * 
 */
@Repository
public class SieCityIpDao extends SieHibernateDao<SieCityIp, Long> {

  /**
   * 根据Ip段查找IP记录
   * 
   * @param ip
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public String findProvinceByIp(Long ip) {
    String hql =
        "select t.province from SieCityIp t where t.startIp <=:ip and t.endIp >=:ip and t.province is not null";
    List<String> provinceList = super.createQuery(hql).setParameter("ip", ip).list();
    if (CollectionUtils.isNotEmpty(provinceList)) {
      return provinceList.get(0);
    }
    return null;
  }

  /**
   * 根据Ip段查找IP记录
   * 
   * @param ip
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public SieCityIp findByIp(Long ip) {
    String hql =
        "from SieCityIp t where t.startIp <=:ip and t.endIp >=:ip and t.province is not null order by t.city asc nulls last";
    List<SieCityIp> provinceList = super.createQuery(hql).setParameter("ip", ip).list();
    if (CollectionUtils.isNotEmpty(provinceList)) {
      return provinceList.get(0);
    }
    return null;
  }
}
