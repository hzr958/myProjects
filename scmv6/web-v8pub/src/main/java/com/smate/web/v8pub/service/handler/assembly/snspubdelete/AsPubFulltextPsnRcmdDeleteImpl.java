package com.smate.web.v8pub.service.handler.assembly.snspubdelete;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.fulltextpsnrcmd.PubFulltextPsnRcmdService;

/**
 * 个人库成果删除，会进行删除掉与之关联的成果全文推荐记录
 * 
 * @author YJ
 *
 *         2019年3月22日
 */

@Transactional(rollbackFor = Exception.class)
public class AsPubFulltextPsnRcmdDeleteImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubFulltextPsnRcmdService pubFulltextPsnRcmdService;

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
      if (NumberUtils.isNotNullOrZero(pub.pubId)) {
        pubFulltextPsnRcmdService.deleteBySnsPubId(pub.pubId);
      }
    } catch (Exception e) {
      logger.error("删除个人库与之关联的成果全文记录出错！srcPubId={}", pub.pubId, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "删除个人库与之关联的成果全文记录出错!", e);
    }
    return null;
  }

}
