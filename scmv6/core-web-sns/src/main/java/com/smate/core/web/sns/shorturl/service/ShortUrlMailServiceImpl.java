package com.smate.core.web.sns.shorturl.service;

import java.util.Date;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.email.service.EmailCommonService;
import com.smate.core.base.mail.dao.MailLinkDao;
import com.smate.core.base.mail.dao.MailLinkRecordDao;
import com.smate.core.base.mail.model.mongodb.MailLink;
import com.smate.core.base.mail.model.mongodb.MailLinkRecord;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.struts2.Struts2Utils;

/**
 * 邮件短地址服务实现
 * 
 * @author tsz
 *
 */
@Service("shortUrlMailService")
@Transactional(rollbackFor = Exception.class)
public class ShortUrlMailServiceImpl implements ShortUrlMailService {
  @Autowired
  private MailLinkDao mailLinkDao;
  @Autowired
  private MailLinkRecordDao mailLinkRecordDao;



  @Override
  public void doSendRedirect() throws Exception {
    MailLink mailLink = mailLinkDao.findByShortUrl(getShortUrl());
    if (mailLink != null && checkUsable(mailLink)) {
      String url = mailLink.getUrl();
      if (url.contains(EmailCommonService.PC_OR_MB_TOKEN)) {
        String[] split = url.split("&&");// 继续防止地址出现冲突
        if (split.length == 2) {
          url = url.replace(EmailCommonService.PC_OR_MB_TOKEN, "");// 删除标识
          // 判断是移动端请求还是pc端请求
          if (judgmentIsMid(Struts2Utils.getRequest())) {// 如果是移动端请求
            // 拆分这个url
            url = url.split("&&")[1].replace("mobile=", "");
          } else {
            url = url.split("&&")[0].replace("pc=", "");
          }
        }
      }
      MailLinkRecord mlr = new MailLinkRecord();
      mlr.setLinkId(mailLink.getLinkId());
      mlr.setCreateDate(new Date());
      mlr.setIp(Struts2Utils.getRemoteAddr());
      mlr.setPsnId(SecurityUtils.getCurrentUserId());
      mailLinkRecordDao.save(mlr);
      mailLink.setCount(mailLink.getCount() + 1);
      mailLinkDao.save(mailLink);
      // 转发 跳转到新系统
      url = url.replace("/scmwebsns/", "/psnweb/");
      Struts2Utils.redirect(url);
    }

  }

  /**
   * 检查短地址是否可用
   * 
   * @param mailLink
   * @return
   */
  private boolean checkUsable(MailLink mailLink) {
    // limitCount 限制访问数量 0=不限访问次数
    // timeOutDate 过期时间 空=不限
    boolean status = false;
    Integer limitCount = mailLink.getLimitCount();
    Integer count = mailLink.getCount();
    Date timeOutDate = mailLink.getTimeOutDate();
    Date date = new Date();
    if (limitCount == 0 && timeOutDate == null) {
      status = true;
    }
    if (limitCount == 0 && timeOutDate != null && timeOutDate.getTime() > date.getTime()) {
      status = true;
    }
    if (limitCount != 0 && timeOutDate == null && limitCount > count) {
      status = true;
    }
    if (limitCount != 0 && timeOutDate != null && timeOutDate.getTime() > date.getTime() && limitCount > count) {
      status = true;
    }
    return status;
  }

  /**
   * 获取短地址方法
   * 
   * @return
   */
  private String getShortUrl() {
    String uri = Struts2Utils.getRequest().getRequestURI();
    String str = uri.substring(uri.lastIndexOf("/") + 1);
    if (str.lastIndexOf("?") > 0) {
      return str.substring(0, uri.lastIndexOf("?"));
    }
    return str;
  }



  /**
   * 判断当前系统是不是移动端
   * 
   * @param httpServletRequest
   * @return
   */
  private boolean judgmentIsMid(HttpServletRequest httpServletRequest) {
    String ua = httpServletRequest.getHeader("User-Agent");
    String regex =
        "ios|symbianos|windows mobile|windows ce|ucweb|rv:1.2.3.4|midp|android|webos|iphone|ipad|ipod|blackberry|iemobile|opera mini";
    Pattern p = Pattern.compile(regex);
    return p.matcher(ua.toLowerCase()).find();
  }
}
