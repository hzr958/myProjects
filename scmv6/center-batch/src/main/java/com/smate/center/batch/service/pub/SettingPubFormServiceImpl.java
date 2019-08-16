package com.smate.center.batch.service.pub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.sns.pub.SettingPubFormDao;
import com.smate.center.batch.model.sns.pub.SettingPubForm;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.exception.PubException;

/**
 * SettingPubFormServiceImp.
 * 
 * @author zk
 * 
 */
@Service("settingPubFormService")
@Transactional(rollbackFor = Exception.class)
public class SettingPubFormServiceImpl implements SettingPubFormService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SettingPubFormDao settingPubFormDao;

  /**
   * 传入单位ID获取单个SettingPubForm实体，每个单位只能配置一个设置.
   * 
   * @param id
   * @return
   * @throws PubException
   */
  public SettingPubForm getSettingPubFormByInsId(Long insId) throws PubException {

    try {

      SettingPubForm form = settingPubFormDao.getSettingPubFormByInsId(insId);
      // 如果未找到合适模板，则使用默认模板
      if (form == null) {
        return getSettingPubFormById(ServiceConstants.SCHOLAR_PUB_FORM_ID);
      }
      return form;

    } catch (Exception e) {
      logger.error("getSettingPubFormById获取单个SettingPubForm实体失败insId: " + insId, e);
      throw new PubException(e);
    }
  }

  /**
   * 传入ID获取单个SettingPubForm实体.
   * 
   * @param id
   * @return
   * @throws PubException
   */
  public SettingPubForm getSettingPubFormById(int id) throws PubException {

    return settingPubFormDao.get(id);
  }
}
