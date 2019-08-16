package com.smate.center.open.service.interconnection.group;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.dao.group.OpenGroupUnionDao;
import com.smate.center.open.dao.interconnection.UnionRefreshGroupPubLogDao;
import com.smate.center.open.dao.prj.ThirdPrjInfoDao;
import com.smate.center.open.dao.union.his.OpenGroupUnionHisDao;
import com.smate.center.open.model.group.association.OpenGroupUnion;
import com.smate.center.open.model.interconnection.UnionGroupCodeCache;
import com.smate.center.open.model.prj.ThirdPrjInfo;
import com.smate.center.open.model.prj.ThirdPrjInfoTemp;
import com.smate.center.open.service.data.ThirdDataService;
import com.smate.center.open.service.data.group.GroupInfoConst;
import com.smate.center.open.service.interconnection.ScmUnionDataHandleService;
import com.smate.center.open.utils.xml.WebServiceUtils;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 互联互通 关联群组
 * 
 * @author AiJiangBin
 *
 */
public class InterconnectionUnionGroupServiceImpl implements ScmUnionDataHandleService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  OpenGroupUnionDao openGroupUnionDao;
  @Autowired
  OpenGroupUnionHisDao openGroupUnionHisDao;
  @Autowired
  private UnionRefreshGroupPubLogDao unionRefreshGroupPubLogDao;
  @Autowired
  private OpenCacheService openCacheService;
  @Autowired
  private ThirdPrjInfoDao thirdPrjInfoDao;
  @Autowired
  private ThirdDataService thirdDataService;

  /*
   * @Autowired private GroupKeywordsDao groupKeywordsDao ;
   * 
   * @Autowired private GroupKeyDiscDao groupKeyDiscDao ;
   */

  @Value("${initOpen.restful.url}")
  private String restfulUrl;
  @Value("${domainscm}")
  private String domainscm;
  @Resource
  private RestTemplate restTemplate;

  /**
   * 检查： 参数
   * 
   * @param dataParamet
   * @return
   */
  private String checkParam(Map<String, Object> dataParamet) {

    // 快速创建群组
    Object quicklyCreateGroup = dataParamet.get("quicklyCreateGroup");

    Object oldGroupCode = dataParamet.get("oldGroupCode");
    // 不为空说明是创建群组关联
    if (quicklyCreateGroup != null) {
      if (StringUtils.isNotBlank(quicklyCreateGroup.toString())
          && "true".equalsIgnoreCase(quicklyCreateGroup.toString())) {
        return checkGroupParam(dataParamet);
      } else {
        logger.error("具体服务类型参数quicklyCreateGroup格式错误");
        return "quicklyCreateGroup格式错误 , 值必须为  true！";
      }
    } else {// 表示关联群组 第一次要验证 ，新项目信息
      Object prjStatus = dataParamet.get("prjStatus");
      if (prjStatus == null || StringUtils.isBlank(prjStatus.toString())) {
        logger.error("具体服务类型参数prjStatus不能为空");
        return "prjStatus不能为空！";
      }
      if ((oldGroupCode == null || StringUtils.isBlank(oldGroupCode.toString())) && !"1".equals(prjStatus.toString())) {
        return checkGroupParam(dataParamet);
      }

    }

    Object groupCode = dataParamet.get("groupCode");

    if (groupCode == null || StringUtils.isBlank(groupCode.toString())) {
      logger.error("具体服务类型参数groupCode不能为空");
      return "groupCode不能为空！";
    }

    return null;
  }

  /**
   * 校验快速创建群组的具体参数
   */
  public String checkGroupParam(Map<String, Object> serviceData) {
    // 校验群组信息
    Object groupJson = serviceData.get(OpenConsts.MAP_GROUP_DATA);
    if (groupJson == null || "".equals(groupJson.toString())) {
      logger.error("快速创建群组的基本数据不能为空");
      return "groupData不能为空！";
    }
    Object fromSys = serviceData.get(OpenConsts.MAP_DATA_FROM_SYS);
    if (fromSys == null || "".equals(fromSys.toString())) {
      logger.error("fromSys不能为空");
      return "fromSys为空！";
    }
    ThirdPrjInfoTemp thirdPrjInfoTemp = null;

    Boolean isJson = JacksonUtils.isJsonString10(groupJson.toString());
    if (!isJson) {
      return "groupData不是json格式的字符串";
    }
    Map<String, String> groupInfoMap = JacksonUtils.jsonToMap(groupJson.toString());

    try {
      thirdPrjInfoTemp = WebServiceUtils.toThirdPrjInfoTempByMap(groupInfoMap);
      String temp = checkThirdPrjInfo(thirdPrjInfoTemp);
      if (StringUtils.isNotBlank(temp)) {
        return temp;
      }

    } catch (Exception e) {
      logger.error("快速创建群组的基本数据格式出错");
      return "groupData解析成对象出错！";
    }
    serviceData.put(OpenConsts.MAP_DATA_THIRD_PRJ, thirdPrjInfoTemp);
    return null;
  }

  /**
   * 具体业务
   */
  @Override
  public String handleUnionData(Map<String, Object> dataParamet) {

    // data 在前面已经 验证不为空 ，并且是json格式的
    Object data = dataParamet.get(OpenConsts.MAP_DATA);
    Long openId = NumberUtils.toLong(dataParamet.get("openid").toString());
    Map<String, Object> serviceData = JacksonUtils.jsonToMap(data.toString());
    String temp = checkParam(serviceData);
    if (temp != null) {
      return buildResultXml(temp, "4");
    }
    dataParamet.put(OpenConsts.MAP_DATA_FROM_SYS, serviceData.get(OpenConsts.MAP_DATA_FROM_SYS));
    dataParamet.put("oldGroupCode", serviceData.get("oldGroupCode"));
    dataParamet.put(OpenConsts.MAP_DATA_THIRD_PRJ, serviceData.get(OpenConsts.MAP_DATA_THIRD_PRJ));
    String groupId = "";
    String groupCode = "";
    Object prjStatus = serviceData.get("prjStatus");
    Object oldGroupCode = serviceData.get("oldGroupCode");
    Object quicklyCreateGroup = serviceData.get("quicklyCreateGroup");
    if (quicklyCreateGroup != null) {// 快速创建群组
      dataParamet.put("groupXml", serviceData.get(OpenConsts.MAP_GROUP_XML));
      groupId = createGroupInfo(dataParamet);
      groupCode = DigestUtils.md5Hex(openId.toString() + "_" + groupId);
    } else {
      // 获取缓存中的 groupId
      UnionGroupCodeCache groupCodeCache = (UnionGroupCodeCache) openCacheService.get(OpenConsts.UNION_GROUP_CODE_CACHE,
          serviceData.get("groupCode").toString());
      if (groupCodeCache != null && groupCodeCache.getGroupId() != null && groupCodeCache.getOpenId() != null) {
        if (!openId.toString().equals(groupCodeCache.getOpenId().toString())) {
          return buildResultXml("groupCode不能正确匹配对应的openId！", "6");
        }
        groupId = groupCodeCache.getGroupId().toString();
        groupCode = serviceData.get("groupCode").toString();
      }
    }
    if (StringUtils.isNoneBlank(groupId)) {
      dataParamet.put("groupId", groupId);
    } else {
      return buildResultXml("groupCode无效或者失效！", "7");
    }
    dataParamet.put("groupCode", groupCode);
    temp = saveOrUpdateUnionGroup(dataParamet);
    if (temp != null) {
      return buildResultXml(temp, "7");
    }
    // 保存关连群组的 项目信息
    if (quicklyCreateGroup == null || StringUtils.isBlank(quicklyCreateGroup.toString())) {
      // 不是申请的项目 ,要保存 项目信息
      if (prjStatus != null && !"1".equals(prjStatus.toString())) {
        saveOrUpdateThirdPrjInfo(dataParamet, groupId, groupCode, oldGroupCode);
      }

    }

    return buildResultXml(groupCode, groupId, openId);
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
    if (oldGroupCode != null && StringUtils.isNoneBlank(oldGroupCode.toString())) {// 第二次关联 项目信息
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
   * 关联群组
   *
   * 保存
   */
  public String saveOrUpdateUnionGroup(Map<String, Object> dataParamet) {
    Long psnId = NumberUtils.toLong(dataParamet.get("psnId").toString());
    Long openId = NumberUtils.toLong(dataParamet.get("openid").toString());
    String token = dataParamet.get("token").toString();
    Long groupId = NumberUtils.toLong(dataParamet.get("groupId").toString());
    String groupCode = dataParamet.get("groupCode").toString();
    Object oldGroupCode = dataParamet.get("oldGroupCode");
    OpenGroupUnion openGroupUnion = new OpenGroupUnion();
    Long id = openGroupUnionDao.findIdByOwnPsnIdAndGroupCode(groupCode);
    if (id != null) {
      return "groupCode已经被关联！";
    }
    if (oldGroupCode != null && StringUtils.isNotBlank(oldGroupCode.toString())) {
      Long oldId = openGroupUnionDao.findIdByOwnPsnIdAndGroupCode(oldGroupCode.toString());
      if (oldId == null) {
        oldId = openGroupUnionHisDao.findIdByOwnPsnIdAndGroupCode(oldGroupCode.toString());
      } else {
        openGroupUnion.setId(oldId);
      }
      if (oldId != null) {
        // 更换群组，删除老群组的成果日志，为了下次可以自动刷新成果
        openCacheService.remove(OpenConsts.UNION_REFRESH_GROUP_PUB_CACHE, oldGroupCode.toString());
        unionRefreshGroupPubLogDao.deleteLogByGroupCode(oldGroupCode.toString());
      } else {
        return "oldGroupCode查询不到关联信息！";
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

  /**
   * 构造返回参数
   * 
   * @param groupBaseInfoList
   * @param dataParamet
   * @return
   */
  private String buildResultXml(String groupCode, String groupId, Long openId) {
    Document doc;
    try {
      doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><group></group>");
      Element rootNode = (Element) doc.selectSingleNode("/group");
      rootNode.addElement("getUnionGroupStatus").addText("0");
      UnionGroupCodeCache groupCodeCache = new UnionGroupCodeCache();
      groupCodeCache.setGroupId(NumberUtils.toLong(groupId));
      groupCodeCache.setOpenId(openId);
      openCacheService.put(OpenConsts.UNION_GROUP_CODE_CACHE, openCacheService.EXP_HOUR_1, groupCode, groupCodeCache);

      rootNode.addElement("group_code").addText(groupCode);
      rootNode.addElement("result").addText("成功关联群组！");
      return doc.getRootElement().asXML();
    } catch (DocumentException e) {
      logger.error("构造成果XML列表出现异常", e);
      return buildResultXml("构造成果XML列表出现异常", "5");
    }
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
   * 创建群组 保存相关的信息 返回群组id
   */

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

  /**
   * 调restful,保存群组信息
   */
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
    String keywordEn = getkeyWord(thirdPrjInfoTemp.getKeywordsEn());
    grpData.put(GroupInfoConst.KEY_WORDS, StringUtils.isNotBlank(keywordZh) ? keywordZh : keywordEn);
    grpData.put(GroupInfoConst.GRP_CATEGORY, "11"); // 项目群组
    grpData.put(GroupInfoConst.OPEN_TYPE, "H"); // 项目群组
    grpData.put(GroupInfoConst.FIRST_CATEGORYID, "");
    grpData.put(GroupInfoConst.FIRST_CATEGORYID, "");
    grpData.put(GroupInfoConst.SECOND_CATEGORYID, "");
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
   * 构造群组的 xml
   */
  private Object buildGroupDataParameter(Map<String, Object> dataParamet) {
    // StringEscapeUtils.escapeXml11(thirdPrjInfoTemp.getInsName())
    Map<String, Object> map = new HashMap<String, Object>();
    ThirdPrjInfoTemp thirdPrjInfoTemp = (ThirdPrjInfoTemp) dataParamet.get(OpenConsts.MAP_DATA_THIRD_PRJ);
    String syncXml = "<root><groupPsn>" + "<groupName>" + StringEscapeUtils.escapeXml11(thirdPrjInfoTemp.getGroupName())
        + "</groupName>" + "<applyCode>" + thirdPrjInfoTemp.getApplyCode() + "</applyCode>" + "<fundingTypes>"
        + thirdPrjInfoTemp.getFundingTypes() + "</fundingTypes>" + "<fundingIns>"
        + thirdPrjInfoTemp.getSchemeAgencyName() + "</fundingIns>" // 资助机构
        + "<prjExternalNo>" + thirdPrjInfoTemp.getPrjExternalNo() + "</prjExternalNo>" + "<amount>"
        + thirdPrjInfoTemp.getAmount() + "</amount>" + "<startDate>" + thirdPrjInfoTemp.getStartDate() + "</startDate>"
        + "<endDate>" + thirdPrjInfoTemp.getEndDate() + "</endDate>" + "<currency>" + thirdPrjInfoTemp.getCurrency()
        + "</currency>" + "<insName>" + StringEscapeUtils.escapeXml11(thirdPrjInfoTemp.getInsName()) + "</insName>"
        + "<partPsnNames>" + thirdPrjInfoTemp.getPartPsnNames() + "</partPsnNames>" + "<prjStatus>" + thirdPrjInfoTemp
        + "</prjStatus>" + "<groupCategory>" + "11" + "</groupCategory>" + "<openType>" + "H" + "</openType>"
        + "</groupPsn></root>";
    map.put("syncXml", syncXml);
    map.put("fromSys", dataParamet.get(OpenConsts.MAP_DATA_FROM_SYS));
    return JacksonUtils.mapToJsonStr(map);

  }

  /**
   * 检查第三方项目的信息
   * 
   * @param thirdPrjInfoTemp
   * @return
   */
  public String checkThirdPrjInfo(ThirdPrjInfoTemp thirdPrjInfoTemp) {
    if (StringUtils.isBlank(thirdPrjInfoTemp.getGroupName())) {
      logger.error("群组名称//项目名称 groupName,不能为空！");
      return "群组名称//项目名称groupName ,不能为空！";
    }

    if (StringUtils.isNotBlank(thirdPrjInfoTemp.getPrjStatus())) {
      if (!NumberUtils.isNumber(thirdPrjInfoTemp.getPrjStatus()) || thirdPrjInfoTemp.getPrjStatus().length() > 1) {
        logger.error("项目状态prjStatus长度只能等于1 ,且为数字！");
        return "项目状态prjStatus长度只能等于1,且为数字！";
      }

    }
    if (StringUtils.isNotBlank(thirdPrjInfoTemp.getAmount())) {
      if (!NumberUtils.isNumber(thirdPrjInfoTemp.getAmount())
          || NumberUtils.toDouble(thirdPrjInfoTemp.getAmount()) > 1000000000000.0) {
        logger.error("项目金额amount整数长度不能大于12位 ,且为数字 ！");
        return "项目金额amount整数长度不能大于12位  ,且为数字！";
      }
    }
    if (StringUtils.isNotBlank(thirdPrjInfoTemp.getPartPsnNames())) {
      if (!JacksonUtils.isJsonString10(thirdPrjInfoTemp.getPartPsnNames())
          || thirdPrjInfoTemp.getPartPsnNames().length() > 1000) {
        logger.error("参与人员信息格式必须为 json ,且长度小于1000 ！");
        return "参与人员信息格式必须为 json ,且长度小于1000 ！";
      }
    }
    if (StringUtils.isNotBlank(thirdPrjInfoTemp.getKeywordsZh())) {
      if (!JacksonUtils.isJsonString10(thirdPrjInfoTemp.getKeywordsZh())
          || thirdPrjInfoTemp.getKeywordsZh().length() > 1000) {
        logger.error("中文关键词格式必须为 json ,且长度小于1000 ！");
        return "中文关键词格式必须为 json ,且长度小于1000 ！";
      }
    }
    if (StringUtils.isNotBlank(thirdPrjInfoTemp.getKeywordsEn())) {
      if (!JacksonUtils.isJsonString10(thirdPrjInfoTemp.getKeywordsEn())
          || thirdPrjInfoTemp.getKeywordsEn().length() > 1000) {
        logger.error("英文关键词格式必须为 json ,且长度小于1000 ！");
        return "英文关键词格式必须为 json ,且长度小于1000 ！";
      }
    }
    return null;
  }

  /**
   * 
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
   * 天充时间
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

  public String buildResultXml(String result, String getUnionGroupStatus) {
    Document doc;
    try {
      doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><group></group>");
      Element rootNode = (Element) doc.selectSingleNode("/group");
      rootNode.addElement("result").addText(result);
      rootNode.addElement("getUnionGroupStatus").addText(getUnionGroupStatus);
      return doc.getRootElement().asXML();
    } catch (DocumentException e) {
      logger.error("构造成果XML列表出现异常", e);
    }
    return "";
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
