package com.smate.web.v8pub.service.repeatpub;

import com.smate.web.v8pub.vo.RepeatPubVO;

/**
 * 重复成果 - 显示-接口
 * 
 * @author zzx
 *
 */
public interface RepeatPubShowService {
  /**
   * 重复记录显示title
   * 
   * @param repeatPubVO
   * @throws Exception
   */
  void repeatPubMainTitle(RepeatPubVO repeatPubVO) throws Exception;

  /**
   * 显示重复记录的成果信息
   * 
   * @param form
   * @throws Exception
   */
  void showRepeatPubPage(RepeatPubVO repeatPubVO) throws Exception;

}
