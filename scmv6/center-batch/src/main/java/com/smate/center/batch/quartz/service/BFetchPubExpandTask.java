package com.smate.center.batch.quartz.service;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.smate.center.batch.connector.model.job.BatchQuartz;
import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.DbCacheBfetch;
import com.smate.center.batch.oldXml.pub.ImportPubXmlDocument;
import com.smate.center.batch.service.pdwh.isipub.IsiPublicationService;
import com.smate.center.batch.service.pdwh.pub.CniprPublicationService;
import com.smate.center.batch.service.pdwh.pub.CnkiPatPublicationService;
import com.smate.center.batch.service.pdwh.pub.CnkiPublicationService;
import com.smate.center.batch.service.pdwh.pub.DbCacheBfetchService;
import com.smate.center.batch.service.pdwh.pub.DbCacheService;
import com.smate.center.batch.service.pdwh.pub.EiPublicationService;
import com.smate.center.batch.service.pdwh.pub.PubMedPublicationService;
import com.smate.center.batch.service.pdwh.pub.SpsPublicationService;
import com.smate.center.batch.service.pdwh.pub.WfPublicationService;
import com.smate.center.batch.util.pub.ImportPubXmlUtils;
import com.smate.center.batch.util.pub.PubXmlDbUtils;
import com.smate.core.base.utils.exception.BatchTaskException;


public class BFetchPubExpandTask implements BatchQuartzTaskService {

  private static final int BATCH_SIZE = 5;

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  private DbCacheBfetchService dbCacheBfetchService;

  @Autowired
  private IsiPublicationService isiPublicationService;

  @Autowired
  private SpsPublicationService spsPublicationService;

  @Autowired
  private CnkiPublicationService cnkiPublicationService;

  @Autowired
  private EiPublicationService eiPublicationService;

  @Autowired
  private WfPublicationService wfPublicationService;

  @Autowired
  private CniprPublicationService cniprPublicationService;

  @Autowired
  private CnkiPatPublicationService cnkiPatPublicationService;

  @Autowired
  private PubMedPublicationService pubMedPatPublicationService;

  @Autowired
  private DbCacheService dbCacheService;

  @Override
  public void taskExecute(BatchQuartz task) throws BatchTaskException {
    try {
      Long startId = 0L;
      List<DbCacheBfetch> list = null;
      try {
        list = this.dbCacheBfetchService.loadExpandBatch(BATCH_SIZE);
      } catch (ServiceException e1) {
        this.logger.error("批量查询临时数据错误", e1);
      }

      if (CollectionUtils.isEmpty(list)) {
        return;
      }

      for (DbCacheBfetch pubCache : list) {
        startId = pubCache.getXmlId();
        try {
          String xmlData = pubCache.getXmlData();
          List<String> pubDatas = ImportPubXmlUtils.splitOnlineXml(xmlData);

          if (CollectionUtils.isEmpty(pubDatas)) {
            dbCacheBfetchService.saveError(startId, "no pub data");
            continue;
          }

          String seqNos = "";
          Integer status = 1;
          String seqNo = "";
          int codeNullNum = 0;

          innerFor: for (String pubData : pubDatas) {
            try {
              pubData = pubData.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", "");
              seqNo = ImportPubXmlUtils.getSeqNo(pubData);
              ImportPubXmlDocument doc = new ImportPubXmlDocument(pubData);

              if (!this.validateXml(doc)) {
                continue;
              }

              String dbCode = doc.getSourceDbCode();
              seqNo = doc.getSeqNo();
              String pubXml = pubData;

              // isi文献
              if (PubXmlDbUtils.isIsiDb(dbCode)) {
                isiPublicationService.addDbCachePub(pubCache.getInsId(), pubXml);
                // scopus文献
              } else if (PubXmlDbUtils.isScopusDb(dbCode)) {
                spsPublicationService.addDbCachePub(pubCache.getInsId(), pubXml);
                // cnki文献
              } else if (PubXmlDbUtils.isCnkiDb(dbCode)) {
                cnkiPublicationService.addDbCachePub(pubCache.getInsId(), pubXml);
                // ei
              } else if (PubXmlDbUtils.isEiDb(dbCode)) {
                eiPublicationService.addDbCachePub(pubCache.getInsId(), pubXml);
                // wanfang
              } else if (PubXmlDbUtils.isWanFangDb(dbCode)) {
                wfPublicationService.addDbCachePub(pubCache.getInsId(), pubXml);
                // CNIPRDb，已经停止了cnipr的导入，2016-02-29和lzh确认
              } else if (PubXmlDbUtils.isCNIPRDb(dbCode)) {
                cniprPublicationService.addDbCachePub(pubCache.getInsId(), pubXml);
                // cnkipat
              } else if (PubXmlDbUtils.isCnkipatDb(dbCode)) {
                cnkiPatPublicationService.addDbCachePub(pubCache.getInsId(), pubXml);
                // pubmed
              } else if (PubXmlDbUtils.isPubMedDb(dbCode)) {
                pubMedPatPublicationService.addDbCachePub(pubCache.getInsId(), pubXml);
              } else {
                codeNullNum++;

                if (codeNullNum >= 10) {
                  status = 9;
                  seqNos = "source_db_code not found number is " + codeNullNum;
                }
              }

            } catch (Exception e) {
              logger.error("批量抓取成果拆分到单位错误", e);
              status = 9;
              seqNos += "," + seqNo;
              dbCacheService.saveDbCacheError(e, startId);
            }
          }

          if (status == 9) {
            dbCacheBfetchService.saveError(startId, "error_xml:" + seqNos);
          } else {
            dbCacheBfetchService.saveSuccess(startId);
          }
          logger.debug("============== task 拆分xml 正常展开 IsiPubCache：{}", pubCache.getXmlId());
        } catch (Exception e) {
          this.logger.error("批量抓取展开成果XML错误", e);
          dbCacheBfetchService.saveError(startId, " ");
        }
      }
    } catch (Exception e) {
      this.logger.error("成果批量抓取数据拆分到基准分库错误", e);
    }
  }

  private boolean validateXml(ImportPubXmlDocument doc) {
    String dbCode = doc.getSourceDbCode();
    String enTitle = doc.getEnTitle();
    String zhTitle = doc.getZhTitle();

    if (StringUtils.isBlank(dbCode)) {
      return false;
    }

    if (StringUtils.isBlank(enTitle) && StringUtils.isBlank(zhTitle)) {
      return false;
    }

    return true;
  }
}
