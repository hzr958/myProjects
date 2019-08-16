package com.smate.sie.center.task.pdwh.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.smate.center.task.single.enums.pub.PublicationEnterFormEnum;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.pub.model.ErrorField;
import com.smate.sie.center.task.pdwh.task.PubXmlDocument;
import com.smate.sie.center.task.pdwh.task.PubXmlProcessContext;
import com.smate.sie.center.task.pdwh.utils.ValidateUtilis;

public class PubJournalValidator implements IPubValidator {

  /** 待校验的字段 */
  private static final List<String> fields = new ArrayList<String>();
  static {
    fields.add("/pub_journal/@jname");
    fields.add("/publication/@publish_state");
    fields.add("/publication/@publish_year");
    fields.add("/publication/@issue");
    fields.add("/publication/@volume");
    fields.add("/publication/@start_page");
    fields.add("/publication/@end_page");
    // fields.add("/publication/@not_number");
    fields.add("/publication/@article_number");
  }

  @Override
  public String getForTmplForm() {

    return PublicationEnterFormEnum.SCHOLAR;
  }

  @Override
  public int getForType() {

    return PublicationTypeEnum.JOURNAL_ARTICLE;
  }

  @Override
  public List<ErrorField> validate(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    List<ErrorField> errors = new ArrayList<ErrorField>();
    Map<String, String> datas = ValidateUtilis.getFieldsData(fields, xmlDocument);
    // jname
    ValidateUtilis.fieldIsEmpty(fields.get(0), datas, errors);
    String publishState = datas.get(fields.get(1));
    String articleNo = datas.get(fields.get(7));
    if ("".equals(publishState)) {
      // publish year
      ValidateUtilis.fieldIsEmpty(fields.get(2), datas, errors);
      // issue || volume
      ValidateUtilis.fieldIsEmpty("issue_volume", new String[] {fields.get(3), fields.get(4)}, datas, errors);
      // if (XmlUtil.isEmpty(articleNo)) {
      // start_page || end_page
      ValidateUtilis.fieldIsEmpty("pages", new String[] {fields.get(5), fields.get(6)}, datas, errors);
      // }
    }
    return errors;
  }

}
