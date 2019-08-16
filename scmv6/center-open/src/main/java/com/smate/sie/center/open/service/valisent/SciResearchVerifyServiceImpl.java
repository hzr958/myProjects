package com.smate.sie.center.open.service.valisent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;

/**
 * 科研验证受理接口
 * 
 * @author hd
 *
 */
@Transactional(rollbackFor = Exception.class)
public class SciResearchVerifyServiceImpl extends ThirdDataTypeBase {
  @Resource
  private VerifyDataAnalysisService verifyDataAnalysisService;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    paramet.putAll(serviceData);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    // 业务系统的keyCode，返回处理结果时，需要返回给业务系统
    String fromKeyCode = paramet.get("keyCode") == null ? null : paramet.get("keyCode").toString();
    // 校验数据
    Map resultMap = verifyDataAnalysisService.doDataValidate(paramet, fromKeyCode);
    if (resultMap == null) {
      // 主表
      String uuid = verifyDataAnalysisService.saveMain(paramet);
      // 拆分数据到待处理表
      resultMap = verifyDataAnalysisService.doDataAnalysis(paramet, uuid, fromKeyCode);
    }
    dataList.add(resultMap);
    return successMap(OpenMsgCodeConsts.SCM_000, dataList);
  }

}
