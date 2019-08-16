package com.smate.center.batch.dao.pdwh.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.PsnPubAllSearch;
import com.smate.core.base.utils.data.PdwhHibernateDao;

@Repository
public class PsnPubAllSearchDao extends PdwhHibernateDao<PsnPubAllSearch, Long> {

  /**
   * 根据psnId删除记录
   * 
   * @param psnId
   * @throws DaoException
   */
  public void delRecord(Long psnId) {
    String hql = "delete from PsnPubAllSearch t where t.psnId = ?";
    super.createQuery(hql, psnId).executeUpdate();
  }

  public Long getPsnPubSearchCount(Long psnId) {
    String hql = "select count(id) from PsnPubAllSearch t where t.psnId = ?";
    return findUnique(hql, psnId);
  }

  public Long countPubNotDisid(Long psnId) {
    String hql =
        "select count(t.id) from PsnPubAllSearch t where t.psnId = ? and not exists (select b.id from PublicationAllDis b where b.pubAllId = t.pubAllId)";
    return (Long) super.createQuery(hql, psnId).uniqueResult();
  }

  public void delPsnPubAllSearchByPuballId(Long puballId) {
    String hql = "delete from PsnPubAllSearch where pubAllId=?";
    super.createQuery(hql, puballId).executeUpdate();
  }
}
