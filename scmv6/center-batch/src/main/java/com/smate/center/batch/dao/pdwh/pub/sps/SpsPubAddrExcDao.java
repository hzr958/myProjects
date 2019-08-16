package com.smate.center.batch.dao.pdwh.pub.sps;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.sps.SpsPubAddrExc;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 排除的机构匹配地址.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class SpsPubAddrExcDao extends PdwhHibernateDao<SpsPubAddrExc, Long> {

  /**
   * 获取某机构排除成果HASH列表.
   * 
   * @param insId
   * @return
   */
  public List<SpsPubAddrExc> getExcAddrHash(Long insId) {

    String hql = "select new SpsPubAddrExc( id,  insId,  addrHash) from SpsPubAddrExc t where t.insId = ? ";
    return super.find(hql, insId);
  }

  /**
   * 获取某机构排除成果HASH列表.
   * 
   * @param insId
   * @return
   */
  public List<SpsPubAddrExc> getExcAddrHash(List<Long> insIds) {

    String hql = "select new SpsPubAddrExc( id,  insId,  addrHash) from SpsPubAddrExc t where t.insId in(:insIds)";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  /**
   * 保存排除地址.
   * 
   * @param insId
   * @param addr
   * @param addrHash
   */
  public SpsPubAddrExc saveSpsPubAddrExc(Long insId, String addr, Long addrHash) {
    String hql = "select count(1) from SpsPubAddrExc where insId = ? and addrHash = ? ";
    Long count = super.findUnique(hql, insId, addrHash);
    if (count > 0) {
      return null;
    }
    SpsPubAddrExc exc = new SpsPubAddrExc(insId, addr, addrHash);
    super.save(exc);
    return exc;
  }
}
