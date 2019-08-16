package com.smate.center.batch.service.psn;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.service.pub.ConstSurNameService;
import com.smate.center.batch.service.pub.mq.RcmdSyncFlagMessageProducer;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.model.PsnProfileUrl;

/**
 * 个人主页URL实现类.
 * 
 * @author zhuangyanming
 * 
 */
@Service("psnProfileUrlService")
@Transactional(rollbackFor = Exception.class)
public class PsnProfileUrlServiceImpl implements PsnProfileUrlService {

  /**
   * 
   */
  private static final long serialVersionUID = -5711349376925686886L;

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PsnScoreService psnScoreService;
  @Autowired
  private ConstSurNameService constSurNameService;
  @Autowired
  private RcmdSyncFlagMessageProducer rcmdSyncFlagMessageProducer;

  @Override
  public PsnProfileUrl findPsnProfileUrlById(Long psnId) throws ServiceException {
    return psnProfileUrlDao.get(psnId);
  }

  // 删除个人主页url
  @Override
  public void delPsnProfileUrl(Long psnId) throws ServiceException {
    psnProfileUrlDao.delete(psnId);
  }

  @Override
  public int setPsnProfileUrl(String url, Long psnId) throws ServiceException {
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
      // FIXME 2015-10-29 取消MQ -done
      rcmdSyncFlagMessageProducer.syncPsnIcons(psnId);
      // 刷新个人信息计分
      /*
       * psnScoreService.updatePsnScore(psnId, PsnScoreConstants.PSN_HOME);
       */
    } catch (Exception e) {
      logger.error("设置个人主页URL", e);
      throw new ServiceException(e);
    }
    return 1;
  }

  @Override
  public String findUrl(Long psnId) throws ServiceException {
    try {
      PsnProfileUrl obj = this.psnProfileUrlDao.find(psnId);
      if (obj != null)
        return obj.getUrl();
      return null;
    } catch (Exception e) {
      logger.error("查找个人主页的URL", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public Long findPsn(String url) throws ServiceException {
    try {
      PsnProfileUrl obj = this.psnProfileUrlDao.find(url);
      if (obj != null)
        return obj.getPsnId();
      return null;
    } catch (Exception e) {
      logger.error("查找个人主页的人员ID", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public String findAndCreateUrl(String cname, Long psnId) throws ServiceException {
    Map<String, String> pinyin = null;
    if (StringUtils.isNotBlank(cname) && StringUtils.isAsciiPrintable(cname)) {
      String firstName = cname.toLowerCase().replaceAll(" ", "");
      pinyin = new HashMap<String, String>();
      pinyin.put(FIRST_NAME, firstName);
    } else {
      pinyin = constSurNameService.parsePinYin(cname);
    }

    return this.findAndCreateUrl(pinyin, psnId);
  }

  private String findAndCreateUrl(Map<String, String> pinyin, Long psnId) throws ServiceException {

    String oldUrl = this.findUrl(psnId);
    if (oldUrl == null) {
      String firstName = StringUtils.trim(Objects.toString(pinyin.get(FIRST_NAME), ""));
      String lastName = StringUtils.trim(Objects.toString(pinyin.get(LAST_NAME), ""));
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

}
