package com.smate.web.psn.service.psnwork;

import java.util.List;
import java.util.Map;

import com.smate.web.psn.exception.ServiceException;
import com.smate.web.psn.model.homepage.PersonProfileForm;

public interface PsnConstRegionService {

  /**
   * 获取国家地区
   */
  String findRegionJsonData(Long superRegionId) throws ServiceException;

  /**
   * 通过region ID反过来获取地区
   */
  Map<String, String> findDataByRegionId(Long superRegionId) throws ServiceException;

  /**
   * 获取下一级是否存在
   * 
   * @param superRegionId
   * @return
   * @throws ServiceException
   */
  Map<String, String> findNextLevelRegionId(Long superRegionId) throws ServiceException;

  void autoRegionPrompt(PersonProfileForm form) throws Exception;

  List<Map<String, String>> findRegionList(Long superRegionId) throws Exception;

}
