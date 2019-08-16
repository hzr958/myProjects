package com.smate.web.group.dao.group;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.GroupBaseInfo;

@Repository
public class GroupBaseinfoDao extends SnsHibernateDao<GroupBaseInfo, Long> {


  /**
   * 获取群组基本信息.
   * 
   * @param groupId
   * @return
   */
  public GroupBaseInfo getGroupBaseInfo(Long groupId) {
    String hql = "from GroupBaseInfo t where t.groupId=? and rownum=1 ";
    Object obj = super.createQuery(hql, groupId).uniqueResult();
    if (obj != null) {
      return (GroupBaseInfo) obj;
    }
    return null;
  }

  /**
   * 查询群组名称 群组图片
   * 
   */
  public GroupBaseInfo getGroupNameAndImage(Long groupId) {
    String hql =
        "select new GroupBaseInfo(t.groupName,t.groupImgUrl,t.groupCategory,t.groupDescription)from GroupBaseInfo t where t.groupId=? and rownum=1 ";
    Object obj = super.createQuery(hql, groupId).uniqueResult();
    if (obj != null) {
      return (GroupBaseInfo) obj;
    }
    return null;
  }

  /**
   * 查询我的未删除的群组名称
   * 
   */
  public List<GroupBaseInfo> getGroupNames(List<Long> groupIds, String nameSub, Integer size) {
    StringBuilder sb = new StringBuilder();
    sb.append("select new GroupBaseInfo(t.groupName,t.groupId) from GroupBaseInfo t where ");
    if (StringUtils.isNotBlank(nameSub)) {
      sb.append("instr(t.groupName,:nameSub)>0 and ");
    }
    sb.append("t.groupId in(:groupIds) and t.groupCategory in(10,11)  order by t.lastVisitDate  desc  nulls Last");
    Query query = null;
    if (StringUtils.isNotBlank(nameSub)) {
      query = super.createQuery(sb.toString()).setParameterList("groupIds", groupIds).setParameter("nameSub", nameSub);
    } else {
      query = super.createQuery(sb.toString()).setParameterList("groupIds", groupIds);
    }
    if (size != null && size != 0) {
      query = query.setMaxResults(size);
    }
    return query.list();
  }

  /**
   * 保存群组基本信息.
   * 
   * @author lhd
   * @param groupBaseInfo
   */
  public void saveBaseInfo(GroupBaseInfo groupBaseInfo) {
    if (groupBaseInfo != null) {
      if (groupBaseInfo.getLastVisitDate() == null) {
        groupBaseInfo.setLastVisitDate(groupBaseInfo.getCreateDate());
      }
      if (groupBaseInfo.getId() != null) {
        super.getSession().update(groupBaseInfo);
      } else {
        super.save(groupBaseInfo);
      }
    }
  }

  /**
   * 更新群组访问时间
   * 
   * @param groupId
   */
  public void updateLastVisitDate(Long groupId) {
    String hql = "update GroupBaseInfo t set t.lastVisitDate=:lastVisitDate where t.groupId=:groupId ";
    super.createQuery(hql).setParameter("lastVisitDate", new Date()).setParameter("groupId", groupId).executeUpdate();
  }

}
