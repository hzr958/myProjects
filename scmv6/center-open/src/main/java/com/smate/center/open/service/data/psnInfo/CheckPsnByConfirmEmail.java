package com.smate.center.open.service.data.psnInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.OpenNsfcException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.profile.PersonManager;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;

@Transactional(rollbackFor = Exception.class)
public class CheckPsnByConfirmEmail extends ThirdDataTypeBase {

  @Autowired
  private PersonManager personManager;
  private Logger logger = LoggerFactory.getLogger(getClass());

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> msgMap = new HashMap<String, Object>();
    String datas = null;
    datas = paramet.get(OpenConsts.MAP_DATA).toString();
    Map<String, String> dataMap = new HashMap<String, String>();
    dataMap = (Map<String, String>) JacksonUtils.jsonObject(datas, new TypeReference<Map<String, String>>() {});
    if (StringUtils.isBlank(paramet.get("psnId").toString())
        || StringUtils.isBlank(dataMap.get("psnEmail").toString())) {
      logger.debug("人员id和邮箱地址不能为空");
      msgMap = super.errorMap(OpenMsgCodeConsts.SCM_223, paramet,
          "检查业务系统人员已验证的邮件是否和科研之友人员邮件相同操作失败-人员id或者邮箱地址为空,服务码:a5392039");
      return msgMap;
    }
    paramet.put("psnEmail", dataMap.get("psnEmail").toString());
    msgMap.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return msgMap;

  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    int flag = 0;
    Map<String, Object> temp = new HashMap<String, Object>();
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
    try {
      Person person = personManager.getPersonById(Long.valueOf(paramet.get("psnId").toString()));
      if (person != null && StringUtils.equals(paramet.get("psnEmail").toString(), person.getEmail())) {
        flag = 1;
        temp.put(OpenConsts.RESULT_FLAG, flag);
        dataList.add(temp);
      } else {
        temp.put(OpenConsts.RESULT_FLAG, flag);
        dataList.add(temp);
      }
    } catch (Exception e) {
      logger.error(String.format("判断IRIS业务系统用户邮箱psnEamil=%s是否时SNS用户psnId=%s首要邮箱出现异常：",
          paramet.get("psnEamil").toString(), paramet.get("psnId").toString()), e);
      throw new OpenNsfcException("判断IRIS业务系统用户邮箱psnEamil=%s是否时SNS用户psnId=%s首要邮箱出现异常：" + paramet.get("psnId").toString()
          + paramet.get("psnEamil").toString(), e);
    }
    return super.successMap("已经检查业务系统人员已验证的邮件是否和科研之友人员邮件相同 ", dataList);
  }

}
