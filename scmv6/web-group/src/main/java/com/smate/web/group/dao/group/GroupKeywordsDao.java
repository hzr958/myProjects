package com.smate.web.group.dao.group;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.GroupKeywords;

/**
 * 群组关键词Dao.
 * 
 * @author jab
 * 
 */
@Repository
public class GroupKeywordsDao extends SnsHibernateDao<GroupKeywords, Long> {
  public List<GroupKeywords> getGroupKeywords(Long groupId) {
    String hql = "select   new GroupKeywords(g.keyword) from  GroupKeywords g where g.groupId=:groupId";
    return this.createQuery(hql).setParameter("groupId", groupId).list();
  }

  /**
   * 根据群组id删除对应的群组关键字
   * 
   * @param groupId
   */
  public void deleteGroupKeywordsByGId(Long groupId) {
    String hql = "delete from GroupKeywords r where r.groupId=?";
    super.createQuery(hql, groupId).executeUpdate();

  }

}
