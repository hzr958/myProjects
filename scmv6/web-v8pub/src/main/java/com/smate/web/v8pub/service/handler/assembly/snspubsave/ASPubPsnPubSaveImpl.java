package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.PsnPubPO;
import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;

/**
 * 个人与sns成果保存/更新
 * 
 * @author YJ
 *
 *         2018年7月24日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubPsnPubSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnPubService psnPubService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubGenre != PubGenreConstants.PSN_PUB) {
      return;
    }
    if (NumberUtils.isNullOrZero(pub.psnId)) {
      String error = this.getClass().getSimpleName() + "保存或更新个人成果信息中psnId为空，psnId=" + pub.psnId;
      logger.error(error);
      throw new PubHandlerAssemblyException(error);
    }
  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubGenre == PubGenreConstants.GROUP_PUB) {
      return null;
    }
    PsnPubPO psnPubPO = null;
    try {
      psnPubPO = psnPubService.getPsnPub(pub.pubId, pub.psnId);
      if (psnPubPO == null) {
        psnPubPO = new PsnPubPO();
        psnPubPO.setPubId(pub.pubId);
        psnPubPO.setOwnerPsnId(pub.psnId);
        psnPubPO.setGmtCreate(new Date());
        psnPubPO.setStatus(0);
      }
      psnPubPO.setGmtModified(new Date());
      psnPubService.savePsnPub(psnPubPO);
      logger.debug("更新或保存成果与个人关系记录成功");
    } catch (Exception e) {
      logger.error("成果与个人关系服务：更新或保存个人与成果关系对象出错！", psnPubPO, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns库个人与成果关系出错!", e);
    }
    return null;
  }

}
