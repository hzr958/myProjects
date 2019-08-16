package com.smate.center.task.v8pub.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import com.smate.center.task.dao.pdwh.quartz.PdwhPubXmlDao;
import com.smate.center.task.dao.sns.quartz.PubSimpleDao;
import com.smate.center.task.dao.sns.quartz.ScmPubXmlDao;
import com.smate.center.task.exception.ServiceException;
import com.smate.center.task.single.dao.solr.PdwhPublicationDao;
import com.smate.center.task.v8pub.dao.pdwh.PdwhDataTaskDAO;
import com.smate.center.task.v8pub.dao.pdwh.PdwhPubDuplicateDAO;
import com.smate.center.task.v8pub.dao.sns.PubDataTaskDAO;
import com.smate.center.task.v8pub.dao.sns.PubDuplicateDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDAO;
import com.smate.center.task.v8pub.dao.sns.PubPdwhDetailDAO;
import com.smate.center.task.v8pub.dao.sns.PubSnsDetailDAO;
import com.smate.center.task.v8pub.pdwh.po.PdwhDataTaskPO;
import com.smate.center.task.v8pub.pdwh.po.PdwhPubDuplicatePO;
import com.smate.center.task.v8pub.pdwh.po.PubPdwhPO;
import com.smate.center.task.v8pub.sns.po.PubDataTaskPO;
import com.smate.center.task.v8pub.sns.po.PubDuplicatePO;
import com.smate.core.base.utils.date.DateStringSplitFormateUtil;
import com.smate.core.base.utils.number.NumberUtils;
import com.smate.core.base.utils.pubHash.PubHashUtils;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;
import com.smate.web.v8pub.dom.sns.PubSnsDetailDOM;

@Service("pubHandleService")
@Transactional(rollbackFor = Exception.class)
public class PubHandleServiceImpl implements PubHandleService {
  protected Logger logger = LoggerFactory.getLogger(getClass());
  @Autowired
  private PubSnsDataInitService pubSnsDataInitService;
  @Autowired
  private PubPdwhDataInitService pubPdwhDataInitService;
  @Autowired
  private ScmPubXmlDao scmPubXmlDao;
  @Autowired
  private PdwhPubXmlDao pdwhPubXmlDao;
  @Autowired
  private PubSimpleDao pubSimpleDao;
  @Autowired
  private PubDataTaskDAO pubDataTaskDAO;
  @Autowired
  private PdwhDataTaskDAO pdwhDataTaskDAO;
  @Autowired
  private PdwhPublicationDao pdwhPublicationDao;
  @Autowired
  private PdwhPubDuplicateDAO pdwhPubDuplicateDAO;
  @Autowired
  private PubPdwhDetailDAO pubPdwhDetailDAO;
  @Autowired
  private PubSnsDetailDAO pubSnsDetailDAO;
  @Autowired
  private PubDuplicateDAO pubDuplicateDAO;
  @Autowired
  private PubPdwhDAO pubPdwhDAO;

  @Override
  public List<PubDataTaskPO> findPubNeedDeal(Long startId, Long endId, Integer size) {
    return pubDataTaskDAO.findPubId(startId, endId, size);
  }

  @Override
  public List<PdwhDataTaskPO> findPdwhNeedDeal(Long startId, Long endId, Integer size) {
    return pdwhDataTaskDAO.findPdwhPubId(startId, endId, size);
  }

  @Override
  public void handlePub(PubDataTaskPO pubData) throws ServiceException {
    Long pubId = pubData.getPubId();
    try {
      PubSnsDetailDOM pubDetail = pubSnsDetailDAO.findByPubId(pubId);
      if (pubDetail == null) {
        pubData.setStatus(-1);
        pubData.setError("mongodb中没有数据");
        pubDataTaskDAO.update(pubData);
        return;
      }
      PubDuplicatePO pubDuplicatePO = pubDuplicateDAO.get(pubId);
      if (pubDuplicatePO == null) {
        pubData.setStatus(-1);
        pubData.setError("查重表没有记录");
        pubDataTaskDAO.update(pubData);
        return;
      }
      String title = pubDetail.getTitle();
      title = HtmlUtils.htmlUnescape(title);
      title = HtmlUtils.htmlEscape(title);
      if (StringUtils.isNotBlank(title)) {
        // pubType 和 title是必须有的，因此无论如何都需要计算hash值
        String hashTP = PubHashUtils.getTpHash(title, String.valueOf(pubDetail.getPubType()));
        pubDuplicatePO.setHashTP(hashTP);
        Map<String, String> publishDateMap = DateStringSplitFormateUtil.split(pubDetail.getPublishDate());
        if (publishDateMap.get("year") != null) {
          Integer publishYear = Integer.valueOf(publishDateMap.get("year"));
          if (publishYear != null && publishYear != 0) {
            // pubYear 可能为null，为null是不计算三者合并的hash值
            String hashTPP = PubHashUtils.getTitleInfoHash(title, pubDetail.getPubType(), publishYear);
            pubDuplicatePO.setHashTPP(hashTPP);
          }
        }

        Long hashT = PubHashUtils.cleanTitleHash(title);
        String hashTitle = NumberUtils.isNullOrZero(hashT) ? "" : hashT + "";
        pubDuplicatePO.setHashTitle(hashTitle);
      }
      if (StringUtils.isNotBlank(pubDetail.getDoi())) {
        Long hashCleanDoi = PubHashUtils.getDoiHashRemotePun(pubDetail.getDoi());
        pubDuplicatePO.setHashCleanDoi(hashCleanDoi + "");
        if (StringUtils.isNotBlank(pubDuplicatePO.getHashCnkiDoi())) {
          pubDuplicatePO.setHashCleanCnkiDoi(hashCleanDoi + "");
        }
      }
      pubDuplicateDAO.saveOrUpdate(pubDuplicatePO);
      // Long hashDoi = PubHashUtils.cleanDoiHash(pubDetail.getDoi());
      // String strHashDoi = NumberUtils.isNullOrZero(hashDoi) ? "" : hashDoi + "";
      //
      // Long hashCleanDoi = PubHashUtils.getDoiHashRemotePun(pubDetail.getDoi());
      // String strHashCleanDoi = NumberUtils.isNullOrZero(hashCleanDoi) ? "" : hashCleanDoi + "";
      // if (StringUtils.isNotBlank(pubDuplicatePO.getHashDoi())) {
      // pubDuplicatePO.setHashCleanDoi(strHashCleanDoi);
      // pubDuplicatePO.setHashDoi(strHashDoi);
      // }
      // if (StringUtils.isNotBlank(pubDuplicatePO.getHashCnkiDoi())) {
      // pubDuplicatePO.setHashCleanCnkiDoi(strHashCleanDoi);
      // pubDuplicatePO.setHashCnkiDoi(strHashDoi);
      // }
      // pubData.setStatus(1);
      // pubDataTaskDAO.saveOrUpdate(pubData);
      // ScmPubXml scmPubXml = scmPubXmlDao.get(pubId);
      // PubSimple pubSimple = pubSimpleDao.get(pubId);
      // if (scmPubXml == null || pubSimple == null) {
      // pubData.setStatus(-1);
      // pubData.setError("xml或者pubSimple表没有数据");
      // pubDataTaskDAO.saveOrUpdate(pubData);
      // return;
      // }
      // String jsonData = SnsPubXMLToJsonStrUtils.dealWithXML(scmPubXml.getPubXml());
      // // jsonData数据的处理
      // jsonData = XSSUtils.xssReplace(jsonData);
      // Map<String, Object> dataMap = JacksonUtils.json2HashMap(jsonData);
      // // 构造数据
      // pubSnsDataInitService.constructData(dataMap);
      // Long fulltextId = pubSnsDataInitService.initFulltext(pubId, dataMap);
      // dataMap.put("fulltextId", fulltextId);
      // pubSnsDataInitService.initPubSns(pubSimple, dataMap);
      // PubSnsDetailDOM pubDetail = pubSnsDataInitService.initPubDetail(pubId, dataMap);
      // pubSnsDataInitService.initAccessory(pubId, dataMap);
      // pubSnsDataInitService.initDuplicate(pubId, pubDetail);
      // pubSnsDataInitService.initCitations(pubId, dataMap);
      // pubSnsDataInitService.initKeywords(pubId, dataMap);
      // pubSnsDataInitService.initMembers(pubId, dataMap);
      // pubSnsDataInitService.initSitation(pubId, dataMap);
      // pubSnsDataInitService.initPubStatistics(pubId, dataMap);
      // pubSnsDataInitService.initPsnPub(pubSimple);
      // pubSnsDataInitService.initGrpPubs(pubSimple);
      // pubSnsDataInitService.initSNSPubDetail(pubId, pubDetail);

    } catch (Exception e) {
      logger.error("处理个人库成果出错！pubId={}", pubId, e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void handlePdwh(PdwhDataTaskPO pubData) throws ServiceException {
    try {
      PubPdwhDetailDOM pdwhDetail = pubPdwhDetailDAO.findByPubId(pubData.getPubId());
      if (pdwhDetail == null) {
        pubData.setStatus(-1);
        pubData.setError("mongodb中没有数据");
        pdwhDataTaskDAO.update(pubData);
        return;
      }
      PubPdwhPO pubPdwhPO = pubPdwhDAO.get(pubData.getPubId());
      if (pubPdwhPO == null) {
        pubData.setStatus(-1);
        pubData.setError("成果主表没有记录");
        pdwhDataTaskDAO.update(pubData);
        return;
      }
      PdwhPubDuplicatePO pdwhDup = pdwhPubDuplicateDAO.get(pubData.getPubId());
      if (pdwhDup == null) {
        pubData.setStatus(-1);
        pubData.setError("查重表没有记录");
        pdwhDataTaskDAO.update(pubData);
        return;
      }
      String title = pdwhDetail.getTitle();
      if (StringUtils.isNotBlank(title)) {
        String hashTP = PubHashUtils.getTpHash(title, pdwhDetail.getPubType() + "");
        pdwhDup.setHashTP(hashTP);
        String hashTPP = "";
        if (pubPdwhPO.getPublishYear() != null && pubPdwhPO.getPublishYear() != 0) {
          hashTPP = PubHashUtils.getTitleInfoHash(title, pdwhDetail.getPubType(), pubPdwhPO.getPublishYear());
        }
        pdwhDup.setHashTPP(hashTPP);

        String hashTitle = PubHashUtils.cleanTitleHash(title) + "";
        pdwhDup.setHashTitle(hashTitle);
      }
      pdwhPubDuplicateDAO.saveOrUpdate(pdwhDup);
      // PubPdwhDetailDOM pubPdwhDetailDOM = pubPdwhDataInitService.initPubDetail(pdwhPubId, null);
      // pubPdwhDataInitService.initDuplicate(pubPdwhDetailDOM);
      // PdwhPubXml pdwhPubXml = pdwhPubXmlDao.get(pubData.getPubId());
      // PdwhPublication pdwhPublication = pdwhPublicationDao.get(pdwhPubId);
      // if (pdwhPubXml == null) {
      // pubData.setStatus(-1);
      // pubData.setError("pdwh库成果数据处理异常");
      // pdwhDataTaskDAO.update(pubData);
      // return;
      // }
      // String jsonData = PdwhPubXMLToJsonStrUtils.dealWithXML(pdwhPubXml.getXml());
      // // jsonData数据的处理
      // jsonData = XSSUtils.xssReplace(jsonData);
      // Map<String, Object> dataMap = JacksonUtils.json2HashMap(jsonData);
      // 处理初始化任务
      // pubPdwhDataInitService.constructData(dataMap);
      // 字段briefDesc构造完存放在dataMap中
      // Long fulltextId = pubPdwhDataInitService.initFulltext(pdwhPubId, dataMap);
      // dataMap.put("fulltextId", fulltextId);
      // pubPdwhDataInitService.initPubPdwh(dataMap, pdwhPublication);
      // pubPdwhDataInitService.initCitations(pdwhPubId, dataMap);
      // pubPdwhDataInitService.initKeywords(pdwhPubId, dataMap);
      // pubPdwhDataInitService.initSitation(pdwhPubId, dataMap);
      // pubPdwhDataInitService.initMembers(pdwhPubId, dataMap);
      // pubPdwhDataInitService.initPubStatistics(pdwhPubId, dataMap);
      // pubPdwhDataInitService.initPdwhPubDetail(pdwhPubId, pubPdwhDetailDOM);
    } catch (Exception e) {
      logger.error("处理基准库数据初始化任务出错！", e);
      throw new ServiceException(e);
    }
  }

  @Override
  public void handlePdwhMember(PdwhDataTaskPO pubData) {
    try {
      pubPdwhDataInitService.dealWithMember(pubData.getPubId());
    } catch (Exception e) {
      logger.error("处理基准库成果作者表数据出错！", e);
      throw new ServiceException(e);
    }

  }
}
