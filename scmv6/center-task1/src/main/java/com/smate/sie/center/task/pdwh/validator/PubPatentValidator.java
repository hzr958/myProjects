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

/** @author yamingd 专利类型检查. */
public class PubPatentValidator implements IPubValidator {

  /** 待校验的字段. */
  private static final List<String> fields = new ArrayList<String>();
  static {
    fields.add("/pub_patent/@patent_no");
    // fields.add("/pub_patent/@patent_org");
    // fields.add("/publication/@country_name");
    fields.add("/pub_patent/@patent_type");
    fields.add("/publication/@publish_year");
    // fields.add("/pub_patent/@start_year");
    // fields.add("/pub_patent/@end_year");
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
    // String patentStatus =
    // xmlDocument.getXmlNodeAttribute("/pub_patent/@patent_status");
    for (int index = 0; index < fields.size(); index++) {
      // if (index == 5 || index == 6)
      // continue;
      // if ("1".equals(patentStatus) && index == 4)
      // continue;
      String xpath = fields.get(index);
      String value = xmlDocument.getXmlNodeAttribute(xpath);
      if (XmlUtil.isEmpty(value)) {
        String name = xpath.split("/@")[1];
        ErrorField ef = new ErrorField(name, ErrorNoEnum.Empty);
        errors.add(ef);
      }
    }
    // if ("1".equals(patentStatus)) {
    // String startYear = xmlDocument.getXmlNodeAttribute(fields.get(5));
    // String endYear = xmlDocument.getXmlNodeAttribute(fields.get(6));
    // if (XmlUtil.isEmpty(startYear) && XmlUtil.isEmpty(endYear)) {
    // ErrorField ef = new ErrorField("effective_date", ErrorNoEnum.Empty);
    // errors.add(ef);
    // }
    // }
    return errors;
  }

}
