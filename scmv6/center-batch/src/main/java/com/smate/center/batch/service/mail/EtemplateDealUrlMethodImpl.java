package com.smate.center.batch.service.mail;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.string.ServiceUtil;


/**
 * @author zk
 * 
 *         生成邮件链接公共服务类
 */

@Service("etemplateDealUrlMethod")
public class EtemplateDealUrlMethodImpl implements EtemplateDealUrlMethod {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ScmSystemUtil scmSystemUtil;

  /**
   * 得到好友个人主页链接
   */

  @Override
  public String getFrdUrl(String email2log, Long psnId, Long toPsnId, String casUrl, Integer isAddFrd)
      throws ServiceException {
    String frdUrl = null;
    try {

      String domain = scmSystemUtil.getSysDomain();
      String autoLoginUrl = scmSystemUtil.getAutoLoginUrl(psnId, casUrl);

      String operateUrl = domain + "/scmwebsns/resume/psnView?des3PsnId=" + ServiceUtil.encodeToDes3(toPsnId.toString())
          + "&menuId=1100";

      if (StringUtils.isNotBlank(email2log)) {
        operateUrl += "&email2log=" + ServiceUtil.encodeToDes3(email2log);
      }
      if (isAddFrd == 1)
        operateUrl += "&isLinkedByMail=true";
      frdUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(operateUrl, ScmSystemUtil.ENCODING);
    } catch (UnsupportedEncodingException e) {
      logger.error("处理好友链接出错！" + e.getMessage());
      throw new ServiceException(e);
    }

    return frdUrl;
  }

  /**
   * 得到个人主页链接
   * 
   */
  @Override
  public String getPsnUrl(String email2log, Long psnId, String casUrl) throws ServiceException {
    String psnUrl = null;
    try {

      String domain = scmSystemUtil.getSysDomain();
      String autoLoginUrl = scmSystemUtil.getAutoLoginUrl(psnId, casUrl);

      String operateUrl = domain + "/psnweb/homepage/show?menuId=1200&email2log=" + ServiceUtil.encodeToDes3(email2log);

      psnUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(operateUrl, ScmSystemUtil.ENCODING);
    } catch (UnsupportedEncodingException e) {
      logger.error("处理个人好友链接出错！" + e.getMessage());
      throw new ServiceException(e);
    }
    return psnUrl;

  }

  /***
   * 得到成果详情url
   */

  @Override
  public String getPubDetail(String email2log, Long psnId, Long pubId, Integer nodeId, String casUrl)
      throws ServiceException {
    String pubDetailUrl = null;
    try {
      String domain = scmSystemUtil.getSysDomain();
      String autoLoginUrl = scmSystemUtil.getAutoLoginUrl(psnId, casUrl);

      String optUrl = domain + "/scmwebsns/publication/loginView?des3Id=" + ServiceUtil.encodeToDes3(pubId.toString())
          + "," + nodeId;
      if (StringUtils.isNotBlank(email2log)) {
        optUrl += "&email2log=" + ServiceUtil.encodeToDes3(email2log);
      }
      pubDetailUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(optUrl, ScmSystemUtil.ENCODING);
    } catch (Exception e) {
      logger.error("获取成果详情链接时出错 ！" + e.getMessage());
      throw new ServiceException(e);
    }
    return pubDetailUrl;

  }

  /**
   * 得到好友所有成果url
   */
  @Override
  public String getFrdSrc(String email2log, Long psnId, Long frdPsnId, String casUrl, String src)
      throws ServiceException {

    String frdPubMoreUrl = null;
    try {
      String domain = scmSystemUtil.getSysDomain();
      String autoLoginUrl = scmSystemUtil.getAutoLoginUrl(psnId, casUrl);
      String groupOperateUrl =
          domain + "/scmwebsns/resume/psnView?des3PsnId=" + ServiceUtil.encodeToDes3(frdPsnId.toString())
              + "&menuId=1100&src=" + src + "&email2log=" + ServiceUtil.encodeToDes3(email2log);
      frdPubMoreUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(groupOperateUrl, ScmSystemUtil.ENCODING);
    } catch (Exception e) {
      logger.error("获取好友个人主页位置链接时出错！" + e.getMessage());
      throw new ServiceException(e);
    }
    return frdPubMoreUrl;

  }

  /**
   * 得到查看url
   */
  @Override
  public String getViewUrl(String email2log, String viewStr, Long psnId, String casUrl, Integer menuId)
      throws ServiceException {
    String viewUrl = null;
    try {
      if (StringUtils.isBlank(viewStr))
        return viewUrl;
      if (StringUtils.isBlank(casUrl))
        casUrl = scmSystemUtil.getSysDomain() + "/cas/";
      String domain = scmSystemUtil.getSysDomain();
      String autoLoginUrl = scmSystemUtil.getAutoLoginUrl(psnId, casUrl);
      StringBuffer optUrl = new StringBuffer();
      optUrl.append(domain + viewStr);
      if (viewStr.contains("?")) {
        optUrl.append("&");
      } else {
        optUrl.append("?");
      }
      optUrl.append("menuId=" + menuId + "&email2log=" + ServiceUtil.encodeToDes3(email2log));
      viewUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(optUrl.toString(), ScmSystemUtil.ENCODING);

    } catch (UnsupportedEncodingException e) {
      logger.error("处理链接出错！" + e.getMessage());
      throw new ServiceException(e);
    }

    return viewUrl;
  }

  /**
   * 邮件设置url
   */
  @Override
  public String getScreenMailUrl() throws ServiceException {
    return scmSystemUtil.getSysDomain() + "/scmwebsns/user/setting/getMailTypeList";
  }

  /***
   * 邮件预览
   */
  @Override
  public String getMailViewUrl(Long mailId) throws ServiceException {
    try {
      String paramValue = mailId + "|" + ServiceConstants.SCHOLAR_NODE_ID_1 + "|" + new Date().getTime();
      String mailViewParams = "des3Id=" + java.net.URLEncoder.encode(ServiceUtil.encodeToDes3(paramValue), "utf-8");
      String viewMailUrl = scmSystemUtil.getSysDomain() + "/scmwebsns/mail/view?" + mailViewParams;

      return viewMailUrl;
    } catch (Exception e) {
      logger.error("生成mailId={}的邮件预览链接时出错 ！", mailId);
      throw new ServiceException(e);
    }
  }

  /**
   * 个人动态首页
   */

  @Override
  public String getPsnDynUrl(String email2log, Long psnId, String casUrl) throws ServiceException {
    String dynUrl = null;
    try {
      String domain = scmSystemUtil.getSysDomain();
      String autoLoginUrl = scmSystemUtil.getAutoLoginUrl(psnId, casUrl);

      String operateUrl = domain + "/scmwebsns/main?menuId=1100&email2log=" + ServiceUtil.encodeToDes3(email2log);
      dynUrl = autoLoginUrl + "&service=" + java.net.URLEncoder.encode(operateUrl, ScmSystemUtil.ENCODING);
    } catch (Exception e) {
      logger.error("生成psnId={}的动态首页链接时出错 ！", psnId);
      throw new ServiceException(e);
    }
    return dynUrl;
  }
}
