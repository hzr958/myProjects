package com.smate.web.group.dao.group.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.group.form.GroupPsnForm;
import com.smate.web.group.model.group.pub.GroupPubs;

/**
 * 群组 成果关系 冗余 dao
 * 
 * @author tsz
 *
 */

@Repository
public class GroupPubsDao extends SnsHibernateDao<GroupPubs, Long> {

  /**
   * 查询成果列表
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void findGroupPubsListRemould(GroupPsnForm form) {
    Page page = form.getPage();
    StringBuilder hql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    String listHql = "select t ";
    String countHql = "select count(t.groupPubsId) ";
    String newestYearHql = "select max(t.publishYear) ";
    String pubTypeHql = "select new Map(t.typeId as id,count(t.typeId) as count) ";
    String groupHql = "group by t.typeId";

    String orderHql = "order by ";

    hql.append(" from GroupPubs t ");// ,ConstPubType c");
    hql.append(" where t.groupId=? ");
    params.add(form.getGroupId());
    // hql.append(" and t.typeId=c.id ");
    // ------------------------------------排序处理-----------------------------------
    if (StringUtils.isNotBlank(page.getOrderBy())) {
      if ("publishYear".equals(page.getOrderBy())) {// 按年份
        orderHql += " nvl(t.publishYear,0) desc,nvl(t.publishMonth,0) desc,nvl(t.publishDay,0) desc";
      } else if ("citedTimes".equals(page.getOrderBy())) {// 按引用次数
        orderHql += " nvl(t.citedTimes,0) desc";
      } else if ("relevant".equals(page.getOrderBy())) {// 按相关性
        orderHql = "";
      }
    } else {// 默认
      orderHql += "t.groupPubsId desc";
    }
    // ------------------------------------关键词处理-------------------------------
    if (StringUtils.isNotEmpty(form.getSearchKey())) {// 不为空
      String searchKey = form.getSearchKey().toUpperCase().trim();// HtmlUtils.htmlEscape(form.getSearchKey().toUpperCase().trim())去掉了外面的那层HtmlUtils.htmlEscape()
      hql.append(" and ");
      hql.append(" ( ");
      hql.append(" instr(upper(t.zhTitle),?)>0");// 中文标题
      params.add(searchKey);
      hql.append(" or instr(upper(t.enTitle),?)>0");// 英文标题
      params.add(searchKey);
      // hql.append(" or instr(upper(c.zhName),?)>0");// 中文类型
      // params.add(searchKey);
      // hql.append(" or instr(upper(c.enName),?)>0");// 英文类型
      // params.add(searchKey);
      hql.append(" or instr(upper(t.briefDesc),?)>0");// 类型
      params.add(searchKey);
      hql.append(" or instr(upper(t.authorNames),?)>0");// 作者
      hql.append(
          " or exists(select c.id from  ConstPubType c where   instr(upper(c.zhName),?)>0 or instr(upper(c.enName),?)>0 )");
      params.add(searchKey);
      params.add(searchKey);
      params.add(searchKey);
      hql.append(" ) ");

    }
    // 收录情况
    String recordsStr = "";
    String[] records = {"t.listEi", "t.listSci", "t.listIstp", "t.listSsci"};
    for (String r : records) {
      Long recordsCount = super.findUnique(countHql + hql + " and " + r + " =1", params.toArray());
      recordsStr += recordsCount + ",";
    }
    form.setRecordsStr(recordsStr);
    // ------------------------------------筛选类别处理------------------------------------
    if (StringUtils.isNotEmpty(form.getScreenYears())) {// 年份
      hql.append(" and t.publishYear >= ? ");
      params.add(NumberUtils.toInt(form.getScreenYears()));
    }
    if (StringUtils.isNotEmpty(form.getScreenPubTypes())) {// 成果类型
      hql.append(" and t.typeId in(" + form.getScreenPubTypes() + ")");
    }
    if (StringUtils.isNotEmpty(form.getScreenRecords())) {// 收录
      String[] recordArr = form.getScreenRecords().split(",");
      for (String record : recordArr) {
        if ("ei".equalsIgnoreCase(record)) {
          hql.append(" and t.listEi=1");
        } else if ("sci".equalsIgnoreCase(record)) {
          hql.append(" and t.listSci=1");
        } else if ("istp".equalsIgnoreCase(record)) {
          hql.append(" and t.listIstp=1");
        } else if ("ssci".equalsIgnoreCase(record)) {
          hql.append(" and t.listSsci=1");
        }
      }

    }
    // 记录数
    Long totalCount = super.findUnique(countHql + hql, params.toArray());
    page.setTotalCount(totalCount);
    // 最新年份
    Integer publishYear = super.findUnique(newestYearHql + hql, params.toArray());
    form.setNewestpublishYear(publishYear);
    // 成果类别
    String pubTypeStr = "";
    Query querypubType = super.createQuery(pubTypeHql + hql + groupHql, params.toArray());
    List<HashMap> list = (List<HashMap>) querypubType.list();
    for (int i = 0; i < list.size(); i++) {
      HashMap hashMap = list.get(i);
      if (hashMap != null) {
        pubTypeStr += hashMap.get("id") + ",";
      }

    }
    form.setPubTypesStr(pubTypeStr);


    // 查询数据实体
    Query queryResult = super.createQuery(listHql + hql + orderHql, params.toArray());
    queryResult.setFirstResult(page.getFirst() - 1);
    queryResult.setMaxResults(page.getPageSize());
    page.setResult(queryResult.list());

  }

  /**
   * 查询成果列表
   */
  public void findGroupPubsList(GroupPsnForm form) {

    Page page = form.getPage();
    if (StringUtils.isBlank(page.getOrderBy())) {
      page.setOrderBy("publishYear");
      page.setOrder("desc");
    }
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
    params.add(form.getGroupId());
    /*
     * if (StringUtils.isNotBlank(groupPudIds)) { hql.append(" and t.groupPubsId in(" + groupPudIds +
     * ")"); }
     */
    hql.append(" and t.typeId=c.id ");
    if (StringUtils.isNotEmpty(form.getSearchKey())) {// 关键词不为空
      String searchKey = HtmlUtils.htmlEscape(form.getSearchKey()).toUpperCase().trim();
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

    if (StringUtils.isNotBlank(form.getSearchName()) && StringUtils.isNotBlank(form.getSearchId())) {
      // 成果类型 const_pub_type
      String searchName = form.getSearchName();
      String searchId = Des3Utils.decodeFromDes3(form.getSearchId());
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
   * 按群组ID及成果类别来统计成果
   * 
   * @param groupId 群组ID
   * @param pubType 成果类别
   * @return 统计结果
   * @throws DaoException
   */
  public Integer sumGroupPubsByType(Long groupId, Integer pubType) {
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
   * 引用最多的前五条成果
   * 
   * @param groupId
   * @return
   */
  public List<GroupPubs> findFivePubs(Long groupId) {
    String hql = "from GroupPubs g where g.groupId=:groupId  order by  g.citedTimes desc nulls last ";
    return this.createQuery(hql).setParameter("groupId", groupId).setMaxResults(5).list();
  }

  @SuppressWarnings("unchecked")
  public List<GroupPubs> getGroupPubList(Long groupId) {
    String hql = "from GroupPubs t where t.groupId =:groupId";
    return super.createQuery(hql).setParameter("groupId", groupId).list();
  }

  /**
   * 群组动态需要的
   * 
   * @param groupId
   * @param pageNo
   * @param pageSize
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<GroupPubs> getGroupPubListForDyn(GroupPsnForm form) {
    StringBuffer hql = new StringBuffer();
    List<Object> params = new ArrayList<Object>();
    hql.append(" from GroupPubs t where   ");
    if (StringUtils.isNotBlank(form.getSearchKey())) {// 关键词不为空
      // String searchKey = HtmlUtils.htmlEscape(form.getSearchKey()).toUpperCase().trim();
      String searchKey = form.getSearchKey().toUpperCase().trim();
      hql.append(" ( ");
      hql.append(" instr(upper(t.zhTitle),?)>0");// 中文标题
      params.add(searchKey);
      hql.append(" or instr(upper(t.enTitle),?)>0");// 英文标题
      params.add(searchKey);
      hql.append(" or instr(upper(t.briefDesc),?)>0");// 类型
      params.add(searchKey);
      hql.append(" or instr(upper(t.authorNames),?)>0");// 作者
      params.add(searchKey);
      hql.append(" ) ");
      hql.append(" and ");
    }
    params.add(form.getGroupId());
    hql.append("  t.groupId = ? order by t.groupPubsId desc ");
    Query queryResult = super.createQuery(hql.toString(), params.toArray());
    queryResult.setFirstResult((form.getPage().getParamPageNo() - 1) * form.getPage().getPageSize());
    queryResult.setMaxResults(form.getPage().getPageSize());
    return queryResult.list();
  }


}
