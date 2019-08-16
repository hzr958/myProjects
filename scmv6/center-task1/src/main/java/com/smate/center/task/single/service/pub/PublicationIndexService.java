package com.smate.center.task.single.service.pub;

import java.math.BigDecimal;
import java.util.List;

public interface PublicationIndexService {

  public void publicationIndex();

  public void indexKeywords();

  /**
   * 获取suggest需要index的人名或者地名 type:1.人名，2地名
   * 
   */
  public List<BigDecimal> getSuggestStrToHandleList(Integer type);

  /**
   * suggest中，更新的人名或者地名的处理结果 type：1.人名，2地名
   * 
   **/
  public void updateSuggestStrStatus(Long id, Integer type, Integer status);

  /**
   * suggest中,处理具体的index需要的信息 type：1.人名，2地名
   * 
   **/
  void indexSuggestInfo(Long id, Integer type);

}
