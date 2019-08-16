package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.ConstDictionary;
import com.smate.center.batch.model.sns.pub.GroupInvitePsnNode;
import com.smate.center.batch.model.sns.pub.GroupInvitePsnNodePk;
import com.smate.center.batch.model.sns.pub.GroupPsnFriendSum;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 群组与人员的邀请关系表(当前人所有节点上加入的群组)Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupInvitePsnNodeDao extends SnsHibernateDao<GroupInvitePsnNode, Long> {
  @Autowired
  private ConstDictionaryCbDao constDictionaryDao;

  public GroupInvitePsnNode findGroupInvitePsnNode(GroupInvitePsnNodePk id) {
    String hql = "from GroupInvitePsnNode where id.groupId=? and id.psnId=?";
    return super.findUnique(hql, id.getGroupId(), id.getPsnId());
  }

  public List<GroupInvitePsnNode> findGroupInvitePsnNodeList(Long groupId) {
    String hql = "from GroupInvitePsnNode where id.groupId=?";
    Query query = createQuery(hql, groupId);
    return query.list();
  }

  /**
   * PSN_ID加入的群组（PSN_ID对应的节点）.
   * 
   * @param psnId
   * @return
   */
  public List<GroupInvitePsnNode> findMyGroupInvitePsnNodeList(Long psnId) throws DaoException {
    String hql = "from GroupInvitePsnNode where id.psnId=? and isAccept='1'";
    Query query = createQuery(hql, psnId);
    return query.list();
  }

  /**
   * PSN_ID创建的群组（PSN_ID对应的节点）.
   * 
   * @param psnId
   * @return
   */
  public List<GroupInvitePsnNode> findMyCreateGroupInvitePsnNodeList(Long psnId) throws DaoException {
    String hql = "from GroupInvitePsnNode where ownerPsnId=? and isAccept='1' and groupRole=1";
    Query query = createQuery(hql, psnId);
    return query.list();
  }

  /**
   * PSN_ID被要求加入加入的群组（PSN_ID对应的节点）.
   * 
   * @param psnId
   * @return
   */
  public List<GroupInvitePsnNode> findMyAddGroupInvitePsnNodeList(Long psnId) throws DaoException {
    String hql = "from GroupInvitePsnNode where id.psnId=? and ownerPsnId<>? and isAccept='1'  and groupRole in(2,3)";
    Query query = createQuery(hql, new Object[] {psnId, psnId});
    return query.list();
  }

  /**
   * PSN_ID加入的群组groupId（PSN_ID对应的节点）.
   * 
   * @param psnId
   * @return
   */
  public List<Long> findMyGroupPsnList(Long psnId) throws DaoException {
    String hql = "select id.groupId from GroupInvitePsnNode where id.psnId=? and (isAccept='1' or isAccept is null)";
    Query query = createQuery(hql, psnId);
    return query.list();
  }

  public List<Long> findMyValidGroupListByPsnId(Long psnId) throws DaoException {
    String hql = "select id.groupId from GroupInvitePsnNode where id.psnId=? and isAccept='1'";
    Query query = createQuery(hql, psnId);
    return query.list();
  }

  public List<GroupInvitePsnNode> findMyGroupList(Long psnId) throws DaoException {
    String sql =
        "select * from GROUP_INVITE_PSN_NODE where PSN_ID=? and IS_ACCEPT='1' order by last_Visit_Date desc nulls last ,GROUP_ID desc";
    List result = super.queryForList(sql, new Object[] {psnId});

    List<GroupInvitePsnNode> objList = new ArrayList<GroupInvitePsnNode>();
    // Map转换为对象
    for (int i = 0; i < result.size(); i++) {
      Map map = (Map) result.get(i);

      GroupInvitePsnNode g = new GroupInvitePsnNode();
      GroupInvitePsnNodePk pk = new GroupInvitePsnNodePk();
      pk.setGroupId(Long.valueOf(ObjectUtils.toString(map.get("GROUP_ID"))));
      pk.setPsnId(Long.valueOf(ObjectUtils.toString(map.get("PSN_ID"))));
      g.setId(pk);
      g.setNodeId(Integer.valueOf(ObjectUtils.toString(map.get("NODE_ID"))));
      g.setGroupImgUrl(ObjectUtils.toString(map.get("GROUP_IMG_URL")));
      g.setGroupName(ObjectUtils.toString(map.get("GROUP_NAME")));
      g.setFirstLetter(ObjectUtils.toString(map.get("GROUP_NAME_FIRST_LETTER")));
      g.setGroupDescription(ObjectUtils.toString(map.get("GROUP_DESCRIPTION")));
      g.setGroupCategory(ObjectUtils.toString(map.get("GROUP_CATEGORY")));
      g.setIsAccept(ObjectUtils.toString(map.get("IS_ACCEPT")));
      g.setGroupRole(ObjectUtils.toString(map.get("GROUP_ROLE")));
      g.setSumToMembers(Integer.valueOf(ObjectUtils.toString(map.get("SUM_TO_MEMBERS"))));
      Object lastVisitDate = map.get("LAST_VISIT_DATE");
      g.setLastVisitDate(lastVisitDate == null ? null : (Date) lastVisitDate);

      objList.add(g);
    }
    return objList;
  }

  /**
   * 按查询关键字及群组名称首字母查询群组.
   * 
   * @param groupCategory
   * @param ownerType
   * @param psnId
   * @param page
   * @param searchKey
   * @param local
   * @return
   * @throws DaoException
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public Page<GroupInvitePsnNode> findMyGroupList(String groupCategory, String ownerType, Long psnId, Page page,
      String searchKey, Locale local) throws DaoException {

    List<Object> params = new ArrayList<Object>();

    // SCM-2212_MJG_2013-04-09.
    StringBuffer sql = new StringBuffer();

    sql.append(
        " SELECT t.GROUP_ID,t.PSN_ID,t.NODE_ID,t.GROUP_IMG_URL,t.GROUP_NAME,t.GROUP_NAME_FIRST_LETTER,t.GROUP_DESCRIPTION,t.GROUP_CATEGORY,t.IS_ACCEPT,t.GROUP_ROLE,t.SUM_TO_MEMBERS,t.LAST_VISIT_DATE");
    sql.append(" FROM GROUP_INVITE_PSN_NODE t");
    sql.append(" WHERE t.PSN_ID = ? AND (t.IS_ACCEPT = '1' or t.IS_ACCEPT IS NULL) ");
    params.add(psnId);
    if (StringUtils.isNotEmpty(groupCategory)) {
      sql.append(" and t.GROUP_CATEGORY=? ");
      params.add(groupCategory);
    }
    if (StringUtils.isNotEmpty(ownerType)) {
      if ("admin".equals(ownerType)) {// 管理和创建
        sql.append(" and t.GROUP_ROLE in ('1','2') ");
      } else if ("join".equals(ownerType)) {
        sql.append(" and t.GROUP_ROLE='3'");
      }
    }
    if (StringUtils.isNotEmpty(searchKey)) {
      sql.append(" and t.GROUP_NAME like ? ");
      params.add("%" + searchKey + "%");
    }
    sql.append(" ORDER BY t.LAST_VISIT_DATE DESC NULLS LAST,t.GROUP_ID DESC ");

    Object[] objects = params.toArray();

    page = this.QueryTable(sql.toString(), objects, page);

    List<GroupInvitePsnNode> objList = new ArrayList<GroupInvitePsnNode>();

    // Map转换为对象
    for (int i = 0; i < page.getResult().size(); i++) {
      Map map = (Map) page.getResult().get(i);

      GroupInvitePsnNode g = new GroupInvitePsnNode();
      GroupInvitePsnNodePk pk = new GroupInvitePsnNodePk();
      Long groupId = Long.valueOf(ObjectUtils.toString(map.get("GROUP_ID")));
      pk.setGroupId(groupId);
      pk.setPsnId(Long.valueOf(ObjectUtils.toString(map.get("PSN_ID"))));
      g.setId(pk);
      g.setNodeId(Integer.valueOf(ObjectUtils.toString(map.get("NODE_ID"))));
      g.setGroupImgUrl(ObjectUtils.toString(map.get("GROUP_IMG_URL")));
      g.setGroupName(ObjectUtils.toString(map.get("GROUP_NAME")));
      g.setFirstLetter(ObjectUtils.toString(map.get("GROUP_NAME_FIRST_LETTER")));
      g.setGroupDescription(ObjectUtils.toString(map.get("GROUP_DESCRIPTION")));
      String category = ObjectUtils.toString(map.get("GROUP_CATEGORY"));
      // 如果表中群组类型值为空，则从群组主表中获取群组类型值_MJG_SCM-3757.
      if (StringUtils.isBlank(category)) {
        category = this.getCategoryByGroupId(groupId);
      }
      g.setGroupCategory(category);
      g.setIsAccept(ObjectUtils.toString(map.get("IS_ACCEPT")));
      g.setGroupRole(ObjectUtils.toString(map.get("GROUP_ROLE")));
      g.setSumToMembers(Integer.valueOf(ObjectUtils.toString(map.get("SUM_TO_MEMBERS"))));
      Object lastVisitDate = map.get("LAST_VISIT_DATE");
      g.setLastVisitDate(lastVisitDate == null ? null : (Date) lastVisitDate);

      // 填充群组类别名称
      ConstDictionary constDictionary = constDictionaryDao.findConstByCategoryAndCode("group", g.getGroupCategory());
      if (constDictionary != null) {
        g.setCategoryName(local.equals(Locale.US) ? constDictionary.getEnUsName() : constDictionary.getZhCnName());
      }
      objList.add(g);
    }

    page.setResult(objList);

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

  public boolean isHaveGroup(Long psnId) {
    StringBuilder hql = new StringBuilder(
        "select count(id.groupId) from GroupInvitePsnNode t where id.psnId=? and (isAccept='1' or isAccept is null)");
    Long totalCount = super.findUnique(hql.toString(), psnId);
    return totalCount > 0 ? true : false;
  }

  @SuppressWarnings("rawtypes")
  public Page<GroupInvitePsnNode> findGroupListForBpo(Map searchMap, Page<GroupInvitePsnNode> page)
      throws DaoException {

    String countHql = "select count(id.groupId) ";
    String orderHql = "order by id.groupId desc ";

    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupInvitePsnNode t ");

    List<Object> params = new ArrayList<Object>();
    hql.append(" where  (isAccept='1' or isAccept is null)  and t.nodeId=?  ");
    params.add(searchMap.get("nodeId"));

    if (StringUtils.isNotEmpty(MapUtils.getString(searchMap, "searchKey"))) {

      hql.append(" and ( groupName like ? or groupDescription like ? ) ");
      params.add("%" + MapUtils.getObject(searchMap, "searchKey") + "%");
      params.add("%" + MapUtils.getObject(searchMap, "searchKey") + "%");

    }

    if (MapUtils.getObject(searchMap, "fromDate") != null || MapUtils.getObject(searchMap, "toDate") != null) {

      hql.append(
          " and id.groupId in ( select  groupId from GroupPsn gp where gp.status='01' and (gp.updateDate between ?  and ? or gp.createDate between ?  and ? )) ");
      params.add(MapUtils.getObject(searchMap, "fromDate"));
      params.add(MapUtils.getObject(searchMap, "toDate"));
      params.add(MapUtils.getObject(searchMap, "fromDate"));
      params.add(MapUtils.getObject(searchMap, "toDate"));

    }

    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);

    // 查询数据实体
    Query queryResult = super.createQuery(hql + orderHql, params.toArray());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
    return page;
  }

  public List<GroupPsnFriendSum> sumMyCategory(Long psnId) throws DaoException {

    StringBuffer hql = new StringBuffer();
    List<Object> params = new ArrayList<Object>();

    hql.append("select count(id.groupId) as categorySum,groupCategory from GroupInvitePsnNode ");
    hql.append(" where id.psnId=? and (isAccept='1' or isAccept is null) ");
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

  public Integer sumMyCategoryByCreate(Long psnId) throws DaoException {

    StringBuffer hql = new StringBuffer();

    hql.append("select count(id.groupId) as categorySum from GroupInvitePsnNode ");
    hql.append(" where id.psnId=? and groupRole='1' and (isAccept='1' or isAccept is null) ");

    return NumberUtils.toInt(this.findUnique(hql.toString(), psnId) + "");
  }

  // 创建人和管理员
  public Integer sumMyCategoryByAdmin(Long psnId) throws DaoException {

    StringBuffer hql = new StringBuffer();

    hql.append("select count(id.groupId) as categorySum from GroupInvitePsnNode ");
    hql.append(" where id.psnId=? and groupRole in ('1','2') and (isAccept='1' or isAccept is null) ");

    return NumberUtils.toInt(this.findUnique(hql.toString(), psnId) + "");
  }

  public Integer sumMyCategoryByJoin(Long psnId) throws DaoException {

    StringBuffer hql = new StringBuffer();

    hql.append("select count(id.groupId) as categorySum from GroupInvitePsnNode ");
    hql.append(" where id.psnId=? and groupRole in('2','3') and (isAccept='1' or isAccept is null) ");

    return NumberUtils.toInt(this.findUnique(hql.toString(), psnId) + "");
  }

  // 普通群组成员
  public Integer sumMyCategoryByUnAdmin(Long psnId) throws DaoException {

    StringBuffer hql = new StringBuffer();

    hql.append("select count(id.groupId) as categorySum from GroupInvitePsnNode ");
    hql.append(" where id.psnId=? and groupRole='3' and (isAccept='1' or isAccept is null) ");

    return NumberUtils.toInt(this.findUnique(hql.toString(), psnId) + "");
  }

  // 统计我创建或加入的群组总数
  public Long sumMyGroupCount(Long psnId) throws DaoException {
    StringBuffer hql = new StringBuffer();

    hql.append("select count(id.groupId) as categorySum from GroupInvitePsnNode ");
    hql.append(" where id.psnId=? and isAccept='1' ");
    return findUnique(hql.toString(), psnId);
  }

  /**
   * 我的群组列表.
   * 
   * @param psnId
   * @param num
   * @return
   * @throws DaoException
   */
  public List<GroupInvitePsnNode> findMyGroupInvitePsnNodeList(Long psnId, Integer num) throws DaoException {
    String hql = "from GroupInvitePsnNode where id.psnId=? and isAccept='1' order by id.groupId desc";
    Query query = createQuery(hql, psnId);
    if (num != 0) {
      query.setMaxResults(num);
    }
    return query.list();
  }

  /**
   * 查询我的公开和半公开群组列表.
   * 
   * @param psnId
   * @param num
   * @return
   * @throws DaoException
   */
  public List<GroupInvitePsnNode> findMyGroupSearchList(Long psnId, Integer num) throws DaoException {
    String hql =
        "from GroupInvitePsnNode where id.psnId=? and isAccept='1' and openType in('O','H') order by createDate desc";
    Query query = createQuery(hql, psnId);
    if (num != 0) {
      query.setMaxResults(num);
    }
    return query.list();
  }

  /**
   * 统计我创建或加入的公开和半公开群组总数.
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  public Long sumMyGroupSearchCount(Long psnId) throws DaoException {
    StringBuffer hql = new StringBuffer();

    hql.append("select count(id.groupId) as categorySum from GroupInvitePsnNode ");
    hql.append(" where id.psnId=? and isAccept='1' and openType in('O','H') ");
    return findUnique(hql.toString(), psnId);
  }

  /**
   * 分页查询我的公开和半公开群组列表.
   * 
   * @param psnId
   * @param num
   * @return
   * @throws DaoException
   */
  public Page<GroupInvitePsnNode> findMyGroupSearchPage(Page<GroupInvitePsnNode> page, Long psnId) throws DaoException {
    String hql =
        "from GroupInvitePsnNode t where t.id.psnId=? and t.isAccept='1' and openType in('O','H') order by t.id.groupId desc";
    return super.findPage(page, hql, psnId);
  }

  /**
   * 分页查询某个人的群组列表.
   * 
   * @param psnId
   * @param num
   * @return
   * @throws DaoException
   */
  public Page<GroupInvitePsnNode> findGroupInvitePsnNodePage(Page<GroupInvitePsnNode> page, Long psnId)
      throws DaoException {
    String hql = "from GroupInvitePsnNode t where t.id.psnId=? and t.isAccept='1' order by t.id.groupId desc";
    return super.findPage(page, hql, psnId);
  }

  /**
   * 好友查询我的群组列表.
   * 
   * @param psnId
   * @param num
   * @return
   * @throws DaoException
   */
  public List<GroupInvitePsnNode> findMyGroupInvitePsnNodeListByFriend(Long psnId, Integer num) throws DaoException {
    String hql =
        "from GroupInvitePsnNode where id.psnId=? and openType in('O','H') and isAccept='1' order by id.groupId desc";
    Query query = createQuery(hql, psnId);
    if (num != 0) {
      query.setMaxResults(num);
    }
    return query.list();
  }

  /**
   * 好友分页查询我的群组列表.
   * 
   * @param psnId
   * @param num
   * @return
   * @throws DaoException
   */
  public Page<GroupInvitePsnNode> findGroupInvitePsnNodePageByFriend(Page<GroupInvitePsnNode> page, Long psnId)
      throws DaoException {
    String hql =
        "from GroupInvitePsnNode t where t.id.psnId=? and t.openType in('O','H') and t.isAccept='1' order by t.id.groupId desc";
    return super.findPage(page, hql, psnId);
  }

  // /**
  // * 热门群组.
  // *
  // * @param num
  // * @return
  // * @throws DaoException
  // */
  // @SuppressWarnings("unchecked")
  // public List<GroupInvitePsnNode> findHotGroupInvitePsnNodeList(Integer
  // num) throws DaoException {
  // StringBuffer hql = new StringBuffer();
  // hql.append("select t.groupId,t.sumMembers,t.groupName,g.nodeId from GroupPsn t,GroupPsnNode g
  // where g.groupId=t.groupId and t.groupId in ( ");
  // hql.append("select id.groupId from GroupInvitePsnNode where openType in('O','H') and
  // isAccept='1') and t.status='01' order by t.sumMembers desc ");
  //
  // Query query = createQuery(hql.toString());
  // query.setMaxResults(num);
  // List<Object[]> list = query.list();
  // List<GroupInvitePsnNode> result = new ArrayList<GroupInvitePsnNode>();
  // for (Object[] objs : list) {
  // GroupInvitePsnNode groupInvitePsnNode = new GroupInvitePsnNode();
  // GroupInvitePsnNodePk groupInvitePsnNodePk = new GroupInvitePsnNodePk();
  // groupInvitePsnNodePk.setGroupId(NumberUtils.toLong(ObjectUtils.toString(objs[0])));
  // groupInvitePsnNode.setId(groupInvitePsnNodePk);
  // groupInvitePsnNode.setSumMembers(NumberUtils.toInt(ObjectUtils.toString(objs[1])));
  // groupInvitePsnNode.setGroupName(ObjectUtils.toString(objs[2]));
  // groupInvitePsnNode.setNodeId(NumberUtils.toInt(ObjectUtils.toString(objs[3])));
  // result.add(groupInvitePsnNode);
  // }
  // return result;
  // }

  /**
   * 当用户，点击接受群主邀请的时候，设置确认状态为1
   * 
   * @param groupId
   */
  public void updateGroupInvitePsnNode(Long groupId, Long psnId) {
    String hql = "UPDATE GroupInvitePsnNode t SET t.isAccept=? WHERE t.id.groupId=? AND t.id.psnId=?";
    super.createQuery(hql, new Object[] {"1", groupId, psnId}).executeUpdate();
  }

  // change group owner
  public void changeGroupOwner(Long ownerPsnId, Long groupId) {
    String hql = "update GroupInvitePsnNode t set t.ownerPsnId = ? where t.id.groupId = ? ";
    this.createQuery(hql, ownerPsnId, groupId).executeUpdate();
    hql = "update GroupInvitePsnNode t set t.groupRole = 3 where t.id.groupId = ? and t.groupRole = 1";
    this.createQuery(hql, groupId).executeUpdate();
    hql = "update GroupInvitePsnNode t set t.groupRole = 1 where t.id.psnId = t.ownerPsnId  and t.id.groupId = ? ";
    this.createQuery(hql, groupId).executeUpdate();
  }

  /**
   * 群组访问时间更新
   * 
   * @param groupId
   * @param psnId
   * @param lastVisitDate
   */
  public void updateGroupVisitDate(Long groupId, Long psnId, Date lastVisitDate) {
    String hql = "update GroupInvitePsnNode t set t.lastVisitDate = ? where t.id.psnId = ? and t.id.groupId = ? ";
    this.createQuery(hql, lastVisitDate, psnId, groupId).executeUpdate();
  }

  /**
   * 删除群组冗余记录.
   * 
   * @param groupId
   * @param psnId
   */
  public void deleteGroupInvitePsnNode(Long groupId, Long psnId) {
    String hql = "delete GroupInvitePsnNode t where t.id.psnId = ? and t.id.groupId = ? ";
    this.createQuery(hql, psnId, groupId).executeUpdate();
  }

  // ==============人员合并 start============
  @SuppressWarnings("unchecked")
  public List<GroupInvitePsnNode> getListByPsnId(Long delPsnId, Long groupId) throws DaoException {
    String hql = "from GroupInvitePsnNode where (id.psnId=? or ownerPsnId=?) and id.groupId=?";
    return super.createQuery(hql, delPsnId, delPsnId, groupId).list();
  }

  public void updateMembersByGroupId(Long groupId, Integer sumToMembers, Integer sumMembers) throws DaoException {
    super.createQuery("update GroupInvitePsnNode t set t.sumToMembers = ? , t.sumMembers = ? where t.id.groupId = ?",
        sumToMembers, sumMembers, groupId);
  }

  // ==============人员合并 end============

  @SuppressWarnings("unchecked")
  public List<Long> findGroupMemberPsnIdByGroupId(Long groupId) throws DaoException {
    String hql = "select psnId from GroupInvitePsnNode where t.id.groupId = ? and isAccept='1'";
    Query query = createQuery(hql, groupId);
    return query.list();
  }
}
