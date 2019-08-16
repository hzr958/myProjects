package com.smate.center.batch.service.mail;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.acegisecurity.util.EncryptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.service.consts.SysDomainConst;

@SuppressWarnings("deprecation")
@Service("scmSystemUtil")
public class ScmSystemUtilImpl implements ScmSystemUtil {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SysDomainConst sysDomainConst;

  /**
   * 获取自动登录地址固定部分(获取完整的自动登录地址需在返回值后追加参数service及对应值).
   * 
   * @param personID
   * @param casUrl
   * @return
   * @throws UnsupportedEncodingException
   */
  @Override
  public String getAutoLoginUrl(Long personID, String casUrl) throws UnsupportedEncodingException {

    if (StringUtils.isBlank(casUrl)) {
      casUrl = this.getSysDomain() + "/cas/";
    }

    String userType = "CITE";
    String passStr = userType + "|" + personID;
    String encpassword = EncryptionUtils.encrypt("111111222222333333444444", passStr);
    String citePassword = java.net.URLEncoder.encode(encpassword, ScmSystemUtil.ENCODING);
    String inviteUrl = casUrl + "login?submit=true&username=CITE&password=" + citePassword;
    return inviteUrl;
  }

  /**
   * 获取当前系统域名.
   * 
   * @return
   */
  @Override
  public String getSysDomain() {
    try {
      return sysDomainConst.getSnsDomain();
    } catch (Exception e) {
      logger.error("获取当前系统域名", e);
    }
    return null;
  }

}
