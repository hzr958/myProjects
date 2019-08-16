package com.smate.center.task.service.pdwh.quartz;

import java.io.IOException;
import java.util.List;

import com.smate.center.task.model.pdwh.quartz.PubFundingInfo;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

public interface PdwhPublicationService {

  List<PubPdwhDetailDOM> getPdwhPubIds(Long lastPubId, int batchSize);

  void savePubFundingInfo(PubFundingInfo pubFundingInfo);



  List<Long> getNeedAssignPubIds(Long pubId, Integer size);

  /**
   * 对成果单位进行分词匹配,字符将会被替换后匹配
   * 
   * @param pubId
   * @param addrs
   * @throws IOException
   * @author LIJUN
   * @date 2018年5月23日
   */
  void segmentPubOrg(Long pubId, String addrs) throws Exception;


  PubPdwhDetailDOM getNeedAssignPub(Long pdwhPubId);

  List<String> getPubMemberEmailList(Long pdwhPubId);

  List<Long> getbatchhandleIdList(Integer size) throws Exception;

  void updateTaskStatus(Long pdwhPubId, int status, String errMsg) throws Exception;

  List<Long> getTmpBatchhandleIdList(Long startHandleId, Long endHandleId);

}
