package com.smate.center.batch.util.pub;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.core.base.pub.enums.PublicationTypeEnum;
import com.smate.core.base.utils.data.XmlUtil;

/**
 * 生成成果特定字段Hashcode.
 * 
 * @author yamingd
 */
public class PublicationHash {

  /**
   * @param dirtyTitle 脏标题(包含HTML)
   * @return int
   */
  public static Integer dirtyTitleHash(String dirtyTitle) {

    if (dirtyTitle == null) {
      return null;
    } else {
      // html解码
      dirtyTitle = HtmlUtils.htmlUnescape(dirtyTitle);
      dirtyTitle = XmlUtil.trimUnSupportHTML(dirtyTitle);
      dirtyTitle = XmlUtil.trimP(dirtyTitle);
    }

    return cleanTitleHash(dirtyTitle);
  }

  /**
   * 获取干净的doi hash.
   * 
   * @param doi
   * @return
   */
  public static Integer cleanDoiHash(String doi) {
    if (StringUtils.isBlank(doi))
      return null;
    return XmlUtil.getTrimBlankLower(doi).hashCode();
  }

  /**
   * 获取干净的IsiId hash.
   * 
   * @param IsiId
   * @return
   */
  public static Integer cleanisiIdHash(String isiId) {
    if (StringUtils.isBlank(isiId))
      return null;
    return XmlUtil.getTrimBlankLower(isiId).hashCode();
  }

  /**
   * 获取干净的patent no hash.
   * 
   * @param IsiId
   * @return
   */
  public static Integer cleanPatentNoHash(String patentNo) {
    if (StringUtils.isBlank(patentNo))
      return null;
    return XmlUtil.getTrimBlankLower(patentNo).hashCode();
  }

  /**
   * 成果会议名称HashCode.
   * 
   * @param title 标题
   * @return int
   */
  public static Integer cleanConfNameHash(String confName) {

    if (confName == null) {
      return null;
    } else {
      confName = XmlUtil.trimAllHtml(confName);
      confName = XmlUtil.filterForCompare(confName);
    }
    confName = confName.replaceAll("\\s+", "").replaceAll("[\\.,，;；'‘\"\\(\\)（）\\[\\]]{1,}", "").toLowerCase();
    if (StringUtils.isBlank(confName)) {
      return null;
    }
    return confName.hashCode();
  }

  /**
   * 成果标题HashCode.
   * 
   * @param title 标题
   * @return int
   */
  public static Integer cleanTitleHash(String title) {

    if (title == null) {
      return null;
    } else {
      // html解码
      title = HtmlUtils.htmlUnescape(title);
      title = XmlUtil.trimAllHtml(title);
      title = XmlUtil.filterForCompare(title);
    }
    title = title.replaceAll("\\s+", "").trim().toLowerCase();
    if ("".equals(title)) {
      return null;
    }
    return title.hashCode();
  }

  /**
   * 期刊articleNo指纹. jid + volume + issue + article_no .
   */
  public static Integer getJaFingerPrint(Long jid, String volume, String issue, String articleNo) {
    if (jid == null || StringUtils.isBlank(volume) || StringUtils.isBlank(articleNo)) {
      return null;
    }
    if (StringUtils.isBlank(issue)) {
      issue = "NONE";
    }
    String[] values = new String[] {jid.toString(), volume, issue, articleNo};
    return fingerPrint(values);
  }

  /**
   * 期刊pageStart指纹. jid + volume + issue + page_start.
   */
  public static Integer getJpFingerPrint(Long jid, String volume, String issue, String pageStart) {
    if (jid == null || StringUtils.isBlank(volume) || StringUtils.isBlank(pageStart)) {
      return null;
    }
    if (StringUtils.isBlank(issue)) {
      issue = "NONE";
    }
    String[] values = new String[] {jid.toString(), volume, issue, pageStart};
    return fingerPrint(values);
  }

  /**
   * 会议指纹. cpfinger_print：Isbn + page_start .
   */
  public static Integer getCpFingerPrint(String isbn, String pageStart) {
    if (StringUtils.isBlank(isbn) || StringUtils.isBlank(pageStart)) {
      return null;
    }
    String[] values = new String[] {isbn, pageStart};
    return fingerPrint(values);
  }

  /**
   * 会议指纹. cafinger_print：Isbn + article_no .
   */
  public static Integer getCaFingerPrint(String isbn, String articleNo) {
    if (StringUtils.isBlank(isbn) || StringUtils.isBlank(articleNo)) {
      return null;
    }
    String[] values = new String[] {isbn, articleNo};
    return fingerPrint(values);
  }

  /**
   * 会议指纹.issn +  volume  + article_no .
   */
  public static Integer getCvaFingerPrint(String issn, String volume, String issue, String articleNo) {
    if (issn == null || StringUtils.isBlank(volume) || StringUtils.isBlank(articleNo)) {
      return null;
    }
    if (StringUtils.isBlank(issue)) {
      issue = "NONE";
    }
    String[] values = new String[] {issn, volume, issue, articleNo};
    return fingerPrint(values);
  }

  /**
   * 会议指纹.issn +  volume + page_start.
   */
  public static Integer getCvpFingerPrint(String issn, String volume, String issue, String pageStart) {
    if (issn == null || StringUtils.isBlank(volume) || StringUtils.isBlank(pageStart)) {
      return null;
    }
    if (StringUtils.isBlank(issue)) {
      issue = "NONE";
    }
    String[] values = new String[] {issn, volume, issue, pageStart};
    return fingerPrint(values);
  }

  /**
   * 获取作者hashcode.
   * 
   * @param auName
   * @return
   */
  public static Integer getAuNameHash(String auName) {

    if (StringUtils.isBlank(auName) || StringUtils.isBlank(auName)) {
      return null;
    }
    // 删除所有html文本空格，分隔符
    auName = auName.replaceAll("\\<.*?>", "").replaceAll("\\s+", "").replaceAll("[\\.,，;；'‘\"\\(\\)（）\\[\\]]{1,}", "")
        .toLowerCase();
    if (StringUtils.isBlank(auName) || StringUtils.isBlank(auName)) {
      return null;
    }
    return auName.hashCode();
  }

  /**
   * 生成成果某些字段的HashCode.
   * 
   * @param values 字符数组
   * @return String
   */
  private static Integer fingerPrint(String[] values) {
    int count = 0;
    for (int i = 0; i < values.length; i++) {
      String val = values[i];
      if (val == null) {
        values[i] = "NONE";
        count++;
      }
      values[i] = values[i].toLowerCase().replaceAll("\\s+", "").trim();
      if ("".equals(values[i])) {
        count++;
        values[i] = "NONE";
      }
    }
    if (count == values.length) {
      return null;
    }
    String str = StringUtils.join(values, "|");
    return str.hashCode();
  }

  /**
   * 专利类型的FingerPrint.
   * 
   * @param patentNo 专利号
   * @return int
   */
  public static Integer patentFingerPrint(String patentNo) {
    if (StringUtils.isBlank(patentNo)) {
      return null;
    }
    return fingerPrint(new String[] {patentNo});
  }

  /**
   * 获取类别指纹.
   * 
   * @param pubType
   * @param extras
   * @return
   * @throws ServiceException
   */
  public static Integer getFingerPrint(int pubType, String[] extras) throws ServiceException {

    Integer figerPrint = null;
    if (extras != null && extras.length > 0) {
      if ((pubType == PublicationTypeEnum.JOURNAL_ARTICLE)) {
        if (extras.length < 3) {
          throw new ServiceException(
              "extras参数个数不正确,要有4个元素[String volume, String issue, String startPage, String endPage].");
        }
        figerPrint = PublicationHash.journalArticleFingerPrint(extras[0], extras[1], extras[2], extras[3]);
      } else if (pubType == PublicationTypeEnum.PATENT) {
        figerPrint = PublicationHash.patentFingerPrint(extras[0]);
      }
    }

    return figerPrint;
  }

  /**
   * 期刊类型的FingerPrint.
   * 
   * @param volume 卷号
   * @param issue 期号
   * @param startPage 开始页
   * @param endPage 结束页
   * @return int
   */
  public static Integer journalArticleFingerPrint(String volume, String issue, String startPage, String endPage) {
    if (StringUtils.isBlank(volume) && StringUtils.isBlank(issue)) {
      return null;
    }
    if (StringUtils.isNotBlank(volume) && StringUtils.isBlank(issue)) {
      return null;
    }
    if (StringUtils.isBlank(startPage) && StringUtils.isBlank(endPage)) {
      return null;
    }
    return fingerPrint(new String[] {volume, issue, startPage, endPage});
  }

  public static void main(String[] args) {

    System.out.println(PublicationHash
        .cleanConfNameHash("Mei, Yun，h；u（i） ;(L)u, Guo-Quan ;Ch'en, Xu ;Gang, Chen ;Luo, Shufang ;Ibitayo, Dimeji;"));
  }
}
