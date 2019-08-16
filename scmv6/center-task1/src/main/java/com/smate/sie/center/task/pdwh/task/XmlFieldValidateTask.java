package com.smate.sie.center.task.pdwh.task;

import java.util.List;

import org.dom4j.Element;

import com.smate.core.base.pub.model.ErrorField;
import com.smate.core.base.utils.constant.PubXmlConstants;
import com.smate.sie.center.task.pdwh.validator.IPubValidator;

/** @author yamingd 校验字段数据 */
public class XmlFieldValidateTask implements IPubXmlTask {

  /** xml字段校验 */
  private final String name = "xml_field_validate";

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#getName()
   */
  @Override
  public String getName() {
    return this.name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#can(com.iris.scm.xml.XmlDocument,
   * com.iris.scm.xml.XmlProcessContext)
   */
  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {
    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#run(com.iris.scm.xml.XmlDocument,
   * com.iris.scm.xml.XmlProcessContext)
   */
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    // 先删除
    xmlDocument.removeNode(PubXmlConstants.PUB_ERRORS_XPATH);
    Element errors = xmlDocument.createElement(PubXmlConstants.PUB_ERRORS_XPATH);

    String formTmpl = xmlDocument.getFormTemplate();
    IPubValidator pubValidator;
    pubValidator = context.getPubXmlValidatorFactory().getValidator(formTmpl, context.getPubTypeId());
    List<ErrorField> fields = pubValidator.validate(xmlDocument, context);
    if (fields != null) {
      for (ErrorField field : fields) {
        Element error = errors.addElement("error");
        error.addAttribute("field", field.getName());
        error.addAttribute("error_no", String.valueOf(field.getErrorNo()));
      }
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "is_valid", "0");
    } else {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "is_valid", "0");
    }
    return true;
  }

}
