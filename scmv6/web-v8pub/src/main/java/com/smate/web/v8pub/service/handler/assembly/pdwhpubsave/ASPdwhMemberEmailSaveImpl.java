package com.smate.web.v8pub.service.handler.assembly.pdwhpubsave;

import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.pdwh.PdwhMemberEmailPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PdwhMemberEmailService;

@Transactional(rollbackFor = Exception.class)
public class ASPdwhMemberEmailSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhMemberEmailService pdwhMemberEmailService;

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
      pdwhMemberEmailService.deleteAll(pub.pubId);
      if (CollectionUtils.isNotEmpty(pub.emailList)) {
        for (String email : pub.emailList) {
          if (StringUtils.isNotBlank(email)) {
            PdwhMemberEmailPO pdwhMemberEmailPO = new PdwhMemberEmailPO();
            pdwhMemberEmailPO.setPdwhPubId(pub.pubId);
            pdwhMemberEmailPO.setEmail(email);
            pdwhMemberEmailService.save(pdwhMemberEmailPO);
          }
        }
      }
    } catch (Exception e) {
      logger.error("保存基准库作者邮件信息出错！pdwhPubId={}", pub.pubId, e);
      throw new PubHandlerAssemblyException(e);
    }
    return null;
  }

}
