package com.smate.web.group.service.grp.pub;

import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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

import com.smate.core.base.psn.dao.PsnPubDAO;
import com.smate.core.base.pub.consts.V8pubQueryPubConst;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.utils.common.HtmlUtils;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.core.base.utils.constant.ShortUrlConst;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.core.base.utils.url.RestUtils;
import com.smate.web.group.action.grp.form.GrpPubForm;
import com.smate.web.group.action.grp.form.GrpPubInfoVO;
import com.smate.web.group.action.grp.form.GrpPubShowInfo;
import com.smate.web.group.constant.grp.GrpConstant;
import com.smate.web.group.dao.grp.grpbase.GrpBaseInfoDao;
import com.smate.web.group.dao.grp.grpbase.GrpKwDiscDao;
import com.smate.web.group.dao.grp.grpbase.GrpStatisticsDao;
import com.smate.web.group.dao.grp.pub.GroupPubsDAO;
import com.smate.web.group.dao.grp.pub.GrpPubIndexUrlDao;
import com.smate.web.group.dao.grp.pub.GrpPubRcmdDao;
import com.smate.web.group.dao.grp.pub.GrpPubsDao;
import com.smate.web.group.dao.grp.pub.PubDataStoreDao;
import com.smate.web.group.dao.grp.pub.PubLikeDAO;
import com.smate.web.group.dao.grp.pub.PubStatisticsDAO;
import com.smate.web.group.dao.grp.pub.PublicationListDao;
import com.smate.web.group.dao.grp.pub.ScmPubXmlDao;
import com.smate.web.group.model.group.GrpPubs;
import com.smate.web.group.model.group.pub.GroupPubPO;
import com.smate.web.group.model.group.pub.PubDataStore;
import com.smate.web.group.model.group.pub.PubStatisticsPO;
import com.smate.web.group.model.group.pub.PubXmlDocument;
import com.smate.web.group.model.group.pub.ScmPubXml;
import com.smate.web.group.model.grp.grpbase.GrpStatistics;
import com.smate.web.group.model.grp.pub.GrpPubIndexUrl;
import com.smate.web.group.model.grp.pub.GrpPubRcmd;
import com.smate.web.group.model.grp.pub.PublicationList;
import com.smate.web.group.service.grp.member.GrpRoleService;

/**
 * 群组成果服务实现类
 * 
 * @author tsz
 */
@Service("grpPubsService")
@Transactional(rollbackFor = Exception.class)
public class GrpPubsServiceImpl implements GrpPubsService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrpPubsDao grpPubDao;
  @Autowired
  private GrpRoleService grpRoleService;
  @Autowired
  private GrpPubShowInfoService grpPubShowInfoService;
  @Autowired
  private GrpStatisticsDao grpStatisticsDao;
  @Autowired
  private PublicationListDao publicationListDao;
  @Autowired
  private PubStatisticsDAO pubStatisticsDAO;
  @Autowired
  private PubLikeDAO pubLikeDAO;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${groupDyn.restful.url}")
  private String groupDynRestfulUrl;
  @Autowired
  private PubDataStoreDao pubDataStoreDao;
  @Autowired
  private GrpBaseInfoDao grpBaseInfoDao;
  @Autowired
  private ScmPubXmlDao scmPubXmlDao;
  @Autowired
  private GrpKwDiscDao grpKwDiscDao;
  public static String ZH_CN = "zh";
  public static String EN = "en";
  @Autowired
  private GrpPubIndexUrlDao grpPubIndexUrlDao;
  @Autowired
  private PsnPubDAO psnPubDAO;
  @Autowired
  private GroupPubsDAO groupPubsDAO;
  @Autowired
  private GrpPubRcmdDao grpPubRcmdDao;
  @Value("${domainscm}")
  private String domainscm;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;


  @Override
  public void getGrpPubs(GrpPubForm form) throws Exception {
    // 查询成果
    getGrpPubsFromV8pub(form);
    if (CollectionUtils.isEmpty(form.getGrpPubInfoVOs())) {
      // 如果没有记录 直接放回
      return;
    }
    List<GrpPubShowInfo> showPubList = new ArrayList<GrpPubShowInfo>();
    form.setRole(grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId()));
    for (GrpPubInfoVO pub : form.getGrpPubInfoVOs()) {
      // 构建群组成果像是数据
      GrpPubShowInfo pubInfo = grpPubShowInfoService.buildShowPubInfo(pub);
      extendInfo(form, pub, pubInfo);
      // 设置成果拥有者加密ID
      pubInfo.setDes3RecvPsnId(Des3Utils.encodeToDes3(pub.getOwnerPsnId() + ""));
      // 是否是自己的成果
      if (pub.getOwnerPsnId().equals(form.getPsnId())) {
        pubInfo.setIsOwn(1);
      }
      pubInfo.setUpdateMark(pub.getUpdateMark());
      showPubList.add(pubInfo);
    }
    form.getPage().setResult(showPubList);
  }

  /* 构建扩展信息 */
  private void extendInfo(GrpPubForm form, GrpPubInfoVO pub, GrpPubShowInfo pubInfo) throws Exception {
    GrpPubs temPubs = grpPubDao.getGrpPubs(pub.getPubId(), form.getGrpId());
    if (temPubs != null) {
      pubInfo.setIsProjectPub(temPubs.getIsProjectPub());
      pubInfo.setLabeld(temPubs.getLabeled() == null ? 0 : temPubs.getLabeled());// 测试旧数据中有的Labeked为空导致不能标记为群组成果
      pubInfo.setRelevance(temPubs.getRelevance() == null ? 0 : temPubs.getRelevance());
    }
    if (pub.getOwnerPsnId() == null) {
      pub.setOwnerPsnId(0L);
    }
    if (pub.getOwnerPsnId().equals(form.getPsnId()) || form.getRole() == 1 || form.getRole() == 2) {
      // 如果是自己的成果或者 该用户是管理员权限 可以删除成果 也可以标记成果
      pubInfo.setCanDelete(1);
      pubInfo.setCanMark(1);
    }
    if (pub.getOwnerPsnId().equals(form.getPsnId())) {
      pubInfo.setCanEdit(1);
    }
    bulidPubStatistics(pubInfo, form.getPsnId());// 构建初始化赞/分享信息
  }

  private void bulidPubStatistics(GrpPubShowInfo pubInfo, Long psnId) {
    // 初始化成果的赞操作
    PubStatisticsPO pubStat = pubStatisticsDAO.getPubStatisticsById(pubInfo.getPubId());
    if (pubStat != null) {
      pubInfo.setAwardCount(pubStat.getAwardCount() == null ? 0 : pubStat.getAwardCount());
      pubInfo.setShareCount(pubStat.getShareCount() == null ? 0 : pubStat.getShareCount());
      pubInfo.setCommentCount(pubStat.getCommentCount() == null ? 0 : pubStat.getCommentCount());
    }
    long count = pubLikeDAO.getLikeRecord(pubInfo.getPubId(), psnId);
    if (count > 0) {
      pubInfo.setIsAward(1);
    }
  }

  @Override
  public void getMemberPubs(GrpPubForm form) throws Exception {
    form.setOrderBy(form.getOrderBy().replaceAll(",", ""));
    // 查询成果
    getPsnPubsFromV8pub(form);
    if (CollectionUtils.isEmpty(form.getGrpPubInfoVOs())) {
      // 如果没有记录 直接放回
      return;
    }
    List<GrpPubShowInfo> showPubList = new ArrayList<GrpPubShowInfo>();
    form.setRole(grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId()));
    for (GrpPubInfoVO pub : form.getGrpPubInfoVOs()) {
      GrpPubShowInfo pubInfo = new GrpPubShowInfo();
      if (pub.existGrpPub) {
        pubInfo.setIsImport(1);
      }
      // 成果图片逻辑
      if (pub.getHasFulltext() == 1) {
        if (StringUtils.isBlank(pub.getFullTextImgUrl())) {
          pubInfo.setFullTextImaUrl("/resscmwebsns/images_v5/images2016/file_img1.jpg");
        } else {
          pubInfo.setFullTextImaUrl(pub.getFullTextImgUrl());
        }
      } else {
        pubInfo.setFullTextImaUrl("/resscmwebsns/images_v5/images2016/file_img.jpg");
      }
      // 中英文标题逻辑
      pubInfo.setShowBrif(pub.getBriefDesc());
      pubInfo.setShowTitle(pub.getTitle());
      pubInfo.setAuthors(pub.getAuthorNames());
      pubInfo.setNoneHtmlLableAuthorNames(HtmlUtils.Html2Text(pub.getAuthorNames()));
      pubInfo.setPubIndexUrl(pub.getPubIndexUrl());
      pubInfo.setPubId(pub.getPubId());
      pubInfo.setDes3RecvPsnId(Des3Utils.encodeToDes3(pub.getOwnerPsnId().toString()));
      // 是否是自己的成果
      if (pub.getOwnerPsnId().equals(SecurityUtils.getCurrentUserId())) {
        pubInfo.setIsOwn(1);
      }
      pubInfo.setHasFulltext(pub.getHasFulltext());
      pubInfo.setFullTextUrl(pub.getFullTextDownloadUrl());
      pubInfo.setExistGrpPub(pub.getExistRepGrpPub());
      // 是否项目资助标注
      int labeld = isMarked(form.getGrpId(), pub.getPubId());
      pubInfo.setLabeld(labeld);
      pubInfo.setUpdateMark(pub.getUpdateMark());
      pubInfo.setFullTextPermission(pub.getFullTextPermission());
      showPubList.add(pubInfo);
    }
    form.getPage().setResult(showPubList);
  }

  private Integer isMarked(Long grpId, Long pubId) {
    String projectNo = this.grpBaseInfoDao.getProjectNo(grpId);
    if (StringUtils.isBlank(projectNo)) {
      return 0;
    }
    String fundInfo = "";
    Map<String, Object> object = getPubDetails(pubId);
    if (object != null) {
      fundInfo = String.valueOf(object.get("fundInfo"));
    }
    if (StringUtils.isBlank(fundInfo)) {
      return 0;
    }
    // 字段过滤
    fundInfo = XmlUtil.filterAuForCompare(fundInfo);
    projectNo = XmlUtil.filterAuForCompare(projectNo);
    /**
     * 2.将基金信息与项目批准号进行匹配 一个项目只对应一个资助基金号，而一个成果对应多个不同的资助基金。 所以用项目基金号fundInfo去匹配成果基金号projectNo
     */
    if (fundInfo.indexOf(projectNo) != -1) {
      return 1;
    }
    return 0;
  }

  private Map<String, Object> getPubDetails(Long pubId) {
    String SERVER_URL = this.domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
    Map<String, Object> object = new HashMap<String, Object>();
    try {
      Map<String, Object> map = new HashMap<>();
      map.put(GrpConstant.DES3_PUB_ID, Des3Utils.encodeToDes3(pubId.toString()));
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

  @Override
  public void getSelectPubsList(GrpPubForm form) throws Exception {
    // 查询成果
    getPsnPubListFromV8pub(form);
    if (CollectionUtils.isEmpty(form.getGrpPubInfoVOs())) {
      // 如果没有记录 直接放回
      return;
    }
    List<GrpPubShowInfo> showPubList = new ArrayList<GrpPubShowInfo>();
    form.setRole(grpRoleService.getGrpRole(form.getPsnId(), form.getGrpId()));
    for (GrpPubInfoVO pub : form.getGrpPubInfoVOs()) {
      GrpPubShowInfo pubInfo = grpPubShowInfoService.buildShowPubInfo(pub);
      // 是否是自己的成果
      if (pub.getOwnerPsnId().equals(SecurityUtils.getCurrentUserId())) {
        pubInfo.setIsOwn(1);
      }
      showPubList.add(pubInfo);
    }
    form.getPage().setResult(showPubList);
  }

  @Override
  public List<GrpPubShowInfo> getFiveGrpPubsForDiscuss(Long grpId) throws Exception {
    Integer grpCatetory = grpBaseInfoDao.getGrpCatetory(grpId);
    boolean isProjectGrp = false; // 是否是项目群组
    if (grpCatetory == 11) {
      isProjectGrp = true;
    }
    List<GrpPubShowInfo> grpPubShowInfoList = new ArrayList<GrpPubShowInfo>();
    GrpPubForm form = new GrpPubForm();
    form.setShowPrjPub(isProjectGrp ? 1 : 0);
    form.setGrpId(grpId);
    getGrpHomePubListFromV8pub(form);
    if (form.getGrpPubInfoVOs() != null && form.getGrpPubInfoVOs().size() > 0) {
      for (int i = 0; i < form.getGrpPubInfoVOs().size(); i++) {
        GrpPubInfoVO pub = form.getGrpPubInfoVOs().get(i);
        GrpPubShowInfo grpPubShowInfo = new GrpPubShowInfo();
        grpPubShowInfo.setPubId(pub.getPubId());
        grpPubShowInfo.setZhTitle(pub.getTitle());
        grpPubShowInfo.setAuthors(pub.getAuthorNames());
        grpPubShowInfo.setEnTitle(pub.getTitle());
        grpPubShowInfo.setZhBrif(pub.getBriefDesc());
        grpPubShowInfo.setEnBrif(pub.getBriefDesc());
        grpPubShowInfo.setNoneHtmlLableAuthorNames(HtmlUtils.Html2Text(pub.getAuthorNames()));
        // grpPubShowInfo.setCitedTimes(pub.getCitedTimes());
        grpPubShowInfoList.add(grpPubShowInfo);
      }
    }
    return grpPubShowInfoList;
  }

  @Override
  public void markGrpPub(GrpPubForm form) throws Exception {
    // 判断是标记为项目成果 还是文献
    GrpPubs temPubs = grpPubDao.getGrpPubs(form.getPubId(), form.getGrpId());
    if (temPubs != null) {
      if (temPubs.getIsProjectPub() == 1) {
        temPubs.setIsProjectPub(0);
      } else {
        temPubs.setIsProjectPub(1);
      }
      temPubs.setUpdateDate(new Date());
      temPubs.setUpdatePsnId(form.getPsnId());
      grpPubDao.save(temPubs);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public void deleteGrpPub(GrpPubForm form) throws Exception {
    // 删除群组成果,只是标记为删除
    String DEL_PUB_URL = domainscm + V8pubQueryPubConst.PUBHANDLER_URL;
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    // 构建成果保存对象
    Map<String, Object> data = new HashMap<>();
    data.put("pubHandlerName", "deleteGroupPubHandler");
    data.put("des3PubId", Des3Utils.encodeToDes3(String.valueOf(form.getPubId())));
    data.put("des3GrpId", form.getDes3GrpId());
    HttpEntity<String> requestEntity = new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(data), requestHeaders);
    restTemplate.postForObject(DEL_PUB_URL, requestEntity, Object.class);
  }

  @Override
  public boolean checkPubIsOwner(Long psnId, Long pubId) throws Exception {
    // 判断群组成果是否属于当前人
    Long ownerPsnId = psnPubDAO.getPsnOwner(pubId);
    if (ownerPsnId == null || ownerPsnId == 0L) {
      GroupPubPO groupPub = groupPubsDAO.findByPubId(pubId);
      if (groupPub != null) {
        ownerPsnId = groupPub.getOwnerPsnId();
      }
    }
    if (ownerPsnId.equals(psnId)) {
      return true;
    }
    return false;
  }

  @Override
  public void saveGrpPubs(GrpPubs grpPubs) throws Exception {
    // 构建必要的数据
    Date currDate = new Date();
    grpPubs.setCreateDate(currDate);
    grpPubs.setStatus(0);
    grpPubs.setUpdateDate(currDate);
    grpPubs.setLabeled(this.groupPubIsLabeled(grpPubs));
    // 修改统计数
    GrpStatistics grpS = grpStatisticsDao.get(grpPubs.getGrpId());
    if (grpS == null) {
      grpS = new GrpStatistics();
    }
    grpS.setSumPubs(grpS.getSumPubs() + 1);
    grpStatisticsDao.save(grpS);
    // 初始化pubList
    PublicationList pubList = publicationListDao.get(grpPubs.getPubId());
    if (pubList == null) {
      pubList = new PublicationList();
      pubList.setId(grpPubs.getPubId());
      publicationListDao.save(pubList);
    }
    // TODO 算 相关度
    // TODO 算是否标注
    grpPubDao.save(grpPubs);
    // SCM-12895 新加群组成果生成短地址
    try {
      this.buildUrlData(grpPubs.getCreatePsnId(), grpPubs.getGrpId(), grpPubs.getPubId(), ShortUrlConst.B_TYPE);
    } catch (Exception e) {
      logger.error("生成群组成果短地址出错,PubId=" + grpPubs.getPubId() + "grpId:" + grpPubs.getGrpId(), e);
    }
    // 产生群组动态
    // 发一个群组动态
    Map<String, Object> groupDynMap = new HashMap<String, Object>();
    groupDynMap.put("groupId", grpPubs.getGrpId());
    groupDynMap.put("psnId", grpPubs.getCreatePsnId());
    groupDynMap.put("resType", "pub");
    groupDynMap.put("resId", grpPubs.getPubId());
    groupDynMap.put("tempType", "GRP_ADDPUB");
    restTemplate.postForObject(this.groupDynRestfulUrl, groupDynMap, Object.class);
  }

  /**
   * 构建参数
   * 
   * @param type
   * @return
   */
  @SuppressWarnings("unchecked")
  public Map<String, Object> buildParameters(Long GrpId, Long PubId, String type) {
    Map<String, Object> map = new HashedMap();
    Map<String, Object> dataMap = new HashedMap();
    Map<String, Object> shortUrlParametMap = new HashedMap();
    map.put("openid", "99999999");
    map.put("token", "00000000sht22url");
    dataMap.put("createPsnId", "0");
    dataMap.put("type", type);
    shortUrlParametMap.put("des3GrpId", ServiceUtil.encodeToDes3(GrpId.toString()));
    shortUrlParametMap.put("des3PubId", ServiceUtil.encodeToDes3(PubId.toString()));
    dataMap.put("shortUrlParamet", JacksonUtils.mapToJsonStr(shortUrlParametMap));
    map.put("data", JacksonUtils.mapToJsonStr(dataMap));
    return map;
  }

  @SuppressWarnings("unchecked")
  public void buildUrlData(Long psnId, Long GrpId, Long PubId, String type) throws Exception {
    String shortUrl = "";
    // 构建参数
    Map<String, Object> parameters = this.buildParameters(GrpId, PubId, type);
    // 访问Open系统接口获取ShortUrl
    Object obj = restTemplate.postForObject(this.SERVER_URL, parameters, Object.class);
    // 接口返回数据处理
    Map<String, Object> objMap = JacksonUtils.jsonToMap(obj.toString());
    // 获取短地址值
    if (objMap.get("result") != null) {
      List<Map<String, Object>> list = (List<Map<String, Object>>) objMap.get("result");
      if (list != null && list.size() > 0 && list.get(0).get("shortUrl") != null) {
        shortUrl = list.get(0).get("shortUrl").toString();
      }
    }
    try {
      if (grpPubIndexUrlDao.findByGrpIdAndPubId(GrpId, PubId) == null) {
        GrpPubIndexUrl gpu = new GrpPubIndexUrl();
        gpu.setPsnId(psnId);
        gpu.setGrpId(GrpId);
        gpu.setPubId(PubId);
        gpu.setPubIndexUrl(shortUrl);
        gpu.setUpdateDate(new Date());
        grpPubIndexUrlDao.save(gpu);
      }
    } catch (Exception e) {
      logger.error("保存群组成果短地址出错,PubId=" + PubId + "grpId:" + GrpId, e);
    }
  }

  @Override
  public void importMemberPub(GrpPubForm form) throws Exception {
    if (StringUtils.isNoneBlank(form.getPubIds())) {
      String[] pubIdStrs = URLDecoder.decode(form.getPubIds(), "utf-8").split(",");
      for (String pubIdStr : pubIdStrs) {
        Long pubId = null;
        if (NumberUtils.isNumber(pubIdStr)) {
          pubId = Long.parseLong(pubIdStr);
        } else {
          pubId = NumberUtils.toLong(Des3Utils.decodeFromDes3(pubIdStr));
        }
        if (pubId == null || pubId == 0L) {
          return;
        }
        Map<String, Object> pubInfo = getPubInfoByPubId(pubId);
        if (pubInfo.get("pubId") == null) {
          return;
        }
        GrpPubs grpPubs = grpPubDao.getGrpPubsByPubIdAndGrpId(pubId, form.getGrpId());
        if (grpPubs != null) {
          grpPubs.setStatus(0);
        } else {
          grpPubs = new GrpPubs();
          grpPubs.setGrpId(form.getGrpId());
          grpPubs.setCreatePsnId(form.getPsnId());
          grpPubs.setPubId(pubId);
        }
        grpPubs.setOwnerPsnId(NumberUtils.toLong(pubInfo.get("pubOwnerPsnId").toString()));
        if (form.getSavePubType() == 1) {
          grpPubs.setIsProjectPub(1);
        } else {
          grpPubs.setIsProjectPub(0);
        }
        // 设置相关度
        Object pubkeyWordsObj = pubInfo.get("keywords");
        String pubkeyWords = pubkeyWordsObj == null ? "" : pubkeyWordsObj.toString();
        grpPubs.setRelevance(this.getPubGroupShareKwCount(grpPubs, pubkeyWords));
        // 是否项目资助标注
        int labeld = isMarked(form.getGrpId(), pubId);
        grpPubs.setLabeled(labeld);
        this.saveNewGrpPubs(grpPubs);
        autoConfirmImportedRCMDPub(pubInfo, form.getGrpId());
      }
    }
  }

  /**
   * 自动确认当前导入的成员的成果中与当前群组推荐成果推荐列表中查重的成果
   * 
   * @throws Exception
   */
  private void autoConfirmImportedRCMDPub(Map<String, Object> pubInfo, Long groupId) throws Exception {
    List<Long> dupPubIds = getDupPubIds(pubInfo);
    if (CollectionUtils.isNotEmpty(dupPubIds)) {
      for (Long dupPubId : dupPubIds) {
        GrpPubRcmd grpPubRcmd = grpPubRcmdDao.getGrpPubRcmd(dupPubId, groupId);
        if (grpPubRcmd != null) {// 存在与当前导入的成果相同的推荐成果,将该推荐成果自动设置为已经确认
          grpPubRcmd.setStatus(1);
          grpPubRcmd.setUpdateDate(new Date());
          grpPubRcmdDao.update(grpPubRcmd);
        }
      }
    }
  }

  /**
   * 获取当前基准库库成果查重得到的重复成果id
   * 
   * @param pubInfo
   * @return
   */
  @SuppressWarnings("unchecked")
  private List<Long> getDupPubIds(Map<String, Object> pubInfo) throws Exception {
    String title = pubInfo.get("title") == null ? "" : pubInfo.get("title").toString();
    String DOI = pubInfo.get("doi") == null ? "" : pubInfo.get("doi").toString();
    Integer pubType = pubInfo.get("pubType") == null ? 7 : Integer.parseInt(pubInfo.get("pubType").toString());
    Integer pubYear =
        pubInfo.get("publishYear") == null ? null : Integer.parseInt(pubInfo.get("publishYear").toString());
    Integer srcDbId = pubInfo.get("srcDbId") == null ? null : Integer.parseInt(pubInfo.get("srcDbId").toString());
    String sourceId = pubInfo.get("sourceId") == null ? "" : pubInfo.get("sourceId").toString();
    String applicationNo = "", publicationOpenNo = "", standardNo = "", registerNo = "";
    Map<String, Object> pubTypeInfo = (Map<String, Object>) pubInfo.get("pubTypeInfo");// 获取成果对应的成果类型详细信息
    if (pubTypeInfo != null && !pubInfo.isEmpty()) {
      applicationNo = pubTypeInfo.get("applicationNo") == null ? "" : pubTypeInfo.get("applicationNo").toString();
      publicationOpenNo =
          pubTypeInfo.get("publicationOpenNo") == null ? "" : pubTypeInfo.get("publicationOpenNo").toString();
      standardNo = pubTypeInfo.get("standardNo") == null ? "" : pubTypeInfo.get("standardNo").toString();
      registerNo = pubTypeInfo.get("registerNo") == null ? "" : pubTypeInfo.get("registerNo").toString();
    }
    List<Long> dupPubIds = new ArrayList<>();
    Map<String, Object> parmaMap = new HashMap<>();
    parmaMap.put("pubGener", 3);// 3表示在基准库进行成果查重,参考 PubGenreConstants.java
    parmaMap.put("title", title);
    parmaMap.put("pubYear", pubYear);
    parmaMap.put("pubType", pubType);
    parmaMap.put("sourceId", sourceId);
    parmaMap.put("srcDbId", srcDbId);
    parmaMap.put("applicationNo", applicationNo);
    parmaMap.put("publicationOpenNo", publicationOpenNo);
    parmaMap.put("standardNo", standardNo);
    parmaMap.put("registerNo", registerNo);
    parmaMap.put("doi", DOI);
    parmaMap.put("des3PsnId", Des3Utils.encodeToDes3(pubInfo.get("pubOwnerPsnId").toString()));
    String SERVER_URL = domainscm + V8pubQueryPubConst.PUB_DUPCHECK_URL;
    Map<String, Object> remoteInfo = (Map<String, Object>) RestUtils.getRemoteInfo(parmaMap, SERVER_URL, restTemplate);
    if ("SUCCESS".equalsIgnoreCase(remoteInfo.get("status").toString())) {
      Object msgList = remoteInfo.get("msgList");// 当有多个重复的时候会保存在msgList中，以"，"分割
      if (msgList != null) {
        String[] split = msgList.toString().split(",");
        if (split != null && split.length > 0) {
          for (String pubId : split) {
            if (StringUtils.isNotBlank(pubId) && NumberUtils.isCreatable(pubId)) {
              dupPubIds.add(NumberUtils.toLong(pubId));
            }
          }
        }
      } else {
        Object msg = remoteInfo.get("msg");// 当只有一个重复时，保存在msg中
        if (msg != null) {
          dupPubIds.add(NumberUtils.toLong(msg.toString()));
        }
      }
    }
    return dupPubIds;
  }

  public Integer isPorject(Long pubId) throws Exception {
    String pubFundInfo = this.getPubFundInfo(pubId);
    if (StringUtils.isEmpty(pubFundInfo)) {
      return 0;
    } else {
      return 1;
    }
  }

  public void saveNewGrpPubs(GrpPubs grpPubs) {
    // 构建必要的数据
    Date currDate = new Date();
    grpPubs.setCreateDate(currDate);
    grpPubs.setStatus(0);
    grpPubs.setUpdateDate(currDate);
    // 修改统计数
    GrpStatistics grpS = grpStatisticsDao.get(grpPubs.getGrpId());
    if (grpS == null) {
      grpS = new GrpStatistics();
    }
    grpS.setSumPubs(grpS.getSumPubs() + 1);
    grpStatisticsDao.save(grpS);
    grpPubDao.save(grpPubs);
    // SCM-12895 新加群组成果生成短地址
    try {
      this.buildUrlData(grpPubs.getCreatePsnId(), grpPubs.getGrpId(), grpPubs.getPubId(), ShortUrlConst.B_TYPE);
    } catch (Exception e) {
      logger.error("生成群组成果短地址出错,PubId=" + grpPubs.getPubId() + "grpId:" + grpPubs.getGrpId(), e);
    }
    // 产生群组动态
    // 发一个群组动态
    Map<String, Object> groupDynMap = new HashMap<String, Object>();
    groupDynMap.put("groupId", grpPubs.getGrpId());
    groupDynMap.put("psnId", grpPubs.getCreatePsnId());
    groupDynMap.put("resType", "pub");
    groupDynMap.put("resId", grpPubs.getPubId());
    groupDynMap.put("tempType", "GRP_ADDPUB");
    restTemplate.postForObject(this.groupDynRestfulUrl, groupDynMap, Object.class);
  }

  /* 成果关键词与群组关键词对应的数量，标识此成果与群组的相关度 */
  @Override
  public Integer getPubGroupShareKwCount(GrpPubs grPub, String pubKeyWords) throws Exception {
    Integer count = 0;
    Long pubId = grPub.getPubId();
    Long groupId = grPub.getGrpId();
    try {
      if (StringUtils.isEmpty(pubKeyWords)) {
        return count;
      }
      List<String> kwStrList = this.getGroupKw(groupId);
      if (CollectionUtils.isEmpty(kwStrList)) {
        logger.debug("群组关键词为空, 群组Id :" + groupId);
        return count;
      }
      // 过滤
      pubKeyWords = this.filterKwString(pubKeyWords);
      kwStrList = this.filterKwStringList(kwStrList);
      // 获取与群组关键词相同的词数
      Integer zhCount = this.getShareKwCount(pubKeyWords, kwStrList);
      return zhCount;
    } catch (Exception e) {
      throw new Exception("getPubGroupShareKwCount计算群组与群组成果相同关键字出错， pubId ： " + pubId + " === " + e);
    }
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

  /** 1成果基金信息与群组对应上，已标注；0未对应上，未标注 */
  @Override
  public Integer groupPubIsLabeled(GrpPubs grpPub) throws Exception {
    Integer isLabeled = 0;
    Long pubId = grpPub.getPubId();
    Long groupId = grpPub.getGrpId();
    try {
      String pubFundInfo = this.getPubFundInfo(pubId);
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
   * 群组中获取个人成果统计数
   */
  @Override
  public void getPsnPubsCallBack(GrpPubForm form) throws Exception {
    // 设置默认参数
    Map<String, Object> map = new HashMap<String, Object>();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    buildPubQueruDTO(form, pubQueryDTO);
    pubQueryDTO.setSearchPsnId(form.getPsnId());
    String SERVER_URL = this.domainscm + GrpConstant.PSN_PUB_COUNT_URL;
    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> requestEntity =
        new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(pubQueryDTO), requestHeaders);
    Map<String, Object> object =
        (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    if (object != null && object.get("status").equals("success")) {
      map = (Map<String, Object>) object.get("resultCount");
    }
    form.setResult2Map(map);
  }

  @Override
  public void getPubsCallBack(GrpPubForm form) throws Exception {
    // 设置默认参数
    Map<String, Object> map = new HashMap<String, Object>();
    List<GrpPubInfoVO> list = new ArrayList<>();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    buildPubQueruDTO(form, pubQueryDTO);
    String SERVER_URL = this.domainscm + GrpConstant.GRP_PUB_COUNT_URL;
    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> requestEntity =
        new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(pubQueryDTO), requestHeaders);
    Map<String, Object> object =
        (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    if (object != null && object.get("status").equals("success")) {
      map = (Map<String, Object>) object.get("resultCount");
    }
    form.setResult2Map(map);
  }

  /**
   * 构建成果查询传输对象
   * 
   * @param form
   * @param pubQueryDTO
   */
  private void buildPubQueruDTO(GrpPubForm form, PubQueryDTO pubQueryDTO) {
    pubQueryDTO.setPageNo(form.getPage().getParamPageNo());
    pubQueryDTO.setSearchGrpId(form.getGrpId());
    pubQueryDTO.setOrderBy(form.getOrderBy());
    pubQueryDTO.setIncludeType(form.getIncludeType());
    pubQueryDTO.setPubType(form.getPubType());
    pubQueryDTO.setPublishYear(form.getPublishYear());
    pubQueryDTO.setSearchPrjPub(form.getShowPrjPub() == 1 ? true : false);
    pubQueryDTO.setSearchRefPub(form.getShowRefPub() == 1 ? true : false);
    pubQueryDTO.setSearchGrpMemberId(form.getMemberId());
    pubQueryDTO.setSearchKey(form.getSearchKey());
  }

  /**
   * 得到群组成果
   * 
   * @param form
   * @throws Exception
   */
  public void getGrpPubsFromV8pub(GrpPubForm form) throws Exception {
    List<GrpPubInfoVO> list = new ArrayList<>();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    buildPubQueruDTO(form, pubQueryDTO);
    pubQueryDTO.setPsnId(form.getPsnId());
    pubQueryDTO.setServiceType("grpPubList");
    String SERVER_URL = this.domainscm + GrpConstant.PUB_LIST_URL;
    parsePubResult(form, list, pubQueryDTO, SERVER_URL);
    form.setGrpPubInfoVOs(list);
  }

  /**
   * 得到成员的个人成果
   * 
   * @param form
   * @throws Exception
   */
  public void getPsnPubsFromV8pub(GrpPubForm form) throws Exception {
    List<GrpPubInfoVO> list = new ArrayList<>();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    buildPubQueruDTO(form, pubQueryDTO);
    pubQueryDTO.setSearchPsnId(form.getPsnId());
    pubQueryDTO.setServiceType("importMemberPubList");
    String SERVER_URL = this.domainscm + GrpConstant.PUB_LIST_URL;
    parsePubResult(form, list, pubQueryDTO, SERVER_URL);
    form.setGrpPubInfoVOs(list);
  }

  /**
   * 动态，消息中我的成果列表,群组成果
   * 
   * @param form
   * @throws Exception
   */
  public void getPsnPubListFromV8pub(GrpPubForm form) throws Exception {
    List<GrpPubInfoVO> list = new ArrayList<>();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    buildPubQueruDTO(form, pubQueryDTO);
    pubQueryDTO.setSearchPsnId(form.getPsnId());
    pubQueryDTO.setSearchGrpId(form.getGrpId());
    pubQueryDTO.setServiceType("dynMyPub");
    String SERVER_URL = this.domainscm + GrpConstant.PUB_LIST_URL;
    parsePubResult(form, list, pubQueryDTO, SERVER_URL);
    form.setGrpPubInfoVOs(list);
  }

  /**
   * 群组首页的成果列表
   * 
   * @param form
   * @throws Exception
   */
  public void getGrpHomePubListFromV8pub(GrpPubForm form) throws Exception {
    List<GrpPubInfoVO> list = new ArrayList<>();
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    buildPubQueruDTO(form, pubQueryDTO);
    pubQueryDTO.setSearchPsnId(form.getPsnId());
    pubQueryDTO.setServiceType("grpHomepagePub");
    String SERVER_URL = this.domainscm + GrpConstant.PUB_LIST_URL;
    parsePubResult(form, list, pubQueryDTO, SERVER_URL);
    form.setGrpPubInfoVOs(list);
  }

  /**
   * 解析返回的结果
   * 
   * @param form
   * @param list
   * @param pubQueryDTO
   * @param SERVER_URL
   * @throws IllegalAccessException
   * @throws InvocationTargetException
   */
  private void parsePubResult(GrpPubForm form, List<GrpPubInfoVO> list, PubQueryDTO pubQueryDTO, String SERVER_URL)
      throws IllegalAccessException, InvocationTargetException {
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
          GrpPubInfoVO vo = new GrpPubInfoVO();
          BeanUtils.populate(vo, map);
          list.add(vo);
        }
      }
    } else {
      form.getPage().setTotalCount(0);
    }
  }

  /**
   * 通过pubId 获取pubInfo信息
   * 
   * @param pubId
   * @throws Exception
   */
  @SuppressWarnings("unchecked")
  public Map<String, Object> getPubInfoByPubId(Long pubId) throws Exception {
    // 设置默认参数
    Map<String, Object> paramMap = new HashMap<String, Object>();
    String SERVER_URL = this.domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
    paramMap.put("des3PubId", Des3Utils.encodeToDes3(String.valueOf(pubId)));
    paramMap.put("serviceType", "snsPub");
    return (Map<String, Object>) RestUtils.getRemoteInfo(paramMap, SERVER_URL, restTemplate);
  }

  @Override
  public Long getGroupPubCounts(Long grpId) throws Exception {
    Long grpPubscount = 0L;
    grpPubscount = grpPubDao.getGrpPubsSum(grpId);
    return grpPubscount;
  }
}
