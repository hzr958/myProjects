package com.smate.web.v8pub.service.handler.assembly.dispose;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.util.DisposeDes3IdUtils;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;

/**
 * 成果参数解密处理 对pubId,grpId,psnId,fileId等id值进行解密的处理
 * 
 * @author YJ
 *
 *         2018年8月10日
 */
@Transactional(rollbackFor = Exception.class)
public class ASDisposePubDecodeIDImpl implements PubHandlerAssemblyService {

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
    pub.pubId = DisposeDes3IdUtils.disposeDes3Id(pub.pubId, pub.des3PubId);
    pub.pdwhPubId = DisposeDes3IdUtils.disposeDes3Id(pub.pdwhPubId, pub.des3PdwhPubId);
    pub.grpId = DisposeDes3IdUtils.disposeDes3Id(pub.grpId, pub.des3GrpId);
    pub.psnId = DisposeDes3IdUtils.disposeDes3Id(pub.psnId, pub.des3PsnId);
    pub.ownerPsnId = DisposeDes3IdUtils.disposeDes3Id(pub.ownerPsnId, pub.des3OwnerPsnId);
    return null;
  }

}
