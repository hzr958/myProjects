package com.smate.center.batch.chain.pub.pdwh.match;

import com.smate.center.batch.model.pdwh.pub.PdwhPubAddrInsRecord;
import com.smate.center.batch.service.pdwh.pubimport.PdwhAuthorMatchPersonService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

/**
 * 基准库成果作者信息和sns人员匹配任务
 * 
 * @author LIJUN
 * @date 2018年3月15日
 */
public class PdwhAuthorMatchPersonTask implements PdwhPubMatchHandleTask {
  private final String name = "pdwhAuthorMatchPersonTask";

  @Autowired
  private PdwhAuthorMatchPersonService pdwhAuthorMatchPersonService;

  @Override
  public String getName() {

    return this.name;
  }

  @Override
  public boolean can(PubPdwhDetailDOM pdwhPub, String context) {
    if (pdwhPub == null) {
      return false;
    }
    return true;
  }

  @Override
  public boolean run(PubPdwhDetailDOM pdwhPub, String context) throws Exception {
    List<PdwhPubAddrInsRecord> records = pdwhAuthorMatchPersonService.getPubAddrMatchedRecord(pdwhPub.getPubId());
    pdwhAuthorMatchPersonService.deleteUnconfirmedRec(pdwhPub.getPubId());
    if (CollectionUtils.isEmpty(records)) {
      return true;
    }
    pdwhAuthorMatchPersonService.startMatchSnsPsn(records, context, pdwhPub);

    return true;
  }

}
