package com.smate.center.task.dao.pdwh.quartz;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.pdwh.quartz.TmpJiangxiPubaddr;
import com.smate.core.base.utils.data.PdwhHibernateDao;

/**
 * 
 * @author zzx
 *
 */
@Repository
public class TmpJiangxiPubaddrDao extends PdwhHibernateDao<TmpJiangxiPubaddr, Long> {

  public List<TmpJiangxiPubaddr> findList(int batchSize) {
    String hql =
        "from TmpJiangxiPubaddr t where not exists(select 1 from PubCategoryPatentTemp t2 where t2.addrId=t.addrId ) and t.pubType=5";
    return this.createQuery(hql).setMaxResults(batchSize).list();
  }

}
