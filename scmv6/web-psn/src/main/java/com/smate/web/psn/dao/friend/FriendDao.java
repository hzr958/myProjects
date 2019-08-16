package com.smate.web.psn.dao.friend;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.type.LongType;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.common.HqlUtils;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.psn.exception.DaoException;
import com.smate.web.psn.model.friend.Friend;
import com.smate.web.psn.model.friend.InviteJionForm;
import com.smate.web.psn.model.friend.PsnListViewForm;

/**
 * 好友Dao
 *
 * @author wsn
 *
 */
@Repository
public class FriendDao extends SnsHibernateDao<Friend, Long> {
  /**
   * 查询我的好友 -按联系时间排序---zzx--
   * 
   * @param psnId
   * @return
   */
  public List<Object> getFriendList(Long psnId) {
    String sql = "select  t.friend_Psn_Id from PSN_FRIEND t left join Recent_Selected t2 "
        + "on t.friend_psn_id = t2.selected_psn_id and t2.psn_Id =:psnId where  t.psn_Id=:psnId "
        + "order by nvl(t2.selected_date,to_date('1900-01-01','yyyy-mm-dd')) desc ";

    return super.getSession().createSQLQuery(sql).setParameter("psnId", psnId).list();
  }

  /**
   * 查询我的好友 -按联系时间,姓名排序---ajb--
   * 
   * @param psnId
   * @return
   */
  public List<BigDecimal> getFriendList(InviteJionForm form) {
    String orderBy = form.getOrderBy();
    StringBuffer sb = new StringBuffer();
    if (orderBy.equals("date")) {// 时间排序
      sb.append("select  t.friend_Psn_Id from PSN_FRIEND t left join Recent_Selected t2 "
          + "on t.friend_psn_id = t2.selected_psn_id and t2.psn_Id =:psnId where  t.psn_Id=:psnId "
          + "order by nvl(t2.selected_date,to_date('1900-01-01','yyyy-mm-dd'))  desc");
    } else {
      Locale locale = LocaleContextHolder.getLocale();
      sb.append(
          "select   f.friend_Psn_Id from  PSN_FRIEND  f , Person p where   f.friend_Psn_Id = p.psn_Id and f.psn_Id=:psnId ");
      if (Locale.US.equals(locale)) {
        sb.append(
            "order by   NLSSORT  ( nvl(nvl(p.ename,p.first_Name||p.last_Name),p.name) , 'NLS_SORT = SCHINESE_PINYIN_M' ) asc  nulls last");
      } else {
        sb.append(
            "order by   NLSSORT   ( nvl(nvl(p.name,p.first_Name||p.last_Name),p.ename) , 'NLS_SORT = SCHINESE_PINYIN_M' ) asc nulls last");
      }
    }
    return super.getSession().createSQLQuery(sb.toString()).setParameter("psnId", form.getPsnId()).list();
  }

  /**
   * 根据检索条件获取好友名字
   * 
   * zzx
   * 
   * @param psnId
   * @param nameSub
   * @param size
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> getFriendNames(Long psnId, String searchKey, Integer size) throws DaoException {
    if (searchKey == null) {
      searchKey = "";
    }
    String hql =
        "select new Map(nvl(nvl(t.name,t.firstName||' '||t.lastName),t.ename) as name,t.personId as code) from Person t where "
            + " instr(upper(nvl(nvl(t.name,t.firstName||t.lastName),t.ename)),:searchKey)>0 and t.personId in(select p.friendPsnId  from Friend p where p.psnId =:psnId) "
            + " order by instr(upper(nvl(nvl(t.name,t.firstName||t.lastName),t.ename)),:searchKey) asc";

    return super.createQuery(hql).setParameter("searchKey", searchKey.toUpperCase().trim()).setParameter("psnId", psnId)
        .setMaxResults(size).list();
  }

  public List<Map<String, Object>> findFriendNames(Long psnId, String searchKey, List<Long> excludePsnIds, Integer size)
      throws DaoException {
    if (searchKey == null) {
      searchKey = "";
    }
    StringBuilder sb = new StringBuilder();
    Locale locale = LocaleContextHolder.getLocale();
    if (Locale.US.equals(locale)) {
      sb.append("select new Map(nvl(nvl(t.ename,t.firstName||' '||t.lastName),t.name) as name,t.personId as code) ");
    } else {
      sb.append("select new Map(nvl(nvl(t.name,t.firstName||' '||t.lastName),t.ename) as name,t.personId as code) ");
    }
    sb.append(
        "from Person t where (upper(t.name) like :searchKey or upper(t.ename) like :searchKey or upper(t.firstName||t.lastName) like :searchKey) ");
    sb.append("and t.personId in(select p.friendPsnId  from Friend p where p.psnId =:psnId) ");
    // 排除成果id的判断
    if (excludePsnIds != null && excludePsnIds.size() > 0) {
      String psnIds = "";
      for (Long p : excludePsnIds) {
        psnIds = psnIds + p + ",";
      }
      psnIds = psnIds.substring(0, psnIds.length() - 1);
      sb.append("  and t.personId not in ( " + psnIds + " ) ");
    }
    return super.createQuery(sb.toString()).setParameter("searchKey", "%" + searchKey.toUpperCase().trim() + "%")
        .setParameter("psnId", psnId).setMaxResults(size).list();
  }

  /**
   * 排除关注人员
   * 
   * @param psnId
   * @param searchKey
   * @param size
   * @return
   * @throws DaoException
   */
  public List<Map<String, Object>> findFriendNamesExcludeAttention(Long psnId, String searchKey,
      List<Long> excludePsnIds, Integer size) throws DaoException {
    if (searchKey == null) {
      searchKey = "";
    }
    StringBuilder sb = new StringBuilder();
    Locale locale = LocaleContextHolder.getLocale();
    if (Locale.US.equals(locale)) {
      sb.append("select new Map(nvl(nvl(t.ename,t.firstName||' '||t.lastName),t.name) as name,t.personId as code) ");
    } else {
      sb.append("select new Map(nvl(nvl(t.name,t.firstName||' '||t.lastName),t.ename) as name,t.personId as code) ");
    }
    sb.append(
        "from Person t where (upper(t.name) like :searchKey or upper(t.ename) like :searchKey or upper(t.firstName||t.lastName) like :searchKey) ");
    sb.append("and t.personId in(select p.friendPsnId  from Friend p where p.psnId =:psnId) ");
    sb.append("and t.personId  not  in(select a.refPsnId  from AttPerson a  where a.psnId =:psnId) ");
    // 排除成果id的判断
    if (excludePsnIds != null && excludePsnIds.size() > 0) {
      String psnIds = "";
      for (Long p : excludePsnIds) {
        psnIds = psnIds + p + ",";
      }
      psnIds = psnIds.substring(0, psnIds.length() - 1);
      sb.append("  and t.personId not in ( " + psnIds + " ) ");
    }
    return super.createQuery(sb.toString()).setParameter("searchKey", "%" + searchKey.toUpperCase().trim() + "%")
        .setParameter("psnId", psnId).setMaxResults(size).list();
  }

  /**
   * 根据人员ID获取好友的人员ID
   * 
   * @param psnId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> getFriendListByPsnId(Long psnId) throws DaoException {
    String hql = "select t.friendPsnId from Friend t where  t.psnId=:psnId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 根据 人员id 与好友人员 id 判断是否好友
   * 
   * @param currendPsnId
   * @param friendPsnId
   * @return
   * @throws DaoException
   */
  public Boolean isFriend(Long currendPsnId, Long friendPsnId) throws DaoException {
    String hql = "select id from Friend t where  t.psnId=:currendPsnId and t.friendPsnId=:friendPsnId";
    Object object = super.createQuery(hql).setParameter("currendPsnId", currendPsnId)
        .setParameter("friendPsnId", friendPsnId).uniqueResult();
    if (object == null) {
      return false;
    } else {
      return true;
    }
  }

  /**
   * 群组-成员-邀请成员加入-获取5个好友
   * 
   * @author lhd
   * @param form
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Long> getFiveFriend(InviteJionForm form) {
    String hql =
        "select t.friendPsnId from Friend t where t.psnId=:psnId and not exists(select t2.psnId from GroupInvitePsn t2 where t2.groupId=:groupId and t.friendPsnId=t2.psnId and (t2.isAccept='1' or t2.isAccept='2' or t2.isAccept is null) and t2.status='01') order by t.createDate desc";
    Page page = form.getPage();
    return super.createQuery(hql).setParameter("psnId", form.getPsnId()).setParameter("groupId", form.getGroupId())
        .setFirstResult(page.getFirst() - 1).setMaxResults(5).list();
  }

  /**
   * 群组-成员-邀请成员加入-获取好友
   * 
   * @author lhd
   * @param form
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Long> getFriendIds(InviteJionForm form, int pageSize) {
    String hql =
        "select t.friendPsnId from Friend t where t.psnId=:psnId and not exists(select t2.psnId from GroupInvitePsn t2 where t2.groupId=:groupId and t.friendPsnId=t2.psnId and (t2.isAccept='1' or t2.isAccept='2' or t2.isAccept is null) and t2.status='01') order by t.createDate desc";
    Page page = form.getPage();
    return super.createQuery(hql).setParameter("psnId", form.getPsnId()).setParameter("groupId", form.getGroupId())
        .setFirstResult(page.getFirst() - 1).setMaxResults(pageSize).list();
  }

  /**
   * 群组成员-邀请成员加入-查找推荐人员
   * 
   * @author lhd
   * @param psnId
   * @param pageSize
   * @param lastPsnId
   * @return
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public List<Long> findLocalPersonIds(Long psnId, int pageSize, Long lastPsnId, Long groupId) {
    StringBuffer sql = new StringBuffer();

    // sql.append("select psn_id from (");
    // sql.append("select row_.psn_id, rownum rownum_ from (");
    sql.append("select psn.psn_id from person psn left join privacy_settings ps on psn.psn_id=ps.psn_id ");
    sql.append(
        "where ps.privacy_action='reqAddFrd' and ps.permission=0 and not exists(select 1 from psn_private pp where pp.psn_id=psn.psn_id) and is_login=1 and psn.psn_id>?  ");
    sql.append(
        "and not exists(select pf.friend_psn_id from psn_friend pf where pf.psn_id = ? and psn.psn_id=pf.psn_id) ");
    sql.append(
        "and not exists(select ps.temp_psn_id from psn_friend_sys ps where ps.psn_id = ? and psn.psn_id=ps.psn_id) ");
    // 已发送过好友请求的
    sql.append(
        "and not exists(select pft.temp_psn_id from psn_friend_temp pft where pft.psn_id = ? and pft.temp_psn_id is not null and psn.psn_id=pft.psn_id) ");
    // 排除当前群组里的人员
    sql.append(
        "and not exists(select t2.psn_id from GROUP_INVITE_PSN t2 where t2.group_id=? and psn.psn_id=t2.psn_id and t2.is_accept='2' and t2.status='01') ");
    sql.append("and rownum<= ? ");
    sql.append("order by psn.psn_id asc ");
    // sql.append(") row_ where rownum <= ?");
    // sql.append(") where rownum_ >?");
    // int frist = 0;
    int maxSize = pageSize;
    // 初始化范围随机获取800-99999区间
    lastPsnId = lastPsnId == 0 ? HqlUtils.getRandom(800, 99999) : lastPsnId;
    List<Map> list =
        super.queryForList(sql.toString(), new Object[] {lastPsnId, psnId, psnId, psnId, groupId, maxSize});
    if (CollectionUtils.isEmpty(list)) {
      return null;
    }
    List<Long> psnIds = new ArrayList<Long>();
    for (Map map : list) {
      psnIds.add(Long.valueOf(map.get("PSN_ID").toString()));
    }
    return psnIds;
  }

  /**
   * 查找好友和已经发送好友请求的人员ID
   * 
   * @param psnId
   * @return
   */
  public List<Long> findFriendAndReqFriendIds(Long psnId) {
    String hql = "select f.friendPsnId from Friend f where f.psnId = :psnId ";
    String hqlT = "select t.tempPsnId from FriendTemp t where t.psnId = :psnId ";
    List<Long> friendIds = super.createQuery(hql).setParameter("psnId", psnId).list();
    List<Long> friendTempIds = super.createQuery(hqlT).setParameter("psnId", psnId).list();
    if (friendIds != null) {
      friendIds.addAll(friendTempIds);
      return friendIds;
    } else if (friendTempIds != null) {
      friendTempIds.addAll(friendIds);
      return friendTempIds;
    } else {
      return null;
    }
  }

  /**
   * 根据批量id判断是不是好友
   */
  public List<Long> findFriendIds(Long currentId, List<Long> psnIds) {

    String hql = "select f.friendPsnId from Friend f where f.psnId=:currentId and f.friendPsnId in(:psnIds)";
    List<Long> friendIds =
        super.createQuery(hql).setParameter("currentId", currentId).setParameterList("psnIds", psnIds).list();
    return friendIds;
  }

  /**
   * 查找好友记录
   * 
   * @param psnId 人员ID
   * @param friendId 好友ID
   * @return
   */
  public Friend findFriendByPsnIdAndFriendId(Long psnId, Long friendId) {
    String hql =
        "select new Friend(id, psnId, friendPsnId, friendNode) from Friend t where t.psnId = :psnId and t.friendPsnId = :friendId";
    return (Friend) super.createQuery(hql).setParameter("psnId", psnId).setParameter("friendId", friendId)
        .uniqueResult();
  }

  /**
   * 获取好友数
   * 
   * @param psnId
   * @return
   */
  public Long getFriendCount(Long psnId) {
    String hql = "select count(*) from Friend where psnId=:psnId";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 获取个人好友ids
   * 
   * @author lhd
   * @param form
   * @return
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public PsnListViewForm queryFriendList(PsnListViewForm form) {
    String countSql = "select count(t.psn_id)";
    String listSql = "select t.psn_id";
    String orderSql = " order by nvl(t2.selected_date,to_date('1900-01-01','yyyy-mm-dd')) desc, t.psn_id desc";
    StringBuilder hql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    hql.append(" from PERSON t");
    hql.append(" left join RECENT_SELECTED t2 on t.psn_id=t2.selected_psn_id and t2.psn_Id =?");
    params.add(form.getPsnId());
    hql.append(" where");
    hql.append(" exists(select t3.friend_psn_id from PSN_FRIEND t3 where t3.psn_id=? and t3.friend_psn_id=t.psn_id)");
    params.add(form.getPsnId());
    String searchKey = form.getSearchKey();
    if (StringUtils.isNotBlank(searchKey)) {
      searchKey = StringEscapeUtils.unescapeHtml4(searchKey.replace("'", "''"));
      searchKey = searchKey.toUpperCase().trim();
      if (Locale.US.equals(LocaleContextHolder.getLocale())) {
        orderSql = " order by instr(nvl(upper(t.ename),upper(t.name)),'" + searchKey + "') desc,";
      } else {
        orderSql = " order by instr(nvl(upper(t.name),upper(t.ename)),'" + searchKey + "') desc,";
      }
      orderSql += " nvl(t2.selected_date,to_date('1900-01-01','yyyy-mm-dd')) desc, t.psn_id desc";
    }
    buildSearchKey(searchKey, hql, params);
    String insIds = form.getInsId();
    if (StringUtils.isNotBlank(insIds)) {
      String[] split = insIds.split(",");
      hql.append(" and (");
      for (String insId : split) {
        hql.append("t.ins_id =?");
        hql.append(" or ");
        params.add(insId);
      }
      hql.replace(hql.length() - 4, hql.length(), "");
      hql.append(")");
    }
    // if (StringUtils.isNotBlank(form.getRegionId())) {
    // hql.append(" and t.region_id in(" + form.getRegionId() + ")");
    // }
    Object[] objects = params.toArray();
    Page page = form.getPage();
    SQLQuery sqlQuery = super.getSession().createSQLQuery(countSql + hql);
    sqlQuery.setParameters(objects, super.findTypes(objects));
    Object totalCount = sqlQuery.uniqueResult();
    page.setTotalCount(NumberUtils.toLong(ObjectUtils.toString(totalCount)));
    List<Long> resultList = new ArrayList<Long>();
    SQLQuery sqlQuery2 = super.getSession().createSQLQuery(listSql + hql + orderSql);
    sqlQuery2.setParameters(objects, super.findTypes(objects));
    sqlQuery2.setFirstResult(page.getFirst() - 1);
    sqlQuery2.setMaxResults(page.getPageSize());
    resultList = sqlQuery2.addScalar("psn_id", new LongType()).list();
    page.setResult(resultList);
    return form;
  }

  private void buildSearchKey(String searchKey, StringBuilder hql, List<Object> params) {
    if (StringUtils.isNotEmpty(searchKey)) {
      // String searchKey = searchString.replaceAll("\'", "&#39;");
      // searchKey = StringEscapeUtils.unescapeHtml4(searchKey);
      // searchKey = searchKey.toUpperCase().trim();
      hql.append(" and");
      hql.append(" (");
      hql.append(" instr(upper(t.name),'" + searchKey + "')>0");// 中文名称
      hql.append(" or instr(upper(t.ename),'" + searchKey + "')>0");// 英文名称
      hql.append(" or instr(upper(t.first_name),'" + searchKey + "')>0");// 名
      hql.append(" or instr(upper(t.last_name),'" + searchKey + "')>0");// 姓氏
      hql.append(" or instr(upper(t.ins_name),'" + searchKey + "')>0");// 机构
      hql.append(" or instr(upper(t.department ),'" + searchKey + "')>0");// 部门
      hql.append(" or instr(upper(t.position ),'" + searchKey + "')>0");// 职称
      // hql.append(" or instr(upper(t.titolo),?)>0");// 头衔
      // params.add(searchKey);
      hql.append(" ) ");
    }

  }

  private void buildSearchKey2(String searchKey, StringBuilder hql, List<Object> params) {
    if (StringUtils.isNotEmpty(searchKey)) {
      // String searchKey = searchString.replaceAll("\'", "&#39;");
      searchKey = StringEscapeUtils.unescapeHtml4(searchKey.replace("'", "''"));
      searchKey = searchKey.toUpperCase().trim();
      hql.append(" and");
      hql.append(" (");
      hql.append(" instr(upper(t.name),'" + searchKey + "')>0");// 中文名称
      hql.append(" or instr(upper(t.ename),'" + searchKey + "')>0");// 英文名称
      hql.append(" or instr(upper(t.firstName),'" + searchKey + "')>0");// 名
      hql.append(" or instr(upper(t.lastName),'" + searchKey + "')>0");// 姓氏
      hql.append(" or instr(upper(t.insName),'" + searchKey + "')>0");// 机构
      hql.append(" or instr(upper(t.department ),'" + searchKey + "')>0");// 部门
      hql.append(" or instr(upper(t.position ),'" + searchKey + "')>0");// 职称
      // hql.append(" or instr(upper(t.titolo),?)>0");// 头衔
      // params.add(searchKey);
      hql.append(" ) ");
    }

  }

  /**
   * 统计数回显
   * 
   * @param form
   * @param type 1:机构统计数
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> friendsCountCallBack(PsnListViewForm form, int type) {
    String insCount = "select new map(t.insId as insId,count(t.personId) as count)";
    // String regionCount =
    // "select new map(t.regionId as regionId,count(t.personId) as count)";
    StringBuilder hql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    hql.append(" from Person t");
    hql.append(" where exists(select 1 from Friend f where f.psnId=? and f.friendPsnId=t.personId)");
    params.add(form.getPsnId());
    buildSearchKey2(form.getSearchKey(), hql, params);
    // if (StringUtils.isNotBlank(form.getInsId()) && type != 1) {
    // hql.append(" and t.insId in(?) ");
    // params.add(form.getInsId());
    // }
    // if (StringUtils.isNotBlank(form.getRegionId()) && type != 2) {
    // hql.append(" and t.regionId in(" + form.getRegionId() + ") ");
    // }
    if (type == 1) {
      hql.append(" and t.insId is not null");
      hql.append(" group by t.insId");
      return super.createQuery(insCount + hql, params.toArray()).list();
    }
    // else if (type == 2) {
    // hql.append(" and t.regionId is not null");
    // hql.append(" group by t.regionId");
    // return super.createQuery(regionCount + hql, params.toArray()).list();
    // }
    return null;
  }

  /**
   * 获取推荐人员id
   * 
   * @param form
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void queryRecommendPsn(List<Long> psnIds, PsnListViewForm form) {
    String countHql = "select count(t.personId)";
    String listHql = "select t.personId";
    StringBuilder hql = new StringBuilder();
    hql.append(" from Person t");
    hql.append(" where t.personId in(:psnIds)");
    List<Long> insIds = new ArrayList<Long>();
    List<Long> regionIds = new ArrayList<Long>();
    List<Integer> scienceAreaIds = new ArrayList<Integer>();
    // 所在机构
    if (StringUtils.isNotBlank(form.getInsId())) {
      insIds = getLongList(form.getInsId());
      hql.append(" and t.insId in(:insIds)");
    }
    // 所在地区
    if (StringUtils.isNotBlank(form.getRegionId())) {
      regionIds = getLongList(form.getRegionId());
      hql.append(" and t.regionId in(:regionIds)");
    }
    // 研究领域(科技领域)
    if (StringUtils.isNotBlank(form.getScienceAreaId())) {
      scienceAreaIds = getIntegerList(form.getScienceAreaId());
      hql.append(
          " and exists(select 1 from PsnScienceArea p where p.psnId=t.personId and p.status=1 and p.scienceAreaId in(:scienceAreaIds))");
    }
    Page page = form.getPage();
    Query query = super.createQuery(listHql + hql).setParameterList("psnIds", psnIds);
    Query query2 = super.createQuery(countHql + hql).setParameterList("psnIds", psnIds);
    if (CollectionUtils.isNotEmpty(insIds)) {
      query.setParameterList("insIds", insIds);
      query2.setParameterList("insIds", insIds);
    }
    if (CollectionUtils.isNotEmpty(regionIds)) {
      query.setParameterList("regionIds", regionIds);
      query2.setParameterList("regionIds", regionIds);
    }
    if (CollectionUtils.isNotEmpty(scienceAreaIds)) {
      query.setParameterList("scienceAreaIds", scienceAreaIds);
      query2.setParameterList("scienceAreaIds", scienceAreaIds);
    }
    page.setTotalCount((Long) query2.uniqueResult());
    List<Long> result = query.setFirstResult(page.getFirst() - 1 == 0 ? 0 : form.getCurrentLoad())
        .setMaxResults(page.getPageSize()).list();
    page.setResult(result);
  }

  /**
   * 传入数字字符串,返回List<Long>
   * 
   * @param longStr
   * @return
   */
  private List<Long> getLongList(String longStr) {
    List<Long> longList = new ArrayList<Long>();
    if (StringUtils.isNotBlank(longStr)) {
      String[] split = longStr.split(",");
      for (String str : split) {
        longList.add(NumberUtils.toLong(str));
      }
    }
    return longList;
  }

  /**
   * 传入数字字符串,返回List<Integer>
   * 
   * @param integerStr
   * @return
   */
  private List<Integer> getIntegerList(String str) {
    List<Integer> list = new ArrayList<Integer>();
    if (StringUtils.isNotBlank(str)) {
      String[] split = str.split(",");
      for (String s : split) {
        list.add(NumberUtils.toInt(s));
      }
    }
    return list;
  }

  public List<Friend> getFriendsByPsnId(Long psnId) {
    String hql = "from Friend  f where f.psnId =:psnId ";
    List list = this.createQuery(hql).setParameter("psnId", psnId).list();
    return list;
  }

  public List<Long> getFriendId(Long psnId) {
    String hql = "select f.friendPsnId from Friend  f where f.psnId =:psnId ";
    List list = this.createQuery(hql).setParameter("psnId", psnId).list();
    return list;
  }

}
