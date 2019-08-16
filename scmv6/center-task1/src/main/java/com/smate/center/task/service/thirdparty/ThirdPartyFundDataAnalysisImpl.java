package com.smate.center.task.service.thirdparty;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.task.dao.thirdparty.ThirdSourcesFundDao;
import com.smate.center.task.model.thirdparty.ThirdSourcesFund;
import com.smate.core.base.utils.date.DateUtils;
import com.smate.core.base.utils.string.StringUtils;

/**
 * 基金机会数据解析类
 *
 * @author aijiangbin
 * @create 2019-04-23 17:31
 **/
@Transactional(rollbackFor = Exception.class)
public class ThirdPartyFundDataAnalysisImpl extends ThirdPartyDataAnalysisBase {
  private static Long sourceId = 1L;
  private static List<String> notBlankFields = new ArrayList<>();
  static {
    notBlankFields.add("fund_desc");
    // notBlankFields.add("discipline_classification_type");
    notBlankFields.add("fund_year");
    notBlankFields.add("apply_date_start");
    notBlankFields.add("apply_date_end");
    notBlankFields.add("funding_agency");
  }

  @Autowired
  private ThirdSourcesFundDao thirdSourcesFundDao;

  @Override
  public void checkData(String token, Map<String, Object> sourcesData) throws Exception {

    if (CollectionUtils.isNotEmpty(notBlankFields)) {
      for (String field : notBlankFields) {
        if (checkBankField(sourcesData, field)) {
          throw new Exception(field + "为空异常");
        }
      }
    }
    String funding_agency = getString(sourcesData.get("funding_agency"));
    if (!token.equalsIgnoreCase(funding_agency)) {
      throw new Exception("funding_agency和token不一样funding_agency=" + funding_agency + ";token=" + token);
    }

  }

  @Override
  public Object analysisData(Map<String, Object> sourcesData) throws Exception {
    ThirdSourcesFund fund = null;
    String fundNumber = getString(sourcesData.get("fund_number"));
    String fundingAgency = getString(sourcesData.get("funding_agency"));
    fund = thirdSourcesFundDao.find(fundNumber, fundingAgency);
    if (fund == null) {
      fund = new ThirdSourcesFund();
      fund.setCreateTime(new Date());
      Long id = thirdSourcesFundDao.createId();
      fund.setId(id);
      fund.setFundNumber(fundNumber);
      fund.setFundingAgency(fundingAgency);
    }
    fund.setFundType(getString(sourcesData.get("fund_type")));
    fund.setAccessoryUrl(getString(sourcesData.get("accessorys")));
    fund.setApplyDateEnd(getDate(sourcesData.get("apply_date_end")));
    fund.setApplyDateStart(getDate(sourcesData.get("apply_date_start")));
    fund.setDeclareGuideUrl(getString(sourcesData.get("declare_guide_url")));
    fund.setDeclareUrl(getString(sourcesData.get("declare_url")));
    fund.setDisciplineClassificationType(getString(sourcesData.get("discipline_classification_type")));
    fund.setDisciplineLimit(getString(sourcesData.get("discipline_limit")));
    fund.setFundDesc(getString(sourcesData.get("fund_desc")));
    fund.setFundKeywords(getString(sourcesData.get("fund_keywords")));
    fund.setFundNumber(getString(sourcesData.get("fund_number")));
    fund.setFundTitleAbbr(getString(sourcesData.get("fund_title_abbr")));
    fund.setFundYear(getInt(sourcesData.get("fund_year")));
    fund.setUpdateTime(new Date());
    fund.setFundTitleCn(dealTitle(sourcesData.get("fund_title_cn")));
    fund.setFundTitleEn(dealTitle(sourcesData.get("fund_title_en")));
    return fund;
  }

  private String dealTitle(Object title) {
    List<String> titleList = (ArrayList) title;
    String content = "";
    if (CollectionUtils.isNotEmpty(titleList)) {
      for (int i = titleList.size() - 1; i >= 0; i--) {
        if (StringUtils.isNotBlank(titleList.get(i)) && i > 0) {
          if (StringUtils.isBlank(titleList.get(i - 1))) {
            content = content + titleList.get(i);
          } else {
            content = content + titleList.get(i) + " - ";
          }
        } else if (StringUtils.isNotBlank(titleList.get(i)) && i == 0) {
          content = content + titleList.get(i);
        }
      }
    }
    return content;
  }

  private String getString(Object obj) {
    if (obj == null) {
      return "";
    }
    return obj.toString().trim();
  }

  private Date getDate(Object obj) {
    if (obj == null) {
      return null;
    }
    return DateUtils.parseStringToDate(obj.toString().trim());
  }

  private Integer getInt(Object obj) {
    if (obj == null) {
      return null;
    }
    return Integer.parseInt(obj.toString().trim());
  }

  private Long getLong(Object obj) {
    if (obj == null) {
      return null;
    }
    return Long.parseLong(obj.toString().trim());
  }

  private Double getDouble(Object obj) {
    if (obj == null) {
      return null;
    }
    return Double.parseDouble(obj.toString().trim());
  }

  @Override
  public void saveData(Object obj) throws Exception {
    thirdSourcesFundDao.save((ThirdSourcesFund) obj);
  }


}
