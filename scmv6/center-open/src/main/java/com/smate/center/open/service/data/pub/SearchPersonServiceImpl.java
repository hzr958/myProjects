package com.smate.center.open.service.data.pub;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.form.SearchPersonForm;
import com.smate.core.base.psn.dao.PersonProfileDao;
import com.smate.core.base.psn.dao.PsnProfileUrlDao;
import com.smate.core.base.psn.dao.PsnStatisticsDao;
import com.smate.core.base.psn.model.PsnProfileUrl;
import com.smate.core.base.psn.model.PsnStatistics;
import com.smate.core.base.utils.model.security.Person;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通过solr 检索人员信息
 * 
 * @author aijiangbin
 * @date 2018年8月23日
 */
@Transactional(rollbackFor = Exception.class)
public class SearchPersonServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());


  @Autowired
  private PersonProfileDao personProfileDao;
  @Autowired
  private PsnProfileUrlDao psnProfileUrlDao;
  @Autowired
  private PsnStatisticsDao psnStatisticsDao;
  @Value("${domainscm}")
  private String scmDomain;


  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    Object pageNo = serviceData.get("pageNo");
    Object pageSize = serviceData.get("pageSize");
    if (pageNo == null) {
      logger.error("具体服务类型参数pageNo不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_272, paramet, "具体服务类型参数pageNo不能为空");
      return temp;
    }
    if (pageSize == null) {
      logger.error("具体服务类型参数pageSize不能为空");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_273, paramet, "具体服务类型参数pageSize不能为空");
      return temp;
    }
    if (!NumberUtils.isNumber(pageNo.toString()) || NumberUtils.toInt(serviceData.get("pageNo").toString()) < 1) {
      logger.error("具体服务类型参数pageNo格式不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_274, paramet, "具体服务类型参数pageNo格式不正确");
      return temp;
    }
    if (!NumberUtils.isNumber(pageSize.toString()) || NumberUtils.toInt(serviceData.get("pageSize").toString()) < 1) {
      logger.error("具体服务类型参数pageSize格式不正确");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_275, paramet, "具体服务类型参数pageSize格式不正确");
      return temp;
    }
    paramet.putAll(serviceData);
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    // 具体业务
    List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();

    SearchPersonForm form = new SearchPersonForm();
    Integer pageNo = NumberUtils.toInt(paramet.get("pageNo").toString());
    Integer pageSize = NumberUtils.toInt(paramet.get("pageSize").toString());
    form.setPageNo(pageNo);
    form.setPageSize(pageSize);
    Object searchKey = paramet.get("searchKey");
    Object name = paramet.get("name");
    Object insName = paramet.get("insName");
    Object position = paramet.get("position");
    Object email = paramet.get("email");

    form.setName(name != null ? name.toString() : "");
    form.setInsName(insName != null ? insName.toString() : "");
    form.setPosition(position != null ? position.toString() : "");
    form.setSearchKey(searchKey != null ? searchKey.toString() : "");
    form.setEmail(email != null ? email.toString() : "");


    try {
      personProfileDao.searchPerson(form);
      // 设置页数
      Map<String, Object> extraMap = new HashMap<String, Object>();
      Integer tatalCount = form.getCount();
      extraMap.put("count", tatalCount);
      if (tatalCount % pageSize == 0) {
        extraMap.put("totalPages", tatalCount / pageSize);
      } else {
        extraMap.put("totalPages", tatalCount / pageSize + 1);
      }
      dataList.add(extraMap);
      if (form.getResult() != null && form.getResult().size() > 0) {
        for (int i = 0; i < form.getResult().size(); i++) {
          Person person = form.getResult().get(i);
          Map<String, Object> dataMap = new HashMap<String, Object>();
          setPersonInfo(person, dataMap);
          dataList.add(dataMap);
        }
      }
    } catch (Exception e) {
      logger.error("检索数据异常!", e);
    }

    return successMap(OpenMsgCodeConsts.SCM_000, dataList);
  }

  private void setPersonInfo(Person person, Map<String, Object> dataMap) {
    Long psnId = person.getPersonId();
    PsnStatistics psnStatistics = psnStatisticsDao.get(psnId);
    // prj_sum pub_sum patent_sum pub_award_sum cited_sum
    dataMap.put("openPubSum", psnStatistics != null ? psnStatistics.getOpenPubSum() : 0);
    dataMap.put("allPubSum", psnStatistics != null ? psnStatistics.getPubSum() : " ");
    dataMap.put("openPrjSum", psnStatistics != null ? psnStatistics.getOpenPrjSum() : 0);
    dataMap.put("allPrjSum", psnStatistics != null ? psnStatistics.getPrjSum() : 0);
    dataMap.put("patentSum", psnStatistics != null ? psnStatistics.getPatentSum() : 0);
    dataMap.put("pubAwardSum", psnStatistics != null ? psnStatistics.getPubAwardSum() : 0);
    dataMap.put("citedSum", psnStatistics != null ? psnStatistics.getCitedSum() : 0);
    dataMap.put("psnId", person.getPersonDes3Id());
    dataMap.put("email", person.getEmail());
    // 职称
    dataMap.put("position", StringUtils.isNotBlank(person.getPosition()) ? person.getPosition() : "");
    String name = "";
    if (StringUtils.isNotBlank(person.getName())) {
      name = person.getName();
    } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
      name = person.getFirstName() + " " + person.getLastName();
    } else {
      name = person.getEname();
    }
    dataMap.put("name", name);
    dataMap.put("insName", StringUtils.isNotBlank(person.getInsName()) ? person.getInsName() : "");
    // 取人员细信息 来拼接显示
    dataMap.put("avatars", person.getAvatars());
    PsnProfileUrl psnUrl = psnProfileUrlDao.find(psnId);
    if (psnUrl != null && psnUrl.getPsnIndexUrl() != null) {
      dataMap.put("psnUrl", scmDomain + "/P/" + psnUrl.getPsnIndexUrl());
    }
    dataMap.put("titolo", buildPsnShowTitolo(person));
  }

  private String buildPsnShowTitolo(Person person) {
    String psnShowTitolo = "";
    if (person != null) {
      if (StringUtils.isNotBlank(person.getInsName())) {
        psnShowTitolo += person.getInsName();
      }
      if (StringUtils.isNotBlank(person.getDepartment())) {
        psnShowTitolo += " " + person.getDepartment();
      }
      if (StringUtils.isNotBlank(person.getPosition())) {
        psnShowTitolo += " " + person.getPosition();
      }
    }
    return StringUtils.trimToEmpty(psnShowTitolo);
  }



}
