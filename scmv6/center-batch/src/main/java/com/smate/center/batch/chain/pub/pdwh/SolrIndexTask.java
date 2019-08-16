package com.smate.center.batch.chain.pub.pdwh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.service.solr.task.SolrIndexDifService;

/**
 * 基准库成果改造，成果索引创建
 * 
 * @author LJ 2017-03-01
 *
 */
public class SolrIndexTask implements PdwhPubHandleTask {
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final String name = "solr_index";
  @Autowired
  private SolrIndexDifService solrIndexDifSerivce;

  @Override
  public String getName() {
    return this.name;
  }

  @Override
  public boolean can(PdwhPublication pdwhPub, PdwhPubImportContext context) {
    return true;
  }

  @Override
  public boolean run(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception {
    try {
      int patentType = pdwhPub.getPubType();
      // 判断是否为专利
      if (patentType == 5) {
        solrIndexDifSerivce.indexPatent(pdwhPub);
      } else {
        solrIndexDifSerivce.indexPublication(pdwhPub);
      }

    } catch (Exception e) {
      logger.error("PdwhPublication索引创建出错,pubId:" + pdwhPub.getPubId(), e);
    }
    return true;
  }

}
