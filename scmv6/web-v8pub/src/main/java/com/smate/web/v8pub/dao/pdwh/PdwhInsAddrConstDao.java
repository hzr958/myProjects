package com.smate.web.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PdwhInsAddrConst;

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

  @SuppressWarnings("unchecked")
  public List<String> getInsNameByInsId(Long insId) {
    String hql = "select distinct(t.insName) from PdwhInsAddrConst t where t.insId =:insId";
    return super.createQuery(hql).setParameter("insId", insId).list();
  }

}
