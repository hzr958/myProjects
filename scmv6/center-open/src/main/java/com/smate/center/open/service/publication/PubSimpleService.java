package com.smate.center.open.service.publication;

import java.util.Date;

import com.smate.center.open.model.publication.PubSimple;

public interface PubSimpleService {

  /**
   * 根据时间检查成果
   * 
   * @param pubId
   * @param importDate
   * @return
   * @throws Exception
   */
  public PubSimple checkPubNewest(Long pubId, Date importDate) throws Exception;

}
