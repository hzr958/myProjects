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
import com.smate.web.v8pub.service.sns.homepage.RepresentPubService;

/**
 * 删除人员代表性成果
 * 
 * @author YJ
 *
 *         2019年1月12日
 */

@Transactional(rollbackFor = Exception.class)
public class ASPsnRepresentPubDeleteImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private RepresentPubService representPubService;

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
      if (NumberUtils.isNotNullOrZero(pub.psnId) && NumberUtils.isNotNullOrZero(pub.pubId)) {
        representPubService.deleteByPsnIdAndPubId(pub.pubId, pub.psnId);
      }
    } catch (Exception e) {
      logger.error("删除人员代表性成果出错！pubId={},psnId={}", pub.pubId, pub.psnId, e);
      throw new PubHandlerAssemblyException(e);
    }
    return null;
  }

}
