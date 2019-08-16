package com.smate.center.open.service.data.psnInfo;

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
 * 根据人员ID返回该人员公开的成果记录总数 fd92cdfa
 * 
 * @author lxz
 *
 */
public class WSSearchPubsCoutByPsnImpl extends ThirdDataTypeBase {

  @Autowired
  private PubInfoService pubInfoService;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    if (paramet.get(OpenConsts.MAP_DATA) == null && StringUtils.isEmpty(paramet.get(OpenConsts.MAP_DATA).toString())) {
      logger.error("根据人员ID返回该人员公开的成果记录总数失败-数据不能为空,服务码:fd92cdfa");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_209, paramet, "根据人员ID返回该人员公开的成果记录总数失败-数据不能为空,服务码:fd92cdfa");
      return temp;
    } else {
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    }
    return temp;
  }

  @Override
  @SuppressWarnings("unchecked")
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    try {
      // {"psnID":"psnID",keywords":"用户输入的标题","psnGuidID":"业务系统人员GUID","excludedPubIDS":"排除的成果ID","putTypes":"成果类型"}
      String data = paramet.get(OpenConsts.MAP_DATA).toString();
      Map<String, String> dataMap =
          (Map<String, String>) JacksonUtils.jsonObject(data, new TypeReference<Map<String, String>>() {});
      String psnID = paramet.get(OpenConsts.MAP_PSNID).toString();
      String keywords = ConvertObjectUtils.objectToString(dataMap.get("keywords"));
      String excludedPubIDS = ConvertObjectUtils.objectToString(dataMap.get("excludedPubIDS"));
      String psnGuidID = ConvertObjectUtils.objectToString(dataMap.get("psnGuidID"));
      String putTypes = ConvertObjectUtils.objectToString(dataMap.get("putTypes"));

      int count = pubInfoService.searchPubsCountByPsn(psnID, keywords, excludedPubIDS, psnGuidID, putTypes);
      Map<String, Object> temp = new HashMap<String, Object>();
      temp.put(OpenConsts.RESULT_COUNT, count);
      dataList.add(temp);
    } catch (Exception e) {
      e.printStackTrace();
      throw new OpenNsfcException("根据人员ID返回该人员公开的成果记录总数失败", e);
    }
    return super.successMap("获取人员公开的成果记录总数 ", dataList);
  }

}
