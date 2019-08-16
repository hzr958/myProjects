package com.smate.web.v8pub.dao.sns;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.psn.model.PsnPubPO;
import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.exception.PubException;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.IrisStringUtils;
import com.smate.web.v8pub.po.PubPO;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.query.PubQueryDTO;
import com.smate.web.v8pub.vo.PsnInfluencePubVO;

/**
 * 个人成果基础信息查询DAO
 * 
 * @author houchuanjie
 * @date 2018/06/01 16:51
 */
@Repository
public class PubSnsDAO extends SnsHibernateDao<PubSnsPO, Long> {
  public final static String INCLUDE_TYPE = "SCIE,SSCI,EI,ISTP,CSSCI,PKU,OTHER";// PubLibraryEnum

  /**
   * 查询个人成果类表
   * 
   * @param pubQueryDTO
   */
  public void queryPubList(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    StringBuilder listHql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    listHql.append(" select t ");
    countHql.append(" select count(t.pubId) ");
    hql.append(" from PubSnsPO t");
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" where");
    hql.append(
        " exists (select 1 from PsnPubPO pp where pp.pubId = t.pubId and pp.ownerPsnId = ?  and  pp.status = 0 )");
    params.add(pubQueryDTO.getSearchPsnId());
    // 检索
    buildSearchKey(pubQueryDTO.getSearchKey(), hql, params);
    // 发表年份
    buildPublishYearSql(pubQueryDTO, hql);
    // 成果类型
    buildPubTypeSql(pubQueryDTO, hql);
    // 收录类别 -- 个人成果
    buildSituationSql(pubQueryDTO, hql);
    // 非本人查看 不存在 小于6=的----》 存在大于6的
    if (!pubQueryDTO.isSelf()) {
      hql.append(" and not exists(select 1 from PsnConfigPub pc where pc.id.pubId=t.pubId and pc.anyUser <=6)");
    }
    // 排序
    String orderString = "";
    orderString = buildOrderSql(pubQueryDTO);
    // 记录数
    Long totalCount = super.findUnique(countHql.toString() + hql, params.toArray());
    pubQueryDTO.setTotalCount(totalCount);
    List<PubPO> resultList;
    if (pubQueryDTO.isQueryAll == false) {// 设置查所有
      resultList = super.createQuery(listHql.toString() + hql + orderString, params.toArray()).list();
    } else {
      resultList = super.createQuery(listHql.toString() + hql + orderString, params.toArray())
          .setFirstResult(pubQueryDTO.getFirst()).setMaxResults(pubQueryDTO.getPageSize()).list();
    }
    pubQueryDTO.setPubList(resultList);
  }

  /**
   * 获取个人隐私成果数量
   *
   * @return
   */
  public Long getPsnPrivatePubCount(Long psnId) {
    String hql = "select count(t.pubId)  from PubSnsPO t where "
        + "exists (select 1 from PsnPubPO pp where pp.pubId = t.pubId and pp.ownerPsnId =:psnId  and  pp.status = 0 ) "
        + "and exists(select 1 from PsnConfigPub pc where pc.id.pubId=t.pubId and pc.anyUser <=6)";
    return (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();

  }

  /**
   * 查询个人成果类表的统计数
   * 
   * @param pubQueryDTO
   */
  public void queryPubCount(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    countHql.append(" select count(t.pubId) ");
    hql.append(" from PubSnsPO t");
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" where");
    hql.append(
        " exists (select 1 from PsnPubPO pp where pp.pubId = t.pubId and pp.ownerPsnId = ?  and  pp.status = 0 )");
    params.add(pubQueryDTO.getSearchPsnId());
    // 检索
    buildSearchKey(pubQueryDTO.getSearchKey(), hql, params);
    // 发表年份
    buildPublishYearSql(pubQueryDTO, hql);
    // 成果类型
    buildPubTypeSql(pubQueryDTO, hql);
    // 收录类别 -- 个人成果
    buildSituationSql(pubQueryDTO, hql);
    // 非本人查看 不存在 小于6=的----》 存在大于6的
    if (!pubQueryDTO.isSelf()) {
      hql.append(" and not exists(select 1 from PsnConfigPub pc where pc.id.pubId=t.pubId and pc.anyUser <=6)");
    }
    // 记录数
    Long totalCount = super.findUnique(countHql.toString() + hql, params.toArray());
    pubQueryDTO.setTotalCount(totalCount);
  }

  private void buildPubTypeSql(PubQueryDTO pubQueryDTO, StringBuilder hql) {
    if (StringUtils.isNotBlank(pubQueryDTO.getPubType())) {
      hql.append(" and t.pubType in(" + pubQueryDTO.getPubType() + ") ");
    } else if (StringUtils.isNotBlank(pubQueryDTO.getSearchPubType())) {
      hql.append(" and t.pubType in(" + pubQueryDTO.getSearchPubType() + ") ");
    }
  }

  private void buildPublishYearSql(PubQueryDTO pubQueryDTO, StringBuilder hql) {
    if (StringUtils.isNotBlank(pubQueryDTO.getPublishYear())) {
      String[] split = pubQueryDTO.getPublishYear().split(",");
      StringBuilder sb = new StringBuilder();
      for (String str : split) {
        sb.append("'" + str.trim() + "',");
      }
      hql.append(" and  t.publishYear in(" + sb.substring(0, sb.length() - 1) + ") ");
    }
  }

  /**
   * 构建收录 过滤
   * 
   * @param pubQueryDTO
   * @param hql
   */
  private void buildSituationSql(PubQueryDTO pubQueryDTO, StringBuilder hql) {
    if (StringUtils.isNotBlank(pubQueryDTO.getIncludeType())) {
      String includeType = Stream.of(pubQueryDTO.getIncludeType().split(","))
          .map(item -> "'" + item.trim().toUpperCase() + "'").collect(Collectors.joining(","));
      includeType = includeType.replaceAll("(?i)'scie?'", "'SCIE','SCI'");// scie和sci都有

      hql.append(" and  exists (select 1 from PubSituationPO ps ");
      hql.append(" where ps.pubId = t.pubId  and ps.libraryName in ( " + includeType + " )  and ps.sitStatus = 1)");
    }
  }

  /**
   * 构建排序的sql
   * 
   * @param pubQueryDTO
   * @return
   */
  private String buildOrderSql(PubQueryDTO pubQueryDTO) {
    String orderString = "";
    switch (pubQueryDTO.getOrderBy()) {
      case "gmtModified":
      case "updateDate":
        orderString = " order by t.gmtModified desc nulls last ,t.pubId desc";
        break;
      case "citations":
      case "citedTimes":
        orderString = " order by  t.citations  desc nulls last,t.pubId desc";
        break;
      case "title":
        orderString = " order by  t.title      desc nulls last,t.pubId desc";
        break;
      case "publishDate":
      case "publishYear":
        /**
         * 由于mobile_find_psn_pub_conditions.jsp中id="orderBy_pubLishDate"命名不规范影响,
         * 并且改动该值又会影响个人成果列表条件筛选的回显问题或其他未知问题,所以添加该值以适应移动端个人成果列表默认为发表年份排序的问题
         */
      case "pubLishDate":
        orderString =
            " order by  nvl(t.publishYear,0) desc,nvl(t.publishMonth,0) desc,nvl(t.publishDay,0) desc,t.pubId desc";
        break;
      case "openPubOrder":
        // 代表成果，公开成果列表的默认排序:
        orderString = " order by  t.pubId desc ,t.publishYear   desc nulls last";
        break;
      case "importMemberPubOrder":
      case "createDate":
        // 导入成员成果的默认排序
        orderString = "  order by  t.gmtCreate desc nulls last";
        break;
      default:
        // 个人成果列表的默认排序
        orderString =
            " order by  nvl(t.publishYear,0) desc,nvl(t.publishMonth,0) desc,nvl(t.publishDay,0) desc,t.pubId desc";
        break;
    }
    return orderString;
  }

  /**
   * 
   * @param searchKey
   * @param hql
   * @param params
   */
  private void buildSearchKey(String searchKey, StringBuilder hql, List<Object> params) {
    if (StringUtils.isNotBlank(searchKey)) {
      // searchKey = StringEscapeUtils.unescapeHtml4(searchKey);
      // 改成转换为%的方式
      searchKey = IrisStringUtils.tranSqlDefineChr(searchKey);
      searchKey = searchKey.toUpperCase().trim();
      hql.append(" and ");
      hql.append(" ( ");
      hql.append(" upper(t.title) like ? ");// 标题
      params.add("%" + searchKey + "%");
      hql.append(" or instr(upper(t.briefDesc),?)>0");// 来源
      params.add(searchKey);
      hql.append(" or instr(upper(t.authorNames),?)>0");// 作者
      params.add(searchKey);
      hql.append(" ) ");
    }
  }

  /**
   * 查询群组成果列表
   * 
   * @param pubQueryDTO
   */
  public void queryGrpPubList(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    StringBuilder listHql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    listHql.append(" select t ");
    countHql.append(" select count(t.pubId) ");
    hql.append(" from PubSnsPO t ,GroupPubPO gp");
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" where");
    // 项目群组查询的成果
    if (pubQueryDTO.isSearchPrjPub() && !pubQueryDTO.isSearchRefPub()) {
      hql.append(" gp.grpId = ?  and  gp.pubId = t.pubId  and gp.isProjectPub = 1 and  gp.status = 0 ");
    } else if (!pubQueryDTO.isSearchPrjPub() && pubQueryDTO.isSearchRefPub()) {
      hql.append(" gp.grpId = ?  and  gp.pubId = t.pubId  and gp.isProjectPub = 0  and  gp.status = 0 ");
    } else {
      // 群组查询的成果 课程群组
      hql.append("  gp.grpId = ?  and  gp.pubId = t.pubId and gp.status = 0 ");
    }
    params.add(pubQueryDTO.getSearchGrpId());
    // 群组成员成果过滤
    if (pubQueryDTO.getSearchGrpMemberId() != null && pubQueryDTO.getSearchGrpMemberId() != 0L) {
      hql.append(" and gp.ownerPsnId = ? ");
      params.add(pubQueryDTO.getSearchGrpMemberId());
    }
    // 检索
    buildSearchKey(pubQueryDTO.getSearchKey(), hql, params);
    // 发表年份
    buildPublishYearSql(pubQueryDTO, hql);
    // 成果类型
    buildPubTypeSql(pubQueryDTO, hql);
    // 收录类别 -- 个人成果
    buildSituationSql(pubQueryDTO, hql);
    // 排序
    String orderString = "";
    if ("publishDate".equals(pubQueryDTO.getOrderBy()) || "publishYear".equalsIgnoreCase(pubQueryDTO.getOrderBy())) {
      orderString =
          " order by nvl(t.publishYear,0) desc,nvl(t.publishMonth,0) desc,nvl(t.publishDay,0) desc,t.pubId desc";
    } else if ("citations".equals(pubQueryDTO.getOrderBy())
        || "citedTimes".equalsIgnoreCase(pubQueryDTO.getOrderBy())) {
      orderString = " order by  t.citations  desc nulls last,t.pubId desc";
    } else {
      orderString = " order by gp.createDate desc nulls last , t.pubId desc ";
    }
    // 记录数
    if (pubQueryDTO.isSearchCount()) {
      Long totalCount = super.findUnique(countHql.toString() + hql, params.toArray());
      pubQueryDTO.setTotalCount(totalCount);
    }
    List<PubPO> resultList = super.createQuery(listHql.toString() + hql + orderString, params.toArray())
        .setFirstResult(pubQueryDTO.getFirst()).setMaxResults(pubQueryDTO.getPageSize()).list();
    pubQueryDTO.setPubList(resultList);
  }

  /**
   * 查询动态我的成果列表
   * 
   * @param pubQueryDTO
   */
  public void queryDynMyPub(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    StringBuilder listHql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    listHql.append(" select t ");
    countHql.append(" select count(t.pubId) ");
    hql.append(" from PubSnsPO t");
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" where");
    if (pubQueryDTO.getSearchGrpId() != null && pubQueryDTO.getSearchGrpId() != 0L) {
      // 查询群组成果
      hql.append(" exists (select 1 from GrpPubs gp where gp.pubId = t.pubId and gp.grpId = ?  and  gp.status = 0 )");
      params.add(pubQueryDTO.getSearchGrpId());
    } else {
      hql.append(
          " exists (select 1 from PsnPubPO pp where pp.pubId = t.pubId and pp.ownerPsnId = ?  and  pp.status = 0 )");
      params.add(pubQueryDTO.getSearchPsnId());
    }
    // 检索
    buildSearchKey(pubQueryDTO.getSearchKey(), hql, params);
    // 排序
    String orderString = " order by t.gmtCreate desc nulls last,t.pubId";
    // 记录数
    Long totalCount = super.findUnique(countHql.toString() + hql, params.toArray());
    pubQueryDTO.setTotalCount(totalCount);
    List<PubPO> resultList = super.createQuery(listHql.toString() + hql + orderString, params.toArray())
        .setFirstResult(pubQueryDTO.getFirst()).setMaxResults(pubQueryDTO.getPageSize()).list();
    pubQueryDTO.setPubList(resultList);
  }

  /**
   * 
   * @param pubQueryDTO
   * @param type 1=成果类型统计数 2=年份类型统计数 3\其他=收录类别统计数
   * @return
   * @throws PubException
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> querySnsPubCountList(PubQueryDTO pubQueryDTO, int type) {
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    String pubTypecount = " select new Map(nvl(t.pubType,7) as pubType,count(t.pubId) as count) ";
    String includeType = " select new Map( s.libraryName as libraryName  ,count(t.pubId) as count) ";
    String publishYearcount = " select new Map( t.publishYear as publishDate,count(t.pubId) as count) ";
    StringBuilder hql = new StringBuilder();
    hql.append(" from PubSnsPO t ");
    if (type == 3) {
      hql.append("  ,PubSituationPO s ");
    }
    hql.append(" where");
    hql.append(
        " exists (select 1 from PsnPubPO pp where pp.pubId = t.pubId and pp.ownerPsnId = ?  and  pp.status = 0 )");
    params.add(pubQueryDTO.getSearchPsnId());
    // 检索
    buildSearchKey(pubQueryDTO.getSearchKey(), hql, params);
    // 发表年份
    if (StringUtils.isNotEmpty(pubQueryDTO.getPublishYear()) && type != 2) {
      buildPublishYearSql(pubQueryDTO, hql);
    }
    // 成果类型
    if (StringUtils.isNotEmpty(pubQueryDTO.getPubType()) && type != 1) {
      buildPubTypeSql(pubQueryDTO, hql);
    }
    // 收录类别
    if (StringUtils.isNotEmpty(pubQueryDTO.getIncludeType()) && (type != 3)) {
      if (StringUtils.isBlank(pubQueryDTO.getIncludeType())) {
        pubQueryDTO.setIncludeType(INCLUDE_TYPE);
      }
      buildSituationSql(pubQueryDTO, hql);
    }
    if (!pubQueryDTO.isSelf()) {// 查看他人的个人成果
      hql.append(" and not exists(select 1 from PsnConfigPub pc where pc.id.pubId=t.pubId and pc.anyUser <=6)");
    }
    if (type == 1) {// 成果类型统计数
      hql.append(" group by nvl(t.pubType,7) ");
      return this.createQuery(pubTypecount + hql.toString(), params.toArray()).list();
    } else if (type == 2) {// 年份统计数
      hql.append(" group by  t.publishYear ");
      return this.createQuery(publishYearcount + hql.toString(), params.toArray()).list();
    } else {// 收录类别统计数
      hql.append("  and t.pubId = s.pubId  and  s.sitStatus = 1 ");
      hql.append("  group by  s.libraryName  ");
      return this.createQuery(includeType + hql.toString(), params.toArray()).list();
    }
  }

  /**
   * 群组成果统计数列表
   * 
   * @param pubQueryDTO
   * @param type 1=成果类型统计数 2=年份类型统计数 3\其他=收录类别统计数
   * @return
   * @throws PubException
   */
  @SuppressWarnings("unchecked")
  public List<Map<String, Object>> queryGrpPubCountList(PubQueryDTO pubQueryDTO, int type) {
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    String pubTypecount = " select new Map(nvl(t.pubType,7) as pubType,count(t.pubId) as count) ";
    String includeType = " select new Map( s.libraryName as libraryName  ,count(t.pubId) as count) ";
    String publishYearcount = " select new Map(t.publishYear as publishDate,count(t.pubId) as count) ";
    StringBuilder hql = new StringBuilder();
    hql.append(" from PubSnsPO t ");
    if (type == 3) {
      hql.append("  ,PubSituationPO s ");
    }
    hql.append(" where");
    // 群组成员成果过滤
    if (pubQueryDTO.getSearchGrpMemberId() != null && pubQueryDTO.getSearchGrpMemberId() != 0L) {
      // 项目群组查询的成果
      if (pubQueryDTO.isSearchPrjPub() && !pubQueryDTO.isSearchRefPub()) {
        hql.append(
            " exists (select 1 from GroupPubPO gp where   gp.grpId = ?  and  gp.pubId = t.pubId  and  gp.ownerPsnId = ? and gp.isProjectPub = 1 and  gp.status = 0 )");
      } else if (!pubQueryDTO.isSearchPrjPub() && pubQueryDTO.isSearchRefPub()) {
        hql.append(
            " exists (select 1 from GroupPubPO gp where  gp.grpId = ?  and  gp.pubId = t.pubId  and   gp.ownerPsnId = ?  and gp.isProjectPub = 0  and  gp.status = 0 ) ");
      } else {
        // 群组查询的成果 课程群组
        hql.append(
            "  exists (select 1 from GroupPubPO gp where  gp.grpId = ?  and  gp.pubId = t.pubId and  gp.ownerPsnId = ? and gp.status = 0 )");
      }
      params.add(pubQueryDTO.getSearchGrpId());
      params.add(pubQueryDTO.getSearchGrpMemberId());
    } else {
      // 项目群组查询的成果
      if (pubQueryDTO.isSearchPrjPub() && !pubQueryDTO.isSearchRefPub()) {
        hql.append(
            " exists (select 1 from GroupPubPO gp where   gp.grpId = ?  and  gp.pubId = t.pubId  and gp.isProjectPub = 1 and  gp.status = 0 )");
      } else if (!pubQueryDTO.isSearchPrjPub() && pubQueryDTO.isSearchRefPub()) {
        hql.append(
            " exists (select 1 from GroupPubPO gp where  gp.grpId = ?  and  gp.pubId = t.pubId  and gp.isProjectPub = 0  and  gp.status = 0 ) ");
      } else {
        // 群组查询的成果 课程群组
        hql.append(
            "  exists (select 1 from GroupPubPO gp where  gp.grpId = ?  and  gp.pubId = t.pubId and gp.status = 0 )");
      }
      params.add(pubQueryDTO.getSearchGrpId());
    }
    // 检索
    buildSearchKey(pubQueryDTO.getSearchKey(), hql, params);
    // 发表年份
    if (StringUtils.isNotEmpty(pubQueryDTO.getPublishYear()) && type != 2) {
      buildPublishYearSql(pubQueryDTO, hql);
    }
    // 成果类型
    if (StringUtils.isNotEmpty(pubQueryDTO.getPubType()) && type != 1) {
      buildPubTypeSql(pubQueryDTO, hql);
    }
    // 收录类别
    if (StringUtils.isNotEmpty(pubQueryDTO.getIncludeType()) && (type != 3)) {
      if (StringUtils.isBlank(pubQueryDTO.getIncludeType())) {
        pubQueryDTO.setIncludeType(INCLUDE_TYPE);
      }
      buildSituationSql(pubQueryDTO, hql);
    }
    /*
     * if (!pubQueryDTO.isSelf()) {// 查看他人的个人成果 hql.
     * append(" and not exists(select 1 from PsnConfigPub pc where pc.id.pubId=t.pubId and pc.anyUser <=6)"
     * ); }
     */
    if (type == 1) {// 成果类型统计数
      hql.append(" group by nvl(t.pubType,7) ");
      return this.createQuery(pubTypecount + hql.toString(), params.toArray()).list();
    } else if (type == 2) {// 年份统计数
      hql.append(" group by  t.publishYear ");
      return this.createQuery(publishYearcount + hql.toString(), params.toArray()).list();
    } else {// 收录类别统计数
      hql.append("  and t.pubId = s.pubId  and  s.sitStatus = 1 ");
      hql.append("  group by  s.libraryName  ");
      return this.createQuery(includeType + hql.toString(), params.toArray()).list();
    }
  }

  /**
   * 查询个人成果类表给open系统
   * 
   * @param pubQueryDTO
   */
  public void queryPubListForOpen(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    StringBuilder listHql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    listHql.append(" select t ");
    countHql.append(" select count(t.pubId) ");
    hql.append(" from PubSnsPO t");
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" where");
    if (pubQueryDTO.getSearchPsnIdList() != null && pubQueryDTO.getSearchPsnIdList().size() > 0) {
      String psnIds = "";
      for (int i = 0; i < pubQueryDTO.getSearchPsnIdList().size(); i++) {
        Object obj = pubQueryDTO.getSearchPsnIdList().get(i);
        psnIds = psnIds + obj.toString() + ",";
      }
      psnIds = psnIds.substring(0, psnIds.length() - 1);
      // 是否查询所有成果 true:查询所有 false:查询未被删除的成果
      if (!pubQueryDTO.isQueryAll) {
        hql.append(" exists (select 1 from PsnPubPO pp where pp.pubId = t.pubId and pp.ownerPsnId in (" + psnIds
            + ")  and pp.status = 0 )");
      } else {
        hql.append(
            " exists (select 1 from PsnPubPO pp where pp.pubId = t.pubId and pp.ownerPsnId in (" + psnIds + ") )");
      }
    } else {
      // 是否查询所有成果 true:查询所有 false:查询未被删除的成果
      if (!pubQueryDTO.isQueryAll) {
        hql.append(
            " exists (select 1 from PsnPubPO pp where pp.pubId = t.pubId and pp.ownerPsnId = ? and pp.status = 0  )");
      } else {
        hql.append(" exists (select 1 from PsnPubPO pp where pp.pubId = t.pubId and pp.ownerPsnId = ? )");
      }

      params.add(pubQueryDTO.getSearchPsnId());
    }

    // 排除成果id的判断
    if (pubQueryDTO.getExcludePsnIdList() != null && pubQueryDTO.getExcludePsnIdList().size() > 0) {
      String pubIds = "";
      for (Long pubId : pubQueryDTO.getExcludePsnIdList()) {
        pubIds = pubIds + pubId + ",";
      }
      pubIds = pubIds.substring(0, pubIds.length() - 1);
      hql.append("  and t.pubId not in ( " + pubIds + " ) ");
    }
    /**
     * 公开成果
     */
    if (pubQueryDTO.isOpenPub()) {
      hql.append(" and not exists(select 1 from PsnConfigPub pc where pc.id.pubId=t.pubId and pc.anyUser <=6)");
    }
    // 成果类型
    buildPubTypeSql(pubQueryDTO, hql);
    if (StringUtils.isNotEmpty(pubQueryDTO.getUuid())) {
      hql.append(" and t.pubId not in(select t3.pubId from IrisExcludedPub t3 where t3.uuid=?)");
      params.add(pubQueryDTO.getUuid());
    }
    if (StringUtils.isNotBlank(pubQueryDTO.getAuthorNames())) {
      hql.append(" and lower(t.authorNames) like '%" + pubQueryDTO.getAuthorNames().toLowerCase().trim() + "%'");
    }
    // 检索标题的关键词*****注意**注意
    if (StringUtils.isNotBlank(pubQueryDTO.getSearchKey())) {
      hql.append(" and lower(t.title) like '%" + pubQueryDTO.getSearchKey().toLowerCase().trim() + "%'");
    }
    if (pubQueryDTO.getBeginPublishYear() != null && pubQueryDTO.getBeginPublishYear() > 0) {
      hql.append(" and t.publishYear>= " + pubQueryDTO.getBeginPublishYear());
    }
    if (pubQueryDTO.getEndPublishYear() != null && pubQueryDTO.getEndPublishYear() > 0) {
      hql.append(" and t.publishYear<=" + pubQueryDTO.getEndPublishYear());
    }
    // 成果更新时间
    if (pubQueryDTO.getPubUpdateDate() != null) {
      hql.append(" and t.gmtModified >= ? ");
      params.add(pubQueryDTO.getPubUpdateDate());
    }
    if ("gmtModified".equals(pubQueryDTO.getOrderBy())) {
      hql.append(" order by t.gmtModified desc, t.pubId desc");
    } else if ("publishYear".equals(pubQueryDTO.getOrderBy())) {
      hql.append(" order by nvl(t.publishYear,0) desc, t.pubId desc");
    } else {
      hql.append(" order by t.pubId desc");
    }
    // 记录数
    Long totalCount = super.findUnique(countHql.toString() + hql, params.toArray());
    pubQueryDTO.setTotalCount(totalCount);
    List<PubPO> resultList = super.createQuery(listHql.toString() + hql, params.toArray())
        .setFirstResult(pubQueryDTO.getFirst()).setMaxResults(pubQueryDTO.getPageSize()).list();
    pubQueryDTO.setPubList(resultList);
  }

  public List<PubSnsPO> getCollectedPubs(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    hql.append("from PubSnsPO t ");
    hql.append(" where t.status = 0 ");
    hql.append(" and exists (select 1 from CollectedPub a where a.psnId=? and a.pubDb=? and a.pubId = t.pubId) ");
    params.add(pubQueryDTO.getSearchPsnId());
    params.add(PubDbEnum.SNS);
    // 收录类别
    buildSituationSql(pubQueryDTO, hql);
    // 构建检索条件
    buildSearchKey(pubQueryDTO.getSearchKey(), hql, params);
    // 发表年份
    if (StringUtils.isNotEmpty(pubQueryDTO.getPublishYear())) {
      hql.append(" and t.publishYear  in(" + pubQueryDTO.getPublishYear() + ") ");
    }
    // 成果类型
    if (StringUtils.isNotEmpty(pubQueryDTO.getPubType())) {
      hql.append(" and t.pubType in(" + pubQueryDTO.getPubType() + ") ");
    }
    return super.createQuery(hql.toString(), params.toArray()).list();
  }

  public List<PubSnsPO> getCollectedPubs(List<Long> pubSnsIds) {
    String hql = "from PubSnsPO t where t.pubId in (:pubIds)";
    return super.createQuery(hql).setParameterList("pubIds", pubSnsIds).list();
  }

  /**
   * 查询群组成果类表给open系统
   * 
   * @param pubQueryDTO
   */
  public void queryGrpPubListForOpen(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    StringBuilder listHql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    listHql.append(" select t ");
    countHql.append(" select count(t.pubId) ");
    hql.append(" from PubSnsPO t");
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" where");
    // 成果更新时间
    if (pubQueryDTO.getPubUpdateDate() != null) {
      hql.append(
          " exists (select 1 from GroupPubPO gp where gp.pubId = t.pubId and gp.grpId = ? and gp.updateDate >= ? )");
      params.add(pubQueryDTO.getSearchGrpId());
      params.add(pubQueryDTO.getPubUpdateDate());
    } else {
      hql.append(" exists (select 1 from GroupPubPO gp where gp.pubId = t.pubId and gp.grpId = ? )");
      params.add(pubQueryDTO.getSearchGrpId());
    }

    // 成果类型
    buildPubTypeSql(pubQueryDTO, hql);

    // 排序 ??
    String orderString = "";
    if ("pubId".equals(pubQueryDTO.getOrderBy())) {
      orderString = " order by t.pubId desc ";
    }
    // 记录数
    Long totalCount = super.findUnique(countHql.toString() + hql, params.toArray());
    pubQueryDTO.setTotalCount(totalCount);
    List<PubPO> resultList = super.createQuery(listHql.toString() + hql + orderString, params.toArray())
        .setFirstResult(pubQueryDTO.getFirst()).setMaxResults(pubQueryDTO.getPageSize()).list();
    pubQueryDTO.setPubList(resultList);
  }

  public void queryAllPubListForOpen(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    StringBuilder listHql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    listHql.append(" select t ");
    countHql.append(" select count(t.pubId) ");
    hql.append(" from PubSnsPO t");
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" where");
    hql.append(
        " exists (select 1 from PsnPubPO pp where pp.pubId = t.pubId and pp.ownerPsnId = ?  and  pp.status = 0 )");
    params.add(pubQueryDTO.getSearchPsnId());
    if (StringUtils.isNotBlank(pubQueryDTO.getPubType())) {
      hql.append(" and t.pubType in (" + pubQueryDTO.getPubType() + " ) ");
    }
    // 排序 ??
    String orderString = "";
    Long totalCount = super.findUnique(countHql.toString() + hql, params.toArray());
    pubQueryDTO.setTotalCount(totalCount);
    List<PubPO> resultList = super.createQuery(listHql.toString() + hql + orderString, params.toArray()).list();
    pubQueryDTO.setPubList(resultList);
  }

  public void queryByPubIds(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    hql.append(" select t from PubSnsPO t where  t.pubId in (:ids ) and t.status = 0");
    if ("citations".equals(pubQueryDTO.getOrderBy())) {
      hql.append(" order by nvl(t.citations,0) desc,t.pubId desc");
    }
    List<PubPO> resultList =
        super.createQuery(hql.toString()).setParameterList("ids", pubQueryDTO.getSearchPubIdList()).list();
    pubQueryDTO.setPubList(resultList);
  }

  @SuppressWarnings("unchecked")
  public List<PubPO> queryPubPOByPubIds(List<Long> idList) {
    StringBuilder hql = new StringBuilder();
    hql.append(" select t from PubSnsPO t where  t.pubId in (:ids )");
    hql.append(" order by decode(t.pubId,");
    for (int i = 0; i < idList.size(); i++) {
      hql.append(idList.get(i) + "," + (i + 1) + ",");
    }
    String str = hql.toString();
    str = str.substring(0, str.lastIndexOf(","));
    hql.delete(0, hql.length());
    hql.append(str);
    hql.append(")");
    List<PubPO> resultList = super.createQuery(hql.toString()).setParameterList("ids", idList).list();
    return resultList;
  }

  /**
   * 查询所有的成果
   * 
   * @param pubQueryDTO
   */
  public void queryPsnPublicPubForOpen(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    StringBuilder listHql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    listHql.append(" select t ");
    countHql.append(" select count(t.pubId) ");
    hql.append(" from PubSnsPO t");
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" where ");
    hql.append(
        " exists (select 1 from PsnPubPO pp where pp.pubId = t.pubId and pp.ownerPsnId = ?  and  pp.status = 0 )");
    params.add(pubQueryDTO.getSearchPsnId());
    if (StringUtils.isNotEmpty(pubQueryDTO.getPubType())) {
      hql.append(" and t.pubType in(" + pubQueryDTO.getPubType() + ") ");
    }
    if (StringUtils.isNotEmpty(pubQueryDTO.getUuid())) {
      hql.append(" and t.pubId not in(select t3.pubId from IrisExcludedPub t3 where t3.uuid=?)");
      params.add(pubQueryDTO.getUuid());
    }
    if (pubQueryDTO.getPermissions() != null && pubQueryDTO.getPermissions().size() > 0) {
      String permissions = "";
      for (Integer permission : pubQueryDTO.getPermissions()) {
        permissions = permissions + permission + ",";
      }
      permissions = permissions.substring(0, permissions.length() - 1);
      hql.append(
          " and exists(select 1 from PsnConfig pc,PsnConfigPub pcp where pc.cnfId=pcp.id.cnfId and pcp.id.pubId=t.pubId and pcp.anyUser in("
              + permissions + ") )");
    }
    if (StringUtils.isNotBlank(pubQueryDTO.getSearchKey())) {
      hql.append(" and lower(t.title) like '%" + pubQueryDTO.getSearchKey() + "%'");
    }
    String orderSql = "";
    if (pubQueryDTO.getOrderBy().equals("gmtModified")) {
      orderSql = " order by  t.gmtModified desc ";
    }
    Long totalCount = super.findUnique(countHql.toString() + hql, params.toArray());
    pubQueryDTO.setTotalCount(totalCount);
    List<PubPO> resultList = super.createQuery(listHql.toString() + hql + orderSql, params.toArray()).list();
    pubQueryDTO.setPubList(resultList);
  }

  public Long getPsnNotExistsResumePubCount(Long psnId) {
    String hql =
        "select count(t.pubId) from PubSnsPO t where exists (select 1 from PsnPubPO pp where pp.pubId = t.pubId and pp.ownerPsnId = :psnId  and  pp.status = :status )";
    hql += " and not exists(select 1 from PsnConfigPub t1 where t.pubId=t1.id.pubId)";
    return (Long) super.createQuery(hql).setParameter("status", 0).setParameter("psnId", psnId).uniqueResult();
  }

  public void queryPsnPubListForOpen(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    StringBuilder listHql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    listHql.append(" select t ");
    countHql.append(" select count(t.pubId) ");
    hql.append(" from PubSnsPO t");
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" where ");
    hql.append(
        " exists (select 1 from PsnPubPO pp where pp.pubId = t.pubId and pp.ownerPsnId = ?  and  pp.status = 0 )");
    params.add(pubQueryDTO.getSearchPsnId());
    if (StringUtils.isNotEmpty(pubQueryDTO.getPubType())) {
      hql.append(" and t.pubType in(" + pubQueryDTO.getPubType() + ") ");
    }
    if (StringUtils.isNotEmpty(pubQueryDTO.getUuid())) {
      hql.append(" and t.pubId not in(select t3.pubId from IrisExcludedPub t3 where t3.uuid=?)");
      params.add(pubQueryDTO.getUuid());
    }
    if (StringUtils.isNotBlank(pubQueryDTO.getAuthorNames())) {
      hql.append(" and lower(t.authorNames) like '%" + pubQueryDTO.getAuthorNames() + "%'");
    }
    if (StringUtils.isNotBlank(pubQueryDTO.getSearchKey())) {
      hql.append(" and lower(t.title) like '%" + pubQueryDTO.getSearchKey() + "%'");
    }
    if (pubQueryDTO.getBeginPublishYear() != null && pubQueryDTO.getBeginPublishYear() > 0) {
      hql.append(" and t.publishYear>=:beginPublishYear ");
    }
    if (pubQueryDTO.getEndPublishYear() != null && pubQueryDTO.getEndPublishYear() > 0) {
      hql.append(" and t.publishYear<=:endPublishYear ");
    }
    if ("updateTime".equals(pubQueryDTO.getOrderBy())) {
      hql.append(" order by t.gmtModified desc, t.pubId desc");
    } else {
      hql.append(" order by nvl(t.publishYear,0) desc,t.pubId desc");
    }
    Long totalCount = super.findUnique(countHql.toString() + hql, params.toArray());
    pubQueryDTO.setTotalCount(totalCount);
    List<PubPO> resultList = super.createQuery(listHql.toString() + hql, params.toArray())
        .setFirstResult(pubQueryDTO.getFirst()).setMaxResults(pubQueryDTO.getPageSize()).list();
    pubQueryDTO.setPubList(resultList);
  }

  /**
   * 是否是成果的拥有者
   * 
   * @param psnId
   * @param pubId
   * @return
   */
  public boolean isOwnerOfPub(Long psnId, Long pubId) {
    String hql = "select t.pubId from PsnPubPO t where t.pubId = :pubId and t.ownerPsnId = :psnId and t.status = 0";
    Long id = (Long) super.createQuery(hql).setParameter("psnId", psnId).setParameter("pubId", pubId).uniqueResult();
    if (id == null) {
      return false;
    } else {
      return true;
    }
  }

  public Long getOwnerPubPsnId(Long pubId) {
    String hql = "select t.ownerPsnId from PsnPubPO t where t.pubId = :pubId";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  /**
   * 查询h-index的成果
   * 
   * @param pubVo
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubSnsPO> findPubListForHindexImprove(PsnInfluencePubVO pubVo) {
    String hql =
        "from PubSnsPO t where exists ( select 1 from PsnPubPO b where t.pubId = b.pubId and b.ownerPsnId=:psnId and b.status=0 ) and t.citations <=:hindex ";
    // + " and t.publishYear >=(extract(year from sysdate)-4) "; 去掉年份限制 WSN
    String orderHql = " order by nvl(t.citations,0) desc,t.pubId desc";
    if ("no".equals(pubVo.getSelf())) {// 非本人查看
      hql += " and not exists(select 1 from PsnConfigPub pc where pc.id.pubId=t.pubId and pc.anyUser <=6)";
    }
    hql += orderHql;
    List<PubSnsPO> list = super.createQuery(hql).setParameter("psnId", pubVo.getPsnId())
        .setParameter("hindex", pubVo.gethIndex()).setMaxResults(4).list();
    return list;
  }

  @SuppressWarnings("unchecked")
  public List<PubSnsPO> findPubListByYearAndCite(PsnInfluencePubVO form, int count, List<Long> exIds) {
    String hql = "from PubSnsPO t" + " where exists("
        + "     select 1 from PsnPubPO b where t.pubId = b.pubId and b.ownerPsnId=:psnId and b.status=0 ) and t.citations >:hindex"
        + "  and t.pubId not in(:exIds)";
    String orderHql = " order by nvl(t.citations,0) asc, t.publishYear desc, t.pubId desc";
    if ("no".equals(form.getSelf())) {// 非本人查看
      hql += " and not exists(select 1 from PsnConfigPub pc where pc.id.pubId=t.pubId and pc.anyUser <=6)";
    }
    hql += orderHql;
    List<PubSnsPO> list = super.createQuery(hql).setParameter("psnId", form.getPsnId())
        .setParameter("hindex", form.gethIndex()).setParameterList("exIds", exIds).setMaxResults(count).list();
    return list;
  }

  @SuppressWarnings("unchecked")
  public List<PubSnsPO> findPubListByYearAndCite2(PsnInfluencePubVO form, int count) {
    String hql = "from PubSnsPO t where exists("
        + "     select 1 from PsnPubPO b where t.pubId = b.pubId and b.ownerPsnId=:psnId and b.status=0 )";
    String orderHql = " order by nvl(t.citations,0) asc, t.publishYear desc, t.pubId desc";
    if ("no".equals(form.getSelf())) {// 非本人查看
      hql += " and not exists(select 1 from PsnConfigPub pc where pc.id.pubId=t.pubId and pc.anyUser <=6)";
    }
    hql += orderHql;
    List<PubSnsPO> list = super.createQuery(hql).setParameter("psnId", form.getPsnId()).setMaxResults(count).list();
    return list;
  }

  /**
   * 查找待查找成果是否全部属于对应的人员
   * 
   * @param pubIds
   * @param psnId
   * @return
   */
  public Long getPsnOwnerPubCount(List<Long> pubIds, Long psnId) {
    String hql = "select count(1) from PubSnsPO a where exists("
        + "select 1 from PsnPubPO b where a.pubId = b.pubId and b.ownerPsnId=:psnId and b.status=0 )"
        + " and a.pubId in (:pubIds)";
    return (Long) super.createQuery(hql).setParameterList("pubIds", pubIds).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 查找待查找成果是否属于对应的人员
   * 
   * @param pubId
   * @param psnId
   * @return
   */
  public Long getPsnOwnerOnePubCount(Long pubId, Long psnId) {
    String hql = "select count(1) from PubSnsPO a where exists("
        + "select 1 from PsnPubPO b where a.pubId = b.pubId and b.ownerPsnId=:psnId and b.status=0 )"
        + " and a.pubId=:pubId";
    return (Long) super.createQuery(hql).setParameter("pubId", pubId).setParameter("psnId", psnId).uniqueResult();
  }

  /**
   * 查询最后更新的成果
   * 
   * @param pubQueryDTO
   */
  public void queryLastUpdatePub(PubQueryDTO pubQueryDTO) {
    String hql = "from PsnPubPO p  where p.ownerPsnId =:psnId and p.status = 0 order by p.gmtModified desc nulls last";
    PsnPubPO psnPubPO = (PsnPubPO) this.createQuery(hql).setParameter("psnId", pubQueryDTO.getSearchPsnId())
        .setMaxResults(1).uniqueResult();
    if (psnPubPO == null) {
      return;
    }
    hql = "from PubSnsPO t where t.pubId =:pubId";
    List<PubPO> list = this.createQuery(hql).setParameter("pubId", psnPubPO.getPubId()).list();
    pubQueryDTO.setPubList(list);
  }

  public void queryLastUpdatePubByPubIds(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    hql.append(" select t from PubSnsPO t where " + "  exists(select 1 from PsnPubPO p where p.ownerPsnId=:psnId )"
        + "  and t.pubId in (:ids ) order by t.gmtModified desc nulls last");
    List<PubPO> resultList = super.createQuery(hql.toString()).setParameter("psnId", pubQueryDTO.getSearchPsnId())
        .setParameterList("ids", pubQueryDTO.getSearchPubIdList()).list();
    pubQueryDTO.setPubList(resultList);
  }

  /**
   * 得到个人成果(计算hindex用).
   * 
   * @param psnId
   * 
   * @return
   */
  @SuppressWarnings("unchecked")
  public List<PubSnsPO> queryPubsByPsnId(Long psnId) {
    String hql = "from PubSnsPO t where  t.status = 0 and "
        + "exists(select 1 from PsnPubPO t1 where t1.pubId=t.pubId and t1.status=0 and t1.ownerPsnId=:psnId) "
        + "order by nvl(t.citations,-9999999) desc,t.pubId";
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  public PubSnsPO getPubsnsById(Long pubId) {
    String hql = "from PubSnsPO t where  t.status = 0 and t.pubId = :pubId";
    return (PubSnsPO) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  public Integer existRepGrpPub(String title, Long grpId) {
    List list = null;
    String hql = "select 1 from PubSnsPO t where  t.title=:title and t.pubId in "
        + "( select t2.pubId from GroupPubPO t2 where t2.grpId=:grpId and t2.status=0) ";
    list = this.createQuery(hql).setParameter("title", title).setParameter("grpId", grpId).list();
    if (list != null && list.size() > 0) {
      return 1;
    } else {
      return 0;
    }

  }

  /**
   * 查询个人成果类表给中山系统系统
   *
   * @param pubQueryDTO
   */
  public void queryPubListForZS(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    StringBuilder listHql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    listHql.append(" select t ");
    countHql.append(" select count(t.pubId) ");
    hql.append(" from PubSnsPO t");
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" where");
    hql.append(
        " exists (select 1 from PsnPubPO pp where pp.pubId = t.pubId and pp.ownerPsnId = ? and pp.status = 0  )");
    params.add(pubQueryDTO.getSearchPsnId());
    // 排除成果id的判断
    if (pubQueryDTO.getExcludePsnIdList() != null && pubQueryDTO.getExcludePsnIdList().size() > 0) {
      String pubIds = "";
      for (Long pubId : pubQueryDTO.getExcludePsnIdList()) {
        pubIds = pubIds + pubId + ",";
      }
      pubIds = pubIds.substring(0, pubIds.length() - 1);
      hql.append("  and t.pubId not in ( " + pubIds + " ) ");
    }
    // 成果类型
    buildPubTypeSql(pubQueryDTO, hql);
    if (pubQueryDTO.getBeginPublishYear() != null && pubQueryDTO.getBeginPublishYear() > 0) {
      hql.append(" and t.publishYear>= " + pubQueryDTO.getBeginPublishYear());
    }
    if (pubQueryDTO.getEndPublishYear() != null && pubQueryDTO.getEndPublishYear() > 0) {
      hql.append(" and t.publishYear<=" + pubQueryDTO.getEndPublishYear());
    }
    // 排序
    hql.append(" order by nvl(t.publishYear,0) desc ");
    // 记录数
    Long totalCount = super.findUnique(countHql.toString() + hql, params.toArray());
    pubQueryDTO.setTotalCount(totalCount);
    List<PubPO> resultList = super.createQuery(listHql.toString() + hql, params.toArray())
        .setFirstResult(pubQueryDTO.getFirst()).setMaxResults(pubQueryDTO.getPageSize()).list();
    pubQueryDTO.setPubList(resultList);
  }

  public List<Integer> queryPublishYear(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    hql.append(" select t.publishYear  from PubSnsPO t");
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" where");
    hql.append(
        " exists (select 1 from PsnPubPO pp where pp.pubId = t.pubId and pp.ownerPsnId = ? and pp.status = 0  )");
    hql.append(" and t.publishYear is not null  order by t.publishYear desc ");
    params.add(pubQueryDTO.getSearchPsnId());
    List list = super.createQuery(hql.toString(), params.toArray()).list();
    return list;
  }


  public void getAllGrpPubRcmd(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    String countHql = " select count(1) ";
    List<Object> params = new ArrayList<Object>();
    hql.append(" from PubSnsPO t  where ");
    hql.append(" exists (select 1  from GrpPubRcmd t where t.grpId= ?  ");
    params.add(pubQueryDTO.getSearchGrpId());
    if (pubQueryDTO.getPubUpdateDate() != null) {
      hql.append(" and t.createDate >= ? ");
      params.add(pubQueryDTO.getPubUpdateDate());
    }
    hql.append(" ) ");
    String order = " order by t.createDate asc";
    Long totalCount = super.findUnique(countHql.toString() + hql, params.toArray());

    List<PubPO> resultList = (List<PubPO>) super.createQuery(hql.toString() + order, params.toArray())
        .setFirstResult(pubQueryDTO.getFirst()).setMaxResults(pubQueryDTO.getPageSize()).list();
    pubQueryDTO.setTotalCount(totalCount);
    pubQueryDTO.setPubList(resultList);

  }

  @SuppressWarnings("unchecked")
  public List<Long> getSnsPubList(List<Long> snsPubIds) {
    String hql = "select t.createPsnId from PubSnsPO  t where t.status = 0 and t.pubId in (:snsPubIds) ";
    return super.createQuery(hql).setParameterList("snsPubIds", snsPubIds).list();
  }

  @SuppressWarnings("unchecked")
  public List<Map<Long, Long>> getSnsPub(List<Long> snsPubIds) {
    String hql =
        "select new Map(t.createPsnId as createPsnId,t.pubId as pubId ) from PubSnsPO  t where t.status = 0 and t.pubId in (:snsPubIds) ";
    return super.createQuery(hql).setParameterList("snsPubIds", snsPubIds).list();
  }


  /**
   * 查询个人成果的统计数和指定成果的序号
   * 
   * @param pubQueryDTO
   */
  public void queryPubCountAndIndex(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    StringBuilder countHql = new StringBuilder();
    StringBuilder indexHql = new StringBuilder();
    countHql.append(" select count(t.PUB_ID) ");
    hql.append(" from V_PUB_SNS t");
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" where");
    hql.append(
        " exists (select 1 from V_PSN_PUB pp where pp.PUB_ID = t.PUB_ID and pp.OWNER_PSN_ID = ?  and  pp.status = 0 )");
    params.add(pubQueryDTO.getSearchPsnId());
    // 发表年份
    addPublishYearCondition(pubQueryDTO, hql);
    // 成果类型
    addPubTypeCondition(pubQueryDTO, hql);
    // 收录类别 -- 个人成果
    addSituationCondition(pubQueryDTO, hql);
    // 非本人查看 不存在 小于6=的----》 存在大于6的
    if (!pubQueryDTO.isSelf()) {
      hql.append(" and not exists(select 1 from PSN_CONFIG_PUB pc where pc.PUB_ID=t.PUB_ID and pc.ANY_USER <=6)");
    }
    // 记录数
    Long totalCount = super.queryForLong(countHql.toString() + hql, params.toArray());
    pubQueryDTO.setTotalCount(totalCount);
    String orderBySql = addOrderByCondition(pubQueryDTO);
    indexHql.append("select pub_index from ( select row_number() over( ");
    indexHql.append(orderBySql);
    indexHql.append(" ) as pub_index, t.PUB_ID ");
    hql.append(orderBySql);
    indexHql.append(hql);
    indexHql.append(" ) tmp where tmp.PUB_ID = ?");
    params.add(Des3Utils.decodeFromDes3(pubQueryDTO.getDes3PubId()));
    Long index = (Long) super.queryForLong(indexHql.toString(), params.toArray());
    pubQueryDTO.setPubIndex(index);
  }

  private void addPubTypeCondition(PubQueryDTO pubQueryDTO, StringBuilder hql) {
    if (StringUtils.isNotBlank(pubQueryDTO.getPubType())) {
      hql.append(" and t.PUB_TYPE in(" + pubQueryDTO.getPubType() + ") ");
    } else if (StringUtils.isNotBlank(pubQueryDTO.getSearchPubType())) {
      hql.append(" and t.PUB_TYPE in(" + pubQueryDTO.getSearchPubType() + ") ");
    }
  }

  private void addPublishYearCondition(PubQueryDTO pubQueryDTO, StringBuilder hql) {
    if (StringUtils.isNotBlank(pubQueryDTO.getPublishYear())) {
      String[] split = pubQueryDTO.getPublishYear().split(",");
      StringBuilder sb = new StringBuilder();
      for (String str : split) {
        sb.append("'" + str.trim() + "',");
      }
      hql.append(" and  t.PUBLISH_YEAR in(" + sb.substring(0, sb.length() - 1) + ") ");
    }
  }

  /**
   * 构建收录 过滤
   * 
   * @param pubQueryDTO
   * @param hql
   */
  private void addSituationCondition(PubQueryDTO pubQueryDTO, StringBuilder hql) {
    if (StringUtils.isNotBlank(pubQueryDTO.getIncludeType())) {
      String[] split = pubQueryDTO.getIncludeType().split(",");
      StringBuilder sb = new StringBuilder();
      for (String str : split) {
        if (str.trim().equals("sci")) {
          str = "scie";
        }
        sb.append("'" + str.trim() + "',");
      }
      String includeType = sb.substring(0, sb.length() - 1).toUpperCase();
      hql.append(" and  exists (select 1 from V_PUB_SITUATION ps ");
      hql.append(" where ps.PUB_ID = t.PUB_ID  and ps.LIBRARY_NAME in ( " + includeType + " )  and ps.SIT_STATUS = 1)");
    }
  }


  /**
   * 构建排序的sql
   * 
   * @param pubQueryDTO
   * @return
   */
  private String addOrderByCondition(PubQueryDTO pubQueryDTO) {
    String orderString = "";
    switch (pubQueryDTO.getOrderBy()) {
      case "gmtModified":
      case "updateDate":
        orderString = " order by t.gmt_Modified desc nulls last ,t.pub_id desc";
        break;
      case "citations":
      case "citedTimes":
        orderString = " order by t.citations desc nulls last,t.pub_id desc";
        break;
      case "title":
        orderString = " order by  t.title desc nulls last,t.pub_id desc";
        break;
      case "publishDate":
      case "publishYear":
        orderString =
            " order by nvl(t.publish_Year,0) desc,nvl(t.publish_Month,0) desc,nvl(t.publish_Day,0) desc,t.pub_id desc";
        break;
      case "openPubOrder":
        // 代表成果，公开成果列表的默认排序:
        orderString = " order by  t.pub_id desc ,t.publish_Year desc nulls last";
        break;
      case "importMemberPubOrder":
      case "createDate":
        // 导入成员成果的默认排序
        orderString = "  order by  t.gmt_Create desc nulls last";
        break;
      default:
        // 个人成果列表的默认排序
        orderString =
            " order by nvl(t.publish_Year,0) desc,nvl(t.publish_Month,0) desc,nvl(t.publish_Day,0) desc,t.pub_id desc";
        break;
    }
    return orderString;
  }

  public void updatePubPermission(Long pubId, Integer anyUser) {
    String hql = "update PsnConfigPub t set t.anyUser= :anyUser where t.id.pubId =:pubId";
    createQuery(hql).setParameter("anyUser", anyUser).setParameter("pubId", pubId).executeUpdate();
  }

  public PsnConfigPub getPsnConfigPub(Long pubId) {
    String hql = "from PsnConfigPub t where  t.id.pubId =:pubId";
    return (PsnConfigPub) super.createQuery(hql).setParameter("pubId", pubId).uniqueResult();
  }

  public List<PubSnsPO> getPrjPubs(PubQueryDTO pubQueryDTO) {
    StringBuilder hql = new StringBuilder();
    List<Object> params = new ArrayList<Object>();
    hql.append("from PubSnsPO t ");
    hql.append(" where t.status = 0 ");
    hql.append(" and exists (select 1 from PrjPub a where a.pubFrom=1 and a.prjId = ? and a.pubId = t.pubId) ");
    params.add(pubQueryDTO.getSearchPrjId());
    // 构建检索条件
    buildSearchKey(pubQueryDTO.getSearchKey(), hql, params);
    // 成果类型
    if (StringUtils.isNotEmpty(pubQueryDTO.getPubType())) {
      hql.append(" and t.pubType in(" + pubQueryDTO.getPubType() + ") ");
    }
    return super.createQuery(hql.toString(), params.toArray()).list();
  }
}
