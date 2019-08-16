package com.smate.center.batch.service.pub;

import java.util.Date;

import com.smate.center.batch.model.sns.pub.PubSimple;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;

public interface PubConfirmXmlManager {

  /**
   * 创建成果XML.
   * 
   * @param postData jsp发回的数据
   * @param pubTypeId 成果类型ID
   * @param articleType 成果(1)/文献(2)
   * @return Long (成果ID)
   */
  Integer createXmlNew(String newXmlData, int pubTypeId, int articleType, PubSimple pubSimple);

  PubXmlProcessContext buildXmlProcessContext(Long psnId, Integer typeId, String local, Date date);

}
