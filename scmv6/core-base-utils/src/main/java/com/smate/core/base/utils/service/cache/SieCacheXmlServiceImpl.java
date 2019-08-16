package com.smate.core.base.utils.service.cache;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.dao.cache.SieCacheXmlDao;
import com.smate.core.base.utils.model.SieCacheXml;

/**
 * 
 * @author yxs
 * @descript xml缓存接口
 */
@Service("sieCacheXmlService")
@Transactional(rollbackFor = Exception.class)
public class SieCacheXmlServiceImpl implements SieCacheXmlService {

  @Autowired
  private SieCacheXmlDao insCacheXmlDao;

  @Override
  public void put(String id, String xml) {
    if (StringUtils.isBlank(id) || StringUtils.isBlank(xml))
      return;
    SieCacheXml xmlCache = insCacheXmlDao.get(id);
    if (xmlCache == null) {
      xmlCache = new SieCacheXml();
      xmlCache.setId(id);
    }
    xmlCache.setXml(xml);
    xmlCache.setCacheDate(new Date());
    insCacheXmlDao.save(xmlCache);
  }

  @Override
  public String get(String id) {
    SieCacheXml xmlObj = insCacheXmlDao.get(id);
    return xmlObj == null ? "" : xmlObj.getXml();
  }

  @Override
  public void remove(String id) {
    insCacheXmlDao.delete(id);
  }

}
