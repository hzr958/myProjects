package com.smate.web.fund.service.recommend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.fund.recommend.dao.CategoryMapBaseDao;
import com.smate.web.fund.recommend.model.CategoryMapBase;
import com.smate.web.fund.recommend.model.FundScienceArea;

/**
 * 科技领域服务类
 * 
 * @author WSN
 *
 *         2017年8月28日 下午3:12:43
 *
 */
@Service("categoryMapBaseService")
@Transactional(rollbackFor = Exception.class)
public class CategoryMapBaseServiceImpl implements CategoryMapBaseService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;

  @Override
  public Map<String, Object> buildCategoryMapBaseInfo(List<FundScienceArea> fundScienceAreaList) {
    Map<String, Object> allData = new HashMap<String, Object>();
    // 一级科技领域
    List<CategoryMapBase> firstLevelList = this.findCategoryMapBaseFirstLevelList();
    List<CategoryMapBase> subLevelList = new ArrayList<CategoryMapBase>();
    for (CategoryMapBase cate : firstLevelList) {
      // 二级科技领域
      subLevelList = this.findSubCategoryMapBaseList(cate.getCategryId());
      // 检查人员已选科技领域
      if (fundScienceAreaList != null && fundScienceAreaList.size() > 0) {
        for (CategoryMapBase ca : subLevelList) {
          for (FundScienceArea area : fundScienceAreaList) {
            if (area.getScienceAreaId() != null
                && ca.getCategryId() == NumberUtils.toInt(area.getScienceAreaId().toString())) {
              ca.setAdded(true);
            }
          }
        }
      }
      allData.put("CategoryMap_sub" + cate.getCategryId().toString(), subLevelList);
    }
    allData.put("CategoryMap_first", firstLevelList);
    if (allData == null || allData.isEmpty()) {
      allData.put("isNull", true);
    } else {
      allData.put("isNull", false);
    }
    return allData;
  }

  @Override
  public Map<String, Object> buildCategoryMapBaseInfo2() {
    Map<String, Object> allData = new HashMap<String, Object>();
    // 一级科技领域
    List<CategoryMapBase> firstLevelList = this.findCategoryMapBaseFirstLevelList();
    List<CategoryMapBase> subLevelList = new ArrayList<CategoryMapBase>();
    for (CategoryMapBase cate : firstLevelList) {
      // 二级科技领域
      subLevelList = this.findSubCategoryMapBaseList(cate.getCategryId());
      allData.put("CategoryMap_sub" + cate.getCategryId().toString(), subLevelList);
    }
    allData.put("CategoryMap_first", firstLevelList);
    if (allData == null || allData.isEmpty()) {
      allData.put("isNull", true);
    } else {
      allData.put("isNull", false);
    }
    return allData;
  }

  @Override
  public List<CategoryMapBase> findCategoryMapBaseFirstLevelList() {
    List<CategoryMapBase> list = categoryMapBaseDao.getCategoryMapBaseFirstLevelList();
    if (CollectionUtils.isNotEmpty(list)) {
      for (CategoryMapBase ca : list) {
        if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
          ca.setShowCategory(ca.getCategoryEn());
        } else {
          ca.setShowCategory(ca.getCategoryZh());
        }
      }
    }
    return list;
  }

  @Override
  public List<CategoryMapBase> findSubCategoryMapBaseList(Integer superCategoryId) {
    List<CategoryMapBase> list = categoryMapBaseDao.getSubCategoryMapBaseList(superCategoryId);
    if (CollectionUtils.isNotEmpty(list)) {
      for (CategoryMapBase ca : list) {
        if ("en_US".equals(LocaleContextHolder.getLocale().toString())) {
          ca.setShowCategory(ca.getCategoryEn());
        } else {
          ca.setShowCategory(ca.getCategoryZh());
        }
      }
    }
    return list;
  }

  @Override
  public Optional<String> getCategoryName(Long id, Locale locale) throws ServiceException {
    try {
      // 当且仅当存在学科领域
      return Optional.ofNullable(categoryMapBaseDao.get(NumberUtils.toInt(Objects.toString(id)))).map(dis -> {
        String disName = dis.getCategryId() + "-";
        disName += Locale.US.equals(locale) ? dis.getCategoryEn() : dis.getCategoryZh();
        return disName;
      });
    } catch (Exception e) {
      logger.error("获取领域名称错误.", e);
      throw new ServiceException(e);
    }
  }

}
