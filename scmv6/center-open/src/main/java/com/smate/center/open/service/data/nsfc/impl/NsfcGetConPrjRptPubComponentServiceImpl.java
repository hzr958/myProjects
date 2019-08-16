package com.smate.center.open.service.data.nsfc.impl;

import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.model.nsfc.continueproject.ConPrjRptPub;
import com.smate.center.open.service.data.nsfc.IsisNsfcDataHandleBase;
import com.smate.center.open.service.nsfc.continueproject.ConPrjRptPubService;
import com.smate.center.open.service.nsfc.continueproject.GetPrjRptPubXmlDocument;
import com.smate.center.open.utils.xml.WebServiceUtils;
import com.smate.core.base.utils.string.IrisStringUtils;

/**
 * 获取 延续报告 成果 接口
 * 
 * @author tsz
 *
 */
@Transactional(rollbackFor = Exception.class)
public class NsfcGetConPrjRptPubComponentServiceImpl extends IsisNsfcDataHandleBase {

  @Autowired
  private ConPrjRptPubService conPrjRptPubService;

  @Override
  public String doVerifyIsisData(Map<String, Object> dataParamet) throws Exception {
    String verifyResult = null;
    Object syncXml = dataParamet.get(OpenConsts.MAP_PARAM_XML);
    if (syncXml == null || "".endsWith(syncXml.toString())) {
      verifyResult = WebServiceUtils.setResut2("-3", "同步延续报告 Missing Parameter");
      return verifyResult;
    }
    GetPrjRptPubXmlDocument doc = this.parseXml(syncXml.toString());
    if (doc == null) {
      verifyResult = WebServiceUtils.setResut2("-3", "同步延续报告 参数对象转换失败");
      return verifyResult;
    }
    StringBuilder missParams = new StringBuilder();
    if (doc.getNsfcRptId() == null) {
      missParams.append("nsfcrPtId,");
    }
    if (missParams.length() > 0) {
      verifyResult = WebServiceUtils.returnMissParams(missParams.toString());
      return verifyResult;
    }
    dataParamet.put("doc", doc);
    return verifyResult;
  }

  @Override
  public String doHandlerIsisData(Map<String, Object> dataParamet) throws Exception {
    Long psnId = (Long) dataParamet.get(OpenConsts.MAP_PSNID);
    GetPrjRptPubXmlDocument doc = (GetPrjRptPubXmlDocument) dataParamet.get("doc");
    List<ConPrjRptPub> list = conPrjRptPubService.findRptPubs(psnId, doc.getNsfcRptId());
    return this.getResultXml(list);
  }


  /**
   * 拼装成果格式XML
   * 
   * @param list
   * @return
   * @throws DocumentException
   */
  private String getResultXml(List<ConPrjRptPub> list) throws DocumentException {
    String xml = WebServiceUtils.XML_HEAD + "<data><final_meta resultcode=\"isis_0\"/></data>";
    Document doc = DocumentHelper.parseText(xml);
    if (CollectionUtils.isEmpty(list)) {
      return xml;
    }

    Element root = doc.getRootElement();
    Element publications = root.addElement("publications");

    for (ConPrjRptPub rptPub : list) {
      Element pub = publications.addElement("publication");
      pub.addElement("seqNo").addText((rptPub.getSeqNo() == null ? "" : rptPub.getSeqNo().toString()));
      pub.addElement("pubType").addText((rptPub.getPubType() == null ? "" : rptPub.getPubType().toString()));
      pub.addElement("pubOwnerPsnId")
          .addText((rptPub.getPubOwnerPsnId() == null ? "" : rptPub.getPubOwnerPsnId().toString()));
      pub.addElement("pubId").addText((rptPub.getPubId() == null ? "" : rptPub.getPubId().toString()));
      pub.addElement("nodeId").addText("1");
      pub.addElement("listInfoSource").addText(rptPub.getListInfoSource() == null ? "" : rptPub.getListInfoSource());
      String autors = IrisStringUtils.filterIllegalXmlChar(rptPub.getAuthors());
      pub.addElement("authors").addText(autors == null ? "" : autors);
      pub.addElement("listInfo").addText(rptPub.getListInfo() == null ? "" : rptPub.getListInfo());
      String title = IrisStringUtils.filterIllegalXmlChar(rptPub.getTitle());
      pub.addElement("title").addText(title == null ? "" : title);
      String source = IrisStringUtils.filterIllegalXmlChar(rptPub.getSource());
      pub.addElement("source").addText(source == null ? "" : source);
      String impactFactors = IrisStringUtils.filterIllegalXmlChar(rptPub.getImpactFactors());
      pub.addElement("impactFactors").addText(impactFactors == null ? "" : impactFactors);
      pub.addElement("pubTypeName").addText(rptPub.getPubTypeName() == null ? "" : rptPub.getPubTypeName());
      pub.addElement("pubYear").addText(rptPub.getPubYear() == null ? "" : rptPub.getPubYear().toString());
      pub.addElement("citedTimes").addText(rptPub.getCitedTimes() == null ? "" : rptPub.getCitedTimes().toString());
      pub.addElement("isTag").addText(rptPub.getIsTag() == null ? "" : rptPub.getIsTag().toString());
      pub.addElement("pubMoth").addText(rptPub.getPubMoth() == null ? "" : rptPub.getPubMoth().toString());
      pub.addElement("pubDay").addText(rptPub.getPubDay() == null ? "" : rptPub.getPubDay().toString());
    }
    return doc.asXML();

  }



  /**
   * 解析参数
   * 
   * @param xmlData
   * @return
   * @throws DocumentException
   */
  private GetPrjRptPubXmlDocument parseXml(String xmlData) throws DocumentException {
    GetPrjRptPubXmlDocument doc = new GetPrjRptPubXmlDocument(xmlData);
    return doc;
  }
}
