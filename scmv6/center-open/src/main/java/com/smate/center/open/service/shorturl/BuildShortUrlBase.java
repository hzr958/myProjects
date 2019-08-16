package com.smate.center.open.service.shorturl;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.service.data.shorturl.ShortUrlUtils;
import com.smate.core.base.utils.dao.shorturl.OpenShortUrlDao;
import com.smate.core.base.utils.dao.shorturl.OpenShortUrlPreproductionDao;

/**
 * 公用类
 * 
 * @author tsz
 *
 */
public abstract class BuildShortUrlBase {

  @Autowired
  private OpenShortUrlDao openShortUrlDao;
  @Autowired
  private OpenShortUrlPreproductionDao openShortUrlPreproductionDao;

  public String getShortUrl() {
    // 数据库预产生 短地址 通过序列来取
    String shortUrl = openShortUrlPreproductionDao.getShortUrlById(openShortUrlPreproductionDao.getNewShortUrlId());

    /*
     * while (isExists) { shortUrl = ShortUrlUtils.createShortUrlRandom6(); isExists =
     * openShortUrlDao.checkShortUrlIsExists(shortUrl);
     * 
     * } return shortUrl;
     */
    return shortUrl;
  }

  public String getShortUrl32() {

    String shortUrl = "";
    Boolean isExists = true;
    while (isExists) {
      shortUrl = ShortUrlUtils.createShortUrl32();
      isExists = openShortUrlDao.checkShortUrlIsExists(shortUrl);

    }
    return shortUrl;
  }

}
