package com.smate.web.psn.service.profile;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.batch.connector.enums.BatchOpenCodeEnum;
import com.smate.center.batch.connector.enums.BatchWeightEnum;
import com.smate.center.batch.connector.factory.BatchJobsFactory;
import com.smate.center.batch.connector.model.job.BatchJobs;
import com.smate.center.batch.connector.service.job.BatchJobsService;
import com.smate.center.batch.connector.util.BatchJobUtil;
import com.smate.core.base.consts.dao.ConstDisciplineDao;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.psncnf.PsnConfigDao;
import com.smate.core.base.psn.dto.profile.Personal;
import com.smate.core.base.psn.enums.PsnCnfEnum;
import com.smate.core.base.psn.model.psncnf.PsnConfig;
import com.smate.core.base.psn.model.psncnf.PsnConfigBrief;
import com.smate.core.base.psn.model.psncnf.PsnConfigContact;
import com.smate.core.base.psn.model.psncnf.PsnConfigExpertise;
import com.smate.core.base.psn.model.psncnf.PsnConfigList;
import com.smate.core.base.psn.model.psncnf.PsnConfigMoudle;
import com.smate.core.base.psn.model.psncnf.PsnConfigPosition;
import com.smate.core.base.psn.service.psncnf.PsnCnfService;
import com.smate.core.base.psn.utils.PsnCnfUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.service.psn.PsnSolrInfoModifyService;
import com.smate.web.psn.dao.open.OpenUserUnionDao;
import com.smate.web.psn.dao.profile.PsnDisciplineDao;
import com.smate.web.psn.dao.profile.PsnDisciplineKeyDao;
import com.smate.web.psn.dao.profile.PsnUpdateDiscLogDao;
import com.smate.web.psn.dao.project.ProjectDao;
import com.smate.web.psn.dao.psnrefreshinfo.PsnRefreshUserInfoDao;
import com.smate.web.psn.dao.pub.PubSimpleDao;
import com.smate.web.psn.dao.pub.PublicationDao;
import com.smate.web.psn.dao.rcmdsync.RcmdSyncPsnInfoDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.exception.PsnInfoDaoException;
import com.smate.web.psn.model.profile.PsnUpdateDiscLog;
import com.smate.web.psn.model.psninfo.PsnRefreshUserInfo;
import com.smate.web.psn.model.rcmdsync.RcmdSyncPsnInfo;

/**
 * 个人专长、研究领域服务接口实现
 * 
 * @author Administrator
 *
 */
@Service("personalManager")
@Transactional(rollbackFor = Exception.class)
public class PersonalManagerImpl implements PersonalManager {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private static final String DELETE_OPERATE_TYPE = "deleteSolrInfo";
  private static final String REFRESH_OPERATE_TYPE = "refreshSolrInfo";
  @Autowired
  private PsnCnfService psnCnfService;

  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;

  @Autowired
  private KeywordIdentificationService identificationService;

  @Autowired
  private PsnSolrInfoModifyService psnSolrInfoModifyService;
  @Autowired
  private PsnUpdateDiscLogDao psnUpdateDiscLogDao;

  @Autowired
  private RcmdSyncPsnInfoDao rcmdSyncPsnInfoDao;
  @Autowired
  private PsnRefreshUserInfoDao psnRefreshUserInfoDao;

  @Autowired
  private PersonManager personManager;

  @Autowired
  private PsnDisciplineDao psnDisciplineDao;

  @Autowired
  private ConstDisciplineDao constDisciplineDao;
  @Autowired
  BatchJobsService batchJobsService;
  @Resource(name = "batchJobsContextFactory")
  private BatchJobsFactory batchJobsFactory;
  @Autowired
  private PsnConfigDao psnConfigDao;
  @Autowired
  private WorkHistoryService workHistoryService;
  @Autowired
  private EducationHistoryService educationHistoryService;
  @Autowired
  private PubSimpleDao pubSimpleDao;
  @Autowired
  private ProjectDao projectDao;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String restfulUrl;
  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;

  @Override
  public int savePersonal(Personal form, Integer anyUser) throws PsnException {
    try {
      PsnConfigExpertise cnfExpertise = new PsnConfigExpertise();
      cnfExpertise.setAnyUser(anyUser);
      String strDisc = form.getStrDisc();
      boolean hasText = StringUtils.isNotBlank(strDisc);
      // 查看权限 & 数据内容有无
      cnfExpertise.setAnyView(cnfExpertise.getAnyUser() & PsnCnfUtils.convertAnyView(hasText));
      psnCnfService.save(form.getPsnId(), cnfExpertise);
      int permission = 0;// 将权限改为研究领域的权限
      if (anyUser != null) {
        permission = anyUser;
        switch (permission) {
          case 7:
            permission = 0;// 所有人可见
            break;
          case 6:
            permission = 1;// 好友可见
            break;
          case 4:
            permission = 2;// 本人可见
            break;
        }
      }
      form.setPermission(permission);
    } catch (Exception e) {
      throw new PsnException(e);
    }
    return this.savePersonal(form);
  }

  /**
   * 个人专长修改，保存个人专长.
   */
  @SuppressWarnings({"rawtypes", "unused"})
  @Override
  public int savePersonal(Personal form) throws PsnException {
    try {
      int editType = 0;
      Long psnId = form.getPsnId();
      List<String> keywordsList = new ArrayList<String>();
      if (!StringUtils.isBlank(form.getStrDisc()) || form.getKeyList() != null) {
        String strDisc = form.getStrDisc();
        List jList = (List) JacksonUtils.jsonObject(strDisc);
        if (jList != null && jList.size() > 0) {

          // 添加数据
          List<Long> discIds = new ArrayList<Long>();
          for (int i = 0; i < jList.size(); i++) {
            Map map = (Map) jList.get(i);

            if (map.containsKey("keys") && form.getKeyList() == null) {// 关键词
              String key = map.get("keys").toString();
              if (key.isEmpty()) {

              } else {
                editType = 2; // 关键词，更新啦，状态要改变
                keywordsList.add(key);
                Integer keyResult = this.psnDisciplineKeyDao.saveKeys(key, psnId, form.getPermission());
                identificationService.recordDropRmKeyword(psnId, key);
                // 如果成功保存
                if (keyResult == 1) {
                  Integer permission = form.getPermission();
                  if (permission == null)
                    permission = 0;
                  // 且权限不为自己，则保存新增日志，用于发送邮件
                  if (permission != 2 && StringUtils.isNotBlank(key)) { // 新增日志表
                    PsnUpdateDiscLog log = new PsnUpdateDiscLog();
                    log.setPsnId(psnId);
                    log.setCreateDate(new Date());
                    log.setStatus(0);
                    log.setKeyWords(key);
                    psnUpdateDiscLogDao.savePsnUpdateDiscLog(log);
                  }
                }
              }
            }
          }
        }
      }

      int returnFlag = 1;

      if (keywordsList.size() > 0) {// 更新关键词
        // 同步人员熟悉关键词到基准库
        returnFlag = 2;
      } else {// 更新学科领域
        personManager.refreshComplete(psnId);
        // 更新人员solr信息
        this.refreshPsnSolrInfoByTask(psnId);
        // 同步用户熟悉的领域
        this.syncPsnDicipline(psnId);
      }
      // 标记人员关键词更新
      if (editType == 2) {
        RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(form.getPsnId());
        PsnRefreshUserInfo psnRefInfo = psnRefreshUserInfoDao.get(form.getPsnId());
        if (rsp == null) {
          rsp = new RcmdSyncPsnInfo(form.getPsnId());
        }
        if (psnRefInfo == null) {
          psnRefInfo = new PsnRefreshUserInfo(form.getPsnId());
        }
        rsp.setKwztFlag(1);
        psnRefInfo.setKwZt(1);
        rcmdSyncPsnInfoDao.save(rsp);
        psnRefreshUserInfoDao.save(psnRefInfo);
        // 更新人员solr信息
        this.refreshPsnSolrInfoByTask(psnId);
      }

      if (StringUtils.isNotBlank(form.getStrDisc()) || CollectionUtils.isNotEmpty(form.getKeyList())) {
        try {
          // TODO
          /*
           * // 修改个人专长动态 JSONObject jsonObject = new JSONObject(); //JacksonUtils.jsonObject(jsonStr); if
           * (editType == 2) { jsonObject.accumulate("resType", DynamicConstant.RES_TYPE_RESEARCH);
           * jsonObject.accumulate("sameFlag", DynamicConstant.RES_TYPE_RESEARCH + "_" +
           * SecurityUtils.getCurrentUserNodeId() + "_" + form.getPsnId());
           * jsonObject.accumulate("permission", form.getPermission() == null ? 0 : form.getPermission());
           * dynamicProduceEditKeywordService.produceDynamic(jsonObject.toString()); }
           */
        } catch (Exception e) {
          logger.error("添加修改学科领域或关键词动态出现异常：", e);
        }
      }
      return returnFlag;

    } catch (Exception e) {
      logger.error("保存个人专长出错", e);
      throw new PsnException(e);
    }
  }

  @Override
  public void syncPsnDicipline(Long psnId) throws PsnException {

    try {
      // TODO 发送MQ
      List<Long> discIds = this.psnDisciplineDao.getPsnDiscId(psnId);
      // 发送更新到机构，用于统计
      /*
       * if (discIds == null || discIds.size() == 0) { 用户删除了
       * psnDiscCategoryProducer.sendPsnDiscCategoryToRol(psnId, null); } else {// 存在 List<Long>
       * disccategory = this.constDisciplineDao.getDiscCategory(discIds);
       * psnDiscCategoryProducer.sendPsnDiscCategoryToRol(psnId, disccategory); }
       */
    } catch (Exception e) {
      logger.error("同步用户熟悉的领域", e);
      throw new PsnException(e);
    }

  }

  /**
   * 获取刷新用户信息完整度的数据.
   * 
   * @param psnId
   * @return
   * @throws PsnException
   */
  @Override
  public boolean isPsnDiscExit(Long psnId) throws PsnInfoDaoException {

    try {
      return psnDisciplineKeyDao.countPsnDisciplineKey(psnId) > 0l;
    } catch (PsnInfoDaoException e) {
      logger.error("获取刷新用户信息完整度的数据", e);
      throw new PsnInfoDaoException(e);
    }
  }

  @Override
  public void refreshPsnSolrInfoByTask(Long psnId) {
    try {
      BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.REFRESH_PSN_SOLR_INFO,
          BatchJobUtil.getPsnSolrRefreshContext(psnId.toString(), REFRESH_OPERATE_TYPE), BatchWeightEnum.A.toString());
      batchJobsService.saveJob(job);
    } catch (Exception e) {
      logger.error("保存更新人员solr信息任务记录出错， psnId = " + psnId, e);
    }
  }

  @Override
  public void deletePsnSolrInfoByTask(Long psnId) {
    try {
      BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.REFRESH_PSN_SOLR_INFO,
          BatchJobUtil.getPsnSolrRefreshContext(psnId.toString(), DELETE_OPERATE_TYPE), BatchWeightEnum.A.toString());
      batchJobsService.saveJob(job);
    } catch (Exception e) {
      logger.error("保存更新人员solr信息任务记录出错， psnId = " + psnId, e);
    }
  }

  @Override
  public void initPsnConfigInfoByTask(Long psnId) {
    try {
      if (psnId != null && psnId > 0) {
        PsnConfig psnConfig = psnCnfService.get(new PsnConfig(psnId));
        if (psnConfig == null) {
          psnConfig = new PsnConfig(psnId);
          // 先清空旧数据，再重建数据------暂时如果psn_config表为空，清空的逻辑就没有意义了
          // 因为清空是通过cnf_id来清空数据的，psn_config为空就找不到对应的cnf_id了
          Long allMoudle = Long.valueOf(PsnCnfEnum.ALL.toString());
          Long dirtyAct = Long.valueOf(PsnCnfEnum.DIRTY.toString());
          psnConfig.setRuns(allMoudle | dirtyAct);
          psnConfigDao.save(psnConfig);
          BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.REFRESH_PSN_CONFIG_INFO,
              BatchJobUtil.getContext(psnId.toString()), BatchWeightEnum.A.toString());
          batchJobsService.saveJob(job);
        } else if (psnConfig.getStatus() != 0) {
          // 判断其他表数据是否有缺失的，有的话也要重建下
          Long dataAct = this.findPsnConfigNeedRefreshData(PsnCnfEnum.ALL, psnId, psnConfig.getCnfId());
          if (dataAct != 0) {
            Long dirtyAct = Long.valueOf(PsnCnfEnum.DIRTY.toString());
            psnConfig.setRuns(dataAct | dirtyAct);
            psnConfig.setStatus(0);
            psnConfigDao.save(psnConfig);
            BatchJobs job = batchJobsFactory.createBatchJob(BatchOpenCodeEnum.REFRESH_PSN_CONFIG_INFO,
                BatchJobUtil.getContext(psnId.toString()), BatchWeightEnum.A.toString());
            batchJobsService.saveJob(job);
          }
        }
      }
    } catch (Exception e) {
      logger.error("保存更新人员配置信息任务记录出错， psnId = " + psnId, e);
    }
  }

  /**
   * 检查人员信息配置
   * 
   * @param PsnCnfAct
   * @return
   */
  @Override
  public Long findPsnConfigNeedRefreshData(PsnCnfEnum PsnCnfAct, Long psnId, Long cnfId) throws PsnException {
    Long act = 0L;
    try {
      switch (PsnCnfAct) {
        case ALL:
          // 检查所有配置
          act = this.checkPsnAllConfig(psnId, cnfId, act);
          break;
        case BRIEF:
          // 判断人员简介配置
          act = this.checkPsnBriefConfig(psnId, cnfId, act);
          break;
        case EXPERTISE:
          // 判断关键词和科技领域配置
          act = this.checkPsnExpertiseConfig(psnId, cnfId, act);
          break;
        case WORK:
          // 判断工作经历配置
          act = this.checkPsnWorkConfig(psnId, cnfId, act);
          break;
        case EDU:
          // 判断教育经历配置
          act = this.checkPsnEduConfig(psnId, cnfId, act);
          break;
        case PUB:
          // 判断成果配置
          // act = this.checkPsnPubConfig(psnId, cnfId, act);
          break;
        case PRJ:
          // 判断项目配置
          act = this.checkPsnPrjConfig(psnId, cnfId, act);
          break;
        case CONTACT:
          // 判断联系方式配置
          act = this.checkPsnContactConfig(psnId, cnfId, act);
          break;
        case TAUGHT:
          // 判断所教课程配置
          break;
        case POSITION:
          act = this.checkPsnConfigPosition(psnId, cnfId, act);
          // 判断职称配置
          break;
        default:
          break;
      }
    } catch (Exception e) {
      logger.error("检查人员配置信息出错， psnId = " + psnId + ", cnfId = " + cnfId + ", act = " + act, e);
      throw new PsnException(e);
    }
    return act;
  }

  // 判断人员简介配置
  private Long checkPsnBriefConfig(Long psnId, Long cnfId, Long act) throws PsnException {
    try {
      PsnConfigBrief briefCnf = psnCnfService.get(new PsnConfigBrief(cnfId));
      if (briefCnf == null) {
        act = act | Long.valueOf(PsnCnfEnum.BRIEF.toString());
      }
    } catch (Exception e) {
      logger.error("校验人员简介配置信息出错，psnId = " + psnId + ", cnfId = " + cnfId, e);
      throw new PsnException(e);
    }
    return act;
  }

  // 校验人员研究领域配置信息
  private Long checkPsnExpertiseConfig(Long psnId, Long cnfId, Long act) throws PsnException {
    try {
      PsnConfigExpertise expertiseCnf = psnCnfService.get(new PsnConfigExpertise(cnfId));
      if (expertiseCnf == null) {
        act = act | Long.valueOf(PsnCnfEnum.EXPERTISE.toString());
      }
    } catch (Exception e) {
      logger.error("校验人员研究领域配置信息出错，psnId = " + psnId + ", cnfId = " + cnfId, e);
      throw new PsnException(e);
    }
    return act;
  }

  // 校验人员工作经历配置信息
  private Long checkPsnWorkConfig(Long psnId, Long cnfId, Long act) throws PsnException {
    try {
      if (workHistoryService.isWorkHistoryExit(psnId) && workHistoryService.hasPsnConfigWorkLost(psnId, cnfId)) {
        act = act | Long.valueOf(PsnCnfEnum.WORK.toString());
      }
    } catch (Exception e) {
      logger.error("校验人员工作经历配置信息出错，psnId = " + psnId + ", cnfId = " + cnfId, e);
      throw new PsnException(e);
    }
    return act;
  }

  // 校验人员教育经历配置信息
  private Long checkPsnEduConfig(Long psnId, Long cnfId, Long act) throws PsnException {
    try {
      if (educationHistoryService.isEduHistoryExit(psnId) && educationHistoryService.hasEduConfigLost(psnId, cnfId)) {
        act = act | Long.valueOf(PsnCnfEnum.EDU.toString());
      }
    } catch (Exception e) {
      logger.error("校验人员教育经历配置信息出错，psnId = " + psnId + ", cnfId = " + cnfId, e);
      throw new PsnException(e);
    }
    return act;
  }

  // 校验人员成果配置信息(表已弃用)
  @Deprecated
  private Long checkPsnPubConfig(Long psnId, Long cnfId, Long act) throws PsnException {
    try {
      if (publicationDao.hasPsnConfigPubLost(psnId, cnfId)) {
        act = act | Long.valueOf(PsnCnfEnum.PUB.toString());
      }
    } catch (Exception e) {
      logger.error("校验人员成果配置信息出错，psnId = " + psnId + ", cnfId = " + cnfId, e);
      throw new PsnException(e);
    }
    return act;
  }

  // 校验人员项目配置信息
  private Long checkPsnPrjConfig(Long psnId, Long cnfId, Long act) throws PsnException {
    try {
      if (projectDao.hasPsnConfigPrjLost(psnId, cnfId)) {
        act = act | Long.valueOf(PsnCnfEnum.PRJ.toString());
      }
    } catch (Exception e) {
      logger.error("校验人员项目配置信息出错，psnId = " + psnId + ", cnfId = " + cnfId, e);
      throw new PsnException(e);
    }
    return act;
  }

  // 校验人员联系配置信息
  private Long checkPsnContactConfig(Long psnId, Long cnfId, Long act) throws PsnException {
    try {
      PsnConfigContact contactCnf = psnCnfService.get(new PsnConfigContact(cnfId));
      if (contactCnf == null) {
        act = act | Long.valueOf(PsnCnfEnum.CONTACT.toString());
      }
    } catch (Exception e) {
      logger.error("校验人员联系方式配置信息出错，psnId = " + psnId + ", cnfId = " + cnfId, e);
      throw new PsnException(e);
    }
    return act;
  }

  // 校验psn_config_list数据是否缺失
  private Long checkPsnConfigList(Long psnId, Long cnfId, Long act) throws PsnException {
    try {
      PsnConfigList cnfList = psnCnfService.get(new PsnConfigList(cnfId));
      if (cnfList == null) {
        act = act | Long.valueOf(PsnCnfEnum.DIRTY.toString());
      }
    } catch (Exception e) {
      logger.error("校验人员配置List信息出错，psnId = " + psnId + ", cnfId = " + cnfId, e);
      throw new PsnException(e);
    }
    return act;
  }

  // 校验psn_config_moudle数据是否缺失
  private Long checkPsnConfigMoudle(Long psnId, Long cnfId, Long act) throws PsnException {
    try {
      PsnConfigMoudle cnfMoudle = psnCnfService.get(new PsnConfigMoudle(cnfId));
      if (cnfMoudle == null) {
        act = act | Long.valueOf(PsnCnfEnum.DIRTY.toString());
      }
    } catch (Exception e) {
      logger.error("校验人员配置Moudle信息出错，psnId = " + psnId + ", cnfId = " + cnfId, e);
      throw new PsnException(e);
    }
    return act;
  }

  // 校验psn_config_position数据是否缺失
  private Long checkPsnConfigPosition(Long psnId, Long cnfId, Long act) throws PsnException {
    try {
      PsnConfigPosition cnfMoudle = psnCnfService.get(new PsnConfigPosition(cnfId));
      if (cnfMoudle == null) {
        act = act | Long.valueOf(PsnCnfEnum.POSITION.toString());
      }
    } catch (Exception e) {
      logger.error("校验人员Position配置信息出错，psnId = " + psnId + ", cnfId = " + cnfId, e);
      throw new PsnException(e);
    }
    return act;
  }

  // 校验人员联系配置信息
  private Long checkPsnAllConfig(Long psnId, Long cnfId, Long act) throws PsnException {
    try {
      act = this.checkPsnConfigList(psnId, cnfId, act);
      act = this.checkPsnConfigMoudle(psnId, cnfId, act);
      act = this.checkPsnBriefConfig(psnId, cnfId, act);
      act = this.checkPsnExpertiseConfig(psnId, cnfId, act);
      act = this.checkPsnWorkConfig(psnId, cnfId, act);
      act = this.checkPsnEduConfig(psnId, cnfId, act);
      // act = this.checkPsnPubConfig(psnId, cnfId, act);
      act = this.checkPsnPrjConfig(psnId, cnfId, act);
      act = this.checkPsnContactConfig(psnId, cnfId, act);
      act = this.checkPsnConfigPosition(psnId, cnfId, act);
    } catch (Exception e) {
      logger.error("校验人员所有配置信息出错，psnId = " + psnId + ", cnfId = " + cnfId, e);
      throw new PsnException(e);
    }
    return act;
  }

  @Override
  public void updateSIEPersonInfo(Long psnId) throws Exception {
    if (psnId != null && psnId > 0) {
      Person person = personProfileDao.get(psnId);
      if (person != null) {
        Long openId = openUserUnionDao.getOpenIdByPsnId(psnId);
        if (openId != null) {
          Map<String, Object> param = new HashMap<String, Object>();
          param.put("openid", openId);
          param.put("token", "11111111sieobtjy");
          param.put("data", JacksonUtils.mapToJsonStr(builParamData(person)));
          HttpHeaders requestHeaders = new HttpHeaders();
          requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
          HttpEntity<String> requestEntity =
              new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(param), requestHeaders);
          restTemplate.postForObject(restfulUrl, requestEntity, Object.class);
        }
      }
    }
  }

  private Map<String, Object> builParamData(Person person) {
    Map<String, Object> dataMap = new HashMap<String, Object>();
    if (person != null) {
      dataMap.put("psnId", person.getPersonId());
      dataMap.put("email", person.getEmail());
      dataMap.put("zhName", person.getName());
      dataMap.put("enName", person.getEname());
      dataMap.put("firstName", person.getFirstName());
      dataMap.put("lastName", person.getLastName());
      dataMap.put("sex", person.getSex());
      dataMap.put("posId", person.getPosId());// 职称id
      dataMap.put("position", person.getPosition());// 职称名
      dataMap.put("tel", person.getTel());
      dataMap.put("avatars", person.getAvatars());// 头像url
      dataMap.put("regionId", person.getRegionId());// 所在地区
    }
    return dataMap;
  }

}
