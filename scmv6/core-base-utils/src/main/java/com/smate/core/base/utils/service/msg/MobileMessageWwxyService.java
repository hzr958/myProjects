package com.smate.core.base.utils.service.msg;

import com.smate.core.base.utils.model.msg.MessageLog;

public interface MobileMessageWwxyService {
  public static String CACHE_NAME = "register_mobile_number";
  public static String CACHE_NAME_LOGIN = "login_mobile_number";
  public static int EXPIRE_DATE = 3 * 60; // 3分钟
  public static Integer SUCCESS = 1; // 成功状态
  public static Integer FAIL = 2; // 失败状态
  public static Long REG_TYPE = 1000L; // 注册类型
  public static Long LOGIN_TYPE = 2000L; // 登录类型类型



  /**
   * 初始化发送消息
   * 
   * @param message
   * @return
   */
  public String initSendMessage(MobileMessageForm message);

  /**
   * 当天是否发送了，验证码
   * 
   * @param mobile
   * @return
   */
  public Boolean isSendMessageTheDay(String mobile, Long type);

  public MessageLog findLastestLogByTime(Long psnId, Long type);

  /**
   * 判断地区 中国大陆 就验证手机号 非中国大陆就验证邮箱
   * 
   * @return
   */
  public String ipCheck();

  public Boolean checkCodeValide(String mobile ,String code) ;
}
