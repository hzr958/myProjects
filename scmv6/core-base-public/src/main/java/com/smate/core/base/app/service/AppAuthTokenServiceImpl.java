package com.smate.core.base.app.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.app.dao.AppAuthTokenDao;
import com.smate.core.base.app.model.AppAuthToken;
import com.smate.core.base.utils.security.Des3Utils;

@Service("appAuthTokenService")
@Transactional(rollbackOn = Exception.class)
public class AppAuthTokenServiceImpl implements AppAuthTokenService {
  @Autowired
  AppAuthTokenDao appAuthTokenDao;

  @Override
  public void saveToken(AppAuthToken auth) {
    appAuthTokenDao.save(auth);
  }

  @Override
  public AppAuthToken getToken(Long psnId) {
    return appAuthTokenDao.get(psnId);
  }

  @Override
  public void updateToken(Long psnId, String token, Date date) {
    appAuthTokenDao.updateTokenAndTime(psnId, token, date);

  }

  @Override
  public String createToken(Long psnId) {
    Date date = new Date();
    long currentTimeMillis = System.currentTimeMillis();
    String des3PsnId = Des3Utils.encodeToDes3(psnId.toString());
    String encryptstr = currentTimeMillis + ":" + psnId;
    String token = Des3Utils.encodeToDes3(encryptstr);
    if (this.getToken(psnId) == null) {
      this.saveToken(new AppAuthToken(psnId, date, token));
    } else {
      this.updateToken(psnId, token, date);
    }
    return token;
  }

  @Override
  public AppAuthToken getToken(Long psnId, String token) {
    return appAuthTokenDao.findOne(psnId, token);
  }
}
