package com.smate.center.open.restful;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.EmailExistException;
import com.smate.center.open.exception.OpenDataGetOpenUserUnionException;
import com.smate.center.open.exception.OpenDataGetThirdRegNameException;
import com.smate.center.open.exception.OpenDataSaveErrorLogException;
import com.smate.center.open.exception.OpenDecodeException;
import com.smate.center.open.exception.OpenEncodeException;
import com.smate.center.open.exception.OpenNsfcException;
import com.smate.center.open.exception.OpenProjectDataJsonException;
import com.smate.center.open.exception.OpenProjectDataJsonNullException;
import com.smate.center.open.exception.OpenProjectDataJsonStateException;
import com.smate.center.open.exception.OpenProjectDataJsonTypeException;
import com.smate.center.open.exception.OpenProjectDataXmlException;
import com.smate.center.open.exception.OpenSearchPsnListException;
import com.smate.center.open.exception.OpenSerBuildDataException;
import com.smate.center.open.exception.OpenSerCheckParameterException;
import com.smate.center.open.exception.OpenSerGetAutoLoginIdException;
import com.smate.center.open.exception.OpenSerGetPsnInfoException;
import com.smate.center.open.exception.OpenSerGetWechatTokenException;
import com.smate.center.open.exception.OpenSerSaveWechatPsnMsgException;
import com.smate.center.open.exception.OpenSerSaveWechatPublicMsgException;
import com.smate.center.open.exception.OpenSerUnunionException;
import com.smate.center.open.exception.OpenSyncPsnException;
import com.smate.center.open.model.OpenErrorLog;
import com.smate.center.open.service.data.OpenErrorLogService;
import com.smate.center.open.service.data.ThirdDataService;
import com.smate.core.base.utils.json.JacksonUtils;

/**
 * 第三方系统获取数据入口(restful).
 * 
 * @author zk
 * @since 6.0.1
 *
 */
@RestController
public class ScmOpenDataController {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ThirdDataService thirdDataService;
  @Autowired
  private OpenErrorLogService openErrorLogService;

  /**
   * 获取开放数据.
   *
   * @return 返回格式 {status:X,msg:X,time:X(毫秒数),result:[{name:X, avatars:X,email:X }]} getOpenData
   *         得到的map(status:X,msg:X,data:list)
   *
   */
  @SuppressWarnings({})
  @RequestMapping(value = "/scmopendata", method = RequestMethod.POST)
  public String getScmOpenData(@RequestBody Map<String, Object> openData) {
    // 记录响应开始时间 毫秒数
    Long startDate = new Date().getTime();
    Map<String, Object> returnData = new HashMap<String, Object>();
    try {
      if (openData.size() > 3) {
        returnData.put(OpenConsts.RESULT_MSG, "参数格式不正确，只支持3个参数,详情请看文档");
      } else {
        openData.put(OpenConsts.REAUEST_TYPE, OpenConsts.REAUEST_TYPE_2);
        Map<String, Object> returnMap = thirdDataService.handleOpenData(openData);
        returnData.put(OpenConsts.RESULT_STATUS, returnMap.get(OpenConsts.RESULT_STATUS));
        returnData.put(OpenConsts.RESULT_MSG, returnMap.get(OpenConsts.RESULT_MSG));
        returnData.put(OpenConsts.MAP_OPENID,
            returnMap.get(OpenConsts.MAP_OPENID) == null ? "" : returnMap.get(OpenConsts.MAP_OPENID));
        returnData.put(OpenConsts.RESULT_RESULT, returnMap.get(OpenConsts.RESULT_DATA));
      }
    } catch (EmailExistException e) {
      returnData = error(OpenMsgCodeConsts.SCM_20361, openData, e);
    } catch (OpenDecodeException e) {
      logger.error("参数  data解密失败");
      returnData = error(OpenMsgCodeConsts.SCM_2035, openData, e);
    } catch (OpenEncodeException e) {
      logger.error("返回结果加密失败!");
      returnData = error(OpenMsgCodeConsts.SCM_2036, openData, e);
    } catch (OpenDataGetThirdRegNameException e) {
      logger.error("从数据库获取第三方系统注册名称异常!");
      returnData = error(OpenMsgCodeConsts.SCM_901, openData, e);
    } catch (OpenDataGetOpenUserUnionException e1) {
      logger.error("根据openId,token从数据库获取open人员关联对象异常!");
      returnData = error(OpenMsgCodeConsts.SCM_902, openData, e1);
    } catch (OpenSerCheckParameterException e2) {
      logger.error("校验参数异常 ");
      returnData = error(OpenMsgCodeConsts.SCM_501, openData, e2);
    } catch (OpenDataSaveErrorLogException e4) {
      logger.error("保存错误日志异常");
      returnData = error(OpenMsgCodeConsts.SCM_903, openData, e4);
    } catch (OpenSerBuildDataException e6) {
      logger.error("返回json格式数据构造异常");
      returnData = error(OpenMsgCodeConsts.SCM_302, openData, e6);
    } catch (OpenSerGetPsnInfoException e8) {
      logger.error("获取人员基本信息服务异常");
      returnData = error(OpenMsgCodeConsts.SCM_502, openData, e8);
    } catch (OpenSerSaveWechatPsnMsgException e7) {
      logger.error("保存个人微信消息出错");
      returnData = error(OpenMsgCodeConsts.SCM_503, openData, e7);
    } catch (OpenSerSaveWechatPublicMsgException e8) {
      logger.error("保存群发微信消息出错");
      returnData = error(OpenMsgCodeConsts.SCM_504, openData, e8);
    } catch (OpenProjectDataJsonException e9) {
      logger.error("第三方接收项目JSON数据解析出错");
      returnData = error(OpenMsgCodeConsts.SCM_521, openData, e9);
    } catch (OpenProjectDataXmlException e10) {
      logger.error("第三方接收项目Xml数据解析出错");
      returnData = error(OpenMsgCodeConsts.SCM_522, openData, e10);
    } catch (OpenSerGetWechatTokenException e11) {
      logger.error("获取 微信 交互授权码 异常");
      returnData = error(OpenMsgCodeConsts.SCM_505, openData, e11);
    } catch (OpenProjectDataJsonTypeException e12) {
      logger.error("第三方接收项目Xml数据解析-type格式出错");
      returnData = error(OpenMsgCodeConsts.SCM_523, openData, e12);
    } catch (OpenProjectDataJsonStateException e12) {
      logger.error("第三方接收项目Xml数据解析-state格式出错");
      returnData = error(OpenMsgCodeConsts.SCM_524, openData, e12);
    } catch (OpenProjectDataJsonNullException e13) {
      logger.error("第三方接收项目Xml数据解析-格式出错");
      returnData = error(OpenMsgCodeConsts.SCM_525, openData, e13);
    } catch (OpenNsfcException e14) {
      logger.error("NSFC接口-出错");
      returnData = error(e14.getMessage(), openData, e14);
    } catch (OpenSerUnunionException e15) {
      logger.error(OpenMsgCodeConsts.SCM_526);
      returnData = error(OpenMsgCodeConsts.SCM_526, openData, e15);
    } catch (OpenSearchPsnListException e16) {
      logger.error(OpenMsgCodeConsts.SCM_528);
      returnData = error(OpenMsgCodeConsts.SCM_528, openData, e16);
    } catch (OpenSyncPsnException e17) {
      logger.error(OpenMsgCodeConsts.SCM_529);
      returnData = error(OpenMsgCodeConsts.SCM_529, openData, e17);
    } catch (OpenSerGetAutoLoginIdException e18) {
      logger.error(OpenMsgCodeConsts.SCM_530);
      returnData = error(OpenMsgCodeConsts.SCM_530, openData, e18);
    } catch (Exception e5) {
      logger.error("第三方系统获取数据入口(restful)数据时出错", e5);
      returnData = error(OpenMsgCodeConsts.SCM_302, openData, e5);
    }
    // 记录响应结束时间 毫秒数
    // 把整个访问过程花的时间记录到返回结果中
    returnData.put(OpenConsts.RESULT_TIME, new Date().getTime() - startDate);
    String str = JacksonUtils.jsonObjectSerializerNoNull(returnData);
    return str;
  }

  private Map<String, Object> error(String msg, Map<String, Object> openData, Exception e) {
    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
    temp.put(OpenConsts.RESULT_MSG, msg);
    try {
      // 记录错误日志
      OpenErrorLog openErrorLog = new OpenErrorLog();
      openErrorLog.setErrorDate(new Date());
      openErrorLog.setErrorFlag(msg);
      openErrorLog.setOpenId(
          openData.get(OpenConsts.MAP_OPENID) == null ? null : openData.get(OpenConsts.MAP_OPENID).toString());
      openErrorLog
          .setToken(openData.get(OpenConsts.MAP_TOKEN) == null ? null : openData.get(OpenConsts.MAP_TOKEN).toString());
      openErrorLog.setErrorInfo(openData.toString() + e.getMessage() + "---" + e.toString());
      openErrorLogService.saveOpenErrorLog(openErrorLog);
    } catch (Exception e1) {
      logger.error("保存错误日志异常 " + e1);
    }
    return temp;
  }

  public ScmOpenDataController() {
    super();
  }

}
