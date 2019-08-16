package com.smate.center.oauth.service.version;

import com.smate.center.oauth.model.mobile.version.AppVersionVo;
import com.smate.center.oauth.model.mobile.version.VersionInfoBean;

/**
 * 移动端app版本信息接口
 * 
 * @author wsn
 * @date Jan 3, 2019
 */
public interface MobileAppVersionService {

  /**
   * 查找最新版本的app信息
   * 
   * @param appType
   * @return
   */
  VersionInfoBean findLastAppVersionInfo(String appType);


  /**
   * 查找某种app所有的版本信息.
   * 
   * @param vo
   * @return
   */
  AppVersionVo findAllAppVersionInfo(AppVersionVo vo);

  /**
   * 编辑版本信息.
   * 
   * @param vo
   * @return
   */
  String updateAppVersionInfo(AppVersionVo vo);

  /**
   * 根据ID查找版本信息.
   * 
   * @param appType
   * @param id
   * @return
   */
  VersionInfoBean findAppVersionInfoById(String appType, Long id);
}
