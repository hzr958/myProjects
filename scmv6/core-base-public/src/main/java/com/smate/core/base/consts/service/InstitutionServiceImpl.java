package com.smate.core.base.consts.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.consts.dao.InstitutionDao;
import com.smate.core.base.consts.model.Institution;
import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 单位服务实现类
 *
 * @author houchuanjie
 * @date 2018年3月22日 下午3:22:57
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InstitutionServiceImpl implements InstitutionService {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private InstitutionDao institutionDao;

  @Override
  public String getInstitution(String startWith, String excludes, int size) throws ServiceException {
    try {
      List<Institution> list = null;
      list = institutionDao.getInstitution(startWith, excludes, size);
      return JacksonUtils.listToJsonStr(list);
    } catch (Exception e) {
      logger.error("getInstitution获取智能匹配单位列表，只读size条记录startWith:" + startWith, e);
      throw new ServiceException();
    }
  }

  @Override
  public Optional<Institution> findByName(String insName) {
    return Optional.ofNullable(institutionDao.findByName(StringUtils.trimToEmpty(insName)));
  }

  @Override
  public List<Institution> searchIns(String keyword, Integer maxSize, String excludes) throws ServiceException {
    List<Institution> list;
    try {
      list = institutionDao.getInstitution(keyword, excludes, maxSize);
    } catch (Exception e) {
      logger.error("查询单位列表出错！keyword={}, maxSize={}, excludes={}", keyword, maxSize, excludes);
      throw new ServiceException(e);
    }
    return Optional.ofNullable(list).orElseGet(Collections::emptyList);
  }
}
