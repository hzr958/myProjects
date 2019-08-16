package com.smate.center.open.service.data.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.OpenNsfcException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.pubinfo.PubInfoService;
import com.smate.center.open.utils.ConvertObjectUtils;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 已关联账号，根据成果ID返回成果详情 b6494ddb
 * 
 * @author lxz
 *
 */
public class WSSearchPubDetailByConnectedPsnImpl extends ThirdDataTypeBase {

  @Autowired
  private PubInfoService pubInfoService;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    if (paramet.get(OpenConsts.MAP_DATA) == null && StringUtils.isEmpty(paramet.get(OpenConsts.MAP_DATA).toString())) {
      logger.error("已关联账号，根据成果ID返回成果详情数据不能为空,服务码:b6494ddb");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_209, paramet, "已关联账号，根据成果ID返回成果详情数据不能为空,服务码:b6494ddb");
      return temp;
    } else {
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    }
    return temp;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    try {
      String data = paramet.get(OpenConsts.MAP_DATA).toString();
      Map<String, String> dataMap =
          (Map<String, String>) JacksonUtils.jsonObject(data, new TypeReference<Map<String, String>>() {});
      String psnID = paramet.get(OpenConsts.MAP_PSNID).toString();
      String pubIDS = ConvertObjectUtils.objectToString(dataMap.get("pubIDS"));
      String psnGuidID = ConvertObjectUtils.objectToString(dataMap.get("psnGuidID"));
      String result = pubInfoService.searchPubDetailByConnectedPsn(pubIDS, psnGuidID, psnID);
      Map<String, Object> temp = new HashMap<String, Object>();
      temp.put(OpenConsts.RESULT_DATA, result);
      dataList.add(temp);
    } catch (Exception e) {
      e.printStackTrace();
      throw new OpenNsfcException("已关联账号，根据成果ID返回成果详情失败", e);
    }
    return super.successMap("具体成果在线服务调用成功", dataList);
  }

}
