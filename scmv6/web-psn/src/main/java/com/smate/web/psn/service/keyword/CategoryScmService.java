package com.smate.web.psn.service.keyword;

import com.smate.web.psn.model.homepage.PersonProfileForm;

/**
 * 科技领域接口
 *
 * @author wsn
 * @createTime 2017年3月21日 下午7:55:38
 *
 */
public interface CategoryScmService {

  /**
   * 根据关键字查找科技领域
   * 
   * @return
   */
  void findCategoryByName(PersonProfileForm form, boolean isNeedfirstArea);
}
