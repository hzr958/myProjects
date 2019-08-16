package com.smate.center.task.service.group;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import com.smate.center.task.base.TaskJobTypeConstants;
import com.smate.center.task.dao.group.GroupPublicationDao;
import com.smate.center.task.dao.group.GrpKwDiscDao;
import com.smate.center.task.dao.group.GrpPubsDao;
import com.smate.center.task.dao.group.GrpRcmdDao;
import com.smate.center.task.dao.pdwh.prj.NsfcProjectPubDao;
import com.smate.center.task.dao.pdwh.quartz.PubFundingInfoDao;
import com.smate.center.task.dao.pdwh.quartz.TmpTaskInfoRecordDao;
import com.smate.center.task.dao.sns.grp.GrpMemberDao;
import com.smate.center.task.dao.sns.grp.GrpPubInitDao;
import com.smate.center.task.dao.sns.psn.PsnDisciplineKeyDao;
import com.smate.center.task.dao.sns.psn.PsnScienceAreaDao;
import com.smate.center.task.dao.sns.quartz.AppSettingDao;
import com.smate.center.task.dao.sns.quartz.GroupFundInfoDao;
import com.smate.center.task.dao.sns.quartz.GrpBaseInfoDao;
import com.smate.center.task.dao.sns.quartz.PublicationPublicDao;
import com.smate.center.task.model.grp.GrpBaseinfo;
import com.smate.center.task.model.grp.GrpKwDisc;
import com.smate.center.task.model.grp.GrpPubInit;
import com.smate.center.task.model.grp.GrpRcmd;
import com.smate.center.task.model.pdwh.quartz.PubFundingInfo;
import com.smate.center.task.model.sns.quartz.GroupFundInfo;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubFullTextDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubFullTextPO;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.pub.po.dao.PubPdwhSnsRelationDAO;
import com.smate.core.base.pub.util.PubDetailVoUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 群组后台任务实现类
 * 
 * @author zll
 *
 */
@Service("groupService")
@Transactional(rollbackOn = Exception.class)
public class GrpServiceImpl implements GrpService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  private static Integer jobType = TaskJobTypeConstants.groupRcmdIncTask;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${groupDyn.restful.url}")
  private String groupDynRestfulUrl;
  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  GroupPublicationDao groupPublicationDao;
  @Autowired
  PublicationPublicDao publicationPublicDao;
  @Autowired
  private GroupFundInfoDao groupFundInfoDao;
  @Autowired
  private PubFundingInfoDao pubFundingInfoDao;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private NsfcProjectPubDao nsfcProjectPubDao;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;
  @Autowired
  private PdwhPubFullTextDAO pdwhPubFullTextDAO;
  @Autowired
  private GrpPubInitDao grpPubInitDao;
  @Autowired
  private PsnScienceAreaDao psnScienceAreaDao;
  @Autowired
  private PsnDisciplineKeyDao psnDisciplineKeyDao;
  @Autowired
  private GrpPubsDao grpPubsDao;
  @Autowired
  private PubPdwhSnsRelationDAO pubPdwhSnsRelationDAO;
  @Autowired
  private PsnPubDAO psnPubDAO;
  @Autowired
  private GrpRcmdDao grpRcmdDao;
  @Autowired
  private GrpKwDiscDao grpKwDiscDao;
  @Autowired
  private GrpMemberDao grpMemberDao;
  @Autowired
  private AppSettingDao appSettingDao;
  @Autowired
  private TmpTaskInfoRecordDao tmpTaskInfoRecordDao;

  @Override
  public List<PubFundingInfo> getPubFundingInfoByFundingNo(String fundingNo) {
    if (StringUtils.isEmpty(fundingNo)) {
      return null;
    }
    List<PubFundingInfo> list = this.pubFundingInfoDao.getPubFundingInfoByFundingNo(fundingNo);
    return list;
  }

  @Override
  public void saveOpResult(GroupFundInfo groupInfo, Integer status) {
    groupInfo.setStatus(status);
    this.groupFundInfoDao.save(groupInfo);
  }

  @Override
  public List<GroupFundInfo> getGroupFundInfo(Integer size, Long startGroupId, Long endGroupId) {
    List<GroupFundInfo> toDoList = this.groupFundInfoDao.getToHandleList(size, startGroupId, endGroupId);
    return toDoList;
  }

  @Override
  public List<Map<String, String>> getInstrestGrpInfo() {
    return grpBaseInfoDao.getInstrestGrpInfo();
  }

  @Override
  public List<Long> getRcmdPdwhPubIds(String nsfcCatId) {
    return nsfcProjectPubDao.getRcmdPdwhPubIds(nsfcCatId);
  }

  @Override
  public void insertIntoRcmdPdwh(Long pdwhPubId, Long grpId) {
    PubPdwhPO pubPdwh = pubPdwhDAO.getPdwhPub(pdwhPubId);
    if (pubPdwh != null) {
      PdwhPubFullTextPO pubFulltext = pdwhPubFullTextDAO.getByPubId(pdwhPubId);
      Integer hasFulltext = 0;
      if (pubFulltext != null) {
        hasFulltext = 1;
      }
      GrpPubInit grpPubInit = grpPubInitDao.getGrpPub(grpId, pdwhPubId);
      if (grpPubInit == null) {
        grpPubInit =
            new GrpPubInit(grpId, pdwhPubId, pubPdwh.getPublishYear(), pubPdwh.getCitations(), hasFulltext, new Date());
      } else {
        grpPubInit.setHasFulltext(hasFulltext);
        grpPubInit.setPubYear(pubPdwh.getPublishYear());
        grpPubInit.setUpdateDate(new Date());
      }
      grpPubInitDao.saveOrUpdate(grpPubInit);
    }

  }

  @Override
  public List<GrpBaseinfo> getInstrestGroup() {
    return grpBaseInfoDao.getInstrestGroup();
  }

  @Override
  public List<GrpPubInit> getGrpPubInit(Long grpId, int year, int number) {
    return grpPubInitDao.getGrpPubInit(grpId, year, number);
  }

  @Override
  public void importPdwhPubIntoGroup(GrpPubInit grpPubInit, Long ownerPsnId) {
    try {
      Long snsPubId = this.grpImportPdwhPub(grpPubInit.getGrpId(), grpPubInit.getPubId(), ownerPsnId);
      // 更新V_GRP_PUB_INIT状态
      if (snsPubId != null) {
        // 发一个群组动态
        Map<String, Object> groupDynMap = new HashMap<String, Object>();
        groupDynMap.put("groupId", grpPubInit.getGrpId());
        groupDynMap.put("psnId", ownerPsnId);
        groupDynMap.put("resType", "pub");
        groupDynMap.put("resId", snsPubId);
        groupDynMap.put("tempType", "GRP_ADDPUB");
        restTemplate.postForObject(groupDynRestfulUrl, groupDynMap, Object.class);
        grpPubInit.setStatus(1);
        grpPubInit.setUpdateDate(new Date());
        grpPubInitDao.saveOrUpdate(grpPubInit);
      }
    } catch (Exception e) {
      logger.error("基准库成果导入兴趣群组出错 grpId:" + grpPubInit.getGrpId() + ";pdwhPubId:" + grpPubInit.getPubId(), e);
      grpPubInit.setStatus(9);
      grpPubInit.setUpdateDate(new Date());
      grpPubInitDao.saveOrUpdate(grpPubInit);
    }
  }

  private Long grpImportPdwhPub(Long grpId, Long pubId, Long ownerPsnId) throws Exception {
    if (pubId == null || grpId == null) {
      return null;
    }
    Map<String, Object> pubMap = getPdwhPubDetail(pubId);
    if (pubMap == null || pubMap.size() == 0) {
      return null;
    }
    pubMap.put("pubHandlerName", "saveSnsPubHandler");
    pubMap.put("des3PdwhPubId", Des3Utils.encodeToDes3(pubId + ""));
    pubMap.put("des3PsnId", Des3Utils.encodeToDes3(String.valueOf(ownerPsnId)));
    pubMap.put("pubGenre", 2); // 设置为群组成果
    pubMap.put("grpId", grpId);
    pubMap.put("isPubConfirm", 1);// 设置这个操作为成果认领
    pubMap.put("des3GrpId", Des3Utils.encodeToDes3(String.valueOf(grpId)));
    pubMap.put("recordFrom", PubSnsRecordFromEnum.IMPORT_FROM_PDWH);// 设置为从基准库导入
    pubMap.put("isProjectPub", 0);// 群组文献
    String dupResult = this.getGroupDupPubs(pubMap, grpId, pubId, ownerPsnId);
    Map dupResultMap = JacksonUtils.jsonToMap(dupResult);
    Long newPubId = 0L;
    if ("SUCCESS".equals(dupResultMap.get("status").toString())) {
      if (dupResultMap.get("msg") != null) {// 成果已经存在
        return -1L;
      } else {
        // 2.导入成果到个人库
        // 调用保存个人成果接口
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(pubMap), headers);
        String saveUrl = domainscm + V8pubQueryPubConst.PUBHANDLER_URL;
        String result = restTemplate.postForObject(saveUrl, entity, String.class);
        Map saveResultMap = JacksonUtils.jsonToMap(result);
        if ("SUCCESS".equals(saveResultMap.get("status").toString())) {
          String pubIdStr = Des3Utils.decodeFromDes3((String) saveResultMap.get("des3PubId"));
          newPubId = Long.parseLong(pubIdStr);
        } else {
          throw new Exception("推荐成果导入群组出错, GroupId=" + grpId + ", pdwhPubId=" + pubId);
        }
      }
    }
    return newPubId;
  }

  private String getGroupDupPubs(Map<String, Object> pubMap, Long grpId, Long pubId, Long ownerPsnId) {
    Map<String, Object> dupMap = new HashMap<String, Object>();
    String title = (String) pubMap.get("title");
    Integer pubType = (Integer) pubMap.get("pubType");
    if (pubMap.get("publishDate") != null && StringUtils.isNotEmpty((String) pubMap.get("publishDate"))) {
      dupMap.put("pubYear", Integer.valueOf(PubDetailVoUtil.parseDateForYear((String) pubMap.get("publishDate"))));
    }
    Integer srcDbId = (Integer) pubMap.get("srcDbId");
    String doi = (String) pubMap.get("doi");
    String sourceId = (String) pubMap.get("sourceId");
    String applicationNo = (String) pubMap.get("applicationNo");
    String publicationOpenNo = (String) pubMap.get("publicationOpenNo");
    String standardNo = (String) pubMap.get("standardNo");
    String registerNo = (String) pubMap.get("registerNo");
    dupMap.put("pubGener", 2);// 成果大类别，1代表个人成果 2代表群组成果 3代表基准库成果
    dupMap.put("title", title);
    dupMap.put("pubType", pubType);
    dupMap.put("doi", doi);
    dupMap.put("srcDbId", srcDbId);
    dupMap.put("sourceId", sourceId);
    dupMap.put("des3PsnId", Des3Utils.encodeToDes3(ownerPsnId.toString()));
    dupMap.put("groupId", grpId);
    if (5 == pubType) {
      dupMap.put("applicationNo", applicationNo);
      dupMap.put("publicationOpenNo", publicationOpenNo);
    }
    if (12 == pubType) {
      dupMap.put("standardNo", standardNo);
    }
    if (13 == pubType) {
      dupMap.put("registerNo", registerNo);
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(dupMap), headers);
    String dupUrl = domainscm + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    String result = restTemplate.postForObject(dupUrl, entity, String.class);
    return result;
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> getPdwhPubDetail(Long pubId) {
    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
    Map<String, Object> rsMap = new HashMap<String, Object>();
    try {
      Map<String, Object> map = new HashMap<>();
      map.put("des3PubId", Des3Utils.encodeToDes3(pubId.toString()));
      map.put("serviceType", V8pubQueryPubConst.PDWH_PUB);
      // 设置请求头部
      HttpHeaders requestHeaders = new HttpHeaders();
      requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
      HttpEntity<String> requestEntity = new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(map), requestHeaders);
      rsMap = (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    } catch (Exception e) {
      logger.error("获取群组推荐成果详情信息出错,pubId=" + pubId, e);
    }
    return rsMap;
  }

  @Override
  public Boolean grpHasInit(Long grpId) {
    List<GrpPubInit> grpPubInitList = grpPubInitDao.getGrpPubInit(grpId);
    if (grpPubInitList != null && grpPubInitList.size() > 0) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public List<Long> getPsnIdByCatId(Integer secondCatId) {
    return psnScienceAreaDao.getPsnIdByCatId(secondCatId);
  }

  @Override
  public Integer getSameKeywords(List<String> grpKwList, Long psnId) {
    Integer sameKeywordCount = 0;
    List<Long> sameKeywords = psnDisciplineKeyDao.getSameKeywords(grpKwList, psnId);
    if (!CollectionUtils.isEmpty(sameKeywords)) {
      sameKeywordCount = sameKeywords.size();
    }
    return sameKeywordCount;
  }

  @Override
  public List<Long> getGrpPubAuthor(Long grpId) {
    List<Long> grpPubIdList = grpPubsDao.findGroupPubIds(grpId);
    // 1.根据个人库成果id找到基准库成果id
    List<Long> pdwhPubIdList = pubPdwhSnsRelationDAO.getPdwhPubIdsBySnsPubId(grpPubIdList);
    // 2.根据基准库的成果找到个人库成果
    List<Long> snsPubIdList = pubPdwhSnsRelationDAO.getSnsPubIdListByPdwhId(pdwhPubIdList);
    // 3.根据个人库成果作者
    List<Long> psnIds = psnPubDAO.getPubsOwner(snsPubIdList);
    return psnIds;
  }

  @Override
  public void saveGrpRcmd(Long psnId, Long grpId, Integer rcmdScore, Boolean isGrpmember) {
    // 如果已经是群组成员，则设置状态为已加入
    GrpRcmd grpRcmd = grpRcmdDao.getGrpRcmd(psnId, grpId);
    if (grpRcmd == null) {
      grpRcmd = new GrpRcmd(psnId, grpId, new Date(), rcmdScore);
    } else {
      grpRcmd.setUpdateDate(new Date());
      grpRcmd.setRcmdScore(rcmdScore);
    }
    grpRcmd.setStatus(isGrpmember ? "1" : "0");
    grpRcmdDao.saveOrUpdate(grpRcmd);
  }

  @Override
  public List<GrpBaseinfo> getNeedRcmdGroup(Long lastGrpId, Integer size) {
    return grpBaseInfoDao.getNeedRcmdGroup(lastGrpId, size);
  }

  @Override
  public GrpKwDisc getGrpKwDisc(Long grpId) {
    return grpKwDiscDao.get(grpId);
  }

  @Override
  public List<Long> getGrpMemberIdByGrpId(Long grpId) {
    return grpMemberDao.getGrpMemberIdByGrpId(grpId);
  }

  @Override
  public void upAppSettingConstant(String grpRcmdStart, Long grpId) {
    appSettingDao.updateAppSetting(grpRcmdStart, grpId.toString());
  }

  @Override
  public List<Long> getbatchhandleIdList(Integer size) throws Exception {
    return tmpTaskInfoRecordDao.getbatchhandleIdList(size, jobType);
  }

  @Override
  public void updateTaskStatus(Long grpId, int status, String message) throws Exception {
    tmpTaskInfoRecordDao.updateTaskStatus(grpId, status, message, jobType);
  }

  @Override
  public GrpBaseinfo getGrpBaseInfo(Long grpId) {
    return grpBaseInfoDao.get(grpId);
  }

  @Override
  public void updateGrpRcmd(Long grpId) {
    grpRcmdDao.updateGrpRcmd(grpId);
  }

  @Override
  public void deleteGrpRcmd(Long grpId) {
    grpRcmdDao.deleteGrpRcmd(grpId);
  }



}
