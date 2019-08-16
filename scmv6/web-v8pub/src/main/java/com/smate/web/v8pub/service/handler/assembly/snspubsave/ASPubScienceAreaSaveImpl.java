package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dto.ScienceAreaDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubScienceAreaPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubScienceAreaService;

@Transactional(rollbackFor = Exception.class)
public class ASPubScienceAreaSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubScienceAreaService pubScienceAreaService;

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
      if (pub.scienceAreas == null) {
        return null;
      }
      List<ScienceAreaDTO> scienceAreaList =
          JacksonUtils.jsonToCollection(pub.scienceAreas.toJSONString(), List.class, ScienceAreaDTO.class);
      PubScienceAreaPO pubScienceAreaPO = null;
      // 删除之前所有的科技领域
      pubScienceAreaService.deleteById(pub.pubId);
      if (scienceAreaList != null && scienceAreaList.size() > 0) {
        for (ScienceAreaDTO p : scienceAreaList) {
          if (NumberUtils.isNullOrZero(p.getScienceAreaId())) {
            continue;
          }
          pubScienceAreaPO = new PubScienceAreaPO();
          if (NumberUtils.isNullOrZero(p.getPubId())) {
            pubScienceAreaPO.setPubId(pub.pubId);
          }
          pubScienceAreaPO.setScienceAreaId(p.getScienceAreaId());
          pubScienceAreaService.save(pubScienceAreaPO);
        }
      }
    } catch (Exception e) {
      logger.error("保存sns库科技领域出错！", pub.scienceAreas, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns库成果科技领域出错!", e);
    }
    return null;
  }

}
