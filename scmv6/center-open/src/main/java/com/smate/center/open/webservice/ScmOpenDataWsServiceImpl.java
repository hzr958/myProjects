package com.smate.center.open.webservice;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.consts.OpenMsgCodeConsts;
import com.smate.center.open.exception.OpenDataGetOpenUserUnionException;
import com.smate.center.open.exception.OpenDataGetThirdRegNameException;
import com.smate.center.open.exception.OpenDataSaveErrorLogException;
import com.smate.center.open.exception.OpenNsfcException;
import com.smate.center.open.exception.OpenProjectDataJsonException;
import com.smate.center.open.exception.OpenProjectDataJsonNullException;
import com.smate.center.open.exception.OpenProjectDataJsonStateException;
import com.smate.center.open.exception.OpenProjectDataJsonTypeException;
import com.smate.center.open.exception.OpenProjectDataXmlException;
import com.smate.center.open.exception.OpenSerCheckParameterException;
import com.smate.center.open.exception.OpenSerGetPsnInfoException;
import com.smate.center.open.exception.OpenSerGetWechatTokenException;
import com.smate.center.open.exception.OpenSerSaveWechatPsnMsgException;
import com.smate.center.open.exception.OpenSerSaveWechatPublicMsgException;
import com.smate.center.open.exception.OpenSerUnunionException;
import com.smate.center.open.exception.OpenSyncPsnException;
import com.smate.center.open.model.OpenErrorLog;
import com.smate.center.open.service.data.OpenErrorLogService;
import com.smate.center.open.service.data.ThirdDataService;

@WebService(serviceName = "scmopenws", portName = "scmOpenDataWsServicePort",
    endpointInterface = "com.smate.center.open.webservice.ScmOpenDataWsService",
    targetNamespace = "http://ws.server.iris.com")
public class ScmOpenDataWsServiceImpl implements ScmOpenDataWsService {

  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private ThirdDataService thirdDataService;
  @Autowired
  private OpenErrorLogService openErrorLogService;

  @Resource
  private WebServiceContext wsContext;

  /**
   * 获取开放数据
   * 
   * @param openData
   * @return 返回 格式 <?xml version="1.0" encoding="UTF-8"?> <smate> <status>XX</status> <msg>XXX</msg>
   *         <time>毫秒数</time> <dataList> <data name=XXX email=XXX avatars=XXX></data>
   *         <data name=XXX email=XXX avatars=XXX></data> <data name=XXX email=XXX avatars=XXX></data>
   *         ... </dataList> </smate> getOpenData 得到的map(status:X,msg:X,data:list<map>)
   */
  @Override
  public String getScmOpenData(String openId, String token, String data) {
    Map<String, Object> openData = new HashMap<String, Object>();
    openData.put(OpenConsts.MAP_OPENID, openId);
    openData.put(OpenConsts.MAP_TOKEN, token);
    openData.put(OpenConsts.MAP_DATA, data);
    // 记录响应开始时间 毫秒数
    Long startDate = new Date().getTime();
    try {
      Map<String, Object> returnMap;
      try {
        // 用户IP
        openData.put(OpenConsts.MAP_USER_IP, getJaxWsUserIP());
        openData.put(OpenConsts.REAUEST_TYPE, OpenConsts.REAUEST_TYPE_1);
        returnMap = thirdDataService.handleOpenData(openData);
      } catch (OpenDataGetThirdRegNameException e) {
        logger.error("从数据库获取第三方系统注册名称异常!");
        returnMap = error(OpenMsgCodeConsts.SCM_901, openData, e);
      } catch (OpenDataGetOpenUserUnionException e1) {
        logger.error("根据openId,token从数据库获取open人员关联对象异常!");
        returnMap = error(OpenMsgCodeConsts.SCM_902, openData, e1);
      } catch (OpenSerCheckParameterException e2) {
        logger.error("校验参数异常 ");
        returnMap = error(OpenMsgCodeConsts.SCM_501, openData, e2);
      } catch (OpenDataSaveErrorLogException e4) {
        logger.error("保存错误日志异常");
        returnMap = error(OpenMsgCodeConsts.SCM_903, openData, e4);
      } catch (OpenSerGetPsnInfoException e8) {
        logger.error("获取人员基本信息服务异常");
        returnMap = error(OpenMsgCodeConsts.SCM_502, openData, e8);
      } catch (OpenSerSaveWechatPsnMsgException e7) {
        logger.error("保存个人微信消息出错");
        returnMap = error(OpenMsgCodeConsts.SCM_503, openData, e7);
      } catch (OpenSerSaveWechatPublicMsgException e8) {
        logger.error("保存群发微信消息出错");
        returnMap = error(OpenMsgCodeConsts.SCM_504, openData, e8);
      } catch (OpenProjectDataJsonException e9) {
        logger.error("第三方接收项目JSON数据解析出错");
        returnMap = error(OpenMsgCodeConsts.SCM_521, openData, e9);
      } catch (OpenProjectDataXmlException e10) {
        logger.error("第三方接收项目Xml数据解析出错");
        returnMap = error(OpenMsgCodeConsts.SCM_522, openData, e10);
      } catch (OpenSerGetWechatTokenException e11) {
        logger.error("获取 微信 交互授权码 异常");
        returnMap = error(OpenMsgCodeConsts.SCM_505, openData, e11);
      } catch (OpenProjectDataJsonTypeException e12) {
        logger.error("第三方接收项目Xml数据解析-type格式出错");
        returnMap = error(OpenMsgCodeConsts.SCM_523, openData, e12);
      } catch (OpenProjectDataJsonStateException e12) {
        logger.error("第三方接收项目Xml数据解析-state格式出错");
        returnMap = error(OpenMsgCodeConsts.SCM_524, openData, e12);
      } catch (OpenProjectDataJsonNullException e13) {
        logger.error("第三方接收项目Xml数据解析-格式出错");
        returnMap = error(OpenMsgCodeConsts.SCM_525, openData, e13);
      } catch (OpenNsfcException e14) {
        logger.error("NSFC接口-出错");
        returnMap = error(e14.getMessage(), openData, e14);
      } catch (OpenSerUnunionException e15) {
        logger.error(OpenMsgCodeConsts.SCM_528);
        returnMap = error(OpenMsgCodeConsts.SCM_528, openData, e15);
      } catch (OpenSyncPsnException e17) {
        logger.error(OpenMsgCodeConsts.SCM_529);
        returnMap = error(OpenMsgCodeConsts.SCM_529, openData, e17);
      } catch (Exception e) {
        e.printStackTrace();
        logger.error("数据交互异常");
        returnMap = error(OpenMsgCodeConsts.SCM_301, openData, e);
      }
      String resutl = this.handleReturnData(returnMap, startDate);
      return resutl;

    } catch (Exception e) {
      // 内部报错时怎么处理
      logger.error("构建返回xml数据异常", e);
      error("scm-21002  构建返回xml数据异常", openData, e);
      Long time = new Date().getTime() - startDate;
      String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><status>error</status><msg>" + OpenMsgCodeConsts.SCM_303
          + "</msg><time>" + time + "</time><resultList></resultList>";
      return str;
    }
  }

  private Map<String, Object> error(String msg, Map<String, Object> openData, Exception e) {
    Map<String, Object> temp = new HashMap<String, Object>();
    temp.put(OpenConsts.RESULT_STATUS, OpenConsts.RESULT_STATUS_ERROR);
    temp.put(OpenConsts.RESULT_MSG, msg);
    // 记录错误日志
    try {
      OpenErrorLog openErrorLog = new OpenErrorLog();
      openErrorLog.setErrorDate(new Date());
      openErrorLog.setErrorFlag(msg);
      openErrorLog.setOpenId(
          openData.get(OpenConsts.MAP_OPENID) == null ? null : openData.get(OpenConsts.MAP_OPENID).toString());
      openErrorLog
          .setToken(openData.get(OpenConsts.MAP_TOKEN) == null ? null : openData.get(OpenConsts.MAP_TOKEN).toString());
      openErrorLog.setErrorInfo(openData.toString() + e.toString());
      openErrorLogService.saveOpenErrorLog(openErrorLog);
    } catch (Exception e1) {
      logger.error("保存错误日志异常 " + e1);
    }
    return temp;
  }

  /**
   * 封装结果
   * 
   * @param returnData
   * @return
   * @throws Exception
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  private String handleReturnData(Map<String, Object> returnData, Long startDate) throws Exception {
    try {
      String returnD = "";
      if (MapUtils.isNotEmpty(returnData)) {
        Document document = DocumentHelper.createDocument();
        Element smate = document.addElement(OpenConsts.RESULT_SMATE);
        Element statusElement = smate.addElement(OpenConsts.RESULT_STATUS);
        statusElement.setText(returnData.get(OpenConsts.RESULT_STATUS).toString());
        Element msgElement = smate.addElement(OpenConsts.RESULT_MSG);
        msgElement.setText(returnData.get(OpenConsts.RESULT_MSG).toString());
        Element openIdElement = smate.addElement(OpenConsts.MAP_OPENID);
        openIdElement.setText(
            returnData.get(OpenConsts.MAP_OPENID) == null ? "" : returnData.get(OpenConsts.MAP_OPENID).toString());
        if (returnData.get(OpenConsts.RESULT_DATA) != null) {
          handleXmlElement(smate, (List<Map>) returnData.get(OpenConsts.RESULT_DATA));
        }
        Element timeElement = smate.addElement(OpenConsts.RESULT_TIME);
        Long time = new Date().getTime() - startDate;
        timeElement.setText(time.toString());
        returnD = document.asXML();
      }
      logger.info("第三方系统获取数据入口(webservice)数据时成功");
      return returnD;
    } catch (Exception e) {
      logger.error("返回数据构造异常");
      throw new Exception(e);
    }
  }

  /**
   * 封装xml-data元素
   * 
   * @param document
   * @param data
   * @throws Exception
   */
  @SuppressWarnings("rawtypes")
  private void handleXmlElement(Element smate, List<Map> data) throws Exception {
    try {
      if (CollectionUtils.isNotEmpty(data)) {
        Element dataListElement = smate.addElement(OpenConsts.RESULT_DATALIST);
        for (Map map : data) {
          Element dataElement = dataListElement.addElement(OpenConsts.RESULT_RESULT);
          Iterator keyIterator = map.keySet().iterator();
          while (keyIterator.hasNext()) {
            Object key = keyIterator.next();
            Object value = map.get(key);
            Element temp = dataElement.addElement(key.toString());
            if (value != null) {
              // 判断 是不是xml文档对象 是的话 直接但一个节点 加入open系统的返回值结果中
              if (value instanceof Document) {
                Document document = (Document) value;
                Element docRoot = document.getRootElement();
                temp.add(docRoot);
              } else {
                // 判断是不是可以转化为xml文档对象 可以的话直接当一个节点加入
                if (value.toString().startsWith("<")) {
                  try {
                    Document doc = DocumentHelper.parseText(value.toString());
                    Element docRoot = doc.getRootElement();
                    temp.add(docRoot);
                  } catch (DocumentException e) {
                    temp.setText(StringEscapeUtils.escapeXml10(value.toString()));
                  }
                } else {
                  // StringEscapeUtils.escapeXml10()
                  temp.setText(value.toString()); // 这个方法
                  // ，已经做了转译
                }
              }
            }
          }
        }
      }
    } catch (Exception e) {
      logger.error("返回数据构造异常");
      throw new Exception(e);
    }
  }

  /**
   * 获取webservice用户的IP
   * 
   * @return
   */
  private String getJaxWsUserIP() {
    String userIP = "";
    try {
      MessageContext mc = wsContext.getMessageContext();
      Object headers = mc.get(MessageContext.HTTP_REQUEST_HEADERS);
      Map<String, Object> map = (Map) headers;
      Object ipObj = map.get("X-real-ip");
      if (ipObj != null) {
        userIP = ipObj.toString();
      }
    } catch (Exception e) {
      logger.error("获取webservice的IP异常", e);
    }
    return userIP;
  }

  public static void main(String[] args) {
    // System.out.println("<pub></pub>".startsWith("<"));
    System.out.println("<publications></publications>".startsWith("<"));
    // char c='<';
    // System.out.println((int)c);
    char c2 = '<';
    System.out.println((int) c2);
  }

}
