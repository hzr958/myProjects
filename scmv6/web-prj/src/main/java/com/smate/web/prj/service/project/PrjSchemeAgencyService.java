package com.smate.web.prj.service.project;

import java.io.Serializable;
import java.util.List;

import com.smate.core.base.exception.ServiceException;
import com.smate.web.prj.model.common.PrjSchemeAgency;
import com.smate.web.prj.vo.PrjSchemeAgencyVO;

/**
 * 项目资助机构.
 *
 * @author houchuanjie
 * @date 2018年3月20日 下午8:33:16
 */

public interface PrjSchemeAgencyService extends Serializable {

  /**
   * 查询指定智能匹配前 N条数据.
   * 
   * @param startStr
   * @param size
   * @param language 语言
   * 
   * @return
   * @throws ServiceException
   */
  List<PrjSchemeAgencyVO> searchPrjSchemeAgencies(String keyword, int maxSize, String language) throws ServiceException;

  /**
   * 通过名字查找项目资助机构.
   * 
   * @param name
   * @return
   * @throws ServiceException
   */
  PrjSchemeAgency findByName(String name) throws ServiceException;
}
