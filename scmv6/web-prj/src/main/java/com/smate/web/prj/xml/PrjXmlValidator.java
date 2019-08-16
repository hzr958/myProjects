package com.smate.web.prj.xml;

import java.util.ArrayList;
import java.util.List;

import com.smate.core.base.utils.data.XmlUtil;
import com.smate.core.base.utils.exception.InvalidXpathException;
import com.smate.core.base.utils.number.NumberUtils;

/**
 * 项目类型校验.
 * 
 * @author liqinghua
 * 
 */
public class PrjXmlValidator {

  /**
   * 待校验的字段.
   */
  private static final String[] FIELDS = {"ins_name"};

  public static List<PrjXmlErrorField> validate(PrjXmlDocument xmlDocument) throws InvalidXpathException {
    List<PrjXmlErrorField> errors = new ArrayList<PrjXmlErrorField>();
    for (String node : FIELDS) {
      String value = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, node);
      if (XmlUtil.isEmpty(value)) {
        PrjXmlErrorField ef = new PrjXmlErrorField(node, NumberUtils.INTEGER_ZERO);
        errors.add(ef);
      }
    }
    return errors;
  }
}
