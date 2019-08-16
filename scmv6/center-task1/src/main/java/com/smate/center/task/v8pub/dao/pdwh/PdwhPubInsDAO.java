package com.smate.center.task.v8pub.dao.pdwh;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhPubInsPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库单位信息dao
 * 
 * @author YJ
 *
 *         2019年3月29日
 */
@Repository
public class PdwhPubInsDAO extends PdwhHibernateDao<PdwhPubInsPO, Long> {

  public void deleteByPdwhPubId(Long pdwhPubId) {
    String hql = "delete from PdwhPubInsPO t where t.pdwhPubId=:pdwhPubId";
    this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }

}
