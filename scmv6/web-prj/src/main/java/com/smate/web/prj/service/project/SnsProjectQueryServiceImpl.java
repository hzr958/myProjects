package com.smate.web.prj.service.project;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.consts.model.ConstDiscipline;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.project.dao.PrjPubDao;
import com.smate.core.base.project.dao.PrjReportDao;
import com.smate.core.base.project.dao.ProjectExpenditureDao;
import com.smate.core.base.project.model.PrjReport;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.model.ProjectExpenditure;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigPrjDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.model.psncnf.PsnConfigPrj;
import com.smate.core.base.psn.service.vip.PersonVipService;
import com.smate.core.base.statistics.PsnStatisticsUtils;
import com.smate.core.base.utils.common.MoneyFormatterUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.fund.dao.wechat.ConstDisplineDao;
import com.smate.web.fund.recommend.dao.CategoryMapBaseDao;
import com.smate.web.fund.recommend.dao.DynamicAwardPsnDao;
import com.smate.web.fund.recommend.dao.DynamicAwardResDao;
import com.smate.web.fund.recommend.model.CategoryMapBase;
import com.smate.web.prj.build.factor.PrjInfoBuildFactory;
import com.smate.web.prj.consts.PsnCnfConst;
import com.smate.web.prj.dao.project.CategoryMapScmNsfcDao;
import com.smate.web.prj.dao.project.PrjGroupRelationDao;
import com.smate.web.prj.dao.project.ScmPrjXmlDao;
import com.smate.web.prj.dao.project.SnsProjectQueryDao;
import com.smate.web.prj.dao.wechat.ProjectStatisticsDao;
import com.smate.web.prj.enums.PrjInfoQueryEnum;
import com.smate.web.prj.form.ProjectForm;
import com.smate.web.prj.form.ProjectQueryForm;
import com.smate.web.prj.form.wechat.PrjWeChatForm;
import com.smate.web.prj.model.common.CategoryMapScmNsfc;
import com.smate.web.prj.model.common.DynamicAwardPsn;
import com.smate.web.prj.model.common.DynamicAwardRes;
import com.smate.web.prj.model.common.GroupPsnNode;
import com.smate.web.prj.model.common.PrjGroupRelation;
import com.smate.web.prj.model.common.PrjInfo;
import com.smate.web.prj.model.common.ScmPrjXml;
import com.smate.web.prj.xml.PrjXmlConstants;
import com.smate.web.prj.xml.PrjXmlDocument;

/**
 * 
 * 项目列表
 * 
 * @author zx
 *
 */
@Service("snsProjectQueryService")
@Transactional(rollbackFor = Exception.class)
public class SnsProjectQueryServiceImpl implements SnsProjectQueryService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private SnsProjectQueryDao snsProjectQueryDao;
  @Autowired
  private PrjGroupRelationDao prjGroupRelationDao;
  @Autowired
  private ScmPrjXmlDao scmPrjXmlDao;
  // @Autowired
  // private ProjectLogService projectLogService;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private PsnConfigDao psnConfigDao;
  @Autowired
  private PsnConfigPrjDao psnConfigPrjDao;
  @Autowired
  private ProjectStatisticsDao projectStatisticsDao;
  // 资助机构,其它选项
  private static final String OTHER_AGENCY = "other_agency_name";
  @Autowired
  private PrjInfoBuildFactory prjInfoBuildFactory;
  @Autowired
  private DynamicAwardResDao dynamicAwardResDao;
  @Autowired
  private DynamicAwardPsnDao dynamicAwardPsnDao;
  @Autowired
  private ConstDisplineDao displineDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;
  @Autowired
  private CategoryMapScmNsfcDao categoryMapScmNsfcDao;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private PersonVipService personVipService;
  @Autowired
  private PrjPubDao prjPubDao;
  @Autowired
  private PrjReportDao prjReportDao;
  @Autowired
  private ProjectExpenditureDao projectExpenditureDao;

  /**
   * 查询项目列表
   */
  @Override
  public void queryOutput(ProjectQueryForm query, Page page) throws ServiceException {
    Long psnId = SecurityUtils.getCurrentUserId();
    // 调用接口获取用户是否VIP
    boolean isVIP = personVipService.checkVIPByPsnId(psnId);
    query.setIsVIP(isVIP);
    // 判断自己查看项目还是别人查看项目
    Locale locale = LocaleContextHolder.getLocale();
    List<ProjectForm> prjForm = new ArrayList<ProjectForm>();
    try {
      // 判断自己查看项目还是别人查看项目
      if (StringUtils.isNotBlank(query.getDes3CurrentId())) {
        Long currentUserId = Long.valueOf(ServiceUtil.decodeFromDes3(query.getDes3CurrentId()));
        bulidParameter(query);
        // 用于资助机构点击其它时候
        if (StringUtils.isNotBlank(query.getAgency()) && query.getAgency().contains(OTHER_AGENCY)) {
          if (!currentUserId.equals(psnId)) {
            query.setOthersSee(true);
          }
          queryAgencyName(query);
          bulidAgencyName(query);
        }
        if (!currentUserId.equals(psnId)) {
          query.setOthersSee(true);
          List<Long> prjIds = snsProjectQueryDao.findNoPsnPrjPrivacy(currentUserId);// 查看他人项目
          if (!prjIds.isEmpty()) {
            List<Project> projects = snsProjectQueryDao.findPsnPrj(prjIds, page, query);
            bulidProjectform(projects, prjForm);
            // 获取全文下载的权限
            /* getDownloadFulltext(projects, page); */
          } else {
            page.setTotalCount(0);
          }
        } else {
          List<Project> projects = snsProjectQueryDao.searchProject(psnId, query, page); // 查询自己的项目
          if (!projects.isEmpty()) {
            bulidProjectform(projects, prjForm);
            bulidAbstractKeywords(prjForm);
            buildVipStatistics(prjForm);
          }
        }
      } else {
        List<Project> projects = snsProjectQueryDao.searchProject(psnId, query, page); // 查询自己的项目
        if (!projects.isEmpty()) {
          bulidProjectform(projects, prjForm);
          bulidAbstractKeywords(prjForm);
          buildVipStatistics(prjForm);
        }
      }
      prjGroupRelation(query, prjForm);// 查询项目和群组是否有关联
      wrapPopulateData(prjForm, true, locale);
      page.setResult(prjForm);
    } catch (Exception e) {
      logger.error("项目列表出错", e);
    }
  }

  /**
   * 查询项目列表
   */
  @Override
  public void queryPrjList(PrjWeChatForm form) throws ServiceException {
    try {
      snsProjectQueryDao.searchProject(form); // 查询自己的项目

      List<PrjInfo> result = form.getPage().getResult();
      List<PrjInfo> prjInfoList = new ArrayList<>();
      if (CollectionUtils.isNotEmpty(result)) {
        for (PrjInfo prjInfo : result) {
          prjInfo.setProjectStatistics(projectStatisticsDao.get(prjInfo.getPrjId()));// TODO
          prjInfoBuildFactory.buildPubInfo(PrjInfoQueryEnum.WECHAT.toInt(), prjInfo);

          Long cnfId = psnConfigDao.getPsnConfId(form.getPsnId());
          String anyUser = String.valueOf(psnConfigPrjDao.getAnyUser(cnfId, prjInfo.getPrjId()));
          prjInfo.setAnyUser(anyUser);// 获取项目的隐私
          // 是否赞过
          DynamicAwardRes dynamicAwardRes = dynamicAwardResDao.getDynamicAwardRes(prjInfo.getPrjId(), 4, 1);
          prjInfo.setAward(0);
          if (dynamicAwardRes != null && form.getCurrentPsnId() != null) {
            DynamicAwardPsn dynamicAwardPsn =
                dynamicAwardPsnDao.getDynamicAwardPsn(form.getCurrentPsnId(), dynamicAwardRes.getAwardId());
            if (dynamicAwardPsn != null) {
              prjInfo.setAward(dynamicAwardPsn.getStatus());
            }
          }
          prjInfo.setDes3PrjId(Des3Utils.encodeToDes3(String.valueOf(prjInfo.getPrjId())));
          prjInfoList.add(prjInfo);
        }
      }
      form.getPage().setResult(prjInfoList);
    } catch (Exception e) {
      logger.error("项目列表出错,psnId={},searchPsnId={},", form.getPsnId(), form.getSearchPsnId(), e);
    }
  }

  /**
   * true = 删除
   * 
   * @param form
   * @return
   * @throws ServiceException
   */
  @Override
  public Boolean checkPrjExist(ProjectQueryForm form) throws ServiceException {
    Project project = snsProjectQueryDao.get(form.getPrjId());
    if (project != null && project.getStatus() != 1) {
      return false;
    }
    return true;
  }

  // 解析前台传来的值并保存
  public void bulidParameter(ProjectQueryForm query) {
    if (StringUtils.isNotBlank(query.getAgency())) {
      String agencyName[] = query.getAgency().split(",");
      List<String> agencyNamesList = new ArrayList<String>();
      for (String agencyNames : agencyName) {
        agencyNamesList.add(agencyNames);
      }
      query.setAgencyName(agencyNamesList);
    }
    if (StringUtils.isNotBlank(query.getSearchKey())) {
      query.setSearchKey(HtmlUtils.htmlUnescape(query.getSearchKey()));
    }
  }

  // 把项目project 传到projectform 中

  public void bulidProjectform(List<Project> prjs, List<ProjectForm> form) {
    if (CollectionUtils.isNotEmpty(prjs)) {
      for (Project project : prjs) {
        ProjectForm prjForm = new ProjectForm();
        if (project.getId() != null) {
          prjForm.setId(project.getId());
          prjForm.setDes3Id(ServiceUtil.encodeToDes3(project.getId().toString()));
        }
        if (StringUtils.isNotBlank(project.getExternalNo())) {
          prjForm.setExternalNo(project.getExternalNo());
        }
        if (StringUtils.isNotBlank(project.getZhTitle())) {
          prjForm.setZhTitle(project.getZhTitle());
        }
        if (StringUtils.isNotBlank(project.getEnTitle())) {
          prjForm.setEnTitle(project.getEnTitle());
        }
        if (StringUtils.isNotBlank(project.getAuthorNames())) {
          prjForm.setAuthorNames(project.getAuthorNames());
        }
        if (StringUtils.isNotBlank(project.getAuthorNamesEn())) {
          prjForm.setAuthorNamesEn(project.getAuthorNamesEn());
        }
        if (StringUtils.isNotBlank(project.getBriefDesc())) {
          prjForm.setBriefDesc(project.getBriefDesc());
        }
        if (StringUtils.isNotBlank(project.getBriefDescEn())) {
          prjForm.setBriefDescEn(project.getBriefDescEn());
        }
        if (StringUtils.isNotBlank(project.getFulltextFileId())) {
          prjForm.setFulltextField(project.getFulltextFileId());
          String downloadUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.ARCHIVE_FILE,
              Long.parseLong(project.getFulltextFileId()), 0L);
          prjForm.setDownloadUrl(downloadUrl);
        }
        form.add(prjForm);
      }
    }
  }

  public void buildVipStatistics(List<ProjectForm> prjForm) {
    for (ProjectForm project : prjForm) {
      Long prjId = project.getId();
      project.setPubCount(prjPubDao.getPrjPubSum(prjId));// 项目成果总数
      PrjReport prjReport = prjReportDao.getPrjReport(prjId);
      if (prjReport != null) {
        project.setReportType(prjReport.getReportType());// 项目报告
      }
      List<ProjectExpenditure> expenList = projectExpenditureDao.listByPrjId(prjId);
      Float availableTotal = 0.00f;
      for (ProjectExpenditure prjExpen : expenList) {
        Float availableAmount = (prjExpen.getSchemeAmount() - prjExpen.getAdvanceAmount() - prjExpen.getUsedAmount());
        availableTotal += availableAmount;
      }
      if (availableTotal > 0) {
        project.setAvailableTotal(MoneyFormatterUtils.format(availableTotal));
      }
    }
  }
  /*
   * @Override public String getRecommendAgencyYear(ProjectQueryForm query) throws Exception {
   * 
   * return null; }
   */

  /**
   * 站外获取项目主页
   */
  @Override
  public void queryOutSidePrj(ProjectQueryForm query, Page page) throws ServiceException {
    Long psnId = Long.valueOf(ServiceUtil.decodeFromDes3(query.getDes3CurrentId()));
    List<ProjectForm> prjForm = new ArrayList<ProjectForm>();
    try {
      bulidParameter(query);
      // 用于资助机构点击其它时候
      if (StringUtils.isNotBlank(query.getAgency()) && query.getAgency().contains(OTHER_AGENCY)) {
        query.setOthersSee(true);
        queryAgencyName(query);
        bulidAgencyName(query);
      }
      List<Long> prjIds = snsProjectQueryDao.findNoPsnPrjPrivacy(psnId);// 查看他人项目
      if (!prjIds.isEmpty()) {
        List<Project> projects = snsProjectQueryDao.findPsnPrj(prjIds, page, query);
        bulidProjectform(projects, prjForm);
        // 获取全文下载的权限
        /* getDownloadFulltext(projects, page); */
        Locale locale = LocaleContextHolder.getLocale();
        wrapPopulateData(prjForm, true, locale);
        page.setResult(prjForm);
        /*
         * Long prjSum = psnStatisticsDao.getPrjNum(psnId); page.setTotalCount(prjSum);
         */
      } else {
        page.setTotalCount(0);
      }
    } catch (Exception e) {
      logger.error("获取全文权限出错", e);
    }
  }

  /**
   * 查询资助机构
   */
  @Override
  public void queryAgencyName(ProjectQueryForm query) throws Exception {
    List<Map<String, Object>> listMap = new ArrayList<Map<String, Object>>();
    List<Map<String, Object>> prjList = new ArrayList<Map<String, Object>>();
    Long psnId = query.getSearchPsnId();
    if (psnId == null || psnId.longValue() == 0L) {
      psnId = NumberUtils.toLong(Des3Utils.decodeFromDes3(query.getDes3CurrentId()));
    }
    prjList = snsProjectQueryDao.findAgencyName(psnId, query.getOthersSee());
    if (CollectionUtils.isNotEmpty(prjList)) {
      for (Map<String, Object> prj : prjList) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("agencyName",
            StringUtils.isNotBlank(prj.get("agencyName").toString()) ? prj.get("agencyName").toString() : "");
        listMap.add(map);
      }

    }
    query.setAgencyNameList(listMap);
  }

  /**
   * 解析资助机构
   */
  public void bulidAgencyName(ProjectQueryForm query) {
    List<Map<String, Object>> agencyNameList = query.getAgencyNameList();
    List<String> agencyName = new ArrayList<String>();
    if (CollectionUtils.isNotEmpty(agencyNameList)) {
      for (Map<String, Object> list : query.getAgencyNameList()) {
        if (!query.getAgencyName().contains(list.get("agencyName").toString())) {
          agencyName.add(list.get("agencyName").toString());
        }
      }
      query.setAllAgencyName(agencyName);
      query.setAgencyName(agencyName);
      if (CollectionUtils.isEmpty(query.getAgencyNameList())) {

      }
    } else if (OTHER_AGENCY.equals(query.getAgency())) {
      query.getAgencyName().clear();
    }
  }

  /**
   * 获取全文下载的权限
   * 
   * @param projects
   * @return
   * @throws Exception
   */
  public void getDownloadFulltext(List<Project> projects, Page page) throws Exception {

    List<Long> prjIds = new ArrayList<Long>();
    for (Project project : projects) {
      prjIds.add(project.getId());
    }
    List<ScmPrjXml> scmPrjXmls = scmPrjXmlDao.getBatchScmPrjXml(prjIds);
    List<Long> fulltextPrjIds = new ArrayList<Long>();
    for (ScmPrjXml scmPrjXml : scmPrjXmls) {
      PrjXmlDocument xmlDocument = new PrjXmlDocument(scmPrjXml.getPrjXml());
      if (!xmlDocument.existsNode(PrjXmlConstants.PRJ_FULLTEXT_XPATH)) {
        continue;
      }
      String permission = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PRJ_FULLTEXT_XPATH, "permission");
      if ("2".equals(permission) || "1".equals(permission)) {
        fulltextPrjIds.add(scmPrjXml.getPrjId());
      }
    }
    // 设置好只让本人下载的标示
    for (Project project : projects) {
      for (int i = 0; i < fulltextPrjIds.size(); i++) {
        if (project.getId().equals(fulltextPrjIds.get(i))) {
          project.setDownUploadFulltext(false);
        }
      }
    }
    page.setResult(projects);
  }

  private void bulidAbstractKeywords(List<ProjectForm> prjForm) throws ServiceException {

    try {
      Locale locale = LocaleContextHolder.getLocale();
      List<Long> prjIds = new ArrayList<Long>();

      for (ProjectForm project : prjForm) {
        prjIds.add(project.getId());
      }
      List<ScmPrjXml> scmPrjXmls = scmPrjXmlDao.findByIds(prjIds);
      for (ProjectForm project : prjForm) {
        for (ScmPrjXml scmPrjXml : scmPrjXmls) {
          if (scmPrjXml.getPrjId().equals(project.getId())) {
            PrjXmlDocument xmlDocument = new PrjXmlDocument(scmPrjXml.getPrjXml());
            dealDiscipline(project, xmlDocument);
            if (Locale.CHINA.equals(locale)) {
              String zhKeywords = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_keywords");
              String zhAbstract = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_abstract");
              String enAbstract = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_abstract");
              String enKeywords = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_keywords");
              zhAbstract = StringUtils.isNotBlank(zhAbstract) ? zhAbstract : enAbstract;
              zhAbstract = XmlUtil.trimAllHtml(zhAbstract);
              project.setAbstracts(zhAbstract);
              if (StringUtils.isNotBlank(zhKeywords)) {
                zhKeywords = zhKeywords.replace(",", ";");
                project.setKeywords(zhKeywords);
              } else if (StringUtils.isNotBlank(enKeywords)) {
                enKeywords = enKeywords.replace(",", ";");
                project.setKeywords(enKeywords);
              }
            } else {
              String zhKeywords = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_keywords");
              String zhAbstract = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_abstract");
              String enAbstract = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_abstract");
              String enKeywords = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_keywords");
              enAbstract = StringUtils.isNotBlank(enAbstract) ? enAbstract : zhAbstract;
              zhAbstract = XmlUtil.trimAllHtml(enAbstract);
              project.setAbstracts(enAbstract);
              if (StringUtils.isNotBlank(enKeywords)) {
                enKeywords = enKeywords.replace(",", ";");
                project.setKeywords(enKeywords);
              } else if (StringUtils.isNotBlank(zhKeywords)) {
                zhKeywords = zhKeywords.replace(",", ";");
                project.setKeywords(zhKeywords);
              }
            }
          }
        }
      }

    } catch (Exception e) {
      logger.error("查找scmprjxml出错", e);
    }

  }

  private void dealDiscipline(ProjectForm form, PrjXmlDocument xmlDocument) {
    String areaId = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "areaId");
    String id = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "discipline");
    if (StringUtils.isNotBlank(areaId)) {
      CategoryMapBase category = categoryMapBaseDao.get(NumberUtils.toInt(areaId));
      if (category != null) {
        CategoryMapBase firstCategory = categoryMapBaseDao.get(category.getSuperCategoryId());
        form.getDisciplineMap().put("firstCategoryId", category.getSuperCategoryId());
        form.getDisciplineMap().put("secondCategoryId", category.getCategryId());
        Locale locale = LocaleContextHolder.getLocale();
        if (Locale.CHINA.equals(locale)) {
          form.getDisciplineMap().put("firstCategory", firstCategory.getCategoryZh());
          form.getDisciplineMap().put("secondCategory", category.getCategoryZh());
        } else {
          form.getDisciplineMap().put("firstCategory", firstCategory.getCategoryEn());
          form.getDisciplineMap().put("secondCategory", category.getCategoryEn());
        }
      }
    } else {
      if (StringUtils.isBlank(id) || !NumberUtils.isDigits(id)) {
        return;
      }
      ConstDiscipline discipline = displineDao.get(NumberUtils.toLong(id));
      if (discipline != null && StringUtils.isNotBlank(discipline.getDiscCode())
          && discipline.getDiscCode().length() == 3) {
        CategoryMapScmNsfc scmNsfc = categoryMapScmNsfcDao.findScmNsfc(discipline.getDiscCode());
        if (scmNsfc != null) {
          CategoryMapBase secondCategory =
              categoryMapBaseDao.get(NumberUtils.toInt(scmNsfc.getScmCategoryId().toString()));
          if (secondCategory != null) {
            CategoryMapBase firstCategory = categoryMapBaseDao.get(secondCategory.getSuperCategoryId());
            form.getDisciplineMap().put("firstCategoryId", secondCategory.getSuperCategoryId());
            form.getDisciplineMap().put("secondCategoryId", secondCategory.getCategryId());
            Locale locale = LocaleContextHolder.getLocale();
            if (Locale.CHINA.equals(locale)) {
              form.getDisciplineMap().put("firstCategory", firstCategory.getCategoryZh());
              form.getDisciplineMap().put("secondCategory", secondCategory.getCategoryZh());
            } else {
              form.getDisciplineMap().put("firstCategory", firstCategory.getCategoryEn());
              form.getDisciplineMap().put("secondCategory", secondCategory.getCategoryEn());
            }
          }
        }
      }
    }
  }

  /**
   * 项目和群组是否有关联
   */
  @Override
  public void prjGroupRelation(ProjectQueryForm form, List<ProjectForm> prjForm) throws ServiceException {

    Long psnId = SecurityUtils.getCurrentUserId();
    for (ProjectForm item : prjForm) {
      Long groupId = null;
      String privacy = null;
      GroupPsnNode groupPsnNode = null;
      try {
        groupId = prjGroupRelationDao.findGroupIdByPrjId(item.getId());

      } catch (Exception e) {
        logger.error("项目列表-设置群组id出错", e);
      }
      if (groupId != null) {
        try {
          item.setDes3GroupId(ServiceUtil.encodeToDes3(groupId.toString()));
          /*
           * groupPsnNode = this.groupPsnNodeDao.findGroupPnsNode(groupId);
           */
          if (form.getOthersSee() != true) {
            item.setShowPrjGroup(true);
          } else {
            privacy = prjGroupRelationDao.findGroupPrivacy(groupId);
            if (!("P".equals(privacy))) {
              item.setShowPrjGroup(true);

            } else {
              Long id = prjGroupRelationDao.findGroupMember(psnId, groupId);
              if (id != null) {
                item.setShowPrjGroup(true);
              }
            }
          }
        } catch (Exception e) {
          logger.error("项目列表-查找群组节点出错", e);
        }
      }
      item.setGroupId(groupId);
      /*
       * if (groupPsnNode != null) { item.setGroupNodeId(groupPsnNode.getNodeId());
       * item.setGroupName(groupPsnNode.getGroupName()); }
       */
    }
  }

  /**
   * 修改此类为仅供内部调用_MJG_SCM-5615.
   * 
   * @param prjForm
   * @param isFillErrorField
   * @param locale
   * @throws ServiceException
   */
  @SuppressWarnings("unused")
  private void wrapPopulateData(List<ProjectForm> prjForm, boolean isFillErrorField, Locale locale)
      throws ServiceException {

    for (ProjectForm form : prjForm) {
      this.bulidBaseInfo(form, locale);
    }
  }

  /**
   * 后台编辑显示列表信息
   */
  @SuppressWarnings("unused")
  private void bulidBaseInfo(ProjectForm item, Locale locale) throws ServiceException {
    try {
      String briefDescTmp = "";
      String title = "";
      String authorNames = "";
      if (Locale.CHINA.equals(locale)) {
        title = StringUtils.isNotBlank(item.getZhTitle()) ? item.getZhTitle() : item.getEnTitle();
        briefDescTmp = StringUtils.isNotBlank(item.getBriefDesc()) ? item.getBriefDesc() : item.getBriefDescEn();
        item.setTitle(title);
        authorNames = StringUtils.isNotBlank(item.getAuthorNames()) ? item.getAuthorNames() : item.getAuthorNamesEn();
        item.setAuthorNames(authorNames);
        authorNames = StringUtils.isNotBlank(item.getAuthorNames()) ? item.getAuthorNames() : item.getAuthorNamesEn();
        item.setAuthorNames(authorNames);
      } else if (Locale.US.equals(locale)) {
        title = StringUtils.isNotBlank(item.getEnTitle()) ? item.getEnTitle() : item.getZhTitle();
        briefDescTmp = StringUtils.isNotBlank(item.getBriefDescEn()) ? item.getBriefDescEn() : item.getBriefDesc();
        item.setTitle(title);
        authorNames = StringUtils.isNotBlank(item.getAuthorNamesEn()) ? item.getAuthorNamesEn() : item.getAuthorNames();
        item.setAuthorNames(authorNames);

      } else {
        title = item.getZhTitle();
        briefDescTmp = item.getBriefDesc();
        authorNames = item.getAuthorNames();
      }
      if (!StringUtils.isBlank(briefDescTmp)) {
        briefDescTmp = briefDescTmp.replace(">", "&gt;").replace("<", "&lt;");

      }
      if (!StringUtils.isBlank(authorNames)) {
        // SCM-9352 统一使用英文标点符号lhd
        // html.append(XmlUtil.formateSymbolAuthors(title,
        // authorNames));
        // authorNames = authorNames.replace("；", "; ");
        // authorNames = authorNames.replace(" ;", "; ");
        // authorNames = authorNames.replace(";", "; ");
        String[] authorStr = authorNames.replaceAll("&nbsp;", "").split("，|；|;|,| and ");
        item.setAuthorNames(StringUtils.join(StringUtils.stripAll(authorStr), ";&nbsp;"));
        item.setNoneHtmlLableAuthorNames(html2Text(item.getAuthorNames()));
      }
      // 第三行：来源
      if (!StringUtils.isBlank(briefDescTmp)) {

        // SCM-9352 统一使用英文标点符号lhd
        // String text=(XmlUtil.formateSymbol(title, briefDescTmp));
        String text = briefDescTmp;
        text = text.replace("；", "; ");
        text = text.replace("，", ", ");
        text = text.replace("。", ". ");
        // text = text.replace(".", ". ");
        text = text.replace(" .", ". ");
        text = text.replace(",", ", ");
        text = text.replace(" ,", ", ");
        text = text.replace(" ;", "; ");
        text = text.replace(";", "; ");
        // text =text.replace("，", ",");
        String regex = "[A-Z]{3}(\\s*)([0-9]*,*,*)(\\s*)([0-9]*)(,?\\s*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
          String a = matcher.group(0);
          String b = a.replace(", ", ",");
          text = text.replace(a, b);
        }
        item.setBriefDesc(briefDescTmp);
      }
    } catch (Exception e) {
      logger.error("编辑项目列表基础信息出错", e);
    }

  }

  /**
   * 资助机构和年份的回显数据
   */
  @Override
  public String getRecommendAgencyYear(ProjectQueryForm query) throws Exception {
    Long psnId = null;
    if (query.getOthersSee()) {
      psnId = Long.valueOf(ServiceUtil.decodeFromDes3(query.getDes3CurrentId()));
    } else {
      psnId = SecurityUtils.getCurrentUserId();
    }
    dataTypeConversion(query);
    List<Map<String, String>> agencyCounts = getAgencyCount(psnId, query.getAgencyCallback(), query.getFundingYear(),
        query.getOthersSee(), query.getSearchKey());
    List<Map<String, String>> yearCounts = getFundYearCount(psnId, query.getAgencyYear(), query.getAllAgencyName(),
        query.getOthersSee(), query.getSearchKey());

    List<Map<String, Object>> listCount = new ArrayList<Map<String, Object>>();
    Map<String, Object> recommendCount = new HashMap<String, Object>();
    recommendCount.put("agency", agencyCounts);
    recommendCount.put("fundingYear", yearCounts);
    listCount.add(recommendCount);
    String count = JacksonUtils.listToJsonStr(listCount);
    count = count.replace("[]", "{}");
    count = count.replace("[", "");
    count = count.replace("]", "");
    count = count.replace("},{", ",");
    count = StringEscapeUtils.unescapeHtml4(count);
    return count;
  }

  /**
   * 资助机构的回显数据
   */
  public List<Map<String, String>> getAgencyCount(Long psnId, List<String> agencyName, Integer fundYear,
      boolean othersSee, String title) {
    List<Map<String, Object>> agencyIds = new ArrayList<Map<String, Object>>();
    List<Map<String, String>> agencyCount = new ArrayList<Map<String, String>>();
    List<Map<String, String>> lsitMap = new ArrayList<Map<String, String>>();
    try {
      if (psnId != null) {
        agencyIds = snsProjectQueryDao.getAgencyCountByPsnId(psnId, agencyName, fundYear, othersSee, title);
        Long otherAgencyName =
            snsProjectQueryDao.getOtherAgencyCountByPsnId(psnId, agencyName, fundYear, othersSee, title);
        if (CollectionUtils.isNotEmpty(agencyIds)) {
          Locale locale = LocaleContextHolder.getLocale();
          for (Map<String, Object> list : agencyIds) {
            Map<String, String> map = new HashMap<String, String>();
            Map<String, String> agencyMap = new HashMap<String, String>();
            if (Locale.US.equals(locale)) {
              map.put("agencyName", list.get("enAgencyName") != null ? list.get("enAgencyName").toString()
                  : list.get("agencyName").toString());
            } else {
              map.put("agencyName", list.get("agencyName") != null ? list.get("agencyName").toString()
                  : list.get("enAgencyName").toString());
            }
            lsitMap.add(map);
            agencyMap.put(map.get("agencyName").toString(), list.get("count").toString());
            agencyCount.add(agencyMap);
          }
          removeAgecyName(agencyCount, psnId, fundYear, lsitMap);
        }
        // 资助机构其它
        Map<String, String> otherMap = new HashMap<String, String>();
        otherMap.put(OTHER_AGENCY, otherAgencyName.toString());
        agencyCount.add(otherMap);

      }
    } catch (Exception e) {
      logger.error("资助机构和年份的回显数据出错", e);
    }
    return agencyCount;
  }

  /**
   * 去掉重复数据
   */
  public void removeAgecyName(List<Map<String, String>> agencyCount, Long psnId, Integer fundYear,
      List<Map<String, String>> mapAgency) {
    // 去掉重复,把重复的
    Set<String> prjSet = new HashSet<String>();
    List<String> listName = new ArrayList<String>();
    List<Map<String, String>> numberList = new ArrayList<Map<String, String>>();
    List<Map<String, String>> removeList = new ArrayList<Map<String, String>>();
    for (Map<String, String> list : mapAgency) {
      if (!prjSet.add(list.get("agencyName"))) {
        listName.add(list.get("agencyName").toString());
      }
    }
    for (String name : listName) {
      int number = 0;
      for (Map<String, String> list : agencyCount) {
        if (StringUtils.isNotBlank(list.get(name))) {
          Map<String, String> map = new HashMap<String, String>();
          number = Integer.parseInt(list.get(name)) + number;
          map.put(name, list.get(name).toString());
          removeList.add(map);
        }

      }

      Map<String, String> map = new HashMap<String, String>();
      map.put(name, String.valueOf(number));
      numberList.add(map);

    }
    agencyCount.removeAll(removeList);
    agencyCount.addAll(numberList);
  }

  /**
   * 项目年份数据回显
   */
  public List<Map<String, String>> getFundYearCount(Long psnId, List<String> agencyName, List<String> agencyYear,
      boolean othersSee, String title) {
    List<Map<String, String>> yearList = new ArrayList<Map<String, String>>();
    if (psnId != null) {
      // 设置年份
      List<Map<String, Object>> fundYearList =
          snsProjectQueryDao.getYearCountByPsnId(psnId, agencyName, agencyYear, othersSee, title);
      // 查询不限年份统计数
      Long allYear = snsProjectQueryDao.getAllYearCountByPsnId(psnId, agencyName, agencyYear, othersSee, title);
      int currentYear = 0;
      int threeYear = 0;
      int fiveYear = 0;
      Map<String, String> yearMap = new HashMap<String, String>();
      // 获取当前年份
      Calendar date = Calendar.getInstance();
      int year = date.get(Calendar.YEAR);
      if (CollectionUtils.isNotEmpty(fundYearList) && fundYearList.size() > 0) {
        for (Map<String, Object> list : fundYearList) {
          // 近一年
          if (String.valueOf(year).equals(list.get("fundingYear").toString())) {
            currentYear = currentYear + Integer.parseInt(list.get("count").toString());
          }
          // 近三年
          if (String.valueOf(year).equals(list.get("fundingYear").toString())
              || String.valueOf(year - 1).equals(list.get("fundingYear").toString())
              || String.valueOf(year - 2).equals(list.get("fundingYear").toString())) {
            threeYear = threeYear + Integer.parseInt(list.get("count").toString());
          }
          // 近五年
          if (String.valueOf(year).equals(list.get("fundingYear").toString())
              || String.valueOf(year - 1).equals(list.get("fundingYear").toString())
              || String.valueOf(year - 2).equals(list.get("fundingYear").toString())
              || String.valueOf(year - 3).equals(list.get("fundingYear").toString())
              || String.valueOf(year - 4).equals(list.get("fundingYear").toString())) {
            fiveYear = fiveYear + Integer.parseInt(list.get("count").toString());
          }
        }

      }
      yearMap.put(String.valueOf(year), String.valueOf(currentYear));
      yearMap.put(String.valueOf(year - 2), String.valueOf(threeYear));
      yearMap.put(String.valueOf(year - 4), String.valueOf(fiveYear));
      yearMap.put(String.valueOf(0), String.valueOf(allYear));
      yearList.add(yearMap);

    }
    return yearList;
  }

  /**
   * 处理接受的数据类型转化
   * 
   * @param form
   */
  public void dataTypeConversion(ProjectQueryForm form) throws Exception {
    if (StringUtils.isNotBlank(form.getAgency())) {
      String agencyName[] = form.getAgency().split(",");
      List<String> agencyNamesList = new ArrayList<String>();
      for (String agencyNames : agencyName) {
        agencyNamesList.add(agencyNames);
      }
      form.setAgencyName(agencyNamesList);
    }
    List<String> agencyNameList = new ArrayList<String>();
    if (CollectionUtils.isNotEmpty(form.getAgencyNameList())) {
      for (Map<String, Object> list : form.getAgencyNameList()) {
        String agencyName = list.get("agencyName").toString();
        if (StringUtils.isNotBlank(agencyName)) {
          agencyNameList.add(agencyName);
        }
      }
      if (CollectionUtils.isNotEmpty(agencyNameList)) {
        form.setAgencyCallback(agencyNameList);
      }
    }
    // 解析前台到后台的资助机构列表
    if (StringUtils.isNotBlank(form.getAgency())) {
      String agencyName[] = form.getAgency().split(",");
      List<String> agencyNamesList = new ArrayList<String>();
      for (String agencyNames : agencyName) {
        agencyNamesList.add(agencyNames);
      }
      form.setAgencyYear(agencyNamesList);
    }
    if (form.getAgency().contains(OTHER_AGENCY)) {
      queryAgencyName(form);
      bulidAgencyName(form);
      form.setAgencyYear(form.getAllAgencyName());
    }
  }

  /**
   * 查询未上传全文数量
   */
  @Override
  public Integer findNoFullTextPrj() throws ServiceException {
    Long psnId = SecurityUtils.getCurrentUserId();
    /* psnId=1000000733628L; */
    int fulltextNum = snsProjectQueryDao.findNoFulltext(psnId);
    return fulltextNum;
  }

  @Override
  public Map<String, Object> queryPrjNumAmount() throws ServiceException {
    Long psnId = SecurityUtils.getCurrentUserId();
    Map<String, Object> prjNumAmount = snsProjectQueryDao.findPrjNumAmount(psnId);
    return prjNumAmount;
  }

  // 查找项目和群组是否有关联
  @Override
  public Long findGroupIdByPrjId(Long prjId) throws ServiceException {
    Long groupId = null;
    try {
      groupId = prjGroupRelationDao.findGroupIdByPrjId(prjId);
    } catch (Exception e) {
      logger.error("根据项目id查找群组id出错了！", e);
    }
    return groupId;
  }

  // 创建项目和群组的关联
  @Override
  public String GreatePrjGrpRelation(Long groupId, Long prjId) throws ServiceException {
    try {
      PrjGroupRelation prjGroupRelation = new PrjGroupRelation();
      prjGroupRelation.setGroupId(groupId);
      prjGroupRelation.setPrjId(prjId);
      prjGroupRelationDao.save(prjGroupRelation);
    } catch (Exception e) {
      logger.error("创建项目和群组的关联关系出错！", e);
      return "error";
    }
    return "sucess";
  }

  @Override
  public String queryPrjFulltextAuthority(Long prjId) throws ServiceException {
    try {
      ScmPrjXml scmPrjXml = scmPrjXmlDao.get(prjId);
      if (scmPrjXml != null) {
        PrjXmlDocument xmlDocument = new PrjXmlDocument(scmPrjXml.getPrjXml());
        if (xmlDocument != null) {
          return xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PRJ_FULLTEXT_XPATH, "permission");
        }
      }
    } catch (Exception e) {
      logger.error("获取项目全文权限出错,prjId= " + prjId, e);
    }
    return null;
  }

  /**
   * 删除项目
   */
  @Override
  public void deleteProject(String prjIds) throws ServiceException {

    Long psnId = SecurityUtils.getCurrentUserId();
    String[] tmpPrjIds = StringUtils.split(prjIds, ",");
    try {
      if (psnId != null) {
        /* Long cnfId=psnConfigDao.getPsnConfId(psnId); */
        PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
        if (psnStatistics != null) {
          for (String tmpPrjId : tmpPrjIds) {
            Long prjId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(tmpPrjId));
            Project prj = snsProjectQueryDao.get(prjId);
            if (prj != null && prj.getPsnId().equals(psnId) && prj.getStatus() != 1) {
              prj.setStatus(1);
              snsProjectQueryDao.save(prj);
              Map<String, String> opDetail = new HashMap<String, String>();
              opDetail.put("function", "my outputs");
              // projectLogService.logOp(prjId, psnId, null, ProjectOperationEnum.Delete, opDetail);
              Integer openPrjSum = updatePsnStatistics(psnId, prjId, psnStatistics.getOpenPrjSum());// 更新公开项目统计数
              psnConfigPrjDao.delsByPrjId(prjId);
              if (psnStatistics.getPrjSum() > 0) {
                psnStatistics.setPrjSum(psnStatistics.getPrjSum() - 1);
              } else {
                psnStatistics.setPrjSum(0);
              }
              psnStatistics.setOpenPrjSum(openPrjSum);
              // 属性为null的保存为0
              psnStatistics.setPsnId(psnId);
              PsnStatisticsUtils.buildZero(psnStatistics);
              psnStatisticsDao.save(psnStatistics);
              // 项目删除的同时也要删除项目-群组关系表的数据
              prjGroupRelationDao.delPrjGroupRelation(prjId);
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("删除项目出错,psnId =" + psnId, e);
    }

  }

  /**
   * 更新公开项目数
   */
  private Integer updatePsnStatistics(Long psnId, Long prjId, Integer openPrjSum) {
    Long cnfId = psnConfigDao.getPsnConfId(psnId);
    PsnConfigPrj psPrj = psnConfigPrjDao.getBycnfIdAndPrjId(cnfId, prjId);
    if (psPrj.getAnyUser().intValue() == PsnCnfConst.ALLOWS.intValue()) {// 如果是公开项目
      openPrjSum = openPrjSum - 1; // 公开项目数-1
    }
    return (openPrjSum.intValue() < 0 ? 0 : openPrjSum);
  }

  @Override
  public Map<String, Object> queryPrjNumAmount(ProjectQueryForm query) throws ServiceException {
    Long psnId = null;
    if (query.getOthersSee()) {
      psnId = Long.valueOf(ServiceUtil.decodeFromDes3(query.getDes3CurrentId()));
    } else {
      psnId = SecurityUtils.getCurrentUserId();
    }
    Map<String, Object> prjNumAmount = snsProjectQueryDao.findPrjNumAmount(psnId);
    return prjNumAmount;
  }

  public String html2Text(String inputString) {
    String htmlStr = inputString; // 含html标签的字符串
    String textStr = "";
    java.util.regex.Pattern p_script;
    java.util.regex.Matcher m_script;
    java.util.regex.Pattern p_style;
    java.util.regex.Matcher m_style;
    java.util.regex.Pattern p_html;
    java.util.regex.Matcher m_html;

    java.util.regex.Pattern p_html1;
    java.util.regex.Matcher m_html1;

    try {
      String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; // 定义ｓｃｒｉｐｔ的正则表达式{或<ｓｃｒｉｐｔ[^>]*?>[\\s\\S]*?<\\/ｓｃｒｉｐｔ>
      // }
      String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>
      // }
      String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
      String regEx_html1 = "<[^>]+";
      p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
      m_script = p_script.matcher(htmlStr);
      htmlStr = m_script.replaceAll(""); // 过滤script标签

      p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
      m_style = p_style.matcher(htmlStr);
      htmlStr = m_style.replaceAll(""); // 过滤style标签

      p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
      m_html = p_html.matcher(htmlStr);
      htmlStr = m_html.replaceAll(""); // 过滤html标签

      p_html1 = Pattern.compile(regEx_html1, Pattern.CASE_INSENSITIVE);
      m_html1 = p_html1.matcher(htmlStr);
      htmlStr = m_html1.replaceAll(""); // 过滤html标签

      textStr = htmlStr;

    } catch (Exception e) {
    }

    return textStr;// 返回文本字符串
  }

  @Override
  public Project getById(Long prjId) throws ServiceException {
    // TODO Auto-generated method stub
    return snsProjectQueryDao.get(prjId);
  }

}
