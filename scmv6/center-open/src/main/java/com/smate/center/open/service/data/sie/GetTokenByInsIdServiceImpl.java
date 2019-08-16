package com.smate.center.open.service.data.sie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.data.OpenTokenServiceConstDao;
import com.smate.center.open.model.OpenTokenServiceConst;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 通过insId获取token
 * 
 * @author aijiangbin
 * @date 2018年7月10日
 */

@Transactional(rollbackFor = Exception.class)
public class GetTokenByInsIdServiceImpl extends ThirdDataTypeBase {


  @Autowired
  private OpenTokenServiceConstDao openTokenServiceConstDao;


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
    Object insId = paramet.get(OpenConsts.MAP_INS_ID);
    if (insId == null || StringUtils.isBlank(insId.toString()) || !NumberUtils.isNumber(insId.toString())) {
      logger.error("scm-2015 具体服务类型参数insId 有误：insId=" + paramet.get(OpenConsts.MAP_INS_ID));
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2015, paramet, "scm-2015 具体服务类型参数insId不能为空，必须为数字");
      return temp;
    }



    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    Map<String, Object> data = new HashMap<String, Object>();
    Long insId = NumberUtils.toLong(paramet.get(OpenConsts.MAP_INS_ID).toString());
    OpenTokenServiceConst serviceConst = openTokenServiceConstDao.findObjBytokenAndInsId(insId);
    String token = "";
    if (serviceConst != null) {
      token = serviceConst.getToken();
    }
    data.put("token", token == null ? "" : token);
    dataList.add(data);
    return successMap("获取token信息成功", dataList);
  }

}
