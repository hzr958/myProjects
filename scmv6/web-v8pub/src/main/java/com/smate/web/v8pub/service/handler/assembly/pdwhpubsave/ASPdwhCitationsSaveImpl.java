package com.smate.web.v8pub.service.handler.assembly.pdwhpubsave;

import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.util.PubParamUtils;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.po.pdwh.PdwhPubCitationsPO;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PdwhPubCitationsService;

/**
 * 基准库成果引用次数更新/保存
 * 
 * @author YJ
 *
 *         2018年7月20日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPdwhCitationsSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PdwhPubCitationsService pdwhPubCitationsService;

  @Override
  public void checkSourcesParameter(PubDTO pub) throws PubHandlerAssemblyException {

  }

  @Override
  public void checkRebuildParameter(PubDTO pub) throws PubHandlerAssemblyException {
    // TODO Auto-generated method stub

  }

  @Override
  public Map<String, String> excute(PubDTO pub) throws PubHandlerAssemblyException {
    try {
      // 当前文献库的引用次数更新
      if (pub.srcDbId != null) {
        pub.citations = saveCitations(pub.pubId, pub.srcDbId, pub.citations, pub.citedType);
      }
      // 低优先级的引用次数更新
      if (pub.bakDbId != null) {
        // 只保存，不需要赋值更新引用次数
        saveCitations(pub.pubId, pub.bakDbId, pub.bakCitations, pub.citedType);
      }
    } catch (Exception e) {
      logger.error("更新pdwh库引用次数记录表对象出错！", e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "基准库更新引用次数记录出错！", e);
    }
    return null;
  }


  private Integer saveCitations(Long pubId, Integer srcDbId, Integer citations, Integer citedType) {
    Integer dbId = PubParamUtils.combineDbid(srcDbId);
    PdwhPubCitationsPO pdwhPubCitationsPO = pdwhPubCitationsService.getByPubIdAndDbId(pubId, dbId);
    if (pdwhPubCitationsPO != null) {
      // 系统引用次数大则更新为系统的
      Integer cites = PubParamUtils.maxCitations(pdwhPubCitationsPO.getCitations(), citations);
      pdwhPubCitationsPO.setCitations(cites);
    } else {
      pdwhPubCitationsPO = new PdwhPubCitationsPO();
      pdwhPubCitationsPO.setPdwhPubId(pubId);
      pdwhPubCitationsPO.setDbId(dbId);
      pdwhPubCitationsPO.setCitations(citations);
    }
    pdwhPubCitationsPO.setGmtModified(new Date());
    pdwhPubCitationsPO.setType(citedType == null ? 0 : citedType);// 默认后台更新
    pdwhPubCitationsService.saveOrUpdate(pdwhPubCitationsPO);
    return PubParamUtils.resetCitation(srcDbId, citations);
  }

}
