package com.smate.center.batch.service.pub.validator;

import java.util.ArrayList;
import java.util.List;

import com.smate.center.batch.enums.pub.ErrorNoEnum;
import com.smate.center.batch.enums.pub.PublicationEnterFormEnum;
import com.smate.center.batch.model.sns.pub.ErrorField;
import com.smate.center.batch.model.sns.pub.IValidator;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;

/**
 * @author yamingd 专利类型检查.
 */
public class PubPatentValidator implements IValidator {

  /**
   * 待校验的字段.
   */
  private static final List<String> fields = new ArrayList<String>();
  static {
    fields.add("/pub_patent/@patent_no");
    fields.add("/pub_patent/@patent_org");
    // fields.add("/publication/@country_name");
    fields.add("/pub_patent/@patent_type");
    fields.add("/publication/@publish_year");
  }

  @Override
  public String getForTmplForm() {
    return PublicationEnterFormEnum.SCHOLAR;
  }

  @Override
  public int getForType() {
    return PublicationTypeEnum.PATENT;
  }

  @Override
  public List<ErrorField> validate(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    List<ErrorField> errors = new ArrayList<ErrorField>();
    for (int index = 0; index < fields.size(); index++) {
      String xpath = fields.get(index);
      String value = xmlDocument.getXmlNodeAttribute(xpath);
      if (XmlUtil.isEmpty(value)) {
        String name = xpath.split("/@")[1];
        ErrorField ef = new ErrorField(name, ErrorNoEnum.Empty);
        errors.add(ef);
      }
    }
    return errors;
  }

}
