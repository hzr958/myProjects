package com.smate.center.open.service.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.data.OpenThirdRegDao;
import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.dao.data.OpenUserUnunionLogDao;
import com.smate.center.open.exception.OpenSerUnunionException;
import com.smate.center.open.model.OpenThirdReg;
import com.smate.center.open.model.OpenUserUnunionLog;
import com.smate.center.open.service.login.ThirdLoginService;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.model.security.Person;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;


/**
 * Open系统解除关联，创建关联关系 调整
 * 
 * @date 2018-11-06
 * @author ajb
 *
 */

@Transactional(rollbackFor = Exception.class)
public class ThridUnbindOpenUserImpl extends ThirdDataTypeBase {


  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private ThirdLoginService thirdLoginService;
  @Autowired
  private OpenUserUnunionLogDao openUserUnunionLogDao;
  @Autowired
  private OpenThirdRegDao openThirdRegDao;
  @Autowired
  private PersonDao personDao;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    Map<String, Object> temp = new HashMap<String, Object>();

    Map<String, Object> serviceData = super.checkDataMapParamet(paramet, temp);
    if (temp.get(OpenConsts.RESULT_STATUS) != null
        && OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      // 校验公用参数
      return temp;
    }
    Object targetToken = serviceData.get("targetToken");
    if (targetToken == null || StringUtils.isBlank(targetToken.toString()) || targetToken.toString().length() != 8) {
      logger.error("服务类型参数 targetToken不能为空,必须为8位");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2016, paramet, "服务类型参数targetToken不能为空,必须为8位");
      return temp;
    }
    OpenThirdReg openThirdReg = openThirdRegDao.getOpenThirdRegByToken(targetToken.toString());
    if (openThirdReg == null) {
      logger.error("具体服务类型参数targetToken不存在");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2019, paramet, "服务类型参数targetToken不存在");
      return temp;
    }
    Object relation = serviceData.get("relation");
    if (relation == null || !checkIsBoolean(relation.toString())) {
      logger.error("服务类型参数relation不能为空，必须为true or false");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2018, paramet, "服务类型参数relation不能为空，必须为true or false");
      return temp;
    }
    Object psnId = serviceData.get("psnId");
    Object openIdToPsnId = paramet.get("psnId");
    if (openIdToPsnId == null) {
      if (psnId == null || !org.apache.commons.lang3.math.NumberUtils.isCreatable(psnId.toString())) {
        logger.error("服务类型参数 psnId不能为空");
        temp = super.errorMap(OpenMsgCodeConsts.SCM_2017, paramet, "服务类型参数psnId不能为空，且为数字");
        return temp;
      }
    } else {
      psnId = openIdToPsnId;
    }
    Person person = personDao.findPerson(NumberUtils.toLong(psnId.toString()));
    if (person == null) {
      logger.error("服务类型参数psnId不存在");
      temp = super.errorMap(OpenMsgCodeConsts.SCM_2020, paramet, "服务类型参数psnId不存在");
      return temp;
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    paramet.putAll(serviceData);
    return temp;
  }

  public boolean checkIsBoolean(String str) {
    if (StringUtils.isBlank(str)) {
      return false;
    } else if (str.equalsIgnoreCase("true")) {
      return true;
    } else if (str.equalsIgnoreCase("false")) {
      return true;
    } else {
      return false;
    }

  }

  // 删除联系 ， 能到这里就一定存在绑定关系
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {

      Map<String, Object> temp = new HashMap<String, Object>();
      String targetToken = paramet.get("targetToken").toString();
      Long psnId = NumberUtils.toLong(paramet.get("psnId").toString());
      Boolean relation = BooleanUtils.toBoolean(paramet.get("relation").toString());
      OpenUserUnion oldOpenUserUnion = openUserUnionDao.getByPsnIdToken(psnId, targetToken);
      List<Map<String, Object>> dataList = new ArrayList<>();
      Map<String, Object> data = new HashMap<>();
      dataList.add(data);
      if (relation) {// 添加关联
        if (oldOpenUserUnion == null) {
          Long openId = thirdLoginService.getOpenId(targetToken, psnId, 7);
          data.put(OpenConsts.MAP_OPENID, openId);
          temp.put(OpenConsts.RESULT_MSG, "添加关联成功");// 响应成功
        } else {
          data.put(OpenConsts.MAP_OPENID, oldOpenUserUnion.getOpenId());
          temp.put(OpenConsts.RESULT_MSG, "已存在该关联账号，添加关联失败");// 响应失败
        }

      } else {// 取消关联
        if (oldOpenUserUnion != null) {
          OpenUserUnunionLog openUserUnunionLog = new OpenUserUnunionLog();
          openUserUnunionLog.setPsnId(oldOpenUserUnion.getPsnId());
          openUserUnunionLog.setOpenId(oldOpenUserUnion.getOpenId());
          openUserUnunionLog.setToken(oldOpenUserUnion.getToken());
          openUserUnunionLog.setLogDate(new Date());
          openUserUnunionLog.setMsg("取消关联成功！");
          openUserUnunionLogDao.save(openUserUnunionLog);
          openUserUnionDao.delete(oldOpenUserUnion);
          data.put(OpenConsts.MAP_OPENID, oldOpenUserUnion.getOpenId());
          temp.put(OpenConsts.RESULT_MSG, "取消关联成功");// 响应成功
        } else {
          temp.put(OpenConsts.RESULT_MSG, "不存在该关联账号，取消关联失败");// 响应失败
        }
      }

      temp.put(OpenConsts.RESULT_DATA, dataList);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      return temp;
    } catch (Exception e) {
      logger.error("添加or解除绑定失败", e);
      throw new OpenSerUnunionException("添加or解除绑定失败 ", e);
    }
  }

}
