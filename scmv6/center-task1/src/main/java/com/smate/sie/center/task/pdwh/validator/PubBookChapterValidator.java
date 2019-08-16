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
 * @author yamingd 书籍章节检查.
 */
public class PubBookChapterValidator implements IPubValidator {

  /**
   * 待校验的字段.
   */
  private static final List<String> fields = new ArrayList<String>();
  static {
    fields.add("/pub_book/@publisher");
    fields.add("/publication/@country_name");
    fields.add("/publication/@publish_year");
    fields.add("/pub_book/@editors");
    fields.add("/pub_book/@book_title");
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

    return PublicationTypeEnum.BOOK_CHAPTER;
  }

  @Override
  public List<ErrorField> validate(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {
    List<ErrorField> errors = new ArrayList<ErrorField>();
    String notNumber = xmlDocument.getXmlNodeAttribute(fields.get(7));
    for (int index = 0; index < fields.size() - 2; index++) {
      String xpath = fields.get(index);
      String value = xmlDocument.getXmlNodeAttribute(xpath);
      if (XmlUtil.isEmpty(value)) {
        if (5 == index || 6 == index)
          break;
        String name = xpath.split("/@")[1];
        ErrorField ef = new ErrorField(name, ErrorNoEnum.Empty);
        errors.add(ef);
      }
    }
    String startPage = xmlDocument.getXmlNodeAttribute(fields.get(5));
    String endPage = xmlDocument.getXmlNodeAttribute(fields.get(6));
    String articleNo = xmlDocument.getXmlNodeAttribute(fields.get(8));
    if (XmlUtil.isEmpty(articleNo) && XmlUtil.isEmpty(startPage) && XmlUtil.isEmpty(endPage)
        && XmlUtil.isEmpty(notNumber)) {
      ErrorField ef = new ErrorField("pages", ErrorNoEnum.Empty);
      errors.add(ef);
    }
    return errors;
  }

}
