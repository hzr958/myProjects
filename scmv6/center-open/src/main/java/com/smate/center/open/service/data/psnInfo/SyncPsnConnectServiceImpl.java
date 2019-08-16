package com.smate.center.open.service.data.psnInfo;

import java.util.ArrayList;
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
import com.smate.center.open.service.email.PsnConCodeSendEmail;
import com.smate.center.open.service.profile.PersonManager;
import com.smate.center.open.service.profile.PsnInfoXmlService;
import com.smate.center.open.service.snscode.IrisSnsCodeService;
import com.smate.center.open.utils.ConvertObjectUtils;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;

/**
 * 发送帐号关联验证码邮件 2de4pka8
 * 
 * @author lyq
 *
 */
@Transactional(rollbackFor = Exception.class)
@Deprecated // 废弃 tsz
public class SyncPsnConnectServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonManager personManager;
  @Autowired
  private PsnInfoXmlService personXmlService;
  @Autowired
  private IrisSnsCodeService irisSnsCodeService;
  @Autowired
  private PsnConCodeSendEmail psnConCodeSendEmail;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();
    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data == null && StringUtils.isEmpty(data.toString())) {
      logger.error("发送帐号关联验证码邮件失败-数据不能为空,服务码:2de4pka8");
      temp = super.errorMap("发送帐号关联验证码邮件失败-数据不能为空,服务码:2de4pka8", paramet, "");
      return temp;
    }
    Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
    if (dataMap == null) {
      logger.error("发送帐号关联验证码邮件失败-参数格式不正确,不能转换成map,服务码:2de4pka8");
      temp = super.errorMap("发送帐号关联验证码邮件失败-参数格式不正确,不能转换成map,服务码:2de4pka8", paramet, "");
      return temp;
    }
    String psnID = ConvertObjectUtils.objectToString(dataMap.get("psnID"));
    if (StringUtils.isBlank(psnID)) {
      logger.error("发送帐号关联验证码邮件失败-psnId不能为空,服务码:2de4pka8");
      temp = super.errorMap("发送帐号关联验证码邮件失败-psnId不能为空,服务码:2de4pka8", paramet, "");
      return temp;
    } else {
      try {
        Long psnId = NumberUtils.toLong(psnID);
        Person person = personManager.getPerson(psnId);
        if (person == null) {
          logger.error("发送帐号关联验证码邮件失败-根据psnId查找person为空,服务码:2de4pka8");
          temp = super.errorMap("发送帐号关联验证码邮件失败-根据psnId查找person为空,服务码:2de4pka8", paramet, "");
          return temp;
        }
      } catch (Exception e) {
        logger.error("发送帐号关联验证码邮件失败-根据psnId查找person异常,服务码:2de4pka8");
        temp = super.errorMap("发送帐号关联验证码邮件失败-根据psnId查找person异常,服务码:2de4pka8", paramet, "");
        return temp;
      }
    }
    String psnGuidID = ConvertObjectUtils.objectToString(dataMap.get("psnGuidID"));
    if (StringUtils.isBlank(psnGuidID)) {
      logger.error("发送帐号关联验证码邮件失败-psnGuidID不能为空,服务码:2de4pka8");
      temp = super.errorMap("发送帐号关联验证码邮件失败-psnGuidID不能为空,服务码:2de4pka8", paramet, "");
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
    try {
      // Long psnId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(psnID));
      Long psnId = NumberUtils.toLong(psnID);
      Person person = personManager.getPerson(psnId);
      if (person != null) {
        String code = this.personXmlService.generateConnectedCode();
        this.irisSnsCodeService.saveIrisSnsCode(psnGuidID, psnId, code);// saveIrisSnsCode(guid, psnId, insId, code)
        psnConCodeSendEmail.syncEmailInfo(person, code);
        flag = 1;
      }
      Map<String, Object> temp = new HashMap<String, Object>();
      temp.put(OpenConsts.RESULT_FLAG, flag);
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
      dataList.add(temp);
      return super.successMap("发送帐号关联验证码邮件 ", dataList);
    } catch (Exception e) {
      logger.error(String.format("关联IRIS业务系统用户guid=%sSNS用户psnId=%s出现异常：", psnGuidID, psnID), e);
      throw new RuntimeException(e);
    }
  }

}
