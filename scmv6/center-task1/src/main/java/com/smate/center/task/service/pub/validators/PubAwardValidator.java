package com.smate.center.task.service.pub.validators;

import java.util.ArrayList;
import java.util.List;

import com.smate.center.task.model.sns.pub.ErrorField;
import com.smate.center.task.model.sns.quartz.IValidator;
import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.single.constants.ErrorNoEnum;
import com.smate.center.task.single.enums.pub.PublicationEnterFormEnum;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;

/**
 * @author yamingd 奖励类型校验
 */
public class PubAwardValidator implements IValidator {

  /**
   * 待校验的字段.
   */
  private static final List<String> FIELDS = new ArrayList<String>();
  static {
    FIELDS.add("/pub_award/@issue_ins_name");
    FIELDS.add("/publication/@publish_year");
    FIELDS.add("/pub_award/@award_category");
    FIELDS.add("/pub_award/@award_grade");

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

  @Override
  public String getForTmplForm() {
    return PublicationEnterFormEnum.SCHOLAR;
  }

  @Override
  public int getForType() {
    return PublicationTypeEnum.AWARD;
  }
}
