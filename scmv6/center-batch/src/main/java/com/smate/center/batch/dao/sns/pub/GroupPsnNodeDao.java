package com.smate.center.batch.dao.sns.pub;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.GroupPsnNode;
import com.smate.center.batch.model.sns.pub.GroupPsnNodeSum;
import com.smate.center.batch.model.sns.pub.GroupPsnNodeSumPk;
import com.smate.center.batch.model.sns.pub.KeywordSplit;
import com.smate.core.base.psn.model.profile.PsnDiscipline;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 所有节点上的群组Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupPsnNodeDao extends SnsHibernateDao<GroupPsnNode, Long> {

  /**
   * 我已加入群组列表.
   * 
   * @param currentUserId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<GroupPsnNode> getGroupInvitePsnNodesByPsnId(Long currentUserId) {
    String hql =
        "select new GroupPsnNode(t1.groupId,t1.groupName,t1.nodeId,t1.disciplines,t1.groupCategory,t1.openType,t.groupRole)"
            + " from GroupInvitePsnNode t ,GroupPsnNode t1 where t.id.groupId=t1.groupId and t.nodeId=t1.nodeId and t.id.psnId=? and t.isAccept = 1 "
            + " order by nvl(t.lastVisitDate,?) desc,t1.groupId desc";
    return super.createQuery(hql, new Object[] {currentUserId, new Date(0)}).list();
  }

  /**
   * 根据guid查找群组是否存在.
   * 
   * @param guid
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("rawtypes")
  public GroupPsnNode findGroupPsnNodesByGuid(String guid) {

    String hql = "from GroupPsnNode t where t.isisGuid=?";
    List list = super.createQuery(hql, new Object[] {guid}).list();
    if (list != null && list.size() == 1) {
      return (GroupPsnNode) list.get(0);
    } else {
      return null;
    }
  }

  /**
   * 已确认群组.
   * 
   * @param currentUserId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<GroupPsnNode> getAcceptGroupInvitePsnNodesByPsnId(Long currentUserId) {
    String hql = "select new GroupPsnNode(t1.groupId,t1.groupName,t1.nodeId,t.groupRole)"
        + " from GroupInvitePsnNode t ,GroupPsnNode t1 where t.isAccept=? and t.id.groupId=t1.groupId and t.nodeId=t1.nodeId and t.id.psnId=?"
        + " order by nvl(t.lastVisitDate,?) desc,t1.groupId desc";
    return super.createQuery(hql, new Object[] {"1", currentUserId, new Date(0)}).list();
  }

  public GroupPsnNode findGroupPnsNode(Long groupId) {
    String hql = "from GroupPsnNode where groupId=?";
    return super.findUnique(hql, groupId);
  }

  /**
   * 群组搜索
   * 
   * @param groupCategory
   * @param disciplines
   * @param groupName
   * @param psnId 用户ID
   * @param page
   * @return
   * @throws DaoException
   */
  public Page<GroupPsnNode> findGroupInSearchList(String groupCategory, String disciplines, String groupName,
      Long psnId, Page<GroupPsnNode> page) {

    StringBuilder countHql = new StringBuilder("select count(t.group_id) from ");
    // String orderHql = " order by t.create_date desc, t.group_id desc ";
    String orderHql = " order by group_no_seq desc, t.create_date desc, t.group_id desc ";

    StringBuilder sql_ = new StringBuilder(
        "SELECT t.group_id,t.group_name,t.node_id,t.disciplines,t.group_category,t.open_type,t.disc_codes,t.key_words,t.isis_guid,t.sum_members,t.create_date FROM ");
    // 增加判断条件，过滤已被删除的群组_MJG_SCM-3754.
    // StringBuilder sql_1 = new
    // StringBuilder("SELECT * FROM GROUP_PSN_NODE t WHERE t.open_type in
    // ('O', 'H') ");
    StringBuilder sql_2 =
        new StringBuilder("SELECT distinct t.friend_group_id FROM friend_group_relation t WHERE t.psn_id = ?");
    String groupNo = StringUtils.isEmpty(groupName) ? "0" : groupName;
    // TSZ_2014-03-20_SCM-4919 判断 输入的检索条件 是否为 数字
    Pattern pattern = Pattern.compile("[0-9]*");
    if (!pattern.matcher(groupNo).matches()) {
      groupNo = "0";
    }

    StringBuilder sql_1 = new StringBuilder("SELECT t.*, nvl2(NULLIF(a.group_no, " + groupNo
        + "), 0, 1) group_no_seq FROM GROUP_PSN_NODE t join group_psn a on a.group_id = t.group_id "
        + "WHERE t.open_type in ('O', 'H') ");
    List<Object> params = new ArrayList<Object>();

    if (StringUtils.isNotEmpty(groupCategory) && !"0".equals(groupCategory)) {
      sql_1.append(" and t.group_category=? ");
      params.add(groupCategory);
    }

    if (StringUtils.isNotEmpty(disciplines)) {
      sql_1.append(" and t.disc_codes like ? ");
      params.add("%" + disciplines + "%");
    }

    if (StringUtils.isNotEmpty(groupName)) {
      sql_1.append(
          " and (upper(t.key_words) like ? or exists (select 1 from group_psn b where b.group_id = t.group_id and b.group_no = "
              + groupNo + "))");

      params.add("%" + HtmlUtils.htmlEscape(groupName).toUpperCase().trim().replaceAll("%", "\\\\%") + "%");
    }

    if (psnId != null) {
      sql_.append("(").append(sql_1).append(") t,").append("(").append(sql_2).append(") t_");
      params.add(psnId);
      sql_.append(" WHERE t.group_id = t_.friend_group_id");
    } else {
      sql_.append("(").append(sql_1).append(") t");
    }

    countHql.append("(").append(sql_).append(") t");
    sql_.append(orderHql);

    Object[] values = params.toArray();

    // 记录数
    Query countQuery = super.getSession().createSQLQuery(countHql.toString());
    for (int i = 0; i < values.length; i++) {
      countQuery.setParameter(i, values[i]);
    }
    page.setTotalCount(Long.valueOf(countQuery.uniqueResult().toString()));

    // 查询数据实体
    Query query = super.getSession().createSQLQuery(sql_.toString());
    for (int i = 0; i < values.length; i++) {
      query.setParameter(i, values[i]);
    }
    query.setFirstResult(page.getFirst() - 1);
    query.setMaxResults(page.getPageSize());
    List<Object[]> list = query.list();
    List<GroupPsnNode> groupList = new ArrayList<GroupPsnNode>(list.size());

    for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
      Object[] obj = iterator.next();
      GroupPsnNode group = new GroupPsnNode();
      Long groupId = Long.valueOf(obj[0].toString());
      group.setGroupId(groupId);
      group.setGroupName(obj[1] == null ? null : obj[1].toString());
      group.setNodeId(Integer.valueOf(obj[2].toString()));
      group.setDisciplines(obj[3] == null ? null : obj[3].toString());
      String category = obj[4] == null ? null : obj[4].toString();
      if (StringUtils.isBlank(category)) {
        category = this.getCategoryByGroupId(groupId);
      }
      group.setGroupCategory(category);
      group.setOpenType(obj[5] == null ? null : obj[5].toString());
      group.setDiscCodes(obj[6] == null ? null : obj[6].toString());
      group.setKeyWords(obj[7] == null ? null : obj[7].toString());
      group.setIsisGuid(obj[8] == null ? null : obj[8].toString());
      group.setSumMembers(Integer.valueOf(obj[9].toString()));
      group.setCreateDate((Date) obj[10]);
      groupList.add(group);
    }

    page.setResult(groupList);

    return page;
  }

  /**
   * 获取群组类型.
   * 
   * @param groupId
   * @return
   */
  private String getCategoryByGroupId(Long groupId) {
    String sql = "select groupCategory from GroupPsn where group_id=? ";
    return super.findUnique(sql, groupId);
  }

  /**
   * 热门群组列表分页查询.
   * 
   * @param groupIds
   * @param page
   * @return
   * @throws DaoException
   */
  public Page<GroupPsnNode> findHotGroupList(Page<GroupPsnNode> page, Long psnId, Integer num) {

    List<Object> params = new ArrayList<Object>();

    String countHql = "select count(groupId)  ";
    String orderHql = " order by t.sumMembers desc,t.createDate desc ";

    StringBuilder hql = new StringBuilder();

    hql.append("from GroupPsnNode t where exists (  ");
    hql.append("select id.groupId  from GroupInvitePsnNode b where b.openType in('O','H') and b.isAccept='1'");
    hql.append(" and t.groupId = b.id.groupId )");
    // 已经确认加入并且不属于自己的群组
    // hql.append(" and isAccept='1' and id.psnId<> ? group by id.groupId
    // )");
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
   * 查询最热门的群组，按成员数量倒排.
   * 
   * @param num
   * @return
   * @throws DaoException
   */
  public List<GroupPsnNode> findHotGroupShortList(Integer num) {
    StringBuffer hql = new StringBuffer();
    hql.append("from GroupPsnNode t ");
    hql.append(" where openType in('O','H') order by t.sumMembers desc  ");

    Query query = createQuery(hql.toString());
    query.setMaxResults(num);
    return query.list();
  }

  /**
   * 获得热门群组的大小
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public Long findHotGroupSize(Long psnId) {
    List<Object> params = new ArrayList<Object>();
    String countHql = "select count(groupId)  ";
    StringBuilder hql = new StringBuilder();
    hql.append("from GroupPsn t where t.groupId in (  ");
    hql.append("select id.groupId  from GroupInvitePsnNode  where openType in('O','H') and isAccept='1'");
    hql.append(" group by id.groupId ) and status='01' ");
    // 已经确认加入并且不属于自己的群组
    // hql.append(" and isAccept='1' and id.psnId<> ? group by id.groupId
    // )");
    // params.add(psnId);
    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    return totalCount;

  }

  // 分页查询感兴趣群组
  @SuppressWarnings("unchecked")
  public Page<GroupPsnNode> findInterestedGroupList(Page<GroupPsnNode> page, Long psnId,
      List<PsnDiscipline> disciplines, String groupName) {

    List<Object> params = new ArrayList<Object>();

    String countHql = "select count(t.groupId)  ";
    String orderHql =
        " order by (g.sumMembers+g.sumPubs+g.sumPrjs +g.sumRefs +g.sumFiles +g.sumWorks+g.sumMaterials) desc,t.createDate desc, t.groupId desc ";

    StringBuilder hql = new StringBuilder();

    hql.append("from GroupPsnNode t,GroupPsn g where ");

    // 当前人已加入的群组.
    hql.append(
        "not exists (select 1 from GroupInvitePsnNode  where openType in('O','H') and isAccept='1' and id.psnId = ? and t.groupId=id.groupId ) ");
    hql.append(
        " and g.groupId=t.groupId and (g.sumMembers+g.sumPubs+g.sumPrjs +g.sumRefs +g.sumFiles +g.sumWorks+g.sumMaterials)>1 ");
    // 群组类型是公开或半公开.
    hql.append("and t.openType in('O','H') ");
    params.add(psnId);
    // 关键词相同或好友的群组.
    hql.append("and ");
    if (disciplines.size() > 0) {
      hql.append("(");
    }
    hql.append(
        "exists (select 1 from GroupInvitePsnFriendNode where openType in('O','H') and t.groupId=id.groupId  and id.psnId = ?  group by id.groupId ) ");
    params.add(psnId);

    if (disciplines.size() > 0) {
      hql.append(" or (");
      boolean flag = true;
      for (PsnDiscipline dis : disciplines) {
        if (StringUtils.isNotEmpty(dis.getDisc().getDiscCode())) {
          if (flag) {
            flag = false;
            hql.append(" t.discCodes like ? ");
            params.add("%" + dis.getDisc().getDiscCode() + "%");
          } else {
            hql.append(" or t.discCodes like ? ");
            params.add("%" + dis.getDisc().getDiscCode() + "%");
          }

        }
      }
      hql.append(" ) ");
      hql.append(")");
    }
    if (StringUtils.isNotEmpty(groupName)) {
      hql.append("and upper(t.groupName) like ? ");
      params.add("%" + HtmlUtils.htmlEscape(groupName).toUpperCase().trim().replaceAll("%", "\\\\%") + "%");
    }

    /*
     * else {// 无学科代码，不显示感兴趣群组 hql.append(" and 1=2"); }
     */

    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());

    page.setTotalCount(totalCount);

    String tmp = "select new GroupPsnNode(t.groupId, t.groupName, t.nodeId) ";
    // 查询数据实体
    Query queryResult = super.createQuery(tmp + hql + orderHql, params.toArray());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());

    return page;
  }

  /**
   * 获取推荐群组的群组内容总数限制符合条件的记录TSZ_2014-1-10_SCM-4368.
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  private List<Long> getRecommendIdLimit() {
    String countSql =
        "select g.groupId  from GroupPsn g  where (g.sumMembers+g.sumPubs+g.sumPrjs +g.sumRefs +g.sumFiles +g.sumWorks+g.sumMaterials) >1   ";
    List<Long> idsList = super.createQuery(countSql).list();
    return idsList;
  }

  /**
   * 获取群组ID条件SQL字符串.
   * 
   * @param idsList
   * @return
   */
  private String getGroupIdLimits(List<Long> idsList) {
    StringBuilder hqlStr = new StringBuilder();
    hqlStr.append("and (");
    if (CollectionUtils.isNotEmpty(idsList)) {
      int size = idsList.size();
      int times = size / 999; // 表示分段数
      if (times > 0) {
        for (int i = 0; i <= times; i++) {
          if (i == 0) {
            hqlStr.append("groupId in ( :ids" + i + ") ");
          } else {
            hqlStr.append(" or groupId in ( :ids" + i + ") ");
          }

        }
      } else {
        hqlStr.append("groupId in ( :ids )");
      }
    }
    hqlStr.append(" ) ");
    return hqlStr.toString();
  }

  /**
   * 封装参数ID的检索条件.
   * 
   * @param query
   * @param idsList
   * @return
   */
  private Query filterQueryCon(Query query, List<Long> idsList) {
    // 对取出的idsList进行分段设置注入
    if (CollectionUtils.isNotEmpty(idsList)) {
      int size = idsList.size();
      int times = size / 999;
      if (times > 0) {
        for (int i = 0; i <= times; i++) {
          if (i == 0) {
            if (idsList.size() < 999) {
              query.setParameterList("ids" + i, idsList);
            } else {
              query.setParameterList("ids" + i, idsList.subList(0, 998));
            }
          } else if (i == times) {
            query.setParameterList("ids" + i, idsList.subList(i * 999, idsList.size() - 1));
          } else {
            query.setParameterList("ids" + i, idsList.subList(i * 999, (i + 1) * 999 - 1));
          }
        }
      } else {
        query.setParameterList("ids", idsList);
      }
    }
    return query;
  }

  /**
   * 获得感兴趣群组的大小
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public Long findInterestedGroupSize(Long psnId, List<PsnDiscipline> disciplines) {
    List<Object> params = new ArrayList<Object>();
    String countHql = "select count(groupId)  ";
    StringBuilder hql = new StringBuilder();
    hql.append("from GroupPsn t where t.groupId not in (  ");
    hql.append("select id.groupId  from GroupInvitePsnNode  where openType in('O','H') ");
    hql.append(" and isAccept='1' and id.psnId = ?  group by id.groupId ) and t.status='01' ");

    params.add(psnId);
    if (disciplines.size() > 0) {
      hql.append(" and (");
      boolean flag = true;
      for (PsnDiscipline dis : disciplines) {
        if (StringUtils.isNotEmpty(dis.getDisc().getDiscCode())) {
          if (flag) {
            flag = false;
            hql.append(" t.discCodes like ? ");
            params.add("%" + dis.getDisc().getDiscCode() + "%");
          } else {
            hql.append(" or t.discCodes like ? ");
            params.add("%" + dis.getDisc().getDiscCode() + "%");
          }

        }
      }
      hql.append(" ) ");
    } else {// 无学科代码，不显示感兴趣群组
      hql.append(" and 1=2");
    }
    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    return totalCount;

  }

  /**
   * 封装群组推荐信息列表.
   * 
   * @param queryList
   * @return
   */
  private List<GroupPsnNode> transGroupPsnNodeList(List queryList) {
    List<GroupPsnNode> resultList = new ArrayList<GroupPsnNode>();
    if (CollectionUtils.isNotEmpty(queryList)) {
      DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
      for (int i = 0; i < queryList.size(); i++) {
        Object[] objArr = (Object[]) queryList.get(i);
        GroupPsnNode psnNode = new GroupPsnNode();
        psnNode.setGroupId(Long.valueOf(String.valueOf(objArr[0])));
        psnNode.setGroupName(String.valueOf(objArr[1]));
        psnNode.setNodeId(Integer.valueOf(String.valueOf(objArr[2])));
        psnNode.setSumMembers(Integer.valueOf(String.valueOf(objArr[3])));
        try {
          psnNode.setCreateDate(format.parse(String.valueOf(objArr[4])));
        } catch (ParseException e) {
          e.printStackTrace();
        }
        resultList.add(psnNode);
      }
    }
    return resultList;
  }

  /**
   * 获取推荐群组的ID.
   * 
   * @param keywordList 个人用户的关键词列表.
   * @param psnId 个人用户ID.
   * @return
   */
  @SuppressWarnings({"unchecked"})
  public List<Long> getRecommendGroupId(List<String> keywordList, Long psnId) {
    List<Object> params = new ArrayList<Object>();
    StringBuilder hql = new StringBuilder();

    hql.append("select t.groupId from GroupPsnNode t where ");

    // 当前人已加入的群组.
    hql.append(
        "not exists (select 1 from GroupInvitePsnNode t where openType in('O','H') and isAccept='1' and id.psnId = ? and t.groupId=id.groupId ) ");
    // 群组类型是公开或半公开.
    hql.append("and t.openType in('O','H') ");
    params.add(psnId);
    // 关键词相同或好友的群组.
    String friendGroupHqlCon =
        "exists (select 1 from GroupInvitePsnFriendNode where openType in('O','H') and t.groupId=id.groupId and id.psnId = ?  group by id.groupId ) ";
    params.add(psnId);
    if (CollectionUtils.isNotEmpty(keywordList)) {
      String sameKeyHqlCon = "t.keyWords in (" + transKeyListToStr(keywordList) + ")";
      hql.append("and (" + friendGroupHqlCon + " or " + sameKeyHqlCon + ") ");
    } else {
      hql.append("and " + friendGroupHqlCon);
    }
    return super.createQuery(hql.toString(), params.toArray()).list();
  }

  /**
   * 转换关键词列表为逗号分隔的字符串.
   * 
   * @param keywordList
   * @return
   */
  private String transKeyListToStr(List<String> keywordList) {
    StringBuilder result = new StringBuilder();
    if (CollectionUtils.isNotEmpty(keywordList)) {
      for (int i = 0; i < keywordList.size(); i++) {
        result.append("'" + keywordList.get(i) + "'");
        if (i < (keywordList.size() - 1)) {
          result.append(",");
        }
      }
    }
    return result.toString();
  }

  /**
   * 查找群组统计信息<因目前群组类型只有教学和科研>_限制了获取统计信息的群组类型_MJG_SCM-5237.
   * 
   * @return
   * @throws DaoException
   */
  public List<GroupPsnNodeSum> findGroupCategorySumList() {
    StringBuffer hql = new StringBuffer();
    hql.append("select count(groupId) as categorySum,groupCategory from GroupPsnNode t ");
    hql.append(" where openType in('O','H') and groupCategory in (10,11) group by groupCategory ");

    List<Object[]> list = super.createQuery(hql.toString()).list();
    List<GroupPsnNodeSum> groupAllSumList = new ArrayList<>();
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
   * 根据成果关键词推荐最多120个群组
   * 
   * @param keyWords 成果关键词
   * @return
   * @throws DaoException
   */
  public List<GroupPsnNode> findGroupBykeyWorks(List<KeywordSplit> keyWords) {
    StringBuilder hql = new StringBuilder(" FROM GroupPsnNode t WHERE t.openType in ('O', 'H')");
    StringBuilder orderHql = new StringBuilder("");
    List<Object> params = new ArrayList<Object>();
    if (keyWords != null && keyWords.size() > 0) {
      hql.append(" and (1=0 ");
      orderHql.append(" order by case");
      for (int i = 0; i < keyWords.size() && i < 30; i++) {
        KeywordSplit key = keyWords.get(i);
        if (key.getKwtxt() != null) {
          hql.append(" or lower(t.keyWords) like ? ");
          params.add("%" + key.getKwtxt() + "%");
          // orderHql.append(" when t.keyWords like '%" +
          // key.getKwtxt() + "%' then " + i);
        }
      }
      for (int i = 0; i < keyWords.size() && i < 10; i++) {
        KeywordSplit key = keyWords.get(i);
        if (key.getKwtxt() != null) {
          orderHql.append(" when lower(t.keyWords) like ? ");
          params.add("%" + key.getKwtxt() + "%");
          orderHql.append(" then " + i);
        }
      }
      hql.append(")");
      orderHql.append(" else t.nodeId end");
    }
    hql.append(orderHql);
    Query queryResult = super.createQuery(hql.toString(), params.toArray());
    queryResult.setMaxResults(120);
    return queryResult.list();
  }

}
