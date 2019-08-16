package com.smate.web.management.service.psn;

import java.util.List;

/**
 * 合并账户设置的业务逻辑接口.
 * 
 * @author mjg
 * 
 */
public interface MergeCountService {

  // 合并用户状态：1-保留；99-删除 .
  static final Integer MERGE_USER_STATUS_SAVED = 1;
  static final Integer MERGE_USER_STATUS_DEL = 99;
  // 帐号合并结果-帐号不存在.
  public static final Long CHECK_RESULT_LOGIN_ERROR = -1L;
  // 帐号合并结果-密码错误.
  public static final Long CHECK_RESULT_PWD_ERROR = -2L;
  // 人员信息对象的key值.
  public static final String PSN_KEY_DESID = "des3PsnId";
  public static final String PSN_KEY_AVATARS = "avatars";
  public static final String PSN_KEY_VIEWNAME = "psnViewName";
  public static final String PSN_KEY_LOGIN_COUNT = "loginCount";
  public static final String PSN_KEY_PSNTITLE = "psnTitle";

  Integer getMergeStatus(Long mergePsnId, Long deletePsnId) throws Exception;

  String mergePsnCount(Long mergePsnId, List<Long> userIds, String mergeCount) throws Exception;

}
