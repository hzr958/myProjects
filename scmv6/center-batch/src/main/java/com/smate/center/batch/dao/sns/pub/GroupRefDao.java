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
import com.smate.center.batch.model.sns.pub.GroupRefs;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;

/**
 * 群组成果关系Dao.
 * 
 * yanmingzhuang
 */
@Repository
public class GroupRefDao extends SnsHibernateDao<GroupRefs, Long> {

  /**
   * 
   * @param groupId 群组ID
   * @param page 分页信息
   * @param groupPudIds TODO
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public void findGroupRefsList(Long groupId, Page<GroupRefs> page, Long groupFolderId, String groupPudIds,
      String searchKey) throws DaoException {

    String countHql = "select count(t.groupRefsId) ";
    String orderHql = "order by ";
    String listHql = "select t ";
    if (StringUtils.isNotBlank(page.getOrder()) && StringUtils.isNotBlank(page.getOrderBy())) {
      orderHql += this.beforeQuery(page);
    } else {
      orderHql += " t.groupRefsId desc";
    }
    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupRefs t, ConstPubType c ");

    List<Object> params = new ArrayList<Object>();
    hql.append(" where t.typeId=c.id and t.groupId=? ");
    params.add(groupId);

    if (StringUtils.isNotBlank(groupPudIds)) {
      hql.append(" and t.groupRefsId in(" + groupPudIds + ")");
    }
    if (groupFolderId != null) {
      if (groupFolderId == -1) {
        hql.append(" and t.groupFolderIds is null ");
      } else {
        hql.append(" and t.groupFolderIds like ? ");
        params.add("%," + groupFolderId + ",%");
      }
    }

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
  public void findGroupRefsList(Long groupId, Page<GroupRefs> page, String groupPudIds, String searchId,
      String searchName, String searchKey) throws DaoException {

    String countHql = "select count(t.groupRefsId) ";
    String orderHql = "order by ";
    String listHql = "select t ";
    if (StringUtils.isNotBlank(page.getOrder()) && StringUtils.isNotBlank(page.getOrderBy())) {
      orderHql += this.beforeQuery(page);
    } else {
      orderHql += " t.groupRefsId desc";
    }
    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupRefs t, ConstPubType c ");

    List<Object> params = new ArrayList<Object>();
    hql.append(" where t.typeId=c.id and t.groupId=? ");
    params.add(groupId);

    if (StringUtils.isNotBlank(groupPudIds)) {
      hql.append(" and t.groupRefsId in(" + groupPudIds + ")");
    }

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
      }
      /*
       * else if ("group".equals(searchName)) { Long searchIdNum = NumberUtils.toLong(searchId); if
       * (searchIdNum.longValue() == -1) { hql.append(" and t.groupId is null "); } else {
       * hql.append(" and t.groupId = ? "); params.add(searchIdNum); } }
       */
      else if ("list".equals(searchName)) {
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
  public Map<String, Object> sumGroupRefByYear(Long groupId) {
    if (groupId == null) {
      return null;
    }

    String hql =
        "SELECT t.publishYear,count(t.groupRefsId) FROM GroupRefs t WHERE t.groupId=? GROUP BY t.publishYear ORDER BY t.publishYear desc";
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
          orderBy += " nvl(t.citedTimes,'9999')";
        } else {
          orderBy += " nvl(t.citedTimes,'-9999')";
        }
        orderBy += " " + page.getOrder();
        // orderBy += " ,citedTimes desc";
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
        orderBy += "t." + page.getOrderBy() + " " + page.getOrder();
      }

      return orderBy;
    }
    return "";
  }

  /**
   * 
   * @param groupId
   * @param groupFolderId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<GroupRefs> findGroupRefsList(Long groupId, Long groupFolderId) throws DaoException {

    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupRefs t ");

    List<Object> params = new ArrayList<Object>();
    hql.append(" where groupId=? and groupFolderIds like ? ");

    params.add(groupId);
    params.add("%," + groupFolderId + ",%");

    hql.append(" order by groupRefsId desc");

    // 查询数据实体
    Query queryResult = super.createQuery(hql.toString(), params.toArray());
    return queryResult.list();
  }

  /**
   * 设置群组主页查询群组文献列表.
   * 
   * @param param
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map queryRefForHomePageEdit(Map param) {
    Long groupId = (Long) param.get("groupId");
    String countHql = " select count(t.groupRefsId) ";
    String orderHql = " order by t.groupRefsId desc ";

    StringBuilder hql = new StringBuilder();
    hql.append(" from GroupRefs t where groupId=? ");
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
    param.put("refList", result);
    return param;
  }


  /**
   * 统计群组成果数量.
   * 
   * @param groupId
   * @param groupFolderId
   * @return
   * @throws DaoException
   */
  public Integer sumGroupRefs(Long groupId, Long groupFolderId) throws DaoException {
    StringBuilder hql = new StringBuilder();
    hql.append("select count(t.groupRefsId) from GroupRefs t ");
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
   * 按群组ID及文献类别来统计成果
   * 
   * @param groupId 群组ID
   * @param refType 文献类别
   * @return 统计结果
   * @throws DaoException
   */
  public Integer sumGroupRefsByType(Long groupId, Integer refType) throws DaoException {
    StringBuilder hql = new StringBuilder();
    hql.append("select count(t.groupRefsId) from GroupRefs t ");
    List<Object> params = new ArrayList<Object>();
    hql.append(" where groupId=? ");
    params.add(groupId);

    if (refType != null) {
      hql.append(" and typeId = ? ");
      params.add(refType);
    }

    return NumberUtils.toInt(this.findUnique(hql.toString(), params.toArray()) + "");
  }

  /**
   * 获取引用类别文献统计.
   * 
   * @param listType 引用类别
   * @param groupId 群组ID
   * @return
   */
  public int getRefListNum(String listType, Long groupId) {

    StringBuilder hql = new StringBuilder("select count(t.groupRefsId) from GroupRefs t where t.groupId = ? ");

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

  public GroupRefs findGroupRefs(Long groupId, Long pubId, Integer nodeId) throws DaoException {

    String hql = "from GroupRefs where groupId=? and pubId=? and nodeId=?";
    return this.findUnique(hql, groupId, pubId, nodeId);
  }

  public GroupRefs findGroupRefs(Long groupRefsId) throws DaoException {
    String hql = "from GroupRefs where groupRefsId=?";
    return this.findUnique(hql, groupRefsId);
  }

  public void deletePub(Long groupId, Long psnId, Long pubId) {
    String hql = "delete from GroupRefs t where t.groupId=? and t.ownerPsnId=? and t.pubId=?";
    this.createQuery(hql, new Object[] {groupId, psnId, pubId}).executeUpdate();
  }

  /**
   * 唯一主键查询.
   * 
   * @param groupId
   * @param psnId
   * @param pubId
   * @return
   */
  public GroupRefs getGroupRefsByUq(Long groupId, Long psnId, Long pubId) {
    String hql = " from GroupRefs t where t.groupId=? and t.ownerPsnId=? and t.pubId=?";
    return super.findUnique(hql, new Object[] {groupId, psnId, pubId});
  }

  @SuppressWarnings("unchecked")
  public List<GroupRefs> getGroupRefsByIds(String groupPudIds, Long groupId) throws DaoException {
    String hql = "from  GroupRefs t where 1=1";
    List<Object> params = new ArrayList<Object>();
    if (groupId != null) {
      hql += " and groupId=?";
      params.add(groupId);
    }
    if (StringUtils.isNotBlank(groupPudIds)) {
      hql += " and t.groupRefsId in(" + groupPudIds + ")";
    }
    hql = hql + " order by t.groupRefsId desc";

    return super.createQuery(hql, params.toArray()).list();
  }

  public void deletePub(Long groupId, Long pubId) {
    String hql = "delete from GroupRefs t where t.groupId=?  and t.pubId=?";
    this.createQuery(hql, new Object[] {groupId, pubId}).executeUpdate();
  }

  /**
   * 查询群组文献.
   * 
   * @param pubIds
   * @param articleType
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<GroupRefs> findGroupPubByIds(List<Long> pubIds) throws DaoException {
    List params = new ArrayList();
    StringBuilder hql = new StringBuilder("select t");
    hql.append(" from GroupRefs t");
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
      hql.append(" and groupRefsId in (" + pubs + ")");
    }
    hql.append(" order by t.publishYear desc, t.publishMonth desc, t.publishDay desc, t.groupRefsId");

    // 查询数据实体
    return super.createQuery(hql.toString(), params.toArray()).list();
  }

  // ===============编辑标签前检查 start====================
  @SuppressWarnings("unchecked")
  public List<String> changeFileFolders(Long groupId, Object[] refIds) {
    String hql = "select groupFolderIds from GroupRefs where groupId=:groupId and groupRefsId in(:refIds)";
    Query q = super.createQuery(hql).setParameter("groupId", groupId).setParameterList("refIds", refIds);
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
        "select t.nodeId , t.pubId from GroupRefs  t where t.groupRefsId in (" + gPubIds + ") order by t.nodeId asc ";
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

  // ==============人员合并 start============
  @SuppressWarnings("unchecked")
  public List<GroupRefs> getListByPsnId(Long delPsnId, Long groupId) throws DaoException {
    String hql = "from GroupRefs where ownerPsnId=? and groupId=?";
    return super.createQuery(hql, delPsnId, groupId).list();
  }

  @SuppressWarnings("unchecked")
  public List<GroupRefs> queryGroupRefsByRefId(Long refId) throws DaoException {
    String hql = "from GroupRefs t where t.pubId=?";
    return super.createQuery(hql, refId).list();
  }

  // ==============人员合并 end============

  // 获取psndi的文献（教学群组复制功能用zk add）
  @SuppressWarnings("unchecked")
  public List<GroupRefs> getListByPsnId(Long psnId, Long groupId, Integer size) throws DaoException {
    return super.createQuery("from GroupRefs where ownerPsnId=? and groupId=?", psnId, groupId).setMaxResults(100)
        .setFirstResult(size * 100).list();
  }

  public Long getRefsSumByPsnId(Long psnId, Long groupId, Integer isN) throws DaoException {

    String hql = "select count(groupRefsId) from GroupRefs where ownerPsnId=?  and groupId=? ";
    if (isN == 1)
      hql += " and ( groupFolderIds='-1' or groupFolderIds is null )";

    return (Long) super.createQuery(hql, psnId, groupId).uniqueResult();
  }

  /**
   * 查询文献所在的群组id.
   * 
   * @param refId
   * @return
   * @throws DaoException
   */
  @SuppressWarnings("unchecked")
  public List<Long> queryGroupIdListByRefId(Long refId) throws DaoException {
    String hql = "select t.groupId from GroupRefs t where t.pubId=?";
    return super.createQuery(hql, refId).list();
  }
}
