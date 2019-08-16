package com.smate.center.batch.service.pub;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.center.batch.model.sns.pub.IPubXmlServiceFactory;

/**
 * @author yamingd Xml处理需要用到的外部服务
 */
@Service("scholarPublicationXmlServiceFactory")
public class ScholarPublicationXmlServiceFactory implements IPubXmlServiceFactory {

  /**
   * 
   */

  @Autowired
  private ConstDictionaryManageCb constDictionaryManage;
  @Autowired
  private PublicationXmlService publicationXmlService;
  @Autowired
  private ConstRefDbService constRefDbService;
  @Autowired
  private ConstPubTypeService constPubTypeService;
  @Autowired
  private PublicationInsService publicationInsService;


  @Override
  public ConstDictionaryManageCb getConstDictionaryManage() {

    return this.constDictionaryManage;
  }


  @Override
  public PublicationXmlService getPublicationXmlService() {
    return this.publicationXmlService;
  }

  @Override
  public ConstRefDbService getConstRefDbService() {

    return this.constRefDbService;
  }


  @Override
  public ConstPubTypeService getConstPubTypeService() {

    return this.constPubTypeService;
  }


  @Override
  public PublicationInsService getPublicationInsService() {

    return this.publicationInsService;
  }



}
