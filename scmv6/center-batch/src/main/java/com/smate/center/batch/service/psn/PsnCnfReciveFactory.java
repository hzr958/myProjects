package com.smate.center.batch.service.psn;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.smate.core.base.psn.consts.PsnCnfConst;

@Service("psnCnfReceiveAdapter")
@Transactional(rollbackFor = Exception.class)
public class PsnCnfReciveFactory implements PsnCnfReceiveAdapter {

  @Resource(name = "psnCnfReceivePub")
  private PsnCnfReceive pubReceive;
  @Resource(name = "psnCnfReceivePrj")
  private PsnCnfReceive prjReceive;
  @Resource(name = "psnCnfReceiveWork")
  private PsnCnfReceive workReceive;
  @Resource(name = "psnCnfReceiveEdu")
  private PsnCnfReceive eduReceive;

  private final Map<String, PsnCnfReceive> receiveAdapters = new HashMap<String, PsnCnfReceive>();

  @PostConstruct
  public void init() {
    receiveAdapters.put(PsnCnfConst.PUB, pubReceive);
    receiveAdapters.put(PsnCnfConst.PRJ, prjReceive);
    receiveAdapters.put(PsnCnfConst.WORK, workReceive);
    receiveAdapters.put(PsnCnfConst.EDU, eduReceive);
  }

  @Override
  public PsnCnfReceive get(String type) {
    PsnCnfReceive receive = receiveAdapters.get(type);
    Assert.notNull(receive);
    return receive;
  }

}
