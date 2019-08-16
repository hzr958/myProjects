package com.smate.center.task.model.sns.quartz;

import java.util.List;

import com.smate.center.task.model.sns.pub.ErrorField;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;



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
