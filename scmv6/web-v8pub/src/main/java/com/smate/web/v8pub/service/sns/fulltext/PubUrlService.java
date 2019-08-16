package com.smate.web.v8pub.service.sns.fulltext;

import com.smate.core.base.pub.vo.PubDetailVO;

public interface PubUrlService {

  void buildPubUrl(PubDetailVO pubDetailVO);

  /**
   * 构建成果的短地址，如果是基准库的用基准库的短地址，没有用个人库的
   * 
   * @param pubDetailVO
   */
  void builPubShortUrl(PubDetailVO pubDetailVO);

}
