package com.smate.center.mail.service.build;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.mail.model.MailDataInfo;
import com.smate.center.mail.service.MailBuildInfoService;

/**
 * 统一保存数据
 * 
 * @author zzx
 *
 */
public class MailBuildInfoExcuteMainSaveDataServiceImpl implements MailBuildInfoExcuteService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private MailBuildInfoService mailBuildInfoService;

  @Override
  public void excute(MailDataInfo info) throws Exception {
    mailBuildInfoService.mainSaveData(info);
  }

}
