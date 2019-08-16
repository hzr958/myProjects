package com.smate.center.batch.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PdwhInsAddrConst;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库标准单位地址信息常量dao
 * 
 * @author LIJUN
 * @date 2018年3月15日
 */
@Repository
public class PdwhInsAddrConstDao extends PdwhHibernateDao<PdwhInsAddrConst, Long> {

  /**
   * 根据namehash获取常量信息
   * 
   * @param addrHash
   * @return
   * @author LIJUN
   * @date 2018年3月19日
   */
  @SuppressWarnings("unchecked")
  public List<PdwhInsAddrConst> getInsInfoByNameHash(Long addrHash) {
    String hql = " from PdwhInsAddrConst where insNameHash=:addrHash and enable=1 order by updateTime desc";
    return super.createQuery(hql).setParameter("addrHash", addrHash).list();

  }

}
