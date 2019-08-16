package com.smate.center.batch.dao.pdwh.pub.pubmed;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.pubmed.PubMedPubMaddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * PUBMED成果地址匹配结果.
 * 
 * @author liqinghua
 * 
 */
@Repository
public class PubMedPubMaddrDao extends PdwhHibernateDao<PubMedPubMaddr, Long> {

  /**
   * 获取匹配加粗的成果地址.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public List<PubMedPubMaddr> getRemarkOrgs(Long pubId, Long insId) {
    String hql = "from PubMedPubMaddr where pubId = ? and insId = ?  and matched <> 4 ";
    return super.find(hql, pubId, insId);
  }

  /**
   * 获取匹配成果地址结果.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public List<PubMedPubMaddr> getPubMedPubMaddrs(Long pubId, Long insId) {
    String hql = "from PubMedPubMaddr where pubId = ? and insId = ?";
    return super.find(hql, pubId, insId);
  }

  /**
   * 获取匹配成果地址结果.
   * 
   * @param pubId
   * @param insId
   * @return
   */
  public PubMedPubMaddr getPubMedPubMaddr(Long insId, Long addrId) {
    String hql = "from PubMedPubMaddr where insId = ? and addrId = ? ";
    return super.findUnique(hql, insId, addrId);
  }

  /**
   * 加载指定pubIds，匹配状态的成果地址列表.
   * 
   * @param matched
   * @param xmlIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubMedPubMaddr> loadOrgs(List<Integer> matched, Set<Long> pubIds, Long insId) {
    String hql =
        "select new PubMedPubMaddr( t.maddrId, t.addrId,  t.insId,  t.pubId,  t.addr,  t.matched,  t1.addr) from PubMedPubMaddr t,PubMedPubAddr t1 where t.addrId = t1.addrId and t.matched in(:matched) and  t.pubId in(:pubIds) and t.insId = ?  ";
    return super.createQuery(hql).setParameterList("matched", matched).setParameterList("pubIds", pubIds)
        .setParameter("insId", insId).list();
  }

  /**
   * 加载指定XMLID，匹配状态的成果地址列表.
   * 
   * @param matched
   * @param pubIds
   * @param insId
   * @return
   */
  public List<PubMedPubMaddr> loadOrgs(Integer matched, Set<Long> pubIds, Long insId) {

    List<Integer> matcheds = new ArrayList<Integer>();
    // 所有状态
    if (matched == -1) {
      matcheds.add(0);
      matcheds.add(1);
      matcheds.add(2);
      matcheds.add(3);
      matcheds.add(4);
    } else {
      matcheds.add(matched);
    }
    return this.loadOrgs(matcheds, pubIds, insId);
  }

  /**
   * 更新地址匹配状态.
   * 
   * @param maddrId
   * @param matched
   */
  public void updateOrgsMatched(Long maddrId, int matched) {
    String hql = "update PubMedPubMaddr set matched = ? where maddrId = ? ";
    super.createQuery(hql, matched, maddrId).executeUpdate();
  }
}
