package com.smate.center.batch.chain.pub.pdwh.match;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.service.pdwh.pubimport.PdwhPubAuthorDeptMatchService;
import com.smate.center.batch.service.pdwh.pubimport.PdwhPublicationService;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

public class PdwhPubAuthorDeptMatchTask implements PdwhPubMatchHandleTask {
  private final String name = "pdwhPubAuthorDeptMatchTask";
  private final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PdwhPubAuthorDeptMatchService pdwhPubAuthorDeptMatchService;
  @Autowired
  private PdwhPublicationService pdwhPublicationService;

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
    List<Map<String, Object>> memberInfoList = pdwhPublicationService.getPubMemberInfoList(pdwhPub.getPubId());
    if (memberInfoList != null && memberInfoList.size() > 0) {
      for (Map<String, Object> memberMap : memberInfoList) {
        pdwhPubAuthorDeptMatchService.saveMemberInsData(memberMap, pdwhPub);
      }
    } else {
      logger.error("基准库成果作者单位信息匹配任务链,获取到的成果为空，pub_id:" + pdwhPub.getPubId());
    }
    pdwhPubAuthorDeptMatchService.updateInsCount(pdwhPub.getPubId());
    return true;
  }

}
