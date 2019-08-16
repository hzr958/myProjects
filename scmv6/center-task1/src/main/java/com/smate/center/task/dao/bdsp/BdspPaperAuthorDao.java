package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPaperAuthor;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 论文作者
 * 
 * @author zzx
 *
 */
@Repository
public class BdspPaperAuthorDao extends SnsbakHibernateDao<BdspPaperAuthor, Long> {

  public List<BdspPaperAuthor> findListByPubId(Long pubId) {
    // TODO Auto-generated method stub
    return null;
  }

}
