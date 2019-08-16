package com.smate.web.v8pub.service.handler.pubdelete;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PubPdwhStatusEnum;
import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.po.pdwh.PubPdwhPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.PubHandlerMapping;
import com.smate.web.v8pub.service.handler.PubHandlerServiceBaseBean;
import com.smate.web.v8pub.service.pdwh.PubPdwhService;

/**
 * PDWH 成果删除处理器
 * 
 * @author YHX
 *
 *         2019年4月17日
 */
@PubHandlerMapping(pubHandlerName = "deletePdwhPubHandler")
@Service
@Transactional(rollbackFor = Exception.class)
public class PHSPdwhPubDeleteImpl extends PubHandlerServiceBaseBean {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubPdwhService pubPdwhService;

  @Override
  protected void setCheckConfig(List<CheckConfig> checkConfigList) {
    checkConfigList.add(new CheckConfig("des3PubId", String.class, false));
  }

  @Override
  protected void checkParameter(PubDTO pub) throws PubHandlerCheckParameterException {
    Long pdwhPubId = Long.valueOf(Des3Utils.decodeFromDes3(pub.des3PubId));

    // 判断所删除的成果是否存在
    PubPdwhPO pubPdwhPO = pubPdwhService.get(pdwhPubId);
    if (pubPdwhPO == null || pubPdwhPO.getStatus().equals(PubPdwhStatusEnum.DELETED)) {
      logger.error("所要删除的成果不存在pdwhPubId=" + pdwhPubId);
      throw new PubHandlerCheckParameterException("所要删除的成果不存在pdwhPubId=" + pdwhPubId);
    }
  }

}
