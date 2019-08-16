package com.smate.web.v8pub.service.handler.assembly.pdwhpubsave;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.pdwh.PdwhPubDuplicatePO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PdwhPubDuplicateService;

@Transactional(rollbackFor = Exception.class)
public class ASPdwhDuplicateSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhPubDuplicateService pdwhPubDuplicateService;

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
    PdwhPubDuplicatePO duplicatePO = pdwhPubDuplicateService.get(pub.pubId);
    try {
      if (duplicatePO == null) {
        duplicatePO = new PdwhPubDuplicatePO();
        duplicatePO.setPdwhPubId(pub.pubId);
      }
      duplicatePO.setHashTP(pub.hashTP);
      duplicatePO.setHashTPP(pub.hashTPP);
      duplicatePO.setHashTitle(pub.hashTitle);
      duplicatePO.setHashDoi(pub.hashDoi);
      duplicatePO.setHashCleanDoi(pub.hashCleanDoi);
      duplicatePO.setHashCnkiDoi(pub.hashCnkiDoi);
      duplicatePO.setHashCleanCnkiDoi(pub.hashCleanCnkiDoi);
      duplicatePO.setHashEiSourceId(pub.hashEiSourceId);
      duplicatePO.setHashIsiSourceId(pub.hashIsiSourceId);
      duplicatePO.setHashApplicationNo(pub.hashApplicationNo);
      duplicatePO.setHashPublicationOpenNo(pub.hashPublicationOpenNo);
      duplicatePO.setHashStandardNo(pub.hashStandardNo);
      duplicatePO.setHashRegisterNo(pub.hashRegisterNo);
      pdwhPubDuplicateService.saveOrUpdate(duplicatePO);
    } catch (Exception e) {
      logger.error("更新基准库成果查重记录表出错！duplicatePO={}", duplicatePO, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库更新或成果查重记录出错！", e);
    }
    return null;
  }
}
