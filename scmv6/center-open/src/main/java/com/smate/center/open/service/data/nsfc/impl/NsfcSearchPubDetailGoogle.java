package com.smate.center.open.service.data.nsfc.impl;


import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.consts.OpenConsts;
import com.smate.center.open.model.nsfc.NsfcwsPublication;
import com.smate.center.open.service.data.nsfc.IsisNsfcDataHandleBase;
import com.smate.center.open.service.google.GooglePubXmlBuildFactory;
import com.smate.center.open.service.nsfc.pub.NsfcwsPubService;
import com.smate.center.open.utils.xml.WebServiceUtils;



/**
 * :根据成果ID返回成果详情
 * 
 * @author ajb
 *
 */
@Transactional(rollbackFor = Exception.class)
public class NsfcSearchPubDetailGoogle extends IsisNsfcDataHandleBase {
  @Autowired
  private NsfcwsPubService nsfcwsPubService;

  @Autowired
  private GooglePubXmlBuildFactory nsfcPubXmlBuildFactory;

  /**
   * 
   */
  @Override
  public String doVerifyIsisData(Map<String, Object> dataParamet) throws Exception {
    // String psnID, pubIDS
    Object pubIds = dataParamet.get(OpenConsts.MAP_PUBIDS);
    if (pubIds != null && StringUtils.isNotBlank(pubIds.toString())) {
      return null;
    }

    return WebServiceUtils.setResut2("-3", "Missing Parameter");
  }

  @Override
  public String doHandlerIsisData(Map<String, Object> dataParamet) throws Exception {
    String pubListXml = "﻿<publications></publications>";
    Long psnId = NumberUtils.toLong(dataParamet.get(OpenConsts.MAP_PSNID).toString());
    String[] pubIdStrs = StringUtils.split(dataParamet.get(OpenConsts.MAP_PUBIDS).toString(), ",");
    StringBuffer sBuffer = new StringBuffer();
    sBuffer.append("<publications>");
    for (String pubIdStr : pubIdStrs) {
      if (NumberUtils.isNumber(pubIdStr)) {
        Long pubId = NumberUtils.createLong(pubIdStr);
        try {
          NsfcwsPublication publication = nsfcwsPubService.getNsfcwsPublicationByPubId(pubId);
          if (publication != null && publication.getPsnId().equals(psnId) && publication.getStatus() == 0) {
            String pubXml = nsfcwsPubService.getPubXmlByPubId(pubId);
            pubXml =
                nsfcPubXmlBuildFactory.createPubXmlServiceBean(publication.getPubTypeId()).buildPubXml(pubId, pubXml);
            sBuffer.append(pubXml);
          }
        } catch (Exception e) {
          logger.error("获取成果xml出现异常pubId=" + pubId, e);
        }
      }
    }
    sBuffer.append("</publications>");
    pubListXml = sBuffer.toString();


    return pubListXml;

  }



}
