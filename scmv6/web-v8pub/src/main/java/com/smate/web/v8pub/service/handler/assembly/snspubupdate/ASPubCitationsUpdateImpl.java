package com.smate.web.v8pub.service.handler.assembly.snspubupdate;

import java.util.Date;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.sns.PubCitationsPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.PubCitationsService;

/**
 * 成果引用次数更新/保存 用户修改多少，就存多少
 * 
 * @author YJ
 *
 *         2018年7月20日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubCitationsUpdateImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubCitationsService pubCitationsService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    // 更新成果引用次数
    PubCitationsPO pubCitationsPO = pubCitationsService.get(pub.pubId);
    try {
      Integer citations = pub.citations;
      // 重置引用次数
      if (pubCitationsPO != null) {
        citations = PubParamUtils.maxCitations(pubCitationsPO.getCitations(), citations);
      } else {
        pubCitationsPO = new PubCitationsPO();
        pubCitationsPO.setPubId(pub.pubId);
      }
      pubCitationsPO.setCitations(citations);
      // 个人库默认手动更新
      pubCitationsPO.setCitedType(pub.citedType == null ? 1 : pub.citedType);
      pubCitationsPO.setGmtModified(new Date());
      pubCitationsService.saveOrUpdate(pubCitationsPO);
      pub.citations = pubCitationsPO.getCitations();
    } catch (Exception e) {
      logger.error("更新sns库引用次数记录表对象出错！对象属性={}", pubCitationsPO, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新sns库引用次数记录出错!", e);
    }
    return null;
  }

}
