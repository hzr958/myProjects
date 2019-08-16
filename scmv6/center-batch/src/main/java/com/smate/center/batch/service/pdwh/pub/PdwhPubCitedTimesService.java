package com.smate.center.batch.service.pdwh.pub;

import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;

/**
 * 成果引用service
 * 
 * @author zjh
 *
 */
public interface PdwhPubCitedTimesService {
  /**
   * 更新引用次数
   * 
   * @param pdwhPub
   * @param context
   * @throws Exception
   */
  public void updatePdwhPubCitedRelation(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception;


  public void savePdwhPubCitedRelation(Long citePubId, Long dupPubId) throws Exception;


}
