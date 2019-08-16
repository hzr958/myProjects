package com.smate.web.prj.service.project.impl;

import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.codehaus.plexus.util.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.connector.util.BatchJobUtil;
import com.smate.core.base.consts.service.ConstDisciplineService;
import com.smate.core.base.enums.LikeStatusEnum;
import com.smate.core.base.exception.NoPermissionException;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.project.model.Project;
import com.smate.core.base.project.model.ProjectStatistics;
import com.smate.core.base.project.service.ProjectService;
import com.smate.core.base.psn.consts.PsnCnfConst;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigPrjDao;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.psn.model.psncnf.PsnConfigPrj;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.statistics.PsnStatisticsUtils;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.dao.consts.ConstDictionaryDao;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.service.consts.ConstDictionaryManage;
import com.smate.core.base.utils.string.MapBuilder;
import com.smate.core.base.utils.struts2.Struts2MoveUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;
import com.smate.web.fund.recommend.dao.DynamicAwardPsnDao;
import com.smate.web.fund.recommend.dao.DynamicAwardResDao;
import com.smate.web.fund.service.recommend.CategoryMapBaseService;
import com.smate.web.prj.dao.project.AwardStatisticsDao;
import com.smate.web.prj.dao.project.SettingPrjFormDao;
import com.smate.web.prj.dao.project.SnsProjectQueryDao;
import com.smate.web.prj.dao.wechat.ProjectStatisticsDao;
import com.smate.web.prj.exception.ProjectNotExistException;
import com.smate.web.prj.form.ProjectOptForm;
import com.smate.web.prj.model.common.AwardStatistics;
import com.smate.web.prj.model.common.DynamicAwardPsn;
import com.smate.web.prj.model.common.DynamicAwardRes;
import com.smate.web.prj.model.common.SettingPrjForm;
import com.smate.web.prj.service.project.PrjFulltextService;
import com.smate.web.prj.service.project.SnsPrjOptService;
import com.smate.web.prj.service.project.SnsPrjXmlService;
import com.smate.web.prj.xml.PrjXmlConstants;
import com.smate.web.prj.xml.PrjXmlDocument;

/**
 * 项目操作相关服务实现类
 *
 * @author houchuanjie
 * @date 2018年3月15日 下午6:12:38
 */
@Service("snsPrjOptService")
@Transactional(rollbackOn = Exception.class)
public class SnsPrjOptServiceImpl implements SnsPrjOptService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  BatchJobsService batchJobsService;
  @Resource(name = "batchJobsContextFactory")
  private BatchJobsFactory batchJobsFactory;
  @Autowired
  private SnsPrjXmlService snsPrjXmlService;
  @Autowired
  private ConstDisciplineService constDisciplineService;
  @Autowired
  private ConstDictionaryManage constDictionaryManage;
  @Autowired
  private DynamicAwardResDao dynamicAwardResDao;
  @Autowired
  private DynamicAwardPsnDao dynamicAwardPsnDao;
  @Autowired
  private PersonDao personDao;
  @Autowired
  private ProjectStatisticsDao projectStatisticsDao;
  @Autowired
  private AwardStatisticsDao awardStatisticsDao;
  @Autowired
  private SnsProjectQueryDao snsProjectQueryDao;
  @Autowired
  private ProjectService projectService;
  @Autowired
  private PrjFulltextService prjFulltextService;
  @Autowired
  private PsnCnfService psnCnfService;
  @Autowired
  private SettingPrjFormDao SettingPrjFormDao;
  @Autowired
  private ConstDictionaryDao constDictionaryDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Autowired
  private PsnConfigDao psnConfigDao;
  @Autowired
  private PsnConfigPrjDao psnConfigPrjDao;
  @Autowired
  private CategoryMapBaseService categoryMapBaseService;

  @Override
  public void bulidEditFormData(ProjectOptForm prjOptForm)
      throws ProjectNotExistException, ServiceException, NoPermissionException {
    if (NumberUtils.isNotZero(prjOptForm.getPrjId())) {
      snsPrjXmlService.loadPrjXml(prjOptForm);
      if (StringUtils.isBlank(prjOptForm.getPrjXml())) {
        throw new ProjectNotExistException();
      }
      // prjOptForm.setDisName(getDisName(prjOptForm.getPrjXml()));
      prjOptForm.setDisName(getAreaName(prjOptForm.getPrjXml()));
      prjOptForm.setPrjMoneyType(constDictionaryManage.getConstByGategory("prj_money_unit"));
    } else {
      throw new ProjectNotExistException();
    }
  }

  @Override
  public void saveEditFormData(ProjectOptForm form, Map paramsMap)
      throws ProjectNotExistException, ServiceException, NoPermissionException {
    Set set = paramsMap.keySet();
    set.forEach(s -> {
      // System.out.println(s.toString() +"="+((String[])paramsMap.get(s))[0]);
    });
    dealParamsMap(paramsMap);
    Assert.notNull(form);
    Long prjId = form.getPrjId();
    try {
      if (NumberUtils.isNotZero(prjId)) {
        Project project = projectService.getProject(prjId);
        // 同步更新xml，project
        PrjXmlDocument prjXmlDoc = snsPrjXmlService.syncUpdatePrjXml(project, paramsMap);
        // 保存项目
        projectService.saveProject(project);
        // 同步项目信息到群组
        // ....
        // ....
        // 更新项目全文附件信息
        Long fulltextFileId = NumberUtils.toLong(project.getFulltextFileId());
        if (NumberUtils.isNullOrZero(fulltextFileId)) {
          prjFulltextService.deleteIfExist(project.getId());
        } else {
          prjFulltextService.saveOrUpdate(project.getId(), fulltextFileId);
        }
        // 更新项目权限信息
        updatePsnCnfPrj(prjXmlDoc, project.getId());
        //
        this.refreshPsnSolrInfoByTask(SecurityUtils.getCurrentUserId());
      } else {
        prjId = snsPrjXmlService.addPrjXml(prjId, paramsMap);
        form.setId(prjId);
      }
    } catch (ProjectNotExistException e) {
      logger.error("保存项目出错！该项目不存在！", e);
      throw e;
    } catch (ServiceException e) {
      logger.error("保存项目出错！prjId={}", prjId, e);
      throw e;
    }
  }

  // 处理一些参数
  private void dealParamsMap(Map paramsMap) {
    String[] fulltextUrl = (String[]) paramsMap.get("/prj_fulltext/@fulltext_url");
    if (fulltextUrl != null && fulltextUrl.length > 0 && "http://".equals(fulltextUrl[0].toString())) {
      fulltextUrl[0] = "";
      paramsMap.put("/prj_fulltext/@fulltext_url", fulltextUrl);
    }
  }

  /**
   * 更新公开项目数
   */
  private Integer updatePsnStatistics(Long psnId, Long prjId, Integer openPrjSum, String authority) {
    Long cnfId = psnConfigDao.getPsnConfId(psnId);
    PsnConfigPrj psPrj = psnConfigPrjDao.getBycnfIdAndPrjId(cnfId, prjId);
    if (StringUtils.isNotBlank(authority)) {
      if (psPrj.getAnyUser().intValue() != Integer.parseInt(authority)) {// 如果是公开项目
        if (!("7".equals(authority))) {
          openPrjSum = openPrjSum - 1; // 公开项目数-1
        } else {
          openPrjSum = openPrjSum + 1; // 公开项目数+1
        }

      }
    }
    return (openPrjSum.intValue() < 0 ? 0 : openPrjSum);
  }

  /**
   * 更新个人项目权限配置
   *
   * @param prjXmlDoc
   * @param prjId
   * @author houchuanjie
   * @date 2018年3月23日 下午12:42:37
   */
  private void updatePsnCnfPrj(PrjXmlDocument prjXmlDoc, Long prjId) {
    try {
      // 更新项目权限信息，个人项目权限设置
      String authority = prjXmlDoc.getXmlNodeAttribute(PrjXmlConstants.PRJ_META_XPATH, "authority");
      PsnStatistics psnStatistics = psnStatisticsDao.get(SecurityUtils.getCurrentUserId());
      if (psnStatistics != null) {
        Integer openPrjSum =
            updatePsnStatistics(SecurityUtils.getCurrentUserId(), prjId, psnStatistics.getOpenPrjSum(), authority);
        // 属性为null的保存为0
        psnStatistics.setOpenPrjSum(openPrjSum);
        psnStatistics.setPsnId(SecurityUtils.getCurrentUserId());
        PsnStatisticsUtils.buildZero(psnStatistics);
        psnStatisticsDao.save(psnStatistics);
      }
      Integer anyUser = NumberUtils.parseInt(authority, PsnCnfConst.ALLOWS);
      // 构造权限对象
      PsnConfigPrj cnfPrj = new PsnConfigPrj();
      cnfPrj.getId().setPrjId(prjId);
      cnfPrj.setAnyUser(anyUser);
      cnfPrj.setAnyView(cnfPrj.getAnyUser());
      psnCnfService.save(SecurityUtils.getCurrentUserId(), cnfPrj);// 保存权限
    } catch (Exception e) {
      logger.error("更新项目权限信息出错！prjId={}", prjId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 获取学科关键词名字
   *
   * @param prjXml
   * @return disName
   * @throws ServiceException
   * @author houchuanjie
   * @date 2018年3月20日 上午9:28:34
   */
  private String getDisName(String prjXml) throws ServiceException {
    Assert.notNull(prjXml);
    try {
      Locale locale = LocaleContextHolder.getLocale();
      // 获取到disId后调用constDisciplineService#getDisciplineName方法获取学科领域名称，获取不到的话，则根据语言返回默认名称
      String disName = Optional.ofNullable(prjXml).map(xml -> {
        try {
          return DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
          logger.error("转换项目xml时发生异常！", e);
          return null;
        }
      }).map(doc -> doc.getRootElement()).map(root -> root.selectSingleNode("/data/project/@discipline"))
          .map(node -> NumberUtils.parseLong(node.getStringValue(), null))
          .flatMap(disId -> constDisciplineService.getDisciplineName(disId, locale))
          .orElse(Locale.US.equals(locale) ? "Select a discipline" : "选择学科");
      return disName;
    } catch (Exception e) {
      logger.error("获取学科关键词名称时出错！");
      throw new ServiceException(e);
    }
  }

  /**
   * 获取领域mingc
   * 
   * @param prjXml
   * @return
   * @throws ServiceException
   */
  private String getAreaName(String prjXml) throws ServiceException {
    Assert.notNull(prjXml);
    try {
      Locale locale = LocaleContextHolder.getLocale();
      // 获取到disId后调用constDisciplineService#getDisciplineName方法获取学科领域名称，获取不到的话，则根据语言返回默认名称
      String disName = Optional.ofNullable(prjXml).map(xml -> {
        try {
          return DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
          logger.error("转换项目xml时发生异常！", e);
          return null;
        }
      }).map(doc -> doc.getRootElement()).map(root -> root.selectSingleNode("/data/project/@areaId"))
          .map(node -> NumberUtils.parseLong(node.getStringValue(), null))
          .flatMap(disId -> categoryMapBaseService.getCategoryName(disId, locale))
          .orElse(Locale.US.equals(locale) ? "Select a discipline" : "选择学科");
      return disName;
    } catch (Exception e) {
      logger.error("获取学科关键词名称时出错！");
      throw new ServiceException(e);
    }
  }

  @Override
  public String prjAddAward(ProjectOptForm form) throws Exception {
    MapBuilder mb = MapBuilder.getInstance();
    // 1.更新赞资源信息
    Long resId = form.getId();
    int resType = form.getResType();
    int resNode = 1;//

    DynamicAwardRes dynamicAwardRes = updateAwardRes(resId, resType, resNode);

    // 2.更新人员赞信息
    Long awarderPsnId = SecurityUtils.getCurrentUserId();
    Person person = personDao.get(awarderPsnId);
    DynamicAwardPsn dynamicAwardPsn = updateAwardPsn(person, dynamicAwardRes.getAwardId());

    // 3.更新项目统计表信息
    updateProjectStatistics(resId, dynamicAwardRes.getAwardTimes());

    // 4.更新赞操作统计表
    updateAwardStatistics(dynamicAwardRes, person, dynamicAwardPsn);

    // 5.返回数据
    mb.put("result", "success"); // 点赞成功
    mb.put("awardCount", dynamicAwardRes.getAwardTimes()); // 返回赞的数量
    mb.put("awardStatus", dynamicAwardPsn.getStatus()); // 返回赞的状态 1 赞 0 取消赞

    return mb.getJson();
  }

  @Override
  public String prjCancelAward(ProjectOptForm form) throws Exception {
    DynamicAwardPsn dynamicAwardPsn = null;
    MapBuilder mb = MapBuilder.getInstance();
    Long resId = form.getId();
    int resType = form.getResType();
    int resNode = form.getResNode();
    Long awarderPsnId = SecurityUtils.getCurrentUserId();
    // 更新资源赞次数
    DynamicAwardRes dynamicAwardRes = dynamicAwardResDao.getDynamicAwardRes(resId, resType, resNode);
    Person person = personDao.get(awarderPsnId);
    if (dynamicAwardRes == null) {
      logger.error("取消赞时未查找到" + resType + "," + resNode + "," + resId);
    } else {
      Long awardTimes = dynamicAwardRes.getAwardTimes();
      Long updateTimes = awardTimes.longValue() == 0 ? 0 : awardTimes - 1;
      dynamicAwardRes.setAwardTimes(updateTimes);
      // 保存资源赞数
      dynamicAwardResDao.save(dynamicAwardRes);
      // 更新人员赞信息的状态 ，0表示取消赞
      dynamicAwardPsnDao.updateAwardStatus(awarderPsnId, dynamicAwardRes.getAwardId(), 0);
      // 更新项目统计表赞的信息
      updateProjectStatistics(resId, updateTimes);
      // 更新赞操作统计表
      dynamicAwardPsn = dynamicAwardPsnDao.getDynamicAwardPsn(awarderPsnId, dynamicAwardRes.getAwardId());
      updateAwardStatistics(dynamicAwardRes, person, dynamicAwardPsn);
    }
    // 返回数据
    mb.put("result", "success"); // 点赞成功
    mb.put("awardCount", dynamicAwardRes.getAwardTimes()); // 返回赞的数量
    mb.put("awardStatus", dynamicAwardPsn.getStatus()); // 返回赞的状态 1 赞 0 取消赞
    return mb.getJson();
  }

  public DynamicAwardRes updateAwardRes(Long resId, int resType, int resNode) {
    try {
      DynamicAwardRes dynamicAwardRes = dynamicAwardResDao.getDynamicAwardRes(resId, resType, resNode);
      if (dynamicAwardRes == null) {
        // 说明是此资源第一次被赞
        dynamicAwardRes = new DynamicAwardRes();
        dynamicAwardRes.setResId(resId);
        dynamicAwardRes.setResType(resType);
        dynamicAwardRes.setResNode(resNode);
        dynamicAwardRes.setAwardTimes(1l);
        dynamicAwardRes.setUpdateDate(new Date());
      } else {
        // 将该资源的赞数加1即可
        dynamicAwardRes.setAwardTimes(dynamicAwardRes.getAwardTimes() + 1l);
      }
      // 保存攒资源信息
      dynamicAwardResDao.save(dynamicAwardRes);
      return dynamicAwardRes;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public DynamicAwardPsn updateAwardPsn(Person person, Long awardId) {
    DynamicAwardPsn dynamicAwardPsn = dynamicAwardPsnDao.getDynamicAwardPsn(person.getPersonId(), awardId);
    if (dynamicAwardPsn == null) {
      dynamicAwardPsn = new DynamicAwardPsn();
    }
    dynamicAwardPsn.setAwardId(awardId);
    dynamicAwardPsn.setAwarderPsnId(person.getPersonId());
    dynamicAwardPsn.setAwarderName(getPsnName(person, "zh_CN"));
    dynamicAwardPsn.setAwarderEnName(getPsnName(person, "en_US"));
    dynamicAwardPsn.setAwarderAvatar(person.getAvatars());
    dynamicAwardPsn.setAwardDate(new Date());
    dynamicAwardPsn.setStatus(1);// 表示是赞
    dynamicAwardPsnDao.save(dynamicAwardPsn);

    return dynamicAwardPsn;
  }

  public void updateProjectStatistics(Long prjId, Long awardCount) {
    ProjectStatistics statistics = projectStatisticsDao.get(prjId);
    if (statistics != null) {
      statistics.setAwardCount(awardCount.intValue());
    } else {
      statistics = new ProjectStatistics();
      statistics.setProjectId(prjId);
      statistics.setAwardCount(awardCount.intValue());
    }
    projectStatisticsDao.save(statistics);
  }

  public void updateAwardStatistics(DynamicAwardRes dynamicAwardRes, Person person, DynamicAwardPsn dynamicAwardPsn) {
    Long psnId = person.getPersonId();
    // Long actionKey = dynamicAwardRes.getAwardId();
    // 2018-07-02 AJB 调整
    Long actionKey = dynamicAwardRes.getResId();
    Integer actionType = 4;
    LikeStatusEnum status = LikeStatusEnum.valueOf(dynamicAwardPsn.getStatus());
    AwardStatistics awardStatistics = awardStatisticsDao.getAwardStatistics(psnId, actionKey, actionType);
    if (Objects.isNull(awardStatistics)) {
      awardStatistics = new AwardStatistics();
      awardStatistics.setPsnId(psnId); // 赞的人的id
      Long awardPsnId = snsProjectQueryDao.get(dynamicAwardRes.getResId()).getPsnId();
      awardStatistics.setAwardPsnId(awardPsnId); // 被赞人的id
      awardStatistics.setCreateDate(new Date()); // 创建赞的时间
      awardStatistics.setFormateDate(new Date().getTime());
      awardStatistics.setActionType(actionType); // 赞类型
      awardStatistics.setActionKey(actionKey); // 赞资源id
      awardStatistics.setAction(status); // 赞状态
      awardStatistics.setIp(Struts2Utils.getRemoteAddr()); // iP地址
    } else {
      // 更新IP地址和赞状态
      awardStatistics.setIp(Struts2Utils.getRemoteAddr());
      awardStatistics.setAction(status); // 赞状态
    }
    awardStatisticsDao.save(awardStatistics);
  }

  public String getPsnName(Person person, String locale) {
    String psnName = "";
    if ("zh_CN".equals(locale)) {
      psnName = person.getName();
      if (StringUtils.isBlank(psnName)) {
        psnName = person.getFirstName() + " " + person.getLastName();
      }
    } else {
      psnName = person.getFirstName() + " " + person.getLastName();
      if (StringUtils.isBlank(person.getFirstName()) && StringUtils.isBlank(person.getLastName())) {
        psnName = person.getName();
      }
    }

    return psnName;
  }

  @Override
  public void bulidEnterFormData(ProjectOptForm form) throws Exception {
    Long userId = SecurityUtils.getCurrentUserId();
    SettingPrjForm settingPrjForm = SettingPrjFormDao.get(ServiceConstants.SCHOLAR_PUB_FORM_ID);
    PrjXmlDocument xmlDoc = new PrjXmlDocument();
    // meta节点信息
    Node node = xmlDoc.getPrjMeta();
    Element elem = node != null ? (Element) node : xmlDoc.createElement(PrjXmlConstants.PRJ_META_XPATH);
    elem.addAttribute("record_ins_id", String.valueOf(SecurityUtils.getCurrentInsId()));
    elem.addAttribute("create_psn_id", String.valueOf(userId));
    elem.addAttribute("tmpl_form", settingPrjForm.getTmpFolder());
    elem.addAttribute("record_from", "0"); // 录入
    elem.addAttribute("record_node_id", String.valueOf(1));

    Person person = personDao.get(userId);
    Element prjMembers = Optional.ofNullable((Element) xmlDoc.getPrjMember())
        .orElse(xmlDoc.createElement(PrjXmlConstants.PRJ_MEMBERS_XPATH));
    Element prj_member = prjMembers.addElement("prj_member");
    prj_member.addAttribute("seq_no", "1");
    prj_member.addAttribute("member_psn_name", person.getZhName());
    prj_member.addAttribute("member_psn_name_en", person.getEnName());
    prj_member.addAttribute("owner", "1");
    prj_member.addAttribute("notify_author", "1");
    prj_member.addAttribute("member_psn_id", userId.toString());
    prj_member.addAttribute("des3_member_psn_id", Des3Utils.encodeToDes3(userId.toString()));
    prj_member.addAttribute("email", person.getEmail());
    prj_member.addAttribute("ins_name1", person.getInsName());
    if (person.getInsId() != null) {
      prj_member.addAttribute("ins_id1", person.getInsId().toString());
    }

    /*
     * prjMembers.addAttribute("ins_name1", value); prjMembers.addAttribute("ins_id1", value);
     * prjMembers.addAttribute("ins_name2", value); prjMembers.addAttribute("ins_id2", value);
     * prjMembers.addAttribute("ins_name3", value); prjMembers.addAttribute("ins_id3", value);
     * prjMembers.addAttribute("ins_name4", value); prjMembers.addAttribute("ins_id4", value);
     */

    form.setPrjXml(xmlDoc.getXmlString());
    form.setPrjMoneyType(constDictionaryDao.findConstByCategory("prj_money_unit"));

    // 清除上一页数据
    Struts2MoveUtils.reomvePreUrl(Struts2Utils.getSession());
  }

  @Override
  public void refreshPsnSolrInfoByTask(Long psnId) {
    try {
      BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.REFRESH_PSN_SOLR_INFO,
          BatchJobUtil.getPsnSolrRefreshContext(psnId.toString(), "refreshSolrInfo"), BatchWeightEnum.A.toString());
      batchJobsService.saveJob(job);
    } catch (Exception e) {
      logger.error("保存更新人员solr信息任务记录出错， psnId = " + psnId, e);
    }
  }

  @Override
  public void deletePsnSolrInfoByTask(Long psnId) {
    try {
      BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.REFRESH_PSN_SOLR_INFO,
          BatchJobUtil.getPsnSolrRefreshContext(psnId.toString(), "deleteSolrInfo"), BatchWeightEnum.A.toString());
      batchJobsService.saveJob(job);
    } catch (Exception e) {
      logger.error("保存更新人员solr信息任务记录出错， psnId = " + psnId, e);
    }
  }
}
