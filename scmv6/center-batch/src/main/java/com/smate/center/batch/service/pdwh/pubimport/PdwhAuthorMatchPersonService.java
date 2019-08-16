package com.smate.center.batch.service.pdwh.pubimport;

import java.util.List;

import com.smate.center.batch.model.pdwh.pub.PdwhPubAddrInsRecord;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

public interface PdwhAuthorMatchPersonService {
  /**
   * 根据pubId查询对应的匹配记录
   * 
   * @param pubId
   * @return
   * @author LIJUN
   * @date 2018年3月20日
   */
  public List<PdwhPubAddrInsRecord> getPubAddrMatchedRecord(Long pubId);

  public void startMatchSnsPsn(List<PdwhPubAddrInsRecord> list, String context, PubPdwhDetailDOM pdwhPub)
      throws Exception;

  void deleteUnconfirmedRec(Long pubId);

}
