package com.smate.center.batch.service.pdwh.pubimport;

/**
 * 生成成果短地址
 * 
 * @author LJ
 *
 */
public interface PubShortUrlSaveService {
  /**
   * 生成成果短地址
   * 
   * @param pubId
   * @param type
   * @throws Exception
   */
  public void producePubShortUrl(Long pubId, String type) throws Exception;

}
