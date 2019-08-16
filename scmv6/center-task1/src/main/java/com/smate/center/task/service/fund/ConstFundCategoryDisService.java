package com.smate.center.task.service.fund;

import java.util.List;
import java.util.Map;

import com.smate.center.task.model.fund.rcmd.ConstFundCategoryDis;


public interface ConstFundCategoryDisService {

  List<Map<String, Object>> getAllCategoryDis();

  List<Long> getScmIdByDiscCode(String discCode);

  void updateCategroyDis(Long scmCategoryId, Long categoryId);

  void saveCategoryDis(ConstFundCategoryDis constFundCategoryDis);

  List<String> findFundDisCodeByCategoryId(Long categoryId);

}
