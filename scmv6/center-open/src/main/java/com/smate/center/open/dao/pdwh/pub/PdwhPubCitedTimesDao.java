package com.smate.center.open.dao.pdwh.pub;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.open.model.pdwh.pub.PdwhPubCitedTimes;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 
 * @author zjh 成果引用次数dao
 *
 */
@Repository
public class PdwhPubCitedTimesDao extends PdwhHibernateDao<PdwhPubCitedTimes, Long> {

  public PdwhPubCitedTimes getObjByPdwhPubId(Long pdwhPubId) {
    String hql =
        "  from  PdwhPubCitedTimes  p where p.pdwhPubId =:pdwhPubId   and p.dbid = 99 order by p.updateDate desc ";
    List list = this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).list();
    if (list != null && list.size() > 0) {
      return (PdwhPubCitedTimes) list.get(0);
    }
    return null;
  }

}
