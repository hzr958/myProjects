package com.smate.web.v8pub.service.handler.pubdelete;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.security.Des3Utils;
import com.smate.web.v8pub.exception.PubHandlerCheckParameterException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.PubHandlerMapping;
import com.smate.web.v8pub.service.handler.PubHandlerServiceBaseBean;
import com.smate.web.v8pub.service.sns.GroupPubService;
import com.smate.web.v8pub.service.sns.PubSnsService;
import com.smate.web.v8pub.service.sns.group.GrpBaseInfoService;

/**
 * SNS群组成果删除处理器
 * 
 * @author YJ
 *
 *         2018年7月31日
 */
@PubHandlerMapping(pubHandlerName = "deleteGroupPubHandler")
@Service
@Transactional(rollbackFor = Exception.class)
public class PHSGroupPubDeleteImpl extends PubHandlerServiceBaseBean {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubSnsService pubSnsService;
  @Autowired
  private GrpBaseInfoService grpBaseInfoService;
  @Autowired
  private GroupPubService groupPubService;

  @Override
  protected void setCheckConfig(List<CheckConfig> checkConfigList) {
    checkConfigList.add(new CheckConfig("des3PubId", String.class, false));
    checkConfigList.add(new CheckConfig("des3GrpId", String.class, false));
  }

  @Override
  protected void checkParameter(PubDTO pub) throws PubHandlerCheckParameterException {
    Long pubId = Long.valueOf(Des3Utils.decodeFromDes3(pub.des3PubId));
    Long grpId = Long.valueOf(Des3Utils.decodeFromDes3(pub.des3GrpId));
    if (pubSnsService.get(pubId) == null) {
      logger.error("删除群组成果业务异常，pubId={}", pubId);
      throw new PubHandlerCheckParameterException("删除群组成果业务异常，所删除的成果不存在");
    }
    if (grpBaseInfoService.getByGrpId(grpId) == null) {
      logger.error("删除群组成果业务异常，grpId={}", grpId);
      throw new PubHandlerCheckParameterException("删除群组成果业务异常，群组不存在");
    }
    if (!groupPubService.existGrpPub(grpId, pubId)) {
      logger.error("删除群组成果业务异常，pubId={},grpId={}", pubId, grpId);
      throw new PubHandlerCheckParameterException("删除群组成果业务异常，群组中不存在此成果");
    }

  }

}
