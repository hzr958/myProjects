package com.smate.center.batch.service.pub.pubtopubsimple;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smate.center.batch.dao.pdwh.pub.cnki.CnkiPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.ei.EiPublicationDao;
import com.smate.center.batch.dao.pdwh.pub.isi.IsiPublicationDao;
import com.smate.center.batch.dao.tmp.pdwh.PubFundingInfoDao;
import com.smate.center.batch.dao.tmp.pdwh.TmpPublicationForSnsGroupDao;
import com.smate.center.batch.model.pdwh.pub.cnki.CnkiPubExtend;
import com.smate.center.batch.model.pdwh.pub.ei.EiPubExtend;
import com.smate.center.batch.model.pdwh.pub.isi.IsiPubExtend;
import com.smate.center.batch.model.tmp.pdwh.PubFundingInfo;
import com.smate.center.batch.model.tmp.pdwh.TmpPublicationForSnsGroup;
import com.smate.center.batch.oldXml.pub.ImportPubXmlDocument;
import com.smate.center.batch.util.pub.PubXmlDbUtils;
import com.smate.core.base.utils.data.XmlUtil;


@Service("pdwhPubForGroupService")
@Transactional(rollbackFor = Exception.class)
public class PdwhPubForGroupServiceImpl implements PdwhPubForGroupService {

  @Autowired
  private TmpPublicationForSnsGroupDao tmpPublicationForSnsGroupDao;
  @Autowired
  private IsiPublicationDao isiPublicationDao;
  @Autowired
  private CnkiPublicationDao cnkiPublicationDao;
  @Autowired
  private EiPublicationDao eiPublicationDao;
  @Autowired
  private PubFundingInfoDao pubFundingInfoDao;

  @Override
  public List<TmpPublicationForSnsGroup> getPdwhPubInfo(Integer size, Long startPubId, Long endPubId) {
    List<TmpPublicationForSnsGroup> toDoList =
        this.tmpPublicationForSnsGroupDao.getToHandleList(size, startPubId, endPubId);
    return toDoList;
  }

  @Override
  public void fetchPubFundingInfo(TmpPublicationForSnsGroup tmpPublicationForSnsGroup) throws Exception {
    Long pubId = tmpPublicationForSnsGroup.getPubId(); // 对应库中的pubid
    Long pubAllId = tmpPublicationForSnsGroup.getPubAllId();
    Integer dbId = tmpPublicationForSnsGroup.getDbId();
    String pubXmlString = getPubXmlString(pubId, dbId);

    if (StringUtils.isEmpty(pubXmlString)) {
      return;
    }
    ImportPubXmlDocument document = new ImportPubXmlDocument(pubXmlString);
    String fundInfoString = document.getFundInfo();

    if (StringUtils.isEmpty(fundInfoString)) {
      return;
    }

    fundInfoString = StringUtils.trimToEmpty(fundInfoString.toLowerCase());
    fundInfoString = XmlUtil.changeSBCChar(fundInfoString);
    String[] fundInfos = StringUtils.split(fundInfoString, ";");
    // 如果已经有重复的，先删除
    if (pubFundingInfoDao.getByPubAllId(pubAllId)) {
      this.pubFundingInfoDao.deleteByPubAllId(pubAllId);
    }

    for (String str : fundInfos) {
      if (StringUtils.isNotEmpty(str)) {
        PubFundingInfo pub = new PubFundingInfo();
        pub.setPubId(pubId);
        pub.setDbId(dbId);
        pub.setFundingInfo(StringUtils.trimToEmpty(str));
        this.pubFundingInfoDao.save(pub);
      }
    }
  }

  private String getPubXmlString(Long pubId, Integer dbId) {
    String pubXmlString = null;

    if (PubXmlDbUtils.isIsiDb(dbId)) {
      IsiPubExtend pubXml = isiPublicationDao.getIsiPubExtend(pubId);
      if (pubXml != null) {
        pubXmlString = pubXml.getXmlData();
      }
    } else if (PubXmlDbUtils.isCnkiDb(dbId)) {
      CnkiPubExtend pubXml = cnkiPublicationDao.getCnkiPubExtend(pubId);
      if (pubXml != null) {
        pubXmlString = pubXml.getXmlData();
      }
    } else if (PubXmlDbUtils.isEiDb(dbId)) {
      EiPubExtend pubXml = eiPublicationDao.getEiPubExtend(pubId);
      if (pubXml != null) {
        pubXmlString = pubXml.getXmlData();
      }
    }
    return pubXmlString;
  }

  @Override
  public void saveStatus(TmpPublicationForSnsGroup tmpPublicationForSnsGroup, Integer status) {
    tmpPublicationForSnsGroup.setStatus(status);
    this.tmpPublicationForSnsGroupDao.save(tmpPublicationForSnsGroup);
  };
}
