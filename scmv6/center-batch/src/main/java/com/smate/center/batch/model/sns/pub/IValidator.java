package com.smate.center.batch.model.sns.pub;

import java.util.List;

import com.smate.center.batch.oldXml.pub.PubXmlDocument;



/**
 * @author yamingd xml校验接口
 */
public interface IValidator {

  /**
   * 成果录入模板名称.
   * 
   * @return
   */
  String getForTmplForm();

  /**
   * 成果类型ID.
   * 
   * @return
   */
  int getForType();

  /**
   * 校验实现.
   * 
   * @param xmlDocument XmlDocument
   * @param context XmlProcessContext
   * @return List<Element>
   */
  List<ErrorField> validate(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception;
}
