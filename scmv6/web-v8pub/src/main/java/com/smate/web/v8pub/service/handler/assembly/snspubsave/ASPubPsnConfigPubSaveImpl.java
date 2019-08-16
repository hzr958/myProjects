package com.smate.web.v8pub.service.handler.assembly.snspubsave;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.psn.model.psncnf.PsnConfigPub;
import com.smate.core.base.psn.model.psncnf.PsnConfigPubPk;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.sns.psnconfigpub.PsnConfigPubService;
import com.smate.web.v8pub.service.sns.psnconfigpub.PsnConfigService;

/**
 * 个人成果配置更新/保存
 * 
 * @author YJ 2018年7月24日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPubPsnConfigPubSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PsnConfigPubService psnConfigPubService;
  @Autowired
  private PsnConfigService psnConfigService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    // 保存数据
    PsnConfigPub psnConfigPub = null;
    // 到这里的cnfId是必须有的
    Long cnfId = psnConfigService.getCnfIdByPsnId(pub.psnId);
    PsnConfigPubPk psnConfigPubPk = new PsnConfigPubPk(cnfId, pub.pubId);
    try {
      psnConfigPub = psnConfigPubService.get(psnConfigPubPk);
      if (psnConfigPub == null) {
        psnConfigPub = new PsnConfigPub();
        psnConfigPub.setId(psnConfigPubPk);
        psnConfigPub.setCreateDate(new Date());
      }
      psnConfigPub.setUpdateDate(new Date());
      if (pub.permission != null && (pub.permission == 7 || pub.permission == 4)) {
        psnConfigPub.setAnyUser(pub.permission);
      } else {
        psnConfigPub.setAnyUser(7);
      }
      psnConfigPub.setAnyView(psnConfigPub.getAnyUser());
      psnConfigPubService.saveOrUpdate(psnConfigPub);
    } catch (Exception e) {
      logger.error("个人成果配置服务类：保存或更新个人成果配置出错！", psnConfigPub, e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "保存或更新个人成果配置出错!", e);
    }
    return null;
  }

}
