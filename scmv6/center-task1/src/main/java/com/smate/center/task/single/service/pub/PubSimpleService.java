package com.smate.center.task.single.service.pub;

import com.smate.center.task.model.sns.quartz.PubDataStore;
import com.smate.center.task.model.sns.quartz.PubSimple;
import com.smate.center.task.model.sns.quartz.Publication;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;

public interface PubSimpleService {


  /**
   * 查询pubSimple
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubId
   * @return
   */
  public PubSimple queryPubSimple(Long pubId);


  /**
   * 保存
   *
   * @author LXZ
   * 
   * @since 6.0.1
   * @version 6.0.1
   * @param pubSimple
   */
  public void save(PubSimple pubSimple);



  public void savePubDataStore(PubDataStore pub);

  Long savePubSimpleData(Publication pub, PubXmlDocument doc) throws Exception;
}
