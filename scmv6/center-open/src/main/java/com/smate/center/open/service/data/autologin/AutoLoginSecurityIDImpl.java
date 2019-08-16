package com.smate.center.open.service.data.autologin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.exception.OpenSerGetAutoLoginIdException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.constant.security.SecurityConstants;
import com.smate.core.base.utils.dao.security.AutoLoginOauthInfoDao;
import com.smate.core.base.utils.model.cas.security.AutoLoginOauthInfo;

/**
 * 自动登录 权限加密串 生成服务，（用于 登录页面记住登录，邮件自动登录，第3方系统 自动登录用 ...）
 * 
 * @author tsz
 *
 */

@Transactional(rollbackFor = Exception.class)
public class AutoLoginSecurityIDImpl extends ThirdDataTypeBase {

  @Autowired
  private AutoLoginOauthInfoDao autoLoginOauthInfoDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    // 校验服务参数 serviceType
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }

    Object autoLoginType = serviceData.get("AutoLoginType");
    if (autoLoginType == null) {
      logger.error("服务参数 自动登陆类型不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_249, paramet, "服务参数 自动登陆类型不能为空");
      return temp;
    }
    Long autoLoginTime = AutoLoginTypeEnum.valueOf(autoLoginType.toString()).toLong();
    if (autoLoginTime == null) {
      logger.error("服务参数 自动登陆类型不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_250, paramet, "服务参数 自动登陆类型不正确");
      return temp;
    }
    paramet.put("AutoLoginType", autoLoginType);
    paramet.put("OverTime", autoLoginTime);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  /**
   * 根据一定的规则 生成加密 ID 并放入数据库
   * 
   * @param psnId
   * @throws OpenException
   */
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      // 获取 加密id 把加密id当做 cache的key 放入缓存中 注意保证唯一性（统一使用 唯一编码做md5加密
      // (openId+时间挫)
      String oauthid = DigestUtils.md5Hex(UUID.randomUUID().toString() + paramet.get(OpenConsts.MAP_OPENID));
      // 缓存 过期时间可以通过 参数的方式 来区分 不同的需求 不一样的过期时间，比如 邮件的自动登录，记住登录的自动登录
      // 第3方系统 跳转的自动登录
      // 不保存缓存 （如果重启缓存服务器的话 所有缓存都会失效） 存数据库
      Date tempDate = new Date();
      AutoLoginOauthInfo autoLogin = new AutoLoginOauthInfo();
      autoLogin.setSecurityId(oauthid);
      autoLogin.setCreateTime(tempDate);
      // 获取过期时间
      autoLogin.setLoginType(paramet.get("AutoLoginType").toString());
      autoLogin.setOverdueTime(getDate(tempDate, Long.valueOf(paramet.get("OverTime").toString())));
      autoLogin.setPsnId(Long.valueOf(paramet.get(OpenConsts.MAP_PSNID).toString()));
      autoLogin.setUseTimes(0);
      autoLogin.setToken(paramet.get(OpenConsts.MAP_TOKEN).toString());
      autoLoginOauthInfoDao.save(autoLogin);

      Map<String, Object> temp = new HashMap<String, Object>();
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
      Map<String, Object> dataMap = new HashMap<String, Object>();
      dataMap.put(SecurityConstants.AUTO_LOGIN_PARAMETER_NAME, oauthid);
      dataList.add(dataMap);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_000);// 响应成功
      temp.put(OpenConsts.RESULT_DATA, dataList);
      return temp;
    } catch (Exception e) {
      logger.error("获取自动登录加密串 异常 map=" + paramet.toString(), e);
      throw new OpenSerGetAutoLoginIdException(e);
    }
  }

  /**
   * 计算时间上的差额
   * 
   * @param date
   * @param saveTime 时间差额(s)
   * @return
   */
  private Date getDate(Date date, Long saveTime) {
    // SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return new Date(date.getTime() + saveTime);
  }

  public static void main(String[] args) {
    String oauthid = UUID.randomUUID().toString();

    System.out.println(DigestUtils.md5Hex(oauthid + "12345678"));
    Date tempDate = new Date();
    System.out.println("今天的日期：" + tempDate);
    System.out.println("两天前的日期：" + new Date(tempDate.getTime() - 2 * 24 * 60 * 60 * 1000));
    System.out.println("三天后的日期：" + new Date(tempDate.getTime() + 3 * 24 * 60 * 60 * 1000));

    System.out.println(tempDate);
    System.out.println(new Date(tempDate.getTime() + 2 * 60 * 1000));

  }

}
