package com.smate.web.management.service.other;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.web.management.dao.other.fund.ConstFundAgencyDao;
import com.smate.web.management.dao.other.fund.OpenThirdRegDao;
import com.smate.web.management.dao.other.fund.ThirdSourcesDao;
import com.smate.web.management.dao.other.fund.ThirdSourcesFundDao;
import com.smate.web.management.model.other.fund.ConstFundAgency;
import com.smate.web.management.model.other.fund.ConstFundCategory;
import com.smate.web.management.model.other.fund.FundThirdForm;
import com.smate.web.management.model.other.fund.ThirdSourcesFund;

@Service("fundThirdService")
@Transactional(rollbackFor = Exception.class)
public class FundThirdServiceImpl implements FundThirdService {

  private static final long serialVersionUID = 7603977418174465406L;
  protected final Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ThirdSourcesFundDao thirdSourcesFundDao;
  @Autowired
  private OpenThirdRegDao openThirdRegDao;
  @Autowired
  private ConstFundAgencyDao constFundAgencyDao;
  @Autowired
  private ThirdSourcesDao thirdSourcesDao;
  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;
  @Value("${initOpen.restful.url}")
  private String SERVER_URL;

  @SuppressWarnings("unchecked")
  @Override
  public void findFundThirdList(FundThirdForm form) throws ServiceException {
    ThirdSourcesFund thirdFund = form.getThirdSourcesFund();
    List<ThirdSourcesFund> fundList = thirdSourcesFundDao.findFundThirdList(thirdFund, form.getPage());
    if (fundList != null && fundList.size() > 0) {
      for (ThirdSourcesFund fund : fundList) {
        String fundingAgency = fund.getFundingAgency();// 资助机构
        Long agencyId = thirdSourcesDao.getAgentcyIdByToken(fundingAgency);
        if (agencyId != null) {
          ConstFundAgency cfa = constFundAgencyDao.get(agencyId);
          if (cfa != null) {
            fund.setAgencyViewName(cfa.getNameZh() == null ? cfa.getNameEn() : cfa.getNameZh());
          }
        }
      }
      form.setThirdSourcesFundlist(fundList);
    }

  }

  @SuppressWarnings("unchecked")
  @Override
  public void viewFundThirdDetails(FundThirdForm form) throws ServiceException {
    ThirdSourcesFund thirdFund = thirdSourcesFundDao.get(form.getId());
    String fundingAgency = thirdFund.getFundingAgency();// 资助机构
    Long agencyId = thirdSourcesDao.getAgentcyIdByToken(fundingAgency);
    if (agencyId != null) {
      String accessoryUrl = thirdFund.getAccessoryUrl();
      thirdFund.setAccessoryUrlView(null);
      if (accessoryUrl != null && JacksonUtils.isJsonList(accessoryUrl)) {
        thirdFund.setAccessoryUrlView(JacksonUtils.jsonToList(accessoryUrl));
      }
      ConstFundAgency cfa = constFundAgencyDao.get(agencyId);
      if (cfa != null) {
        thirdFund.setAgencyViewName(cfa.getNameZh() == null ? cfa.getNameEn() : cfa.getNameZh());
      }
    }
    form.setThirdSourcesFund(thirdFund);

  }

  /**
   * 基金机会审核
   */
  @Override
  public ConstFundCategory checkFundThird(Long id) throws ServiceException {
    // 1.获取基金机会信息
    ThirdSourcesFund fund = thirdSourcesFundDao.get(id);
    ConstFundCategory fundCategory = new ConstFundCategory();
    if (fund != null) {
      String fundingAgency = fund.getFundingAgency();// 资助机构
      Long agencyId = thirdSourcesDao.getAgentcyIdByToken(fundingAgency);
      if (agencyId != null) {
        ConstFundAgency cfa = constFundAgencyDao.get(agencyId);
        if (cfa != null) {
          fundCategory.setAgencyViewName(cfa.getNameZh() == null ? cfa.getNameEn() : cfa.getNameZh());
        }
      }
      fundCategory.setNameZh(fund.getFundTitleCn());
      fundCategory.setNameEn(fund.getFundTitleEn());
      fundCategory.setAbbr(fund.getFundTitleAbbr());
      fundCategory.setCode(fund.getFundNumber());
      fundCategory.setDescription(fund.getFundDesc());
      fundCategory.setKeywordList(fund.getFundKeywords());
      fundCategory.setFileList(fund.getAccessoryUrl());
      fundCategory.setStartDate(fund.getApplyDateStart());
      fundCategory.setEndDate(fund.getApplyDateEnd());
      fundCategory.setGuideUrl(fund.getDeclareGuideUrl());
      fundCategory.setDeclareUrl(fund.getDeclareUrl());
      fundCategory.setDisList(fund.getDisciplineClassificationType());
      fundCategory.setFundId(id);
    }
    return fundCategory;
  }

  @Override
  public void updateFundThird(Long id, Integer status) throws ServiceException {
    ThirdSourcesFund fund = thirdSourcesFundDao.get(id);
    if (fund != null) {
      fund.setAuditStatus(status);
      fund.setUpdateTime(new Date());
      thirdSourcesFundDao.saveOrUpdate(fund);
    }
  }

  @Override
  public void deleteFundThird(Long id) throws ServiceException {
    updateFundThird(id, 2);
  }

}
