package com.smate.center.batch.model.sns.pub;

import com.smate.center.batch.service.pub.ConstDictionaryManageCb;
import com.smate.center.batch.service.pub.ConstPubTypeService;
import com.smate.center.batch.service.pub.ConstRefDbService;
import com.smate.center.batch.service.pub.PublicationInsService;
import com.smate.center.batch.service.pub.PublicationXmlService;

/**
 * XML处理使用到的外部服务的工厂.
 * 
 * @author yamingd
 * 
 */
public interface IPubXmlServiceFactory {

  /**
   * 1节点ID.
   */
  public static final int FIRST_NODE_ID = 1;

  ConstDictionaryManageCb getConstDictionaryManage();

  PublicationXmlService getPublicationXmlService();

  ConstRefDbService getConstRefDbService();

  ConstPubTypeService getConstPubTypeService();

  PublicationInsService getPublicationInsService();

}
