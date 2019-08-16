package com.smate.web.v8pub.service.handler.assembly.construct;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dto.PubSituationDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;

/**
 * 重新构造引用次数
 * 
 * @author YJ
 *
 *         2018年10月25日
 */

@Transactional(rollbackFor = Exception.class)
public class ASConstructPubCitationsImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

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
      if (pub.pubType == PublicationTypeEnum.JOURNAL_ARTICLE || pub.pubType == PublicationTypeEnum.CONFERENCE_PAPER) {
        // 期刊论文，会议论文需要根据收录情况进行重构引用次数
        // ISTP，SSCI，SCI 的引用次数才保存
        if (!isISI(pub.situations)) {
          pub.citations = 0;
        }
      }
    } catch (Exception e) {
      logger.error("重构成果引用次数出错！", e);
      throw new PubHandlerAssemblyException(e);
    }
    return null;
  }

  @SuppressWarnings("unchecked")
  private boolean isISI(JSONArray situations) {
    if (situations != null && situations.size() > 0) {
      List<PubSituationDTO> sitList =
          JacksonUtils.jsonToCollection(situations.toJSONString(), List.class, PubSituationDTO.class);
      if (sitList != null && sitList.size() > 0) {
        for (PubSituationDTO p : sitList) {
          Integer dbId = PubParamUtils.buildDbId(p.getLibraryName());
          if (dbId != null && (dbId == 15 || dbId == 16 || dbId == 17)) {
            return true;
          }
        }
      }
    }
    return false;
  }

}
