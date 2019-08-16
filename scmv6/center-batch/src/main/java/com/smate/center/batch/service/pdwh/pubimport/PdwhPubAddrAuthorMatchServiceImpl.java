package com.smate.center.batch.service.pdwh.pubimport;

import org.springframework.stereotype.Service;

import com.smate.center.batch.process.pub.PdwhPubAddrAuthorMacthProcess;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

/**
 * * 基准库成果地址和作者信息匹配任务处理链业务处理
 * 
 * @author LIJUN
 * @date 2018年3月20日
 */
@Service("pdwhPubAddrAuthorMatchService")
/* @Transactional(rollbackFor = Exception.class) */
public class PdwhPubAddrAuthorMatchServiceImpl implements PdwhPubAddrAuthorMatchService {
  private PdwhPubAddrAuthorMacthProcess pdwhPubAddrAuthorMacthProcess;

  public PdwhPubAddrAuthorMacthProcess getPdwhPubAddrAuthorMacthProcess() {
    return pdwhPubAddrAuthorMacthProcess;
  }

  public void setPdwhPubAddrAuthorMacthProcess(PdwhPubAddrAuthorMacthProcess pdwhPubAddrAuthorMacthProcess) {
    this.pdwhPubAddrAuthorMacthProcess = pdwhPubAddrAuthorMacthProcess;
  }

  @Override
  public void saveOrUpdateData(PubPdwhDetailDOM pdwhPub, String operate) throws Exception {
    pdwhPubAddrAuthorMacthProcess.start(pdwhPub, operate);
  }

}
