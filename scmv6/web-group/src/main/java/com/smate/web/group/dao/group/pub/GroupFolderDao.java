package com.smate.web.group.dao.group.pub;



import java.util.List;

import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.pub.GroupFolder;



/**
 * 群组文件夹Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupFolderDao extends SnsHibernateDao<GroupFolder, Long> {

  public void saveGroupFolder(GroupFolder groupFolder) {
    super.getSession().save(groupFolder);
  }

  public GroupFolder findGroupFolder(Long groupFolderId) {
    String hql = "from GroupFolder where groupFolderId = ?";
    return this.findUnique(hql, groupFolderId);
  }

  @SuppressWarnings("unchecked")
  public List<GroupFolder> findGroupFolderList(Long groupId) {
    String hql = "from GroupFolder where groupId=? and enabled='1' order by groupFolderId";
    Query query = createQuery(hql, groupId);
    return query.list();
  }

  public List<GroupFolder> findGroupFolderList(Long groupId, String folderType) {
    String hql = "from GroupFolder t where t.groupId=? and t.enabled=? and t.folderType=? order by t.groupFolderId";
    return super.find(hql, new Object[] {groupId, "1", folderType});
  }

  public GroupFolder findGroupFolderByName(Long groupId, String groupFolderName, String folderType) {
    String hql = "from GroupFolder t where t.groupId = ? and t.folderName = ? and t.folderType = ?";
    return this.findUnique(hql, groupId, groupFolderName, folderType);
  }

  @SuppressWarnings("unchecked")
  public List<GroupFolder> findGroupFolderByPsn(Long groupId, String folderType) {
    String hql = "from GroupFolder where groupId=? and folderType=? and enabled=?";
    return createQuery(hql, groupId, folderType, "1").list();
  }

  // ==============人员合并 start============
  @SuppressWarnings("unchecked")
  public List<GroupFolder> getGroupFolderByPsnId(Long delPsnId, Long groupId) {
    String hql = "from GroupFolder where (createPsnId=? or updatePsnId=?) and groupId=?";
    return super.createQuery(hql, delPsnId, delPsnId, groupId).list();
  }

  // ==============人员合并 end============
  // 获取在某个群组中中文件夹数
  public Long getSumFolder(Long groupId) {
    return (Long) super.createQuery("select count(groupFolderId) from GroupFolder where groupId=? ", groupId)
        .uniqueResult();
  }
}
