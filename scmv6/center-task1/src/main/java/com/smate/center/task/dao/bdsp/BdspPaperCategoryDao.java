package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPaperCategory;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 论文类别
 * 
 * @author zzx
 *
 */
@Repository
public class BdspPaperCategoryDao extends SnsbakHibernateDao<BdspPaperCategory, Long> {

  public List<BdspPaperCategory> findListByPubId(Long pubId) {
    // TODO Auto-generated method stub
    return null;
  }

}
