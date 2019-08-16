package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.file.model.ArchiveFile;
import com.smate.core.base.file.service.ArchiveFileService;
import com.smate.core.base.pub.util.DisposeDes3IdUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dao.sns.PubFulltextPsnRcmdDao;
import com.smate.web.v8pub.dto.PubFulltextDTO;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.PubFulltextUploadLog;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.publics.PubfulltextUploadService;

/**
 * 添加上传全文记录，做全文推荐用
 * 
 * @author Administrator
 *
 */
@Transactional(rollbackFor = Exception.class)
public class AsPubFulltextUploadLogSaveImpl implements PubHandlerAssemblyService {
  @Autowired
  private PubfulltextUploadService pubfulltextUploadService;
  @Autowired
  private ArchiveFileService archiveFileService;
  @Autowired
  private PubFulltextPsnRcmdDao pubFulltextPsnRcmdDao;

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
    if (pub.fullText == null) {
      return null;
    }
    PubFulltextDTO pDTO = JacksonUtils.jsonObject(pub.fullText.toJSONString(), PubFulltextDTO.class);
    if (pDTO == null) {
      return null;
    }
    Long psnId = DisposeDes3IdUtils.disposeDes3Id(pub.psnId, pub.des3PsnId);
    if (NumberUtils.isNullOrZero(psnId)) {
      return null;
    }
    // 先判断全文是否已删除
    PubFulltextUploadLog pubFulltextUploadLog = pubfulltextUploadService.getSnsUploadLog(pub.pubId);
    if (pub.fulltextId != null) {// 上传全文或者更新全文
      // 先删除推荐给这条成果的推荐记录
      pubFulltextPsnRcmdDao.deletePubFulltextPsnRcmd(pub.pubId);
      Long fileId = DisposeDes3IdUtils.disposeDes3Id(null, pDTO.getDes3fileId());
      if (NumberUtils.isNullOrZero(fileId)) {
        return null;
      }
      ArchiveFile archiveFile = archiveFileService.getArchiveFileById(fileId);
      if (archiveFile != null) {
        if (pubFulltextUploadLog != null) {// 更新全文
          pubFulltextUploadLog.setFulltextFileId(archiveFile.getFileId());
          pubFulltextUploadLog.setGmtCreate(new Date());
          pubFulltextUploadLog.setUploadPsnId(psnId);
          pubFulltextUploadLog.setStatus(0);
          pubFulltextUploadLog.setPdwhPubToImage(0);
          pubFulltextUploadLog.setSnsPubToImage(0);
          if (pub.permission != null && pub.permission == 4) {
            pubFulltextUploadLog.setIsPrivacy(2);
          } else {
            pubFulltextUploadLog.setIsPrivacy(pDTO.getPermission());
          }
          pubFulltextUploadLog.setIsDelete(0);
          pubfulltextUploadService.saveOrUpdate(pubFulltextUploadLog);
        } else {// 上传全文
          pubFulltextUploadLog = new PubFulltextUploadLog(psnId, archiveFile.getFileId(), 0, new Date(), 0, 0);
          pubFulltextUploadLog.setSnsPubId(pub.pubId);
          if (pub.permission != null && pub.permission == 4) {
            pubFulltextUploadLog.setIsPrivacy(2);
          } else {
            pubFulltextUploadLog.setIsPrivacy(pDTO.getPermission());
          }

          pubfulltextUploadService.saveOrUpdate(pubFulltextUploadLog);
        }
      }
    } else {// 删除全文
      if (pubFulltextUploadLog != null) {
        pubFulltextUploadLog.setGmtCreate(new Date());
        pubFulltextUploadLog.setIsDelete(1);
        pubFulltextUploadLog.setStatus(0);
        pubFulltextUploadLog.setPdwhPubToImage(0);
        pubFulltextUploadLog.setSnsPubToImage(0);
        pubfulltextUploadService.saveOrUpdate(pubFulltextUploadLog);
      }
    }

    return null;
  }

}
