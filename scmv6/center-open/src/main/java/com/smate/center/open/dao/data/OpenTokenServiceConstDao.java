package com.smate.center.open.dao.data;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.smate.center.open.model.OpenTokenServiceConst;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 
 * TOKEN 和 service_type 对应的常量表 DAO
 * 
 * @author AiJiangBin
 *
 */

@Repository
public class OpenTokenServiceConstDao extends SnsHibernateDao<OpenTokenServiceConst, Long> {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  public Long findIdBytokenAndServiceType(String token, String serviceType) {
    String hql =
        "select  t.id  from OpenTokenServiceConst t where   t.status = 0 and  t.token =:token and t.serviceType =:serviceType ";
    Object obj =
        this.createQuery(hql).setParameter("token", token).setParameter("serviceType", serviceType).uniqueResult();
    if (obj != null) {
      return (Long) obj;
    }
    return null;

  }

  public OpenTokenServiceConst findObjBytokenAndServiceType(String token, String serviceType) {
    String hql =
        "from OpenTokenServiceConst t where   t.status = 0 and  t.token =:token and t.serviceType =:serviceType ";
    Object obj =
        this.createQuery(hql).setParameter("token", token).setParameter("serviceType", serviceType).uniqueResult();
    return (OpenTokenServiceConst) obj;

  }

  /**
   * 
   * 通过单位id获取token.
   * 
   * @param token
   * @param insId
   * @return
   */
  public OpenTokenServiceConst findObjBytokenAndInsId(Long insId) {
    String hql = "from OpenTokenServiceConst t where   t.status = 0  and t.insId =:insId ";
    List<OpenTokenServiceConst> list = this.createQuery(hql).setParameter("insId", insId).list();
    if (CollectionUtils.isNotEmpty(list)) {
      return list.get(0);
    }
    return null;

  }


}
