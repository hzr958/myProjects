package com.smate.center.batch.dao.sns.pub;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.batch.model.sns.pub.GroupBaseInfo;
import com.smate.center.batch.model.sns.pub.GroupPsnFriendSum;
import com.smate.center.batch.model.sns.pub.GroupPsnNodeSum;
import com.smate.center.batch.model.sns.pub.GroupPsnNodeSumPk;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 群组基础信息表DAO类.
 * 
 * @author mjg
 * 
 */
@Repository
public class GroupBaseinfoDao extends SnsHibernateDao<GroupBaseInfo, Long> {

  @Autowired
  private GroupKeyDiscDao groupKeyDiscDao;
  @Autowired
  private GroupStatisticsDao groupStatisticsDao;

  /**
   * 保存群组基本信息.
   * 
   * @param groupBaseInfo
   */
  public void saveBaseInfo(GroupBaseInfo groupBaseInfo) {
    if (groupBaseInfo != null) {
      if (groupBaseInfo.getLastVisitDate() == null) {
        groupBaseInfo.setLastVisitDate(groupBaseInfo.getCreateDate());
      }
      if (groupBaseInfo.getId() != null) {
        super.getSession().update(groupBaseInfo);
        // String hql =
        // "update GroupBaseInfo t set t.address=?,t.email=?,t.fileId=?,t.fundingTypes,"
        // +
        // "t.groupAnnouncement=?,t.groupCategory=?,t.groupDescription=?,"
        // +
        // "t.groupImgUrl=?,t.groupName=?,t.groupPageUrl=? where t.groupId=? ";
        // super.createQuery(hql, groupBaseInfo.getAddress(),
        // groupBaseInfo.getEmail(), groupBaseInfo.getFileId(),
        // groupBaseInfo.getFundingTypes(),
        // groupBaseInfo.getGroupAnnouncement(),
        // groupBaseInfo.getGroupCategory(),
        // groupBaseInfo.getGroupDescription(),
        // groupBaseInfo.getGroupImgUrl(), groupBaseInfo.getGroupName(),
        // groupBaseInfo.getGroupPageUrl(),
        // groupBaseInfo.getGroupId()).executeUpdate();
      } else {
        super.save(groupBaseInfo);
      }
    }
  }

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

  // 在GroupBaseInfo对应表中查找groupname
  public String findGroupName(Long groupId) {
    String hql = "select g.groupName from GroupBaseInfo g where g.groupId= ? ";
    return (String) super.createQuery(hql, groupId).uniqueResult();
  }

  /**
   * 更新群组简介.
   * 
   * @param sourceGroupId
   * @param targetGroupId
   */
  public void updateGroupDesc(Long sourceGroupId, Long targetGroupId) {
    String hql = "select groupDescription from GroupBaseInfo t where t.groupId=? ";
    Object obj = super.createQuery(hql, sourceGroupId).uniqueResult();
    if (obj != null) {
      hql = "update GroupBaseInfo t set t.groupDescription=? where t.groupId=? ";
      super.createQuery(hql, ObjectUtils.toString(obj), targetGroupId).executeUpdate();
    }
  }

  /**
   * 获取群组序列ID.
   * 
   * @return @
   */
  public Long findNewGroupId() {

    BigDecimal groupId =
        (BigDecimal) super.getSession().createSQLQuery("select SEQ_GROUP_PSN.nextval from dual").uniqueResult();
    return groupId.longValue();
  }

  /**
   * 获取群组编号.
   * 
   * @return @
   */
  public Long creatGroupNo() {

    BigDecimal groupNo =
        (BigDecimal) super.getSession().createSQLQuery("select SEQ_GROUP_NO.nextval from dual").uniqueResult();
    return groupNo.longValue();
  }

  /**
   * 获取最大群组ID.
   * 
   * @return
   */
  public Long getMaxGroupId() {
    String hql = "select max(groupId) from GroupBaseInfo t ";
    Object obj = super.createQuery(hql).uniqueResult();
    if (obj != null) {
      return Long.valueOf(ObjectUtils.toString(obj));
    }
    return null;
  }


  /**
   * 获取我的群组列表.
   * 
   * @param groupCategory
   * @param ownerType
   * @param userId
   * @param page
   * @param searchKey
   * @param local
   * @return
   */
  @SuppressWarnings({"rawtypes"})
  public Page findMyGroupList(String groupCategory, String ownerType, Long userId, Page page, String searchKey,
      Locale local) {
    String countHql = "select count(t.groupId) ";
    // String resultHql =
    // "select new
    // GroupBaseInfo(t.groupId,t.groupName,t.groupDescription,t.groupCategory,t.groupImgUrl,t.lastVisitDate,p.psnId,p.isAccept,p.groupRole,(select
    // s.sumToMembers from GroupStatistics s where t.groupId=s.groupId and rownum=1) as sumToMembers) ";
    String resultSql = "select t.group_id,p.psn_id ,p.is_accept ,p.group_role ";
    // SCM-2212_MJG_2013-04-09.
    List<Object> params = new ArrayList<Object>();
    StringBuffer hql = new StringBuffer();
    hql.append("from GroupBaseInfo t,GroupInvitePsn p ");
    hql.append("where t.groupId=p.groupId and t.groupCategory in (10,11) ");
    hql.append("and exists (select status from GroupFilter f where f.groupId=t.groupId and f.status='01') ");
    hql.append("and p.psnId=? and p.status='01' and (p.isAccept=1 or p.isAccept is null) ");
    StringBuffer sql = new StringBuffer();

    sql.append("from GROUP_BASEINFO t,GROUP_INVITE_PSN p ");
    sql.append("where t.group_id=p.group_id and t.group_category in (10,11) ");
    sql.append("and exists (select status from GROUP_FILTER f where f.group_id=t.group_id and f.status='01') ");
    sql.append("and p.psn_id=? and p.status='01' and (p.is_accept=1 or p.is_accept is null) ");
    params.add(userId);

    if (StringUtils.isNotEmpty(groupCategory)) {
      hql.append(" and t.groupCategory=? ");
      sql.append("and t.group_category=? ");
      params.add(groupCategory);
    }
    if (StringUtils.isNotEmpty(ownerType)) {
      if ("admin".equals(ownerType)) {// 管理和创建
        hql.append(" and p.groupRole in ('1','2') ");
        sql.append("and p.group_role in ('1','2') ");
      } else if ("join".equals(ownerType)) {
        hql.append(" and p.groupRole in ('3') ");
        sql.append("and p.group_role in ('3') ");
      }
    }
    if (StringUtils.isNotEmpty(searchKey)) {
      String groupNo = StringUtils.isEmpty(searchKey) ? "0" : searchKey;
      // TSZ_2014-03-20_SCM-4919 判断 输入的检索条件 是否为 数字
      Pattern pattern = Pattern.compile("[0-9]*");
      if (!pattern.matcher(groupNo).matches()) {
        groupNo = "0";
      }

      hql.append(" and (upper(t.groupName) like ? or t.groupNo = ?) ");
      sql.append(" and (upper(t.group_name) like ? or t.group_no = ?) ");
      params.add("%" + HtmlUtils.htmlEscape(searchKey).toUpperCase().trim().replaceAll("%", "\\\\%") + "%");
      params.add(Long.valueOf(groupNo));
    }
    // String orderHql = "order by t.lastVisitDate desc,t.groupId desc";
    String orderSql = "order by t.last_visit_date desc nulls last,t.group_id desc ";
    // 记录数
    Long totalCount = super.findUnique(countHql + hql.toString(), params.toArray());
    page.setTotalCount(totalCount);

    // 查询数据实体
    page = this.QueryTable(resultSql + sql.toString() + orderSql, params.toArray(), page);
    return page;
  }

  /**
   * 查找群组菜单左侧统计信息<因目前群组类型只有教学和科研>_限制了获取统计信息的群组类型_MJG_SCM-5237.
   * 
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<GroupPsnNodeSum> findGroupCategorySumList() {
    StringBuffer hql = new StringBuffer();
    hql.append("select count(groupId) as categorySum,groupCategory from GroupBaseInfo t ");
    hql.append(
        " where exists (select 1 from GroupFilter f where f.groupId=t.groupId and f.openType in('O','H') and f.status='01') and t.groupCategory in (10,11) group by t.groupCategory ");

    List<Object[]> list = super.createQuery(hql.toString()).list();
    List<GroupPsnNodeSum> groupAllSumList = new ArrayList<GroupPsnNodeSum>();
    for (Object[] object : list) {
      GroupPsnNodeSum groupPnsNodeSum = new GroupPsnNodeSum();
      groupPnsNodeSum.setCategorySum(NumberUtils.toInt(ObjectUtils.toString(object[0])));
      groupPnsNodeSum.setId(new GroupPsnNodeSumPk());
      groupPnsNodeSum.getId().setCode(ObjectUtils.toString(object[1]));
      groupAllSumList.add(groupPnsNodeSum);
    }
    return groupAllSumList;

  }

  /**
   * 获取检索群组列表.
   * 
   * @param groupCategory
   * @param disciplines
   * @param groupName
   * @param psnId 用户ID
   * @param page
   * @return @
   */
  @SuppressWarnings({"rawtypes"})
  public Page findGroupInSearchList(String groupCategory, String disciplines, String groupName, Long psnId, Page page) {

    StringBuilder countHql = new StringBuilder("select count(t.group_id) ");
    String orderHql = " order by t.create_date desc nulls last,t.group_no desc ";

    StringBuilder resultHql = new StringBuilder("select t.group_id , t.is_accept,t.group_role ");

    List<Object> params = new ArrayList<Object>();
    StringBuilder hql = new StringBuilder();
    hql.append(
        "from (select b.*,g.is_accept as is_accept ,g.group_role as group_role from group_baseinfo b left join (select * from group_invite_psn where psn_id=? and status='01') g on b.group_id=g.group_id ) t,group_filter f where t.group_id=f.group_id ");
    params.add(SecurityUtils.getCurrentUserId());
    hql.append("and f.open_type in ('O','H') and f.status='01' and t.group_category in (10,11)  ");
    if (psnId != null && psnId.longValue() > 0) {
      hql.append(
          "and exists (select 1 from FRIEND_GROUP_RELATION t where t.friend_group_id=t.group_id and t.psn_id=?) ");
      params.add(psnId);
    }
    if (StringUtils.isNotEmpty(groupCategory) && !"0".equals(groupCategory)) {
      hql.append(" and t.group_category=? ");
      params.add(groupCategory);
    }

    if (StringUtils.isNotEmpty(disciplines)) {
      hql.append(" and exists (select 1 from group_key_disc k where t.group_id=k.group_id and k.disc_codes like ?) ");
      params.add("%" + disciplines + "%");
    }

    if (StringUtils.isNotEmpty(groupName)) {
      String groupNo = StringUtils.isEmpty(groupName) ? "0" : groupName;
      // TSZ_2014-03-20_SCM-4919 判断 输入的检索条件 是否为 数字
      Pattern pattern = Pattern.compile("[0-9]*");
      if (!pattern.matcher(groupNo).matches()) {
        groupNo = "0";
      }

      hql.append(" and (upper(t.group_name) like ? or t.group_no = ?) ");
      params.add("%" + HtmlUtils.htmlEscape(groupName).toUpperCase().trim().replaceAll("%", "\\\\%") + "%");
      params.add(groupNo);
    }

    // 记录数
    countHql.append(hql.toString());
    Long totalCount = super.queryForLong(countHql.toString(), params.toArray());
    page.setTotalCount(totalCount);

    // 查询数据实体
    resultHql.append(hql.toString());
    resultHql.append(orderHql);
    page = this.QueryTable(resultHql.toString(), params.toArray(), page);

    return page;
  }

  /**
   * 热门群组列表分页查询.
   * 
   * @param groupIds
   * @param page
   * @return @
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public Page findHotGroupList(Page page, Long psnId, Integer num) {

    List<Object> params = new ArrayList<Object>();

    String countHql = "select count(groupId)  ";
    String orderHql = " order by s.sumMembers desc,t.createDate desc ";

    StringBuilder hql = new StringBuilder();

    hql.append("select t.groupId from GroupBaseInfo t left join GroupStatistics s ");
    hql.append("where t.groupId=s.groupId ");
    hql.append("and exists (select 1  from GroupFilter b where b.openType in('O','H') and t.groupId = b.groupId ) ");
    hql.append("and exists (select 1 from GroupInvitePsn p where p.groupId=t.groupId and p.isAccept='1') ");
    // 已经确认加入并且不属于自己的群组
    // hql.append(" and isAccept='1' and id.psnId<> ? group by id.groupId )");
    // params.add(psnId);

    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    if (totalCount >= num) {
      page.setTotalCount(num);

    } else {
      page.setTotalCount(totalCount);

    }
    // 查询数据实体
    Query queryResult = super.createQuery(hql + orderHql, params.toArray());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());

    page.setResult(queryResult.list());
    return page;
  }

  /**
   * 更新群组最后访问日期.
   * 
   * @param groupId
   */
  public void updateLastVisiDate(Long groupId) {
    String hql = "update GroupBaseInfo t set t.lastVisitDate=sysDate where t.groupId=? ";
    super.createQuery(hql, groupId).executeUpdate();
  }

  /**
   * 获取我的群组统计信息.
   * 
   * @param psnId
   * @return @
   */
  @SuppressWarnings("unchecked")
  public List<GroupPsnFriendSum> sumMyCategory(Long psnId) {

    StringBuffer hql = new StringBuffer();
    List<Object> params = new ArrayList<Object>();

    hql.append("select count(id.groupId) as categorySum,groupCategory from GroupBaseInfo t ");
    hql.append(
        " where t.groupCategory in (10,11) and exists (select 1 from GroupInvitePsn p where p.groupId=t.groupId and p.psnId=? and p.status='01' and (p.isAccept='1' or p.isAccept is null)) ");
    hql.append("and exists (select 1  from GroupFilter b where b.status='01' and t.groupId = b.groupId ) ");
    params.add(psnId);

    hql.append(" group by groupCategory");

    List<Object[]> list = super.createQuery(hql.toString(), params.toArray()).list();
    List<GroupPsnFriendSum> groupPnsFriendSumList = new ArrayList<GroupPsnFriendSum>();
    for (Object[] object : list) {
      GroupPsnFriendSum groupPnsFriendSum = new GroupPsnFriendSum();
      groupPnsFriendSum.setCategorySum(NumberUtils.toInt(ObjectUtils.toString(object[0])));
      groupPnsFriendSum.setCode(ObjectUtils.toString(object[1]));
      groupPnsFriendSumList.add(groupPnsFriendSum);
    }
    return groupPnsFriendSumList;
  }

  public Map<String, Object> getGroupInviteInfoMap(Long groupId, Long psnId) {
    List<Object> params = new ArrayList<Object>();
    String sql =
        "select b.group_id as group_id,g.is_accept as is_accept ,g.group_role as group_role from group_baseinfo b left join (select * from group_invite_psn where psn_id=? and status='01') g on b.group_id=g.group_id where b.group_id=? ";
    params.add(psnId);
    params.add(groupId);
    List<Map<String, Object>> result = this.queryForList(sql, params.toArray());
    if (result != null) {
      return result.get(0);
    }
    return null;
  }

  // 创建人和管理员
  public Integer sumMyCategoryByAdmin(Long psnId) {
    List<String> roleList = new ArrayList<String>();
    roleList.add("1");
    roleList.add("2");
    return this.sumMyCategoryNum(psnId, roleList);
  }

  public Integer sumMyCategoryByJoin(Long psnId) {
    List<String> roleList = new ArrayList<String>();
    roleList.add("2");
    roleList.add("3");
    return this.sumMyCategoryNum(psnId, roleList);
  }

  // 普通群组成员
  public Integer sumMyCategoryByUnAdmin(Long psnId) {
    List<String> roleList = new ArrayList<String>();
    roleList.add("3");
    return this.sumMyCategoryNum(psnId, roleList);
  }

  /**
   * 获取人员特定角色的群组统计数.
   * 
   * @param psnId
   * @param groupRoleList
   * @return
   */
  private Integer sumMyCategoryNum(Long psnId, List<String> groupRoleList) {
    StringBuffer hql = new StringBuffer();

    hql.append("select count(t.groupId) as categorySum from GroupBaseInfo t ");
    hql.append(
        " where t.groupCategory in (10,11) and exists (select 1 from GroupInvitePsn p where p.groupId=t.groupId and p.psnId=:psnId and p.status='01' and (p.isAccept='1' or p.isAccept is null) and p.groupRole in (:groupRoleList)) ");
    hql.append("and exists (select 1  from GroupFilter b where b.status='01' and t.groupId = b.groupId ) ");
    // 增加群组类型条件限制_MJG_SCM-6305.
    return NumberUtils.toInt(super.createQuery(hql.toString()).setParameter("psnId", psnId)
        .setParameterList("groupRoleList", groupRoleList).uniqueResult() + "");
  }

  @SuppressWarnings("rawtypes")
  public List<Map> findMyGroupLists(Long psnId) {
    String resultSql = "select t.group_id,t.group_name ,t.group_img_url";
    StringBuffer sql = new StringBuffer();
    List<Object> params = new ArrayList<Object>();

    sql.append(" from  GROUP_BASEINFO t,GROUP_INVITE_PSN p ");
    sql.append("where t.group_id=p.group_id and t.group_category in (10,11) ");
    sql.append("and exists (select status from GROUP_FILTER f where f.group_id=t.group_id and f.status='01') ");
    sql.append("and p.psn_id=? and p.status='01' and (p.is_accept=1 or p.is_accept is null) ");
    params.add(psnId);
    String orderSql = "order by t.last_visit_date desc nulls last,t.group_id desc ";
    List<Map> resultMap = super.queryForList(resultSql + sql.toString() + orderSql, params.toArray(), 3, 0);

    return resultMap;
  }

  public String getGroupFundInfo(Long groupId) {
    String hql = "select g.fundingTypes from GroupBaseInfo g where g.groupId =:groupId";
    return (String) super.createQuery(hql).setParameter("groupId", groupId).uniqueResult();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getGroupIdList(Long groupId, Integer size) {
    String hql = "select g.groupId from GroupBaseInfo g where g.groupId >:groupId order by g.groupId asc";
    return super.createQuery(hql).setParameter("groupId", groupId).setMaxResults(size).list();

  }
}
