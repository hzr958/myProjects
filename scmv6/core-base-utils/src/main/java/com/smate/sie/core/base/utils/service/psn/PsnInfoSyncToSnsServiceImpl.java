package com.smate.sie.core.base.utils.service.psn;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.smate.core.base.utils.dao.consts.Sie6InstitutionDao;
import com.smate.core.base.utils.exception.SysServiceException;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.consts.Sie6Institution;
import com.smate.sie.core.base.utils.model.psn.SieInsPerson;

/**
 * 同步人员信息到SNS服务实现
 * 
 * @author hd
 *
 */
@Service("psnInfoSyncToSnsService")
@Transactional(rollbackFor = Exception.class)
public class PsnInfoSyncToSnsServiceImpl implements PsnInfoSyncToSnsService {
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private Sie6InstitutionDao sie6InstitutionDao;

  @Resource(name = "restTemplate")
  private RestTemplate restTemplate;

  @Value("${initOpen.restful.url}")
  private String SERVER_URL;

  @SuppressWarnings({"finally"})
  @Override
  public String doSync(SieInsPerson psnIns) throws SysServiceException {
    String msg = "";
    try {
      if (psnIns != null && psnIns.getPk().getPsnId() != null) {
        Map<String, Object> mapSync = new HashMap<String, Object>();
        mapSync.put("openid", 99999999L);// 系统默认openId
        mapSync.put("token", "11111111syncfsie");
        mapSync.put("data", JacksonUtils.mapToJsonStr(buildSyncInfo(psnIns)));
        // 调用open接口
        Map<String, Object> resultMap =
            JacksonUtils.jsonToMap(restTemplate.postForObject(SERVER_URL, mapSync, Object.class).toString());
        if (!"success".equals(resultMap.get("status"))) {
          msg = "调用人员同步信息接口syncfsie失败:" + resultMap.get("msg") + ";";
          logger.error(msg);
        } else {
          msg = "调用人员同步信息接口syncfsie成功;";
        }

      } else {
        msg = "调用人员同步信息接口syncfsie失败:psnIns为null;";
      }
    } catch (Exception e) {
      logger.error("调用人员同步信息接口syncfsie异常: ", e);
      msg = "调用人员同步信息接口syncfsie异常:" + StringUtils.substring(e.toString(), 0, 100) + ";";
    } finally {
      return msg;
    }

  }

  /**
   * 构造需要同步的数据
   * 
   * @param psnIns
   * @return
   */
  private Map<String, Object> buildSyncInfo(SieInsPerson psnIns) throws SysServiceException {
    Map<String, Object> mapdata = new HashMap<String, Object>();
    try {
      Map<String, Object> data = new HashMap<String, Object>();
      // 获取人员所在机构
      Sie6Institution ins = sie6InstitutionDao.get(psnIns.getPk().getInsId());
      data.put("psnId", psnIns.getPk().getPsnId().toString());
      data.put("firstName", psnIns.getFirstName());
      data.put("lastName", psnIns.getLastName());
      data.put("name", psnIns.getZhName());
      data.put("tel", psnIns.getMobile());
      data.put("regionId", psnIns.getRegionId());
      data.put("sex", psnIns.getSex());
      data.put("position", psnIns.getPosition());
      data.put("posId", psnIns.getPosId());
      data.put("insId", psnIns.getPk().getInsId().toString());
      data.put("insName", ins == null ? ins : ins.getInsName());
      data.put("unitName", psnIns.getUnitName());
      mapdata.put("psnData", data);
    } catch (Exception e) {
      logger.error("调用人员同步信息接口syncfsie出错: 构造需要同步的数出错", e);
      throw new SysServiceException("调用人员同步信息接口syncfsie出错: 构造需要同步的数据出错", e);
    }
    return mapdata;
  }

  @SuppressWarnings("finally")
  @Override
  public String updatePersonByApprove(Long psnId, String email) throws SysServiceException {
    String msg = "";
    try {
      Map<String, Object> resultMap = JacksonUtils.jsonToMap(updatePsnApproveParam(email, psnId).toString());
      if ("success".equals(resultMap.get("status"))) {
        msg = "调用接口更新SNS的人员email信息成功;";
        logger.info("调用接口更新SNS的人员email信息成功;");
      } else {
        msg = "调用人员同步email信息接口syncpsn1失败:" + resultMap.get("msg") + ";";
        logger.error(msg);
      }

    } catch (Exception e) {
      msg = "调用人员同步email信息接口syncpsn1异常:" + StringUtils.substring(e.toString(), 0, 100) + ";";
      logger.error("调用人员同步email信息接口syncpsn1异常", e);
    } finally {
      return msg;
    }

  }

  private Object updatePsnApproveParam(String email, Long psnId) {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    mapDate.put("openid", 99999999L);// 系统默认openId
    mapDate.put("token", "11111111syncpsn1");// 系统默认token
    mapDate.put("data", buildPsnApproveParameter(email, psnId));
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  private String buildPsnApproveParameter(String email, Long psnId) {
    Map<String, Object> date = new HashMap<String, Object>();
    date.put("psnId", psnId);
    date.put("email", email);
    return JacksonUtils.mapToJsonStr(date);
  }

  @SuppressWarnings("finally")
  @Override
  public String updatePassword(Long psnId, String email, String password) throws SysServiceException {
    String msg = "";
    try {
      Map<String, Object> resultMap = JacksonUtils.jsonToMap(updateUsnApprove(psnId, email, password).toString());
      if ("success".equals(resultMap.get("status"))) {
        msg = "调用接口syncusn1更新CAS用户信息成功;";
        logger.info("调用接口syncusn1更新CAS用户信息成功;");
      } else {
        msg = "调用接口syncusn1更新CAS用户信息失败:" + resultMap.get("msg") + ";";
        logger.error(msg);
      }

    } catch (Exception e) {
      msg = "调用接口syncusn1更新CAS人员信息异常:" + StringUtils.substring(e.toString(), 0, 100) + ";";
      logger.error("调用接口syncusn1更新CAS人员信息异常", e);
    } finally {
      return msg;
    }

  }

  private Object updateUsnApprove(Long psnId, String email, String password) {
    Map<String, Object> mapDate = new HashMap<String, Object>();
    mapDate.put("openid", 99999999L);// 系统默认openId
    mapDate.put("token", "11111111syncusn1");// 系统默认token
    mapDate.put("data", buildUsnApproveParameter(psnId, email, password));
    return restTemplate.postForObject(SERVER_URL, mapDate, Object.class);
  }

  private String buildUsnApproveParameter(Long psnId, String email, String password) {
    Map<String, Object> date = new HashMap<String, Object>();
    date.put("psnId", psnId);
    date.put("email", email);
    date.put("password", password);

    return JacksonUtils.mapToJsonStr(date);
  }

}
