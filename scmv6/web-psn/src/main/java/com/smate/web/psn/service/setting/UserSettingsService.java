package com.smate.web.psn.service.setting;

import java.util.List;

import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.form.AttPersonInfo;
import com.smate.web.psn.form.PsnSettingForm;
import com.smate.web.psn.model.setting.SnsPersonSyncMessage;
import com.smate.web.psn.model.setting.UserSettings;

public interface UserSettingsService {
  /**
   * 获取隐私设置配置
   * 
   * @return
   * @throws ServiceException
   */
  UserSettings getPrivacyConfig() throws ServiceException;

  /**
   * 获取隐私常量列表
   * 
   * @return
   * @throws ServiceException
   */
  public List<ConstDictionary> getPrivacyConstList() throws ServiceException;

  /**
   * 获取隐私设置，下拉列表选项
   * 
   * @return
   * @throws ServiceException
   */
  List<ConstDictionary> getConstListByCategroy(String category) throws ServiceException;

  /**
   * 加载个人隐私设置配置表
   * 
   * @return
   * @throws ServiceException
   */
  public String loadPrivacySettings() throws ServiceException;

  /**
   * 保存隐私配置信息
   * 
   * @param privacyConfig
   * @throws ServiceException
   */
  public void savePrivacyConfig(String privacyConfig) throws ServiceException;

  /**
   * 用户设置类型
   * 
   * @return
   * @throws ServiceException
   */
  public UserSettings getAttConfig() throws ServiceException;

  public List<ConstDictionary> getAttConstList() throws ServiceException;

  public List<ConstDictionary> getConstListByCategroy(String category, Integer size) throws ServiceException;

  /**
   * 获取关注人员数量
   * 
   * @param psnId
   * @return
   * @throws ServiceException
   */
  public Long getAllAttPsnCount(Long psnId) throws ServiceException;

  /**
   * 加载关注人员列表信息
   * 
   * @param userSettings
   * @return
   * @throws ServiceException
   */
  public List<AttPersonInfo> loadAttPersonList(UserSettings userSettings) throws ServiceException;

  /**
   * 取消被关注的人员
   * 
   * @param userSettings
   * @throws ServiceException
   */
  public void cancelAttPerson(UserSettings userSettings) throws ServiceException;

  /**
   * // 取消关注后刷新分页信息
   * 
   * @param userSettings
   * @return
   * @throws ServiceException
   */
  public UserSettings refreshUserSettings(UserSettings userSettings) throws ServiceException;

  /**
   * 保存关注人员列表
   * 
   * @param psnIds
   * @return
   * @throws ServiceException
   */
  public long saveAttFrdList(List<Long> psnIds) throws ServiceException;

  void getAttPersonId(PsnSettingForm form) throws Exception;



}
