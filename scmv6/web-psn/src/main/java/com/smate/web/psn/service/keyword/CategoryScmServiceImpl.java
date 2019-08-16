package com.smate.web.psn.service.keyword;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.data.XmlUtil;
import com.smate.web.psn.dao.keywork.CategoryScmDao;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.keyword.CategoryScm;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service("CategoryScmService")
@Transactional(rollbackFor = Exception.class)
public class CategoryScmServiceImpl implements CategoryScmService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private CategoryScmDao categoryScmDao;

  @Override
  public void findCategoryByName(PersonProfileForm form, boolean isNeedfirstArea) {
    List<CategoryScm> categoryList;
    if (isNeedfirstArea) {// 不需要一级科技领域
      categoryList = categoryScmDao.findCategoryByNameNotFirst(form.getSearchKey());
    } else {
      categoryList = categoryScmDao.findCategoryByName(form.getSearchKey());
    }
    if (categoryList != null && categoryList.size() > 0) {
      JSONArray jsonArray = new JSONArray();
      if (XmlUtil.containZhChar(form.getSearchKey())) {
        for (CategoryScm area : categoryList) {
          JSONObject jsonObj = new JSONObject();
          jsonObj.put("code", area.getCategoryId());
          jsonObj.put("name", area.getCategoryZh());
          jsonArray.add(jsonObj);
        }
      } else {
        for (CategoryScm area : categoryList) {
          JSONObject jsonObj = new JSONObject();
          jsonObj.put("code", area.getCategoryId());
          jsonObj.put("name", area.getCategoryEn());
          jsonArray.add(jsonObj);
        }
      }
      form.setData(jsonArray.toString());
    }
  }

}
