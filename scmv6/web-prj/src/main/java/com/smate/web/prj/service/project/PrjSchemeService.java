package com.smate.web.prj.service.project;

import java.io.Serializable;
import java.util.List;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.prj.model.common.PrjScheme;
import com.smate.web.prj.vo.PrjSchemeVO;

/**
 * 项目资助类别.
 * 
 * @author liqinghua
 * 
 */
public interface PrjSchemeService extends Serializable {

  /**
   * 查询指定智能匹配前 N 条数据.
   * 
   * @param agencyId
   * @param language TODO
   * @param startStr
   * @param size
   * 
   * @return
   * @throws ServiceException
   */
  List<PrjSchemeVO> searchPrjSchemes(String nameKeyword, Long agencyId, int maxSize, String language)
      throws ServiceException;

  /**
   * 查找项目自助类别
   * 
   * @param name
   * @param agencyId
   * @return
   * @throws ServiceException
   */
  PrjScheme find(String name, Long agencyId) throws ServiceException;

  /**
   * 通过名字查找项目资助类别.
   *
   * @author houchuanjie
   * @date 2018年3月20日 下午8:46:38
   * @param name
   * @return
   * @throws ServiceException
   */
  PrjScheme findByName(String name) throws ServiceException;
}
