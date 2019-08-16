package com.smate.web.psn.service.keyword;

import java.util.List;
import java.util.Map;

import com.smate.web.psn.exception.PsnException;
import com.smate.web.psn.model.homepage.PersonProfileForm;
import com.smate.web.psn.model.keyword.CategoryMapBase;
import com.smate.web.psn.model.keyword.PsnScienceArea;

/**
 * 科技领域接口
 *
 * @author wsn
 * @createTime 2017年3月21日 下午7:55:38
 *
 */
public interface CategoryMapBaseService {

  /**
   * 查找科技领域一级分类
   * 
   * @return
   */
  List<CategoryMapBase> findCategoryMapBaseFirstLevelList();

  /**
   * 根据父级学科ID查找子学科
   * 
   * @param superCategoryId
   * @return
   */
  List<CategoryMapBase> findSubCategoryMapBaseList(Integer superCategoryId);

  /**
   * 构建学科信息
   * 
   * @return
   */
  Map<String, Object> buildCategoryMapBaseInfo(List<PsnScienceArea> scienceAreaList);

  /**
   * 构建学科信息
   * 
   * @return
   */
  void buildCategoryMapBaseInfo(PersonProfileForm form);

  /**
   * 获取所有科技领域
   * 
   * @return
   */
  List<CategoryMapBase> findAllCategoryMapBaseList();

  List<Object> getCategoryMapBaseInfo(List<PsnScienceArea> scienceAreaList);

  /**
   * 根据关键字查找科技领域
   * 
   * @return
   */
  void findCategoryByName(PersonProfileForm form);

  /**
   * 新的构建科技领域信息方法
   * 
   * @return
   * @throws PsnException
   */
  public Map<String, Object> buildAllScienceAreaInfo() throws PsnException;

  /**
   * 
   * @return
   * @throws PsnException
   */
  public List<Map<String, Object>> buildEditScienceAreaInfo(List<Integer> selectAreaIds) throws PsnException;
}
