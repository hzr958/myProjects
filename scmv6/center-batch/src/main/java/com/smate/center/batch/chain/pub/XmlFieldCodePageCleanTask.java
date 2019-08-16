package com.smate.center.batch.chain.pub;

import java.util.ArrayList;
import java.util.List;

import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IPubXmlTask;
import com.smate.core.base.utils.data.XmlUtil;

/**
 * 全角转半角.
 * 
 * @author yamingd
 */
public class XmlFieldCodePageCleanTask implements IPubXmlTask {

  /**
   * 
   */
  private static final List<String> toCleaningField = new ArrayList<String>();
  static {
    toCleaningField.add("/pub_book@isbn");
    toCleaningField.add("/publication@isbn");
    toCleaningField.add("/pub_book@totalpages");
    toCleaningField.add("/pub_book@totalwords");
    toCleaningField.add("/publication@start_page");
    toCleaningField.add("/publication@end_page");
    toCleaningField.add("/publication@page");
    toCleaningField.add("/publication@volume");
    toCleaningField.add("/publication@issue");
    toCleaningField.add("/pub_patent@reg_no");
    toCleaningField.add("/pub_patent@patent_no");
    toCleaningField.add("/pub_book@chapter_no");
  }

  /**
   * 
   */
  private final String name = "xml_field_code_page_clean";

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#getName()
   */
  @Override
  public String getName() {
    return this.name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#can(com.iris.scm.xml.XmlDocument,
   * com.iris.scm.xml.XmlProcessContext)
   */
  @Override
  public boolean can(PubXmlDocument xmlDocument, PubXmlProcessContext context) {

    return true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.iris.scm.xml.IXmlTask#run(com.iris.scm.xml.XmlDocument,
   * com.iris.scm.xml.XmlProcessContext)
   */
  @Override
  public boolean run(PubXmlDocument xmlDocument, PubXmlProcessContext context) throws Exception {

    for (int index = 0; index < toCleaningField.size(); index++) {
      String[] field = toCleaningField.get(index).split("@");
      String xpath = field[0];

      if (xmlDocument.existsNodeAttribute(xpath, field[1])) {
        String val = xmlDocument.getXmlNodeAttribute(xpath, field[1]);
        val = XmlUtil.changeSBCChar(val);
        xmlDocument.setXmlNodeAttribute(xpath, field[1], val);
      }
    }

    return true;

  }

}
