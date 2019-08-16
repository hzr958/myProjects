package com.smate.web.v8pub.service.handler.pubdelete;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.PsnPubPO;
import com.smate.core.base.psn.service.psnpub.PsnPubService;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.PubHandlerMapping;
import com.smate.web.v8pub.service.handler.PubHandlerServiceBaseBean;
import com.smate.web.v8pub.service.sns.psnconfigpub.PsnConfigService;

/**
 * SNS个人成果删除处理器
 * 
 * @author YJ
 *
 *         2018年7月31日
 */
@PubHandlerMapping(pubHandlerName = "deletePsnPubHandler")
@Service
@Transactional(rollbackFor = Exception.class)
public class PHSPsnPubDeleteImpl extends PubHandlerServiceBaseBean {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PsnConfigService psnConfigService;
  @Autowired
  private PsnPubService psnPubService;

  @Override
  protected void setCheckConfig(List<CheckConfig> checkConfigList) {
    checkConfigList.add(new CheckConfig("pubGenre", Integer.class, false));
    checkConfigList.add(new CheckConfig("des3PubId", String.class, false));
    checkConfigList.add(new CheckConfig("des3PsnId", String.class, false));
  }

  @Override
  protected void checkParameter(PubDTO pub) throws PubHandlerCheckParameterException {
    Long pubId = Long.valueOf(Des3Utils.decodeFromDes3(pub.des3PubId));
    Long psnId = Long.valueOf(Des3Utils.decodeFromDes3(pub.des3PsnId));

    // 判断所删除的成果是否存在
    PsnPubPO psnPubPO = psnPubService.getPsnPub(pubId, psnId);
    if (psnPubPO == null || psnPubPO.getStatus() == 1) {
      logger.error("所要删除的成果不存在pubId=" + pubId);
      throw new PubHandlerCheckParameterException("所要删除的成果不存在pubId=" + pubId);
    }
    // 校验psnId是否有效
    Long cnfId = psnConfigService.getCnfIdByPsnId(psnId);
    if (NumberUtils.isNullOrZero(cnfId)) {
      logger.error("人员不存在psnId=" + psnId);
      throw new PubHandlerCheckParameterException("人员不存在psnId=" + psnId);
    }
  }

}
