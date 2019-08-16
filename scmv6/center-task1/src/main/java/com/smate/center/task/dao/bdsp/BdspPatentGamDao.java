package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPatentGam;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 专利社交
 * 
 * @author zzx
 *
 */
@Repository
public class BdspPatentGamDao extends SnsbakHibernateDao<BdspPatentGam, Long> {

  public List<BdspPatentGam> findListByPubId(Long pubId) {
    // TODO Auto-generated method stub
    return null;
  }

}
