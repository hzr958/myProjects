package com.smate.web.management.service.mail;

import com.smate.web.management.model.mail.IrisszMailDetail;


public interface IrisszMailDetailService {

  /**
   * @param irisszMailDetail
   * @throws Exception
   */
  void sendIrisMailDetail(IrisszMailDetail irisszMailDetail) throws Exception;

}
