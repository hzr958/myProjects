package com.smate.web.v8pub.service.repeatpub;

import com.smate.web.v8pub.vo.RepeatPubVO;

/**
 * 重复成果 - 操作-接口
 * 
 * @author zzx
 *
 */
public interface RepeatPubOptService {
  /**
   * 删除重复记录
   * 
   * @param form
   * @throws Exception
   */
  void repeatPubDel(RepeatPubVO repeatPubVO) throws Exception;

  /**
   * 保留重复记录成果
   * 
   * @param form
   * @throws Exception
   */
  void repeatPubKeep(RepeatPubVO repeatPubVO) throws Exception;

}
