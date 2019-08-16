package com.smate.center.task.service.pub.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.smate.center.task.model.sns.pub.ErrorField;
import com.smate.center.task.model.sns.quartz.IValidator;
import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.single.enums.pub.PublicationEnterFormEnum;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.utils.ValidateUtilis;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;

public class PubJournalValidator implements IValidator {
  /**
   * 待校验的字段
   */
  private static final List<String> fields = new ArrayList<String>();
  static {
    fields.add("/pub_journal/@jname");
    fields.add("/publication/@publish_state");
    fields.add("/publication/@publish_year");
    fields.add("/publication/@issue");
    fields.add("/publication/@volume");
    fields.add("/publication/@start_page");
    fields.add("/publication/@end_page");
    fields.add("/publication/@not_number");
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
    String notNumber = datas.get(fields.get(7));
    String articleNo = datas.get(fields.get(8));
    if ("p".equalsIgnoreCase(publishState)) {
      // publish year
      ValidateUtilis.fieldIsEmpty(fields.get(2), datas, errors);
      // issue || volume
      ValidateUtilis.fieldIsEmpty("issue_volume", new String[] {fields.get(3), fields.get(4)}, datas, errors);
      if (XmlUtil.isEmpty(articleNo) && XmlUtil.isEmpty(notNumber)) {
        // start_page || end_page
        ValidateUtilis.fieldIsEmpty("pages", new String[] {fields.get(5), fields.get(6)}, datas, errors);
      }
    }
    return errors;
  }
}
