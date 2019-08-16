package com.smate.center.merge.dao.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.group.GroupDynamicAwards;
import com.smate.core.base.utils.data.SnsHibernateDao;

/**
 * 群组动态赞 记录 dao
 * 
 * @author tsz
 *
 */
@Repository
public class GroupDynamicAwardsDao extends SnsHibernateDao<GroupDynamicAwards, Long> {

  /**
   * 得到当前人的动态赞
   * 
   * @param awardPsnId
   * @return
   */
  public List<GroupDynamicAwards> getListByPsnId(Long awardPsnId) {
    String hql = "from GroupDynamicAwards g where g.awardPsnId =:awardPsnId ";
    return this.createQuery(hql).setParameter("awardPsnId", awardPsnId).list();

  }

}
