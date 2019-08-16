package com.smate.center.batch.dao.pdwh.pub.isi;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.isi.IsiPubAddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果基准库地址拆分表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class IsiPubAddrDao extends PdwhHibernateDao<IsiPubAddr, Long> {

  /**
   * 获取成果地址列表.
   * 
   * @param pubId
   * @return
   */
  public List<IsiPubAddr> getIsiPubAddr(Long pubId) {
    String hql = "from IsiPubAddr where pubId = ? ";
    return super.find(hql, pubId);
  }
}
