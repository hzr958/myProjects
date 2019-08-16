package com.smate.center.batch.service.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.model.sns.pub.PublicationForm;

/**
 * 第二阶段成果处理类
 * 
 * @author lxz
 *
 */
@Service("publicationBatchService")
@Transactional(rollbackFor = Exception.class)
public class PublicationBatchServiceImpl implements PublicationBatchService {
  @Autowired
  private PublicationService publicationService;
  @Autowired
  private ConPrjRptPubService conPrjRptPubService;

  public void saveSuccessSync(String from, PublicationForm model) {

    PublicationForm loadXml = publicationService.getPublication(model);
    loadXml.setFrom(from);
    loadXml.setIsisGuid(model.getIsisGuid());
    loadXml.setRptId(model.getRptId());
    loadXml.setPId(model.getPId());
    model.copyPublicationForm(loadXml);
    publicationService.isSyncPublicationToFinalReport(loadXml);
    if ("proposal".equals(from) || "gxprp".equals(from)) {
      publicationService.isSyncPublicationToProposal(loadXml);
    } else if ("expertpub".equalsIgnoreCase(from)) {
      publicationService.isSyncPublicationToExpertPub(loadXml);

    } else if ("labpub".equalsIgnoreCase(from)) {
      publicationService.isSyncPublicationToLabPub(loadXml);

    } else if ("finalRptPub".equals(from)) {

    } else if ("conPrj".equals(from)) {
      conPrjRptPubService.updateMatePubToConPub(model.getPubId());
    }
  }
}
