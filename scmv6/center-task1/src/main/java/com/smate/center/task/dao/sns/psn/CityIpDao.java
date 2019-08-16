package com.smate.center.task.dao.sns.psn;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import com.smate.center.task.exception.DaoException;
import com.smate.center.task.model.sns.psn.CityIp;
import com.smate.core.base.utils.data.SnsHibernateDao;

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
    if (CollectionUtils.isNotEmpty(provinceList)) {
      return provinceList.get(0);
    }
    return null;
  }
}
