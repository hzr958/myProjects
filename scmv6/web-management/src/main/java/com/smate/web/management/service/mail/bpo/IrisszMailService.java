package com.smate.web.management.service.mail.bpo;

import com.smate.web.management.model.mail.bpo.IrisszMail;


public interface IrisszMailService {

  /**
   * 
   * @param irisszMial
   * @throws Exception
   */
  void sendIrisMail(IrisszMail irisszMail) throws Exception;

}
