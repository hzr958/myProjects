package com.smate.center.open.service.data.checkemail;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.exception.OpenSerGetPsnInfoException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.login.ThirdLoginService;
import com.smate.core.base.utils.dao.security.UserDao;
import com.smate.core.base.utils.model.cas.security.User;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;

/**
 * 验证邮件是否在科研之友 有账号 如果有账号 返回对应的openid 如果对应的openid与 对应的token没有关联记录 就创建一个关联记录
 * 
 * @author tsz
 *
 */

@Transactional(rollbackFor = Exception.class)
public class CheckEmailServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private UserDao userDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private ThirdLoginService thirdLoginService;

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
    Object fromSys = serviceData.get(OpenConsts.MAP_DATA_FROM_SYS);
    if (email != null) {
      paramet.put(OpenConsts.MAP_DATA_FROM_SYS, fromSys.toString());
    }
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

      String email = paramet.get("email").toString();

      User user = userDao.getUserByUsername(email);
      Object fromSys = paramet.get(OpenConsts.MAP_DATA_FROM_SYS);
      if (fromSys != null && fromSys.toString().equalsIgnoreCase(OpenConsts.FROM_SIE)) {
        buildSIEInfo(paramet, infoMap, user);
      } else {
        buildBaseInfo(paramet, infoMap, user);
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

  private void buildBaseInfo(Map<String, Object> paramet, Map<String, Object> infoMap, User user) {
    if (user != null) {
      // 取openid
      Long openId = openUserUnionDao.getOpenIdByPsnId(user.getId());
      if (openId != null) {
        // 如果不等于空 判断有没有关联记录 没有就创建关联记录
        createOpenUnion(paramet.get("token").toString(), user.getId(), openId);
      } else {
        // 没有openid产生openid
        openId =
            thirdLoginService.getOpenId(paramet.get("token").toString(), user.getId(), OpenConsts.OPENID_CREATE_TYPE_2);
      }
      // 放回空
      infoMap.put("openid", openId);
    } else {
      // 放回空
      infoMap.put("openid", "");
    }
  }

  /**
   * 构建sie需要的参数
   * 
   * @param paramet
   * @param infoMap
   * @param user
   */
  private void buildSIEInfo(Map<String, Object> paramet, Map<String, Object> infoMap, User user) {
    if (user != null) {
      infoMap.put("psnid", user.getId());
      infoMap.put("email", user.getEmail());
      infoMap.put("loginName", user.getLoginName());
      infoMap.put("isLogin", true);
      infoMap.put("enable", user.getEnabled());
    } else {
      // 放回空
      infoMap.put("psnid", "");
      infoMap.put("email", "");
      infoMap.put("loginName", "");
      infoMap.put("isLogin", false);
      infoMap.put("enable", false);
    }
  }

  private void createOpenUnion(String tempToken, Long psnId, Long openId) {
    OpenUserUnion temp = openUserUnionDao.getOpenUserUnion(openId, tempToken);
    if (temp == null) {
      temp = new OpenUserUnion();
      temp.setCreateDate(new Date());
      temp.setOpenId(openId);
      temp.setCreateType(OpenConsts.OPENID_CREATE_TYPE_1);
      temp.setToken(tempToken);
      temp.setPsnId(psnId);
      openUserUnionDao.save(temp);
    }
  }

}
