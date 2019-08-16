package com.smate.web.psn.service.resume;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.psn.model.profile.PsnDisciplineKey;
import com.smate.core.base.pub.dao.PubIndexUrlDao;
import com.smate.core.base.pub.model.PubIndexUrl;
import com.smate.core.base.utils.constant.ImageBase64Encode;
import com.smate.core.base.utils.constant.ResumeConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.string.StringHtml;
import com.smate.web.psn.dao.project.ProjectDao;
import com.smate.web.psn.dao.resume.ResumePubSimpleDao;
import com.smate.web.psn.form.resume.PersonalResumeForm;
import com.smate.web.psn.model.consts.LocaleConsts;
import com.smate.web.psn.model.keyword.PsnScienceArea;
import com.smate.web.psn.model.resume.PubPrjData;
import com.smate.web.psn.model.resume.ResumePubSimple;
import com.smate.web.psn.model.resume.WorkEduData;
import com.smate.web.psn.service.profile.PersonManager;
import com.smate.web.psn.service.profile.PsnDisciplineKeyService;
import com.smate.web.psn.service.sciencearea.ScienceAreaService;
import com.smate.web.psn.utils.LocaleStringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 导出word简历服务实现类
 * 
 * @author lhd
 *
 */
@Service("normalWordService")
@Transactional(rollbackFor = Exception.class)
public class NormalWordServiceImpl implements WordService {
  private final String encoding = "utf-8";
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Resource(name = "wordFreemarkerConfiguration")
  private Configuration wordFreemarkerConfiguration;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PersonManager personManager;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private ScienceAreaService scienceAreaService;
  @Autowired
  private PsnDisciplineKeyService psnDisciplineKeyService;
  @Autowired
  private ResumePubSimpleDao resumePubSimpleDao;
  @Autowired
  private PubIndexUrlDao pubIndexUrlDao;
  @Autowired
  private ProjectDao projectDao;
  @Value("${file.root}")
  private String fileRoot;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public File exportWordFile(PersonalResumeForm form) throws Exception {
    Map<String, Object> wordMap = new HashMap<String, Object>();
    Locale locale = LocaleContextHolder.getLocale();
    // 页眉信息
    String headerDataTmp = buildHeaderDataTmp(locale);
    wordMap.put("headerDataTmp", headerDataTmp);
    // 头部信息
    String userDataTmp = buildUserDataTmp(locale, form);
    wordMap.put("userDataTmp", userDataTmp);
    // body信息
    StringBuffer leftDataTmp = buildLeftDataTmp(locale, form);
    wordMap.put("leftDataTmp", leftDataTmp.toString());
    // 整个word简历信息
    String wordXmlStr = buildWordTmp(locale, wordMap);
    return new File(writeToWord(wordXmlStr));
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
      int count = ResumeConstants.WORD_BODY_COUNT;
      for (int i = 0; i < count; i++) {
        switch (i) {
          case ResumeConstants.WORD_BODY_BRIEF:
            // 个人简介
            leftFtl = buildWordBodyTxt(locale, form, leftFtl, leftDataMap, ResumeConstants.WORD_BODY_BRIEF_STR);
            break;
          case ResumeConstants.WORD_BODY_EDU:
            // 教育经历
            leftFtl = buildWordBodyWorkEdu(locale, form, leftFtl, leftDataMap, ResumeConstants.WORD_BODY_EDU_STR);
            break;
          case ResumeConstants.WORD_BODY_WORK:
            // 工作经历
            leftFtl = buildWordBodyWorkEdu(locale, form, leftFtl, leftDataMap, ResumeConstants.WORD_BODY_WORK_STR);
            break;
          case ResumeConstants.WORD_BODY_SCIENCEAREA:
            // 科技领域
            leftFtl = buildWordBodyTxt(locale, form, leftFtl, leftDataMap, ResumeConstants.WORD_BODY_SCIENCEAREA_STR);
            break;
          case ResumeConstants.WORD_BODY_KEYWORDS:
            // 关键词
            leftFtl = buildWordBodyTxt(locale, form, leftFtl, leftDataMap, ResumeConstants.WORD_BODY_KEYWORDS_STR);
            break;
          case ResumeConstants.WORD_BODY_PUB:
            // 成果
            leftFtl = buildWordBodyPubPrj(locale, form, leftFtl, leftDataMap, ResumeConstants.WORD_BODY_PUB_STR);
            break;
          case ResumeConstants.WORD_BODY_PRJ:
            // 项目
            leftFtl = buildWordBodyPubPrj(locale, form, leftFtl, leftDataMap, ResumeConstants.WORD_BODY_PRJ_STR);
            break;
          default:
            break;
        }
        Template leftTemplate = wordFreemarkerConfiguration.getTemplate(leftFtl, encoding);
        String leftData = FreeMarkerTemplateUtils.processTemplateIntoString(leftTemplate, leftDataMap);
        leftDataTmp.append(leftData);
      }
    } catch (Exception e) {
      logger.error("导出word简历,导出body信息出错", e);
    }
    return leftDataTmp;
  }

  /**
   * 构建成果/项目模板信息
   * 
   * @param locale
   * @param form
   * @param leftFtl
   * @param leftDataMap
   * @param type
   * @return
   */
  private String buildWordBodyPubPrj(Locale locale, PersonalResumeForm form, String leftFtl,
      Map<String, Object> leftDataMap, String type) {
    PubPrjData pubPrjData = new PubPrjData();
    List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
    String title = "";
    String authorNames = "";
    String briefDesc = "";
    // 成果
    if (ResumeConstants.WORD_BODY_PUB_STR.equals(type)) {
      List<ResumePubSimple> pubs = resumePubSimpleDao.getPubs(form.getPsnId());
      if (CollectionUtils.isNotEmpty(pubs)) {
        if (Locale.US.equals(locale)) {
          pubPrjData.setTitle("Publications");
        } else {
          pubPrjData.setTitle("成果");
        }
        for (ResumePubSimple pub : pubs) {
          Map<String, Object> map = new HashMap<String, Object>();
          if (Locale.US.equals(locale)) {
            title = pub.getEnTitle();
            if (StringUtils.isBlank(title)) {
              title = pub.getZhTitle();
            }
            briefDesc = pub.getBriefDescEn();
            if (StringUtils.isBlank(briefDesc)) {
              briefDesc = pub.getBriefDesc();
            }
          } else {
            title = pub.getZhTitle();
            if (StringUtils.isBlank(title)) {
              title = pub.getEnTitle();
            }
            briefDesc = pub.getBriefDesc();
            if (StringUtils.isBlank(briefDesc)) {
              briefDesc = pub.getBriefDescEn();
            }
          }
          authorNames = pub.getAuthorNames();
          // 构建author,title,briefDesc信息
          buildATB(map, authorNames, title, briefDesc);
          String linkUrl = "";
          PubIndexUrl pubIndexUrl = pubIndexUrlDao.get(pub.getPubId());
          if (pubIndexUrl != null && StringUtils.isNotBlank(pubIndexUrl.getPubIndexUrl())) {
            linkUrl = domainscm + "/" + ShortUrlConst.A_TYPE + "/" + pubIndexUrl.getPubIndexUrl();
          } else {
            linkUrl = domainscm + "/pubweb/outside/details?des3Id=" + ServiceUtil.encodeToDes3(pub.getPubId() + "");
          }
          map.put("linkUrl", linkUrl);
          list.add(map);
        }
      }
    }
    // 项目
    if (ResumeConstants.WORD_BODY_PRJ_STR.equals(type)) {
      List<Project> prjs = projectDao.getPrjs(form.getPsnId());
      if (CollectionUtils.isNotEmpty(prjs)) {
        if (Locale.US.equals(locale)) {
          pubPrjData.setTitle("Projects");
        } else {
          pubPrjData.setTitle("项目");
        }
        for (Project prj : prjs) {
          Map<String, Object> map = new HashMap<String, Object>();
          if (Locale.US.equals(locale)) {
            authorNames = prj.getAuthorNamesEn();
            if (StringUtils.isBlank(authorNames)) {
              authorNames = prj.getAuthorNames();
            }
            title = prj.getEnTitle();
            if (StringUtils.isBlank(title)) {
              title = prj.getZhTitle();
            }
            briefDesc = prj.getBriefDescEn();
            if (StringUtils.isBlank(briefDesc)) {
              briefDesc = prj.getBriefDesc();
            }
          } else {
            authorNames = prj.getAuthorNames();
            if (StringUtils.isBlank(authorNames)) {
              authorNames = prj.getAuthorNamesEn();
            }
            title = prj.getZhTitle();
            if (StringUtils.isBlank(title)) {
              title = prj.getEnTitle();
            }
            briefDesc = prj.getBriefDesc();
            if (StringUtils.isBlank(briefDesc)) {
              briefDesc = prj.getBriefDescEn();
            }
          }
          // 构建author,title,briefDesc信息
          buildATB(map, authorNames, title, briefDesc);
          String linkUrl = domainscm + "/scmwebsns/project/view?des3Id=" + ServiceUtil.encodeToDes3(prj.getId() + "");
          map.put("linkUrl", linkUrl);
          list.add(map);
        }
      }
    }
    pubPrjData.setObjectContent(list);
    leftDataMap.put("pubPrjData", pubPrjData);
    if (Locale.US.equals(locale)) {
      leftFtl = ResumeConstants.WORD_PUBPRJ_2003_EN;
    } else {
      leftFtl = ResumeConstants.WORD_PUBPRJ_2003_ZH;
    }
    return leftFtl;
  }

  /**
   * 构建author,title,briefDesc信息
   * 
   * @param map
   * @param authorNames
   * @param title
   * @param briefDesc
   */
  private void buildATB(Map<String, Object> map, String authorNames, String title, String briefDesc) {
    authorNames = XmlUtil.formateSymbolAuthors(title, authorNames);
    String author0 = "";
    String author1 = "";
    String author2 = "";
    if (authorNames.contains("<strong>") && authorNames.contains("</strong>")) {
      int start = authorNames.indexOf("<strong>");
      int startLength = "<strong>".length();
      int endLength = "</strong>".length();
      int end = authorNames.indexOf("</strong>");
      String startStr = authorNames.substring(0, start);
      if (StringUtils.isNotBlank(startStr)) {
        author0 = StringHtml.wordHandler(startStr);
      }
      String str = authorNames.substring(start + startLength, end);
      if (StringUtils.isNotBlank(str)) {
        author1 = StringHtml.wordHandler(str);
      }
      String endStr = authorNames.substring(end + endLength);
      if (StringUtils.isNotBlank(endStr)) {
        author2 = StringHtml.wordHandler(endStr);
      }
      author2 += ", ";
    } else {
      author2 = StringHtml.wordHandler(authorNames);
      if (StringUtils.isNotBlank(author2)) {
        author2 += ", ";
      }
    }
    map.put("author0", author0);
    map.put("author1", author1);
    map.put("author2", author2);
    title = StringEscapeUtils.unescapeHtml4(title);
    map.put("title", StringHtml.wordHandler(title));
    briefDesc = XmlUtil.formatBriefDesc(briefDesc);
    briefDesc = StringHtml.wordHandler(briefDesc);
    if (StringUtils.isNotBlank(briefDesc)) {
      briefDesc = ", " + briefDesc;
    }
    map.put("briefDesc", briefDesc);
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
      leftFtl = ResumeConstants.WORD_WORKEDU_2003_EN;
    } else {
      leftFtl = ResumeConstants.WORD_WORKEDU_2003_ZH;
    }
    return leftFtl;
  }

  /**
   * 构建个人简介/科技领域/关键词模板信息
   * 
   * @param locale
   * @param form
   * @param leftFtl
   * @param leftDataMap
   * @param type
   * @return
   */
  private String buildWordBodyTxt(Locale locale, PersonalResumeForm form, String leftFtl,
      Map<String, Object> leftDataMap, String type) {
    String txt = "";
    // 个人简介
    if (ResumeConstants.WORD_BODY_BRIEF_STR.equals(type)) {
      txt = personManager.getPersonBrief(form.getPsnId());
      if (StringUtils.isNotBlank(txt)) {
        if (Locale.US.equals(locale)) {
          leftDataMap.put("title", "About");
        } else {
          leftDataMap.put("title", "个人简介");
        }
      }
    }
    // 科技领域
    if (ResumeConstants.WORD_BODY_SCIENCEAREA_STR.equals(type)) {
      List<PsnScienceArea> psaList = scienceAreaService.findPsnScienceAreaList(form.getPsnId(), 1);
      if (CollectionUtils.isNotEmpty(psaList)) {
        if (Locale.US.equals(locale)) {
          leftDataMap.put("title", "Areas");
        } else {
          leftDataMap.put("title", "科技领域");
        }
        for (PsnScienceArea psa : psaList) {
          txt += psa.getShowScienceArea() + "; ";
        }
        txt = txt.substring(0, txt.length() - 2);
      }
    }
    // 关键词
    if (ResumeConstants.WORD_BODY_KEYWORDS_STR.equals(type)) {
      List<PsnDisciplineKey> keyList = psnDisciplineKeyService.findPsnKeyWords(form.getPsnId());
      if (CollectionUtils.isNotEmpty(keyList)) {
        if (Locale.US.equals(locale)) {
          leftDataMap.put("title", "Keywords");
        } else {
          leftDataMap.put("title", "关键词");
        }
        for (PsnDisciplineKey keyword : keyList) {
          txt += keyword.getKeyWords() + "; ";
        }
        txt = txt.substring(0, txt.length() - 2);
      }
    }
    leftDataMap.put("txt", StringHtml.wordHandler(txt));
    if (Locale.US.equals(locale)) {
      leftFtl = ResumeConstants.WORD_TXT_2003_EN;
    } else {
      leftFtl = ResumeConstants.WORD_TXT_2003_ZH;
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
    if (ResumeConstants.WORD_BODY_EDU_STR.equals(type)) {
      List<EducationHistory> eduList = educationHistoryDao.findByPsnId(form.getPsnId());
      if (CollectionUtils.isNotEmpty(eduList)) {
        if (Locale.US.equals(locale)) {
          workEduData.setTitle("Education");
        } else {
          workEduData.setTitle("教育经历");
        }
        for (EducationHistory edu : eduList) {
          StringBuilder item = new StringBuilder();
          insName = StringUtils.isBlank(edu.getInsName()) ? "" : edu.getInsName();
          department = StringUtils.isBlank(edu.getStudy()) ? "" : edu.getStudy();
          position = StringUtils.isBlank(edu.getDegreeName()) ? "" : edu.getDegreeName();
          fromYear = edu.getFromYear();
          toYear = edu.getToYear();
          fromMonth = edu.getFromMonth();
          toMonth = edu.getToMonth();
          isActive = edu.getIsActive() == null ? 0l : edu.getIsActive();
          item = buildItem(item, insName, department, position, fromYear, toYear, fromMonth, toMonth, isActive);
          items.add(item.toString());
        }
      }
    }
    if (ResumeConstants.WORD_BODY_WORK_STR.equals(type)) {
      List<WorkHistory> workList = workHistoryDao.findListByPersonId(form.getPsnId());
      if (CollectionUtils.isNotEmpty(workList)) {
        if (Locale.US.equals(locale)) {
          workEduData.setTitle("Work");
        } else {
          workEduData.setTitle("工作经历");
        }
        for (WorkHistory work : workList) {
          StringBuilder item = new StringBuilder();
          insName = StringUtils.isBlank(work.getInsName()) ? "" : work.getInsName();
          department = StringUtils.isBlank(work.getDepartment()) ? "" : work.getDepartment();
          position = StringUtils.isBlank(work.getPosition()) ? "" : work.getPosition();
          fromYear = work.getFromYear();
          toYear = work.getToYear();
          fromMonth = work.getFromMonth();
          toMonth = work.getToMonth();
          isActive = work.getIsActive() == null ? 0l : work.getIsActive();
          item = buildItem(item, insName, department, position, fromYear, toYear, fromMonth, toMonth, isActive);
          items.add(item.toString());
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
  private StringBuilder buildItem(StringBuilder item, String insName, String department, String position, Long fromYear,
      Long toYear, Long fromMonth, Long toMonth, Long isActive) {
    insName = StringHtml.wordHandler(insName);
    department = StringHtml.wordHandler(department);
    position = StringHtml.wordHandler(position);
    // 开始时间-结束时间
    if (fromYear != null) {
      item.append(fromYear);
      if (fromMonth != null) {
        item.append("/" + fromMonth);
      }
      if (toYear != null) {
        item.append(" - " + toYear);
        if (toMonth != null) {
          item.append("/" + toMonth);
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
        item.append(", " + insName);
      } else {
        item.append(insName);
      }
    }
    // 主修/部门
    if (StringUtils.isNotBlank(department)) {
      if (StringUtils.isNotBlank(item)) {
        item.append(", " + department);
      } else {
        item.append(department);
      }
    }
    // 学位/职称
    if (StringUtils.isNotBlank(position)) {
      if (StringUtils.isNotBlank(item)) {
        item.append(", " + position);
      } else {
        item.append(position);
      }
    }
    return item;
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
      Person person = personProfileDao.getPsnInfo(form.getPsnId());
      if (person != null) {
        // 头像
        String base64Avatars = "";
        // String avatars = "";
        // String[] arr =
        // FileNamePathParseUtil.generalDirWithFileName(form.getPsnId()
        // + ".png");
        // if (arr.length == 2) {
        // avatars = fileRoot + "/avatars" + arr[0] + arr[1];
        // base64Avatars = ImageBase64Encode.getBase64Encode(avatars);
        // }
        String avatars = person.getAvatars();
        if (StringUtils.isNotBlank(avatars)) {
          String[] arr = avatars.split("avatars");
          if (arr.length >= 2) {
            if (arr[1].contains("?")) {
              String[] split = arr[1].split("\\?");
              avatars = fileRoot + "/avatars" + split[0];
            } else {
              avatars = fileRoot + "/avatars" + arr[1];
            }
          }
          base64Avatars = ImageBase64Encode.getBase64Encode(avatars);
        }
        userDataMap.put("base64Avatars", base64Avatars);
        // 名称
        String info0 = buildPsnName(locale, person);
        userDataMap.put("info0", StringHtml.wordHandler(info0));
        // 机构+部门+职称
        String info1 = buildPsnTitle(person);
        userDataMap.put("info1", StringHtml.wordHandler(info1));
        // 邮件 电话 手机
        if (Locale.US.equals(locale)) {
          if (StringUtils.isNotBlank(person.getEmail())) {
            userDataMap.put("info2", "Email：" + StringHtml.wordHandler(person.getEmail()));
          }
          if (StringUtils.isNotBlank(person.getTel())) {
            userDataMap.put("info3", "Tel：" + StringHtml.wordHandler(person.getTel()));
          }
          if (StringUtils.isNotBlank(person.getMobile())) {
            userDataMap.put("info4", "Mobile：" + StringHtml.wordHandler(person.getMobile()));
          }
        } else {
          if (StringUtils.isNotBlank(person.getEmail())) {
            userDataMap.put("info2", "邮件：" + StringHtml.wordHandler(person.getEmail()));
          }
          if (StringUtils.isNotBlank(person.getTel())) {
            userDataMap.put("info3", "电话：" + StringHtml.wordHandler(person.getTel()));
          }
          if (StringUtils.isNotBlank(person.getMobile())) {
            userDataMap.put("info4", "手机：" + StringHtml.wordHandler(person.getMobile()));
          }
        }
      }
      // 主页
      PsnProfileUrl psnProfileUrl = psnProfileUrlDao.find(form.getPsnId());
      if (psnProfileUrl != null && StringUtils.isNotBlank(psnProfileUrl.getPsnIndexUrl())) {
        String info5 = domainscm + "/" + ShortUrlConst.P_TYPE + "/" + psnProfileUrl.getPsnIndexUrl();
        if (Locale.US.equals(locale)) {
          userDataMap.put("info5", "Profile：" + info5);
        } else {
          userDataMap.put("info5", "主页：" + info5);
        }
      }
      userDataMap.put("avatarName", "avatars.jpg");
      String userFtl = "";
      if (Locale.US.equals(locale)) {
        userFtl = ResumeConstants.WORD_USER_2003_EN;
      } else {
        userFtl = ResumeConstants.WORD_USER_2003_ZH;
      }
      Template userTemplate = wordFreemarkerConfiguration.getTemplate(userFtl, encoding);
      userXml = FreeMarkerTemplateUtils.processTemplateIntoString(userTemplate, userDataMap);
    } catch (Exception e) {
      logger.error("导出简历,构建头部信息出错", e);
    }
    return userXml;
  }

  /**
   * 构建 机构+部门+职称 信息
   * 
   * @param person
   * @return
   */
  private String buildPsnTitle(Person person) {
    String title = "";
    String insName = person.getInsName();
    String department = person.getDepartment();
    String position = person.getPosition();
    // 机构+部门
    if (StringUtils.isBlank(insName) || StringUtils.isBlank(department)) {
      title = (StringUtils.isBlank(insName) ? "" : insName) + (StringUtils.isBlank(department) ? "" : department);
    } else {
      title = insName + ", " + department;
    }
    // 职称
    if (StringUtils.isNotBlank(position)) {
      if (StringUtils.isNotBlank(title)) {
        title += ", " + position;
      } else {
        title = position;
      }
    }
    return title;
  }

  /**
   * 构建名称信息
   * 
   * @param locale
   * @param person
   * @return
   */
  private String buildPsnName(Locale locale, Person person) {
    String info0 = "";
    String name = person.getName();
    String eName = person.getEname();
    if (Locale.US.equals(locale)) {
      if (StringUtils.isBlank(eName)) {
        info0 = person.getName();
      } else {
        info0 = person.getEname();
      }
    } else {
      if (StringUtils.isBlank(name)) {
        info0 = person.getEname();
      } else {
        info0 = person.getName();
      }
    }
    return info0;
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
    String wordFtl = "";
    if (Locale.US.equals(locale)) {
      wordFtl = ResumeConstants.WORD_RESUME_2003_EN;
    } else {
      wordFtl = ResumeConstants.WORD_RESUME_2003_ZH;
    }
    Template wordTemplate = wordFreemarkerConfiguration.getTemplate(wordFtl, encoding);
    String wordXml = FreeMarkerTemplateUtils.processTemplateIntoString(wordTemplate, wordMap);
    return wordXml;
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
      if (Locale.US.equals(locale)) {
        headerDataMap.put("title", "ScholarMate: Connect people to innovate smarter.");
        headerFtl = ResumeConstants.WORD_HEADER_2003_EN;
      } else {
        headerDataMap.put("title", "科研之友 - 连接，让创新更高效");
        headerFtl = ResumeConstants.WORD_HEADER_2003_ZH;
      }
      Template headerTemplate = wordFreemarkerConfiguration.getTemplate(headerFtl, encoding);
      headerDataTmp = FreeMarkerTemplateUtils.processTemplateIntoString(headerTemplate, headerDataMap);
    } catch (Exception e) {
      logger.error("导出word简历,构建页眉信息出错", e);
    }
    return headerDataTmp;
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

}
