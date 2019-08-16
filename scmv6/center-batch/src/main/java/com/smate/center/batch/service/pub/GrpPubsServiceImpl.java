package com.smate.center.batch.service.pub;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.client.RestTemplate;

import com.smate.center.batch.dao.sns.pub.GrpBaseInfoDao;
import com.smate.center.batch.dao.sns.pub.GrpKwDiscDao;
import com.smate.center.batch.dao.sns.pub.GrpPubsDao;
import com.smate.center.batch.dao.sns.pub.GrpStatisticsDao;
import com.smate.center.batch.dao.sns.pub.PubDataStoreDao;
import com.smate.center.batch.dao.sns.pub.PublicationDao;
import com.smate.center.batch.dao.sns.pub.ScmPubXmlDao;
import com.smate.center.batch.dao.sns.pub.TaskGroupResNotifyDao;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.GroupPsnNode;
import com.smate.center.batch.model.sns.pub.GrpPubs;
import com.smate.center.batch.model.sns.pub.GrpStatistics;
import com.smate.center.batch.model.sns.pub.PubDataStore;
import com.smate.center.batch.model.sns.pub.Publication;
import com.smate.center.batch.model.sns.pub.ScmPubXml;
import com.smate.center.batch.model.sns.pub.TaskGroupResNotify;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;

/**
 * 群组成果服务实现类
 * 
 * @author zjh
 *
 */
@Service("grpPubsService")
@Transactional(rollbackFor = Exception.class)
public class GrpPubsServiceImpl implements GrpPubsService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  public static String ZH_CN = "zh";
  public static String EN = "en";
  @Autowired
  private ScmPubXmlDao scmPubXmlDao;
  @Autowired
  private PubDataStoreDao pubDataStoreDao;
  @Autowired
  private GrpKwDiscDao grpKwDiscDao;
  @Autowired
  private GrpPubsDao grpPubsDao;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private DynamicGroupProduceService dynGroupProduceAddPubService;
  @Autowired
  private PublicationDao publicationDao;
  @Autowired
  private PublicationStatisticsService publicationStatisticsService;
  @Autowired
  private TaskGroupResNotifyDao taskGroupResNotifyDao;
  @Autowired
  private PublicationListService publicationListService;
  @Autowired
  private GrpStatisticsDao grpStatisticsDao;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;
  @Autowired
  private RestTemplate restTemplate;

  /*
   * @Autowired private RestTemplate restTemplate;
   * 
   * @Value("${groupDyn.restful.url}") private String groupDynRestfulUrl;
   */

  @Override
  public void saveGrpPubs(GrpPubs grpPubs) throws Exception {
    grpPubsDao.save(grpPubs);
  }

  @Override
  public List<GrpPubs> getGrpPubsList(Long groupId) throws Exception {
    return this.grpPubsDao.getGroupPubList(groupId);
  }

  @Override
  public List<String> getGroupKw(Long grpId) {
    String grpkwords = this.grpKwDiscDao.getGrpKwDiscList(grpId);
    if (grpkwords != null) {
      int startIndex = grpkwords.indexOf(";");
      if (startIndex == 0) {
        grpkwords = grpkwords.substring(startIndex, startIndex + 1);// 去掉第一个；分隔符，否则split后会出现一个空的关键字
      }
      if (grpkwords.split(";") != null) {
        return Arrays.asList(grpkwords.split(";"));
      }
    }
    return null;
  }

  @Override
  public Integer getShareKwCount(String pubKw, List<String> groupKws) {
    Integer count = 0;
    if (StringUtils.isEmpty(pubKw) || CollectionUtils.isEmpty(groupKws)) {
      return count;
    }

    for (String groupKw : groupKws) {
      if (StringUtils.isNotEmpty(groupKw)) {
        if (pubKw.indexOf(groupKw) != -1) {
          count++;
        }
      }
    }
    return count;
  }

  public List<String> filterKwStringList(List<String> kwStrList) {
    List<String> strList = new ArrayList<String>();
    for (String str : kwStrList) {
      str = this.filterKwString(str);
      if (StringUtils.isEmpty(str)) {
        continue;
      }
      strList.add(str);
    }
    return strList;
  }

  public String filterKwString(String str) {
    if (StringUtils.isEmpty(str)) {
      return "";
    }

    str = XmlUtil.filterAuForCompare(str);
    // 过滤数字
    str = str.replaceAll("\\d+", "");
    return str;
  }

  public String getKwStr(Long pubId, String lang) {
    String kwStr = null;
    String pubXmlStr = null;
    // 被修改的内容优先存储在pubDataStore中
    PubDataStore pubDataStore = pubDataStoreDao.get(pubId);

    if (pubDataStore == null || StringUtils.isEmpty(pubDataStore.getData())) {
      ScmPubXml pubXml = scmPubXmlDao.get(pubId);
      if (pubDataStore == null || StringUtils.isEmpty(pubXml.getPubXml())) {
        return kwStr;
      }
      pubXmlStr = pubXml.getPubXml();
    } else {
      pubXmlStr = pubDataStore.getData();
    }

    try {
      PubXmlDocument newDoc = new PubXmlDocument(pubXmlStr);
      if (ZH_CN.equals(lang)) {
        kwStr = newDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "zh_keywords");
      }
      if (EN.equals(lang)) {
        kwStr = newDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "en_keywords");
      }

    } catch (Exception e) {
      logger.error("GroupPubRelevanceReCalTasklet 从xml获取关键词出错, pubId :" + pubId, e);
    }

    return kwStr;
  }

  /*
   * 成果关键词与群组关键词对应的数量，标识此成果与群组的相关度
   */
  @Override
  public Integer getPubGroupShareKwCount(GrpPubs grPub) throws Exception {
    Integer count = 0;
    Long pubId = grPub.getPubId();
    Long groupId = grPub.getGrpId();
    try {
      String zhKwStr = this.getKwStr(pubId, ZH_CN);
      String enKwStr = this.getKwStr(pubId, EN);
      if (StringUtils.isEmpty(zhKwStr) && StringUtils.isEmpty(enKwStr)) {
        return count;
      }
      List<String> kwStrList = this.getGroupKw(groupId);
      if (CollectionUtils.isEmpty(kwStrList)) {
        logger.debug("群组关键词为空, 群组Id :" + groupId);
        return count;
      }
      // 过滤
      zhKwStr = this.filterKwString(zhKwStr);
      enKwStr = this.filterKwString(enKwStr);
      kwStrList = this.filterKwStringList(kwStrList);
      // 获取与群组关键词相同的词数
      Integer zhCount = this.getShareKwCount(zhKwStr, kwStrList);
      Integer enCount = this.getShareKwCount(enKwStr, kwStrList);

      return zhCount + enCount;
    } catch (Exception e) {
      throw new Exception("getPubGroupShareKwCount计算群组与群组成果相同关键字出错， pubId ： " + pubId + " === " + e);
    }
  }

  /*
   * 成果关键词与群组关键词对应的数量，标识此成果与群组的相关度
   */
  @Override
  public Integer getNewPubGroupShareKwCount(GrpPubs grPub, Map<String, Object> map) throws Exception {
    Integer count = 0;
    Long pubId = grPub.getPubId();
    Long groupId = grPub.getGrpId();
    try {
      String kwStr = "";
      if (map != null) {
        kwStr = String.valueOf(map.get("keywords"));
      }
      if (StringUtils.isEmpty(kwStr)) {
        return count;
      }
      List<String> kwStrList = this.getGroupKw(groupId);
      if (CollectionUtils.isEmpty(kwStrList)) {
        logger.debug("群组关键词为空, 群组Id :" + groupId);
        return count;
      }
      // 过滤
      kwStr = this.filterKwString(kwStr);
      kwStrList = this.filterKwStringList(kwStrList);
      // 获取与群组关键词相同的词数
      Integer zhCount = this.getShareKwCount(kwStr, kwStrList);

      return zhCount;
    } catch (Exception e) {
      throw new Exception("getPubGroupShareKwCount计算群组与群组成果相同关键字出错， pubId ： " + pubId + " === " + e);
    }
  }

  /**
   * 1成果基金信息与群组对应上，已标注；0未对应上，未标注
   * 
   * 
   */
  @Override
  public Integer groupNewPubIsLabeled(GrpPubs grpPub, Map<String, Object> map) throws Exception {
    Integer isLabeled = 0;
    Long pubId = grpPub.getPubId();
    Long groupId = grpPub.getGrpId();

    try {
      String pubFundInfo = "";
      if (map != null) {
        pubFundInfo = String.valueOf(map.get("fundInfo"));
      }
      if (StringUtils.isEmpty(pubFundInfo)) {
        return isLabeled;
      }

      String projectNo = this.grpBaseInfoDao.getProjectNo(groupId);
      if (StringUtils.isEmpty(projectNo)) {
        return isLabeled;
      }

      // pubFundInfo过滤
      pubFundInfo = XmlUtil.filterAuForCompare(pubFundInfo);

      // 需要过滤所有不必要的信息
      projectNo = XmlUtil.filterAuForCompare(projectNo);

      // 一个项目只对应一个资助基金号，而一个成果对应多个不同的资助基金。所以用项目基金号去匹配成果基金号string。
      isLabeled = this.pubIsLabeled(pubFundInfo, projectNo);

      return isLabeled;
    } catch (Exception e) {
      throw new Exception("groupPubIsLabeled群组成果标记出错, pubId : " + pubId + " === " + e);
    }
  }

  /**
   * 1成果基金信息与群组对应上，已标注；0未对应上，未标注
   * 
   * 
   */
  @Override
  public Integer groupPubIsLabeled(GrpPubs grpPub) throws Exception {
    Integer isLabeled = 0;
    Long pubId = grpPub.getPubId();
    Long groupId = grpPub.getGrpId();

    try {
      // String pubFundInfo = this.getPubFundInfo(pubId);
      String pubFundInfo = "";
      Map<String, Object> object = getPubDetails(pubId);
      if (object != null) {
        pubFundInfo = String.valueOf(object.get("fundInfo"));
      }
      if (StringUtils.isEmpty(pubFundInfo)) {
        return isLabeled;
      }

      String projectNo = this.grpBaseInfoDao.getProjectNo(groupId);
      if (StringUtils.isEmpty(projectNo)) {
        return isLabeled;
      }

      // pubFundInfo过滤
      pubFundInfo = XmlUtil.filterAuForCompare(pubFundInfo);

      // 需要过滤所有不必要的信息
      projectNo = XmlUtil.filterAuForCompare(projectNo);

      // 一个项目只对应一个资助基金号，而一个成果对应多个不同的资助基金。所以用项目基金号去匹配成果基金号string。
      isLabeled = this.pubIsLabeled(pubFundInfo, projectNo);

      return isLabeled;
    } catch (Exception e) {
      throw new Exception("groupPubIsLabeled群组成果标记出错, pubId : " + pubId + " === " + e);
    }
  }

  public Integer pubIsLabeled(String pubFundInfo, String groupFundInfo) {
    Integer isLabeled = 0;
    // 成果可能会对应多个基金资助，而项目群组只对应一个
    if (StringUtils.isEmpty(groupFundInfo) || StringUtils.isEmpty(pubFundInfo)) {
      return isLabeled;
    }

    if (pubFundInfo.indexOf(groupFundInfo) != -1) {
      isLabeled = 1;
    }

    return isLabeled;
  }

  @Override
  public Map<String, Object> getPubDetails(Long pubId) throws Exception {
    String SERVER_URL = this.domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
    Map<String, Object> object = new HashMap<String, Object>();
    try {
      Map<String, Object> map = new HashMap<>();
      map.put("des3PubId", Des3Utils.encodeToDes3(pubId.toString()));
      map.put("serviceType", V8pubQueryPubConst.QUERY_OPEN_SNS_PUB_DETAIL_BY_PUB_ID_SERVICE);
      // 设置请求头部
      HttpHeaders requestHeaders = new HttpHeaders();
      requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
      HttpEntity<String> requestEntity = new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(map), requestHeaders);
      object = (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    } catch (Exception e) {
      logger.error("获取成果详情信息出错,pubId=" + pubId, e);
    }
    return object;
  }

  private String getPubFundInfo(Long pubId) {
    // 优先从pubDataStore中取值
    String pubXmlStr = null;
    String fundInfoStr = null;
    PubDataStore pubDataStore = pubDataStoreDao.get(pubId);
    ScmPubXml pubXml = scmPubXmlDao.get(pubId);
    // 不影响整体成果处理流程
    try {
      if (pubDataStore == null || StringUtils.isEmpty(pubDataStore.getData())) {
        if (pubXml == null || StringUtils.isEmpty(pubXml.getPubXml())) {
          return fundInfoStr;
        }
        pubXmlStr = pubXml.getPubXml();
        PubXmlDocument newDoc = new PubXmlDocument(pubXmlStr);
        fundInfoStr = newDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo");
      } else {
        pubXmlStr = pubDataStore.getData();
        PubXmlDocument newDoc = new PubXmlDocument(pubXmlStr);
        fundInfoStr = newDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo");
        // 如果pubdatastore中取得的fundInfoStr还是为空，则从scmpubxml中取值
        if (StringUtils.isEmpty(fundInfoStr)) {
          if (pubXml == null || StringUtils.isEmpty(pubXml.getPubXml())) {
            return fundInfoStr;
          }
          pubXmlStr = pubXml.getPubXml();
          PubXmlDocument pubDoc = new PubXmlDocument(pubXmlStr);
          fundInfoStr = pubDoc.getXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "fundinfo");
        }
      }
    } catch (Exception e) {
      logger.error("pub processor处理群组成果信息， 从xml获取基金资助信息fundinfo出错, pubId :" + pubId, e);
    }

    return fundInfoStr;
  }

  /**
   * 增加群组成果文献数据(重新整理方法中代码_MJG_SCM-3543). 在群组中无法用SecurityUtils获取当前操作人id，重新方法
   */
  @Override
  public void addPublicationToGroup(Long psnId, String des3PubIds, String groupIds, Integer articleType, Long folderId,
      int dynFlag) throws ServiceException {
    String[] pubIds = StringUtils.split(des3PubIds, ",");
    String[] groups = StringUtils.split(groupIds, ",");
    Long currentUserId = psnId;
    for (String iPubId : pubIds) {
      String pubIdStr = iPubId;
      if (!StringUtils.isNumeric(pubIdStr)) {
        pubIdStr = ServiceUtil.decodeFromDes3(pubIdStr);
      }
      Long pubId = Long.valueOf(pubIdStr);
      for (String iGroupId : groups) {
        if (!StringUtils.isNumeric(iGroupId)) {
          // groupId = ServiceUtil.decodeFromDes3(groupId);
        }
        Long groupId = Long.valueOf(iGroupId);
        if (this.isAddGroupByPubId(pubId, groupId, articleType)) {
          continue;
        }
        // 保存群组成果数据
        this.saveGrpPubs(pubId, groupId);
        // 生成群组增加资源文件的动态.在新增群组成果的时候会产生动态
        /*
         * if (dynFlag == 1) { this.createGroupAddResDyn(groups, pubId, articleType, currentNodeId,
         * currentUserId); }
         */
      }
    }

    // 清除统计缓存, 万一出错不影响整体导入
    publicationStatisticsService.clearPubGroupStatistic(currentUserId, articleType);
  }

  public void saveGrpPubs(Long pubId, Long grpId) throws ServiceException {
    GrpPubs grpPubs = new GrpPubs();
    Publication publication = this.publicationDao.get(pubId);
    // 重新计算相关度与标注
    try {
      grpPubs = this.wrapGroupPublication(publication, grpPubs);
      Integer sharePubCount = this.getPubGroupShareKwCount(grpPubs);
      Integer isLabeled = this.groupPubIsLabeled(grpPubs);
      if (sharePubCount != null) {
        grpPubs.setRelevance(sharePubCount);
      }

      if (isLabeled != null) {
        grpPubs.setLabeled(isLabeled);
      }
    } catch (Exception e) {
      logger.error("群组成果相关性更新失败， 群组Id :" + grpId + "== pubId : " + pubId, e);
    }
    grpPubs.setGrpId(grpId);
    grpPubsDao.save(grpPubs);
    GrpStatistics grpStatistics = grpStatisticsDao.getGrpStatistics(grpId);
    grpStatistics.setSumPubs(grpStatistics.getSumPubs() + 1);
    grpStatisticsDao.save(grpStatistics);
  }

  public Integer isPorject(Long pubId) throws Exception {
    String pubFundInfo = this.getPubFundInfo(pubId);
    if (StringUtils.isEmpty(pubFundInfo)) {
      return 0;
    } else {
      return 1;
    }

  }

  private GrpPubs wrapGroupPublication(Publication publication, GrpPubs grpPubs) throws Exception {
    grpPubs.setPubId(publication.getId());
    grpPubs.setCreateDate(new Date());
    grpPubs.setCreatePsnId(publication.getPsnId());
    grpPubs.setIsProjectPub(this.isPorject(publication.getId()));
    grpPubs.setStatus(0);
    // 同步收录情况
    return grpPubs;
  }

  /**
   * 生成群组增加资源文件的动态. 需要添加当前操作人，否则后台任务无法用
   * 
   * @param groups
   * @param resDetails
   * @param articleType
   * @param currentNodeId
   * @throws ServiceException
   */
  /*
   * private void createGroupAddResDyn(String[] groups, Long pubId, Integer articleType, int
   * currentNodeId, Long currenctUserId) { for (String groupId : groups) { Map<String, Object>
   * groupDynMap = new HashMap<String, Object>(); groupDynMap.put("groupId", Long.valueOf(groupId));
   * groupDynMap.put("psnId", currenctUserId); groupDynMap.put("resType", "pub");
   * groupDynMap.put("resId", pubId); groupDynMap.put("tempType", "GRP_ADDPUB");
   * 
   * PubSimple pubSimple = pubSimpleDao.get(pubId); Map<String, Object> pubSimpleMap = new
   * HashMap<String, Object>(); pubSimpleMap.put("AUTHOR_NAMES", pubSimple.getAuthorNames());
   * pubSimpleMap.put("EN_RES_NAME", StringUtils.isNoneBlank(pubSimple.getEnTitle()) ?
   * pubSimple.getEnTitle() : pubSimple.getZhTitle()); pubSimpleMap.put( "EN_RES_DESC",
   * StringUtils.isNoneBlank(pubSimple.getBriefDescEn()) ? pubSimple.getBriefDescEn() : pubSimple
   * .getBriefDesc()); pubSimpleMap.put("ZH_RES_NAME", StringUtils.isNoneBlank(pubSimple.getZhTitle())
   * ? pubSimple.getZhTitle() : pubSimple.getEnTitle()); pubSimpleMap.put("ZH_RES_DESC",
   * StringUtils.isNoneBlank(pubSimple.getBriefDesc()) ? pubSimple.getBriefDesc() :
   * pubSimple.getBriefDescEn()); if (StringUtils.isNoneBlank(pubSimple.getFullTextField())) {
   * pubSimpleMap.put("FULL_TEXT_FIELD", pubSimple.getFullTextField()); }
   * groupDynMap.put("pubSimpleMap", JacksonUtils.jsonObjectSerializer(pubSimpleMap));
   * 
   * restTemplate.postForObject(this.groupDynRestfulUrl, groupDynMap, Object.class); } }
   */

  /**
   * 收集群组资源更新数据.
   * 
   * @param pubId
   * @param groupId
   * @param currentUserId
   * @param groupPsnNode
   */
  private void collectionGroupResData(Long pubId, Long groupId, Long currentUserId, GroupPsnNode groupPsnNode) {
    Integer nodeId = SecurityUtils.getCurrentUserNodeId();// groupPsnNode.getNodeId();
    // 群组增加成果、文献动态
    Publication pub = this.publicationDao.get(pubId);
    TaskGroupResNotify groupRes =
        new TaskGroupResNotify(pubId, groupId, currentUserId, pub.getArticleType(), 0, new Date(), nodeId);
    if ("zh".equalsIgnoreCase(LocaleContextHolder.getLocale().getLanguage())) {
      groupRes.setLocale("zh");
    } else {
      groupRes.setLocale("en");
    }

    try {
      taskGroupResNotifyDao.save(groupRes);
    } catch (Exception e) {
      logger.error("本节点nodeId={}保存群组groupId={}的成果类别资源resKey={}失败！", new Object[] {nodeId, groupId, pubId, e});
    }
  }

  @Override
  public boolean isAddGroupByPubId(Long pubId, Long groupId, Integer articleType) throws ServiceException {
    Long count = 0L;
    try {
      count = this.grpPubsDao.getCountGroupPub(pubId, groupId);
    } catch (Exception e) {
      logger.error("判断成果是否在群组中失败" + e);
    }
    if (count > 0) {
      return true;
    }
    return false;
  }
}
