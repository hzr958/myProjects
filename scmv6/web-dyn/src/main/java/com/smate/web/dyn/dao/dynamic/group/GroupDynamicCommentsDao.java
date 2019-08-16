package com.smate.web.dyn.dao.dynamic.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.dyn.form.dynamic.group.GroupDynShowForm;
import com.smate.web.dyn.model.dynamic.group.GroupDynamicComments;


/**
 * 群组冬天评论记录dao
 * 
 * @author tsz
 *
 */
@Repository
public class GroupDynamicCommentsDao extends SnsHibernateDao<GroupDynamicComments, Long> {
  /**
   * 获取评论列表
   * 
   * @param dynId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GroupDynamicComments> getGroupDynContents(GroupDynShowForm form) {
    String hql = "from GroupDynamicComments g where g.dynId=:dynId order by g.commentDate asc,g.id";
    if (form.getMaxResults() != 1) {
      return super.createQuery(hql.toString()).setParameter("dynId", form.getDynId()).list();
    } else {
      return super.createQuery(hql.toString()).setParameter("dynId", form.getDynId())
          .setMaxResults(form.getMaxResults()).list();
    }

  }


  /**
   * 查询群组动态的时候附带最新的一条评论
   * 
   * @param dynId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GroupDynamicComments> getGroupDynContent(Long dynId) {
    String hql = "from GroupDynamicComments g where g.dynId=:dynId order by g.commentDate desc,g.id desc ";
    List<GroupDynamicComments> list =
        super.createQuery(hql.toString()).setParameter("dynId", dynId).setMaxResults(1).list();
    if (list != null && list.size() > 0) {
      return list;
    } else {
      return null;
    }
  }

}
