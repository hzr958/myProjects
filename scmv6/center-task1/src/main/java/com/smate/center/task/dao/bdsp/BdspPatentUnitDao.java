package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPatentUnit;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 专利单位（产权人）
 * 
 * @author zzx
 *
 */
@Repository
public class BdspPatentUnitDao extends SnsbakHibernateDao<BdspPatentUnit, Long> {

  public List<BdspPatentUnit> findListByPubId(Long pubId) {
    String hql = "from BdspPatentUnit t where t.pubId=:pubId";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }

}
