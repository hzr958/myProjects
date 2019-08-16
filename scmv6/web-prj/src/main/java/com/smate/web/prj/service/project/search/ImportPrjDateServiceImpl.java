package com.smate.web.prj.service.project.search;

import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.prj.exception.PrjException;
import com.smate.web.prj.form.PrjXmlProcessContext;
import com.smate.web.prj.util.PrjXmlFragmentCleanerHelper;
import com.smate.web.prj.xml.PrjXmlConstants;
import com.smate.web.prj.xml.PrjXmlDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * 处理xml中的时间.
 * 
 * @author wsn
 * @date Dec 18, 2018
 */
public class ImportPrjDateServiceImpl implements ImportPrjXmlDealService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Override
  public String checkParameter(PrjXmlProcessContext context) throws PrjException {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String dealWithXml(PrjXmlDocument xmlDocument, PrjXmlProcessContext context) throws PrjException {
    try {
      String pattern = "yyyy-MM-dd";
      String[] dates = null;
      if (xmlDocument.existsNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "start_date")) {
        dates = PrjXmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PrjXmlConstants.PROJECT_XPATH, "start_date",
            pattern);
        xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "start_year", dates[0]);
        xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "start_month", dates[1]);
        xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "start_day", dates[2]);
        dates = PrjXmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PrjXmlConstants.PROJECT_XPATH, "end_date",
            pattern);
        xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "end_year", dates[0]);
        xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "end_month", dates[1]);
        xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "end_day", dates[2]);
      }
      if (xmlDocument.existsNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "start_year")) {
        PrjXmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/project/@start_", pattern);
        PrjXmlFragmentCleanerHelper.setDateValueFromYMD(xmlDocument, "/data/project/@end_", pattern);
      }
      if (xmlDocument.existsNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "end_date")) {
        dates = PrjXmlFragmentCleanerHelper.splitDateYearMonth(xmlDocument, PrjXmlConstants.PROJECT_XPATH, "end_date",
            pattern);
        judgePrjStatus(dates, xmlDocument);
      }
    } catch (Exception e) {
      throw new PrjException(e);
    }
    return null;
  }


  // 根据结束时间设置项目状态
  protected void judgePrjStatus(String[] endDate, PrjXmlDocument xmlDocument) {
    Calendar cal = Calendar.getInstance();
    // 结束时间大于现在，进行中;
    // 结束时间小于现在，已完成;
    // 结束日期就在今天算已完成
    String prjStatus = compareDate("", endDate[0], cal.get(Calendar.YEAR));
    prjStatus = compareDate(prjStatus, endDate[1], cal.get(Calendar.MONTH) + 1);
    prjStatus = compareDate(prjStatus, endDate[2], cal.get(Calendar.DAY_OF_MONTH));
    prjStatus = StringUtils.isBlank(prjStatus) ? "02" : prjStatus;
    xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "prj_state", prjStatus);
  }

  // 依次比较年、月、日
  protected String compareDate(String prjStatus, String dateStr, int date) {
    // 不等时，01或02，后面的不进行比较；相等时，为空，后面继续比较；时间为空时，03，下面不进行比较
    if (StringUtils.isBlank(prjStatus)) {
      if(StringUtils.isNotBlank(dateStr)){
        dateStr = dateStr.replaceAll("^[0]+","");
      }
      if (StringUtils.isNotBlank(dateStr)) {
        int compareVal = Integer.parseInt(dateStr) - date;
        if (compareVal > 0) {
          prjStatus = "01"; // 进行中
        } else if (compareVal < 0) {
          prjStatus = "02"; // 已完成
        }
      } else {
        prjStatus = "03"; // 其他
      }
    }
    return prjStatus;
  }

}
