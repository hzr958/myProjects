package com.smate.web.v8pub.service.handler.assembly.snspubdelete;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.pub.enums.PubSnsStatusEnum;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.PubSnsService;

/**
 * 更新sns库成果status字段的状态为删除状态 <br/>
 * 更新的规则：<br/>
 * 1.个人与成果建立了关系，且此条成果未与群组建立关系时，个人删除这条成果，主表状态位也置为删除状态<br/>
 * 2.群组与成果建立了关系，且此条成果未与人员建立关系时，群组删除这条成果，主表状态位也置为删除状态<br/>
 * 3.个人与成果建立了关系（未删除状态），且这条成果与群组也建立了关系（未删除状态）时，个人或者群组删除这条成果，主表状态不置为删除状态<br/>
 * 4.个人与成果建立了关系，且这条成果与群组也建立了关系（已删除状态）时，个人删除这条成果，主表状态位置为删除状态<br/>
 * 5.群组与成果建立了关系，且这条成果与个人也建立了关系（已删除状态）时，群组删除这条成果，主表状态位置为删除状态<br/>
 * 
 * @author YJ
 *
 *         2018年9月17日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubSnsDeleteImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private PsnPubService psnPubService;
  @Autowired
  private GroupPubService groupPubService;

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
      PubSnsPO pubSnsPO = pubSnsService.get(pub.pubId);
      if (pubSnsPO != null) {
        Integer psnPubStatus = psnPubService.getStatus(pub.pubId);
        Integer grpPubStatus = groupPubService.getStatus(pub.pubId);
        if (psnPubStatus == null && grpPubStatus == null) {
          pubSnsPO.setStatus(PubSnsStatusEnum.DELETED);
          pubSnsPO.setGmtModified(new Date());
        } else {
          pubSnsPO.setStatus(PubSnsStatusEnum.DEFAULT);
          pubSnsPO.setGmtModified(new Date());
        }
        pubSnsService.update(pubSnsPO);
      }
    } catch (Exception e) {
      logger.error("更新sns库主表的status字段出错！", e);
      throw new PubHandlerAssemblyException(e);
    }
    return null;
  }

}
