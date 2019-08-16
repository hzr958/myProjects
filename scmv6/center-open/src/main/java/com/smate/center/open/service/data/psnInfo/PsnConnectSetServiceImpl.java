package com.smate.center.open.service.data.psnInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.profile.PersonManager;
import com.smate.center.open.service.user.UserService;
import com.smate.center.open.utils.ConvertObjectUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.cas.security.SysRolUser;
import com.smate.core.base.utils.model.security.Person;

/**
 * 直接设置帐号关联 3hst8glw
 * 
 * @author lyq
 *
 */
@Transactional(rollbackFor = Exception.class)
@Deprecated // 废弃
public class PsnConnectSetServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonManager personManager;
  @Autowired
  private UserService userService;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data == null && StringUtils.isEmpty(data.toString())) {
      logger.error("直接设置帐号关联失败-数据不能为空,服务码:3hst8glw");
      temp = super.errorMap("直接设置帐号关联失败-数据不能为空,服务码:3hst8glw", paramet, "");
      return temp;
    }
    Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
    if (dataMap == null) {
      logger.error("直接设置帐号关联失败-参数格式不正确,不能转换成map,服务码:3hst8glw");
      temp = super.errorMap("直接设置帐号关联失败-参数格式不正确,不能转换成map,服务码:3hst8glw", paramet, "");
      return temp;
    }
    String psnID = ConvertObjectUtils.objectToString(dataMap.get("psnID"));
    if (StringUtils.isBlank(psnID)) {
      logger.error("直接设置帐号关联失败-psnId不能为空,服务码:3hst8glw");
      temp = super.errorMap("直接设置帐号关联失败-psnId不能为空,服务码:3hst8glw", paramet, "");
      return temp;
    } else {
      try {
        Long psnId = NumberUtils.toLong(psnID);
        Person person = personManager.getPerson(psnId);
        if (person == null) {
          logger.error("直接设置帐号关联失败-根据psnId查找person为空,服务码:3hst8glw");
          temp = super.errorMap("直接设置帐号关联失败-根据psnId查找person为空,服务码:3hst8glw", paramet, "");
          return temp;
        }
      } catch (Exception e) {
        logger.error("直接设置帐号关联失败-根据psnId查找person异常,服务码:3hst8glw");
        temp = super.errorMap("直接设置帐号关联失败-根据psnId查找person异常,服务码:3hst8glw", paramet, "");
        return temp;
      }
    }
    String psnGuidID = ConvertObjectUtils.objectToString(dataMap.get("psnGuidID"));
    if (StringUtils.isBlank(psnGuidID)) {
      logger.error("直接设置帐号关联失败-psnGuidID不能为空,服务码:3hst8glw");
      temp = super.errorMap("直接设置帐号关联失败-psnGuidID不能为空,服务码:3hst8glw", paramet, "");
      return temp;
    }
    String insID = ConvertObjectUtils.objectToString(dataMap.get("insID"));
    if (StringUtils.isBlank(insID)) {
      logger.error("直接设置帐号关联失败-insID不能为空,服务码:3hst8glw");
      temp = super.errorMap("直接设置帐号关联失败-insID不能为空,服务码:3hst8glw", paramet, "");
      return temp;
    }
    String psnEmail = ConvertObjectUtils.objectToString(dataMap.get("psnEmail"));
    if (StringUtils.isBlank(psnEmail)) {
      logger.error("直接设置帐号关联失败-psnEmail不能为空,服务码:3hst8glw");
      temp = super.errorMap("直接设置帐号关联失败-psnEmail不能为空,服务码:3hst8glw", paramet, "");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    int flag = 0;
    String data = paramet.get(OpenConsts.MAP_DATA).toString();
    Map<String, String> dataMap =
        (Map<String, String>) JacksonUtils.jsonObject(data, new TypeReference<Map<String, String>>() {});
    String psnID = ConvertObjectUtils.objectToString(dataMap.get("psnID"));
    String psnGuidID = ConvertObjectUtils.objectToString(dataMap.get("psnGuidID"));
    String insID = ConvertObjectUtils.objectToString(dataMap.get("insID"));
    String psnEmail = ConvertObjectUtils.objectToString(dataMap.get("psnEmail"));
    try {

      // Long psnId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(psnID));
      Long psnId = NumberUtils.toLong(psnID);
      Long insId = NumberUtils.toLong(insID);
      Person person = personManager.getPerson(psnId);
      if (person != null && StringUtils.equals(psnEmail, person.getEmail())) {
        SysRolUser sysRolUser = new SysRolUser();
        sysRolUser.setInsId(insId);
        sysRolUser.setGuid(psnGuidID);
        sysRolUser.setPsnId(psnId);
        sysRolUser.setFlag(sysRolUser.FLAG_SNS_LIKE);// 直接关联
        sysRolUser.setLastDate(new Date());
        userService.saveSysRolUser(sysRolUser);
        flag = 1;
      } else {
        flag = 2;
      }

      Map<String, Object> temp = new HashMap<String, Object>();
      temp.put(OpenConsts.RESULT_FLAG, flag);
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
      dataList.add(temp);
      return super.successMap("直接设置帐号关联 ", dataList);
    } catch (Exception e) {
      logger.error(String.format("关联IRIS业务系统用户guid=%sSNS用户psnId=%s出现异常：", psnGuidID, psnID), e);
      throw new RuntimeException(e);
    }
  }

}
