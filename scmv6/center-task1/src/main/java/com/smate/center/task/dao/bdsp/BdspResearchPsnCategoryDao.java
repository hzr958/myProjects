package com.smate.center.task.dao.bdsp;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.task.model.bdsp.BdspResearchPsnCategory;
import com.smate.core.base.utils.data.SnsbakHibernateDao;

/**
 * 人员分类
 * 
 * @author zzx
 *
 */
@Repository
public class BdspResearchPsnCategoryDao extends SnsbakHibernateDao<BdspResearchPsnCategory, Long> {

  public List<BdspResearchPsnCategory> findListByPsnId(Long psnId) {
    String hql = "from BdspResearchPsnCategory t where t.psnId=:psnId";
    return this.createQuery(hql).setParameter("psnId", psnId).list();
  }

}
