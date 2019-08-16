package com.smate.center.batch.dao.sns.prj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.batch.model.sns.prj.GroupInvitePsn;
import com.smate.center.batch.model.sns.prj.GroupMember;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.MapBuilder;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 群组与人员的邀请关系表Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupInvitePsnDao extends SnsHibernateDao<GroupInvitePsn, Long> {

  /**
   * 
   * @param groupId 群组ID
   * @param page 分页信息
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public Page<GroupInvitePsn> findGroupInvitePsnList(Long groupId, Page<GroupInvitePsn> page, String searchKey)
      throws Exception {

    String countHql = "select count(t.invitePsnId) ";
    String orderHql = "order by t.groupRole asc,t.createDate desc,t.invitePsnId desc ";

    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupInvitePsn t ");

    List<Object> params = new ArrayList<Object>();
    hql.append(" where t.groupId = ? and t.status = '01' and t.isAccept='1' ");
    params.add(groupId);
    // 按照关键字查询，即页面左边的检索框不为空,则根据输入的名称进行查询.
    if (StringUtils.isNotEmpty(searchKey)) {
      // 对输入的关键字进行编码转换.
      searchKey = HtmlUtils.htmlEscape(searchKey).toUpperCase().trim().replaceAll("%", "\\\\%");
      hql.append("and (upper(t.psnName) like ? or upper(t.psnLastName || ' ' || t.psnFirstName) like ?) ");
      // 如果查询参数中包含中文字符时不区分大小写.
      boolean flag = searchKey.length() == searchKey.getBytes().length;
      String psnNameConditions = flag ? searchKey.trim() : searchKey.trim().toUpperCase();
      params.add("%" + psnNameConditions + "%");
      params.add("%" + psnNameConditions + "%");
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

  public List<Long> findGroupPnsIds(Long groupId, Long userId) throws Exception {
    String hql =
        "select psnId from GroupInvitePsn where groupId = ? and status = '01' and isAccept='1' and psnId not in(?)";
    return find(hql, groupId, userId);
  }

  @SuppressWarnings("unchecked")
  public Page<GroupMember> findGroupMemberPsnIds(Page<GroupMember> page, Long groupId, Long userId) throws Exception {
    String hql =
        "select psnId from GroupInvitePsn where groupId = ? and status = '01' and isAccept='1' and psnId not in(?)";
    Query q = createQuery(hql, groupId, userId);
    List<Long> list = q.list();
    page.setTotalCount(list.size());
    q.setFirstResult(page.getFirst() - 1);
    q.setMaxResults(page.getPageSize());
    List<GroupMember> groupMemberList = new ArrayList<GroupMember>();
    for (Long psnId : list) {
      GroupMember groupMember = new GroupMember();
      groupMember.setPsnId(psnId);
      groupMemberList.add(groupMember);
    }
    page.setResult(groupMemberList);
    return page;
  }

  @SuppressWarnings("unchecked")
  public List<GroupInvitePsn> findGroupMemberByAll(Long groupId) throws Exception {

    String hql = "from GroupInvitePsn where groupId = ? and status = '01' ";
    Query query = createQuery(hql, groupId);
    return query.list();
  }

  /**
   * 获取所有群组人员，包括被删除的人员.
   * 
   * @param groupId
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<GroupInvitePsn> findAllGroupPsn(Long groupId) throws Exception {
    String hql = "from GroupInvitePsn where groupId = ? ";
    Query query = createQuery(hql, groupId);
    return query.list();
  }

  @SuppressWarnings("unchecked")
  public List<GroupInvitePsn> findGroupMemberByGroupId(Long groupId) throws Exception {

    String hql = "from GroupInvitePsn where groupId = ? and status = '01' and isAccept='1' order by createDate desc";
    Query query = createQuery(hql, groupId);
    return query.list();
  }

  @SuppressWarnings("unchecked")
  public List<GroupInvitePsn> findGroupMemberByGroupId(Long groupId, List<Long> excludePsnIds) throws Exception {
    String hql =
        "from GroupInvitePsn where groupId=:groupId and status=:status and isAccept=:isAccept and psnId not in(:pids) order by createDate desc";
    Query query = createQuery(hql).setParameter("groupId", groupId).setParameter("status", "01")
        .setParameter("isAccept", "1").setParameterList("pids", excludePsnIds);
    return query.list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findGroupMemberPsnIdByGroupId(Long groupId) throws Exception {
    String hql = "select psnId from GroupInvitePsn where groupId = ? and status = '01' and isAccept='1'";
    Query query = createQuery(hql, groupId);
    return query.list();
  }

  /**
   * 获取群组管理员的人员ID.
   * 
   * @param groupId
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<Long> findAdminPsnIdByGroupId(Long groupId) throws Exception {
    String hql =
        "select psnId  from GroupInvitePsn where groupId = ? and isAccept='1' and status='01' and groupRole in('1','2') ";
    Query query = createQuery(hql, groupId);
    return query.list();
  }

  @SuppressWarnings("unchecked")
  public List<GroupInvitePsn> findGroupMemberByPsnIds(String invitePsnIds) throws Exception {

    String hql = "from GroupInvitePsn where invitePsnId in(" + invitePsnIds
        + ") and status = '01' and isAccept='1' order by createDate desc";
    Query query = createQuery(hql);
    return query.list();
  }

  /**
   * 
   * @param groupId 群组ID
   * @param page 分页信息
   * @param searchKey 页面左边检索的关键字.
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public Page<GroupInvitePsn> findGroupMemberPsnIdNotYet(Long groupId, Page<GroupInvitePsn> page, String searchKey)
      throws Exception {

    String countHql = "select count(t.invitePsnId) ";
    String orderHql = "order by createDate desc ";

    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupInvitePsn t ");

    List<Object> params = new ArrayList<Object>();
    hql.append(" where groupId = ? and status = '01' and isAccept is null ");
    params.add(groupId);

    // 按照关键字查询，即页面左边的检索框不为空.
    if (StringUtils.isNotEmpty(searchKey)) {
      searchKey = HtmlUtils.htmlEscape(searchKey).toUpperCase().trim().replaceAll("%", "\\\\%");
      hql.append(" and ");
      hql.append(" ( ");
      hql.append(" upper(t.psnName) like ?");// 组成员中文名
      params.add("%" + searchKey + "%");
      hql.append(" ) ");
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

  public GroupInvitePsn findGroupMember(Long groupId, Long psnId) throws Exception {

    String hql = "from GroupInvitePsn where groupId = ? and psnId = ?";
    List<GroupInvitePsn> list = super.createQuery(hql, groupId, psnId).list();
    if (list != null && list.size() > 0)
      return list.get(0);
    return null;

  }

  public GroupInvitePsn findGroupInvitePsn(Long groupId, Long psnId) throws Exception {
    String hql = "from GroupInvitePsn where groupId = ? and psnId=? and status = '01' and isAccept='1'";
    return this.findUnique(hql, groupId, psnId);
  }

  public GroupInvitePsn findGroupInvitePsnReady(Long groupId, Long psnId) throws Exception {
    String hql = "from GroupInvitePsn where groupId = ? and psnId=? and status = '01' and isAccept is null";
    return this.findUnique(hql, groupId, psnId);
  }

  public GroupInvitePsn findGroupInvitePsn(Long invitePsnId) throws Exception {
    String hql = "from GroupInvitePsn where invitePsnId = ?";
    return this.findUnique(hql, invitePsnId);
  }

  // change group owner
  public void changeGroupOwner(Long ownerPsnId, Long groupId) {

    String hql = "update GroupInvitePsn t set t.createPsnId = ? where t.groupId = ? ";
    this.createQuery(hql, ownerPsnId, groupId).executeUpdate();
    hql = "update GroupInvitePsn t set t.groupRole = 3 where t.groupId = ? and t.groupRole = 1";
    this.createQuery(hql, groupId).executeUpdate();
    hql = "update GroupInvitePsn t set t.groupRole = 1 where t.createPsnId = t.psnId  and t.groupId = ? ";
    this.createQuery(hql, groupId).executeUpdate();
  }

  public Integer findGroupMemberCount(Long groupId) throws Exception {

    String hql = "select count(invitePsnId) from GroupInvitePsn where groupId = '" + groupId
        + "' and isAccept = 1 and status = '01' ";
    Long sum = super.findUnique(hql);
    // Long sum = super.findUnique(hql, groupId);
    return sum.intValue();
  }

  public Integer findGroupToMemberCount(Long groupId) throws Exception {

    String hql = "select count(*) from GroupInvitePsn where groupId = ? and status = '01' and isAccept is null";

    Long sum = super.findUnique(hql, groupId);
    return sum.intValue();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findGroupIdByFriendsPsnIds(String psnIds) throws Exception {

    String hql = "select distinct groupId from GroupInvitePsn ";
    hql += " where psnId in(" + psnIds + ") and status = '01' and isAccept='1'";
    Query query = createQuery(hql);
    return query.list();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> findGroupMembersInfo(Long groupId) throws Exception {

    String hql = "select psnId,psnName as name from GroupInvitePsn where groupId = ? and isAccept='1' and status='01'";
    Query query = createQuery(hql.toString(), groupId);
    List<Object[]> list = query.list();
    List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
    for (Object[] objs : list) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("psnId", objs[0]);
      map.put("name", objs[1]);
      memberList.add(map);
    }
    return memberList;
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> findGroupAdminsInfo(Long groupId) throws Exception {

    String hql =
        "select psnId,psnName as name,psnFirstName as firstName,psnLastName as lastName,email from GroupInvitePsn where groupId = ? and isAccept='1' and status='01' and groupRole in('1','2')";
    Query query = createQuery(hql.toString(), groupId);
    List<Object[]> list = query.list();
    List<Map<String, Object>> memberList = new ArrayList<Map<String, Object>>();
    for (Object[] objs : list) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("psnId", objs[0]);
      map.put("name", objs[1]);
      map.put("enName", ObjectUtils.toString(objs[2]) + " " + ObjectUtils.toString(objs[3]));
      map.put("email", objs[4]);
      memberList.add(map);
    }
    return memberList;
  }

  @SuppressWarnings("unchecked")
  public List<Long> findGroupAdminsPsnIdList(Long groupId) throws Exception {

    String hql =
        "select psnId from GroupInvitePsn where groupId = ? and isAccept='1' and status='01' and groupRole in('1','2')";
    Query query = createQuery(hql.toString(), groupId);
    return query.list();
  }

  @SuppressWarnings("unchecked")
  public List<GroupInvitePsn> findGroupInvitePsnByPsnId(Long psnId) throws Exception {

    String hql = "from GroupInvitePsn where psnId=?";
    Query query = createQuery(hql, psnId);
    return query.list();
  }

  /**
   * 判断是否为群组管理员或创建人.
   * 
   * @param groupId
   * @param psnId
   * @return
   * @throws Exception
   */
  public boolean isGroupAdmin(Long groupId, Long psnId) throws Exception {

    String hql =
        "select count(*) from GroupInvitePsn where groupId = ? and psnId=? and isAccept='1' and status='01' and groupRole in('1','2')";
    Long count = super.findUnique(hql.toString(), groupId, psnId);
    return count.intValue() > 0;
  }

  @SuppressWarnings("unchecked")
  public List<Long> findGroupInvitePsnByPrj(List<Long> psnIds) {
    String hql =
        "select distinct t.psnId from GroupInvitePsn t where t.groupId in(select t2.groupId from GroupPsn t2 where t2.groupCategory=1 and t2.status='01') and t.psnId in(:psnIds) order by t.psnId asc";
    return createQuery(hql).setParameterList("psnIds", psnIds).list();
  }


  // 只查询从基金委isis导到sns的群组
  @SuppressWarnings("unchecked")
  public List<Long> findPrjGroupIdsByPsnId(Integer nodeId, Long psnId) {
    String hql =
        "select distinct t.id.groupId from GroupInvitePsnNode t,GroupPsnNode t2,GroupPsn t3 where t.id.groupId=t2.groupId and t.id.groupId=t3.groupId and t3.isIsisPrj=1 and t2.groupCategory=1 and t.nodeId=? and t.id.psnId=?";
    return createQuery(hql, nodeId, psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> findGroupInvitePsnByGroupIds(List<Long> groupIds, Long psnId) {
    String hql =
        "select distinct t.psnId from GroupInvitePsn t,PersonKnow t2 where t.psnId=t2.personId and t2.isAddFrd=0 and t2.isLogin=1 and t2.isPrivate=0 and t.isAccept='1' and t.status='01' and t.psnId not in(select t3.friendPsnId from Friend t3 where t3.psnId=:psnId1) and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=:psnId2) and t.groupId in(:groupIds)";
    return createQuery(hql).setParameter("psnId1", psnId).setParameter("psnId2", psnId)
        .setParameterList("groupIds", groupIds).list();
  }

  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> findGroupInvitePsnIdsByMap(List<Long> groupIds, Long psnId) {
    String hql =
        "select t.psnId,count(t.psnId) from GroupInvitePsn t,PersonKnow t2 where t.psnId=t2.personId and t2.isAddFrd=0 and t2.isLogin=1 and t2.isPrivate=0 and t.isAccept='1' and t.status='01' and t.psnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=:psnId2) and t.groupId in(:groupIds) group by t.psnId";
    Query query = createQuery(hql).setParameter("psnId2", psnId).setParameterList("groupIds", groupIds);
    List<Object[]> list = query.list();
    List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
    for (Object[] objs : list) {
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("psnId", objs[0]);
      map.put("count", objs[1]);
      mapList.add(map);
    }
    return mapList;
  }

  /**
   * 按照请求参数精确查询匹配的系统用户.
   * 
   * @param paramInfo 请求参数.
   * @return 人员信息列表.
   * @throws Exception
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public List<Object[]> getMatchedUser(Map<String, Object> paramInfo) throws Exception {
    String param = ObjectUtils.toString(paramInfo.get("param"));
    String psnId = ObjectUtils.toString(paramInfo.get("psnId"));
    String isVerify = ObjectUtils.toString(paramInfo.get("isVerify"));

    // 拼接数据库查询语句.
    StringBuilder hql = new StringBuilder();
    hql.append("select p.name,p.first_name||' '||p.last_name,p.psn_id,p.email,p.avatars from person p where 1=1 ");
    // 拼接查询条件参数.
    List<Object> paramList = new ArrayList<Object>();

    List personIdList = null;// 邮件对应人员ID的列表.
    // 如果人员ID参数不为空.
    if (psnId != null && !"".equals(psnId)) {
      hql.append("and p.psn_id=? ");
      paramList.add(Long.valueOf(psnId));
    } else {
      // 对查询参数进行去空转换操作.
      String paramConditions = null;
      if (param != null && !"".equals(param)) {
        paramConditions = param.trim().toLowerCase();
      }
      if (paramConditions != null) {
        // 检查是否正确邮件格式的字符串,如果是，则与系统中的邮件进行匹配，否则与系统中的人员进行匹配.
        boolean format = ServiceUtil.isEmailValidate(paramConditions);
        if (format) {
          // 与系统中用户的邮件地址进行匹配，获取对应人员ID(格式为逗号分隔的人员ID字符串).
          personIdList = this.getPsnIdByEmail(paramConditions, isVerify);
          if (personIdList != null && personIdList.size() > 0) {
            hql.append("and p.psn_id in (:personIds) ");
          }
          // 未匹配到已确认的邮件地址则直接返回.
          else {
            // hql.append("and lower(p.email)=? ");
            // paramList.add(param_conditions);
            return null;
          }
        }
        // 条件参数不是正确格式的邮件地址，因此与系统中人员的姓名进行匹配.
        else {
          hql.append(
              "and (lower(p.name) = ? or lower(p.last_name || ' ' || p.first_name) = ? or lower(p.first_name || ' ' || p.last_name) = ?) ");
          paramList.add(paramConditions);
          paramList.add(paramConditions);
          paramList.add(paramConditions);
        }
      }
    }
    // 执行查询.
    SQLQuery sqlQuery = super.getSession().createSQLQuery(hql.toString());
    sqlQuery.setParameters(paramList.toArray(), super.findTypes(paramList.toArray()));
    // 如果匹配到了系统人员，则对sql语句中的内容进行替换.
    if (personIdList != null && personIdList.size() > 0) {
      sqlQuery.setParameterList("personIds", personIdList);
    }
    return sqlQuery.list();
  }

  /**
   * 按照人员姓名和当前登录用户ID精确查询匹配的系统用户(将此方法实现逻辑封装入getMatchedUser()方法_2013-03-01_应SCM- 1894功能改造实现需要).
   * 
   * @param param 人员姓名或email.
   * @param psnId 人员ID.
   * @return 人员信息列表.
   * @throws Exception
   */
  @SuppressWarnings({"unchecked"})
  public List<Object[]> autoFindFriendFull(String param, String psnId) throws Exception {
    Map<String, Object> paramMap = MapBuilder.getInstance().getMap();
    paramMap.put("param", param);
    paramMap.put("psnId", psnId);
    paramMap.put("isVerify", "true");// getPsnIdByEmail方法中调用此参数(邮箱校验状态必须是1-已确认)_MaoJianGuo_2012-11-26_SCM-1333.
    List<Object[]> result = this.getMatchedUser(paramMap);
    return result;
  }

  /**
   * 根据邮箱地址获取人员ID.
   * 
   * @param email 邮箱地址.
   * @param isVerify 是否限制邮箱校验状态(true-限制；false-不限制).
   * @return 逗号分隔的人员ID字符串.
   */
  @SuppressWarnings("rawtypes")
  private List getPsnIdByEmail(String email, String isVerify) {
    String psnSql = "select t.psn_id from psn_email t where t.first_mail='1' and lower(t.email)=? ";
    // 是否限制邮箱校验状态.
    if (StringUtils.isNotBlank(isVerify) && "true".equalsIgnoreCase(isVerify)) {
      psnSql += "and is_verify=1 ";
    }
    // 拼接查询条件参数.
    List<Object> conList = new ArrayList<Object>();
    conList.add(email);
    // 执行查询.
    SQLQuery sqlQuery = super.getSession().createSQLQuery(psnSql);
    Object[] objects = conList.toArray();
    sqlQuery.setParameters(objects, super.findTypes(objects));
    return sqlQuery.list();
  }

  public GroupInvitePsn findGroupPsnRelation(Long groupId, Long psnId) throws Exception {
    String hql = "from GroupInvitePsn where groupId = ? and psnId=? and status = '01'";
    return this.findUnique(hql, groupId, psnId);
  }

  @SuppressWarnings("unchecked")
  public List<Object[]> getGroupList(Long groupId, String param) throws Exception {
    String hql =
        "select distinct t.psnId ,t.psnName , t.psnFirstName || ' ' ||t.psnLastName , t.titolo from GroupInvitePsn t where (lower(t.psnName) like ? or lower(t.psnFirstName || ' ' || t.psnLastName) like ?) and t.groupId = ? and t.isAccept = 1";
    hql += " order by t.psnName asc";
    String psnNameConditions = param.length() == param.getBytes().length ? param.trim() : param.trim().toLowerCase();
    Query query =
        this.createQuery(hql, new Object[] {"%" + psnNameConditions + "%", "%" + psnNameConditions + "%", groupId});
    query.setMaxResults(5);
    return query.list();
  }

  /**
   * 查看群组主页，成员列表.
   * 
   * @param psnId
   * @param page
   * @param confId
   * @param isAll
   * @throws Exception
   */
  public void queryMemberForHomePageView(long groupId, Page<GroupInvitePsn> page, long confId, int isAll) {
    String countHql = "select count(t.invitePsnId) ";
    String orderHql = " order by t.groupRole asc,t.createDate desc,t.invitePsnId desc ";
    String listHql = " select t ";
    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupInvitePsn t ");

    List<Object> params = new ArrayList<Object>();
    if (isAll == 0) {
      hql.append(",GroupHomePageMember");
      hql.append(" ghp where t.psnId = ghp.id.memberId and ghp.id.confId = ? and ");
      params.add(confId);
    } else {
      hql.append(" where ");
    }
    hql.append(" t.groupId=? and t.status = '01' and t.isAccept='1' ");
    params.add(groupId);
    hql.append(" and exists (select 1 from Person p where p.personId = t.psnId) ");
    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);
    if (totalCount == 0) {
      return;
    }
    hql.append(orderHql);
    hql.insert(0, listHql);
    // 查询数据实体
    Query queryResult = super.createQuery(hql.toString(), params.toArray());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
  }

  /**
   * 更新群组邀请成员的记录状态.
   * 
   * @param status
   * @param isAccept
   * @param invitePsnId
   */
  public void upGroupInvitePsnStatus(String status, String isAccept, Long invitePsnId) {
    String hql = "update GroupInvitePsn set status=?,isAccept=? where invitePsnId=?";
    super.createQuery(hql, new Object[] {status, isAccept, invitePsnId}).executeUpdate();
  }

  // ==============人员合并 start============
  @SuppressWarnings("unchecked")
  public List<GroupInvitePsn> getGroupInviteByPsnId(Long delPsnId, Long groupId) throws Exception {
    String hql = "from GroupInvitePsn where (createPsnId=? or psnId=?) and groupId=?";
    return super.createQuery(hql, delPsnId, delPsnId, groupId).list();
  }

  public void deleteByInvitePsnId(Long invitePsnId) throws Exception {
    super.update("delete from GROUP_INVITE_PSN where INVITE_PSN_ID =?", new Object[] {invitePsnId});
  }

  // ==============人员合并 end============

  public boolean checkPsnInGroups(Long psnId, List<Long> groupIdList) throws Exception {
    String hql =
        "select count(invitePsnId) from GroupInvitePsn where groupId in(:groupIdList) and psnId=:psnId and status = '01' and isAccept='1'";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameterList("groupIdList", groupIdList)
        .uniqueResult();
    if (count > 0) {
      return true;
    }
    return false;
  }

  /**
   * 获取群组成员的角色.
   * 
   * @param psnId
   * @param groupId
   * @return
   */
  public String getPsnGroupRole(Long psnId, Long groupId) {
    String hql = "select groupRole from GroupInvitePsn t where t.psnId=? and t.groupId=? ";
    return ObjectUtils.toString(super.createQuery(hql, psnId, groupId).uniqueResult());
  }

  /**
   * 获取群组统计数(修改了取群组的表为GroupFilter_MJG_SCM-6255).
   * 
   * @param psnId
   * @return
   */
  public Integer getGroupCountByPsnId(Long psnId) {

    String hql =
        "select count(gi.invitePsnId) from GroupInvitePsn gi,GroupFilter g where gi.psnId=? and gi.status='01' and gi.isAccept='1' and gi.groupId = g.groupId and g.status='01' and g.openType in ('H','O') ";
    Long count = (Long) super.createQuery(hql, psnId).uniqueResult();
    return count.intValue();
  }
}
