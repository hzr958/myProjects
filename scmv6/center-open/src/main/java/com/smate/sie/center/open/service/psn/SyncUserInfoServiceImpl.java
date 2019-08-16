package com.smate.sie.center.open.service.psn;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.OpenSerGetPsnInfoException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.security.SysUserLoginDao;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.model.cas.security.SysUserLogin;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * 更新账号的的密码和邮箱
 * 
 * @author hd
 *
 */
@Transactional(rollbackFor = Exception.class)
public class SyncUserInfoServiceImpl extends ThirdDataTypeBase {
  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserDao userDao;
  @Autowired
  private SysUserLoginDao sysUserLoginDao;

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
    if (email == null) {
      logger.error("服务参数 邮件不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_285, paramet, "服务参数 邮件 email不能为空");
      return temp;
    }
    paramet.put("email", email);
    Object psnId = serviceData.get(OpenConsts.MAP_PSNID);

    if (psnId == null || !NumberUtils.isDigits(psnId.toString())) {
      logger.error("获取通过psnId不能为空，必须为数字，psnId=" + paramet.get(OpenConsts.MAP_PSNID));
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2002, paramet, "scm-2002 具体服务类型参数  psnId 不能为空，必须为数字");
      return temp;
    }
    paramet.put("psnId", psnId);

    Object password = serviceData.get("password");
    if (password == null || StringUtils.isBlank(password.toString())) {
      logger.error("服务参数 password不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_298, paramet, "服务参数 邮件 password不能为空");
      return temp;
    }
    paramet.put("password", password);
    Object changed = serviceData.get("lastpwdchanged");
    paramet.put("changed", changed);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      Map<String, Object> temp = new HashMap<String, Object>();
      Map<String, Object> infoMap = new HashMap<String, Object>();
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
      Long psnId = Long.parseLong(paramet.get(OpenConsts.MAP_PSNID).toString());
      String email = paramet.get("email").toString();
      String password = paramet.get("password").toString();
      Object changed = paramet.get("changed");
      User user = userDao.get(psnId);
      if (user != null) {
        if (!StringUtils.isBlank(email)) {
          user.setEmail(email);
        }
        user.setPassword(password);
        userDao.save(user);
      }
      if (changed != null && Boolean.parseBoolean(changed.toString())) {
        SysUserLogin sys = sysUserLoginDao.get(psnId);
        if (sys != null) {
          sys.setLastPwdChanged(new Date());
          sysUserLoginDao.save(sys);
        }
      }
      dataList.add(infoMap);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_000);// 响应成功
      temp.put(OpenConsts.RESULT_DATA, dataList);

      return temp;
    } catch (Exception e) {
      logger.error("同步人员账号的email、password 异常  map=" + paramet.toString());
      throw new OpenSerGetPsnInfoException(e);
    }
  }


}
