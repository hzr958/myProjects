package com.smate.center.batch.tasklet.pubassign;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubAssign;
import com.smate.center.batch.service.pdwh.isipub.IsiPublicationService;
import com.smate.center.batch.service.pdwh.pub.CnkiPatPublicationService;
import com.smate.center.batch.service.pdwh.pub.CnkiPublicationService;
import com.smate.center.batch.service.pdwh.pub.EiPublicationService;
import com.smate.center.batch.service.pdwh.pub.PubMedPublicationService;
import com.smate.center.batch.service.pdwh.pub.SpsPublicationService;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPubAssignService;
import com.smate.center.batch.tasklet.base.BaseTasklet;
import com.smate.center.batch.tasklet.base.DataVerificationStatus;
import com.smate.core.base.utils.exception.BatchTaskException;


/**
 * 发送匹配到单位的成果XML到单位.
 * 
 * @author hzr
 *
 */
public class PubCacheSendTasklet extends BaseTasklet {

  @Autowired
  private IsiPublicationService isiPublicationService;
  @Autowired
  private CnkiPublicationService cnkiPublicationService;
  @Autowired
  private CnkiPatPublicationService cnkiPatPublicationService;
  @Autowired
  private SpsPublicationService spsPublicationService;
  @Autowired
  private PubMedPublicationService pubMedPublicationService;
  @Autowired
  private EiPublicationService eiPublicationService;
  @Autowired
  private PdwhPubAssignService pdwhPubAssignService;

  @Override
  public DataVerificationStatus dataVerification(String withData) throws BatchTaskException {
    return DataVerificationStatus.TRUE;
  }

  @Override
  public void taskExecution(Map jobContentMap) throws BatchTaskException {

    String pubIdStr = String.valueOf(jobContentMap.get("msg_id"));
    Long pubId = Long.parseLong(pubIdStr);
    String insIdStr = String.valueOf(jobContentMap.get("ins_id"));
    Long insId = Long.parseLong(insIdStr);
    String dbidStr = String.valueOf(jobContentMap.get("dbSource"));
    Integer dbid = Integer.parseInt(dbidStr);

    PdwhPubAssign pdwhAssign = this.pdwhPubAssignService.getPdwhPubAssign(pubId, insId);
    this.pdwhPubAssignService.sendInsPub(pdwhAssign, dbid);

  }

}
