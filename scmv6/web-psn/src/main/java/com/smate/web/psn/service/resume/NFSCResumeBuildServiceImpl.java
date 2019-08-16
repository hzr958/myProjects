package com.smate.web.psn.service.resume;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.vo.ProjectInfo;
import com.smate.core.base.psn.dao.EducationHistoryDao;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.WorkHistoryDao;
import com.smate.core.base.psn.model.EducationHistory;
import com.smate.core.base.psn.model.WorkHistory;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.model.PubMemberInfo;
import com.smate.core.base.pub.model.PubMemberPO;
import com.smate.core.base.pub.vo.PubInfo;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.StringHtml;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.core.base.v8pub.restTemp.service.PubRestemplateService;
import com.smate.web.prj.xml.PrjXmlConstants;
import com.smate.web.prj.xml.PrjXmlDocument;
import com.smate.web.psn.dao.project.ProjectDao;
import com.smate.web.psn.dao.project.ScmPrjXmlDao;
import com.smate.web.psn.dao.resume.CVModuleInfoDao;
import com.smate.web.psn.dao.resume.PsnResumeDao;
import com.smate.web.psn.dao.resume.ResumeModuleDao;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.form.resume.PersonalResumeForm;
import com.smate.web.psn.model.consts.ResumeModuleConsts;
import com.smate.web.psn.model.newresume.CVModuleInfo;
import com.smate.web.psn.model.newresume.PsnResume;
import com.smate.web.psn.model.newresume.ResumeModule;
import com.smate.web.psn.model.project.ScmPrjXml;
import com.smate.web.psn.model.resume.CVEduInfo;
import com.smate.web.psn.model.resume.CVPrj;
import com.smate.web.psn.model.resume.CVPsnInfo;
import com.smate.web.psn.model.resume.CVWorkInfo;
import com.smate.web.psn.service.pub.PubMemberService;
import com.smate.web.psn.utils.LocaleStringUtils;
import com.smate.web.psn.utils.PubMemberNameUtils;

/**
 * 基金委简历信息构建
 * 
 * @author wsn
 *
 */
@Transactional(rollbackFor = Exception.class)
public class NFSCResumeBuildServiceImpl implements PsnResumeBuildService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonDao personDao;
  @Autowired
  private EducationHistoryDao educationHistoryDao;
  @Autowired
  private WorkHistoryDao workHistoryDao;
  @Autowired
  private PsnResumeDao psnResumeDao;
  @Autowired
  private CVModuleInfoDao cVModuleInfoDao;
  @Autowired
  private ResumeModuleDao resumeModuleDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private ScmPrjXmlDao scmPrjXmlDao;
  /*
   * @Autowired private CVScmPubXmlDao cVScmPubXmlDao;
   */
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PubMemberService pubMemberService;
  // @Autowired
  // private PubSimpleDao pubSimpleDao;
  @Resource(name = "NFSCWordService")
  private WordService NFSCWordService;
  /*
   * @Autowired private PubIndexUrlDao pubIndexUrlDao;
   */
  @Value("${domainscm}")
  private String domainScm;
  @Autowired
  private PubRestemplateService pubRestemplateService;

  @Override
  public void buildPsnResumeInfo(PersonalResumeForm form) throws Exception {
    if (form.getModuleId() != null) {
      switch (form.getModuleId()) {
        case ResumeModuleConsts.BASE_INFO:
          this.buildPsnBaseInfo(form);
          break;
        case ResumeModuleConsts.EDU_INFO:
          this.buildPsnEduInfo(form);
          break;
        case ResumeModuleConsts.WORK_INFO:
          this.buildPsnWorkInfo(form);
          break;
        case ResumeModuleConsts.PRJ_INFO:
          this.buildPsnResumePrjInfo(form);
          break;
        case ResumeModuleConsts.REPRESENT_PUB_INFO:
          this.buildPsnResumePubInfo(form);
          break;
        case ResumeModuleConsts.OTHER_PUB_INFO:
          this.buildPsnResumePubInfo(form);
          break;
        case ResumeModuleConsts.BRIEF_INFO:
          this.buildPsnBriefInfo(form);
          break;
        default:
          break;
      }
    }
  }

  @Override
  public void buildPsnBaseInfo(PersonalResumeForm form) throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null) {
      if (form.getResumeId() != null) {
        ResumeModule resumeModule =
            resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), ResumeModuleConsts.BASE_INFO);// 模块主表
        if (resumeModule != null && resumeModule.getStatus() != 1) {
          CVModuleInfo cvModuleInfo = cVModuleInfoDao.get(resumeModule.getModuleInfoId());// 模块信息表
          String psnInfoJsonStr = cvModuleInfo.getModuleInfo();
          CVPsnInfo cvPsnInfo = (CVPsnInfo) JacksonUtils.jsonObject(psnInfoJsonStr, CVPsnInfo.class);
          if (cvPsnInfo != null) {
            String psnInfoStr = getPsnBaseInfo(cvPsnInfo);
            form.setPsnBaseInfo(psnInfoStr);
          }
        }
        form.setIsShowMoule(resumeModule.getStatus().toString());
      }
    }
  }

  private void saveInitPsnBaseInfo(PersonalResumeForm form) {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null) {
      Person person = personDao.get(psnId);
      CVPsnInfo cvPsnInfo = new CVPsnInfo();
      cvPsnInfo.setName(person.getName() != null ? person.getName() : person.getEnName());
      cvPsnInfo.setInsInfo(person.getInsName());
      cvPsnInfo.setDegree(person.getPosition());
      cvPsnInfo.setDepartment(person.getDepartment());
      String psnInfoJsonStr = JacksonUtils.jsonObjectSerializerNoNull(cvPsnInfo);
      CVModuleInfo cvModuleInfo = new CVModuleInfo();
      cvModuleInfo.setModuleInfo(psnInfoJsonStr);
      cVModuleInfoDao.save(cvModuleInfo);// 更新信息表
      saveResumeModule(form.getResumeId(), cvModuleInfo.getId(), ResumeModuleConsts.BASE_INFO,
          ResumeModuleConsts.SEQ_CV_PSNINFO, ResumeModuleConsts.TITLE_CV_PSNINFO, 0);
    }
  }

  private String getPsnBaseInfo(CVPsnInfo cvPsnInfo) {
    String baseInfo = "";
    baseInfo = cvPsnInfo.getName();
    baseInfo += StringUtils.isBlank(cvPsnInfo.getInsInfo()) ? "" : ", " + cvPsnInfo.getInsInfo();
    baseInfo += StringUtils.isBlank(cvPsnInfo.getDepartment()) ? "" : ", " + cvPsnInfo.getDepartment();
    baseInfo += StringUtils.isBlank(cvPsnInfo.getDegree()) ? "" : ", " + cvPsnInfo.getDegree();
    return baseInfo;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void buildPsnWorkInfo(PersonalResumeForm form) throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null && form.getResumeId() != null) {
      ResumeModule resumeModule =
          resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), ResumeModuleConsts.WORK_INFO);
      if (resumeModule != null && resumeModule.getStatus() != 1) {
        CVModuleInfo cvModuleInfo = cVModuleInfoDao.get(resumeModule.getModuleInfoId());
        String workJsonStr = cvModuleInfo.getModuleInfo();
        List<CVWorkInfo> workInfoList =
            (List<CVWorkInfo>) JacksonUtils.jsonToCollection(workJsonStr, ArrayList.class, CVWorkInfo.class);
        form.setWorkMapList(getPsnWorkInfo(workInfoList));
      }
      form.setIsShowMoule(resumeModule.getStatus().toString());
    }

  }

  /**
   * 显示个人工作经历
   * 
   * @param educationHistoryList
   * @return
   */
  private List<Map<String, String>> getPsnWorkInfo(List<CVWorkInfo> workInfoList) {
    Collections.sort(workInfoList, Collections.reverseOrder());// 按时间逆序排序
    List<Map<String, String>> workMapList = new ArrayList<Map<String, String>>();
    for (CVWorkInfo work : workInfoList) {
      Map<String, String> workInfo = new HashMap<String, String>();
      workInfo.put("seqWork", work.getSeqWork().toString());
      String department = StringUtils.isBlank(work.getDepartment()) ? "" : work.getDepartment();
      String degreeName = StringUtils.isBlank(work.getDegreeName()) ? "" : work.getDegreeName();
      String insName = StringUtils.isBlank(work.getInsName()) ? "" : work.getInsName();
      String description = StringUtils.isBlank(work.getDescription()) ? null : work.getDescription();
      Long fromYear = work.getFromYear();
      Long toYear = work.getToYear();
      Long fromMonth = work.getFromMonth();
      Long toMonth = work.getToMonth();
      Long isActive = work.getIsActive() == null ? 0l : work.getIsActive();
      StringBuilder workDesc = new StringBuilder();
      if (fromYear != null && fromYear != 0) {
        workDesc.append(fromYear);
        if (fromMonth != null && fromMonth != 0) {
          workDesc.append("." + fromMonth);
        }
        if (toYear != null && toYear != 0) {
          workDesc.append(" - " + toYear);
          if (toMonth != null && toMonth != 0) {
            workDesc.append("." + toMonth);
          }
        } else {
          if (isActive == 1) {
            workDesc.append(LocaleStringUtils.getStringByLocale(" - to Present", " - 至今"));
          }
        }
      }
      if (StringUtils.isNotBlank(insName)) {
        workDesc.append(", " + insName);
      }
      if (StringUtils.isNotBlank(department)) {
        workDesc.append(", " + department);
      }
      if (StringUtils.isNotBlank(degreeName)) {
        workDesc.append(", " + degreeName);
      }
      workInfo.put("workinfo", workDesc.toString());
      if (description != null) {
        workInfo.put("description", description);
      }
      workMapList.add(workInfo);
    }
    return workMapList;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void buildPsnEduInfo(PersonalResumeForm form) throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null && form.getResumeId() != null) {
      ResumeModule resumeModule =
          resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), ResumeModuleConsts.EDU_INFO);
      if (resumeModule != null && resumeModule.getStatus() != 1) {
        CVModuleInfo cvModuleInfo = cVModuleInfoDao.get(resumeModule.getModuleInfoId());
        String eduJsonStr = cvModuleInfo.getModuleInfo();
        List<CVEduInfo> eduInfoList =
            (List<CVEduInfo>) JacksonUtils.jsonToCollection(eduJsonStr, ArrayList.class, CVEduInfo.class);
        form.setEduMapList(getPsnEduInfo(eduInfoList));// 设置个人的教育经历
      }
      form.setIsShowMoule(resumeModule.getStatus().toString());
    }
  }

  /**
   * 显示个人的教育经历
   * 
   * @param educationHistoryList
   * @return
   */
  private List<Map<String, String>> getPsnEduInfo(List<CVEduInfo> eduInfoList) {
    Collections.sort(eduInfoList, Collections.reverseOrder());// 按时间逆序排序
    List<Map<String, String>> eduMapList = new ArrayList<Map<String, String>>();
    if (eduInfoList != null) {
      for (CVEduInfo edu : eduInfoList) {
        if (edu != null) {
          Map<String, String> eduInfo = new HashMap<String, String>();
          eduInfo.put("seqEdu", edu.getSeqEdu() == null ? "0" : edu.getSeqEdu().toString());
          String insName = StringUtils.isBlank(edu.getInsName()) ? "" : edu.getInsName();
          String department = StringUtils.isBlank(edu.getDepartment()) ? "" : edu.getDepartment();
          String position = StringUtils.isBlank(edu.getDegreeName()) ? "" : edu.getDegreeName();
          String description = StringUtils.isBlank(edu.getDescription()) ? null : edu.getDescription();
          Long fromYear = edu.getFromYear();
          Long toYear = edu.getToYear();
          Long fromMonth = edu.getFromMonth();
          Long toMonth = edu.getToMonth();
          Long isActive = edu.getIsActive() == null ? 0l : edu.getIsActive();
          StringBuilder eduDesc = new StringBuilder();
          if (fromYear != null && fromYear != 0) {
            eduDesc.append(fromYear);
            if (fromMonth != null && fromMonth != 0) {
              eduDesc.append("." + fromMonth);
            }
            if (toYear != null && toYear != 0) {
              eduDesc.append(" - " + toYear);
              if (toMonth != null && toMonth != 0) {
                eduDesc.append("." + toMonth);
              }
            } else {
              if (isActive == 1) {
                eduDesc.append(LocaleStringUtils.getStringByLocale(" - to Present", " - 至今"));
              }
            }
          }
          if (StringUtils.isNotBlank(insName)) {
            eduDesc.append(", " + insName);
          }
          if (StringUtils.isNotBlank(department)) {
            eduDesc.append(", " + department);
          }
          if (StringUtils.isNotBlank(position)) {
            eduDesc.append(", " + position);
          }
          eduInfo.put("eduinfo", eduDesc.toString());
          if (description != null) {
            eduInfo.put("description", description);
          }
          eduMapList.add(eduInfo);
        }
      }
    }
    return eduMapList;
  }

  @Override
  public void buildPsnBriefInfo(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void buildPsnCredentialsInfo(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @SuppressWarnings("unchecked")
  @Override
  public void buildPsnResumePubInfo(PersonalResumeForm form) throws ServiceException {
    try {
      if (form.getPsnId() > 0 && form.getResumeId() != null && form.getModuleId() != null) {
        String jsonPubInfo =
            pubRestemplateService.psnResumePubInfo(form.getPsnId(), form.getResumeId(), form.getModuleId());
        if (StringUtils.isNotBlank(jsonPubInfo)) {
          List<PubInfo> pubList = JacksonUtils.jsonToCollection(jsonPubInfo, List.class, PubInfo.class);
          buildRepresentPubInfo(pubList, form.getPsnId());
          if (pubList != null) {
            for (PubInfo info : pubList) {
              info.setWordHrefSeq(form.getWordSeqStart() + 1);
              form.setWordSeqStart(form.getWordSeqStart() + 1);
            }
          }
          form.setPubInfos(pubList);
        }
      }
    } catch (Exception e) {
      logger.error("构建人员简历成果信息出错， resumeId = " + form.getResumeId() + ", moduleId=" + form.getModuleId(), e);
      throw new ServiceException(e);
    }

  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void buildPsnResumePrjInfo(PersonalResumeForm form) throws Exception {
    List<ProjectInfo> prjInfos = new ArrayList<ProjectInfo>();
    ResumeModule resumeModule =
        resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), form.getModuleId());
    if (resumeModule != null) {
      String moduleInfo = cVModuleInfoDao.getModuleInfo(resumeModule.getModuleInfoId());
      if (StringUtils.isNotBlank(moduleInfo)) {
        List<CVPrj> cVPrjList = JacksonUtils.jsonToCollection(moduleInfo, ArrayList.class, CVPrj.class);
        if (CollectionUtils.isNotEmpty(cVPrjList)) {
          for (CVPrj cvPrj : cVPrjList) {
            Project prj = projectDao.get(cvPrj.getPrjId());
            if (prj != null) {
              ProjectInfo prjInfo = new ProjectInfo();
              // Locale locale = LocaleContextHolder.getLocale();
              prjInfo.setPrjId(prj.getId());
              String showTitle = "";
              String agencyAndscheme = "";
              String showDate = "";
              // ===========================================
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
              // ===========================================
              // if (Locale.US.equals(locale)) {
              // if (StringUtils.isBlank(prj.getEnTitle())) {
              // showTitle = prj.getZhTitle();
              // } else {
              // showTitle = prj.getEnTitle();
              // }
              // if (StringUtils.isBlank(prj.getEnAgencyName())) {
              // agencyAndscheme = prj.getAgencyName();
              // } else {
              // agencyAndscheme = prj.getEnAgencyName();
              // }
              // } else {
              // if (StringUtils.isBlank(prj.getZhTitle())) {
              // showTitle = prj.getEnTitle();
              // } else {
              // showTitle = prj.getZhTitle();
              // }
              // if (StringUtils.isBlank(prj.getAgencyName())) {
              // agencyAndscheme = prj.getEnAgencyName();
              // } else {
              // agencyAndscheme = prj.getAgencyName();
              // }
              // }
              // ===========================================
              prjInfo.setAgencyAndScheme(StringUtils.trimToEmpty(agencyAndscheme));// 项目类别
              prjInfo.setExternalNo(StringUtils.trimToEmpty(prj.getExternalNo()));// 批准号
              prjInfo.setShowTitle(StringUtils.trimToEmpty(showTitle));// 名称
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
              prjInfo.setShowDate(StringUtils.trimToEmpty(showDate));// 研究起止年月
              if (prj.getAmount() != null) {
                prjInfo.setAmountAndUnit(prj.getAmount() + " " + StringUtils.trimToEmpty(prj.getAmountUnit()));// 资助金额
              }
              if ("01".equals(prj.getState())) {
                prjInfo.setPrjState("在研");
              } else if ("02".equals(prj.getState())) {
                prjInfo.setPrjState("已结题");
              } else {
                prjInfo.setPrjState("其他");// 项目状态
              }
              prjInfo.setPrjOwner("参加");
              ScmPrjXml scmPrjXml = scmPrjXmlDao.get(prj.getId());
              if (scmPrjXml != null) {
                PrjXmlDocument prjXmlDocument = new PrjXmlDocument(scmPrjXml.getPrjXml());
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
                        break;
                      }

                    } else {
                      if (person != null) {
                        String name = person.getName() == null ? "" : person.getName();
                        String ename = person.getEname() == null ? "" : person.getEname();
                        if (name.equals(zName) || name.equals(iName) || ename.equals(eName) || ename.equals(iName)) {
                          if ("1".equals(notifyAuthor)) {
                            prjInfo.setPrjOwner("主持");// 主持或参加
                          }
                          break;
                        }
                      }
                    }
                  }
                }
              }
              prjInfos.add(prjInfo);
            }
          }
        }
        // form.setCVPrjList(cVPrjList);
      }
    }
    form.setPrjInfo(prjInfos);
  }

  @Override
  public void extend(PersonalResumeForm form) throws Exception {
    // TODO Auto-generated method stub

  }

  @Override
  public void createPsnResume(PersonalResumeForm form) throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    List<PsnResume> cvList = psnResumeDao.queryPsnResumeList(psnId);
    if (CollectionUtils.isNotEmpty(cvList)) {
      if (cvList.size() >= 10) {
        form.setResultMsg("每个人最多10份简历");
        return;
      }
    }
    if (psnId != null && form.getResumeName() != null) {
      PsnResume psnResume = new PsnResume();
      psnResume.setCreateDate(new Date());
      psnResume.setOwnerPsnId(psnId);
      psnResume.setResumeName(form.getResumeName());
      psnResume.setResumeType(ResumeModuleConsts.NFSC_TYPE);
      psnResume.setUpdateDate(new Date());
      psnResumeDao.save(psnResume);
      form.setDes3ResumeId(Des3Utils.encodeToDes3(psnResume.getResumeId().toString()));
      saveEduAndWork(psnResume.getResumeId(), psnId);// 保存教育经历和工作经历
      saveInitPsnBaseInfo(form);
      saveResumeModule(psnResume.getResumeId(), cVModuleInfoDao.findNextCVModuleInfoId(), ResumeModuleConsts.PRJ_INFO,
          ResumeModuleConsts.SEQ_CV_PRJ, ResumeModuleConsts.TITLE_CV_PRJ, 0);
      saveResumeModule(psnResume.getResumeId(), cVModuleInfoDao.findNextCVModuleInfoId(),
          ResumeModuleConsts.REPRESENT_PUB_INFO, ResumeModuleConsts.SEQ_CV_REPRESENT_PUB,
          ResumeModuleConsts.TITLE_CV_REPRESENT_PUB, 0);
      saveResumeModule(psnResume.getResumeId(), cVModuleInfoDao.findNextCVModuleInfoId(),
          ResumeModuleConsts.OTHER_PUB_INFO, ResumeModuleConsts.SEQ_CV_OTHER_PUB, ResumeModuleConsts.TITLE_CV_OTHER_PUB,
          0);
    }

  }

  /**
   * 保存教育经历和工作经历到表CVModuleInfo和ResumeModule表
   * 
   * @param resumeId
   * @param psnId
   */
  private void saveEduAndWork(Long resumeId, Long psnId) {
    List<EducationHistory> educationHistoryList = educationHistoryDao.findEduInsByPsnId(psnId);// 查找个人教育经历
    Long edumoduleInfoId = saveInitEduModuleInfo(psnId, educationHistoryList);// 保存主页设置的教育经历到CVModuleInfo表并返回id
    saveResumeModule(resumeId, edumoduleInfoId, ResumeModuleConsts.EDU_INFO, ResumeModuleConsts.SEQ_CV_EDU,
        ResumeModuleConsts.TITLE_CV_EDU, 0);
    List<WorkHistory> workHistoryList = workHistoryDao.findWorkInsByPsnId(psnId);// 查找个人工作经历
    Long workModuleInfoId = saveInitWorkModuleInfo(psnId, workHistoryList);// 保存主页设置的工作经历到CVModuleInfo表并返回id
    saveResumeModule(resumeId, workModuleInfoId, ResumeModuleConsts.WORK_INFO, ResumeModuleConsts.SEQ_CV_WORK,
        ResumeModuleConsts.TITLE_CV_WORK, 0);

  }

  /**
   * 保存主页设置的工作经历到CVModuleInfo表
   */
  private Long saveInitWorkModuleInfo(Long psnId, List<WorkHistory> workHistoryList) {
    List<CVWorkInfo> cvWorkInfoList = new ArrayList<CVWorkInfo>();
    Integer seqWork = 1;
    for (WorkHistory work : workHistoryList) {
      CVWorkInfo workInfo = new CVWorkInfo();
      Long insId = work.getInsId();
      String department = StringUtils.isBlank(work.getDepartment()) ? "" : work.getDepartment();// 部门名称
      String degreeName = StringUtils.isBlank(work.getPosition()) ? "" : work.getPosition();// 学位名称
      String insName = StringUtils.isBlank(work.getInsName()) ? "" : work.getInsName();// 单位名称
      String description = StringUtils.isBlank(work.getDescription()) ? null : work.getDescription();// 描述
      Long fromYear = work.getFromYear();
      Long toYear = work.getToYear();
      Long fromMonth = work.getFromMonth();
      Long toMonth = work.getToMonth();
      Long isActive = work.getIsActive() == null ? 0l : work.getIsActive();
      workInfo.setInsId(insId);
      workInfo.setSeqWork(seqWork.toString());
      workInfo.setInsName(insName);
      workInfo.setDepartment(department);
      workInfo.setDegreeName(degreeName);
      workInfo.setDescription(description);
      workInfo.setFromYear(fromYear);
      if (fromMonth != null && fromMonth != 0) {
        workInfo.setFromMonth(fromMonth);
      }
      if (toYear != null && toYear != 0) {
        workInfo.setToYear(toYear);
      }
      if (toMonth != null && toMonth != 0) {
        workInfo.setToMonth(toMonth);
      }
      workInfo.setIsActive(isActive);
      cvWorkInfoList.add(workInfo);
      seqWork++;
    }
    String eduJson = JacksonUtils.listToJsonStr(cvWorkInfoList);
    CVModuleInfo cvModuleInfo = new CVModuleInfo();
    cvModuleInfo.setModuleInfo(eduJson);
    cVModuleInfoDao.save(cvModuleInfo);
    return cvModuleInfo.getId();
  }

  /**
   * 保存主页设置的教育经历到CVModuleInfo表
   */
  private Long saveInitEduModuleInfo(Long psnId, List<EducationHistory> educationHistoryList) {
    List<CVEduInfo> cvEduInfoList = new ArrayList<CVEduInfo>();
    Integer seqEdu = 1;
    for (EducationHistory edu : educationHistoryList) {
      CVEduInfo eduInfo = new CVEduInfo();
      Long insId = edu.getInsId();
      String degreeCode = StringUtils.isBlank(edu.getDegree()) ? null : edu.getDegree();
      String insName = StringUtils.isBlank(edu.getInsName()) ? "" : edu.getInsName();// 单位名称
      String department = StringUtils.isBlank(edu.getStudy()) ? "" : edu.getStudy();// 部门专业名称
      String degreeName = StringUtils.isBlank(edu.getDegreeName()) ? "" : edu.getDegreeName();// 学位名称
      String description = StringUtils.isBlank(edu.getDescription()) ? null : edu.getDescription();// 描述
      Long fromYear = edu.getFromYear();
      Long toYear = edu.getToYear();
      Long fromMonth = edu.getFromMonth();
      Long toMonth = edu.getToMonth();
      Long isActive = edu.getIsActive() == null ? 0l : edu.getIsActive();
      eduInfo.setSeqEdu(seqEdu.toString());
      eduInfo.setInsName(insName);
      eduInfo.setDepartment(department);
      eduInfo.setDegreeName(degreeName);
      eduInfo.setDescription(description);
      eduInfo.setFromYear(fromYear);
      eduInfo.setFromMonth(fromMonth);
      eduInfo.setToYear(toYear);
      eduInfo.setToMonth(toMonth);
      eduInfo.setIsActive(isActive);
      eduInfo.setInsId(insId);
      eduInfo.setDegreeCode(degreeCode);
      cvEduInfoList.add(eduInfo);
      seqEdu++;
    }
    String eduJson = JacksonUtils.listToJsonStr(cvEduInfoList);
    CVModuleInfo cvModuleInfo = new CVModuleInfo();
    cvModuleInfo.setModuleInfo(eduJson);
    cVModuleInfoDao.save(cvModuleInfo);
    return cvModuleInfo.getId();
  }

  /**
   * 保存记录到ResumeModule表
   */
  private void saveResumeModule(Long resumeId, Long moduleInfoId, Integer moduleId, Integer moduleSeq,
      String moduleTitle, Integer status) {
    ResumeModule resumeModule = new ResumeModule();
    resumeModule.setResumeId(resumeId);
    resumeModule.setModuleId(moduleId);
    resumeModule.setModuleSeq(moduleSeq);
    resumeModule.setModuleInfoId(moduleInfoId);
    resumeModule.setModuleTitle(moduleTitle);
    resumeModule.setUpdateTime(new Date());
    resumeModule.setStatus(status);
    resumeModuleDao.save(resumeModule);
  }

  @Override
  public void updateResumePrjInfo(PersonalResumeForm form) throws Exception {
    ResumeModule resumeModule =
        resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), form.getModuleId());
    if (StringUtils.isBlank(form.getAddedPrjIds())) {// 删除
      if (resumeModule != null) {
        if (resumeModule.getModuleInfoId() != null && resumeModule.getModuleInfoId() > 0l) {
          cVModuleInfoDao.delete(resumeModule.getModuleInfoId());
          resumeModuleDao.delete(resumeModule);
        }
      }
    } else {// 保存
      List<CVPrj> cvPrjList = new ArrayList<CVPrj>();
      String[] split = form.getAddedPrjIds().split(",");
      if (split != null) {
        int seq = 1;
        for (String prjId : split) {
          CVPrj cvPrj = new CVPrj();
          cvPrj.setPrjId(NumberUtils.toLong(prjId));
          cvPrj.setSeq(seq);
          cvPrjList.add(cvPrj);
          seq++;
        }
        if (resumeModule == null) {
          CVModuleInfo cvModuleInfo = new CVModuleInfo();
          cvModuleInfo.setModuleInfo(JacksonUtils.listToJsonStr(cvPrjList));
          cVModuleInfoDao.save(cvModuleInfo);
          resumeModule = new ResumeModule();
          resumeModule.setResumeId(form.getResumeId());
          resumeModule.setModuleId(ResumeModuleConsts.PRJ_INFO);
          resumeModule.setModuleSeq(ResumeModuleConsts.PRJ_INFO);
          resumeModule.setModuleTitle("科研项目");
          resumeModule.setModuleInfoId(cvModuleInfo.getId());
          resumeModule.setStatus(0);
        } else {
          // 保存简历模块信息表的内容
          CVModuleInfo cvModuleInfo = cVModuleInfoDao.get(resumeModule.getModuleInfoId());
          if (cvModuleInfo != null) {
            cvModuleInfo.setModuleInfo(JacksonUtils.listToJsonStr(cvPrjList));
            cVModuleInfoDao.save(cvModuleInfo);
          } else {
            cvModuleInfo = new CVModuleInfo();
            cvModuleInfo.setId(resumeModule.getModuleInfoId());
            cvModuleInfo.setModuleInfo(JacksonUtils.listToJsonStr(cvPrjList));
            // 此处设置了ID后，save操作最终执行的update语句，所以该实体类不会保存到数据库
            // cVModuleInfoDao.save(cvModuleInfo);
            cVModuleInfoDao.insertNewRecord(cvModuleInfo);
          }
        }
        resumeModule.setUpdateTime(new Date());
        resumeModuleDao.save(resumeModule);
      }
    }
    psnResumeDao.updatePsnResumeUpdateTime(form.getResumeId());
  }

  /**
   * 构建ProjectInfo对象
   * 
   * @param prj
   */
  private void buildPrjInfoByLanguage(Project prj) {
    ProjectInfo prjInfo = new ProjectInfo();
    prjInfo = prjInfo.buildProjectInfo(prj, prjInfo);
    if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
      prjInfo.setShowAuthorNames(
          StringUtils.isNotBlank(prjInfo.getAuthorNamesEn()) ? prjInfo.getAuthorNamesEn() : prjInfo.getAuthorNames());
      prjInfo.setShowTitle(StringUtils.isNotBlank(prjInfo.getEnTitle()) ? prjInfo.getEnTitle() : prjInfo.getZhTitle());
      prjInfo.setShowBriefDesc(
          StringUtils.isNotBlank(prjInfo.getBriefDescEn()) ? prjInfo.getBriefDescEn() : prjInfo.getBriefDesc());
    } else {
      prjInfo.setShowAuthorNames(
          StringUtils.isNotBlank(prjInfo.getAuthorNames()) ? prjInfo.getAuthorNames() : prjInfo.getAuthorNamesEn());
      prjInfo.setShowTitle(StringUtils.isNotBlank(prjInfo.getZhTitle()) ? prjInfo.getZhTitle() : prjInfo.getEnTitle());
      prjInfo.setShowBriefDesc(
          StringUtils.isNotBlank(prjInfo.getBriefDesc()) ? prjInfo.getBriefDesc() : prjInfo.getBriefDescEn());
    }
    prj.setPrjInfo(prjInfo);
  }

  @SuppressWarnings("unchecked")
  @Override
  public void queryPrjInfo(PersonalResumeForm form) throws Exception {
    projectDao.queryPrjList(form);
    List<Project> prjList = form.getPage().getResult();
    if (CollectionUtils.isNotEmpty(prjList)) {
      for (Project prj : prjList) {
        buildPrjInfoByLanguage(prj);
      }
    }
    form.getPage().setResult(prjList);
  }

  /**
   * 构建代表性成果信息格式
   * 
   * @param pubId
   * @return
   */
  private List<PubInfo> buildRepresentPubInfo(List<PubInfo> pubInfoList, Long psnId) throws ServiceException {
    String locale = LocaleContextHolder.getLocale().toString();
    try {
      for (PubInfo pub : pubInfoList) {
        Person psn = this.personProfileDao.getPsnAllName(psnId);
        String currentPsnName = "zh_CN".equals(locale) ? psn.getZhName() : psn.getEname();

        List<PubMemberPO> pubMemberList = pubMemberService.getPubMemberList(pub.getPubId());
        List<PubMemberInfo> pubMemberInfoList = new ArrayList<PubMemberInfo>();
        // 设置作者标识
        if (psnId != null) {
          for (PubMemberPO member : pubMemberList) {
            PubMemberInfo memberInfo = new PubMemberInfo();
            BeanUtils.copyProperties(memberInfo, member);
            if (memberInfo.getPsnId() != null && psnId.longValue() == memberInfo.getPsnId().longValue()) {
              StringBuffer authorFlag = new StringBuffer();
              if (memberInfo.getFirstAuthor() != null && memberInfo.getFirstAuthor() == 1) {
                authorFlag.append("1");
                if (memberInfo.getCommunicable() != null && memberInfo.getCommunicable() == 1) {
                  authorFlag.append(",2");
                }
              } else {
                if (memberInfo.getCommunicable() != null && memberInfo.getCommunicable() == 1) {
                  authorFlag.append("2");
                }
              }
              if (currentPsnName != null && currentPsnName.equals(memberInfo.getName())) {
                memberInfo.setOwner(1);
              }
              pub.setAuthorFlag(authorFlag.toString());
            }
            memberInfo.setName(StringHtml.wordHandler(memberInfo.getName()));
            pubMemberInfoList.add(memberInfo);
          }
        }
        pub.setMemberInfo(pubMemberInfoList);
        pub.setMemberList(pubMemberList);
        String authors = pub.getAuthorNames();
        String requestHead = Struts2Utils.getRequest().getHeader("User-Agent");
        if (pub.getPubType() == PublicationTypeEnum.AWARD) {
          String awardAuthorList = PubMemberNameUtils.getSNSPubMemberNameList(pubMemberList, currentPsnName);
          pub.setAwardAuthorList(awardAuthorList);
        }
        if (StringUtils.isNotBlank(requestHead) && requestHead.contains("Chrome")) {
          if (StringUtils.isNotBlank(authors)) {
            authors = authors.replaceAll("sup", "sub");
            pub.setAuthorNames(authors);
          }
          if (StringUtils.isNotBlank(pub.getAwardAuthorList())) {
            String authorList = pub.getAwardAuthorList().replaceAll("sup", "sub");
            pub.setAwardAuthorList(authorList);
          }
        }
        pub.setPubTypeName(this.findPubTypeName(pub.getPubType(), locale));
      }
    } catch (Exception e) {
      throw new ServiceException(e);
    }
    return pubInfoList;
  }

  /**
   * 构建代表性成果之外的成果信息
   * 
   * @param pubId
   * @return
   */
  /*
   * private String buildOtherPubInfo(Long pubId) {
   * 
   * return null; }
   */
  /**
   * 根据语言环境获取成果标题
   * 
   * @param locale
   * @return
   */

  /*
   * private String getPubTitleByLocale(String zhTitle, String enTitle, String locale) { if
   * ("zh_CN".equals(locale)) { return StringUtils.isNotBlank(zhTitle) ? zhTitle : enTitle; } else {
   * return StringUtils.isNotBlank(enTitle) ? enTitle : zhTitle; } }
   */
  /**
   * 获取论文类别名称
   * 
   * @param pubType
   * @param locale
   * @return
   */
  private String findPubTypeName(Integer pubType, String locale) {
    // TODO 国际化
    switch (pubType.intValue()) {
      case PublicationTypeEnum.AWARD:
        return "zh_CN".equals(locale) ? "奖励" : "Award";
      case PublicationTypeEnum.CONFERENCE_PAPER:
        return "zh_CN".equals(locale) ? "会议论文" : "Conference Paper";
      case PublicationTypeEnum.JOURNAL_ARTICLE:
        return "zh_CN".equals(locale) ? "期刊论文" : "Journal Article";
      case PublicationTypeEnum.PATENT:
        return "zh_CN".equals(locale) ? "专利" : "Patent";
      case PublicationTypeEnum.BOOK:
        return "zh_CN".equals(locale) ? "书/著作" : "Book/Monograph";
      case PublicationTypeEnum.BOOK_CHAPTER:
        return "zh_CN".equals(locale) ? "书籍章节" : "Book Chapter";
      case PublicationTypeEnum.THESIS:
        return "zh_CN".equals(locale) ? "学位论文" : "Thesis";
      case PublicationTypeEnum.STANDARD:
        return "zh_CN".equals(locale) ? "标准" : "Standard";
      case PublicationTypeEnum.SOFTWARE_COPYRIGHT:
        return "zh_CN".equals(locale) ? "软件著作权" : "Software Copyright";
      case PublicationTypeEnum.OTHERS:
        return "zh_CN".equals(locale) ? "其他" : "Others";
      default:
        return "";
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void getResumePrjList(PersonalResumeForm form) throws Exception {
    List<Project> prjList = new ArrayList<Project>();
    String prjIds = "";
    if (SecurityUtils.getCurrentUserId().equals(form.getPsnId())) {
      form.setModuleId(ResumeModuleConsts.PRJ_INFO);// 项目
      ResumeModule resumeModule =
          resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), form.getModuleId());
      if (resumeModule != null) {
        String moduleInfo = cVModuleInfoDao.getModuleInfo(resumeModule.getModuleInfoId());
        if (StringUtils.isNotBlank(moduleInfo)) {
          List<CVPrj> cVPrjList = JacksonUtils.jsonToCollection(moduleInfo, ArrayList.class, CVPrj.class);
          if (CollectionUtils.isNotEmpty(cVPrjList)) {
            for (CVPrj cvPrj : cVPrjList) {
              Project prj = projectDao.get(cvPrj.getPrjId());
              if (prj != null) {
                prjIds += cvPrj.getPrjId() + ",";
                buildPrjInfoByLanguage(prj);
                prjList.add(prj);
              }
            }
          }
        }
      }
    }
    form.setAddedPrjIds(prjIds);
    form.setPrjList(prjList);
  }

  @Override
  public File exportCVToWord(PersonalResumeForm form) throws ServiceException {
    try {
      String locale = LocaleContextHolder.getLocale().toString();
      Person psn = this.personProfileDao.getPsnAllName(SecurityUtils.getCurrentUserId());
      form.setCurrentPsnName("zh_CN".equals(locale) ? psn.getZhName() : psn.getEname());
      // 构建代表性成果信息
      form.setModuleId(ResumeModuleConsts.REPRESENT_PUB_INFO);
      this.buildPsnResumePubInfo(form);
      form.setRepresentPubList(form.getPubInfos());
      // 构建其他成果信息
      form.setModuleId(ResumeModuleConsts.OTHER_PUB_INFO);
      this.buildPsnResumePubInfo(form);
      form.setOtherPubList(form.getPubInfos());
      // 构建简历名称
      String resumeName = psnResumeDao.findCVName(form.getResumeId());
      if (StringUtils.isBlank(resumeName)) {
        form.setCvFileName(form.getCurrentPsnName() + "的简历.doc");
      } else {
        form.setCvFileName(resumeName + ".doc");
      }
      return NFSCWordService.exportWordFile(form);
    } catch (Exception e) {
      throw new ServiceException(e);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void updateEduInfo(PersonalResumeForm form) throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null && form.getResumeId() != null) {
      ResumeModule resumeModule =
          resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), ResumeModuleConsts.EDU_INFO);// 模块主表
      if (resumeModule != null) {
        CVModuleInfo cvModuleInfo = cVModuleInfoDao.get(resumeModule.getModuleInfoId());// 模块信息表
        String eduJsonStr = cvModuleInfo.getModuleInfo();
        List<CVEduInfo> eduInfoList =
            (List<CVEduInfo>) JacksonUtils.jsonToCollection(eduJsonStr, ArrayList.class, CVEduInfo.class);
        CVEduInfo cvEduInfo = form.getCvEduInfo();
        if (cvEduInfo != null && cvEduInfo.getSeqEdu() != null) {// 修改教育经历
          eduInfoList = updateEdu(eduInfoList, cvEduInfo);
        } else if (cvEduInfo != null) {// 添加教育经历
          Integer newSeqEdu = 1;
          if (CollectionUtils.isNotEmpty(eduInfoList)) {
            newSeqEdu = Integer.parseInt(eduInfoList.get(eduInfoList.size() - 1).getSeqEdu()) + 1;
          }
          cvEduInfo.setSeqEdu(newSeqEdu.toString());
          eduInfoList.add(cvEduInfo);
        }
        String eduJson = JacksonUtils.listToJsonStr(eduInfoList);
        cvModuleInfo.setModuleInfo(eduJson);
        cVModuleInfoDao.save(cvModuleInfo);// 更新信息表
      } else {
        List<CVEduInfo> eduInfoList = new ArrayList<CVEduInfo>();
        CVEduInfo cvEduInfo = form.getCvEduInfo();
        cvEduInfo.setSeqEdu("1");
        eduInfoList.add(cvEduInfo);
        String eduJson = JacksonUtils.listToJsonStr(eduInfoList);
        CVModuleInfo cvModuleInfo = new CVModuleInfo();
        cvModuleInfo.setModuleInfo(eduJson);
        cVModuleInfoDao.save(cvModuleInfo);// 信息表
        saveResumeModule(form.getResumeId(), cvModuleInfo.getId(), ResumeModuleConsts.EDU_INFO,
            ResumeModuleConsts.SEQ_CV_EDU, ResumeModuleConsts.TITLE_CV_EDU, 0);
      }
      resumeModule.setUpdateTime(new Date());
      psnResumeDao.updatePsnResumeUpdateTime(form.getResumeId());
      resumeModuleDao.save(resumeModule);

    }
  }

  /**
   * 修改教育经历
   */
  private List<CVEduInfo> updateEdu(List<CVEduInfo> eduInfoList, CVEduInfo cvEduInfo) {
    List<CVEduInfo> newEduInfoList = new ArrayList<CVEduInfo>();
    for (CVEduInfo edu : eduInfoList) {
      if (edu.getSeqEdu().equals(cvEduInfo.getSeqEdu())) {
        newEduInfoList.add(cvEduInfo);
      } else {
        newEduInfoList.add(edu);
      }
    }
    return newEduInfoList;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void updateWorkInfo(PersonalResumeForm form) throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null && form.getResumeId() != null) {
      ResumeModule resumeModule =
          resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), ResumeModuleConsts.WORK_INFO);// 模块主表
      if (resumeModule != null) {
        CVModuleInfo cvModuleInfo = cVModuleInfoDao.get(resumeModule.getModuleInfoId());// 模块信息表
        String workJsonStr = cvModuleInfo.getModuleInfo();
        List<CVWorkInfo> workInfoList =
            (List<CVWorkInfo>) JacksonUtils.jsonToCollection(workJsonStr, ArrayList.class, CVWorkInfo.class);
        CVWorkInfo cvWorkInfo = form.getCvWorkInfo();
        if (cvWorkInfo != null && cvWorkInfo.getSeqWork() != null) {// 修改教育经历
          workInfoList = updateWork(workInfoList, cvWorkInfo);
        } else if (cvWorkInfo != null) {// 添加教育经历
          Integer newSeqWork = 1;
          if (CollectionUtils.isNotEmpty(workInfoList)) {
            newSeqWork = Integer.parseInt(workInfoList.get(workInfoList.size() - 1).getSeqWork()) + 1;
          }
          cvWorkInfo.setSeqWork(newSeqWork.toString());
          workInfoList.add(cvWorkInfo);
        }
        String eduJson = JacksonUtils.listToJsonStr(workInfoList);
        cvModuleInfo.setModuleInfo(eduJson);
        cVModuleInfoDao.save(cvModuleInfo);// 更新信息表
      } else {
        List<CVWorkInfo> cvWorkInfoList = new ArrayList<CVWorkInfo>();
        CVWorkInfo cvWorkInfo = form.getCvWorkInfo();
        cvWorkInfo.setSeqWork("1");
        cvWorkInfoList.add(cvWorkInfo);
        String workJson = JacksonUtils.listToJsonStr(cvWorkInfoList);
        CVModuleInfo cvModuleInfo = new CVModuleInfo();
        cvModuleInfo.setModuleInfo(workJson);
        cVModuleInfoDao.save(cvModuleInfo);// 信息表
        saveResumeModule(form.getResumeId(), cvModuleInfo.getId(), ResumeModuleConsts.WORK_INFO,
            ResumeModuleConsts.SEQ_CV_WORK, ResumeModuleConsts.TITLE_CV_WORK, 0);
      }
      resumeModule.setUpdateTime(new Date());
      psnResumeDao.updatePsnResumeUpdateTime(form.getResumeId());
      resumeModuleDao.save(resumeModule);

    }
  }

  /**
   * 修改工作经历
   */
  private List<CVWorkInfo> updateWork(List<CVWorkInfo> workInfoList, CVWorkInfo cvWorkInfo) {
    List<CVWorkInfo> newWorkInfoList = new ArrayList<CVWorkInfo>();
    for (CVWorkInfo work : workInfoList) {
      if (work.getSeqWork().equals(cvWorkInfo.getSeqWork())) {
        newWorkInfoList.add(cvWorkInfo);
      } else {
        newWorkInfoList.add(work);
      }
    }
    return newWorkInfoList;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void getEduInfo(PersonalResumeForm form) throws Exception {
    if (form.getResumeId() != null) {
      ResumeModule resumeModule =
          resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), ResumeModuleConsts.EDU_INFO);// 模块主表
      if (resumeModule != null) {
        CVModuleInfo cvModuleInfo = cVModuleInfoDao.get(resumeModule.getModuleInfoId());// 模块信息表
        String eduJsonStr = cvModuleInfo.getModuleInfo();
        List<CVEduInfo> eduInfoList =
            (List<CVEduInfo>) JacksonUtils.jsonToCollection(eduJsonStr, ArrayList.class, CVEduInfo.class);
        if (eduInfoList != null) {
          for (CVEduInfo edu : eduInfoList) {
            if (edu != null && edu.getSeqEdu().toString().equals(form.getSeqEdu())) {
              if (edu.getFromMonth() == null || edu.getFromMonth() == 0) {
                edu.setFromMonth(null);
              }
              if (edu.getToYear() == null || edu.getToYear() == 0) {
                edu.setToYear(null);
              }
              if (edu.getToMonth() == null || edu.getToMonth() == 0) {
                edu.setToMonth(null);
              }
              form.setCvEduInfo(edu);
            }
          }
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void deleteEduInfo(PersonalResumeForm form) throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null && form.getResumeId() != null) {
      ResumeModule resumeModule =
          resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), ResumeModuleConsts.EDU_INFO);// 模块主表
      if (resumeModule != null) {
        CVModuleInfo cvModuleInfo = cVModuleInfoDao.get(resumeModule.getModuleInfoId());// 模块信息表
        String eduJsonStr = cvModuleInfo.getModuleInfo();
        List<CVEduInfo> eduInfoList =
            (List<CVEduInfo>) JacksonUtils.jsonToCollection(eduJsonStr, ArrayList.class, CVEduInfo.class);
        if (form.getSeqEdu() != null && eduInfoList != null) {// 修改教育经历
          Iterator<CVEduInfo> it = eduInfoList.iterator();
          while (it.hasNext()) {
            CVEduInfo edu = it.next();
            if (edu.getSeqEdu().toString().equals(form.getSeqEdu())) {
              it.remove();
            }
          }
        }
        String eduJson = JacksonUtils.listToJsonStr(eduInfoList);
        cvModuleInfo.setModuleInfo(eduJson);
        cVModuleInfoDao.save(cvModuleInfo);// 更新信息表
        resumeModule.setUpdateTime(new Date());
        resumeModuleDao.save(resumeModule);
      }

    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void getWorkInfo(PersonalResumeForm form) throws Exception {
    if (form.getResumeId() != null) {
      ResumeModule resumeModule =
          resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), ResumeModuleConsts.WORK_INFO);// 模块主表
      if (resumeModule != null) {
        CVModuleInfo cvModuleInfo = cVModuleInfoDao.get(resumeModule.getModuleInfoId());// 模块信息表
        String workJsonStr = cvModuleInfo.getModuleInfo();
        List<CVWorkInfo> workInfoList =
            (List<CVWorkInfo>) JacksonUtils.jsonToCollection(workJsonStr, ArrayList.class, CVWorkInfo.class);
        if (workInfoList != null) {
          for (CVWorkInfo work : workInfoList) {
            if (work != null && work.getSeqWork().toString().equals(form.getSeqWork())) {
              if (work.getFromMonth() == null || work.getFromMonth() == 0) {
                work.setFromMonth(null);
              }
              if (work.getToYear() == null || work.getToYear() == 0) {
                work.setToYear(null);
              }
              if (work.getToMonth() == null || work.getToMonth() == 0) {
                work.setToMonth(null);
              }
              form.setCvWorkInfo(work);
            }

          }
        }
      }
    }

  }

  @SuppressWarnings("unchecked")
  @Override
  public void deleteWorkInfo(PersonalResumeForm form) throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null && form.getResumeId() != null) {
      ResumeModule resumeModule =
          resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), ResumeModuleConsts.WORK_INFO);// 模块主表
      if (resumeModule != null) {
        CVModuleInfo cvModuleInfo = cVModuleInfoDao.get(resumeModule.getModuleInfoId());// 模块信息表
        String workJsonStr = cvModuleInfo.getModuleInfo();
        List<CVWorkInfo> workInfoList =
            (List<CVWorkInfo>) JacksonUtils.jsonToCollection(workJsonStr, ArrayList.class, CVWorkInfo.class);
        if (form.getSeqWork() != null && workInfoList != null) {// 修改教育经历
          Iterator<CVWorkInfo> it = workInfoList.iterator();
          while (it.hasNext()) {
            CVWorkInfo work = it.next();
            if (work.getSeqWork().toString().equals(form.getSeqWork())) {
              it.remove();
            }
          }
        }
        String WorkJson = JacksonUtils.listToJsonStr(workInfoList);
        cvModuleInfo.setModuleInfo(WorkJson);
        cVModuleInfoDao.save(cvModuleInfo);// 更新信息表
        resumeModule.setUpdateTime(new Date());
        resumeModuleDao.save(resumeModule);
      }

    }
  }

  @Override
  public void editpsninfo(PersonalResumeForm form) throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null && form.getResumeId() != null) {
      ResumeModule resumeModule =
          resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), ResumeModuleConsts.BASE_INFO);// 模块主表
      if (resumeModule != null) {
        CVModuleInfo cvModuleInfo = cVModuleInfoDao.get(resumeModule.getModuleInfoId());// 模块信息表
        CVPsnInfo newCvPsnInfo = form.getCvPsnInfo();
        if (newCvPsnInfo != null && newCvPsnInfo.getName() != null) {// 修改教育经历
          String psnInfoJsonStr = JacksonUtils.jsonObjectSerializerNoNull(newCvPsnInfo);
          cvModuleInfo.setModuleInfo(psnInfoJsonStr);
          cVModuleInfoDao.save(cvModuleInfo);// 更新信息表
        }
      } else {
        CVPsnInfo cvPsnInfo = form.getCvPsnInfo();
        if (cvPsnInfo != null && cvPsnInfo.getName() != null) {// 修改教育经历
          String psnInfoJsonStr = JacksonUtils.jsonObjectSerializerNoNull(cvPsnInfo);
          CVModuleInfo cvModuleInfo = new CVModuleInfo();
          cvModuleInfo.setModuleInfo(psnInfoJsonStr);
          cVModuleInfoDao.save(cvModuleInfo);// 更新信息表
          saveResumeModule(form.getResumeId(), cvModuleInfo.getId(), ResumeModuleConsts.BASE_INFO,
              ResumeModuleConsts.SEQ_CV_PSNINFO, ResumeModuleConsts.TITLE_CV_PSNINFO, 0);
        }

      }
      resumeModule.setUpdateTime(new Date());
      resumeModuleDao.save(resumeModule);
      psnResumeDao.updatePsnResumeUpdateTime(form.getResumeId());
      form.setPsnBaseInfo(getPsnBaseInfo(form.getCvPsnInfo()));
    }
  }

  @Override
  public void getpsninfo(PersonalResumeForm form) throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null && form.getResumeId() != null) {
      ResumeModule resumeModule =
          resumeModuleDao.findResumeModuleByResumeIdAndModuleId(form.getResumeId(), ResumeModuleConsts.BASE_INFO);// 模块主表
      if (resumeModule != null) {
        CVModuleInfo cvModuleInfo = cVModuleInfoDao.get(resumeModule.getModuleInfoId());// 模块信息表
        String psnInfoJsonStr = cvModuleInfo.getModuleInfo();
        CVPsnInfo cvPsnInfo = (CVPsnInfo) JacksonUtils.jsonObject(psnInfoJsonStr, CVPsnInfo.class);
        form.setCvPsnInfo(cvPsnInfo);
      }
    }
  }

  @Override
  public void editCvName(PersonalResumeForm form) throws Exception {
    Long psnId = SecurityUtils.getCurrentUserId();
    if (psnId != null && form.getResumeId() != null) {
      PsnResume psnResume = psnResumeDao.get(form.getResumeId());
      if (psnResume != null && StringUtils.isNotBlank(form.getResumeName())) {
        psnResume.setResumeName(form.getResumeName());
        psnResumeDao.save(psnResume);
        psnResumeDao.updatePsnResumeUpdateTime(form.getResumeId());
      }
    }
  }
}
