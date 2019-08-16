package com.smate.center.task.v8pub.dao.pdwh;

import org.springframework.stereotype.Repository;

import com.smate.center.task.v8pub.pdwh.po.PdwhPubPatentPO;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 基准库成果专利数据
 * 
 * @author YJ
 *
 *         2019年3月29日
 */
@Repository
public class PdwhPubPatentDAO extends PdwhHibernateDao<PdwhPubPatentPO, Long> {

  public void deleteByPdwhPubId(Long pdwhPubId) {
    String hql = "delete from PdwhPubPatentPO t where t.pdwhPubId=:pdwhPubId";
    this.createQuery(hql).setParameter("pdwhPubId", pdwhPubId).executeUpdate();
  }

}
