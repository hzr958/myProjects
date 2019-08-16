package com.smate.center.batch.dao.pdwh.pub.cnipr;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.cnipr.CniprPubMaddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * cnipr成果地址匹配结果.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class CniprPubMaddrDao extends PdwhHibernateDao<CniprPubMaddr, Long> {

  /**
   * 获取匹配成果地址结果.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public List<CniprPubMaddr> getCniprPubMaddrs(Long pubId, Long insId) {
    String hql = "from CniprPubMaddr where pubId = ? and insId = ?";
    return super.find(hql, pubId, insId);
  }

  /**
   * 获取匹配成果地址结果.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public CniprPubMaddr getCniprPubMaddr(Long insId, Long addrId) {
    String hql = "from CniprPubMaddr where insId = ? and addrId = ? ";
    return super.findUnique(hql, insId, addrId);
  }

  /**
   * 更新地址匹配状态.
   * 
   * @param maddrId
   * @param matched
   */
  public void updateOrgsMatched(Long maddrId, int matched) {
    String hql = "update CniprPubMaddr set matched = ? where maddrId = ? ";
    super.createQuery(hql, matched, maddrId).executeUpdate();
  }
}
