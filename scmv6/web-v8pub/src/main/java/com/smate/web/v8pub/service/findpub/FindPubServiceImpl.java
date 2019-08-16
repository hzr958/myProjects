package com.smate.web.v8pub.service.findpub;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.common.LocaleTextUtils;
import com.smate.web.v8pub.dao.sns.CategoryScmDao;
import com.smate.web.v8pub.po.sns.CategoryScm;

@Service("findPubService")
@Transactional(rollbackFor = Exception.class)
public class FindPubServiceImpl implements FindPubService {
  @Autowired
  private CategoryScmDao categoryScmDao;

  // 获取全部的我科技领域
  @Override
  public List<Map<String, Object>> getAllScienceArea() throws ServiceException {
    List<Map<String, Object>> allScienceAreaList = new ArrayList<Map<String, Object>>();
    List<CategoryScm> firstAreaList = categoryScmDao.findFirstScienceArea();
    Locale locale = LocaleContextHolder.getLocale();
    Map<String, Object> areaItemMap = null;
    for (CategoryScm item : firstAreaList) {
      // 一级科技领域
      areaItemMap = new HashMap<String, Object>();
      Long areaId = item.getCategoryId();
      areaItemMap.put("areaId", areaId);
      areaItemMap.put("showName", LocaleTextUtils.getLocaleText(locale, item.getCategoryZh(), item.getCategoryEn()));

      // 二级科技领域
      List<CategoryScm> secondAreaList = categoryScmDao.findSecondScienceArea(areaId);
      List<Map<String, Object>> secondAreas = new ArrayList<Map<String, Object>>();
      if (CollectionUtils.isNotEmpty(secondAreaList)) {
        for (CategoryScm area : secondAreaList) {
          Map<String, Object> secondItemMap = new HashMap<String, Object>();
          secondItemMap.put("areaId", area.getCategoryId());
          secondItemMap.put("showName",
              LocaleTextUtils.getLocaleText(locale, area.getCategoryZh(), area.getCategoryEn()));
          secondAreas.add(secondItemMap);
        }
      }
      areaItemMap.put("second", secondAreas);
      allScienceAreaList.add(areaItemMap);
    }
    return allScienceAreaList;
  }

}
