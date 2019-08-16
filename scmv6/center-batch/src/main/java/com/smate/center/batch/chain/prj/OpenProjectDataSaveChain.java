package com.smate.center.batch.chain.prj;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.context.OpenProjectContext;
import com.smate.center.batch.model.rol.prj.ProjectScheme;
import com.smate.center.batch.model.sns.prj.OpenProject;
import com.smate.center.batch.model.sns.prj.PrjRelatedPubRefresh;
import com.smate.center.batch.oldXml.prj.PrjRelatedPubContant;
import com.smate.center.batch.oldXml.prj.PrjXmlConstants;
import com.smate.center.batch.oldXml.prj.PrjXmlDocument;
import com.smate.center.batch.service.projectmerge.OpenProjectService;
import com.smate.center.batch.service.projectmerge.ProjectStatisticsService;
import com.smate.center.batch.service.projectmerge.ProjectXmlService;
import com.smate.core.base.project.model.PrjMember;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.utils.string.IrisNumberUtils;

/**
 * 数据保存
 * 
 * @author LXZ
 * 
 * @since 6.0.1
 * @version 6.0.1
 * @throws Exception
 */
public class OpenProjectDataSaveChain implements OpenProjectBaseChain {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private OpenProjectService openProjectService;
  @Autowired
  private ProjectStatisticsService projectStatisticsService;
  @Autowired
  private ProjectXmlService projectXmlService;

  @Override
  public boolean can(OpenProjectContext context, OpenProject project) {
    return true;
  }

  @Override
  public OpenProjectContext run(OpenProjectContext context, OpenProject project) throws Exception {
    Project prj = new Project();
    saveProject(context, prj);
    return context;
  }

  /**
   * 保存sns.project,sns.prj|_member等相关数据
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param context
   * @param prj
   * @throws Exception
   */
  private void saveProject(OpenProjectContext context, Project prj) throws Exception {
    PrjXmlDocument doc = context.getXmlDocument();
    Long psnId = Long.valueOf(doc.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "create_psn_id"));
    prj.setCreatePsnId(psnId);
    prj.setStatus(0);
    prj.setPsnId(psnId);
    this.wrapProjectSaveField(doc, context, prj);
    prj.setVersionNo(1);
    prj.setCreateDate(new Date());
    prj.setCreatePsnId(psnId);
    prj.setPrjReviews(0);
    prj.setPrjStartPsns(0);
    openProjectService.snsProjectDaoSave(prj);
    doc.setXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "prj_id", prj.getId().toString());
    doc.setXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "version_no", prj.getVersionNo().toString());
    context.setCurrentPrjId(prj.getId());
    // 保存人员关系
    this.savePrjMember(doc, context);
    prj.setAuthorNames(
        StringUtils.substring(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names"), 0, 200));

    prj.setAuthorNamesEn(
        StringUtils.substring(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names_en"), 0, 200));
    openProjectService.snsProjectDaoSave(prj);
    // 清理缓存
    // projectStatisticsService.clearAllPrjStatistic(context.getCurrentUserId());
    // 存储XML数据
    String xml = doc.getXmlString();
    projectXmlService.save(context.getCurrentPrjId(), xml);
    Integer sy = prj.getStartYear() == null ? 0 : prj.getStartYear();
    Integer sm = prj.getStartMonth() == null ? 0 : prj.getStartMonth();
    Integer searchTime = sy * 100 + sm;
    // 保存资助类别
    ProjectScheme pojo = new ProjectScheme();
    pojo.setId(prj.getId());
    pojo.setPsnId(prj.getPsnId());
    pojo.setAgencyName(prj.getAgencyName());
    pojo.setAgencyEnName(prj.getEnAgencyName());
    pojo.setAmount(prj.getAmount());
    pojo.setSchemeName(prj.getSchemeName());
    pojo.setSchemeEnName(prj.getEnSchemeName());
    pojo.setStartYear(prj.getStartYear());
    pojo.setStartMonth(prj.getStartMonth());
    pojo.setSearchTime(searchTime);
    pojo.setFundingYear(prj.getFundingYear());
    openProjectService.projectSchemeDaoSave(pojo);
    // 以下需要(项目保存事务提交之后)--start
    PrjRelatedPubRefresh prjRelatedPubRefresh =
        new PrjRelatedPubRefresh(prj.getId(), null, psnId, PrjRelatedPubContant.REFRESH_SOURCE_PRJ, 0);
    openProjectService.prjRelatedPubRefreshDaoSave(prjRelatedPubRefresh);
    // 以下需要(项目保存事务提交之后)--end
  }

  /**
   * 这里放的是项目录入和编辑时都需要保存更新的字段.
   * 
   * @param doc
   * @param context
   * @param now
   * @param prj
   */
  private void wrapProjectSaveField(PrjXmlDocument doc, OpenProjectContext context, Project prj) {

    Date now = new Date();

    // 项目批准号(本机构)
    prj.setInternalNo(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "prj_internal_no"));
    // 项目批准号（资助机构)
    prj.setExternalNo(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "prj_external_no"));
    // 资金总数
    prj.setAmount(IrisNumberUtils.createBigDecimal(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount")));
    // 资金单位
    prj.setAmountUnit(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount_unit"));
    // 参与方式：主导单位1/参与单位0
    prj.setIsPrincipalIns(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "is_principal_ins")));
    // 项目状态01进行中/02完成/03其他,04:申请项目
    prj.setState(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "prj_state"));
    // 项目年度
    prj.setFundingYear(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "funding_year")));
    // 资助机构名称
    prj.setAgencyName(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name"));
    prj.setEnAgencyName(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_name_en"));
    // 资助机构ID
    prj.setAgencyId(
        IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_agency_id")));
    // 资助类别ID
    prj.setSchemeId(IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_id")));
    // 资助类别名称
    prj.setSchemeName(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name"));
    prj.setEnSchemeName(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "scheme_name_en"));
    // 项目类型,1：内部项目，0:外部项目
    prj.setType(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "prj_type"));
    // 开始日期
    prj.setStartYear(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "start_year")));
    prj.setStartMonth(
        IrisNumberUtils.monthDayToInteger(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "start_month")));
    prj.setStartDay(
        IrisNumberUtils.monthDayToInteger(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "start_day")));
    // 结束日期
    prj.setEndYear(IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "end_year")));
    prj.setEndMonth(
        IrisNumberUtils.monthDayToInteger(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "end_month")));
    prj.setEndDay(IrisNumberUtils.monthDayToInteger(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "end_day")));
    // 依托单位Id
    prj.setInsId(IrisNumberUtils.createLong(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "ins_id")));
    // 依托单位名
    prj.setInsName(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "ins_name"));
    // 数据来源DBID
    prj.setDbId(IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "source_db_id")));
    // 最后更新时间
    prj.setUpdateDate(now);
    // 最后更新人
    prj.setUpdatePsnId(Long.valueOf(doc.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "create_psn_id")));
    // 是否校验通过
    prj.setIsValid(IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "is_valid")));
    // 数据来源
    prj.setRecordFrom(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "record_from")));
    // 外文标题
    prj.setEnTitle(
        StringUtils.substring(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title_text"), 0, 250));
    prj.setEnTitleHash(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_title_hash")));
    // 中文标题
    prj.setZhTitle(
        StringUtils.substring(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title_text"), 0, 250));
    prj.setZhTitleHash(
        IrisNumberUtils.createInteger(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_title_hash")));
    // brief
    prj.setBriefDesc(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "brief_desc"));
    prj.setBriefDescEn(doc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "brief_desc_en"));
  }

  /**
   * 录入项目时保存成果、人员关系数据.
   * 
   * @param doc
   * @return @throws
   */
  @SuppressWarnings({"unchecked", "deprecation"})
  private Set<Long> savePrjMember(PrjXmlDocument doc, OpenProjectContext context) throws Exception {

    try {
      Long prjId = context.getCurrentPrjId();

      List<Node> ndList = doc.getPrjMembers();
      if (ndList == null || ndList.size() == 0)
        return null;
      String authorNamesZh = "";
      String authorNamesEn = "";
      Set<Long> insSet = new HashSet<Long>();
      for (Node node : ndList) {

        Element em = (Element) node;

        PrjMember pm = null;
        // 获取成员ID，如果存在则查找修改
        Long pmId = IrisNumberUtils.createLong(em.attributeValue("pm_id"));
        if (pmId != null) {
          pm = openProjectService.snsProjectDaoGetMemberById(pmId);
          // 创建成员
          if (pm == null) {
            pm = new PrjMember();
          }
        } else {
          // 创建成员
          pm = new PrjMember();
        }
        // 项目负责人
        pm.setNotifyAuthor(IrisNumberUtils.createInteger(em.attributeValue("notify_author")));
        pm.setEmail(em.attributeValue("email"));
        pm.setInsCount(IrisNumberUtils.createInteger(em.attributeValue("ins_count")));
        String zhName = StringUtils.substring(em.attributeValue("member_psn_name"), 0, 50);
        String enName = StringUtils.substring(em.attributeValue("member_psn_name_en"), 0, 50);
        pm.setName(StringUtils.isBlank(zhName) ? "null" : zhName);
        Integer owner = IrisNumberUtils.createInteger(em.attributeValue("owner"));
        if (owner == null)
          owner = 0;
        pm.setOwner(owner);
        pm.setPrjId(prjId);
        Long memberPsnId = IrisNumberUtils.createLong(em.attributeValue("member_psn_id"));
        pm.setPsnId(memberPsnId);

        pm.setSeqNo(IrisNumberUtils.createInteger(em.attributeValue("seq_no")));
        // 单位、成果数据
        for (int i = 1; i < 6; i++) {
          Long insId = IrisNumberUtils.createLong(em.attributeValue("ins_id" + i));
          if (insId != null && insId > 0)
            insSet.add(insId);
        }
        pm.setInsCount(insSet.size());
        // 重构作者
        authorNamesZh = this.rebuildPrjXmlAuthorNames(authorNamesZh, em, zhName, enName, "zh");
        authorNamesEn = this.rebuildPrjXmlAuthorNames(authorNamesEn, em, zhName, enName, "en");

        // 保存数据
        openProjectService.snsProjectDaoSavePrjMember(pm);
        // 成员ID写入XML
        em.addAttribute("pm_id", pm.getId().toString());
      }

      doc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names",
          StringUtils.isBlank(authorNamesZh) ? "" : authorNamesZh.substring(1));

      doc.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names_en",
          StringUtils.isBlank(authorNamesEn) ? "" : authorNamesEn.substring(1));

      return insSet;

    } catch (Exception e) {
      logger.error("savePrjMember保存成员出错 ", e);
      throw e;
    }

  }

  /**
   * 构造作者
   * 
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param author_names
   * @param em
   * @param nameZh
   * @param nameEn
   * @param lang
   * @return
   */
  private String rebuildPrjXmlAuthorNames(String author_names, Element em, String nameZh, String nameEn, String lang) {
    if (StringUtils.isBlank(nameZh) && StringUtils.isBlank(nameEn))
      return author_names;
    String authorName = "";
    if ("zh".equalsIgnoreCase(lang)) {
      authorName = StringUtils.isBlank(nameZh) ? nameEn : nameZh;
    } else {
      authorName = StringUtils.isBlank(nameEn) ? nameZh : nameEn;
    }
    if (1 == (IrisNumberUtils.createInteger(em.attributeValue("notify_author")) == null ? 0
        : (IrisNumberUtils.createInteger(em.attributeValue("notify_author"))))) {
      author_names += "; *" + authorName; // 是项目负责人，则名称前加*号
    } else if (null == IrisNumberUtils.createLong(em.attributeValue("member_psn_id"))) {
      author_names += "; " + authorName;
    } else {
      author_names += "; " + String.format("<strong>%s</strong>", authorName);// 可以关联到psn_id,则加粗显示
    }
    return author_names;
  }

}
