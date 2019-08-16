package com.smate.center.open.service.publication;

import com.smate.center.open.model.publication.PublicationList;

/**
 * 成果收录情况.
 * 
 * @author LY
 * 
 */
public interface PublicationListService {
  /**
   * 查询成果的收录情况.
   * 
   * @param pubId
   * @return
   */
  public PublicationList getPublicationList(Long pubId) throws Exception;

  /**
   * 返回格式如下的字符串:.<br>
   * EI,SCI,ISTP,SSCI
   * 
   * @param pubList
   * @return
   */
  public String convertPubListToString(PublicationList pubList);


}
