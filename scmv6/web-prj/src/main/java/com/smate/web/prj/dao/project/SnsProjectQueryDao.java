package com.smate.web.prj.dao.project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.hibernate.Query;
import org.hibernate.transform.Transformers;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.project.model.PrjComment;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.utils.data.SnsHibernateDao;
import com.smate.core.base.utils.model.Page;
import com.smate.web.prj.form.ProjectDetailsForm;
import com.smate.web.prj.form.ProjectQueryForm;
import com.smate.web.prj.form.wechat.PrjWeChatForm;
import com.smate.web.prj.model.common.PrjInfo;

/**
 * 
 * 项目查询dao
 * 
 * @author zx
 *
 */
@Repository
public class SnsProjectQueryDao extends SnsHibernateDao<Project, Long> {
  /**
   * 项目列表查询
   */
  @SuppressWarnings("unchecked")
  public List<Project> searchProject(long psnId, ProjectQueryForm query, Page<Project> page) {

    StringBuffer selectHql = new StringBuffer();
    StringBuffer countHql = new StringBuffer();
    StringBuffer hql = new StringBuffer();
    selectHql.append(
        "select new Project(t.id,t.externalNo,t.zhTitle,t.enTitle,t.authorNames,t.authorNamesEn, t.briefDesc,t.briefDescEn,t.fulltextFileId) from Project t where  t.status = 0 ");
    countHql.append("select count(1) from Project t where  t.status = 0 ");
    List params = new ArrayList();
    if (StringUtils.isNotBlank(query.getSearchKey())) {
      String title = query.getSearchKey().toUpperCase().trim();
      hql.append(" and ((upper(t.zhTitle) like ? or upper(t.enTitle) like ? ))");
      params.add("%" + title + "%");
      params.add("%" + title + "%");
    }
    if (query.getFundingYear() != null && query.getFundingYear() != 0) {
      hql.append(" and t.fundingYear >= ? and t.fundingYear<=").append(Calendar.getInstance().getWeekYear());
      params.add(query.getFundingYear());
    }
    hql.append(" and t.psnId = ?");
    params.add(psnId);
    if (CollectionUtils.isNotEmpty(query.getAllAgencyName())) {
      hql.append(" and ((nvl(t.agencyName,0) not in(:agencyName) and nvl(t.enAgencyName,0) not in(:agencyName)))");
    } else if (CollectionUtils.isNotEmpty(query.getAgencyName()) && CollectionUtils.isEmpty(query.getAllAgencyName())) {
      hql.append(
          "and (t.agencyName in(:agencyName) or (t.enAgencyName in(:agencyName)) and (t.agencyName is null or t.agencyName in(:agencyName)))");
    }
    if ("publishYear".equals(query.getOrderBy())) {
      hql.append(" order by  t.fundingYear desc nulls last ,t.id desc");
    } else if ("updateDate".equals(query.getOrderBy())) {
      hql.append(" order by  t.updateDate desc nulls last ,t.id desc");
    } else {
      hql.append(" order by  t.updateDate desc nulls last ,t.id desc");
    }
    List<Project> prjlists = new ArrayList<Project>();
    Object[] paramArray = params.toArray();
    if (CollectionUtils.isNotEmpty(query.getAgencyName())) {
      Long totalCount = (Long) super.createQuery(countHql.toString() + hql.toString(), paramArray)
          .setParameterList("agencyName", query.getAgencyName()).uniqueResult();
      page.setTotalCount(totalCount);
      return super.createQuery(selectHql.toString() + hql.toString(), paramArray)
          .setParameterList("agencyName", query.getAgencyName())
          .setFirstResult(page.getFirst() - 1 - query.getFulltextCount()).setMaxResults(page.getPageSize()).list();
    } else {
      Long totalCount = (Long) super.createQuery(countHql.toString() + hql.toString(), paramArray).uniqueResult();
      page.setTotalCount(totalCount);
      return super.createQuery(selectHql.toString() + hql.toString(), paramArray)
          .setFirstResult(page.getFirst() - 1 - query.getFulltextCount()).setMaxResults(page.getPageSize()).list();
    }
  }

  /**
   * 项目列表查询
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void searchProject(PrjWeChatForm form) {

    StringBuffer selectHql = new StringBuffer();
    StringBuffer countHql = new StringBuffer();
    StringBuffer hql = new StringBuffer();
    selectHql.append(
        "select new Project(t.id,t.externalNo,t.zhTitle,t.enTitle,t.authorNames,t.authorNamesEn, t.briefDesc,t.briefDescEn,t.fulltextFileId) from Project t where  t.status = 0 ");
    countHql.append("select count(1) from Project t where  t.status = 0 ");

    List params = new ArrayList();
    if (!form.getSearchPsnId().equals(form.getPsnId())) {// 不是查看自己的
      hql.append(
          " and t.id in (select p.id.prjId from PsnConfig b,PsnConfigPrj p where b.psnId=? and b.cnfId=p.id.cnfId and p.anyUser=7) ");
      params.add(form.getSearchPsnId());
    }
    if (StringUtils.isNotBlank(form.getSearchKey())) {// 查询字符串
      String title = HtmlUtils.htmlUnescape(form.getSearchKey()).toUpperCase().trim();
      hql.append(" and (upper(t.zhTitle) like ? or upper(t.enTitle) like ? )");
      params.add("%" + title + "%");
      params.add("%" + title + "%");
    }
    Calendar date = Calendar.getInstance();
    Integer currentYear = date.get(Calendar.YEAR);
    if (form.getFundingYear() != null && form.getFundingYear() != 0) {// 项目年度
      hql.append(" and t.fundingYear >= ? and t.fundingYear <= ?");
      params.add(form.getFundingYear());
      params.add(currentYear);
    }
    hql.append(" and t.psnId = ?");
    params.add(form.getSearchPsnId());
    if (StringUtils.isNotEmpty(form.getAgencyNames())) {// 机构名称
      if (form.getAgencyNames().contains("其他")) {
        hql.append(" and (t.agencyName in(:agencyName) or t.enAgencyName in(:agencyName) or t.agencyName is null)");
      } else {
        hql.append(" and (t.agencyName in(:agencyName) or t.enAgencyName in(:agencyName))");
      }
    }

    if ("publishYear".equals(form.getOrderBy())) {// 项目年度
      hql.append(" order by  t.fundingYear desc nulls last ,t.id desc");
    } else if ("updateDate".equals(form.getOrderBy())) {// 修改时间
      hql.append(" order by  t.updateDate desc nulls last ,t.id desc");
    } else {
      hql.append(" order by  t.updateDate desc nulls last ,t.id desc");
    }
    List<Project> prjlists = new ArrayList<Project>();
    Object[] paramArray = params.toArray();
    if (StringUtils.isNotEmpty(form.getAgencyNames())) {
      Long totalCount = (Long) super.createQuery(countHql.toString() + hql.toString(), paramArray)
          .setParameterList("agencyName", getAgencyNameList(form.getAgencyNames())).uniqueResult();
      form.getPage().setTotalCount(totalCount);// 列表总数
      prjlists = super.createQuery(selectHql.toString() + hql.toString(), paramArray)
          .setParameterList("agencyName", getAgencyNameList(form.getAgencyNames()))
          .setFirstResult(form.getPage().getFirst() - 1).setMaxResults(form.getPage().getPageSize()).list();
    } else {
      Long totalCount = (Long) super.createQuery(countHql.toString() + hql.toString(), paramArray).uniqueResult();
      form.getPage().setTotalCount(totalCount);// 列表总数
      prjlists = super.createQuery(selectHql.toString() + hql.toString(), paramArray)
          .setFirstResult(form.getPage().getFirst() - 1).setMaxResults(form.getPage().getPageSize()).list();
    }
    form.getPage().setResult(buildPrjInfo(prjlists));
  }

  private List<String> getAgencyNameList(String agencyNames) {
    List<String> agencyNameList = new ArrayList<String>();
    if (StringUtils.isNoneBlank(agencyNames)) {
      agencyNameList = Stream.of(agencyNames.split(",")).collect(Collectors.toList());
    }
    return agencyNameList;
  }

  private List<PrjInfo> buildPrjInfo(List<Project> prjList) {
    List<PrjInfo> prjInfoList = new ArrayList<PrjInfo>();
    if (CollectionUtils.isNotEmpty(prjList)) {
      for (Project prj : prjList) {
        PrjInfo prjInfo = new PrjInfo();
        prjInfo.setPrjId(prj.getId());
        prjInfo.setPrj(prj);
        prjInfo.setAuthors(prj.getAuthorNames());
        prjInfo
            .setAuthors(StringUtils.isNotBlank(prj.getAuthorNames()) ? prj.getAuthorNames() : prj.getAuthorNamesEn());
        prjInfo.setTitle(StringUtils.isNotBlank(prj.getZhTitle()) ? prj.getZhTitle() : prj.getEnTitle());
        prjInfo.setBriefDesc(StringUtils.isNotBlank(prj.getBriefDesc()) ? prj.getBriefDesc() : prj.getBriefDescEn());
        prjInfoList.add(prjInfo);
      }
    }
    return prjInfoList;
  }

  /**
   * 查询未上传全文数量
   * 
   * @param psnId
   * @return
   */
  public Integer findNoFulltext(long psnId) {
    String hql = "select count(*) from Project t where t.psnId =:psnId and t.status = 0 and t.fulltextFileId is null";
    Long count = (Long) super.createQuery(hql).setParameter("psnId", psnId).uniqueResult();
    return count.intValue();
  }

  /**
   * 查询项目总数和金额数
   */
  public Map<String, Object> findPrjNumAmount(Long psnId) {
    String hql =
        "select count(*) as prj,nvl(sum(t.amount),0) as amounts from Project t where t.psnId =:psnId and t.status =0";
    return (Map<String, Object>) super.createQuery(hql).setParameter("psnId", psnId)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
  }

  /**
   * 排除掉人员的隐私项目
   */
  @SuppressWarnings("unchecked")
  public List<Long> findNoPsnPrjPrivacy(long psnId) {
    String hql =
        "select p.id.prjId from PsnConfig t,PsnConfigPrj p where t.psnId=:psnId and t.cnfId=p.id.cnfId and p.anyUser=7";
    /*
     * String countHql=
     * "select count(p.id.prjId) from PsnConfig t,PsnConfigPrj p where t.psnId=:psnId and t.cnfId=p.id.cnfId and p.anyUser<>4"
     * ;
     */
    /*
     * page.setTotalCount((Long) super.createQuery(countHql).setParameter("psnId",
     * psnId).uniqueResult());
     */
    return super.createQuery(hql).setParameter("psnId", psnId).list();
  }

  /**
   * 根据prjId查找项目
   */
  @SuppressWarnings("unchecked")
  public List<Project> findPsnPrj(List<Long> prjIds, Page<Project> page, ProjectQueryForm query) {
    StringBuffer hql = new StringBuffer();
    hql.append(
        "select new Project(id, zhTitle,enTitle,authorNames,authorNamesEn, briefDesc,briefDescEn,fulltextFileId) from Project t where  t.status = 0 ");
    List params = new ArrayList();
    if (StringUtils.isNotBlank(query.getSearchKey())) {
      String title = query.getSearchKey().toUpperCase().trim();
      hql.append(" and ((upper(t.zhTitle) like ? or upper(t.enTitle) like ? ))");
      params.add("%" + title + "%");
      params.add("%" + title + "%");
    }
    if (query.getFundingYear() != null && query.getFundingYear() != 0) {
      hql.append(" and t.fundingYear >= ? and t.fundingYear<=").append(Calendar.getInstance().getWeekYear());
      params.add(query.getFundingYear());
    }
    if (CollectionUtils.isNotEmpty(query.getAllAgencyName())) {
      hql.append(" and ((nvl(t.agencyName,0) not in(:agencyName) and nvl(t.enAgencyName,0) not in(:agencyName)))");
    } else if (CollectionUtils.isNotEmpty(query.getAgencyName()) && CollectionUtils.isEmpty(query.getAllAgencyName())) {
      hql.append(" and (t.agencyName in(:agencyName) or t.enAgencyName in(:agencyName))");
    }
    hql.append(" and t.id in(:prjIds) ");
    // hql.append("order by t.fundingYear desc nulls last");
    if ("publishYear".equals(query.getOrderBy())) {
      hql.append(" order by  t.fundingYear desc nulls last ,t.id desc");
    } else if ("updateDate".equals(query.getOrderBy())) {
      hql.append(" order by  t.updateDate desc nulls last ,t.id desc");
    } else {
      hql.append(" order by  t.updateDate desc nulls last ,t.id desc");
    }
    Object[] paramArray = params.toArray();
    String countHql = "select count(*) from Project t where  t.status = 0 and t.id in(:prjIds)";
    List<Project> prjlists = new ArrayList<Project>();
    if (CollectionUtils.isNotEmpty(query.getAgencyName()) || CollectionUtils.isNotEmpty(query.getAllAgencyName())) {
      prjlists = super.createQuery(hql.toString(), paramArray).setParameterList("agencyName", query.getAgencyName())
          .setParameterList("prjIds", prjIds).list();
      page.setTotalCount(prjlists.size());
      return super.createQuery(hql.toString(), paramArray).setParameterList("prjIds", prjIds)
          .setParameterList("agencyName", query.getAgencyName()).setFirstResult(page.getFirst() - 1)
          .setMaxResults(page.getPageSize()).list();
    } else {
      prjlists = super.createQuery(hql.toString(), paramArray).setParameterList("prjIds", prjIds).list();
      page.setTotalCount(prjlists.size());
      return super.createQuery(hql.toString(), paramArray).setParameterList("prjIds", prjIds)
          .setFirstResult(page.getFirst() - 1).setMaxResults(page.getPageSize()).list();
    }

  }

  /**
   * 查找项目权限
   * 
   */
  public Integer findPsnPrjPrivacy(Long prjId) {
    String hql = "select t.anyUser from PsnConfigPrj t where t.id.prjId=:prjId";

    return (Integer) super.createQuery(hql).setParameter("prjId", prjId).uniqueResult();
  }

  /**
   * 查询项目的资助机构
   */
  public List<Map<String, Object>> findAgencyName(Long psnId, boolean othersSee) {
    Locale locale = LocaleContextHolder.getLocale();
    String hql = null;
    if (Locale.CHINA.equals(locale)) {
      hql = "select nvl(agencyName,enAgencyName) as agencyName from Project  t where t.psnId =:psnId and t.status = 0 ";
      if (othersSee == true) {
        hql = hql
            + " and not exists(select 1 from PsnConfig b,PsnConfigPrj p where  p.id.prjId =t.id and b.cnfId=p.id.cnfId and p.anyUser <=6)";
      }
      hql = hql
          + " and( t.agencyName is not null or t.enAgencyName is not null)  group by nvl(agencyName,enAgencyName) order by count(t.id) desc, nlssort(nvl(SCHEME_AGENCY_NAME,SCHEME_AGENCY_NAME_EN), 'NLS_SORT=SCHINESE_PINYIN_M')";
    } else {
      hql = "select nvl(enAgencyName,agencyName) as agencyName from Project  t where t.psnId =:psnId and t.status = 0 ";
      if (othersSee == true) {
        hql = hql
            + " and not exists(select 1 from PsnConfig b,PsnConfigPrj p where  p.id.prjId =t.id and b.cnfId=p.id.cnfId and p.anyUser <=6)";
      }
      hql = hql
          + "  and( t.agencyName is not null or t.enAgencyName is not null)  group by nvl(enAgencyName,agencyName) order by count(t.id) desc, nlssort(nvl(SCHEME_AGENCY_NAME_EN,SCHEME_AGENCY_NAME), 'NLS_SORT=SCHINESE_PINYIN_M')";

    }
    return super.createQuery(hql).setParameter("psnId", psnId).setFirstResult(0).setMaxResults(10)
        .setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
  }

  /**
   * 根据项目id查询项目的资助机构
   */
  /*
   * public List<Project> findAgencyNameByPrjId(List<Long> prjIds) { String hql =
   * "select distinct new Project(agencyName,enAgencyName,agencyId) from Project  t where t.id in(:prjIds) and t.status = 0 and( t.agencyName is not null or t.enAgencyName is not null)"
   * ; hql = hql +
   * " and not exists(select 1 from PsnConfig b,PsnConfigPrj p where  p.id.prjId =t.id and b.cnfId=p.id.cnfId and p.anyUser <=6)"
   * ; return super.createQuery(hql).setParameterList("prjIds", prjIds).list(); }
   */

  /**
   * 查询项目的资助机构统计数
   * 
   */
  public List<Map<String, Object>> getAgencyCountByPsnId(Long psnId, List<String> agencyName, Integer fundYear,
      boolean othersSee, String title) {
    StringBuffer hql = new StringBuffer();
    List params = new ArrayList();
    hql.append(
        "select nvl(t.agencyName,t.enAgencyName) as agencyName,nvl(t.enAgencyName,t.agencyName) as enAgencyName ,count(t.id) as count from Project t where t.psnId = ? and t.status = 0");
    params.add(psnId);
    if (fundYear != null && fundYear != 0) {
      hql.append(" and t.fundingYear >= ?");
      params.add(fundYear);
    }
    if (StringUtils.isNotBlank(title)) {
      title = title.toUpperCase().trim();
      hql.append(" and ((upper(t.zhTitle) like ? or upper(t.enTitle) like ? ))");
      params.add("%" + title + "%");
      params.add("%" + title + "%");
    }
    if (CollectionUtils.isNotEmpty(agencyName)) {
      hql.append(" and (t.agencyName in(:agencyName) or t.enAgencyName in(:agencyName))");
    }
    if (othersSee == true) {
      hql.append(
          " and not exists(select 1 from PsnConfig b,PsnConfigPrj p where  p.id.prjId =t.id and b.cnfId=p.id.cnfId and p.anyUser <=6)");
    }
    hql.append(
        " and( t.agencyName is not null or t.enAgencyName is not null) group by nvl(t.agencyName,t.enAgencyName),nvl(t.enAgencyName,t.agencyName)");
    Object[] paramArray = params.toArray();
    if (CollectionUtils.isNotEmpty(agencyName)) {
      return super.createQuery(hql.toString(), paramArray).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .setParameterList("agencyName", agencyName).list();
    } else {
      return super.createQuery(hql.toString(), paramArray).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .list();
    }

  }

  /**
   * 查询资助机构的其它的统计数
   */
  public Long getOtherAgencyCountByPsnId(Long psnId, List<String> agencyName, Integer fundYear, boolean othersSee,
      String title) {
    StringBuffer hql = new StringBuffer();
    List params = new ArrayList();
    hql.append("select count(*) from Project t where t.psnId = ? and t.status = 0");
    params.add(psnId);
    if (fundYear != null && fundYear != 0) {
      hql.append(" and t.fundingYear >= ?  and t.fundingYear<=").append(Calendar.getInstance().getWeekYear());
      params.add(fundYear);
    }
    if (StringUtils.isNotBlank(title)) {
      title = title.toUpperCase().trim();
      hql.append(" and ((upper(t.zhTitle) like ? or upper(t.enTitle) like ? ))");
      params.add("%" + title + "%");
      params.add("%" + title + "%");
    }
    if (CollectionUtils.isNotEmpty(agencyName)) {
      hql.append(" and ( Coalesce(t.agencyName,t.enAgencyName,'0000') not in(:agencyName) ) ");
    }
    if (othersSee == true) {
      hql.append(
          " and not exists(select 1 from PsnConfig b,PsnConfigPrj p where  p.id.prjId =t.id and b.cnfId=p.id.cnfId and p.anyUser <=6)");
    }
    /*
     * for (String agency : agencyName) { hql.append(" and (t.agencyName != ? or t.enAgencyName != ?)");
     * params.add(agency); params.add(agency); }
     */
    Object[] paramArray = params.toArray();
    if (CollectionUtils.isNotEmpty(agencyName)) {
      return (Long) super.createQuery(hql.toString(), paramArray).setParameterList("agencyName", agencyName)
          .uniqueResult();
    } else {
      return (Long) super.createQuery(hql.toString(), paramArray).uniqueResult();
    }

  }

  /**
   * 查询年份统计数
   */
  public List<Map<String, Object>> getYearCountByPsnId(Long psnId, List<String> agencyName, List<String> allAgency,
      boolean othersSee, String title) {
    StringBuffer hql = new StringBuffer();
    List params = new ArrayList();
    hql.append(
        "select nvl(t.fundingYear,0) as fundingYear,count(t.id) as count from Project t where t.psnId = ? and t.status = 0");
    params.add(psnId);
    if (StringUtils.isNotBlank(title)) {
      title = title.toUpperCase().trim();
      hql.append(" and ((upper(t.zhTitle) like ? or upper(t.enTitle) like ? ))");
      params.add("%" + title + "%");
      params.add("%" + title + "%");
    }
    if (CollectionUtils.isNotEmpty(allAgency)) {
      hql.append(" and ((nvl(t.agencyName,0) not in(:agencyName) and nvl(t.enAgencyName,0) not in(:agencyName)))");
    } else if (CollectionUtils.isNotEmpty(agencyName)) {
      hql.append(" and (t.agencyName in(:agencyName) or t.enAgencyName in(:agencyName))");
    }
    if (othersSee == true) {
      hql.append(
          " and not exists(select 1 from PsnConfig b,PsnConfigPrj p where  p.id.prjId =t.id and b.cnfId=p.id.cnfId and p.anyUser <=6)");
    }
    hql.append(" group by nvl(t.fundingYear,0)");
    Object[] paramArray = params.toArray();
    if (CollectionUtils.isNotEmpty(agencyName)) {
      return super.createQuery(hql.toString(), paramArray).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .setParameterList("agencyName", agencyName).list();
    } else {
      return super.createQuery(hql.toString(), paramArray).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP)
          .list();
    }

  }

  // 查询不限年份的项目数量
  public Long getAllYearCountByPsnId(Long psnId, List<String> agencyName, List<String> allAgency, boolean othersSee,
      String title) {
    StringBuffer hql = new StringBuffer();
    List params = new ArrayList();
    hql.append("select count(*)  from Project t where t.psnId = ? and t.status =0");
    params.add(psnId);
    if (StringUtils.isNotBlank(title)) {
      title = title.toUpperCase().trim();
      hql.append(" and ((upper(t.zhTitle) like ? or upper(t.enTitle) like ? ))");
      params.add("%" + title + "%");
      params.add("%" + title + "%");
    }
    if (CollectionUtils.isNotEmpty(allAgency)) {
      hql.append(" and ((nvl(t.agencyName,0) not in(:agencyName) and nvl(t.enAgencyName,0) not in(:agencyName)))");
    } else if (CollectionUtils.isNotEmpty(agencyName)) {
      hql.append(
          "and (t.agencyName in(:agencyName) or (t.enAgencyName in(:agencyName)) and (t.agencyName is null or t.agencyName in(:agencyName)))");
    }
    if (othersSee == true) {
      hql.append(
          " and not exists(select 1 from PsnConfig b,PsnConfigPrj p where  p.id.prjId =t.id and b.cnfId=p.id.cnfId and p.anyUser <=6)");
    }
    Object[] paramArray = params.toArray();
    if (CollectionUtils.isNotEmpty(agencyName)) {
      return (Long) super.createQuery(hql.toString(), paramArray).setParameterList("agencyName", agencyName)
          .uniqueResult();
    } else {
      return (Long) super.createQuery(hql.toString(), paramArray).uniqueResult();
    }

  }

  public boolean isAward(Long psnId, Long prjId) {

    String sql = "select t.status from DynamicAwardPsn t where "
        + "exists(select 1 from DynamicAwardRes t2 where t2.resType= 4 and t2.resId= ? and t.awardId = t2.awardId) "
        + "and t.awarderPsnId= ? order by t.awardDate desc";
    Query query = super.createQuery(sql, new Object[] {prjId, psnId});
    Integer status = (Integer) query.uniqueResult();
    // 状态为1 === 赞 记住
    if (status != null && status == 1) {
      return true;
    } else {
      return false;
    }
  }

  public List<PrjComment> getPrjComments(Long prjId, ProjectDetailsForm form) {
    String countHql = "select count(1) ";
    String hql = "from PrjComment t where t.prjId=:prjId and t.isAudit=1 ";
    String orderHql = "order by t.commentsId desc";
    Object countObj = (Object) this.createQuery(countHql + hql).setParameter("prjId", prjId).uniqueResult();
    Integer totalCount = 0;
    if (totalCount != null) {
      totalCount = NumberUtils.toInt(countObj.toString());
    }
    form.getPage().setTotalCount(totalCount);
    List<PrjComment> comments = super.createQuery(hql + orderHql).setParameter("prjId", prjId)
        .setFirstResult(form.getPage().getFirst() - 1).setMaxResults(form.getPage().getPageSize()).list();
    return comments;
  }


  /**
   * 查找重复的项目.
   * 
   * @param zhTitle
   * @param enTitle
   * @param pubType
   * @param extras
   * @return
   */
  @SuppressWarnings("unchecked")
  public Long getDupPrjId(Integer zhTitleHash, Integer enTitleHash, Long psnId) {
    List<Object> params = new ArrayList<Object>();
    String hql =
        "select id  from Project t where t.psnId = :psnId and t.status = 0 and ( t.zhTitleHash = :zhTitleHash  or t.enTitleHash = :enTitleHash or t.zhTitleHash = :enTitleHash  or t.enTitleHash = :zhTitleHash  ) ";
    params.add(psnId);
    params.add(zhTitleHash == null ? enTitleHash : zhTitleHash);
    params.add(enTitleHash == null ? zhTitleHash : enTitleHash);
    params.add(enTitleHash == null ? zhTitleHash : enTitleHash);
    params.add(zhTitleHash == null ? enTitleHash : zhTitleHash);
    // 查询结果并返回
    List<Long> prjIds = super.createQuery(hql).setParameter("psnId", psnId)
        .setParameter("enTitleHash", enTitleHash == null ? zhTitleHash : enTitleHash)
        .setParameter("zhTitleHash", zhTitleHash == null ? enTitleHash : zhTitleHash).setMaxResults(1).list();
    if (prjIds != null && prjIds.size() > 0) {
      return prjIds.get(0);
    }
    return null;
  }

}
