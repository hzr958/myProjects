package com.smate.web.v8pub.service.handler.assembly.snspubdelete;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.model.psncnf.PsnConfigPubPk;
import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubSnsPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.service.sns.psnconfigpub.PsnConfigPubService;
import com.smate.web.v8pub.service.sns.psnconfigpub.PsnConfigService;

/**
 * 个人与个人库成果关系删除
 * 
 * @author YJ
 *
 *         2018年7月20日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubPsnDeleteImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnPubService psnPubService;

  @Autowired
  private PsnConfigPubService psnConfigPubService;
  @Autowired
  private PsnConfigService psnConfigService;

  @Autowired
  private PubSnsService pubSnsService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {

    /**
     * 删除个人库个人成果 本质上是更新个人库成果与个人的关系表状态为删除状态
     */
    try {
      psnPubService.updateStatusByPsnIdAndPubId(pub.psnId, pub.pubId);
      // 取权限
      pub.permission = buildPermission(pub);
      // 取成果类型
      pub.pubType = buildPubType(pub);
    } catch (Exception e) {
      logger.error("删除sns库个人成果出错！psnId={},pubId={}", new Object[] {pub.psnId, pub.pubId}, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "删除sns库个人成果出错!", e);
    }

    return null;
  }

  private Integer buildPubType(PubDTO pub) {
    PubSnsPO pubSnsPO = pubSnsService.get(pub.pubId);
    if (pubSnsPO != null) {
      return pubSnsPO.getPubType();
    }
    return 7;
  }

  private Integer buildPermission(PubDTO pub) {
    Long cnfId = psnConfigService.getCnfIdByPsnId(pub.psnId);
    if (NumberUtils.isNullOrZero(cnfId)) {
      return 4;
    }
    PsnConfigPubPk psnConfigPubPk = new PsnConfigPubPk(cnfId, pub.pubId);
    PsnConfigPub psnConfigPub = psnConfigPubService.get(psnConfigPubPk);
    if (psnConfigPub != null) {
      return psnConfigPub.getAnyUser();
    }
    return 4;
  }

}
