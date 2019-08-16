package com.smate.sie.center.task.pdwh.validator;

import java.util.ArrayList;
import java.util.List;

import com.smate.center.task.single.constants.ErrorNoEnum;
import com.smate.center.task.single.enums.pub.PublicationEnterFormEnum;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.model.ErrorField;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;
import com.smate.sie.center.task.pdwh.task.PubXmlProcessContext;

/** @author yamingd 书籍类型校验 */
public class PubBookValidator implements IPubValidator {

  /** 待校验的字段. */
  private static final List<String> FIELDS = new ArrayList<String>();
  static {
    FIELDS.add("/publication/@publish_year");
    FIELDS.add("/pub_book/@publisher");
    // FIELDS.add("/publication/@country_name");
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
    // String publicationStatus =
    // xmlDocument.getXmlNodeAttribute("/pub_book/@publication_status");
    for (int index = 0; index < FIELDS.size(); index++) {
      String xpath = FIELDS.get(index);
      // if ("/publication/@publish_year".equals(xpath))
      // continue;
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
