package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.po.PubPdwhSnsRelationPO;
import com.smate.core.base.pub.util.DisposeDes3IdUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;
import com.smate.web.v8pub.service.sns.PubPdwhSnsRelationService;

/**
 * sns成果与pdwh成果关系 保存/更新
 * 
 * @author YJ
 *
 *         2018年7月24日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPdwhSnsRelationSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhSnsRelationService pubPdwhSnsRelationService;
  @Autowired
  private PubPdwhService pubPdwhService;

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
    PubPdwhSnsRelationPO pubPdwhSnsRelationPO = null;
    try {
      if (!NumberUtils.isNullOrZero(pub.pdwhPubId)) {
        pubPdwhSnsRelationPO = pubPdwhSnsRelationService.getByPubIdAndPdwhId(pub.pubId, pub.pdwhPubId);
        if (pubPdwhSnsRelationPO == null) {
          pubPdwhSnsRelationPO = new PubPdwhSnsRelationPO();
          pubPdwhSnsRelationPO.setPdwhPubId(pub.pdwhPubId);
          pubPdwhSnsRelationPO.setSnsPubId(pub.pubId);
          pubPdwhSnsRelationPO.setCreateDate(new Date());
          pubPdwhSnsRelationService.save(pubPdwhSnsRelationPO);
        }
      }
    } catch (Exception e) {
      logger.error("保存或更新sns成果与pdwh成果关系异常！PubPdwhSnsRelationPO={}", pubPdwhSnsRelationPO, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns成果与pdwh成果关系异常", e);
    }
    return null;
  }

}
