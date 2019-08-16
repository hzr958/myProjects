package com.smate.center.batch.dao.pdwh.pubimport;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubMaddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 成果地址匹配dao
 * 
 * @author zjh
 *
 */
@Repository
public class PdwhPubMaddrDao extends PdwhHibernateDao<PdwhPubMaddr, Long> {

  /**
   * 获取匹配成果地址结果.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public List<PdwhPubMaddr> getPdwhPubMaddrs(Long pubId, Long insId) {
    String hql = "from PdwhPubMaddr where pubId = ? and insId = ?";
    return super.find(hql, pubId, insId);
  }

}
