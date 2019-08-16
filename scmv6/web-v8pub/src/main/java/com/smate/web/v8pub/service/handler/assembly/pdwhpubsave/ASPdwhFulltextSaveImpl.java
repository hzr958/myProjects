package com.smate.web.v8pub.service.handler.assembly.pdwhpubsave;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.pdwh.PdwhPubFullTextPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PdwhPubFullTextService;

/**
 * 基准库成果全文保存/更新
 * 
 * @author YJ
 *
 *         2018年7月20日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPdwhFulltextSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubFullTextService pdwhPubFullTextService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {}

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    // 保存成果全文
    try {
      PdwhPubFullTextPO pdwhPubFullTextPO = constructPdwhPubFullTextPO(pub);
      if (pdwhPubFullTextPO != null) {
        PdwhPubFullTextPO oldFulltext =
            pdwhPubFullTextService.getFullText(pdwhPubFullTextPO.getPdwhPubId(), pdwhPubFullTextPO.getFileId());
        if (oldFulltext != null) {
          oldFulltext.setFileName(pdwhPubFullTextPO.getFileName());
          oldFulltext.setPermission(pdwhPubFullTextPO.getPermission());
          oldFulltext.setThumbnailPath(pdwhPubFullTextPO.getThumbnailPath());
          oldFulltext.setSourceFulltextUrl(pub.srcFulltextUrl);
          oldFulltext.setGmtModified(new Date());
          pdwhPubFullTextService.update(oldFulltext);
          pub.fulltextId = oldFulltext.getId();
        } else {
          pdwhPubFullTextPO.setSourceFulltextUrl(pub.srcFulltextUrl);
          pdwhPubFullTextPO.setGmtCreate(new Date());
          pdwhPubFullTextPO.setGmtModified(new Date());
          pdwhPubFullTextService.save(pdwhPubFullTextPO);
          pub.fulltextId = pdwhPubFullTextPO.getId();
        }
      }
    } catch (Exception e) {
      logger.error("保存pdwh库成果全文出错！FullText={}", pub.fullText.toJSONString(), e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库更新或保存成果全文出错！", e);
    }

    return null;
  }

  private PdwhPubFullTextPO constructPdwhPubFullTextPO(PubDTO pub) {
    if (null == pub.fullText) {
      return null;
    }
    PubFulltextDTO pDTO = JacksonUtils.jsonObject(pub.fullText.toJSONString(), PubFulltextDTO.class);
    if (pDTO == null) {
      return null;
    }
    PdwhPubFullTextPO p = new PdwhPubFullTextPO();
    Long fileId = null;
    String des3FileId = pDTO.getDes3fileId();
    if (StringUtils.isEmpty(des3FileId)) {
      return null;
    }
    if (!StringUtils.isNumeric(des3FileId)) {
      fileId = Long.valueOf(Des3Utils.decodeFromDes3(des3FileId));
    } else {
      fileId = Long.valueOf(des3FileId);
    }
    if (NumberUtils.isNullOrZero(p.getPdwhPubId())) {
      p.setPdwhPubId(pub.pubId);
    }
    p.setFileId(fileId);
    p.setFileName(pDTO.getFileName());
    p.setPermission(pDTO.getPermission());
    return p;
  }

}
