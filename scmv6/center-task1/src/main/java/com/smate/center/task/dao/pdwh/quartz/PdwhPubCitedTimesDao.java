package com.smate.center.task.dao.pdwh.quartz;

import com.smate.center.task.model.pdwh.pub.PdwhPubCitedTimes;
import com.smate.core.base.utils.data.PdwhHibernateDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PdwhPubCitedTimesDao extends PdwhHibernateDao<PdwhPubCitedTimes, Long> {

  public Long getPubCitedTimesCount() {
    String hql = "select  count(distinct t.pdwhPubId) from PdwhPubCitedTimes t";
    return (Long) super.createQuery(hql).uniqueResult();
  }

  public PdwhPubCitedTimes getcitesByPubDBId(Long currentPubId, Integer dbId) {
    String hql = "from PdwhPubCitedTimes t  where t.pdwhPubId=:currentPubId and t.dbId =:dbId";
    return (PdwhPubCitedTimes) super.createQuery(hql).setParameter("currentPubId", currentPubId)
        .setParameter("dbId", dbId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhPubCitedTimes> getRecordByPubId(Long currentPubId) {
    String hql = "from PdwhPubCitedTimes t  where t.pdwhPubId=:currentPubId";
    return super.createQuery(hql).setParameter("currentPubId", currentPubId).list();
  }

  @SuppressWarnings("unchecked")
  public List<PdwhPubCitedTimes> listByPubId(Long pdwhPubId) {
    String hql = "from PdwhPubCitedTimes t  where t.pdwhPubId=:pdwhPubId";
    return super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
  }

  public Integer getCitedTimesByPdwhPubId(Long pdwhPubId) {
    String hql =
        "select t.citedTimes from PdwhPubCitedTimes t  where t.pdwhPubId=:pdwhPubId order by t.citedTimes desc";
    List list = super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
    if (list != null && list.size() > 0) {
      return (Integer) list.get(0);
    }
    return 0;
  }

  public Integer getCitedTimesByPdwhPubId(Long pdwhPubId, Integer dbId) {
    String hql = "select max(t.citedTimes) from PdwhPubCitedTimes t  where t.pdwhPubId=:pdwhPubId and t.dbId=:dbId";
    return (Integer) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("dbId", dbId)
        .uniqueResult();
  }

  public PdwhPubCitedTimes getByPdwhPubId(Long pdwhPubId, Integer dbId) {
    String hql = "from PdwhPubCitedTimes t  where t.pdwhPubId=:pdwhPubId and t.dbId=:dbId";
    return (PdwhPubCitedTimes) super.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).setParameter("dbId", dbId)
        .uniqueResult();
  }

}
