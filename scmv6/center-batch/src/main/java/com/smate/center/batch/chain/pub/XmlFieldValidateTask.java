package com.smate.center.batch.chain.pub;

import java.util.List;

import org.dom4j.Element;

import com.smate.center.batch.model.sns.pub.ErrorField;
import com.smate.center.batch.model.sns.pub.IValidator;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.core.base.utils.constant.PubXmlConstants;

/**
 * @author yamingd 校验字段数据
 */
public class XmlFieldValidateTask implements IPubXmlTask {

  /**
   * xml字段校验
   */
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

    IValidator validator = context.getXmlValidatorFactory().getValidator(formTmpl, context.getPubTypeId());

    List<ErrorField> fields = validator.validate(xmlDocument, context);

    if (fields != null) {
      for (ErrorField field : fields) {
        Element error = errors.addElement("error");
        error.addAttribute("field", field.getName());
        error.addAttribute("error_no", String.valueOf(field.getErrorNo()));
      }
      if (fields.size() > 0)
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "is_valid", "0");
      else
        xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "is_valid", "1");

    } else {
      xmlDocument.setXmlNodeAttribute(PubXmlConstants.PUBLICATION_XPATH, "is_valid", "1");
    }
    return true;
  }

}
