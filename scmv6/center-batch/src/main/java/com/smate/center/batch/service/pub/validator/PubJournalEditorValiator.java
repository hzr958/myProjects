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

public class PubJournalEditorValiator implements IValidator {

  /**
   * 待校验的字段
   */
  private static final List<String> fields = new ArrayList<String>();
  static {
    fields.add("/pub_journal/@jname");
    fields.add("/pub_journal_editor/@position");
    fields.add("/pub_journal_editor/@start_year");
    fields.add("/pub_journal_editor/@end_year");
  }

  @Override
  public String getForTmplForm() {
    return PublicationEnterFormEnum.SCHOLAR;
  }

  @Override
  public int getForType() {
    return PublicationTypeEnum.JOURNAL_EDITOR;
  }

  @Override
  public List<ErrorField> validate(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    List<ErrorField> errors = new ArrayList<ErrorField>();
    for (int index = 0; index < fields.size() - 2; index++) {
      String xpath = fields.get(index);
      String value = xmlDocument.getXmlNodeAttribute(xpath);
      if (XmlUtil.isEmpty(value)) {
        String name = xpath.split("/@")[1];
        ErrorField ef = new ErrorField(name, ErrorNoEnum.Empty);
        errors.add(ef);
      }
    }
    String startYear = xmlDocument.getXmlNodeAttribute(fields.get(2));
    String endYear = xmlDocument.getXmlNodeAttribute(fields.get(3));
    if (XmlUtil.isEmpty(startYear) && XmlUtil.isEmpty(endYear)) {
      ErrorField ef = new ErrorField("publish_date_11", ErrorNoEnum.Empty);
      errors.add(ef);
    }
    return errors;
  }

}
