package com.smate.web.v8pub.service.handler.assembly.pdwhpubsave;

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
import com.smate.web.v8pub.po.pdwh.PdwhPubSituationPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PdwhPubSituationService;

/**
 * 基准库成果收录情况保存/更新
 * 
 * @author yhx
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ASPdwhSituationSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubSituationService pdwhPubSituationService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

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
      PdwhPubSituationPO pdwhPubSituationPO = null;
      List<PubSituationDTO> sitList =
          JacksonUtils.jsonToCollection(pub.situations.toJSONString(), List.class, PubSituationDTO.class);
      // 先删除，后新增
      pdwhPubSituationService.deleteByPubId(pub.pubId);
      if (sitList != null && sitList.size() > 0) {
        for (PubSituationDTO p : sitList) {
          if (StringUtils.isEmpty(p.getLibraryName())) {
            continue;
          }
          pdwhPubSituationPO = new PdwhPubSituationPO();
          if (NumberUtils.isNullOrZero(pdwhPubSituationPO.getPdwhPubId())) {
            pdwhPubSituationPO.setPdwhPubId(pub.pubId);
          }
          pdwhPubSituationPO.setLibraryName(p.getLibraryName().toUpperCase());
          pdwhPubSituationPO.setSitStatus(p.isSitStatus() ? 1 : 0);
          pdwhPubSituationPO.setSitOriginStatus(p.isSitOriginStatus() ? 1 : 0);
          pdwhPubSituationPO.setSrcDbId(p.getSrcDbId());
          pdwhPubSituationPO.setSrcId(p.getSrcId());
          pdwhPubSituationPO.setSrcUrl(p.getSrcUrl());
          pdwhPubSituationPO.setGmtCreate(new Date());
          pdwhPubSituationPO.setGmtModified(new Date());
          pdwhPubSituationService.saveOrUpdate(pdwhPubSituationPO);
        }
      }
    } catch (Exception e) {
      logger.error("更新基准库成果收录表出错！situations={}", pub.situations.toJSONString(), e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库保存或更新成果收录情况记录出错！", e);
    }
    return null;
  }

}
