package com.smate.center.open.service.publication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smate.core.base.utils.service.consts.ConstDictionaryManage;

/**
 * @author yamingd Xml处理需要用到的外部服务
 */
@Service("scholarPublicationXmlServiceFactory")
public class ScholarPublicationXmlServiceFactory implements IPubXmlServiceFactory {

  /**
   * 
   */
  private Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstDictionaryManage constDictionaryManage;

  @Override
  public ConstDictionaryManage getConstDictionaryManage() {

    return this.constDictionaryManage;
  }

}
