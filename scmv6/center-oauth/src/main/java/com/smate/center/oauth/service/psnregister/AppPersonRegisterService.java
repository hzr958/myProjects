package com.smate.center.oauth.service.psnregister;

import com.smate.center.oauth.model.profile.PersonRegisterForm;
import com.smate.center.oauth.model.profile.ScmRegisterVerifCode;

public interface AppPersonRegisterService {

  public String sendverifiMail(String emailOrLogin) throws Exception;

  public String sendverifiMessage(String phonenumber) throws Exception;

  public ScmRegisterVerifCode getInfoByAccount(String emailOrLogin) throws Exception;


  public Boolean checkUserName(String lowerCase) throws Exception;

  public int checkCode(String lowerCase, String verifCode) throws Exception;

  public long saveUserInfo(PersonRegisterForm form) throws Exception;

  public void checkUserInfo(PersonRegisterForm form) throws Exception;
}
