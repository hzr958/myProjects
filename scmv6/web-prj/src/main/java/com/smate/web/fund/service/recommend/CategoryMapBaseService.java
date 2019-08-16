package com.smate.web.fund.service.recommend;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.fund.recommend.model.CategoryMapBase;
import com.smate.web.fund.recommend.model.FundScienceArea;
import com.smate.web.prj.exception.PrjException;

/**
 * 科技领域接口
 *
 * @author wsn
 * @createTime 2017年3月21日 下午7:55:38
 *
 */
public interface CategoryMapBaseService {

  /**
   * 构建学科信息
   * 
   * @return
   */
  Map<String, Object> buildCategoryMapBaseInfo(List<FundScienceArea> fundScienceArea) throws PrjException;

  /**
   * 获取第一级的科技领域
   * 
   * @return
   * @throws PrjException
   */
  public List<CategoryMapBase> findCategoryMapBaseFirstLevelList() throws PrjException;

  /**
   * 获取二级科技领域
   * 
   * @param superCategoryId
   * @return
   * @throws PrjException
   */
  public List<CategoryMapBase> findSubCategoryMapBaseList(Integer superCategoryId) throws PrjException;

  public Map<String, Object> buildCategoryMapBaseInfo2();

  public Optional<String> getCategoryName(Long id, Locale locale) throws ServiceException;

}
