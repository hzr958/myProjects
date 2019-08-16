package com.smate.center.open.service.data.group;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.group.OpenGroupUnionDao;
import com.smate.center.open.dao.prj.ThirdPrjInfoDao;
import com.smate.center.open.dao.publication.CategoryMapBaseDao;
import com.smate.center.open.dao.publication.CategoryMapScmNsfcDao;
import com.smate.center.open.dao.union.his.OpenGroupUnionHisDao;
import com.smate.center.open.model.group.association.OpenGroupUnion;
import com.smate.center.open.model.interconnection.UnionGroupCodeCache;
import com.smate.center.open.model.prj.ThirdPrjInfo;
import com.smate.center.open.model.prj.ThirdPrjInfoTemp;
import com.smate.center.open.service.data.ThirdDataService;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.utils.xml.WebServiceUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;

/**
 * 重构 互联互通 关联群组 信息服务实现
 * 
 * @author ajb
 */

@Transactional(rollbackFor = Exception.class)
public class InterconnetcionRefactorUnionGroupServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  OpenGroupUnionDao openGroupUnionDao;
  @Autowired
  OpenGroupUnionHisDao openGroupUnionHisDao;
  @Autowired
  private OpenCacheService openCacheService;
  @Autowired
  private ThirdPrjInfoDao thirdPrjInfoDao;
  @Autowired
  private CategoryMapScmNsfcDao categoryMapScmNsfcDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;

  @Autowired
  private ThirdDataService thirdDataService;

  @Value("${initOpen.restful.url}")
  private String restfulUrl;
  @Value("${domainscm}")
  private String domainscm;
  @Resource
  private RestTemplate restTemplate;

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {

    Map<String, Object> temp = new HashMap<String, Object>();
    // 校验服务参数 serviceType
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    paramet.putAll(serviceData);

    // 快速创建群组
    Object quicklyCreateGroup = paramet.get(GroupInfoConst.QUICKLY_CREATE_GROUP);
    Object oldGroupCode = paramet.get(GroupInfoConst.OLD_GROUP_CODE);
    // 不为空说明是创建群组关联
    if (quicklyCreateGroup != null) {
      if (StringUtils.isNotBlank(quicklyCreateGroup.toString())
          && "true".equalsIgnoreCase(quicklyCreateGroup.toString())) {
        temp = checkGroupParam(paramet);
        if (temp != null && temp.size() > 0) {
          return temp;
        }
        temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
        return temp;
      } else {
        logger.error("quicklyCreateGroup格式错误 , 值必须为  true！");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_255, paramet, "quicklyCreateGroup格式错误 , 值必须为  true！");
        return temp;

      }
    } else {// 表示关联群组 第一次要验证 ，新项目信息
      Object prjStatus = paramet.get(GroupInfoConst.PRJ_STATUS);
      if (prjStatus == null || StringUtils.isBlank(prjStatus.toString())) {
        logger.error("具体服务类型参数prjStatus不能为空");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_256, paramet, "具体服务类型参数prjStatus不能为空");
        return temp;
      }
      if ((oldGroupCode == null || StringUtils.isBlank(oldGroupCode.toString())) && !"1".equals(prjStatus.toString())) {
        temp = checkGroupParam(paramet);
        if (temp != null && temp.size() > 0) {
          return temp;
        }
      }

    }

    Object groupCode = paramet.get(GroupInfoConst.GROUP_CODE);
    if (groupCode == null || StringUtils.isBlank(groupCode.toString())) {
      logger.error("服务参数  群组的groupCode不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_254, paramet, "服务参数  群组的groupCode不能为空");
      return temp;
    }

    // 获取缓存中的 groupId
    UnionGroupCodeCache groupCodeCache = (UnionGroupCodeCache) openCacheService.get(OpenConsts.UNION_GROUP_CODE_CACHE,
        paramet.get(GroupInfoConst.GROUP_CODE).toString());
    Long openId = NumberUtils.toLong(paramet.get(OpenConsts.MAP_OPENID).toString());
    if (groupCodeCache != null && groupCodeCache.getGroupId() != null && groupCodeCache.getOpenId() != null) {
      if (!openId.toString().equals(groupCodeCache.getOpenId().toString())) {
        logger.error("服务参数  groupCode正确    但不能 不能配皮 对应的 openId！");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_405, paramet, "服务参数 groupCode正确    但不能 不能配皮 对应的 openId！");
        return temp;
      }
      paramet.put(GroupInfoConst.GROUP_ID, groupCodeCache.getGroupId().toString());
    } else {
      logger.error("服务参数  groupCode无效 或者失效！");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_267, paramet, "服务参数  groupCode无效 或者失效！");
      return temp;
    }

    temp = new HashMap<String, Object>();
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  /** 校验快速创建群组的具体参数 */
  public Map<String, Object> checkGroupParam(Map<String, Object> serviceData) {
    Map<String, Object> temp = new HashMap<String, Object>();
    // 校验群组信息
    Object groupJson = serviceData.get(OpenConsts.MAP_GROUP_DATA);
    if (groupJson == null || "".equals(groupJson.toString())) {
      logger.error("groupData不能为空！");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_257, serviceData, "groupData不能为空！");
      return temp;
    }
    Object fromSys = serviceData.get(OpenConsts.MAP_DATA_FROM_SYS);
    if (fromSys == null || "".equals(fromSys.toString())) {
      logger.error("fromSys不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_258, serviceData, "fromSys不能为空");
      return temp;
    }
    ThirdPrjInfoTemp thirdPrjInfoTemp = null;

    Map<String, String> groupInfoMap = JacksonUtils.jsonToMap(groupJson.toString());
    try {
      // 检查第三方项目具体的信息
      thirdPrjInfoTemp = WebServiceUtils.toThirdPrjInfoTempByMap(groupInfoMap);
      temp = checkThirdPrjInfo(thirdPrjInfoTemp, serviceData);
      if (temp != null && temp.size() > 0) {
        return temp;
      }
    } catch (Exception e) {
      logger.error("快速创建群组的基本数据格式出错");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_260, serviceData, "groupData解析成对象出错！");
      return temp;
    }
    serviceData.put(OpenConsts.MAP_DATA_THIRD_PRJ, thirdPrjInfoTemp);
    return temp;
  }

  /**
   * 检查第三方项目的信息
   * 
   * @param thirdPrjInfoTemp
   * @return
   */
  public Map<String, Object> checkThirdPrjInfo(ThirdPrjInfoTemp thirdPrjInfoTemp, Map<String, Object> serviceData) {
    Map<String, Object> temp = new HashMap<String, Object>();
    if (StringUtils.isBlank(thirdPrjInfoTemp.getGroupName())) {
      logger.error("群组名称//项目名称 groupName,不能为空！");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_261, serviceData, "群组名称//项目名称groupName ,不能为空！");
      return temp;
    }

    if (StringUtils.isNotBlank(thirdPrjInfoTemp.getPrjStatus())) {
      if (!NumberUtils.isNumber(thirdPrjInfoTemp.getPrjStatus()) || thirdPrjInfoTemp.getPrjStatus().length() > 1) {
        logger.error("项目状态prjStatus长度只能等于1 ,且为数字！");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_262, serviceData, "项目状态prjStatus长度只能等于1,且为数字！");
        return temp;
      }

    }
    if (StringUtils.isNotBlank(thirdPrjInfoTemp.getAmount())) {
      if (!NumberUtils.isNumber(thirdPrjInfoTemp.getAmount())
          || NumberUtils.toDouble(thirdPrjInfoTemp.getAmount()) > 1000000000000.0) {
        logger.error("项目金额amount整数长度不能大于12位 ,且为数字 ！");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_263, serviceData, "项目金额amount整数长度不能大于12位  ,且为数字！");
        return temp;
      }
    }
    if (StringUtils.isNotBlank(thirdPrjInfoTemp.getPartPsnNames())) {
      if (!JacksonUtils.isJsonString10(thirdPrjInfoTemp.getPartPsnNames())
          || thirdPrjInfoTemp.getPartPsnNames().length() > 3000) {
        logger.error("参与人员信息格式必须为 json ,且长度小于3000 ！");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_264, serviceData, "参与人员信息格式必须为 json ,且长度小于3000 ！");
        return temp;
      }
    }
    if (StringUtils.isNotBlank(thirdPrjInfoTemp.getKeywordsZh())) {
      if (!JacksonUtils.isJsonString10(thirdPrjInfoTemp.getKeywordsZh())
          || thirdPrjInfoTemp.getKeywordsZh().length() > 1000) {
        logger.error("中文关键词格式必须为 json ,且长度小于1000 ！");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_265, serviceData, "中文关键词格式必须为 json ,且长度小于1000 ！");
        return temp;
      }
    }
    if (StringUtils.isNotBlank(thirdPrjInfoTemp.getKeywordsEn())) {
      if (!JacksonUtils.isJsonString10(thirdPrjInfoTemp.getKeywordsEn())
          || thirdPrjInfoTemp.getKeywordsEn().length() > 1000) {
        logger.error("英文关键词格式必须为 json ,且长度小于1000 ！");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_266, serviceData, "英文关键词格式必须为 json ,且长度小于1000 ！");
        return temp;
      }
    }
    return temp;
  }

  /**
   * 
   */
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {

    Map<String, Object> temp = new HashMap<String, Object>();
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    String groupCode = "";
    Long openId = NumberUtils.toLong(paramet.get(OpenConsts.MAP_OPENID).toString());
    Map<String, Object> infoMap = new HashMap<String, Object>();
    String groupId = "";
    Object prjStatus = paramet.get(GroupInfoConst.PRJ_STATUS);
    Object oldGroupCode = paramet.get(GroupInfoConst.OLD_GROUP_CODE);
    Object quicklyCreateGroup = paramet.get(GroupInfoConst.QUICKLY_CREATE_GROUP);
    if (quicklyCreateGroup != null) {// 快速创建群组
      groupId = createGroupInfo(paramet);
      groupCode = DigestUtils.md5Hex(openId.toString() + "_" + groupId);
    } else {
      groupId = paramet.get(GroupInfoConst.GROUP_ID).toString();
      groupCode = paramet.get(GroupInfoConst.GROUP_CODE).toString();
    }
    if (StringUtils.isNotBlank(groupId)) {
      paramet.put(GroupInfoConst.GROUP_ID, groupId);
    } else {
      logger.error("服务参数  groupCode无效 或者失效！");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_267, paramet, "服务参数  groupCode无效 或者失效！");
      return temp;
    }
    paramet.put("groupCode", groupCode);
    temp = saveOrUpdateUnionGroup(paramet);
    if (temp != null && temp.size() > 0) {
      return temp;
    }
    // 保存关连群组的 项目信息
    if (quicklyCreateGroup == null || StringUtils.isBlank(quicklyCreateGroup.toString())) {
      // 不是申请的项目 ,要保存 项目信息
      if (prjStatus != null && !"1".equals(prjStatus.toString())) {
        saveOrUpdateThirdPrjInfo(paramet, groupId, groupCode, oldGroupCode);
      }

    }
    infoMap = buildInfoMap(groupCode, groupId, openId);
    dataList.add(infoMap);
    temp = super.successMap(OpenMsgCodeConsts.SCM_000, dataList);
    return temp;

  }

  /**
   * 构建返回的数据
   * 
   * @param groupBaseInfo
   * @param groupInvitePsnList
   * @param serviceData
   * @return
   */
  public Map<String, Object> buildInfoMap(String groupCode, String groupId, Long openId) {

    Map<String, Object> mapInfo = new HashMap<String, Object>();
    UnionGroupCodeCache groupCodeCache = new UnionGroupCodeCache();
    groupCodeCache.setGroupId(NumberUtils.toLong(groupId));
    groupCodeCache.setOpenId(openId);
    openCacheService.put(OpenConsts.UNION_GROUP_CODE_CACHE, openCacheService.EXP_HOUR_1, groupCode, groupCodeCache);
    mapInfo.put(GroupInfoConst.GROUP_CODE, groupCode);
    mapInfo.put("des3GrpId", Des3Utils.encodeToDes3(groupId));
    return mapInfo;
  }

  /**
   * 关联群组
   *
   * 保存
   */
  public Map<String, Object> saveOrUpdateUnionGroup(Map<String, Object> dataParamet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Long psnId = NumberUtils.toLong(dataParamet.get("psnId").toString());
    Long openId = NumberUtils.toLong(dataParamet.get("openid").toString());
    String token = dataParamet.get("token").toString();
    Long groupId = NumberUtils.toLong(dataParamet.get("groupId").toString());
    String groupCode = dataParamet.get("groupCode").toString();
    Object oldGroupCode = dataParamet.get("oldGroupCode");
    OpenGroupUnion openGroupUnion = new OpenGroupUnion();
    Long id = openGroupUnionDao.findIdByOwnPsnIdAndGroupCode(groupCode);
    if (id != null) {
      logger.error("服务参数  groupCode已经被关联！");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_406, dataParamet, "服务参数  groupCode已经被关联！！");
      return temp;
    }
    if (oldGroupCode != null && StringUtils.isNotBlank(oldGroupCode.toString())) {
      Long oldId = openGroupUnionDao.findIdByOwnPsnIdAndGroupCode(oldGroupCode.toString());
      if (oldId == null) {
        oldId = openGroupUnionHisDao.findIdByOwnPsnIdAndGroupCode(oldGroupCode.toString());
      }
      if (oldId != null) {
        openGroupUnion.setId(oldId);
      } else {
        logger.error("服务参数  oldGroupCode查询不到关联信息！");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_407, dataParamet, "服务参数  oldGroupCode查询不到关联信息！！");
        return temp;
      }
    }
    openGroupUnion.setGroupCode(groupCode);
    openGroupUnion.setGroupId(groupId);
    openGroupUnion.setOwnerPsnId(psnId);
    openGroupUnion.setCreateTime(new Date());
    openGroupUnion.setOwnerOpenId(openId);
    openGroupUnion.setToken(token);
    openGroupUnionDao.save(openGroupUnion);
    return null;
  }

  /** 创建群组 保存相关的信息 返回群组id */
  public String createGroupInfo(Map<String, Object> dataParamet) {
    String groupId = getGroupId(saveGroupInfo(dataParamet));
    if (StringUtils.isBlank(groupId)) {
      return "";
    }
    Long openId = NumberUtils.toLong(dataParamet.get("openid").toString());
    String groupCode = DigestUtils.md5Hex(openId.toString() + "_" + groupId);
    Object oldGroupCode = dataParamet.get("oldGroupCode");
    if (oldGroupCode != null && StringUtils.isNotBlank(oldGroupCode.toString())) {
      // 更新项目的groupcode
      saveOrUpdateThirdPrjInfo(dataParamet, groupId, groupCode, oldGroupCode);
    } else {
      // 创建项目信息
      saveThirdPrjInfo(dataParamet, groupId);
    }

    return groupId;
  }

  private String getGroupId(Map<String, Object> resultMap) {
    if (resultMap != null && "success".equals(resultMap.get("status"))) {
      List<Map<String, Object>> result = (List<Map<String, Object>>) resultMap.get("data");
      if (result != null && result.size() > 0) {
        return result.get(0).get("grpId").toString();
      }
    }
    return "";
  }

  /**
   * 关联群组
   * 
   * @param dataParamet
   * @param groupId
   * @param groupCode
   * @param oldGroupCode
   */
  private void saveOrUpdateThirdPrjInfo(Map<String, Object> dataParamet, String groupId, String groupCode,
      Object oldGroupCode) {
    if (oldGroupCode != null && StringUtils.isNoneBlank(oldGroupCode.toString())) {// 第二次关联,项目信息
      ThirdPrjInfo thirdPrjInfo = thirdPrjInfoDao.getThirdPrjInfoByGroupCode(oldGroupCode.toString());
      if (thirdPrjInfo != null) {
        thirdPrjInfo.setGroupCode(groupCode);
        thirdPrjInfoDao.save(thirdPrjInfo);
      }
    } else {// 第一次关联，同步项目信息 但是不同步， 关键词
      saveThirdPrjInfoNotSaveKeywords(dataParamet, groupId);
    }
  }

  /**
   * 保存第三方项目信息
   * 
   * @param dataParamet
   * @param groupId
   */
  private void saveThirdPrjInfo(Map<String, Object> dataParamet, String groupId) {
    ThirdPrjInfo thirdPrjInfo = new ThirdPrjInfo();
    ThirdPrjInfoTemp thirdPrjInfoTemp = (ThirdPrjInfoTemp) dataParamet.get(OpenConsts.MAP_DATA_THIRD_PRJ);

    if (thirdPrjInfoTemp != null && StringUtils.isNotBlank(groupId) && NumberUtils.isNumber(groupId)) {
      thirdPrjInfo.setOpenId(dataParamet.get(OpenConsts.MAP_OPENID).toString());
      thirdPrjInfo.setFromSys(dataParamet.get(OpenConsts.MAP_DATA_FROM_SYS).toString());
      String groupCode = DigestUtils.md5Hex(thirdPrjInfo.getOpenId() + "_" + groupId);
      thirdPrjInfo.setGroupCode(groupCode);
      copyProperties(thirdPrjInfoTemp, thirdPrjInfo);
      thirdPrjInfoDao.save(thirdPrjInfo);
    }
  }

  /**
   * 保存第三方项目信息 但是不保存，关键词
   * 
   * @param dataParamet
   * @param groupId
   */
  private void saveThirdPrjInfoNotSaveKeywords(Map<String, Object> dataParamet, String groupId) {
    ThirdPrjInfo thirdPrjInfo = new ThirdPrjInfo();
    ThirdPrjInfoTemp thirdPrjInfoTemp = (ThirdPrjInfoTemp) dataParamet.get(OpenConsts.MAP_DATA_THIRD_PRJ);

    if (thirdPrjInfoTemp != null && StringUtils.isNotBlank(groupId) && NumberUtils.isNumber(groupId)) {
      thirdPrjInfo.setOpenId(dataParamet.get(OpenConsts.MAP_OPENID).toString());
      thirdPrjInfo.setFromSys(dataParamet.get(OpenConsts.MAP_DATA_FROM_SYS).toString());
      String groupCode = DigestUtils.md5Hex(thirdPrjInfo.getOpenId() + "_" + groupId);
      thirdPrjInfo.setGroupCode(groupCode);
      copyProperties(thirdPrjInfoTemp, thirdPrjInfo);
      thirdPrjInfoDao.save(thirdPrjInfo);
    }
  }

  /** 调restful,保存群组信息 */
  private Map<String, Object> saveGroupInfo(Map<String, Object> dataParamet) {
    Map<String, Object> mapData = new HashMap<String, Object>();
    Map<String, Object> dataData = new HashMap<String, Object>();
    Map<String, Object> grpData = new HashMap<String, Object>();
    mapData.put("openid", dataParamet.get("openid"));
    mapData.put("token", dataParamet.get("token").toString() + "lhd25dhl");
    // start
    buildGrpDataInfo(dataParamet, grpData);
    // end
    dataData.put("grpData", JacksonUtils.mapToJsonStr(grpData));
    mapData.put("data", JacksonUtils.mapToJsonStr(dataData));
    Map<String, Object> resultData = null;
    try {
      resultData = thirdDataService.handleOpenData(mapData);
      return resultData;
    } catch (Exception e) {
      logger.error("互联互通，快速关联创建群组，异常，");
    }
    return resultData;
  }

  private void buildGrpDataInfo(Map<String, Object> dataParamet, Map<String, Object> grpData) {
    ThirdPrjInfoTemp thirdPrjInfoTemp = (ThirdPrjInfoTemp) dataParamet.get(OpenConsts.MAP_DATA_THIRD_PRJ);
    grpData.put(GroupInfoConst.GRP_NAME, StringEscapeUtils.escapeXml11(thirdPrjInfoTemp.getGroupName()));
    String keywordZh = getkeyWord(thirdPrjInfoTemp.getKeywordsZh());
    String keywordEn = getkeyWord(thirdPrjInfoTemp.getKeywordsZh());
    grpData.put(GroupInfoConst.KEY_WORDS, StringUtils.isNotBlank(keywordZh) ? keywordZh : keywordEn);
    grpData.put(GroupInfoConst.GRP_CATEGORY, "11"); // 项目群组
    grpData.put(GroupInfoConst.OPEN_TYPE, "H"); // 项目群组
    // 拿申请代码 查出学科代码
    // applicationCode
    // category_map_scm_nsfc.
    List<Long> categoryList = categoryMapScmNsfcDao.getScmCategoryByNsfcCategory(thirdPrjInfoTemp.getApplyCode());
    if (CollectionUtils.isNotEmpty(categoryList)) {
      Long category = categoryList.get(0);
      if (category != null && category != 0L) {
        // 申请代码对应到的是二级领域
        grpData.put(GroupInfoConst.SECOND_CATEGORYID, category);
        grpData.put(GroupInfoConst.FIRST_CATEGORYID,
            categoryMapBaseDao.getSuperCategoryBySubCategory(Integer.parseInt(category.toString())));
      }
    }
    grpData.put(GroupInfoConst.PROJECT_NO, thirdPrjInfoTemp.getPrjExternalNo());
    grpData.put(GroupInfoConst.PROJECT_STATUS, thirdPrjInfoTemp.getPrjStatus());
  }

  private String getkeyWord(String keyword) {
    String kw = "";
    if (StringUtils.isNoneBlank(keyword)) {
      List<String> list = JacksonUtils.jsonToList(keyword);
      if (list != null && list.size() > 0) {
        for (int i = 0; i < list.size(); i++) {
          if (StringUtils.isBlank(list.get(i)))
            continue;
          kw += list.get(i);
          if (list.size() != i + 1) {
            kw += ";";
          }
        }
      }
    }
    return kw;
  }

  /**
   * @param source
   * @param destinate
   */
  private void copyProperties(ThirdPrjInfoTemp source, ThirdPrjInfo destinate) {
    // truncateString(source.getCurrency() , 10)

    destinate.setPrjName(truncateString(source.getGroupName(), 200));
    destinate.setApplyCode(truncateString(source.getApplyCode(), 10));
    destinate.setFundingTypes(truncateString(source.getFundingTypes(), 100));
    destinate.setPrjExternalNo(truncateString(source.getPrjExternalNo(), 20));
    destinate.setAmount(NumberUtils.toDouble(source.getAmount()));
    fillDate(source, destinate);
    destinate.setInsName(truncateString(source.getInsName(), 200));
    destinate.setPartPsnNames(truncateString(source.getPartPsnNames(), 1000));
    destinate.setKeywordsEn(truncateString(source.getKeywordsEn(), 1000));
    destinate.setKeywordsZh(truncateString(source.getKeywordsZh(), 1000));
    destinate.setPrjStatus(NumberUtils.toInt(source.getPrjStatus()));
    destinate.setCurrency(truncateString(source.getCurrency(), 10));
    destinate.setSchemeAgencyName(truncateString(source.getSchemeAgencyName(), 50));
  }

  /**
   * 填充时间
   * 
   * @param source
   * @param destinate
   */
  private void fillDate(ThirdPrjInfoTemp source, ThirdPrjInfo destinate) {
    Date startDate = parseStringToDate(source.getStartDate());
    Date endDate = parseStringToDate(source.getEndDate());
    Calendar calendar = Calendar.getInstance();
    int year;
    int month;
    int day;
    if (startDate != null) {
      calendar.setTime(startDate);
      year = calendar.get(Calendar.YEAR);
      month = calendar.get(Calendar.MONTH) + 1;
      day = calendar.get(Calendar.DAY_OF_MONTH);
      destinate.setStartYear(year);
      if (source.getStartDate().length() > 4) {
        destinate.setStartMonth(month);
      }
      if (source.getStartDate().length() > 7) {
        destinate.setStartDay(day);
      }
    }
    if (endDate != null) {
      calendar.setTime(endDate);
      year = calendar.get(Calendar.YEAR);
      month = calendar.get(Calendar.MONTH) + 1;
      day = calendar.get(Calendar.DAY_OF_MONTH);
      destinate.setEndYear(year);
      if (source.getEndDate().length() > 4) {
        destinate.setEndMonth(month);
      }
      if (source.getEndDate().length() > 7) {
        destinate.setEndDay(day);
      }
    }

  }

  /**
   * 解析时间
   * 
   * @param dateString
   * @return
   */
  private Date parseStringToDate(String dateString) {

    Date date = null;
    if (StringUtils.isNoneBlank(dateString)) {
      String pattem = "";
      switch (dateString.length()) {
        case 4:
          pattem = "yyyy";
          break;
        case 7:
          pattem = "yyyy-MM";
          break;
        case 10:
          pattem = "yyyy-MM-dd";
          break;
        default:
          break;
      }
      DateFormat df = new SimpleDateFormat(pattem);
      try {
        date = df.parse(dateString);
      } catch (ParseException e) {

      }
    }
    return date;
  }

  /**
   * 截取字符
   * 
   * @param source
   * @param length
   * @return
   */
  public String truncateString(String source, Integer length) {
    if (StringUtils.isNotBlank(source) && length > 0 && source.length() > length) {
      return source.substring(0, length);
    }
    return source;
  }

}
