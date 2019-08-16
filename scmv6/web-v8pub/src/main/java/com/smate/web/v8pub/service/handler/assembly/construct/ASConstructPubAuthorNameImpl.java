package com.smate.web.v8pub.service.handler.assembly.construct;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.utils.PubBuildAuthorNamesUtils;

/**
 * 成果作者名的构建
 * 
 * @author YJ
 *
 *         2018年8月1日
 */
@Transactional(rollbackFor = Exception.class)
public class ASConstructPubAuthorNameImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

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
    String authorNames = "";
    try {
      if (pub.members != null && pub.members.size() > 0) {
        List<PubMemberDTO> memberList =
            JacksonUtils.jsonToCollection(pub.members.toJSONString(), List.class, PubMemberDTO.class);
        if (memberList != null && memberList.size() > 0) {
          authorNames = PubBuildAuthorNamesUtils.buildSnsPubAuthorNames(memberList);
        }
      }
      pub.authorNames = authorNames;
    } catch (Exception e) {
      logger.error("ASConstructPubAuthorNameImpl构建authorNames参数出错！", e);
      throw new PubHandlerAssemblyException("ASConstructPubAuthorNameImpl构建authorNames参数出错！", e);
    }
    return null;
  }

}
