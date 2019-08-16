package com.smate.web.v8pub.service.pdwh;

import java.util.List;

import com.smate.web.v8pub.exception.ServiceException;
import com.smate.web.v8pub.vo.pdwh.PdwhPubIndustryVO;

/**
 * 产业分类
 * 
 * @author YJ
 *
 *         2019年5月30日
 */
public interface CategoryHightechIndustryService {

  /**
   * 查询出所有行业信息，封装成json数据，存在缓存中，缓存中不存在时进行读表写缓存
   * 
   * @param industryCode
   * @return
   */
  String findAllIndustry() throws ServiceException;

  /**
   * 构建已选择的行业数据
   * 
   * @param codes
   * @return
   * @throws ServiceException
   */
  List<PdwhPubIndustryVO> buildChooseIndustry(String codes) throws ServiceException;

}
