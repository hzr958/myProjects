package com.smate.web.v8pub.service.sns.publist;

import com.smate.web.v8pub.vo.PubListVO;

public interface PubListService {

  /**
   * 显示成果列表
   * 
   * @param pubListVO
   */
  public void showPubList(PubListVO pubListVO);

  public void dealPubStatistics(PubListVO pubListVO);

}
