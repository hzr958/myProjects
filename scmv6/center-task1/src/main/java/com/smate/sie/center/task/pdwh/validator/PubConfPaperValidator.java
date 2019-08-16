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

/** @author yamingd 会议论文校验. */
public class PubConfPaperValidator implements IPubValidator {

  /** 待校验的字段 */
  private static final List<String> fields = new ArrayList<String>();
  static {
    fields.add("/pub_conf_paper/@conf_name");
    fields.add("/publication/@country_name");
    fields.add("/pub_conf_paper/@end_year");
    fields.add("/pub_conf_paper/@start_year");
    fields.add("/publication/@publish_year");
    fields.add("/pub_conf_paper/@paper_type");
    // fields.add("/pub_conf_paper/@end_month");
    // fields.add("/pub_conf_paper/@start_month");
  }

  @Override
  public String getForTmplForm() {
    return PublicationEnterFormEnum.SCHOLAR;
  }

  @Override
  public int getForType() {
    return PublicationTypeEnum.CONFERENCE_PAPER;
  }

  @Override
  public List<ErrorField> validate(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    List<ErrorField> errors = new ArrayList<ErrorField>();
    Map<String, String> datas = ValidateUtilis.getFieldsData(fields, xmlDocument);
    ValidateUtilis.fieldIsEmpty(fields.get(0), datas, errors);
    ValidateUtilis.fieldIsEmpty(fields.get(1), datas, errors);

    String endYear = datas.get(fields.get(2));
    String startYear = datas.get(fields.get(3));
    ValidateUtilis.fieldIsEmpty(fields.get(4), datas, errors);
    ValidateUtilis.fieldIsEmpty(fields.get(5), datas, errors);
    if (XmlUtil.isEmpty(endYear) && XmlUtil.isEmpty(startYear)) {
      ErrorField ef = new ErrorField("conf_date", ErrorNoEnum.Empty);
      errors.add(ef);
    } else {
      // String endMonth = datas.get(fields.get(3));
      // String startMonth = datas.get(fields.get(5));
      // if ((!XmlUtil.isEmpty(endYear) && XmlUtil.isEmpty(endMonth))
      // || (!XmlUtil.isEmpty(startYear) &&
      // XmlUtil.isEmpty(startMonth))) {
      // ErrorField ef = new ErrorField("conf_month", ErrorNoEnum.Empty);
      // errors.add(ef);
      // }
      // if ((XmlUtil.isEmpty(endYear) && !XmlUtil.isEmpty(endMonth))
      // || (XmlUtil.isEmpty(startYear) &&
      // !XmlUtil.isEmpty(startMonth))) {
      // ErrorField ef = new ErrorField("conf_year", ErrorNoEnum.Empty);
      // errors.add(ef);
      // }
    }

    return errors;
  }

}
