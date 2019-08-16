package com.smate.center.batch.dao.pdwh.pub.sps;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.sps.SpsPubAddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果基准库地址拆分表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class SpsPubAddrDao extends PdwhHibernateDao<SpsPubAddr, Long> {

  /**
   * 获取成果地址列表.
   * 
   * @param pubId
   * @return
   */
  public List<SpsPubAddr> getSpsPubAddr(Long pubId) {
    String hql = "from SpsPubAddr where pubId = ? ";
    return super.find(hql, pubId);
  }
}
