package com.smate.sie.center.open.service.psn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.OpenSerGetPsnInfoException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.model.cas.security.User;

/**
 * 根据Id获取用户的账号信息
 * 
 * @author hd
 *
 */
@Transactional(rollbackFor = Exception.class)
public class GetUserByIdServiceImpl extends ThirdDataTypeBase {
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
    Object psnId = serviceData.get(OpenConsts.MAP_PSNID);

    if (psnId == null || !NumberUtils.isDigits(psnId.toString())) {
      logger.error("获取通过psnId不能为空，必须为数字，psnId=" + paramet.get(OpenConsts.MAP_PSNID));
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2002, paramet, "scm-2002 具体服务类型参数  psnId 不能为空，必须为数字");
      return temp;
    }
    paramet.put("psnId", psnId);
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
      User user = userDao.get(psnId);
      buildInfo(paramet, infoMap, user);
      dataList.add(infoMap);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_000);// 响应成功
      temp.put(OpenConsts.RESULT_DATA, dataList);

      return temp;
    } catch (Exception e) {
      logger.error("根据Id获取用户的账号信息 异常  map=" + paramet.toString());
      throw new OpenSerGetPsnInfoException(e);
    }
  }

  /**
   * 构建sie需要的参数
   * 
   * @param paramet
   * @param infoMap
   * @param user
   */
  private void buildInfo(Map<String, Object> paramet, Map<String, Object> infoMap, User user) {
    if (user != null) {
      infoMap.put("psnId", user.getId());
      infoMap.put("email", user.getEmail());
      infoMap.put("loginName", user.getLoginName());
      infoMap.put("isLogin", true);
      infoMap.put("enable", user.getEnabled());
    } else {
      // 放回空
      infoMap.put("psnId", "");
      infoMap.put("email", "");
      infoMap.put("loginName", "");
      infoMap.put("isLogin", false);
      infoMap.put("enable", false);
    }
  }

}
