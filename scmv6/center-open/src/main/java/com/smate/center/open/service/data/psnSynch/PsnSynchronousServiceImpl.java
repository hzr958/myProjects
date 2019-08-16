package com.smate.center.open.service.data.psnSynch;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.psn.sysch.ThirdPersonDao;
import com.smate.center.open.exception.OpenSyncPsnException;
import com.smate.center.open.model.third.psn.ThirdPsnInfo;
import com.smate.center.open.model.third.psn.ThirdPsnInfoTemp;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.psnSynch.SynchronousPersonNsfcService;
import com.smate.center.open.utils.xml.WebServiceUtils;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 不同系统 人员同步
 * 
 * @author AiJiangBin
 *
 */
@Transactional(rollbackFor = Exception.class)
public class PsnSynchronousServiceImpl extends ThirdDataTypeBase {

  @Autowired
  private ThirdPersonDao thirdPersonDao;

  // 具体来源系统类型 ： 区分调用什么系统的服务实现 eg ISIS SCM GX....
  private Map<String, SynchronousPersonNsfcService> nsfcServiceMap;

  public Map<String, SynchronousPersonNsfcService> getNsfcServiceMap() {
    return nsfcServiceMap;
  }

  public void setNsfcServiceMap(Map<String, SynchronousPersonNsfcService> nsfcServiceMap) {
    this.nsfcServiceMap = nsfcServiceMap;
  }

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {

    Map<String, Object> temp = new HashMap<String, Object>();
    // 校验数据参数
    try {
      Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
      if (temp.get(OpenConsts.RESULT_STATUS) != null
          && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
        return temp;
      }
      // 校验 来源系统参数

      Object fromSys = serviceData.get(OpenConsts.MAP_DATA_FROM_SYS);
      if (fromSys == null || "".equals(fromSys.toString())) {
        logger.error("人员同步来源系统不能为空");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_236, paramet, "");
        return temp;
      }

      paramet.put(OpenConsts.MAP_DATA_FROM_SYS, fromSys);
      /*
       * SynchronousPersonNsfcService synchronousPersonNsfcService = nsfcServiceMap
       * .get(serviceData.get(OpenConsts.MAP_DATA_FROM_SYS)); if (synchronousPersonNsfcService == null) {
       * logger.error("人员同步来源系统 不正确"); temp = super.errorMap(OpenMsgCodeConsts.SCM_240, paramet, "");
       * return temp; }
       */

      // 校验人员同步 具体参数
      Object psnData = serviceData.get(OpenConsts.PSN_DATA);
      if (psnData == null || "".equals(psnData.toString())) {
        logger.error("人员同步 人员基本数据不能为空");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_237, paramet, "");
        return temp;
      }

      Boolean isJson = JacksonUtils.isJsonString10(psnData.toString());
      if (!isJson) {
        logger.error("psnData不是json格式的字符串");
        temp = super.errorMap("psnData不是json格式的字符串", paramet, "");
        return temp;
      }
      Map<String, String> psnInfoMap = JacksonUtils.jsonToMap(psnData.toString());
      ThirdPsnInfoTemp thirdPsnInfoTemp = WebServiceUtils.toThirdPsnInfoTempByMap(psnInfoMap);
      if (serviceData.get("specialJson") != null && "true".equals(serviceData.get("specialJson"))) {
        String result = checkPsnInfoJsonParam(thirdPsnInfoTemp);
        if (StringUtils.isNotBlank(result)) {
          logger.error(result);
          temp = super.errorMap(result, paramet, "");
          return temp;
        }
      }

      ThirdPsnInfo thirdPsnInfo = new ThirdPsnInfo();
      fillThirdPsnInfo(paramet, thirdPsnInfoTemp, thirdPsnInfo);
      paramet.put(OpenConsts.MAP_DATAPATAMETMAP, serviceData);
      paramet.put(OpenConsts.MAP_DATA_PERSON_SYSCH, thirdPsnInfo);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    } catch (Exception e) {
      logger.error("人员同步，参数校验出错", e);
      throw new OpenSyncPsnException("人员同步，参数校验出错", e);
    }
    return temp;

  }

  public String checkPsnInfoJsonParam(ThirdPsnInfoTemp thirdPsnInfoTemp) {
    String disciplineCode = thirdPsnInfoTemp.getDisciplineCodes();
    String researchDirection = thirdPsnInfoTemp.getResearchDirection();
    String keywordsZh = thirdPsnInfoTemp.getKeywordsZh();
    String keywordsEn = thirdPsnInfoTemp.getKeywordsEn();
    if (StringUtils.isNotBlank(disciplineCode)) {
      if (!JacksonUtils.isJsonString10(disciplineCode) || disciplineCode.length() > 1000) {
        return "disciplineCode不是json格式或者长度大于1000";
      }
    }
    if (StringUtils.isNotBlank(researchDirection)) {
      if (!JacksonUtils.isJsonString10(researchDirection) || researchDirection.length() > 1000) {
        return "researchDirection不是json格式或者长度大于1000";
      }
    }
    if (StringUtils.isNotBlank(keywordsZh)) {
      if (!JacksonUtils.isJsonString10(keywordsZh) || keywordsZh.length() > 1000) {
        return "keywordsZh不是json格式或者长度大于1000";
      }
    }
    if (StringUtils.isNotBlank(keywordsEn) || keywordsEn.length() > 1000) {
      if (!JacksonUtils.isJsonString10(keywordsEn)) {
        return "keywordsEn不是json格式或者长度大于1000";
      }
    }
    return "";
  }

  private void fillThirdPsnInfo(Map<String, Object> paramet, ThirdPsnInfoTemp thirdPsnInfoTemp,
      ThirdPsnInfo thirdPsnInfo) {

    thirdPsnInfo.setPsnId(NumberUtils.toLong(paramet.get(OpenConsts.MAP_PSNID).toString()));
    thirdPsnInfo.setFromSys(paramet.get(OpenConsts.MAP_DATA_FROM_SYS).toString());

    thirdPsnInfo.setName(truncateString(thirdPsnInfoTemp.getName(), 61));
    thirdPsnInfo.setEmail(truncateString(thirdPsnInfoTemp.getEmail(), 50));
    thirdPsnInfo.setInsName(truncateString(thirdPsnInfoTemp.getInsName(), 300));
    thirdPsnInfo.setDepartment(truncateString(thirdPsnInfoTemp.getDepartment(), 601));
    thirdPsnInfo.setPosition(truncateString(thirdPsnInfoTemp.getPosition(), 200));
    thirdPsnInfo.setDegree(truncateString(thirdPsnInfoTemp.getDegree(), 30));
    // String code = thirdPsnInfoTemp.getDisciplineCode();
    // if (StringUtils.isNotBlank(code) && NumberUtils.isNumber(code)) {
    // 第三方系统人员学科代码
    thirdPsnInfo.setDisciplineCodes(truncateString(thirdPsnInfoTemp.getDisciplineCodes(), 1000));
    // }
    thirdPsnInfo.setBirthdate(truncateString(thirdPsnInfoTemp.getBirthdate(), 10));
    thirdPsnInfo.setCountry(truncateString(thirdPsnInfoTemp.getCountry(), 50));
    thirdPsnInfo.setProvince(truncateString(thirdPsnInfoTemp.getProvince(), 50));
    thirdPsnInfo.setCity(truncateString(thirdPsnInfoTemp.getCity(), 50));
    thirdPsnInfo.setResearchDirection(truncateString(thirdPsnInfoTemp.getResearchDirection(), 1000));
    thirdPsnInfo.setKeywordsZh(truncateString(thirdPsnInfoTemp.getKeywordsZh(), 1000));
    thirdPsnInfo.setKeywordsEn(truncateString(thirdPsnInfoTemp.getKeywordsEn(), 1000));
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {

    @SuppressWarnings("unchecked")
    Map<String, Object> serviceData = (Map<String, Object>) paramet.get(OpenConsts.MAP_DATAPATAMETMAP);
    ThirdPsnInfo thirdPsnInfo = (ThirdPsnInfo) paramet.get(OpenConsts.MAP_DATA_PERSON_SYSCH);
    try {
      // 不同来源系统的具体处理信息。 暂时没有特别的 ，统一公用
      /*
       * SynchronousPersonNsfcService synchronousPersonNsfcService=nsfcServiceMap.get(serviceData.get(
       * OpenConsts.MAP_DATA_FROM_SYS)); synchronousPersonNsfcService.handleNsfcData(thirdPsnInfo);
       */
      Long id = thirdPersonDao.getId(thirdPsnInfo.getPsnId(), thirdPsnInfo.getFromSys());
      if (id != null) {
        thirdPsnInfo.setId(id);
      }

      thirdPersonDao.save(thirdPsnInfo);

      return super.successMap("人员同步成功", null);
    } catch (Exception e) {
      logger.error("人员同步，初始化数据处理出错", e);
      throw new OpenSyncPsnException("人员同步，初始化数据处理出错", e);
    }

  }

  /**
   * 截断字符
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
