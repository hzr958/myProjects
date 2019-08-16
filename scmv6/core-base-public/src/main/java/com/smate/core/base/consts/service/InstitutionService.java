package com.smate.core.base.consts.service;

import java.util.List;
import java.util.Optional;

import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.exception.ServiceException;

/**
 * 单位服务接口
 *
 * @author houchuanjie
 * @date 2018年3月22日 下午3:23:19
 */
public interface InstitutionService {

  String getInstitution(String startWith, String excludes, int size) throws ServiceException;

  /**
   * 根据单位名称查找单位实体，忽略中英文和大小写
   *
   * @author houchuanjie
   * @date 2018年3月22日 下午3:25:35
   * @param insName
   * @return
   */
  Optional<Institution> findByName(String insName);

  /**
   * 通过关键字搜索查询单位，限制结果条数，并根据excludes对结果排除过滤。如果没有查询到结果，则返回空的list（Collections.emptyList()）
   *
   * @author houchuanjie
   * @date 2018年3月26日 上午11:45:15
   * @param keyword 关键字
   * @param maxSize 最大条数
   * @param excludes 排除字段
   * @return
   * @throws ServiceException
   */
  List<Institution> searchIns(String keyword, Integer maxSize, String excludes) throws ServiceException;
}
