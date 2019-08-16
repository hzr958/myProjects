package com.smate.center.batch.service.mail;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.constant.EmailConstants;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.string.ServiceUtil;


/**
 * 邮件退订url服务实现
 * 
 * @author tsz
 * 
 */
@Service("mailUnsubscribeUrlService")
public class MailUnsubscribeUrlServiceImpl implements MailUnsubscribeUrlService {
  // @Resource(name = "remotingServiceFactory")
  // private BaseRemotingServiceFactory remotingServiceFactory;

  @Autowired
  private ScmSystemUtil scmSystemUtil;
  @Autowired
  private MailUnsubscribeService mailUnsubscribeService;
  @Autowired
  private ConstMailTypeTemplateRelService constMailTypeTemplateRelService;

  @Autowired
  PersonService personService;

  /**
   * 生成退订url 用于添加在邮件模板中
   * 
   * @throws UnsupportedEncodingException
   * @throws ServiceException
   */
  @Override
  public String getUnsubscribeMailUrl(Long receivePsnId, String tempName)
      throws UnsupportedEncodingException, ServiceException {
    // 根据模板名字得到模板id 得到邮件类型id
    Integer mailCode = mailUnsubscribeService.findTemplateCodeByName(tempName);
    // 根据模板得到类别id

    Long typeId = constMailTypeTemplateRelService.getTypeidFromTemplateid(mailCode);

    StringBuilder url = new StringBuilder();
    // ServiceConstants.SCHOLAR_NODE_ID_1
    String domain = scmSystemUtil.getSysDomain();
    if (typeId != null && !typeId.equals(0L) && receivePsnId != null) {
      // String languageVersion =
      // personService.getLangByPsnId(receivePsnId);
      String languageVersion = "zh_CN";
      url.append(domain);
      // 拼接url
      url.append("/scmwebsns/unsubscribe/mail?psnid=");
      url.append(
          java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(String.valueOf(receivePsnId)), EmailConstants.ENCODING));
      url.append("&typeid=");
      url.append(java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(String.valueOf(typeId)), EmailConstants.ENCODING));
      // 拼接邮件模板语言
      url.append("&locale=");
      url.append(languageVersion);
    } else {

      // 获取当前系统的域名
      url.append(domain);
      url.append("/scmwebsns/user/setting/getMailTypeList");
      if (typeId != null) {
        url.append("?typeid=" + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(typeId.toString()), "utf-8"));
      }

    }

    return url.toString();
  }

}
