package com.smate.web.institution.service;

import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.psninfo.PsnInfoUtils;
import com.smate.core.base.utils.url.HttpRequestUtils;
import com.smate.web.institution.consts.InstitutionConsts;
import com.smate.web.institution.dao.ConstClassifyCseiDao;
import com.smate.web.institution.dao.ConstClassifyEconomicDao;
import com.smate.web.institution.form.InstitutionForm;
import com.smate.web.institution.form.InstitutionInfo;
import com.smate.web.institution.model.ConstClassifyCsei;
import com.smate.web.institution.model.ConstClassifyEconomic;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aijiangbin
 * @create 2019-07-02 9:40
 **/

@Service("institutionOptService")
@Transactional(rollbackFor = Exception.class)
public class InstitutionOptServiceImpl implements InstitutionOptService {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private ConstClassifyEconomicDao economicDao;
  @Autowired
  private ConstClassifyCseiDao cseiDao;
  @Autowired
  private PersonDao personDao;
  @Value("${domainrol.https}")
  private String domainrol;


  @Override
  public void buildEconomicInfo(InstitutionForm form) {
    String scienceAreaIds = form.getEconomicIds();// 选中的科技领域
    Map<String, Object> allData = new HashMap<String, Object>();
    // 一级经济产业
    List<ConstClassifyEconomic> firstLevelList = this.findFirstEconomic();
    List<ConstClassifyEconomic> subLevelList = new ArrayList<ConstClassifyEconomic>();
    List<ConstClassifyEconomic> scienceAreaList = new ArrayList<ConstClassifyEconomic>();

    for (ConstClassifyEconomic economic : firstLevelList) {
      // 二级经济产业
      subLevelList = this.findSecondEconomic(economic.getCode());
      // 检查人员已选科技领域
      if (StringUtils.isNotBlank(scienceAreaIds)) {
        for (ConstClassifyEconomic ec : subLevelList) {
          if (scienceAreaIds.contains(ec.getCode())) {
            ec.setAdded(true);
            economic.setAdded(true);
            scienceAreaList.add(ec);
          }
        }
      }
      allData.put("CategoryMap_sub" + economic.getCode(), subLevelList);
    }

    List<ConstClassifyEconomic> sortAreaList = new ArrayList<ConstClassifyEconomic>();
    String[] selectIds = scienceAreaIds.split(",");
    for (String id : selectIds) {// 排序选中的
      for (ConstClassifyEconomic economic : scienceAreaList) {
        if (id.equals(economic.getCode())) {
          sortAreaList.add(economic);
        }
      }
    }

    allData.put("CategoryMap_first", firstLevelList);
    if (allData == null || allData.isEmpty()) {
      allData.put("isNull", true);
    } else {
      allData.put("isNull", false);
    }
    form.setEconomicMap(allData);
    form.setEconomicList(sortAreaList);// 选中的
  }


  protected List<ConstClassifyEconomic> findFirstEconomic() {
    List<ConstClassifyEconomic> list = economicDao.getFirstLev();
    if (CollectionUtils.isEmpty(list))
      return new ArrayList<>();

    for (ConstClassifyEconomic economic : list) {
      economic.setShowCode(economic.getCode());
      economic.setShowName(economic.getZhName());
    }
    return list;
  }

  protected List<ConstClassifyEconomic> findSecondEconomic(String superCode) {
    List<ConstClassifyEconomic> list = economicDao.getSecondLev(superCode);
    if (CollectionUtils.isEmpty(list))
      return new ArrayList<>();
    for (ConstClassifyEconomic economic : list) {
      economic.setShowCode(economic.getCode());
      economic.setShowName(economic.getZhName());
    }
    return list;
  }


  @Override
  public void buildCseiInfo(InstitutionForm form) {
    String scienceAreaIds = form.getEconomicIds();// 选中的科技领域
    Map<String, Object> allData = new HashMap<String, Object>();
    // 一级经济产业
    List<ConstClassifyCsei> firstLevelList = this.findFirstCsei();
    List<ConstClassifyCsei> subLevelList = new ArrayList<ConstClassifyCsei>();
    List<ConstClassifyCsei> scienceAreaList = new ArrayList<ConstClassifyCsei>();

    for (ConstClassifyCsei economic : firstLevelList) {
      // 二级经济产业
      subLevelList = this.findSecondCsei(economic.getCode());
      // 检查人员已选科技领域
      if (StringUtils.isNotBlank(scienceAreaIds)) {
        for (ConstClassifyCsei ec : subLevelList) {
          if (scienceAreaIds.contains(ec.getCode())) {
            ec.setAdded(true);
            economic.setAdded(true);
            scienceAreaList.add(ec);
          }
        }
      }
      allData.put("CategoryMap_sub" + economic.getCode(), subLevelList);
    }

    List<ConstClassifyCsei> sortAreaList = new ArrayList<ConstClassifyCsei>();
    String[] selectIds = scienceAreaIds.split(",");
    for (String id : selectIds) {// 排序选中的
      for (ConstClassifyCsei economic : scienceAreaList) {
        if (id.equals(economic.getCode())) {
          sortAreaList.add(economic);
        }
      }
    }

    allData.put("CategoryMap_first", firstLevelList);
    if (allData == null || allData.isEmpty()) {
      allData.put("isNull", true);
    } else {
      allData.put("isNull", false);
    }
    form.setEconomicMap(allData);
    form.setCseiList(sortAreaList);// 选中的
  }


  protected List<ConstClassifyCsei> findFirstCsei() {
    List<ConstClassifyCsei> list = cseiDao.getFirstLev();
    if (CollectionUtils.isEmpty(list))
      return new ArrayList<>();

    for (ConstClassifyCsei economic : list) {
      economic.setShowCode(economic.getCode());
      economic.setShowName(economic.getZhName());
    }
    return list;
  }

  protected List<ConstClassifyCsei> findSecondCsei(String superCode) {
    List<ConstClassifyCsei> list = cseiDao.getSecondLev(superCode);
    if (CollectionUtils.isEmpty(list))
      return new ArrayList<>();
    for (ConstClassifyCsei economic : list) {
      economic.setShowCode(economic.getCode());
      economic.setShowName(economic.getZhName());
    }
    return list;
  }

  /**
   * true == 可用 false == 不可用
   * 
   * @param form
   * @return
   */
  @Override
  public boolean checkInsName(InstitutionForm form) {
    String url = domainrol + InstitutionConsts.CHECK_INS_NAME_URL;
    StringBuilder sb = new StringBuilder();
    sb.append("type=" + form.getCheckInsNameType());
    sb.append("&ins_name=" + encode(form.getInsName()));
    sb.append("&domain=" + encode(form.getInsDomain() + InstitutionConsts.SCM_DOMAIN_SUFFIX));
    String s = HttpRequestUtils.sendPost(url, sb.toString());
    if (StringUtils.isNotBlank(s) && JacksonUtils.isJsonString(s)) {
      Map<String, String> objectMap = JacksonUtils.jsonToMap(s);
      if (objectMap != null && objectMap.get("is_exist") != null && objectMap.get("is_exist").equalsIgnoreCase("no")) {
        return true;
      }
      if (objectMap.get("domian") != null) {
        form.setInsDomain(objectMap.get("domian"));
      }
    }
    return false;
  }

  /**
   * true = 创建成功 false = 创建失败
   * 
   * @param form
   * @return
   */
  @Override
  public boolean createInsPage(InstitutionForm form) {
    Person person = personDao.get(form.getPsnId());
    if (person == null)
      return false;
    String name = PsnInfoUtils.getPersonName(person, "zh");
    form.setForwardUrl("http://" + form.getInsDomain() + InstitutionConsts.SCM_DOMAIN_SUFFIX);
    String url = domainrol + InstitutionConsts.CREATE_INS_URL;
    StringBuilder sb = new StringBuilder();
    sb.append("psn_id=" + person.getPersonId().toString());
    sb.append("&psn_name=" + encode(StringUtils.isNotBlank(name) ? name : ""));
    sb.append("&ins_name=" + encode(form.getInsName()));
    sb.append("&nature_id=" + form.getNature());
    sb.append("&domain=" + encode(form.getInsDomain() + InstitutionConsts.SCM_DOMAIN_SUFFIX));
    sb.append("&url=" + encode(form.getUrl()));
    sb.append("&eco_code=" + form.getEcoCode());
    sb.append("&csei_code=" + form.getCseiCode());
    sb.append("&description=" + encode(form.getDescription()));
    sb.append("&country_id=" + form.getCountryId());
    sb.append("&prv_id=" + form.getPrvId());
    sb.append("&cy_id=" + form.getCyId());
    // sb.append("&dis_id=");
    String s = HttpRequestUtils.sendPost(url, (sb.toString()));
    if (StringUtils.isNotBlank(s) && JacksonUtils.isJsonString(s)) {
      Map<String, String> objectMap = JacksonUtils.jsonToMap(s);
      if (objectMap != null && objectMap.get("status") != null && objectMap.get("status").equalsIgnoreCase("success")) {
        return true;
      }
    }
    return false;
  }

  public String encode(String param){
    if(StringUtils.isBlank(param)) return  "" ;
    try {
      param = StringEscapeUtils.unescapeHtml4(param);
      param =  URLEncoder.encode(param,"utf-8");
      return param;
    } catch (Exception e) {
       logger.error("url参数，编码异常+"+param ,e);
    }
    return  "";
  }
  @Override
  public void findInsPageList(InstitutionForm form) {
    // "type":"1我的主页 2我关注的主页" "psn_id":"12345678",
    String url = domainrol + InstitutionConsts.INS_FOLLOW_LIST_URL;
    StringBuilder sb = new StringBuilder();
    sb.append("psn_id=" + form.getPsnId());
    sb.append("&type=" + form.getInsPageType());
    String s = HttpRequestUtils.sendPost(url, sb.toString());
    if (StringUtils.isNotBlank(s) && JacksonUtils.isJsonString(s)) {
      Map<String, Object> resultMap = JacksonUtils.jsonToMap(s);
      List list = (List) resultMap.get("result");
      if (CollectionUtils.isNotEmpty(list)) {
        for (Object obj : list) {
          Map<String, Object> map = (Map) obj;
          InstitutionInfo insInfo = new InstitutionInfo();
          form.getListInfo().add(insInfo);
          insInfo.setInsId((Integer) map.get("ins_id"));
          insInfo.setInsName(ObjectUtils.toString(map.get("ins_name")));
          insInfo.setLogoUrl(String.valueOf(map.get("log_url")));
          insInfo.setDomain(ObjectUtils.toString(map.get("domain")));
          insInfo.setStView((Integer) map.get("st_view"));
          insInfo.setStShare((Integer) map.get("st_share"));
          insInfo.setStFollow((Integer) map.get("st_follow"));
          insInfo.setManageUrl(
              InstitutionConsts.HTTPS + ObjectUtils.toString(map.get("domain")) + InstitutionConsts.INS_MANAGE_SUFFIX);
          insInfo.setVisitUrl(
              InstitutionConsts.HTTPS + ObjectUtils.toString(map.get("domain")) + InstitutionConsts.INS_VISIT_SUFFIX);
        }
      }
    }
  }

  public void testData(InstitutionForm form) {
    for (int i = 0; i < 5; i++) {
      InstitutionInfo insInfo = new InstitutionInfo();
      form.getListInfo().add(insInfo);
      // insInfo.setInsId("875");
      if (form.getInsPageType() == 1) {
        insInfo.setInsName("我的机构名" + RandomStringUtils.randomAlphanumeric(5));
      } else {
        insInfo.setInsName("我关注的机构名" + RandomStringUtils.randomAlphanumeric(5));
      }
      insInfo.setLogoUrl("https://uat.scholarmate.com/avatars/e7/d0/0c/1000000003147.jpg");
      insInfo.setDomain("uatsns");
      // insInfo.setStView("123");
      // insInfo.setStShare("44");
      // insInfo.setStFollow("66");
    }
  }

  /**
   * type 1赞2分享3关注4取消赞5取消关注 ins_id 单位id data_from 1机构主页-机构版；2个人版
   * 
   * @param form
   * @return true = 取消成功，
   */
  @Override
  public boolean cancelFollow(InstitutionForm form) {

    String url = domainrol + InstitutionConsts.INS_SOCIAL_URL;
    StringBuilder sb = new StringBuilder();
    sb.append("ins_id=" + form.getInsId());
    sb.append("&psn_id=" + form.getPsnId());
    sb.append("&type=5");
    sb.append("&data_from=2");
    String s = HttpRequestUtils.sendPost(url, sb.toString());
    if (StringUtils.isNotBlank(s) && JacksonUtils.isJsonString(s)) {
      Map<Object, Object> map = JacksonUtils.jsonToMap(s);
      if (map != null && map.get("status") != null && map.get("status").equals("success")) {
        return true;
      }
    }
    return false;
  }
}
