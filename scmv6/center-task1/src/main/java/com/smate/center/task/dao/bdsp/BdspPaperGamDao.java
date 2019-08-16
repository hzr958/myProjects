package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPaperGam;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 论文社交记录
 * 
 * @author zzx
 *
 */
@Repository
public class BdspPaperGamDao extends SnsbakHibernateDao<BdspPaperGam, Long> {

  public List<BdspPaperGam> findListByPubId(Long pubId) {
    // TODO Auto-generated method stub
    return null;
  }

}
