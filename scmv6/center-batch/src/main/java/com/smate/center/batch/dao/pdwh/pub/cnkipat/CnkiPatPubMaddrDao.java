package com.smate.center.batch.dao.pdwh.pub.cnkipat;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnkipat.CnkiPatPubMaddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * cnkipat成果地址匹配结果.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CnkiPatPubMaddrDao extends PdwhHibernateDao<CnkiPatPubMaddr, Long> {

  /**
   * 获取匹配成果地址结果.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public List<CnkiPatPubMaddr> getCnkiPatPubMaddrs(Long pubId, Long insId) {
    String hql = "from CnkiPatPubMaddr where pubId = ? and insId = ?";
    return super.find(hql, pubId, insId);
  }

  /**
   * 获取匹配成果地址结果.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public CnkiPatPubMaddr getCnkiPatPubMaddr(Long insId, Long addrId) {
    String hql = "from CnkiPatPubMaddr where insId = ? and addrId = ? ";
    return super.findUnique(hql, insId, addrId);
  }

  /**
   * 更新地址匹配状态.
   * 
   * @param maddrId
   * @param matched
   */
  public void updateOrgsMatched(Long maddrId, int matched) {
    String hql = "update CnkiPatPubMaddr set matched = ? where maddrId = ? ";
    super.createQuery(hql, matched, maddrId).executeUpdate();
  }
}
