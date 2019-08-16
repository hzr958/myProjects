package com.smate.center.batch.dao.sns.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.Query;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.batch.exception.pub.DaoException;
import com.smate.center.batch.model.sns.pub.GroupPubs;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 群组成果关系Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupPubDao extends SnsHibernateDao<GroupPubs, Long> {

  /**
   * 查询群组成果
   * 
   * @param groupId
   * @param page
   * @param groupFolderId
   * @param groupPudIds
   * @param searchKey
   * @throws DaoException
   */
  public void findGroupPubsList(Long groupId, Page<GroupPubs> page, Long groupFolderId, String groupPudIds,
      String searchKey) throws DaoException {

    String listHql = "select t ";
    String countHql = "select count(t.groupPubsId) ";
    String orderHql = "order by ";
    if (StringUtils.isNotBlank(page.getOrder()) && StringUtils.isNotBlank(page.getOrderBy())) {
      orderHql += this.beforeQuery(page);
      if ("citedList".equals(page.getOrderBy()))
        orderHql = "and (t.sourceDbId is null or t.sourceDbId not in(8)) " + orderHql;
    } else {
      orderHql += " t.groupPubsId desc";
    }
    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupPubs t ,ConstPubType c");

    List<Object> params = new ArrayList<Object>();
    hql.append(" where t.groupId=? ");
    params.add(groupId);
    if (StringUtils.isNotBlank(groupPudIds)) {
      hql.append(" and t.groupPubsId in(" + groupPudIds + ")");
    }
    if (groupFolderId != null) {
      if (groupFolderId == -1) {
        hql.append(" and t.groupFolderIds is null ");
      } else {
        hql.append(" and t.groupFolderIds like ? ");
        params.add("%," + groupFolderId + ",%");
      }
    }
    hql.append(" and t.typeId=c.id ");
    if (StringUtils.isNotEmpty(searchKey)) {// 关键词不为空
      searchKey = HtmlUtils.htmlEscape(searchKey).toUpperCase().trim();
      hql.append(" and ");
      hql.append(" ( ");
      hql.append(" instr(upper(t.zhTitle),?)>0");// 中文标题
      params.add(searchKey);
      hql.append(" or instr(upper(t.enTitle),?)>0");// 英文标题
      params.add(searchKey);
      hql.append(" or instr(upper(c.zhName),?)>0");// 中文类型
      params.add(searchKey);
      hql.append(" or instr(upper(c.enName),?)>0");// 英文类型
      params.add(searchKey);
      hql.append(" or instr(upper(t.briefDesc),?)>0");// 类型
      params.add(searchKey);
      hql.append(" or instr(upper(t.authorNames),?)>0");// 作者
      params.add(searchKey);
      hql.append(" ) ");
    }
    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);

    // 查询数据实体
    Query queryResult = super.createQuery(listHql + hql + orderHql, params.toArray());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
  }

  /**
   * 按条件查询群组成果
   * 
   * @param groupId
   * @param page
   * @param groupFolderId
   * @param groupPudIds
   * @param searchId
   * @param searchName
   * @param searchKey
   * @throws DaoException
   */
  public void findGroupPubsList(Long groupId, Page<GroupPubs> page, String groupPudIds, String searchId,
      String searchName, String searchKey) throws DaoException {

    String listHql = "select t ";
    String countHql = "select count(t.groupPubsId) ";
    String orderHql = "order by ";
    if (StringUtils.isNotBlank(page.getOrder()) && StringUtils.isNotBlank(page.getOrderBy())) {
      orderHql += this.beforeQuery(page);
      if ("citedList".equals(page.getOrderBy()))
        orderHql = "and (t.sourceDbId is null or t.sourceDbId not in(8)) " + orderHql;
    } else {
      orderHql += " t.groupPubsId desc";
    }
    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupPubs t ,ConstPubType c");

    List<Object> params = new ArrayList<Object>();
    hql.append(" where t.groupId=? ");
    params.add(groupId);
    if (StringUtils.isNotBlank(groupPudIds)) {
      hql.append(" and t.groupPubsId in(" + groupPudIds + ")");
    }

    hql.append(" and t.typeId=c.id ");
    if (StringUtils.isNotEmpty(searchKey)) {// 关键词不为空
      searchKey = HtmlUtils.htmlEscape(searchKey).toUpperCase().trim();
      hql.append(" and ");
      hql.append(" ( ");
      hql.append(" instr(upper(t.zhTitle),?)>0");// 中文标题
      params.add(searchKey);
      hql.append(" or instr(upper(t.enTitle),?)>0");// 英文标题
      params.add(searchKey);
      hql.append(" or instr(upper(c.zhName),?)>0");// 中文类型
      params.add(searchKey);
      hql.append(" or instr(upper(c.enName),?)>0");// 英文类型
      params.add(searchKey);
      hql.append(" or instr(upper(t.briefDesc),?)>0");// 类型
      params.add(searchKey);
      hql.append(" or instr(upper(t.authorNames),?)>0");// 作者
      params.add(searchKey);
      hql.append(" ) ");
    }

    if (StringUtils.isNotBlank(searchName) && StringUtils.isNotBlank(searchId)) {
      // 成果类型 const_pub_type
      if ("category".equals(searchName)) {
        hql.append(" and t.typeId=?");
        params.add(NumberUtils.toInt(searchId));
      } else if ("folder".equals(searchName)) {
        Long searchIdNum = NumberUtils.toLong(searchId);
        if (searchIdNum.longValue() == -1) {
          hql.append(" and t.groupFolderIds is null ");
        } else {
          hql.append(" and t.groupFolderIds like ? ");
          params.add("%," + searchIdNum + ",%");
        }
      } else if ("publishYear".equals(searchName)) {
        Integer publishYear = NumberUtils.toInt(searchId);
        if (publishYear.longValue() == -1) {
          hql.append(" and t.publishYear is null ");
        } else {
          hql.append(" and t.publishYear = ? ");
          params.add(publishYear);
        }
      } else if ("group".equals(searchName)) {
        Long searchIdNum = NumberUtils.toLong(searchId);
        if (searchIdNum.longValue() == -1) {
          hql.append(" and t.groupId is null ");
        } else {
          hql.append(" and t.groupId = ? ");
          params.add(searchIdNum);
        }
      } else if ("list".equals(searchName)) {
        if (StringUtils.isNotBlank(searchId)) {
          if ("ei".equalsIgnoreCase(searchId)) {
            hql.append(" and t.listEi=1");
          } else if ("sci".equalsIgnoreCase(searchId)) {
            hql.append(" and t.listSci=1");
          } else if ("istp".equalsIgnoreCase(searchId)) {
            hql.append(" and t.listIstp=1");
          } else if ("ssci".equalsIgnoreCase(searchId)) {
            hql.append(" and t.listSsci=1");
          }
        }
      }
    }

    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);

    // 查询数据实体
    Query queryResult = super.createQuery(listHql + hql + orderHql, params.toArray());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
  }

  /**
   * 按年份统计群组成果数量
   * 
   * @return
   */
  public Map<String, Object> sumGroupPubByYear(Long groupId) {
    if (groupId == null) {
      return null;
    }

    String hql =
        "SELECT t.publishYear,count(t.groupPubsId) FROM GroupPubs t WHERE t.groupId=? GROUP BY t.publishYear ORDER BY t.publishYear desc";
    List<Object[]> list = super.createQuery(hql, groupId).list();

    List<Map<String, Object>> countList = new ArrayList<Map<String, Object>>(list.size());
    int notClassifiedCount = 0;

    for (Iterator<Object[]> iterator = list.iterator(); iterator.hasNext();) {
      Object[] obj = iterator.next();

      if (obj[0] == null) {
        notClassifiedCount = Integer.valueOf(ObjectUtils.toString(obj[1]));
        continue;
      }

      Map<String, Object> map = new HashMap<String, Object>(2);

      map.put("year", ObjectUtils.toString(obj[0]));
      map.put("count", ObjectUtils.toString(obj[1]));
      countList.add(map);
    }

    Map<String, Object> result = new HashMap<String, Object>(2);
    result.put("list", countList);
    result.put("notClassifiedCount", notClassifiedCount);

    return result;
  }

  /**
   * 排序处理.
   * 
   * @param page
   * @param form
   */
  private String beforeQuery(Page page) {
    if (("desc".equals(page.getOrder()) || "asc".equals(page.getOrder()) || page.getOrder() == null)
        && StringUtils.isNotBlank(page.getOrderBy())) {
      Locale locale = LocaleContextHolder.getLocale();
      if (page.getOrder() == null) {
        page.setOrder("asc");
      }
      String orderBy = "";
      if ("title".equals(page.getOrderBy())) {
        if ("en".equals(locale.getLanguage())) {
          orderBy = " nvl(t.enTitle,t.zhTitle)";
        } else {
          orderBy = " nvl(t.zhTitle,t.enTitle)";
        }
        orderBy += " " + page.getOrder();
      } else if ("citedList".equals(page.getOrderBy())) {
        if ("asc".equals(page.getOrder())) {
          orderBy += " nvl(t.citedTimes,9999)";
        } else {
          orderBy += " nvl(t.citedTimes,-9999)";
        }
        orderBy += " " + page.getOrder();
        // orderBy += " t.citedTimes desc";
      } else if ("impactFactors".equals(page.getOrderBy())) {
        if ("asc".equals(page.getOrder())) {
          orderBy += " nvl(t.impactFactors,9999) " + page.getOrder();
        } else {
          orderBy += " nvl(t.impactFactors,-9999) " + page.getOrder();
        }
        // orderBy = " t." + page.getOrderBy() + " " + page.getOrder();
      } else if ("publishYear".equals(page.getOrderBy())) {
        if ("asc".equalsIgnoreCase(page.getOrder())) {
          orderBy = " nvl(t.publishYear,9999) asc,nvl(t.publishMonth,99) asc,nvl(t.publishDay,99) asc,t.id ";
        } else {
          orderBy = "  nvl(t.publishYear,0) desc,nvl(t.publishMonth,0) desc,nvl(t.publishDay,0) desc,t.id ";
        }
      } else {
        orderBy = " t." + page.getOrderBy() + " " + page.getOrder();
      }

      return orderBy;
    }
    return null;
  }

  /**
   * 
   * @param groupId
   * @param groupFolderId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<GroupPubs> findGroupPubsList(Long groupId, Long groupFolderId) throws DaoException {

    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupPubs t ");

    List<Object> params = new ArrayList<Object>();
    hql.append(" where groupId=? and groupFolderIds like ? ");

    params.add(groupId);
    params.add("%," + groupFolderId + ",%");

    hql.append(" order by groupPubsId desc");

    // 查询数据实体
    Query queryResult = super.createQuery(hql.toString(), params.toArray());
    return queryResult.list();
  }

  // 重构成果同authorNames时同步更新
  public void updatePubAuthorNames(Long pubId, String newAuthorNames) {
    String hql = "update GroupPubs set authorNames=? where pubId=?";
    super.createQuery(hql, newAuthorNames, pubId).executeUpdate();
  }

  /**
   * 设置群组主页查询群组成果列表.
   * 
   * @param param
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map queryPubForHomePageEdit(Map param) {
    Long groupId = (Long) param.get("groupId");
    String countHql = "select count(t.groupPubsId) ";
    String orderHql = "order by groupPubsId desc";

    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupPubs t  where groupId=? ");
    // 记录数
    Long totalCount = super.findUnique(countHql + hql, groupId);
    param.put("totalCount", totalCount);
    if (totalCount == 0) {
      return param;
    }

    Integer first = (Integer) param.get("first");
    Integer size = (Integer) param.get("pageSize");
    // 查询数据实体
    Query queryResult = super.createQuery(hql + orderHql, groupId);
    queryResult.setFirstResult(first - 1);
    queryResult.setMaxResults(size);
    List<Publication> result = queryResult.list();
    param.put("listSize", result.size());
    param.put("pubList", result);
    return param;
  }

  /**
   * 查看群组主页，成果列表.
   * 
   * @param psnId
   * @param page
   * @param confId
   * @param isAll
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public void queryPubForHomePageView(long groupId, Page<GroupPubs> page, long confId, int isAll) {
    String countHql = " select count(t.groupPubsId) ";
    String orderHql = " order by t.groupPubsId desc ";
    String listHql = " select t ";
    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupPubs t ");

    List<Object> params = new ArrayList<Object>();
    if (isAll == 0) {
      // hql.append(" where ");
      hql.append(",GroupHomePagePub ghp where t.groupPubsId = ghp.id.pubId and ghp.id.confId = ? and ");
      params.add(confId);
    } else {
      hql.append(" where  ");
    }

    hql.append(" t.groupId=? ");
    params.add(groupId);

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
   * 统计群组成果数量.
   * 
   * @param groupId
   * @param groupFolderId
   * @return
   * @throws DaoException
   */
  public Integer sumGroupPubs(Long groupId, Long groupFolderId) throws DaoException {
    StringBuilder hql = new StringBuilder();
    hql.append("select count(t.groupPubsId) from GroupPubs t ");
    List<Object> params = new ArrayList<Object>();
    hql.append(" where groupId=? ");
    params.add(groupId);

    if (groupFolderId != null) {
      if (groupFolderId == -1) {
        hql.append(" and groupFolderIds is null ");
      } else {
        hql.append(" and groupFolderIds like ? ");
        params.add("%," + groupFolderId + ",%");
      }
    }

    return NumberUtils.toInt(this.findUnique(hql.toString(), params.toArray()) + "");
  }

  /**
   * 按群组ID及成果类别来统计成果
   * 
   * @param groupId 群组ID
   * @param pubType 成果类别
   * @return 统计结果
   * @throws DaoException
   */
  public Integer sumGroupPubsByType(Long groupId, Integer pubType) throws DaoException {
    StringBuilder hql = new StringBuilder();
    hql.append("select count(t.groupPubsId) from GroupPubs t ");
    List<Object> params = new ArrayList<Object>();
    hql.append(" where groupId=? ");
    params.add(groupId);

    if (pubType != null) {
      hql.append(" and typeId = ? ");
      params.add(pubType);
    }

    return NumberUtils.toInt(this.findUnique(hql.toString(), params.toArray()) + "");
  }

  /**
   * 获取引用类别成果统计.
   * 
   * @param listType 引用类别
   * @param groupId 群组ID
   * @return
   */
  public int getPubListNum(String listType, Long groupId) {

    StringBuilder hql = new StringBuilder("select count(t.groupPubsId) from GroupPubs t where t.groupId = ? ");

    if ("ei".equalsIgnoreCase(listType)) {
      hql.append(" and t.listEi=1");
    } else if ("sci".equalsIgnoreCase(listType)) {
      hql.append(" and t.listSci=1");
    } else if ("istp".equalsIgnoreCase(listType)) {
      hql.append(" and t.listIstp=1");
    } else if ("ssci".equalsIgnoreCase(listType)) {
      hql.append(" and t.listSsci=1");
    } else {
      return 0;
    }
    Long count = super.findUnique(hql.toString(), groupId);
    return count.intValue();
  }


  public GroupPubs findGroupPubs(Long groupId, Long id, Integer nodeId) throws DaoException {

    String hql = "from GroupPubs where groupId=? and pubId=? and nodeId=?";
    return this.findUnique(hql, groupId, id, nodeId);
  }

  public GroupPubs findGroupPubs(Long groupPubsId) throws DaoException {
    String hql = "from GroupPubs where groupPubsId=?";
    return this.findUnique(hql, groupPubsId);
  }

  public void deletePub(Long groupId, Long psnId, Long pubId) {
    String hql = "delete from GroupPubs t where t.groupId=? and t.ownerPsnId=? and t.pubId=?";
    this.createQuery(hql, new Object[] {groupId, psnId, pubId}).executeUpdate();
  }

  public void deletePub(Long groupId, Long pubId) {
    String hql = "delete from GroupPubs t where t.groupId=?  and t.pubId=?";
    this.createQuery(hql, new Object[] {groupId, pubId}).executeUpdate();
  }

  /**
   * 唯一主键查询.
   * 
   * @param groupId
   * @param psnId
   * @param id
   * @return
   */
  public GroupPubs getGroupPubsByUq(Long groupId, Long psnId, Long id) {
    String hql = "from GroupPubs t where t.groupId=? and t.ownerPsnId=? and t.pubId=?";
    return this.findUnique(hql, new Object[] {groupId, psnId, id});
  }

  @SuppressWarnings("unchecked")
  public List<GroupPubs> getGroupPubsByIds(String groupPudIds, Long groupId) throws DaoException {
    String hql = "from  GroupPubs t where 1=1";
    List<Object> params = new ArrayList<Object>();
    if (groupId != null) {
      hql += " and groupId=?";
      params.add(groupId);
    }
    if (StringUtils.isNotBlank(groupPudIds)) {
      hql += " and t.groupPubsId in(" + groupPudIds + ")";
    }
    hql = hql + " order by t.groupPubsId desc";
    return super.createQuery(hql, params.toArray()).list();
  }

  /**
   * @param groupId
   * @param page
   * @param beginYear
   * @param endYear
   * @param pubTypeId
   * @param folderId
   * @param exPubIds 排除的pubId.
   */
  @SuppressWarnings("unchecked")
  public void findGroupPubsList(Long groupId, Page<GroupPubs> page, Integer beginYear, Integer endYear,
      Integer pubTypeId, Long folderId, String exPubIds) {
    String countHql = "select count(t.groupPubsId) ";
    String orderHql =
        "order by nvl(t.publishYear,0) desc,nvl(t.publishMonth,0) desc,nvl(t.publishDay,0) desc, groupPubsId desc";

    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupPubs t ");

    List<Object> params = new ArrayList<Object>();
    hql.append(" where groupId=? ");
    params.add(groupId);
    if (folderId != null) {
      if (folderId == -1) {
        hql.append(" and groupFolderIds is null ");
      } else {
        hql.append(" and groupFolderIds like ? ");
        params.add("%," + folderId + ",%");
      }
    }
    if (beginYear != null && endYear != null) {
      hql.append(" and publishYear>=? and publishYear<=?");
      params.add(beginYear);
      params.add(endYear);
    } else if (beginYear != null) {
      hql.append(" and publishYear>=?");
      params.add(beginYear);
    } else if (endYear != null) {
      hql.append(" and publishYear<=?");
      params.add(endYear);
    }
    if (StringUtils.isNotBlank(exPubIds)) {
      hql.append(" and pubId not in(" + exPubIds + ")");
    }
    if (pubTypeId != null) {
      hql.append(" and typeId=?");
      params.add(pubTypeId);
    }
    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);

    // 查询数据实体
    Query queryResult = super.createQuery(hql + orderHql, params.toArray());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());
  }

  public List<Integer> findGroupYears(Long groupId) {
    List<Object> params = new ArrayList<Object>();
    String hql = "select distinct t.publishYear from GroupPubs t where publishYear is not null ";
    if (groupId != null) {
      hql += " and t.groupId=?";
      params.add(groupId);
    }
    hql += " order by publishYear ";
    Query queryResult = super.createQuery(hql, params.toArray());
    return queryResult.list();
  }

  /**
   * 查询群组成果.
   * 
   * @param pubIds
   * @param articleType
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<GroupPubs> findGroupPubByIds(List<Long> pubIds) throws DaoException {
    List params = new ArrayList();
    StringBuilder hql = new StringBuilder("select t");
    hql.append(" from GroupPubs t");
    hql.append(" where 1=1 ");

    if (CollectionUtils.isNotEmpty(pubIds)) {
      String pubs = "";
      for (Long pubId : pubIds) {
        if ("".equals(pubs)) {
          pubs = String.valueOf(pubId);
        } else {
          pubs += "," + String.valueOf(pubId);
        }
      }
      hql.append(" and t.groupPubsId in (" + pubs + ")");
    }
    hql.append(" order by t.publishYear desc, t.publishMonth desc, t.publishDay desc, t.groupPubsId");

    // 查询数据实体
    return super.createQuery(hql.toString(), params.toArray()).list();
  }

  // ===============编辑标签前检查 start====================
  @SuppressWarnings("unchecked")
  public List<String> changeFileFolders(Long groupId, Object[] pubIds) {
    String hql = "select groupFolderIds from GroupPubs where groupId=:groupId and groupPubsId in(:pubIds)";
    Query q = super.createQuery(hql).setParameter("groupId", groupId).setParameterList("pubIds", pubIds);
    return q.list();
  }

  /**
   * 查找groupPubIds 对应的pubId和nodeId
   * 
   * @param groupPubIds
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public Map<Integer, List<Long>> getPubNodeIdByPubIds(List<Long> groupPubIds) throws DaoException {
    if (groupPubIds.isEmpty()) {
      throw new DaoException("groupPubIds 不能为空");
    }
    String gPubIds = "";
    if (CollectionUtils.isNotEmpty(groupPubIds)) {
      for (Long pubId : groupPubIds) {
        if ("".equals(gPubIds)) {
          gPubIds = String.valueOf(pubId);
        } else {
          gPubIds += "," + String.valueOf(pubId);
        }
      }
    }
    String hql =
        "select t.nodeId , t.pubId from GroupPubs  t where t.groupPubsId in (" + gPubIds + ") order by t.nodeId asc ";
    Query q = super.createQuery(hql);
    List<Object[]> result = q.list();
    Map<Integer, List<Long>> resMap = new HashMap<Integer, List<Long>>();
    List<Long> tmpList = null;
    for (Object[] obj : result) {
      tmpList = resMap.get(Integer.valueOf(obj[0].toString()));
      if (tmpList == null) {
        tmpList = new ArrayList<Long>();
      }
      tmpList.add(Long.valueOf(obj[1].toString()));
      resMap.put(Integer.valueOf(obj[0].toString()), tmpList);
    }
    return resMap;
  }

  /**
   * 查找成果在开放群组中的数量.
   * 
   * @param pubId
   * @return
   * @throws DaoException
   */
  public Integer findPubCountInOpenGroup(Long pubId) throws DaoException {
    StringBuffer hql = new StringBuffer();
    hql.append("select count(t.groupId) from GroupPubs t");
    hql.append(" where t.pubId=?  ");
    hql.append(" and exists(select 1 from GroupPsnNode g where g.groupId=t.groupId and g.openType in('O','H'))");

    return NumberUtils.toInt(this.findUnique(hql.toString(), new Object[] {pubId}) + "");
  }

  /**
   * 根据群组ID获取群组成果ID及对应节点.
   * 
   * @param groupId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GroupPubs> getGroupPubByGroupId(Long groupId) {
    String hql = "select new GroupPubs(pubId,nodeId) from GroupPubs where groupId=?";
    return super.createQuery(hql, groupId).list();
  }

  // ==============人员合并 start============
  @SuppressWarnings("unchecked")
  public List<GroupPubs> getListByPsnId(Long delPsnId, Long groupId) throws DaoException {
    String hql = "from GroupPubs where ownerPsnId=? and groupId=?";
    return super.createQuery(hql, delPsnId, groupId).list();
  }

  // ==============人员合并 end============

  /**
   * 判断这个成果是不是这个群组的
   */
  public boolean isExistGroupPub(Long groupId, Long pubId) {
    String hql = "select count(*) from GroupPubs t where t.groupId = ? and t.pubId = ?";
    Long count = (Long) super.createQuery(hql, groupId, pubId).uniqueResult();
    if (count != null && count > 0) {
      return true;
    } else {
      return false;
    }
  }

  @SuppressWarnings("unchecked")
  public List<GroupPubs> queryGroupPubsByPubId(Long pubId) throws DaoException {
    String hql = "from GroupPubs t where t.pubId=?";
    return super.createQuery(hql, pubId).list();
  }

  /**
   * 查询成果所在的群组id.
   * 
   * @param pubId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> queryGroupIdListByPubId(Long pubId) throws DaoException {
    String hql = "select t.groupId from GroupPubs t where t.pubId=?";
    return super.createQuery(hql, pubId).list();
  }

  /**
   * 根据群组ID获取群组成果详细信息.
   * 
   * @param groupId
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GroupPubs> getGroupPubList(Long groupId) {
    String hql = "from GroupPubs t where t.groupId =:groupId";
    return super.createQuery(hql).setParameter("groupId", groupId).list();
  }
}
