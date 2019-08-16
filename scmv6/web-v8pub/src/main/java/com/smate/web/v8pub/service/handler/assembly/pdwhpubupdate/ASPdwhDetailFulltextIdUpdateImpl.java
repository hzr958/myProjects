package com.smate.web.v8pub.service.handler.assembly.pdwhpubupdate;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.number.NumberUtils;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;

/**
 * 基准库成果详情对象中全文id的更新
 * 
 * @author YJ
 *
 *         2018年8月27日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPdwhDetailFulltextIdUpdateImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PubPdwhDetailService pubPdwhDetailService;

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
    try {
      PubPdwhDetailDOM pdwhDetail = pubPdwhDetailService.get(pub.pubId);
      if (pdwhDetail != null && !NumberUtils.isNullOrZero(pub.fulltextId)) {
        pdwhDetail.setFulltextId(pub.fulltextId);
        pubPdwhDetailService.saveOrUpdate(pdwhDetail);
      }
    } catch (Exception e) {
      logger.error("更新基准库成果详情fullTextId字段出错！", e);
      throw new PubHandlerAssemblyException(this.getClass().getSimpleName() + "更新基准库成果详情fullTextId字段出错", e);
    }

    return null;
  }

}
