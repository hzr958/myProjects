package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPatentCategory;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 专利类别
 * 
 * @author zzx
 *
 */
@Repository
public class BdspPatentCategoryDao extends SnsbakHibernateDao<BdspPatentCategory, Long> {

  public List<BdspPatentCategory> findListByPubId(Long pubId) {
    // TODO Auto-generated method stub
    return null;
  }

}
