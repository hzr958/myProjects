package com.smate.sie.center.open.service.valiget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.string.IrisNumberUtils;
import com.smate.sie.center.open.service.valisent.KpiPaymentValidateService;

/**
 * 判断是否有权限使用科研验证服务
 * 
 * @author sjzhou xr
 *
 */

@Transactional(rollbackFor = Exception.class)
public class KpiPaymentVarificationServiceImpl extends ThirdDataTypeBase {
  @Autowired
  private KpiPaymentValidateService kpiPaymentValidateService;
  @Autowired
  private PersonDao personDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data != null && data.toString().length() > 0) {
      Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
      if (dataMap != null) {
        paramet.putAll(dataMap);
      }
    }
    if (paramet.get("psnId") == null) {
      return BuildUniformErrorReturn(temp, paramet);
    }
    if (StringUtils.isBlank(paramet.get("psnId").toString())) {
      return BuildUniformErrorReturn(temp, paramet);
    }
    // 校验psnId对应人员是否存在
    Long psnId = NumberUtils.parseLong(paramet.get("psnId").toString());
    Long personId = personDao.existsPerson(psnId);
    if (NumberUtils.isNullOrZero(personId)) {
      return BuildUniformErrorReturn(temp, paramet);
    }
    if (paramet.get("appType") == null) {
      return BuildUniformErrorReturn(temp, paramet);
    } else if (StringUtils.isBlank(paramet.get("appType").toString())) {
      return BuildUniformErrorReturn(temp, paramet);
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  /**
   * 构造统一错误MSG返回码
   * 
   * @param temp
   * @return
   */
  private Map<String, Object> BuildUniformErrorReturn(Map<String, Object> temp, Map<String, Object> parame) {
    temp = super.errorMap(OpenMsgCodeConsts.SCM_999, parame, "");
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Map<String, Object> data = null;
    Long psnId = IrisNumberUtils.createLong(paramet.get("psnId").toString());
    String ip = "";
    if (paramet.get("ip") != null) {
      ip = paramet.get("ip").toString();
    }
    String appType = paramet.get("appType").toString();
    try {
      // 1 开题分析, 2科研验证,3项目助理
      if ("1".equals(appType)) {
        data = kpiPaymentValidateService.paymentAnalysisVarification(psnId, ip);
      } else if ("2".equals(appType)) {
        data = kpiPaymentValidateService.paymentValidateVarification(psnId, ip);
      } else if ("3".equals(appType)) {
        data = kpiPaymentValidateService.paymentProjectVarification(psnId, ip);
      }
      if (MapUtils.isEmpty(data)) {
        data.put("status", "0");
        data.put("endTime", "");
      }
      dataList.add(data);
      return successMap(OpenMsgCodeConsts.SCM_000, dataList);
    } catch (Exception e) {
      data = new HashMap<String, Object>();
      data.put("status", "0");
      data.put("endTime", "");
      logger.error("判断当前用户是否为付费单位人员报错", e);
    }
    data.put("status", "0");
    data.put("endTime", "");
    dataList.add(data);
    return successMap(OpenMsgCodeConsts.SCM_000, dataList);
  }

}
