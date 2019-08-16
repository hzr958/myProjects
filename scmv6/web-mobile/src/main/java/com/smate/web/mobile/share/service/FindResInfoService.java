package com.smate.web.mobile.share.service;

import com.smate.web.mobile.share.vo.SmateShareVO;

/**
 * 资源信息接口
 * 
 * @author wsn
 * @date May 31, 2019
 */
public interface FindResInfoService {

  /**
   * 查找各种资源的信息
   * 
   * @param vo
   */
  void findResInfo(SmateShareVO vo);
}
