package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubFullTextPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubFullTextService;

/**
 * 成果全文保存/更新
 * 
 * @author YJ
 *
 *         2018年7月20日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubFulltextSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubFullTextService pubFullTextService;
  @Autowired
  private PsnPubService psnPubService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    // 保存成果全文
    try {
      Long fulltextId = null;
      // 删除成果全文
      PubFullTextPO oldFulltext = pubFullTextService.findPubfulltext(pub.pubId);
      pubFullTextService.deleteByPubId(pub.pubId);
      PubFullTextPO pubFullTextPO = constructPubFullTextPO(pub);
      if (pubFullTextPO != null) {
        long oldFileId = (oldFulltext != null ? oldFulltext.getFileId() : 0);
        if (oldFileId == pubFullTextPO.getFileId()) {
          pubFullTextPO.setGmtCreate(oldFulltext.getGmtCreate());
        } else {
          pubFullTextPO.setGmtCreate(new Date());
        }
        pubFullTextPO.setGmtModified(new Date());
        pubFullTextService.save(pubFullTextPO);
        // 更新时间
        psnPubService.updatePubDate(pubFullTextPO.getPubId(), pubFullTextPO.getGmtModified());
        fulltextId = pubFullTextPO.getId();
      }
      pub.fulltextId = fulltextId;
    } catch (Exception e) {
      logger.error("保存sns库成果全文出错！FullText={}", pub.fullText, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns库成果全文出错!", e);
    }
    return null;
  }

  private PubFullTextPO constructPubFullTextPO(PubDTO pub) {
    if (null == pub.fullText) {
      return null;
    }
    // 处理全文中fileId为加密的情况
    PubFulltextDTO pubFulltextDTO = JacksonUtils.jsonObject(pub.fullText.toJSONString(), PubFulltextDTO.class);
    if (pubFulltextDTO == null) {
      return null;
    }
    PubFullTextPO pubFullTextPO = new PubFullTextPO();
    Long fileId = null;
    String des3FileId = pubFulltextDTO.getDes3fileId();
    if (StringUtils.isEmpty(des3FileId)) {
      return null;
    }
    if (!StringUtils.isNumeric(des3FileId)) {
      fileId = Long.valueOf(Des3Utils.decodeFromDes3(des3FileId));
    } else {
      fileId = Long.valueOf(des3FileId);
    }
    if (NumberUtils.isNullOrZero(pubFullTextPO.getPubId())) {
      pubFullTextPO.setPubId(pub.pubId);
    }
    pubFullTextPO.setFileId(fileId);
    pubFullTextPO.setFileName(pubFulltextDTO.getFileName());
    // SCM-21167解决群组成果认领时将全文信息导入到数据表中，出现无法显示缩略图的问题
    pubFullTextPO.setThumbnailPath(pubFulltextDTO.getThumbnailPath());
    // 成果是公开的，全文可以设置为公开或隐私。成果是隐私的，全文只能是隐私
    if (pub.permission != null && pub.permission == 4) {
      // 隐私
      pubFullTextPO.setPermission(2);
    } else {
      // permission只能是0和2，不是0就是2(0表示公开,2表示私有)
      pubFullTextPO.setPermission(NumberUtils.isNotNullOrZero(pubFulltextDTO.getPermission()) ? 2 : 0);
    }
    return pubFullTextPO;
  }

}
