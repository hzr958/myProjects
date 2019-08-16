package com.smate.center.task.service.pub.brief;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import com.smate.center.task.model.sns.quartz.PubXmlProcessContext;
import com.smate.center.task.single.enums.pub.PublicationEnterFormEnum;
import com.smate.center.task.single.oldXml.pub.PubXmlDocument;
import com.smate.center.task.single.service.pub.IBriefDriver;
import com.smate.center.task.utils.BriefUtilis;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;

/**
 * @author yamingd 期刊文章Brief生成驱动.
 */
public class PubJournalArticleBriefDriver implements IBriefDriver {

  /**
   * 字段.
   */
  private static final List<String> FIELDS = new ArrayList<String>();
  static {
    FIELDS.add("/pub_journal/@jname");
    FIELDS.add("/pub_journal/@zh_name");
    FIELDS.add("/pub_journal/@en_name");
    FIELDS.add("/publication/@issue");
    FIELDS.add("/publication/@volume");
    FIELDS.add("/publication/@start_page");
    FIELDS.add("/publication/@end_page");
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
  private final String pattern =
      "<out>${jname}</out><vol_issue>, ${volume,issue}</vol_issue><page>, ${start_page,end_page}</page><out>, ${publish_date}</out><out></out>";

  @Override
  public Map<String, String> getData(Locale locale, PubXmlDocument xmlDocument, PubXmlProcessContext context)
      throws Exception {

    Map<String, String> datas = BriefUtilis.getFieldsData(FIELDS, xmlDocument);
    String year = datas.get(FIELDS.get(7));
    String month = datas.get(FIELDS.get(8));
    String day = datas.get(FIELDS.get(9));
    // scm-6684 方法中加入了一个locale参数便于获取中英文不同格式日期
    String publishDate = BriefUtilis.getDateString(locale, year, month, day);
    if (locale.getLanguage().equalsIgnoreCase("zh_CN") || locale.getLanguage().equalsIgnoreCase("zh")) {
      datas.put("publish_date", publishDate + " " + datas.get(FIELDS.get(10)));
    } else {
      datas.put("publish_date", datas.get(FIELDS.get(10)) + " " + publishDate);
    }

    String zhSourceDbName = datas.get(FIELDS.get(11));
    String enSourceDbName = datas.get(FIELDS.get(12));
    String sourceDbName = XmlUtil.getLanguageSpecificText(locale.getLanguage(), zhSourceDbName, enSourceDbName);
    datas.put("source_db_name", sourceDbName);

    String jname = "";
    String jcname = datas.get(FIELDS.get(1));
    String jename = datas.get(FIELDS.get(2));

    if (StringUtils.isNotBlank(zhSourceDbName)) {
      // 期刊名称显示英文还是中文，要取决于来源数据库是英文库还是中文库。
      Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
      Matcher m = p.matcher(zhSourceDbName);// 如果zhSourceDbName中包含有中文字符则表明是中文
      jname = m.find() ? jcname : jename;
    }
    if (StringUtils.isBlank(jname)) {
      datas.put("jname", datas.get(FIELDS.get(0)));
    } else {
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

    return PublicationTypeEnum.JOURNAL_ARTICLE;
  }

  @Override
  public String getPattern() {

    return this.pattern;
  }
}
