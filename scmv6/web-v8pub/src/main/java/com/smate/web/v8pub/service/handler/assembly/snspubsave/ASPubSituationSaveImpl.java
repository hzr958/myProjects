package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubSituationPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubSituationService;

/**
 * 个人库成果收录情况保存/更新
 * 
 * @author yhx
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubSituationSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSituationService pubSituationService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      if (pub.situations == null) {
        return null;
      }
      PubSituationPO pubSituationPO = null;
      List<PubSituationDTO> sitList =
          JacksonUtils.jsonToCollection(pub.situations.toJSONString(), List.class, PubSituationDTO.class);
      // 先删除所有
      pubSituationService.deleteAll(pub.pubId);
      if (sitList != null && sitList.size() > 0) {
        for (PubSituationDTO p : sitList) {
          String libraryName = p.getLibraryName();
          if (StringUtils.isBlank(libraryName)) {
            continue;
          }
          pubSituationPO = new PubSituationPO();
          if (NumberUtils.isNullOrZero(pubSituationPO.getPubId())) {
            pubSituationPO.setPubId(pub.pubId);
          }
          pubSituationPO.setLibraryName(libraryName);
          pubSituationPO.setSitStatus(p.isSitStatus() ? 1 : 0);
          pubSituationPO.setSitOriginStatus(p.isSitOriginStatus() ? 1 : 0);
          pubSituationPO.setSrcDbId(p.getSrcDbId());
          pubSituationPO.setSrcId(p.getSrcId());
          pubSituationPO.setSrcUrl(p.getSrcUrl());
          pubSituationPO.setGmtCreate(new Date());
          pubSituationPO.setGmtModified(new Date());
          pubSituationService.save(pubSituationPO);
        }
      }
      logger.debug("更新或保存成果引用情况记录成功");
    } catch (Exception e) {
      logger.error("更新sns库成果收录表出错！situations={}", pub.situations, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns库收录情况出错!", e);
    }
    return null;
  }

}
