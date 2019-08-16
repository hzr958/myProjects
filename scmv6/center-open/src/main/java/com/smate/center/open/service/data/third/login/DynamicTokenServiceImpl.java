package com.smate.center.open.service.data.third.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;

/**
 * 获取动态 token服务实现
 * 
 * @author tsz
 *
 */
public class DynamicTokenServiceImpl extends ThirdDataTypeBase {

  @Autowired
  private OpenCacheService openCacheService;

  /**
   * 参数校验
   */
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

    Object dynTokenType = serviceData.get(OpenConsts.DYN_TOKEN_TYPE);
    if (dynTokenType == null) {
      logger.error("服务参数 动态token类型不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_252, paramet, "服务参数 动态token类型不能为空");
      return temp;
    }
    Integer dynTokenCacheTime = DynamicTokenEnum.valueOf(dynTokenType.toString()).toInt();
    if (dynTokenCacheTime == null) {
      logger.error("服务参数  动态token类型不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_253, paramet, "服务参数 动态token类型不正确");
      return temp;
    }
    paramet.put("dynTokenType", dynTokenType);
    paramet.put("dynTokenCacheTime", dynTokenCacheTime);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  /**
   * 业务实现
   */
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    // 随机生成一个动态token 这个token有一定的时间限制
    String dynToken = DigestUtils.md5Hex(UUID.randomUUID().toString() + paramet.get(OpenConsts.MAP_TOKEN));

    // 区分类型 保存不一样的 时间限制 或者次数限制
    // 保存缓存
    openCacheService.put(OpenConsts.DYN_TOKEN_CACHE,
        DynamicTokenEnum.valueOf(paramet.get("dynTokenType").toString()).toInt(),
        dynToken + "_" + paramet.get("dynTokenType").toString(), paramet.get(OpenConsts.MAP_TOKEN).toString());

    Map<String, Object> temp = new HashMap<String, Object>();
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Map<String, Object> dataMap = new HashMap<String, Object>();
    dataMap.put(OpenConsts.DYN_TOKEN, dynToken);
    dataList.add(dataMap);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    temp.put(OpenConsts.RESULT_MSG, "scm-0000");// 响应成功
    temp.put(OpenConsts.RESULT_DATA, dataList);
    return temp;
  }

}
