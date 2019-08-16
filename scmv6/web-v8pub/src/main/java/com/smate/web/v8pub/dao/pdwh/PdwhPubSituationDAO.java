package com.smate.web.v8pub.dao.pdwh;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.PdwhHibernateDao;
import com.smate.web.v8pub.po.pdwh.PdwhPubSituationPO;

/**
 * 基准库成果被收录情况Dao
 * 
 * @author YJ
 *
 *         2018年5月31日
 */
@Repository
public class PdwhPubSituationDAO extends PdwhHibernateDao<PdwhPubSituationPO, Long> {

  /**
   * 根据pubId和收录机构名获取成果收录情况对象
   * 
   * @param pdwhPubId 成果id
   * @param libraryName 收录机构名
   * @return
   */
  public PdwhPubSituationPO findByPdwhIdAndLibraryName(Long pdwhPubId, String libraryName) {
    String hql = "from PdwhPubSituationPO p where p.pdwhPubId=:pdwhPubId and p.libraryName=:libraryName "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) "
        + " order by p.gmtModified desc";
    List list = this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId)
        .setParameter("libraryName", libraryName.toUpperCase()).list();
    if (!list.isEmpty() && list.size() > 0) {
      return (PdwhPubSituationPO) list.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public List<String> listByPdwhPubId(Long pdwhPubId) {
    String hql = " select p.srcDbId from PdwhPubSituationPO p where p.pdwhPubId=:pdwhPubId and p.sitStatus =1 "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) "
        + " order by p.gmtModified desc";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

  public void deleteByPubId(Long pdwhPubId) {
    String hql = "delete from PdwhPubSituationPO p where p.pdwhPubId=:pdwhPubId";
    this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }

  @SuppressWarnings("unchecked")
  public PdwhPubSituationPO findByPdwhIdAndSrcDbId(Long pdwhPubId, String srcDbId) {
    String hql = "from PdwhPubSituationPO p where p.pdwhPubId=:pdwhPubId and p.srcDbId=:srcDbId and p.sitStatus =1 "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) "
        + " order by p.gmtModified desc";
    List<Object> list =
        this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("srcDbId", srcDbId).list();
    if (!list.isEmpty() && list.size() > 0) {
      return (PdwhPubSituationPO) list.get(0);
    }
    return null;
  }
}
