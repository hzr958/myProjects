package com.smate.web.prj.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.smate.core.base.exception.ServiceException;
import com.smate.core.base.utils.constant.ConstDictionary;
import com.smate.core.base.utils.service.consts.ConstDictionaryManage;
import com.smate.core.base.utils.string.StringUtils;
import com.smate.web.prj.xml.PrjXmlConstants;
import com.smate.web.prj.xml.PrjXmlDocument;

/**
 * 公共刷新xml常数字段.
 * 
 * @author liqinghua
 * 
 */
public class PrjCommonConstFieldRefresh {

  /**
   * 
   */
  protected static final Logger LOGGER = LoggerFactory.getLogger(PrjCommonConstFieldRefresh.class);

  /**
   * 刷新XML.
   * 
   * @param xmlDocument
   * @param xmlDaoService
   */
  public static void refresh(PrjXmlDocument xmlDocument, ConstDictionaryManage constDictionaryManage) throws Exception {
    Assert.notNull(xmlDocument);
    Assert.notNull(constDictionaryManage);

    // 主导单位1/参与单位0
    String principalIns = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "is_principal_ins");
    if (StringUtils.isNotBlank(principalIns)) {
      try {
        ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("prj_is_principal", principalIns);
        if (cd == null) {
          throw new ServiceException("读取prj_is_principal常数错误，code=" + principalIns);
        } else {
          xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_is_principal_ins_name", cd.getZhCnName());
          xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_is_principal_ins_name", cd.getEnUsName());
        }
      } catch (Exception e) {
        throw e;
      }
    }
    String resource = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "source");
    String amount = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount_unit");
    if (StringUtils.isNotBlank(resource) && "CnkiFund".equals(resource) && StringUtils.isNotBlank(amount)) {
      xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "amount_unit", "RMB");
    }
    // 项目类型:内部项目1/外部项目0
    String prjType = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "prj_type");
    if (StringUtils.isNotBlank(principalIns) && ("0".equalsIgnoreCase(prjType) || "1".equalsIgnoreCase(prjType))) {
      try {
        ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("prj_type", prjType);
        if (cd == null) {
          throw new ServiceException("读取prj_type常数错误，code=" + prjType);
        } else {
          xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_prj_type_name", cd.getZhCnName());
          xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_prj_type_name", cd.getEnUsName());
        }
      } catch (Exception e) {
        throw e;
      }
    }

    // 项目状态01进行中/02完成/03其他
    String prjState = xmlDocument.getXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "prj_state");
    if (StringUtils.isNotBlank(prjState)) {
      try {
        ConstDictionary cd = constDictionaryManage.findConstByCategoryAndCode("prj_state", prjState);
        if (cd == null) {
          throw new ServiceException("读取prj_state常数错误，code=" + prjState);
        } else {
          xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "zh_prj_state_name", cd.getZhCnName());
          xmlDocument.setXmlNodeAttribute(PrjXmlConstants.PROJECT_XPATH, "en_prj_state_name", cd.getEnUsName());
        }
      } catch (Exception e) {
        throw e;
      }
    }
  }
}
