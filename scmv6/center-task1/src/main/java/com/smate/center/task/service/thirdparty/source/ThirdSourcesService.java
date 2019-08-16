package com.smate.center.task.service.thirdparty.source;

import java.util.List;

import com.smate.center.task.model.thirdparty.ThirdSources;

/**
 * 第三方 信息来源记录服务
 * 
 * @author tsz
 *
 */
public interface ThirdSourcesService {

  /**
   * 获取第三方 信息来源记录.包括来源类型
   * 
   * @return
   * @throws Exception
   */
  public List<ThirdSources> getSourcesList() throws Exception;

}
