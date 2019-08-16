package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.group.GrpPubIndexUrlPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.group.GrpPubIndexUrlService;

/**
 * 群组成果短地址保存
 * 
 * @author YJ
 *
 *         2018年9月28日
 */
@Transactional(rollbackFor = Exception.class)
public class ASGroupPubIndexSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private GrpPubIndexUrlService grpPubIndexUrlService;

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
      if (pub.pubGenre == PubGenreConstants.GROUP_PUB) {
        GrpPubIndexUrlPO grpPubIndexUrlPO = grpPubIndexUrlService.get(pub.pubId, pub.grpId);
        if (grpPubIndexUrlPO == null) {
          grpPubIndexUrlPO = new GrpPubIndexUrlPO();
          grpPubIndexUrlPO.setGrpId(pub.grpId);
          grpPubIndexUrlPO.setPubId(pub.pubId);
          grpPubIndexUrlPO.setPsnId(pub.psnId);
        }
        grpPubIndexUrlPO.setPubIndexUrl(pub.pubIndexUrl);
        grpPubIndexUrlPO.setUpdateDate(new Date());
        grpPubIndexUrlService.saveOrUpdate(grpPubIndexUrlPO);
      }
    } catch (Exception e) {
      logger.error("保存群组成果短地址表出错！", e);
      throw new PubHandlerAssemblyException(e);
    }
    return null;
  }

}
