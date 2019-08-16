package com.smate.web.psn.service.keyword;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.common.LocaleTextUtils;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.web.psn.dao.keywork.CategoryMapBaseDao;
import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.keyword.CategoryMapBase;
import com.smate.web.psn.model.keyword.CategoryMapBaseInfo;
import com.smate.web.psn.model.keyword.PsnScienceArea;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("categoryMapBaseService")
@Transactional(rollbackFor = Exception.class)
public class CategoryMapBaseServiceImpl implements CategoryMapBaseService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;

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
  public Map<String, Object> buildCategoryMapBaseInfo(List<PsnScienceArea> scienceAreaList) {
    Map<String, Object> allData = new HashMap<String, Object>();
    // 一级科技领域
    List<CategoryMapBase> firstLevelList = this.findCategoryMapBaseFirstLevelList();
    List<CategoryMapBase> subLevelList = new ArrayList<CategoryMapBase>();
    for (CategoryMapBase cate : firstLevelList) {
      // 二级科技领域
      subLevelList = this.findSubCategoryMapBaseList(cate.getCategryId());
      // 检查人员已选科技领域
      if (scienceAreaList != null && scienceAreaList.size() > 0) {
        for (CategoryMapBase ca : subLevelList) {
          for (PsnScienceArea area : scienceAreaList) {
            if (area.getScienceAreaId() != null && Integer.compare(ca.getCategryId(), area.getScienceAreaId()) == 0) {
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
  public void buildCategoryMapBaseInfo(PersonProfileForm form) {
    String scienceAreaIds = form.getScienceAreaIds();// 选中的科技领域
    Map<String, Object> allData = new HashMap<String, Object>();
    // 一级科技领域
    List<CategoryMapBase> firstLevelList = this.findCategoryMapBaseFirstLevelList();
    List<CategoryMapBase> subLevelList = new ArrayList<CategoryMapBase>();
    List<CategoryMapBase> scienceAreaList = new ArrayList<CategoryMapBase>();

    for (CategoryMapBase cate : firstLevelList) {
      // 二级科技领域
      subLevelList = this.findSubCategoryMapBaseList(cate.getCategryId());
      // 检查人员已选科技领域
      if (StringUtils.isNotBlank(scienceAreaIds)) {
        for (CategoryMapBase ca : subLevelList) {
          if (scienceAreaIds.contains(ca.getCategryId().toString())) {
            ca.setAdded(true);
            cate.setAdded(true);
            scienceAreaList.add(ca);
          }
        }
      }
      allData.put("CategoryMap_sub" + cate.getCategryId().toString(), subLevelList);
    }

    List<CategoryMapBase> sortAreaList = new ArrayList<CategoryMapBase>();
    String[] selectIds = scienceAreaIds.split(",");
    for (String id : selectIds) {// 排序选中的
      for (CategoryMapBase area : scienceAreaList) {
        if (id.equals(area.getCategryId().toString())) {
          sortAreaList.add(area);
        }
      }
    }

    allData.put("CategoryMap_first", firstLevelList);
    if (allData == null || allData.isEmpty()) {
      allData.put("isNull", true);
    } else {
      allData.put("isNull", false);
    }
    form.setCategoryMap(allData);
    form.setCategoryList(sortAreaList);// 选中的
  }

  @Override
  public List<Object> getCategoryMapBaseInfo(List<PsnScienceArea> scienceAreaList) {

    List<Object> sumlist = new ArrayList<Object>();
    // 一级科技领域
    List<CategoryMapBase> firstLevelList = this.findCategoryMapBaseFirstLevelList();
    List<CategoryMapBase> subLevelList = new ArrayList<CategoryMapBase>();

    for (CategoryMapBase cate : firstLevelList) {

      Map<String, Object> newmap = new HashMap<String, Object>();
      newmap.put("CategoryMap_" + cate.getCategryId().toString(), cate);
      // 二级科技领域
      subLevelList = this.findSubCategoryMapBaseList(cate.getCategryId());
      newmap.put("CategoryMap_sub" + cate.getCategryId().toString(), subLevelList);

      sumlist.add(newmap);

    }

    return sumlist;
  }

  @Override
  public List<CategoryMapBase> findAllCategoryMapBaseList() {
    return categoryMapBaseDao.getAllCategoryMapBaseList();
  }

  @Override
  public void findCategoryByName(PersonProfileForm form) {
    List<CategoryMapBase> categoryList = categoryMapBaseDao.findCategoryByName(form.getSearchKey());
    if (categoryList != null && categoryList.size() > 0) {
      JSONArray jsonArray = new JSONArray();
      if (XmlUtil.containZhChar(form.getSearchKey())) {
        for (CategoryMapBase area : categoryList) {
          JSONObject jsonObj = new JSONObject();
          jsonObj.put("code", area.getCategryId());
          jsonObj.put("name", area.getCategoryZh());
          jsonArray.add(jsonObj);
        }
      } else {
        for (CategoryMapBase area : categoryList) {
          JSONObject jsonObj = new JSONObject();
          jsonObj.put("code", area.getCategryId());
          jsonObj.put("name", area.getCategoryEn());
          jsonArray.add(jsonObj);
        }
      }
      form.setData(jsonArray.toString());
    }
  }

  @Override
  public Map<String, Object> buildAllScienceAreaInfo() throws PsnException {
    Map<String, Object> allData = new HashMap<String, Object>();
    try {
      // 科技领域表数据只有几十条，可全部查出来后再区分一级、二级科技领域，减少查数据库次数
      List<CategoryMapBase> all = categoryMapBaseDao.getAllCategoryMapBaseList();
      if (CollectionUtils.isNotEmpty(all)) {
        // 一级科技领域
        List<CategoryMapBaseInfo> firstLevelList = new ArrayList<CategoryMapBaseInfo>();
        // 二级科技领域
        List<CategoryMapBaseInfo> subLevelList = new ArrayList<CategoryMapBaseInfo>();
        boolean isZhLanguage = "zh_CN".equals(LocaleContextHolder.getLocale().toString());
        // 遍历list,找到一级、二级科技领域
        for (CategoryMapBase area : all) {
          // 拷贝实体类信息到操作类
          CategoryMapBaseInfo info = new CategoryMapBaseInfo();
          info.copyCategoryMapBaseInfo(area);
          if (!isZhLanguage) {
            info.setShowCategory(info.getCategoryEn());
          } else {
            info.setShowCategory(info.getCategoryZh());
          }
          // 将一级和二级领域分类
          if (area.getSuperCategoryId() == 0) {
            firstLevelList.add(info);
          } else {
            subLevelList.add(info);
          }
        }
        Collections.sort(firstLevelList);
        allData.put("area_first", firstLevelList);
        allData.put("area_second", subLevelList);
      }
    } catch (Exception e) {
      logger.error("获取科技领域信息出错", e);
      throw new PsnException(e);
    }
    return allData;
  }

  @Override
  public List<Map<String, Object>> buildEditScienceAreaInfo(List<Integer> selectAreaIds) throws PsnException {
    List<Map<String, Object>> areaList = new ArrayList<Map<String, Object>>();
    Locale locale = LocaleContextHolder.getLocale();
    try {
      // 科技领域表数据只有几十条，可全部查出来后再区分一级、二级科技领域，减少查数据库次数
      List<CategoryMapBase> firstLevel = categoryMapBaseDao.getCategoryMapBaseFirstLevelList();
      if (CollectionUtils.isNotEmpty(firstLevel)) {
        for (CategoryMapBase first : firstLevel) {
          Map<String, Object> firtMap = new LinkedHashMap<String, Object>();
          firtMap.put("categoryId", first.getCategryId());
          firtMap.put("categoryZh", first.getCategoryZh());
          firtMap.put("categoryEn", first.getCategoryEn());
          firtMap.put("showCategory",
              LocaleTextUtils.getLocaleText(locale, first.getCategoryZh(), first.getCategoryEn()));
          List<CategoryMapBase> secondLevel = categoryMapBaseDao.getSubCategoryMapBaseList(first.getCategryId());
          List<CategoryMapBaseInfo> secondLevelList = new ArrayList<CategoryMapBaseInfo>();
          Integer selectNum = 0;
          for (CategoryMapBase second : secondLevel) {
            if (CollectionUtils.isNotEmpty(selectAreaIds)) {
              for (Integer areaId : selectAreaIds) {
                if (areaId != null && second.getCategryId().longValue() == areaId.longValue()) {
                  second.setAdded(true);
                  selectNum++;
                }
              }
            }
            CategoryMapBaseInfo info = new CategoryMapBaseInfo();
            info.copyCategoryMapBaseInfo(second);
            info.setShowCategory(LocaleTextUtils.getLocaleText(locale, second.getCategoryZh(), second.getCategoryEn()));
            secondLevelList.add(info);
          }
          firtMap.put("selectNum", selectNum);
          firtMap.put("secondLevel", secondLevelList);
          areaList.add(firtMap);
        }

      }
    } catch (Exception e) {
      logger.error("获取科技领域信息出错", e);
      throw new PsnException(e);
    }
    return areaList;
  }

}
