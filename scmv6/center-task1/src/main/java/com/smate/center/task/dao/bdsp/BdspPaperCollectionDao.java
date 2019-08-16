package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPaperCollection;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 论文收录
 * 
 * @author zzx
 *
 */
@Repository
public class BdspPaperCollectionDao extends SnsbakHibernateDao<BdspPaperCollection, Long> {

  public List<BdspPaperCollection> findListByPubId(Long pubId) {
    // TODO Auto-generated method stub
    return null;
  }

}
