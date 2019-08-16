package com.smate.web.psn.service.profile;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.utils.dao.shorturl.OpenShortUrlDao;
import com.smate.core.base.utils.security.SecurityUtils;
import com.smate.core.base.utils.string.ServiceUtil;
import com.smate.web.psn.dao.rcmdsync.RcmdSyncPsnInfoDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.rcmdsync.RcmdSyncPsnInfo;

/**
 * 个人主页URL服务接口实现类
 * 
 * @author Administrator
 *
 */
@Service("psnProfileUrlService")
@Transactional(rollbackFor = Exception.class)
public class PsnProfileUrlServiceImpl implements PsnProfileUrlService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private RcmdSyncPsnInfoDao rcmdSyncPsnInfoDao;
  @Autowired
  private OpenShortUrlDao openShortUrlDao;

  /**
   * 查找个人主页URL
   */
  @Override
  public String findUrl(Long psnId) throws PsnException {
    try {
      PsnProfileUrl obj = this.psnProfileUrlDao.find(psnId);
      if (obj != null)
        return obj.getUrl();
      return null;
    } catch (Exception e) {
      logger.error("查找个人主页的URL", e);
      throw new PsnException(e);
    }
  }

  /**
   * 保存个人主页URL
   */
  @Override
  public int setPsnProfileUrl(String url, Long psnId) throws PsnException {
    try {
      // URL是否被占用
      if (psnProfileUrlDao.isUsed(url)) {
        return 0;
      }
      PsnProfileUrl obj = psnProfileUrlDao.find(psnId);
      if (obj == null) {
        obj = new PsnProfileUrl(psnId);
      }
      obj.setUrl(url);
      psnProfileUrlDao.save(obj);
      // 同步rcmd
      RcmdSyncPsnInfo rsp = rcmdSyncPsnInfoDao.get(psnId);
      if (rsp == null) {
        rsp = new RcmdSyncPsnInfo(psnId);
      }
      rsp.setAdditinfoFlag(1);
      rcmdSyncPsnInfoDao.save(rsp);
      // rcmdSyncFlagMessageProducer.syncPsnIcons(psnId);
      // 刷新个人信息计分
      /*
       * psnScoreService.updatePsnScore(psnId, PsnScoreConstants.PSN_HOME);
       */
    } catch (Exception e) {
      logger.error("设置个人主页URL", e);
      throw new PsnException(e);
    }
    return 1;
  }

  @Override
  public String findAndCreateUrl(String cname, Long psnId) throws PsnException {
    Map<String, String> pinyin = null;
    if (StringUtils.isNotBlank(cname) && StringUtils.isAsciiPrintable(cname)) {
      String firstName = cname.toLowerCase().replaceAll(" ", "");
      pinyin = new HashMap<String, String>();
      pinyin.put(FIRST_NAME, firstName);
    } else {
      pinyin = ServiceUtil.parsePinYin(cname);
    }

    return this.findAndCreateUrl(pinyin, psnId);
  }

  private String findAndCreateUrl(Map<String, String> pinyin, Long psnId) throws PsnException {

    String oldUrl = this.findUrl(psnId);
    if (oldUrl == null) {
      String firstName = StringUtils.trim(ObjectUtils.toString(pinyin.get(FIRST_NAME)));
      String lastName = StringUtils.trim(ObjectUtils.toString(pinyin.get(LAST_NAME)));
      String url = (firstName + lastName).toLowerCase().replaceAll("\\pP", "").replaceAll(" ", "");
      if (url.length() > MAX_URL_LEN) {
        if (firstName.length() > 1) {// 字符超长只截取firstName首个字母
          firstName = firstName.substring(0, 1);
        }
        url = StringUtils.trim(firstName + lastName).toLowerCase();
        if (url.length() > MAX_URL_LEN) {// 字符还是超长截取前MAX_URL_LEN个字符
          url = url.substring(0, MAX_URL_LEN);
        }
      }

      if (url.length() == 0) {// 没有姓名使用psnId
        url = RandomStringUtils.random(RAND_URL_LEN, RAND_URL_CHAR);// 随机字符串
      }
      int count = 0;
      String newUrl = url;
      while (this.setPsnProfileUrl(url, psnId) == 0) {// 出现重复，加数字重新生成
        count++;
        url = newUrl + count;
        if (count > MAX_URL_LOOP) {
          count = 0;
          url = RandomStringUtils.random(RAND_URL_LEN, RAND_URL_CHAR);// 随机字符串
        }
        if (url.length() > MAX_URL_LEN) {// 字符还是超长截取前MAX_URL_LEN个字符
          url = url.substring(0, MAX_URL_LEN);
        }
      }
      return url;
    } else {
      return oldUrl;
    }
  }

  @Override
  public void savePsnShortUrl(PersonProfileForm form) throws PsnException {
    if (!openShortUrlDao.isExIst(form.getNewShortUrl())) {
      openShortUrlDao.modifierShortUrl(form.getOldShortUrl(), form.getNewShortUrl());
      PsnProfileUrl psnProfileUrl = psnProfileUrlDao.find(SecurityUtils.getCurrentUserId());
      if (psnProfileUrl != null) {
        psnProfileUrl.setPsnIndexUrl(form.getNewShortUrl());
        psnProfileUrl.setUpdateDate(new Date());
      }
      form.getResultMap().put("result", "success");
      // form.getResultMap().put("msg", this.getStringByLocale("短地址已修改！",
      // "短地址已修改！"));
    } else {
      form.getResultMap().put("result", "error");
      form.getResultMap().put("msg", this.getStringByLocale("Short-url is already used.", "短地址已存在！"));
    }

  }

  // 根据语言环境返回对应的字符串
  private String getStringByLocale(String enString, String zhString) {
    if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
      return enString;
    } else {
      return zhString;
    }
  }

}
