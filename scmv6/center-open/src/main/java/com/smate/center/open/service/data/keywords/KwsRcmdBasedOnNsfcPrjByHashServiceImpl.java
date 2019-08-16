package com.smate.center.open.service.data.keywords;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.keyword.NsfcKeywordsService;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 基于nsfc关键词的相关关键词推荐
 * 
 * @author
 * @date 20181012
 */
@Transactional(rollbackFor = Exception.class)
public class KwsRcmdBasedOnNsfcPrjByHashServiceImpl extends ThirdDataTypeBase {


  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private NsfcKeywordsService nsfcKeywordsService;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> parameter) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = parameter.get(OpenConsts.MAP_DATA);
    if (data != null && data.toString().length() > 0) {
      Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
      if (dataMap != null) {
        parameter.putAll(dataMap);
      }
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> parameter) {
    List<String> dataList = new ArrayList<String>();
    String kwStr = (String) parameter.get("kwStr");
    String discode = (String) parameter.get("discode");
    // 1查询时同时查询此类别下的所有信息，包含其子类别，比如A01会查询A01XX, A01XXXX的学科
    String queryType = (String) parameter.get("queryType");
    if (StringUtils.isEmpty(kwStr)) {
      return successMap("接收到的关键词为空", dataList);
    }

    String msg = "";
    try {
      if (StringUtils.isEmpty(discode)) {
        dataList = this.nsfcKeywordsService.getRelatedKwByHash(kwStr, null, null);
        msg = "无nsfc学科代码约束，推荐关键词成功";
      } else {
        dataList = this.nsfcKeywordsService.getRelatedKwByHash(kwStr, discode, queryType);
        msg = "nsfc学科代码为" + discode + "，推荐关键词成功";
      }
    } catch (Exception e) {
      logger.error("nsfc关键词推荐出错", e);
      return errorMap("nsfc关键词推荐出错", parameter, "通过nsfc关键词hash推荐出错");
    }
    return successMap(msg, dataList);
  }


}
