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

/**
 * @author yamingd 其他类型检查.
 */
public class PubOtherValidator implements IPubValidator {

  /**
   * 待校验的字段
   */
  private static final List<String> fields = new ArrayList<String>();
  static {
    fields.add("/publication/@publish_year");
    fields.add("/publication/@country_name");
  }

  @Override
  public String getForTmplForm() {

    return PublicationEnterFormEnum.SCHOLAR;
  }

  @Override
  public int getForType() {
    return PublicationTypeEnum.OTHERS;
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
