package com.smate.center.batch.service.pub;

import com.smate.center.batch.model.sns.pub.SettingPubForm;
import com.smate.core.base.utils.exception.PubException;

/**
 * SettingPubFormService.
 * 
 * @author zk
 * 
 */
public interface SettingPubFormService {

  /**
   * 传入单位ID获取单个SettingPubForm实体.
   * 
   * @param insId
   * @return
   * @throws PubException
   */
  SettingPubForm getSettingPubFormByInsId(Long insId) throws PubException;

  /**
   * 传入ID获取单个SettingPubForm实体.
   * 
   * @param id
   * @return
   * @throws PubException
   */
  SettingPubForm getSettingPubFormById(int id) throws PubException;
}
