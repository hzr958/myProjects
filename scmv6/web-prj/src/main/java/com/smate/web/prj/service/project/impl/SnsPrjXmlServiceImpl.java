package com.smate.web.prj.service.project.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.smate.core.base.consts.model.ConstDiscipline;
import com.smate.core.base.consts.service.InstitutionService;
import com.smate.core.base.exception.NoPermissionException;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.file.dao.ArchiveFileDao;
import com.smate.core.base.file.enums.FileTypeEnum;
import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.service.FileDownloadUrlService;
import com.smate.core.base.project.dao.PrjMemberDao;
import com.smate.core.base.project.dao.PrjReportDao;
import com.smate.core.base.project.dao.ProjectExpenditureDao;
import com.smate.core.base.project.model.PrjMember;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.model.ProjectExpenditure;
import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.project.service.PrjMemberService;
import com.smate.core.base.project.service.ProjectService;
import com.smate.core.base.psn.dao.PsnFileDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnFile;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.model.StationFile;
import com.smate.core.base.psn.model.psncnf.PsnConfigPrj;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.statistics.PsnStatisticsUtils;
import com.smate.core.base.utils.common.MoneyFormatterUtils;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.ConstDictionaryManage;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.fund.dao.wechat.ConstDisplineDao;
import com.smate.web.fund.recommend.dao.CategoryMapBaseDao;
import com.smate.web.fund.recommend.model.CategoryMapBase;
import com.smate.web.prj.consts.PrjXmlOperationEnum;
import com.smate.web.prj.consts.PsnCnfConst;
import com.smate.web.prj.dao.project.CategoryMapScmNsfcDao;
import com.smate.web.prj.dao.project.PrjFulltextDao;
import com.smate.web.prj.dao.project.ScmPrjXmlDao;
import com.smate.web.prj.dao.sns.PersonOpenDao;
import com.smate.web.prj.dao.wechat.ProjectStatisticsDao;
import com.smate.web.prj.dto.PrjXmlDTO;
import com.smate.web.prj.exception.ProjectNotExistException;
import com.smate.web.prj.form.PrjXmlProcessContext;
import com.smate.web.prj.form.ProjectOptForm;
import com.smate.web.prj.form.fileimport.PrjInfoDTO;
import com.smate.web.prj.model.common.CategoryMapScmNsfc;
import com.smate.web.prj.model.common.PrjFulltext;
import com.smate.web.prj.model.common.ScmPrjXml;
import com.smate.web.prj.service.project.SnsPrjXmlService;
import com.smate.web.prj.util.PrjCommonConstFieldRefresh;
import com.smate.web.prj.util.PrjUtils;
import com.smate.web.prj.xml.PrjXmlConstants;
import com.smate.web.prj.xml.PrjXmlDocument;
import com.smate.web.prj.xml.PrjXmlDocumentBuilder;
import com.smate.web.prj.xml.PrjXmlProcessor;

/**
 * 项目XML相关服务类
 * 
 * @author houchuanjie
 * @date 2018年3月15日 下午5:13:36
 */
@Service
@Transactional(rollbackOn = Exception.class)
public class SnsPrjXmlServiceImpl implements SnsPrjXmlService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ScmPrjXmlDao scmPrjXmlDao;
  @Autowired
  private ConstDictionaryManage constDictionaryManage;
  @Autowired
  private PsnCnfService psnCnfService;
  @Autowired
  private InstitutionService institutionService;
  @Autowired
  private ProjectService projectService;
  @Autowired
  private PrjMemberService prjMemberService;
  @Autowired
  private ProjectStatisticsDao projectStatisticsDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private PrjMemberDao prjMemberDao;
  @Autowired
  private PrjFulltextDao prjFulltextDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private PsnFileDao psnFileDao;
  @Autowired
  private ArchiveFileDao archiveFileDao;
  @Autowired
  private FileDownloadUrlService fileDownUrlService;
  @Autowired
  private ConstDisplineDao displineDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;
  @Autowired
  private CategoryMapScmNsfcDao categoryMapScmNsfcDao;
  @Autowired
  private PrjReportDao prjReportDao;
  @Autowired
  private ProjectExpenditureDao projectExpenditureDao;
  @Autowired
  private PersonOpenDao personOpenDao;

  @Override
  public ProjectOptForm loadPrjXml(ProjectOptForm prjOptForm)
      throws ServiceException, ProjectNotExistException, NoPermissionException {
    Assert.notNull(prjOptForm);
    Long prjId = prjOptForm.getPrjId();
    // 获取缓存数据
    Optional<PrjXmlDTO> optPrjXmlDto = getPrjXmlByPrjId(prjId);
    String xmlData = optPrjXmlDto.map(px -> px.getXmlData()).filter(s -> StringUtils.isNotBlank(s))
        .orElseThrow(ProjectNotExistException::new);
    try {
      PrjXmlDocument prjXmlDocument = new PrjXmlDocument(xmlData);
      // 操作权限判断，判断是否是项目拥有者，否则没有权限进行编辑
      if (!SecurityUtils.getCurrentUserId().equals(prjXmlDocument.getOwerPsnId())) {
        throw new NoPermissionException("您没有权限进行项目编辑操作！");
      }
      List nodes = prjXmlDocument.getNodes(PrjXmlConstants.PRJ_MEMBERS_MEMBER_XPATH);
      if (!CollectionUtils.isEmpty(nodes)) {
        nodes.forEach(ele -> {
          Element element = (Element) ele;
          String des3psnId = element.attributeValue("des3_member_psn_id");
          String seq_no = element.attributeValue("seq_no");
          if (StringUtils.isBlank(des3psnId) && StringUtils.isNotBlank(seq_no)) {
            PrjMember member = prjMemberDao.findOneByPrjId(prjId, Integer.parseInt(seq_no));
            if (member != null && member.getPsnId() != null) {
              element.addAttribute("des3_member_psn_id", Des3Utils.encodeToDes3(member.getPsnId().toString()));
              element.addAttribute("member_psn_id", member.getPsnId().toString());
            }
          }
        });
      }
      // 刷新常数字段
      PrjCommonConstFieldRefresh.refresh(prjXmlDocument, constDictionaryManage);
      // 格式化中英标点符号
      String authorNames = prjXmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names");
      authorNames = XmlUtil.formateSymbolAuthors(authorNames, authorNames);
      prjXmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "author_names", authorNames);
      // 格式化资金
      if (prjXmlDocument.existsNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount")) {
        String amount = prjXmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount");
        amount = MoneyFormatterUtils.format(amount);
        prjXmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount_view", amount);
      }
      dealDiscipline(prjXmlDocument);
      checkContainsHtml(prjXmlDocument, prjOptForm);
      prjOptForm.setPrjXml(prjXmlDocument.getXmlString());
      prjOptForm.setOwnerPsnId(prjXmlDocument.getOwerPsnId());
      // 采用新的附件下载，构建下载url
      String fileId = prjXmlDocument.getXmlNodeAttribute(PrjXmlConstants.PRJ_FULLTEXT_XPATH, "file_id");
      String downloadUrl = "";
      if (StringUtils.isNotBlank(fileId)) {
        if (NumberUtils.isCreatable(fileId)) {
          Long fileIdNum = Long.parseLong(fileId);
          downloadUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.ARCHIVE_FILE, fileIdNum, 0L);
        }
      }
      // 附件下载
      List attachs = prjXmlDocument.getNodes(PrjXmlConstants.PRJ_ATTACHMENTS_ATTACHMENT_XPATH);
      if (!CollectionUtils.isEmpty(attachs)) {
        for (int i = 0; i < attachs.size(); i++) {
          Element attach = (Element) attachs.get(i);
          String file_id = attach.attributeValue("file_id");
          Long attachId = 0L;
          if (StringUtils.isNotBlank(file_id) && !NumberUtils.isDigits(file_id)) {
            attachId = NumberUtils.toLong(Des3Utils.decodeFromDes3(file_id));
          } else {
            attachId = NumberUtils.toLong(file_id);
          }
          downloadUrl = fileDownUrlService.getDownloadUrl(FileTypeEnum.ARCHIVE_FILE, attachId, 0L);
          prjOptForm.getPrjAttachDownload().put(file_id, downloadUrl);
        }
      }

      prjOptForm.setDownloadUrl(downloadUrl);
      // 刷新权限
      Optional.ofNullable(prjXmlDocument.getOwerPsnId()).filter(ownerPsnId -> ownerPsnId > 0).ifPresent(ownerPsnId -> {
        PsnConfigPrj cnfPrj = new PsnConfigPrj();
        cnfPrj.getId().setPrjId(prjId);
        PsnConfigPrj cnfPrjExists = psnCnfService.get(ownerPsnId, cnfPrj);
        if (Objects.isNull(cnfPrjExists)) {
          prjOptForm.setAuthority(PsnCnfConst.ALLOWS);
        } else {
          prjOptForm.setAuthority(cnfPrjExists.getAnyUser());
        }
      });
    } catch (NoPermissionException e) {
      throw e;
    } catch (Exception e) {
      logger.error("loadXml加载XML,转换为XmlDocument错误, prjId=" + prjOptForm.getPrjId(), e);
      throw new ServiceException(e);
    }
    return prjOptForm;
  }

  private void dealDiscipline(PrjXmlDocument xmlDocument) {
    String id = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "discipline");
    String areaId = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "areaId");
    if (StringUtils.isBlank(id) || !NumberUtils.isDigits(id) || StringUtils.isNotBlank(areaId)) {
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
          xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "areaId",
              String.valueOf(secondCategory.getCategryId()));
        }
      }
    }
  }

  @Override
  public void savePrjXml(Long prjId, String xml) {
    ScmPrjXml prjXml = Optional.ofNullable(scmPrjXmlDao.get(prjId)).orElse(new ScmPrjXml(prjId));
    prjXml.setPrjXml(xml);
    this.scmPrjXmlDao.save(prjXml);
  }

  @Override
  public Optional<PrjXmlDTO> getPrjXmlByPrjId(Long prjId) throws ServiceException {
    try {
      return Optional.ofNullable(scmPrjXmlDao.get(prjId)).flatMap(scmPrjXml -> Optional.of(new PrjXmlDTO(scmPrjXml)));
    } catch (Exception e) {
      logger.error("获取项目XML出错！prjId={}", prjId, e);
      throw new ServiceException("获取项目XML！prjId=" + prjId, e);
    }
  }

  /**
   * 检查是否包含html标记.
   *
   * @author houchuanjie
   * @date 2018年3月22日 下午3:28:59
   * @param prjXmlDoc 不能为{@code null}
   * @param form 不能为{@code null}
   */
  private void checkContainsHtml(PrjXmlDocument prjXmlDoc, ProjectOptForm form) {
    String zhAbstract = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_abstract");
    String enAbstract = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_abstract");
    String regExHtml = "<[^>]+>";
    java.util.regex.Pattern pattern = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
    java.util.regex.Matcher zhAbstractMatcher = pattern.matcher(zhAbstract);
    java.util.regex.Matcher enAbstractMatcher = pattern.matcher(enAbstract);
    form.setIsHtmlZhAbstract(zhAbstractMatcher.find());
    form.setIsHtmlEnAbstract(enAbstractMatcher.find());
  }

  @Override
  public PrjXmlDocument createPrjXml(Map<String, Object> paramsMap, String prjId) {
    try {
      String userId = Objects.toString(SecurityUtils.getCurrentUserId());
      PrjXmlDocument prjXmlDoc = PrjXmlDocumentBuilder.build(paramsMap);
      // 设置record_from 参数
      String recordFrom = String.valueOf(paramsMap.get("recordFrom"));
      if (StringUtils.isEmpty(recordFrom)) {
        // 默认手工导入
        recordFrom = "0";
      }
      prjXmlDoc.setXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "record_from", recordFrom);
      Element prjMeta = Optional.ofNullable((Element) prjXmlDoc.getPrjMeta())
          .orElse(prjXmlDoc.createElement(PrjXmlConstants.PRJ_META_XPATH));
      // 设置编辑的人员id和编辑时间
      String date = ServiceUtil.formateZhDateFull(new Date());
      prjMeta.addAttribute("last_update_psn_id", userId);
      prjMeta.addAttribute("last_update_date", date);
      prjMeta.addAttribute("record_psn_id", userId);
      prjMeta.addAttribute("create_psn_id", userId);
      prjMeta.addAttribute("create_date", date);
      prjMeta.addAttribute("prj_id", prjId);
      return prjXmlDoc;
    } catch (Exception e) {
      logger.error("创建项目XML错误！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public PrjXmlDocument syncUpdatePrjXml(Project project, Map<String, Object> paramsMap)
      throws ProjectNotExistException, ServiceException, NoPermissionException {
    Assert.notNull(project, "project对象不能为空");
    // 从数据库获取旧的xml数据
    PrjXmlDTO prjXmlDTO = getPrjXmlByPrjId(project.getId()).orElseThrow(ProjectNotExistException::new);
    try {
      PrjXmlDocument oldXmlDoc = new PrjXmlDocument(prjXmlDTO.getXmlData());
      // 操作权限判断，判断是否是项目拥有者，否则没有权限进行编辑
      if (!SecurityUtils.getCurrentUserId().equals(oldXmlDoc.getOwerPsnId())) {
        throw new NoPermissionException("您没有权限进行项目编辑操作！");
      }
      PrjXmlProcessor prjXmlProcessor = PrjXmlProcessor
          // 从旧xml数据复制，并用新的参数数据替换原有值，得到prjXml处理器的实例
          .copyFromOldPrjXml(oldXmlDoc, paramsMap)
          // 更新元信息
          .updatePrjMeta(null)
          // 更新项目日期信息
          .updateDateNode()
          // 更新项目组成员信息
          .updatePrjMember(institutionService)
          // 处理标题、关键词、摘要等信息
          .updateTitileAbstractAndKeywords()
          // 全角转半角
          .SBCase2DBCase()
          // 格式化资金金额
          .formatMoney()
          // 更新依托单位
          .updateIns(institutionService)
          // 字段校验
          .validate()
          // 生成简要描述
          .generateBrief()
          // 生成项目标题的哈希值
          .generateHashCode();

      String xmlString = prjXmlProcessor
          // 同步项目字段信息
          .syncProjectData(project)
          // 同步项目成员
          .syncPrjMember(prjMemberService, personOpenDao, project)
          // 作者太多时缩略
          .abbreviateAuthorNames(project)
          // 获取xml数据
          .getPrjXmlDocument().getXmlString();
      // 保存项目xml
      savePrjXml(project.getId(), xmlString);
      return prjXmlProcessor.getPrjXmlDocument();
    } catch (DocumentException e) {
      logger.error("更新项目XML错误！prjId={}", project.getId(), e);
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public Long addPrjXml(Long prjId, Map paramsMap)
      throws ProjectNotExistException, ServiceException, NoPermissionException {
    Long newPrjId = projectService.createPrjId();
    Long psnId = SecurityUtils.getCurrentUserId();
    PrjXmlDocument xmlDoc = createPrjXml(paramsMap, newPrjId.toString());
    // 从旧xml数据复制，并用新的参数数据替换原有值，得到prjXml处理器的实例
    PrjXmlProcessor prjXmlProcessor = PrjXmlProcessor.copyFromOldPrjXml(xmlDoc, paramsMap);
    PrjXmlProcessContext context = this.buildXmlProcessContext(psnId);
    context.setCurrentPrjId(newPrjId);
    this.dealWithXmlByProcessor(context, xmlDoc, prjXmlProcessor);
    return newPrjId;
  }

  private void buildAuthorNameInfo(Project project, PrjXmlDocument prjXmlDoc) {
    Element prjMembers = Optional.ofNullable((Element) prjXmlDoc.getPrjMember())
        .orElse(prjXmlDoc.createElement(PrjXmlConstants.PRJ_MEMBERS_XPATH));
    List<Element> prjMemberNodes = prjMembers.selectNodes("prj_member");
    if (prjMemberNodes != null && prjMemberNodes.size() > 0) {
      List<String> zhNamelist = new ArrayList<String>();
      List<String> enNamelist = new ArrayList<String>();
      for (Element p : prjMemberNodes) {
        String zhName = p.attributeValue("member_psn_name");
        String enName = p.attributeValue("member_psn_name_en");
        if (zhName != null) {
          zhNamelist.add(zhName.trim());
        }
        if (enName != null) {
          enNamelist.add(enName.trim());
        }
      }
      project.setAuthorNames(StringUtils.join(zhNamelist.toArray(), "; "));
      project.setAuthorNamesEn(StringUtils.join(enNamelist.toArray(), "; "));
    }

  }

  /**
   * .创建项目统计表信息
   * 
   * @param newPrjId 不能为{@code null}
   */
  private void buildPrjstatistics(Long newPrjId) throws ServiceException {
    try {
      ProjectStatistics statistics = projectStatisticsDao.get(newPrjId);
      if (statistics == null) {
        statistics = new ProjectStatistics(newPrjId, 0, 0, 0, 0, 0, 0);
        projectStatisticsDao.save(statistics);
      }
    } catch (Exception e) {
      logger.error("创建项目统计表信息！prjId={}", newPrjId, e);
      throw new ServiceException(e);
    }

  }

  @Override
  public Page getPsnFileListInGroup(Page<StationFile> page) throws Exception {
    List<StationFile> list = new ArrayList<StationFile>();
    List<PsnFile> psnFilelist = new ArrayList<PsnFile>();
    Long psnId = SecurityUtils.getCurrentUserId();
    psnFilelist = psnFileDao.getFileListForPsn(page, psnId, null, null);
    // list = stationFileDao.getFileListForPsn(page, psnId, null, null);
    for (int i = 0; i < psnFilelist.size(); i++) {
      PsnFile psnFile = psnFilelist.get(i);
      if (psnFile == null || psnFile.getArchiveFileId() == null || psnFile.getArchiveFileId() == 0L) {
        continue;
      }
      StationFile stationFile = new StationFile();
      ArchiveFile sf = this.archiveFileDao.get(psnFile.getArchiveFileId());
      if (sf != null) {
        stationFile.setFileSize(sf.getFileSize());
        stationFile.setFileDesc(sf.getFileDesc());
        stationFile.setArchiveFileId(sf.getFileId());
        stationFile.setFileName(sf.getFileName());
        stationFile.setUploadTime(psnFile.getUploadDate());
        // stationFile.set(psnFile.getUpdateDate());
        stationFile.setFileType(sf.getFileType());
        stationFile.setDownloadUrl(fileDownUrlService.getDownloadUrl(FileTypeEnum.PSN, psnFile.getId(), 0L));
        list.add(stationFile);
      }

    }
    page.setResult(list);
    return page;
  }

  @Override
  public PrjXmlProcessContext buildXmlProcessContext(Long psnId) {
    PrjXmlProcessContext context = new PrjXmlProcessContext();
    Locale locale = LocaleContextHolder.getLocale();
    context.setCurrentLanguage(locale.getLanguage());
    context.setCurrentUserId(psnId);
    context.setLocale(locale);
    return context;
  }


  /**
   * 处理项目XML及更新相关表,这个地方编辑保存项目和导入项目都用到了, 改动后记得看下这两部分功能是否都正常.
   * 
   * @param context 不能为{@code null}
   * @param xmlDoc 不能为{@code null}
   * @param prjXmlProcessor 不能为{@code null}
   */
  @Override
  public void dealWithXmlByProcessor(PrjXmlProcessContext context, PrjXmlDocument xmlDoc,
      PrjXmlProcessor prjXmlProcessor) {
    Long prjId = context.getCurrentPrjId();
    Long psnId = context.getCurrentUserId();
    // 更新元信息
    prjXmlProcessor.updatePrjMeta(context)
        // 拆分导入的人员信息，构建prjMembers节点（导入项目时才会做具体处理）
        .splitImportAuthorName(context)
        // 更新项目日期信息
        .updateDateNode()
        // 更新项目组成员信息
        .updatePrjMember(institutionService)
        // 处理标题、关键词、摘要等信息
        .updateTitileAbstractAndKeywords()
        // 导入项目操作需要处理xml中资助机构和资助类别信息
        .updateImportXmlScheme(context)
        // 全角转半角
        .SBCase2DBCase()
        // 格式化资金金额
        .formatMoney()
        // 更新依托单位
        .updateIns(institutionService)
        // 更新文件导入的相关数据
        .updateFileImportData(categoryMapBaseDao, categoryMapScmNsfcDao)
        // 字段校验
        .validate()
        // 生成简要描述
        .generateBrief()
        // 生成项目标题的哈希值
        .generateHashCode();
    // 保存项目信息到project表
    xmlDoc = prjXmlProcessor.getPrjXmlDocument();
    Project project = this.saveProjectInfo(xmlDoc, prjXmlProcessor, context);
    // 保存项目xml到scm_prj_xml表
    this.savePrjXml(prjId, xmlDoc.getXmlString());
    // 原权限
    String oldAuthority = xmlDoc.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "authority");
    // 保存项目权限信息
    String authority = this.savePrjConfig(prjId, xmlDoc, psnId);
    // 创建项目统计表信息
    buildPrjstatistics(prjId);
    // 保存项目全文信息
    this.savePrjFulltext(project);
    // 保存项目报告和经费
    this.buildPrjReport(context.getPrjInfo());
    this.buildPrjExpenditure(context.getPrjInfo());
    if ("skip".equals(context.getDupOperation())
        || ("refresh".equals(context.getDupOperation()) && PsnCnfConst.ALLOWS.toString().equals(oldAuthority))) {
      return;// 查重后操作为跳过或更新不是隐私改公开权限的项目，都不需要更新人员项目总数和公开项目总数
    }
    // 更新人员项目总数和公开项目总数
    this.updatePsnPrjStatistics(psnId, authority, context.getDupOperation());
  }

  /**
   * 构建项目报告
   * 
   * @param prjInfo
   */
  private void buildPrjReport(PrjInfoDTO prjInfo) {
    if (prjInfo != null && !CollectionUtils.isEmpty(prjInfo.getPrjReportDTOS())) {
      prjReportDao.delete(prjInfo.getPrjId());
      prjInfo.getPrjReportDTOS().forEach(prjReport -> {
        com.smate.core.base.project.model.PrjReport report = new com.smate.core.base.project.model.PrjReport();
        report.setPrjId(prjInfo.getPrjId());
        report.setGmtCreate(new Date());
        report.setWarnDate(DateUtils.parseStringToDate(prjReport.getAbortDate()));
        report.setReportType(PrjUtils.getReportType(prjReport.getReportType()));
        Date currentDate = new Date();
        /**
         * 1已结束 2已填写 3前往填写 4未填写<br/>
         * 已结束 - 未点击前往填写，且时间过了截止时间<br/>
         * 已填写 - 点击过前往填写<br/>
         * 前往填写 - 截止时间在当前时间之后<br/>
         * 未填写 - 只显示一个前往填写的记录，其他均是未填写<br/>
         */
        if (report.getWarnDate() != null) {
          // 当前时间大于截止时间，说明已经过了截止时间，状态应为已结束，否则为未填写
          report.setStatus(currentDate.getTime() > report.getWarnDate().getTime() ? 1 : 4);
        } else {
          report.setStatus(4);
        }
        prjReportDao.save(report);
      });
    }

  }

  /**
   * 构建项目经费
   * 
   * @param prjInfo
   */
  private void buildPrjExpenditure(PrjInfoDTO prjInfo) {
    if (prjInfo != null && !CollectionUtils.isEmpty(prjInfo.getPrjFundPlanDTOS())) {
      projectExpenditureDao.delete(prjInfo.getPrjId());
      prjInfo.getPrjFundPlanDTOS().forEach(prjFundPlan -> {
        ProjectExpenditure expenditure = new ProjectExpenditure();
        expenditure.setPrjId(prjInfo.getPrjId());
        expenditure.setSeqNo(prjFundPlan.getSeqNo());
        expenditure.setExpenItem(prjFundPlan.getItemName());
        expenditure.setParentSeqNo(prjFundPlan.getpSeq());
        expenditure.setAllocatedAmount(0.00f);
        expenditure.setUsedAmount(0.00f);
        expenditure.setAdvanceAmount(0.00f);
        expenditure.setSchemeAmount(Float.parseFloat(prjFundPlan.getItemAmout()));
        expenditure.setSelfAmount(Float.parseFloat(prjFundPlan.getZcAmout()));
        expenditure.setSupportAmount(Float.parseFloat(prjFundPlan.getPtAmout()));
        expenditure.setGmtCreate(new Date());
        expenditure.setStatus(0);
        expenditure.setGmtModified(expenditure.getGmtCreate());
        projectExpenditureDao.save(expenditure);
      });
    }

  }

  /**
   * .保存项目信息到project表
   * 
   * @param xmlDoc 不能为{@code null}
   * @param xmlDoc 不能为{@code null}
   * @param prjXmlProcessor 不能为{@code null}
   */
  private Project saveProjectInfo(PrjXmlDocument xmlDoc, PrjXmlProcessor prjXmlProcessor,
      PrjXmlProcessContext context) {

    Project project = projectService.getProject(context.getCurrentPrjId());
    if (project == null) {
      project = new Project();
      project.setVersionNo(0);
      project.setId(context.getCurrentPrjId());
      project.setPsnId(context.getCurrentUserId());
      project.setCreatePsnId(context.getCurrentUserId());
    }
    prjXmlProcessor
        // 同步项目字段信息
        .syncProjectData(project)
        // 同步项目成员
        .syncPrjMember(prjMemberService, personOpenDao, project)
        // 作者太多时缩略
        .abbreviateAuthorNames(project);
    project.setRecordFrom(PrjXmlOperationEnum.Import.equals(context.getCurrentAction()) ? 1 : 0);
    project.setStatus(0);
    // buildAuthorNameInfo(project, xmlDoc);
    projectService.saveProject(project);
    return project;
  }



  /**
   * .保存项目权限信息
   * 
   * @param prjId 不能为{@code null}
   * @param xmlDoc 不能为{@code null}
   */
  private String savePrjConfig(Long prjId, PrjXmlDocument xmlDoc, Long psnId) {
    String authority = xmlDoc.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "authority");
    if (StringUtils.isBlank(authority) || !NumberUtils.isCreatable(authority)) {
      authority = PsnCnfConst.ALLOWS.toString();
    }
    // 保存权限
    PsnConfigPrj pcp = new PsnConfigPrj();
    pcp.getId().setPrjId(prjId);
    pcp.setAnyUser(Integer.parseInt(authority));
    pcp.setAnyView(Integer.parseInt(authority));
    psnCnfService.save(psnId, pcp);
    return authority;
  }

  /**
   * .保存项目全文信息到PRJ_FULLTEXT表
   * 
   * @param project 不能为{@code null}
   */
  private void savePrjFulltext(Project project) {
    if (NumberUtils.isCreatable(project.getFulltextFileId())) {
      PrjFulltext pf = new PrjFulltext();
      pf.setPrjId(project.getId());
      pf.setFulltextFileId(Long.parseLong(project.getFulltextFileId()));
      pf.setFulltextNode(1);
      prjFulltextDao.save(pf);
    }
  }

  /**
   * .更新人员项目总数和公开项目总数
   * 
   * @param psnId 不能为{@code null}
   * @param authority 可为{@code null}
   */
  private void updatePsnPrjStatistics(Long psnId, String authority, String dupOperation) {
    PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
    if (psnStatistics != null) {
      if (!"refresh".equals(dupOperation)) {// 不是更新项目则增加项目总数
        psnStatistics.setPrjSum(psnStatistics.getPrjSum() + 1);
      }
      if (StringUtils.isNotBlank(authority) && "7".equals(authority)) {// 若为隐私改公开权限的项目则更新公开项目总数
        psnStatistics.setOpenPrjSum(psnStatistics.getOpenPrjSum() + 1); // 更新公开项目统计数
      }
      // 属性为null的保存为0
      psnStatistics.setPsnId(psnId);
      PsnStatisticsUtils.buildZero(psnStatistics);
      psnStatisticsDao.save(psnStatistics);
    }
  }
}
