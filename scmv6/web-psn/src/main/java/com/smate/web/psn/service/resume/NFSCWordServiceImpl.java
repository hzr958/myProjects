package com.smate.web.psn.service.resume;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.vo.ProjectInfo;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.constant.ResumeConstants;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.string.StringHtml;
import com.smate.web.prj.xml.PrjXmlConstants;
import com.smate.web.prj.xml.PrjXmlDocument;
import com.smate.web.psn.dao.project.ProjectDao;
import com.smate.web.psn.dao.project.ScmPrjXmlDao;
import com.smate.web.psn.dao.resume.CVModuleInfoDao;
import com.smate.web.psn.dao.resume.ResumeModuleDao;
import com.smate.web.psn.form.resume.PersonalResumeForm;
import com.smate.web.psn.model.consts.LocaleConsts;
import com.smate.web.psn.model.consts.ResumeModuleConsts;
import com.smate.web.psn.model.newresume.CVModuleInfo;
import com.smate.web.psn.model.newresume.ResumeModule;
import com.smate.web.psn.model.project.ScmPrjXml;
import com.smate.web.psn.model.pub.PubMember;
import com.smate.web.psn.model.resume.CVEduInfo;
import com.smate.web.psn.model.resume.CVPrj;
import com.smate.web.psn.model.resume.CVPsnInfo;
import com.smate.web.psn.model.resume.CVWorkInfo;
import com.smate.web.psn.model.resume.PubPrjData;
import com.smate.web.psn.model.resume.WorkEduData;
import com.smate.web.psn.utils.LocaleStringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

@Service("NFSCWordService")
@Transactional(rollbackFor = Exception.class)
public class NFSCWordServiceImpl implements WordService {

  private final String encoding = "utf-8";
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Resource(name = "wordFreemarkerConfiguration")
  private Configuration wordFreemarkerConfiguration;
  @Value("${domainscm}")
  private String domainscm;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private CVModuleInfoDao cVModuleInfoDao;
  @Autowired
  private ResumeModuleDao resumeModuleDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private ScmPrjXmlDao scmPrjXmlDao;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private PersonDao personDao;
  @Value("${file.root}")
  private String fileRoot;

  @Override
  public File exportWordFile(PersonalResumeForm form) throws Exception {
    Map<String, Object> wordMap = new HashMap<String, Object>();
    Locale locale = LocaleContextHolder.getLocale();
    // 页眉信息
    // String headerDataTmp = buildHeaderDataTmp(locale);
    // wordMap.put("headerDataTmp", headerDataTmp);
    // 头部信息
    /*
     * String userDataTmp = buildUserDataTmp(locale, form); wordMap.put("userDataTmp", userDataTmp);
     */
    // body信息
    StringBuffer leftDataTmp = buildLeftDataTmp(locale, form);
    wordMap.put("leftDataTmp", leftDataTmp.toString());
    // 超链接信息
    String hrefTemp = this.buildPubHrefDataTmp(locale, form);
    wordMap.put("hrefDataTmp", hrefTemp);
    // 整个word简历信息
    String wordXmlStr = buildWordTmp(locale, wordMap);
    return new File(writeToWord(wordXmlStr));
  }

  /**
   * 构建页眉信息
   * 
   * @param locale
   * @return
   */
  private String buildHeaderDataTmp(Locale locale) {
    String headerDataTmp = "";
    try {
      Map<String, Object> headerDataMap = new HashMap<String, Object>();
      String headerFtl = "";
      headerDataMap.put("title", "科研之友 - 连接，让创新更高效");
      headerFtl = ResumeConstants.WORD_HEADER_2003_ZH;
      headerDataMap.put("linkUrl", "http://dev.scholarmate.com/psnweb/resume/resumedetail?des3ResumeId=SHrjDqouJGs%3D");
      headerDataMap.put("linkLabel", "查看科研之友简历");
      Template headerTemplate = wordFreemarkerConfiguration.getTemplate(headerFtl, encoding);
      headerDataTmp = FreeMarkerTemplateUtils.processTemplateIntoString(headerTemplate, headerDataMap);
    } catch (Exception e) {
      logger.error("导出word简历,构建页眉信息出错", e);
    }
    return headerDataTmp;
  }

  /**
   * 构建头部信息
   * 
   * @param locale
   * @param form
   * @return
   */
  private String buildUserDataTmp(Locale locale, PersonalResumeForm form) {
    String userXml = "";
    try {
      Map<String, Object> userDataMap = new HashMap<String, Object>();
      Map<String, Object> userData = new HashMap<String, Object>();
      userDataMap.put("info0", "名字");
      userDataMap.put("title", "单位，部门，职称");
      userData.put("userData", userDataMap);
      String userFtl = userFtl = ResumeConstants.NFSC_WORD_USERINFO;
      Template userTemplate = wordFreemarkerConfiguration.getTemplate(userFtl, encoding);
      userXml = FreeMarkerTemplateUtils.processTemplateIntoString(userTemplate, userData);
    } catch (Exception e) {
      logger.error("---------------------出错了--------------------", e);
    }
    return userXml;
  }

  /**
   * body信息
   * 
   * @param locale
   * @param form
   * @return
   */
  private StringBuffer buildLeftDataTmp(Locale locale, PersonalResumeForm form) {
    StringBuffer leftDataTmp = new StringBuffer();
    try {
      Map<String, Object> leftDataMap = new HashMap<String, Object>();
      String leftFtl = "";
      int count = ResumeModuleConsts.NFSC_MAX_MODULE_ID;
      for (int i = 1; i <= count; i++) {
        switch (i) {
          case ResumeModuleConsts.BASE_INFO:
            // 个人简介
            // leftFtl = buildWordBodyTxt(locale, form, leftFtl,
            // leftDataMap, ResumeConstants.WORD_BODY_BRIEF_STR);
            leftFtl = buildWordBodyPsnInfo(leftDataMap, form, leftFtl, ResumeModuleConsts.BASE_INFO);
            break;
          case ResumeModuleConsts.EDU_INFO:
            // 教育经历
            leftFtl = buildWordBodyWorkEdu(locale, form, leftFtl, leftDataMap, ResumeConstants.WORD_BODY_EDU_STR);
            break;
          case ResumeModuleConsts.WORK_INFO:
            // 工作经历
            leftFtl = buildWordBodyWorkEdu(locale, form, leftFtl, leftDataMap, ResumeConstants.WORD_BODY_WORK_STR);
            break;
          case ResumeModuleConsts.PRJ_INFO:
            // 项目
            leftFtl = buildWordBodyPrj(locale, form, leftFtl, leftDataMap, ResumeConstants.WORD_BODY_PRJ_STR);
            break;
          case ResumeModuleConsts.REPRESENT_PUB_INFO:
            // 代表性成果
            leftFtl =
                this.buildWordBodyPubPrj(locale, form, leftFtl, leftDataMap, ResumeModuleConsts.REPRESENT_PUB_INFO);
            break;
          case ResumeModuleConsts.OTHER_PUB_INFO:
            // 其他成果
            leftFtl = this.buildWordBodyPubPrj(locale, form, leftFtl, leftDataMap, ResumeModuleConsts.OTHER_PUB_INFO);
            break;
          default:
            break;
        }
        if (StringUtils.isNotBlank(leftFtl)) {
          Template leftTemplate = wordFreemarkerConfiguration.getTemplate(leftFtl, encoding);
          String leftData = FreeMarkerTemplateUtils.processTemplateIntoString(leftTemplate, leftDataMap);
          leftDataTmp.append(leftData);
          leftFtl = "";
        }
      }
    } catch (Exception e) {
      logger.error("导出word简历,导出body信息出错", e);
    }
    return leftDataTmp;
  }

  @SuppressWarnings("unchecked")
  private String buildWordBodyPrj(Locale locale, PersonalResumeForm form, String leftFtl,
      Map<String, Object> leftDataMap, String wordBodyPrjStr) {
    PubPrjData pubPrjData = new PubPrjData();
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    Long CVModuleInfoId = resumeModuleDao.findCVModuleInfoId(form.getResumeId(), ResumeModuleConsts.PRJ_INFO);
    if (CVModuleInfoId != null && CVModuleInfoId > 0l) {
      CVModuleInfo cvModuleInfo = cVModuleInfoDao.get(CVModuleInfoId);
      if (cvModuleInfo != null) {
        List<CVPrj> CVPrjList =
            JacksonUtils.jsonToCollection(cvModuleInfo.getModuleInfo(), ArrayList.class, CVPrj.class);
        if (CollectionUtils.isNotEmpty(CVPrjList)) {
          List<ProjectInfo> prjInfoList = new ArrayList<ProjectInfo>();
          for (CVPrj cvPrj : CVPrjList) {
            Project prj = projectDao.get(cvPrj.getPrjId());
            if (prj != null) {
              ProjectInfo prjInfo = new ProjectInfo();
              prjInfo.setPrjId(prj.getId());
              Map<String, Object> map = new HashMap<String, Object>();
              String showTitle = "";
              String agencyAndscheme = "";
              String showDate = "";
              if (StringUtils.isBlank(prj.getZhTitle())) {
                showTitle = prj.getEnTitle();
              } else {
                showTitle = prj.getZhTitle();
              }
              if (StringUtils.isBlank(prj.getAgencyName())) {
                agencyAndscheme = prj.getEnAgencyName();
              } else {
                agencyAndscheme = prj.getAgencyName();
              }
              if (StringUtils.isNotBlank(agencyAndscheme)) {
                if (StringUtils.isBlank(prj.getSchemeName())) {
                  if (StringUtils.isNotBlank(prj.getEnSchemeName())) {
                    agencyAndscheme += " - " + prj.getEnSchemeName();
                  }
                } else {
                  agencyAndscheme += " - " + prj.getSchemeName();
                }
              } else {
                if (StringUtils.isBlank(prj.getSchemeName())) {
                  agencyAndscheme = prj.getEnSchemeName();
                } else {
                  agencyAndscheme = prj.getSchemeName();
                }
              }
              map.put("agencyAndscheme", StringHtml.wordHandler(agencyAndscheme));// 项目类别
              map.put("externalNo", StringHtml.wordHandler(prj.getExternalNo()));// 批准号
              map.put("showTitle", StringHtml.wordHandler(showTitle));// 名称
              map.put("wordHrefSeq", form.getWordSeqStart() + 1);
              prjInfo.setWordHrefSeq(form.getWordSeqStart() + 1);
              prjInfoList.add(prjInfo);
              form.setWordSeqStart(form.getWordSeqStart() + 1);
              String startYM = "";
              String endYM = "";
              Integer startYear = prj.getStartYear();
              Integer startMonth = prj.getStartMonth();
              Integer endYear = prj.getEndYear();
              Integer endMonth = prj.getEndMonth();
              if (startYear != null && startMonth != null) {
                startYM = startYear + "." + startMonth;
              } else if (startYear != null && startMonth == null) {
                startYM = startYear + "";
              } else if (startYear == null && startMonth != null) {
                startYM = startMonth + "";
              }
              if (endYear != null && endMonth != null) {
                endYM = endYear + "." + endMonth;
              } else if (endYear != null && endMonth == null) {
                endYM = endYear + "";
              } else if (endYear == null && endMonth != null) {
                endYM = endMonth + "";
              }
              if (StringUtils.isNotBlank(startYM) && StringUtils.isNotBlank(endYM)) {
                showDate = startYM + " - " + endYM;
              } else if (StringUtils.isNotBlank(startYM) && StringUtils.isBlank(endYM)) {
                showDate = startYM;
              } else if (StringUtils.isBlank(startYM) && StringUtils.isNotBlank(endYM)) {
                showDate = endYM;
              }
              map.put("showDate", StringHtml.wordHandler(showDate));// 研究起止年月
              if (prj.getAmount() != null) {
                map.put("amountAndUnit", StringHtml.wordHandler(prj.getAmount() + " " + prj.getAmountUnit()));// 资助金额
              }
              if ("01".equals(prj.getState())) {
                map.put("prjState", "在研");
              } else if ("02".equals(prj.getState())) {
                map.put("prjState", "已结题");
              } else {
                map.put("prjState", "其他");// 项目状态
              }
              map.put("prjOwner", "参加");
              ScmPrjXml scmPrjXml = scmPrjXmlDao.get(prj.getId());
              if (scmPrjXml != null) {
                try {
                  PrjXmlDocument prjXmlDocument;
                  prjXmlDocument = new PrjXmlDocument(scmPrjXml.getPrjXml());
                  if (prjXmlDocument != null) {
                    List authors = prjXmlDocument.getNodes(PrjXmlConstants.PRJ_MEMBERS_MEMBER_XPATH);
                    for (int i = 0; i < authors.size(); i++) {
                      Element element = (Element) authors.get(i);
                      String notifyAuthor = element.attributeValue("notify_author");
                      String ownerPsnId = element.attributeValue("member_psn_id");
                      String iName = element.attributeValue("import_member_name");
                      String zName = element.attributeValue("member_psn_name");
                      String eName = element.attributeValue("member_psn_name_en");
                      Person person = personDao.get(form.getPsnId());
                      if (form.getPsnId().equals(NumberUtils.toLong(ownerPsnId))) {
                        if ("1".equals(notifyAuthor)) {
                          prjInfo.setPrjOwner("主持");// 主持或参加
                          map.put("prjOwner", "主持");
                          break;
                        }

                      } else {
                        if (person != null) {
                          String name = person.getName() == null ? "" : person.getName();
                          String ename = person.getEname() == null ? "" : person.getEname();
                          if (name.equals(zName) || name.equals(iName) || ename.equals(eName) || ename.equals(iName)) {
                            if ("1".equals(notifyAuthor)) {
                              prjInfo.setPrjOwner("主持");// 主持或参加
                              map.put("prjOwner", "主持");
                            }
                            break;
                          }
                        }
                      }
                    }
                  }
                } catch (DocumentException e) {
                  logger.error("简历 word nsfc 项目 scmPrjXml转换xml数据 出错", e);
                }
              }
              list.add(map);
            }
          }
          form.setPrjInfo(prjInfoList);
        }

      }
    }
    pubPrjData.setObjectContent(list);
    leftDataMap.put("pubPrjData", pubPrjData);
    leftFtl = ResumeConstants.WORD_NFSC_PRJ_2003_ZH;
    return leftFtl;
  }

  // 构造项目详情url
  private List<ProjectInfo> buildPrjUrlInfo(PersonalResumeForm form) throws UnsupportedEncodingException {
    List<ProjectInfo> prjInfoList = form.getPrjInfo();
    if (CollectionUtils.isNotEmpty(prjInfoList)) {
      for (ProjectInfo prj : prjInfoList) {
        prj.setPrjUrl(domainscm + "/scmwebsns/project/view?des3Id="
            + URLEncoder.encode(Des3Utils.encodeToDes3(prj.getPrjId().toString()), "utf-8"));
      }
    }
    return prjInfoList;
  }

  /**
   * 整个word简历信息
   * 
   * @param locale
   * @param wordMap
   * @return
   * @throws Exception
   */
  private String buildWordTmp(Locale locale, Map<String, Object> wordMap) throws Exception {
    String wordFtl = ResumeConstants.NFSC_WORD_CV_TEMPLATE;
    Template wordTemplate = wordFreemarkerConfiguration.getTemplate(wordFtl, encoding);
    String wordXml = FreeMarkerTemplateUtils.processTemplateIntoString(wordTemplate, wordMap);
    return wordXml;
  }

  /**
   * 生成word文档
   * 
   * @param wordXmlDataStr
   * @return
   * @throws IOException
   */
  private String writeToWord(String wordXmlDataStr) throws IOException {
    String outFile = fileRoot + "/export/resume" + getFileName() + ".doc";
    FileOutputStream out = new FileOutputStream(new File(outFile));
    ByteArrayInputStream fis = new ByteArrayInputStream(wordXmlDataStr.getBytes());
    byte[] buffer = new byte[fis.available()];
    while (fis.read(buffer) != -1) {
      out.write(buffer);
    }
    out.flush();
    out.close();
    fis.close();
    return outFile;
  }

  /**
   * 生成文件名
   * 
   * @return
   */
  private String getFileName() {
    String fileStr = UUID.randomUUID().toString();
    String[] fileArray = fileStr.split("-");
    String fileName = "";
    for (int i = 0; i < fileArray.length; i++)
      fileName = fileName + fileArray[i];
    return fileName;
  }

  /**
   * 构建成果模板信息
   * 
   * @param locale
   * @param form
   * @param leftFtl
   * @param leftDataMap
   * @param moduleId
   * @return
   * @throws UnsupportedEncodingException
   */
  private String buildWordBodyPubPrj(Locale locale, PersonalResumeForm form, String leftFtl,
      Map<String, Object> leftDataMap, int moduleId) throws UnsupportedEncodingException {
    PubPrjData pubPrjData = new PubPrjData();
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    // 成果
    List<PubInfo> pubs = new ArrayList<PubInfo>();
    if (ResumeModuleConsts.REPRESENT_PUB_INFO == moduleId) {
      pubs = form.getRepresentPubList();
      leftFtl = ResumeConstants.NFSC_WORD_REPRESENTPUB;
    } else {
      pubs = form.getOtherPubList();
      leftFtl = ResumeConstants.NFSC_WORD_OTHERPUB;
    }
    if (CollectionUtils.isNotEmpty(pubs)) {
      if (ResumeModuleConsts.REPRESENT_PUB_INFO == moduleId) {
        if (Locale.US.equals(locale)) {
          pubPrjData.setTitle("10篇以内代表性论著");
        } else {
          pubPrjData.setTitle("10篇以内代表性论著");
        }
      } else {
        if (Locale.US.equals(locale)) {
          pubPrjData.setTitle("论著之外的代表性研究成果和学术奖励");
        } else {
          pubPrjData.setTitle("论著之外的代表性研究成果和学术奖励");
        }
      }
      int seqNo = 1;
      for (PubInfo pub : pubs) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("seqNo", seqNo);
        map.put("authorList", pub.getMemberInfo());
        /*
         * map.put("awardSeq", this.getSNSAwardPubMemberSeq(pub.getMemberList(), form.getCurrentPsnName(),
         * pub.getPubType()));
         */
        map.put("title", StringHtml.wordHandler(pub.getTitle()));
        map.put("source", StringHtml.wordHandler(pub.getBriefDesc()));
        map.put("pubTypeName", pub.getPubTypeName());
        map.put("currentPsnName", StringHtml.wordHandler(form.getCurrentPsnName()));
        map.put("pubType", pub.getPubType());
        map.put("wordHrefSeq", pub.getWordHrefSeq());
        // this.findPsnIsFirstOrPosAuthor(pub.getMemberList(),
        // form.getCurrentPsnName(), pub.getPubType(), map);
        // String linkUrl = "";
        // PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(pub.getPubId());
        // if (pubIndexUrl != null &&
        // StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
        // linkUrl = domainscm + "/" + ShortUrlConst.A_TYPE + "/" +
        // pubIndexUrl.getPubIndexUrl();
        // } else {
        // linkUrl = domainscm + "/pubweb/outside/details?des3Id="
        // +
        // URLEncoder.encode(ServiceUtil.encodeToDes3(pub.getPubId().toString()),
        // "utf-8");
        // }
        // map.put("linkUrl", linkUrl);
        list.add(map);
        seqNo++;
      }
    }
    pubPrjData.setObjectContent(list);
    leftDataMap.put("cvPubData", pubPrjData);
    return leftFtl;
  }

  /**
   * 构建教育经历/工作经历模板信息
   * 
   * @param locale
   * @param form
   * @param leftFtl
   * @param leftDataMap
   * @param type
   * @return
   */
  private String buildWordBodyWorkEdu(Locale locale, PersonalResumeForm form, String leftFtl,
      Map<String, Object> leftDataMap, String type) {
    WorkEduData workEduData = new WorkEduData();
    workEduData.setItems(buildWorkEduItems(workEduData, locale, form, type));
    leftDataMap.put("workEduData", workEduData);
    if (Locale.US.equals(locale)) {
      leftFtl = ResumeConstants.NSFC_WORD_WORKEDU_2003_EN;
    } else {
      leftFtl = ResumeConstants.NSFC_WORD_WORKEDU_2003_ZH;
    }
    return leftFtl;
  }

  /**
   * 构建工作经历和教育经历信息 ： 开始年月-结束年月, 机构名称, 部门/主修, 职称/学位
   * 
   * @param workEduData
   * @param locale
   * @param form
   * @param type
   * @return
   */
  private List<String> buildWorkEduItems(WorkEduData workEduData, Locale locale, PersonalResumeForm form, String type) {
    List<String> items = new ArrayList<String>();
    String insName = "";
    String department = "";
    String position = "";
    Long fromYear = null;
    Long toYear = null;
    Long fromMonth = null;
    Long toMonth = null;
    Long isActive = null;
    String description = "";
    if (ResumeConstants.WORD_BODY_EDU_STR.equals(type)) {
      if (Locale.US.equals(locale)) {
        workEduData.setTitle("教育经历:");
      } else {
        workEduData.setTitle("教育经历:");
      }
      // 获取教育经历
      if (form.getPsnId() != null && form.getResumeId() != null) {
        ResumeModule resumeModule =
            resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), ResumeModuleConsts.EDU_INFO);
        if (resumeModule != null && resumeModule.getStatus() != 1) {
          CVModuleInfo cvModuleInfo = cVModuleInfoDao.get(resumeModule.getModuleInfoId());
          String eduJsonStr = cvModuleInfo.getModuleInfo();
          List<CVEduInfo> eduInfoList =
              (List<CVEduInfo>) JacksonUtils.jsonToCollection(eduJsonStr, ArrayList.class, CVEduInfo.class);
          if (CollectionUtils.isNotEmpty(eduInfoList)) {
            Collections.sort(eduInfoList, Collections.reverseOrder());// 按时间逆序排序
            for (CVEduInfo edu : eduInfoList) {
              StringBuilder item = new StringBuilder();
              insName = StringUtils.isBlank(edu.getInsName()) ? "" : edu.getInsName();
              department = StringUtils.isBlank(edu.getDepartment()) ? "" : edu.getDepartment();
              position = StringUtils.isBlank(edu.getDegreeName()) ? "" : edu.getDegreeName();
              fromYear = edu.getFromYear();
              toYear = edu.getToYear();
              fromMonth = edu.getFromMonth();
              toMonth = edu.getToMonth();
              isActive = edu.getIsActive() == null ? 0l : edu.getIsActive();
              description = StringUtils.isBlank(edu.getDescription()) ? "" : edu.getDescription();
              item = buildItem(locale, item, insName, department, position, fromYear, toYear, fromMonth, toMonth,
                  isActive, description);
              items.add(item.toString());
            }
          }
        }
      }
    }
    if (ResumeConstants.WORD_BODY_WORK_STR.equals(type)) {
      if (Locale.US.equals(locale)) {
        workEduData.setTitle("科研与学术工作经历:");
      } else {
        workEduData.setTitle("科研与学术工作经历:");
      }
      if (form.getPsnId() != null && form.getResumeId() != null) {
        ResumeModule resumeModule =
            resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), ResumeModuleConsts.WORK_INFO);
        if (resumeModule != null && resumeModule.getStatus() != 1) {
          CVModuleInfo cvModuleInfo = cVModuleInfoDao.get(resumeModule.getModuleInfoId());
          String workJsonStr = cvModuleInfo.getModuleInfo();
          List<CVWorkInfo> workInfoList =
              (List<CVWorkInfo>) JacksonUtils.jsonToCollection(workJsonStr, ArrayList.class, CVWorkInfo.class);
          if (CollectionUtils.isNotEmpty(workInfoList)) {
            Collections.sort(workInfoList, Collections.reverseOrder());// 按时间逆序排序
            for (CVWorkInfo work : workInfoList) {
              StringBuilder item = new StringBuilder();
              insName = StringUtils.isBlank(work.getInsName()) ? "" : work.getInsName();
              department = StringUtils.isBlank(work.getDepartment()) ? "" : work.getDepartment();
              position = StringUtils.isBlank(work.getDegreeName()) ? "" : work.getDegreeName();
              fromYear = work.getFromYear();
              toYear = work.getToYear();
              fromMonth = work.getFromMonth();
              toMonth = work.getToMonth();
              description = work.getDescription();
              isActive = work.getIsActive() == null ? 0l : work.getIsActive();
              item = buildItem(locale, item, insName, department, position, fromYear, toYear, fromMonth, toMonth,
                  isActive, description);
              items.add(item.toString());
            }
          }
        }
      }
    }
    return items;
  }

  /**
   * 构建开始时间-结束时间,机构名称,主修/部门,学位/职称信息
   * 
   * @param item
   * @param insName
   * @param department
   * @param position
   * @param fromYear
   * @param toYear
   * @param fromMonth
   * @param toMonth
   * @param isActive
   * @return
   */
  private StringBuilder buildItem(Locale locale, StringBuilder item, String insName, String department, String position,
      Long fromYear, Long toYear, Long fromMonth, Long toMonth, Long isActive, String description) {
    insName = StringHtml.wordHandler(insName);
    department = StringHtml.wordHandler(department);
    position = StringHtml.wordHandler(position);
    description = StringHtml.wordHandler(description);
    // String splitStr = Locale.US.equals(locale) ? ", " : "，";// 统一为", "
    String splitStr = ", ";
    // 开始时间-结束时间
    if (fromYear != null && fromYear != 0) {
      item.append(fromYear);
      if (fromMonth != null && fromMonth != 0) {
        item.append("." + fromMonth);
      }
      if (toYear != null && toYear != 0) {
        item.append(" - " + toYear);
        if (toMonth != null && toMonth != 0) {
          item.append("." + toMonth);
        }
      } else {
        if (isActive == 1) {
          item.append(" - " + LocaleStringUtils.getStringByLocale(LocaleConsts.TO_PRESENT_EN, LocaleConsts.TO_PRESENT));
        }
      }
    }
    // 机构名称
    if (StringUtils.isNotBlank(insName)) {
      if (StringUtils.isNotBlank(item)) {
        item.append(splitStr + insName);
      } else {
        item.append(insName);
      }
    }
    // 主修/部门
    if (StringUtils.isNotBlank(department)) {
      if (StringUtils.isNotBlank(item)) {
        item.append(splitStr + department);
      } else {
        item.append(department);
      }
    }
    // 学位/职称
    if (StringUtils.isNotBlank(position)) {
      if (StringUtils.isNotBlank(item)) {
        item.append(splitStr + position);
      } else {
        item.append(position);
      }
    }
    // 描述信息
    if (StringUtils.isNotBlank(description)) {
      if (StringUtils.isNotBlank(item)) {
        item.append(splitStr + description);
      }
    }
    return item;
  }

  public String buildWordBodyPsnInfo(Map<String, Object> wordBodyMap, PersonalResumeForm form, String leftFtl,
      int moduleId) throws Exception {
    // 查询个人信息
    CVPsnInfo cVPsnInfo = new CVPsnInfo();
    ResumeModule resumeModule = resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), moduleId);
    if (resumeModule != null) {
      String moduleInfo = cVModuleInfoDao.getModuleInfo(resumeModule.getModuleInfoId());
      if (StringUtils.isNotBlank(moduleInfo)) {
        Map<String, String> psnInfoResume = JacksonUtils.jsonMapUnSerializer(moduleInfo);
        if (StringUtils.isNotBlank(psnInfoResume.get("name"))) {
          cVPsnInfo.setName(StringHtml.wordHandler(psnInfoResume.get("name")));
        }
        if (StringUtils.isNotBlank(psnInfoResume.get("degree"))) {
          cVPsnInfo.setDegree(StringHtml.wordHandler(psnInfoResume.get("degree")));
        }
        if (StringUtils.isNotBlank(psnInfoResume.get("insInfo"))) {
          cVPsnInfo.setInsInfo(StringHtml.wordHandler(psnInfoResume.get("insInfo")));
        }
        if (StringUtils.isNotBlank(psnInfoResume.get("department"))) {
          cVPsnInfo.setDepartment(StringHtml.wordHandler(psnInfoResume.get("department")));
        }

      }
    }
    wordBodyMap.put("cVPsnInfo", cVPsnInfo);

    leftFtl = ResumeConstants.NFSC_WORD_USERINFO;
    return leftFtl;
  }

  /**
   * 奖励的作者格式
   * 
   * @param pubMemberList
   * @param currentPsnName
   * @return
   */
  private String getSNSAwardPubMemberSeq(List<PubMember> PubMemberList, String currentPsnName, Integer pubType) {
    if (pubType == PublicationTypeEnum.AWARD) {
      // 先用名字匹配
      if (StringUtils.isNotBlank(currentPsnName)) {
        for (PubMember pubMember : PubMemberList) {
          if (currentPsnName.equals(pubMember.getName())) {
            return "(" + pubMember.getSeqNo() + "/" + PubMemberList.size() + ")";
          }
        }
      }
      // 找不到的话，再用成果本人
      for (PubMember PubMember : PubMemberList) {
        if (PubMember.getOwner() == 1) {
          return "(" + PubMember.getSeqNo() + "/" + PubMemberList.size() + ")";
        }
      }
    }
    // 还是找不到就用本人（没有排名）
    return "";
  }

  /**
   * 奖励的成果中本人是否是第一作者、通讯作者
   * 
   * @param pubMemberList
   * @param currentPsnName
   * @return
   */
  private void findPsnIsFirstOrPosAuthor(List<PubMember> PubMemberList, String currentPsnName, Integer pubType,
      Map<String, Object> map) {
    map.put("isPosAuthor", "0");
    map.put("isFirstAuthor", "0");
    if (pubType == PublicationTypeEnum.AWARD) {
      boolean findSelf = false;
      // 先用名字匹配
      if (StringUtils.isNotBlank(currentPsnName)) {
        for (PubMember pubMember : PubMemberList) {
          if (currentPsnName.equals(pubMember.getName())) {
            findSelf = true;
            if (pubMember.getAuthorPos() == 1) {
              map.put("isPosAuthor", "1");
            }
            if (pubMember.getFirstAuthor() == 1) {
              map.put("isFirstAuthor", "1");
            }
          }
        }
      }
      if (!findSelf) {
        // 找不到的话，再用成果本人
        for (PubMember pubMember : PubMemberList) {
          if (pubMember.getOwner() == 1) {
            if (pubMember.getAuthorPos() == 1) {
              map.put("isPosAuthor", "1");
            }
            if (pubMember.getFirstAuthor() == 1) {
              map.put("isFirstAuthor", "1");
            }
          }
        }
      }
    }
  }

  /**
   * 构建头部信息
   * 
   * @param locale
   * @param form
   * @return
   */
  private String buildPubHrefDataTmp(Locale locale, PersonalResumeForm form) {
    String hrefXml = "";
    try {
      Map<String, Object> hrefDataMap = new HashMap<String, Object>();
      hrefDataMap.put("representPubList", form.getRepresentPubList());
      hrefDataMap.put("otherPubList", form.getOtherPubList());
      hrefDataMap.put("prjList", buildPrjUrlInfo(form));
      String hrefFtl = ResumeConstants.NSFC_WORD_PUBHREF;
      Template hrefTemplate = wordFreemarkerConfiguration.getTemplate(hrefFtl, encoding);
      hrefXml = FreeMarkerTemplateUtils.processTemplateIntoString(hrefTemplate, hrefDataMap);
    } catch (Exception e) {
      logger.error("---------------------出错了--------------------", e);
    }
    return hrefXml;
  }

}
