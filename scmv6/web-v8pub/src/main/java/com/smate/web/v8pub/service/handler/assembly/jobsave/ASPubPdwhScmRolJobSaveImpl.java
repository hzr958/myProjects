package com.smate.web.v8pub.service.handler.assembly.jobsave;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.job.PubPdwhScmRol;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.job.PubPdwhScmRolService;

@Transactional(rollbackFor = Exception.class)
public class ASPubPdwhScmRolJobSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhScmRolService pubPdwhScmRolService;

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
      PubPdwhScmRol pubPdwhScmRol = pubPdwhScmRolService.get(pub.pubId);
      if (pubPdwhScmRol == null) {
        pubPdwhScmRol = new PubPdwhScmRol(pub.pubId, 1, 0, 0);
      } else {// 成果更新需要重新匹配基准库成果
        pubPdwhScmRol.setStatus(0);
        pubPdwhScmRol.setMatchCounts(0);
      }
      pubPdwhScmRolService.saveOrUpdate(pubPdwhScmRol);
    } catch (Exception e) {
      logger.error("保存ScmPubId匹配PdwhPubId任务记录出错！", e);
    }
    return null;
  }

}
