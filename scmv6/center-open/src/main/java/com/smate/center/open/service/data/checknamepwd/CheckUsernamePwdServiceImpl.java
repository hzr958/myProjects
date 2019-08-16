package com.smate.center.open.service.data.checknamepwd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.exception.OpenSerGetPsnInfoException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * 验证用户名密码接口
 * 
 * @author aijiangbin
 *
 */

@Transactional(rollbackFor = Exception.class)
public class CheckUsernamePwdServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserDao userDao;

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
    Object username = serviceData.get("username");
    if (username == null || StringUtils.isBlank(username.toString())) {
      logger.error("服务参数 username不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_297, paramet, "服务参数 邮件 username不能为空");
      return temp;
    }
    paramet.put("username", username);
    Object password = serviceData.get("password");
    if (password == null || StringUtils.isBlank(password.toString())) {
      logger.error("服务参数 password不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_298, paramet, "服务参数 邮件 password不能为空");
      return temp;
    }
    paramet.put("password", password);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  /**
   * 
   * 
   * @param psnId
   * @throws OpenException
   */
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      Map<String, Object> temp = new HashMap<String, Object>();
      Map<String, Object> infoMap = new HashMap<String, Object>();
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

      String username = paramet.get("username").toString();
      String password = paramet.get("password").toString();

      User user = userDao.getUser(username, password);
      if (user != null) {
        infoMap.put("match", true);
        infoMap.put("psnId", user.getId());
      } else {
        infoMap.put("match", false);
        infoMap.put("psnId", 0L);
      }

      dataList.add(infoMap);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_000);// 响应成功
      temp.put(OpenConsts.RESULT_DATA, dataList);
      return temp;
    } catch (Exception e) {
      logger.error("验证  username   , password 异常  map=" + paramet.toString());
      throw new OpenSerGetPsnInfoException(e);
    }
  }



}
