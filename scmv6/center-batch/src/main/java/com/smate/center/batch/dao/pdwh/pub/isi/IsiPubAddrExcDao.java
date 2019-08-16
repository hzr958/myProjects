package com.smate.center.batch.dao.pdwh.pub.isi;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.isi.IsiPubAddrExc;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 排除的机构匹配地址.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class IsiPubAddrExcDao extends PdwhHibernateDao<IsiPubAddrExc, Long> {

  /**
   * 获取某机构排除成果HASH列表.
   * 
   * @param insId
   * @return
   */
  public List<IsiPubAddrExc> getExcAddrHash(Long insId) {

    String hql = "select new IsiPubAddrExc( id,  insId,  addrHash) from IsiPubAddrExc t where t.insId = ? ";
    return super.find(hql, insId);
  }

  /**
   * 获取某机构排除成果HASH列表.
   * 
   * @param insId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<IsiPubAddrExc> getExcAddrHash(List<Long> insIds) {

    String hql = "select new IsiPubAddrExc( id,  insId,  addrHash) from IsiPubAddrExc t where t.insId in(:insIds) ";
    return super.createQuery(hql).setParameterList("insIds", insIds).list();
  }

  /**
   * 保存排除地址.
   * 
   * @param insId
   * @param addr
   * @param addrHash
   */
  public IsiPubAddrExc saveIsiPubAddrExc(Long insId, String addr, Long addrHash) {
    String hql = "select count(id) from IsiPubAddrExc where insId = ? and addrHash = ? ";
    Long count = super.findUnique(hql, insId, addrHash);
    if (count > 0) {
      return null;
    }
    IsiPubAddrExc exc = new IsiPubAddrExc(insId, addr, addrHash);
    super.save(exc);
    return exc;
  }
}
