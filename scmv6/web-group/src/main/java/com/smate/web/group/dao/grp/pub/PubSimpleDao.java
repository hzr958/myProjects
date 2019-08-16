package com.smate.web.group.dao.grp.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.group.action.grp.form.GrpPubForm;
import com.smate.web.group.action.grp.form.GrpPubRcmdForm;
import com.smate.web.group.action.grp.form.GrpPubShowInfo;
import com.smate.web.group.model.grp.pub.PubSimple;

/**
 * 成果表Dao
 * 
 * @author tsz
 *
 */
@Repository
public class PubSimpleDao extends SnsHibernateDao<PubSimple, Long> {
  /**
   * 
   * @param form
   * @param type 1=成果类型统计数 2=年份类型统计数 3\其他=收录类别统计数
   * @return
   * @throws Exception
   */
  public List<Map<String, Object>> PubsCountCallBack(GrpPubForm form, int type) throws Exception {
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    String pubTypecount = " select new Map(nvl(p.pubType,7) as pubType,count(p.pubId) as count) ";
    String publishYearcount = " select new Map(nvl(p.publishYear,0) as publishYear,count(p.pubId) as count) ";
    String includeType = " select count(p.pubId) ";
    StringBuilder hql = new StringBuilder();
    hql.append(" from PubSimple p ");
    if (!"1".equals(form.getIsPsnPubs()) && form.getGrpId() != null) {
      hql.append(" ,GrpPubs gp ");
    }
    if (StringUtils.isNotEmpty(form.getIncludeType()) || type == 3) {
      hql.append(" ,PublicationList t ");
    }
    hql.append(" where p.status<>1  ");
    hql.append(
        " and exists(select 1 from PsnConfigPub pcp,PsnConfig pc where pc.psnId=? and pc.cnfId=pcp.id.cnfId and pcp.id.pubId=p.pubId and pcp.anyUser=7) ");
    params.add(form.getPsnId());
    if (!"1".equals(form.getIsPsnPubs()) && form.getGrpId() != null) {// 群组参数
      hql.append(" and p.pubId=gp.pubId and gp.status=0  and gp.grpId=? ");
      params.add(form.getGrpId());
      // 条件过滤
      if (form.getShowPrjPub() == 1 && form.getShowRefPub() != 1) {
        // 只显示项目成果
        hql.append(" and gp.isProjectPub = 1 ");
      }
      if (form.getShowPrjPub() != 1 && form.getShowRefPub() == 1) {
        // 只显示项目成果
        hql.append(" and gp.isProjectPub = 0 ");
      }
      if (form.getMemberId() != null && form.getMemberId() != 0L) {
        hql.append(" and p.ownerPsnId=? ");
        params.add(form.getMemberId());
      }
    } else {
      hql.append(" and p.ownerPsnId=? ");
      params.add(form.getPsnId());
    }

    if (StringUtils.isNotEmpty(form.getIncludeType()) || type == 3) {
      hql.append(" and p.pubId=t.id ");
    }
    // 检索
    buildSearchKey(form.getSearchKey(), hql, params);
    // 发表年份
    if (StringUtils.isNotEmpty(form.getPublishYear()) && type != 2) {
      hql.append(" and p.publishYear in(" + form.getPublishYear() + ") ");
    }
    // 成果类型
    if (StringUtils.isNotEmpty(form.getPubType()) && type != 1) {
      hql.append(" and p.pubType in(" + form.getPubType() + ") ");
    }
    // 收录类别
    if (StringUtils.isNotEmpty(form.getIncludeType()) && (type == 1 || type == 2)) {
      String[] listArray = form.getIncludeType().split(",");
      StringBuilder temp = new StringBuilder();
      for (int i = 0; i < listArray.length; i++) {
        if ("ei".equalsIgnoreCase(listArray[i])) {
          temp.append("or t.listEi=1 ");
        } else if ("sci".equalsIgnoreCase(listArray[i])) {
          temp.append("or t.listSci=1 ");
        } else if ("istp".equalsIgnoreCase(listArray[i])) {
          temp.append("or t.listIstp=1 ");
        } else if ("ssci".equalsIgnoreCase(listArray[i])) {
          temp.append("or t.listSsci=1 ");
        }
      }
      if (StringUtils.isNotEmpty(temp)) {
        String temp1 = temp.toString().substring(2);
        hql.append(" and ");
        hql.append(" ( ");
        hql.append(temp1);
        hql.append(" ) ");
      }
    }
    /////////////////////////////
    if ("1".equals(form.getIsPsnPubs()) && "no".equals(form.getSelf())) {// 查看他人的个人成果
      hql.append(" and not exists(select 1 from PsnConfigPub pc where pc.id.pubId=p.pubId and pc.anyUser <=6)");
    }
    /////////////////////////////
    if (type == 1) {// 成果类型统计数
      hql.append(" group by nvl(p.pubType,7) ");
      return this.createQuery(pubTypecount + hql.toString(), params.toArray()).list();
    } else if (type == 2) {// 年份统计数
      hql.append(" group by nvl(p.publishYear,0) ");
      return this.createQuery(publishYearcount + hql.toString(), params.toArray()).list();
    } else {// 收录类别统计数
      List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
      Map<String, Object> map = new HashMap<String, Object>();
      map.put("ei",
          this.createQuery(includeType + hql.toString() + "and t.listEi=1 ", params.toArray()).uniqueResult());
      map.put("sci",
          this.createQuery(includeType + hql.toString() + "and t.listSci=1 ", params.toArray()).uniqueResult());
      map.put("istp",
          this.createQuery(includeType + hql.toString() + "and t.listIstp=1 ", params.toArray()).uniqueResult());
      map.put("ssci",
          this.createQuery(includeType + hql.toString() + "and t.listSsci=1 ", params.toArray()).uniqueResult());
      list.add(map);
      return list;
    }

  }

  /**
   * 检索群组成果
   * 
   * @param form
   * @param pubIds
   * @throws Exception
   */
  public List<PubSimple> getPubByPubIds(GrpPubForm form) throws Exception {

    String countHql = "select count(*) ";
    String queryHql =
        "select new PubSimple(p.pubId,p.ownerPsnId,p.zhTitle,p.enTitle,p.authorNames,p.briefDesc,p.briefDescEn,p.fullTextField,p.updateMark) ";
    // String queryString= ""
    StringBuilder hql = new StringBuilder();

    hql.append(" from PubSimple p,GrpPubs gp ");
    if (StringUtils.isNotEmpty(form.getIncludeType())) {
      hql.append(" ,PublicationList t ");
    }
    hql.append(" where p.status!=1  and p.pubId=gp.pubId and gp.status=0 ");
    if (StringUtils.isNotEmpty(form.getIncludeType())) {
      hql.append(" and p.pubId=t.id ");
    }

    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" and gp.grpId=? ");
    params.add(form.getGrpId());
    if (form.getMemberId() != null && form.getMemberId() != 0L) {
      hql.append(" and p.ownerPsnId=? ");
      params.add(form.getMemberId());
    }
    // 检索
    buildSearchKey(form.getSearchKey(), hql, params);
    // 条件过滤
    if (form.getShowPrjPub() == 1 && form.getShowRefPub() != 1) {
      // 只显示项目成果
      hql.append(" and gp.isProjectPub = 1 ");
    }
    if (form.getShowPrjPub() != 1 && form.getShowRefPub() == 1) {
      // 只显示项目成果
      hql.append(" and gp.isProjectPub = 0 ");
    }

    // 发表年份
    if (StringUtils.isNotEmpty(form.getPublishYear())) {
      hql.append(" and p.publishYear in(" + form.getPublishYear() + ") ");
      // params.add(form.getPublishYear());
    }
    // 成果类型
    if (StringUtils.isNotEmpty(form.getPubType())) {
      hql.append(" and p.pubType in(" + form.getPubType() + ") ");
      // params.add(form.getPubType());
    }
    // 收录类别
    if (StringUtils.isNotEmpty(form.getIncludeType())) {
      String[] listArray = form.getIncludeType().split(",");

      StringBuilder temp = new StringBuilder();
      for (int i = 0; i < listArray.length; i++) {
        if ("ei".equalsIgnoreCase(listArray[i])) {
          temp.append("or t.listEi=1 ");
        } else if ("sci".equalsIgnoreCase(listArray[i])) {
          temp.append("or t.listSci=1 ");
        } else if ("istp".equalsIgnoreCase(listArray[i])) {
          temp.append("or t.listIstp=1 ");
        } else if ("ssci".equalsIgnoreCase(listArray[i])) {
          temp.append("or t.listSsci=1 ");
        }
      }
      if (StringUtils.isNotEmpty(temp)) {
        String temp1 = temp.toString().substring(2);
        hql.append(" and ");
        hql.append(" ( ");
        hql.append(temp1);
        hql.append(" ) ");
      }
    }

    // 排序
    String orderString = "";
    if ("publishYear".equals(form.getOrderBy())) {
      orderString = " order by p.publishYear desc nulls last ";
    } else if ("citedTimes".equals(form.getOrderBy())) {
      orderString = " order by p.citedTimes desc nulls last , p.pubId desc ";
    } else {
      orderString = " order by gp.createDate desc nulls last , p.pubId desc ";
    }
    Page<GrpPubShowInfo> page = form.getPage();
    // 记录数
    Long totalCount = (Long) super.createQuery(countHql + hql, params.toArray()).uniqueResult();
    page.setTotalCount(totalCount);

    @SuppressWarnings("unchecked")
    List<PubSimple> result = super.createQuery(queryHql + hql.toString() + orderString, params.toArray())
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();

    return result;
  }

  /**
   * 构建检索条件
   * 
   * @param form
   * @param hql
   * @param params
   */
  private void buildSearchKey(String searchString, StringBuilder hql, List<Object> params) {
    if (StringUtils.isNotEmpty(searchString)) {
      String searchKey = searchString.replaceAll("\'", "&#39;");
      searchKey = searchKey.toUpperCase().trim();
      hql.append(" and ");
      hql.append(" ( ");
      hql.append(" instr(upper(p.zhTitle),?)>0");// 中文标题
      params.add(searchKey);
      hql.append(" or instr(upper(p.enTitle),?)>0");// 英文标题
      params.add(searchKey);
      hql.append(" or instr(upper(p.briefDesc),?)>0");// 来源
      params.add(searchKey);
      hql.append(" or instr(upper(p.authorNames),?)>0");// 作者
      params.add(searchKey);
      hql.append(" ) ");
    }
  }


  /**
   * 获取推荐群组成果
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public List<PubSimple> getGrpPubRcmd(GrpPubRcmdForm form) throws Exception {
    String countHql = "select count(*) ";
    String queryHql =
        "select new PubSimple(p.pubId,p.ownerPsnId,p.zhTitle,p.enTitle,p.authorNames,p.briefDesc,p.briefDescEn,p.fullTextField) ";

    StringBuilder hql = new StringBuilder();
    hql.append("from PubSimple p,GrpPubRcmd gr where p.status!=1 and p.pubId=gr.pubId and gr.status=0 ");
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" and gr.grpId=? ");
    params.add(form.getGrpId());
    // 检索
    buildSearchKey(form.getSearchKey(), hql, params);

    String orderString = "order by gr.createDate desc";

    Page<GrpPubShowInfo> page = form.getPage();
    // 记录数
    Long totalCount = (Long) super.createQuery(countHql + hql, params.toArray()).uniqueResult();
    page.setTotalCount(totalCount);

    @SuppressWarnings("unchecked")
    /*
     * List<PubSimple> resultList = super.createQuery(queryHql + hql.toString() + orderString,
     * params.toArray()) .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
     */
    List<PubSimple> resultList = super.createQuery(queryHql + hql.toString() + orderString, params.toArray()).list();

    return resultList;
  }

  /**
   * 获取成员成果列表
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public List<PubSimple> getMemberPubList(GrpPubForm form) throws Exception {
    String countHql = "select count(*) ";
    String queryHql =
        "select new PubSimple(p.pubId,p.ownerPsnId,p.zhTitle,p.enTitle,p.authorNames,p.briefDesc,p.briefDescEn,p.fullTextField) ";

    StringBuilder hql = new StringBuilder();
    hql.append("from PubSimple p ");
    if (StringUtils.isNotEmpty(form.getIncludeType())) {
      hql.append(" ,PublicationList pl ");
    }
    hql.append(" where p.status<>1 and p.articleType=1 ");
    if (StringUtils.isNotEmpty(form.getIncludeType())) {
      hql.append(" and p.pubId=pl.id ");
    }

    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" and p.ownerPsnId=? ");
    params.add(form.getPsnId());
    hql.append(
        " and exists(select 1 from PsnConfigPub pcp,PsnConfig pc where pc.psnId=? and pc.cnfId=pcp.id.cnfId and pcp.id.pubId=p.pubId and pcp.anyUser=7)");
    params.add(form.getPsnId());
    // 检索
    buildSearchKey(form.getSearchKey(), hql, params);
    // 条件过滤
    // 发表年份
    // 发表年份
    if (StringUtils.isNotEmpty(form.getPublishYear())) {
      hql.append(" and p.publishYear in(" + form.getPublishYear() + ") ");
      // params.add(form.getPublishYear());
    }
    // 成果类型
    if (StringUtils.isNotEmpty(form.getPubType())) {
      hql.append(" and p.pubType in(" + form.getPubType() + ") ");
      // params.add(form.getPubType());
    }
    // 收录类别
    if (StringUtils.isNotEmpty(form.getIncludeType())) {
      String[] listArray = form.getIncludeType().split(",");

      StringBuilder temp = new StringBuilder();
      for (int i = 0; i < listArray.length; i++) {
        if ("ei".equalsIgnoreCase(listArray[i])) {
          temp.append("or pl.listEi=1 ");
        } else if ("sci".equalsIgnoreCase(listArray[i])) {
          temp.append("or pl.listSci=1 ");
        } else if ("istp".equalsIgnoreCase(listArray[i])) {
          temp.append("or pl.listIstp=1 ");
        } else if ("ssci".equalsIgnoreCase(listArray[i])) {
          temp.append("or pl.listSsci=1 ");
        }
      }
      if (StringUtils.isNotEmpty(temp)) {
        String temp1 = temp.toString().substring(2);
        hql.append(" and ");
        hql.append(" ( ");
        hql.append(temp1);
        hql.append(" ) ");
      }
    }

    // 排序
    String orderString = "";
    if ("publishYear".equals(form.getOrderBy())) {
      orderString = " order by p.publishYear desc nulls last ";
    } else if ("citedTimes".equals(form.getOrderBy())) {
      orderString = " order by p.citedTimes desc nulls last ";
    } else {
      hql.append(" order by p.createDate desc nulls last");
    }
    Page<GrpPubShowInfo> page = form.getPage();
    // 记录数
    Long totalCount = (Long) super.createQuery(countHql + hql, params.toArray()).uniqueResult();
    page.setTotalCount(totalCount);

    @SuppressWarnings("unchecked")
    List<PubSimple> resultList = super.createQuery(queryHql + hql.toString() + orderString, params.toArray())
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();

    return resultList;
  }

  /**
   * 查询自己的成果 或者群组成果 有群组id 查群组成果 没有群组id 查个人成果
   * 
   * @param form
   * @return
   * @throws Exception
   */
  public List<PubSimple> getSelectPubList(GrpPubForm form) throws Exception {

    String countHql = "select count(*) ";
    String queryHql =
        "select new PubSimple(p.pubId,p.ownerPsnId,p.zhTitle,p.enTitle,p.authorNames,p.briefDesc,p.briefDescEn,p.fullTextField) ";
    // String queryString= ""
    StringBuilder hql = new StringBuilder();
    // 查询条件
    List<Object> params = new ArrayList<Object>();
    hql.append(" from PubSimple p ");
    if (form.getGrpId() != null) {
      hql.append(" ,GrpPubs gp ");
    }
    if (form.getGrpId() != null) {
      hql.append(" where p.status!=1 and p.articleType=1");
      hql.append(" and p.pubId=gp.pubId and gp.status=0 ");
      hql.append(" and gp.grpId=? ");
      params.add(form.getGrpId());
    } else {
      hql.append(" where p.status=0 and p.articleType=1");
      hql.append(" and p.ownerPsnId=? ");
      params.add(form.getPsnId());
    }
    // 检索
    buildSearchKey(form.getSearchKey(), hql, params);
    // 排序
    String orderString = "";
    if (form.getGrpId() != null) {
      orderString = " order by gp.createDate desc nulls last,p.pubId ";
    } else {
      orderString = " order by p.createDate desc nulls last,p.pubId";
    }
    Page<GrpPubShowInfo> page = form.getPage();
    // 记录数
    Long totalCount = (Long) super.createQuery(countHql + hql, params.toArray()).uniqueResult();
    page.setTotalCount(totalCount);

    @SuppressWarnings("unchecked")
    List<PubSimple> result = super.createQuery(queryHql + hql.toString() + orderString, params.toArray())
        .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();

    return result;
  }

  public Integer existGrpPub(String zhTitle, String enTitle, Long grpId) {
    String hql = "";
    String title = "";
    List list = null;
    if (StringUtils.isBlank(zhTitle)) {
      title = enTitle;
      hql = "select 1 from PubSimple t where  t.enTitle=:title and t.pubId in "
          + "( select t2.pubId from GrpPubs t2 where t2.grpId=:grpId and t2.status=0) ";
      list = this.createQuery(hql).setParameter("title", title).setParameter("grpId", grpId).list();
    } else if (StringUtils.isBlank(enTitle)) {
      title = zhTitle;
      hql = "select 1 from PubSimple t where  t.zhTitle=:title and t.pubId in "
          + "( select t2.pubId from GrpPubs t2 where t2.grpId=:grpId and t2.status=0) ";
      list = this.createQuery(hql).setParameter("title", title).setParameter("grpId", grpId).list();
    } else {
      hql = "select 1 from PubSimple t where  t.enTitle=:enTitle and t.zhTitle=:zhTitle and t.pubId in "
          + "( select t2.pubId from GrpPubs t2 where t2.grpId=:grpId and t2.status=0) ";
      list = this.createQuery(hql).setParameter("zhTitle", zhTitle).setParameter("enTitle", enTitle)
          .setParameter("grpId", grpId).list();
    }
    if (list != null && list.size() > 0) {
      return 1;
    } else {
      return 0;
    }

  }

}
