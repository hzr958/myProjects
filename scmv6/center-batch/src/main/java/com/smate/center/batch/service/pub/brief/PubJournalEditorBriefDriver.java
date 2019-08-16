package com.smate.center.batch.service.pub.brief;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.smate.center.batch.enums.pub.PublicationEnterFormEnum;
import com.smate.center.batch.model.sns.pub.PubXmlProcessContext;
import com.smate.center.batch.oldXml.prj.BriefUtilis;
import com.smate.center.batch.oldXml.pub.PubXmlDocument;
import com.smate.center.batch.service.pub.IBriefDriver;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;

/**
 * @author yamingd 期刊编辑Brief生成驱动.
 */
public class PubJournalEditorBriefDriver implements IBriefDriver {

  /**
   * 字段.
   */
  private static final List<String> FIELDS = new ArrayList<String>();
  static {
    FIELDS.add("/pub_journal/@jname");
    FIELDS.add("/pub_journal/@zh_name");
    FIELDS.add("/pub_journal/@en_name");
    FIELDS.add("/pub_journal_editor/@position");
    FIELDS.add("/publication/@publish_year");
    FIELDS.add("/publication/@publish_month");
    FIELDS.add("/publication/@publish_day");
    FIELDS.add("/publication/@pub_date_desc");
    FIELDS.add("/pub_meta/@zh_source_db_name");
    FIELDS.add("/pub_meta/@en_source_db_name");
  }

  /**
   * 格式化Pattern.
   */
  private final String pattern = "<out>${position}</out><date>, ${publish_date}</date><out></out>";

  @Override
  public Map<String, String> getData(Locale locale, PubXmlDocument xmlDocument, PubXmlProcessContext context)
      throws Exception {

    Map<String, String> datas = BriefUtilis.getFieldsData(FIELDS, xmlDocument);
    String year = datas.get(FIELDS.get(4));
    String month = datas.get(FIELDS.get(5));
    String day = datas.get(FIELDS.get(6));
    String publishDate = BriefUtilis.getStandardDateString(year, month, day);
    datas.put("publish_date", publishDate);

    String zhSourceDbName = datas.get(FIELDS.get(8));
    String enSourceDbName = datas.get(FIELDS.get(9));
    String sourceDbName = XmlUtil.getLanguageSpecificText(locale.getLanguage(), zhSourceDbName, enSourceDbName);
    datas.put("source_db_name", sourceDbName);

    String jcname = datas.get(FIELDS.get(1));
    String jename = datas.get(FIELDS.get(2));
    String jname = XmlUtil.getLanguageSpecificText(locale.getLanguage(), jcname, jename);
    if (!"".equals(jname)) {
      datas.put("jname", jname);
    } else {
      jname = datas.get(FIELDS.get(0));
      if (!"".equals(jname))
        datas.put("jname", jname);
    }
    datas = BriefUtilis.normalizeData(datas);

    return datas;
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
  public String getPattern() {

    return this.pattern;
  }

}
