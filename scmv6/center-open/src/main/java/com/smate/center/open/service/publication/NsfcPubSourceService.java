package com.smate.center.open.service.publication;



/**
 * 生成基金委成果XML格式中的source字段
 * 
 * @author Scy
 * 
 */
public interface NsfcPubSourceService {

  /**
   * 获取基金委成果来源格式
   * 
   * @param pubId
   * @return
   * @throws ServiceException
   */
  public String getNsfcPubSource(Long pubId) throws Exception;
}
