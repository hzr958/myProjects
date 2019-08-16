package com.smate.center.merge.dao.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.center.merge.model.sns.group.GroupDynamicComments;
import com.smate.core.base.utils.data.SnsHibernateDao;


/**
 * 群组冬天评论记录dao
 * 
 * @author tsz
 *
 */
@Repository
public class GroupDynamicCommentsDao extends SnsHibernateDao<GroupDynamicComments, Long> {

  /**
   * 得到当前人的动太评论
   * 
   * @param commentPsnId
   * @return
   */
  public List<GroupDynamicComments> getListByPsnId(Long commentPsnId) {
    String hql = "from GroupDynamicComments g where g.commentPsnId =:commentPsnId   and g.status = 0 ";
    return this.createQuery(hql).setParameter("commentPsnId", commentPsnId).list();

  }

}
