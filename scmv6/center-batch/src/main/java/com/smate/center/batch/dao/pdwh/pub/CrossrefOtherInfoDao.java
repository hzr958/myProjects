package com.smate.center.batch.dao.pdwh.pub;

import org.springframework.stereotype.Repository;

import com.smate.center.batch.model.pdwh.pub.CrossrefOtherInfo;
import com.smate.core.base.utils.data.PdwhHibernateDao;


@Repository
public class CrossrefOtherInfoDao extends PdwhHibernateDao<CrossrefOtherInfo, Long> {

  public CrossrefOtherInfo getInfo(Long pubId) {
    String hql = "from CrossrefOtherInfo t  where t.pubId= :pubId";
    return (CrossrefOtherInfo) createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

}
