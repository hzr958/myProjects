package com.smate.center.batch.dao.rol.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.rol.pub.RolPubXmlPubId;
import com.smate.core.base.utils.data.RolHibernateDao;


/**
 * RolPubXmlPubIdDao
 * 
 * @author zk
 * 
 */
@Repository
public class RolPubXmlPubIdDao extends RolHibernateDao<RolPubXmlPubId, Long> {

  /**
   * 获取未处理数据
   * 
   * @param startPubId
   * @param size
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<RolPubXmlPubId> gets(Long startPubId, Integer size) {
    String hql = "from RolPubXmlPubId r where  r.status=0 and rownum<=? ";
    return super.createQuery(hql, size.longValue()).list();
  }
}
