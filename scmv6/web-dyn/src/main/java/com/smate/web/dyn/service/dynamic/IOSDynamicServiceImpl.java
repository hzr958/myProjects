package com.smate.web.dyn.service.dynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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

import com.smate.core.base.app.dao.AppVersionRecordDao;
import com.smate.core.base.app.model.AppVersionRecord;
import com.smate.core.base.dyn.consts.DynamicConstants;
import com.smate.core.base.dyn.dao.DynamicContentDao;
import com.smate.core.base.dyn.dao.DynamicMsgDao;
import com.smate.core.base.pub.enums.PubDbEnum;
import com.smate.core.base.utils.constant.dyn.DynTemplateConstant;
import com.smate.core.base.utils.exception.DynException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.dyn.dao.fund.MyFundDao;
import com.smate.web.dyn.dao.pub.CollectedPubDao;
import com.smate.web.dyn.form.dynamic.DynReplayInfo;
import com.smate.web.dyn.form.dynamic.DynamicForm;
import com.smate.web.dyn.model.dynamic.IOSDynamicInfo;
import com.smate.web.dyn.model.fund.MyFund;
import com.smate.web.dyn.service.statistics.DynStatisticsService;

/**
 * IOS动态相关服务
 * 
 * @author LJ
 *
 */
@Service("iosDynamicService")
@Transactional(rollbackFor = Exception.class)
public class IOSDynamicServiceImpl implements IOSDynamicService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private DynamicMsgDao dynamicMsgDao;
  @Autowired
  private DynamicContentDao dynamicContentDao;
  @Autowired
  private DynStatisticsService dynStatisticsService;
  @Autowired
  private DynamicAwardService dynamicAwardService;
  @Autowired
  private DynamicQuickShareService dynamicQuickShareService;
  @Autowired
  private DynamicReplyService dynamicReplyService;
  @Autowired
  private MyFundDao MyFundDao;
  @Autowired
  private CollectedPubDao collectedPubDao;
  @Autowired
  private AppVersionRecordDao appVersionRecordDao;
  @Autowired
  private RestTemplate restTemplate;
  @Value("${domainscm}")
  private String domainscm;

  @Override
  public List<IOSDynamicInfo> dynamicShow(Long psnId, Integer pageSize, Integer pageNumber) throws Exception {
    List<Long> dynIds = dynamicMsgDao.getDynListForPsnAll(psnId, pageSize, pageNumber);
    if (CollectionUtils.isNotEmpty(dynIds)) {
      return handleDynamicMsg(dynIds, psnId);
    }
    return null;
  }

  /**
   * 对动态信息进行处理
   * 
   * @param dynIds
   * @param psnId
   * @return
   * @throws DynException
   */
  private List<IOSDynamicInfo> handleDynamicMsg(List<Long> dynIds, Long psnId) throws DynException {
    List<IOSDynamicInfo> list = new ArrayList<IOSDynamicInfo>();
    for (Long dynId : dynIds) {
      try {
        IOSDynamicInfo dyninfo = this.buildDynInfo(dynId, psnId);
        if (dyninfo != null) {
          // 处理赞评论分享数据
          String dynStatisticsIds = Des3Utils.encodeToDes3(dynId.toString());
          Map<String, Map<String, Object>> dynStatisMap =
              dynStatisticsService.getDynStatistics(dynStatisticsIds, psnId);
          dyninfo = this.dealStatistics(dynStatisMap, psnId, dyninfo);
          list.add(dyninfo);
        }
      } catch (Exception e) {
        logger.error("app遍历获取动态出错，dynId=" + dynId, e);
      }
    }
    return list;
  }

  /**
   * 处理统计数，赞状态
   * 
   * @param dynStatisMap
   * @param psnId
   * @param dyninfo
   * @return
   */
  public IOSDynamicInfo dealStatistics(Map<String, Map<String, Object>> dynStatisMap, Long psnId,
      IOSDynamicInfo dyninfo) {
    try {
      boolean hasAward;
      hasAward = dynamicAwardService.getPsnHasAward(psnId, dyninfo.getDynId());
      dyninfo.setAwardStatus(hasAward + "");
      // 处理统计数
      dyninfo.setAwardCount(getDynStatisCount(dynStatisMap, dyninfo.getDynId(), "awardCount"));
      dyninfo.setCommentCount(getDynStatisCount(dynStatisMap, dyninfo.getDynId(), "commentCount"));
      dyninfo.setShareCount(getDynStatisCount(dynStatisMap, dyninfo.getDynId(), "shareCount"));
      dyninfo.setResDelete(getResHasDelete(dynStatisMap, dyninfo.getDynId(), "resDelete"));
      if (Des3Utils.encodeToDes3(Objects.toString(psnId)).equals(dyninfo.getDes3PubOwnerPsnId())) {
        dyninfo.setFulltextPermission(0);
      } else {
        dyninfo.setFulltextPermission(getResHasDelete(dynStatisMap, dyninfo.getDynId(), "permission"));
      }
      dyninfo.setCollectStatus(getResHasDelete(dynStatisMap, dyninfo.getDynId(), "collected"));
      dyninfo.setPublishDate(Objects.toString(dynStatisMap.get(dyninfo.getDynId().toString()).get("publishDate"), ""));

    } catch (Exception e) {
      logger.error("获取初始化赞，评论，分享数据出错：dynid:" + dyninfo.getDynId(), e);
    }

    return dyninfo;

  }

  /**
   * 解析资源是否删除
   * 
   * @param dynStatisMap
   * @param strCount
   * @param dyninfo
   * @return
   */
  public Integer getResHasDelete(Map<String, Map<String, Object>> dynStatisMap, Long dynId, String strCount) {
    String string = Objects.toString(dynStatisMap.get(dynId.toString()).get(strCount), "");
    try {
      if (StringUtils.isNotEmpty(string)) {
        return Integer.valueOf(string);
      }
    } catch (Exception e) {
      logger.error("app获取首页动态赞、分享、评论计数错误！", e);
    }
    return 0;

  }

  /**
   * 解析动态获取统计数
   * 
   * @param dynStatisMap
   * @param strCount
   * @param dyninfo
   * @return
   */
  public Integer getDynStatisCount(Map<String, Map<String, Object>> dynStatisMap, Long dynId, String strCount) {
    String string = Objects.toString(dynStatisMap.get(dynId.toString()).get(strCount), "");
    try {
      if (StringUtils.isNotEmpty(string)) {
        return Integer.valueOf(string);
      }
    } catch (Exception e) {
      logger.error("app获取首页动态赞、分享、评论计数错误！", e);
    }
    return 0;

  }

  /**
   * 获取Map值
   * 
   * @param Map<String, Object>
   * @param key
   * @return String
   */
  public String getStringValue(Map<String, Object> dataMap, String string) {
    String value = "";
    try {
      value = Objects.toString(dataMap.get(string), "");
    } catch (NullPointerException e) {
      logger.error("该条动态" + string + "属性值为空,dynid:" + dataMap.get(DynTemplateConstant.DYN_ID), e);
    } catch (Exception e) {
      logger.error("获取该条动态" + string + "属性值出错,dynId:" + dataMap.get(DynTemplateConstant.DYN_ID), e);
    }
    return value;

  }

  /**
   * 获取Map值
   * 
   * @param Map<String, Object>
   * @param key
   * @return Long
   */
  public Long getLongValue(Map<String, Object> dataMap, String string) {
    Long value = 0L;
    try {
      value = Long.valueOf(dataMap.get(string).toString());
    } catch (NullPointerException e) {
      logger.error("该条动态" + string + "属性值为空,dynid:" + dataMap.get(DynTemplateConstant.DYN_ID), e);
    } catch (Exception e) {
      logger.error("获取该条动态" + string + "属性值出错,dynId:" + dataMap.get(DynTemplateConstant.DYN_ID), e);
    }
    return value;

  }

  /**
   * 获取Map值
   * 
   * @param Map<String, Object>
   * @param key
   * @return Integer
   */
  public Integer getIntValue(Map<String, Object> dataMap, String string) {
    Integer value = 0;
    try {
      value = Integer.valueOf(dataMap.get(string).toString());
    } catch (NullPointerException e) {
      logger.error("该条动态" + string + "属性值为空,dynid:" + dataMap.get(DynTemplateConstant.DYN_ID), e);
    } catch (Exception e) {
      logger.error("获取该条动态" + string + "属性值出错,dynId:" + dataMap.get(DynTemplateConstant.DYN_ID), e);
    }
    return value;

  }

  /**
   * 分动态类型转换为对象保存 2018-3-30 代码结构需要优化
   * 
   * @param dynData
   * @return IOSDynamicInfo
   */
  @SuppressWarnings("unchecked")
  public IOSDynamicInfo JsonToObj(String dynData, Long psnId) throws Exception {

    IOSDynamicInfo dynInfo = new IOSDynamicInfo();
    Map<String, Object> dataMap = JacksonUtils.jsonToMap(dynData);
    if (dataMap == null) {
      return null;
    }
    // TODO 项目类型当前这个版本暂时不显示
    /*
     * if (getIntValue(dataMap, DynTemplateConstant.RES_TYPE).equals(DynamicConstants.RES_TYPE_PRJ)) {
     * return null; }
     */

    // 根据原有动态类型构建数据
    String type = checkType(dataMap.get(DynTemplateConstant.DYN_TEMPLATE_ZH).toString());
    dynInfo.setDynType(type);
    // 处理B1类型
    if (DynTemplateConstant.B1TEMP.equals(type)) {
      return this.buildB1DynInfo(dataMap, psnId);
    }
    dynInfo.setDynId(getLongValue(dataMap, DynTemplateConstant.DYN_ID));

    // 处理A类型
    if (DynTemplateConstant.ATEMP.equals(type)) {
      dynInfo = this.buildBaseInfo(dynInfo, dataMap);
      // 获取基金动态信息
      if (getIntValue(dataMap, DynTemplateConstant.RES_TYPE).equals(DynamicConstants.RES_TYPE_FUND)) {
        dynInfo = this.buildFundInfo(dynInfo, dataMap, psnId);
      }
      // 获取成果动态信息
      else {
        dynInfo = this.buildPubInfo(dynInfo, dataMap, psnId);
      }
      dynInfo.setParentDynId(getLongValue(dataMap, DynTemplateConstant.PARENT_DYN_ID));
      dynInfo.setUserAddContent(getStringValue(dataMap, DynTemplateConstant.USER_ADD_CONTENT));
      dynInfo.setResId(getLongValue(dataMap, DynTemplateConstant.RES_ID));
      dynInfo.setResType(getIntValue(dataMap, DynTemplateConstant.RES_TYPE));

    }
    // 处理B2、B3类型
    else if (DynTemplateConstant.B2TEMP.equals(type) || DynTemplateConstant.B3TEMP.equals(type)) {
      dynInfo = this.buildBaseInfo(dynInfo, dataMap);

      // 获取基金动态信息
      if (getIntValue(dataMap, DynTemplateConstant.RES_TYPE).equals(DynamicConstants.RES_TYPE_FUND)) {
        dynInfo = this.buildFundInfo(dynInfo, dataMap, psnId);
      }
      // 获取成果动态信息
      else {
        dynInfo = this.buildPubInfo(dynInfo, dataMap, psnId);
      }

      if (DynTemplateConstant.B2TEMP.equals(type)) {
        Integer operateStatus = getIntValue(dataMap, DynTemplateConstant.OPERATE_STATUS);
        dynInfo.setOperateStatus(operateStatus);
        if (operateStatus == 1) {
          dynInfo.setOperatorComment(getStringValue(dataMap, DynTemplateConstant.OPERATOR_COMMENT));
        }
      }
      dynInfo.setResId(getLongValue(dataMap, DynTemplateConstant.RES_ID));
      dynInfo.setDes3ResId(getStringValue(dataMap, DynTemplateConstant.DES3_RES_ID));
      dynInfo.setResType(getIntValue(dataMap, DynTemplateConstant.RES_TYPE));
      dynInfo.setParentDynId(getLongValue(dataMap, DynTemplateConstant.PARENT_DYN_ID));
      dynInfo.setOperatorTypeEn(getStringValue(dataMap, DynTemplateConstant.OPERATOR_TYPE_EN));
      dynInfo.setOperatorTypeZh(getStringValue(dataMap, DynTemplateConstant.OPERATOR_TYPE_ZH));
    }
    return dynInfo;
  }

  /**
   * 处理B1类型数据
   * 
   * @return
   * @throws Exception
   */

  @SuppressWarnings({"unchecked"})
  public IOSDynamicInfo buildB1DynInfo(Map<String, Object> dataMap, Long psnId) throws Exception {
    IOSDynamicInfo dynInfo = new IOSDynamicInfo();
    try {
      dynInfo = this.buildBaseInfo(dynInfo, dataMap);
      Long dynId = getLongValue(dataMap, DynTemplateConstant.DYN_ID);
      String optTypeZh = getStringValue(dataMap, DynTemplateConstant.OPERATOR_TYPE_ZH);
      String optTypeEn = getStringValue(dataMap, DynTemplateConstant.OPERATOR_TYPE_EN);
      Object obj = dataMap.get("USER_ADD_CONTENT");
      if (obj != null && obj.toString().trim() != "") {
        dynInfo.setReContent(obj.toString().trim());
      }
      dynInfo.setDynId(dynId);
      dynInfo.setDes3PsnAId(getStringValue(dataMap, DynTemplateConstant.DES3_PSN_A_ID));
      dynInfo = this.buildPubInfo(dynInfo, dataMap, psnId);
      dynInfo.setParentDynId(getLongValue(dataMap, DynTemplateConstant.PARENT_DYN_ID));
      dynInfo.setOperateStatus(getIntValue(dataMap, DynTemplateConstant.OPERATE_STATUS));
      dynInfo.setOperatorTypeEn(optTypeEn);
      dynInfo.setOperatorTypeZh(optTypeZh);
      dynInfo.setDynType(DynTemplateConstant.B1TEMP);
      // 如果操作状态为空则是个人新发布的动态，否则则是生成的动态需要获取源动态信息
      if (dynInfo.getOperatorTypeZh() != null || dynInfo.getOperatorTypeEn() != null) {
        // 根据parentDynID获取源动态信息
        Long parentDynId = getLongValue(dataMap, DynTemplateConstant.PARENT_DYN_ID);
        if (parentDynId == null || parentDynId == 0L) {
          logger.error("parentDynId为空,动态信息构造出错！dynid:" + dynId);
          return null;
        }
        IOSDynamicInfo newDynInfo = this.buildParentDyn(parentDynId, psnId);
        newDynInfo.setDynType(DynTemplateConstant.B1TEMP);
        newDynInfo.setDynId(dynId);
        newDynInfo.setOperatorTypeEn(optTypeEn);
        newDynInfo.setOperatorTypeZh(optTypeZh);
        newDynInfo.setOperateStatus(dynInfo.getOperateStatus());
        newDynInfo.setOperatorComment(dynInfo.getOperatorComment());
        newDynInfo.setDes3ProducerPsnId(dynInfo.getDes3ProducerPsnId());
        newDynInfo.setPersonAvatars(dynInfo.getPersonAvatars());
        newDynInfo.setPersonNameZh(dynInfo.getPersonNameZh());
        newDynInfo.setPersonNameEn(dynInfo.getPersonNameEn());
        newDynInfo.setPersonInsInfoZh(dynInfo.getPersonInsInfoZh());
        newDynInfo.setPersonInsInfoEn(dynInfo.getPersonInsInfoEn());
        newDynInfo.setDes3PsnAId(dynInfo.getDes3PsnAId());
        newDynInfo.setParentDynId(dynInfo.getParentDynId());
        newDynInfo.setReContent(dynInfo.getReContent());
        String newDynData = dynamicContentDao.getDynInfo(getLongValue(dataMap, DynTemplateConstant.PARENT_DYN_ID));
        Map<String, Object> newDataMap = JacksonUtils.jsonToMap(newDynData);
        if (newDataMap == null) {
          return null;
        }
        newDynInfo.setSrcdes3ProducerPsnId(getStringValue(newDataMap, DynTemplateConstant.DES3_PRODUCER_PSN_ID));
        newDynInfo.setSrcpersonAvatars(getStringValue(newDataMap, DynTemplateConstant.PERSON_AVATARS));
        newDynInfo.setSrcPsnNameEn(getStringValue(newDataMap, DynTemplateConstant.PERSON_NAME_EN));
        newDynInfo.setSrcPsnNameZh(getStringValue(newDataMap, DynTemplateConstant.PERSON_NAME_ZH));
        newDynInfo.setSrcPsnInsInfoZh(getStringValue(newDataMap, DynTemplateConstant.PERSON_INSINFO_ZH));
        newDynInfo.setSrcPsnInsInfoEn(getStringValue(newDataMap, DynTemplateConstant.PERSON_INSINFO_EN));
        newDynInfo = this.buildPubInfo(newDynInfo, newDataMap, psnId);
        return newDynInfo;
      }
    } catch (Exception e) {
      logger.error("app构建B1类型动态信息出错", e);
    }
    return dynInfo;

  }

  /**
   * 
   * @param dynId
   * @return
   * @throws Exception
   */

  public IOSDynamicInfo buildParentDyn(Long dynId, Long psnId) throws Exception {
    String newDynData = dynamicContentDao.getDynInfo(dynId);
    IOSDynamicInfo newDynInfo = this.JsonToObj(newDynData, psnId);
    return newDynInfo;

  }

  /**
   * 构建成果信息
   * 
   * @param dynInfo
   * @param dataMap
   * @return
   */
  public IOSDynamicInfo buildPubInfo(IOSDynamicInfo dynInfo, Map<String, Object> dataMap, Long psnId) {
    try {
      dynInfo.setHasDes3PubId(getStringValue(dataMap, DynTemplateConstant.HAS_DES3_PUB_ID));
    } catch (NullPointerException e) {
      logger.error("该动态无成果信息，dynid:" + dynInfo.getDynId(), e);
      return dynInfo;
    }


    dynInfo.setDes3ResId(getStringValue(dataMap, DynTemplateConstant.DES3_RES_ID));
    dynInfo.setLinkTitleZh(getStringValue(dataMap, DynTemplateConstant.LINK_TITLE_ZH));
    dynInfo.setLinkTitleEn(getStringValue(dataMap, DynTemplateConstant.LINK_TITLE_EN));
    dynInfo.setPubDescrZh(getStringValue(dataMap, DynTemplateConstant.PUB_DESCR_ZH));
    dynInfo.setPubDescrEn(getStringValue(dataMap, DynTemplateConstant.PUB_DESCR_EN));
    dynInfo.setPubAuthors(filterSpace(getStringValue(dataMap, DynTemplateConstant.PUB_AUTHORS)));
    dynInfo.setPubPublishYear(getStringValue(dataMap, DynTemplateConstant.PUB_PUBLISHYEAR));

    dynInfo.setCollectPubStatus(
        isCollectedPub(psnId, NumberUtils.toLong(Des3Utils.decodeFromDes3(dynInfo.getDes3ResId())), PubDbEnum.SNS)
            + "");
    // 基准库成果
    if (StringUtils.isNotBlank(getStringValue(dataMap, DynTemplateConstant.PDWH_URL))) {
      dynInfo.setDatabaseType(getIntValue(dataMap, DynTemplateConstant.PDWH_URL));
      dynInfo.setDbId(getIntValue(dataMap, DynTemplateConstant.DB_ID));
      dynInfo.setCollectPubStatus(
          isCollectedPub(psnId, NumberUtils.toLong(Des3Utils.decodeFromDes3(dynInfo.getDes3ResId())), PubDbEnum.PDWH)
              + "");
    } else {
      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("des3PubId", dynInfo.getDes3ResId());
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
      HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(paramMap), headers);
      String saveUrl = domainscm + "/data/pub/query/pubdes3psnId";
      String des3PsnId = restTemplate.postForObject(saveUrl, entity, String.class);
      dynInfo.setDes3PubOwnerPsnId(Objects.toString(des3PsnId, ""));
    }
    String imageUrl = getFulltextImageUrl(dynInfo.getDes3ResId());
    /*
     * if (StringUtils.isBlank(imageUrl)) { imageUrl = getStringValue(dataMap,
     * DynTemplateConstant.FULLTEXT_IMAGE); }
     */
    /*
     * else { dynInfo.setFullTextImage(imageUrl);// 全文图片
     * 
     * }
     */
    dynInfo.setFullTextImage(imageUrl);// 全文图片
    dynInfo.setLinkImage(imageUrl);

    return dynInfo;
  }

  /**
   * 获取全文图片
   * 
   * @param des3ResId
   * @return
   */
  private String getFulltextImageUrl(String des3ResId) {
    String fullTextImageUrl = "";
    if (StringUtils.isNotBlank(des3ResId)) {
      Map<String, Object> paramMap = new HashMap<>();
      paramMap.put("des3PubId", des3ResId);
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
      HttpEntity<String> entity = new HttpEntity<String>(JacksonUtils.mapToJsonStr(paramMap), headers);
      String url = domainscm + "/data/pub/getfulltextimageurl";
      fullTextImageUrl = restTemplate.postForObject(url, entity, String.class);
    }
    return Objects.toString(fullTextImageUrl, "");
  }

  private String filterSpace(String s) {
    if (StringUtils.isBlank(s)) {
      return "";
    }
    s = s.replaceAll(" ", "");
    s = s.replaceAll(";", "; ");
    s = s.replaceAll(",", ", ");
    return s;
  }

  public boolean isCollectedPub(Long psnId, Long pubId, PubDbEnum pubDb) {
    return collectedPubDao.isCollectedPub(psnId, pubId, pubDb);
  }

  /**
   * 构建基金信息
   * 
   * @param dynInfo
   * @param dataMap
   * @return
   */
  private IOSDynamicInfo buildFundInfo(IOSDynamicInfo dynInfo, Map<String, Object> dataMap, Long psnId) {
    try {
      // 基金的参数已经和成果统一了,为了app不在修改处理逻辑这里重新赋值
      dynInfo.setFundId(getLongValue(dataMap, DynTemplateConstant.RES_ID));
      dynInfo.setEncodeFundId(getStringValue(dataMap, DynTemplateConstant.DES3_RES_ID));
      dynInfo.setFundDescEn(getStringValue(dataMap, DynTemplateConstant.PUB_DESCR_EN));
      dynInfo.setFundDescZh(getStringValue(dataMap, DynTemplateConstant.PUB_DESCR_ZH));
      dynInfo.setFundTitleEn(getStringValue(dataMap, DynTemplateConstant.LINK_TITLE_EN));
      dynInfo.setFundTitleZh(getStringValue(dataMap, DynTemplateConstant.LINK_TITLE_ZH));
      dynInfo.setFundLogoUrl(getStringValue(dataMap, DynTemplateConstant.LINK_IMAGE));
      MyFund findMyFund = MyFundDao.findMyFund(psnId, dynInfo.getFundId());
      if (findMyFund == null) {
        dynInfo.setCollectFundStatus("false");
      } else {
        dynInfo.setCollectFundStatus("true");
      }
    } catch (Exception e) {
      logger.error("app动态-基金出错", e);
      dynInfo = null;
    }
    return dynInfo;

  }

  /**
   * 构建基本信息
   * 
   * @param dynInfo
   * @param dataMap
   * @return
   */
  public IOSDynamicInfo buildBaseInfo(IOSDynamicInfo dynInfo, Map<String, Object> dataMap) {
    dynInfo.setDes3ProducerPsnId(getStringValue(dataMap, DynTemplateConstant.DES3_PRODUCER_PSN_ID));
    dynInfo.setPersonAvatars(getStringValue(dataMap, DynTemplateConstant.PERSON_AVATARS));
    dynInfo.setPersonNameZh(getStringValue(dataMap, DynTemplateConstant.PERSON_NAME_ZH));
    dynInfo.setPersonNameEn(getStringValue(dataMap, DynTemplateConstant.PERSON_NAME_EN));
    dynInfo.setPersonInsInfoZh(getStringValue(dataMap, DynTemplateConstant.PERSON_INSINFO_ZH));
    dynInfo.setPersonInsInfoEn(getStringValue(dataMap, DynTemplateConstant.PERSON_INSINFO_EN));
    return dynInfo;
  }

  /**
   * 动态类型判断
   * 
   * @param str
   * @return
   */
  public String checkType(String str) {
    if (str.contains("A")) {
      return DynTemplateConstant.ATEMP;
    } else if (str.contains("B1")) {
      return DynTemplateConstant.B1TEMP;
    } else if (str.contains("B2")) {
      return DynTemplateConstant.B2TEMP;
    } else if (str.contains("B3")) {
      return DynTemplateConstant.B3TEMP;
    }
    return null;

  }

  /**
   * 获取下一个模板类型
   * 
   * @param dynType
   * @return
   */
  public String getNextDynType(String dynType) {
    String nextDynType = null;
    switch (dynType) {
      case DynTemplateConstant.ATEMP:
        nextDynType = DynTemplateConstant.B1TEMP;
        break;
      case DynTemplateConstant.B1TEMP:
        nextDynType = DynTemplateConstant.B1TEMP;
        break;
      case DynTemplateConstant.B2TEMP:
        nextDynType = DynTemplateConstant.B2TEMP;
        break;
      case DynTemplateConstant.B3TEMP:
        nextDynType = DynTemplateConstant.B2TEMP;
        break;
      default:
        break;
    }
    return nextDynType;

  }

  /**
   * 根据ID构建动态数据
   * 
   * @param dyninfo
   * @param dynId
   * @return
   * @throws Exception
   */

  public IOSDynamicInfo buildDynInfo(Long dynId, Long psnId) throws Exception {
    String dynData = null;
    IOSDynamicInfo dyninfo = null;
    dynData = dynamicContentDao.getDynInfo(dynId);// Json字符串
    if (dynData == null) {
      logger.info("从MongoDb获取动态信息为空，dynid:" + dynId);
      dyninfo = null;
    }
    try {
      dyninfo = JsonToObj(dynData, psnId);
    } catch (Exception e) {
      logger.error("Json字符串转换为对象出错：dynid:" + dynId, e);
      dyninfo = null;
    }
    return dyninfo;
  }

  /**
   * 根据ID获取动态信息
   */
  @Override
  public List<IOSDynamicInfo> getDynById(Long psnId, Long dynId, int flag) throws Exception {
    List<Long> list = new ArrayList<Long>();
    if (dynId == null) {
      return null;
    }
    list.add(dynId);
    List<Long> dynIds = dynamicMsgDao.getDynListByDynId(psnId, dynId, flag);

    if (CollectionUtils.isNotEmpty(dynIds)) {
      return handleDynamicMsg(dynIds, psnId);
    }
    return null;

  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> addAward(DynamicForm form) throws Exception {
    Map<String, Object> awardPsnContent = null;
    form.setDynType(getNextDynType(form.getDynType()));
    String awardContent = dynamicAwardService.addAward(form);
    awardPsnContent = JacksonUtils.jsonToMap(awardContent);
    return awardPsnContent;

  }

  /**
   * 分享
   */
  @Override
  public Map<String, Object> quickshare(DynamicForm form) throws Exception {
    Map<String, Object> map = new HashMap<String, Object>();
    form.setDynType(getNextDynType(form.getDynType()));
    form.setOperatorType(3);
    try {
      dynamicQuickShareService.quickShare(form);
      map.put("shareresult", "success");
    } catch (Exception e) {
      logger.error("快速分出错,dynId=" + form.getParentDynId(), e);
      map.put("shareresult", "fail");
    }
    return map;
  }

  @Override
  public IOSDynamicInfo buildDynamicDetail(Long psnId, Long dynId) throws Exception {
    IOSDynamicInfo dyninfo = null;
    dyninfo = this.buildDynInfo(dynId, psnId);
    if (dyninfo != null) {
      // 处理赞评论分享数据
      String dynStatisticsIds = Des3Utils.encodeToDes3(dynId.toString());
      Map<String, Map<String, Object>> dynStatisMap = dynStatisticsService.getDynStatistics(dynStatisticsIds, psnId);
      dyninfo = this.dealStatistics(dynStatisMap, psnId, dyninfo);
    }
    return dyninfo;

  }

  @Override
  public Map<String, Map<String, Object>> getStatistics(DynamicForm form) throws Exception {

    Map<String, Map<String, Object>> dynStatisMap =
        dynStatisticsService.getDynStatistics(form.getDes3DynId(), form.getPsnId());

    boolean hasAward;
    hasAward = dynamicAwardService.getPsnHasAward(form.getPsnId(), form.getDynId());
    dynStatisMap.get(form.getDynId().toString()).put("awardStatus", hasAward + "");
    return dynStatisMap;

  }

  @Override
  public List<DynReplayInfo> loadDynRely(DynamicForm form) throws Exception {
    dynamicReplyService.loadDynReply(form);
    return form.getDynReplayInfoList();

  }

  @Override
  public AppVersionRecord getIosVersionInfo() throws Exception {
    return appVersionRecordDao.getNewestIOSVesion();
  }
}
