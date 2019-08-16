package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.util.DisposeDes3IdUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dao.pdwh.PdwhPubLikeDAO;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.pdwh.PdwhPubLikePO;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.PubLikePO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;
import com.smate.web.v8pub.service.sns.PubLikeService;

/**
 * 基准库导入到sns库的成果 赞记录同步到sns库
 * 
 * @author YHX
 *
 *         2019年3月15日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubLikeSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhService pubPdwhService;
  @Autowired
  private PdwhPubLikeDAO pdwhPubLikeDAO;
  @Autowired
  private PubLikeService pubLikeService;

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
    PubLikePO pubLikePO = null;
    try {
      if (!NumberUtils.isNullOrZero(pub.pdwhPubId)) {
        List<PdwhPubLikePO> list = pdwhPubLikeDAO.findByPdwhPubId(pub.pdwhPubId);
        if (list != null) {
          for (PdwhPubLikePO pdwhPubLike : list) {
            pubLikePO = pubLikeService.findByPubIdAndPsnId(pub.pubId, pdwhPubLike.getPsnId());
            if (pubLikePO == null) {
              pubLikePO = new PubLikePO();
              pubLikePO.setPubId(pub.pubId);
              pubLikePO.setPsnId(pdwhPubLike.getPsnId());
            }
            pubLikePO.setStatus(pdwhPubLike.getStatus());
            pubLikePO.setGmtCreate(pdwhPubLike.getGmtCreate());
            pubLikePO.setGmtModified(pdwhPubLike.getGmtModified());
            pubLikeService.saveOrUpdate(pubLikePO);
          }
        }
        Long awardCount = pdwhPubLikeDAO.getLikeCountByPdwhPubId(pub.pdwhPubId);
        pub.awardCount = (int) (awardCount == null ? 0 : awardCount);
      }
    } catch (Exception e) {
      logger.error("基准库导入到sns库的成果,赞记录同步到sns库异常！PubLikePO={}", pubLikePO, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库导入到sns库的成果,赞记录同步到sns库异常", e);
    }
    return null;
  }

}
