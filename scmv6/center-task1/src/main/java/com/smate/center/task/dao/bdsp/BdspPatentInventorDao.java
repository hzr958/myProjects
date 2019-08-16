package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPatentInventor;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 专利发明人
 * 
 * @author zzx
 *
 */
@Repository
public class BdspPatentInventorDao extends SnsbakHibernateDao<BdspPatentInventor, Long> {

  public List<BdspPatentInventor> findListByPubId(Long pubId) {
    String hql = "from BdspPatentInventor t where t.pubId=:pubId";
    return this.createQuery(hql).setParameter("pubId", pubId).list();
  }

}
