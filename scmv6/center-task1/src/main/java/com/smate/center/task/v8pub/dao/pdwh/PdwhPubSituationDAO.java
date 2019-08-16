package com.smate.center.task.v8pub.dao.pdwh;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhPubSituationPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

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
  @SuppressWarnings("rawtypes")
  public PdwhPubSituationPO findByPdwhIdAndLibraryName(Long pdwhPubId, String libraryName) {
    List<Object> params = new ArrayList<>();
    String hql = "from PdwhPubSituationPO p where p.pdwhPubId=? and p.libraryName like ? "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) "
        + " order by p.gmtModified desc";
    params.add(pdwhPubId);
    params.add(libraryName + "%");
    List list = createQuery(hql.toString(), params.toArray()).list();
    if (!list.isEmpty() && list.size() > 0) {
      return (PdwhPubSituationPO) list.get(0);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  public Long getPubIdBySrcId(String sourceId) {
    String hql = "select p.pdwhPubId from PdwhPubSituationPO p where p.srcId = :sourceId and p.sitStatus =1 "
        + " and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) ";
    List<Long> list = super.createQuery(hql).setParameter("sourceId", sourceId).list();
    if (!list.isEmpty() && list.size() > 0) {
      return list.get(0);
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

  public void deleteAll(Long pdwhPubId) {
    String hql = "delete from PdwhPubSituationPO p where p.pdwhPubId=:pdwhPubId";
    this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }

  public List<PdwhPubSituationPO> queryListByPdwhPubId(Long pdwhPubId) {
    String hql = "from PdwhPubSituationPO p where p.pdwhPubId=:pdwhPubId and p.sitStatus =1 "
        + "and exists (SELECT 1 FROM PubPdwhPO pp where pp.status = 0 and pp.pubId = p.pdwhPubId) "
        + "order by p.gmtModified desc";
    return this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }
}
