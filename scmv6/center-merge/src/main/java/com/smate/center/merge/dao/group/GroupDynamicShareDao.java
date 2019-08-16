package com.smate.center.merge.dao.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.group.GroupDynamicShare;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 群组动态分享 dao
 * 
 * @author tsz
 *
 */
@Repository
public class GroupDynamicShareDao extends SnsHibernateDao<GroupDynamicShare, Long> {

  /**
   * 得到当前人的动态分享
   * 
   * @param commentPsnId
   * @return
   */
  public List<GroupDynamicShare> getListByPsnId(Long sharePsnId) {
    String hql = "from GroupDynamicShare g where g.sharePsnId =:sharePsnId   and g.status = 0 ";
    return this.createQuery(hql).setParameter("sharePsnId", sharePsnId).list();

  }

}
