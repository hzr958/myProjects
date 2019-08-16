package com.smate.core.base.consts.service;

import com.smate.core.base.exception.ServiceException;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

/**
 * 学科领域常量服务类
 * 
 * @author houchuanjie
 * @date 2018年3月19日 下午5:33:27
 */
public interface ConstDisciplineService {
  /**
   * 获取学科领域名称，有可能为空
   * 
   * @param id
   * @param locale
   * @return
   * @throws ServiceException
   */
  Optional<String> getDisciplineName(Long id, Locale locale) throws ServiceException;

  /**
   * 
   *
   * @author houchuanjie
   * @date 2018年3月21日 下午2:29:09
   * @param discCode
   * @return
   * @throws ServiceException
   */
  String findDiscJsonData(String discCode) throws ServiceException;
  Map<String,List<Map<String, String>>> findDiscData(Integer disciplineId) throws ServiceException;
  Integer  dealDisciplineId(Integer disciplineId) throws ServiceException;
}
