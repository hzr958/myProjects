package com.smate.center.open.service.resume;

import java.io.Serializable;

/**
 * 设置公开信息接口.
 * 
 * @author liqinghua
 * 
 */
/**
 * @author lichangwen
 * 
 */
@Deprecated
public interface ResumeService extends Serializable {

  /**
   * 获取成果权限,默认公开.
   * 
   * @param pubId
   * @return
   */
  String getPubAuthority(Long pubId) throws Exception;

}
