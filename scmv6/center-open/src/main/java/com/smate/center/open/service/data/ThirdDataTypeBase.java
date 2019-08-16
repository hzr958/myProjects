package com.smate.center.open.service.data;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.data.log.OpenDataAccessLogDao;
import com.smate.center.open.model.OpenErrorLog;
import com.smate.center.open.model.data.log.OpenDataAccessLog;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.utils.json.JacksonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 开放数据 数据服务基础类
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */

public abstract class ThirdDataTypeBase implements ThirdDataTypeService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private OpenErrorLogService openErrorLogService;
  @Autowired
  private OpenDataAccessLogDao openDataAccessLogDao;
  @Resource(name = "restTemplate")
  protected RestTemplate restTemplate;
  @Value("${domainscm}")
  public String domainscm;

  /**
   * 主要是校验业务参数 不校验权限参数 校验参数 方法 返回值 必需有 {status:success/error} 如果是error 必需包含错误信息
   * 
   * @param paramet
   * @return
   */
  public abstract Map<String, Object> doVerify(Map<String, Object> paramet);

  // 数据处理方法
  public abstract Map<String, Object> doHandler(Map<String, Object> paramet);

  @Override
  public Map<String, Object> handleOpenDataForType(Map<String, Object> paramet) {
    Map<String, Object> returnMap = doVerify(paramet);
    if (OpenConsts.RESULT_STATUS_SUCCESS.equalsIgnoreCase(returnMap.get(OpenConsts.RESULT_STATUS).toString())) {
      // 就在这里记录访问日志 （正确的访问日志）
      saveAccessLog(paramet);
      return doHandler(paramet);
    } else {
      return returnMap;
    }
  }

  /**
   * 记录访问日志 （正确的访问日志）
   */
  private void saveAccessLog(Map<String, Object> paramet) {

    try {
      OpenDataAccessLog openDataAccessLog = new OpenDataAccessLog();
      openDataAccessLog.setOpenId(paramet.get(OpenConsts.MAP_OPENID).toString());
      openDataAccessLog.setToken(paramet.get(OpenConsts.MAP_TOKEN).toString());
      openDataAccessLog.setServiceType(paramet.get(OpenConsts.MAP_TYPE).toString());
      openDataAccessLog.setParameter(JacksonUtils.mapToJsonStr(paramet));
      openDataAccessLog.setAccessDate(new Date());
      openDataAccessLog.setRequestType(
          paramet.get(OpenConsts.REAUEST_TYPE) == null ? "3" : paramet.get(OpenConsts.REAUEST_TYPE).toString());
      openDataAccessLogDao.save(openDataAccessLog);
    } catch (Exception e) {
      logger.error("保存访问日志出错，吃掉异常!", e);
    }
  }

  /**
   * 公用封装错误 保持错误日志方法
   * 
   * @param msg
   * @param openData
   * @param e
   * @return
   */
  protected Map<String, Object> errorMap(String msg, Map<String, Object> openData, String e) {
    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
    temp.put(OpenConsts.RESULT_MSG, msg);
    // 记录错误日志
    OpenErrorLog openErrorLog = new OpenErrorLog();
    openErrorLog.setErrorDate(new Date());
    openErrorLog.setErrorFlag(msg);
    openErrorLog
        .setOpenId(openData.get(OpenConsts.MAP_OPENID) == null ? null : openData.get(OpenConsts.MAP_OPENID).toString());
    openErrorLog
        .setToken(openData.get(OpenConsts.MAP_TOKEN) == null ? null : openData.get(OpenConsts.MAP_TOKEN).toString());
    openErrorLog.setErrorInfo(openData.toString() + e);
    openErrorLogService.saveOpenErrorLog(openErrorLog);
    return temp;
  }

  /**
   * 公用 数据交互成功 调用 方法
   * 
   * @param msg
   * @param resultData 需要返回的数据注意要封装成list
   * @return
   */
  protected Map<String, Object> successMap(String msg, Object resultData) {
    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    temp.put(OpenConsts.RESULT_MSG, msg);
    temp.put(OpenConsts.RESULT_DATA, resultData);
    return temp;
  }

  /**
   * 数据参数校验公用方法 map格式
   * 
   * @param paramet
   * @param temp
   */
  protected Map<String, Object> checkDataMapParamet(Map<String, Object> paramet, Map<String, Object> temp) {
    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data == null || data.toString().length() == 0) {
      logger.error("服务参数不能为空");
      temp.putAll(errorMap(OpenMsgCodeConsts.SCM_224, paramet, ""));
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
      return null;
    }
    boolean b = JacksonUtils.isJsonString(data.toString());
    if (!b) {
      logger.error("服务参数格式不正确,必须是json格式");
      temp.putAll(errorMap(OpenMsgCodeConsts.SCM_225, paramet, ""));
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
      return null;
    }
    Map<String, Object> serviceData = JacksonUtils.jsonToMap(data.toString());
    if (serviceData == null) {
      logger.error("服务参数格式不正确,服务参数不能正确的转换成Map");
      temp.putAll(errorMap(OpenMsgCodeConsts.SCM_226, paramet, ""));
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
      return null;
    }
    return serviceData;
  }

  /**
   * 查询远程成果的信息
   * 
   * @param pubQueryDTO
   * @param SERVER_URL
   * @return
   */
  public Object getRemotePubInfo(PubQueryDTO pubQueryDTO, String SERVER_URL) {
    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity =
        new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(pubQueryDTO), requestHeaders);
    Map<String, Object> object =
        (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    return object;
  }

  /**
   * 查询远程成果的信息
   * 
   * @param SERVER_URL
   * @return
   */
  public Object getRemotePubInfo(Map<String, Object> map, String SERVER_URL) {
    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity = new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(map), requestHeaders);
    Map<String, Object> object =
        (Map<String, Object>) restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    return object;
  }


}
