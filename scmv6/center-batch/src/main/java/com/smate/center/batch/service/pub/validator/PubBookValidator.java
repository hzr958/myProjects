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
 * @author yamingd 书籍类型校验
 */
public class PubBookValidator implements IValidator {

  /**
   * 待校验的字段.
   */
  private static final List<String> FIELDS = new ArrayList<String>();
  static {
    FIELDS.add("/publication/@publish_year");
    FIELDS.add("/pub_book/@publisher");
    FIELDS.add("/publication/@country_name");
  }

  @Override
  public String getForTmplForm() {
    return PublicationEnterFormEnum.SCHOLAR;
  }

  @Override
  public int getForType() {
    return PublicationTypeEnum.BOOK;
  }

  @Override
  public List<ErrorField> validate(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    List<ErrorField> errors = new ArrayList<ErrorField>();
    for (int index = 0; index < FIELDS.size(); index++) {
      String xpath = FIELDS.get(index);
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
