package com.smate.web.fund.dao.wechat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.fund.model.common.ConstFundCategory;
import com.smate.web.fund.recommend.model.FundRecommendForm;
import com.smate.web.prj.form.wechat.FundWeChatForm;
import com.smate.web.prj.model.wechat.FundWeChat;

/*
 * @author zjh 基金constFundCategoryDao
 */
@Repository
public class ConstFundCategoryDao extends SnsHibernateDao<ConstFundCategory, Long> {

  protected Logger logger = LoggerFactory.getLogger(getClass());


  @SuppressWarnings("unchecked")
  public void queryFundINfo(FundWeChatForm form) throws Exception {
    long fundId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3FundId()));
    String hql =
        "select new ConstFundCategory(c.id,c.agencyId,c.nameZh,c.nameEn,c.startDate,c.endDate,c.description,c.guideUrl) from ConstFundCategory c where c.id=:fundId";
    ConstFundCategory fund = (ConstFundCategory) super.createQuery(hql).setParameter("fundId", fundId).uniqueResult();
    FundWeChat f = new FundWeChat();

    String locale = LocaleContextHolder.getLocale().toString();
    f.setFundId(Objects.toString(fundId));
    f.setFundAgencyId(fund.getAgencyId());
    f.setFundName("zh_CN".equals(locale) ? fund.getNameZh() : fund.getNameEn());
    // f.setFundAgency((String) m.get("agencyname"));
    f.setStartDate(fund.getStartDate());
    logger.info("微信--基金开始时间" + f.getStartDate());
    f.setEndDate(fund.getEndDate());
    logger.info("微信--基金结束时间" + f.getEndDate());
    f.setGuideUrl(fund.getGuideUrl());
    // f.setLogoUrl(fund.getLogoUrl());
    f.setDescription(HtmlUtils.htmlUnescape(fund.getDescription()));
    DateFormat formatDate = new SimpleDateFormat("YYYY-MM-dd");
    if (f.getStartDate() != null && f.getEndDate() != null) {
      f.setTime(formatDate.format(f.getStartDate()) + " ~ " + formatDate.format(f.getEndDate()));
    } else if (f.getStartDate() != null && f.getEndDate() == null) {
      f.setTime(formatDate.format(f.getStartDate()) + " ~ ");
    } else if (f.getStartDate() == null && f.getEndDate() != null) {
      f.setTime(" ~ " + formatDate.format(f.getEndDate()));
    } else if (f.getStartDate() == null && f.getEndDate() == null) {
      f.setTime("");
    }
    List<FundWeChat> result = new ArrayList<FundWeChat>();
    result.add(f);
    form.setResultList(result);

    /*
     * StringBuilder hql = new StringBuilder(); hql.append("select"); hql.append(c) String hql =
     * "select " + "c.nameZh as fundname," + "c.startDate as startdate," + "c.endDate as enddate," +
     * "a.nameZh as agencyname," + "a.logoUrl as logoUrl," + "c.description as description," +
     * "c.guideUrl as guideUrl " +
     * "from ConstFundCategory c,ConstFundAgency a where c.id=? and  c.agencyId=a.id "; List<Object>
     * list = new ArrayList<Object>(); long fundId = 0L; fundId =
     * NumberUtils.toLong(ServiceUtil.decodeFromDes3(form.getDes3FundId())); list =
     * super.createQuery(hql,
     * fundId).setMaxResults(1).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP) .list();
     * 
     * Map<Object, Object> m = new HashMap<Object, Object>();
     * 
     * 没有开始,结束日期时则不显示，不默认生成日期 Calendar c = new GregorianCalendar();// 新建日期对象 int year =
     * c.get(Calendar.YEAR);// 获取年份 Calendar calendar3 = new GregorianCalendar(year, 0, 1, 0, 0, 0);//
     * 获取当前年的第一天：例：2016-1-1 Calendar calendar4 = new GregorianCalendar(year, 11, 31, 0, 0, 0);//
     * 获取当前年的最后一天：例：2016-12-31 Date startDate = calendar3.getTime(); Date endDate = calendar4.getTime();
     * 
     * 
     * if (list.size() > 0) { m = (Map<Object, Object>) list.get(0); FundWeChat f = new FundWeChat();
     * f.setStartDate( m.get("startdate") == null || "".equals(m.get("startdate")) ? null : (Date)
     * m.get("startdate")); logger.info("微信--基金开始时间" + f.getStartDate()); f.setFundName((String)
     * m.get("fundname")); f.setFundAgency((String) m.get("agencyname")); f.setEndDate(m.get("enddate")
     * == null || "".equals(m.get("enddate")) ? null : (Date) m.get("enddate"));
     * logger.info("微信--基金结束时间" + f.getEndDate()); f.setGuideUrl((String) m.get("guideUrl"));
     * f.setLogoUrl((m.get("logoUrl") == null || "".equals(m.get("logoUrl"))) ? "" :
     * m.get("logoUrl").toString()); f.setDescription((String) m.get("description")); DateFormat
     * formatDate = new SimpleDateFormat("YYYY-MM-dd"); if (f.getStartDate() != null && f.getEndDate()
     * != null) { f.setTime(formatDate.format(f.getStartDate()) + " ~ " +
     * formatDate.format(f.getEndDate())); } else if (f.getStartDate() != null && f.getEndDate() ==
     * null) { f.setTime(formatDate.format(f.getStartDate()) + " ~ "); } else if (f.getStartDate() ==
     * null && f.getEndDate() != null) { f.setTime(" ~ " + formatDate.format(f.getEndDate())); } else if
     * (f.getStartDate() == null && f.getEndDate() == null) { f.setTime(""); }
     * 
     * List<FundWeChat> result = new ArrayList<FundWeChat>(); result.add(f); form.setResultList(result);
     * }
     */
  }

  /**
   * 根据地区，领域查出该机构下的基金
   * 
   * @param agencyId
   * @param disId
   * @param insType
   * @param page
   * @return
   */
  @SuppressWarnings("unchecked")
  public Page findConstFundCategory(Long agencyId, Long disId, String insType, Page page) {
    String listHql = "select new ConstFundCategory(t.id,t.agencyId,t.nameZh,t.nameEn,t.startDate,t.endDate)";
    String countHql = "select count(t.id)";
    StringBuilder hql = new StringBuilder();
    hql.append(" from ConstFundCategory t");
    hql.append("where");
    hql.append("agencyId = ?");
    List<Object> param = new ArrayList<Object>();
    if (disId != null) {
      hql.append(
          "and exists( select 1 from ConstFundCategoryDis a where t.id=a.categoryId and (a.disId=? or a.superDisId=?))");
      param.add("form.getDisId");
    }
    if (StringUtils.isNotBlank(insType)) {
      hql.append("and instr(t.insType,?)>0");
      param.add(insType);
    }
    String orderHql = "order by endDate desc";
    Long totalCount = super.findUnique(countHql + hql, param.toArray());
    page.setTotalCount(totalCount);// 查询总数

    Query querResult = super.createQuery(listHql + hql + orderHql, param.toArray());
    querResult.setFirstResult(page.getFirst() - 1);
    querResult.setMaxResults(page.getPageSize());
    page.setResult(querResult.list());
    return page;
  }

  /**
   * 查找这个包含这个一级的科技领域和他的子领域下的基金的数量
   * 
   * @param disId
   * @return
   */
  public Integer getfundCountByDisId(Long disId, Long agencyId) {
    String hql =
        "select count(t.id) from  ConstFundCategory t where exists( select 1 from ConstFundCategoryDis a where t.id=a.categoryId and (a.disId=:disId or a.superDisId=:disId)) and agencyId=:agencyId";
    return (Integer) super.createQuery(hql).setParameter("disId", disId).setParameter("agencyId", agencyId)
        .uniqueResult();
  }

  /**
   * 根据单位要求查找基金数量
   * 
   * @param insType
   * @return
   */
  public Long getfundCountByInsType(String insType) {
    String hql = "select count(t.id) from ConstFundCategory t where instr(t.insType,:insType)>0";
    return (Long) super.createQuery(hql).setParameter("insType", insType).uniqueResult();
  }

  /**
   * 根据IDs获取基金信息
   * 
   * @param fundIds
   * @return
   */
  public List<ConstFundCategory> findConstFundCategoryByIds(List<Long> fundIds) {
    String fundIdsStr = fundIds.stream().map(a -> a.toString()).collect(Collectors.joining(","));
    String hql = " from ConstFundCategory t where t.id in (:fundIds) order by instr(:fundIdsStr,t.id)";
    return super.createQuery(hql).setParameterList("fundIds", fundIds).setParameter("fundIdsStr", fundIdsStr).list();
  }

  /**
   * 获取基金名称
   * 
   * @param fundId
   * @return
   */
  public ConstFundCategory findFundName(Long fundId) {
    String hql = "select new ConstFundCategory(nameZh,nameEn) from ConstFundCategory t where t.id=:fundId";
    return (ConstFundCategory) super.createQuery(hql).setParameter("fundId", fundId).uniqueResult();
  }



  /**
   * 检索基金
   * 
   * @param form
   * @return
   */
  public Page<Long> searchMyFundList(FundRecommendForm form) {
    Page<Long> page = form.getPage();
    if (page == null) {
      page = new Page<Long>();
    }
    String countHql = "select count(1) ";
    String listHql = "select f.fundId ";
    String queryHql =
        " from ConstFundCategory c, MyFund f, ConstFundAgency a where f.fundId = c.id and c.agencyId = a.id and f.psnId = :psnId and c.insId=0 "
            + " and (lower(c.nameZh) like lower(:searchKey) or lower(c.nameEn) like lower(:searchKey) or lower(a.nameZh) like lower(:searchKey) or lower(a.nameEn) like lower(:searchKey) or lower(c.description) like lower(:searchKey))";
    String searchStr = form.getSearchKey();
    if (StringUtils.isBlank(searchStr)) {
      searchStr = "%";
    } else {
      searchStr = "%" + searchStr.toLowerCase() + "%";
    }
    String orderBy = " order by f.collectTime desc";
    Long count = (Long) super.createQuery(countHql + queryHql).setParameter("psnId", form.getPsnId())
        .setParameter("searchKey", searchStr).uniqueResult();
    page.setTotalCount(count);
    page.setTotalPages(count / page.getPageSize() + 1);
    List<Long> result = super.createQuery(listHql + queryHql + orderBy).setParameter("psnId", form.getPsnId())
        .setParameter("searchKey", searchStr).setMaxResults(page.getPageSize()).setFirstResult(page.getFirst() - 1)
        .list();
    page.setResult(result);
    form.setPage(page);
    return page;
  }

  public void findMyList(FundRecommendForm form) {
    Page<Long> page = form.getPage();
    page.getTotalCount();
    String searchKey = form.getSearchKey();
    if (StringUtils.isBlank(searchKey)) {
      searchKey = "%";
    } else {
      searchKey = "%" + searchKey + "%";
    }
    Long psnId = form.getPsnId();
    String count = "select count(1) ";
    String query = "select t1.id,nvl(t1.name_Zh,t1.name_En),t1.FUND_AGENCY_ID,t1.START_DATE,t1.END_DATE ";
    String sql = " from CONST_FUND_CATEGORY t1, (select * from V_MY_FUND t where t.psn_Id=:psnId) t2 "
        + "where t1.id=t2.fund_Id ";
    String orderby = "order by t2.collect_Time desc ";
    String search =
        "and (nvl(lower(t1.name_Zh),lower(t1.name_En)) like lower(:searchKey) or lower(t1.description) like lower(:searchKey) "
            + "or exists(select 1 from CONST_FUND_AGENCY t3 "
            + "where nvl(lower(t3.NAME_ZH),lower(t3.NAME_EN)) like lower(:searchKey) and t3.id=t1.FUND_AGENCY_ID)) ";
    Object objCount = super.getSession().createSQLQuery(count + sql + search).setParameter("searchKey", searchKey)
        .setParameter("psnId", psnId).uniqueResult();
    Long totalCount = 0L;
    if (objCount != null) {
      totalCount = Long.parseLong(objCount.toString());
    }
    if (totalCount != null && totalCount > 0) {
      List<Object[]> list = super.getSession().createSQLQuery(query + sql + search + orderby)
          .setParameter("searchKey", searchKey).setParameter("psnId", psnId).setMaxResults(page.getPageSize())
          .setFirstResult(page.getFirst() - 1).list();
      form.setQueryList(list);
      page.setTotalCount(totalCount);
    }
  }

}
