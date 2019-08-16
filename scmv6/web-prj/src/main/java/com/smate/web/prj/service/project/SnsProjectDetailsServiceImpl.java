package com.smate.web.prj.service.project;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.HtmlUtils;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.project.dao.PrjPubDao;
import com.smate.core.base.project.dao.PrjReportAccessoryDao;
import com.smate.core.base.project.dao.PrjReportDao;
import com.smate.core.base.project.dao.ProjectExpenAccessoryDao;
import com.smate.core.base.project.dao.ProjectExpenRecordDao;
import com.smate.core.base.project.dao.ProjectExpenditureDao;
import com.smate.core.base.project.dao.ProjectViewDao;
import com.smate.core.base.project.model.PrjComment;
import com.smate.core.base.project.model.PrjPub;
import com.smate.core.base.project.model.PrjReport;
import com.smate.core.base.project.model.PrjReportAccessory;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.model.ProjectExpenAccessory;
import com.smate.core.base.project.model.ProjectExpenRecord;
import com.smate.core.base.project.model.ProjectExpenditure;
import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.project.model.ProjectView;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.service.vip.PersonVipService;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.utils.common.MoneyFormatterUtils;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.dynamicds.InspgDynamicUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.fund.agency.dao.FundAgencyDao;
import com.smate.web.fund.model.common.ConstFundAgency;
import com.smate.web.prj.dao.pdwh.PubPdwhDAO;
import com.smate.web.prj.dao.project.PrjCommentsDao;
import com.smate.web.prj.dao.project.PrjGroupRelationDao;
import com.smate.web.prj.dao.project.ScmPrjXmlDao;
import com.smate.web.prj.dao.project.SnsProjectQueryDao;
import com.smate.web.prj.dao.sns.PrjPubAssignLogDao;
import com.smate.web.prj.dao.wechat.ProjectStatisticsDao;
import com.smate.web.prj.dto.PrjExpenditureDTO;
import com.smate.web.prj.dto.ProjectExpenAccessoryDTO;
import com.smate.web.prj.dto.ProjectExpenRecordDTO;
import com.smate.web.prj.form.PrjCommentInfo;
import com.smate.web.prj.form.PrjPubInfo;
import com.smate.web.prj.form.ProjectDetailsForm;
import com.smate.web.prj.model.common.PrjInfo;
import com.smate.web.prj.model.common.ScmPrjXml;
import com.smate.web.prj.xml.PrjXmlConstants;
import com.smate.web.prj.xml.PrjXmlDocument;

/**
 * 项目详情服务实现类
 * 
 * @author zzx
 *
 */
@Service("snsProjectDetailsService")
@Transactional(rollbackFor = Exception.class)
public class SnsProjectDetailsServiceImpl implements SnsProjectDetailsService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${domainscm}")
  private String domainscm;

  @Autowired
  private SnsProjectQueryDao snsProjectQueryDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private ProjectStatisticsDao projectStatisticsDao;
  @Autowired
  private PrjCommentsDao prjCommentsDao;
  @Autowired
  private ScmPrjXmlDao scmPrjXmlDao;
  @Autowired
  private PrjGroupRelationDao prjGroupRelationDao;
  @Autowired
  private ProjectExpenditureDao projectExpenditureDao;
  @Autowired
  private PrjReportDao prjReportDao;
  @Autowired
  private PrjReportAccessoryDao prjReportAccessoryDao;
  @Autowired
  private ArchiveFileService archiveFileService;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private ProjectExpenRecordDao projectExpenRecordDao;
  @Autowired
  private ArchiveFileDao archiveFileDao;
  @Autowired
  private ProjectExpenAccessoryDao projectExpenAccessoryDao;
  @Autowired
  private PersonVipService personVipService;
  @Autowired
  private ProjectViewDao projectViewDao;
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private PrjPubDao prjPubDao;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PrjPubAssignLogDao prjPubAssignLogDao;
  @Autowired
  private FundAgencyDao fundAgencyDao;

  @Override
  public void showPrjComment(ProjectDetailsForm form) throws Exception {
    List<PrjCommentInfo> commentList = new ArrayList<>();
    PrjCommentInfo prjCommentInfo = null;
    PsnProfileUrl psnProfileUrl = null;
    Person person = null;
    String locale = LocaleContextHolder.getLocale().toString();
    List<PrjComment> comments = snsProjectQueryDao.getPrjComments(form.getPrjId(), form);
    if (CollectionUtils.isNotEmpty(comments)) {
      for (PrjComment prjComment : comments) {
        prjCommentInfo = new PrjCommentInfo();
        // 或者人员信息
        person = personDao.get(prjComment.getPsnId());
        prjCommentInfo.setPrjId(prjComment.getPrjId()); // 项目id
        prjCommentInfo.setPsnId(prjComment.getPsnId()); // 人员id
        prjCommentInfo.setDes3ReplyerId(Des3Utils.encodeToDes3(prjComment.getPsnId().toString()));
        prjCommentInfo.setCommentsContent(prjComment.getCommentsContent()); // 内容
        if (Locale.CHINA.toString().equals(locale)) {
          prjCommentInfo.setRebuildTime(InspgDynamicUtil.formatDate(prjComment.getCreateDate()));// 迄今间隔时间
        } else {
          prjCommentInfo.setRebuildTime(InspgDynamicUtil.formatDateUS(prjComment.getCreateDate()));
        }

        prjCommentInfo.setName(buildPsnName(person)); // 人员姓名
        prjCommentInfo.setAvatars(person.getAvatars()); // 头像url
        prjCommentInfo.setPosition(person.getPosition()); // 职称
        prjCommentInfo.setInsName(person.getInsName()); // 机构信息
        prjCommentInfo.setDepartment(person.getDepartment()); // 部门信息
        psnProfileUrl = psnProfileUrlDao.find(prjComment.getPsnId());
        if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
          // 人员短地址
          prjCommentInfo.setPsnIndexUrl(domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl());
        } else {
          // 空地址
          prjCommentInfo.setPsnIndexUrl("");
        }
        commentList.add(prjCommentInfo);
      }
    }
    form.setCommentList(commentList);
  }

  @Override
  public void showPrjDetails(ProjectDetailsForm form) throws Exception {
    form.setPsnId(SecurityUtils.getCurrentUserId());
    form.setCurrentLocale(LocaleContextHolder.getLocale().toString());

    form.setResultMap(new HashMap<String, Object>());
    if (form.getPrjId() == null || form.getPrjId() == 0L) {
      form.getResultMap().put("result", "notExists");
      form.getResultMap().put("msg", "项目Id为空！");
      return;
    }
    Project project = snsProjectQueryDao.get(form.getPrjId());
    if (project == null || project.getStatus() == 1) {
      form.getResultMap().put("result", "notExists");
      form.getResultMap().put("msg", "项目不存在！");
      return;
    }
    if (form.getPsnId() != null && form.getPsnId() != 0L) {
      // 确保当前为登录状态
      form.setIsOwn(form.getPsnId().equals(project.getPsnId()));
      // 查看他人的项目，要检查权限和判断是否是好友关系
      if (!form.getIsOwn()) {
        Integer privacy = snsProjectQueryDao.findPsnPrjPrivacy(project.getId());
        if (privacy != 7) {
          form.getResultMap().put("result", "noPrivacy");
          form.getResultMap().put("msg", "没有权限查看项目！");
          return;
        }

        form.setIsFriend(personDao.isFriend(form.getPsnId(), project.getPsnId()));
      }
    } else {
      // 未登录状态下，也要判断权限是否可以访问
      Integer privacy = snsProjectQueryDao.findPsnPrjPrivacy(project.getId());
      if (privacy != 7) {
        form.getResultMap().put("result", "noPrivacy");
        form.getResultMap().put("msg", "没有权限查看项目！");
        return;
      }
    }
    // 1、项目基本信息（标题、作者、来源、资助机构、依托机构...）
    buildPrjBaseInfo(project, form);
    // 2、项目所有人信息(姓名、头像、单位、部门、职称、H指数...)
    buildPrjOwnerInfo(project, form);
    // 3、项目统计信息(赞统计数、赞状态、评论统计数、分享统计数...)
    buildPrjstatistics(project, form);
    // 4、项目表中没有存储的特殊信息（关键字，摘要...）
    buildPrjSpecialInfo(project, form);

    // 设置项目访问者的头像
    if (form.getPsnId() != null && form.getPsnId() != 0L) {
      Person person = personDao.get(form.getPsnId());
      form.setVisitAvatars(person.getAvatars());
    }
    form.setDes3PrjPsnId(Des3Utils.encodeToDes3(project.getPsnId().toString()));

    // 调用接口获取用户是否VIP
    boolean isVIP = personVipService.checkVIPByPsnId(form.getPsnId());
    form.setIsVIP(isVIP);
  }

  /**
   * 构建项目中关键字 摘要等特殊信息 作用 scm_prj_xml表
   * 
   * @param project
   * @param form
   * @throws DocumentException
   */
  private void buildPrjSpecialInfo(Project project, ProjectDetailsForm form) throws DocumentException {
    ScmPrjXml scmPrjXml = scmPrjXmlDao.get(project.getId());
    Assert.notNull(scmPrjXml, "项目xml数据为空");
    PrjXmlDocument xmlDocument = new PrjXmlDocument(scmPrjXml.getPrjXml());
    // 英文关键字
    String en_keywords = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_keywords");
    // 中文关键字
    String zh_keywords = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_keywords");
    // 中文摘要
    String zh_abstract = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_abstract");
    zh_abstract = XmlUtil.trimAllHtml(zh_abstract);
    // 英文摘要
    String en_abstract = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_abstract");
    en_abstract = XmlUtil.trimAllHtml(en_abstract);
    // 项目批准号 本机构
    String prj_internal_no = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "prj_internal_no");
    // 项目批准号 资助机构
    String prj_external_no = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "prj_external_no");

    // 项目关键词，连接符统一使用;+空格进行拼接
    en_keywords = StringUtils.replace(en_keywords, ";", "; ");
    zh_keywords = StringUtils.replace(zh_keywords, ";", "; ");

    // 设置进项目信息
    project.setZhKeywords(zh_keywords);
    project.setEnKeywords(en_keywords);
    project.setZhAbstract(zh_abstract);
    project.setEnAbstract(en_abstract);
    project.setInternalNo(prj_internal_no);
    project.setExternalNo(prj_external_no);
    if (Locale.US.toString().equals(form.getCurrentLocale())) {
      // 英文
      form.setKeywords(StringUtils.isNotBlank(en_keywords) ? en_keywords : zh_keywords);
      form.setPrjAbstract(StringUtils.isNotBlank(en_abstract) ? en_abstract : zh_abstract);
    } else {
      form.setKeywords(StringUtils.isNotBlank(zh_keywords) ? zh_keywords : en_keywords);
      form.setPrjAbstract(StringUtils.isNotBlank(zh_abstract) ? zh_abstract : en_abstract);
    }
    if (project.getAmount() != null) {
      if (project.getAmount().toString().contains(".")) {
        form.setPrjAmt(project.getAmountUnit() + " " + MoneyFormatterUtils.format(project.getAmount()));
      } else {
        form.setPrjAmt(project.getAmountUnit() + " " + MoneyFormatterUtils.formatNum(project.getAmount()));
      }

    }
    buildPrjDateShow(form, project);

    form.setPrjExternalNo(prj_external_no);
    form.setPrjInternalNo(prj_internal_no);

  }

  private void buildPrjDateShow(ProjectDetailsForm form, Project project) {
    StringBuilder date = new StringBuilder();
    if (project.getStartYear() != null) {
      date.append(project.getStartYear());
    }
    if (project.getStartMonth() != null) {
      date.append("-" + project.getStartMonth());
    }
    if (project.getStartDay() != null) {
      date.append("-" + project.getStartDay());
    }
    if (project.getEndYear() != null) {
      date.append(" ~ " + project.getEndYear());
    }
    if (project.getEndMonth() != null) {
      date.append("-" + project.getEndMonth());
    }
    if (project.getEndDay() != null) {
      date.append("-" + project.getEndDay());
    }
    form.setPrjDate(date.toString());
  }

  /**
   * 获的项目类别
   * 
   * @param cheme_agency_name 项目类别上
   * @param scheme_name 项目类别下
   * @return
   */
  @SuppressWarnings("unused")
  private String getAgencyName(String cheme_agency_name, String scheme_name) {
    StringBuilder agencyName = new StringBuilder();
    if (StringUtils.isNotEmpty(cheme_agency_name)) {
      agencyName.append(cheme_agency_name);
    }
    if (StringUtils.isNotEmpty(cheme_agency_name) && StringUtils.isNotEmpty(scheme_name)) {
      agencyName.append(" - ");
    }
    if (StringUtils.isNotEmpty(scheme_name)) {
      agencyName.append(scheme_name);
    }
    return agencyName.toString();
  }

  private void buildPrjstatistics(Project project, ProjectDetailsForm form) {
    ProjectStatistics statistics = projectStatisticsDao.get(project.getId());
    if (statistics == null) {
      statistics = new ProjectStatistics(project.getId(), 0, 0, 0, 0, 0, 0);
      projectStatisticsDao.save(statistics);
    }
    form.setAwardCount(statistics.getAwardCount());
    form.setCommentCount(statistics.getCommentCount());
    form.setShareCount(statistics.getShareCount());
    form.setReadCount(statistics.getReadCount());
    if (form.getPsnId() != null && form.getPsnId() != 0L) {
      form.setIsAward(snsProjectQueryDao.isAward(form.getPsnId(), project.getId()));
    }
  }

  private void buildPrjOwnerInfo(Project project, ProjectDetailsForm form) {
    if (project.getPsnId() != null) {
      Person person = personDao.get(project.getPsnId());
      if (person != null) {
        form.setOwnerName(buildPsnName(person));
        form.setOwnerAvatars(person.getAvatars());
        form.setOwnerInsName(person.getInsName());
        form.setOwnerDepartment(person.getDepartment());
        form.setOwnerPosition(person.getPosition());
        // ownerInsName,ownerDepartment,ownerPosition
        form.setOwnerMessage(buildOwnerMessage(form));
        PsnStatistics statistics = psnStatisticsDao.get(project.getPsnId());
        if (statistics != null) {
          form.sethIndex(statistics.getHindex());
        }
        PsnProfileUrl psnProfileUrl = psnProfileUrlDao.find(project.getPsnId());
        if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
          form.setOwnerPsnIndexUrl(domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl());
        }
      }
    }
  }

  private String buildOwnerMessage(ProjectDetailsForm form) {
    StringBuffer ownerMessage = new StringBuffer();
    if (!StringUtils.isBlank(form.getOwnerInsName())) {
      ownerMessage.append(form.getOwnerInsName());
    }
    if (!StringUtils.isBlank(form.getOwnerInsName()) && !StringUtils.isBlank(form.getOwnerDepartment())) {
      ownerMessage.append(",&nbsp;");
    }
    if (!StringUtils.isBlank(form.getOwnerDepartment())) {
      ownerMessage.append(form.getOwnerDepartment());
    }
    if (!StringUtils.isBlank(form.getOwnerPosition()) && !StringUtils.isBlank(form.getOwnerDepartment())) {
      ownerMessage.append(",&nbsp;");
    }
    if (!StringUtils.isBlank(form.getOwnerInsName()) && !StringUtils.isBlank(form.getOwnerPosition())
        && StringUtils.isBlank(form.getOwnerDepartment())) {
      ownerMessage.append(",&nbsp;");
    }
    if (!StringUtils.isBlank(form.getOwnerPosition())) {
      ownerMessage.append(form.getOwnerPosition());
    }
    return ownerMessage.toString();
  }

  private String buildPsnName(Person person) {
    String name = "";
    Locale locale = LocaleContextHolder.getLocale();
    if (Locale.US.equals(locale)) {
      name = person.getEname();
    } else {
      name = person.getName();
    }
    if (StringUtils.isBlank(name)) {
      name = person.getFirstName() + " " + person.getLastName();
    }
    return name;
  }

  private void buildPrjBaseInfo(Project project, ProjectDetailsForm form) {
    if (Locale.US.toString().equals(form.getCurrentLocale())) {
      form.setTitle(StringUtils.isNotBlank(project.getEnTitle()) ? project.getEnTitle() : project.getZhTitle());
      form.setAuthorNames(
          StringUtils.isNotBlank(project.getAuthorNamesEn()) ? project.getAuthorNamesEn() : project.getAuthorNames());
      form.setBriefDesc(
          StringUtils.isNotBlank(project.getBriefDescEn()) ? project.getBriefDescEn() : project.getBriefDesc());
      form.setAgencyName(
          StringUtils.isNotBlank(project.getEnAgencyName()) ? project.getEnAgencyName() : project.getAgencyName());
      form.setSchemeName(
          StringUtils.isNotBlank(project.getEnSchemeName()) ? project.getEnSchemeName() : project.getSchemeName());
    } else {
      form.setTitle(StringUtils.isNotBlank(project.getZhTitle()) ? project.getZhTitle() : project.getEnTitle());
      form.setAuthorNames(
          StringUtils.isNotBlank(project.getAuthorNames()) ? project.getAuthorNames() : project.getAuthorNamesEn());
      form.setBriefDesc(
          StringUtils.isNotBlank(project.getBriefDesc()) ? project.getBriefDesc() : project.getBriefDescEn());
      form.setAgencyName(
          StringUtils.isNotBlank(project.getAgencyName()) ? project.getAgencyName() : project.getEnAgencyName());
      form.setSchemeName(
          StringUtils.isNotBlank(project.getSchemeName()) ? project.getSchemeName() : project.getEnSchemeName());
    }
    form.setInsName(project.getInsName());

  }

  @Override
  public void prjAddComment(ProjectDetailsForm form) throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (form.getPrjId() == null || psnId == null || psnId == 0L) {
      throw new Exception("项目id或人员id为空");
    }
    if (StringUtils.isBlank(form.getComment())) {
      throw new Exception("评论内容为空");
    }
    Person person = personDao.get(psnId);
    if (person == null) {

      throw new Exception("评论项目人员不存在");
    }
    Long prjId = form.getPrjId();
    Long commentCount = prjCommentsDao.getCommentCount(prjId) + 1;
    // 更新评论表信息
    updatePrjComment(form, person);
    // 更新项目统计表评论数量
    updateProjectStatistics(prjId, commentCount);
    // 返回数据
    form.getResultMap().put("result", "success");
    form.getResultMap().put("commentCount", commentCount);
  }

  private void updatePrjComment(ProjectDetailsForm form, Person person) {
    PrjComment pc = new PrjComment();
    pc.setPsnId(person.getPersonId());
    pc.setPrjId(form.getPrjId());
    pc.setCommentsContent(HtmlUtils.htmlUnescape(form.getComment()));
    pc.setIsAudit(1);
    pc.setCreateDate(new Date());
    pc.setPsnAvatars(person.getAvatars());
    pc.setPsnName(person.getName());
    prjCommentsDao.save(pc);
  }

  public void updateProjectStatistics(Long prjId, Long commentCount) {
    ProjectStatistics statistics = projectStatisticsDao.get(prjId);
    if (statistics != null) {
      statistics.setCommentCount(commentCount.intValue());
      projectStatisticsDao.save(statistics);
    }
  }

  @Override
  public void checkCurrentPsnLogin(ProjectDetailsForm form, String domainscm) {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null && psnId != 0L) {
      // 说明用户登录 需要直接跳转站内
      try {
        // 不编码字符串处理
        Struts2Utils.redirect(domainscm + "/prjweb/project/detailsshow?des3PrjId="
            + URLEncoder.encode(form.getDes3PrjId().toString(), "utf-8"));
      } catch (IOException e) {
        logger.error("跳转站内项目地址错误，prjId = ", form.getPrjId(), e);
      }
    }
  }

  @Override
  public PrjInfo findGrpRelationPrjInfo(Long grpId) throws Exception {
    PrjInfo prjInfo = new PrjInfo();
    Long prjId = prjGroupRelationDao.findPrjIdByGroupId(grpId);
    if (NumberUtils.isNotNullOrZero(prjId)) {
      Project prj = snsProjectQueryDao.get(prjId);
      if (prj != null) {
        BeanUtils.copyProperties(prjInfo, prj);
        prjInfo.setPrjId(prj.getId());
      }
    }
    return prjInfo;
  }

  @Override
  public void loadDetailProjectInfo(ProjectDetailsForm form) throws Exception {
    try {
      Project project = snsProjectQueryDao.get(form.getPrjId());
      Assert.notNull(project, "prjId=" + form.getPrjId() + ",查询出的project为空");
      // 需要的数据：资助机构，资助类别，金额，起止时间，摘要，关键词
      buildPrjBaseInfo(project, form);
      buildPrjSpecialInfo(project, form);
    } catch (Exception e) {
      logger.error("加载项目基本信息出错！prjId={}", form.getPrjId(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void loadDetailProjectExpenditure(ProjectDetailsForm form) throws Exception {
    try {
      Assert.state(NumberUtils.isNotNullOrZero(form.getPrjId()), "传入的项目id为null或者0L");
      List<ProjectExpenditure> expenList = projectExpenditureDao.listByPrjId(form.getPrjId());
      // Assert.state(CollectionUtils.isNotEmpty(expenList), "根据prjId查询结果为空");
      if (CollectionUtils.isNotEmpty(expenList)) {
        List<PrjExpenditureDTO> prjExpens = new ArrayList<>();
        Float schemeTotal = 0.00f, allocatedTotal = 0.00f, usedTotal = 0.00f, advanceTotal = 0.00f,
            availableTotal = 0.00f;

        for (ProjectExpenditure prjExpen : expenList) {
          PrjExpenditureDTO expenDto = new PrjExpenditureDTO();
          expenDto.setId(prjExpen.getId());
          expenDto.setPrjId(prjExpen.getPrjId());
          expenDto.setSeqNo(prjExpen.getSeqNo());
          expenDto.setExpenItem(prjExpen.getExpenItem());
          expenDto.setSchemeAmount(buildAmount(prjExpen.getSchemeAmount()));
          expenDto.setAllocatedAmount(buildAmount(prjExpen.getAllocatedAmount()));
          expenDto.setAdvanceAmount(buildAmount(prjExpen.getAdvanceAmount()));
          expenDto.setUsedAmount(buildAmount(prjExpen.getUsedAmount()));
          Float availableAmount = (prjExpen.getSchemeAmount() - prjExpen.getAdvanceAmount() - prjExpen.getUsedAmount());
          expenDto.setAvailableAmount(buildAmount(availableAmount));
          prjExpens.add(expenDto);

          // 合计
          schemeTotal += prjExpen.getSchemeAmount();
          allocatedTotal += prjExpen.getAllocatedAmount();
          usedTotal += prjExpen.getUsedAmount();
          advanceTotal += prjExpen.getAdvanceAmount();
          availableTotal += availableAmount;
        }
        form.setPrjExpens(prjExpens);
        form.setSchemeTotal(buildAmount(schemeTotal));
        form.setAllocatedTotal(buildAmount(allocatedTotal));
        form.setUsedTotal(buildAmount(usedTotal));
        form.setAdvanceTotal(buildAmount(advanceTotal));
        form.setAvailableTotal(buildAmount(availableTotal));
      }

    } catch (Exception e) {
      logger.error("加载项目经费列表出错！prjId={}", form.getPrjId(), e);
      throw new ServiceException(e);
    }
  }

  private String buildAmount(float amount) {
    DecimalFormat secondFormat = new DecimalFormat("##0.00");
    DecimalFormat forthFormat = new DecimalFormat("##0.0000");
    amount = amount / 10000;

    String amStr = String.valueOf(amount);
    // 计算小数点后的位数
    int pointCount = amStr.length() - amStr.indexOf(".") - 1;

    if (pointCount <= 2) {
      // 小于就采用四位
      return secondFormat.format(amount);
    } else {
      return forthFormat.format(amount);
    }
  }

  @Override
  public void loadDetailProjectReport(ProjectDetailsForm form) throws Exception {
    try {
      form.setReportCount(prjReportDao.getReportSum(form.getPrjId()));
    } catch (Exception e) {
      logger.error("加载项目报告列表出错！prjId={}", form.getPrjId(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void loadDetailProjectReportList(ProjectDetailsForm form) throws Exception {
    try {
      bulidParameter(form);
      List<PrjReport> reportList = prjReportDao.getReportList(form.getPrjId(), form.getReportTypeList());
      List<Long> reportIds = new ArrayList<>();
      for (PrjReport pr : reportList) {
        reportIds.add(pr.getId());
      }
      if (reportIds != null && reportIds.size() > 0) {
        List<PrjReportAccessory> accessoryList = prjReportAccessoryDao.getByReportIds(reportIds);
        if (accessoryList != null && reportList != null) {
          for (PrjReport report : reportList) {
            for (PrjReportAccessory accessory : accessoryList) {
              if (accessory.getReportId().longValue() == report.getId().longValue()) {
                report.setFileId(accessory.getFileId());
                report.setDownloadUrl(
                    fileDownUrlService.getDownloadUrl(FileTypeEnum.ARCHIVE_FILE, accessory.getFileId(), 0L));
              }
            }
          }
        }
        // 获取 资助机构的ID
        Project project = snsProjectQueryDao.get(form.getPrjId());
        String agencyName = "";
        if (project != null) {
          agencyName =
              StringUtils.isNotBlank(project.getEnAgencyName()) ? project.getEnAgencyName() : project.getAgencyName();
        }
        if (StringUtils.isNotBlank(agencyName)) {
          ConstFundAgency constFundAgency = fundAgencyDao.getFundAgencyByName(agencyName);
          if (constFundAgency != null) {
            form.setDes3AgencyId(Des3Utils.encodeToDes3(constFundAgency.getId().toString()));
          }
        }
      }
      form.setPrjRepostList(reportList);
    } catch (Exception e) {
      logger.error("加载项目报告列表出错！prjId={}", form.getPrjId(), e);
      throw new ServiceException(e);
    }
  }

  // 解析前台传来的值并保存
  public void bulidParameter(ProjectDetailsForm form) {
    if (StringUtils.isNotBlank(form.getReportType())) {
      String reportType[] = form.getReportType().split(",");
      List<Integer> reportTypeList = new ArrayList<Integer>();
      for (String rt : reportType) {
        reportTypeList.add(Integer.parseInt(rt));
      }
      form.setReportTypeList(reportTypeList);
    }
  }

  @Override
  public Map<String, Object> loadDetailProjectReportCount(ProjectDetailsForm form) throws Exception {
    // 设置默认参数
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, String> reportTypeMap = new HashMap<String, String>();
    List<Map<String, Object>> reportTypecount = prjReportDao.queryReportCountList(form.getPrjId());
    if (reportTypecount != null && reportTypecount.size() > 0) {
      for (Map<String, Object> m : reportTypecount) {
        reportTypeMap.put(m.get("reportType").toString(), m.get("count").toString());
      }
    }
    map.put("reportType", reportTypeMap);
    return map;
  }

  @Override
  public Map<String, String> uploadReportFile(Long reportId, Long fileId, Long psnId) throws Exception {
    Map<String, String> map = new HashMap<String, String>();
    try {
      PrjReport prjReport = prjReportDao.get(reportId);
      if (prjReport == null) {
        map.put("downUrl", "");
        return map;
      }
      ArchiveFile archiveFile = archiveFileService.getArchiveFileById(fileId);
      prjReportAccessoryDao.deleteByReportId(reportId);
      PrjReportAccessory reportAccessory = new PrjReportAccessory();
      reportAccessory.setFileId(fileId);
      reportAccessory.setReportId(reportId);
      reportAccessory.setFileName(archiveFile.getFileName());
      reportAccessory.setGmtCreate(new Date());
      reportAccessory.setGmtModified(new Date());
      prjReportAccessoryDao.save(reportAccessory);
      String link = fileDownUrlService.getDownloadUrl(FileTypeEnum.ARCHIVE_FILE, fileId, 0L);
      map.put("downUrl", link);
    } catch (Exception e) {
      logger.error("项目报告列表上传附件出错！reportId={}", reportId, e);
    }
    return map;
  }

  @Override
  public void loadDetailProjectPubinfo(ProjectDetailsForm form) throws Exception {
    try {
      List<Long> pubList = prjPubDao.getPrjPubIdsByPrjId(form.getPrjId(), 0);
      if (pubList != null && pubList.size() > 0) {
        form.setPubCount(pubPdwhDAO.getPubCount(pubList));
      } else {
        form.setPubCount(0L);
      }
    } catch (Exception e) {
      logger.error("加载项目成果列表出错！prjId={}", form.getPrjId(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void loadProjectExpenditureAdd(ProjectDetailsForm form) throws Exception {
    try {
      Assert.state(NumberUtils.isNotNullOrZero(form.getPrjId()), "传入的项目id为null或者0L");
      Project project = snsProjectQueryDao.get(form.getPrjId());
      Assert.notNull(project, "prjId=" + form.getPrjId() + ",查询出的project为空");
      // 构建项目名称
      if (Locale.US.toString().equals(form.getCurrentLocale())) {
        form.setTitle(StringUtils.isNotBlank(project.getEnTitle()) ? project.getEnTitle() : project.getZhTitle());
      } else {
        form.setTitle(StringUtils.isNotBlank(project.getZhTitle()) ? project.getZhTitle() : project.getEnTitle());
      }
      // 构建项目的经费科目
      List<ProjectExpenditure> expenList = projectExpenditureDao.listByPrjId(form.getPrjId());
      if (CollectionUtils.isNotEmpty(expenList)) {
        List<PrjExpenditureDTO> prjExpens = new ArrayList<>();
        for (ProjectExpenditure prjExpen : expenList) {
          PrjExpenditureDTO prjExpenDto = new PrjExpenditureDTO();
          prjExpenDto.setId(prjExpen.getId());
          prjExpenDto.setPrjId(prjExpen.getPrjId());
          prjExpenDto.setExpenItem(prjExpen.getExpenItem());
          prjExpens.add(prjExpenDto);
        }
        form.setPrjExpens(prjExpens);
      }
      // 如果有传expenRecordId过来，说明是编辑，需要返回指定的数据
      if (NumberUtils.isNotNullOrZero(form.getExpenRecordId())) {
        buildExpenRecord(form);
      }
    } catch (Exception e) {
      logger.error("加载项目经费记一笔窗口出错！prjId={}", form.getPrjId(), e);
      throw new ServiceException(e);
    }
  }

  private void buildExpenRecord(ProjectDetailsForm form) throws Exception {
    ProjectExpenRecord projectExpenRecord = projectExpenRecordDao.get(form.getExpenRecordId());
    // 如果是已删除的支出记录，则提示记录已被删除
    if (projectExpenRecord == null || projectExpenRecord.getStatus() == 1) {
      return;
    }
    form.setExpenId(projectExpenRecord.getExpenId());
    form.setExpenAmount(projectExpenRecord.getExpenAmount());
    form.setExpenRecordId(projectExpenRecord.getId());
    form.setExpenDate(projectExpenRecord.getGmtExpen());
    form.setRemark(projectExpenRecord.getRemark());
    // 加载附件记录
    loadExpenAccessory(form);
  }

  @Override
  public void saveProjectExpenditure(ProjectDetailsForm form) throws Exception {
    try {
      // 保存经费支出记录,保存经费附件记录
      Long expenRecordId = saveExpenRecord(form);
      // 保存附件记录
      saveExpenAccessory(form, expenRecordId);
      // 重新计算经费表中的已用和预支金额
      updateExpenditure(form);
    } catch (Exception e) {
      logger.error("保存一笔经费记录出错！", e);
      throw new ServiceException(e);
    }

  }

  /**
   * 更新经费表记录
   * 
   * @param form
   * @param isAdd 标识是记一笔，还是删除一笔
   * @param modifiedDate 记一笔传的是new Date() 删除一笔传的是表中gmtModified
   */
  private void updateExpenditure(ProjectDetailsForm form) {
    Assert.isTrue(NumberUtils.isNotNullOrZero(form.getExpenId()), "经费表主键为空或者0L");
    try {
      ProjectExpenditure prjExpen = projectExpenditureDao.get(form.getExpenId());
      Assert.notNull(prjExpen, "根据id查询项目经费表记录失败！");
      // 统计已用金额
      Float usedAmount = projectExpenRecordDao.countUsedAmount(form.getExpenId());
      prjExpen.setUsedAmount(usedAmount);
      // 统计预支金额
      Float advanceAmount = projectExpenRecordDao.countAdvanceAmount(form.getExpenId());
      prjExpen.setAdvanceAmount(advanceAmount);
      projectExpenditureDao.save(prjExpen);
    } catch (Exception e) {
      logger.error("更新预支和已用金额出错，时间日期转化异常！", e);
    }
  }

  private Long saveExpenRecord(ProjectDetailsForm form) {
    /**
     * 这里因为支出金额的修改存在多种情况，但是修改值之后又需要重新计算经费情况，所以需要手动提交事务 <br/>
     * 列表界面也可以进行重新计算，但是连接数据库次数过多，得不偿失
     */
    Assert.isTrue(NumberUtils.isNotNullOrZero(form.getExpenId()), "经费表主键为空或者0L");
    try {
      projectExpenRecordDao.getSession().getTransaction().begin();
      ProjectExpenRecord prjExpenRecord = null;
      if (NumberUtils.isNotNullOrZero(form.getExpenRecordId())) {
        prjExpenRecord = projectExpenRecordDao.get(form.getExpenRecordId());
        prjExpenRecord.setGmtExpen(form.getExpenDate());
        prjExpenRecord.setRemark(StringUtils.substring(form.getRemark(), 0, 100));
        // 表中记录还是不变
        prjExpenRecord.setExpenAmount(form.getExpenAmount());

      } else {
        prjExpenRecord =
            new ProjectExpenRecord(form.getExpenId(), form.getExpenDate(), form.getRemark(), form.getExpenAmount(), 0);
      }
      projectExpenRecordDao.saveOrUpdate(prjExpenRecord);
      projectExpenRecordDao.getSession().getTransaction().commit();
      return prjExpenRecord.getId();
    } catch (Exception e) {
      projectExpenRecordDao.getSession().getTransaction().rollback();
      logger.error("保存支出记录出错！", e);
    }
    return null;
  }

  private void saveExpenAccessory(ProjectDetailsForm form, Long expenRecordId) {
    // 先删除
    projectExpenAccessoryDao.deleteAccessory(expenRecordId);
    if (StringUtils.isBlank(form.getDes3fileids()))
      return;
    String[] des3FileIds = form.getDes3fileids().split(";");
    for (int i = 0; i < des3FileIds.length; i++) {
      if (StringUtils.isBlank(des3FileIds[i]))
        continue;
      Long fileId = Long.valueOf(Des3Utils.decodeFromDes3(des3FileIds[i]));
      if (NumberUtils.isNullOrZero(fileId))
        continue;
      // 正确的fileId，则进行保存
      String fileName = archiveFileDao.getArchiveFileName(fileId);
      ProjectExpenAccessory prjExpenFile =
          new ProjectExpenAccessory(expenRecordId, fileId, StringUtils.substring(fileName, 0, 100));
      projectExpenAccessoryDao.save(prjExpenFile);
    }
  }

  @Override
  public void loadExpenAccessory(ProjectDetailsForm form) throws Exception {
    try {
      Assert.isTrue(NumberUtils.isNotNullOrZero(form.getExpenId()), "经费表主键为空或者0L");
      List<ProjectExpenAccessory> fileList = projectExpenAccessoryDao.listByExpenRecordId(form.getExpenRecordId());
      if (CollectionUtils.isNotEmpty(fileList)) {
        form.setAccessorys(new ArrayList<>());
        for (ProjectExpenAccessory accessory : fileList) {
          ProjectExpenAccessoryDTO dto = new ProjectExpenAccessoryDTO();
          dto.setId(accessory.getId());
          dto.setExpenRecordId(accessory.getExpenRecordId());
          dto.setDes3FileId(Des3Utils.encodeToDes3(accessory.getFileId() + ""));
          dto.setDownloadUrl(fileDownUrlService.getDownloadUrl(FileTypeEnum.ARCHIVE_FILE, accessory.getFileId(), 0L));
          dto.setFileName(accessory.getFileName());
          form.getAccessorys().add(dto);
        }
      }
    } catch (Exception e) {
      logger.error("加载项目经费附件出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void loadDetailPrjPubList(ProjectDetailsForm form) throws Exception {
    // 查询成果
    List<PrjPubInfo> list = new ArrayList<>();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPrjId(form.getPrjId());
    pubQueryDTO.setOrderBy("publishYear");
    pubQueryDTO.setPubType(form.getPubType());
    pubQueryDTO.setSearchKey(form.getSearchKey());
    pubQueryDTO.setSearchPsnId(form.getPsnId());
    pubQueryDTO.setPageNo(form.getPage().getPageNo());
    pubQueryDTO.setServiceType("prjDetailPubList");
    String SERVER_URL = this.domainscm + "/data/pub/query/list";
    parsePubResult(form, list, pubQueryDTO, SERVER_URL);
    form.setPrjPubList(list);
  }

  private void parsePubResult(ProjectDetailsForm form, List<PrjPubInfo> list, PubQueryDTO pubQueryDTO,
      String SERVER_URL) throws IllegalAccessException, InvocationTargetException {
    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();
    MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
    requestHeaders.setContentType(type);
    HttpEntity<String> requestEntity =
        new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(pubQueryDTO), requestHeaders);
    Map<String, Object> object =
        (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    if (object != null && object.get("status").equals("success")) {
      form.getPage().setTotalCount(NumberUtils.toInt(object.get("totalCount").toString()));
      List<Map<String, Object>> listResult = (List<Map<String, Object>>) object.get("resultList");
      if (listResult != null && listResult.size() > 0) {
        for (Map<String, Object> map : listResult) {
          PrjPubInfo vo = new PrjPubInfo();
          BeanUtils.populate(vo, map);
          list.add(vo);
        }
      }
    } else {
      form.getPage().setTotalCount(0);
    }
  }

  @Override
  public Map<String, Object> loadDetailPrjPubCount(ProjectDetailsForm form) throws Exception {
    // 设置默认参数
    Map<String, Object> map = new HashMap<String, Object>();
    Map<String, String> pubTypeMap = new HashMap<String, String>();
    List<Long> pubIds = prjPubDao.getPrjPubIdsByPrjId(form.getPrjId(), 0);// 基准库成果
    if (pubIds != null && pubIds.size() > 0) {
      List<Map<String, Object>> pubTypecount = pubPdwhDAO.queryPubCountList(pubIds);
      if (pubTypecount != null && pubTypecount.size() > 0) {
        for (Map<String, Object> m : pubTypecount) {
          pubTypeMap.put(m.get("pubType").toString(), m.get("count").toString());
        }
      }
      map.put("pubType", pubTypeMap);
    }
    return map;
  }

  @Override
  public void saveProjectView(ProjectDetailsForm form) throws Exception {
    try {
      Assert.isTrue(NumberUtils.isNotNullOrZero(form.getPrjId()), "prjId为空或者0L");
      Project project = snsProjectQueryDao.get(form.getPrjId());
      Assert.notNull(project, "项目表记录为空，prjId=" + form.getPrjId());
      if (project.getPsnId().equals(form.getPsnId())) {
        // 如果是拥有人浏览不需要记录，不等于就记录，站外也记录
        return;
      }
      // 记录阅读记录
      long formateDate = DateUtils.getDateTime(new Date());
      String ip = Struts2Utils.getRemoteAddr();

      ProjectView prjView = projectViewDao.findPrjView(form.getPsnId(), form.getPrjId(), formateDate, ip);
      if (prjView == null) {
        prjView = new ProjectView();
        prjView.setPrjId(form.getPrjId());
        prjView.setViewPsnId(form.getPsnId());
        prjView.setIp(ip);
        prjView.setGmtCreate(new Date());
        prjView.setFormateDate(formateDate);
        prjView.setTotalCount(1l);
      } else {
        prjView.setGmtCreate(new Date());
        long viewCount = prjView.getTotalCount() == null ? 0 : prjView.getTotalCount();
        prjView.setTotalCount(viewCount + 1);
      }
      projectViewDao.save(prjView);

      // 计算阅读统计数
      ProjectStatistics statistics = projectStatisticsDao.get(form.getPrjId());
      // 显示详情时会判断记录数是否有，没有会新增记录，因而这里直接抛异常
      Assert.notNull(statistics, "项目统计表为空，prjId=" + form.getPrjId());
      Integer readCount = statistics.getReadCount() == null ? 0 : statistics.getReadCount();
      statistics.setReadCount(readCount + 1);
      projectStatisticsDao.saveOrUpdate(statistics);

    } catch (Exception e) {
      logger.error("记录项目阅读记录出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void loadExpenRecord(ProjectDetailsForm form) throws Exception {
    try {
      Assert.isTrue(NumberUtils.isNotNullOrZero(form.getPsnId()), "psnId为空或者0L");
      if (NumberUtils.isNotNullOrZero(form.getExpenId())) {
        List<ProjectExpenRecord> expenRecordList = projectExpenRecordDao.findByExpenId(form.getExpenId());
        if (CollectionUtils.isEmpty(expenRecordList)) {
          form.setExpenRecords(new ArrayList<>());
          return;
        }
        List<ProjectExpenRecordDTO> eRecords = new ArrayList<>();
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
        DecimalFormat decimalFormat = new DecimalFormat("##0.00");
        for (ProjectExpenRecord expenRecord : expenRecordList) {
          ProjectExpenRecordDTO recordDto = new ProjectExpenRecordDTO();
          recordDto.setId(expenRecord.getId());
          recordDto.setExpenId(expenRecord.getExpenId());
          recordDto.setRemark(expenRecord.getRemark());
          recordDto.setGmtExpen(expenRecord.getGmtExpen());
          recordDto.setFormatDate(dataFormat.format(expenRecord.getGmtExpen()));
          recordDto.setFormatAmount(decimalFormat.format(expenRecord.getExpenAmount()));
          eRecords.add(recordDto);
        }
        form.setExpenRecords(eRecords);
      }
    } catch (Exception e) {
      logger.error("根据项目经费id加载支出记录出错！expenId={}", form.getExpenId(), e);
      throw new ServiceException(e);
    }

  }

  @Override
  public void deleteExpenRecord(ProjectDetailsForm form) throws Exception {
    try {
      Assert.isTrue(NumberUtils.isNotNullOrZero(form.getPsnId()), "psnId为空或者0L");
      Assert.isTrue(NumberUtils.isNotNullOrZero(form.getExpenRecordId()), "expenRecordId为空或者0L，删除失败");
      ProjectExpenRecord projectExpenRecord = projectExpenRecordDao.get(form.getExpenRecordId());
      if (projectExpenRecord == null || projectExpenRecord.getStatus() == 1) {
        form.getResultMap().put("msg", "no_record");
        return;
      }
      /**
       * 这里因为支出金额的修改存在多种情况，但是修改值之后又需要重新计算经费情况，所以需要手动提交事务 <br/>
       * 列表界面也可以进行重新计算，但是连接数据库次数过多，得不偿失
       */
      // 置为删除状态
      projectExpenRecord.setStatus(1);
      projectExpenRecordDao.getSession().getTransaction().begin();
      projectExpenRecordDao.saveOrUpdate(projectExpenRecord);
      projectExpenRecordDao.getSession().getTransaction().commit();

      // 删除与只对应的附件
      projectExpenAccessoryDao.deleteAccessory(form.getExpenRecordId());

      // 重新计算经费表中的已用和预支金额
      form.setExpenId(projectExpenRecord.getExpenId());
      updateExpenditure(form);

      form.getResultMap().put("msg", "success");
    } catch (Exception e) {
      projectExpenRecordDao.getSession().getTransaction().rollback();
      logger.error("根据项目支出记录id删除记录出错！expenRecordId={}", form.getExpenRecordId(), e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void loadDetailPrjPubConfirm(ProjectDetailsForm form) throws Exception {
    // 查询成果
    List<PrjPubInfo> list = new ArrayList<>();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPrjId(form.getPrjId());
    pubQueryDTO.setSearchPsnId(form.getPsnId());
    pubQueryDTO.setIsAll(form.getIsAll());
    pubQueryDTO.setServiceType("prjPubConfirmList");
    String SERVER_URL = this.domainscm + "/data/pub/query/list";
    parsePubResult(form, list, pubQueryDTO, SERVER_URL);
    form.setPrjPubList(list);
  }

  @Override
  public Map<String, Object> ajaxPrjPubConfirmOpt(ProjectDetailsForm form) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    try {
      // 认领结果 0：未认领，1：已认领，2：拒绝
      if (form.getConfirmResult() == 1) {
        PrjPub prjPub = prjPubDao.getPrjPubByPrjIdAndPubId(form.getPrjId(), form.getPubId());
        if (prjPub == null) {
          prjPub = new PrjPub();
          prjPub.setPrjId(form.getPrjId());
          prjPub.setPubId(form.getPubId());
          prjPub.setPubFrom(0);
          prjPub.setStatus(0);
          prjPub.setGmtCreate(new Date());
          prjPubDao.saveOrUpdate(prjPub);
        }
      }
      prjPubAssignLogDao.updateConfirmResult(form.getPrjId(), form.getPubId(), form.getConfirmResult());
      map.put("result", "success");
    } catch (Exception e) {
      map.put("result", "error");
      logger.error("项目成果匹配操作出错！", e);
      throw new ServiceException(e);
    }
    return map;
  }
}
