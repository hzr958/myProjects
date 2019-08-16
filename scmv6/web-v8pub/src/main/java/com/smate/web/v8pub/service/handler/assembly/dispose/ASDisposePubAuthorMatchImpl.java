package com.smate.web.v8pub.service.handler.assembly.dispose;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.smate.core.base.pub.enums.PubSnsRecordFromEnum;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.v8pub.dto.PubMemberDTO;
import com.smate.web.v8pub.enums.PubHandlerEnum;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.match.PubAuthorMatchService;

/**
 * 基准库成果导入至个人库时，对authorNames进行拆分匹配
 * 
 * @author YJ
 *
 *         2018年10月16日
 */
@Transactional(rollbackFor = Exception.class)
public class ASDisposePubAuthorMatchImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubAuthorMatchService pubAuthorMatchService;

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
      if (isRunAuthorMatch(pub)) {
        if (pub.members != null && pub.members.size() > 0) {
          List<PubMemberDTO> memberList =
              JacksonUtils.jsonToCollection(pub.members.toJSONString(), List.class, PubMemberDTO.class);
          pubAuthorMatchService.authorMatch(memberList, pub.psnId);
          pub.members = JSONArray.parseArray(JacksonUtils.jsonObjectSerializer(memberList));
        }
      }
    } catch (Exception e) {
      logger.error("基准库导入至个人库匹配成果作者出错！", e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库导入至个人库匹配成果作者出错！", e);
    }
    return null;
  }

  /**
   * 是否执行作者名拆分匹配逻辑
   * 
   * @param pub
   * @return
   */
  private boolean isRunAuthorMatch(PubDTO pub) {
    boolean recordType = (pub.recordFrom == PubSnsRecordFromEnum.IMPORT_FORM_FILE
        || pub.recordFrom == PubSnsRecordFromEnum.IMPORT_FROM_PDWH
        || pub.recordFrom == PubSnsRecordFromEnum.IMPORT_FROM_DATABASE);

    if (pub.pubHandlerName.equalsIgnoreCase(PubHandlerEnum.SAVE_SNS.getName())) {
      // 成果保存走这个逻辑
      return recordType;
    } else if (pub.pubHandlerName.equalsIgnoreCase(PubHandlerEnum.PSNPUB_TO_SNS.getName())) {
      // 成果保存中人员成果导入走这个逻辑
      return true;
    }
    return false;
  }

}
