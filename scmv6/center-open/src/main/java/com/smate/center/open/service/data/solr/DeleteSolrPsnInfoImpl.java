package com.smate.center.open.service.data.solr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.user.UserService;
import com.smate.core.base.utils.constant.SolrConstants;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 防止修改不全，更新人员solr信息统一在center-batch处理
 * 
 * 
 */
@Transactional(rollbackFor = Exception.class)
@Deprecated
public class DeleteSolrPsnInfoImpl extends ThirdDataTypeBase {

  @Autowired
  private UserService userService;

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

    if (parameter.get(SolrConstants.SOLR_PSN_ID) == null) {
      logger.error("solr更新人员信息，服务参数  psn_id 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_295, parameter, "SCM_295  solr删除人员信息,服务参数  psn_id 不能为空");
      return temp;
    }

    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> parameter) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Long psnId = Long.parseLong(String.valueOf(parameter.get(SolrConstants.SOLR_PSN_ID)));
    Map<String, Object> data = new HashMap<String, Object>();
    try {
      this.userService.deleteSolrPsnInfo(psnId);
    } catch (Exception e) {
      // 返回结果list，为2时说明solr处理出错
      logger.error("通过open系统solr删除人员信息出错", e);
      data.put(SolrConstants.SOLR_RETURN_STATUS, 2);
      dataList.add(data);
      return successMap("通过open系统solr删除人员信息出错", dataList);
    }
    data.put(SolrConstants.SOLR_RETURN_STATUS, 1);
    dataList.add(data);
    return successMap("通过open系统solr删除人员信息成功", dataList);
  }

}
