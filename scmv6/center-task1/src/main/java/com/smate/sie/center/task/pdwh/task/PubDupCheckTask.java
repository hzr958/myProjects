package com.smate.sie.center.task.pdwh.task;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.task.model.rol.quartz.PubRolSubmissionStatusEnum;
import com.smate.center.task.model.rol.quartz.PublicationRol;
import com.smate.center.task.service.rol.quartz.PublicationRolService;

/**
 * XML查重触发.
 *
 */
public class PubDupCheckTask implements IPubXmlTask {

  private String name = "pub_dup_check";

  @Autowired
  private PublicationRolService publicationRolService;

  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return true;
  }

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    Long pubId = context.getCurrentPubId();
    PublicationRol pub = this.publicationRolService.getPublicationById(pubId);
    int status = pub.getStatus();
    if (status == PubRolSubmissionStatusEnum.APPROVED) {
      // 只有成果是通过审核的才查重，否则不用查重.
      // publicationRolService.sendPubDupCheckQueue(context); to do
      return true;
    }
    return false;
  }

}
