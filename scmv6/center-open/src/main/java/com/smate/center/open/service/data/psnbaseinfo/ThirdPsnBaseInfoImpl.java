package com.smate.center.open.service.data.psnbaseinfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.exception.OpenSerGetPsnInfoException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.core.base.utils.dao.security.PersonDao;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.security.Person;

/**
 * 个人基本信息服务实现
 * 
 * @author tsz
 *
 */

@Transactional(rollbackFor = Exception.class)
public class ThirdPsnBaseInfoImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private Map<String, ThirdPsnBaseInfoExtendService> serviceMap;

  @Autowired
  private PersonDao personDao;

  @SuppressWarnings("unchecked")
  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    // 没有 参数校验 直接返回成功
    Map<String, Object> temp = new HashMap<String, Object>();

    Object data = paramet.get(OpenConsts.MAP_DATA);
    if (data != null && data.toString().length() > 0) {
      Map<String, Object> dataMap = JacksonUtils.jsonToMap(data.toString());
      if (dataMap != null) {
        paramet.putAll(dataMap);
      }
    }
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);

    return temp;
  }

  /**
   * 获取个人基本信息 主要有{name:X,avatars:X,email:X}
   * 
   * @param psnId
   * @throws OpenException
   */
  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      Map<String, Object> temp = new HashMap<String, Object>();

      Object o = paramet.get(OpenConsts.MAP_PSNID);
      Long psnId = Long.parseLong(o.toString());
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
      // start 构建成员信息开始
      if (psnId != null) {
        Map<String, Object> data = buildPersonInfo(paramet, psnId);
        dataList.add(data);
      } else {
        // 关键词查询人员信息
        findPersonListInfoBySearchKey(paramet, dataList);
      }
      // end 构建成员信息结束
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_000);// 响应成功
      temp.put(OpenConsts.RESULT_DATA, dataList);
      return temp;
    } catch (Exception e) {
      logger.error("获取人员基本信息服务异常 map=" + paramet.toString() + e.toString(), e);
      throw new OpenSerGetPsnInfoException(e);
    }
  }

  /**
   * 通过关键词查询用户信息
   * 
   * @param paramet
   * @param dataList
   */
  private void findPersonListInfoBySearchKey(Map<String, Object> paramet, List<Map<String, Object>> dataList) {
    // psnId为空则，模糊查询
    Object searchKeyObj = paramet.get("searchKey");
    Map<String, Object> extraMap = new HashMap<String, Object>();
    if (searchKeyObj == null || StringUtils.isBlank(searchKeyObj.toString())) {
      // 为空
      extraMap.put("count", 0);
      extraMap.put("totalPages", 0);
      dataList.add(extraMap);
    } else {
      Object pageNoObj = paramet.get("pageNo");
      Object pageSizeObj = paramet.get("pageSize");
      String searchKey = searchKeyObj.toString().trim();
      Integer pageNo = 1;
      Integer pageSize = 10;
      if (pageNoObj != null && NumberUtils.isNumber(pageNoObj.toString())) {
        pageNo = Integer.parseInt(pageNoObj.toString());
      }
      if (pageSizeObj != null && NumberUtils.isNumber(pageSizeObj.toString())) {
        pageSize = Integer.parseInt(pageSizeObj.toString());
      }
      Long count = personDao.findPsnIdCount(searchKey);
      if (count == null || count == 0L) {
        extraMap.put("count", 0);
        extraMap.put("totalPages", 0);
      } else {
        extraMap.put("count", count);
        extraMap.put("totalPages", count % pageSize == 0 ? count / pageSize : count / pageSize + 1);
      }
      dataList.add(extraMap);
      List<Long> psnIdList = personDao.findPsnIdList(searchKey, pageNo, pageSize);
      if (psnIdList != null && psnIdList.size() > 0) {
        for (Long personId : psnIdList) {
          Map<String, Object> data = buildPersonInfo(paramet, personId);
          dataList.add(data);
        }
      }
    }
  }

  private Map<String, Object> buildPersonInfo(Map<String, Object> paramet, Long psnId) {
    Person person = personDao.get(psnId);
    // 需要改造 通过参数 不一样的参数 调用不一样的服务 得到不一样的返回结果
    Map<String, Object> infoMap = new HashMap<String, Object>();
    if (person == null) {
      infoMap.put("psnId", "psnId查询不到结果！");
      return infoMap;
    }
    Long openId = Long.parseLong(paramet.get(OpenConsts.MAP_OPENID).toString());
    infoMap = getPersonBaseMap(person);
    Map<String, Object> dataMap = JacksonUtils.jsonToMap(paramet.get(OpenConsts.MAP_DATA).toString());
    ThirdPsnBaseInfoExtendService thirdPsnBaseInfoBaseService = serviceMap.get(dataMap.get(OpenConsts.MAP_DATA_TYPE));
    if (thirdPsnBaseInfoBaseService != null) {
      infoMap.putAll(thirdPsnBaseInfoBaseService.extendPsnInfo(person, openId));
    }
    return infoMap;
  }

  /**
   * 基础信息map
   * 
   * @param psnId
   * @return Map<String, String>
   */
  private Map<String, Object> getPersonBaseMap(Person person) {
    Map<String, Object> map = new HashMap<String, Object>();
    if (StringUtils.isNotBlank(person.getName())) {
      map.put(BaseInfoConsts.RESULT_MAP_NAME, person.getName());
    } else if (StringUtils.isNotBlank(person.getFirstName()) && StringUtils.isNotBlank(person.getLastName())) {
      map.put(BaseInfoConsts.RESULT_MAP_NAME, person.getFirstName() + " " + person.getLastName());
    } else if (StringUtils.isNotBlank(person.getEname())) {
      map.put(BaseInfoConsts.RESULT_MAP_NAME, person.getEname());
    } else {
      map.put(BaseInfoConsts.RESULT_MAP_NAME, "");
    }
    map.put(BaseInfoConsts.RESULT_MAP_AVATARS, StringUtils.isNotBlank(person.getAvatars()) ? person.getAvatars() : "");
    map.put(BaseInfoConsts.RESULT_MAP_EMAIL, StringUtils.isNotBlank(person.getEmail()) ? person.getEmail() : "");
    return map;
  }

  public void setServiceMap(Map<String, ThirdPsnBaseInfoExtendService> serviceMap) {
    this.serviceMap = serviceMap;
  }

}
