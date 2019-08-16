package com.smate.center.open.service.data.checkemail;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.exception.OpenSerGetPsnInfoException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.security.Des3Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 检查业务系统人员已验证的邮件是否和科研之友人员邮件相同
 * 
 * @author aijiangbin
 * @date 2018年8月24日
 */

@Transactional(rollbackFor = Exception.class)
public class CheckPsnIdByEmailServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PersonDao personDao;


  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    // 没有 参数校验 直接返回成功
    Map<String, Object> temp = new HashMap<String, Object>();

    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    Object email = serviceData.get("email");
    if (email == null || StringUtils.isBlank(email.toString())) {
      logger.error("服务参数 邮件不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_285, paramet, "服务参数 邮件 email不能为空");
      return temp;
    }
    Object psnId = serviceData.get("psnId");
    psnId = psnId != null ? Des3Utils.decodeFromDes3(psnId.toString()) : null;
    serviceData.put("psnId", psnId);
    if (psnId == null || !NumberUtils.isCreatable(psnId.toString())) {
      logger.error("服务参数 邮件不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2002, paramet, "服务参数 邮件 psnId不能为空，且为数字");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    paramet.putAll(serviceData);
    return temp;
  }

  /**
   * 
   * 
   * @throws OpenException
   */
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      Map<String, Object> temp = new HashMap<String, Object>();
      Map<String, Object> infoMap = new HashMap<String, Object>();
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

      String email = paramet.get("email").toString();
      Long psnId = NumberUtils.toLong(paramet.get("psnId").toString());

      Person person = personDao.get(psnId);
      if (person != null && StringUtils.equals(email.trim().toLowerCase(), person.getEmail().toLowerCase())) {
        infoMap.put("flag", 1);
      } else {
        infoMap.put("flag", 0);
      }
      dataList.add(infoMap);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_000);// 响应成功
      temp.put(OpenConsts.RESULT_DATA, dataList);
      return temp;
    } catch (Exception e) {
      logger.error("获取人员基本信息服务异常 map=" + paramet.toString());
      throw new OpenSerGetPsnInfoException(e);
    }
  }

}
