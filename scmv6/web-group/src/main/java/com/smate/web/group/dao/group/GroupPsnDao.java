package com.smate.web.group.dao.group;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.web.group.model.group.GroupPsn;
import com.smate.web.group.model.group.psn.GroupPsnNodeSum;
import com.smate.web.group.model.group.psn.GroupPsnNodeSumPk;

/**
 * 群组主表 dao
 * 
 * @author tsz
 *
 */
@Deprecated
@Repository
public class GroupPsnDao extends SnsHibernateDao<GroupPsn, Long> {
  /**
   * 查询群组信息
   * 
   */
  public GroupPsn findMyGroup(Long groupId) {
    String hql = "from GroupPsn where groupId=? and status='01'";
    return super.findUnique(hql, groupId);
  }

  /**
   * 查询群组名称 群组图片
   * 
   */
  public GroupPsn findMyGroupName(Long groupId) {
    String hql =
        "select new GroupPsn(g.groupName ,g.groupImgUrl,g.groupCategory,g.openType,g.isPubView,g.isRefView,g.isShareFile) from GroupPsn g where g.groupId=? and g.status='01'";
    return super.findUnique(hql, groupId);
  }

  @SuppressWarnings("unchecked")
  public List<GroupPsnNodeSum> sumGroupByCategory(Integer nodeId) throws Exception {
    String hql =
        "select count(groupId) as categorySum,groupCategory from GroupPsn where status='01' and openType in('O','H') and groupCategory is not null group by groupCategory";

    List<Object[]> list = super.createQuery(hql).list();
    List<GroupPsnNodeSum> groupPnsNodeSumList = new ArrayList<GroupPsnNodeSum>();
    for (Object[] object : list) {
      GroupPsnNodeSum groupPsnNodeSum = new GroupPsnNodeSum();
      groupPsnNodeSum.setCategorySum(NumberUtils.toInt(ObjectUtils.toString(object[0])));
      groupPsnNodeSum.setId(new GroupPsnNodeSumPk(ObjectUtils.toString(object[1]), nodeId));

      groupPnsNodeSumList.add(groupPsnNodeSum);
    }

    return groupPnsNodeSumList;
  }

}
