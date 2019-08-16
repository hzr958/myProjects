package com.smate.web.mobile.share.service;

import com.smate.web.mobile.share.vo.SmateShareVO;

/**
 * 群组动态分享处理
 * 
 * @author wsn
 * @date Jun 19, 2019
 */
public interface GrpDynShareService {

  /**
   * 更新群组动态分享统计数
   */
  void updateGrpDynShareStatistics(SmateShareVO vo);
}
