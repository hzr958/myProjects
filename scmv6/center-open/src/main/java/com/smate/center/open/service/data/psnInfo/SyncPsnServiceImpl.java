package com.smate.center.open.service.data.psnInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.exception.OpenSerGetPsnInfoException;
import com.smate.center.open.service.data.ThirdDataTypeBase;
import com.smate.center.open.service.profile.PersonManager;
import com.smate.center.open.service.profile.PsnInfoXmlService;
import com.smate.core.base.utils.model.security.Person;

/**
 * 根据人员ID返回人员的记录 pd3ir4di
 * 
 * @author lyq
 *
 */
@Transactional(rollbackFor = Exception.class)
public class SyncPsnServiceImpl extends ThirdDataTypeBase {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PersonManager personManager;
  @Autowired
  private PsnInfoXmlService personXmlService;

  @Override
  public Map<String, Object> doVerify(Map<String, Object> paramet) {
    // 没有 参数校验 直接返回成功
    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
    return temp;
  }

  @Override
  public Map<String, Object> doHandler(Map<String, Object> paramet) {
    try {
      Map<String, Object> temp = new HashMap<String, Object>();
      Object o = paramet.get("psnId");
      Long psnId = (Long) o;
      List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
      Map<String, Object> psnListMap = new HashMap<String, Object>();
      String psnListXml = "<persons></persons>";
      Person person = personManager.getPerson(psnId);
      if (person != null) {
        List<Person> psnList = new ArrayList<Person>();
        psnList.add(person);
        psnListXml = personXmlService.buildPsnListXmlStr(psnList);
      }
      psnListMap.put("psnXml", psnListXml);
      dataList.add(psnListMap);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, "scm-0000");// 响应成功
      temp.put(OpenConsts.RESULT_DATA, dataList);
      return temp;
    } catch (Exception e) {
      logger.error("IRIS业务系统接口-通过人员psnId=%s查询用户出现异常 map=" + paramet.toString());
      throw new OpenSerGetPsnInfoException(e);
    }
  }

}
