package com.smate.web.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PdwhPubMemberPO;

/**
 * 成果 成员dao
 * 
 * @author aijiangbin
 * @date 2018年5月31日
 */

@Repository
public class PdwhPubMemberDAO extends PdwhHibernateDao<PdwhPubMemberPO, Long> {

  public List<PdwhPubMemberPO> findByPubId(Long pdwhPubId) {
    String hql = "from PdwhPubMemberPO p where p.pdwhPubId =:pdwhPubId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) "
        + " order by p.seqNo asc  nulls last ";
    List list = this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
    return list;
  }

  public PdwhPubMemberPO getByPubIdAndPsnId(Long pdwhPubId, Long psnId) {
    String hql = "from PdwhPubMemberPO p where p.pdwhPubId =:pdwhPubId and p.psnId =:psnId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) ";
    List list = this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("psnId", psnId).list();
    if (!list.isEmpty() && list.size() > 0) {
      return (PdwhPubMemberPO) list.get(0);
    }
    return null;
  }

  public void deleteAllMember(Long pdwhPubId) {
    // 先删除
    String hql = "delete from PdwhPubMemberPO p where p.pdwhPubId=:pdwhPubId ";
    this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }

  public List<PdwhPubMemberPO> findPosMemberByPubId(Long pdwhPubId) {
    String hql = "from PdwhPubMemberPO p where p.pdwhPubId =:pdwhPubId "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) "
        + " order by p.seqNo asc nulls last ";
    List list = this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
    return list;
  }

}
