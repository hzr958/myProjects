package com.smate.center.open.service.data.openid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.open.PersonOpenService;
import com.smate.center.open.service.profile.PersonManager;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 获取openid接口服务实现
 * 
 * @author tsz
 *
 */
public class GetOpenIdServiceImpl extends ThirdDataTypeBase {

  @Autowired
  private PersonManager personManager;
  @Autowired
  private PersonOpenService personOpenService;

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

    // 校验psn_id是否为空
    if (paramet.get(OpenConsts.MAP_PSNID) == null) {
      logger.error("获取openId参数 psnId为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_410, paramet, "psnId不能为空");
      return temp;
    }
    // 校验psn_id是否正确
    if (!NumberUtils.isNumber(paramet.get(OpenConsts.MAP_PSNID).toString())) {
      logger.error("获取 openId 参数 psnId不存在 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_411, paramet, "psnId不存在");
      return temp;
    }

    try {
      if (!personManager.checkPsnId(Long.parseLong(paramet.get(OpenConsts.MAP_PSNID).toString()))) {
        logger.error("获取 openId 参数 psnId不存在 ");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_411, paramet, "psnId不存在");
        return temp;
      }
    } catch (Exception e) {
      logger.error("获取 openId 参数 psnId不存在 ");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_411, paramet, "psnId不存在");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      Map<String, Object> temp = new HashMap<String, Object>();
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
      Long psnId = Long.parseLong(paramet.get(OpenConsts.MAP_PSNID).toString());
      Long openId = personOpenService.getOpenIdByPsnId(psnId);
      temp.put("openId", openId);
      dataList.add(temp);
      return super.successMap("获取openId成功", dataList);
    } catch (Exception e) {
      logger.error("获取openId异常 map=" + paramet.toString() + e.toString(), e);
      throw new RuntimeException(e);
    }
  }

}
