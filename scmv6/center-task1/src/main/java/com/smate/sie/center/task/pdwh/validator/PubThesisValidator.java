package com.smate.sie.center.task.pdwh.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.smate.center.task.single.constants.ErrorNoEnum;
import com.smate.center.task.single.enums.pub.PublicationEnterFormEnum;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.model.ErrorField;
import com.smate.core.base.utils.data.XmlUtil;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;
import com.smate.sie.center.task.pdwh.task.PubXmlProcessContext;
import com.smate.sie.center.task.pdwh.utils.ValidateUtilis;

/** @author yamingd 学位论文检查. */
public class PubThesisValidator implements IPubValidator {

  /** 待校验的字段 */
  private static final List<String> fields = new ArrayList<String>();
  static {
    fields.add("/pub_thesis/@programme");
    fields.add("/pub_thesis/@issue_org");
    fields.add("/publication/@country_name");
    fields.add("/pub_thesis/@department");
    fields.add("/publication/@publish_year");
  }

  @Override
  public String getForTmplForm() {

    return PublicationEnterFormEnum.SCHOLAR;
  }

  @Override
  public int getForType() {

    return PublicationTypeEnum.THESIS;
  }

  @Override
  public List<ErrorField> validate(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    List<ErrorField> errors = new ArrayList<ErrorField>();
    Map<String, String> datas = ValidateUtilis.getFieldsData(fields, xmlDocument);
    for (int index = 0; index < fields.size(); index++) {
      String xpath = fields.get(index);
      String value = datas.get(xpath);
      if (XmlUtil.isEmpty(value)) {
        String name = xpath.split("/@")[1];
        if ("issue_org".equalsIgnoreCase(name)) {
          name = "thesis_issue_org";
        }
        ErrorField ef = new ErrorField(name, ErrorNoEnum.Empty);
        errors.add(ef);
      }
    }
    return errors;
  }

}
