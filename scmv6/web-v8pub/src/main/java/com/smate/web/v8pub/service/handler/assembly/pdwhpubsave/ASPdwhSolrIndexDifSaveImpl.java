package com.smate.web.v8pub.service.handler.assembly.pdwhpubsave;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.exception.PubHandlerAssemblyException;
import com.smate.web.v8pub.service.handler.PubDTO;
import com.smate.web.v8pub.service.handler.assembly.PubHandlerAssemblyService;
import com.smate.web.v8pub.service.pdwh.PubPdwhDetailService;
import com.smate.web.v8pub.service.solr.SolrIndexDifService;

/**
 * 基准库成果sorl索引的增加
 * 
 * @author YJ
 *
 *         2018年9月10日
 */
@Transactional(rollbackFor = Exception.class)
public class ASPdwhSolrIndexDifSaveImpl implements PubHandlerAssemblyService {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private SolrIndexDifService solrIndexDifService;
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
      PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDetailService.getByPubId(pub.pubId);
      // 判断是否为专利
      if (PublicationTypeEnum.isPatent(pub.pubType)) {
        solrIndexDifService.indexPatent(pubPdwhDetailDOM, pub.pubIndexUrl, pub.publishYear, pub.publishMonth,
            pub.publishDay);
      } else {
        solrIndexDifService.indexPublication(pubPdwhDetailDOM, pub.pubIndexUrl, pub.publishYear, pub.publishMonth,
            pub.publishDay);
      }
    } catch (Exception e) {
      logger.error("基准库成果索引创建出错,pubId={}" + pub.pubId, e);
    }
    return null;
  }

}
