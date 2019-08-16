package com.smate.center.open.service.pubinfo;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.open.service.common.IrisCommonService;
import com.smate.center.open.service.publication.IrisPubXmlBuildFactory;
import com.smate.center.open.service.publication.PublicationService;
import com.smate.center.open.service.resume.ResumeService;
import com.smate.center.open.utils.IrisSnsConstants;
import com.smate.core.base.pub.model.Publication;
import com.smate.core.base.utils.constant.ServiceConstants;
import com.smate.core.base.utils.model.Page;
import com.smate.core.base.utils.string.ServiceUtil;

@Service("pubInfoService")
@Transactional(rollbackFor = Exception.class)
public class PubInfoServiceImpl implements PubInfoService {

  protected final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private PublicationService publicationService;
  @Autowired
  private IrisCommonService irisCommonService;
  @Autowired
  private ResumeService resumeService;
  @Autowired
  private IrisPubXmlBuildFactory irisPubXmlBuildFactory;

  @Override
  public int searchPubsCountByPsn(String psnID, String keywords, String excludedPubIDS, String psnGuidID,
      String pubTypes) {
    int pubCount = 0;
    try {
      Long psnId = NumberUtils.toLong(psnID);
      if (StringUtils.isNotBlank(excludedPubIDS) && !excludedPubIDS.matches(ServiceConstants.IDPATTERN)) {
        excludedPubIDS = null;
      }
      List<Integer> permissions = this.irisCommonService.getQueryPubPermission(psnGuidID, psnId);
      pubCount =
          publicationService.getPsnPublicPubCount(psnId, keywords, excludedPubIDS, permissions, pubTypes).intValue();
    } catch (Exception e) {
      logger.error(String.format("IRIS业务系统接口-获取SNS用户psnId=%s公开成果数出现异常：", psnID), e);
    }
    return pubCount;
  }

  @Override
  public String searchPubsListByPsn(String psnID, String keywords, String excludedPubIDS, String psnGuidID,
      String sortType, Integer pageSize, Integer pageNum, String pubTypes, Integer xmlType) {
    String pubListXml = "﻿<publications></publications>";
    try {
      Long psnId = NumberUtils.toLong(psnID);
      Page<Publication> page = new Page<Publication>(pageSize);
      page.setIgnoreMin(true);
      page.setPageSize(pageSize);
      page.setPageNo(pageNum);
      if (StringUtils.isNotBlank(excludedPubIDS) && !excludedPubIDS.matches(ServiceConstants.IDPATTERN)) {
        excludedPubIDS = null;
      }
      List<Integer> permissions = this.irisCommonService.getQueryPubPermission(psnGuidID, psnId);
      page = publicationService.getPsnPublicPubByPage(psnId, keywords, excludedPubIDS, permissions, sortType, page,
          pubTypes);
      List<Publication> pubList = page.getResult();
      if (CollectionUtils.isNotEmpty(pubList)) {
        if (xmlType != null && xmlType.intValue() == 2) {
          // 返回详情格式xml
          String pubIds = "";
          for (Publication publication : pubList) {
            pubIds += "," + publication.getPubId();
          }
          pubIds = pubIds.length() > 0 ? pubIds.substring(1) : pubIds;
          pubListXml = this.searchPubDetail(pubIds, psnID);
        } else {
          // 返回列表格式xml
          pubListXml = irisCommonService.buildPubListXmlStr(pubList);
        }
      }
    } catch (Exception e) {
      logger.error(String.format("IRIS业务系统接口-通过人员psnId=%s查询成果列表出现异常：", psnID), e);
    }
    return pubListXml;
  }

  @Override
  public String searchPubDetail(String pubIDS, String psnID) {
    String pubListXml = "<publications></publications>";
    try {
      Long psnId = 0L;
      if (NumberUtils.isNumber(psnID)) {
        psnId = NumberUtils.toLong(psnID);
      } else {
        psnId = NumberUtils.toLong(ServiceUtil.decodeFromDes3(psnID));
      }
      StringBuffer sBuffer = new StringBuffer();
      sBuffer.append("<publications>");
      String[] pubIdStrs = StringUtils.split(pubIDS, ",");
      for (String pubIdStr : pubIdStrs) {
        if (NumberUtils.isNumber(pubIdStr)) {
          Long pubId = NumberUtils.createLong(pubIdStr);
          String authority = resumeService.getPubAuthority(pubId);
          if ("0".equals(authority)) {
            Publication pub = this.publicationService.getPub(pubId);
            if (pub != null && pub.getOwnerPsnId().equals(psnId)) {
              try {
                String pubXml = publicationService.getPubXmlById(pubId);
                pubXml = irisPubXmlBuildFactory.createPubXmlServiceBean(pub.getPubType()).buildPubXml(pub, pubXml);
                sBuffer.append(pubXml);
              } catch (Exception e) {
                logger.error("获取成果xml出现异常pubId=" + pubId, e);
              }
            }
          }
        }
      }
      sBuffer.append("</publications>");
      pubListXml = sBuffer.toString();
    } catch (Exception e) {
      logger.error(String.format("IRIS业务系统接口-导出用户psnId=%s的成果pubIds=%s出现异常：", psnID, pubIDS), e);
    }
    return pubListXml;
  }

  @Override
  public String searchPubsCountByConnectedPsn(String psnGuidID, String psnID, String keywords, String excludedPubIDS,
      String pubTypes) {
    String pubCount = IrisSnsConstants.NOT_CONNECTED;
    try {
      Long psnId = NumberUtils.toLong(psnID);
      int isConnected = irisCommonService.checkPsnConnected(psnGuidID, psnId);
      if (isConnected == 1) {
        pubCount =
            ObjectUtils.toString(publicationService.getPsnPubCount(psnId, keywords, null, excludedPubIDS, pubTypes));
      }
    } catch (Exception e) {
      logger.error(String.format("IRIS业务系统接口-通过guid=%s查询关联人员psnId=%s的成果数出现异常：", psnGuidID, psnID), e);
    }
    return pubCount;
  }

  @Override
  public String searchPubsListByConnectedPsn(String psnGuidID, String psnID, String keywords, String excludedPubIDS,
      String sortType, int pageSize, int pageNum, String pubTypes, Integer xmlType) {
    String pubListXml = "<publications></publications>";
    try {
      Long psnId = NumberUtils.toLong(psnID);
      int isConnected = irisCommonService.checkPsnConnected(psnGuidID, psnId);
      if (isConnected == 1) {
        Page<Publication> page = new Page<Publication>();
        page.setIgnoreMin(true);
        page.setPageSize(pageSize);
        page.setPageNo(pageNum);
        if (StringUtils.isNotBlank(excludedPubIDS) && !excludedPubIDS.matches(ServiceConstants.IDPATTERN)) {
          excludedPubIDS = null;
        }
        page = publicationService.getPsnPubByPage(psnId, keywords, null, excludedPubIDS, sortType, page, pubTypes);
        List<Publication> pubList = page.getResult();
        if (CollectionUtils.isNotEmpty(pubList)) {
          if (xmlType != null && xmlType.intValue() == 2) {
            // 返回详情格式xml
            String pubIds = "";
            for (Publication publication : pubList) {
              pubIds += "," + publication.getPubId();
            }
            pubIds = pubIds.length() > 0 ? pubIds.substring(1) : pubIds;
            pubListXml = this.searchPubDetail(pubIds, psnID);
          } else {
            for (Publication publication : pubList) {
              publication.setOwnerPsnId(psnId);
            }
            // 返回列表格式xml
            pubListXml = irisCommonService.buildPubListXmlStr(pubList);
          }
        }
      } else {
        pubListXml = IrisSnsConstants.NOT_CONNECTED;
      }
    } catch (Exception e) {
      logger.error(String.format("IRIS业务系统接口-通过人员psnId=%s查询成果列表出现异常：", psnID), e);
    }
    return pubListXml;
  }

  @Override
  public String searchPubDetailByConnectedPsn(String pubIDS, String psnGuidID, String psnID) {
    String pubListXml = "﻿<publications></publications>";
    try {
      Long psnId = NumberUtils.toLong(psnID);
      int isConnected = irisCommonService.checkPsnConnected(psnGuidID, psnId);
      if (isConnected == 1) {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append("<publications>");
        String[] pubIdStrs = StringUtils.split(pubIDS, ",");
        for (String pubIdStr : pubIdStrs) {
          if (NumberUtils.isNumber(pubIdStr)) {
            Long pubId = NumberUtils.createLong(pubIdStr);
            Publication pub = this.publicationService.getPub(pubId);
            if (pub != null && pub.getOwnerPsnId().equals(psnId)) {
              try {
                String pubXml = publicationService.getPubXmlById(pubId);
                pubXml = irisPubXmlBuildFactory.createPubXmlServiceBean(pub.getPubType()).buildPubXml(pub, pubXml);
                sBuffer.append(pubXml);
              } catch (Exception e) {
                logger.error("获取成果xml出现异常pubId=" + pubId, e);
              }
            }
          }
        }
        sBuffer.append("</publications>");
        pubListXml = sBuffer.toString();
      } else {
        pubListXml = IrisSnsConstants.NOT_CONNECTED;
      }
    } catch (Exception e) {
      logger.error(String.format("IRIS业务系统接口-导出不关联用户psnId=%s的成果pubIds=%s出现异常：", psnID, pubIDS), e);
    }
    return pubListXml;
  }

}
