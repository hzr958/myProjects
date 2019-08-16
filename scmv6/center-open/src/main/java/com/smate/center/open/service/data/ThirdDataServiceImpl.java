package com.smate.center.open.service.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.cache.OpenCacheService;
import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.dao.data.OpenThirdRegDao;
import com.smate.center.open.dao.data.OpenTokenServiceConstDao;
import com.smate.center.open.dao.data.OpenUserUnionDao;
import com.smate.center.open.dao.login.OpenErrorLogDao;
import com.smate.center.open.dao.psn.PsnInsDao;
import com.smate.center.open.exception.OpenDataGetOpenUserUnionException;
import com.smate.center.open.exception.OpenDataGetThirdRegNameException;
import com.smate.center.open.exception.OpenException;
import com.smate.center.open.exception.OpenSerCheckParameterException;
import com.smate.center.open.model.OpenErrorLog;
import com.smate.center.open.model.OpenThirdReg;
import com.smate.center.open.model.OpenTokenServiceConst;
import com.smate.center.open.service.codec.OpenCodecService;
import com.smate.center.open.service.data.log.OpenDataHandleLogService;
import com.smate.center.open.service.login.ThirdLoginService;
import com.smate.center.open.service.user.SysRolUserService;
import com.smate.core.base.utils.json.JacksonUtils;
import com.smate.core.base.utils.model.cas.security.SysRolUser;
import com.smate.core.base.utils.model.wechat.OpenUserUnion;

/**
 * 开放数据 服务实现
 * 
 * @author tsz
 * 
 * @since 6.0.1
 * @version 6.0.1
 */
@Transactional(rollbackFor = Exception.class)
public class ThirdDataServiceImpl implements ThirdDataService {

  protected Logger logger = LoggerFactory.getLogger(getClass());

  private static final Long HALF_HOUR = 30 * 60 * 1000L; // 30分钟

  // 类型服务 map
  private Map<String, ThirdDataTypeService> typeMap;

  @Autowired
  private OpenThirdRegDao openThirdRegDao;
  @Autowired
  private OpenUserUnionDao openUserUnionDao;
  @Autowired
  private OpenErrorLogDao openErrorLogDao;
  @Autowired
  private ThirdLoginService thirdLoginService;
  @Autowired
  private SysRolUserService sysRolUserService;
  @Autowired
  private OpenCacheService openCacheService;
  @Autowired
  private OpenDataHandleLogService openDataHandleLogService;
  @Autowired
  private OpenTokenServiceConstDao openTokenServiceConstDao;

  @Autowired
  private HttpServletRequest request;

  @Resource
  private WebServiceContext wsContext;

  @Autowired
  private PsnInsDao psnInsDao;
  @Autowired
  private OpenCodecService openCodecService;

  /**
   * 取数据统一入口
   * 
   * @param map
   * @return
   * @throws Exception
   * @throws OpenException
   */
  @Override
  /* @NextDataSourceAnnotation(dataSource=DataSourceEnum.DB_CAS) */
  public Map<String, Object> handleOpenData(Map<String, Object> map) {
    Map<String, Object> result = new HashMap<String, Object>();
    // 首先判断 参数openid 是否为动态openid 如果是动态openid 需要转成静态的openid tsz
    this.checkDynamicOpenId(map);
    // 兼容Guid 1.通过guid取psnId 2.通过psnId取open 3.有openid直接用 没有就重新生成
    // 4.用openid替换guid tsz
    this.getOpenIdByGUID(map);
    // 判断 token openId type是否正确
    Map<String, Object> temp = this.checkParameter(map);
    if (OpenConsts.RESULT_STATUS_ERROR.equalsIgnoreCase(temp.get(OpenConsts.RESULT_STATUS).toString())) {
      saveErrorLog(map, temp);
      return temp;
    }
    map.put(OpenConsts.MAP_TYPE, temp.get(OpenConsts.MAP_TYPE));
    ThirdDataTypeService thirdDataTypeService = typeMap.get(map.get(OpenConsts.MAP_TYPE));
    if (thirdDataTypeService == null) {
      // 参数错误直接返回
      saveErrorLog(map, temp);
      logger.error("请求数据类型参数错误!" + map.get(OpenConsts.MAP_TYPE));
      result.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
      result.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_208);
      return result;
    }
    map.put(OpenConsts.MAP_PSNID, temp.get(OpenConsts.MAP_PSNID));
    // 根据服务编码判断是否对数据 data解密密 tsz
    openCodecService.checkParameterDecode(map);
    result = thirdDataTypeService.handleOpenDataForType(map);
    // 对结果进行加密
    openCodecService.checkParameterEncode(map, result);
    result.put(OpenConsts.MAP_OPENID, map.get(OpenConsts.MAP_OPENID));
    // 记录日志 成功日志 请求成功日志
    openDataHandleLogService.saveLog(map.get(OpenConsts.MAP_TOKEN).toString(), map.get(OpenConsts.MAP_TYPE).toString(),
        "");
    return result;
  }

  private void saveErrorLog(Map<String, Object> map, Map<String, Object> temp) {
    // 打印日志
    logger.error(JacksonUtils.mapToJsonStr(map));
    OpenErrorLog openErrorLog = new OpenErrorLog();
    openErrorLog.setErrorDate(new Date());
    openErrorLog.setErrorFlag(temp.get(OpenConsts.RESULT_MSG).toString());
    openErrorLog.setOpenId(map.get(OpenConsts.MAP_OPENID) == null ? null : map.get(OpenConsts.MAP_OPENID).toString());
    openErrorLog.setToken(map.get(OpenConsts.MAP_TOKEN) == null ? null : map.get(OpenConsts.MAP_TOKEN).toString());
    openErrorLog.setErrorInfo(JacksonUtils.mapToJsonStr(map));
    openErrorLogDao.saveOpenErrorLog(openErrorLog);

  }

  /**
   * 判断是否动态openId
   */
  private void checkDynamicOpenId(Map<String, Object> map) {
    // 判断顺序 请与 方法 checkParameter 判断顺序保持一致 tsz
    if (map.get(OpenConsts.MAP_OPENID) == null
        || map.get(OpenConsts.MAP_OPENID).toString().length() == OpenConsts.OPEN_LENGTH) {
      return;
    }
    if (map.get(OpenConsts.MAP_TOKEN) == null || map.get(OpenConsts.MAP_TOKEN).toString().length() == 0) {
      return;
    }
    if (map.get(OpenConsts.MAP_TOKEN).toString().length() != OpenConsts.TOKEN_MAXLENGTH) {
      return;
    }
    String tempToken = map.get(OpenConsts.MAP_TOKEN).toString().substring(0, OpenConsts.TOKEN_LENGTH);
    if (tempToken == null) {
      return;
    }
    // 取缓存
    Object obj = openCacheService.get(OpenConsts.DYN_OPENID_CACHE, map.get(OpenConsts.MAP_OPENID) + "_" + tempToken);
    if (obj != null) {
      /*
       * // 用缓存中的 openid 替换传入的动态openid 继续后面的操作 map.put(OpenConsts.DYN_OPENID,
       * map.get(OpenConsts.MAP_OPENID)); map.put(OpenConsts.MAP_OPENID, obj);
       */

      // 如果邀请码正确 创建一个关联记录
      Long openId = openUserUnionDao.getOpenIdByPsnId(Long.parseLong(obj.toString()));
      // 存在psnId ，找不到关联的openId , 就会生成一个openId
      if (openId == null) {
        openId =
            thirdLoginService.getOpenId(tempToken, Long.parseLong(obj.toString()), OpenConsts.OPENID_CREATE_TYPE_1);
      } else {
        // tsz 如果 openid 不为null就直接判断 当前openId 与当前 token能不能匹配到关联数据 如果不能
        // 就添加一条数据
        createOpenUnion(tempToken, Long.parseLong(obj.toString()), openId);
      }
      map.put(OpenConsts.DYN_OPENID, map.get(OpenConsts.MAP_OPENID));
      map.put(OpenConsts.MAP_OPENID, openId);
    }
  }

  /**
   * 通过juid 得到openId
   */
  private void getOpenIdByGUID(Map<String, Object> map) {
    // 判断顺序 请与 方法 checkParameter 判断顺序保持一致 tsz
    if (map.get(OpenConsts.MAP_OPENID) == null
        || map.get(OpenConsts.MAP_OPENID).toString().length() == OpenConsts.OPEN_LENGTH) {
      return;
    }
    if (map.get(OpenConsts.MAP_TOKEN) == null || map.get(OpenConsts.MAP_TOKEN).toString().length() == 0) {
      return;
    }
    if (map.get(OpenConsts.MAP_TOKEN).toString().length() != OpenConsts.TOKEN_MAXLENGTH) {
      return;
    }
    String tempToken = map.get(OpenConsts.MAP_TOKEN).toString().substring(0, OpenConsts.TOKEN_LENGTH);
    String tempType = map.get(OpenConsts.MAP_TOKEN).toString().substring(8, OpenConsts.TOKEN_MAXLENGTH);
    if (tempType == null) {
      return;
    }
    String sysname = openThirdRegDao.getThirdSysNameByToken(tempToken);
    if (sysname == null) {
      return;
    }
    // cas数据原 通过（guid） 找到 pnsId openId 可能是guid可能是openId
    SysRolUser sysRolUser = sysRolUserService.getSysRolUserByGuid(map.get(OpenConsts.MAP_OPENID).toString());
    if (sysRolUser == null) {
      return;
    }
    // !tempType.equals(OpenConsts.MAP_UNBUND_SERVICE
    Long openId = openUserUnionDao.getOpenIdByPsnId(sysRolUser.getPsnId());
    // 存在psnId ，找不到关联的openId , 就会生成一个openId
    if (openId == null) {
      openId = thirdLoginService.getOpenId(tempToken, sysRolUser.getPsnId(), OpenConsts.OPENID_CREATE_TYPE_1);
    } else {
      // tsz 如果 openid 不为null就直接判断 当前openId 与当前 token能不能匹配到关联数据 如果不能
      // 就添加一条数据
      createOpenUnion(tempToken, sysRolUser.getPsnId(), openId);
    }
    map.put(OpenConsts.MAP_GUID, map.get(OpenConsts.MAP_OPENID).toString());
    map.put(OpenConsts.MAP_OPENID, openId);
  }

  private void createOpenUnion(String tempToken, Long psnId, Long openId) {
    OpenUserUnion temp = openUserUnionDao.getOpenUserUnion(openId, tempToken);
    if (temp == null) {
      temp = new OpenUserUnion();
      temp.setCreateDate(new Date());
      temp.setOpenId(openId);
      temp.setCreateType(OpenConsts.OPENID_CREATE_TYPE_1);
      temp.setToken(tempToken);
      temp.setPsnId(psnId);
      openUserUnionDao.save(temp);
    }
  }

  /**
   * 判断调用 入口的参数 是否正确 １.token是否与数据库记录匹配 2.openid是否与数据库记录匹配
   * 
   * @param map
   * @return
   * @throws OpenException
   */
  private Map<String, Object> checkParameter(Map<String, Object> map) {
    try {
      Map<String, Object> temp = new HashMap<String, Object>();
      if (map.get(OpenConsts.MAP_OPENID) == null || map.get(OpenConsts.MAP_OPENID).toString().length() == 0) {
        logger.error("openid不能为空!");
        temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
        temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_201);
        return temp;
      }
      if (map.get(OpenConsts.MAP_TOKEN) == null || map.get(OpenConsts.MAP_TOKEN).toString().length() == 0) {
        logger.error("token不能为空!");
        temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
        temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_202);
        return temp;
      }
      if (map.get(OpenConsts.MAP_TOKEN).toString().length() != OpenConsts.TOKEN_MAXLENGTH) {
        logger.error("token格式不正确!");
        temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
        temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_203);
        return temp;
      }
      String tempToken = map.get(OpenConsts.MAP_TOKEN).toString().substring(0, OpenConsts.TOKEN_LENGTH);
      String tempType = map.get(OpenConsts.MAP_TOKEN).toString().substring(8, OpenConsts.TOKEN_MAXLENGTH);
      map.put(OpenConsts.MAP_TYPE, tempType);
      map.put(OpenConsts.MAP_TOKEN, tempToken);
      // 检查token 和 ip
      String userRealIP = getUserRealIp(map);
      OpenThirdReg openThirdReg = openThirdRegDao.getOpenThirdRegByToken((map.get(OpenConsts.MAP_TOKEN).toString()));
      if (openThirdReg == null || !matchPermitIp(userRealIP, openThirdReg.getPermitIp())) {
        logger.error("token权限未开放! openId=" + map.get(OpenConsts.MAP_OPENID) + "  来访者Ip=" + userRealIP);
        temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
        temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_204);
        map.put("userRealIP", userRealIP);
        return temp;
      }
      // token 后面的字符串
      if (map.get(OpenConsts.MAP_TYPE) == null) {
        logger.error("请求数据类型不能为空!");
        temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
        temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_205);
        return temp;
      }

      if (map.get(OpenConsts.MAP_OPENID).toString().length() != OpenConsts.OPEN_LENGTH
          || !NumberUtils.isDigits(map.get(OpenConsts.MAP_OPENID).toString())) {
        logger.error("openid格式不正确!");
        temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
        temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_206);
        return temp;
      }
      // 99999999 表示 没有真实用户的 数据交互 检查openId 和 tokenId 是否存在对应的数据
      if (!this.checkEnableSystemOpenId(map)) {
        OpenUserUnion openUserUnion = openUserUnionDao.getOpenUserUnion(
            Long.parseLong(map.get(OpenConsts.MAP_OPENID).toString()), map.get(OpenConsts.MAP_TOKEN).toString());
        // --------------------------------
        if (openUserUnion == null) {
          logger
              .error("openid不能正确的匹配到token对应的系统" + map.get(OpenConsts.MAP_OPENID) + ";" + map.get(OpenConsts.MAP_TOKEN));
          temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
          temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_207);
          return temp;
        }
        temp.put(OpenConsts.MAP_PSNID, openUserUnion.getPsnId());
        // 2018-06-21 检查insId需要用到psnId
        map.put(OpenConsts.MAP_PSNID, openUserUnion.getPsnId());
      }
      if (!checkEnableTokenAndServiceType(map)) {
        logger.error("业务系统未开通此服务" + map.get(OpenConsts.MAP_TOKEN) + ";" + map.get(OpenConsts.MAP_TYPE));
        temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
        temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_278);
        return temp;
      }

      if (!checkInsId(map)) {
        logger.error("没有加入单位,没有权限调用该接口" + map.get(OpenConsts.MAP_TOKEN) + ";" + map.get(OpenConsts.MAP_TYPE));
        temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
        temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_2005);
        return temp;
      }
      if (!checkAccessNum(map)) {
        logger.error("超过该接口的调用次数，稍后再试" + map.get(OpenConsts.MAP_TOKEN) + ";" + map.get(OpenConsts.MAP_TYPE));
        temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
        temp.put(OpenConsts.RESULT_MSG, OpenMsgCodeConsts.SCM_2014);
        return temp;
      }
      temp.put(OpenConsts.MAP_TYPE, tempType);
      temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_SUCCESS);
      temp.put(OpenConsts.RESULT_MSG, "true");
      return temp;
    } catch (OpenDataGetThirdRegNameException e) {
      logger.error("重数据库获取第三方系统注册名称异常!");
      throw new OpenDataGetThirdRegNameException(e);
    } catch (OpenDataGetOpenUserUnionException e1) {
      logger.error("根据openId,token从数据库获取open人员关联对象异常!");
      throw new OpenDataGetOpenUserUnionException(e1);
    } catch (Exception e3) {
      logger.error("校验参数异常 map=" + map.toString());
      throw new OpenSerCheckParameterException(e3);
    }
  }

  /**
   * 获取用户的真实IP
   * 
   * @return
   */
  private String getUserRealIp(Map<String, Object> dataMap) {
    String userRealIP = "";
    try {
      userRealIP = request.getHeader("X-Real-IP");
      if (StringUtils.isBlank(userRealIP)) {
        userRealIP = request.getRemoteAddr();
      }
    } catch (Exception e) {
      // webservice获取IP的方法
      if (dataMap.get(OpenConsts.MAP_USER_IP) != null) {
        userRealIP = dataMap.get(OpenConsts.MAP_USER_IP).toString();
      }
    }
    dataMap.put(OpenConsts.MAP_USER_IP, userRealIP);
    return userRealIP;
  }

  /**
   * 
   * 验证Ip是否允许 true允许
   * 
   * @return
   */
  private Boolean matchPermitIp(String userIp, String permitIp) {
    if (StringUtils.isBlank(permitIp) || StringUtils.isBlank(userIp)) {
      return false;
    }
    String[] ipList = permitIp.split(";");
    for (String ip : ipList) {
      if (userIp.contains(ip)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 判断 是否可以使用 系统级 openId 99999999
   * 
   * @param map
   * @return
   */
  private boolean checkEnableSystemOpenId(Map<String, Object> map) {
    boolean case1 = OpenConsts.SYSTEM_OPENID.toString().equals(map.get(OpenConsts.MAP_OPENID).toString());
    boolean case2 = false;
    for (String temp : OpenConsts.SYSTEM_OPENID_ENABLE_SERVICE) {
      if (temp.equalsIgnoreCase(map.get(OpenConsts.MAP_TYPE).toString())) {
        case2 = true;
        break;
      }
    }
    return case1 && case2;
  }

  /**
   * 检查token 和serviceType是否可用
   * 
   * @param dataMap
   * @return
   */
  private boolean checkEnableTokenAndServiceType(Map<String, Object> dataMap) {
    boolean flag = false;
    Long id = openTokenServiceConstDao.findIdBytokenAndServiceType(dataMap.get(OpenConsts.MAP_TOKEN).toString(),
        dataMap.get(OpenConsts.MAP_TYPE).toString());
    if (id != null && id != 0L) {
      flag = true;
    }
    return flag;
  }

  public void setTypeMap(Map<String, ThirdDataTypeService> typeMap) {
    this.typeMap = typeMap;
  }

  /**
   * 检查单位id权限
   * 
   * @return
   */
  private boolean checkInsId(Map<String, Object> dataMap) {
    // 99999999的用户不要验证insId
    if (dataMap.get(OpenConsts.MAP_OPENID).toString().equalsIgnoreCase(OpenConsts.SYSTEM_OPENID.toString())) {
      return true;
    }
    String token = dataMap.get(OpenConsts.MAP_TOKEN).toString();
    Long psnId = NumberUtils.toLong(dataMap.get(OpenConsts.MAP_PSNID).toString());
    String serviceType = dataMap.get(OpenConsts.MAP_TYPE).toString();
    // tsz 调整单位 id限制方案
    OpenTokenServiceConst serviceConst = openTokenServiceConstDao.findObjBytokenAndServiceType(token, serviceType);
    if (serviceConst.getInsId() != null && !psnInsDao.findPsnIsIns(psnId, serviceConst.getInsId())) {
      return false;
    }
    return true;
  }

  /**
   * 检查访问次数 当机构id不为空时
   * 
   * @return
   */
  private boolean checkAccessNum(Map<String, Object> dataMap) {
    String token = dataMap.get(OpenConsts.MAP_TOKEN).toString();
    String serviceType = dataMap.get(OpenConsts.MAP_TYPE).toString();
    // tsz 调整单位id限制方案
    OpenTokenServiceConst serviceConst = openTokenServiceConstDao.findObjBytokenAndServiceType(token, serviceType);
    if (serviceConst.getInsId() != null) {
      // 大于30分钟，重新计算
      Date now = new Date();
      if (serviceConst.getAccessDate() == null || serviceConst.getAccessNum() == null
          || serviceConst.getAccessMaxNum() == null) {
        // 参数 配置不正常 tsz
        return false;
      }
      if (now.getTime() - serviceConst.getAccessDate().getTime() > HALF_HOUR) {
        serviceConst.setAccessDate(now);
        serviceConst.setAccessNum(1);
        openTokenServiceConstDao.save(serviceConst);
        return true;
      }
      if (serviceConst.getAccessNum() >= serviceConst.getAccessMaxNum()) {
        return false;
      }
      serviceConst.setAccessNum(serviceConst.getAccessNum() + 1);
      openTokenServiceConstDao.save(serviceConst);
    }
    return true;
  }

  public static void main(String[] args) {

    /*
     * System.out.println("1234567890a".substring(0,8));
     * System.out.println("12345678".substring(8,"12345678".length()));
     * System.out.println(NumberUtils.isNumber("543543"));
     * System.out.println(NumberUtils.isDigits("543"));
     * System.out.println("ree@qq.32432".toUpperCase());
     */
  }
}
