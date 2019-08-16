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
import com.smate.center.open.service.fund.FundService;
import com.smate.core.base.utils.constant.SolrConstants;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 当solr处理成功时，返回结果为1；处理报错，返回结果为2
 * 
 * 
 */
@Transactional(rollbackFor = Exception.class)
public class ModifySolrFundInfoImpl extends ThirdDataTypeBase {

  @Autowired
  private FundService fundService;

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

    if (parameter.get(SolrConstants.SOLR_FUND_ID) == null) {
      logger.error("solr更新基金信息，服务参数  fun_id 不能为空 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2955, parameter, "SCM_2955  solr更新基金信息,服务参数  fun_id 不能为空");
      return temp;
    }

    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> parameter) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Long fundId = Long.parseLong(String.valueOf(parameter.get(SolrConstants.SOLR_FUND_ID)));
    Map<String, Object> data = new HashMap<String, Object>();
    try {
      this.fundService.updateSolrFundInfo(fundId);
    } catch (Exception e) {
      // 返回结果list，为2时说明solr处理出错
      logger.error("通过open系统solr更新基金信息出错", e);
      data.put(SolrConstants.SOLR_RETURN_STATUS, 2);
      dataList.add(data);
      return successMap("通过open系统solr更新基金信息出错", dataList);
    }
    data.put(SolrConstants.SOLR_RETURN_STATUS, 1);
    dataList.add(data);
    return successMap("通过open系统solr更新基金信息成功", dataList);
  }

}
