package com.smate.web.prj.service.project.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.web.prj.dao.project.SettingPrjFormDao;
import com.smate.web.prj.model.common.SettingPrjForm;
import com.smate.web.prj.service.project.SettingPrjFormService;

/**
 * SettingPrjFormServiceImp.
 * 
 * @author liqinghua
 * 
 */
@Service("settingPrjFormService")
@Transactional(rollbackFor = Exception.class)
public class SettingPrjFormServiceImpl implements SettingPrjFormService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SettingPrjFormDao settingPrjFormDao;

  /**
   * 传入单位ID获取单个SettingPrjForm实体，每个单位只能配置一个设置.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  public Optional<SettingPrjForm> getSettingPrjFormByInsId(Long insId) throws ServiceException {

    try {

      Optional<SettingPrjForm> optSpf = Optional.ofNullable(settingPrjFormDao.getByInsId(insId));

      // 如果未找到合适模板，则使用默认模板
      return Optional.ofNullable(optSpf.orElse(settingPrjFormDao.get(ServiceConstants.SCHOLAR_PRJ_FORM_ID)));

    } catch (Exception e) {
      logger.error("getSettingPrjFormById获取单个SettingPrjForm实体失败insId: " + insId, e);
      throw new ServiceException(e);
    }
  }

  /**
   * 传入ID获取单个SettingPrjForm实体.
   * 
   * @param id
   * @return
   * @throws ServiceException
   */
  public Optional<SettingPrjForm> getSettingPrjFormById(int id) throws ServiceException {

    return Optional.ofNullable(settingPrjFormDao.get(id));
  }

}
