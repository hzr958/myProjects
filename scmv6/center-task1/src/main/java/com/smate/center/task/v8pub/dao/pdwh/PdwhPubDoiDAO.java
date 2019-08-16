package com.smate.center.task.v8pub.dao.pdwh;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhPubDoiPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果doi数据dao
 * 
 * @author YJ
 *
 *         2019年3月29日
 */

@Repository
public class PdwhPubDoiDAO extends PdwhHibernateDao<PdwhPubDoiPO, Long> {

  public void deleteByPdwhPubId(Long pdwhPubId) {
    String hql = "delete from PdwhPubDoiPO t where t.pdwhPubId=:pdwhPubId";
    this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }

}
