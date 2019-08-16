package com.smate.center.batch.dao.pdwh.pub.cnki;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubMaddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * cnki成果地址匹配结果.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CnkiPubMaddrDao extends PdwhHibernateDao<CnkiPubMaddr, Long> {

  /**
   * 获取匹配成果地址结果.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public List<CnkiPubMaddr> getCnkiPubMaddrs(Long pubId, Long insId) {
    String hql = "from CnkiPubMaddr where pubId = ? and insId = ?";
    return super.find(hql, pubId, insId);
  }

  /**
   * 获取匹配成果地址结果.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public CnkiPubMaddr getCnkiPubMaddr(Long insId, Long addrId) {
    String hql = "from CnkiPubMaddr where insId = ? and addrId = ? ";
    return super.findUnique(hql, insId, addrId);
  }

  /**
   * 更新地址匹配状态.
   * 
   * @param maddrId
   * @param matched
   */
  public void updateOrgsMatched(Long maddrId, int matched) {
    String hql = "update CnkiPubMaddr set matched = ? where maddrId = ? ";
    super.createQuery(hql, matched, maddrId).executeUpdate();
  }
}
