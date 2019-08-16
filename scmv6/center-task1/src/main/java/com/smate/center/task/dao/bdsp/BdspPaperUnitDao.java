package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPaperUnit;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 论文单位
 * 
 * @author zzx
 *
 */
@Repository
public class BdspPaperUnitDao extends SnsbakHibernateDao<BdspPaperUnit, Long> {

  public List<BdspPaperUnit> findListByPubId(Long pubId) {
    // TODO Auto-generated method stub
    return null;
  }

}
