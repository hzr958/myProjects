package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubDuplicatePO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubDuplicateService;

/**
 * 个人库成果查重记录更新/保存
 * 
 * @author YJ
 *
 *         2018年7月24日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubDuplicateSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubDuplicateService pubDuplicateService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    // 更新或保存成果查重记录表
    PubDuplicatePO pubDuplicatePO = pubDuplicateService.get(pub.pubId);
    try {
      if (pubDuplicatePO == null) {
        pubDuplicatePO = new PubDuplicatePO();
        pubDuplicatePO.setPubId(pub.pubId);
        pubDuplicatePO.setGmtCreate(new Date());
      }
      pubDuplicatePO.setHashTPP(pub.hashTPP);
      pubDuplicatePO.setHashTP(pub.hashTP);
      pubDuplicatePO.setHashTitle(pub.hashTitle);
      pubDuplicatePO.setHashDoi(pub.hashDoi);
      pubDuplicatePO.setHashCleanDoi(pub.hashCleanDoi);
      pubDuplicatePO.setHashCnkiDoi(pub.hashCnkiDoi);
      pubDuplicatePO.setHashCleanCnkiDoi(pub.hashCleanCnkiDoi);
      pubDuplicatePO.setHashEiSourceId(pub.hashEiSourceId);
      pubDuplicatePO.setHashIsiSourceId(pub.hashIsiSourceId);
      pubDuplicatePO.setHashApplicationNo(pub.hashApplicationNo);
      pubDuplicatePO.setHashPublicationOpenNo(pub.hashPublicationOpenNo);
      pubDuplicatePO.setHashStandardNo(pub.hashStandardNo);
      pubDuplicatePO.setHashRegisterNo(pub.hashRegisterNo);
      pubDuplicatePO.setDetailsHash(pub.detailsHash);
      pubDuplicatePO.setGmtModified(new Date());
      pubDuplicateService.saveOrUpdate(pubDuplicatePO);
    } catch (Exception e) {
      logger.error("更新sns库成果查重记录表出错！pubDuplicatePO={}", pubDuplicatePO, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns库查重记录出错!", e);
    }

    return null;
  }


}
