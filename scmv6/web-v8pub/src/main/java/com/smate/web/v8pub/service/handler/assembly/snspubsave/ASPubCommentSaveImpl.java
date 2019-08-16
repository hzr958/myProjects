package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.util.DisposeDes3IdUtils;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dao.pdwh.PdwhPubCommentDAO;
import com.smate.web.v8pub.dao.sns.PubCommentDAO;
import com.smate.web.v8pub.enums.PubGenreConstants;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.pdwh.PdwhPubCommentPO;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.po.sns.PubCommentPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;

/**
 * 基准库导入到sns库的成果 评论记录同步到sns库
 * 
 * @author YHX
 *
 *         2019年3月15日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubCommentSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhService pubPdwhService;
  @Autowired
  private PdwhPubCommentDAO pdwhPubCommentDAO;
  @Autowired
  private PubCommentDAO pubCommentDAO;

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
    PubCommentPO pubCommentPO = null;
    try {
      if (!NumberUtils.isNullOrZero(pub.pdwhPubId)) {
        List<PdwhPubCommentPO> list = pdwhPubCommentDAO.findByPubId(pub.pdwhPubId);
        if (list != null) {
          for (PdwhPubCommentPO pdwhPubCommentPO : list) {
            pubCommentPO = pubCommentDAO.findComment(pub.pubId, pdwhPubCommentPO.getPsnId(),
                pdwhPubCommentPO.getContent(), pdwhPubCommentPO.getGmtCreate());
            if (pubCommentPO == null) {
              pubCommentPO = new PubCommentPO();
              pubCommentPO.setPubId(pub.pubId);
              pubCommentPO.setPsnId(pdwhPubCommentPO.getPsnId());
              pubCommentPO.setContent(pdwhPubCommentPO.getContent());
              pubCommentPO.setStatus(pdwhPubCommentPO.getStatus());
              pubCommentPO.setGmtCreate(pdwhPubCommentPO.getGmtCreate());
            }
            pubCommentPO.setGmtModified(pdwhPubCommentPO.getGmtModified());
            pubCommentDAO.saveOrUpdate(pubCommentPO);
          }
        }
        Long commentCount = pdwhPubCommentDAO.getCommentsCount(pub.pdwhPubId);
        pub.commentCount = (int) (commentCount == null ? 0 : commentCount);
      }
    } catch (Exception e) {
      logger.error("基准库导入到sns库的成果,评论记录同步到sns库异常！PubCommentPO={}", pubCommentPO, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库导入到sns库的成果,评论记录同步到sns库异常", e);
    }
    return null;
  }

}
