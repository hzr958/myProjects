package com.smate.center.open.service.data.psnbaseinfo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import com.smate.core.base.utils.model.security.Person;

/**
 * 高校 项目 获取人员详细信息服务
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class ThirdPsnBaseInfoExtendServiceGXImpl implements ThirdPsnBaseInfoExtendService {


  /**
   * 科研系统要求在调用scm接口获取用户更详细的信息，需要包含信息（如果有务必提供）：姓名、英文名、性别、出生日期、电话、邮箱、手机、籍贯、民族、 通讯地址、证件类型、证件号码 。
   */
  @Override
  public Map<String, Object> extendPsnInfo(Person person, Long openId) {

    Map<String, Object> map = new HashMap<String, Object>();
    map.put(BaseInfoConsts.RESULT_MAP_ENAME, person.getEname() == null ? person.getName() : person.getEname());
    if (person.getSex() != null) {
      map.put(BaseInfoConsts.RESULT_MAP_SEX, person.getSex() == 1 ? "男" : "女");
    } else {
      map.put(BaseInfoConsts.RESULT_MAP_SEX, "");
    }
    map.put(BaseInfoConsts.RESULT_MAP_BIRTHDAY,
        StringUtils.isNotBlank(person.getBirthday()) ? person.getBirthday() : "");
    map.put(BaseInfoConsts.RESULT_MAP_TEL, StringUtils.isNotBlank(person.getTel()) ? person.getTel() : "");
    map.put(BaseInfoConsts.RESULT_MAP_MOBILE, StringUtils.isNotBlank(person.getMobile()) ? person.getMobile() : "");
    return map;
  }

}
