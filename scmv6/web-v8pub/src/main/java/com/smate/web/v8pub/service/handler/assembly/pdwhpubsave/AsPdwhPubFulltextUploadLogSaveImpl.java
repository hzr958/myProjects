package com.smate.web.v8pub.service.handler.assembly.pdwhpubsave;

import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.smate.core.base.pub.util.DisposeDes3IdUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.PubFulltextUploadLog;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.publics.PubfulltextUploadService;

@Transactional(rollbackFor = Exception.class)
public class AsPdwhPubFulltextUploadLogSaveImpl implements PubHandlerAssemblyService {
  @Autowired
  private PubfulltextUploadService pubfulltextUploadService;

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
    if (4 != pub.srcDbId || 21 != pub.srcDbId) {// cnki 不推荐全文
      PubFulltextDTO pDTO = JacksonUtils.jsonObject(pub.fullText.toJSONString(), PubFulltextDTO.class);
      if (pDTO == null) {
        return null;
      }
      Long field = DisposeDes3IdUtils.disposeDes3Id(null, pDTO.getDes3fileId());
      if (NumberUtils.isNullOrZero(field)) {
        return null;
      }
      PubFulltextUploadLog pubFulltextUploadLog = pubfulltextUploadService.getUploadLog(pub.pubId);
      if (pubFulltextUploadLog != null) {
        pubFulltextUploadLog.setFulltextFileId(field);
        pubFulltextUploadLog.setGmtCreate(new Date());
        pubFulltextUploadLog.setStatus(0);
        pubFulltextUploadLog.setPdwhPubToImage(0);
        pubFulltextUploadLog.setSnsPubToImage(0);
      } else {
        pubFulltextUploadLog = new PubFulltextUploadLog(9999999999999L, field, 0, new Date(), 0, 0);// 后台导入的全文的psnid统一9999999999999
        pubFulltextUploadLog.setPdwhPubId(pub.pubId);
      }
      pubfulltextUploadService.saveOrUpdate(pubFulltextUploadLog);
    }
    return null;
  }

}
