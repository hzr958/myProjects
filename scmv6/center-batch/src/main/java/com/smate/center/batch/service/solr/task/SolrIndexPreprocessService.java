package com.smate.center.batch.service.solr.task;

public interface SolrIndexPreprocessService {
  public void buildAuthors();

  public void cleanDupPubAll();

  public void checkFullTextForPdwh();
}
