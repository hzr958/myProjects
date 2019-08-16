package com.smate.center.batch.service.user;

import java.util.List;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.sns.pub.ConstDictionary;
import com.smate.center.batch.model.sns.pub.PrivacySettings;
import com.smate.core.base.utils.model.security.Person;

public interface UserSettingsService {
  /**
   * 读取用户隐私权限配置
   * 
   * @param psnId
   * @param privacyAction
   * @return
   * @throws ServiceException
   */
  public PrivacySettings loadPsByPsnId(Long psnId, String privacyAction) throws ServiceException;

  /**
   * 出初始化用户隐私设置
   * 
   * @param psnId
   */
  public void initPrivacySettingsConfig(Long psnId);

  public List<ConstDictionary> getConstListByCategroy(String category, Integer size) throws Exception;

  public List<ConstDictionary> getAttConstList() throws Exception;

  /**
   * 初始化 用户关注 类型设置
   * 
   * @param psnId
   * @throws Exception
   */
  public void initAttTypeConfig(Long psnId) throws Exception;

  /**
   * 关注设置同步人员数据
   * 
   * @param msg
   * @throws ServiceException
   */
  void syncPersonInfo(Person person) throws ServiceException;
}
