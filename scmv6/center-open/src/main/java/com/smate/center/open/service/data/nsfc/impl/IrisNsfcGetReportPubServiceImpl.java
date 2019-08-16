package com.smate.center.open.service.data.nsfc.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.model.nsfc.project.NsfcPrjRptPub;
import com.smate.center.open.service.data.nsfc.IsisNsfcDataHandleBase;
import com.smate.center.open.service.nsfc.NsfcService;
import com.smate.center.open.service.nsfc.PubReportService;
import com.smate.core.base.utils.string.IrisStringUtils;

/**
 * 3.5获取进展/结题报告成果
 * 
 * @author ajb
 * 
 */
@Transactional(rollbackFor = Exception.class)
public class IrisNsfcGetReportPubServiceImpl extends IsisNsfcDataHandleBase {
  @Autowired
  private NsfcService nsfcService;



  @Autowired
  private PubReportService pubReportService;

  private long nsfcRptId;
  private String guid;
  private String rolId;

  /**
   * 老系统是guid 现在是openId与guid相同价值(一定不为空) 生成 psnId 在webservice时就做好了
   * 
   * @param nsfcPrjId：研究成果报告ID
   * @param rolId:系统标识ID，基金委是 2565 参数校验
   */
  @Override
  public String doVerifyIsisData(Map<String, Object> dataParamet) {
    String verifyResult = null;
    Object nsfcRptIdO = dataParamet.get(OpenConsts.MAP_NSFCRPTID);
    Object rolIdO = dataParamet.get(OpenConsts.MAP_ROLID); // 待定rolId

    if (dataParamet.get(OpenConsts.MAP_GUID) != null) {
      guid = dataParamet.get(OpenConsts.MAP_GUID).toString();
    }
    if (nsfcRptIdO != null && rolIdO != null) {
      nsfcRptId = NumberUtils.toLong(nsfcRptIdO.toString());
      rolId = rolIdO.toString();
    } else {
      verifyResult = genResultXml(null, "isis_2", nsfcRptId, guid, rolId);
    }
    if (nsfcRptId < 1 || NumberUtils.toLong(rolId) < 1) {
      verifyResult = genResultXml(null, "isis_2", nsfcRptId, guid, rolId);
    }



    return verifyResult;
  }

  /**
   * 数据操作
   * 
   * @param paramet 没用到 ，为了方便
   * @throws Exception
   */
  @Override
  public String doHandlerIsisData(Map<String, Object> paramet) throws Exception {


    // 通过 guid 和 rolId 去查询 psnId 所以重复了
    // SysRolUser sysRolUser = new SysRolUser(guid, NumberUtils.toLong(rolId));
    // long psnId = -1L ;
    // psnId = nsfcService.getSyncRolPerson(sysRolUser);

    List<NsfcPrjRptPub> pubs = null;
    try {
      pubs = pubReportService.getProjectFinalPubs(nsfcRptId);
      this.nsfcService.fillPFPubsAddtlProps(pubs);
    } catch (Exception e) {
      // 验证失败
      return genResultXml(null, "isis_9", nsfcRptId, guid, rolId);
    }
    String xmlResult = genResultXml(pubs, "isis_0", nsfcRptId, guid, rolId);
    return xmlResult;

  }



  private String genResultXml(List<NsfcPrjRptPub> pubs, String resultCode, long nsfcRptId, String guid, String rolId) {
    String result = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><data><final_meta guid=\"" + guid + "\" rolid=\"" + rolId
        + "\" resultcode=\"" + resultCode + "\"/></data>";
    if (CollectionUtils.isEmpty(pubs)) {
      return result;
    }
    try {
      Document doc = DocumentHelper.parseText(result);
      Element root = doc.getRootElement();
      Element publications = root.addElement("publications");
      for (NsfcPrjRptPub pub : pubs) {
        Element publication = publications.addElement("publication");
        createPublication(publication, pub);
      }
      return doc.asXML();
    } catch (DocumentException e) {
      result = result.replace("resultcode=\"" + resultCode + "\"", "resultcode=\"isis_9\"");
      return result;
    } catch (Exception e) {
      result.replace("resultcode=\"" + resultCode + "\"", "resultcode=\"isis_9\"");
      return result;
    }

  }

  @SuppressWarnings("rawtypes")
  private void createPublication(Element publication, NsfcPrjRptPub pub) throws Exception {

    try {
      Map pros = BeanUtils.describe(pub);
      for (Iterator iterator = pros.keySet().iterator(); iterator.hasNext();) {
        String key = ObjectUtils.toString(iterator.next());
        if ("id".equals(key)) {
          publication.addElement("rptId").addText(String.valueOf(pub.getId().getRptId()));
          publication.addElement("pubId").addText(String.valueOf(pub.getId().getPubId()));
        } else if ("additionalProperties".equalsIgnoreCase(key)) {
          Map<String, Object> additionalProperties = pub.getAdditionalProperties();
          if (additionalProperties != null) {
            for (Iterator it = additionalProperties.keySet().iterator(); it.hasNext();) {
              key = ObjectUtils.toString(it.next());
              String str = ObjectUtils.toString(additionalProperties.get(key));
              publication.addElement(key).addText(str);
            }
          }
        } else if (!"class".equalsIgnoreCase(key) && !"pubCw".equalsIgnoreCase(key) && !"serialVersionUID".equals(key)
            && !"source".equals(key)) {
          String str = ObjectUtils.toString(pros.get(key));
          // 出去特殊字符
          str = IrisStringUtils.filterIllegalXmlChar(str);
          if ("nsfcSource".equals(key)) {
            publication.addElement("source").addText(str);
          } else {
            publication.addElement(key).addText(str);
          }

        }
      }
    } catch (IllegalAccessException e) {
      throw new Exception(e.getMessage());
    } catch (InvocationTargetException e) {
      throw new Exception(e.getMessage());
    } catch (NoSuchMethodException e) {
      throw new Exception(e.getMessage());
    }

  }



}
