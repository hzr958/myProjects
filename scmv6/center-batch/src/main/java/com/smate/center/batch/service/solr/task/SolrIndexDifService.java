package com.smate.center.batch.service.solr.task;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;

/**
 * 索引接口
 * 
 * @author LJ
 *
 */

public interface SolrIndexDifService {

  public void runIndex();

  public void addPsnIndex(Long psnId);

  public void calculateTF();

  public void deletePrivateUser();

  public void getFulltextPath();

  /**
   * 基准库改造，成果索引
   * 
   * @param pdwhPubId
   * @throws Exception
   */
  public void indexPublication(PdwhPublication pdwhPub) throws Exception;

  /**
   * 基准库改造，专利索引
   * 
   * @param pdwhPubId
   * @throws Exception
   */
  public void indexPatent(PdwhPublication pdwhPub) throws Exception;

  void saveKwStrForTf(SolrServer server, String kws, String disCode, String id) throws Exception;

  void saveKwStrForCoTf(SolrServer server, String firstKw, String secondKw, String disCode, String id, int k)
      throws Exception;

  void saveKwStrForCoTf(SolrServer server, List<SolrInputDocument> docList) throws Exception;

  SolrInputDocument getKwStrForCoTf(String firstKw, String secondKw, String disCode, String id) throws Exception;
}
