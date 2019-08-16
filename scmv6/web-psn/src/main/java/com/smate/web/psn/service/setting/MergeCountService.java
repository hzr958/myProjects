package com.smate.web.psn.service.setting;

import java.util.List;

import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.form.MergeCountForm;

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



  /**
   * 获取当前登录帐号的信息.
   * 
   * @return
   */
  MergeCountForm initMainCountInfo(MergeCountForm form) throws Exception;


  /**
   * 获取当前登录用户的正在合并用户记录列表.
   * 
   * @return
   */
  List<MergeCountForm> getMergingList();

  /**
   * 验证用户名和密码是否正确，并返回用户ID.
   * 
   * @param userName
   * @param passWord
   * @return
   */
  Long checkLoginCount(String userName, String passWord);

  /**
   * 进行帐号合并操作，并返回被合并帐号的人员信息.
   * 
   * @param targetPsnId
   * @return
   * @throws ServiceException
   */
  String mergePsnCount(Long targetPsnId) throws Exception;


}
