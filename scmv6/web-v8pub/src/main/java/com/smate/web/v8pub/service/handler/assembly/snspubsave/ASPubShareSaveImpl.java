package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.util.DisposeDes3IdUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dao.pdwh.PdwhPubShareDAO;
import com.smate.web.v8pub.dao.sns.PubShareDAO;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.pdwh.PdwhPubSharePO;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.PubSharePO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;

/**
 * 基准库导入到sns库的成果 分享记录同步到sns库
 * 
 * @author YHX
 *
 *         2019年3月15日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubShareSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhService pubPdwhService;
  @Autowired
  private PdwhPubShareDAO pdwhPubShareDAO;
  @Autowired
  private PubShareDAO pubShareDAO;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubGenre != PubGenreConstants.PDWH_SNS_PUB) {
      return;
    }
    Long pdwhPubId = DisposeDes3IdUtils.disposeDes3Id(pub.pdwhPubId, pub.des3PdwhPubId);
    if (NumberUtils.isNullOrZero(pdwhPubId)) {
      String error = this.getClass().getSimpleName() + "基准库导入个人库时，pdwhPubId不能为空，pdwhPubId=" + pub.pdwhPubId;
      logger.error(error);
      throw new PubHandlerAssemblyException(error);
    }
    PubPdwhPO pubPdwhPO = pubPdwhService.get(pdwhPubId);
    if (pubPdwhPO == null) {
      String error = this.getClass().getSimpleName() + "基准库导入个人库时，pdwhPubId无效，pdwhPubId=" + pub.pdwhPubId;
      logger.error(error);
      throw new PubHandlerAssemblyException(error);
    }

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    if (pub.pubGenre != PubGenreConstants.PDWH_SNS_PUB) {
      return null;
    }
    PubSharePO pubSharePO = null;
    try {
      if (!NumberUtils.isNullOrZero(pub.pdwhPubId)) {
        List<PdwhPubSharePO> list = pdwhPubShareDAO.findByPdwhPubId(pub.pdwhPubId);
        if (list != null) {
          for (PdwhPubSharePO pdwhPubSharePO : list) {
            pubSharePO = pubShareDAO.findShare(pub.pubId, pdwhPubSharePO.getPsnId(), pdwhPubSharePO.getPlatform(),
                pdwhPubSharePO.getGmtCreate());
            if (pubSharePO == null) {
              pubSharePO = new PubSharePO();
              pubSharePO.setPubId(pub.pubId);
              pubSharePO.setPsnId(pdwhPubSharePO.getPsnId());
              pubSharePO.setComment(pdwhPubSharePO.getComment());
              pubSharePO.setPlatform(pdwhPubSharePO.getPlatform());
              pubSharePO.setStatus(pdwhPubSharePO.getStatus());
              pubSharePO.setSharePsnGroupId(pdwhPubSharePO.getSharePsnGroupId());
              pubSharePO.setGmtCreate(pdwhPubSharePO.getGmtCreate());
            }
            pubSharePO.setGmtModified(pdwhPubSharePO.getGmtModified());
            pubShareDAO.saveOrUpdate(pubSharePO);
          }
        }
        Long shareCount = pdwhPubShareDAO.getShareCount(pub.pdwhPubId);
        pub.shareCount = (int) (shareCount == null ? 0 : shareCount);
      }
    } catch (Exception e) {
      logger.error("基准库导入到sns库的成果,分享表记录同步到sns库异常！PubSharePO={}", pubSharePO, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库导入到sns库的成果,分享记录同步到sns库异常", e);
    }
    return null;
  }

}
