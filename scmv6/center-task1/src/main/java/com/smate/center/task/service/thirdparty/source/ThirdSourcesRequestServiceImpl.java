package com.smate.center.task.service.thirdparty.source;

import com.smate.center.task.model.thirdparty.ThirdSources;
import com.smate.center.task.model.thirdparty.ThirdSourcesGetLog;
import com.smate.center.task.model.thirdparty.ThirdSourcesType;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.file.FileUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.string.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * 调用业务系统接口实现类.
 *
 * @author tsz
 *
 */
@Service("thirdSourcesRequestService")
@Transactional(rollbackFor = Exception.class)
public class ThirdSourcesRequestServiceImpl implements ThirdSourcesRequestService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private RestTemplate restTemplate;
  @Autowired
  private ThirdSourcesGetLogService thirdSourcesGetLogService;

  @Override
  public List<Map<String, Object>> postUrl(ThirdSources ts, ThirdSourcesType tst) {
    // 通过js文件，获取内容
    if(ts.getFromUrl().endsWith(".js")){
      return  getFileUrl(ts , tst);
    }
    List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();
    // 拼接参数调用接口
    Map<String, String> map = new HashMap<>();
    map.put("type", tst.getType().toString());
    map.put("time_node", DateUtils.getDateFormat(tst.getLastGetDate(), "yyyy-MM-dd hh:mm:ss"));
    map.put("from_sys", "smate");
    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity = new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(map), requestHeaders);
    Object object = null;
    try {
      object = restTemplate.postForObject(ts.getFromUrl(), requestEntity, String.class);
    } catch (RestClientException e) {
      logger.error("调用业务系统接口出错", e);
      // 保存调用记录
      buildLog(tst, map, null, null, 1);
      return listResult;
    }
    if (object == null) {
      // 记录调用记录
      buildLog(tst, map, null, null, 1);
      return listResult;
    }
    resultBuild(tst, listResult, map, object);
    return listResult;
  }

  public List<Map<String, Object>> getFileUrl(ThirdSources ts, ThirdSourcesType tst) {

    List<Map<String, Object>> listResult = new ArrayList<Map<String, Object>>();
    // 拼接参数调用接口
    Map<String, String> map = new HashMap<>();
    String contnet = "";
    try {
      contnet = FileUtils.openFile(ts.getFromUrl()+"?time="+new Date().getTime());
    } catch (Exception e) {
      logger.error("调用业务系统接口出错", e);
      // 保存调用记录
      buildLog(tst, map, null, null, 1);
      return listResult;
    }
    if (StringUtils.isBlank(contnet)) {
      logger.error("调用业务系统接口出错,获取js文件为空");
      // 记录调用记录
      buildLog(tst, map, null, null, 1);
      return listResult;
    }
    fileResultBuild(tst, listResult, map, contnet);
    return listResult;
  }


  /**
   * 结果解析
   *
   * @param tst
   * @param listResult
   * @param map
   * @param object
   */
  private void resultBuild(ThirdSourcesType tst, List<Map<String, Object>> listResult, Map<String, String> map,
      Object object) {
    Map<String, Object> mapResult = null;
    try {
      mapResult = JacksonUtils.json2HashMap(object.toString());
    } catch (Exception e) {
      logger.error("接口返回结果解析失败", e);
      // 保存调用记录
      buildLog(tst, map, object.toString(), null, 2);
      return;
    }
    if (mapResult == null) {
      // 记录调用记录
      buildLog(tst, map, object.toString(), null, 2);
      return;
    }
    // 判断接口返回情况,并记录
    Object statusO = mapResult.get("status");
    Object resultO = mapResult.get("result");
    if (statusO == null || resultO == null) {
      // 记录调用记录
      buildLog(tst, map, object.toString(), mapResult, 3);
      return;
    }
    if ("success".equals(statusO.toString())) {
      if(mapResult.get("result") instanceof  List){
        List<Map<String,Object>>  dataList = (List<Map<String, Object>>)mapResult.get("result");
        listResult.addAll(dataList);
      }else{
        List<Map<String,Object>>  dataList = JacksonUtils.jsonListObjUnSerializer(mapResult.get("result").toString());
        listResult.addAll(dataList);
      }
      if (listResult == null) {
        buildLog(tst, map, object.toString(), mapResult, 4);
      } else {
        buildLog(tst, map, object.toString(), mapResult, 0);
      }
    } else {
      // 响应错误标记
      buildLog(tst, map, object.toString(), mapResult, 0);
    }
  }

  /**
   * 结果解析
   *
   * @param tst
   * @param listResult
   * @param map
   * @param object
   */
  private void fileResultBuild(ThirdSourcesType tst, List<Map<String, Object>> listResult, Map<String, String> map,
      Object object) {
    List<Map<String,Object>>  dataList = null ;
    try {
      dataList = JacksonUtils.jsonToList(object.toString());
      listResult.addAll(dataList);
    } catch (Exception e) {
      logger.error("接口返回结果解析失败", e);
      // 保存调用记录
      buildLog(tst, map, object.toString(), null, 4);
      return;
    }
    if (listResult == null) {
      buildLog(tst, map, object.toString(), null, 4);
    } else {
      buildLog(tst, map, object.toString(), null, 0);
    }

  }

  /**
   * 日志构建
   *
   * @param tst
   * @param parame
   * @param strResult
   * @param mapResult
   * @param status
   */
  private void buildLog(ThirdSourcesType tst, Map<String, String> parame, String strResult,
      Map<String, Object> mapResult, int status) {

    ThirdSourcesGetLog thirdSourcesGetLog = new ThirdSourcesGetLog();
    thirdSourcesGetLog.setRequestParams(JacksonUtils.mapToJsonStr(parame));
    thirdSourcesGetLog.setResult(strResult);
    if (mapResult != null) {
      thirdSourcesGetLog.setResultStatus(mapResult.get("status") == null ? "" : mapResult.get("status").toString());
    }
    thirdSourcesGetLog.setSourceId(tst.getSourceId());
    thirdSourcesGetLog.setType(tst.getType());
    thirdSourcesGetLog.setStatus(status);
    thirdSourcesGetLog.setCreateDate(new Date());
    try {
      thirdSourcesGetLogService.saveLog(thirdSourcesGetLog);
    } catch (Exception e) {
      logger.error("保存调用日志出错", e);
    }
  }


}
