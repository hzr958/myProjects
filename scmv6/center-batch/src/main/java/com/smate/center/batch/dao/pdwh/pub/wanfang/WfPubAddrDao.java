package com.smate.center.batch.dao.pdwh.pub.wanfang;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.wanfang.WfPubAddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果基准库地址拆分表.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class WfPubAddrDao extends PdwhHibernateDao<WfPubAddr, Long> {

  /**
   * 获取成果地址列表.
   * 
   * @param pubId
   * @return
   */
  public List<WfPubAddr> getWfPubAddr(Long pubId) {
    String hql = "from WfPubAddr where pubId = ? ";
    return super.find(hql, pubId);
  }
}
