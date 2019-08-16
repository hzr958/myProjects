package com.smate.center.task.single.factory.pub;

import com.smate.center.task.single.service.pub.ConstDictionaryManageCb;
import com.smate.center.task.single.service.pub.PublicationXmlService;

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

  /*
   * ConstRefDbService getConstRefDbService();
   * 
   * ConstPubTypeService getConstPubTypeService();
   * 
   * PublicationInsService getPublicationInsService();
   */

}
