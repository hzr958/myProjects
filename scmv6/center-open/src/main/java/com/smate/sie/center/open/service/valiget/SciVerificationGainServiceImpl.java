package com.smate.sie.center.open.service.valiget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;

/**
 * 科研验证获取接口
 * 
 * @author ztg
 *
 */
@Transactional(rollbackFor = Exception.class)
public class SciVerificationGainServiceImpl extends ThirdDataTypeBase {

  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private KpiVerifyResultService kpiVerifyResultServiceImpl;

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

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    // 校验请求数据
    Map resultMap = kpiVerifyResultServiceImpl.doDataValidate(paramet);

    if (resultMap == null) {// uuid不为空，而且有效
      resultMap = kpiVerifyResultServiceImpl.constituteContent(paramet);
    }
    dataList.add(resultMap);
    return successMap(OpenMsgCodeConsts.SCM_000, dataList);
  }


}
