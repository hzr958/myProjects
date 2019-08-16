package com.smate.center.open.service.interconnection.pub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.V8pubQueryPubConst;
import com.smate.center.open.service.interconnection.ScmUnionDataHandleService;
import com.smate.core.base.pub.dto.PubQueryDTO;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 获取互联成果 通过 pubId
 * 
 * @author AiJiangBin
 *
 */
public class GetPubInfoByPubIdServiceImpl extends AbstractCommonPubInfoService implements ScmUnionDataHandleService {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * 
   * 
   * @param dataParamet
   * @return
   */
  public String checkParam(Map<String, Object> dataParamet) {

    Object data_template_key = dataParamet.get("data_template_key");
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



  @Override
  public String handleUnionData(Map<String, Object> dataParamet) throws Exception {
    Object data = dataParamet.get(OpenConsts.MAP_DATA);
    Map<String, Object> serviceData = JacksonUtils.jsonToMap(data.toString());
    String temp = checkParam(serviceData);
    if (temp != null) {
      return buildResultXml(temp, "4");
    }
    Long openId = NumberUtils.toLong(dataParamet.get("openid").toString());
    String pubTemplate = pubTemplateMap.get(serviceData.get("data_template_key").toString());
    List<Long> pubIdList = handleParmaToPubIds(serviceData);


    String SERVER_URL = domainscm + V8pubQueryPubConst.QUERY_PUB_URL;
    PubQueryDTO pubQueryDTO = new PubQueryDTO();
    pubQueryDTO.setSearchPubIdList(pubIdList);
    pubQueryDTO.setServiceType(V8pubQueryPubConst.PUB_LIST_QUERY_BY_PUB_IDS);
    Map<String, Object> result = (Map<String, Object>) getRemotePubInfo(pubQueryDTO, SERVER_URL);
    List<Map<String, Object>> resultList = null;
    if (result.get("status").equals("success")) {
      resultList = (List<Map<String, Object>>) result.get("resultList");
    }
    if (resultList != null && resultList.size() > 0) {
      try {
        HashMap<String, Object> dataMap = new HashMap<String, Object>();
        dataMap.put("openId", openId);
        dataMap.put("pubTemplate", pubTemplate);
        dataMap.put("count", pubIdList.size());
        return buildUnionPubListXmlStr(resultList, dataMap, dataParamet, false);
      } catch (Exception e) {
        // 吃掉异常，当做open系统的正确请求来处理
        logger.error(e.getMessage() + JacksonUtils.mapToJsonStr(dataParamet), e);
        return buildResultXml("构建成果 列表 出现异常", "5");
      }
    }
    return buildResultXml("没有查询到成果!", "3");
  }


  /**
   * 解析参数中的成果id
   * 
   * @return
   */
  public List<Long> handleParmaToPubIds(Map<String, Object> serviceData) {

    List<Long> pubIdList = new ArrayList<Long>();
    if (serviceData != null && serviceData.get("pubIdList") != null) {
      String pubIds = serviceData.get("pubIdList").toString();
      if (StringUtils.isNotBlank(pubIds)) {
        String[] pubIdArr = pubIds.split(",");
        for (int i = 0; i < pubIdArr.length; i++) {
          String pubId = StringUtils.trim(pubIdArr[i]);
          if (StringUtils.isNotBlank(pubId) && NumberUtils.isNumber(pubId)) {
            pubIdList.add(NumberUtils.toLong(pubId));
          }
        }
      }
    }
    return pubIdList;
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

