package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspPrjCategory;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 项目分类
 * 
 * @author zzx
 *
 */
@Repository
public class BdspPrjCategoryDao extends SnsbakHibernateDao<BdspPrjCategory, Long> {

  public List<BdspPrjCategory> findListByPrjId(Long prjId) {
    String hql = "from BdspPrjCategory t where t.prjId=:prjId";
    return this.createQuery(hql).setParameter("prjId", prjId).list();
  }

}
