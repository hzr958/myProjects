package com.smate.center.task.service.fund;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.task.dao.fund.rcmd.ConstFundCategoryDisDao;
import com.smate.center.task.dao.sns.quartz.CategoryMapBaseDao;
import com.smate.center.task.dao.sns.quartz.CategoryMapScmNsfcDao;
import com.smate.center.task.model.fund.rcmd.ConstFundCategoryDis;

@Service("constFundCategoryDisService")
@Transactional(rollbackOn = Exception.class)
public class ConstFundCategoryDisServiceImpl implements ConstFundCategoryDisService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ConstFundCategoryDisDao constFundCategoryDisDao;
  @Autowired
  private CategoryMapScmNsfcDao categoryMapScmNsfcDao;
  @Autowired
  private CategoryMapBaseDao categoryMapBaseDao;


  @Override
  public List<Map<String, Object>> getAllCategoryDis() {

    return constFundCategoryDisDao.getAllCategoryDis();
  }

  @Override
  public List<Long> getScmIdByDiscCode(String discCode) {

    return categoryMapScmNsfcDao.getScmCategoryByNsfcCategory(discCode);
  }

  @Override
  public void updateCategroyDis(Long scmCategoryId, Long id) {
    constFundCategoryDisDao.updateCategroyDis(scmCategoryId, id);
  }

  @Override
  public void saveCategoryDis(ConstFundCategoryDis constFundCategoryDis) {
    constFundCategoryDisDao.save(constFundCategoryDis);
  }

  @Override
  public List<String> findFundDisCodeByCategoryId(Long categoryId) {
    List<String> mapList = new ArrayList<String>();
    try {
      List<ConstFundCategoryDis> disList = constFundCategoryDisDao.findFundDisByCategoryId(categoryId);
      if (CollectionUtils.isNotEmpty(disList)) {
        for (ConstFundCategoryDis dis : disList) {
          mapList.add(dis.getDisId().toString());
        }
      }
    } catch (Exception e) {
      logger.error("", e);
    }
    return mapList;
  }

}
