package com.smate.sie.center.task.pdwh.validator;

import java.util.List;

import com.smate.core.base.pub.model.ErrorField;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;
import com.smate.sie.center.task.pdwh.task.PubXmlProcessContext;

/** @author yamingd xml校验接口 */
public interface IPubValidator {

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
