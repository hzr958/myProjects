package com.smate.center.open.service.interconnection.pub;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.dao.nsfc.logs.LogInfoDao;
import com.smate.center.open.model.nsfc.logs.LogInfo;
import com.smate.center.open.service.interconnection.pub.extract.ExtractFileToXmlService;
import com.smate.center.open.service.util.PubDetailVoUtil;
import com.smate.core.base.pub.vo.PubDetailVO;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.security.Des3Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
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
import java.util.List;
import java.util.Map;

public abstract class AbstractCommonPubInfoService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private LogInfoDao logInfoDao;

  /**
   * 成果模板
   */
  public static Map<String, String> pubTemplateMap = new HashMap<String, String>();
  static {
    pubTemplateMap.put("isis", "ISIS_PUB");
    pubTemplateMap.put("isis_group", "ISIS_GROUP_PUB");
  }

  @Resource(name = "oldIsisPubService")
  private OldIsisPubService oldIsisPubService;
  @Autowired
  private ExtractFileToXmlService extractFileToXmlService;
  @Value("${domainscm}")
  public String domainscm;
  @Resource(name = "restTemplate")
  protected RestTemplate restTemplate;

  /**
   * 检查： 获取成果的 pageNo pageSize
   * 
   * @param dataParamet
   * @return
   */
  public String checkCommonParam(Map<String, Object> dataParamet) {
    Object pageNo = dataParamet.get("pageNo");
    Object pageSize = dataParamet.get("pageSize");
    Object data_template_key = dataParamet.get("data_template_key");
    if (pageNo == null) {
      logger.error("具体服务类型参数pageNo不能为空");
      return "pageNo缺少";
    }
    if (pageSize == null) {
      logger.error("具体服务类型参数pageSize不能为空");
      return "pageSize缺少";
    }

    if (!NumberUtils.isNumber(pageNo.toString())) {
      logger.error("具体服务类型参数pageNo格式不正确");
      return "pageNo格式不正确";
    }
    if (!NumberUtils.isNumber(pageSize.toString())) {
      logger.error("具体服务类型参数pageSize格式不正确");
      return "pageSize格式不正确";
    }

    if (data_template_key == null) {
      logger.error("具体服务类型参数data_template_key不能为空");
      return "data_template_key缺少";
    } else {
      if (StringUtils.isBlank(pubTemplateMap.get(data_template_key.toString()))) {
        logger.error("具体服务类型参数data_template_key参数不正确");
        return "data_template_key参数不正确";
      }
    }
    return null;
  }

  /**
   * 
   * @param resultList
   * @param dataMap
   * @param dataParamet open入口 传来的参数
   * @param isGrpPubs 群组成果的标志
   * @return
   */
  public String buildUnionPubListXmlStr(List<Map<String, Object>> resultList, HashMap<String, Object> dataMap,
      Map<String, Object> dataParamet, Boolean isGrpPubs) {

    // 返回的map数据
    Map<Integer, Map<String, Object>> resultMap = new HashMap<Integer, Map<String, Object>>();
    int index = 0;
    for (Map<String, Object> pubInfoMap : resultList) {
      Map<String, Object> pubMap = new HashMap<String, Object>();
      try {
        if (pubInfoMap.get("status") != null && (pubInfoMap.get("status").toString() == "1" || Integer.parseInt(pubInfoMap.get("status").toString()) == 1)) { // 个人成果已经删除的
          pubMap.put("status", "1");
          pubMap.put("pub_id", pubInfoMap.get("pubId"));
        } else {
          String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_DETAIL_URL;
          Map<String, Object> map = new HashMap<>();
          map.put(V8pubQueryPubConst.DES3_PUB_ID, Des3Utils.encodeToDes3(pubInfoMap.get("pubId").toString()));
          map.put("serviceType", V8pubQueryPubConst.QUERY_OPEN_SNS_PUB);
          Map<String, Object> pubDetailMap = (Map<String, Object>) getRemotePubInfo(map, SERVER_URL);
          if (pubDetailMap != null) {
            PubDetailVO pubDetailVO = PubDetailVoUtil.getBuilPubDetailVO(JacksonUtils.mapToJsonStr(pubDetailMap));
            pubMap = oldIsisPubService.parseXmlToMap(pubDetailVO);
            pubMap.put("status", 0);
          }

        }
      } catch (Exception e) {
        this.saveAccessLog(dataParamet, e.toString(), 2, "获取成果历史数据异常，服务类型为："
            + dataParamet.get(OpenConsts.MAP_SERVICE_TYPE).toString() + " 异常成果id：" + pubMap.get("pubId"));
        continue;
      }
      // 群组成果相关度
      if (isGrpPubs) {
        pubMap.put("labeled", pubInfoMap.get("labeled"));
        pubMap.put("relevance", pubInfoMap.get("relevance"));
      }
      resultMap.put(index++, pubMap);
    }
    // 文件名是通过 系统类型来获取的
    String fileName = dataMap.get("pubTemplate").toString();
    Map<String, Object> extraData = new HashMap<String, Object>();
    extraData.put("open_id", dataMap.get("openId").toString());
    extraData.put("total_pages", dataMap.get("totalPages") == null ? "" : dataMap.get("totalPages").toString());
    extraData.put("count", dataMap.get("count") == null ? "" : dataMap.get("count").toString());
    extraData.put("current_get_pub_date",
        dataMap.get("currentGetPubDate") == null ? "" : dataMap.get("currentGetPubDate").toString());
    // 群组
    if (dataMap.get("groupCode") != null) {
      extraData.put("group_code", dataMap.get("groupCode").toString());
    }
    // -1 表示额外的参数
    resultMap.put(-1, extraData);
    String extract = extractFileToXmlService.extract(resultMap, fileName);
    return extract;

  }

  /**
   * 日志信息记录
   * 
   * @param dataParamet
   * @param msg
   * @param status
   */
  public void saveAccessLog(Map<String, Object> dataParamet, String msg, int status, String desc) {
    LogInfo log = new LogInfo();
    log.setActionDate(new Date());
    log.setClientIP(dataParamet.get(OpenConsts.MAP_TOKEN).toString()); // token
    log.setActionResource(msg); // 操作结果 后者异常信息
    log.setMethodName(dataParamet.get(OpenConsts.MAP_SERVICE_TYPE).toString()); // 服务类型
    log.setParameters(JacksonUtils.mapToJsonStr(dataParamet)); // 调用open系统的参数
    log.setStatus(status); // 1成功 ,2失败
    if (dataParamet.get(OpenConsts.MAP_HISTORY_PUBLICATION) != null) {
      log.setDescription(desc + "同步历史数据"); // 描述-- 重点
    } else {
      log.setDescription(desc + "同步成果数据"); // 描述-- 重点
      // log.setDescribe("同步成果数据");
    }
    logInfoDao.save(log);
  }

  /**
   * 查询远程成果的信息
   * 
   * @param param
   * @param SERVER_URL
   * @return
   */
  public Object getRemotePubInfo(Object param, String SERVER_URL) {
    // 设置请求头部
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    HttpEntity<String> requestEntity = new HttpEntity<String>(JacksonUtils.jsonObjectSerializer(param), requestHeaders);
    Object object = restTemplate.postForObject(SERVER_URL, requestEntity, Object.class);
    return object;
  }
}
