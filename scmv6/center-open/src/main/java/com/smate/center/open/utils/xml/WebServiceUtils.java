package com.smate.center.open.utils.xml;

import com.smate.center.open.model.group.GroupPsn;
import com.smate.center.open.model.nsfc.NsfcSyncProject;
import com.smate.center.open.model.nsfc.ReschProjectReportPubModel;
import com.smate.center.open.model.nsfc.SyncProposalModel;
import com.smate.center.open.model.prj.ThirdPrjInfoTemp;
import com.smate.center.open.model.register.PersonRegister;
import com.smate.center.open.model.third.psn.ThirdPsnInfoTemp;
import com.smate.core.base.utils.string.ServiceUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

/**
 * 成果在线xml数据处理工具 返回数据的构造，参数的转换
 * 
 * @author tsz
 *
 */

public class WebServiceUtils {
  protected static final Logger logger = LoggerFactory.getLogger(WebServiceUtils.class);
  public static final String SYSN_ROL_PERSON = "syncRolPerson";
  public static String XML_HEAD = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

  public static final String SYNC_ROL_PROJECT = "syncRolProject";
  public static final String RESET_NSFC_PWD = "RESET_NSFC_PWD";
  public static final String SYNC_PRP_INFO = "syncPrpInfo";
  public static final String key = "111111111155555555555999999999999";
  public static final long ROL_NSFC = 2565L;
  public static final long ROL_GX = 26000L;
  public static final long ROL_NS = 110000L;
  public static final String SYNC_NSFC_PLAN = "syncNsfcPlan";


  public static String getResutlError(String syncType) {
    StringBuffer sb = new StringBuffer();
    if (SYSN_ROL_PERSON.equals(syncType)) {
      sb.append("<return result=\"-1\"/>");
    }
    if (SYNC_ROL_PROJECT.equals(syncType)) {
      sb.append("<return result=\"false\"/>");
    }
    if (RESET_NSFC_PWD.equals(syncType)) { // isis获取基金委成果在线账号密码

      sb.append("<return result=\"-2\"/>");

    }
    if (SYNC_PRP_INFO.equals(syncType)) { // 同步杰青申报书信息

      sb.append(setResut2("-3", "Missing Parameter"));
    }
    if (SYNC_NSFC_PLAN.equalsIgnoreCase(syncType)) {

      sb.append(setResut2("-5", "nsfc plan  param Illegal  error"));

    }

    return sb.toString();
  }

  public static String setResutl(String value) {
    return "<return result=\"" + value + "\"/>";
  }

  public static String setResut2(String value, String des) {
    return "<return result=\"" + value + "\"  des=\"" + des + "\"/>";
  }

  public static String setResutl(String loginName, String pass) {
    return "<return  result=\"1\"  username=\"" + loginName + "\"  plainpwd=\"" + pass + "\" />";
  }

  public static String setResutKV(String value, String date) {

    return "<return result=\"" + value + "\"  datestr=\"" + date + "\"/>";

  }

  /**
   * 返回缺少参数的错误结果
   * 
   * @param missParams
   * @return
   */
  public static String returnMissParams(String missParams) {
    return XML_HEAD + "<result value=\"2\"><errorMsg>Missing Parameter:" + missParams + "</errorMsg><result>";
  }


  public static String setResult3(String value, String des) {
    return "<root><results result=\"" + value + "\"  des=\"" + des + "\"/> </root>";
  }

  private static void elementToObj(Element element, Object object) {
    Iterator elements = element.elementIterator();
    while (elements.hasNext()) {

      Element ele = (Element) elements.next();
      String nae = ele.getText();
      logger.debug("name->" + ele.getName() + "     text->" + ele.getText());
      setObjProperty(object, ele.getName(), ele.getText() == null ? "" : ele.getText());
    }
  }

  private static void setObjProperty(Object entity, String propertyName, Object value) {
    try {
      Assert.notNull(entity, "entity不能为空");
      Assert.hasText(propertyName, "propertyName不能为空");
      Method[] methods = entity.getClass().getDeclaredMethods();
      String methodName;
      String methodNameFix;
      for (int i = 0; i < methods.length; i++) {
        methodName = methods[i].getName();
        methodNameFix = methodName.substring(3, methodName.length());
        methodNameFix = methodNameFix.toLowerCase();
        if (methodName.startsWith("set")) {
          Type[] type = methods[i].getGenericParameterTypes();// 获取方法的参数类型。
          if (methodNameFix.equals(propertyName.toLowerCase())) {
            try {
              if (type[0].getTypeName().equals("java.lang.Integer")) {
                methods[i].invoke(entity, NumberUtils.toInt(Objects.toString(value)));
              } else if (type[0].getTypeName().equals("java.lang.Long")) {
                methods[i].invoke(entity, NumberUtils.toLong(Objects.toString(value)));
              } else if(type[0].getTypeName().equals("java.lang.Boolean") && StringUtils.isNotBlank(Objects.toString(value))){
                methods[i].invoke(entity, Boolean.parseBoolean(Objects.toString(value)));
              }else {
                methods[i].invoke(entity, new Object[] {value});
              }
            } catch (IllegalArgumentException e) {
              logger.error("entity_propertyName:{},propertyName:{},value:{}",
                  new Object[] {methodNameFix, propertyName, value});
              e.printStackTrace();
            } catch (IllegalAccessException e) {
              logger.error("entity_propertyName:{},propertyName:{},value:{}",
                  new Object[] {methodNameFix, propertyName, value});
              e.printStackTrace();
            } catch (InvocationTargetException e) {
              logger.error("entity_propertyName:{},propertyName:{},value:{}",
                  new Object[] {methodNameFix, propertyName, value});
              e.printStackTrace();
            }
            continue;
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static Object strToObject(String syncXml, Object object) throws Exception {
    try {
      // 当匹配到特殊字符时候进行特殊字符转义，如果已经转义的话先还原在进行转义
      String regEx = "[&']";
      Pattern pattern = Pattern.compile(regEx);
      java.util.regex.Matcher mathche = pattern.matcher(syncXml);
      if (mathche != null) {
        syncXml = syncXml.replace("&amp;", "&");
        syncXml = syncXml.replace("&apos;", "'");
        syncXml = syncXml.replace("&", "&amp;");
        syncXml = syncXml.replace("'", "&apos;");
      }
      Document doc = null;

      doc = DocumentHelper.parseText(syncXml);

      Element root = doc.getRootElement();
      List list = root.elements();
      for (Iterator it = list.iterator(); it.hasNext();) {
        Element ele = (Element) it.next();
        elementToObj(ele, object);
      }

    } catch (DocumentException e) {
      e.printStackTrace();
      throw e;
    }
    return object;
  }

  private static Object mapToObject(Map<String, String> syncMap, Object object) throws Exception {

    Set<Entry<String, String>> entrySet = syncMap.entrySet();
    if (entrySet != null && entrySet.size() > 0) {
      for (Entry<String, String> entry : entrySet) {
        logger.debug("name->" + entry.getKey() + "     text->" + entry.getValue());
        setObjProperty(object, entry.getKey(), entry.getValue());
      }
    }



    return object;
  }

  public static NsfcSyncProject toNsfcSyncProject(String syncXml) throws Exception {
    return (NsfcSyncProject) strToObject(syncXml, new NsfcSyncProject());
  }

  public static SyncProposalModel toSyncPrpModel(String syncXml) throws Exception {
    return (SyncProposalModel) strToObject(syncXml, new SyncProposalModel());
  }

  public static PersonRegister toRegisterPerson(String syncXml) throws Exception {
    return (PersonRegister) strToObject(syncXml, new PersonRegister());
  }

  public static ThirdPsnInfoTemp toThirdPsnInfoTemp(String syncXml) throws Exception {
    return (ThirdPsnInfoTemp) strToObject(syncXml, new ThirdPsnInfoTemp());
  }

  public static ThirdPrjInfoTemp toThirdPrjInfoTemp(String syncXml) throws Exception {
    return (ThirdPrjInfoTemp) strToObject(syncXml, new ThirdPrjInfoTemp());
  }


  public static ReschProjectReportPubModel toReschProjectReportPubModel(String syncXml) throws Exception {
    return (ReschProjectReportPubModel) strToObject(syncXml, new ReschProjectReportPubModel());
  }

  public static GroupPsn toCreateGroup(String syncXml) throws Exception {
    return (GroupPsn) strToObject(syncXml, new GroupPsn());
  }

  public static ThirdPsnInfoTemp toThirdPsnInfoTempByMap(Map<String, String> syncMap) throws Exception {
    return (ThirdPsnInfoTemp) mapToObject(syncMap, new ThirdPsnInfoTemp());
  }

  public static ThirdPrjInfoTemp toThirdPrjInfoTempByMap(Map<String, String> syncMap) throws Exception {
    return (ThirdPrjInfoTemp) mapToObject(syncMap, new ThirdPrjInfoTemp());
  }

  public static Boolean checkWsParams(String syncXml, String rolId) {
    boolean flag = true;
    if (StringUtils.isBlank(syncXml) || StringUtils.isBlank(rolId))
      flag = false;
    if (!NumberUtils.isNumber(rolId))
      flag = false;
    return flag;

  }

  public static Boolean compareKey(String paramDes3Key) {
    Boolean flag = false;
    try {
      if (StringUtils.isNotBlank(paramDes3Key)) {
        if (key.equals(ServiceUtil.decodeFromDes3(paramDes3Key))) {
          String test = ServiceUtil.decodeFromDes3(paramDes3Key);
          flag = true;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return flag;
  }

}
