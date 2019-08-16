package com.smate.center.task.dao.sns.quartz;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.smate.center.task.model.sns.quartz.Friend;
import com.smate.core.base.utils.common.HqlUtils;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.SecurityUtils;

/**
 * 个人好友Dao.
 * 
 * cwli
 */
@Repository
public class FriendDao extends SnsHibernateDao<Friend, Long> {

  public List<Long> findFriendPsnIds(List<Long> psnIdList) {
    List<Long> fPsnIdList = new ArrayList<Long>();
    List<Friend> list = this.findFriend(psnIdList);
    if (CollectionUtils.isNotEmpty(list)) {
      for (Friend friend : list) {
        fPsnIdList.add(friend.getFriendPsnId());
      }
    }
    return fPsnIdList;
  }

  @SuppressWarnings("unchecked")
  public List<Friend> findFriend(List<Long> psnIdList) {
    Criteria criteria = getSession().createCriteria(Friend.class);
    criteria.add(Restrictions.in("psnId", psnIdList));
    return criteria.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).list();
  }

  public List<Friend> findFriendByPsnId(Long psnId) throws Exception {
    return findBy("psnId", psnId);
  }

  public List<Friend> findByFriendPsnId(Long friendPsnId) throws Exception {
    return findBy("friendPsnId", friendPsnId);
  }

  @SuppressWarnings("unchecked")
  public List<Friend> findFriendAutoRecommend(Long psnId) {
    String hql =
        "select t1 from Friend t1,Person t2 where t1.friendPsnId=t2.personId and not exists(select 1 from PsnPrivate pp where pp.psnId=t1.friendPsnId) and t2.isLogin=1 and t1.psnId = :psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  @SuppressWarnings("unchecked")
  public List<Friend> findFriendAutoRecommend(Long psnId, Long friendPsnId) {
    String hql =
        "select t1 from Friend t1,PersonKnow t2 where t1.friendPsnId=t2.personId and t2.isAddFrd=0 and t2.isPrivate=0 and t2.isLogin=1 and t1.friendPsnId not in(select t3.friendPsnId from Friend t3 where t3.psnId=?) and t1.friendPsnId not in(select t5.tempPsnId from FriendTempSys t5 where t5.psnId=?) and t1.psnId=?";
    Query q = createQuery(hql, new Object[] {psnId, psnId, friendPsnId});
    return (List<Friend>) q.list();
  }

  public Long findFriendIds(Long friendId) throws Exception {
    String hql = "select count(*) from Friend where friendPsnId=?";
    return findUnique(hql, friendId);
  }

  public List<Long> findFriend(Long psnId) throws Exception {
    String hql = "select friendPsnId from Friend where psnId=?";
    return find(hql, psnId);
  }

  public Friend getFriendByPsn(Long psnId, Long friendPsnId) throws Exception {
    String hql = "from Friend where psnId=? and friendPsnId=?";
    return findUnique(hql, psnId, friendPsnId);
  }

  @SuppressWarnings("unchecked")
  public List<Long> findFriendByPsnNode(Long psnId, Integer node, int firstResult, int maxResults) {
    String hql = "select friendPsnId from Friend where psnId=? and friendNode=?";
    Query query = createQuery(hql, psnId, node);
    query.setFirstResult(firstResult);
    query.setMaxResults(maxResults);
    return query.list();
  }

  public Long getFriendCount(Long psnId) {
    String hql = "select count(*) from Friend where psnId=?";
    return findUnique(hql, psnId);
  }

  public Long isFriend(Long psnId, Long friendPsnId) throws Exception {
    String hql = "select count(*) from Friend where psnId=? and friendPsnId=?";
    return findUnique(hql, psnId, friendPsnId);
  }

  /**
   * 获取人员列表中是用户好友的人员ID.
   * 
   * @param psnId
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> filterFriendPsn(Long psnId, List<Long> psnIds) {

    String hql = "select friendPsnId from Friend where psnId=:psnId and friendPsnId in(:friendPsnId) ";
    return super.createQuery(hql).setParameter("psnId", psnId).setParameterList("friendPsnId", psnIds).list();
  }

  public Long getFriendId(Long psnId, Long friendPsnId) throws Exception {
    String hql = "select id from Friend where psnId=? and friendPsnId=?";
    return findUnique(hql, psnId, friendPsnId);
  }

  // @SuppressWarnings("unchecked")
  // public List<Object[]> autoFindFriend(String param) throws Exception {
  // param = "%" + param.replace(" ", "") + "%";
  // String hql =
  // "select distinct p.psnName,p.psnId from SyncPerson p,Friend f where
  // p.psnId=f.friendPsnId and (replace(p.psnName,' ','') like ? or
  // replace(p.psnLastName,' ','')||replace(p.psnFirstName,' ','') like ?) and
  // f.psnId=? ";
  // Query query = createQuery(hql, new Object[] { param, param,
  // SecurityUtils.getCurrentUserId() });
  // return query.list();
  // }

  /**
   * 按照人员姓名,当前登录用户ID和需要排除的用户ID搜索匹配的好友
   * 
   * @param param 人员姓名
   * @param psnIds 需要排除的用户
   * @return 好友信息列表
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> autoFindFriend(String param, String psnIds, Integer size) throws Exception {
    String hql =
        "select distinct p.psnName,p.psnFirstName || ' ' || p.psnLastName,p.psnId,p.psnEmail,p.psnHeadUrl,p.psnNode from SyncPerson p,Friend f where p.psnId=f.friendPsnId and (lower(p.psnName) like :psnName or lower(p.psnLastName || ' ' || p.psnFirstName) like :psnName) and f.psnId = :psnId ";
    if (StringUtils.isNotBlank(psnIds)) {
      hql += "and f.friendPsnId not in (:friendPsnId) ";
    }
    hql += "order by p.psnName asc";
    // 如果查询参数中包含中文字符时不区分大小写
    String psnName_conditions = param.length() == param.getBytes().length ? param.trim() : param.trim().toLowerCase();
    Query query = createQuery(hql);
    query.setParameter("psnName", "%" + psnName_conditions + "%");
    query.setParameter("psnId", SecurityUtils.getCurrentUserId());
    if (StringUtils.isNotBlank(psnIds)) {
      query.setParameterList("friendPsnId", StrArrayToLong(psnIds));
    }
    if (size != null && size > 0) {
      query.setMaxResults(size);
    }
    return query.list();
  }

  private Long[] StrArrayToLong(String str) {
    String[] strArray = str.split(",");
    Long[] longArray = new Long[strArray.length];
    for (int i = 0; i < strArray.length; i++) {
      longArray[i] = Long.valueOf(strArray[i]);
    }
    return longArray;
  }

  /**
   * 按照人员姓名和当前登录用户ID搜索匹配的好友
   * 
   * @param param 人员姓名
   * @return 好友信息列表
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<Object[]> autoFindFriendFull(String param) throws Exception {
    String hql =
        "select distinct p.psnName,p.psnFirstName || ' ' || p.psnLastName,p.psnId,p.psnEmail,p.psnHeadUrl,p.psnNode from SyncPerson p,Friend f where p.psnId=f.friendPsnId and (lower(p.psnName) like ? or lower(p.psnLastName || ' ' || p.psnFirstName) like ?) and f.psnId=? ";
    hql += "order by p.psnName asc";
    // 如果查询参数中包含中文字符时不区分大小写
    String psnName_conditions = param.length() == param.getBytes().length ? param.trim() : param.trim().toLowerCase();
    Query query = createQuery(hql, new Object[] {"%" + psnName_conditions + "%", "%" + psnName_conditions + "%",
        SecurityUtils.getCurrentUserId()});
    return query.list();
  }

  @SuppressWarnings("unchecked")
  public List<Friend> findFriendByAutoRecommend(List<Long> psnIds) {
    String hql = "from Friend t1 where t1.psnId in(:psnIds) order by t1.psnId asc";
    return createQuery(hql).setParameterList("psnIds", psnIds).list();
  }

  /**
   * 检索我的主页菜单右侧边栏-我的好友弹出框_MJG_SCM-5707.
   * 
   * @param page
   * @param friend
   * @return
   */
  @SuppressWarnings({"unchecked"})
  public Page<Friend> findMyFriend(Page<Friend> page, Friend friend) {
    StringBuffer hql = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    String hqlSuf = "select new Friend(id,psnId,friendPsnId, friendNode, status, friendGroupList) ";
    String hqlCount = "select count(t.id) ";

    hql.append(" from Friend t where t.psnId=?");
    params.add(friend.getPsnId());
    if (friend.getFriendRecent()) {// 最近联系
      List<Long> recentFriendIds = friend.getRecentFriendIds();
      if (recentFriendIds != null) {
        hql.append(" and t.friendPsnId in (");
        for (int i = 0; i < recentFriendIds.size() - 1; i++) {
          hql.append("?,");
          params.add(recentFriendIds.get(i));
        }
        hql.append("?)");
        params.add(recentFriendIds.get(recentFriendIds.size() - 1));
      }
    }

    // 获取记录数量.
    Long count = (Long) super.createQuery(hqlCount + hql.toString(), params.toArray()).uniqueResult();
    if (count == 0) {
      return page;
    }
    if (CollectionUtils.isNotEmpty(friend.getRecentFriendIds())) {// 最近联系，按联系时间排序
      hql.append(" order by instr ('" + friend.getRecentFriendIds().toString() + "',t.friendPsnId)");
    } else {
      hql.append(" order by t.id");
    }
    page.setTotalCount(count);
    List<Friend> resultList = super.createQuery(hqlSuf + hql.toString(), params.toArray())
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    page.setResult(resultList);
    return page;
  }

  /**
   * 按照搜索条件、姓名首字母等搜索
   * 
   * @param page
   * @param friend
   * @param existPsnIds
   * @return
   * @throws Exception
   */
  @SuppressWarnings({"unchecked"})
  public Page<Friend> findFriend(Page<Friend> page, Friend friend, String existPsnIds) throws Exception {
    List<Object> params = new ArrayList<Object>();
    StringBuffer hql = new StringBuffer();

    hql.append(
        "select t.id,t.psnId,t.friendPsnId,t.friendNote,t.status,t.friendGroupList,p.psnNode,p.psnName,p.psnFirstName,p.psnLastName");

    hql.append(",p.psnTel,p.psnMobile,p.psnQQ,p.psnMsn,p.psnEmail,p.psnHeadUrl,p.psnTitle,p.psnInsIdList,p.regionId");

    hql.append(",p.psnPrjNum,p.psnPubNum,p.psnISI,p.hindex,p.psnDiscipline,p.psnSkype");

    Query q = null;

    if (friend.getFreindGroupId() != null && friend.getFreindGroupId() != 9) {
      hql.append(" from Friend t ,SyncPerson p,FriendInGroups g where t.friendPsnId=p.psnId and t.id=g.friendId");
      hql.append(" and t.psnId=? and g.groupId=?");

      params.add(friend.getPsnId());
      params.add(friend.getFreindGroupId());

    } else {
      hql.append(" from Friend t,SyncPerson p where t.friendPsnId=p.psnId and t.psnId=?");
      params.add(friend.getPsnId());

      if (friend.getRegionId() != null) {
        if (2 == friend.getRegionId().intValue()) {
          hql.append(" and p.regionId is not null");
        } else {
          hql.append(" and p.regionId=?");
          params.add(friend.getRegionId());
        }
      } else if (StringUtils.isNotBlank(friend.getInsIdOrNames())) {
        String paramIns = "";
        try {
          if ("1".equalsIgnoreCase(friend.getInsIdOrNames())) {
            hql.append(" and (p.psnInsIdList is not null or p.psnInsNameList is not null)");
          } else {
            paramIns = "%" + Long.parseLong(friend.getInsIdOrNames()) + "%";
            hql.append(" and p.psnInsIdList like ?");
          }
        } catch (Exception e) {
          paramIns = "%" + friend.getInsIdOrNames() + "%";
          hql.append(" and p.psnInsNameList like ?");
        }
        if (StringUtils.isNotBlank(paramIns)) {
          params.add(paramIns);
        }

      } else if (friend.getFriendRecent()) {// 最近联系
        List<Long> recentFriendIds = friend.getRecentFriendIds();
        if (recentFriendIds != null) {
          hql.append(" and t.friendPsnId in (");
          for (int i = 0; i < recentFriendIds.size() - 1; i++) {
            hql.append("?,");
            params.add(recentFriendIds.get(i));
          }
          hql.append("?)");
          params.add(recentFriendIds.get(recentFriendIds.size() - 1));
        }
      }
    }

    if (StringUtils.isNotBlank(existPsnIds)) {
      hql.append(" and t.friendPsnId not in(");
      String[] existPsnIdArray = existPsnIds.split(",");
      for (int i = 0; i < existPsnIdArray.length - 1; i++) {
        hql.append("?,");
        params.add(Long.valueOf(existPsnIdArray[i]));
      }
      hql.append("?)");
      params.add(Long.valueOf(existPsnIdArray[existPsnIdArray.length - 1]));
    }

    // 搜索条件不为空
    if (StringUtils.isNotBlank(friend.getFriendName())) {
      hql.append(
          " and (lower(p.psnName) like ? or lower((p.psnFirstName ||' '|| p.psnLastName)) like ? or lower((p.psnLastName ||' '|| p.psnFirstName)) like ? or lower(t.friendNote) like ?) ");
      String pm = StringUtils.lowerCase(friend.getFriendName().trim());
      params.add(StringUtils.isBlank(pm) ? "" : "%" + pm + "%");
      params.add(StringUtils.isBlank(pm) ? "" : "%" + pm + "%");
      params.add(StringUtils.isBlank(pm) ? "" : "%" + pm + "%");
      params.add(StringUtils.isBlank(pm) ? "" : "%" + pm + "%");
    }

    // 按字母类别查询
    if (StringUtils.isNotBlank(friend.getLettersType())) {
      hql.append(" and p.firstLetter=?");
      params.add(StringUtils.upperCase(friend.getLettersType()));
    }

    if (CollectionUtils.isNotEmpty(friend.getRecentFriendIds())) {// 最近联系，按联系时间排序
      hql.append(" order by instr ('" + friend.getRecentFriendIds().toString() + "',t.friendPsnId)");
    } else {
      hql.append(" order by p.firstLetter, t.id");
    }

    q = super.createQuery(hql.toString(), params.toArray());

    @SuppressWarnings("rawtypes")
    List list = q.list();
    if (CollectionUtils.isNotEmpty(list)) {
      int totalCount = list.size();
      page.setTotalCount(totalCount);
      if (page.getPageSize() < 10) {
        Long random = HqlUtils.getRandom(0, totalCount - page.getPageSize());
        random = random < 0 ? 0L : random;
        q.setFirstResult(random.intValue());
        q.setMaxResults(random.intValue() + page.getPageSize());
      } else {
        q.setFirstResult(page.getFirst() - 1);
        q.setMaxResults(page.getPageSize());
      }
      List<Object[]> objList = q.list();
      page.setResult(setFriendList((List<Object[]>) objList));
    }
    return page;
  }

  @SuppressWarnings("unchecked")
  public Friend getFriend(Long friendPsnId, Long currentPsnId) throws Exception {
    StringBuffer hql = new StringBuffer();
    hql.append(
        "select distinct t.friendPsnId,p.psnName,p.psnHeadUrl,p.psnTitle,p.psnEmail,t.friendNote,p.psnFirstName,p.psnLastName,p.psnSkype ");
    hql.append(" from Friend t ,SyncPerson p");
    hql.append(" where t.friendPsnId=p.psnId and t.friendPsnId=? and t.psnId=?");
    Query q = createQuery(hql.toString(), new Object[] {friendPsnId, currentPsnId});
    List<Object[]> objList = q.list();
    List<Friend> friendList = new ArrayList<Friend>();
    for (Object[] objects : objList) {
      int i = 0;
      Friend friend = new Friend();
      friend.setFriendPsnId(Long.valueOf(String.valueOf(objects[i])));
      friend.setFriendName(String.valueOf(objects[++i]));
      friend.setFriendHeadUrl(String.valueOf(objects[++i]));
      if (objects[++i] != null) {
        friend.setFriendTitle(String.valueOf(objects[i]));
      }
      if (objects[++i] != null) {
        friend.setFriendEmail(String.valueOf(objects[i]));
      }
      if (objects[++i] != null) {
        friend.setFriendNote(String.valueOf(objects[i]));
      }
      if (objects[++i] != null) {
        friend.setFriendFirstName(String.valueOf(objects[i]));
      }
      if (objects[++i] != null) {
        friend.setFriendLastName(String.valueOf(objects[i]));
      }
      friendList.add(friend);
    }
    return friendList.size() > 0 ? friendList.get(0) : null;
  }

  private String createHql(String psnName, String firstName, String lastName, String workName, String schoolName,
      List<Long> disciplines, List<Long> exisPsnIds) {
    StringBuffer hqlFrom = new StringBuffer(
        "select distinct t.psnId,t.psnName,t.firstName,t.lastName,t.psnEmail,t.psnTitle,t.psnHeadUrl,t.insName,t.regionId from NodePerson t");
    // // 过滤掉不公开的用户
    StringBuffer hqlWhere = new StringBuffer(" where not exists(select 1 from PsnPrivate pp where pp.psnId=t.psnId)");

    if (StringUtils.isNotBlank(psnName) && StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
      String name = psnName.replace(" ", "");
      firstName = firstName.trim().replace(" ", "").toLowerCase();
      lastName = lastName.trim().replace(" ", "").toLowerCase();
      hqlWhere.append("and (t.psnName ='" + name + "' and lower(replace(t.firstName,' ','')) ='" + firstName
          + "' and lower(replace(t.lastName,' ','')) ='" + lastName + "')");
    } else if (StringUtils.isBlank(psnName) && StringUtils.isNotBlank(firstName) && StringUtils.isNotBlank(lastName)) {
      firstName = firstName.trim().replace(" ", "").toLowerCase();
      lastName = lastName.trim().replace(" ", "").toLowerCase();
      hqlWhere.append(
          "and ((lower(replace(t.firstName,' ','')) ='" + firstName + "' and lower(replace(t.lastName,' ','')) ='"
              + lastName + "') or lower(replace(t.psnEname,' ',''))='" + firstName + lastName + "')");
    } else if (StringUtils.isNotBlank(psnName) && StringUtils.isBlank(firstName) && StringUtils.isBlank(lastName)) {
      psnName = psnName.replace(" ", "").toLowerCase();
      hqlWhere.append(" and (t.psnName ='" + psnName + "' or lower(replace(t.psnEname,' ','')) = '" + psnName
          + "' or lower(replace(t.lastName,' ','')) || lower(replace(t.firstName,' ','')) = '" + psnName + "')");
    }
    if (StringUtils.isNotBlank(workName)) {
      workName = "'%" + workName.trim() + "%'";
      hqlFrom.append(",PsnWork w");
      hqlWhere.append(" and t.psnId=w.psnId and (w.insName like " + workName
          + " or w.insId in (select t2.id from Institution t2 where t2.zhName like " + workName
          + " or lower(t2.enName) like lower(" + workName + ") or lower(t2.abbreviation) like lower(" + workName
          + ") ))");
    }
    if (StringUtils.isNotBlank(schoolName)) {
      schoolName = "'%" + schoolName.trim() + "%'";
      hqlFrom.append(",PsnEdu e");
      hqlWhere.append(" and t.psnId=e.psnId and (e.insName like " + schoolName
          + " or e.insId in (select t2.id from Institution t2 where t2.zhName like " + schoolName
          + " or lower(t2.enName) like lower(" + schoolName + ") or lower(t2.abbreviation) like lower(" + schoolName
          + ")))");
    }
    if (CollectionUtils.isNotEmpty(disciplines)) {
      disciplines = disciplines.size() > 99 ? disciplines.subList(0, 99) : disciplines;
      String disIds = "";
      for (Long disid : disciplines) {
        disIds += disid + ",";
      }
      if (disIds.length() > 0) {
        disIds = disIds.substring(0, disIds.length() - 1);
      }
      hqlFrom.append(",PsnDiscipline d");
      hqlWhere.append(" and t.psnId=d.id.psnId and d.id.disId in(" + disIds + ")");
    }
    if (CollectionUtils.isNotEmpty(exisPsnIds)) {
      hqlWhere.append(" and t.psnId not in(:paramIds2)");
    }
    return hqlFrom.toString() + hqlWhere.toString();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getCancelFriendAttList() throws Exception {
    String hql =
        "select t.friendPsnId from Friend t where t.friendPsnId not in(select ap.refPsnId from AttPerson ap where ap.psnId=?) and t.psnId=?";
    return super.createQuery(hql, new Object[] {SecurityUtils.getCurrentUserId(), SecurityUtils.getCurrentUserId()})
        .list();

  }

  /**
   * 构建好友人员信息(因此方法与其他方法代码绑定关联太密，不容易维护扩展，建议逐步替换为com.iris.scm.scmweb.service.
   * friend.FriendServiceImpl.buildFriendPsnInfo(Friend)方法)_MJG_SCM-5707.
   * 
   * @param objList
   * @return
   */
  private List<Friend> setFriendList(List<Object[]> objList) {
    try {
      List<Friend> friendList = new ArrayList<Friend>();
      for (Object[] object : objList) {
        int i = 0;
        Friend friend = new Friend();
        friend.setId(Long.valueOf(String.valueOf(object[i])));
        friend.setPsnId(Long.valueOf(String.valueOf(object[++i])));
        friend.setFriendPsnId(Long.valueOf(String.valueOf(object[++i])));
        if (null != object[++i]) {
          friend.setFriendNote(String.valueOf(object[i]));
        }
        friend.setStatus(Integer.valueOf(String.valueOf(object[++i])));
        if (null != object[++i]) {
          friend.setFriendGroupList(String.valueOf(object[i]));
        }
        if (null != object[++i]) {
          friend.setFriendNode(Integer.valueOf(String.valueOf(object[i])));
        }
        if (null != object[++i]) {
          friend.setFriendName(String.valueOf(object[i]));
        }
        if (null != object[++i]) {
          friend.setFriendFirstName(String.valueOf(object[i]));
        }
        if (null != object[++i]) {
          friend.setFriendLastName(String.valueOf(object[i]));
        }
        if (null != object[++i]) {
          friend.setFriendTel(String.valueOf(object[i]));
        }
        if (null != object[++i]) {
          friend.setFriendMobile(String.valueOf(object[i]));
        }
        if (null != object[++i]) {
          friend.setFriendQQ(String.valueOf(object[i]));
        }
        if (null != object[++i]) {
          friend.setFriendMsn(String.valueOf(object[i]));
        }
        if (null != object[++i]) {
          friend.setFriendEmail(String.valueOf(object[i]));
        }
        if (null != object[++i]) {
          friend.setFriendHeadUrl(String.valueOf(object[i]));
        }
        if (null != object[++i]) {
          friend.setFriendTitle(String.valueOf(object[i]));
        }
        if (null != object[++i]) {
          friend.setInsIdOrNames(String.valueOf(object[i]));
        }
        if (null != object[++i]) {
          friend.setRegionId(Long.valueOf(String.valueOf(object[i])));
        }
        if (null != object[++i]) {
          friend.setFriendPrjNum(Long.valueOf(String.valueOf(object[i])));
        }
        if (null != object[++i]) {
          friend.setFriendPubNum(Long.valueOf(String.valueOf(object[i])));
        }
        if (null != object[++i]) {
          friend.setFriendISI(Long.valueOf(String.valueOf(object[i])));
        }
        if (null != object[++i]) {
          friend.setFriendHindex(Long.valueOf(String.valueOf(object[i])));
        }
        if (null != object[++i]) {
          friend.setFriendDiscipline(String.valueOf(object[i]));
        }
        if (null != object[++i]) {
          friend.setFriendSkype(String.valueOf(object[i]));
        }
        friendList.add(friend);
      }
      return friendList;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void updateLoginStatus(Long psnId, int status) throws Exception {
    String hql = "update Friend set status=? where friendPsnId=?";
    createQuery(hql, status, psnId).executeUpdate();
  }

  /**
   * 过滤人员好友，注意在好友端过滤(反过来的).
   * 
   * @param friendPsnId
   * @param psnIds
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Long> filterPsnFriend(Long friendPsnId, List<Long> psnIds) {

    String hql = "select psnId from Friend t where t.friendPsnId = :friendPsnId and psnId in(:psnId) ";
    return super.createQuery(hql).setParameter("friendPsnId", friendPsnId).setParameterList("psnId", psnIds).list();
  }

  /**
   * 按姓名查找好友.
   * 
   * @param name
   * @param currentPsnId
   * @return
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public List<Friend> findMyFriendByName(String name, Long currentPsnId) throws Exception {
    String hql =
        "select t1 from Friend t1,SyncPerson t2 where t1.psnId=? and t1.friendPsnId=t2.psnId and (t2.psnName=? or (t2.psnFirstName||' '|| t2.psnLastName)=?)";
    return super.createQuery(hql, new Object[] {currentPsnId, name, name}).list();
  }

  /**
   * 获取与有动态人员是好友关系的人员
   */
  @SuppressWarnings("unchecked")
  public List<Long> verifyfriendByPro(List<Long> proIds) {
    if (proIds == null || proIds.size() == 0) {
      return null;
    }
    List<Long> ids = super.createQuery("select p.psnId from Friend p where p.friendPsnId in (:proId) ")
        .setParameterList("proId", proIds).list();
    return ids;
  }

  /**
   * 获取好友请求数量
   * 
   * @param psnId
   * @return
   * @throws Exception
   */
  public Long getUnreadInviteMsg(Long psnId) throws Exception {
    return findUnique(
        "select count(t1.mailId) from InviteMailBox t1,PsnInviteInbox t2 where t1.inviteType=0 and t1.mailId=t2.mailBox.mailId and t2.optStatus =? and t2.psnId=? and t2.status<>?",
        new Object[] {0, psnId, 2});
  }

  @SuppressWarnings("unchecked")
  public List<Long> filterFriendPsnId(Long curPsnId, List<Long> psnIds) {
    if (CollectionUtils.isEmpty(psnIds)) {
      return null;
    }
    return super.createQuery(
        "select t.friendPsnId from Friend t where t.psnId = :psnId and t.friendPsnId in(:friendPsnId) ")
            .setParameter("psnId", curPsnId).setParameterList("friendPsnId", psnIds).list();
  }

  @SuppressWarnings("unchecked")
  public List<Long> getFriendByPsn(Long psnId) {
    String hql = "select friendPsnId from Friend where psnId = :psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }
}
