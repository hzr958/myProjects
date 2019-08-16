package com.smate.center.oauth.service.psnregister;

/**
 * 
 * @author Ai Jiangbin
 * 
 * @creation 2017年10月13日
 */
public interface ConstRegionService {

  /**
   * 获取指定国家或地区的省份.
   * 
   * @param superRegionId
   * @return
   * @throws ServiceException
   */
  public String findRegionJsonData(Long superRegionId) throws Exception;

}
