package com.smate.center.task.service.sns.psn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.utils.ShortUrlUtils;
import com.smate.core.base.utils.dao.shorturl.OpenShortUrlDao;
import com.smate.core.base.utils.dao.shorturl.OpenShortUrlPreproductionDao;
import com.smate.core.base.utils.model.shorturl.OpenShortUrlPreproduction;

@Service("preGenerateShortUrlServive")
@Transactional(rollbackFor = Exception.class)
public class PreGenerateShortUrlServiveImpl implements PreGenerateShortUrlServive {
  @Autowired
  private OpenShortUrlDao openShortUrlDao;
  @Autowired
  private OpenShortUrlPreproductionDao openShortUrlPreproductionDao;

  @Override
  public void startGenerateShortUrl() throws Exception {

    String shortUrl = "";
    Boolean isExists = true;
    while (isExists) {
      // 生成短地址
      shortUrl = ShortUrlUtils.createShortUrlRandom6();
      // 查询短地址是否存在
      boolean preExists = openShortUrlDao.checkShortUrlIsExists(shortUrl);
      boolean oldExists = openShortUrlPreproductionDao.checkShortUrlIsExists(shortUrl);
      if (preExists || oldExists) {
        isExists = true;
      } else {
        isExists = false;
      }
    }
    OpenShortUrlPreproduction url = new OpenShortUrlPreproduction();
    url.setShortUrl(shortUrl);
    openShortUrlPreproductionDao.save(url);
  }

}
