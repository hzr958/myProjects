package com.smate.core.base.utils.data;

import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class XMLEntityConvertUtils {

  public static String convertToXmlString(String str) {
    if (StringUtils.isNotBlank(str)) {
      Element pe = DocumentHelper.createElement("elementName");
      pe.addText(str);
      return pe.asXML().replaceAll("<elementName>|</elementName>", "");
    } else {
      return "";
    }
  }

  public static void main(String[] args) {
    String str = "αΑ &Alpha βΒ &Beta γΓ &Gamma δΔ &Delta κκ &Kappa μΜ &Mu ∞ °｝"
        + "|“？》《：+——）（*&……%￥#@！!@#$%^&*()__+(^}|\'/.,<>?:";
    System.out.println(XMLEntityConvertUtils.convertToXmlString(str));
  }
}
