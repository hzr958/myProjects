package com.smate.web.v8pub.service.handler.assembly.jobsave;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.smate.web.v8pub.enums.PubHandlerEnum;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.backups.PubDataBackups;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubDataBackupsService;

@Transactional(rollbackFor = Exception.class)
public class ASPubBackupsJobSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubDataBackupsService pubDataBackupsService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      PubDataBackups pubDataBackups = pubDataBackupsService.get(pub.pubId);
      if (pubDataBackups == null) {
        pubDataBackups = new PubDataBackups();
        pubDataBackups.setPubId(pub.pubId);
      }
      // 基准库
      if (pub.pubHandlerName.equalsIgnoreCase(PubHandlerEnum.SAVE_PDWH.getName())
          || pub.pubHandlerName.equalsIgnoreCase(PubHandlerEnum.UPDATE_PDWH.getName())) {
        pubDataBackups.setDataType(1);

      }
      // 个人库
      if (pub.pubHandlerName.equalsIgnoreCase(PubHandlerEnum.SAVE_SNS.getName())
          || pub.pubHandlerName.equalsIgnoreCase(PubHandlerEnum.UPDATE_SNS.getName())
          || pub.pubHandlerName.equalsIgnoreCase(PubHandlerEnum.PSNPUB_TO_SNS.getName())) {
        pubDataBackups.setDataType(0);
      }
      pubDataBackupsService.saveOrUpdate(pubDataBackups);
    } catch (Exception e) {
      logger.error("保存备份数据任务表记录出错！", e);
    }
    return null;
  }

}
