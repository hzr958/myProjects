package com.smate.center.batch.service.pdwh.pubimport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.smate.center.batch.exception.pub.ServiceException;
import com.smate.center.batch.model.pdwh.pub.PubPdwhPO;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubImportContext;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubSourceDb;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPubXml;
import com.smate.center.batch.model.pdwh.pubimport.PdwhPublication;
import com.smate.center.batch.model.pdwh.pubimport.TmpPdwhPubAddr;
import com.smate.center.batch.model.pdwh.pubimport.TmpPublicationAll;
import com.smate.center.batch.oldXml.pub.ImportPubXmlDocument;
import com.smate.web.v8pub.dom.pdwh.PubPdwhDetailDOM;

public interface PdwhPublicationService {

  public Map<String, Object> praseXmlData(String xmlData);

  public PdwhPublication constructPdwhPub(Map<String, Object> xmlMap);

  public void saveKeywords(PdwhPublication pdwhPub, PdwhPubImportContext context) throws ServiceException;

  public PdwhPubSourceDb saveCitedInfo(PdwhPublication pdwhPub);

  public void savePubSourceDb(PdwhPubSourceDb pubSourceDb);

  public void updateCitedInfo(Integer DbId, PdwhPublication pdwhPub, PdwhPubXml pdwhPubXml);

  public void savePubAddress(PdwhPublication pdwhPublication, PdwhPubImportContext context) throws ServiceException;

  public void updateXml(PdwhPublication newPub, PdwhPubImportContext context) throws Exception;

  public void pubAddrMatch(PdwhPublication pdwhPub, PdwhPubImportContext context) throws ServiceException;

  public void updateAuthorInfo(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception;

  public void savePdwhPublication(PdwhPublication pdwhPub) throws Exception;

  public void updatePdwhPubInfo(PdwhPublication pdwhPub, PdwhPubImportContext context) throws Exception;

  public void updatePdwhDupPubInfo(PdwhPublication newPdwhPub, PdwhPubImportContext context) throws Exception;

  public void handleInfo(PdwhPublication pdwhPub);

  void saveToBatchJobs(Long pubId, Long insId, Integer dbid) throws ServiceException;


  public List<TmpPublicationAll> getTmpPublicationAllList(Integer size);

  void handlePdwhCiteTimes(PdwhPublication pdwhPub);

  public PdwhPublication getPdwhPubById(Long pubId);

  public Integer extractInstitutionAddr(TmpPdwhPubAddr pubAddr) throws Exception;

  public List<TmpPdwhPubAddr> getToHandleList(Long minPubId, Long maxPubId);

  public void saveTmpPdwhPubAddr(TmpPdwhPubAddr tmpPdwhPubAddr);

  public PubPdwhDetailDOM getFullPdwhPubInfoById(Long pubId);

  public ImportPubXmlDocument getPubXmlDocById(Long pubId);

  public Long getJnlIdByJournalNameOrIssn(String original, String issn);

  public List<BigDecimal> getPrjIdList(Integer size);

  public void updatePrjStatus(Long prjId, Integer status);

  public Integer extractPrjKws(Long prjId) throws Exception;

  public PubPdwhPO getNewPdwhPubById(Long pubId);

  public List<Map<String, Object>> getPubMemberInfoList(Long pubId);

  public void saveTmpTaskInfoRecord(Long pubId);
}
