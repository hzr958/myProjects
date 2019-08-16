package com.smate.center.open.service.interconnection.pub;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.dao.interconnection.UnionRefreshPubLogDao;
import com.smate.center.open.model.interconnection.UnionRefreshPubLog;
import com.smate.center.open.service.interconnection.ScmUnionDataHandleService;
import com.smate.center.open.service.interconnection.log.InterconnectionGetPubLogService;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 获取互联成果
 * 
 * @author AiJiangBin
 *
 */
public class GetPubInfoServiceImpl extends AbstractCommonPubInfoService implements ScmUnionDataHandleService {
  protected Logger logger = LoggerFactory.getLogger(getClass());


  @Autowired
  private UnionRefreshPubLogDao unionRefreshPubLogDao;
  @Autowired
  private InterconnectionGetPubLogService interconnectionGetPubLogService;



  @Override
  public String handleUnionData(Map<String, Object> dataParamet) throws Exception {

    // data 在前面已经 验证不为空 ，并且是json格式的
    String lastGetPubDate = "";
    String currentGetPubDate = "";
    Date getPubDate = null;
    Object data = dataParamet.get(OpenConsts.MAP_DATA);
    Map<String, Object> serviceData = JacksonUtils.jsonToMap(data.toString());
    Object lastTimeObj = serviceData.get("lastGetPubDate");
    lastGetPubDate = lastTimeObj != null ? lastTimeObj.toString() : "";
    String temp = checkCommonParam(serviceData);
    if (temp != null) {
      return buildResultXml(temp, "4");
    }
    getPubDate = DateUtils.parseStringToDate(lastGetPubDate);
    Long psnId = NumberUtils.toLong(dataParamet.get("psnId").toString());
    Long openId = NumberUtils.toLong(dataParamet.get("openid").toString());
    Integer pageNo = NumberUtils.toInt(serviceData.get("pageNo").toString());
    Integer pageSize = NumberUtils.toInt(serviceData.get("pageSize").toString());
    String pubTemplate = pubTemplateMap.get(serviceData.get("data_template_key").toString());

    if (serviceData.get(OpenConsts.MAP_HISTORY_PUBLICATION) != null) {
      dataParamet.put("historyPublication", serviceData.get("historyPublication"));
    }
    ArrayList<Integer> pubTypeList = new ArrayList<Integer>();
    buildPubType(serviceData, pubTypeList);

    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPsnId(psnId);
    pubQueryDTO.setPubType(serviceData.get("pubType").toString());
    pubQueryDTO.setPageNo(pageNo);
    pubQueryDTO.setPageSize(pageSize);
    pubQueryDTO.setQueryAll(false);
    if (getPubDate != null) {
      pubQueryDTO.setPubUpdateDate(getPubDate);
    }

    pubQueryDTO.setServiceType(V8pubQueryPubConst.CENTER_OPEN_PUB_LIST);
    Map<String, Object> result = (Map<String, Object>) getRemotePubInfo(pubQueryDTO, SERVER_URL);
    Long totalCount = 0L;
    if (result.get("status").equals("success")) {
      totalCount = Long.parseLong(result.get("totalCount").toString());
      if (totalCount == null || totalCount < 1) {
        // 没有成果
        if (getPubDate != null) {
          return buildResultXml("没有成果被更新", "7");
        }
        return buildResultXml("没有查询到成果!", "3");
      }
    } else {
      return buildResultXml("scm-111 系统异常,!", "3");
    }
    if (pageNo < 1) {
      pageNo = 1;
    }
    if (pageSize < 1) {
      pageSize = 1;
    }
    Long totalPages = 0L;
    if (totalCount % pageSize == 0) {
      totalPages = totalCount / pageSize;
    } else {
      totalPages = totalCount / pageSize + 1;
    }
    currentGetPubDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    List<Map<String, Object>> resultList = (List<Map<String, Object>>) result.get("resultList");
    if (resultList != null && resultList.size() > 0) {
      // 记录日志
      interconnectionGetPubLogService.saveGetPubLog(1, resultList.size(), dataParamet);
      try {
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("openId", openId);
        dataMap.put("totalPages", totalPages);
        dataMap.put("count", totalCount);
        dataMap.put("pubTemplate", pubTemplate);
        dataMap.put("currentGetPubDate", currentGetPubDate);
        return buildUnionPubListXmlStr(resultList, dataMap, dataParamet, false);
      } catch (Exception e) {
        // 吃掉异常，当做open系统的正确请求来处理
        logger.error(e.getMessage() + JacksonUtils.mapToJsonStr(dataParamet), e);
        this.saveAccessLog(dataParamet, e.toString(), 2,
            "获取成果历史数据异常，服务类型为：" + dataParamet.get(OpenConsts.MAP_SERVICE_TYPE).toString());
        return buildResultXml("构建成果 列表 出现异常", "5");
      }
    }
    return buildResultXml("没有查询到成果!", "3");
  }


  /**
   * 兼容 成果Type
   * 
   * @param serviceData
   * @param pubTypeList
   */
  private void buildPubType(Map<String, Object> serviceData, ArrayList<Integer> pubTypeList) {
    Object pubTypeObj = serviceData.get("pubType");
    String type = "";
    if (pubTypeObj != null && StringUtils.isNotBlank(pubTypeObj.toString())) {
      String[] pubTypeArr = pubTypeObj.toString().split(",");
      for (int i = 0; i < pubTypeArr.length; i++) {
        String pubType = StringUtils.trimToEmpty(pubTypeArr[i]);
        if (StringUtils.isNotBlank(pubType) && NumberUtils.isNumber(pubType)) {
          type = type + pubType + ",";
        }
      }
      if (StringUtils.isNotBlank(type)) {
        type = type.substring(0, type.length() - 1);
      }
    }
    serviceData.put("pubType", type);
  }



  /**
   * 查找当天的刷新记录 true:已经刷新 false：没有刷新
   * 
   * @param openId
   * @param token
   * @return
   */
  public boolean hasAutoReflush(Long openId, String token) {
    Date date = new Date();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    int year = calendar.get(calendar.YEAR);
    int month = calendar.get(calendar.MONTH) + 1;
    int day = calendar.get(calendar.DAY_OF_MONTH);
    String dateString = "" + year;
    if (month < 10) {
      dateString += "-0" + month;
    } else {
      dateString += "-" + month;
    }
    if (day < 10) {
      dateString += "-0" + day;
    } else {
      dateString += "-" + day;
    }
    List<UnionRefreshPubLog> list =
        unionRefreshPubLogDao.findUnionRefreshPubLogListCurrentDay(openId, token, dateString);
    if (list != null && list.size() > 0) {
      return true;
    }
    return false;
  }

  public String buildResultXml(String result, String getPubStatus) {
    Document doc;
    try {
      doc = DocumentHelper.parseText("<?xml version=\"1.0\" encoding=\"UTF-8\"?><publications></publications>");
      Element rootNode = (Element) doc.selectSingleNode("/publications");
      rootNode.addElement("result").addText(result);
      rootNode.addElement("getPubStatus").addText(getPubStatus);
      return doc.getRootElement().asXML();
    } catch (DocumentException e) {
      logger.error("构造成果XML列表出现异常", e);
    }
    return "";
  }


}

