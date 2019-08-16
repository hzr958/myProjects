package com.smate.center.open.service.data.psnbaseinfo;

import java.util.Map;

import com.smate.core.base.utils.model.security.Person;

/**
 * 不同系统 需要不通的 服务实现接口
 * 
 * @author tsz
 *
 */
public interface ThirdPsnBaseInfoExtendService {

  /**
   * 构造对应的 map对象
   * 
   * @param infoMap
   * @return
   */
  public Map<String, Object> extendPsnInfo(Person person, Long openId);

}
