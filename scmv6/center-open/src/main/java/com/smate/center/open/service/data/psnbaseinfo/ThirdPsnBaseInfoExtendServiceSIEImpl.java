package com.smate.center.open.service.data.psnbaseinfo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.model.security.Person;

/**
 * sie获取人员信息
 * 
 * @author aijiangbin
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ThirdPsnBaseInfoExtendServiceSIEImpl implements ThirdPsnBaseInfoExtendService {

  protected Logger logger = LoggerFactory.getLogger(getClass());



  @Value("${domainscm}")
  private String domain;

  @Override
  public Map<String, Object> extendPsnInfo(Person person, Long openId) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put(BaseInfoConsts.RESULT_MAP_FIRSTNAME,
        StringUtils.isNotEmpty(person.getFirstName()) ? person.getFirstName() : "");
    map.put(BaseInfoConsts.RESULT_MAP_LASTNAME,
        StringUtils.isNotBlank(person.getLastName()) ? person.getLastName() : "");
    map.put(BaseInfoConsts.RESULT_MAP_ENNAME, StringUtils.isNotBlank(person.getEname()) ? person.getEname() : "");
    map.put(BaseInfoConsts.RESULT_MAP_ZHNAME, StringUtils.isNotBlank(person.getName()) ? person.getName() : "");
    map.put(BaseInfoConsts.RESULT_MAP_MOBILE, StringUtils.isNotBlank(person.getMobile()) ? person.getMobile() : "");

    map.put(BaseInfoConsts.RESULT_MAP_POS_ID, person.getPosId());
    map.put(BaseInfoConsts.RESULT_MAP_POS_GRADES, person.getPosGrades());


    map.put(BaseInfoConsts.RESULT_MAP_TEL, StringUtils.isNotBlank(person.getTel()) ? person.getTel() : "");
    if (person.getSex() != null) {
      map.put(BaseInfoConsts.RESULT_MAP_SEX, person.getSex() == 1 ? "男" : "女");
    } else {
      map.put(BaseInfoConsts.RESULT_MAP_SEX, "");
    }
    map.put("position", StringUtils.isNotBlank(person.getPosition()) ? person.getPosition() : "");
    // 地区id
    map.put("regionId", person.getRegionId());
    // 头衔
    map.put("titolo", StringUtils.isNotBlank(person.getTitolo()) ? person.getTitolo() : "");
    return map;
  }


}
